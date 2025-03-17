package server;
/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: The interface to a dealer server.
 */
public interface DealerServer {

    /**
     * Causes the dealer server to listen for requests.
     * @throws DealerServerException -if an error occurs while trying to listen for connections.
     */
    void listen() throws DealerServerException;

}
