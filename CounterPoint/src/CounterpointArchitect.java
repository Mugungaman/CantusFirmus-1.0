import java.util.HashMap;
import java.util.Random;

/**
 * Eventually Counterpoint Architect will be the master controller that calls Arpeggiator, Melody sequencer, etc
 * and will build entire songs based on user parameters. 
 * 
 * Bach's Prelude in C Major is a perfect example of arpeggiation and Chord progression, 
 * so we are using it to model the components necessary for an arpeggiator: Notes, Keys, Intervals, ChordType, etc....
 * 
 * @author laurencemarrin
 *
 */
public class CounterpointArchitect {
	
	private SpeciesRules rules;
	private ChordProgression chordProg;
	
	public CounterpointArchitect() {
		
	}
			
	public CounterpointArchitect(SpeciesType speciesType) {
		rules = new SpeciesRules(speciesType);
		l("Hello CounterPoint Architect");
		
		chordProg = new ChordProgression();
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.C)); // I
		chordProg.add(new Chord(ChordType.MINOR_7th, Key.D));   //II 
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.G));   //V
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.C)); //I
		
		chordProg.add(new Chord(ChordType.MINOR_TRIAD, Key.A)); //VI
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.D)); //II
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.G)); //V
		chordProg.add(new Chord(ChordType.MAJOR_M7th, Key.C));  //I
		
		chordProg.add(new Chord(ChordType.MINOR_7th, Key.A));  //VI
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.D)); //II
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.G));  //V
		chordProg.add(new Chord(ChordType.DIMINISHED_7th, Key.E));  //Vdim
		
		chordProg.add(new Chord(ChordType.MINOR_7th, Key.D));  //II
		chordProg.add(new Chord(ChordType.DIMINISHED_7th, Key.B));   //IIdim
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.C)); //I (III) of (V) of new tonic D? 
		chordProg.add(new Chord(ChordType.MAJOR_M7th, Key.F)); //IV
		
		chordProg.add(new Chord(ChordType.MINOR_7th, Key.D)); //II
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.G)); //V
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.C));  //I
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.C));   //I7
		
		chordProg.add(new Chord(ChordType.MAJOR_M7th, Key.F)); //IV
		chordProg.add(new Chord(ChordType.DIMINISHED_7th, Key.A)); //IVdim
		chordProg.add(new Chord(ChordType.MINOR_6th_ADD_5b, Key.F));  // IVm6add5bweird squeeze? 
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.G));
		
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.C));  //I
		chordProg.add(new Chord(ChordType.SUSTAINED_4th_7th, Key.G)); //V sus 4M7
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.G)); //V7
		chordProg.add(new Chord(ChordType.MINOR_6th_ADD_5b, Key.C)); //Im6 add 5b
		
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.C));  //I
		chordProg.add(new Chord(ChordType.SUSTAINED_4th_7th, Key.G)); //V sus 4M7
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.G));     // V7
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.C));   //I7
		
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.F));      //IV
		chordProg.add(new Chord(ChordType.MAJOR_7th, Key.G));      //I
		
		chordProg.add(new Chord(ChordType.MAJOR_TRIAD, Key.C));

		String chordName = "Gb-";
		if(chordName.matches("\\wb.*")) {
			String chordKey = (String) chordName.subSequence(0, 2);
			String chordType = chordName.substring(2);
			
			l("flat chord" + chordName);
			l("chordKey: " + chordKey);
			l("chordType:" + chordType+";");
		}
		Arpeggiator arp = new Arpeggiator(new ChordSystem(chordProg));
		arp.arpeggiate(33);
	}
	
	
	private void generateChordProgression(HashMap<String, ChordFreq> chordStructure) {
		// TODO Auto-generated method stub
		
	}

	private void l(String msg) {
		System.out.println("CP-Architect-log:      " + msg);
	}

}




