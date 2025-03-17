/* Populate the sail database for mongodb in project 5.
 @author Mark Holliday
 @date 8 April 2023
*/

db.sailors.deleteMany({});
db.boats.deleteMany({});
db.reserves.deleteMany({});

db.sailors.insertMany( [
  {"sid": 22, "sname": "Dustin", "rating": 7, "age": 45.0 },
  {"sid": 29, "sname": "Brutus", "rating": 1, "age": 33.0 },
  {"sid": 31, "sname": "Lubber", "rating": 8, "age": 55.5 },
  {"sid": 32, "sname": "Andy", "rating": 8, "age": 25.5 },
  {"sid": 58, "sname": "Rusty", "rating": 10, "age": 35.0 },
  {"sid": 64, "sname": "Horatio", "rating": 7, "age": 35.0 },
  {"sid": 71, "sname": "Zorba", "rating": 10, "age": 16.0 },
  {"sid": 74, "sname": "Horatio", "rating": 9, "age": 35.0 },
  {"sid": 85, "sname": "Art", "rating": 3, "age": 25.5 },
  {"sid": 95, "sname": "Bob", "rating": 3, "age": 63.5 },
]);

db.boats.insertMany( [
  {"bid": 101, "bname": "Interlake", "color": "blue"},
  {"bid": 102, "bname": "Interlake", "color": "red"},
  {"bid": 103, "bname": "Clipper", "color": "green"},
  {"bid": 104, "bname": "Marine", "color": "red"}
]);

db.reserves.insertMany( [
  {"sid": 22, "bid": 101, "day": "10/10/16"},
  {"sid": 22, "bid": 102, "day": "10/10/16"},
  {"sid": 22, "bid": 103, "day": "10/8/16"},
  {"sid": 22, "bid": 104, "day": "10/7/16"},
  {"sid": 31, "bid": 102, "day": "11/10/16"},
  {"sid": 31, "bid": 103, "day": "11/6/16"},
  {"sid": 31, "bid": 104, "day": "11/12/16"},
  {"sid": 64, "bid": 101, "day": "9/5/16"},
  {"sid": 64, "bid": 102, "day": "9/8/16"},
  {"sid": 74, "bid": 103, "day": "9/8/16"},
]);
