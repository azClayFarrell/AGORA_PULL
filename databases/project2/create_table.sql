--Authors: Dalton Rogers and Clay Farrell
--Date: 17 March 2023
--This is our create table file in which we specifiy what attributes
--our tables will have, identifying foreign and primary keys and specify the
--order in which our tables need to be created. 


drop table if exists reserves;
drop table if exists boats;
drop table if exists sailors;

create table sailors(
    sid int,
    sname varchar(20),
    rating int,
    age float(24),
    primary key (sid));

create table boats(
    bid int,
    bname varchar(20),
    color varchar(20),
    primary key (bid));

create table reserves(
    sid int,
    bid int, 
    day date,
    primary key (sid, bid),
    foreign key (bid) references boats,
    foreign key (sid) references sailors);