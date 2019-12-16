package com.mugunga.counterpoint;

public enum Interval {
	UNISON(0, 0),
	MINOR_2ND(1, 1),
	MAJOR_2ND(2, 1),
	MINOR_3RD(3, 2),
	MAJOR_3RD(4, 2),
	PERFECT_4TH(5, 3),
	TRITONE(6, 4),
	PERFECT_5FTH(7, 4),
	AUGMENTED_5FTH(8, 4),
	MINOR_SIX(8, 5),
	MAJOR_SIX(9, 5),
	MINOR_7TH(10, 6),
	MAJOR_7TH(11, 6),
	NOTHING(911, 911);
	
	
	public final int steps;
	public final int modeIndex;
	
	Interval(int steps, int modeIndex) {
		this.steps = steps;
		this.modeIndex = modeIndex;
	}
	
	public static Interval getInterval(int steps) {
		for(Interval i : Interval.values()) {
			if(steps == i.steps) {
				return i;
			}
		}
		log("INVALID INTERVAL REQUESTED");
		return NOTHING;
	}
	
	private static void log(String msg) {
		System.out.println("Interval Log     :" + msg);
	}
	
}
