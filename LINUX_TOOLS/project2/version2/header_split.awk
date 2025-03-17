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
# 1 - the invoice has no name on the CUSTOMER header line
# 2 - address line was empty
# 3 - the categories header contains nothing that could be an entry
# 4 - illegal characters are present in the ITEMS header
# 8 - the state field contained non letters
# 9 - the categories contained a reserved header word
##################################

#prints all elements in a provided list(BEBUG)
#Parameter: list - the list with elements to be printed
function print_all(list){
    for (i = 1; i < count_size(list); i++){
        print list[i];
    }
}

#counts the number of elements in a list
#Parameter: list - a list that needs the length to be calculated
#Return: the length of the list of elements
function count_size(){
    size=0;
    for (i=2; i <= NF; i++){
        good = match($i, /[^ \t\n]/);
        if (good != 0){
            size++;
        }
    }
    return size;
}

#finds reserved words in the categories header
function find_reserved(){
    reserved_words = 0;
    for (i = 2; i <= NF; i++){
        if ($i == "customer" || $i == "address" ||
            $i == "categories" || $i == "items"){
                reserved_words++;
        }
    }
    return reserved_words;
}

#finds illegal characters for the items header
function find_illegal_char(){
    #illegal flag
    illegal = 0;
    #start index
    i = 2;
    #while no illegal character is encountered and there are fields left
    while (i <= NF && illegal == 0){
        #if there is a match for an illegal character
        if ($i ~ /[^,0-9]/){
            illegal++;
        }
        #increment index
        i++;
    }
    return illegal;
}



#init a couple variables in begin
BEGIN{
    headers=""; name=""; address=""; state=""; categories=""; cat_len=0;
    items=""; items_len=0; matches=0; fail="false"; FS="[:,]"
}

#if the line contains letters it should be one of the header lines
$0 ~ /[A-Za-z]+/ {

    #increment the matches made and concat to the headers the name of this header.
    matches++;
    headers = headers $1;
    if (matches < 4){
        headers = headers " "
    }

    if (matches == 1){
        name = $2;
        #to determine if the invoice has a name present
        has_name = match(name, /[^ \t\n]+/);
        #if has_name is 0 then no possible name was found
        if (has_name == 0){
            fail="true";
            exit 1;
        }
    }
    if (matches == 2){
        #the address of the person, to be split and checked in END
        address = $0;
        state = $4;
        has_address = match(address, /address:[A-Za-z0-9]/);
        #if has_address is 0 then no possible address was found
        if (has_address == 0){
            fail="true";
            exit 2;
        }
    }
    if (matches == 3){
        #The categories, to be split and checked in END
        categories = $0;
        no_entries = match(categories, /categories:[^ \t\n,]+/);
        if (no_entries == 0){
            fail = "true";
            exit 3;
        }
        bad_entry = find_reserved();
        if (bad_entry != 0){
            fail = "true";
            exit 9;
        }
        #counts the number of entries for categories
        cat_len = count_size();
        
    }
    if (matches == 4){
        #checks for the presence of illegal characters and exits if necessary
        illegal = find_illegal_char();

        #exits if illegal characters were found
        if (illegal != 0){
            fail="true";
            exit 4;
        }

        #counts the number of entries for items
        items_len = count_size();
    }
    
}

END{
    #checks to see if the main body encountered a call to exit
    if (fail == "false") {

        #strip the state of white space then check if it contains non-letters
        gsub(/ */, "", state);
        non_letters = match(state, /[^A-Za-z]/);
        #if non_letters is not a 0, then state has illegal characters in it
        if (non_letters != 0){
            exit 8;
        }
        #have to use printf to avoid unneeded spaces in output
        printf "%s\n%s\n%d\n%d\n\n", tolower(headers), toupper(state), cat_len, items_len;

        #successful exit
        exit 0;
    }    
}
