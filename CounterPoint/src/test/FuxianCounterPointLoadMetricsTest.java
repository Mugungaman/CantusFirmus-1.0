package test;

import java.io.IOException;
import java.util.ArrayList;

import com.mugunga.counterpoint.CantusFirmus;
import com.mugunga.counterpoint.Mode;
import com.mugunga.counterpoint.NoteLength;
import com.mugunga.counterpoint.SpeciesBuilder;
import com.mugunga.counterpoint.SpeciesType;
import com.mugunga.counterpoint.TestMelody;

/**
 * This Test class is for large batch monitoring of algorithm. When running many Cantus/Firmus, metrics like 
 * Standard Deviation, # of empty Cantus Firmus, # of First Species per Cantus Firmus, etc, should
 * remain steady. A large swing means something drastic happened to the algorithm.
 * 
 *  
 * 
 * 
 * @author laurencemarrin
 *
 */
//TODO Set this class to run a specified # of Melodies and to assert the metrics are in range
public class FuxianCounterPointLoadMetricsTest {
	
	private int successCount = 0;
	private int failCount = 0;
	private long startTime;
	private long endTime;
	private TestMelody testCFMelody;
	private TestMelody test1SMelody;
	private Mode mode;
	private boolean test1S = false;
	//private boolean validCantusFirmus = false;
	private boolean validFirstSpecies = false;
	private int firstSpeciesCount = 0;
	private int cantusFirmusCount = 0;
	private int cfW1s = 0;
	private int targetCantusFirmusCount;
	private ArrayList <CantusFirmus> generatedCantusFirmi = new ArrayList<CantusFirmus>();
	
	public FuxianCounterPointLoadMetricsTest() {
		mode = Mode.IONIAN;
		targetCantusFirmusCount = 250;
	}
	
	public FuxianCounterPointLoadMetricsTest(Mode mode) {
		this.mode = mode;
		targetCantusFirmusCount = 250;
	}
	
	public FuxianCounterPointLoadMetricsTest(Mode mode, int cantusFirmusCount) {
		this.mode = mode;
		targetCantusFirmusCount = this.cantusFirmusCount;
	}

	public void testCantusFirmus() {
		//TestMelody testMelody = new TestMelody(testCFMelody, NoteLength.WHOLE_NOTE);
		SpeciesBuilder patientZero = new SpeciesBuilder(mode, SpeciesType.CANTUS_FIRMUS, testCFMelody);
//		int number = 1;
//		assertEquals(number, 1);
		ArrayList<SpeciesBuilder> buildChain = new ArrayList<SpeciesBuilder>();
		buildChain.add(patientZero);
		

		for(int i: patientZero.getNextValidIndexArrayRandomized()) {
			SpeciesBuilder cantusFirmusBuilder = new SpeciesBuilder(patientZero);
			if(cantusFirmusBuilder.checkAndSetFirstNote(i)) {
				buildChain.add(cantusFirmusBuilder);
				recursiveMelodySequencer(buildChain);				
			}
		}
		//log("" + generatedCantusFirmi.size() + " cantus firmi were completed.");
		
		
	}
	
	private void recursiveMelodySequencer(ArrayList<SpeciesBuilder> buildChain) {
		
		SpeciesBuilder currentCFB = buildChain.get(buildChain.size()-1);
		ArrayList<Integer> nextValidIndexes = currentCFB.getNextValidIndexArrayRandomized();
		int successCount = 0;
		int failCount = 0;
		long startTime;
		long endTime;
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
	
	private void writeCantusFirmus(SpeciesBuilder cf) {
		
		log("Found CF: " + cf.getNotes().getAll() + "Success% " + (double)successCount/(double)failCount + " Success Total: " + successCount + " Failtotal" + failCount);
		cantusFirmusCount++;
		CantusFirmus cfx = new CantusFirmus(cf, test1S);
		if(test1S) {
			runFirstSpecies(cfx);
		}
		
		 //cfx.createMIDIfile(MIDIdirectory, generatedCantusFirmi.size() + " Master");
		 
		 //Metrics ensuring we haven't suddenly drastically decreased the # of valid melodies
		 endTime = System.currentTimeMillis();
		 long totalTime = endTime-startTime;
		 log("totalTime:" + totalTime);
		 double CF1s = (double)firstSpeciesCount/(double)cantusFirmusCount;
		 long timePerCF = totalTime;
		 log("1S per CF" + CF1s + " Empty CF#: " + cfW1s + " timePerCF:" + timePerCF);
		 /*
		  * Exit here to quit after 1 Cantus Firmus and its melodies has been found. Otherwise this program will
		  * run for a long time and create thousands of MIDI files. 
		  */
		 
		 //System.exit(1);
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
	private void runFirstSpecies(CantusFirmus cfx) {
		
		//TestMelody testMelody = new TestMelody(test1SMelody, NoteLength.WHOLE_NOTE);
		cfx.setChildSpeciesTest(test1SMelody);
		cfx.generateSpecies(SpeciesType.FIRST_SPECIES);				
		firstSpeciesCount += cfx.firstSpeciesList.size();
		log("firstSpeciesTotalCount:" + firstSpeciesCount);
		
		log("CF # " + cantusFirmusCount+ " With " + cfx.firstSpeciesList.size() + " first species");
		
		if(cfx.firstSpeciesList.size() > 0) {
			generatedCantusFirmi.add(cfx);
			cfW1s++;
		}	
	}
	

	public void setTestCantusFirmus(int[] testCFMelody) {
		this.testCFMelody = new TestMelody(testCFMelody, NoteLength.WHOLE_NOTE);
		
	}

	public void setTestFirstSpecies(int[] test1SMelody) {
		this.test1SMelody = new TestMelody(test1SMelody, NoteLength.WHOLE_NOTE);
		this.test1S = true;
		
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		
	}
	
	private static void log(String msg) {
		System.out.println("FuxianCPTest Log:           " + msg);
	}

	public boolean validCantusFirmus() {
		return cantusFirmusCount == 1 ? true : false;
	}
	
	public boolean validFirstSpecies() {
		return firstSpeciesCount == 1 ? true : false;
	}
}
