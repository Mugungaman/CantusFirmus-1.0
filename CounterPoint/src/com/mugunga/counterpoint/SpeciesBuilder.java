package com.mugunga.counterpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author laurencemarrin
 *
 */
public class SpeciesBuilder {
	//public boolean logTestString = false;
	public boolean logginOn = true;
	public boolean logCF = false;
	public boolean testingAMelody = false;
	
	private SpeciesRules rules;
	private double maxMeasures;
	private int maxNotes;
	private double minMeasures;
	private int minNotes;
    //private boolean seedNextIndexNoteArray = false;

	private NoteIndexCollection validNextIndexes;// = new NoteIndexCollection();
	private NoteIndexCollection validNextIndexesSaved;
	//private TestMelody testMelody;
	private int lastIndex = 0;
	private int lastInterval = 0;
	private Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
	//private MelodyInProgress melodyInProgress; // = new MelodyInProgress();
	private NoteMelodyInProgress noteMelody;
	
	//Test Fields used to vet whether the current note being tested will be a valid addition to the melody
	private int testInterval;
	private int testStepInterval;
	private int testIndex;
	private int testStepIndex;
	private int testMotionType;
	
	public int nextInterval;
	private int nextMotion;
	public Map <Integer, NoteIndexCollection> validIndexesMap;
	
	//main constructor from Driver. This SB will create Cantus Firmi
	public SpeciesBuilder(Mode mode, 
						SpeciesType speciesType, 
						TestMelody testMelody) {
		
		//this.speciesType = speciesType;
		rules = new SpeciesRules(speciesType);
//		log("Have created Rules in primary Species Builder..." + rules);
		//melodyInProgress = new MelodyInProgress(rules, mode);
		noteMelody = new NoteMelodyInProgress(rules, mode);
		maxMeasures = rules.maxMeasures;
		minMeasures = rules.minMeasures;
		
		ArrayList<Integer> startIdxList = new ArrayList<Integer>();
		if(null == testMelody) {
			log("ASDF adding valid StartIndex");
			for(int i : rules.validStartIndexes ) {
				log("valid start index: " + i);
				startIdxList.add(i);
			}
			
		} else {
			testingAMelody = true;
			noteMelody.setTestMelody(testMelody);
			//setTestMelody(testMelody);
			minMeasures = testMelody.melodyLength();
			maxMeasures = testMelody.melodyLength();
			startIdxList.add(testMelody.getFirst());
		}
		
		validIndexesMap = new HashMap <Integer, NoteIndexCollection>();
		validIndexesMap.put(1, new NoteIndexCollection(startIdxList) );
//		log("validIndexesMap" + validIndexesMap);
		validNextIndexes = validIndexesMap.get(1);
		lastIndex = 0;
	}
	
	/**
	 * 
	 * @param cantusFirmus
	 * @param speciesType
	 * 
	 * To be called by the Parent Melody to create children melody. 
	 * 
	 */
	public SpeciesBuilder(CantusFirmus cantusFirmus, SpeciesType speciesType) {
//		log("Species Builder Constructor from first Species");
		//this.mode = cantusFirmus.getMode();
		//this.speciesType = speciesType;
		rules =  new SpeciesRules(speciesType);
		//this.cantusFirmus = cantusFirmus;
//		melodyInProgress = new MelodyInProgress(rules, cantusFirmus.getMode());
//		melodyInProgress.setParentMelody(cantusFirmus);
		noteMelody = new NoteMelodyInProgress(rules, cantusFirmus.getMode());
		noteMelody.setParentMelody(cantusFirmus);
		validNextIndexes = new NoteIndexCollection();
		if(cantusFirmus.isTestingChildSpecies()) {
			log(" we are testing a child species?");
			testingAMelody = true;
			//setTestMelodyFromParent(cantusFirmus.getChildSpeciesTestMelody());
			noteMelody.setTestMelody(cantusFirmus.getChildSpeciesTestMelody());
			minMeasures = noteMelody.testMelodyLength();
			maxMeasures = noteMelody.testMelodyLength();
			//testMelody = cantusFirmus.getChildSpeciesTestMelody();
			//if we are running a test, set the test notes
			validNextIndexes.add(cantusFirmus.getChildSpeciesTestMelody().get(0));
		} else {
			//if we are not running a specific test, use all valid first species start indexes as the valid next index
			for (int i : rules.speciesType.validStartIndexes) {
				//log("adding next start note to validNextIndexes" + i);
				validNextIndexes.add(i);
			}			
		}
		maxMeasures = cantusFirmus.size();
		minMeasures = cantusFirmus.size();
	}

//	private void //setTestMelodyFromParent(ArrayList<Integer> childSpeciesTestMelody) {
//		// TODO Auto-generated method stub
//		
//	}

	//This Constructor merely clones all fields from it's parent Species Builder so it's a separate entity and can 
	public SpeciesBuilder(SpeciesBuilder o) {
		this.rules = o.rules;
		//log("SB second constructor rules:" + rules + " speciesType = " + rules.speciesType);
		this.noteMelody = new NoteMelodyInProgress(o.noteMelody);
		this.noteMelody.setTestMelody(o.getMelody().getTestMelody());
//		this.melodyInProgress = new MelodyInProgress(o.melodyInProgress);
		this.testingAMelody = o.testingAMelody;
		this.validIndexesMap = o.validIndexesMap;
		this.lastIndex = o.lastIndex;
		this.testIndex = o.testIndex;
		this.testInterval = o.testInterval;
		this.testStepInterval = o.testStepInterval;
		this.lastInterval = o.lastInterval;
		this.nextInterval = o.nextInterval;
		this.logginOn = o.logginOn;
		this.nextMotion = o.nextMotion;
		this.testStepIndex = o.testStepIndex;
		this.maxMeasures = o.maxMeasures;
		this.minMeasures = o.minMeasures;
		this.maxNotes = o.maxNotes;
		this.minNotes = o.minNotes;
		this.indexMap = o.indexMap;
		
//		if(testingAMelody) {
//			this.testMelody = new NoteIndexCollection();
//			for(int i : o.testMelody.getAll()) {
//				this.testMelody.add(i);
//			}			
//		}
		
		validNextIndexes = new NoteIndexCollection();
		for(int i : o.validNextIndexes) {
			this.validNextIndexes.add(i);
		}
		if(null != validNextIndexesSaved) {
			validNextIndexesSaved = new NoteIndexCollection();
			for(int i : o.validNextIndexesSaved) {
				this.validNextIndexesSaved.add(i);
			}			
		}
//		log("validNextIndexes:" + validNextIndexes);
	}
	
	public boolean checkAndSetFirstNote(int firstNote) {
		log("check and set first note:::::::::::::::::" + firstNote);
		testIndex = firstNote;
		if (!rules.checkAgainstParentMelody(noteMelody, firstNote, 0, 0)) {
			log("failed rules check");
			return false;
		}
//		log("species Type:" + rules.speciesType);
//		log("Setting first note:" + firstNote + "first species?" + isFirstSpecies());
		if(isCantusFirmus() || isFirstSpecies()) {
			noteMelody.addNote(firstNote, NoteLength.WHOLE_NOTE.noteLength);			
		} else if(isSecondSpecies()) {
			noteMelody.addNote(firstNote, NoteLength.HALF_NOTE.noteLength);						
		}
		setNotesAndRanges();
		int currentStepIndex = indexMap.get(firstNote);
		noteMelody.addStepIndex(currentStepIndex);
		log( "species tyoe: " + rules.speciesType);
		if (isFirstSpecies()) {		//TODO -> need to allow second species start on tritone? Third? 
			log("FIRST SPECIES NOTE ADD?");
			if(!rules.checkLastIndexAsTritone(noteMelody, firstNote, currentStepIndex)) {
				log("invalid tritone start note, exile emlody");
				return false;
				//melodyInProgress.setTritoneResolutionNeeded();
			} else {
				log("return true");
			}
		}
		log("about to generate valid notes arrays");
		generateValidNoteArrays();
//		if(!isCantusFirmus()) {
//			//check tritone
//		}
//		generatevalidnotearrays
		
		
		validNextIndexes = validIndexesMap.get(noteMelody.size() + 1);
		log("validnextIndexes: " + validNextIndexes);
		
		lastIndex = firstNote;
		return true;
	}
	
	//called during constructors
	private void setNotesAndRanges() {
		
		
		for (int i  = noteMelody.getMinNadirIndex(); i <= noteMelody.getMaxZenithIndex(); i++ ) {
			int y = 0;
			if(i < 0) {
				y = 1;
			}
			int x = (7 + (i%7))%7;
//			log("x:" + x + "ModeNotes# " + modeNotes.length);
			int f = (noteMelody.getMode().notes[x] + ((i + y)/7)*12 - (y*12));
			indexMap.put(i, f);
		}
	}
	
	private boolean isCantusFirmus() {
		return rules.speciesType.equals(SpeciesType.CANTUS_FIRMUS) ? true : false;
	}
	private boolean isChildSpecies() {
		return rules.speciesType.equals(SpeciesType.CANTUS_FIRMUS) ? false : true;
	}
	private boolean isFirstSpecies() {
		return rules.speciesType.equals(SpeciesType.FIRST_SPECIES) ? true : false;
	}
	private boolean isSecondSpecies() {
		return rules.speciesType.equals(SpeciesType.SECOND_SPECIES) ? true : false;
	}
	private boolean isThirdSpecies() {
		return rules.speciesType.equals(SpeciesType.THIRD_SPECIES) ? true : false;
	}
	private boolean isFourthSpecies() {
		return rules.speciesType.equals(SpeciesType.FOURTH_SPECIES) ? true : false;
	}

	/**
	 * Here we have a partial melody and are testing whether the proposed next note is valid (testIndex)
	 * Thus, we need to check all the rules of our melody, and as soon as our note breaks a rule, we return 
	 * false to indicate this is an invalid note and to move on to the next one. 
	 * 
	 * The rules are ordered to determine the invalid note as soon as possible, for efficiency. 
	 * 
	 * 
	 * @param testIndex
	 * @return
	 */
	public boolean testAsNextIndex(int testIndex) {
		
		//debugging purposes only
		if(! logCF && isCantusFirmus()) {
			logginOn = false;
		}
		
		//the starting note is 0, so our Index is the # of half steps away from the starting note. 
		this.testIndex = testIndex;
		
		//an Interval is the # of half steps from the previous note, 
		testInterval = testIndex - lastIndex;
		log(" $$$ testingIndex:" + testIndex + "testInterval: " + testInterval + " for " + noteMelody.getAll().toString() + " $$$ "); 
		log("final note ready? " + noteMelody.finalNoteIsReady() + "  pentultiamte found? " + noteMelody.isPentultimateFound());
		if(isSecondSpecies()) {
			log("parent melody length: " + noteMelody.getParentMelody().melodyLength() + " melody length: " + noteMelody.melodyLength());
		}
		if(isFirstSpecies() && noteMelody.size() == noteMelody.getParentMelody().size() - 1) {
			if (!rules.validEndIndexForSpecies(testIndex)) {
//				log("needs to be last note of species but isn't");
				return false;
			}
		} else if(isSecondSpecies()) {
			//TODO check end of melody (skipFirstDownbeat, pentultimateWholeNote)
		}

		/*
		 * If the melody is ready to conclude with a final note, but the current test index is not a
		 * valid final note, then we are no longer ready for the final note, however, processing 
		 * continues because we might still be a valid melody
		 */
		if(noteMelody.finalNoteIsReady()) {
			if ( isCantusFirmus() && testIndex != 0) {
				noteMelody.setFinalNoteNotReady(); 
				noteMelody.setPentultimateFound(false);
//				log("This is not a valid final note for a cantus Firmus!" + melodyInProgress.finalNoteIsReady());
			} 
		}
		
		//is note in bounds of cantus firmus range? -> if not, sets testNote
		if(!rules.checkTestNoteRange(testIndex, noteMelody)) {
//			log("Index " + testIndex + " out of range");
			return false;
		}
//		log("Index Bounds Passed");
		
		setTestStepFields();
		
		if(isSecondSpecies()) {
			if(nextNoteDownbeat()) {
				log("verifying next note as a downbeat");
				if(!rules.checkValid2SDownbeat(noteMelody, testIndex, testInterval)) {
					log("not a valid downbeat for 2s given the last halfbeat note: " + testIndex + " testInterval: " + testInterval);
					return false;
				}
			} else {
				log("verifying next note as a halfbeat");
				if(!rules.checkValid2SHalfbeat(noteMelody, testIndex, testInterval)) {
					
					log("not a valid downbeat for 2s given the last halfbeat note: " + testIndex + " testInterval: " + testInterval);
					return false;
				}
			}
		}
		
//		if(is2SDownbeat()) {
//			//TODO test coming from parallel 5th or octave
//			//TODO is dissonant?
//		} else if(is2SHalfbeat()) {
//		}
		
		if(noteMelody.size() > 0) {
//			log("about to check motion..");
			if(!rules.validMotionCheck(noteMelody, testIndex, testInterval)) {
//				log("motion issue detected");
				return false;
			} else {
//				log("no motion issue...");
			}
		}
		
		if(isChildSpecies()) {
//			log("rules check against parent melody");
			if(!rules.checkAgainstParentMelody(noteMelody, testIndex, testStepIndex, testInterval)) {
				return false;
			}
		}
		//TODO check 2s species after this point..........V^V^V
		
//		log("about to final note ready check");
		if(noteMelody.finalNoteIsReady() && !rules.validEndIndexForSpecies(testIndex)) {
			log("invalid index as final note");
			if(isFirstSpecies() && noteMelody.size() == noteMelody.getParentMelody().size() - 1) {
//				log("Needs to be final note for species, but is: " + testIndex + "SHOULD NOT GET GET HERE");
				return false;
			} else if(isSecondSpecies() && noteMelody.melodyLength() == noteMelody.getParentMelody().melodyLength() - 1) {
				log("Needs to be final note for second species" + noteMelody);
				return false;
			}
		}
		
//		log("about to check note repeition");
		//make sure notes or patterns are not repeating too much within the series. 
		if(!rules.checkNoteRepetition(testIndex, lastIndex, testInterval, noteMelody)) {
//			log("rules note repetition check failed testing: " + testIndex + " for " + melodyInProgress.getAll());
			return false;
		}
//		log("Repetition Check Passed");
		
		
		if(!rules.checkLeaps(noteMelody, testIndex)) {
//			log("Species Rules Leap CHeck Fail");
			return false;
		}
//		log("Leap Check Passed");
		
		
		if(!rules.validTestInterval(noteMelody, testInterval, testStepInterval)) {
//			log("invalid interval test from rules...");
			return false;
		}
//		log("Interval Check Passed");
		
		//Two things: if melodyInProgress had pentultimate found, set that, if needs to be pentultimate,but isn't, error false. 
		if(!rules.checkPentultimateFound(noteMelody, testIndex, testInterval)) {
			
//			log("pentultimate check issue, failing" + testIndex + " for: " + melodyInProgress.getAll());
			return false;
		}
		
//		log("Pentultimate has determine: " + melodyInProgress.isPentultimateFound());
		
//		if(isSecondSpecies()) {
//			log("looking for valid second note");
//			if(!rules.getValid2SHalfNotes()) {
//				//No valid half note, return false;
//			} else {
//				//return valid half notes to parent.
//			}
//		}
		
//		log("PASSED ALL");
		nextInterval = testInterval;
		nextMotion = testMotionType;
		return true;
	}

	
//	private boolean is2SDownbeat() {
//		if(rules.speciesType == SpeciesType.SECOND_SPECIES && noteMelody.size()%2 == 0) {
//			return true;
//		}
//		return false;
//	}
//	
//	private boolean is2SHalfbeat() {
//		if(rules.speciesType == SpeciesType.SECOND_SPECIES && noteMelody.size()%2 == 1) {
//			return true;
//		}
//		return false;
//	}

	public boolean addIntervalAndCheckForCompletion(int interval) {
//		log("!!!!!!adding interval " + interval + "!!!!!");
		int noteIndex = lastIndex + interval;
		if(isFirstSpecies() && noteMelody.size() > 0) {
			noteMelody.addMotion(rules.determineMotionType(noteMelody, interval));
			//log("motion added " + this.motions);
		//TODO add second species motion to this	
		}
//		log("notes before adding: " + melodyInProgress);
		noteMelody.addInterval(interval);
		if(isCantusFirmus() || isFirstSpecies()) {
			noteMelody.addNote(noteIndex, NoteLength.WHOLE_NOTE.noteLength );			
		}else if (isSecondSpecies()) {
			//TODO If pentultimate ready, add a whole note if downbeat & pentultimate? 
			noteMelody.addNote(noteIndex, NoteLength.HALF_NOTE.noteLength );
			//TODO: add logic for the whole notes that do occur. 
		}
		noteMelody.addStepIndex(indexMap.get(noteIndex));
//		log("notes after adding: " + melodyInProgress);
		
//		log("about to check if we have completed a species with " + testNoteIndex);
		
//		log("is my final note ready?" + melodyInProgress.finalNoteIsReady());
		if(noteMelody.finalNoteIsReady()) {
			//finalNoteReady = false;
			if(rules.validEndIndexForSpecies(noteIndex)) {
				return true;
			} 
		}
		
		if(noteMelody.isPentultimateFound()) {
			noteMelody.setFinalNoteReady();
		}
//		log("am 1st species?...");
		if(isFirstSpecies()) {
//			log("checking tritone");
			if(!rules.checkLastIndexAsTritone(noteMelody, testIndex, testStepIndex)) {
				//return false;
				//melodyInProgress.setTritoneResolutionNeeded();
			} else {
				log("Tritone is def needed!");
//				log("tritone resolution form this step isn't needed, set to resolved");
				//melodyInProgress.setTritoneResolved();			
			}	
		}
		lastInterval = interval;
		lastIndex = noteIndex;

		pruneValidIndexArrays();
//		log("About to return, am I complete?");
		return false;
	}
	
	public NoteMelodyInProgress getNotes() {
		return noteMelody;
	}

	public ArrayList<Integer> getValidNextIndexes() {
		return validNextIndexes.getAll();
	}
	
	public ArrayList<Integer>getValidNextIndexesRandomized() {
		return validNextIndexes.getRandomized();
	}
	
//11-30-2018 combined with generate valid note arrays - can remove	
//	public void generateValid1SNoteArrays() {
////		log("generating valid S1 arrays");
//		//log("max peak index:" + maxPeakIndex);
//		//log("min low index:" + minLowIndex);
//		validIndexesMap = new HashMap<Integer, NoteIndexCollection>();
//		CantusFirmus cf = (CantusFirmus) melodyInProgress.getParentMelody();
//		if(cf.isTestingChildSpecies()) {
//			log("Testing first spsecies!!!" + cf.getChildSpeciesTestMelody());
//			generateChildArraysFromTestMelody();
//		} else {
//			generateS1ArraysFromValidHarmonyIndexes();
//		}
//	}
	
	public void generateValidNoteArrays() {
		validIndexesMap = new HashMap<Integer, NoteIndexCollection>();
//		CantusFirmus cf = (CantusFirmus) melodyInProgress.getParentMelody();
//		if(cf.isTestingChildSpecies()) {
		log("generating valid note arrays, are we testing a melody?" + testingAMelody);
		if(testingAMelody) {
			
			if(isCantusFirmus()) {
				generateCFArraysFromTestMelody();				
			} else {
				generateChildArraysFromTestMelody();				
			}
		} else {
			
			switch(rules.speciesType) {
			case FIRST_SPECIES:
				generateS1ArraysFromValidHarmonyIndexes();
				break;
			case SECOND_SPECIES:
				generateS2ArraysFromValidHarmonyIndexes();
				break;
			case CANTUS_FIRMUS:
				generateCFArraysFromValidIndexes();
				break;
			default:
				log("ATTEMPTING TO GENERATE NOTE ARRAYS FOR INVALID SPECIES TYPE!!!");
				break;
			}
		}
		
//		log("generating valid CF arrays");
//		validIndexesMap = new HashMap<Integer, NoteIndexCollection>();
//		if(testingAMelody) {
//			log("generating CF Arrays from test moellody");
//			generateCFArraysFromTestMelody();
//		} else {
//			generateCFArraysFromValidIndexes();
//		}
	}
	
	private void generateCFArraysFromValidIndexes() {
		int c = 0;
		for(int i = 1; i <= maxMeasures; i ++) {
			c++;
			ArrayList<Integer> currIdxList = new ArrayList<Integer>();
			if(i == 1) {
				for(int j : rules.validStartIndexes) {
					currIdxList.add(j);
				}
			} else if(i == maxMeasures) {
				for(int j : rules.validEndIndexes) {
					currIdxList.add(j);
				}
			} else if(i == 2) {
				for(int j : rules.validIntervals) {
					currIdxList.add(j);
				}
			} else {
				for(int j = 0-rules.maxIndexRange(); j<= 0 + rules.maxIndexRange(); j++) {
					currIdxList.add(j);
				}
			}
			if (currIdxList.size() == 0) {
				System.out.println("Trying to have a note with NO valid notes? + i");
				System.exit(0);
			}
//			log("notePos: " + c + "  " + currIdxList);
			validIndexesMap.put(c, new NoteIndexCollection(currIdxList));
		}
	}
	
	private void generateS1ArraysFromValidHarmonyIndexes() {
		int c = 0;
		for (int i : noteMelody.getParentMelody().getAll()) {

			c++;
			ArrayList<Integer> currIdxList = new ArrayList<Integer>();
			
			//If we are getting valid pentultimate array indexes
			if(noteMelody.getParentMelody().size() - 1 == c) {
				log("setting s1 array index pentultimate # " + c + " for cf length: " + noteMelody.parentSize());
				if(noteMelody.getParentMelody().getLastInterval() < 0) {
					//if cantus firmus approaches from below, 1s must approach from above
					for (int k : rules.validEndIndexes) {
						currIdxList.add(k -1);
						if(!noteMelody.getValidPentultimates().contains(k - 1)) {							
							noteMelody.addValidPentultimate(k-1);
						}
					}
				} else {
					for (int k : rules.validEndIndexes) {
						currIdxList.add(k + 1);
						if(!noteMelody.getValidPentultimates().contains(k + 1)) {
							noteMelody.addValidPentultimate(k+1);
						}
					}
				}
			} 
			
			//valid end notes
			if(noteMelody.getParentMelody().size() == c) {
				for(int k : rules.validEndIndexes) {
					currIdxList.add(k);
				}
			}
			
			//for each non pentultimate non whatever...
			if(noteMelody.getParentMelody().size() > c + 1 ) {		
				for(int j : rules.speciesType.validCFHarmonies) {
					if(i + j <= noteMelody.getMaxZenithIndex() &&
					   i+j >= noteMelody.getMinNadirIndex()) {
						currIdxList.add(i + j);
					}
				}
			}
			
			if (currIdxList.size() == 0) {
				log("Trying to have a note with NO valid notes? + i");
				System.exit(0);
			}
			
//			log("notePos: " + c + "  " + currIdxList);
			validIndexesMap.put(c, new NoteIndexCollection(currIdxList));
		}
	}
	
	private void generateS2ArraysFromValidHarmonyIndexes() {
		//Downbeatindexes map
		//offbeat indexes map
		int mapIndex = 0;
		mapIndex++;
		validIndexesMap.put(mapIndex, validNextIndexes);
		log("valid NExt INdexes: " + validNextIndexes);
		
		
		
		log("generate s2Arrays from valid Harmony Indexes");
		log("map has: " + validIndexesMap.size() + " entries");
		ArrayList<Integer> currHalfBeatIndexList = new ArrayList<Integer>();
		for(int i : SpeciesType.SECOND_SPECIES.validCFHarmonies) {
			currHalfBeatIndexList.add(i);
		}
				
		
		
		int c = 0;
		for (int i : noteMelody.getParentMelody().getAll()) {

			c++;
			ArrayList<Integer> currDownBeatIndexList = new ArrayList<Integer>();
			
			//If we are getting valid pentultimate array indexes
			if(noteMelody.getParentMelody().size() - 1 == c) {
				log("setting s1 array index pentultimate # " + c + " for cf length: " + noteMelody.parentSize());
				if(noteMelody.getParentMelody().getLastInterval() < 0) {
					//if cantus firmus approaches from below, 1s must approach from above
					for (int k : rules.validEndIndexes) {
						currDownBeatIndexList.add(k -1);
						if(!noteMelody.getValidPentultimates().contains(k - 1)) {							
							noteMelody.addValidPentultimate(k-1);
						}
					}
				} else {
					for (int k : rules.validEndIndexes) {
						currDownBeatIndexList.add(k + 1);
						if(!noteMelody.getValidPentultimates().contains(k + 1)) {
							noteMelody.addValidPentultimate(k+1);
						}
					}
				}
			} 
			
			//valid end notes
			if(noteMelody.getParentMelody().size() == c) {
				for(int k : rules.validEndIndexes) {
					currDownBeatIndexList.add(k);
				}
			}
			
			//for each non pentultimate non whatever...
			if(noteMelody.getParentMelody().size() > c + 1 ) {		
				for(int j : rules.speciesType.validCFHarmonies) {
					if(i + j <= noteMelody.getMaxZenithIndex() &&
					   i+j >= noteMelody.getMinNadirIndex()) {
						currDownBeatIndexList.add(i + j);
					}
				}
			}
			
			if (currDownBeatIndexList.size() == 0) {
				log("Trying to have a note with NO valid notes? + i");
				System.exit(0);
			}
			
			
//			log("notePos: " + c + "  " + currIdxList);
			mapIndex++;
			log("for note: " + mapIndex + " currHalfBeatIndex: " + currHalfBeatIndexList); 
			validIndexesMap.put(mapIndex, new NoteIndexCollection(currHalfBeatIndexList));
			mapIndex++;
			log("for note: " + mapIndex + " currDownBeatIndex: " + currHalfBeatIndexList); 
			validIndexesMap.put(mapIndex, new NoteIndexCollection(currDownBeatIndexList));
		}
	}

	//This takes a single potential 1S melody and fits puts each note into a valid 1S Index array, meaning each step will only test 
	//one note. 

	//replacing this logic with generic generate from TestMelody. 
	
	private void generateChildArraysFromTestMelody() {
		int c = 0;
//		for(int i : noteMelody.getParentMelody().getChildSpeciesTestMelody()) {
		for(int i : noteMelody.getTestMelody().getAll()) {
			c++;
			ArrayList<Integer> currIdxList = new ArrayList<Integer>();
			currIdxList.add(i);
			log("notePos: " + c + "  " + currIdxList);
			validIndexesMap.put(c, new NoteIndexCollection(currIdxList));
		}
	}
	private void generateCFArraysFromTestMelody() {
		int c = 0;

		for(int i : noteMelody.getTestMelody().getAll()) {
			c++;
			ArrayList<Integer> currIdxList = new ArrayList<Integer>();
			currIdxList.add(i);
			log("notePos: " + c + "  " + currIdxList);
			validIndexesMap.put(c, new NoteIndexCollection(currIdxList));
		}
	}
	
	
	public void pruneValidIndexArrays() {
		validNextIndexes = new NoteIndexCollection();
		
		if(noteMelody.pruneIndexArraysForVoiceDisposition()) {
//			log("prune the validIndex arrays for upper?" + melodyInProgress.isUpperVoice() + " lower?" + melodyInProgress.isLowerVoice() );
			pruneForUpperLowerVoice();
			noteMelody.indexArraysPrunedForVoiceDisposition();
		}
		
//		log("melodyInProgress size:" + melodyInProgress.size());
		
		
//		for(int i : validIndexesMap.get(melodyInProgress.size() + 1)) {
//			validNextIndexes.add(i);
//		}
		log("valid indexes first:" + validNextIndexes);
		validNextIndexes = validIndexesMap.get(noteMelody.size() + 1);
		log("valid indexes from map:" + validNextIndexes);
		
		if(noteMelody.isTritoneResolutionNeeded()) {
			log("Need log to prune everything that isn't a resolution of the next tritone");
			NoteIndexCollection tritoneResolutionIndex = new NoteIndexCollection();
			

			if(noteMelody.getParentMelody().getInterval(noteMelody.size()) == 1) {
				for(int j : validNextIndexes) {
					if(j - noteMelody.getLast() == -1) {
						tritoneResolutionIndex.add(j);
					}
				}
				//validNextIndexes.add(-1);
			} else if(noteMelody.getParentMelody().getInterval(noteMelody.size()) == -1) {
				//validNextIndexes.add(1);
				for(int j : validNextIndexes) {
					if(j - noteMelody.getLast() == 1) {
						tritoneResolutionIndex.add(j);
					}
				}
				
			}
			validNextIndexes = tritoneResolutionIndex;
//			log(validNextIndexes + "c next indexes");
			
			//melodyInProgress.setTritoneResolved();
		} else if(noteMelody.leapTally() >= rules.maxLeaps) {
			NoteIndexCollection leapRestraintIndexes = new NoteIndexCollection();
			for(int j : validNextIndexes) {
//				log("checking j" + j);
				if(Math.abs(noteMelody.getLast() - j) == 1) {
					leapRestraintIndexes.add(j);
				}
			}
//			log("max leap pruning?" + leapRestraintIndexes);
			validNextIndexes = leapRestraintIndexes;
		}
		
		
		
		if(isSecondSpecies()) {
			
			int lastNote = noteMelody.getLast();
			int lastParentNote = noteMelody.getPreviousParentNote();
			log("last Parent NOte:" + lastParentNote);
			int nextParentDownbeat = noteMelody.getNextParentNote();
			log("next Parent Downbeat:" + nextParentDownbeat);
			if(nextNoteDownbeat()) {
				log("determine indexes for next down beat from interval:" + lastInterval);
				//NoteIndexCollection validNextHalfbeatIndexes = new NoteIndexCollection();
				NoteIndexCollection validNextDownbeatIndexes = new NoteIndexCollection();
				//what are next valid downbeats
				switch(lastInterval) {
				case 1:
					validNextDownbeatIndexes.add(lastNote + 1);
					addIfConsonantHarmony(lastNote - 1, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote + 2, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case -1:
					validNextDownbeatIndexes.add(lastNote - 1);
					addIfConsonantHarmony(lastNote + 1, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote - 2, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case 2:
					addIfConsonantHarmony(lastNote - 1, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote + 1, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote + 2, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote + 3, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case -2:
					addIfConsonantHarmony(lastNote + 1, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote - 1, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote - 2, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote - 3, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case 3:
					addIfConsonantHarmony(lastNote - 1, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote + 2, nextParentDownbeat, validNextDownbeatIndexes );
//					addIfConsonantHarmony(lastNote + 1, nextParentDownbeat, validNextDownbeatIndexes ); //TODO can we jump up a fourth then climb to the fifth?
					break;
				case -3:
					addIfConsonantHarmony(lastNote + 1, nextParentDownbeat, validNextDownbeatIndexes );
					addIfConsonantHarmony(lastNote - 2, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case 4:
					addIfConsonantHarmony(lastNote - 1, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case -4:
					addIfConsonantHarmony(lastNote + 1, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case 5:
					addIfConsonantHarmony(lastNote - 1, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case -5:
					addIfConsonantHarmony(lastNote + 1, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case 7:
					addIfConsonantHarmony(lastNote - 1, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				case -7:
					addIfConsonantHarmony(lastNote + 1, nextParentDownbeat, validNextDownbeatIndexes );
					break;
				default:
					log("unhanlded test interval in valid index pruning section!!");
					break;
				}
				//TODO -> verify against validNext Indexes
				validNextIndexes = validNextDownbeatIndexes;
			} else {
				log("determineing next half beat now:");
				NoteIndexCollection validNextHalfbeatIndexes = new NoteIndexCollection();
				for(int i : validNextIndexes) {
					if (Math.abs(i) != 1) {
						addIfConsonantHarmony(i, nextParentDownbeat, validNextHalfbeatIndexes);
					} else {
						validNextHalfbeatIndexes.addIfNotDuplicate(i);
					}
				}
				validNextIndexes = validNextHalfbeatIndexes;
			}
			
			log("final form validNextIndexes:" + validNextIndexes);
			
//			if(noteMelody.size() % 2 == 0) {
//				for(int i : validNextIndexes) {
//					testInterval = i - lastNote ;
//					boolean hasValidHalfNote = false;
//					switch(testInterval) {
//					case 0:
//						//check for consonant neighbor tone
//						if(rules.isConsonantHarmony(lastNote - 1, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote-1);
//							hasValidHalfNote = true;
//						}
//						if(rules.isConsonantHarmony(lastNote + 1, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote + 1);
//							hasValidHalfNote = true;
//						}
//						break;
//					case 1:
//						if(rules.isConsonantHarmony(lastNote + 2, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote + 2);
//							hasValidHalfNote = true;
//						}
//						break;
//					case -1:
//						if(rules.isConsonantHarmony(lastNote - 2, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote - 2);
//							hasValidHalfNote = true;
//						}
//						break;
//					case 2:
//						//passing tone
//						validNextHalfNoteIndexes.addIfNotDuplicate(lastNote + 1);
//						hasValidHalfNote = true;
//						//fourth jump
//						if(rules.isConsonantHarmony(lastNote + 3, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote + 3);
////							hasValidHalfNote = true;
//						}
//						break;
//					case -2:
//						//passing tone
//						validNextHalfNoteIndexes.addIfNotDuplicate(lastNote - 1);
//						hasValidHalfNote = true;
//						//consonant substitution
//						if(rules.isConsonantHarmony(lastNote - 3, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote - 3);
////							hasValidHalfNote = true;
//						}
//						break;
//					case 3:
//						//split a fourth by a step and a 2nd
//						if(rules.isConsonantHarmony(lastNote + 2, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote + 2);
//							hasValidHalfNote = true;
//						}
//						if(rules.isConsonantHarmony(lastNote + 1, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote + 1);
//							hasValidHalfNote = true;
//						}
//						if(rules.isConsonantHarmony(lastNote + 4, lastParentNote)) {
//							validNextHalfNoteIndexes.addIfNotDuplicate(lastNote + 4);
//							hasValidHalfNote = true;
//						}
//						break;
//					case -3:
//						//split a fourth by a step and a 2nd
//						if(rules.isConsonantHarmony(lastNote - 2, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote - 2);
//							hasValidHalfNote = true;
//						}
//						//split a fourth by a step and a 2nd
//						if(rules.isConsonantHarmony(lastNote - 1, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote - 1);
//							hasValidHalfNote = true;
//						}
//						if(rules.isConsonantHarmony(lastNote - 4, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote - 4);
//							hasValidHalfNote = true;
//						}
//						break;
//						
//					case 4:
//						//split a fifth jump by a half
//						if(rules.isConsonantHarmony(lastNote + 2, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote + 2);
//							hasValidHalfNote = true;
//						}
//						if(rules.isConsonantHarmony(lastNote + 5, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote + 5);
//							hasValidHalfNote = true;
//						}
//						break;
//						
//					case -4:
//						//split a fifth jump by a half
//						if(rules.isConsonantHarmony(lastNote - 2, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote - 2);
//							hasValidHalfNote = true;
//						}
//						if(rules.isConsonantHarmony(lastNote - 5, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote - 5);
//							hasValidHalfNote = true;
//						}
//						break;
//						
//					case 5:
//						//split a sixth jump by a half
//						if(rules.isConsonantHarmony(lastNote + 2, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote + 2);
//							hasValidHalfNote = true;
//						}
//						if(rules.isConsonantHarmony(lastNote + 3, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote + 3);
//							hasValidHalfNote = true;
//						}
//						break;
//					case -5:
//						if(rules.isConsonantHarmony(lastNote - 2, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote - 2);
//							hasValidHalfNote = true;
//						}
//						if(rules.isConsonantHarmony(lastNote - 3, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote - 3);
//							hasValidHalfNote = true;
//						}
//						break;
//					case 6:
//						if(rules.isConsonantHarmony(lastNote + 7, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote +7);
//							hasValidHalfNote = true;
//						}
//						break;
//					case -6:
//						if(rules.isConsonantHarmony(lastNote -7, lastParentNote)) {
//							validNextHalfNoteIndexes.add(lastNote - 7);
//							hasValidHalfNote = true;
//						}
//						break;
//					case 7:
//						//TODO -> how to traverse this?
//						break;
//					case -7:
//						//TODO -> how to traverse this? 
//						break;
//						
//					default:
//						log("Unhandled Second Species Interval INVESTIGATE THIS IMMEDIATELY");
//						break;
//						
//						
//					}
//					if(hasValidHalfNote) {
//						validNextWholeNoteIndexes.add(i);
//					}
//					
//				}
//				
//				validNextIndexes = validNextHalfNoteIndexes;
//				validNextIndexesSaved = validNextWholeNoteIndexes;
//				log("validNextIndexes are half note idexes");
//				
//			} else {
//				log("else we set the valid Next Indexes Saved to true");
//				//TODO logic to truly prune this second index. 
//				validNextIndexes = validNextIndexesSaved;
//			}
		}
		
	}

	private void pruneForUpperLowerVoice() {
		if(isFirstSpecies()) {
			for(int i = 1; i <= noteMelody.getParentMelody().size(); i++) {
				ArrayList<Integer> currIdxList = validIndexesMap.get(i).getAll();
				for (int j = currIdxList.size() - 1; j >= 0	; j--) {
					if(noteMelody.isUpperVoice() && 
							noteMelody.getParentMelody().get(i) > currIdxList.get(j)) {
//						log("Pruning away " + currIdxList.get(j) + " less than CF note: " + noteMelody.getParentNote(i));
						currIdxList.remove(j);
					}
					if(noteMelody.isLowerVoice() && 
							noteMelody.getParentMelody().get(i) < currIdxList.get(j)) {
//						log("Pruning away " + currIdxList.get(j) + " greater than CF note: " + noteMelody.getParentNote(i));
						currIdxList.remove(j);
					}
				}
//			log("Pruned idx list for " + i + " is " + currIdxList.toString());
			}
		} else if(isSecondSpecies()) {
			log("will prune on it's own");
		} else {
			log("pruning for something unhandled:" + rules.speciesType.name);
		}
		
	}

	public void addIfConsonantHarmony(int index1, int index2, NoteIndexCollection indexList) {
		log("have we determine upper vs lower voice? upper: " + noteMelody.isUpperVoice() + "lower:" + noteMelody.isLowerVoice());
		if(rules.isConsonantHarmony(index1, index2)) {
			//TODO check if we violate the upper or lower voice.
			log("Consonant; add; pass:" + index1 + " vs: " + index2);
			indexList.addIfNotDuplicate(index1);
		} else {
			log("Not consonant, pass:" + index1 + " vs: " + index2);
		}
	}
	
	private boolean nextNoteDownbeat() {
		if(isSecondSpecies()) {
			//this is a stupid way to determine this, I'm sure there's a better way. 
			log("noteMelody.melodyLength()" + noteMelody.melodyLength() );
			log("note melody length%1: " + noteMelody.melodyLength()%1 );
			if((noteMelody.melodyLength()) % 1 == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			log("lastNoteDownbeat called for unhandled species type:" + rules.speciesType);
		}
		return false;
	}

	public ArrayList<Integer> getNextValidIndexArray() {
		return validIndexesMap.get(noteMelody.size() + 1).getAll();
	}

	public ArrayList<Integer> getNextValidIndexArrayRandomized() {
//		log("notes:" + melodyInProgress.getAll().toString());
		if(null == noteMelody) {
			log("notes are null");
		}
//		log("validIndexesMap" + validIndexesMap);
//		log("melodyInProgress size" + melodyInProgress.size());
//		log("valid next indexes:" + validNextIndexes);
		return validNextIndexes.getRandomized();
	
	}
	
	public SpeciesSystem getSpeciesSystem() {
		return rules.speciesType.speciesSystem;
	}
	
	public void log(String msg) {
		if(logginOn) {
			
			System.out.println("SpeciesBuilder-Log:   " + msg + "   testIndex " + testIndex + "         melody: " + noteMelody.getAll() );
		}
	}


//	public MelodyInProgress getMelody() {
//		return melodyInProgress;
//	}
	public NoteMelodyInProgress getMelody() {
		return noteMelody;
	}
	
	private void setTestStepFields() {
		testStepIndex = indexMap.get(testIndex);
		testStepInterval = testStepIndex - indexMap.get(lastIndex);
//		log("testStepInterval:" + testStepInterval);
		
	}

}
