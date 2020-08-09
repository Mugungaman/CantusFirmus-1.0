package com.mugunga.musicmodels;


public enum Key {
	C("C", 0),
	Cs("C#", 1),
	Db("Db", 1),
	D("D", 2),
	Ds("D#", 3),
	Eb("Eb", 3),
	E("E", 4),
	F("F", 5),
	Fs("F#", 6),
	Gb("Gb", 6),
	G("G", 7),
	Gs("G#", 8),
	Ab("Ab", 8),
	A("A", 9),
	As("A#", 10),
	Bb("A#", 10),
	B("A#", 11),
	X("X", 99);
	
	final String name;
	final int relativeIndex;
	
	Key(String name, int relativeIndex) {
		this.name= name;
		this.relativeIndex = relativeIndex;
	}
}
