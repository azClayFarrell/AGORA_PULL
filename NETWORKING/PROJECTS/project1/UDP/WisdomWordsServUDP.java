/**
 * @author Clay Farrell
 * @date 9/21/22
 * Server for UDP protocol. Recieve requests and sends wise words
 */

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Random;

/**
 * This is a class that can be run on a server. It will await a connection from
 * a client process via port 6000. When a client sends a valid number, the server
 * will create a WisdomWords object that holds strings of wise sayings. This
 * program will randomly pick sayings from that storage and send them back to
 * the client. The client will then send the index of thier favorite one, and
 * the server will send that one again and terminate that connection.
 */
public class WisdomWordsServUDP {
    /**A default for a byte array size*/
    final static int INT_BYTES = 256;
    /**The port number for UDP access*/
    final static int PORT = 6000;
    /**The amount of milliseconds for a time out. 0 meaning infinity*/
    final static int TIMEOUT = 0;
    /**The number for having valid command line arguments*/
    final static int ARG = 1;

    /**
     * The main method of the program holds almost all the logic for the server
     * to operate. main() does all the error checking and handling as well
     *
     * @param args - the command line arguments
     */
    public static void main(String[] args){
        //create a buffer
        byte [] buffer = new byte[INT_BYTES];
        //if the number of arguments is not enough to have been supplied a file
        if (args.length != ARG){
            System.err.println("ERROR: Not enough command line arguments!");
            System.out.println("Usage: java WisdomWordsServUDP " +
                    "<file of wise sayings>");
            System.exit(1);
        }

        try {
            WisdomWords words = new WisdomWords(args[0]);
            //setting up the server
            DatagramSocket datSoc = new DatagramSocket(PORT);
            DatagramPacket datPack = new DatagramPacket(buffer, INT_BYTES);
            datSoc.setSoTimeout(TIMEOUT);
            //while connection is open
            while (!datSoc.isClosed()){
                datSoc.receive(datPack);
                //this is to open the possibility for the offset to be sent later
                //int offset = datPack.getOffset();
                //in which case this would need to be commented out
                //int offset = 0;

                //pull out all the info for returning the packet
                InetAddress address = datPack.getAddress();
                int port = datPack.getPort();
                byte[] numAsBytes = datPack.getData();
                //WisdomWords words = new WisdomWords(args[0]);
                Random rand = new Random();
		        //DEBUG
		        //System.out.println(numAsBytes.length);
		        //for (int i = 0; i < numAsBytes.length; i++){
		        //    System.out.println("********" + i + ": " + numAsBytes[i]);
		        //}
		        //System.out.println(convertToString(numAsBytes));
                //END DEBUG

		        //converts the number as bytes into a string and stores it
		        String numAsString = convertToString(numAsBytes).strip();
		        //should parse the byte array to a string then an integer
                int num = Integer.parseInt(numAsString.toString());
                //DEBUG
		        //System.out.println("I made it, I'm alive!");
		        //END DEBUG

		        //storage for the sayings that we will grab from WisdomWords
                String[] sayings = new String[num];
                //per the number of requests from the client
                for (int requests = 0; requests < num; requests++){
                    //will get a string from the WisdomWords class and store it
                    sayings[requests] = words.getWisdom(
                            rand.nextInt(words.getSize()));
                }
		        //DEBUG
		        //for (int i = 0; i < num; i++){
		        //    System.out.println("*****" + sayings[i] + "*****");
		        //}
		        //END DEBUG

                //sends all the sayings that were picked back to the client
                for(int index = 0; index < sayings.length; index++){
                    byte[] byteSizeWisdom = sayings[index].getBytes();
                    DatagramPacket shipPacket = new DatagramPacket(byteSizeWisdom,
                            byteSizeWisdom.length, address, port);
                    datSoc.send(shipPacket);
                }
                //reveive the favorite from the client
                datSoc.receive(datPack);
		        /*idk if i need to get the address and port again since this
		        should be along the same connection, but I did*/
                address = datPack.getAddress();
                port = datPack.getPort();
                numAsBytes = datPack.getData();
                num = Integer.parseInt(convertToString(numAsBytes));
                byte[] byteSizeWisdom = sayings[num].getBytes();
                DatagramPacket shipPacket = new DatagramPacket(byteSizeWisdom,
                        byteSizeWisdom.length, address, port);
                datSoc.send(shipPacket);
            }
            datSoc.close();
        }
        catch (SocketTimeoutException ste){
            System.err.println("Took too long to respond. " +
                    "Connection closing...");
        }
        catch (FileNotFoundException fnfe){
            System.err.println("ERROR: The file entered could not be found.");
        }
        catch (IOException ioe){
            System.err.println("There was a problem on the server side. x_x");
        }
    }

    /**
     * helper function to convert a byte array into a String to parse as an int
     *
     * @param bytes - the byte array received from the client
     * @return the bytes converted into a string
     */
    private static String convertToString(byte[] bytes){
        //ArrayList for the bytes to be converted
        ArrayList<Character> num = new ArrayList<>();
        for (int index = 0; index < bytes.length; index++){
            //loop and cast to chars and store in the ArrayList
            if(bytes[index] != 0){
                num.add((char)bytes[index]);
            }
        }
        //now store the ArrayList as an array
        Object[] charArray = num.toArray();
        //make a char[] for the String constructor
        char[] number = new char[charArray.length];
        //loop through and unbox accordingly
        for(int index = 0; index < charArray.length; index++){
	    //this check is overkill but I've been taught to do it this way
	    if (charArray[index] instanceof Character){
		//convert and unbox into the char array for the String constructor
                Character letter = (Character) charArray[index];
                number[index] = letter;
            }
	}
        //i think this makes a string from the char[] lol
        return new String(number);
    }
}
