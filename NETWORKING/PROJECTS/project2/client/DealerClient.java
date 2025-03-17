package client;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: The interface to a dealer client component.
 */

public interface DealerClient {

    /**
     * Establishes a TCP connection to the host/port specified when this object was created,
     * reads a continuous stream of random cards from the socket's input stream, and
     * prints that data to the specified output stream.
     * @param out - the stream to which to write the random cards received.
     * @throws IOException - if there is an I/O error while receiving the data.
     */
    void printToStream(PrintStream out) throws IOException, ClassNotFoundException;

}
