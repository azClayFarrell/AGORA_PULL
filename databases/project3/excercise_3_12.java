/**
 * Project 3 Excercise 3.12 a-d
 * @author Clay Farrell
 * @version 4/4/23
 * does the questions from the book for excercises 3.12 a-d
 */

import java.sql.*;
import java.io.*;

/**
 * the class that holds the logic for doing the excercises a-d from
 * excercise 3.12 in the book
 */
public class excercise_3_12{

    /**connection to the database */
    private Connection conn = null;
    /**used to perform SQL statements to the connected database */
    private Statement stmt = null;
    /**holds the cursor results of a query */
    private ResultSet rset = null;

    /**
     * Constructor for excercise_3_12
     */
    public excercise_3_12(){
        this.setup();
    }

    /**
     * Setup method for the constructor to have high level abstraction
     */
    private void setup(){
        try{
            Class.forName("org.postgresql.Driver");
        }
        catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + e.getMessage());
            System.exit(0);
        }

        this.conn = null;
        this.stmt = null;
        this.rset = null;

        String cs = this.userLogin();
        try{
            this.conn = DriverManager.getConnection(cs);
        }
        catch(SQLException e){
            System.out.println("Exception: " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Prompts the user to login
     */
    private String userLogin(){
        Console console = System.console();

        String serverIp = "localhost:5432";

        System.out.print("Enter your username: ");
        String username = console.readLine();

        System.out.print("Enter the database name: ");
        String dbname = console.readLine();

        System.out.print("Enter your password: ");
        String password = new String(console.readPassword());

        return "jdbc:postgresql://" + serverIp + "/" + dbname +
               "?user=" + username + "&password=" + password;
    }


    private void solutions(){
        try{
            //shows the table for course before and after adding a course
            String prompt = "\n\na) Create a new course \"CS-001\", titled " +
                            "\"Weekly Seminar\", with 1 credits";
            System.out.println(prompt);
            System.out.println("\nBefore\n================================================");
            showCourse();
            this.partA();
            System.out.println("\nAfter\n================================================");
            showCourse();

            //shows the table for section before and after adding a section
            prompt = "\nb) Create a section of the CS-001 course in Fall 2017 " + 
                     "with sec_id of 1, and\nwith the location of this " +
                     "section not yet specified";
            System.out.println(prompt);
            System.out.println("\nBefore\n================================================");
            showSection();
            this.partB();
            System.out.println("\nAfter\n================================================");
            showSection();

            /*enrolls every student in the Comp. Sci. department into
              the CS-001 course*/
            prompt = "\nc) Enroll every student in the Comp. Sci. department in" +
                     " the above section";
            System.out.println(prompt);
            System.out.println("\nBefore\n================================================");
            showTakes();
            this.partC();
            System.out.println("\nAfter\n================================================");
            showTakes();

            /*Deletes enrollments in the above section where the student's 
              ID is 12345*/
            prompt = "\nd) Delete enrollments in the above section where the " +
                     "student's ID is 12345";
            System.out.println(prompt);
            System.out.println("\nBefore\n================================================");
            showTakes();
            this.partD();
            System.out.println("\nAfter\n================================================");
            showTakes();
        }
        catch (SQLException e){
            System.out.println("Exception: " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Creates a new course "CS-001" with title "Weekly Seminar" and 1 credit
     * hour in the "Comp. Sci." department
     */
    private void partA() throws SQLException{
        //inserts into course the values of the preparedStatement
        PreparedStatement insertion = this.conn.prepareStatement(
            "insert into course values (?, ?, ?, ?)");
        insertion.setString(1, "CS-001");
        insertion.setString(2, "Weekly Seminar");
        insertion.setString(3, "Comp. Sci.");
        insertion.setDouble(4, 1.00);
        insertion.execute();
        insertion.close();

    }

    /**
     * Create a section of the CS-001 course in Fall 2017 with sec_id of 1, and
     * with the location of this section not yet specified
     */
    private void partB() throws SQLException{
        //inserts into the section the values of the preparedStatement
        PreparedStatement insertSection = this.conn.prepareStatement(
            "insert into section values (?, ?, ?, ?, ?, ?, ?)");
        insertSection.setString(1, "CS-001");
        insertSection.setString(2, "1");
        insertSection.setString(3, "Fall");
        insertSection.setInt(4, 2017);
        insertSection.setString(5, null);
        insertSection.setString(6, null);
        insertSection.setString(7, null);
        insertSection.execute();
        insertSection.close();
        
    }

    /**
     * Enroll every student in teh Comp. Sci. department in the above section
     */
    private void partC() throws SQLException{

        //the first query for finding all the students in the Comp. Sci. department
        String query = "select ID from student where dept_name = 'Comp. Sci.'";
        this.stmt = this.conn.createStatement();
        this.rset = stmt.executeQuery(query);
        
        //a second query to get the info from the section table
        query = "select * from section where course_id = 'CS-001' " + 
                    "and sec_id = '1' and semester = 'Fall' and year = 2017";
        this.stmt = this.conn.createStatement();
        ResultSet rsetOther = stmt.executeQuery(query);

        //advance the pointer once and no further, so that we are on the record itself
        rsetOther.next();

        PreparedStatement insertTakes = this.conn.prepareStatement(
                      "insert into takes values (?, ?, ?, ?, ?, ?)");
        while(rset.next()){
            
            //insert the ID from the student query
            insertTakes.setString(1, rset.getString(1));

            //insert the rest of the info from the section query
            insertTakes.setString(2, rsetOther.getString(1));
            insertTakes.setString(3, rsetOther.getString(2));
            insertTakes.setString(4, rsetOther.getString(3));
            insertTakes.setInt(5, rsetOther.getInt(4));
            insertTakes.setString(6, null);
            insertTakes.execute();
        }
        insertTakes.close();
        this.stmt.close();
        
    }

    /**
     * Delete enrollments in the above section where the student's ID is 12345
     */
    private void partD() throws SQLException{
        String query = "delete from takes where ID = '12345'";
        this.stmt = this.conn.createStatement();
        int affected = stmt.executeUpdate(query);
        //tells how many rows were affected by the deletion
        System.out.println("\nDeleting rows with ID 12345...\nDone: " +
                             affected + " row(s) affected\n");
        this.stmt.close();
    }

    /**
     * Shows the "course" table
     */
    private void showCourse() throws SQLException{
        //makes a query, updates the stmt, and gets a new rset from the execution
        String query = "select * from course";
        this.stmt = this.conn.createStatement();
        this.rset = this.stmt.executeQuery(query);
        ResultSetMetaData metaData = rset.getMetaData();

        //prints the name of the table and the attribute headers using metaData
        System.out.println("Table Name: " + metaData.getTableName(1));
        System.out.println(metaData.getColumnName(1) + "\t" + 
                           metaData.getColumnName(2) + "\t" +
                           metaData.getColumnName(3) + "\t" +
                           metaData.getColumnName(4));

        //while there are records to be read, print the info
        while(rset.next()){
            System.out.print(rset.getString(1) + "\t");
            System.out.print(rset.getString(2) + "\t");
            System.out.print(rset.getString(3) + "\t");
            System.out.println(rset.getDouble(4));
        }
        System.out.println();
        stmt.close();
        rset.close();
    }

    /**
     * shows the "section" table
     */
    private void showSection() throws SQLException{
        //makes a query, updates the stmt, and gets a new rset from the execution
        String query = "select * from section";
        this.stmt = this.conn.createStatement();
        this.rset = this.stmt.executeQuery(query);
        ResultSetMetaData metaData = rset.getMetaData();

        //prints the name of the table and the attribute headers
        System.out.println("Table Name: " + metaData.getTableName(1));
        System.out.println(metaData.getColumnName(1) + "\t" + 
        metaData.getColumnName(2) + "\t" +
        metaData.getColumnName(3) + "\t" +
        metaData.getColumnName(4) + "\t" +
        metaData.getColumnName(5) + "\t" +
        metaData.getColumnName(6) + "\t" +
        metaData.getColumnName(7));

        //while there are records to be read, print the info
        while(rset.next()){
            System.out.print(rset.getString(1) + "\t");
            System.out.print(rset.getString(2) + "\t");
            System.out.print(rset.getString(3) + "\t");
            System.out.print(rset.getInt(4) + "\t");
            System.out.print(rset.getString(5) + "\t");
            System.out.print(rset.getString(6) + "\t");
            System.out.println(rset.getString(7));
        }
        System.out.println();
        stmt.close();
        rset.close();
    }

    /**
     * Makes a print of the "takes" table
     */
    private void showTakes() throws SQLException{
        //makes a query, updates the stmt, and gets a new rset from the execution
        String query = "select * from takes";
        this.stmt = this.conn.createStatement();
        this.rset = this.stmt.executeQuery(query);
        ResultSetMetaData metaData = rset.getMetaData();

        //prints the name of the table and the attribute headers
        System.out.println("Table Name: " + metaData.getTableName(1));
        System.out.println(metaData.getColumnName(1) + "\t" + 
        metaData.getColumnName(2) + "\t" +
        metaData.getColumnName(3) + "\t" +
        metaData.getColumnName(4) + "\t" +
        metaData.getColumnName(5) + "\t" +
        metaData.getColumnName(6));

        //while there are records to be read, print the info
        while(rset.next()){
            System.out.print(rset.getString(1) + "\t");
            System.out.print(rset.getString(2) + "\t");
            System.out.print(rset.getString(3) + "\t");
            System.out.print(rset.getString(4) + "\t");
            System.out.print(rset.getInt(5) + "\t");
            System.out.println(rset.getString(6));
        }
        System.out.println();
        stmt.close();
        rset.close();
    }

    /**
     * The main method, makes a new instance of this excercise group and calls
     * the solutions() method
     * @param args
     */
    public static void main(String[] args){
        excercise_3_12 excercise = new excercise_3_12();
        excercise.solutions();
    }
}
