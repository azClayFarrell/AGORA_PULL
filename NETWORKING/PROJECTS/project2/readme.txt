Authors: Clay Farrell & Hannah Young
Date: 11/07/2022
Class: CS465 Networking
Project 2 Viva Las Vegas!
Description: Using TCP and UDP protocols to create a card dealing program. The client inputs the
    protocol they would like to use, the amount/type of cards they would like to display and the
    server produces those cards to the client side of the application.

Compilation:
    While in the "project2" folder:

        Server side:
            javac server/*.java common/*.java

        Client side:
            javac client/*.java common/*.java

Usage:

    While in the "project2" folder:

        Server side (run before clients can connect):
            java server.DealerServerDriver <tcp|udp> [PORT]

        Client side:
            java client.DealerClientDriver <tcp|udp> <host> [PORT] [FLAG]

Bugs:
    None known.