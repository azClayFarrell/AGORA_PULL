package proof_of_concept;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketThread implements Runnable{

    private final String clientInterruptMSG = "This printed after an interrupt:client\n";

    private final String serverInterruptMSG = "This printed after an interrupt:server\n";
    //the socket
    private Socket socket;
    //scanner made from the socket input stream
    private Scanner in;
    //the output stream gotten from the socket
    private PrintStream out;
    //thread made from this SocketThread instance
    private Thread thread;



    public SocketThread(Socket socket){
        this.socket = socket;
        try{
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintStream(socket.getOutputStream(), true);
            thread = new Thread(this);
            thread.start();
        }
        catch(IOException ioe){
            System.err.println("Problem opening the io streams");
        }
    }

    public void sendMessage(String msg){
        thread.interrupt();
        try{
            out.write(msg.getBytes());
        }
        catch(IOException ioe){
            System.err.println("Message could not send");
        }
        thread = new Thread(this);
        thread.start();
    }
    public void close(){
        //not needed for this practical
    }

    public boolean isConnected(){
        return true;
    }

    @Override
    public void run(){

        while(!thread.isInterrupted()){
            while(in.hasNextLine()){
                String msg = in.nextLine();
                //todo this is where notify would be called --> notify(msg)
                //but for now i'm just printing it so that we can see msg arrived
                System.out.println(msg);
            }
        }
    }

}
