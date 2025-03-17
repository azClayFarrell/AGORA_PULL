import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HitchHikerClientTCP {
    public static void main(String[] args){
        if (args.length != 2){
            System.err.println("ERROR: Wrong number of command " +
                    "line arguments!");
            System.out.println("Usage: java HitchHikerClientTCP" +
                    " <server name(textual)> <port>");
            System.exit(1);
        }
        Scanner scan = new Scanner(System.in);
        System.out.println("Connecting to server: \"" + args[0] + "\" using " +
                "port: " + args[1] + "...");

        try{
            int port = Integer.parseInt(args[1]);
            Socket socket = new Socket(InetAddress.getByName(args[0]), port);
            InputStreamReader input = new InputStreamReader(
                    socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            PrintWriter write = new PrintWriter(socket.getOutputStream(), true);
            //just in case
            write.flush();

            System.out.print("Enter your age: ");
            int age = scan.nextInt();
            write.println(age); 
            System.out.println("Your age is... " + reader.readLine());

            write.close();
            reader.close();
            input.close();
            socket.close();
            scan.close();

        }
        catch (NumberFormatException nfe){
            System.err.println("The port number supplied was not an integer");
        }
        catch (InputMismatchException imme){
            System.err.println("The age entered was not an integer");
        }
        catch (IOException ioe){
            System.err.println("Something went wrong with the client :(");
            System.err.println(ioe.getMessage());
        }
        finally{
            scan.close();
        }
    }
}
