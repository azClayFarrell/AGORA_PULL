/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * ConnectionAgent.java
 * This class is the class responsible for sending messages to and receiving messages
 * from remote hosts. The class extends the MessageSource class, indicating that it can play
 * the role of the “subject” in an instance of the observer pattern. The class also
 * implements the Runnable interface, indicating that it encapsulates  the logic associated
 * with a Thread.
 */

package common;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * A class for connecting two endpoints of a communication session, receives messages in a child
 * thread. It sends messages from the parent thread via the PrintStream.
 * */
public class ConnectionAgent extends MessageSource implements Runnable{

    /** the socket from the client or server that is passed into the constructor*/
    private final Socket socket;
    /** a scanner that is made from the socket's InputStream*/
    private Scanner in;
    /** a printstream to the socket for the other end of this communication*/
    private PrintStream out;
    /** a thread made from this Connection agent that will only listen for messages*/
    private Thread thread;
    /** a username associated with the player that is associated with this connection agent*/
    private String username;

    /**
     * Constructor for the ConnectionAgent, takes in a socket and with it makes a PrintStream and
     * a Scanner from the InputStream. Also makes a new thread that will accept messages using
     * this ConnectionAgent for the parameter
     * @param socket - the socket that connects us to the opposing socket for this connection
     */
    public ConnectionAgent(Socket socket){
        //get the socket and try to open the streams
        this.socket = socket;
        try{
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintStream(socket.getOutputStream(), true);
            this.username = "";
            out.flush();
            //make and start the thread
            thread = new Thread(this);
            thread.start();
        }
        catch(IOException ioe){
            System.err.println("There was a problem opening the socket streams");
        }
    }

    /**
     * Gets the username associated with the player associated with this ConnectionAgent
     * @return username - the username of the player associated with this ConnectionAgent
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Sets the username associated with the player that is associated with this ConnectionAgent
     * @param username - the new username of the player associated with this ConnectionAgent
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Sends a message through the sockets PrintStream
     * @param message - the message from this end that we need to send to the other end
     */
    public void sendMessage(String message){
        out.println(message);
    }

    /**
     * Returns if the socket is currently still connected or not
     * @return true if the socket is connected, false otherwise
     */
    public boolean isConnected(){return this.socket.isConnected();}

    /**
     * Calls the closeMessageSource method from the super class so that observers can
     * then deregister themselves from listening for messages from this connection agent
     */
    public void close(){
        //notify listeners that this will no longer be sending messages
        super.closeMessageSource();
        //then interrupt the thread running and close the output stream
        thread.interrupt();
        out.close();
        //while the socket isn't closed try to close it.
        while(!socket.isClosed()){
            try{
                System.out.println("Closing socket...");
                socket.close();
            }
            catch(IOException ioe){
                System.err.println("Error while trying to close socket to server. " +
                        "Attempting again");
            }
        }
    }

    /**
     * When an object implementing interface Runnable is used to create a thread, starting the
     * thread causes the object's run method to be called in that separately executing thread.
     * The general contract of the method run is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        /*while we are not interrupted and the scanner has a next line, we notify the
        listeners about the next line*/
        while(!thread.isInterrupted()){
            while(in.hasNextLine()){
                super.notifyReceipt(in.nextLine());
            }
        }
        //close the input once we are interrupted
        in.close();
    }

    /**
     * returns if two connection agent references are equal to one another
     * @param other - the other object (ConnectionAgent) that is to be compared to this CA
     * @return true if the objects are the same, false otherwise
     */
    @Override
    public boolean equals(Object other){
        boolean same = false;
        //if other is also a connection agent, we can cast it and compare the usernames
        if(other instanceof ConnectionAgent){
            ConnectionAgent otherAgent = (ConnectionAgent) other;
            if(username.equals(otherAgent.getUsername())){
                same = true;
            }
        }
        return same;
    }

    /**
     * return a string representation of this connection agent
     *
     * @return the username associated with this ConnectionAgent
     * */
    @Override
    public String toString(){
        return this.username;
    }
}
