#!/bin/bash

#Script that uses environment variables set up with get_vserver_info.sh
#to eliminate the hastle of connecting with the username and path

#for ultimate smoothness set up ssh keys as well so no passwords are needed

#Add the following to your ~/.bashrc file
#alias <alias name>="<absolute path to this script>/servscp.sh"
#after adding alias, enter . ~/.bashrc to make the alias usable


if [[ $# > 0 ]]; then
    for file in $@; do
        scp -r $file $VIRUSER:$VIRPATH
    done
else
    echo "No command line arguments given"
    echo "Useage: servscp <file name or *> [LIST OF FILES]"
fi
