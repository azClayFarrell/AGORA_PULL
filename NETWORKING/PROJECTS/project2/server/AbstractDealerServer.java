package server;

import java.io.FileNotFoundException;
/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: An abstract class that contains fields and methods that may be common to a dealer implementation
 */
public abstract class AbstractDealerServer implements DealerServer{
    /** The default of selecting all types of cards */
    protected static final int ALL_TYPES = 15;
    /** The default port of 5500 if one is not supplied */
    static final int DEFAULT_PORT = 5500;
    /** The default of selecting 15 cards */
    static final int NUM_ITEMS = 15;
    /** The default of one type, 5 cards */
    protected static final int ONE_TYPE = 5;
    /** The default of two types, 10 cards */
    protected static final int TWO_TYPES = 10;

    /** the port */
    private int port;
    /** the CardSource source card */
    protected CardSource source;
    /** the type of card */
    protected CardType cType;
    /** the number of items */
    private int numItems;

    /**
     * Initializes a new AbstractDealerServer using the default port and the default source.
     */
    public AbstractDealerServer() throws FileNotFoundException {
        this.port = DEFAULT_PORT;
        this.source = new CardSource();
        this.numItems = NUM_ITEMS;
    }

    /**
     * Initializes a new AbstractDealerServer using the specified port and the default source.
     * @param port - the specified port
     * @throws FileNotFoundException - If the source file cannot be found.
     */
    public AbstractDealerServer(int port) throws FileNotFoundException{
        this.port = port;
        this.source = new CardSource();
        this.numItems = NUM_ITEMS;
    }

    /**
     * Initializes a new AbstractDealerServer using the default port and the specified source.
     * @param port - the specified port
     * @param source - the specified card source
     */
    public AbstractDealerServer(int port, CardSource source){
        this.port = port;
        this.source = source;
        this.numItems = NUM_ITEMS;
    }

    /**
     * Initializes a new AbstractDealerServer using the specified port, card source, and number of
     * items to send.
     * @param port - the specified port
     * @param source - the specified card source
     * @param numItems - the number of items to send
     */
    public AbstractDealerServer(int port, CardSource source, int numItems){
        this.port = port;
        this.source = source;
        this.numItems = numItems;
    }

    /**
     * Initializes a new AbstractDealerServer using the default port and the specified source.
     * @param source - the specified source
     */
    public AbstractDealerServer (CardSource source) {
        this.port = DEFAULT_PORT;
        this.source = source;
        this.numItems = NUM_ITEMS;
    }

    /**
     * Change how many items to send back to the client. This number cannot fall below the default
     * of 5 items.
     * @param numItems - the number of items to send back to the client
     */
    protected void changeItemsToSend(int numItems) {
        if (numItems < 5) {
            this.numItems = NUM_ITEMS;
        } else {
            this.numItems = numItems;
        }
    }

    /**
     * Change which source is being used to generate characters for the server.
     * @param source - a CardSource used to generate cards
     */
    protected void changeSource(CardSource source){
        this.source = source;
    }

    /**
     * Determine the current number of items we should be sending back to the client.
     * @return - the number of items to send back to the client
     */
    protected int getItemsToSend() {
        return numItems;
    }

    /**
     * Get the port to which the server will bind and listen for incoming connections.
     * @return - the port to which the server will bind and listen for incoming connections
     */
    protected int getPort() {
        return port;
    }

    /**
     * Get the CardSource to use for generating the character stream to send to clients.
     * @return - the CardSource used to generate the cards being sent to clients
     */
    protected CardSource getSource() {
        return source;
    }

    /**
     * Get the card type.
     * @return - the card type
     */
    protected CardType getCType(){ return this.cType; }

    /**
     * Determine the type (Hearts, Diamonds, Clubs, Spades, or some combination thereof) as well as
     * the number returned to the client.
     * @param command - the flag data returned from the server
     */
    protected void setCardsReturned(String command){
        // determine based on the flag given
        switch(command){
            // all the possible cases for the flags & set number of cards
            case "-H":
                cType = CardType.HEARTS;
                numItems = AbstractDealerServer.ONE_TYPE;
                break;
            case "-S":
                cType = CardType.SPADES;
                numItems = AbstractDealerServer.ONE_TYPE;
                break;
            case "-D":
                cType = CardType.DIAMONDS;
                numItems = AbstractDealerServer.ONE_TYPE;
                break;
            case "-C":
                cType = CardType.CLUBS;
                numItems = AbstractDealerServer.ONE_TYPE;
                break;
            case "-RED":
            case "-REDRED":
                cType = CardType.RED;
                numItems = AbstractDealerServer.TWO_TYPES;
                break;
            case "-BLK":
            case "-BLKBLK":
                cType = CardType.BLACK;
                numItems = AbstractDealerServer.TWO_TYPES;
                break;
            case "-REDBLK":
            case "-BLKRED":
            case "-A":
                cType = CardType.ALL;
                numItems = AbstractDealerServer.NUM_ITEMS;
                break;
            default:
                // unusable flag
                System.err.println("Unknown flag type given.");
                cType = CardType.UNKNOWN;
        }
    }

    /**
     * Causes the dealer server to listen for requests
     * @throws DealerServerException - if an error occurs while trying to listen for connections
     */
    public abstract void listen() throws DealerServerException;
}
