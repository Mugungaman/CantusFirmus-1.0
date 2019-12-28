package com.mugunga.counterpoint;

public class Driver {
	
	private static CounterPointRunner cpr;
	private static boolean testCF = false;
	private static boolean quitAfterCF = false;
	private static boolean test1S = false;
	private static boolean run1S = true;
	private static DBHandler dbHandler;
	// storeMelodies tells the dbHandler whether to insert melodies into our database. 
	private static boolean storeMelodies = true;
	
	private static int[] testBaseMelody =   {0, 2, 1, 3, 2, 4, 5, 4, 3, 1, 0 }; //Andrew's cantus firmus, gets melodic minor
	private static int[] test1SMelody  =  {0, 4, 6, 5, 6, 9, 7, 6, 5, 6, 7};  //also need to raise leading tone in final bar.
	

	public static void main(String[] args) {
		
		dbHandler = new DBHandler(storeMelodies);
		dbHandler.setup();
		
		cpr = new CounterPointRunner(SpeciesSystem.FUXIAN_COUNTERPOINT);
		if(testCF) {
			cpr.setTestBaseMelody(new TestMelody(testBaseMelody,NoteLength.WHOLE_NOTE));
		} else {
			cpr.setTargetBaseSpeciesCount(200);
		}
		if(test1S) {
			cpr.setTestFirstSpeciesMelody(new TestMelody(test1SMelody,NoteLength.WHOLE_NOTE));
		}
		cpr.setMode(Mode.DORIAN);
		cpr.setDBHandler(dbHandler);
		cpr.generateMusic();
		
		dbHandler.cleanup();
	}
	
}
