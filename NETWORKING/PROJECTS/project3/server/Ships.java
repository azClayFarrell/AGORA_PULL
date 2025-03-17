/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * Ships.java
 * This enum represents information for each ship in the game.
 */

package server;

public enum Ships{
    CARRIER("C", 2),
    BATTLESHIP("B", 3),
    SUBMARINE("S", 3),
    CRUISER("R", 4),
    DESTROYER("D", 5);

    /** Name of the ship */
    private  final String name;
    /** Size of the ship */
    private final int size;

    /**
     * Constructor method
     * @param name Name of the ship
     * @param size Size of the ship
     */
    Ships(String name, int size){
        this.name = name;
        this.size = size;
    }

    /**
     * Returns the name of the ship
     * @return Name of the ship
     */
    public String getName() {return name;}

    /**
     * Returns the size of the ship
     * @return Size of the ship
     */
    public int getSize() {return size;}

}