""""
Author: Clay Farrell
Date: 4/30/22
Class: CS 352
Project 4: Functional Programming

This file holds the logic for the game

The file includes options for creating and viewing the characters, healing and
having damage dealt to the characters, and showing instructions for the user
to read through to understand how the program works.
"""

import character
import combat
import functools
from random import randrange

"""the code for getting the heal method from combat.py"""
HEAL = 1
"""the code for getting the damage method from combat.py"""
DAMAGE = 0


def useage():
    """prints out the options menu for the user"""
    
    print("\n============\n"
          "Options\n"
          "============\n"
          "1: Make characters\n"
          "2: View characters\n"
          "3: Enter damage\n"
          "4: Enter heals\n"
          "5: Random hp stats\n"
          "6: Instructions\n"
          "0: EXIT\n\n")


def instructions():
    """prints out the list of instructions for the user"""

    print("Option 1 asks for a character name and their health.\n"
          "Option 2 lets you see all the characters separated into a list "
          "of alive and dead characters.\nOption 3 will ask how many times you "
          "want to damage, then accept input for the damage numbers.\n\t"
          "You will then need to enter the names of the characters you wish "
          "for this damage to be applied to.\nOption 4 is like option 3, but "
          "instead of damage, you will enter healing.\nOption 5 shows you "
          "what the current health pool is(sum of all current character hp),\n"
          "\tand a list of all the healths that are currently at or above "
          "100.\nOption 6, as you can see, are all the instructions.")


def make_character(char_list: list):
    """Makes a new Character object according to the user input and adds them
           to the list of all characters made
    
    Args:
        char_list: the current list of characters that have been created
    """
    #need to default the health value to something to enter the while loop
    health = True
    name = input("Enter the character name: ")
    while type(health) != int:
        health = input("Enter the character health: ")
        try:
            #if health is parsed into something that is not an int we try again
            health = int(health)
        except:
            print("Error trying to parse the health as an int. Try again.\n")
    #once we get a valid input for the health we can create a character
    char_list.append(character.Character(name, health))


def enter_names():
    """Makes a hit list of all the characters that will be attacked
    
    Returns:
        hit_list: a list of strings that will be cross referenced with the list
            of all character to see if they will be attacked
    """
    #init the victims to a string
    victims = ""
    while type(victims) != int:
        try:
            #while victims is not an int, try to accept input to make it an int
            victims = int(input("Enter the number of victims: "))
        except:
            print("The input was not an integer")
    #once good input is recieved, make the hit list
    hit_list = []
    for i in range(victims):
        #then accept names for how ever many victims were requested
        hit_list.append(input("Enter victim name: "))
    return hit_list



def enter_attacks(action):
    """gets the number of 'attacks' to be made against the list of characters
    
    Args:
        action: string for if the attacks are healing or damaging, for clarity

    Returns:
        attacks: the number of attacks to be made against the characters
    
    """
    attacks = ""
    while type(attacks) != int:
        try:
            attacks = int(input("Enter the number of " + action + " desired: "))
        except:
            print("The number of " + action + " entered was not an integer.")
    return attacks


def damage_amts(attacks, action):
    """makes a list of the damage amounts to be returned
    
    Args:
        attacks: the number of attacks to be added to the list
        action: string for if its a 'heal' or a 'damage'

    Returns:
        amounts: the list of all the damage/heal numbers created
    
    """
    amounts = []
    try:
        #loops through adding to the damage amounts
        for i in range(attacks):
            amounts.append(int(input("Enter " + action + " amounts: ")))
    #return when the exception occurs
    except:
        return amounts
    #or return when the loop has finished
    return amounts


def apply_damage(char_list, damages, people, code):
    """Applies healing or damage to the list of people depending on the code
    
    Args:
        damages: the list of damages or heals to be applied
        people: the list of people the damage or heals will apply to
        code: 0 if damage method is called, 1 if heal method is called    
    """
    #Captures a function definition as a return type from a method call.
    augment = combat.get_augment_type(code)

    #rolls to possibly double a value in the damages array
    for index in range(len(damages)):
        if randrange(20) == 0:
            #print("CRIT!!")
            #here is the code for double being used on a critical hit or heal
            damages[index] = combat.double(damages[index])

    #for getting each of the characters
    for i in range(len(char_list)):
        #for checking each of the provided names
        for j in range(len(people)):
            #if one of the names matches
            if char_list[i].get_name() == people[j]:
                #then iterate through each of the damage instances and apply em.
                for k in range(len(damages)):
                    augment(char_list[i], damages[k])


def get_stats(hp_list):
    """LAMBDA/FILTER/REDUCE: random stats about the health of the characters
    
    Args:
        hp_list: the list of hp's that the characters have, currently
    
    """
    #Here is a usage of FILTER and a LAMBDA
    print("LIST OF HP OVER 100:", list(filter(lambda hp: hp> 99, hp_list)))
    #Here is a usage of REDUCE and another LAMBDA
    print("HP POOL: ", functools.reduce(lambda prev, next: prev + next, hp_list, 0))
    

def for_each(action, list):
    """Applies the action to each element in the list provided
    
    Args:
        action: a function definition
        list: an iterable
    """
    for i in range(len(list)):
        action(list[i])


def get_health(char_list):
    """Extracts the health of each character in the list of characters
    
    Args:
        char_list: the list of characters

    Returns:
        health_list: the list of current health totals for each characters
    """
    health_list = []
    for i in range(len(char_list)):
        health_list.append(char_list[i].get_hp())
    return health_list


def are_alive(hp_list, char_list):
    """Cross references the character health totals and stores the alive
    characters in a list to be reutred
    
    Args:
        hp_list: the list of current health for each character
        char_list: the list of characters, living and dead
        
    Returns:
        alive: the list of characters that were determined to be alive
    """
    alive = []
    for i in range(len(char_list)):
        if hp_list[i] > 0:
            alive.append(char_list[i])
    return alive


def are_dead(hp_list, char_list):
    """Cross references the hp_list to determine which characters are dead
    
    Args:
        hp_list: the list of health totals for each character
        char_list: the list of characters, living and dead

    Returns:
        dead: the list of characters that were determined to be dead
    """
    dead = []
    for i in range(len(char_list)):
        if hp_list[i] < 1:
            dead.append(char_list[i])
    return dead


def how_many(count:list, index, hp_list:list):
    """RECURSION: sums up the number of alive and dead characters
    
    Args:
        count: a list with the totals of the alive and dead characters
        index: the index we are on for the hp_list
        hp_list: the list of all the hp_totals for all the characters

    Returns:
        count: the final totals for the alive and dead characters
    
    """
    #if we are not at the end of the list
    if index < len(hp_list):
        #check the hp at the index and and increment alive or dead
        if hp_list[index] > 0:
            count[1] = count[1] +  1
        else:
            count[0] += count[0] + 1
        #increment the index
        index = index + 1
        #Recursive Call, captures the final count on the way up from recurrsion
        count = how_many(count, index, hp_list)
    else:
        #base case returns
        return count
    #returns the final count
    return count


def view_all(char_list: list):
    """CLOSURE: gets the list of alive and dead characters and displays them
    to screen. CLOSURE is used to display the alive and dead lists when
    dead_alive() is called, closes around the dead and alive lists
    
    Args:
        char_list: the list of characters that is to be displayed

    """
    #extract the list of current hp for each character
    hp_list = get_health(char_list)
    #extract the alive characters
    alive = are_alive(hp_list, char_list)
    #extract the dead characters
    dead = are_dead(hp_list, char_list)
    #gets how many characters in each category of alive and dead there are
    count = how_many([0,0], 0, hp_list)

    #here is the method that requires closure for the lists
    def dead_alive():
        """CLOSURE: uses for_each to call print on all the elements of the
            lists for dead and alive"""
        nonlocal alive
        nonlocal dead
        nonlocal count
        print("\n")
        #prints the living characters
        print(count[1], "ALIVE FELLOWS:")
        if len(alive) > 0:
            #here is a funciton call with another function as a parameter?
            for_each(print, alive)
        print("")
        #prints the dead characters
        print(count[0], "DEAD FELLOWS:\n")
        if len(dead) > 0:
            #here is another instance of a function using function def as param
            for_each(print, dead)
    dead_alive()
    

def go():
    """contains all the options for how to navigate the program logic"""
    #makes sure that the global values are used
    global DAMAGE
    global HEAL
    #this will be the list of characters
    npc_list = []
    #allows for entry into the while loop
    option = 100
    #while there was not a request to exit
    while(option != 0):
        #prints the options menu then tries to parse the next input
        useage()
        try:
            option = int(input("Enter option number to continue>>> "))
            if option == 1:
                make_character(npc_list)
            elif option == 2:
                view_all(npc_list)
            #does the damage attack on players
            elif option == 3:
                attacks = enter_attacks("attacks")
                damages = damage_amts(attacks, "attacks")
                apply_damage(npc_list, damages, enter_names(), DAMAGE)
            #does the heal actions on players
            elif option == 4:
                heals = enter_attacks("heals")
                heal_list = damage_amts(heals, "heals")
                apply_damage(npc_list, heal_list, enter_names(), HEAL)
            #does the goofy stats using the characters
            elif option == 5:
                hp_list = get_health(npc_list)
                get_stats(hp_list)
            #prints the instructions
            elif option == 6:
                instructions()
            #shows the option isn't supported
            elif option != 0:
                print("Option not supported")
        #catching errors
        except ValueError:
            print("ValueError occured, likely while parsing.")
        except TypeError:
            print("A TypeError occured, operation not supported for type. Probably")
        except:
            print("Unexpected error occured")
    #printing a successful run was had
    print("Game over. Exiting successfully...")
