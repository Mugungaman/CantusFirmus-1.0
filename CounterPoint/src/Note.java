
public class Note {
	
	public Note(int key, double length) {
		this.keyIndex = key;
		this.noteLength = length;
	}
	
	public Note(int index, NoteLength noteLength) {
		this.keyIndex = index;
		this.noteLength = noteLength.noteLength;
	}

	public int keyIndex;
	public double noteLength;
	
	public int index() {
		return keyIndex;
	}
}
