/**
 * Project 3 Excercise 3.11 a-d
 * @author Colton Brooks
 */

import java.sql.*;
import java.io.*;

public class Excercise311 {
    
    /* connection to the database */
    private Connection conn;
    /* used to perform SQL statements to the connected database */
    private Statement stmt;
    /* holds the cursor results of a query */
    private ResultSet rset;

    /**
     * Constructor for Excercise311
     */
    public Excercise311() {
        this.setup();
    }

    /**
     * Setup method for the constructor to have high level abstraction
     */
    private void setup() {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + e.getMessage());
            System.exit(0);
        }
        
        this.conn = null;
        this.stmt = null;
        this.rset = null;
        String cs = this.userLogin();

        try {
            this.conn = DriverManager.getConnection(cs);
        }
        catch(SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * User login prompt with hidden password
     */
    private String userLogin() {
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
    
    /**
     * Main method creating an instance of this class and
     * calling the solutions method
     */
    public static void main(String[] args) {
        Excercise311 excercise = new Excercise311();
        excercise.solutions();
    }
    
    /**
     * Calls all of the solution methods
     */
    private void solutions() {
        System.out.println("\n\nExcercise 3.11: Solve the following problems using the " +
            "university schema"); 
        try {
            this.partA();
            this.partB();
            this.partC();
            this.partD();
        }
        catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Method to solve Part A of Excercise 3.11
     */
    private void partA() throws SQLException {
        // showing the user the prompt for the problem
        String prompt = "\nPart A: Find the ID and name of each student who has taken at " +
            "least one Comp. Sci. course; make sure there are no duplicates in the result; " +
            "display rows sorted by student ID.";
        System.out.println(prompt);
        
        // the query to be executed
        String query = "select distinct ID, name " +
                       "from student natural join takes natural join course " +
                       "where dept_name = 'Comp. Sci.' " +
                       "order by ID;";
        // calling the method to create the statement and execute the query
        this.queryExecutor(query);
        // showing the user the metadata for the resulting query
        this.metaDataPrinter();
        
        // printing the solution table for the problem
        while(rset.next()) {
            System.out.print(rset.getString(1) + "\t");
            System.out.println(rset.getString(2));
        }
        // calling the method to close() rset and stmt
        this.closeAll();
    }
    
    /**
     * Method to solve Part B of Excercise 3.11
     */
    private void partB() throws SQLException {
        // showing the user the prompt for the problem
        String prompt = "\nPart B: Find the ID and name of each student who has not taken " +
            "any course offered before 2017; display the rows sorted by student ID.";
        System.out.println(prompt);

        // the query to be executed
        String query = "select distinct ID, name " +
                       "from student natural join takes " +
                       "where ID not in " +
                           "(select distinct ID " +
                           "from takes " +
                           "where year < 2017) " +
                       "order by ID;";
        // calling the method to create the statement and execute the query
        this.queryExecutor(query);
        // showing the user the metadata for the resulting query
        this.metaDataPrinter();

        // printing the solution table for the problem
        while(rset.next()) {
            System.out.print(rset.getString(1) + "\t");
            System.out.println(rset.getString(2));
        }
        // calling the method to close() rset and stmt
        this.closeAll();
    }
    
    /**
     * Method to solve Part C of Excercise 3.11
     */
    private void partC() throws SQLException {
        // showing the user the prompt for the problem
        String prompt = "\nPart C: For each department, find the maximum salary of instructors " +
            "in that department. You may assume that every department has at least one instructor.";
        System.out.println(prompt);

        // the query to be executed
        String query = "select dept_name, max(salary) as max_sal " +
                       "from instructor " +
                       "group by dept_name;";
        // calling the method to create the statement and execute the query
        this.queryExecutor(query);
        // showing the user the metadata for the resulting query
        this.metaDataPrinter();

        // printing the solution table for the problem
        while(rset.next()) {
            System.out.print(rset.getString(1) + "\t");
            System.out.println(rset.getInt(2));
        }
        // calling the method to close() rset and stmt
        this.closeAll();
    }

    /**
     * Method to solve Part D of Excercise 3.12
     */
    private void partD() throws SQLException {
        // showing the user the prompt for the problem
        String prompt = "\nPart D: Find the lowest, across all departments, of the " +
            "per-department maximum salary computed by the preceding query.";
        System.out.println(prompt);

        // the query to be executed
        String query = "with max_sal_table as " +
                           "(select dept_name, max(salary) as salary " +
                           "from instructor " +
                           "group by dept_name) " +
                       "select dept_name, salary " +
                       "from max_sal_table " +
                       "where salary = " +
                           "(select min(salary) " +
                           "from max_sal_table);";
        // calling the method to create the statement and execute the query
        this.queryExecutor(query);
        // showing the user the metadata for the resulting query
        this.metaDataPrinter();

        // printing the solution table foe the problem
        while(rset.next()) {
            System.out.print(rset.getString(1) + "\t");
            System.out.println(rset.getInt(2));
        }
        // calling the method to close() rset and stmt
        this.closeAll();
    }

    /**
     * Method to create a statement via the connection and execute a passed in query
     */
    private void queryExecutor(String query) throws SQLException {
        this.stmt = this.conn.createStatement();
        this.rset = this.stmt.executeQuery(query);
    }

    /**
     * Method to us the close() method of the the stmt and rset fields
     */
    private void closeAll() throws SQLException {
        stmt.close();
        rset.close();
    }
    
    /**
     * Method to print the metadata information of a ResultSetMetaData
     */
    private void metaDataPrinter() throws SQLException {
        ResultSetMetaData rsmd = this.rset.getMetaData();

        System.out.println("\nMetadata for the resulting query:");
        // shwoing the table name for the result set
        System.out.println("Table name: " + rsmd.getTableName(1));
        // number for how many columns / attributes the result set has
        int columnCount = rsmd.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {    // for loop to print each column name neatly
        System.out.println("Column " + i + ": " + rsmd.getColumnName(i));
        }
        System.out.println();
    }
}
