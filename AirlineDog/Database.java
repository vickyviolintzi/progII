package AirlineDog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Database {

	private static String dbURL = "jdbc:derby:derbyDB;create=true";
	
	private static Connection conn = null;
    private static Statement stmt = null;

    private static int number_of_users;
    private static int number_of_locations;
    
    public static void main(String[] args) {
    	createConnection();


    	createUserTable();
    	createLocationsTable();
    	insertIntoUserTable("AirlineDog", "Salami");
    	insertIntoUserTable("Kostakis", "Makaronia");
    	insertIntoUserTable("Vik", "Pastitsio");
    	insertIntoLocationsTable("Paiania", 13, 1);
    	insertIntoLocationsTable("Pagrati", 14, 2);
    	insertIntoLocationsTable("Vourla", 22, 3);
    	printUsersTable();
    	printLocationsTable();
    	deleteTables();
    	shutdownConnection();    
    }
	

    	
	

    public static void createUserTable() {
		 try {
			 stmt = conn.createStatement();
			 stmt.execute("CREATE TABLE USERS (USER_ID INT NOT NULL, USER_NAME VARCHAR(255), PASSWORD VARCHAR(255),PRIMARY KEY (USER_ID) )");
			 stmt.close();
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
    public static void createLocationsTable() {
		 try {
			 stmt = conn.createStatement();
			 stmt.execute("CREATE TABLE LOCATIONS (LOCATION_ID INT NOT NULL, LOCATION VARCHAR(255), TIME INT,USER_ID INT REFERENCES USERS(USER_ID),PRIMARY KEY (LOCATION_ID))");
			 stmt.close();
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
    
    public static void deleteTables() {
    	try {
    		stmt = conn.createStatement();
    		stmt.execute("DROP TABLE LOCATIONS ");
    		stmt.execute("DROP TABLE USERS");
    		stmt.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void insertIntoUserTable(String User_name, String Password) {
	        number_of_users++;
	        int id = number_of_users;
    		try {
	            stmt = conn.createStatement();
	            stmt.execute("INSERT INTO USERS" + " VALUES ("+ id + ",'" + User_name + "','" + Password +"')");
	            stmt.close();
	        } catch (SQLException sqlExcept) {
	            sqlExcept.printStackTrace();
	        }
	    }
    
    public static void insertIntoLocationsTable(String location, int time, int user_id) {
		number_of_locations++;
		int id =  number_of_locations;
    	try {
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO LOCATIONS" + " VALUES ("+ id + ",'" + location + "'," + time +","+user_id+")");
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    public static void printUsersTable() {
	        try {
	            stmt = conn.createStatement();
	            ResultSet results = stmt.executeQuery("select * from USERS");
	            ResultSetMetaData rsmd = results.getMetaData();
	            int numberCols = rsmd.getColumnCount();
	            for (int i=1; i<=numberCols; i++)
	            {
	                //print Column Names
	                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
	            }

	            System.out.println("\n------------------------------------------------");

	            while(results.next())
	            {
	                int id = results.getInt(1);
	                String Name = results.getString(2);
	                String pass = results.getString(3);
	                System.out.println(id + "\t\t" + Name + "\t\t" + pass);
	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
	    }
    
    public static void printLocationsTable() {
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from LOCATIONS");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-----------------------------------------------------------------------");

            while(results.next())
            {
                int id = results.getInt(1);
                String Location = results.getString(2);
                int time = results.getInt(3);
                int user_id = results.getInt(4);
                System.out.println(id + "\t\t\t" + Location + "\t\t\t" + time + "\t\t" + user_id);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    public static void createConnection() {
			try {
				Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
	            //Get a connection
	            conn = DriverManager.getConnection(dbURL); 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
    public static void shutdownConnection() {
		        try {

		            if (stmt != null) {
		                stmt.close();
		            }
		            if (conn != null) {
		                conn.close();
		            }     
		        	DriverManager.getConnection("jdbc:derby:;shutdown=true");
		        }catch (Exception e) {
		            System.out.println("database shutdown");
		        }
		    }
}