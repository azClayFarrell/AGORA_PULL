/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * Coordinate.java
 * This class represents a single coordinate on a grid in a game of battleship
 */

package server;

public class Coordinate {
    /**Row and column of coordinate*/
    private final int row;
    private final int col;
    /**Either is empty, contains a ship, or is a hit/miss*/
    private String currValue;
    /**If a user has guessed this spot yet*/
    private boolean guessed;

    /**
     * Constructor method
     * @param row Row position of coordinate
     * @param col Column position of coordinate
     */
    public Coordinate(int row, int col){
        this.row = row;
        this.col = col;
        this.currValue = Grid.EMPTY;
        this.guessed = false;
    }

    /**
     * Returns the row position of the coordinate
     * @return Row position of the coordinate
     */
    public int getRow(){return this.row;}

    /**
     * Returns the column position of the coordinate
     * @return Column position of the coordinate
     */
    public int getCol(){return this.col;}

    /**
     * Sets the current value of the coordinate
     * @param flag Flag to set the current value of the coordinate
     */
    public void setCurrValue(String flag){this.currValue = flag;}

    /**
     * Gets the current value of the coordinate
     * @return Current value of the coordinate
     */
    public String getCurrValue(){return this.currValue;}

    /**
     * Checks if the coordinate is still empty
     * @return True is coordinate is empty, false if not
     */
    public boolean isEmpty(){
        return this.currValue.equals(Grid.EMPTY);
    }

    /**
     * Returns if someone has guessed this coordinate already
     * @return True if already guessed, false if not
     */
    public boolean isGuessed(){return this.guessed;}

    /**
     * Sets if a coordinate has been guessed or not
     * @param guessed True if guessing coordinate, false if resetting coordinate
     */
    public void setGuessed(boolean guessed){this.guessed = guessed;}

}
