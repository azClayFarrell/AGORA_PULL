package server;

import common.Card;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: This class represents a concrete implementation of a dealer server that uses the
 * UDP transport layer protocol.
 */

public class UdpDealerServer extends AbstractDealerServer {
    /**
     * the buffer size
     */
    private final int BUFFER_SIZE = 256;

    /**
     * Creates a new UdpDealerServer that listens for connections on the default dealer TCP port,
     * and uses the default card source.
     */
    public UdpDealerServer() throws FileNotFoundException {
        super();
    }

    /**
     * Creates a new UdpDealerServer that listens for connections on the specified port.
     *
     * @param port - the port
     */
    public UdpDealerServer(int port) throws FileNotFoundException {
        super(port);
    }

    /**
     * Creates a new UdpDealerServer that listens for connections on the specified TCP port and
     * uses the specified card source.
     *
     * @param port   - the specified port
     * @param source - source used to generate cards
     */
    public UdpDealerServer(int port, CardSource source) {
        super(port, source);
    }

    /**
     * Creates a new UdpDealerServer that listens for connections on the default magic TCP port and
     * uses the specified card source.
     *
     * @param source - source used to generate cards
     */
    public UdpDealerServer(CardSource source) {
        super(source);
    }


    /**
     * Causes the dealer server to listen for requests.
     */
    @Override
    public void listen() throws DealerServerException {
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            DatagramSocket sock = new DatagramSocket(super.getPort());
            DatagramPacket pack = new DatagramPacket(buffer, buffer.length);

            while (!sock.isClosed()) {
                sock.receive(pack); // receiving the flag
                byte[] flagBytes = pack.getData();
                ByteArrayInputStream input = new ByteArrayInputStream(flagBytes);
                ObjectInputStream makeFlag = new ObjectInputStream(input);
                Object flagAsObj = makeFlag.readObject();
                String flag = "";
                if (flagAsObj instanceof String) {
                    // set the flag
                    flag = (String) flagAsObj;
                }

                // select flag type
                super.setCardsReturned(flag);
                // an array of cards that is numCards size
                Card[] hand = new Card[getItemsToSend()];
                //picks a number of cards depending on the flag
                for (int i = 0; i < getItemsToSend(); i++) {
                    // sets the card type
                    source.setCardType(cType);
                    // adds the random card to hand
                    hand[i] = source.next();
                }

                // for each card in the hand, send each card back to the client
                for (Card card : hand) {
                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream(BUFFER_SIZE);
                    ObjectOutputStream outObject = new ObjectOutputStream(byteBuffer);
                    outObject.writeObject(card);
                    byte[] cardBytes = byteBuffer.toByteArray();
                    DatagramPacket sendPack = new DatagramPacket(cardBytes, cardBytes.length,
                            pack.getAddress(), pack.getPort());
                    sock.send(sendPack);
                }
                byte[] emptyArray = {};
                // send datagram of 0 bytes to end
                DatagramPacket sendPack = new DatagramPacket(emptyArray, 0,
                        pack.getAddress(), pack.getPort());
                sock.send(sendPack);
                source.resetDeck();

                input.close();
                makeFlag.close();
            }
            sock.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new DealerServerException();
        }
    }
}

