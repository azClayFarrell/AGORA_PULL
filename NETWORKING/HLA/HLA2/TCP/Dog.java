/**
 * @author Clay Farrell
 * @version 10-4-2022
 * small class for a Dog that has a name and a breed
 * */

import java.io.Serializable;

/**Class for making a dog object. Dog will have a name and breed*/
public class Dog implements Serializable {

    /** the name of the Dog*/
    private String name;
    /** the breed of the Dog*/
    private String breed;

    /**Default constructor that will make Clifford the big red dog*/
    public Dog(){
        name = "Clifford";
        breed = "Big Red Dog";
    }

    /**Constructor that will take 2 strings for the name and breed of the
     * dog and make a Dog object from them
     *
     * @param name - the name of the dog
     * @param breed - the breed of the dog
     * */
    public Dog(String name, String breed){
        this.name = name;
        this.breed = breed;
    }

    /**
     * A toString method that will return the name and breed of the dog
     *
     * @return result - a string containing the name and breed of the dog*/
    @Override
    public String toString(){
        String result = name + " is a " + breed + ".\n";
        return result;
    }

}
