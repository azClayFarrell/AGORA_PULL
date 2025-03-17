package common;
/**
 * Clay Farrell and Hannah Young
 * 10-26-22
 * Enumeration for the Types (faces) of the cards
 * */

/**The actual enumeration*/
public enum Type {

    /** constant or the Clubs card*/
    CLUBS("Clubs"),
    /** constant for the Diamonds card*/
    DIAMONDS("Diamonds"),
    /** constant for the Hearts card*/
    HEARTS("Hearts"),
    /** constant for the Spades card*/
    SPADES("Spades"),
    /** constant for Unknown card types*/
    UNKNOWN("Unknown");

    /** The string that represents the name of the cart common.Type*/
    private String name;

    /** Enumerated object constructor for the Types
     * @param name_ - the string representation of the name of the card type*/
    private Type(String name_){
        this.name = name_;
    }

    /** method that returns the string representation of the card type*/
    @Override
    public String toString() {
        return this.name;
    }
}
