package com.mugunga.counterpoint;

public class Note {
	
	private int keyIndex;
	
	private double noteLength;
	
	public Note(int key, double length) {
		this.keyIndex = key;
		this.noteLength = length;
	}
	
	public Note(int index, NoteLength noteLength) {
		this.keyIndex = index;
		this.noteLength = noteLength.noteLength;
	}
	
	public int index() {
		return keyIndex;
	}
	
	public double noteLength() {
		return noteLength;
	}
}
