package server;

import java.io.*;

/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: This class contains an application that can drive both the TCP and UDP
 * implementations of a DealerServer.
 */

public class DealerServerDriver {

    /**
     * This method serves as the entry point of the program.
     * @param args - Command line arguments to the program. There must be exactly 0 or 1 arguments.
     * The second parameter, if present, must be the port number on which the server
     * will listen for requests.
     */
    public static void main(String[] args) {
        // too many command line arguments
        if(args.length > 2 || args.length < 1){
            System.err.println("Usage: java DealerServerDriver <tcp|udp> [port]");
            System.exit(1);
        }

        int port = AbstractDealerServer.DEFAULT_PORT;
        String protocol = args[0]; // set the protocol to the first command line arg

        if(args.length == 2){
            try{
                // set the port to the second command line arg, try to parse as int
                port = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException nfe){
                System.err.println("Error: invalid number input. Provided: " + args[1]);
                System.exit(2);
            }
        }
        try{
            if(protocol.toLowerCase().equals("tcp")){
                TcpDealerServer myTcpServer = new TcpDealerServer(port);
                myTcpServer.listen();
            }
            else if (protocol.toLowerCase().equals("udp")){
                UdpDealerServer myUdpServer = new UdpDealerServer(port);
                myUdpServer.listen();
            }
        }catch (FileNotFoundException fnfe){
            System.err.println("ERROR: file not found for making the cards");
        } catch (DealerServerException e) {
            System.err.println("ERROR: Something went wrong with the server");
        }
    }
}
