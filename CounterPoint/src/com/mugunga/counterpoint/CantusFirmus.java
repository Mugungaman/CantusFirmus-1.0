package com.mugunga.counterpoint;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;


public class CantusFirmus extends NoteMelody {
	
	private final  String MIDIdirectory = "MidiFiles/";
	private final MusicUtility mUtility = new MusicUtility();
	//notes in step increment (0 = tonic, 1 = a minor second, 2 =major second, 12 = octave, etc)
	
	private Pattern cantusFirmusPattern;
	private ArrayList<FirstSpecies> firstSpeciesList = new ArrayList<FirstSpecies>();
	private String cfMIDIpattern = "";
	private ArrayList<String> firstSpeciesPatternStrings = new ArrayList<String>();
	public ArrayList<Pattern> firstSpeciesMIDIPatterns = new ArrayList<Pattern>();
	public ArrayList<Pattern> secondSpeciesMIDIPatterns = new ArrayList<Pattern>();
	private static ArrayList<SpeciesBuilder> buildChain = new ArrayList<SpeciesBuilder>();
	
	private int dbID;
	
	
	public CantusFirmus (SpeciesBuilder sb, boolean no1S) {
		super(sb.getMelody());
		this.tailorStepIndexes();
		setPattern();
	}

	public void generateSpecies(SpeciesType speciesType) {

		SpeciesBuilder speciesZero = new SpeciesBuilder(this, speciesType);
		for(int i : speciesZero.getValidNextIndexesRandomized()) {
			SpeciesBuilder childSpecies = new SpeciesBuilder(speciesZero);
			if(childSpecies.checkAndSetFirstNote(i)) {
				buildChain.add(childSpecies);
				recursiveMelodySequencer(buildChain);				
			}
		}
	}
	
	private void recursiveMelodySequencer(ArrayList<SpeciesBuilder> buildChain) {
		
		SpeciesBuilder currentSB = buildChain.get(buildChain.size()-1);
		List<Integer> nextValidIndexes = currentSB.getNextValidIndexArrayRandomized();
		
		for (int i : nextValidIndexes) {
			//log("Current melody: " + currentSB.getNotes().getAll()+ " current testIndex: " + i);
			if (currentSB.testAsNextIndex(i)) {
				SpeciesBuilder newSB = new SpeciesBuilder(currentSB);
				if (newSB.addIntervalAndCheckForCompletion(newSB.nextInterval)) {
					logFirstSpecies(newSB);
						
				} else {
					buildChain.add(newSB);
					recursiveMelodySequencer(buildChain);
				}
			} else {
			}
		}
		buildChain.remove(buildChain.size() - 1);
	}	
	
	private void logFirstSpecies(SpeciesBuilder newCFB) {
		firstSpeciesList.add(new FirstSpecies(newCFB.getMelody()));
		String patternString = mUtility.getMIDIString(this.getLastFirstSpecies(), getMode(), mUtility.melodyStartIndex);
		firstSpeciesPatternStrings.add(patternString);
		firstSpeciesMIDIPatterns.add(new Pattern(patternString));
	}

	private void writeMasterMIDIFile(String prefix) {
		String masterMIDIPattern = cfMIDIpattern + "R ";
		
		for(Pattern p : firstSpeciesMIDIPatterns) {
			masterMIDIPattern += p + "R ";
		}
		
		//System.out.println("masterMIDIPattern:" + masterMIDIPattern);
		Pattern masterPattern = new Pattern(masterMIDIPattern);
		File file = new File(MIDIdirectory + prefix + cantusFirmusPattern.toString() + ".mid" );
		try {
			MidiFileManager.savePatternToMidi((PatternProducer) masterPattern, file);
		} catch (Exception ex) {
			ex.getStackTrace();
		}
	}

	private void setPattern() {
		this.cfMIDIpattern = mUtility.getMIDIString(this, getMode(), mUtility.melodyStartIndex);
		this.cantusFirmusPattern = mUtility.getMIDIPattern(this, getMode(), mUtility.melodyStartIndex);
	}

	public void createMIDIfile(String directory, String filenamePrefix) {
		writeMasterMIDIFile(filenamePrefix);			
	}

	public NoteMelody getLastFirstSpecies() {
		return firstSpeciesList.get(firstSpeciesList.size() - 1);
	}
	
	private void log(String msg) {
		System.out.println("CantusFirmusLog:      " + msg);
	}

	public void setdbID(int dbID) {
		this.dbID = dbID;
	}

	public ArrayList<FirstSpecies> getFirstSpeciesList() {
		return firstSpeciesList;
	}

	public int getDBid() {
		return dbID;
	}

	public int getfirstSpeciesCount() {
		return firstSpeciesList.size();
	}
	
}
