/**
 * Authors: Allison Holt and Clay Farrell
 * Date: December 10, 2022
 * Grid.java
 * This class represents the logic for a single board of Battleship.
 */
package server;
import java.util.*;

public class Grid {
    /**a carrier ship*/
    public final static String CARRIER = "C";
    /**a cruiser ship*/
    public final static String CRUISER = "R";
    /**a battleship*/
    public final static String BATTLESHIP = "B";
    /**a submarine*/
    public final static String SUBMARINE = "S";
    /**a destroyer ship*/
    public final static String DESTROYER = "D";
    /**a tile that has been shot at and hit*/
    public final static String HIT = "X";
    /**a tile that has been shot at and missed*/
    public final static String MISS = "O";
    /**an empty tile*/
    public final static String EMPTY = "   ";
    /**the four directions the ships can be facing*/
    private final int DIRECTIONS = 4;
    /**the size of the board*/
    private final int boardSize;
    /**the board*/
    private final Coordinate[][] board;
    /**Holds all the ships currently on the board*/
    private final List<Ships> shipsList;
    /**Holds the positions of all the ships on the board*/
    private final List<List<Coordinate>> shipPositions;

    /**
     * Constructor method
     * @param boardSize Size of the game board
     */
    public Grid(int boardSize){
        this.boardSize = boardSize;
        board = new Coordinate[boardSize][boardSize];
        this.shipPositions = new ArrayList<>();
        this.shipsList = new ArrayList<>();
        initializeGrid();
    }

    /**
     * Initializes board with empty values
     */
    public void initializeGrid(){
        for(int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                this.board[i][j] = new Coordinate(i,j);
            }
        }
    }

    /**
     * Displays the current board and displays ships.
     * @return Formatted string containing the grid and its contents
     */
    public String displayGrid(){
        //Grid header
        String line = "     ";
        for(int i = 0; i < this.boardSize; i++){
            line += i + "   ";
        }
        line += printLine();

        //Inner grid
        for(int i = 0; i < this.boardSize; i++) {
            line += i + "  ";
            for (int j = 0; j < this.boardSize; j++) {
                if(j < (this.boardSize - 1)) {
                    line += "|" + this.board[i][j].getCurrValue();
                }
                else{
                    line += "|" + this.board[i][j].getCurrValue() + "|";
                }
            }
           line += printLine();
        }
        line += "\n";
        return line;
    }

    /**
     * Prints out the current board, but only hits and misses so others don't see ships
     * @return Formatted string containing the grid and its contents
     */
    public String displayHitMiss(){
        //Grid header
        String line = "     ";
        for(int i = 0; i < this.boardSize; i++){
            line += i + "   ";
        }
        line += printLine();

        //Inner grid
        for(int i = 0; i < this.boardSize; i++) {
            line += i + "  ";
            for (int j = 0; j < this.boardSize; j++) {
                String currValue = EMPTY;
                //Only prints hits and misses
                if(this.board[i][j].isGuessed()){
                    currValue = this.board[i][j].getCurrValue();
                }
                    if (j < (this.boardSize - 1)) {
                        line += ("|" + currValue);
                    } else {
                        line += ("|" + currValue + "|");
                    }
            }
            line += printLine();
        }
        line += "\n";
        return line;
    }

    /**
     * Formats a line for the grid display based on grid size
     * @return Formatted line for grid display
     */
    private String printLine() {
        String line = "\n   +";
        for(int i = 0; i < this.boardSize; i++) {
            line += "---+";
        }
        line += "\n";
        return line;
    }

    /**
     * Marks the spot on the grid with the provided flag
     *
     * @param flag Ship name, hit, or miss flag
     * @param row  Row position in grid
     * @param col  Column position in grid
     */
    public void markSpot(String flag, int row, int col){
        flag = flag.toUpperCase();
        switch(flag){
            case CARRIER, BATTLESHIP, CRUISER, SUBMARINE, DESTROYER:
                if(this.board[row][col].isEmpty()) {
                    this.board[row][col].setCurrValue(" " + flag + " ");
                }
                break;
            case HIT, MISS:
                if(!this.board[row][col].isGuessed()) {
                    this.board[row][col].setCurrValue(" " + flag + " ");
                    this.board[row][col].setGuessed(true);
                }
                break;
            default:
                //Otherwise flag is not correct
                System.out.println("Invalid marking!");
        }
    }

    /**
     * Gets number of ships to set based on board size and sets them accordingly.
     */
    public void setShips(){
        /*
         * For a 10 x 10 grid, randomly choose a number between 4-6 (inclusive) and place that many ships.
         * For a 9 x 9 grid, randomly choose a number between 3-5
         * For an 8 x 8 grid, randomly choose a number between 3-5
         * For a 7 x 7 grid, randomly choose a number between 2-3
         * For a 6 x 6 grid, randomly choose a number between 2-3
         * For a 5 x 5 grid, randomly choose a number between 1-2
         */
        Random rand = new Random();
        //Get number of ships to set
        int numShips = 0;
        switch(this.boardSize){
            case 10:
                numShips = rand.nextInt(4, 7);
                break;
            case 9, 8:
                numShips = rand.nextInt(3, 6);
                break;
            case 7, 6:
                numShips = rand.nextInt(2, 4);
                break;
            case 5:
                numShips = rand.nextInt(1, 3);
        }
        //Set the number of ships indicated by the random number
        for(int i = 0; i < numShips; i++){
            oneShip(rand);
        }
        //Prints the ships' information to server so we can easily test game
        printResults();
    }

    /**
     * for a board, prints the ship types and placements of all the ships
     */
    public void printResults() {
        System.out.println("Current board size: " + this.boardSize + "x" + this.boardSize);
        System.out.println("Number of ships: " + shipsList.size());
        System.out.print("------ShipList------");

        for (int i = 0; i < this.shipPositions.size(); i++) {
            System.out.println("\n" + this.shipsList.get(i) + "(" + this.shipsList.get(i).getSize() +
                    ")");
            for (Coordinate c : this.shipPositions.get(i)) {
                System.out.print("\t(" + c.getRow() + ", " + c.getCol() + ")");
            }
        }
        System.out.println("\n");
    }

    /**
     * Sets one random ship on the board
     * @param rand Random number generator
     */
    public void oneShip(Random rand){
        //List of types of ships
        List<Ships> possibleShips = new ArrayList<>(Arrays.asList(Ships.values()));
        int pickShip = rand.nextInt(possibleShips.size());
        //Pick random ship to place on board
        Ships ship = possibleShips.get(pickShip);
        List<Coordinate> coordinates = null;
        List<Coordinate> alreadyCheckedCoord = new ArrayList<>();

        //Check if coordinates do not contain another ship
        while(coordinates == null) {
            //Get random coordinates
            int direction = rand.nextInt(DIRECTIONS); //Up, down, right, left
            int startRow = rand.nextInt(this.boardSize);
            int startCol = rand.nextInt(this.boardSize);
            //System.out.println("Help I cannot find an orientation to put a " + ship.getName());
            //If start coordinate is already in list, do not check again
            if(!alreadyCheckedCoord.contains(this.board[startRow][startCol])) {
                coordinates = placeShips(startRow, startCol, direction, ship);
                alreadyCheckedCoord.add(this.board[startRow][startCol]);
                //If coordinates are not valid
                if(coordinates == null || coordinates.isEmpty()){
                    //Don't check this start coordinate again
                    alreadyCheckedCoord.add(this.board[startRow][startCol]);
                }
            }
        }
        //Mark ship at valid coordinates
        for(Coordinate c : coordinates){
            markSpot(ship.getName(), c.getRow(),c.getCol());
        }
        this.shipsList.add(ship);
        this.shipPositions.add(coordinates);
    }

    /**
     * Checks if a coordinate has already been set with a ship before setting a ship there
     * @param startRow Starting row position
     * @param startCol Starting column position
     * @param direction Direction ship is going
     * @param ship Current ship to be placed
     * @return List of coordinates that ship is placed on, null if coordinates are not valid
     */
    public List<Coordinate> placeShips(int startRow, int startCol, int direction, Ships ship){
        List<Coordinate> coordinates = new ArrayList<>();
        switch(direction){
            case 0:
                //Up (Column stays same but row decreases)
                for(int i = startRow; i > (startRow - ship.getSize()); i--){
                    if(checkValidCoords(i, startCol)) {
                        if (!this.board[i][startCol].isEmpty()) {
                            coordinates.add(null); //Coordinate contains a ship already
                        }
                        else coordinates.add(this.board[i][startCol]); //Does not contain value
                    } else coordinates.add(null); //Index is out of bounds
                }
                break;

            case 1:
                //Down (Column stays same but row increases)
                for(int j = startRow; j < (startRow + ship.getSize()); j++){
                    if(checkValidCoords(j, startCol)) {
                        if (!this.board[j][startCol].isEmpty()) {
                            coordinates.add(null); //Coordinate contains a ship already
                        }
                        else coordinates.add(this.board[j][startCol]);
                    } else coordinates.add(null); //Index is out of bounds
                }
                break;

            case 2:
                //Left (Row stays same but column decreases)
                for(int k = startCol; k > (startCol - ship.getSize()); k--){
                    if(checkValidCoords(startRow, k)) {
                        if (!this.board[startRow][k].isEmpty()) {
                            coordinates.add(null); //Coordinate contains ship already
                        }
                        else coordinates.add(this.board[startRow][k]);
                    } else coordinates.add(null); //Index is out of bounds
                }
                break;

            case 3:
                //Right (Row stays same but column increases)
                for(int l = startCol; l < (startCol + ship.getSize()); l++){
                    if(checkValidCoords(startRow, l)) {
                        if (!this.board[startRow][l].isEmpty()) {
                            coordinates.add(null); //Coordinate contains ship already
                        }
                        else coordinates.add(this.board[startRow][l]);
                    } else coordinates.add(null); //Index is out of bounds
                }
                break;
            default:
                return null; //Direction was not valid
        }
        if(!coordinates.contains(null)) {
            return coordinates; //All coordinates are true
        }
        else return null;
    }

    /**
     * Checks if a coordinate is valid
     * @param row Row number of position
     * @param col Column number of position
     * @return True if both row and column is valid, false if not
     */
    public boolean checkValidCoords(int row, int col){
        boolean validRow = false , validCol = false;
        if(row >= 0 && row < this.boardSize){
            validRow = true;
        }
        if(col >= 0 && col < this.boardSize){
            validCol = true;
        }
        return validRow&&validCol; //Returns false if either is not valid
    }

    /**
     * Fires at the grid and determines if it's a hit or a miss
     * @param row Row number of the position
     * @param col Column number of the position
     */
    public void hitMiss(int row, int col){
        //A player can choose the same coordinates as ones previously chosen.
        String currentValue = this.board[row][col].getCurrValue().trim();
        switch (currentValue) {
            case "C", "B", "S", "R", "D" -> {
                markSpot(HIT, row, col);
                System.out.println("(" + row + ", " + col + ") is a HIT!");
                removeHit(row, col);
            }
            default -> {
                markSpot(MISS, row, col);
                System.out.println("(" + row + ", " + col + ") is a MISS!");
            }
        }
    }

    /**
     * If a coordinate is hit, remove it from position list
     * @param row Row number of the position
     * @param col Column number of the position
     */
    public void removeHit(int row, int col){
        for (List<Coordinate> shipPosition : this.shipPositions) {
            for (int j = 0; j < shipPosition.size(); j++) {
                Coordinate currentPos = shipPosition.get(j);
                //Find hit coordinate in the position list
                if (currentPos.getRow() == row && currentPos.getCol() == col) {
                    Coordinate c = shipPosition.remove(j);
                }
            }
        }
    }

    /**
     * Returns if there is at least one ship still afloat and in play
     *
     * @return true if there is at least one ship in play, false otherwise
     * */
    public boolean hasValidShips(){
        //loop conditions
        boolean hasShips = true;
        int i = 0;
        int j = 0;
        while(hasShips && i < shipPositions.size()){
            while (hasShips && j < shipPositions.get(i).size()){
                //if the ship coordinate has not been hit, escape the loop
                hasShips = shipPositions.get(i).get(j).isGuessed();
                j++;
            }
            i++;
        }
        return !hasShips;
    }

    /**
     * Returns if a coordinate is has been fired upon in the grid.
     *
     * @param row - the row of the coordinate in the grid
     * @param col - the column of the coordinate in the grid
     * @return true if the coordinate is guessed, false otherwise
     */
    public boolean coordinateIsGuessed(int row, int col){
        boolean isGuessed = true;
        if(checkValidCoords(row, col)){
            isGuessed = this.board[row][col].isGuessed();
        }
        return isGuessed;
    }
}
