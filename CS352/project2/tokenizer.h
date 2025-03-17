/**
 * Header file for the tokenizer project 
 * @author Clay Farrell
 * @version 4/17/22
 */

/* Constants */
/* the length of the line buffer*/
#define LINE 100
/* the size of the token array*/
#define TSIZE 20
/* the number of characters being concatenated to a token at a time*/
#define CAT 1
/* for checking if two strings are a match*/
#define MATCH 0
/* boolean for true*/
#define TRUE 1
/* boolean for false*/
#define FALSE 0

/**
* gets the next token from the input using the current line pointer and stores
* it in the char* variable
*/
void get_token(char *);   

/**
 * checks if the char is one of the 10 digits for a number [0-9]
 * 
 * @param - a character
 * @return 1 if the char is a digit, 0 otherwise
 */
int is_number(char);

/**
 * checks if the char on it's own is always itself a lexical unit (1:1 mapping)
 * 
 * @return 1 if the char is a lexical unit and 0 otherwise
 */
int is_unit();

/**
 * checks if the global pointer is currently on a whitespace character
 * 
 * @return 1 if the pointer is to whitespace and 0 otherwise
 */
int is_white_space();

/**
 * recursively makes a number token when called
 * 
 * @param - the token string that we will append to for creating the number 
 */
void make_number(char *);

/**
 * Gets the token category
 * 
 * @param - the string for the token we need to get the category for
 * @return the string of the token category
 */
char* get_category(char[]);

/**
 * takes the token and category information and writes it to the output file
 * 
 * @param - the token to write to the file
 * @param - the category the token belongs to as a string
 * @param - the address of the current tally of the lexemes
 * @param - if we are at the start of a statement and need the statement number
 * @param - the statement number we are at
 * @param - the output file
 * @return 0 if the statement is ongoing and 1 if the token was a semi colon
 */
int write_token(char[], char*, int*, int, int, FILE*);

/**
 * iterates through the token string and sets everything back to a null char
 * 
 * @param token - the token string needing to be cleared
*/
void reset_token(char[]);
