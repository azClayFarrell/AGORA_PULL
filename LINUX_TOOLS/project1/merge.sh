#!/bin/bash
# Authors: Hannah Young & Clay Farrell
# Date: 9/23/2022
# Description: this program when given valid file input merges two files into
#    a third file. If there are invalid number of files, files that have been
#    created or limited permissions then the program catches those and outputs
#    them to a log file that will contain all of the valid and invalid merges.


numCom=3   # the number of command line args we should have
zero=0  # zero :)
# set the command line args to variables
file1=$1
file2=$2
file3=$3
exponent=0 #the exponent used to determine unique error codes
exit_code=0 #the running total of the exit statuses incurred
two=2 #base for determining unique error codes
err_msg="" #the error message that will be printed to user if needed

echo "----------------" >> log.txt #seperator for the log entries

# no command line given, user prompted for arguments
if  [[ $# == $zero ]]; then 
    echo "Enter file to merge >"
    read file1 file2 file3

    # check if files have been supplied
    if [[ $file1 == "" ]]; then  
        echo "file1 is not supplied."
        echo "Cannot perform merge, please check files."
        echo "file1 is not supplied." >> log.txt
        exit_code=513
    fi
    if [[ $file2 == "" ]]; then
        echo "file2 is not supplied."
        echo "Cannot perform merge, please check files."
        echo "file2 is not supplied." >> log.txt
        if [[ $exit_code != $zero ]];then
            ((exit_code++))  # 514 if doesnt exist
        else
        # first file exists, second file does not
            exit_code=517  
        fi
    fi
    if [[ $file3 == "" ]]; then
        echo "file3 is not supplied."
        echo "Cannot perform merge, please check files."
        echo "file3 is not supplied." >> log.txt
        if [[ $exit_code != $zero ]];then
            ((exit_code += 2))
        else
        # first two files exist, the third does not
            exit_code=518  
        fi
    fi 

elif [[ $# != $numCom ]]; then # check if num args is not 3
    echo "useage: merge.sh [FILE FILE FILE]"
    echo "Invalid number of command line arguments. Supplied: $#" >> log.txt
    exit 520 # err out and exit
fi

#if there is a failure at this point we can exit because we cannot continue
if [[ $exit_code != $zero ]]; then
    echo "exit code: $exit_code"
    exit $exit_code
fi

# check if args do not exist
if [[ ! -e $file1 ]]; then  
    echo "$file1 does not exist."
    echo "Cannot perform merge, please check files."
    echo "$file1 does not exist." >> log.txt
    exit_code=521
fi
if [[ ! -e $file2 ]]; then
    echo "$file2 does not exist."
    echo "Cannot perform merge, please check files."
    echo "$file2 does not exist." >> log.txt
    if [[ $exit_code != $zero ]]; then
        ((exit_code++))
    else
        exit_code=523
    fi
fi
if [[ ! -e $file3 ]]; then
    touch $file3  # creates merged into file if does not exist
    chmod a+rwx $file3  # set file perm to be open for everything
fi

#if there is a failure at this point we can exit because we cannot continue
if [[ $exit_code != $zero ]]; then
    echo "exit code: $exit_code"
    exit $exit_code
fi

# check if file1 has all the permissions
if [[ ! -r $file1 ]]; then  
    err_msg="$err_msg$file1 does not have read permissions.\n"    
    ((exit_code += two**exponent))
fi
((exponent++))
if [[ ! -w $file1 ]]; then
    err_msg="$err_msg$file1 does not have write permissions.\n"
    ((exit_code += two**exponent))
fi
((exponent++))
if [[ ! -x $file1 ]]; then
    err_msg="$err_msg$file1 does not have executable permissions.\n"
    ((exit_code += two**exponent))
fi

# check if file2 has all the permissions
((exponent++))
if [[ ! -r $file2 ]]; then  
    err_msg="$err_msg$file2 does not have read permissions.\n"
    ((exit_code += two**exponent))
fi
((exponent++))
if [[ ! -w $file2 ]]; then
    err_msg="$err_msg$file2 does not have write permissions.\n"
    ((exit_code += two**exponent))
fi
((exponent++))
if [[ ! -x $file2 ]]; then
    err_msg="$err_msg$file2 does not have executable permissions.\n"
    ((exit_code += two**exponent))
fi

# check if file3 has all the permissions
((exponent++))
if [[ ! -r $file3 ]]; then  
    err_msg="$err_msg$file3 does not have read permissions.\n"
    ((exit_code += two**exponent))
fi
((exponent++))
if [[ ! -w $file3 ]]; then
    err_msg="$err_msg$file3 does not have write permissions.\n"
    ((exit_code += two**exponent))
fi
((exponent++))
if [[ ! -x $file3 ]]; then
    err_msg="$err_msg$file3 does not have executable permissions.\n"
    ((exit_code += two**exponent))
fi

#check if the error code is not zero, indicating we need not attempt the merge
if [[ $exit_code != $zero ]]; then
    echo -e "$err_msg\nCannot perform merge, please check files."
    echo "Merge failed for files: $file1 , $file2 , $file3." >> log.txt
    echo "exit code: $exit_code"
    exit $exit_code
fi

#passing the previous if indicates we can successfully merge
echo "$file1 merged with $file2"

# merge!
echo "====$file1=====" >> $file3
cat $file1 >> $file3
echo -e "\n=====$file2====" >> $file3
cat $file2 >> $file3

echo "$file1 merged with $file2" >> log.txt
exit 0