/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * BattleClient.java
 * This class is one of the classes that implement the client-side logic of this client-server
 * application. It is responsible for creating a ConnectionAgent, reading input from the user,
 * and sending that input to the server via the ConnectionAgent. The class implements the
 * MessageListener interface (i.e., it can “observe” objects that are MessageSources). The
 * class also extends MessageSource, indicating that it also plays the role of “subject” in an
 * instance of the observer pattern.
 */

package client;
import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class BattleClient extends MessageSource implements MessageListener {
    
    /** the host address for the server that is hosting the battleship game*/
    private final InetAddress host;
    /** the port through which we need to connect to the server*/
    private final int port;
    /** the username the player wants to be known by during the game*/
    private final String username;
    /** the ConnectionAgent that is made to send and receive messages to and from the server*/
    private ConnectionAgent agent;
    /** bool for if the username of the player has been determined by the server to be valid*/
    private boolean validUsername;
    /** the message that is to be received from the server when the username
     * of the player was valid to use*/
    private final String usernameConfirmationMSG = "Valid username received! Press enter to continue.";

    /**
     * Constructor for the BattleClient. Takes an InetAddress for the host, int for the
     * port, and a username String to make a ConnectionAgent to connect to and play battleship
     * on a server
     * 
     * @param host - an InetAddress for the host server
     * @param port - in int for the port number to connect to the server with
     * @param username - the username the player wants to use on the server
     */
    public BattleClient(InetAddress host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.validUsername = false;
    }

    /**
     * Sets if the username of the client is valid or not
     * @param valid True if valid, false if not
     */
    protected void setValidUsername(boolean valid){
        this.validUsername = valid;
    }

    /**
     * Checks if the username is valid or not
     * @return True if username is valid, false if not
     */
    protected boolean isValidUsername(){return this.validUsername;}

    /**
     * Attempts to make a ConnectionAgent object using a socket made from the host and
     * port initialized in the constructor
     * @throws IOException If connection cannot be done
     */
    public void connect() throws IOException{
        try {
            this.agent = new ConnectionAgent(new Socket(this.host, this.port));
            agent.addMessageListener(this);
        }catch(IOException ioe){
            System.err.println("Error: server game already in progress, connection refused.");
            //throw new exception so that the driver will exit
            throw new IOException("");
        }
    }

    /**
     * Returns if this connection agent is still receiving messages
     * @return True if the socket for the connection agent is open, false otherwise
     */
    public boolean agentIsConnected(){
        return this.agent.isConnected();
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        //source is not needed really, as far as I can tell, just notify observers
        super.notifyReceipt(message);
        //if the message that was received was confirmation of valid username then validate it
        if(message.equals(usernameConfirmationMSG)){
            setValidUsername(true);
        }
        //If client receives the game over message, automatically surrender to stop the game
        if(message.split("\s")[0].equals("Game") && message.split("\s")[1].equals("Over!")){
            send("/surrender");
            super.notifyReceipt("Closing the connection to the server...");
        }
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        /*BattleClient removes itself from the CA's observer list then tells its own observer
          list to deregister themselves from BattleClients observer list*/
        source.removeMessageListener(this);
        super.closeMessageSource();
    }

    /**
     * Sends the message received from the Driver to the ConnectionAgent so that it can
     * be sent to the server. Gets the message cleaned up a bit before
     * @param message - the message from the Driver to be sent to the server
     */
    public void send(String message){
        //split message on the whitespace and makes the message blank to reconstruct it
        String [] parts = message.split("\s+");
        message = "";
        //strips off any potential whitespace left over after the split then appends the part
        for (String part : parts){
            part = part.strip();
            message += part + " ";
        }
        //strips off that last space that was added at the end of the for each loop then sends msg
        message = message.strip();
        agent.sendMessage(message);
        //if the command being sent is /battle, go ahead and assign the username to the connection agent
        parts = message.split("\s+");
        if (parts[0].equals("/battle")){
            this.agent.setUsername(parts[1]);
        }
        if(parts[0].equals("/surrender")){
            agent.close();
        }
    }
}
