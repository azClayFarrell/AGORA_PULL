Author: Clay Farrell
Date of Submission: 4/21/22
Assignment: Project 2 - tokenizer.

INSTRUCTIONS:


To compile type into command line while in the directory that contains both the
tokenizer.c and tokenizer.h files:

    gcc -Wall tokenizer.c

The executable created should be called a.out if you did not rename it, and it
should have been placed in the same directory you ran this command in.

To run the program, type in the same directory that contains a.out:

    ./a.out <input file> <output file>


You should replace where it says input file and output file with the actual
relative path names that you wish to read from and write to.


DESCRIPTION:
    You will enter an input file and an output file when you run the command to
    start this program. The input file will contain possible lexemes that
    tokenizer can recognize and possibly others it cannot. It could also contain
    any amount of white spaces. After execution, the output file will contain
    the number of statements found, the number of lexemes in the statement, the
    token category the lexeme belongs to, and will show any non lexical units
    found while doing lexical analysis. Each statement will be seperated by a
    line of dashes.



POSSIBLE PROBLEMS:

    There are no checks when making a INT_LITERAL lexeme that the number of
    digits is less than the token array, which would maybe result in a core
    dump? I assumed the input files would contain numbers of 19 digits or less.