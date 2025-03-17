#!/bin/bash

#sets up new environment variables to be used for servscp.sh to a virtual server

#add alias to your ~/.bashrc file
#alias <name of alias>=". <absolute path to this file>/get_vserver_info.sh"
#once added, enter . ~/.bashrc to make the alias usable


#condition variables
overwriteuser="no"
overwritepath="no"
detecteduser=0
detectedpath=0
#this is needed in case VIRUSER exists and is changed during this script execution
vuser="$VIRUSER"

grep "VIRUSER" ~/.bashrc > /dev/null
founduser=$?
grep "VIRPATH" ~/.bashrc > /dev/null
foundpath=$?
#detect if there are existing variables determine if we are overwriting them
if [[ $founduser == 0 ]]; then
    detecteduser=1
    echo "Detected existing virtual server username env variable." \
            "Enter \"yes\" if you wish to overwrite the information."
    read overwriteuser
else
    echo -e "No existing virtual server username variable found" \
         "\n**************************************************"
fi
if [[ $foundpath == 0 ]]; then
    detectedpath=1
    echo "Detected existing virtual server path env variable." \
            "Enter \"yes\" if you wish to overwrite the information."
    read overwritepath
else
    echo -e "No existing virtual server path variable found" \
         "\n**********************************************"
fi
#if we are accepting new username and path variables
if [[ $detecteduser == 0 || $detectedpath == 0 || "$overwriteuser" == "yes" || "$overwritepath" == "yes" ]]; then
    #if they existed get rid of the existing code containing references to it
    if [[ $detecteduser == 0 || "$overwriteuser" == "yes" ]]; then
        echo "Enter your new virtual server username (everything before the colon)> "
        read vuser
        vuser="$vuser"
        #delete here so that it cannot be easily interrupted by the user
        sed -i '/VIRUSER/d' ~/.bashrc
        #concat new user variables to .bashrc file
        echo "VIRUSER=$vuser" >> ~/.bashrc
        echo "export VIRUSER" >> ~/.bashrc
    fi
    if [[ $detectedpath == 0 || "$overwritepath" == "yes" ]]; then
        #delete old path variables and get new path variables
        echo "Enter your new virtual server path (everything after the colon)> "
        read vpath
        vpath="$vpath"
        #break the path down into individual components
        path_components=$(echo $vpath | tr "/" "\n")
        count=0
        vpath=""
        for part in $path_components; do
            #if the first argument is ~ change it to the correct home directory
            if [[ $count == 0 && "$part" == "~" ]]; then
                part="home/$(echo $vuser | cut -d@ -f1)"
            fi
            #concat everything back onto the vpath and increment count
            vpath="$vpath/$part"
            ((count++))
        done
        #delete the line here so it cannot be easily interrupted by the user
        sed -i '/VIRPATH/d' ~/.bashrc
        #concat new path variables to .bashrc file
        echo "VIRPATH=$vpath" >> ~/.bashrc
        echo "export VIRPATH" >> ~/.bashrc
    fi
    #source .bashrc so that changes take effect, assuming this script was run with source as well
    . ~/.bashrc
else
    #tell the user we are not editing
    echo "Exiting without editing the virtual server username or path."
fi
