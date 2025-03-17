/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * Game.java
 * This class contains the logic for the game of BattleShip. It has a Grid for each client
 */

package server;
import common.ConnectionAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {
    /** Size of the current game board */
    private final int boardSize;
    /** List containing all the current game boards/grids */
    List<Grid> gridList;
    List<Grid> defeatedGrids;
    /** If the game is in progress or not */
    private boolean inProgress;
    /** the minimum players needed to start a game of battleship*/
    private final int MIN_PLAYERS = 2;

    /**
     * Constructor method
     *
     * @param boardSize Size of the game board
     */
    public Game(int boardSize){
        this.gridList = new ArrayList<>();
        this.defeatedGrids = new ArrayList<>();
        this.boardSize = boardSize;
        this.inProgress = false;
    }

    /**
     * Gets if the game is in progress or not
     *
     * @return True if the game is in progress, false if not
     */
    public boolean isInProgress() {return inProgress;}

    /**
     * Sets if the game is in progress or not
     *
     * @param inProgress True if the game is in progress or false if not
     */
    public void setInProgress(boolean inProgress) {this.inProgress = inProgress;}

    /**
     * Gets the board of the player at the index in the gridList
     * @param index Position of the board you want
     * @return Board at the given position in the gridList
     */
    public Grid getBoard(int index){return this.gridList.get(index);}

    /**
     * Gets the player's position in the lists with the username entered
     *
     * @param username Username of the player
     * @param turnOrder List of all the current players
     * @return The position in the list of the player with the username entered
     */
    public int getUserPosition(String username, ArrayList<ConnectionAgent> turnOrder){
        //Sees if a connection agent is in the turnOrder with the entered username
        for(int i = 0; i < turnOrder.size(); i++){
            if(Objects.equals(turnOrder.get(i).getUsername(), username)){
                return i;
            }
        }
        //If username is not in the list
        return -1;
    }

    /**
     * Removes the player with the entered username from the game
     * @param player Player to remove
     * @param turnOrder List of players to remove from
     */
    public void removePlayer(ConnectionAgent player, ArrayList<ConnectionAgent> turnOrder){
        int playerPosition = getUserPosition(player.getUsername(), turnOrder);
        //If player is in turnOrder
        if(playerPosition != -1) {
            defeatedGrids.add(this.gridList.remove(playerPosition));
        }
        else System.out.println("Invalid username! Cannot remove a player that doesn't exist!");
    }

    /**
     * The /start command is sent by clients to begin a game of Battleship. Play cannot
     * begin if 2 or more users are not “joined”. This command is ignored if a game is
     * already in progress.
     */
     public ArrayList<ConnectionAgent> start(ArrayList<ConnectionAgent> spectators){
         ArrayList<ConnectionAgent> turnOrder = new ArrayList<>();
         /*if the CA has a name at the time the game has started, they can be added to the turn
           order so that they can play*/
         for(ConnectionAgent agent : spectators){
             if(!agent.getUsername().equals("")){
                 Grid grid = new Grid(this.boardSize);
                 grid.setShips();
                 gridList.add(grid);
                 turnOrder.add(agent);
             }
         }
         //start the game if there were enough players with usernames set
         if(turnOrder.size() >= MIN_PLAYERS){
             if(!this.inProgress) {
                 this.inProgress = true;
             }
         }
         //clear the order and gridList if there were not enough players to start
         else{
             turnOrder.clear();
             gridList.clear();
         }
         return turnOrder;
     }

    /**
     * The /fire command is sent by clients during game play to indicate the user to attack,
     *  and the location within the board they wish to attack. Players attacking an invalid
     * location or sending this command when a game is not in session or when it is not a
     * client’s turn results in the server sending back a private message to the client who
     * sent the command with an informative message.
     *
     * @param rowStr Index of row the player wants to fire at
     * @param colStr - the column the player wants to fire at
     * @param username Username of the player to fire at
     * @param currentTurn Current turn of the game
     * @param turnOrder List of current players
     * @param origin - the ConnectionAgent that originally requested to fire
     */
     public String fire(String  rowStr, String  colStr, String username, int currentTurn,
                        ConnectionAgent origin, ArrayList<ConnectionAgent> turnOrder) {
         try {
             int row = Integer.parseInt(rowStr);
             int col = Integer.parseInt(colStr);
             //Game is in progress
             if (this.inProgress) {
                 int userPosition = getUserPosition(username, turnOrder);
                 //Username is valid (A player can fire on themselves)
                 if (userPosition > -1 && userPosition < turnOrder.size()) {
                     if(currentTurn == turnOrder.indexOf(origin)){
                         Grid board = this.gridList.get(userPosition);
                         boolean validCoord = board.checkValidCoords(row, col);
                         //(Row, Column) is a valid coordinate
                         if (validCoord) {
                             board.hitMiss(row, col);
                             return "HIT MISS";
                         }
                         //(Row, Column) is not a valid coordinate
                         else{
                             return "INVALID ROW COLUMN";
                         }
                     }
                     //It is not the player's turn
                     else{
                         return "INVALID TURN";
                     }
                 }
                 //Username is not valid
                 else{
                     return "INVALID USERNAME";
                 }
             }
             //Game is not in progress
             else {
                 return "NOT IN PROGRESS";
             }
         } //Row and column values are not string representations of ints
         catch (NumberFormatException nfe) {
             return "INVALID ROW COLUMN";
         }
     }

    /**
     * The /display command is sent by clients when they want to examine the current state
     * of a board. This command will cause the server to respond with the game board of the
     * client specified.
     * If the client is the “owner” of the board, then hits, misses, and ships are
     * displayed on the board map. If the client does not own the board, then only
     * hits and missed are displayed.
     * This command is ignored if the game is not in progress, but client’s can issue this
     * command when it is not their turn.
     *
     * @param username Username of the player to display
     * @param turnOrder Turn order of all the players
     * @param origin Player who sent message
     */
     public String display(String username, ArrayList<ConnectionAgent> turnOrder,
                           ConnectionAgent origin){
         //Game is in progress
         if(this.inProgress) {
             int playerPosition = getUserPosition(username, turnOrder);
             //Checks if username is a valid and existing username
             if (playerPosition > -1 && playerPosition < turnOrder.size()) {
                 Grid toDisplay = this.gridList.get(playerPosition);
                 String grid;
                 if (username.equals(origin.getUsername())) {
                     //If player is asking for own grid
                     grid = toDisplay.displayGrid();
                 } else {
                     //If player is asking for other player's grid
                     grid = toDisplay.displayHitMiss();
                 }
                 grid = username + "'s grid: \n" +  grid;
                 return grid;
             }
             //If playerPosition is -1 (username does not exist)
             // or greater than playerList size (which is invalid)
             else return "INVALID USERNAME";
         }
         //Game is not in progress
         else {
             return "NOT IN PROGRESS";
         }
     }

}
