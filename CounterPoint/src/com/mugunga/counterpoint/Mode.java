package com.mugunga.counterpoint;

public enum Mode {
	//TODO: Plagal modes? 
	IONIAN(new int[] {0, 2, 4, 5, 7, 9, 11, 12}, "IONIAN"), //-> tested
	DORIAN(new int[] {0, 2, 3, 5, 7, 9, 10, 12},"DORIAN" ),
	PHYRGIAN(new int[] {0, 1, 3, 5, 7, 8, 10, 12},"PHRYGIAN" ),
	LYDIAN(new int[] {0, 2, 4, 6, 7, 9, 11, 12 },"LYDIAN"),
	MIXOLYDIAN(new int[] {0, 2, 4, 5, 7, 9, 10, 12 },"MIXOLYDIAN"),
	AEOLIAN(new int[] {0, 2, 3, 5, 7, 8, 10, 12 },"AEOLIAN"),
	LOCRIAN(new int[] {0, 1, 3, 5, 6, 8, 10, 12 },"LOCRIAN");
	
	
	final int[] notes;
	final public String modeName;
	
	Mode(int[] notes, String modeName) {
		this.notes = notes;
		this.modeName = modeName;
//		System.out.println("Mode construcotr, notes are: " + notes.toString() + " #notes" + notes.length);
//		for(int i : notes) {
//			System.out.println("notes[i]" + i);
//		}
		
	}

}
