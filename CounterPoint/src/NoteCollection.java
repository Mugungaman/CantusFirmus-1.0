import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public abstract class NoteCollection implements Iterable<Note> {
	
	protected ArrayList<Note> notes = new ArrayList<Note>();
	
	public NoteCollection() {
		
	}
	
	public NoteCollection(ArrayList<Note> notes) {
		this.notes = notes;
	}
	
	public void setNotes(int[] indexes, double[] lengths) {
		if(indexes.length != lengths.length) {
			log("mismatch in number of notes:" + indexes + " lengths: " + lengths);
		} else {
			for(int i = 0; i < indexes.length; i++) {
				addNote(new Note(indexes[i], lengths[i] ));
			}
		}
	}
	
	public void setUniformLengthMelody(int[] indexes, NoteLength length) {
		for(int i : indexes) {
			addNote(new Note(i, length));
		}
	}
	
	public void addNote(Note note) {
		notes.add(note);
	}
	
	/*
	 * This method has some important assumptions:
	 * 
	 * 1) Generally we are looking back from the most recent notes. So if we send a parameter -3, it means we want the 3rd note
	 * back from what we currently have. All negative parameters are looking backward
	 * 
	 * 2) Positive requests n will return the nth note, since the index starts at 0, we add 1 to the index.
	 * 
	 * 3) requesting the 0th note just returns the 0th index, or the first note. 
	 */
	public int get(int index) {
//		log("getting index of:" + index + " from " + notes.size() + " notes");
		if(index > 0) {
			return notes.get(index - 1).index();
		} else if(index < 0) {
			return notes.get(notes.size() + index).index();
		} else if (index == 0) {
			return notes.get(0).index();
		}
		return 911;
	}
	
	public void add(Note item) {
		notes.add(item);
	}
	
	public void addIfNotDuplicate(Note item) {
		if(!notes.contains(item)) {
			add(item);
		}
	}
	
	public void addAllNotes(NoteCollection notes) {
		this.notes.addAll(notes.getAllNotes());
	}
	
//	public void addAll(MusicIntCollection notes) {
//		this.notes.addAll(notes.getAll());
//		//this.notes.addAll(notes);
//	}
	public ArrayList<Note> getAllNotes() {
		return notes;
	}
	
	public ArrayList<Integer> getAll() {
		ArrayList<Integer> getNotes = new ArrayList<Integer>();
		for(Note note : notes) {
			getNotes.add(note.index());
		}
		
		return getNotes;
	}
	
	public Iterator<Note> iterator() {
		return notes.iterator();
	}
	
	public int size() {
		return notes.size();
	}
	
	public int getLast() {
		return notes.get(notes.size() - 1).index();
	}
	
	public int getFirst() {
		return notes.get(0).index();
	}
	
	
	public String toString() {
		return notes.toString();
	}
	
	//randomizes the notes and returns them but does not randomize the actual object's notes
//	public ArrayList<Integer> getRandomized() {
//		
//		ArrayList<Integer> reduceList = new ArrayList<Integer>();
//		ArrayList<Integer> randomizedSteps = new ArrayList<Integer>();
//		if(notes.size() > 0) {
//			Random random = new Random();
////		log("reduceList:" + reduceList);
//			for(int i : notes) {
//				reduceList.add(i);
//			}
////		log("reduceList:" + reduceList);
//			do {
//				int randomIndex = random.nextInt(reduceList.size());
//				randomizedSteps.add(reduceList.get(randomIndex));
//				reduceList.remove(randomIndex);
//			} while(reduceList.size() > 0);
////		log(" PRE RANDOMIZED notes:" + this.notes);
////		log(" PST RANDOMIZED notes:" + randomizedSteps);
//		}
//		
//		return randomizedSteps;
//	}
	
	boolean empty() {
		return notes.size() == 0 ? true : false;
	}
	
	private void log(String msg) {
		System.out.println("MusicIntColl-Log:     " + msg);
	}
	
	public void removeDuplicates() {
		//
	}
	
}