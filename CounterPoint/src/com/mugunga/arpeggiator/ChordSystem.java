package com.mugunga.arpeggiator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChordSystem {
//	HashMap<String, ChordFollowFreq> chordFreq = new HashMap<String, ChordFollowFreq>();
//	HashMap<String, ChordFollowFreq> chordPrecede = new HashMap<String, ChordFollowFreq>();
	HashMap<String, ChordFreq> chordFreq = new HashMap<String, ChordFreq>();
	HashMap<String, ChordFreq> chordPrecede = new HashMap<String, ChordFreq>();
	ChordProgression chordProg;
	
	public ChordSystem(ChordProgression chordProg) {
		this.chordProg = chordProg;
		generateFrequencyMap();
	}

	private void generateFrequencyMap() {
		HashMap<String, ChordFreq> chordFreq = new HashMap<String, ChordFreq>();
		int c = 0;
		Chord lastChord = null;
		for(Chord chord : chordProg) {
			c++;
			log("processing chord: " + chord + " :: " + c);
			if(c>1) {
				if(!chordFreq.containsKey(lastChord)){
					log(" add Chord Follow Freq for " + lastChord.toString() + " to map");
					chordFreq.put(lastChord.toString(), new ChordFreq(lastChord));
					
				} else {
					log("chordStructure already contains map for chord");
					
				}
				
				chordFreq.get(lastChord).addChordOccurance(chord, 1);
				chordFreq.get(lastChord).print();
				log("chordStructure size" + chordFreq.size());
				
			}
			lastChord =  chord;
			
			//chord.print();
		}
		this.chordFreq = chordFreq;
		
	}
	
	public Chord firstChord() {
		return chordProg.get(0);
	}
	
	private void log(String msg) {
		System.out.println("Chord System Log:      " + msg);
	}

	public Chord randomNextChord(Chord previousChord) {
		//TODO Chord Progression needs a tpString method
		log("get random chord :" + previousChord.toString());
		//TODO: chord freq returning null
		log("chordFreq: " + printChordMap(chordFreq));
		if(chordFreq.containsKey(previousChord)) {
			log("do I contain previous chord?");
			return chordFreq.get(previousChord).getRandomNextChord();
		} else {
			return null;
		}
		
		
	}

	private String printChordMap(HashMap<String, ChordFreq> chordFreq) {
		for (Map.Entry<String, ChordFreq> entry : chordFreq.entrySet()) {
			l("Chord " + entry.getKey() + ":::");
			entry.getValue().print();
			
		}
		l("---");
		
		return null;
	}
	
	private void l(String msg) {
		System.out.println("ChordFollowFreqLog:   " + msg);
	}
}
