package client;

import server.DealerServerException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: This class contains an application that can drive both the TCP and UDP
 * implementations of a DealerClient.
 */

public class DealerClientDriver {

    /**
     * Provides the entry point of the program.
     * Usage:
     * java DealerClientDriver <tcp|udp> <host> [port] [flag]
     * @param args - command line arguments
     */
    public static void main (String[] args) {
        int port = AbstractDealerClient.DEFAULT_PORT;
        String flag = AbstractDealerClient.DEFAULT_FLAG;

        try{
            InetAddress address = InetAddress.getByName(args[1]);

            // check the amount of command line args given
            if (args.length == 3){
                // if the second arg is not a number, parse to int
                if (!args[2].matches("[^0-9]+")) {
                    port = Integer.parseInt(args[2]);
                } else {
                    // the second arg is the flag, & set the the port to the default
                    flag = args[2].toUpperCase();
                }
            }
            // check the amnt command line args
            else if (args.length == 4){
                port = Integer.parseInt(args[2]);
                flag = args[3].toUpperCase();
            }

            //if tcp do this to make a tcpClient object
            if(args[0].toLowerCase().equals("tcp")){
                DealerTcpClient myTCP = new DealerTcpClient(address, port, flag);
                myTCP.printToStream(System.out);

            //if udp do this to make a udpClient object
            }else if(args[0].toLowerCase().equals("udp")){
                DealerUdpClient mrUDP = new DealerUdpClient(address, port, flag);
                mrUDP.printToStream(System.out);
            }else{
                System.err.println("Usage: " +
                        "java DealerClientDriver <tcp|udp> <host> [port] [flag]");
                System.exit(1);
            }
        }catch (UnknownHostException UHE){
            System.err.println("Error: host name not findable. Provided: " + args[1]);
            System.err.println(UHE.getMessage());
        }catch (NumberFormatException nfe){
            System.err.println("Error: invalid number input. Provided: " + args[2]);
        }catch (IOException ioe) {
            System.err.println("Error: Something went wrong with the client.");
        }catch (ClassNotFoundException cnfe){
            System.err.println(cnfe.getMessage());
        }

    }
}
