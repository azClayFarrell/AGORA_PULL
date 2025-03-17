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



Partner review:

I think we put in an equal amount of effort and we did a really good job, but
I found it frustrating working with Allison. There was a bit of a knowledge
gap which made it hard to read her code, which made it hard to debug when we
ran into issues to the point I stopped even really looking at her stuff. If I
encountered a bug I would have to just tell her about it so she could fix it
because there was too much to read for me to find it. She would also
unintentionally make my code harder to read because she would put it into a
format she was more comfortable with. It's left me feeling a little exhausted.
With that aside she worked very hard on this and I think deserves full marks.
The only thing that I found to be a genuine bother is that, after the first
Milestone, I had told her not to work too far ahead of me. I didn't want her to
get too far into the weeds again, but she coded like 300 lines without me
without telling me, and it was not un-ugly code. However, I think her
dedication to the completion of the project outweighs the few shortcomings
she may have in Java.
