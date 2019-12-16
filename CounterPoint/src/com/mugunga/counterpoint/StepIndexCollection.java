package com.mugunga.counterpoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class StepIndexCollection extends MusicIntCollection {
	//ArrayList<Integer> steps = new ArrayList<Integer>();
	
	
	public StepIndexCollection (ArrayList<Integer> steps) {
		for(int i : steps) {
			this.items.add(i);
		}
	}

	public StepIndexCollection() {
	
	}
	
	public int getLastStepInterval() {
		return (getLast() - getPentultimateStep());
	}

	private int getPentultimateStep() {
		return items.get(items.size() - 2);
	}
	
	public int sum() {
		int accum = 0;
		for(int i: this) {
			accum += i;
		}
		return accum;
	}

	public void appendInterval(Interval interval) {
		add(getLast() + interval.steps);
//		log("" + items);
		
	}
	private void log(String msg) {
		System.out.println("StepIndexColl-Log   " + msg);
	}

}
