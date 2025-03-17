-- Project One: Tester
-- Use the nba database. The expected results are only for the
-- database instance provided. Other database instances with
-- different results may be in evaluating the correctness of 
-- your solutions
-- @author Mark Holliday, Nolan Flinchum, Clay Farrell
-- @version 27 January 2022

\echo 'Problem 1: Find the names of the teams that have had at least one player be a member who also went to college at WCU.'

\echo '\nResult should be:\n  tname\n------------\n Kings\n Rockets\n Thunder\n Timberwolves\n'

SELECT tname
FROM has_played_for, player
WHERE has_played_for.pname = player.pname and player.college = 'WCU'
ORDER BY tname asc;


\echo 'Problem 2: Find the name of each player who has been a member of at least three teams.'

\echo 'Result should be:\n  pname\n------------\n Iguodala\n Malone\n Martin\n Redick\n'

SELECT pname
FROM has_played_for
GROUP BY pname
HAVING COUNT(pname) >= 3
ORDER BY pname asc;


\echo 'Problem 3: Find the average number of players who have played for a team. This query is asking for one number  that is the average over all of the teams of the number of players that have played for that team.'

\echo 'Result should be:\n  average_number_players\n------------\n 1.600000000000\n'

SELECT (COUNT(pname) * 1.0/COUNT(distinct tname)) as average_number_players
FROM has_played_for;


\echo 'Problem 4:Find the names of the players who played the SmallForward position but did not play for the Spurs.'

\echo 'Result should be:\n  pname\n-------------\n Iguodala\n'

(SELECT pname
FROM player
WHERE position = 'SmallForward')
except
(SELECT distinct pname
FROM has_played_for
WHERE tname = 'Spurs');


\echo 'Problem 5: Find the colleges of the players who have played for both the team named the 'Spurs' and the team named 'Bucks'.'

\echo 'Result should be:\n  college\n------------\n Maryland\n'

WITH bucks_and_spurs_players(pname) as(
(SELECT pname
FROM has_played_for
WHERE tname = 'Spurs')
intersect
(SELECT pname
FROM has_played_for
WHERE tname = 'Bucks'))

SELECT college
FROM player, bucks_and_spurs_players
WHERE bucks_and_spurs_players.pname = player.pname;


\echo 'Problem 6: Find the colleges which have had either players who have played for the 'Hornets' or who play the 'Center' position.'

\echo 'Result should be:\n  college\n-------------\n Maryland\n WakeForest\n Wisconsin\n'

SELECT DISTINCT college
FROM player NATURAL INNER JOIN has_played_for
WHERE tname = 'Hornets' OR position = 'Center'
ORDER BY college ASC;

\echo 'Problem 7: Find the position which the largest number of players play. If one than one position is tied for the largest numbers of players, then all the tied positions are in the result table.'


\echo 'Result should be:\n  position\n-------------\n ShootingGuard\n'


SELECT position, count(*)
FROM player NATURAL INNER JOIN has_played_for
GROUP BY position
HAVING count(*) >= ALL count(*)
ORDER BY position ASC;


\echo 'Problem 8: Find the cities of the teams that the player named �Leonard� has played for.'

\echo 'Result should be:\n city\n------------\n SanAntonio\n'

SELECT city
FROM team
WHERE team.tname IN (
SELECT tname
FROM has_played_for
WHERE pname = 'Leonard')
ORDER BY city ASC;



\echo 'Problem 9: Find the college that has had the most players who have played the 'Point Guard' position who have played for at least one team. Note that a player is listed in the players relation does not imply that the have played for any teams.'

\echo 'Result should be:\n college\n------------\n WakeForest\n'


SELECT college, count(*)
FROM player NATURAL INNER JOIN has_played_for
GROUP BY college
HAVING count(*) >= ALL count(*)
WHERE position = 'Point Guard';




\echo 'Problem 10: Find the names of the players who have not played for any team. Note that a player is listed in the players relation does not imply that the have played for any teams.'

\echo 'Result should be:\n pname\n------------\n Smith\n'

(SELECT pname
FROM player)
except
(SELECT DISTINCT pname
FROM player NATURAL INNER JOIN has_played_for)
ORDER BY pname ASC;



\echo 'Problem 11: Find the names of the players taller than every player who went to college at WCU.'

\echo 'Result should be:\n pname\n-----------\n Duncan\n Kaminsky\n Malone\n Parker\n Smith\n'

SELECT pname
FROM player
WHERE height > ALL (
    SELECT height
    FROM player
    WHERE college = 'WCU'
)
ORDER BY pname ASC;
