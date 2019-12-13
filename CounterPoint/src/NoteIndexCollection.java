import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class NoteIndexCollection extends MusicIntCollection{
	//ArrayList<Integer> notes = new ArrayList<Integer>();
	
	public NoteIndexCollection() {
		
	}
	
	//This constructor deliberately does not use the add() method of parent class because it will trigger a bunch of uneeded logic
	public NoteIndexCollection(ArrayList<Integer> notes) {
		for(int i : notes) {
			items.add(i);
		}
	}

	
//	@Override
//	public Iterator<Integer> iterator() {
//		return items.iterator();
//	}
 }
