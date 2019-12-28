package com.mugunga.counterpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Driver {
	
	private static CounterPointRunner cpr;
	private static boolean testCF = false;
	private static boolean quitAfterCF = false;
	private static boolean test1S = false;
	private static boolean run1S = true;
	private static Connection con = null;
	
	private static int[] testBaseMelody =   {0, 2, 1, 3, 2, 4, 5, 4, 3, 1, 0 }; //Andrew's cantus firmus, gets melodic minor
	private static int[] test1SMelody  =  {0, 4, 6, 5, 6, 9, 7, 6, 5, 6, 7};  //also need to raise leading tone in final bar.
	

	public static void main(String[] args) {
		
		dbSetup();
		
		cpr = new CounterPointRunner(SpeciesSystem.FUXIAN_COUNTERPOINT);
		if(testCF) {
			cpr.setTestBaseMelody(new TestMelody(testBaseMelody,NoteLength.WHOLE_NOTE));
		} else {
			cpr.setTargetBaseSpeciesCount(5);
		}
		if(test1S) {
			cpr.setTestFirstSpeciesMelody(new TestMelody(test1SMelody,NoteLength.WHOLE_NOTE));
		}
		cpr.setMode(Mode.DORIAN);
		cpr.setDBConnection(con);
		cpr.generateMusic();
		
		dbCleanup();
	}
	
	
	private static void dbCleanup() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}


	private static void dbSetup() {
		String JdbcURL = "jdbc:mysql://localhost:3306/mugunga?useSSL=false";
	      String Username = "gituser";
	      String password = "gituser1";
	      
	      try {
	         System.out.println("Connecting to database..............."+JdbcURL);
	         con=DriverManager.getConnection(JdbcURL, Username, password);
	         System.out.println("Connection is successful!!!!!!");
	         Statement stmt = con.createStatement();
	         stmt.close();
	         
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
	}


	private static void log(String msg) {
		System.out.println("Driver-Log:           " + msg);
	}
}
