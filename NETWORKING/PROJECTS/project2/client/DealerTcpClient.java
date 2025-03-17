package client;

import common.Card;
import common.Type;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: The client implementation of TCP client
 */

public class DealerTcpClient extends AbstractDealerClient{

    /**
     * Initializes a new DealerTcpClient with the specified host, and the default port.
     * @param host - the specified host
     */
    public DealerTcpClient(InetAddress host) {
        super(host);
    }

    /**
     * Initializes a new DealerTcpClient with the specified host and port.
     * @param host - the specified host
     * @param port - the specified port
     */
    public DealerTcpClient(InetAddress host, int port) {
        super(host, port);
    }

    /**
     * Initializes a new DealerTcpClient with the specified host, port, and flag.
     * @param host - the specified host
     * @param port - the specified port
     * @param flag - the specified flag
     */
    public DealerTcpClient(InetAddress host, int port, String flag) {
        super(host, port, flag);
    }

    /**
     * Establishes a TCP connection to the host/port specified when this object was created,
     * reads a continuous stream of random cards from the socket's input stream, and prints
     * that data to the specified output stream.
     * @param out - The stream to which to write the random cards received
     * @throws IOException - if there is an I/O error while receiving the data
     */
    public void printToStream(PrintStream out) throws IOException, ClassNotFoundException{
        InetAddress address = super.getHost();
        int port = super.getPort();
        String flag = super.getFlag();
        if(flag.matches("-[AHSDC]") || flag.matches("(-RED|-BLK)(RED|BLK)?")){
            Socket socket = new Socket(address, port); // create socket for connection
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()); // receiving the info from driver
            output.flush();
            output.writeObject(flag); // send the flag server
            // input stream linked to a socket
            InputStream input = socket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(input);
            while(!socket.isClosed()){
                Object unknownType = in.readObject(); // read in the cards
                Card tempCard = null;
                // check if the unknown card is a Card
                if (unknownType instanceof Card){
                    // set unknown card type to be a card object
                    tempCard = (Card) unknownType;
                    // if the card is unknown close the socket, we've reached the end of the cards
                    if (tempCard.getType() == Type.UNKNOWN){
                        socket.close();
                    }
                    else{
                        out.println(tempCard);
                    }
                }else{
                    throw new ClassNotFoundException("Error: unexpected class type received" +
                            " from the server");
                }
            }

            // close all the things
            in.close();
            input.close();
            output.close();
            socket.close();
        }
        else{
            System.err.println("Error: Flag is invalid. Provided: " + flag);
        }
    }
}
