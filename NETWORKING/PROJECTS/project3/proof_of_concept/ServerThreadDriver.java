package proof_of_concept;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerThreadDriver {
    private static final int DEFAULT_PORT = 5500;

    private static final int TIMEOUT = 5000;

    private static final String serverMSG = "This is from the server\n";



    public static void main(String [] args){

        int port = DEFAULT_PORT;

        try{

            ServerSocket socket = new ServerSocket(port);

            while(!socket.isClosed()){
                Socket incoming = socket.accept();
                SocketThread myServerThread = new SocketThread(incoming);
                myServerThread.sendMessage(serverMSG);

            }
        }
        catch(UnknownHostException uhe){
            System.err.println("The host could not be found");
        }
        catch(IOException ioe){
            System.err.println("Problem connecting to server");
        }



    }
}
