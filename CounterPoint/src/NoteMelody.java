import java.util.ArrayList;
import java.util.List;

public abstract class NoteMelody extends NoteCollection {
	protected SpeciesRules rules; // = new SpeciesRules();
	
	private boolean uniqueZenith = false;
	private boolean uniqueNadir = false;
	
	private int zenithPos;
	private int nadirPos;
	private int climaxPos;
	public boolean validZenith;
	public boolean validNadir;
	public boolean raisedLeadingTone;
	//protected ArrayList<Integer> testChildMelodyNotes;
	//private int firstNote;
	private int minNadirIndex;
	private int maxZenithIndex;
	private List<Integer> validZenithIndexes = new ArrayList<Integer>();
	private List<Integer> validNadirIndexes = new ArrayList<Integer>();
	private StepIndexCollection stepIndexes = new StepIndexCollection();
	private ArrayList<Integer> motions = new ArrayList<Integer>();
	private ArrayList<Integer> downBeats = new ArrayList<Integer>();
	//protected Melody parentMelody;
	protected NoteMelody parentNoteMelody;
	private TestMelody testMelody;
	protected TestMelody testChildMelody;
	private int noteRepeats;
	private Mode mode;
	
	public boolean upperVoice = false;
	public boolean lowerVoice = false;
	
	private IntervalIndexCollection intervals = new IntervalIndexCollection();
	
	public NoteMelody() {
		
	}
	

	public NoteMelody(SpeciesRules rules, Mode mode) {
		this.rules = rules;
		this.mode = mode;
//		log("Melody rulesOnly constructor rules object: " + rules);
	}
	
	public NoteMelody (NoteMelody parent) {
		addAllNotes(parent);
//		for(int i : parentNIC.getAll()) {
//			this.items.add(i);
//		}
		//this.firstNote = parentNIC.firstNote;
		this.rules = parent.rules;
		this.mode = parent.mode;
//		log("Melody constructorClone rules object:" + rules);
		//this.parentMelody = parent.parentMelody;
		this.parentNoteMelody = parent.parentNoteMelody;
		this.uniqueNadir = parent.uniqueNadir;
		this.uniqueZenith = parent.uniqueZenith;
		this.validZenith = parent.validZenith;
		this.validNadir = parent.validNadir;
		this.raisedLeadingTone = parent.raisedLeadingTone;
		this.climaxPos = parent.climaxPos;
		this.zenithPos = parent.zenithPos;
		this.nadirPos = parent.nadirPos;
		this.minNadirIndex = parent.minNadirIndex;
		this.maxZenithIndex = parent.maxZenithIndex;
		this.noteRepeats = parent.noteRepeats;
		this.upperVoice = parent.upperVoice;
		this.lowerVoice = parent.lowerVoice;
		
		//this.validNadirIndexes = new ArrayList<Integer>();
		for(int i : rules.validNadirIndexesPrimitive) {
			this.validNadirIndexes.add(i);
		}
		
		//this.validZenithIndexes = new ArrayList<Integer>();
		for(int i : rules.validZenithIndexesPrimitive) {
			this.validZenithIndexes.add(i);
		}
		
		this.intervals = new IntervalIndexCollection();
		for (int i : parent.getIntervals().getAll()) {
			this.intervals.add(i);
		}
		
		//this.stepIndexes = new StepIndexCollection();
//		log("ParentNIC step INdexes in Melody constructor: " + parentNIC.getStepIndexes());
		for (int i : parent.stepIndexes.getAll()) {
			this.stepIndexes.add(i);
		}
		
		//this.motions = new ArrayList<Integer>();
		for(int i : parent.motions) {
			this.motions.add(i);
		}
//		log("what are my stepINdex now: " + this.getStepIndexes());
		
		for(int i : parent.downBeats) {
			this.downBeats.add(i);
		}
	}
	


	//this method is used instead of add to perform extra maintenance logic every time a note is added. 
	public void addNote(int newNote, double length) {
		//log("items before:" + items.toString());
		//log("addNote Melody constructor:" + size());
		super.add(new Note(newNote, length));
		//super.add(newNote);  ///old call adding an integer
		//log("items now:" + items.toString());
		//stepIndexes.add(newStepIndex);
		checkInternals(newNote);
		//log("zenith is: " + zenith() + "and unique is:" + uniqueZenith );
		//log("nadir iz: " + nadir() + "and unique is:" + uniqueNadir);	
	}
	
	public void addStepIndex(Integer newStepIndex) {
		stepIndexes.add(newStepIndex);
		
	}
	
	private void checkInternals(int newNote) {
		if(size() > 1) {
			
			
			if(newNote == get(-2)) {
				noteRepeats++;
			}
			
			if(newNote > zenith()) {
				//log("New note greater than current Zenith");
				zenithPos = notes.size();
				uniqueZenith = true;
				minNadirIndex = (minNadirIndex < (zenith() - rules.maxIndexRange())) ? (zenith() - rules.maxIndexRange()) : minNadirIndex;
				//log("new zenith: " + zenith() + " minNadirIndex: " + minNadirIndex + " rules.maxIndexRage: " + rules.maxIndexRange);
			} else if (newNote == zenith()) {
				uniqueZenith = false;
			}
			if(newNote < nadir()) {
				//log("New note less than current Nadir");
				nadirPos = notes.size();
				uniqueNadir = true;
				maxZenithIndex = (maxZenithIndex > (nadir() + rules.maxIndexRange())) ? (nadir() + rules.maxIndexRange()) : maxZenithIndex;
				//log("new nadir: " + nadir() + " maxZenithIndex: " + maxZenithIndex + " rules.maxIndexRage: " + rules.maxIndexRange);
			} else if (newNote == nadir()) {
				uniqueNadir = false;
			}

		} else if(size() == 1) {
			minNadirIndex = getFirst() - rules.maxIndexRange();
			maxZenithIndex = getFirst() + rules.maxIndexRange();
//			log("minNadirIndex" + minNadirIndex);
//			log("maxZenithIndex" + maxZenithIndex);
			
			for(int i : rules.validZenithIndexesPrimitive) {
				this.validZenithIndexes.add(i + getFirst());
			}
			for(int i : rules.validNadirIndexesPrimitive) {
				this.validNadirIndexes.add(i + getFirst());
			}
			
		}
		
		/**
		 * If neither upper nor lower voice has been checked, check them upon adding this note 
		 * 
		 */

		//log("Note add to Melody, Zenith of : " + zenith() + " is " + uniqueZenith + "; nadir of " + nadir() + " is " + uniqueNadir);
	}
	
	protected boolean hasParentMelody() {
		//log("Checking parent Melody for this " + rules.speciesType + ": " + parentMelody );
		return null == parentNoteMelody ? false : true;
	}

	public int zenith() {
		return get(zenithPos);
	}
	
	public int nadir() {
		return get(nadirPos);
	}
	
	public int zenithPos() {
		return zenithPos;
	}
	
	public int nadirPos() {
		return nadirPos;
	}
	
	public boolean uniqueZenith() {
		return uniqueZenith;
	}
	public boolean uniqueNadir() {
		return uniqueNadir;
	}

	public int getLastInterval() {
//		log("getLast()" + getLast() + "; get(-1)" + get(-1));
		return (getLast() - get(-2));
	}

	public int getInterval(int intervalIndex) {
		return intervals.get(intervalIndex);
		//return get(intervalIndex) - get(intervalIndex - 1);
	}
	
	public int getMinNadirIndex() {
		return minNadirIndex;
	}
	
	public int getMaxZenithIndex() {
		return maxZenithIndex;
	}

//	private int getPentultimateNote() {
//		return items.get(items.size() - 2);
//	}
	
	public boolean checkIfValidZenithIndex(int testIndex) {
//		log("validZenithIndexs" + validZenithIndexes.toString() + "testIndex:" + testIndex );
		if(this.validZenithIndexes.contains((get(1) + testIndex)%7)) {
			return true;
		}
		return false;
	}
	
	public boolean checkIfValidNadirIndex(int testIndex) {
//		log("validNadirIndexs" + validNadirIndexes.toString() + "testIndex:" + testIndex );
		if(this.validNadirIndexes.contains((get(1) + testIndex)%7)) {
			return true;
		}
		return false;
	}
	
	public void setParentMelody(NoteMelody parentMelody) {
		this.parentNoteMelody = parentMelody;
//		ArrayList<Integer> parentNoteIndexes = new ArrayList<Integer>();
//		for(Note note : parentMelody) {
//			parentNoteIndexes.add(note.index());
//		}
//		this.parentMelody = parentNoteIndexes;
	}
//	
//	public Melody getParentMelody() {
//		return parentMelody;
//	}
	
	public NoteMelody getParentMelody() {
		return parentNoteMelody;
	}
	private void log(String msg) {
		System.out.println("Melody-Log:           " + msg);
	}


	public int zenithMagnitude() {
		return Math.abs(zenith() - getFirst());
	}
	public int nadirMagnitude() {
		return Math.abs(getFirst() - nadir());
	}
	
	public void annealZenith() {
//		log("annealing Zenith" + zenithPos);
		validZenith = true;
		validNadir = false;
		climaxPos = zenithPos;
	}
	
	public void annealNadir() {
//		log("annealing Nadir" + nadirPos);
		validZenith = false;
		validNadir = true;	
		climaxPos = nadirPos;
	}
	
	int modeNotesTally(int noteIndex) {
		int t = 0;
		int testNote = noteIndex %7;
		for (int i: getAll()) {
			if((i + 14)%7 == testNote) {
				t++;
			}
		}
		//log("returning mode tally of: " + t);
		return t;
	}
	
	int downbeatNotesTally(int noteIndex) {
		
		int t = 0;
		int testNote = noteIndex %7;
		
		if(rules.speciesType == SpeciesType.SECOND_SPECIES) {
			for(int i = 0; i <= size(); i += 2) {
				if((i + 14)%7 == testNote) {
					t++;
				}
			}
		} else {
			t = modeNotesTally(noteIndex); //TODO verify for cantus firmus. 
		}
		
//		for (int i: getAll()) {
//			if((i + 14)%7 == testNote) {
//				t++;
//			}
//		}
		//log("returning mode tally of: " + t);
		return t;
	}

	public int getClimaxPos() {
	    //log("returning climax pos" + climaxPos);
		return climaxPos;
	}

	public IntervalIndexCollection getIntervals() {
		return intervals;
	}
	
	public int leapTally() {
//		log("leapTally() intervals are: " + intervals);
		return intervals.leapTally();
	}

	public void addInterval(int interval) {
		intervals.add(interval);
		
	}

	public int noteRepeatCount() {
		return noteRepeats;
	}
	
	public void incrementNoteRepeat() {
		noteRepeats++;
		
	}
	
	public SpeciesType getSpeciesType() {
		return rules.speciesType;
	}
	
	public int getStepIndex(int notePos) {
		
		return stepIndexes.get(notePos);
	}
	
	public StepIndexCollection getStepIndexes() {
		return stepIndexes;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void tailorStepIndexes() {
		stepIndexes = new StepIndexCollection(rules.tailorStepIndexes(this));
	}

	public boolean isUpperVoice() {
		return upperVoice;
	}
	
	public boolean isLowerVoice() {
		return lowerVoice;
	}

	public int getLastStepIndex() {
		return stepIndexes.getLast();
	}

	public void setRaisedLeadingTone(boolean isLeadingToneRaised) {
		raisedLeadingTone = isLeadingToneRaised;
	}
	
	public boolean isLeadingToneRaised() {
		return raisedLeadingTone;
	}
	
	public ArrayList<Integer> getMotions() {
		return motions;
	}
	
	public void addMotion(int newMotion) {
		motions.add(newMotion);
	}
	
	//TODO -> how is this handled? 
//	public ArrayList<Integer> getChildSpeciesTestMelody() {
//		return testChildMelodyNotes;
//	}
	
	
	public void setChildSpeciesTest(TestMelody testMelody) {
		this.testChildMelody = testMelody;
	}
	
	public TestMelody getChildSpeciesTestMelody() {
		return this.testChildMelody;
	}
	
	public TestMelody getTestMelody() {
		return testMelody;
	}
	
//	public void setChildSpeciesTest(int[] testMelody) {
//		log("setting child test melody!!" + testMelody);
//		testChildMelodyNotes = new ArrayList<Integer>();
//		
//		for (int i = 0; i < testMelody.length; i++) {
//			this.testChildMelodyNotes.add(testMelody[i]);
//		}
//	}
	
	public boolean isTestingChildSpecies() {
		return null == testChildMelody ? false : true;
	}
	
	public double melodyLength() {
		double melodyLength= 0;
		for(Note note : notes ) {
			melodyLength += note.noteLength;
		}
		return melodyLength;
	}
	
	public double parentMelodyLength() {
		
		return parentNoteMelody.melodyLength();
	}
	
	public void setTestMelody(TestMelody testMelody) {
		this.testMelody = testMelody;
		
	}
	
	public double testMelodyLength() {
		// TODO Auto-generated method stub
		return testMelody.melodyLength();
	}
	
}
