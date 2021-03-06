package gr.aueb.StopSpread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author alexd
 *Database handing class
 */
public class Database {

	private static String dbURL = "jdbc:derby:derbyDB;create=true";
	
	private static Connection conn = null;
    private static Statement stmt = null;
    
    /**
     * @param args
     */
    public static void main(String[] args) {
    	createConnection();


    	/*createUserTable();
    	createLocationsTable();
    	insertIntoUserTable("AirlineDog", "Salami");
    	insertIntoUserTable("Kostakis", "Makaronia");
    	insertIntoUserTable("Vik", "Pastitsio");
    	insertIntoLocationsTable("Paiania","stamoy", 13, 15, 1);
    	insertIntoLocationsTable("Pagrati","fanti", 11, 14, 2);
    	insertIntoLocationsTable("Vourla","shame", 17, 22, 3);*/
    	//deleteTables();
    	//insertIntoUserTable("Eva", "apaapa");
    	//deleteUsersRow();
    	printUsersTable();
    	printLocationsTable();
    	shutdownConnection();    
    }
	

    	
	
    /**Creates Table of users*/
    public static void createUserTable() {
		 try {
			 stmt = conn.createStatement();
			 stmt.execute("CREATE TABLE USERS (USER_ID INT NOT NULL, USER_NAME VARCHAR(30), PASSWORD VARCHAR(30),PRIMARY KEY (USER_ID) )");
			 stmt.close();
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
    
    /**Creates Table of users Locations*/
    public static void createLocationsTable() {
		 try {
			 stmt = conn.createStatement();
			 stmt.execute("CREATE TABLE LOCATIONS (CITY VARCHAR(30), ADDRESS VARCHAR(50), ARRIVAL_TIME INT, DEPARTURE_TIME INT, USER_ID INT REFERENCES USERS(USER_ID))");
			 stmt.close();
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
    
    
    /**Deletes the users and locations tables*/
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
    
    
    /**Inserts rows into users table
     * @param User_name 
     * @param Password */
    public static void insertIntoUserTable(String User_name, String Password) {
    		try {
	            stmt = conn.createStatement();
	            ResultSet results = stmt.executeQuery("SELECT MAX(USER_ID) FROM USERS");
	            results.next();
	            int id = results.getInt(1) + 1;
	            stmt.execute("INSERT INTO USERS" + " VALUES ("+ id + ",'" + User_name + "','" + Password +"')");
	            stmt.close();
	        } catch (SQLException sqlExcept) {
	            sqlExcept.printStackTrace();
	        }
	    }
    
    
    
    /**Inserts rows into locations table
     * @param City 
     * @param Address 
     * @param arrival_time 
     * @param departure_time 
     * @param user_id */
    public static void insertIntoLocationsTable(String City, String Address, int arrival_time,int departure_time, int user_id) {
    	try {
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO LOCATIONS" + " VALUES ('" + City + "','"+Address+"',"+arrival_time+"," + departure_time +","+user_id+")");
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    
    
    /**Prints the users table*/
    public static void printUsersTable() {
	        try {
	            stmt = conn.createStatement();
	            ResultSet results = stmt.executeQuery("SELECT * FROM USERS");
	            ResultSetMetaData rsmd = results.getMetaData();
	            int numberCols = rsmd.getColumnCount();
	            
	            System.out.println("\n--------------------------------------------");
	            for (int i=1; i<=numberCols; i++)
	            {
	                //print Column Names
	                System.out.printf("%-18s", rsmd.getColumnLabel(i));  
	            }
	            System.out.println("\n--------------------------------------------");

	            while(results.next())
	            {
	                int id = results.getInt(1);
	                String Name = results.getString(2);
	                String pass = results.getString(3);
	                System.out.printf("%-18s%-18s%-18s%n", id, Name, pass);
	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
	    }
    
    
    /**Prints the locations table*/
    public static void printLocationsTable() {
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM LOCATIONS");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            
            System.out.println("\n-------------------------------------------------------------------------------");
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.printf("%-18s", rsmd.getColumnLabel(i));  
            }

            System.out.println("\n-------------------------------------------------------------------------------");

            while(results.next())
            {
                String City = results.getString(1);
                String Address = results.getString(2);
                int arrival_time = results.getInt(3);
                int departure_time = results.getInt(4);
                int user_id = results.getInt(5);
                System.out.printf("%-18s%-18s%-18s%-18s%-18s%n",City , Address , arrival_time, departure_time , user_id);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    
    
    /**Initiates connection with the database*/
    public static void createConnection() {
			try {
				Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
	            //Get a connection
	            conn = DriverManager.getConnection(dbURL); 
	            System.out.println("Database connection created");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
    
    
    /**Terminates connection with the database*/
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




    /**
     * @param user_name 
     * @return returns true if user name exists, false if not
     */
	public static boolean usernameCheck(String user_name) {
		try {
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("SELECT USER_NAME FROM USERS");
			while(results.next()) {
				String existingName = results.getString("USER_NAME");
				if (existingName.equals(user_name)) {
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}




	/**Searching for user_id based on user_name
	 *  
	 * @param name 
	 * @return Returns -1 if the user_name does not exist in the database
	 */
	public static int findUsersId(String name) {
		int id = -1;
		try {
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("SELECT USER_ID, USER_NAME FROM USERS");
			while(results.next()) {
				String existingNames = results.getString("USER_NAME");
				if (name.equals(existingNames)) {
					id = results.getInt("USER_ID");
					return id;
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**Serching for user's password based on user_id
	 *
	 * @param user_id 
	 * @return Returs -1 in String format if password not found
	 */
	public static String findUsersPass(int user_id) {
		String pass = "-1";
		try {
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("SELECT PASSWORD FROM USERS WHERE USER_ID=" + user_id);
			results.next();
			pass = results.getString(1);
			return pass;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return pass;
	}
	
	
	/**Deletes row from USERS based on user_id
	 * @param user_id */
	public static void deleteUsersLine(int user_id) {
		try{
			stmt = conn.createStatement();
			stmt.execute("DELETE FROM USERS WHERE USER_ID=" + user_id);
			stmt.close();
			System.out.println("Users Line deleted");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**Deletes row from LOCATIONS based on user_id
	 * @param user_id */
	public static void deleteLocationsLine(int user_id) {
		try{
			stmt = conn.createStatement();
			stmt.execute("DELETE FROM LOCATIONS WHERE USER_ID=" + user_id);
			stmt.close();
			System.out.println("Users Line deleted");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}





}
