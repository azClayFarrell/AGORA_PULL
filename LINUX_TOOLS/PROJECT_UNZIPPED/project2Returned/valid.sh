#!/bin/bash

####################################
# Clay Farrell and Zack West
# 10-19-22
#
# validates an invoice file by looking at the header lines
#
# Args: the name of an invoice file
#
####################################
# Exit legend:
# 
# 1 - the file argument does not exist
# 2 - the argument is a directory
# 3 - the file extension is not correct for an invoice file
# 4 - invoice has no name in CUSTOMER header line
# 5 - no address was given
# 6 - the CATEGORIES header line contained nothing that could be an entry
# 7 - the state for an in state extension file was not "NC"
# 8 - the length of the state abbreviation was not of length 2
# 9 - the headers were formatted incorrectly
# 10 - there was not at least 1 category
# 11 - there was not at least 1 item request(defunct)
# 12 - there are an unequal amount of categories and orders, should be equal
# 13 - we do not have read permissions for the file
# 14 - the .oso file had NC as the state
# 15 - illegal characters found in the ITEMS header
# 19 - the state field contained non-letters
# 20 - there was no command line argument given
# 21 - categories used a reserved word in it's list
#
####################################




#the in state file extension
INSTATE="iso"
#the out of state file extension
OUT_STATE="oso"
#the valid header string
VALID_HEADER="customer address categories items"
#the state that should be used for in state stuff
NC="NC"
#a very unlikely temp file to be used to store the awk script output
TEMP_FILE="a_very_unlikely_temp_file_name.txt"

#exit if no command line argument was given
if [[ $# != 1 ]]; then
    echo "Usage: ./valid.sh <invoice file>"
    exit 20
fi

#if parameter did not exist
if [[ ! -e "$1" ]]; then
    echo "ERROR: $1 did not exist"
    exit 1
fi

#if parameter was a directory
if [[ -d "$1" ]]; then
    echo "ERROR: $1 is a directory"
    exit 2
fi

#read permissions are restricted
if [[ ! -r "$1" ]]; then
    echo "ERROR: $1 does not have read permissions"
    exit 13
fi

#gets the extension of the file after the "."
file_ex=$(echo "$1" | cut -d'.' -f2)

#if the file extension is not a legal extension, exit
if [[ "$file_ex" != "$INSTATE" && "$file_ex" != "$OUT_STATE" ]]; then
    echo "ERROR: file extension \"$file_ex\" is not accepted"
    exit 3
else
    #make the temp file and make sure we can read and write to it
    touch "$TEMP_FILE"
    chmod 666 "$TEMP_FILE"
    
    #sends the split header file into the temp file
    $(gawk -F: -f header_split.awk "$1" > "$TEMP_FILE")
    
    #if the awk script exited with an error, determine what when wrong
    status=$?
    if [[ "$status" != "0" ]]; then
        #echo "STATUS: $status"
        #head "$TEMP_FILE"
        #always remove the temp file first
        rm "$TEMP_FILE"

        #exit when there was a string where there should have been a number
        if [[ "$status" == "1" ]]; then
            echo "ERROR: the invoice has no name in the CUSTOMER header line"
            exit 4
        #exit when there was a negative number when there should only be positives
        elif [[ "$status" == "2" ]]; then
            echo "ERROR: the address line was empty"
            exit 5
        #awk found a non-letter in the state part of the address
        elif [[ "$status" == "3" ]]; then
            echo "ERROR: the CATEGORIES header contains nothing that could" \
                    "be an entry"
            exit 6
        #there was no customer name in the invoice file
        elif [[ "$status" == "4" ]]; then
            echo "ERROR: illegal characters are present in the ITEMS header"
            exit 15
        #the state field contained non-letters
        elif [[ "$status" == "8" ]]; then
            echo "ERROR: the state field contains non-letters"
            exit 19
        #the categories header contained a reserved word
        elif [[ "$status" == "9" ]]; then
            echo "ERROR: the CATEGORIES header contained a reserved word"
            exit 21
        fi
    fi

    # if all else is good, init a line count to read the split file
    count=0
    #loop to read the lines
    while read -r line; do
        case "$count" in
            #first line is the headers
            "0")    headers=$line
                    ;;
            #second line is the state abbreviation
            "1")    state=$line
                    ;;
            #third line is the number of order categories
            "2")    cat_len=$(($line + 0))
                    ;;
            #fourth is the number of requests for items, should be equal to cat_len
            "3")    items_len=$(($line + 0))
                    ;;
        esac
        #increment the count
        ((count++))
    done < "$TEMP_FILE" # using the temp file as input for this while loop
    #head "$TEMP_FILE"
    rm "$TEMP_FILE"
    #gets the length of the state abbreviation to check in a second
    state_len=$(echo -n "$state" | wc -c)
    state_len=$(($state_len + 0))
fi



#invalid in state extension exit
if [[ "$file_ex" == "$INSTATE" && "$state" != "$NC" ]]; then
    echo "ERROR: the state was not \"$NC\" for an in state invoice. Provided:" \
        "\"$state\""
    exit 7
elif [[ "$file_ex" == "$OUT_STATE" && "$state" == "$NC" ]]; then
    echo "ERROR: the state for .oso files cannot be \"$NC\""
    exit 14
#if the state abbreviation length is not correct (length 2) exit
elif [[ $state_len != 2 ]]; then
    echo "ERROR: the length of the state field was not 2." \
        "Length: $state_len for state: \"$state\""
    exit 8
fi

#exit for invalid headers
if [[ "$headers" != "$VALID_HEADER" ]]; then
    echo -e "ERROR: incorrect header format, needs \"$VALID_HEADER\""\
            "\nProvided: \"$headers\""
    exit 9
fi

#exit when the categories list was less than 1
if [[ $cat_len < 1 ]]; then
    echo "ERROR: the CATEGORIES list was empty"
    exit 10
fi

#exit when the items list was less than 1
if [[ $items_len < 1 ]]; then
    echo "ERROR: the ITEMS list was empty"
    exit 11
fi

#if there are more categories than orders or more orders than categories, exit
if [[ $cat_len != $items_len ]]; then
    echo "ERROR: invalid item quantities: $cat_len categories but $items_len" \
        "items"
    exit 12
fi

#echo -e "Exiting successfully\n"
#exit successfully
exit 0
