/**
Example use of JDBC to connect to a PostgrSQL database
@author Mark Holliday, Brayan Mauricio Gonzalez
@version 25 January 2022, 13 April 2022
*/
import java.sql.*;
import java.io.*;

public class PostgreSQLJDBCTest {
    
    private Connection conn = null; /* connection to the database */
    private Statement stmt = null; /* uses to perform SQL statements on the database */
    private ResultSet rset = null; /* holds the results of a query */

    /** 
     * Constructor for the driver.
     */
    public PostgreSQLJDBCTest() {
        this.setup();
    }

    /**
     * Sets up and establishes a connection to the database 
     */
    private void setup() {
        try { 
            Class.forName("org.postgresql.Driver"); 
        } catch (Exception e) { 
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
        } catch (SQLException e) { 
            System.out.println("Exception: " + e.getMessage()); 
            System.exit(0); 
        }
    }

    /**
     * Prompts the user to login.
     */
    private String userLogin() {
        Console console = System.console();
        
        String serverIp = "localhost:5432";

        System.out.print("Enter your username: ");
        String username = console.readLine();

        System.out.print("Enter the database name: ");
        String databasename = console.readLine();


        System.out.print("Enter your password: ");
        String password = new String(console.readPassword());

        return "jdbc:postgresql://" + serverIp + "/" + databasename + 
            "?user=" + username + "&password=" + password;
    }

    /**
     * Execute and display the first query of the example.
     * @throws SQLException If anything goes wrong with the SQL of the code.
     */
    private void showRestaurants() throws SQLException {
        String prompt = "\nFind all the information about all the restaurants.";
        System.out.println(prompt);

        String query = "select * from restaurant;";
        this.stmt = this.conn.createStatement();
        this.rset = this.stmt.executeQuery(query); 
 
        while( rset.next( ) ) { 
            System.out.print( rset.getString( 1 ) + "\t"); 
            System.out.print( rset.getString( 2 ) + "\t"); 
            System.out.print( rset.getString( 3 ) + "\t"); 
            System.out.println( rset.getDouble( 4 ) ); 
        } 
        stmt.close(); 
        rset.close(); 
    }


    /**
     * Execute and display the second query of the example.
     * @throws SQLException If anything goes wrong with the SQL of the code.
     */
    private void addRestaurant() throws SQLException {     
        String prompt = "\nAdd one new customer.";
        System.out.println(prompt);

        PreparedStatement pstmt = this.conn.prepareStatement( 
                "insert into restaurant values (?, ?, ?, ?)"); 
        pstmt.setString(1, "004"); 
        pstmt.setString(2, "Mesquite Grill"); 
        pstmt.setString(3, "Sylva"); 
        pstmt.setDouble(4, 40.00); 
        pstmt.execute(); 
        pstmt.close(); 
    }

    /**
     * Performs all parts of the exercise.
     */
    public void go() {
        try {
            this.showRestaurants();
            this.addRestaurant();
            this.showRestaurants();

            this.conn.close();
        } catch (SQLException e) { 
            System.out.println("Exception: " + e.getMessage()); 
            System.exit(0); 
        }
    }
 

    /**
     * Creates the driver object and calls go() to start the program.
     */
    public static void main(String args[]) { 
        PostgreSQLJDBCTest example = new PostgreSQLJDBCTest();
        example.go();
    } // end of main 
}
