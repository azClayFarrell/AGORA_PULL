/**
 * @author Clay Farrell
 * @date 9/21/22
 * Server for the TCP protocol. Receive requests and sends wise words.
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * This is the class for the server for the TCP protocol. This program will
 * create a WisdomWords Object to store Strings to send to the client.
 * It will then open on port 500 and wait until it is contacted by the
 * client with the number of wise sayings it wants. This class will then
 * pick random Strings from the WisdomWords class to be sent back to the
 * client, the client will send the index of their favorite, and that one
 * will be resent.
 */
public class WisdomWordsServTCP {
    
    /** The port on which the TCP connection will be established*/
    private static final int TCP_PORT = 5000;
    /** The number for having valid number of command line arguments*/
    private static final int ARG = 1;

    /**
     * The main method of the TCP server class has all the required logic to
     * make the server functional. The method will handle errors and accept
     * data from the client, create a WisdomWords instance, and send Strings
     * back to the client.
     *
     * @param args - the command line arguments
     */
    public static void main(String[] args){
        //if there are not enough args to have a file provided
        if (args.length != ARG){
            //then the command line args are invalid
            System.err.println("USAGE: java WisdomWordsServTCP <file of wise" +
			   " sayings>");
	    System.exit(1);
        }
        try{
            //try using the file provided to make a WisdomWords object
            WisdomWords words = new WisdomWords(args[0]);
            //open the sockets
            ServerSocket servSock = new ServerSocket(TCP_PORT);
            while(!servSock.isClosed()){
                //continue setting up the connections
                Socket accepted = servSock.accept();
                InputStreamReader input = new InputStreamReader(
                        accepted.getInputStream());
                //make the reader and writer for communication
                BufferedReader read = new BufferedReader(input);
                PrintWriter write = new PrintWriter(accepted.getOutputStream(),
                        true);
                //just in case
                write.flush();

                //make random number generator to use on WisdomWords object
                Random rand = new Random();
                //gets the number of wise sayings desired from the client
                String numberOfSayings = read.readLine();
                //parses the sayings into an int
                int number = Integer.parseInt(numberOfSayings);
                //creates an array with the number of string elements requested
                String[] sayings = new String[number];
		//get the size of the words array
                int size = words.getSize();
                //populate the selection of sayings on the server side
                for (int pick = 0; pick < number; pick++){
                    sayings[pick] = words.getWisdom(rand.nextInt(size));
                }

                /* this could have been done in the previous loop, but I think
                   this is slightly more coherent. writes back to the client*/
                for (int index = 0; index < number; index++){
                    write.println(sayings[index]);
		}

                //gets the favorite saying from the client
                String favorite = read.readLine();
                //I don't think I need to check like this again but I am
                if (favorite.matches("[0-9]*")){
		    number = Integer.parseInt(favorite);	
		    //if the string only held numbers then it parsed correctly
                    write.println(sayings[number]);
                }
                else{
                    //otherwise the client sent non-parsable characters
                    throw new NumberFormatException();
                }

                //closing streams and listening Socket
                write.close();
                read.close();
                input.close();
                accepted.close();
            }
            //close server socket
            servSock.close();

        }
        catch (NumberFormatException nfe){
            System.err.println("The number received from the client could not" +
                    " be parsed as an integer");
        }
        catch (FileNotFoundException fnfe) {
            System.err.println("The file provided from command line was not " +
                    "found");
        }
        catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
        }
        catch (IOException ioe){
            System.err.println("There was a problem with the server x_x");
            System.err.println(ioe.getMessage());
        }
    }
}
