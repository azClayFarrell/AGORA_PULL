-- Authors: Dalton Rogers and Clay Farrell
-- Date: 17 March 2023
-- This is our copy table file in which we copy the data from files and 
-- populate the tables that we have created. The order that the data is copied
-- is consistent with the order in which they were created.


\copy sailors from 'sailors_data.txt';
\copy boats from 'boats_data.txt';
\copy reserves from 'reserves_data.txt';