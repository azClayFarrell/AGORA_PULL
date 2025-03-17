--author: Clay Farrell
--date: 4/17/23
-- SQL file for excercise 3.12 from the book, completed as stored procedures

--helper procedures to extract out longer queries
create or replace procedure get_CS001(class inout varchar(8))
language plpgsql
as $procedure$
begin
    select course_id
    into class
    from course
    where course_id = 'CS-001';
end;
$procedure$;


--part a

\echo '\nCreate a new course  "CS-001", titled "Weekly Seminar",\nwith 1 credit hour in the "Comp. Sci." department\n'

\echo '\ncourse: BEFORE\n==========================================='

select * from course; --shows the table before the insertion on CS-001

create or replace procedure insert_CS001()
language plpgsql
as $procedure$
begin
    insert into course(course_id, title, dept_name, credits)
    values ('CS-001', 'Weekly Seminar', 'Comp. Sci.', 1);
end;
$procedure$;

call insert_CS001();

\echo '\ncourse: AFTER\n==========================================='
select * from course; --shows the table after the insertion of course


-- part b

\echo '\nCreate a section of the above course in Fall 2017, with sec_id of 1, and'
\echo 'with the location of this section not yet specified.\n'

\echo '\nsection: BEFORE\n==========================================='
select * from section; --shows the section table before insertion

create or replace procedure make_section() --makes stored procedure
language plpgsql
as $procedure$
declare
    class varchar(8);
begin
    call get_CS001(class);
    insert into section(course_id, sec_id, semester, year, building, room_number, time_slot_id)
    values (class, '1', 'Fall', 2017, null, null, null);
end;
$procedure$;

call make_section(); --calls stored prodedure

\echo '\nsection: AFTER\n==========================================='
select * from section; --shows table after insertion of section



--part c

\echo '\nEnroll every student in the Comp. Sci. department in the above section.\n'

\echo '\ntakes: BEFORE\n==========================================='
select * from takes; -- shows the students and the classes they are taking before procedure

create or replace procedure enroll_comp_sci()
language plpgsql
as $procedure$
declare
    stu record;
begin
   
    for stu in select ID --the result set we need to loop through
            from student 
            where dept_name = 'Comp. Sci.'
    loop
        with sec as (select * from section 
                    where course_id = 'CS-001' and sec_id = '1'
                    and semester = 'Fall' and year = 2017)
        insert into takes(ID, course_id, sec_id, semester, year, grade)
        values(stu.ID, (select course_id from sec), (select sec_id from sec),
            (select semester from sec), (select year from sec), null);
    end loop;
        
end;
$procedure$;

call enroll_comp_sci(); --call to enroll the students in the comp. sci department into the new section 

--\echo '\nEnrolling students into CS-001...'
--\echo '@affected rows added\n'


\echo '\ntakes: AFTER\n==========================================='
select * from takes;



--part d

\echo '\nDelete enrollments in the above section where the students ID is 12345\n'

\echo '\ntakes: BEFORE\n==========================================='
select * from takes; --shows the table for takes

create or replace procedure delete_student_12345() -- procedure for removing the student with ID 12345
language plpgsql
as $procedure$
begin
    delete from takes where ID = '12345' and course_id = 'CS-001' and sec_id = '1' 
                                         and semester = 'Fall' and year = 2017;
end;
$procedure$;

call delete_student_12345(); --calls the procedure for removing the student

\echo '\ntakes: AFTER\n==========================================='
select * from takes; --shows the table for takes after removal
