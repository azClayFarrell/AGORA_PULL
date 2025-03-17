/**
 * @author Clay Farrell
 * @date 9/21/22
 * Small class for storing words from a file
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * This is the wisdom words class that will take a file and store the text
 * contents within in an array for the server to send to the client
 * upon request
 */
public class WisdomWords {
    /**the initial size of the array of words of wisdom*/
    private final int INIT_ARRAY = 5;
    /**the array list storage for the strings in the given file*/
    ArrayList<String> words;

    /**
     * Constructor. Instantiates the ArrayList and starts the loading process
     *
     * @param file - the file that the words will be loaded in from
     * @throws FileNotFoundException if the file does not exist
     */
    public WisdomWords(String file) throws FileNotFoundException {
        //stuff
        words = new ArrayList<>(INIT_ARRAY);
        readSayings(file);
    }

    /**
     * Loads file of wise sayings into string array
     *
     * @param file - the file that the words will be loaded in from
     * @throws FileNotFoundException if the file does not exist
     */
    public void readSayings(String file) throws FileNotFoundException {
        File wisdomText = new File(file);
        Scanner scan = new Scanner(wisdomText);
        while(scan.hasNextLine()){
            words.add(scan.nextLine());
        }
        scan.close();
    }

    /**
     * Returns the wises saying at the index
     *
     * @param index - the index of the desired saying
     * @reutrn the saying at the index
     */
    public String getWisdom(int index) {
        return words.get(index);
    }

    /**
     * Returns the size of the ArrayList
     *
     * @return the number of elements in the array
     */
    public int getSize() {
        return words.size();
    }

}
