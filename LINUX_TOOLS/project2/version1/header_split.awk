##################################
# Clay Farrell and Zack West
# 10-19-22
# 
# Splits up the headers of a invoice file to needed components to
# make sure the file is valid
#
##################################
#
# Exit status:
# 1 - element in the items category was not a number
# 2 - number in the items category was less than 0
# 3 - there were non-letters in the ITEMS header line
# 4 - the invoice has no name on the CUSTOMER header line
#
##################################

#function for printing the elements of an array(DEBUG)
#Parameter: list - the array you are trying to see the elements of
function print_all(list){
    for (i = 1; i < count_size(list); i++){
        print list[i];
    }
}

#counts the number of elements in a list
#Parameter: list - a list that needs the length to be calculated
#Return: the length of the list of elements
function count_size(list){
    size=0;
    for (element in list){
        size++;
    }
    return size;
}

#checks the item numbers in the items line to see if they are larger than 0;
#fails at first occurence if any issues are encountered
#if one of the elements is not a number, exit with 1
#if one of the elements that is a number is less than 0, exit with a 2
#Parameter: list - a list of what is expected to be numbers greater than 0
function check_numbers(list){
    for (element in list){
        #if it was a number less than 0, exit with 2
        if (element < 0){
            print "exit with 2";
            fail="true";
            exit 2;
        }
    }
}


#init a couple variables in begin
BEGIN{
    headers=""; name=""; address=""; state=""; categories=""; cat_len=0;
    items=""; items_len=0; matches=0; fail="false";
}

#if the line contains letters it should be one of the header lines
$0 ~ /[A-Za-z]+/ {

    #increment the matches made and concat to the headers the name of this header.
    matches++;
    headers = headers $1;
    if (matches < 4)
        headers = headers " "

    #depending on which header line we are on, assign to one of the following    
    if (matches == 1)
        #the name of the person
        name = $2;
        #to determine if the invoice has a name present
        has_name = match(name, /[^ \t\n]+/);
        #if has_name is 0 then no possible name was found
        if (has_name == 0){
            fail="true";
            exit 4;
        }
    if (matches == 2)
        #the address of the person, to be split and checked in END
        address = $2;
    if (matches == 3)
        #The categories, to be split and checked in END
        categories = $2;
        #print categories;
    if (matches == 4)
        #the items numbers, to be split and checked in END
        items = $2;
        non_numeric = match(items, /[A-Za-z]+/);
        #if there are non-numeric characters exit here
        if (non_numeric != 0){
            fail="true";
            exit 1;
        }
}

END{
    #checks to see if the main body encountered a call to exit
    if (fail == "false") {
    
    #sets the state to third component of the array, which should be the state
    split(address, address_sep, ",");
    state = address_sep[3];    

    #strips any leading non-legal characters off the front
    sub(/[^A-Za-z0-9]*/, "", categories)
    #splits and counts the number of entries for categories and items
    #regex is necessary to avoid an ending sep to be counted as another split
    split(categories, categories_sep, /[^A-Za-z0-9]+./);
    cat_len = count_size(categories_sep);
    #strips characters off the front
    sub(/[^0-9]*/, "", items);
    split(items, items_sep, /[^0-9]+./);
    items_len = count_size(items_sep);

    #print non_numeric;

    #checks for negative numbers in the items array. can exit with error in method
    check_numbers(items_sep);

    #strip the state of white space then check if it contains non-letters
    sub(/ */, "", state);
    non_letters = match(state, /[^A-Za-z]+/);
    #if non_letters is not a 0, then state has illegal characters in it
    if (non_letters != 0){
        exit 3;
    }
    #have to use printf to avoid unneeded spaces in output
    printf "%s\n%s\n%d\n%d\n\n", tolower(headers), toupper(state), cat_len, items_len;

    #successful exit
    exit 0;
    }    
}
