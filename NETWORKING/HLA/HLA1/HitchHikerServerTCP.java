import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HitchHikerServerTCP {
    public static void main(String[] args){
        try{
            ServerSocket servSock = new ServerSocket(5500);
            while(!servSock.isClosed()){
                Socket accepted = servSock.accept();
                InputStreamReader input = new InputStreamReader(
                        accepted.getInputStream());
                BufferedReader read = new BufferedReader(input);
                PrintWriter write = new PrintWriter(accepted.getOutputStream(),
                        true);
                //just in case
                write.flush();

                String ageString = read.readLine();
                int age = Integer.parseInt(ageString);
                age += 42;
                write.println(age);

                //closing streams and listening Socket
                write.close();
                read.close();
                input.close();
                accepted.close();
            }
            //close server socket
            servSock.close();

        }
        catch (NumberFormatException nfe){
            System.err.println("The number received from the client could not" +
                    " be parsed as an integer");
        }
        catch (IOException ioe){
            System.err.println("There was a problem with the server x_x");
            System.err.println(ioe.getMessage());
        }
    }
}
