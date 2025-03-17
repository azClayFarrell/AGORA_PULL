Author: Clay Farrell
Date of Submission: 4/30/22
Class: CS 352
Assignment: Project 4 - RPG Character Builder

INSTRUCTIONS:

To run the program,navigate to the directory that contains the driver.py file
    and while in that directory type the following:

    python3 driver.py


DESCRIPTION:

    On program launch you will be met with an Options menu. Typing a number
    on the list and hitting enter will navigate you to the selected item. The
    first thing you may want to do is type 6 into the prompt to read through the
    instructions. After reading, you are able to add characters to the list of
    all the characters you have created since you began the program. After, you
    can view their stats, deal damage, heal them, and view different odd things
    about the health totals such as a list of those currently over 100 health
    and the total amount of health over all the characters.


OCCURANCES OF REQUIREMENTS:
    
    * Recursion: in game_logic.py, the how_many() method. Line 266.

    * Pure function: in combat.py, the double() method. Used in
        game_logic.py in the apply_damage() method on line 157.

    * Function def calling another function def with one function def as an
        argument: Does my for_each count when I use print on the characters?
        Line 304 and 310.

    * Return a function definition you wrote as the return value: in 
        game_logic.py line 150, the method get_augment_type() is called from
        combat.py that returns the definition for damage or heal.

    * Lambda usage: in game_logic.py, the get_stats() method. Lines 178 and 180.

    * Closure: in game_logic.py, the dead_alive() method within the view_all()
        method. Uses nonlocal lists within the containing method. Lines 293-311.

    * MAP: UNIMPLEMENTED

    * FILTER: in game_logic.py, the get_stats() method. Line 178

    * REDUCE: in game_logic.py, the get_stats() method. Line 180

    * List comprehension: UNIMPLEMENTED



POSSIBLE PROBLEMS:

    There is a coding practice mistake for when I am accessing the fields of
    the Character class without doing it through a getter method. I don't have
    time to revisit this even though it is a serious oversight and an easy fix.
    
