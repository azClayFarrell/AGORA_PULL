package client;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;

/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: An abstract class that contains fields and methods that may be common to
 * implementations of 'dealer' protocol
 */

/**
 * class for the abstraction for how a dealer behaves
 * */
public abstract class AbstractDealerClient implements DealerClient{
    /** The default flag of all cards*/
    static final String DEFAULT_FLAG = "-A";
    /** The default port */
    static final int DEFAULT_PORT = 5500;
    /** The default timeout time*/
    static final int TIMEOUT = 5000;
    /** field for the InetAddress for the host*/
    private InetAddress host;
    /** filed for the port that will be input*/
    private int port;
    /** field for the flag that will be input*/
    private String flag;

    /**
     * Initializes a new AbstractDealerClient with the specified host, and the default port.
     * @param host - the address of the remote host to which to connect
     */
    public AbstractDealerClient (InetAddress host){
        this.host = host;
        this.port = DEFAULT_PORT;
        this.flag = DEFAULT_FLAG;
    }

    /**
     * Initializes a new AbstractDealerClient with the specified host and port.
     * @param host - the specified host
     * @param port - the specified port
     */
    public AbstractDealerClient (InetAddress host, int port){
        this.host = host;
        this.port = port;
        this.flag = DEFAULT_FLAG;
    }

    /**
     * Initializes a new AbstractDealerClient with the specified host and port.
     * @param host - the address of the remote host to which to connect
     * @param port - the port on the remote host to which to connect
     * @param flag - the flags which determine which cards to send back
     */
    public AbstractDealerClient(InetAddress host, int port, String flag){
        this.host = host;
        this.port = port;
        this.flag = flag;
    }

    /**
     * Returns the address of the host to which to connect.
     * @return - the address of the host to which to connect
     */
    protected InetAddress getHost() { return host; }

    /**
     * Returns the port on which the remote host is listening.
     * @return - the port on which the remote host is listening
     */
    protected int getPort() { return port; }

    /**
     * Returns the flags that we want to send to the server.
     * @return - the flags to send to the server
     */
    protected String getFlag() { return flag; }

    /**
     * Establishes a TCP connection to the host/port specified when this object was created,
     * reads a continuous stream of random cards from the socket's input stream, and
     * prints that data to the specified output stream.
     * @param out - the stream to which to write the random cards received
     * @throws IOException - if there is an I/O error while receiving the data.
     */
    public abstract void printToStream(PrintStream out) throws IOException, ClassNotFoundException;
}
