#!/bin/bash

# driver.sh - The autograder
#   Usage: ./driver.sh

# Compile the code
echo "Compiling project"
(make clean; make)
status=$?
if [ ${status} -ne 0 ]; then
    echo "Failure: Unable to compile (return status = ${status})"
    echo "{\"scores\": {\"Correctness\": 0}}"
    exit
fi

# Run the code
echo "Running"
java Driver

exit

