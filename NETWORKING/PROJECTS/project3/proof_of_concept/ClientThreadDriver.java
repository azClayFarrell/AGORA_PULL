package proof_of_concept;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThreadDriver {
    static final int DEFAULT_PORT = 5500;

    private static final int TIMEOUT = 5000;

    private static final String clientMSG = "This is from the client\n";

    public static void main(String [] args){

        int port = DEFAULT_PORT;

        try{
            InetAddress address = InetAddress.getByName(args[0]);

            Socket socket = new Socket(address, port);

            SocketThread socketThread = new SocketThread(socket);
            for (int i = 0; i < 1000; i++){
                for (int j = 0; j < 1000; j++){
                    //do nothing to waste time to give the server time to send a
                    //message and start the thread back up
                }
            }
            socketThread.sendMessage(clientMSG);
        }
        catch(UnknownHostException uhe){
            System.err.println("The host could not be found");
        }
        catch(IOException ioe){
            System.err.println("Problem connecting to server");
        }



    }
}
