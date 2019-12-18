package com.mugunga.counterpoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 * This class should receive the parameters needed to create the melody. It needs to know whether
 * we are using Fuxian Counterpoint or some other SpeciesSystem, as well as the mode or any 
 * other pertinent information. It then creates the SpeciesBuilders and performs the recursive 
 * logic upon them to build out the music. 
 * 
 * It should be able to be called by various drivers: Applications, Test Classes, etc.
 * 
 * @author laurencemarrin
 *
 */
public class CounterPointRunner {
	
	private SpeciesSystem speciesSystem;
	private SpeciesType speciesType;
	private CounterPointStats stats;
	private Mode mode;
	private final static String MIDIdirectory = "MidiFiles/LoadTest/";
	private TestMelody testBaseMelody;
	private TestMelody testFirstSpeciesMelody;
	private boolean speciesGenerationComplete = false;
	private boolean validFirstSpecies = false;
	//private int firstSpeciesCount = 0;
	private int baseSpeciesCount = 0;
	private int baseFailCount = 0;
	private int targetBaseSpeciesCount;
	private boolean test1S = false;
	private boolean run1S = true;
	private int cfW1s = 0;
	private ArrayList <CantusFirmus> generatedCantusFirmi = new ArrayList<CantusFirmus>();
	
	public CounterPointRunner() {		
		
	}
	
	public CounterPointRunner(SpeciesSystem ss) {		
		this.speciesSystem = ss;
		if(speciesSystem == SpeciesSystem.FUXIAN_COUNTERPOINT) {
			speciesType = SpeciesType.CANTUS_FIRMUS;
		}
	}

	public void generateMusic() {
		
		try {
			createMIDIDirectory();
		} catch (IOException e1) {
			log("failed to create midi directory");
			e1.printStackTrace();
		}
		stats = new CounterPointStats();
		stats.setStartTime(System.currentTimeMillis());
		SpeciesBuilder patientZero = new SpeciesBuilder(mode, speciesType, testBaseMelody);
		ArrayList<SpeciesBuilder> buildChain = new ArrayList<SpeciesBuilder>();
		buildChain.add(patientZero);

		for(int i: patientZero.getNextValidIndexArrayRandomized()) {
			SpeciesBuilder cantusFirmusBuilder = new SpeciesBuilder(patientZero);
			if(cantusFirmusBuilder.checkAndSetFirstNote(i)) {
				buildChain.add(cantusFirmusBuilder);
				recursiveMelodySequencer(buildChain);				
			}
		}
		
		stats.setBaseMeldies(baseSpeciesCount);
		stats.setBaseFailCount(baseFailCount);
		stats.logStats();
	}
	
	private void recursiveMelodySequencer(ArrayList<SpeciesBuilder> buildChain) {
		
		SpeciesBuilder currentCFB = buildChain.get(buildChain.size()-1);
		ArrayList<Integer> nextValidIndexes = currentCFB.getNextValidIndexArrayRandomized();

		for (int i : nextValidIndexes) {
			//log("Current cf: " + currentCFB.getNotes().toString() + " current testIndex: " + i);
			if (currentCFB.testAsNextIndex(i) & ! speciesGenerationComplete) {
				//log("Add next  I to: " + currentCFB.getNotes());
				SpeciesBuilder newCFB = new SpeciesBuilder(currentCFB);
				if (newCFB.addIntervalAndCheckForCompletion(newCFB.nextInterval) & !speciesGenerationComplete) {
					baseSpeciesCount++;
					log("Cantus Firmus Count: " + baseSpeciesCount );
					writeBaseSpecies(newCFB);
					if (baseSpeciesCount >= targetBaseSpeciesCount) {
						speciesGenerationComplete = true;
					}
				} else {
					buildChain.add(newCFB);
					recursiveMelodySequencer(buildChain);
				}
			} else {
				baseFailCount++;
			}
		}
		buildChain.remove(buildChain.size() - 1);	
	}
	
	private void writeBaseSpecies(SpeciesBuilder cf) {
	    log("Found CF: " + cf.getNotes().getAll());
		CantusFirmus cfx = new CantusFirmus(cf, test1S);
		generatedCantusFirmi.add(cfx);
		if(run1S) {
			runFirstSpecies(cfx);
		}
		cfx.createMIDIfile(MIDIdirectory, generatedCantusFirmi.size() + " Master");
		//System.exit(1);
	}

	private void runFirstSpecies(CantusFirmus cfx) {
		cfx.setChildSpeciesTest(testFirstSpeciesMelody);
		cfx.generateSpecies(SpeciesType.FIRST_SPECIES);				
		stats.tallyFirstSpecies(cfx.firstSpeciesList.size());
		log("firstSpeciesTotalCount:" + cfx.firstSpeciesList.size());
		log("CF # " + baseSpeciesCount + " With " + cfx.firstSpeciesList.size() + " first species");
		if(cfx.firstSpeciesList.size() > 0) {
			cfW1s++;
		}	
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public int getTargetBaseSpeciesCount() {
		return targetBaseSpeciesCount;
	}

	public void setTargetBaseSpeciesCount(int targetBaseSpeciesCount) {
		this.targetBaseSpeciesCount = targetBaseSpeciesCount;
	}

	public void setTestBaseMelody(TestMelody testBaseMelody) {
		this.testBaseMelody = testBaseMelody;
	}

	public void setTestFirstSpeciesMelody(TestMelody testFirstSpeciesMelody) {
		this.testFirstSpeciesMelody = testFirstSpeciesMelody;
		test1S = true;
	}
	
	public int getBaseSpeciesCount() {
		return baseSpeciesCount;
	}

	public int getFirstSpeciesCount() {
		return stats.getFirstSpeciesCount();
	}

	public CounterPointStats getStats() {
		return stats;
	}

	private static void log(String msg) {
		System.out.println("CounterPointRunner Log:           " + msg);
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
	
}
