/**
 * @author Clay Farrell and Allison Holt
 * @date 11-19-22
 * driver for the milestone one battleship game
 */

package server.milestone;
import java.util.ArrayList;
import java.util.Scanner;

/** The driver for a game of battleship, sets everything up then passes
 * off the work to the game object*/
public class MileStoneOneDriver {

    /** the default size for a board for the game (can be 5 - 10 inclusive)*/
    private static final int DEFAULT_BOARD_SIZE = 5;
    /** the minimum players required for starting a game of battleship*/
    private static final int MIN_PLAYERS = 2;

    /** the main method of the driver, gathers all the player and game information before
     * passing the logic load along to the game object
     *
     * @param args - command line arguments (unused in this instance)
     * */
    public static void main(String [] args){
        //list to store the names and scanner to take input
        ArrayList<String> names = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        //boolean for accepting input and the number that will be taken
        boolean goodNum = false;
        int num = -1;
        //while we have not had good input, ask for input and take input
        while (!goodNum){
            System.out.println("Enter the amount of players you want to add> ");
            String strNum = scan.nextLine();
            //if the string matches only numbers
            if (strNum.matches("[0-9]+")){
                //make it a number and update the escape condition
                num = Integer.parseInt(strNum);
                if (num >= MIN_PLAYERS){
                    goodNum = true;
                }
            }
            else{
                //otherwise say it was an invalid number
                System.out.println("Not a valid number, enter a number");
            }
        }
        //keep track of the players added
        int addedPlayers = 0;
        while (addedPlayers < num){
            //ask for a new user and scan in the input
            System.out.println("Enter a unique player name");
            String newPlayer = scan.nextLine();
            newPlayer = newPlayer.strip();
            //if the player name is not in the list then add it and increment
            if(!names.contains(newPlayer)){
                names.add(newPlayer);
                addedPlayers++;
            }
        }

        /*-----------------------------------------------------------------*/
        //make the game instance
        MileStoneOneGame myGame = new MileStoneOneGame(DEFAULT_BOARD_SIZE);
        //add all the players to the game
        for (String name: names){
            myGame.messageReceiver("/battle " + name);
        }

        String gameStart = "";
        while(!gameStart.equals("/start") || !myGame.canStart()){
            //wait for someone to call start and see if we can start the game
            System.out.println("Waiting for /start flag to be sent to start the game...");
            gameStart = scan.nextLine();
            if (gameStart.matches("/fire*")){
                System.out.println("/fire <[0-9]+> <[0-9]+> <username> cannot be executed yet.");
            }
        }
        myGame.messageReceiver(gameStart);
    }
}
