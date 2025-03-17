/*
 * @author Clay Farrell
 * @date 9/21/22
 * Client for TCP protocol. Sends request off to server and prints the sayings
 * it receives
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * The client class for the TCP protocol. It will run the client and ask for
 * user input to be sent to the server, handling any input errors that would
 * cause the server to error out and stop taking new connections.
 */
public class WisdomWordsCLTCP {
    /**
     * The main function for the WisdomWordsCLTCP client contains all
     * the logic to make the program function, including error handling and
     * checking input before being sent to the server. Will take user input,
     * parse it and send it to the server, then print out the response.
     *
     * @param args - the command line arguments containing the server IP and
     * port number.
     */
    public static void main(String[] args){
        //check args# validity
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
            //checks if the port is a number then parses if it is
            if(!args[1].matches("[0-9]*")){
                throw new NumberFormatException("ERROR: The port number" +
                        " entered could not be parsed as an integer.");
            }
            int port = Integer.parseInt(args[1]);
            System.out.println("***************");
            //get the number of sayings the client wants
            System.out.println("Enter number of sayings: ");
            String amount = scan.nextLine();
            //the int that will store the number of requests after parsing      
            int sayingsAmt;
            //I did this bit to avoid the server crash from the HLA1
            if (amount.matches("[0-9]*")){                                      
                //if the string matched the regex then parse to be sent
                sayingsAmt = Integer.parseInt(amount);                          
                if (sayingsAmt < 1){                                            
                    System.out.println("Request not large enough, defaulting" + 
                            " to request 1 saying.");                           
                    sayingsAmt = 1;                                             
                }                                                               
            } 
            else{                                                               
                //otherwise throw an error                                      
                throw new NumberFormatException("ERROR: Number of sayings" +    
                        " could not be parsed as an integer.");                 
            }
            System.out.println("***************");
            //set up contact with the server
            Socket socket = new Socket(InetAddress.getByName(args[0]), port);
            InputStreamReader input = new InputStreamReader(
                    socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            PrintWriter write = new PrintWriter(socket.getOutputStream(), true);
            //just in case
            write.flush();
            //now request the number of sayings we want
            write.println(sayingsAmt);
            //prints as many times as there were requested sayings
            for (int index = 0; index < sayingsAmt; index++){
                System.out.println(index + ":\n" + reader.readLine() + "\n");
            }
            System.out.println("***************");
            //requests the favorite of the saying provided
            System.out.println("Enter your favorite: ");
            String favorite = scan.nextLine();
            //checking against the regex again to see if it's valid
            if (favorite.matches("[0-9]*")){
                //parse
                int favIndex = Integer.parseInt(favorite);
                //checking if the index is out of bounds
                if (favIndex >= sayingsAmt){
                    System.err.println("ERROR: Bad Index Given. Defaulting to"
                            + " 0.");
                    //default the favorite to the first of the idecies.
                    favIndex = 0;
                    write.println(favIndex);
                }
                else{
                    write.println(favIndex);
                }
            }
            else{
                //otherwise print an error and default to 0
                System.err.println("ERROR: Input could not be parsed" +
                        " as an integer. Defaulting value to 0.");
                int favIndex = 0;
                write.println(favIndex);
            }
            System.out.println("***************");

            //prints the favorite quote
            System.out.println(reader.readLine());

            System.out.println("***************");
            //close everything down
            write.close();
            reader.close();
            input.close();
            socket.close();
            scan.close();

        }
        catch (NumberFormatException nfe){
            System.err.println(nfe.getMessage());
        }
        catch (IndexOutOfBoundsException ioobe){
            System.err.println("ERROR: Bad Index Given!");
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
