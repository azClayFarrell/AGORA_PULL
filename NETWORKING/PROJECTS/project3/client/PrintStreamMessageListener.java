/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * PrintStreamMessageListener.java
 * This class is a class that is responsible for writing messages to a
 * PrintStream (e.g., System.out). The class implements the MessageListener interface, indicating
 * that it plays the role of “observer”  in an instance of the observer pattern.
 */

package client;
import common.MessageListener;
import common.MessageSource;
import java.io.PrintStream;

public class PrintStreamMessageListener implements MessageListener {
    /** Print stream to send messages */
    private PrintStream out;

    public PrintStreamMessageListener(PrintStream out){
       this.out = out;
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        out.println(message);
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }
}
