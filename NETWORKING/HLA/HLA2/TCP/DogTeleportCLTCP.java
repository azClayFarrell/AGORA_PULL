/**
 * @author Clay Farrell
 * @version 10-4-2022
 * this is the client for the Dog Teleportation experiments. Connects to
 * the server, asks user input about the dog, makes the dog object, sends
 * the dog object to the server
 */


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**The client side of the Dog Teleportation. Contains and carries out all the
 * logic for sending a dog*/
public class DogTeleportCLTCP {
    /**main method for all the logic of teleporting a dog. checks args,
     * connects to server, takes user input, makes a dog, sends a dog, handles
     * errors that occur.
     *
     * @param args - command line arguments*/
    public static void main (String [] args) {
        if (args.length != 2){
            System.err.println("ERROR: Wrong number of command " +
                    "line arguments!");
            System.out.println("Usage: java DogTeleportCLTCP" +
                    " <server name(textual)> <port>");
            System.exit(1);
        }
        Scanner scan = new Scanner(System.in);
        System.out.println("Connecting to server: \"" + args[0] + "\" using " +
                "port: " + args[1] + "...");

        try {
            //opening the ports and output streams
            int port = Integer.parseInt(args[1]);
            Socket socket = new Socket(InetAddress.getByName(args[0]), port);
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.flush();
            //taking user input about the dog and making the Dog instance
            System.out.print("Enter breed of dog: ");
            String breed = scan.nextLine();
            System.out.println("Enter dog's name: ");
            String name = scan.nextLine();
            Dog myDog = new Dog(name, breed);
            //System.out.println(myDog);
            //sending the dog as an object through TCP connection
            out.writeObject(myDog);

            socket.close();

        }

        catch (IOException ioe){
            System.err.println("Something went wrong with the client :(");
            System.err.println(ioe.getMessage());
        }
        finally{
            scan.close();
        }
    }
}
