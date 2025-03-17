-- Authors: Dalton Rogers and Clay Farrell
-- Date: 17 March 2023
-- This is our drop table file which specifies the order in which our tables
-- need to be dropped to be consistent with foreign key constraints.

drop table if exists reserves;
drop table if exists boats;
drop table if exists sailors;