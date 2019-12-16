package com.mugunga.counterpoint;
import java.util.ArrayList;

public class IntervalIndexCollection extends MusicIntCollection  {
	//ArrayList<Integer> intervals = new ArrayList<Integer>();
	
	public IntervalIndexCollection (ArrayList<Integer> intervals) {
		//addAll(intervals)
		for(int i : intervals) {
			this.items.add(i);
		}
	}

	public IntervalIndexCollection(){}
	
	public int leapTally() {
		int leapTally = 0;
		for(int i: items) {
			if(Math.abs(i) > 1) {
				leapTally++;
			}
		}
		return leapTally;
	}
}
