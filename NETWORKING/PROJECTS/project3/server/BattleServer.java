/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * BattleServer.java
 * This class is one of the classes that implement the server-side logic of this client-server
 * application. It is responsible for accepting incoming connections, creating ConnectionAgents,
 * and passing the ConnectionAgent off to threads for processing. The class implements the
 * MessageListener interface (i.e., it can “observe” objects that are MessageSources).
 */

package server;
import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * A class that acts as the Server for a game of Battleship. Holds all the player data and game
 * logic. Keeps track of turns and opens new connections to clients.
 */
public class BattleServer implements MessageListener {
    /** the server socket used to accept new connections*/
    private final ServerSocket serverSocket;
    /** the current index of the player whose turn it is*/
    private int current;
    /** the game object that holds the logic for a game of battleship*/
    private Game game;
    /** the CA associated with the player that can currently fire*/
    private ConnectionAgent currentPlayer;
    /** a linked list that is the turn order of players with ships left in play*/
    private ArrayList<ConnectionAgent> turnOrder;
    /** the list of all spectators currently in the game*/
    private ArrayList<ConnectionAgent> spectators;
    /** a confirmation message to be sent back to the client side when a valid username is
     * received*/
    private final String usernameConfirmationMSG = "Valid username received! Press enter to " +
            "continue.";
    /** denial message for the username of the client sent to the server*/
    private final String usernameDenialMSG = "Username already taken. Please choose another> ";
    /** a set of usernames that are taken by other players*/
    private HashSet<String> takenUsernames;
    /** the minimum players to have in a game*/
    private final int MIN_PLAYERS = 2;
    /** the size of the board we are playing on that is passed in*/
    private final int boardSize;

    /**
     * Constructor method
     *
     * @param port Port number that server is on
     * @param boardSize Size of the game board
     * @throws IOException if an error occurs when opening a socket
     */
    public BattleServer(int port, int boardSize) throws IOException {
        //assign all the variables that we need to start accepting clients
        this.game = new Game(boardSize);
        this.boardSize = boardSize;
        this.serverSocket = new ServerSocket(port);
        this.turnOrder = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.takenUsernames = new HashSet<>();
        this.current = 0;
    }

    /**
     * Listens out for messages from clients
     */
    public void listen(){
        while (!serverSocket.isClosed()) {
            //Until game is in progress continue to allow players to connect
            if(!this.game.isInProgress()) {
                try {
                    Socket incomingConnection = serverSocket.accept();
                    ConnectionAgent newAgent = new ConnectionAgent(incomingConnection);
                    spectators.add(newAgent);
                    newAgent.addMessageListener(this);
                } catch (IOException ioe) {
                    System.err.println("Error: connection failed to open to incoming client");
                }
            }
        }
    }

    /**
     * Sends a message to all clients
     *
     * @param message Message to send to clients
     */
    public void broadcast(String message){
        for(ConnectionAgent agent : this.spectators){
            //Send message to players
            agent.sendMessage(message);
        }
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        //breaks down the command into components
        String [] splitCommand = message.split(" ");
        String flag = splitCommand[0].toLowerCase();
        //finds which method to call depending on the command sent
        ConnectionAgent caSource = null;
        if(source instanceof ConnectionAgent) {
            caSource = (ConnectionAgent) source;
        }
        //Parse and execute commands of the game
        switch (flag) {
            case "/battle" -> {  // /battle <username>
                if (!takenUsernames.contains(splitCommand[1])) {
                    takenUsernames.add(splitCommand[1]);
                    caSource.setUsername(splitCommand[1]);
                    caSource.sendMessage(usernameConfirmationMSG);
                    broadcast("\n! ! ! " + caSource.getUsername() + " has entered battle!\n");
                } else {
                    caSource.sendMessage(usernameDenialMSG);
                }
            }
            case "/start" -> {
                //starts the game if we are able to
                ArrayList<ConnectionAgent> order = game.start(spectators);
                if (!order.isEmpty()) {
                    if(this.game.isInProgress()) {
                        this.turnOrder = order;
                        broadcast("\n! ! ! The game is starting ! ! !\n");
                    }
                } else {
                    caSource.sendMessage("There is not enough players to start a game!");
                }
            }
            case "/fire" -> {   // /fire <[0-9]+> <[0-9]+> <username>
                if(splitCommand.length == 4){
                    String valid = game.fire(splitCommand[1], splitCommand[2], splitCommand[3],
                            current, caSource, turnOrder);
                    //checks to see what message came back and what needs to be replied to the CA
                    switch (valid) {
                        case "INVALID TURN" -> caSource.sendMessage("Player turn is not correct!");
                        case "INVALID ROW COLUMN" -> caSource.sendMessage("Invalid row or column " +
                                "number! Try again.");
                        case "INVALID USERNAME" ->
                                caSource.sendMessage(splitCommand[3] + " is not a valid username! Try " +
                                        "again.");
                        case "NOT IN PROGRESS" ->
                                caSource.sendMessage("Game is currently not in progress! You cannot " +
                                        "fire right now.");
                        case "HIT MISS" -> {
                            broadcast("\nShots fired at " + splitCommand[3] + " by " + caSource.getUsername());
                            //If player sinks their own ship
                            if(!game.getBoard(current).hasValidShips()){
                                defeated();
                            }
                            else{
                                updatePlayerTurn();
                            }
                        }
                    }
                } else { //Invalid command/amount of fields entered
                    caSource.sendMessage("\nValid fire command: /fire <[0-9]+> <[0-9]+> " +
                            "<username>");
                }
            }
            case "/display" -> {  // /display <username>
                if (splitCommand[1] != null) {
                    String grid = game.display(splitCommand[1], turnOrder, caSource);
                    switch (grid) {
                        case "INVALID USERNAME" ->
                                caSource.sendMessage("You have entered an invalid username!");
                        case "NOT IN PROGRESS" ->
                                caSource.sendMessage("Game is currently not in progress! You " +
                                        "cannot see the boards right now.");
                        default ->
                            //If there was not an error, grid contains the game grid they asked for
                                caSource.sendMessage(grid);
                    }
                }
                else{
                    caSource.sendMessage("Valid display command: /display <username>");
                }
            }
            case "/surrender" -> surrender(caSource);
            case "/commands" -> printCommands(caSource);
            case "/players" -> showPlayers(caSource);
            default -> caSource.sendMessage("Command not valid, entered \"" + flag + "\"");
        }
        caSource.sendMessage("\nEnter a command to send to the server>");
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {source.removeMessageListener(this);}

    /**
     * Prints all the players currently still in the game
     * */
    public void showPlayers(ConnectionAgent origin) {
        //prints the players that are in the turn order if there is one
        if(!turnOrder.isEmpty()) {
            origin.sendMessage("\nCurrent players:\n");
            for (ConnectionAgent agent : turnOrder) {
                origin.sendMessage(agent.toString());
            }
        }else{
            //otherwise print the people waiting in the lobby with you
            origin.sendMessage("\nLobby:\n");
            for(ConnectionAgent agent : spectators){
                origin.sendMessage(agent.toString());
            }
        }
    }

    /**
     * prints a list of commands the players can use while a game is in session
     * */
    private void printCommands(ConnectionAgent origin){
        origin.sendMessage("/fire <[0-9]+> <[0-9]+> <username>\n" +
                "/display <username>\n" +
                "/players\n" +
                "/surrender\n\n");
    }

    /**
     * Removes a player from the turn order when they are defeated in combat, but they can still
     * spectate the game.
     * */
    private void defeated(){
        //Removes the current player from the game and updates the turn order
        ConnectionAgent defeated = currentPlayer;
        broadcast("\nPlayer: " + defeated.toString() + " was defeated, all ships sunk. " +
                "Advancing turn order.");
        //Remove player's grid from game
        game.removePlayer(defeated, turnOrder);
        //Remove player from game rotation
        turnOrder.remove(defeated);

        //If there is only one player left stop the game and clear the takenUsernames
        if(turnOrder.size() < MIN_PLAYERS){
            game.setInProgress(false);
            takenUsernames.clear();
        }
        //if the turn order is greater than 0 update whos turn it is
        if(turnOrder.size() > 0){
            updatePlayerTurn();
        }
        else{
            //otherwise make a new game session
            this.game = new Game(boardSize);
        }
    }

    /**
     * Removes a player from the turn order when they no longer wish to play and disconnects them
     * from the game.
     *
     * @param origin - the connection agent from which the original message came from
     * */
    private void surrender(ConnectionAgent origin){
        //surrenders the currentPlayer and updates the turn order(if needed)
        if(turnOrder.contains(origin)){
            //Remove player's grid from game
            game.removePlayer(origin, turnOrder);
            //Remove player from game rotation
            turnOrder.remove(origin);
        }
        spectators.remove(origin);
        broadcast("\nPlayer: " + origin.toString() + " has surrendered!");

        //loop for wasting time to get a reply from the client
        for(int i = 0; i < 10000; i++){
            for(int j = 0; j < 10000; j++){
                //do nothing, waiting for client
            }
        }
        //If there is only one player left, reset game and start anew
        if(turnOrder.size() < MIN_PLAYERS){
            game.setInProgress(false);
            takenUsernames.clear();
        }
        origin.close();
        if(turnOrder.size() > 0){
            updatePlayerTurn();
        }
        else{
            this.game = new Game(boardSize);
        }
    }

    /**
     * Updates who the current player is
     * */
    private void updatePlayerTurn(){
        //increment the index
        current++;
        //if the index is greater than or equal to the size of the player list
        if (current >= turnOrder.size()){
            //we go back to the start of the list
            current = 0;
        }
        if(turnOrder.size() > 0){
            //then we update the current player
            this.currentPlayer = turnOrder.get(current);
            broadcast("\nIt is now \"" + currentPlayer.toString() + "'s\" turn");
            /*if the current player does not have valid ships, auto surrender them, which
              will also auto change the player turn*/
            if(!game.getBoard(current).hasValidShips()){
                defeated();
            }
        }
        if(turnOrder.size() == MIN_PLAYERS - 1){
            broadcast("\nGame Over! Winner is: '" + currentPlayer.toString() + "'");
        }
    }

}
