import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

public class Driver {
	
	private final static String MIDIdirectory = "MidiFiles/";
	private static boolean chordTest = false;
	
	private static boolean testCF = false;
	private static boolean quitAfterCF = false;
	
	private static boolean test1S = false;
	private static boolean run1S = true;
	
// !!!TESTING: 
	
	//VANILLA CF AEOLIAN
	//test tritone resolution
//	private static int[] testCFMelody =   {0, 2, 1, 3, 2, 4, 5, 4, 3, 2, 1, 0 }; 
//	private static int[] test1SMelody = {7, 6, 8, 7, 9, 8, 7, 6, 5, 6, 6,  7,  };  //climax fail ionian
//	private static int[] test1SMelody = {7, 10, 9, 7, 6, 8, 7, 6, 7 }; //works
	
	//FAILING LEAP CHECK
//	private static int[] testCFMelody =   {0, -2, -5, -4, 0, -1, 2, 0, -1, 1, 0}; // has passed leap check. 

	//dual climax check
//	private static int[] testCFMelody =   {0, -2, 0, 1, 4, 3, -1, 0, 1, 0}; //climax on same note? Forgot mode keep getting tritone
//	private static int[] test1SMelody  = {0, 3, 2, 6, 9, 8, 8, 7, 6, 7 }; 
	
	//Lydian Test
//	private static int[] testCFMelody =   {0, -2, 1, 2, -1, 0, 2, 1, 0}; //lydian A CF

	//Aeolian crossing voices test
//	private static int[] testCFMelody =   {0, 5, 4, 1,  0, 3,  4,  5,  -2, -1, 0, 1, 0}; //Aeolian 1st note voice cross, 
//	private static int[] test1SMelody  =  {4, 7, 8, 10, 9, 12, 11, 10, 7, 6, 5, 6, 7 };  //also need to raise leading tone in final bar.
	//aeolian weird test
//	private static int[] testCFMelody =   {0, -2, 0, 1, 4, 3, 2, 1, 2, 3, 1, 0}; //tritone interval into melodic ascent, 
//	private static int[] test1SMelody  =  {4, 5, 5, 10, 9, 8, 11, 10, 9, 5, 6, 7 };  //also need to raise leading tone in final bar.

	//aeolian melodic test
	private static int[] testCFMelody =   {0, 2, 1, 3, 2, 4, 5, 4, 3, 1, 0 }; //Andrew's cantus firmus, gets melodic minor
	private static int[] test1SMelody  =  {0, 4, 6, 5, 6, 9, 7, 6, 5, 6, 7};  //also need to raise leading tone in final bar.
	
	
//	private static int[] testCFMelody =   {0, -2, -4, -3, -2, 2, 1, 0, 1, 3,2, 1, 0 }; //tritone on first note not resolving inward (lydian)
//	private static int[] test1SMelody  =  {4, 3, 5, 6, 5, 7, 6, 7, 5, 8, 7, 6, 7};  //also need to raise leading tone in final bar.
	
//	private static int[] testCFMelody =   {0, -2, 1, 2, -1, 0, 2, 1, 0 }; //tritone on first note not resolving inward (lydian)
//	private static int[] test1SMelody  =  {};  
	private static int[] test2SMelody  = {7,  8, 9, 8,10, 9, 7, 5, 6, 7, 9,10,11, 4, 5, 6,7}; 
	private static double[] test2SLengths = {.5,.5,.5,.5,.5,.5,.5,.5,.5,.5,.5,.5,.5,.5,.5,.5,1};
	
/**
 * Set the mode of the fuxian counterpoint melody. 
 */
//	private static Mode mode = Mode.IONIAN;
//	private static Mode mode = Mode.DORIAN;
	private static Mode mode = Mode.PHYRGIAN;
	//TODO LYDIAN NEEDS  A FLATTED fourth to avoid a tritone as a 4th. 
//	private static Mode mode = Mode.LYDIAN;
//	private static Mode mode = Mode.MIXOLYDIAN;
// 	private static Mode mode = Mode.AEOLIAN;
// 	private static Mode mode = Mode.LOCRIAN;
 	
	static File fout = new File("cantiFirmi.txt");
	
	private static ArrayList <CantusFirmus> generatedCantusFirmi = new ArrayList<CantusFirmus>();
	private static int cantusFirmusCount = 0;
	private static int firstSpeciesCount = 0;
	private static int secondSpeciesCount = 0;
	private static int cfW1s = 0;
	static FileOutputStream fos = null;
	static BufferedWriter bw;

	private static int successCount = 0;
	private static int failCount = 0;
	private static long startTime;
	private static long endTime;
	
	private static int maxWriteCount = 50000000;
	private static ArrayList<SpeciesBuilder> buildChain = new ArrayList<SpeciesBuilder>();

	public static void main(String[] args) {

		setUpOutputFiles();
		startTime = System.currentTimeMillis();
		log("startTime:" + startTime);
		
		if(chordTest) {
			CounterpointArchitect cpa = new CounterpointArchitect(SpeciesType.MUGUNGRAL_BASE_CHORD);
		} else {
			runSpeciesBuilder();
		}
		closeOutputFiles();
	}
	
	/**
	 * A species is simply a musical melody. The species builder is the active melody building class that collects notes and 
	 * actively checks all the rules for whatever type of melody we are creating. This is what ultimately decides if 
	 * each note will be kept or discarded from the current melody. 
	 */
	private static void runSpeciesBuilder() {
		SpeciesBuilder patientZero;
		
		if(testCF) {
			TestMelody testMelody = new TestMelody(testCFMelody, NoteLength.WHOLE_NOTE);
			patientZero = new SpeciesBuilder(mode, SpeciesType.CANTUS_FIRMUS, testMelody);
			log("setting test Melody");
			//patientZero.setCFTestMelody(testCFMelody);
		} else {
			patientZero = new SpeciesBuilder(mode, SpeciesType.CANTUS_FIRMUS, null);
		}
		buildChain.add(patientZero);
		

		for(int i: patientZero.getNextValidIndexArrayRandomized()) {
			log("Did we getinside the loop");
			SpeciesBuilder cantusFirmusBuilder = new SpeciesBuilder(patientZero);
			if(cantusFirmusBuilder.checkAndSetFirstNote(i)) {
				buildChain.add(cantusFirmusBuilder);
				recursiveMelodySequencer(buildChain);				
			}
		}
		log("" + generatedCantusFirmi.size() + " cantus firmi were completed.");
		
	}

	/**
	 * @param buildChain
	 * 
	 * To sequentially build a melody, we start with a first note (the tonic) and based on the parameters of our 
	 * type of melody, we will check every single next potential note to see if the melody would still be valid. 
	 * We continue to check all potential notes until the melody concludes. 
	 * Thus, recursion is needed for this because if we find a bad note, we only need to rewind to the previous note and 
	 * continue checking so we can determine every possible variation. 
	 * Every fragment of a melody is saved as a Species Builder
	 */
	private static void recursiveMelodySequencer(ArrayList<SpeciesBuilder> buildChain) {
		
		SpeciesBuilder currentCFB = buildChain.get(buildChain.size()-1);
		ArrayList<Integer> nextValidIndexes = currentCFB.getNextValidIndexArrayRandomized();
		for (int i : nextValidIndexes) {
			//log("Current cf: " + currentCFB.getNotes().toString() + " current testIndex: " + i);
			if (currentCFB.testAsNextIndex(i)) {
				//log("Add next  I to: " + currentCFB.getNotes());
				SpeciesBuilder newCFB = new SpeciesBuilder(currentCFB);
				if (newCFB.addIntervalAndCheckForCompletion(newCFB.nextInterval)) {
					successCount++;
					writeCantusFirmus(newCFB);
						
				} else {
					buildChain.add(newCFB);
					recursiveMelodySequencer(buildChain);
				}
			} else {
				failCount++;
			}
		}
		buildChain.remove(buildChain.size() - 1);
	}
	


	private static void writeCantusFirmus(SpeciesBuilder cf) {
		
			log("Found CF: " + cf.getNotes().getAll() + "Success% " + (double)successCount/(double)failCount + " Success Total: " + successCount + " Failtotal" + failCount);
			try {
				bw.write("Success% ");
				//bw.write("Success% " + (double)successCount/(double)failCount + " Success Total: " + successCount + " Failtotal" + failCount);
				//bw.write("/n Notes:" + cf.getNotes().toString() + "  Mode: " + cf.getModeName());
				//bw.write("/n Steps:" + cf.getStepIndexes().toString());
				cantusFirmusCount++;
			} catch (IOException e) {
				log("fail to write success stats ");
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				log("fail to create new line");
				e.printStackTrace();
			}
			
			CantusFirmus cfx = new CantusFirmus(cf, test1S);
			if(!quitAfterCF) {
				/*
				 * A Cantus Firmus is a primary starting melody that follows fauxian counterpoint rules. 
				 * 
				 * A First Species is a seconday melody on top of the first. When running First Species (1S) we will 
				 * determine ALL valid first species that fit with the Cantus Firmus. 
				 * 
				 */
				if(run1S) {
					runFirstSpecies(cfx);
				}
			}
			
			/*
			 * Once all the notes have been determined, create a MIDI file so the melody can 
			 * be further manipulated with music editing software
			 */
			 cfx.createMIDIfile(MIDIdirectory, generatedCantusFirmi.size() + " Master");
			 
			 //Metrics ensuring we haven't suddenly drastically decreased the # of valid melodies
			 endTime = System.currentTimeMillis();
			 long totalTime = endTime-startTime;
			 log("totalTime:" + totalTime);
			 double CF1s = (double)firstSpeciesCount/(double)cantusFirmusCount;
			 long timePerCF = totalTime/cantusFirmusCount;
			 log("1S per CF" + CF1s + " Empty CF#: " + cfW1s + " timePerCF:" + timePerCF);
			 /*
			  * Exit here to quit after 1 Cantus Firmus and its melodies has been found. Otherwise this program will
			  * run for a long time and create thousands of MIDI files. 
			  */
			 
			 //System.exit(1);
			
			try {
				bw.newLine();
			} catch (IOException e) {
				log("fail to create new line");
				e.printStackTrace();
			}
	}
	
	/**
	 *
	 * @param cfx
	 * 
	 * A First Species is actually the second melody that fits over our base melody.
	 * 
	 * When First Species is turned on, we generate every secondary melody that is valid for our initial Cantus Firmus
	 * and send it to a MIDI file for further analysis.
	 */
	private static void runFirstSpecies(CantusFirmus cfx) {
		if (test1S) {
			TestMelody testMelody = new TestMelody(test1SMelody, NoteLength.WHOLE_NOTE);
			cfx.setChildSpeciesTest(testMelody);
		} 
		
		cfx.generateSpecies(SpeciesType.FIRST_SPECIES);				
		firstSpeciesCount += cfx.firstSpeciesList.size();
		log("firstSpeciesTotalCount:" + firstSpeciesCount);
		
		log("CF # " + cantusFirmusCount+ " With " + cfx.firstSpeciesList.size() + " first species");
		
		if(cfx.firstSpeciesList.size() > 0) {
			generatedCantusFirmi.add(cfx);
			cfW1s++;
		}	
	}

	private static void createMIDIDirectory() throws IOException {
		 File file = new File("MidiFiles");
		 if (file.exists() ) {
			 deleteFolder(file);
		 }
		 if (file.mkdir()) {
		     try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 } else {
		     try {
				throw new IOException("Failed to create directory " + file.getParent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
	}
	
	private static void setUpOutputFiles() {
		
		try {
			fos = new FileOutputStream(fout);
		} catch (FileNotFoundException e) {
			log("failed to create file output stream");
			e.printStackTrace();
		}
		
		bw = new BufferedWriter(new OutputStreamWriter(fos));
		try {
			createMIDIDirectory();
		} catch (IOException e1) {
			log("failed to create midi directory");
			e1.printStackTrace();
		}
	}
	
	public static void deleteFolder(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	    			
	    		   file.delete();
	    			
	    		}else{
	        	   String files[] = file.list();
	     
	        	   for (String temp : files) {
	        	      //construct the file structure
	        	      File fileDelete = new File(file, temp);
	        		 
	        	      //recursive delete
	        	     deleteFolder(fileDelete);
	        	   }
	        		
	        	   //check the directory again, if empty then delete it
	        	   if(file.list().length==0){
	           	     file.delete();
	        	   }
	    		}
	    		
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    	}
	}
	
	private static void closeOutputFiles() {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void log(String msg) {
		System.out.println("Driver-Log:           " + msg);
	}
}
