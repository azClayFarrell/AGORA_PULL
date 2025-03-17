/**                                                                             
 * @author Clay Farrell                                                         
 * @version 10-4-2022                                                           
 * this is the server for the Dog Teleportation experiments. Opens connection
 * for clients to connect to and remains open until manually terminated.
 * Receives dog objects from clients and prints them by using their toString
 * method        
 */ 

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.ClassNotFoundException;

/**The server class for teleporting dogs. Contains all the logic for receving
 * a dog object and printing it out*/
public class DogTeleportServTCP {

    /** The port we will accept connections along*/
    private static final int PORT = 5500;

    /** The main method for receving a dog that has teleported. Opens the
     * sockets and input streams, reads in the dog object, prints the dog
     * according to its toString method, and handles errors
     *
     * @param args - command line arguments, unused in this case*/
    public static void main(String [] args){
        try{
            ServerSocket servSock = new ServerSocket(PORT);
            while (!servSock.isClosed()){
                //setting up sockets and input streams
                Socket accepted = servSock.accept();
                InputStream input = accepted.getInputStream();
                ObjectInputStream in = new ObjectInputStream(input);
                //getting the object and printing it out
                Object myDog = in.readObject();
                System.out.println(myDog.toString());
                //closing everything
                in.close();
                input.close();
                accepted.close();
            }
            servSock.close();
        }
        catch (ClassNotFoundException cnde){
	    System.err.println("Class not found");
	}
	
	//catching io
        catch (IOException ioe) {
            System.err.println("Something went wrong with the server x_x");
        }
    }
}
