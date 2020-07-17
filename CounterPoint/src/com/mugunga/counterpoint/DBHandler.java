package com.mugunga.counterpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBHandler {
	private Connection dbConnection = null;
	private final String JDBCURL = "jdbc:mysql://localhost:3306/mugunga?useSSL=false";
	private final String DBUSER = "gituser";
	private final String DBPASSWORD = "gituser1";
	private final String TIMEZONE = "UTC";
	private boolean storeMelodies = true;
	
	public DBHandler(boolean storeMelodies) {
		this.storeMelodies = storeMelodies;
	}
	
	void setup() {
		
	      try {
	         Properties info = new Properties();
	         info.setProperty("user", DBUSER);
	         info.setProperty("password", DBPASSWORD);
	         info.setProperty("serverTimezone", TIMEZONE);
	         System.out.println("Connecting to database: "+JDBCURL);
	         dbConnection=DriverManager. getConnection(JDBCURL, info);
	         System.out.println("Connection is successful.");
	         Statement stmt = dbConnection.createStatement();
	         stmt.close();
	      }
	      catch(Exception e) {
	    	 this.storeMelodies = false;
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

	public void insertCantusFirmus(CantusFirmus cfx) {		
		if(storeMelodies) {
			Statement q;
			try {
				q = dbConnection.createStatement();
				q.executeUpdate("INSERT INTO mugunga.cantus_firmi (melody, mode_id) "
						+ "VALUES ('" + cfx.getStepIndexesAsCSV() + "' , " + cfx.getModeID() + ")", 
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = q.getGeneratedKeys();
				rs.next();
				int cfxID = rs.getInt(1);
				cfx.setdbID(cfxID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void insertAllFirstSpeciesForCantusFirmus(CantusFirmus cfx) {
		if(storeMelodies) {
			int cantusFirmusID = cfx.getDBid();
			log("Storing first species melodies for Cantus Firmus #" + cantusFirmusID);
			Statement q;
			try {
				q = dbConnection.createStatement();
				cfx.getFirstSpeciesList().forEach( firstSpecies ->
				insertFirstSpecies(cantusFirmusID, firstSpecies, q));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void insertFirstSpecies(int cantusCirmusID, FirstSpecies firstSpecies, Statement q) {
		try {
			int fsID = q.executeUpdate("INSERT INTO MUGUNGA.FIRST_SPECIES (cantus_firmus_ID, melody) "
					+ "VALUES (" + cantusCirmusID + " , '" + firstSpecies.getStepIndexesAsCSV() + "')", 
					Statement.RETURN_GENERATED_KEYS);
			firstSpecies.setdbID(fsID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void log(String msg) {
		System.out.println("DBLog:                " + msg);
	}
	
}
