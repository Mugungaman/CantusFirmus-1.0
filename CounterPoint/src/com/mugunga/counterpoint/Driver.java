package com.mugunga.counterpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Driver {
	
	private static CounterPointRunner cpr;
	private static boolean testCF = false;
	private static boolean quitAfterCF = false;
	private static boolean test1S = false;
	private static boolean run1S = true;
	
	private static int[] testBaseMelody =   {0, 2, 1, 3, 2, 4, 5, 4, 3, 1, 0 }; //Andrew's cantus firmus, gets melodic minor
	private static int[] test1SMelody  =  {0, 4, 6, 5, 6, 9, 7, 6, 5, 6, 7};  //also need to raise leading tone in final bar.
	

	public static void main(String[] args) {
		
//		dbStuff();
//		System.exit(0);
		cpr = new CounterPointRunner(SpeciesSystem.FUXIAN_COUNTERPOINT);
		if(testCF) {
			cpr.setTestBaseMelody(new TestMelody(testBaseMelody,NoteLength.WHOLE_NOTE));
		} else {
			cpr.setTargetBaseSpeciesCount(50);
		}
		if(test1S) {
			cpr.setTestFirstSpeciesMelody(new TestMelody(test1SMelody,NoteLength.WHOLE_NOTE));
		}
		cpr.setMode(Mode.DORIAN);
		
		cpr.generateMusic();
	}
	
	
	private static void dbStuff() {
		String JdbcURL = "jdbc:mysql://localhost:3306/mugunga?useSSL=false";
	      String Username = "gituser";
	      String password = "gituser1";
	      Connection con = null;
	      try {
	         System.out.println("Connecting to database..............."+JdbcURL);
	         con=DriverManager.getConnection(JdbcURL, Username, password);
	         System.out.println("Connection is successful!!!!!!");
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery("SELECT * FROM cantus_firmi");
	         while(rs.next()) {
//	        	 rs.toString();
	        	 System.out.println("rs." + rs.getString(1));
	        	 System.out.println("rs." + rs.getString(2));
	         }
	         
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
//		


//		rs.close();
//		stmt.close();
//		conn.close();

		
	}


	private static void log(String msg) {
		System.out.println("Driver-Log:           " + msg);
	}
}
