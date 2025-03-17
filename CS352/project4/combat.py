""""
Author: Clay Farrell
Date: 4/30/22
Class: CS 352
Project 4: Functional Programming

This file has the logic for combat, such as healing and damaging characters
"""

def damage(player, amt):
    """Deals damage to the player if the player is conscious
    
    Args:
        player: the character that is to take damage
        amt: the amount of damage the player will take
    """
    if player.current_hp > 0:
        player.current_hp = player.current_hp - amt


def heal(player, amt):
    """Heals the player up to their own max_hp
    
    Args:
        player: the player that is to be healed
        amt: the amount the player is to be healed for
    """
    if player.current_hp + amt >= player.max_hp:
        player.current_hp = player.max_hp

    else:
        player.current_hp = player.current_hp + amt
        

def double(base):
    """PURE FUNCTION: Doubles the amount of damage/heal that is dealt
    
    Args:
        base: the base amount of healing or damage that will be doubled
    Returns:
        the base amount, doubled
    """
    return base * 2


#def augment_hp(action, player, amt):
#    """HIGHER ORDER: carries out the health augmentation depending on the
#    function that is passed in (heal/damage)
#    
#    Args:
#        action: the function that is to be carried out for the player health
#        player: the character that is to have their health augmented
#        amt: the amount the character is to have their health augmented by
#    """
#    action(player, amt)


def get_augment_type(code):
    """RETURN FUNC DEF: Returns the method for damage or the method for heal
    
    Args:
        code: the code of the function desired from this file

    Returns:
        either the function def for damage or the function def for heal
    
    """
    if code == 0:
        return damage
    elif code == 1:
        return heal