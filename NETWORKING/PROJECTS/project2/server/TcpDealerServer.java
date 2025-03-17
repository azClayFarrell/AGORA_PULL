package server;

import common.Card;
import common.Type;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;


/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: This class represents a concrete implementation of a dealer server that uses
 * the TCP transport layer protocol.
 */

public class TcpDealerServer extends AbstractDealerServer {


    /**
     * Initializes a new AbstractDealerServer using the default port and the default source. : *
     *
     * @throws FileNotFoundException - If the source file cannot be found.
     */
    public TcpDealerServer() throws FileNotFoundException {
        super();
    }

    /**
     * Creates a new TcpDealerServer that listens for connections on the specified port.
     *
     * @param port - port the server will listen at
     * @throws FileNotFoundException - the input file cannot be located
     */
    public TcpDealerServer(int port) throws FileNotFoundException {
        super(port);
    }

    /**
     * Creates a new TcpDealerServer that listens for connections on the default dealer TCP
     * port and uses the specified card source.
     *
     * @param source - source used to generate cards
     * @throws FileNotFoundException - the input file cannot be located
     */
    public TcpDealerServer(CardSource source) throws FileNotFoundException {
        super(source);
    }

    /**
     * Creates a new TcpDealerServer that listens for connections on the specified port and
     * uses the specified card source.
     *
     * @param port   - port the server will listen at
     * @param source - source used to generate cards
     */
    public TcpDealerServer(int port, CardSource source) {
        super(port, source);
    }

    /**
     * Causes the dealer server to listen for requests.
     */
    @Override
    public void listen() throws DealerServerException {
        try {
            ServerSocket socket = new ServerSocket(super.getPort());

            while(!socket.isClosed()){
                // accepts the servers request
                Socket incoming = socket.accept();

                // input stream linked to socket
                InputStream inStream = incoming.getInputStream();
                ObjectInputStream objInStream = new ObjectInputStream(inStream);

                // create flag object
                Object genericFlag = objInStream.readObject();
                String flag = ""; // instantiate a flag string to hold the flag
                // if the flag is an instance of a string..
                if (genericFlag instanceof String){
                    // set the flag to the genFlag
                    flag = (String) genericFlag;
                }
                else{
                    // theres a problem, flag is not string
                    throw new InputMismatchException("ERROR: flag was unexpected type.");
                }

                // select flag type
                super.setCardsReturned(flag);
                // an array of cards that is numCards size
                Card [] hand = new Card[getItemsToSend()];

                // sending to client :)
                OutputStream output = incoming.getOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(output);
                out.flush();

                //picks a number of cards depending on the flag
                for (int i=0; i < getItemsToSend(); i++){
                    // sets the card type
                    source.setCardType(cType);
                    // adds the random card to hand
                    hand[i] = source.next();
                }

                // for each card in the hand, send each card back to the client
                for (Card card : hand){
                    out.writeObject(card);
                }
                // write unknown card
                out.writeObject(new Card((short)0, "", Type.UNKNOWN, ""));
                source.resetDeck();

                // closing stuff!
                out.close();
                output.close();
                objInStream.close();
                inStream.close();
                incoming.close();
            }
            socket.close(); // close server
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            throw new DealerServerException("Something went wrong with the server :(");
        }
    }
}
