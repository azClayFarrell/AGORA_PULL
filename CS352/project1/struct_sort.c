/**
 * @file struct_sort.c
 * 
 * @author Clay Farrell
 * @version 1.0
 * @date 2022-04-15
 * 
 * This program takes 3 command line arguments, the executable and the input
 * and output files, for making a list of persons, sorts them, and writes
 * to an output file the results
 */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "struct_sort.h"

/***
 * main takes the command line arguments and does some checks and calls go()
 * @param argc - the number of arguments in argv
 * @param argv - the command line arguments passed in by user
 * @return 0 if program completed successfully
 * */
int main (int argc, char** argv) {
    
    //if the number of arguments was not the right amount
    if (argc != ARG_LEN){
        //print a usage message and exit
        printf("Usage: struct_sort <input file> <output file>\n");
        exit(1);
    }
    //if this line is reached we have enough to try and call go()
    go(argv);
    
  
    return 0;
}

/**
 * tries to open the files from the command line args and call the functions
 * needed to make a list of person_t, sort it, and write the result to a file
 * @param argv - the command line strings that need to try and be opened into
 *               file pointers
 */
void go(char** argv){
    //the input and output files that are supplied in the arguments
    FILE* input;
    FILE* output;

    //open the input file for reading
    input = fopen(argv[1], "r");
    //if the file could not be opened
    if(input == NULL){
        //print the error and exit
        fprintf(stderr, "ERROR: input file %s could not be opened for reading\n"
        , argv[1]);
        exit(1);
    }
    //open the output file for writing
    output = fopen(argv[2], "w");
    //if the file cannot be opened
    if(output == NULL){
        //print the error and exit
        fprintf(stderr, "ERROR: output file %s could not be open for writing\n"
        , argv[2]);
        exit(1);
    }
    //make an array of person_t
    person_t contacts[CONT];
    //read all the values in from the file
    int entries = read_all(contacts, input);

    //if there were any entries made
    if(entries > 0){
        //if there were 2 or more we should sort them
        if(entries > 1){
            //sort the contacts that were scanned in
            sort(contacts, entries);
        }
        
        //write all the sorted contacts to the output file
        write_all(contacts, output, entries);
    }
    //there were no entries and we should tell the user as much
    else{
        fprintf(stderr, "ERROR: there were no entries in input file \"%s\"\n",
        argv[1]);
        exit(1);
    }
    

}

/**
 * uses the input file to fill out the list of person_t
 * @param contacts - the array of person_t that needs to be filled in
 * @param input - the input file that we are reading records from
 * @returns index - the index of the final entry in the list
*/
int read_all(person_t contacts[], FILE* input){
    //char arrays and vars that we will need to hold the fields of the structs
    char street [CITY_STREET];
    char city [CITY_STREET];
    char state[STATE];
    int zip;
    char first[F_NAME_L_NAME];
    char last [F_NAME_L_NAME];
    char phone[PHONE];
    //the buffer that will hold the line that fgets reads from the input file
    char buffer [LINE_BUFF];
    //the index of the current contact in the array of person_t
    int index = 0;
    
    //while we get a line back from fgets and we have room in the contact array
    while(fgets(buffer, LINE_BUFF, input) != NULL && index < CONT){
        //split almost all the fields on the commas, otherwise as %d or %s
        sscanf(buffer, "%[^,],%[^,],%[^,],%[^,],%[^,],%d,%s",
            first, last, street, city, state, &zip, phone);
        
        //passes the input fields for a second pass to get rid of white space
        strip_spaces(first, last, street, city, state);
        //grab the pointer to the person at the index in the contacts
        person_t* temp = &contacts[index];
        
        //transfer all the info over to the person at the index
        strcpy(temp->address.street, street);
        strcpy(temp->address.city, city);
        strcpy(temp->address.state, state);
        temp->address.zip = zip;
        strcpy(temp->first_name, first);
        strcpy(temp->last_name, last);
        strcpy(temp->phone, phone);
        //increment the index
        index++;

        err_check_fields(state, phone, zip, index);
        
    }
    return index;
}

/**
 * sorts the array of person_t that is passed to it
 * calls find_min() which does the actual list traversal that results in O(n^2)
 * 
 * @param contacts - an array of person_t that needs sorting
 * @param entries - the number of entries needing to be sorted (in case there is
 *                  unutilized space at the end of the array)
 */
void sort(person_t contacts [], int entries){
    //array of sorted elements
    person_t sorted [entries];
    //the index of the sorted array to be inserted at
    int index = 0;
    //the number of remaining entries in contacts to sort through
    int remaining = entries;
    //the index of the "smallest" element of contacts
    int min;
    //while the insert index is less than the entries of contacts
    while (index < entries){
        //find the smallest element in contacts
        min = find_min(contacts, remaining);
        //copy the value into the sorted array at the next element
        sorted[index] = contacts[min];
        //then while the min index is less than the end of the array of contacts
        while(min < entries){
            //squish the list down, writing over the min value
            contacts[min] = contacts[min+1];
            min++;
        }
        
        //update condition variables
        remaining--;
        index++;
    }
    
    //write back to the contacts list in the sorted order
    for(index = 0; index < entries; index++){
        contacts[index] = sorted[index];
    }
    
}

/**
 * writes all the entries of person_t stored in the array contacts into a file
 * 
 * @param contacts - an array of sorted person_t that will be recorded
 * @param output - an output file that will be written to
 * @param entries - number of entries needing to be written to the output file
 */
void write_all(person_t contacts[], FILE* output, int entries){
    //index counter
    int index;
    //loop through all the entries
    for(index = 0; index < entries; index++){
        //grab the person
        person_t person = contacts[index];
        //write to the output file
        fprintf(output, "%s, %s, %s, %s, %s, %d, %s\n",
        person.first_name, person.last_name, person.address.street,
        person.address.city, person.address.state, person.address.zip,
        person.phone);
    }
    
}


/**
 * strips the whitespaces left after scanning in from fgets
 * 
 * @param first - the first name of the person
 * @param last - the last name of the person
 * @param street - the street of the person
 * @param city - the city the person lives in
 * @param state - the state the peron lives in
 */
void strip_spaces(char first [], char last [],char street [],char city [],char state []){
    //takes all the arrays and only keeps the character components

    strip_composite(first);    
    strip_composite(last);
    strip_composite(state);
    strip_composite(street);    
    strip_composite(city);
    
}

/**
 * strips the street and city which are composed of "multiple" strings
 * @param composite - the multistring char array that needs the first bit of
 *                    white space stripped off
 */
void strip_composite (char composite []){
    //make a val pointer for pointer arithmetic
    char * val = (char*) malloc(strlen(composite) * sizeof(char));
    //hold onto a reference to the original pointer
    char * temp = val;

    //copy the contents of the composite string into val
    strcpy(val, composite);

    //need to check for space and tab since those are the only possible chars
    while(*val == ' ' || *val == '\t'){
        //move the pointer of the array to the next value
        val++;
    }
    //copy back once we encounter a character thats not whitespace during search
    strcpy(composite, val);
    //then free the heap allocation back up and temp will pop during return
    free(temp);
}


/**
 * displays all the entries of contacts after reading them in
 * NOTE: this was used for debugging but I like it here
 * 
 * @param contacts - array to be printed
 * @param entries - the number of iterations needed to complete the display
 */
void display(person_t contacts [], int entries){
    //int for iteration
    int i;
    //extract all the fields and print them
    for(i=0; i< entries; i++){
        person_t temp = contacts[i];
        printf("\n\n==============\n%s\n", temp.first_name);
        printf("%s\n", temp.last_name);
        printf("%s\n", temp.phone);
        printf("%s\n", temp.address.city);
        printf("%s\n", temp.address.street);
        printf("%s\n", temp.address.state);
        printf("%d\n", temp.address.zip);
    }
    
}



/**
 * returns the index of the smallest element of the array of person
 * 
 * @param contacts - the array that we need to find the min value for
 * @param remaining - number of contiguous elements from 0 needing to be checked
 *                    in the array
 * @return min - the index of the relative "smallest" entry in contacts
 */
int find_min(person_t contacts [], int remaining){
    //default minimum index
    int min = 0;
    //the index of the current person
    int current = 1;
    //need to check the whole list
    while(current < remaining){
        //get the people in question
        person_t min_person = contacts[min];
        person_t other_person = contacts[current];
        //make room for their name information
        char min_last [strlen(min_person.last_name) + 1];
        char other_last [strlen(other_person.last_name) + 1];
        
        //copy the information(easier for me)
        strcpy(min_last, min_person.last_name);
        strcpy(other_last, other_person.last_name);
        //counters for iterating thru the strings and making them lowercase
        int min_i;
        int other_i;
        //convert both strings to lowercase
        for(min_i = 0; min_i < strlen(min_last); min_i++){
            min_last[min_i] = tolower(min_last[min_i]);
        }
        for(other_i = 0; other_i < strlen(other_last); other_i++){
            other_last[other_i] = tolower(other_last[other_i]);
        }
        //if the other last name is less than the current minimum
        if(strcmp(other_last, min_last) < 0){
            min = current;
        }
        //if they match move to the first names
        else if(strcmp(other_last, min_last) == 0){
            //copy the first names now since we know we need them
            char min_first [strlen(min_person.first_name) + 1];
            char other_first [strlen(other_person.first_name) + 1];
            strcpy(min_first, min_person.first_name);
            strcpy(other_first, other_person.first_name);
            //cast both of them to lowercase as before
            for(min_i = 0; min_i < strlen(min_first); min_i++){
                min_first[min_i] = tolower(min_first[min_i]);
            }
            for(other_i = 0; other_i < strlen(other_first); other_i++){
                other_first[other_i] = tolower(other_first[other_i]);
            }
            //if the other first name is less than the current minimum
            if(strcmp(other_first, min_first) < 0){
                min = current;
            }
            //if the first name was not less, we dont have to change anything
        }
        //if the min was less than the other string then we do nothing

        //increment the current index
        current++;
    }
    return min;
}

/**
 * does some error checking for the fields that need them
 * 
 * @param state - the state field that needs checking
 * @param phone - the phone number that needs checking
 * @param zip - the zip code that needs checking
 * @param index - the index for the entry of these fields
 */
void err_check_fields(char state [], char phone [], int zip, int index){
    //counter for for loops
    int i;
    //doing some validation, state doesn't have exactly 2 characters
        if(strlen(state) != STATE_2){
            fprintf(stderr,
            "ERROR: state field was not two characters for entry %d\n",
            index);
            exit(1);
        }
        //if the phone string is not the length of the phone array - null char
        if(strlen(phone) != PHONE -1){
            fprintf(stderr,
            "ERROR: phone number was not long enough %d\n",
            index);
            exit(1);
        }
        //if the periods of the phone number are not in the right position
        if(phone[PRD_POS1] != '.' || phone[PRD_POS2] != '.'){
            fprintf(stderr,
            "ERROR: wrong phone format for entry %d, use format XXX.XXX.XXXX\n",
            index);
            exit(1);
        }
        //if the zip integer is outside a 5 digit boundry
        if(zip > ZIP_UBOUND || zip < ZIP_LBOUND){
            fprintf(stderr,
            "ERROR: zip for entry %d is not a 5-digit zipcode\n",
            index);
            exit(1);
        }
        //for the first part of the phone number check if they are numbers
        for(i = 0; i< PRD_POS1; i++){
            if(check_phone_digit(phone[i]) == TRUE){
                fprintf(stderr,
                "ERROR: phone for entry %d contained a non-numeric\n",
                index);
                exit(1);
            }
        }
        //checks if the second part of the phone number is made of numbers
        for(i = PRD_POS1 + 1; i < PRD_POS2; i++){
            if(check_phone_digit(phone[i]) == TRUE){
                fprintf(stderr,
                "ERROR: phone for entry %d contained a non-numeric\n",
                index);
                exit(1);
            }
        }
        //checks if the last part of the phone number is made of numbers
        for(i = PRD_POS2 + 1; i < strlen(phone); i++){
            if(check_phone_digit(phone[i]) == TRUE){
                fprintf(stderr,
                "ERROR: phone for entry %d contained a non-numeric\n",
                index);
                exit(1);
            }
        }
}

/**
 * checks if the character is one of the digits [0-9]
 * 
 * @param digit - the digit that needs comparison
 * @return 1 if there was no match and 0 otherwise
 */
int check_phone_digit(char digit){
    return digit != '0' && digit != '1' && digit != '2' && digit != '3' &&
        digit != '4' && digit != '5' && digit != '6' && digit != '7' &&
        digit != '8' && digit != '9';
}