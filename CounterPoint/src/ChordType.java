import java.util.ArrayList;

/**
 * 
 * @author laurencemarrin
 *
 *Step reference:
 *     1   -> minor 2nd
 *     2  -> major 2nd
 *     3   -> minor 3rd
 *     4  -> major 3rd
 *     5   -> perfect 4th
 *     6  -> tritone
 *     7   -> perfect 5th
 *     8  -> minor 6th/augmented 5th
 *     9   -> major 6th
 *     10 -> minor 7th
 *     11  -> major 7th
 *     12 -> octave
 *
 *
 */
public enum ChordType {
	

	
	MAJOR_TRIAD("Major Triad", "", new Interval[]{Interval.MAJOR_3RD, Interval.MINOR_3RD},
			new int[] {}),
	MINOR_TRIAD("Major Triad", "-", new Interval[]{Interval.MINOR_3RD, Interval.MAJOR_3RD},
			new int[] {}),
	AUGMENTED("Augumented", "aug", new Interval[] {Interval.MAJOR_3RD, Interval.MAJOR_3RD},
			new int[] {}),
	DIMINISHED("Diminished", "dim",new Interval[]{Interval.MINOR_3RD, Interval.MINOR_3RD},
			new int[] {}),       
	MAJOR_7th("Major 7th", "7", new Interval[]{Interval.MAJOR_3RD, Interval.MINOR_3RD, Interval.MINOR_3RD},
			new int[] {}),
	MINOR_7th("Minor 7th", "-7",new Interval[]{Interval.MINOR_3RD, Interval.MAJOR_3RD, Interval.MINOR_3RD},
			new int[] {}),       

	MAJOR_M7th("Major M7th", "M7", new Interval[]{Interval.MAJOR_3RD, Interval.MINOR_3RD, Interval.MAJOR_3RD},
			new int[] {}),       
	MINOR_M7th("Minor M7th", "-M7", new Interval[]{Interval.MINOR_3RD, Interval.MAJOR_3RD, Interval.MAJOR_3RD},
			new int[] {}),       
	DIMINISHED_7th("Diminished", "dim7",new Interval[]{Interval.MINOR_3RD, Interval.MINOR_3RD, Interval.MINOR_3RD},
			new int[] {}),       
	SUSPENDED_4th("Suspended 4th", "sus4", new Interval[]{Interval.PERFECT_4TH, Interval.MAJOR_2ND},
			new int[] {}),       
	SUSTAINED_4th_7th("Sustained 4th-7th", "sus4-7",new Interval[]{Interval.PERFECT_4TH, Interval.MAJOR_2ND, Interval.MINOR_3RD},
			new int[] {}),       
	MINOR_6th_ADD_5b("Minor 6th add 5b", "-6 add5b",new Interval[]{Interval.MINOR_3RD, Interval.MINOR_3RD,Interval.MINOR_2ND,  Interval.MAJOR_2ND},
			new int[] {}),       
	MAJOR_6th("Major 6th", "6",new Interval[]{Interval.MAJOR_3RD, Interval.MINOR_3RD,Interval.MAJOR_2ND},
			new int[] {}),       
	MINOR_6th("Minor 6th", "-6",new Interval[]{Interval.MINOR_3RD, Interval.MAJOR_3RD,Interval.MAJOR_2ND},
			new int[] {}),       
	NOTHING("", "", new Interval[0], new int[0]);

	public final int ZERO_INVERSION = 0;
	public final int FIRST_INVERSION = 1;
	public final int SECOND_INVERSION = 2;
	public final int THIRD_INVERSION = 3;
	public final int FOURTH_INVERSION = 4;
	
	public final String longDisplay;
	public final String shortDisplay;
	public final Interval[] intervals;
	public final int[] firstInversionSteps;
	
	ChordType(String longDisplay,
			String shortDisplay,
			Interval[] intervals, 
			int[] i) {
		
		this.longDisplay = longDisplay;
		this.shortDisplay = shortDisplay;
		this.intervals = intervals;
		this.firstInversionSteps = i;
	}
	
	ChordType() {
		longDisplay = "";
		shortDisplay = "";
		firstInversionSteps = null;
		intervals = null;
	}
	
	public StepIndexCollection getChord() {
		StepIndexCollection  chordSteps = new StepIndexCollection();
		chordSteps.add(0);
		for(Interval i : intervals) {
//			int nextInterval = chordSteps.sum() + i.steps;
			chordSteps.appendInterval(i);
//			chordSteps.add(chordSteps.getLast() + i.steps);
//			log("chordSteps:" + chordSteps.toString());
		}
		return chordSteps;
	}
	
	public StepIndexCollection getSteps(int inversionType) {
		ArrayList<Interval> invertedIntervals = invertedIntervals(inversionType);
		StepIndexCollection  chordSteps = new StepIndexCollection();
		if(intervals.length < inversionType) {
			return chordSteps;
		}
		chordSteps.add(0);
		for(Interval i : invertedIntervals) {
			chordSteps.add(chordSteps.getLast() + i.steps);
		}
		return chordSteps;

		
	}
	
	public void printNotesAndInversions() {
		log("Chord:" + longDisplay);
		
	}
	
	public ArrayList<Interval> invertedIntervals(int inversion) {
		ArrayList<Interval> invertedIntervals = new ArrayList<Interval>();
		if(intervals.length  < inversion) {
			return new ArrayList<Interval>();
		}
		
		int accum = 0;
		for(int i = inversion; i < intervals.length; i++) {
			invertedIntervals.add(intervals[i]);
			accum += intervals[i].steps;
		}
		if(inversion > 0) {
			Interval interval = lastHiddenInterval();
			invertedIntervals.add(interval);
			accum += interval.steps;
			
			
			
			for(int i = 0; i < inversion - 1; i++) {
				invertedIntervals.add(intervals[i]);
				accum += intervals[i].steps;
			}
			
		}
		
		return invertedIntervals;
	}
	
	private Interval lastHiddenInterval() {
//		log("Last HIdden INterval: 12-" + sumIntervals());
		return Interval.getInterval(12-sumIntervals());
	}

	public int sumIntervals() {
		int sum = 0;
		for(Interval i : intervals) {
			sum += i.steps;
		}
		return sum;
		
	}
	
	private void log(String msg) {
		System.out.println("ChordType log:   " + msg);
	}

	public void printInversions() {
		for(int i = 1; i <=4 ; i++) {
			StepIndexCollection inversionSteps = getSteps(i);
			if(inversionSteps.empty()) {
				log("Inversion " + i + ": does not exist");
				
			} else {
				log("Inversion " + i + ":" + inversionSteps.getAll());
			}
		}
		
	}

	private StepIndexCollection inversionSteps(int i) {
		ArrayList<Interval> invertedIntervals = invertedIntervals(i);
		log("inverted Intervals..." + invertedIntervals);
		return null;
	}

/**
 *  C MAJOr
 * 
 * 
 * 
 */

}

