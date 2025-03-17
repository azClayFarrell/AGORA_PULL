package client;

import common.Card;
import common.Type;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: This class represents a concrete implementation of a dealer client that uses the
 * UDP network layer protocol.
 */

public class DealerUdpClient extends AbstractDealerClient{
    /** the buffer size*/
    private final int BUFFER_SIZE = 256;
    /**
     * Initializes a new DealerUdpClient with the specified host, and the default port.
     * @param host - the address of the remote host to which to connect.
     */
    public DealerUdpClient(InetAddress host) {
        super(host);
    }

    /**
     * Initializes a new DealerUdpClient with the specified host and port.
     * @param host - the address of the remote host to which to connect
     * @param port - the port on the remote host to which to connect
     */
    public DealerUdpClient(InetAddress host, int port) {
        super(host, port);
    }

    /**
     * Initializes a new DealerUdpClient with the specified host and port.
     * @param host - the address of the remote host to which to connect
     * @param port - the port on the remote host to which to connect
     * @param flag - the arguments to send to the server
     */
    public DealerUdpClient(InetAddress host, int port, String flag) {
        super(host, port, flag);
    }

    @Override
    public void printToStream(PrintStream out) throws IOException, ClassNotFoundException{
        InetAddress address = super.getHost();
        int port = super.getPort();
        String flag = super.getFlag();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(BUFFER_SIZE);
        ObjectOutputStream outObject = new ObjectOutputStream(buffer);
        outObject.flush();

        if(flag.matches("-[AHSDC]") || flag.matches("(-RED|-BLK)(RED|BLK)?")){
            outObject.writeObject(flag);
            byte[] flagBytes = buffer.toByteArray();
            DatagramSocket sock = new DatagramSocket();
            DatagramPacket pack = new DatagramPacket(flagBytes, flagBytes.length, address, port);
            sock.send(pack); // send to server
            sock.setSoTimeout(AbstractDealerClient.TIMEOUT); // time out after the default
                // time if no response
            while(!sock.isClosed()){
                byte [] cardBuffer = new byte [BUFFER_SIZE];
                DatagramPacket cardPacket = new DatagramPacket(cardBuffer, cardBuffer.length);
                sock.receive(cardPacket);
                byte [] cardBytes = cardPacket.getData();
                // if the array passed is empty then we stop
                if (cardPacket.getLength() == 0){
                    sock.close();
                } else {
                    ByteArrayInputStream input = new ByteArrayInputStream(cardBytes);
                    ObjectInputStream makeFlag = new ObjectInputStream(input);
                    Object unknownType = makeFlag.readObject();
                    Card tempCard = null;
                    // check if the unknown card is a Card
                    if (unknownType instanceof Card){
                        // set unknown card type to be a card object
                        tempCard = (Card) unknownType;
                        // if the card is unknown close the socket, we've reached the end of the cards
                        if (tempCard.getType() == Type.UNKNOWN){
                            sock.close();
                        }
                        else{
                            out.println(tempCard);
                        }
                    }else{
                        throw new ClassNotFoundException("Error: unexpected class type received" +
                                " from the server");
                    }
                }
            }
        }else {
            System.err.println("Error: Flag is invalid. Provided: " + flag);
        }
    }
}
