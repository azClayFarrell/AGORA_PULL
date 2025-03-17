package server;

import common.Type;

/**
 * @author Clay Farrell & Hannah Young
 * @date 11/7/2022
 * Description: Enumeration of CardTypes
 */
public enum CardType {
    /** Single card types */
    HEARTS(new Type[] {Type.HEARTS}),
    SPADES(new Type[] {Type.SPADES}),
    DIAMONDS(new Type[] {Type.DIAMONDS}),
    CLUBS(new Type[] {Type.CLUBS}),
    /** All card types */
    ALL(new Type []{Type.CLUBS, Type.DIAMONDS, Type.HEARTS, Type.SPADES}),
    /** Black card */
    BLACK(new Type [] {Type.SPADES, Type.CLUBS}),
    /** Red card */
    RED(new Type [] {Type.HEARTS, Type.DIAMONDS}),
    /** Unknown card */
    UNKNOWN(new Type [] {Type.UNKNOWN});

    /** Type list that holds valid card types*/
    private Type [] validTypes;

    /**
     * Constructor, sets the validTypes list to the list containing Types
     * @param list - the card type list
     */
    private CardType(Type [] list) {
        validTypes = list;
    }

    /**
     * If CardType list contains the type
     * @param other - type of card
     * @return - true if it contains
     */
    public boolean contains(Type other){
        boolean val = false;
        //iterate through the array of Types
        for (Type type : validTypes){
            //if one matches the other type we are looking for, it is a valid card
            if(type == other){
                val = true;
            }
        }
        return val;
    }
}
