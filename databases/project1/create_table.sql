-- @author Nolan Flinchum and Clay Farrell
-- @version 2/27/2023

create table player(
    pname varchar(20),
    college varchar(20),
    position varchar(20),
    height int,
    primary key (pname));

create table team(
    tname varchar(20),
    city varchar(20),
    primary key (tname));

create table has_played_for(
    pname varchar(20),
    tname varchar(20),
    primary key(pname, tname),
    foreign key (pname) references player,
    foreign key (tname) references team);
