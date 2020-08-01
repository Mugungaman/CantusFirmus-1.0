package com.mugunga.counterpoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class MusicIntCollection implements Iterable<Integer> {
	public List<Integer> items = new ArrayList<>();
	
	/**
	 * This class has some important assumptions:
	 * 
	 * 1) Generally we are looking back from the most recent notes. So if we send a parameter -3, it means we want the 3rd note
	 * back from what we currently have. All negative parameters are looking backward
	 * 
	 * 2) Positive requests n will return the nth note, since the index starts at 0, we add 1 to the index.
	 * 
	 * 3) requesting the 0th note just returns the 0th index, or the first note. 
	 */
	public int get(int index) {
//		log("getting index of:" + index + " from " + items.size() + " notes");
		if(index > 0) {
			return items.get(index - 1);
		} else if(index < 0) {
			return items.get(items.size() + index);
		} else if (index == 0) {
			return items.get(0);
		}
		return 911;
	}
	
	public void add(int item) {
		items.add(item);
	}
	
	public void addIfNotDuplicate(int item) {
		if(!items.contains(item)) {
			add(item);
		}
	}
	
	public void addAll(MusicIntCollection items) {
		this.items.addAll(items.getAll());
	}
	
	public List<Integer> getAll() {
		return items;
	}
	
	public Iterator<Integer> iterator() {
		return items.iterator();
	}
	
	public int size() {
		return items.size();
	}
	
	public int getLast() {
		return items.get(items.size() - 1);
	}
	
	public int getFirst() {
		return items.get(0);
	}
	
	
	public String toString() {
		return items.toString();
	}
	
	//randomizes the notes and returns them but does not randomize the actual object's notes
	public List<Integer> getRandomized() {
		
		List<Integer> reduceList = new ArrayList<>();
		List<Integer> randomizedSteps = new ArrayList<>();
		if(items.size() > 0) {
			Random random = new Random();
//		log("reduceList:" + reduceList);
			for(int i : items) {
				reduceList.add(i);
			}
//		log("reduceList:" + reduceList);
			do {
				int randomIndex = random.nextInt(reduceList.size());
				randomizedSteps.add(reduceList.get(randomIndex));
				reduceList.remove(randomIndex);
			} while(reduceList.size() > 0);
		}
		
		return randomizedSteps;
	}
	
	//TODO create getRandomized(int weight) where you weight the randomization to favor smaller intervals (creating a smoother melody)
	
	public boolean empty() {
		return items.size() == 0 ? true : false;
	}
	
	public boolean contains(int i) {
		return items.contains(i) ? true : false;
		
	}
	
	private void log(String msg) {
		System.out.println("MusicIntColl-Log:     " + msg);
	}
}
