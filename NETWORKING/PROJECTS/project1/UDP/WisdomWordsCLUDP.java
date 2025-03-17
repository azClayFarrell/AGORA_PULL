/**
 * @author Clay Farrell
 * @date 9/21/22
 * Client for UDP protocol. Sends request off to server and prints the sayings
 * it receives
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class for the client using UDP protocol. Accepts integer input from the user
 * to be sent to the server to get words of wisdom.
 */
public class WisdomWordsCLUDP {
    /** The default size of our arrays*/
    final static int INT_BYTES = 256;
    //final static int BYTE_LEN = 8;

    /**
     * The main method for the class does almost all the logic for the
     * execution of this program. It also does all the error handling.
     * Connects to server, then takes user input and sends it to the server,
     * waits for a return, and prints what it receives to the screen
     *
     * @param args - the command line arguments
     */
    public static void main(String[] args){
        //wrong number of arguments
        if (args.length != 2){
            System.err.println("ERROR: Wrong number of command " +
                    "line arguments!");
            System.out.println("Usage: java WisdomWordsCLUDP" +
                    " <server name(textual)> <port>");
            System.exit(1);
        }
        Scanner scan = new Scanner(System.in);

        System.out.println("Connecting to server: \"" + args[0] + "\" using " +
                "port: " + args[1] + "...");

        try{
            //parses the port as an integer
            int port = Integer.parseInt(args[1]);
            System.out.println("***************");
            System.out.print ("Enter number of sayings: ");
            //I'm accepting this as int so that the server won't have to check
            Integer num = scan.nextInt();
            if(num < 1){
                System.out.println("Number too small, defaulting to requesting"
                        + " 1 saying");
                num = 1;
            }
            System.out.println("***************");
            byte[] numAsBytes = num.toString().getBytes();
            //sending off the number
            DatagramSocket datSoc = new DatagramSocket();
            DatagramPacket datPack = new DatagramPacket(numAsBytes,
                    numAsBytes.length, InetAddress.getByName(args[0]), port);
            datSoc.send(datPack);
            //receive as many times as we requested
            for (int requests = 0; requests < num; requests++){
                //make and receive the packet
                DatagramPacket updatedPack = new DatagramPacket(
                        new byte[INT_BYTES], INT_BYTES);
                datSoc.receive(updatedPack);
                //print the sayings
                printSaying(updatedPack.getData(), requests);
            }
            System.out.println("***************");
            System.out.println("Enter your favorite: ");
            Integer favorite = 0;
            try{
                favorite = scan.nextInt();
            }
            catch(InputMismatchException imme){
                System.out.println("Invalid input, defaulting to index 0.");
                favorite = 0;
            }
            System.out.println("***************");
            if (favorite >= num){
                System.out.println("ERROR: Bad Index Given! Defaulting to 0.");
                favorite = 0;
            }
            //convert the favorite into a byte array
            numAsBytes = favorite.toString().getBytes();
            //create and send the clients favorite saying index
            datSoc.send(new DatagramPacket(numAsBytes, numAsBytes.length,
                    InetAddress.getByName(args[0]), port));
            DatagramPacket updatedPack = new DatagramPacket(
                    new byte[INT_BYTES], INT_BYTES);
            //reveive and print one more time
            datSoc.receive(updatedPack);
            printSaying(updatedPack.getData());
            System.out.println("***************");
            datSoc.close();
        }

        catch(NumberFormatException nfe){
            System.err.println("The port supplied was not an integer");
        }
        catch (IndexOutOfBoundsException ioobe) {
            System.err.println("ERROR: Bad Index Given!");
        }
        catch (InputMismatchException imme){
            System.err.println("ERROR: input was not an integer");
        }
        catch (IOException ioe){
            System.err.println("Something when wrong on the client side :(");
            System.err.println(ioe.getMessage());
        }
        finally{
            scan.close();
        }
    }

    /**
     * Helper function to print the sayings out
     *
     * @param words - the array of bytes that are to be converted to words
     *              and printed
     * @param requests - the index of the wise saying
     */
    private static void printSaying(byte[] words, int requests){
        //int byteAmt = updatedPack.getLength();
        //char array for the bytes to be converted
        char[] letters = new char[words.length];
        for (int index = 0; index < words.length; index++){
            //loop and cast to chars and store the result
            letters[index] = (char) words[index];
        }
        //i think this makes a string from the char[] lol
        String wiseWords = new String(letters);
        System.out.println(requests + ":\n" + wiseWords + "\n");

    }

    /**
     * Helper function to print the sayings out
     *
     * @param words - the array of bytes that are to be converted to words and
     *              printed
     */
    private static void printSaying(byte[] words){
        //int byteAmt = updatedPack.getLength();
        //char array for the bytes to be converted
        char[] letters = new char[words.length];
        for (int index = 0; index < words.length; index++){
            //loop and cast to chars and store the result
            letters[index] = (char) words[index];
        }
        //i think this makes a string from the char[] lol
        String wiseWords = new String(letters);
        System.out.println(wiseWords + "\n");

    }
}
