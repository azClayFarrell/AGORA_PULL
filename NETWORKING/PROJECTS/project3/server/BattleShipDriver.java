/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * BattleshipDriver.java
 * This class contains the main() method for the server. It parses command line options,
 * instantiates a BattleServer, and calls its listen() method. This takes two command line
 * arguments, the port number for the server and the size of the board (if the size is left off,
 * default to size 10 x 10). You may assume square arrays.
 */

package server;
import java.io.IOException;

/**
 * Class for the driver to start the server for hosting battleship. Makes a BattleServer object
 * and calls the listen method.
 */
public class BattleShipDriver {
    /** Largest size a board can be */
    private final static int MAX_BOARD_SIZE = 10;

    /**
     * Constructor method
     */
    public BattleShipDriver() {}

    /**
     * Main driver of the server
     * @param args Command line arguments read from the user
     */
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            System.out.println("Usage: java server/BattleShipDriver <portNumber> [boardSize]");
        }
        //Check if port is valid
        int portNum = checkPort(args[0]);

        //Board has a default size of 10x10 unless there's a command line argument entered for size
        int boardSize = MAX_BOARD_SIZE;
        if (args.length == 2) {
            boardSize = checkSize(args[1]);
        }

        if(portNum != -1 && boardSize != -1) { //If port and size is valid
            try {
                BattleServer server = new BattleServer(portNum, boardSize);
                server.listen();
            }catch(IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    }

    /**
     * Checks if the port entered by the user is valid
     * @param port Port entered by the user
     * @return Port number if it is valid, -1 if it is not
     */
    public static int checkPort(String port) {
        try {
            int portNum = Integer.parseInt(port);
            if (portNum > 0 && portNum < 65535) {
                return portNum; //Port is valid
            }
            return -1; //Port is not valid
        } catch (NumberFormatException nfe) {
            System.out.println("Port is not valid!");
            return -1; //Port is not valid
        }
    }

    /**
     * Checks if the board size is valid
     * @param size Board size entered by the user
     * @return Board size if it is valid, returns the default board size if not
     */
    public static int checkSize(String size){
        boolean validSize = false;
        try {
            int boardSize = Integer.parseInt(size);
            //Board size is valid if it is greater than 5 and at most 10
            if(boardSize > 5 || boardSize <= MAX_BOARD_SIZE){
                return boardSize;
            }
            return MAX_BOARD_SIZE; //Entered size not valid
        }catch (NumberFormatException nfe){
            System.out.println("Please enter a valid board size!");
            return MAX_BOARD_SIZE; //Entered size not valid
        }
    }

}