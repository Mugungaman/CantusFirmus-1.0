import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



public class ChordFreq {
	private Chord baseChord;
	private Map<String, Integer> chordFreqs;
//	private ArrayList<Chord> chordFreqs;
	//private ArrayList<Chord> followChords;
	//private int freq;
	
	ChordFreq(Chord baseChord) {
		this.baseChord = baseChord;
		chordFreqs = new HashMap<String, Integer>();
//		chordFreqs = new ArrayList<Chord>();
	}
	
	public void addChordOccurance(Chord chord, int Freq) {
		l("adding a chord to " + baseChord + " chord occurnace for " + chord);
//		chordFreqs.add(chord);
//		
//		if(!chordFreqs.contains(chord)) {
//			log("does not contain key yet");
//			chordFreqs.add(chord);
//		} else {
//			log("already contain keys");
//			chordFreqs.put(chord.toString(), chordFreqs.get(chord) + 1);
//		}
		
		if(!chordFreqs.containsKey(chord)) {
			chordFreqs.put(chord.toString(), 1);
		} else {
			l("already contain keys");
			chordFreqs.put(chord.toString(), chordFreqs.get(chord) + 1);
		}
	}
	
	public void print() {
		l("<---Printing Chord Follow Freq For: " + baseChord + " --->");
		int totalEntries = 0;
		for (Map.Entry<String, Integer> entry : chordFreqs.entrySet()) {
			totalEntries += entry.getValue();
		}
		for (Map.Entry<String, Integer> entry : chordFreqs.entrySet()) {
			l("Chord " + entry.getKey() + " Freq%: " + (double)entry.getValue()/(double) totalEntries);
		}
		
	}
	
	private void l(String msg) {
		System.out.println("ChordFollowFreqLog:   " + msg);
	}
	
	public Chord getRandomNextChord() {
		ArrayList<Chord> freqChart = new ArrayList<Chord>();
		for(Map.Entry<String, Integer> entry : chordFreqs.entrySet()) {
			for(int i = 0; i < entry.getValue(); i ++) {
				
				//freqChart.add(entry.getKey());
			}
		}
		//TODO save this freqChart?
		
		Random random = new Random();
		return freqChart.get(random.nextInt(freqChart.size()));
		//TODO need to return String back to Chord

	}
	
}
