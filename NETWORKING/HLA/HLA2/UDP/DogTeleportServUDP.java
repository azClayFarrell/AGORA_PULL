/**
 * @author Clay Farrell
 * @version 10-6-2022
 * This is the server that will receive a teleported dog using UDP
 */


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

/** the Class that holds all the logic for receiving a dog that has teleported*/
public class DogTeleportServUDP {
    /**the length of the byte array being received*/
    final static int INT_BYTES = 256;

    /**the port the server will be open on*/
    final static int PORT = 5500;

    /**allows the server to run until a manual interrupt happens*/
    final static int TIMEOUT = 0;

    /**the main method for receiving a dog that has teleported. Contains all
     * the logic and handles errors.
     *
     * @param args - any command line arguments supplied (unused in this case)*/
    public static void main(String[] args){

        //making the buffer for the DatagramPacket
        byte [] buffer = new byte[INT_BYTES];

        try {
            //setting up the server
            DatagramSocket datSoc = new DatagramSocket(PORT);
            DatagramPacket datPack = new DatagramPacket(buffer, INT_BYTES);
            datSoc.setSoTimeout(TIMEOUT);
            //while connection is open
            while (!datSoc.isClosed()){
                datSoc.receive(datPack);
                //pull out the info about the dog in bytes
                byte[] dogBytes = datPack.getData();
                ByteArrayInputStream input = new ByteArrayInputStream(
                        dogBytes);
                ObjectInputStream makeDog = new ObjectInputStream(input);
		//convert the dog byte array back to an Object
                Object myDog = makeDog.readObject();
		//print the dog we got it to the terminal
                System.out.println(myDog);

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
        catch (ClassNotFoundException cnfe){
            System.err.println("ERROR: The class received was not found on" +
                    " the server side :(");
        }
        catch (IOException ioe) {
            System.err.println("There was a problem on the server side. x_x");
        }
    }
}

