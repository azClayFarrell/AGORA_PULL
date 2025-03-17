package common;
/**
 * @author Clay Farrell and Hannah Young
 * @date 10-26-22
 * A class for making a playing card
 * */

import java.io.Serializable;

/**The card class, represents a playing card*/
public class Card implements Serializable {

    /** the id number of the card*/
    private short id;
    /** the face of the card*/
    private String mark;
    /** the suit of the card*/
    private Type type;
    /**the value of the card*/
    private String value;
    /** boolean representing if a card has been dealt*/
    private boolean dealt;

    /** Constructor for the card class, makes a card instance
     * @param id - the id of the card
     * @param mark - the face of the card
     * @param type - the suit of the card
     * @param value - the value of the card
     * */
    public Card (short id, String mark, Type type, String value){
        this.id = id;
        this.mark = mark;
        this.type = type;
        this.value = value;
        this.dealt = false;
    }

    /** Constructor for the card class, makes a card instance
     * @param id - the id of the card
     * @param mark - the face of the card
     * @param type - the suit of the card
     * @param value - the value of the card
     * */
    public Card(short id, String mark, String type, String value){
        this(id, mark, Type.valueOf(type), value);
    }

    /**
     * gets the id of the card
     * @return the id of the card
     * */
    public short getId() {
        return id;
    }

    /**
     * gets the mark of the card
     * @return the mark of the card
     * */
    public String getMark(){
        return mark;
    }

    /**
     * gets the common.Type of the card
     * @return the type of the card
     * */
    public Type getType(){
        return type;
    }

    /**
     * changes the type of the card
     * @param type - the new type of the card
     * */
    public void setType(Type type){
        this.type = type;
    }

    /**
     * gets if the card is already in play
     * @return boolean saying if the card has been chosen
     * */
    public boolean getChosen(){
        return dealt;
    }

    /**
     * changes the status of the card being in play
     * @param ch - boolean if the card is in play or not
     * */
    public void setChosen(boolean ch){
        dealt = ch;
    }

    /**
     * returns a string representation of the card
     * @return a string representation of the card
     * */
    @Override
    public String toString(){
        String output = String.format("%15s:%12s (%6s)", this.mark, this.type, this.value);
        return output;
    }
}
