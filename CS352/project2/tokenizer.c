/**
 * tokenizer.c - A simple token recognizer.
 *
 * NOTE: The terms 'token' and 'lexeme' are used interchangeably in this
 *       program.
 *
 * @author Clay Farrell
 * @version 4/21/22
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "tokenizer.h"

// global variables
char *line; // Global pointer to line of input

// (optional) can declare some additional variable if you want to


/**
 * the main function for the tokenizer. Checks the args and opens the read file
 * and loops through the lines of input, calls get_token(), then writes to the
 * output file.
 * @param argc - the number of argument strings in argv
 * @param argv - the string arguments passed in from command line
 * @return 0 if the program executed successfully
 */
int main(int argc, char *argv[]) {
    char token[TSIZE];     /* Spot to hold a token, fixed size */
    char input_line[LINE]; /* Line of input, fixed size        */
    FILE *in_file = NULL;  /* File pointer                     */
    FILE *out_file = NULL;
    int line_count, /* Number of statements tally           */
        start,      /* is this the start of a new statement? */
        count;      /* count of tokens                  */

    if (argc != 3){
        printf("Usage: tokenizer inputFile outputFile\n");
        exit(1);
    }

    in_file = fopen(argv[1], "r");
    if (in_file == NULL){
        fprintf(stderr, "ERROR: could not open %s for reading\n", argv[1]);
        exit(1);
    }

    out_file = fopen(argv[2], "w");
    if (out_file == NULL){
        fprintf(stderr, "ERROR: could not open %s for writing\n", argv[2]);
        exit(1);
    }

    //we have not started, so if we start it is a new statement
    start = TRUE;
    //it also means line count would be 1 if we get a line
    line_count = 1;
    //count would start at 0 here according to the output file examples
    count = 0;
    while (fgets(input_line, LINE, in_file) != NULL){
        line = input_line; // Sets a global pointer to the memory location
                           // where the input line resides.

        // call get_token while we have not encountered null character
        while (*line != '\0'){
            if(is_white_space()){
                line++;
            }
            else{
                //get the token
                get_token(token);
                //grab the token category
                char* category = get_category(token);
                
                //writes the token and returns if it ended the statement
                start = write_token(token, category, &count, start,
                                    line_count, out_file);
                if (start){
                    //we would have a new statement if start was true here
                    line_count++;
                }
                //reset the token string
                reset_token(token);  
            }          
        }
    }

    fclose(in_file);
    fclose(out_file);
    return 0;
}

/**
 * gets the next token from the input using the current line pointer and stores
 * it in the char* variable
 * 
 * @param token_ptr - a pointer to the token string
 */
void get_token(char *token_ptr){
    //the char on it's own is a lexical unit always
    if (is_unit()){
        //add the single character to the token pointer
        strncat(token_ptr, line, CAT);
        line++;
    }

    //else if the token could be an (in)equality operation and followed by chars
    else if (*line == '='|| *line == '<'|| *line == '>'|| *line == '!'){
        //add the first char and advance the pointer to check the next char
        strncat(token_ptr, line, CAT);
        line++;
        if(*line == '='){
            //if the next char was the second half of of the token, add it too
            strncat(token_ptr, line, CAT);
            line++;
        }
    }
    //if the token is a number where the line pointer is currently
    else if(is_number(*line)){
        //call recursive function to make a number token
        make_number(token_ptr);
    }
    //if the pointer is on white space just skip it
    else if(is_white_space()){
        line++;
    }
    //if there is a non null character left it is unrecognized
    else{
        //add it to the token array for a later error
        strncat(token_ptr, line, CAT); 
        line++;
    }
}


/**
 * checks if the char is one of the 10 digits for a number [0-9]
 * 
 * @return 1 if the char is a digit and 0 otherwise
 */
int is_number(char num){
    return (num == '0' || num == '1' || num == '2' || num == '3' || 
        num == '4' || num == '5' || num == '6' ||
        num == '7' || num == '8' || num == '9');
}



/**
 * checks if the char on it's own is always itself a lexical unit (1:1 mapping)
 * 
 * @return 1 if the char is a lexical unit and 0 otherwise
 */
int is_unit(){
    return (*line == '+' || *line == '-' || *line == '*' ||
        *line == '/' || *line == '^' || *line == '(' || 
        *line == ')' || *line == ';');
}

/**
 * checks if the global pointer is to whitespace or null char
 * 
 * @return 1 if the pointer is to whitespace and 0 otherwise
 */
int is_white_space(){
    //for whatever reason I had to use ascii for new lines
    return *line == ' ' || *line == '\t' || *line == '\n';
}

/***
 * recursive method for making a number from the line of input
 * @param token_ptr - a pointer to a char that is the token
 * */
void make_number(char * token_ptr){
    //on entry we know we can add what is at the line pointer
    strncat(token_ptr, line, CAT);
    line++; //advance the pointer
    //then we check again if we should call make_number
    if(is_number(*line)){
        make_number(token_ptr);
    }//implicit base case is we are no longer pointing to a digit
}


/**
 * gets the token category or an error message
 * 
 * @param token - the token that was made in get_token
 * @return category - the category of the token that was passed in
 */
char* get_category(char token []){
    //return value
    char* category;
    //comparing the token string to the possible outcomes
    if (strcmp(token, "+") == 0){category = "an ADD_OP";}
    else if(strcmp(token, "-") == MATCH){category = "a SUB_OP";}
    else if(strcmp(token, "*") == MATCH){category = "a MULT_OP";}
    else if(strcmp(token, "/") == MATCH){category = "a DIV_OP";}
    else if(strcmp(token, "(") == MATCH){category = "a LEFT_PAREN";}
    else if(strcmp(token, ")") == MATCH){category = "a RIGHT_PAREN";}
    else if(strcmp(token, "^") == MATCH){category = "an EXPON_OP";}
    else if(strcmp(token, "=") == MATCH){category = "an ASSIGN_OP";}
    else if(strcmp(token, "<") == MATCH){category = "a LESS_THAN_OP";}
    else if(strcmp(token, "<=") == MATCH){category = "a LESS_THAN_OR_EQUAL_OP";}
    else if(strcmp(token, ">") == MATCH){category = "a GREATER_THAN_OP";}
    else if(strcmp(token, ">=") == MATCH)
    {category = "a GREATER_THAN_OR_EQUAL_OP";}
    else if(strcmp(token, "==") == MATCH){category = "an EQUALS_OP";}
    else if(strcmp(token, "!") == MATCH){category = "a NOT_OP";}
    else if(strcmp(token, "!=") == MATCH){category = "a NOT_EQUALS_OP";}
    else if(strcmp(token, ";") == MATCH){category = "a SEMI_COLON";}
    //only need to check the first character of token for int_literals
    else if(is_number(token[0])){category = "an INT_LITERAL";}
    //if none of the other cases were a match, lexeme is unidentifiable
    else{category = "Lexical error: not a lexeme";}
    return category;
}

/**
 * resets all the chars in token to be null characters
 * 
 * @param token - the token string needing to be reset
 */
void reset_token(char token []){
    int i;
    for (i = 0; i < TSIZE; i++){
        token[i] = '\0';
    }
}


/**
 * takes the token and category information and writes it to the output file
 * 
 * @param token - the token to write to the file
 * @param category - the category the token belongs to as a string
 * @param count - address of the tally for the number of lexemes recognized
 * @param start - if we are at the start of a statement and need the statement number
 * @param line_count - the statement number we are at
 * @param output - the output file
 * @return start - 0 if the statement is ongoing, 1 if the token was a semicolon
 */
int write_token(char token [], char * category, int* count, int start,
                                        int line_count, FILE* output){

    
    //if valid is 0 we have an unrecognized token
    int valid = strcmp(category, "Lexical error: not a lexeme");
    //if this the start of a statement, we print it to output and change start
    if(start){
        fprintf(output, "Statement #%d\n", line_count);
        start = FALSE;
    }
    //if it's a valid lexeme that follows
    if(valid != FALSE){
        //print the lexeme info and increment the count
        fprintf(output, "Lexeme %d is %s and is %s\n", *count, token, category);
        *count = *count + 1;
        //and if the lexeme was the end of a statement
        if(strcmp(token, ";") == MATCH){
            //print the footer, the next statement would be the start of one
            fprintf(output,
            "---------------------------------------------------------\n");
            start = TRUE;
            //and the tally of lexemes for the statement would be reset
            *count = 0;
        }
    }
    //otherwise, we print that the token was unrecognized
    else{
        fprintf(output, "===> '%s'\n%s\n", token, category);
    }
    return start;
}