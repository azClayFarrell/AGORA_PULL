/**
 * @author Clay Farrell
 * @author Allison Holt
 * @date 11-20-22
 * A player class for the first milestone of the battleship game
 */

package server.milestone;

import server.Grid;

/**the player class has a battle grid and a username*/
public class Player {

    /**the players grid*/
    private Grid myGrid;
    /**the username of the player*/
    private String userName;

    /**
     * The player constructor
     *
     * @param userName - the players username
     * @param boardSize - the size of the grid the players will be using
     * */
    public Player(String userName, int boardSize){
        this.userName = userName;
        this.myGrid = new Grid(boardSize);
        this.myGrid.setShips();
    }

    /**
     * gets the players board
     *
     * @return myGrid - the players board
     * */
    public Grid getBoard(){
        return this.myGrid;
    }

    /**
     * returns the players username
     *
     * @return userName - the players userName
     * */
    public String getUserName(){
        return this.userName;
    }

    /**
     * equals method for the players, used for .contains() when in a List
     *
     * @param other - an object for comparison
     * @return a boolean indicating if this player is equal to another Object that is passed in
     * */
    @Override
    public boolean equals(Object other){
        boolean isEqual = false;
        if(other instanceof Player){
            Player otherPlayer = (Player) other;
            if (this.userName.equals(otherPlayer.getUserName())){
                isEqual = true;
            }
        }
        return isEqual;
    }

    /**
     * returns a string representation of this player
     *
     * @return userName - the username of this player
     * */
    @Override
    public String toString(){
        return this.userName;
    }
}
