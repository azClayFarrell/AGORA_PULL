package server;
import common.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * @author Hannah Young and Clay Farrell
 * @date 10-30-22
 * Description: Class that defines the type of cards that can be returned for a deck of playing
 * cards
 */
public class CardSource {

    /**the number of cards in our deck*/
    private final int SIZE = 52;
    /**the deck of cards*/
    private Card[] deck;
    /**the type of card that is in play currently*/
    private CardType type;
    /**a randomizer*/
    private Random rand;
    /**an arrayList of cards that are in play*/
    private ArrayList<Card> inPlay;
    /**the name of the csv file the card info is listed in*/
    private final String CSV_NAME = "cards-1.csv";

    /**
     * Create a new CardSource object to store and choose cards to send back to client.
     * @throws FileNotFoundException - if the input file cannot be found.
     */
    public CardSource() throws FileNotFoundException {
        this.deck = new Card[SIZE];
        this.type = CardType.UNKNOWN;
        this.rand = new Random();
        this.inPlay = new ArrayList<>();
        populate();
    }

    /**
     * Change the type of card allowed to be sent back to the client.
     * @param type - type of card allowed to be sent via the network
     */
    protected void setCardType(CardType type){
        this.type = type;
    }

    /**
     * Displays the current deck to the screen
     */
    public void displayDeck(){
        // for each card in the deck
        for( Card card : deck ){
            // if the card has not been chosen i.e. is still playable
            if(!card.getChosen()){
                System.out.println(card);
            }
        }
    }

    /**
     * Gets a randomly chosen card to return to the client.
     * @return - a randomly chosen card to return to the client.
     */
    public Card next(){
        boolean valid = false; // exit condition, the card meets the requirements to be chosen
        Card myCard = null;
        while(!valid){
            int index = rand.nextInt(SIZE); // pick a random number :)
            myCard = deck[index]; // set new card to card from the deck at the random index
            // card has not been dealt and the type matches
            if (!myCard.getChosen() && this.type.contains(myCard.getType())){
                valid = true;
                myCard.setChosen(true);
            }
        }
        this.inPlay.add(myCard); // if card has been dealt add it to arraylist
        return myCard;
    }

    /**
     * Helper function to populate the deck with cards
     * */
    private void populate() throws FileNotFoundException{
        //make a file and open a scanner for it
        File cardCSV = new File(CSV_NAME);
        Scanner scan = new Scanner(cardCSV);
        //index of the card to insert
        int count = 0;
        //while we have lines to read
        while(scan.hasNextLine()){
            //read the line and split on the commas
            String line = scan.nextLine();
            String[] info = line.split(",");
            //extract the information into the card fields
            short id = Short.parseShort(info[0]);
            String mark = info[1];
            String type = info[2].toUpperCase();
            String value = info[3];
            //make a card and add it to the deck
            deck[count] = new Card(id, mark, type, value);
            count++;
        }
        //close the file scanner
        scan.close();
    }

    /**
     * Sets all cards to not chosen
     */
    public void resetDeck(){
        // for each in the played deck
        for ( Card card : inPlay ){
            card.setChosen(false); // set to not dealt
        }
        this.inPlay = new ArrayList<>(); // new list so no need to nuke
    }

    /**helper function to print all the cards to see if parsing worked*/
    public void printAll(){
        //the array index
        int count = 0;
        for (Card card: deck){
            System.out.println(count + ": " + card + ": " + card.getChosen());
            count++;
        }
    }

    /**
     * gets a reference to the inPlay ArrayList
     * @return inPlay - the array list holding the cards that have been dealt
     * */
    public ArrayList<Card> getInPlay(){
        return this.inPlay;
    }

    /**
     * Helper function to check if the inPlay array list is working properly
     * @param list - an array list of any Object to be printed
     * */
    public void printList(ArrayList<?> list){
        if (!list.isEmpty()){
            for (Object card : list){
                System.out.println("***" + card + "***");
            }
        }
        else{
            System.out.println("************EMPTY HAND************");
        }

    }

    /**
     * Helper method for tests in main. Randomly generates a type for next()
     * @return newType - the new type of card we are choosing
     * */
    public CardType randomType(){
        //gen a rand number and init a variable to hold the Type
        int index = rand.nextInt(4);
        CardType newType = null;
        //switch to get the type from Type enum
        switch(index){
            case 0:
                newType = CardType.CLUBS;
                break;
            case 1:
                newType = CardType.DIAMONDS;
                break;
            case 2:
                newType = CardType.HEARTS;
                break;
            case 3:
                newType = CardType.SPADES;
                break;
        }
        return newType;
    }


    /**
     * Used for testing data input.
     * @param args - not used
     */
    public static void main(String[] args){
        try{
            CardSource testSource = new CardSource();
            //show the deck and the status of the dealt
            testSource.printAll();
            testSource.displayDeck();
            //pick 10 random cards
            for (int i = 0; i < 10; i++){
                testSource.setCardType(testSource.randomType());
                testSource.next();
            }
            //show the deck again
            testSource.printAll();
            testSource.displayDeck();
            //show just the cards that are dealt
            testSource.printList(testSource.getInPlay());
            //reset the deck
            testSource.resetDeck();
            System.out.println("****************************************");
            //show the hand again
            testSource.printAll();
            testSource.printList(testSource.getInPlay());
        }
        catch(FileNotFoundException fnfe){
            System.err.println("File was not found");
        }
    }
}
