/* Module 4 Mongodb Project 5 Tester
 Mark Holliday, 12 April 2023
 Colton Brooks and Clay Farrell, 5 May 2023
 Modification of the Project 2 queries using MangoDB. There are no duplicate queries and 
 you only need to answer three of the queries.
*/

// Problem 1. 
prompt = "Problem 1. Find the sids and names (in name alphabetical order) of sailors "
    + "who have reserved boat 103.\nExpected Answer:\n{sid: 22, sname: 'Dustin'}\n"
    + "{sid: 74, sname: 'Horatio'}\n{sid: 31, sname: 'Lubber'}\nAnswer\n";
print(prompt);

reserved103 = db.sailors.aggregate([
    {$lookup: {
        from: "reserves",
        localField: "sid",
        foreignField: "sid",
        as: "reserved"
    }},
    {$match: {
        "reserved.bid": 103
    }},
    {$project: {
        "_id": 0,
        "sid": 1,
        "sname": 1
    }},
    {$sort: {
        "sname": 1
    }}
]);

while(reserved103.hasNext()) {
    print(reserved103.next());
}


// Problem 2. 
prompt = "Problem 2. Find the the total number of reservations by each sailor. In particular, "
    + " for each sailor display their sid, name, and total number of reservations "
    + " (in numerical order by sid) if they have at least one reservation. "
    + "\nExpected Answer:\n{sid: 22, sname: 'Dustin', reservations: 4}\n"
    + "{sid: 31, sname: 'Lubber', reservations: 3}\n"
    + "{sid: 64, sname: 'Horatio', reservations: 2}\n"
    + "{sid: 74, sname: 'Horatio', reservations: 1}\nAnswer\n";
print(prompt);

numOfResv = db.sailors.aggregate([
    {$lookup: {
        from: "reserves",
        localField: "sid",
        foreignField: "sid",
        as: "reserved"
    }},
    {$project: {
        "_id": 0,
        "sid": 1,
        "sname": 1,
        "reservations": {$size: "$reserved"}
    }},
    {$match: {
        "reservations": {$gt: 0}
    }},
    {$sort: {
        "sid": 1
    }}
]);

while(numOfResv.hasNext()) {
    print(numOfResv.next());
}


// Problem 3. 
prompt = "Problem 3. Find the sids and names (in numerical order by sid) of sailors whose "
    + "name starts with 'A' or both their rating is greater that 7 and their age is less than 30."
    + "\nExpected Answer:\n{sid: 32, sname: 'Andy'}\n{sid: 71, sname: 'Zorba'}\n"
    + "{sid: 85, sname: 'Art'}\nAnswer\n";
print(prompt);

sailorList = db.sailors.aggregate([
    {$match: {
        $or: [
            {"sname": /^A/},
            {$and: [
                {"rating": {$gt: 7}},
                {"age": {$lt: 30}}
            ]}
        ]
    }},
    {$project: {
        "_id": 0,
        "sid": 1,
        "sname": 1
    }},
    {$sort: {
        "sid": 1
    }}
]);

while(sailorList.hasNext()) {
    print(sailorList.next());
}


// Problem 4. 
prompt = "Problem 4. Find the names (in alphabetical order) of sailors who have reserved "
    + "a red boat.\nExpected Answer:\n{sname: 'Dustin'}\n{sname: 'Horatio'}\n"
    + "{sname: 'Lubber'}\nAnswer\n";
print(prompt);

redBoatSail = db.reserves.aggregate([
    {$lookup: {
        from: "sailors",
        localField: "sid",
        foreignField: "sid",
        as: "sailors"
    }},
    {$lookup: {
        from: "boats",
        localField: "bid",
        foreignField: "bid",
        as: "boats"
    }},
    {$match: {
        "boats.color": "red"
    }},
    {$group: {
        "_id": {"sname": "$sailors.sname"}
    }},
    {$project: {
        "_id": 0,
        "sname": "$_id.sname"
    }},
    {$sort: {
        "sname": 1
    }}
]);

while(redBoatSail.hasNext()) {
    print(redBoatSail.next());
}


// Problem 5. 
prompt = "Problem 5. Find the names (in alphabetical order) of sailors who have not reserved "
    + "a red boat.\nExpected Answer:\n{sname: 'Andy'}\n{sname: 'Art'}\n{sname: 'Bob'}"
    + "\n{sname: 'Brutus'}\n{sname: 'Horatio'}\n{sname: 'Rusty'}\n{sname: 'Zorba'}\n"
    + "Answer\n";
print(prompt);

notRedBoat = db.sailors.aggregate([
    {$lookup: {
        from: "reserves",
        localField: "sid",
        foreignField: "sid",
        as: "reserves"
    }},
    {$lookup: {
        from: "boats",
        localField: "reserves.bid",
        foreignField: "bid",
        as: "boats"
    }},
    {$match: {
        "boats.color": {$ne: "red"}
    }},
    {$group: {
        "_id": {"sname": "$sname"}
    }},
    {$project: {
        "_id": 0,
        "sname": "$_id.sname"
    }},
    {$sort: {
        "sname": 1
    }}
]);

while(notRedBoat.hasNext()) {
    print(notRedBoat.next());
}

// Problem 6. 
prompt = "Problem 6. Find the sid and names (in sid numerical order) of sailors whose "
    + "rating is better than some sailor called Horatio.\n"
    + "Expected Answer:\n{sid: 31, sname: 'Lubber'}\n{sid: 32, sname: 'Andy'}\n"
    + "{sid: 58, sname: 'Rusty'}\n{sid: 71, sname: 'Zorba'}\n{sid: 74, sname: 'Horatio'}\n"
    + "Answer\n";
print(prompt);


horatios = db.sailors.aggregate([
    {$match:{"sname":"Horatio"}},
]).sort({rating:1}).toArray();
//print(horatios)
lowestRating = horatios[0].rating;
//print(lowestRating);
better = db.sailors.aggregate([
    {$match:{"rating":{$gt:lowestRating}}},
    {$project:{
        _id:0,
        "sid":1,
        "sname":1
    }},
    {$sort:{"sid":1}}    
]);
//print(better)
while(better.hasNext()){
    doc = better.next();
    print(doc);
}

// Problem 7. 
prompt = "Problem 7. Find the sid and name (in sid numerical order) of sailors whose rating is "
    + "better than all sailors called Horatio.\n"
    + "Expected Answer:\n{sid: 58, sname: 'Rusty'}\n{sid: 71, sname: 'Zorba'}\nAnswer\n";
print(prompt);

horatios = db.sailors.aggregate([
    {$match:{"sname":"Horatio"}},
]).sort({rating:-1}).toArray();
//print(horatios)
highestRating = horatios[0].rating;
//print(lowestRating);
better = db.sailors.aggregate([
    {$match:{"rating":{$gt:highestRating}}},
    {$project:{
        _id:0,
        "sid":1,
        "sname":1
    }},
    {$sort:{"sid":1}}    
]);
//print(better)
while(better.hasNext()){
    doc = better.next();
    print(doc);
}

// Problem 8. 
prompt = "Problem 8. Find the sid and name (in sid numerical order) of sailors with "
    + "the highest rating.\nExpected Answer:\n{sid: 58, sname: 'Rusty'}\n"
    + "{sid: 71, sname: 'Zorba'}\nAnswer\n";
print(prompt);

maxRating = db.sailors.aggregate([
    {$project:{
        _id:0,
        rating:1
    }},
    {$sort:{"rating":-1}}
]).toArray();
//print(maxRating[0].rating);
maxRating = maxRating[0].rating;
maxList = db.sailors.aggregate([
    {$match:{"rating":{$eq:maxRating}}},
    {$project:{
        _id:0,
        sid:1,
        sname:1
    }},
    {$sort:{"sid":1}}
]);

//print(maxList.hasNext());

while (maxList.hasNext()){
    doc = maxList.next();
    print(doc);
}


// Problem 9. 
prompt = "Problem 9. Find the names (in alphabetical order) of sailors who have reserved "
    + "both a red boat and a green boat.\nExpected Answer:\n{sname: 'Dustin'}\n"
    + "{sname: 'Lubber'}\nAnswer\n";
print(prompt);

red_green_boats = db.boats.aggregate([
    {$match:{$or:
        [{"color":"red"}, {"color":"green"}]
    }},
    
    {$project:{
        _id:0,
        bid:1,
        color:1
    }}
]);

redBoats = [];
greenBoats = [];
while (red_green_boats.hasNext()){
    doc = red_green_boats.next();
    if (doc.color === "green"){
        greenBoats.push(doc.bid);
    }
    if (doc.color === "red"){
        redBoats.push(doc.bid);
    }
}
//print(redBoats);
//print(greenBoats);
rentedBoth = db.sailors.aggregate([
    {$lookup:{
        from:"reserves",
        localField:"sid",
        foreignField:"sid",
        as:"rented"
    }},
    {$match:{"rented.bid":{$in:redBoats}}},
    {$match:{"rented.bid":{$in:greenBoats}}},
    {$project:{
        _id:0,
        sname:1
    }},
    {$sort:{"sname":1}}
]);

//print(rentedBoth);

while(rentedBoth.hasNext()){
    doc = rentedBoth.next();
    print(doc);
}

// Problem 10. 
prompt = "Problem 10. Find the names (in alphabetical order) of sailors who have reserved "
    + "all boats.\nExpected Answer:\n{sname: 'Dustin'}\nAnswer\n";
print(prompt);


allBoatsDocs = db.boats.aggregate([
    {$project:{
        _id:0,
        bid:1,
        color:1
    }}
]);

allBoats = [];
while (allBoatsDocs.hasNext()){
    allBoats.push(allBoatsDocs.next().bid);
}

rentedAll = db.sailors.aggregate([
    {$lookup:{
        from:"reserves",
        localField:"sid",
        foreignField:"sid",
        as:"rented"
    }},
    {$match:{"rented.bid":{$all:allBoats}}},
    {$project:{
        _id:0,
        sname:1
    }},
    {$sort:{"sname":1}}
]);

while(rentedAll.hasNext()){
    doc = rentedAll.next();
    print(doc);
}


