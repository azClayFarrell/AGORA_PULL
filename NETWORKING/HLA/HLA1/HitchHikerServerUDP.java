/**
 * @author Clay Farrell
 * @date 9/6/22
 * server that accepts a number, adds 42 and returns it using UDP connection
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class HitchHikerServerUDP {

    public static void main(String[] args){
        int INT_BYTES = 4;
        int BYTE_LEN = 8;
        byte [] buffer = new byte[INT_BYTES];
        try {
            DatagramSocket datSoc = new DatagramSocket(5500);
            DatagramPacket datPack = new DatagramPacket(buffer, INT_BYTES);
            datSoc.setSoTimeout(60000);
            while (!datSoc.isClosed()){
                datSoc.receive(datPack);
                //this is to open the possibility for the offset to be sent later
                //int offset = datPack.getOffset();
                //in which case this would need to be commented out
                int offset = 0;

                InetAddress address = datPack.getAddress();
                int port = datPack.getPort();
                byte[] numAsBytes = datPack.getData();
                int byteAmt = datPack.getLength();
                int num = 0;
                for (int index = 0; index < byteAmt; index++){
                    /*gets the byte at the correct index, adding the offset.
                     * then shift left logical to get the bits into the correct
                     * spot to be added to the running bit total*/
                    num += (numAsBytes[index + offset] <<
                            (BYTE_LEN * (byteAmt - 1))) >>>
                            (BYTE_LEN * index);
                }
                int finalNum = num + 42;
                //for breaking the int back down into byte array
                byte[] returnBytes = new byte[byteAmt];
                byte temp = 0;
                for (int index = 0; index < byteAmt; index++){
                    //shifts the bits of the int and grabs the rightmost byte
                    temp = (byte) (finalNum >>> (BYTE_LEN *
                            (byteAmt - (index + 1))));
                    //then adds it to the return array
                    returnBytes[index] = temp;
                }

                DatagramPacket shipPacket = new DatagramPacket(returnBytes,
                        INT_BYTES, address, port);
                datSoc.send(shipPacket);
            }
            datSoc.close();

        }
        catch (SocketTimeoutException ste){
            System.err.println("Took too long to respond. " +
                    "Connection closing...");
        }
        catch (IOException ioe){
            System.err.println("There was a problem on the server side. x_x");
        }
    }
}
