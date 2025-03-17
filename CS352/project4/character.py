""""
Author: Clay Farrell
Date: 4/30/22
Class: CS 352
Project 4: Functional Programming

This is the class that is used to create Character objects
"""

class Character:
    """A class for making a named Character that takes damage and heals
    
    Attributes:
        name: the unique name of the character
        max_hp: the max hp the character can heal to
        current_hp: the characters current hp, initially set to the max
    """

    def __init__(self, name, max_hp):
        """Inits all the attributes according to input"""
        self.name = name
        self.max_hp = max_hp
        self.current_hp = max_hp

    def get_hp(self):
        """returns the current hp of the character"""
        return self.current_hp

    def get_max(self):
        """returns the max hp of the character"""
        return self.max_hp

    def get_name(self):
        """returns the name of the character"""
        return self.name

    def __str__(self):
        """returns a string representation of the character"""
        return ("'" + self.name + "'/" + str(self.current_hp) + "/" 
        + str(self.max_hp))