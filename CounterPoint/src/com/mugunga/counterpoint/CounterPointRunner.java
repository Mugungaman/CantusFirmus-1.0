package com.mugunga.counterpoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
	
	private final  String MIDIdirectory = "MidiFiles/LoadTest/";
	private File csvout = new File("cantiFirmi.csv");
	private FileOutputStream csvfos = null;
	private BufferedWriter csvbw;
	private DBHandler dbHandler;
	
	private SpeciesSystem speciesSystem;
	private SpeciesType speciesType;
	private CounterPointStats stats;
	private Mode mode;
	private TestMelody testBaseMelody;
	private TestMelody testFirstSpeciesMelody;
	private boolean speciesGenerationComplete = false;
	//private boolean validFirstSpecies = false;
	private int firstSpeciesCount = 0;
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
		
		fileSetup();

		stats = new CounterPointStats();
		stats.logStartTime();
		stats.setMode(mode);
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
		
		stats.logEndTime();
		stats.setBaseMeldies(baseSpeciesCount);
		stats.setBaseFailCount(baseFailCount);
		//stats.logStats();
		closeOutputFiles();
	}
	
	private void recursiveMelodySequencer(ArrayList<SpeciesBuilder> buildChain) {
		
		SpeciesBuilder currentCFB = buildChain.get(buildChain.size()-1);
		ArrayList<Integer> nextValidIndexes = currentCFB.getNextValidIndexArrayRandomized();

		for (int i : nextValidIndexes) {
			//log("Current cf: " + currentCFB.getNotes().toString() + " current testIndex: " + i);
			if (currentCFB.testAsNextIndex(i) & ! speciesGenerationComplete) {
				SpeciesBuilder newCFB = new SpeciesBuilder(currentCFB);
				if (newCFB.addIntervalAndCheckForCompletion(newCFB.nextInterval) & !speciesGenerationComplete) {
					baseSpeciesCount++;
					processBaseSpecies(newCFB);
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
	
	private void processBaseSpecies(SpeciesBuilder cf) {
		CantusFirmus cfx = new CantusFirmus(cf, test1S);
		writeBaseSpecies(cfx);
		generatedCantusFirmi.add(cfx);
		dbHandler.insertCantusFirmus(cfx);
		if(run1S) {
			runFirstSpecies(cfx);
			
		}
		cfx.createMIDIfile(MIDIdirectory, generatedCantusFirmi.size() + " Master");
	}

	private void writeBaseSpecies(NoteMelody cfx) {
		try {
			csvbw.write(cfx.getNotesAsCSV());
		} catch (IOException e) {
			log("fail to write success stats ");
			e.printStackTrace();
		}
		try {
			csvbw.newLine();
		} catch (IOException e) {
			log("fail to create new line");
			e.printStackTrace();
		}
	}

	private void runFirstSpecies(CantusFirmus cfx) {
		cfx.setChildSpeciesTest(testFirstSpeciesMelody);
		cfx.generateSpecies(SpeciesType.FIRST_SPECIES);				
		stats.tallyFirstSpecies(cfx.getFirstSpeciesList().size());
		if(cfx.getFirstSpeciesList().size() > 0) {
			cfW1s++;
		}	
		dbHandler.insertAllFirstSpeciesForCantusFirmus(cfx);
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
	
	private void fileSetup() {
		
		 if (csvout.exists()){
		     csvout.delete();
		 }
		
		//.csv for statistical analysis
		try {
			csvfos = new FileOutputStream(csvout);
		} catch (FileNotFoundException e) {
			log("failed to create csv file output stream");
			e.printStackTrace();
		}
		csvbw = new BufferedWriter(new OutputStreamWriter(csvfos));
		
		try {
			createMIDIDirectory();
		} catch (IOException e1) {
			log("failed to create midi directory");
			e1.printStackTrace();
		}
	}
	
	private void closeOutputFiles() {

		try {
			csvbw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setDBHandler(DBHandler dbHandler) {
		this.dbHandler = dbHandler;
	}
	
}
