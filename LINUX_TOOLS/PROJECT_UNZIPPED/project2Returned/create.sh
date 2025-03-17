#!/bin/bash
################################################################################
# Clay Farrell and Zack West
# 10-19-22
# Accepts two command line arguments: a file type, either “-i” or “-o”,
# followed by a file name (no extension) and create files with headers only.
#
# Args:
#   -i: in-state invoice (will use NC as state)
#   -o: out-of-state invoice (will prompt user for state)
################################################################################
# Exit statuses
# 1 - invalid option specified
# 2 - no option specified
# 3 - invalid value specified for state
# 4 - output filename already exists
# 5 - invalid number of arguments passed (no filename)
# 6 - data invalidated via valid.sh
# 7 - invalid user input from prompts
# 8 - invalid entry for qty
################################################################################

# define value for in state invoices here for clarity.
INSTATE="NC"
FIELD_DELIMITER=","
TMP_FILENAME="invoice.tmp"

# 1 prints stuff; 0 turns the prints off
DEBUG=0

# A convenience function to display uniform usage messages
# across a range of exception cases.
usage(){
  printf "Usage: \$ create.sh -i|-o filename"
}

# debugger that echos output if the global DEBUG
# variable is == 1
log_info(){
  if [[ $DEBUG == 1 ]]; then
    echo "[DEBUG]: $1"
  fi
}

# convenience function to print out an array of strings
# delimited by a single char.
# Note: doesn't verify anything.
# Args:
#   $1 - the delimiter
#   $2 - the array of strings
print_with_delimiter(){
  
  # the final string to be output
  output=""
  
  # get delimiter from first arg and shift out to get array
  _delimiter="$1"
  shift

  # track progress to avoid adding trailing delimiter
  array=("$@")
  count=0
  total=${#array[@]}

  # build an output string from items in the array.
  for item in "${array[@]}"; do
    ((count++))
    # add item with a delimiter except on last item
    if [[ $count < $total ]]; then
      output="$output$item$_delimiter"
    else
      output="$output$item"
    fi
  done

  # echos the value allowing cmd sub for return
  echo "$output"

}

# validate number of arguments; there should be 2
if [[ $# != 2 ]]; then
  log_info "ERROR 5 - Invalid number of arguments"
  usage
  exit 5
fi

# if no options passed exit
if [[ $OPTIND != 1 ]]; then
  log_info "ERROR 2 - no options passed. OPTIND = $OPTIND"
  usage
  exit 2
fi

# run a loop over the options to see if any provided
in_state=''
oo_state=''
while getopts "io" opt; do
  case "${opt}" in
    i) in_state='true' ;;
    o) oo_state='true' ;;  # borderline redundant
    *)
      log_info "ERROR 1 - invalid option passed: $opt"
      usage
      exit 1 ;;
  esac
done

# determine filename based on residency status
if [[ "$in_state" == 'true' ]]; then
  filename="$2.iso"
else
  filename="$2.oso"
fi

# check filename for duplicate
if [[ -f "$filename" ]]; then
  printf "ERROR: %s already exists" "$filename"
  exit 4
fi

# gather customer info;
# exit w/usage message if not entered.
read -rp "Please enter customer name > " customer_name
if [[ "${#customer_name}" == 0 ]]; then
  usage
  exit 7
fi

# get street address
# if no input, exit w/usage msg
read -rp "Please enter street address > " street_address
if [[ "${#street_address}" == 0 ]]; then
  usage
  exit 7
fi

# get city
# if no input, exit w/usage msg
read -rp "Please enter city > " city
if [[ "${#city}" == 0 ]]; then
  usage
  exit 7
fi

# set NC auto for in-state
if [[ "$in_state" == "true" ]]; then
  state=$INSTATE
else
  read -rp "Please enter state > " state

  # if no input, exit w/usage msg
  valid_pattern='^[a-zA-Z]{2}$'
  if [[ ! $state =~ $valid_pattern ]]; then
    printf "ERROR: invalid value for state. must be two alphabet characters."
    exit 3
  fi
fi

# assert that no category name is a reserved word.
valid="false"
while [[ "$valid" != "true" ]]; do

  # gather order info
  echo -n "Please enter the fields that comprise the order >"
  read -a order_items

  # check all items entered by the user against disallowed the disallowed
  # item names "items" and "categories"
  for item_name in "${order_items[@]}"; do

    # if disallowed entry found declare input invalid
    if [[ "$item_name" == "items" || "$item_name" == "categories" ]]; then
      printf "Invalid item name specified (%s). Re-enter item information."\
       "$item_name"

      # this will re-start the while loop prompt if anything invalid is found
      valid="false"
      break

    # a full array of valid items will result in the valid variable
    # remaining "true" and ending the while loop.
    else
      valid="true"
    fi
  done
done

# loop through each item and prompt for qty
qty=()
for item in "${order_items[@]}"; do

  # endlessly prompt a user for a valid qty that is an integer 0 or greater.
  valid_qty=1
  while [[ $valid_qty != 0 ]]; do

    # prompt user with this really long prompt that wants to test
    # the balance of being < 80 lines and actual readability :|
    read -rp \
    "Please enter the number of \"$item\" items you want to purchase > "\
    item_count

    # if the qty is an integer and gte 0 all good.
    qty_pattern="^[0-9]+$"
    if ! [[ "$item_count" =~ $qty_pattern ]]; then
      valid_qty=1
      echo "Please enter a positive integer for the qty."
      else
        valid_qty=0
    fi
  done

  # test if the qty entered was a number; exit if not.
  # todo - would be better as while loop nag for re-entry.
  numeric_pattern='^[0-9]+$'  # positive numbers only w/o commas or decimals.
  if [[ ! "$item_count" =~ $numeric_pattern ]]; then
    printf "Invalid entry for qty. Must be a positive integer not <%s>" \
     "$item_count"
    exit 8
  fi
  qty+=("$item_count")
done


# idk -- couldn't get cmd sub to work within a HEREDOC block
cats=$(print_with_delimiter $FIELD_DELIMITER "${order_items[@]}")
qtys=$(print_with_delimiter $FIELD_DELIMITER "${qty[@]}")

# create the output that gets validated and written to disk
output=$( cat << HEREDOC
customer:$customer_name
address:$street_address, $city, $state
categories:$cats
items:$qtys
HEREDOC
)

# writes output data to specified file
echo "$output" > "$filename"

# validates output in output file via valid.sh
./valid.sh "$filename"
validated=$?
log_info "return from valid: $validated"

# if validation exits with anything other than 0, remove output file
if [[ $validated != 0 ]]; then
  log_info "validation failed with status code: $validated."
  rm "$filename"
  exit 6
else
  # output results
  printf "\"%s\" has been created for\ncustomer:%s\naddress:%s, %s, %s\n" \
 "$filename" "$customer_name" "$street_address" "$city" "$state"
fi
