Authors: Allison Holt and Clay Farrell
Date: December 10, 2022
Project 3: Battleship(Multiuser game)

This program is a game of battleship that is able to be multiplayer using TCP communication and
threading. It has a game server that many game clients can connect to in order to play together.

To compile the project:
    From within the project3 directory enter:
        "javac client/*.java server/*.java common/*.java"

Usage: After compiling all the files of the three packages, use the following commands to run the
game:

    For the server, use:
        "java server.BattleShipDriver <portNumber> [boardSize]"

    For the client, use:
        "java client.BattleDriver <hostname> <portNum>"

Known bug:
    When a player attempts to join the server while a game is currently
    in progress, they are able to make it onto the server. The request
    is not ignored like I thought I coded it to be and I cannot see, for
    the life of me, how they are able to make it into the lobby. Those who make
    it into the lobby are not in the turn order and are considered spectators.
    So, essentially they are harmless to the conduct of the game, but it does
    not align with handout specs. I just cannot find out how they get in.
