-- use the psql command
-- \i proj2_tester.sql
-- to load and run this batch file

-- Project Two: Tester
-- Use the sail database
-- @author Mark Holliday, Dalton Rogers and Clay Farrell
-- @version 18 January 2022, 17 March 2023

\echo 'Problem 1: Find the names of sailors who have reserved boat 103.'

\echo '\nResult should be:\n sname\n---------\n Dustin\n Horatio\n Lubber\n'


select distinct sname
from sailors
where sid in (select sid
              from reserves
              where bid = 103);
order by sname asc;


\echo '\nProblem 2: Find the name of sailors who have reserved a red boat.'

\echo '\nResult should be:\n sname\n------------\n Dustin\n Horatio\n Lubber\n'

select sname
from sailors
where sid in (select sid
              from boats natural inner join reserves
              where color = 'red')
order by sname asc;

\echo '\nProblem 3: Find the names of sailors who have not reserved a red boat.'

\echo -n 'Result should be:'
\echo -n '\n sname\n---------\n Andy\n Art\n Bob\n Brutus\n Horatio\n Rusty\n Zorba\n'


select distinct sname
from sailors
where sid not in (select sid
                  from reserves natural join boats
                  where color = 'red')
order by sname asc;


\echo '\nProblem 4: Find the names of sailors who have reserved boat number 103.'
\echo 'Yes, this is the same query as Problem 1 above but'
\echo 'your answer must be substantially different with respect to the form of '
\echo 'the nested subquery and thconnective that you use.'

\echo '\nResult should be:\n sname\n---------\n Dustin\n Horatio\n Lubber\n'

select sname
from sailors natural inner join reserves
where bid < all (select bid
                 from reserves
                 where bid > 103)
  and bid > all (select bid
                 from reserves
                 where bid < 103)
order by sname asc;


\echo '\nProblem 5: Find the names and ids of the sailors whose rating is better than '
\echo -n 'some sailor called Horatio.'

\echo -n '\nResult should be:\n sname\t\tsid\n-------------------\n Andy\t\t32\n Horatio\t\t74\n'
\echo 'Lubber\t\t31\n Rusty\t\t58\n Zorba\t\t71\n'


select distinct sname, sid
from sailors
where rating > any (select rating
                    from sailors
                    where sname = 'Horatio')
order by sname asc;


\echo '\nProblem 6: Find the names and ids of the sailors whose rating is better than '
\echo -n 'all the sailors called Horatio.'

\echo '\nResult should be:\n sname\tsid\n----------\n Rusty\t58\n Zorba\t71\n'



select sname, sid
from sailors
where rating > all (select rating
                    from sailors
                    where sname = 'Horatio')
order by sname, sid asc;


\echo '\nProblem 7: Find the names and ids of the sailors with the highest rating.'

\echo '\nResult should be:\n sname\tsid\n---------\n Rusty\t58\n Zorba\t71\n'

\echo 'replace this line (including \echo) with your query'


\echo '\nProblem 8: Find the names of sailors who have reserved both a red and a green boat.'

\echo '\nResult should be:\n sname\n----------\n Dustin\n Lubber\n'

with red_boats as (select sid
                   from boats natural inner join reserves
                   where color = 'red')
select distinct sname
from sailors, red_boats
where sailors.sid = red_boats.sid and
      red_boats.sid in (select sid
                        from boats natural inner join reserves
                        where color = 'green')
order by sname asc;

\echo '\nProblem 9: Find the names of sailors who have reserved both a red and a green boat.'
\echo 'Yes, this is the same query as the previous query above but'
\echo 'your answer must be substantially different with respect to the form of '
\echo 'the nested subquery that you use.'

\echo '\nResult should be:\n sname\n----------\n Dustin\n Lubber\n'

\echo 'replace this line (including \echo) with your query'

\echo '\nProblem 10: Find the names of sailors who have reserved all boats.'

\echo '\nResult should be:\n sname\n----------\n Dustin\n'

select sname
from sailors as S
where 3 < (select count(distinct R.bid)
           from reserves as R
           where S.sid = R.sid)
order by sname asc;


\echo '\nProblem 11: Find the names of sailors who have reserved all boats.'
\echo -'Same query as the previous one but '
\echo -n 'your answer must be a substantively different nested subquery.'

\echo '\nResult should be:\n sname\n----------\n Dustin\n'

\echo 'replace this line (including \echo) with your query'


