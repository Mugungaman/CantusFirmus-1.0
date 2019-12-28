package com.mugunga.counterpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler {
	private Connection dbConnection = null;
	private final String JDBCURL = "jdbc:mysql://localhost:3306/mugunga?useSSL=false";
	private final String DBUSER = "gituser";
	private final String DBPASSWORD = "gituser1";
	private final boolean storeMelodies = true;
	
	public DBHandler() {
		
	}
	
	void setup() {
	      
	      try {
	         System.out.println("Connecting to database..............."+JDBCURL);
	         dbConnection=DriverManager.getConnection(JDBCURL, DBUSER, DBPASSWORD);
	         System.out.println("Connection is successful!!!!!!");
	         Statement stmt = dbConnection.createStatement();
	         stmt.close();
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
	}
	
	void cleanup() {
		try {
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	public int insertCantusFirmus(CantusFirmus cfx) {
		int cfxID = -1;
		if(storeMelodies) {
			Statement q;
			try {
				q = dbConnection.createStatement();
				cfxID = q.executeUpdate("INSERT INTO mugunga.cantus_firmi (melody, mode_id) "
						+ "VALUES ('" + cfx.getStepIndexesAsCSV() + "' , " + cfx.getModeID() + ")", 
						Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cfxID;
	}
	
}
