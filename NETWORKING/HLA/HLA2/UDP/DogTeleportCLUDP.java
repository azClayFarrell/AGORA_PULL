/**
 * @author Clay Farrell
 * @version 10-6-2022
 * A client for sending dogs using UDP
 */


import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**The class that is used for sending dogs as bytes using UDP*/
public class DogTeleportCLUDP {

    /**The length of the buffer (in bytes) that we will use to send our dog*/
    final static int LEN_BUFFER = 256;

    /**the main method for sending the dog over a UDP connection. Checks
     * command line arguments, takes user input, converts the dog to bytes,
     * sends the dog, catches errors
     *
     * @param args - command line arguments that should be the server name and
     *               the port number the connection will be established over*/
    public static void main(String[] args){
        if (args.length != 2){
            System.err.println("ERROR: Wrong number of command " +
                    "line arguments!");
            System.out.println("Usage: java DogTeleportCLUDP" +
                    " <server name(textual)> <port>");
            System.exit(1);
        }
        Scanner scan = new Scanner(System.in);
        System.out.println("Connecting to server: \"" + args[0] + "\" using " +
                "port: " + args[1] + "...");
        try {
            //try to parse the port as an int to exit before taking input
            int port = Integer.parseInt(args[1]);

            //getting info about the dog
            System.out.print("Enter breed of dog: ");
            String breed = scan.nextLine();
            System.out.println("Enter dog's name: ");
            String name = scan.nextLine();
            //making the output streams
            ByteArrayOutputStream dogBytes = new ByteArrayOutputStream(
                    LEN_BUFFER);
            ObjectOutputStream teleportApparatus = new ObjectOutputStream(
                    dogBytes);
            //for good measure
            teleportApparatus.flush();
            //making the dog and converting it to be sent to the server
            Serializable myDog = new Dog(name, breed);
            teleportApparatus.writeObject(myDog);
            byte[] dogMeat = dogBytes.toByteArray();
            //creating the socket and packet to send the dog
            DatagramSocket datSoc = new DatagramSocket();
            DatagramPacket datPack = new DatagramPacket(dogMeat, dogMeat.length
                    ,InetAddress.getByName(args[0]), port);
            //send the dog
            datSoc.send(datPack);
        }
        catch (NumberFormatException nfe){
            System.err.println("The port number entered was not numeric");
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
