/**
 * Header file for the struct_sort project 1
 * @author Clay Farrell
 * @version 4/17/22
 * 
 * This file defines all the preprocessor variables and creates all the method
 * headers to be included in struct_sort.c
 */


/**the max number of characters on one line of the input, 101 because line max
 * is of size 100, so we need one extra character for null**/
#define LINE_BUFF 101
/**the max number of person records in the input file*/
#define CONT 100
/**the length of the city and street arrays*/
#define CITY_STREET 75
/**the length of the state array*/
#define STATE 75
/**length of the first name array*/
#define F_NAME_L_NAME 75
/**length of the phone number, 10 digits, 2 periods, 1 null character = size 13*/
#define PHONE 13
/**the position for the first period of a phone number*/
#define PRD_POS1 3
/**the position for the second period of a phone number*/
#define PRD_POS2 7
/**the upper limit of the zipcode boundry*/
#define ZIP_UBOUND 99999
/**the lower limit of the zipcode boundry*/
#define ZIP_LBOUND 10000
/**the number of arguments we should have at the start of the program*/
#define ARG_LEN 3
/**boolean true*/
#define TRUE 1
/**boolean false*/
#define FALSE 0
/**true size the state should be after stripping all the whitespace*/
#define STATE_2 2


/**
 * a structure for the address of a person
 */
typedef struct{
    //the persons street addresss
    char street[CITY_STREET];
    //the city the person lives in
    char city[CITY_STREET];
    //the state the person lives in
    char state[STATE];
    //the zip code of the person
    int zip;
} address_t;

/**
 * a structure for the person_t type, represents a person record
 */
typedef struct{
    //pointer to the persons first name
    char first_name[F_NAME_L_NAME];
    //pointer to the persons last name
    char last_name[F_NAME_L_NAME];
    //pointer to the persons telephone numeber
    char phone[PHONE];
    //the address of the person
    address_t address;

} person_t;



/**
 * takes the argv array from the command line and calls other fuctions
 */
void go(char**);


/**
 * reads all the input from the file and puts it into the person array
 * @returns index - the index at which fgets came back null
 */

int read_all(person_t  [], FILE* );


/**
 * writes all the contact entries to the output file
 */

void write_all(person_t  [], FILE*, int);


/**
 * sorts the person array
 */
void sort(person_t [], int);


/**
 * swaps one person and another person entry
 * NOTE: UNUSED, didn't go this route
 */
//void swap(person_t* , person_t*);

/**
 * strips the whitespaces left after scanning in from fgets
 */
void strip_spaces(char [], char [], char [], char [], char []);


/**
 * strips the street and city which are composed of "multiple" strings
 */
void strip_composite(char[]);

/**
 * to show the array of contacts for debug purposes
 */

void display(person_t [], int);


/**
 * returns the index of the smallest element of the array of person
 * 
 * @return min - the index of the smallest value in the array
 */
int find_min(person_t [], int);

/**
 * does some error checking for the fields that need them
 * 
 */
void err_check_fields(char [], char [], int, int);


/**
 * checks a single digit to see if it is one of [0-9]
 * 
 * @return 1 if the char did not match a digit and 0 otherwise
 */
int check_phone_digit(char);