/**
 * @author Clay Farrell
 * @date 9/6/22
 * client that asks for a number, sends it to a server and reads the returned
 * value over a UDP connection
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HitchHikerClientUDP {
    public static void main(String[] args){
        if (args.length != 2){
            System.err.println("ERROR: Wrong number of command " +
                    "line arguments!");
            System.out.println("Usage: java HitchHikerClientUDP" +
                    " <server name(textual)> <port>");
            System.exit(1);
        }
        Scanner scan = new Scanner(System.in);

        System.out.println("Connecting to server: \"" + args[0] + "\" using " +
                "port: " + args[1] + "...");
        //because ints in java are 4 bytes
        int INT_BYTES = 4;
        int BYTE_LEN = 8;

        try{
            //parses the port as an integer
            int port = Integer.parseInt(args[1]);
            System.out.print ("Please enter your age: ");
            int age = scan.nextInt();

            //for breaking the age down into byte array
            byte[] sendBytes = new byte[INT_BYTES];
            byte temp = 0;
            for (int index = 0; index < INT_BYTES; index++){
                //shifts the bits of the int and grabs the rightmost byte
                temp = (byte) (age >>> (BYTE_LEN *
                        (INT_BYTES - (index + 1))));
                //then adds it to the return array
                sendBytes[index] = temp;
            }

            DatagramSocket datSoc = new DatagramSocket();
            DatagramPacket datPack = new DatagramPacket(sendBytes, INT_BYTES,
                    InetAddress.getByName(args[0]), port);
            datSoc.send(datPack);
            DatagramPacket updatedPack = new DatagramPacket(
                    new byte[INT_BYTES], INT_BYTES);
            datSoc.receive(updatedPack);

            byte[] numAsBytes = updatedPack.getData();
            int byteAmt = updatedPack.getLength();
            int num = 0;
            for (int index = 0; index < byteAmt; index++){
                /*gets the byte at the correct index,
                 * then shift left then right to get the bits into the correct
                 * spot to be added to the running bit total*/
                num += (numAsBytes[index] << (BYTE_LEN * (byteAmt - 1))) >>>
                        (BYTE_LEN * index);
            }

            System.out.println("Your number is: " + num);
            datSoc.close();
            scan.close();
        }

        catch(NumberFormatException nfe){
            System.err.println("The port supplied was not an integer");
        }
        catch (InputMismatchException imme){
            System.err.println("The age entered was not an integer");
        }
        catch (IOException ioe){
            System.err.println("Something when wrong on the client side :(");
            System.err.println(ioe.getMessage());
        }
        finally{
            scan.close();
        }
    }
}