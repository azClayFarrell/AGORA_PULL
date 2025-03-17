#!/bin/bash

exit_code=0
exit_code=$(./merge.sh one.txt two.txt output.txt)
exit_code=$?
echo -e "exit code: $exit_code\n"
