/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 2022
 * BattleDriver.java
 * This class contains the main() method for the client. It parses command line options,
 * instantiates a BattleClient, reads messages from the keyboards, and sends them to the client.
 * The command line arguments are: hostname, port number and user nickname. All of these command
 * line arguments are required.
 */

package client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class BattleDriver {
    /** Highest number a port can be */
    private final static int MAX_PORT = 65535;
    /** Smallest number a port can be */
    private final static int MIN_PORT = 0;

    /**
     * Constructor method
     */
    public BattleDriver(){}

    /**
     * Main driver for the server
     * @param args Command line arguments read from user
     */
    public static void main(String[] args) {
        //Parse command line options
        if (args.length != 3) {
            System.out.println("Usage: java client/BattleDriver <hostname> <portNum> " +
                    "<nickName>");
            System.exit(1);
        }

        //Check if hostname is valid
        InetAddress address = checkHost(args[0]);

        //Check if port is valid
        int portNum = checkPort(args[1]);

        //If hostname and port is valid
       if(address != null && portNum != -1) {
           BattleClient client = new BattleClient(address, portNum, args[2]);
           PrintStreamMessageListener printListener = new PrintStreamMessageListener(System.out);
           client.addMessageListener(printListener);

           //catch if a game was in progress
           try {
               client.connect();
           }catch (IOException ioe){System.exit(1);}

           Scanner scan = new Scanner(System.in);
           //makes sure that the player will have a unique username
           String userInput = "";
           System.out.print("Confirm username> ");
           while(!client.isValidUsername()){
               userInput = scan.nextLine();
               if(!userInput.equals("")){
                   client.send("/battle " + userInput);

                   //loop for wasting time to get a reply from the server
                   for(int i = 0; i < 10000; i++){
                       for(int j = 0; j < 10000; j++){
                           //do nothing, waiting for server
                       }
                   }
               }
               else{
                   System.out.print("> ");
               }
           }

           //Read messages from the keyboard while we don't ask to surrender
           while(!userInput.equalsIgnoreCase("/surrender") && client.agentIsConnected()){
               userInput = scan.nextLine().strip();

               //checks the first part of the userInput to see if it's part of a valid command
               if(validInput(userInput.split("\s+")[0])){
                   client.send(userInput);
               }
               else{
                   System.err.println("Error: Invalid command");
               }
           }
           //If client is connected to server send command to server
           if(client.agentIsConnected()){
               client.send(userInput.toLowerCase());
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
            if (portNum > MIN_PORT && portNum < MAX_PORT) {
                return portNum; //Port is valid
            }
            return -1; //Port is not valid
        } catch (NumberFormatException nfe) {
            System.out.println("Port is not valid!");
            return -1; //Port is not valid
        }
    }

    /**
     * Checks if the inputted hostname is valid
     * @param hostname Hostname entered by user
     * @return Correct InetAddress if valid, null if hostname not valid
     */
    public static InetAddress checkHost(String hostname) {
        try {
            return InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            System.out.println("Invalid hostname!");
            return null;
        }
    }

    /**
     * Makes a check to see if the input string was part of a valid command
     * @param message - the first part of the command
     * @return true if the first part of the command was valid, false otherwise
     */
    public static boolean validInput(String message){
        //returns if the first part of the message from the user is part of a valid command
        return switch (message.toLowerCase()) {
            case "/surrender", "/battle", "/start", "/fire", "/display", "/players", "/commands" -> true;
            default -> false;
        };
    }
}
