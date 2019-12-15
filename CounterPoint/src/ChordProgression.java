import java.util.ArrayList;
import java.util.Iterator;


public class ChordProgression implements Iterable<Chord>{

	private ArrayList<Chord> chords = new ArrayList<Chord>();
	
	public ChordProgression() {
		
	}
	
	public ChordProgression(ArrayList<Chord> chords) {
		for(Chord chord : chords)  {
			this.chords.add(chord);
		}
	}
	
	public void add(Chord chord) {
		chords.add(chord);
	}
	
	@Override
	public Iterator<Chord> iterator() {
		
		return chords.iterator();
	}
	public int size() {
		return chords.size();
	}
	
	public Chord getFirst(int index) {
		return chords.get(0);
	}
	
	public Chord get(int index) {
		
//			log("getting index of:" + index + " from " + items.size() + " notes");
		if(index > 0) {
			return chords.get(index - 1);
		} else if(index < 0) {
			return chords.get(chords.size() + index);
		} else if (index == 0) {
			return chords.get(0);
		}
		return new Chord();
	}
}
