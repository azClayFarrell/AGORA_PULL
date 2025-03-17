Authors: Colton Brooks, Clay Farrell
Project: 5
Date: 5/5/23
Description: Program that does MongoDB queries using an aggregation pipeline on a
             normalized sailor database


To run our program you will first navigate to the location in which you have
saved the files "proj5_sail_populate-1.js" and "proj5_tester-2.js". Once you
have done this you will log into your MongoDB account. To do this you will
enter:

    mongosh username -u username -p

The "username" is a placeholder and should be replaced with your actual
username for your account. Once entered, you will be prompted for your
password and should enter it at this time. Now that you are in your
MongoDB account, you can populate the database and run the program by
running the following commands in the MongoDB shell:

    load("proj5_sail_populate-1.js")
    load("proj5_tester-2.js")

The program will then carry out the queries and print the answers to
the console.