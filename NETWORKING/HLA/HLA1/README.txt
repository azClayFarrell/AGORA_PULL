NOTE:

    I have the UDP server set to time out after 1 minute, so the connection
    the connection closes pretty quick but it should be fine if you have
    the client side commands already typed beforehand.

BUG(maybe?):

    I don't know why it is happening, but when I enter a string on the Client
    side instead of an int, it crashes the server. I don't understand at all
    how this is even happening. The write() method is being called to send
    the scanned entry to the server before the error of the input is being
    caught. The server catches the error but it also closes the server when
    it does. This wouldn't be an issue if the Client side would parse and
    then send, but I don't know why it isn't. It catches the error immediately
    after sending it to the server though.

    The odd bit is that the UDP version seems stable when I do this. The
    server does not close when I attempt to send a String rather than an int.
