/**
 * @author Clay Farrell and Allison Holt
 * @date 11-19-22
 * handles a game of battleship
 */

package server.milestone;
import java.util.ArrayList;
import java.util.Scanner;

/** Class that contains all the logic for hosting a game of battleship*/
public class MileStoneOneGame {

    /**the size of the board*/
    private int boardSize;
    /**the list of players*/
    private ArrayList<Player> playerList;
    /**the current player*/
    private Player currentPlayer;
    /**the index of the currentPlayer*/
    private int currentIndex;
    /**The minimum number of players to start*/
    private final int MIN_PLAYERS = 2;
    /**The amount of players to dictate a victory condition*/
    private final int LAST_PLAYER = 1;
    /**holds the names of all the players once the game has started*/
    private ArrayList<String> playerNames;

    /**
     * Constructor for the game object
     *
     * @param boardSize - the dimensions of the board
     * */
    public MileStoneOneGame(int boardSize){
        this.boardSize = boardSize;
        this.currentIndex = 0;
        this.playerList = new ArrayList<>();
        this.playerNames = new ArrayList<>();
    }

    /**
     * parses messages to play the game of battleship
     *
     * @param message - the message needing to be parsed to carry out a command
     * */
    public void messageReceiver(String message){
        //breaks down the command into components
        message = message.replaceAll("\s+/", " ");
        String [] splitCommand = message.split(" ");
        String flag = splitCommand[0];
        String command = "";
        for (int i = 1; i < splitCommand.length; i++){
            //puts the remainder of the command back together
            command += splitCommand[i] + " ";
        }
        command = command.strip();
        //finds which method to call depending on the command sent
        switch(flag){
            case "/battle":
                battle(command);
                break;
            case "/start":
                start();
                break;
            case "/fire":
                fire(command);
                break;
            case "/display":
                display(command);
                System.out.print("\nEnter a command> ");
                break;
            case "/surrender":
                surrender();
                break;
            case "/commands":
                printCommands();
                break;
            case "/players":
                System.out.println();
                showPlayers();
                System.out.print("\nEnter a command> ");
                break;
            default:
                System.out.println("Command not valid, entered \"" + flag + "\"");
        }

    }

    /**
     * determines if a game can be started
     *
     * @return boolean for if we can start. true if we can, false otherwise
     * */
    public boolean canStart(){
        if (playerList.size() >= MIN_PLAYERS){
            return true;
        }
        return false;
    }

    /**
     * adds a player to the playerList
     *
     * @param name - the name of the person to be added to the player list
     * */
    public void addPlayer(String name){
        //makes a new player
        Player anotherPlayer = new Player(name, this.boardSize);
        //if the player is not already in the list, add them
        if (!playerList.contains(anotherPlayer)){
            playerList.add(anotherPlayer);
        }
        //if the player does exist print an error
        else{
            System.err.println("Player with name \"" + name + "\" already exists." +
                                "\nCannot have duplicate player");
        }
    }

    /**
     * starts the game, loops until there is one player left
     * */
    public void start(){
        System.out.println("Starting game with " + playerList.size() + " players");
        for (Player player : playerList){
            playerNames.add(player.getUserName());
        }
        //make a scanner for input, get a reference to current player
        Scanner scan = new Scanner(System.in);
        this.currentPlayer = playerList.get(currentIndex);
        System.out.println("Player \"" + currentPlayer.toString() + "\" goes first!");
        System.out.print("Enter a command or enter /commands to see a list of commands> ");
        while(playerList.size() > LAST_PLAYER){
            String command = scan.nextLine();
            if (!command.matches("/start*")){
                messageReceiver(command);
            }
        }
        System.out.println("Game over! Winner: " + currentPlayer.toString() + "!!!!");

    }

    /**
     * battle command, players call this with /battle to be added to the list
     * */
    private void battle(String name){
        addPlayer(name);
    }

    /**
     * fires on a coordinate on the grids
     *
     * @param command - string that contains the coordinates and the username to
     *                  be fired upon
     * */
    private void fire(String command){
        //break the command back up into parts
        String [] parts = command.split(" ");
        String name = "";
        for (int i = 2; i < parts.length; i++){
            name += name + " " + parts[i];
        }
        //strips the last bit of space off the end of the name after putting it back together
        name = name.strip();
        //if the coords does not match a number, print an error
        if (!parts[0].matches("[0-9]+") || !parts[1].matches("[0-9]+")){
            System.err.println("Invalid coordinate");
        }
        else {
            //make the coords integers
            int yCoord = Integer.parseInt(parts[0]);
            int xCoord = Integer.parseInt(parts[1]);
            //if the coords are in bounds
            if (yCoord >= 0 && yCoord < boardSize && xCoord >= 0 && xCoord < boardSize){
                if (playerNames.contains(name)) {
                    //if we fire on ourselves we get reprimanded
                    if (name.equals(currentPlayer.getUserName())){
                        System.err.println("We can't fire on our own troops sir!! " +
                                           "I'll have you court martialled for this!");
                    }
                    else{
                        //get the enemy if they exist
                        int indexPlayer = playerNames.indexOf(name);
                        Player enemy = playerList.get(indexPlayer);
                        //if the coordinate on the enemies has not been shot at
                        if (!enemy.getBoard().coordinateIsGuessed(yCoord, xCoord)){
                            //shoot at the board and update the turn order
                            enemy.getBoard().hitMiss(yCoord, xCoord);
                            updatePlayerTurn();
                        }
                        else{
                            System.err.println("The coordinate: " + yCoord + ", " + xCoord +
                                    " has already been fired upon for player \"" + enemy + "\"." +
                                    "\nTry again.");
                        }
                    }
                }
                else{
                    //say the player does not exist
                    System.err.println("The player \"" + name + "\" does not exist");
                }
            }
            else{
                //say the coordinates were out of bounds
                System.err.println("Coordinates out of bounds. Max = " + (boardSize - 1) +
                                    "; Min = 0" +
                                    "\nSupplied: " + yCoord + ", " + xCoord);
            }
        }
    }

    /**
     * displays the board of the player of the provided name
     *
     * @param name - name of the player whos board is to be displayed
     * */
    private void display(String name){
        //if the name parameter if this players username display everything
        if (name.equals(currentPlayer.getUserName())){
            currentPlayer.getBoard().displayGrid();
        }
        //TODO this may not work
        else if (playerNames.contains(name)){
            //if the player's name exists in the list of usernames then display the hits and misses
            int indexPlayer = playerNames.indexOf(name);
            Player otherPlayer = playerList.get(indexPlayer);
            otherPlayer.getBoard().displayHitMiss();
        }
        else{
            //otherwise the player does not exist so tell the player
            System.err.println("Username not found for a player");
        }
    }

    /**
     * surrenders a person from the game
     * */
    private void surrender(){
        //surrenders the currentPlayer and updates the turn order
        Player surrendered = currentPlayer;
        System.out.println("Player: " + surrendered.toString() + " has surrendered");
        updatePlayerTurn();
        playerList.remove(surrendered);
        playerNames.remove(surrendered.getUserName());
    }

    /**
     * updates who the current player is
     * */
    private void updatePlayerTurn(){
        //increment the index
        currentIndex++;
        //if the index is greater than or equal to the size of the player list
        if (currentIndex >= playerList.size()){
            //we go back to the start of the list
            currentIndex = 0;
        }
        //then we update the current player
        this.currentPlayer = playerList.get(currentIndex);
        System.out.println("Player turn has ended, it is now \"" + currentPlayer.toString() +
                            "'s\" turn");
        System.out.print("Enter a command or enter /commands to see a list of commands> ");
        /*if the current player does not have valid ships, auto surrender them, which
          will also auto change the player turn*/
        if(!currentPlayer.getBoard().hasValidShips()){
            surrender();
        }
    }

    /**
     * prints all the players currently still in the game
     * */
    public void showPlayers(){
        for (Player player: playerList){
            System.out.println(player);
        }
    }

    /**
     * prints a list of commands the players can use while a game is in session
     * */
    private void printCommands(){
        System.out.print("/fire <[0-9]+> <[0-9]+> <username>\n" +
                            "/display <username>\n" +
                            "/players\n" +
                            "/surrender\n\n" +
                            "Enter a command> ");
    }

}
