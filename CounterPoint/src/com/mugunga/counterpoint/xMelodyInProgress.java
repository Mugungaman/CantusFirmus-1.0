package com.mugunga.counterpoint;
import java.util.ArrayList;
import java.util.List;

public class xMelodyInProgress extends xMelody {

	private boolean tritoneResolutionNeeded;
	private boolean triggerValidIndexArrayPruning;
	private boolean pruneIndexArraysForVoiceDisposition;
	private boolean pentultimateFound;
	private boolean finalNoteReady;
	private boolean skipFirstDownbeat;
	private boolean pentultimateWholeNote;
	//private boolean invokeDissonantPassingTone2sRules;
//	private boolean killMelody;
	
	//valid notes for the second to last note, do we need to add 5-1 to allow fifths? 
	private ArrayList<Integer> validPentultimates = new ArrayList<Integer>();
	
	public xMelodyInProgress() {
	}
	
	public xMelodyInProgress(xMelodyInProgress melody) {
		super(melody);
		this.tritoneResolutionNeeded = melody.tritoneResolutionNeeded;
		this.triggerValidIndexArrayPruning = melody.triggerValidIndexArrayPruning;
		this.pruneIndexArraysForVoiceDisposition = melody.pruneIndexArraysForVoiceDisposition;
		this.pentultimateFound = melody.pentultimateFound;
		
		this.finalNoteReady = melody.finalNoteReady;
		this.skipFirstDownbeat = melody.skipFirstDownbeat;
		this.pentultimateWholeNote = melody.pentultimateWholeNote;
//		this.killMelody = melody.killMelody;
//		log("melody clone constructor:" + finalNoteReady);
//		log("melody clone constructor:" + pentultimateFound);
		for(int i : melody.validPentultimates) {
			validPentultimates.add(i);
		}
		
	}
	
//	public xMelodyInProgress(SpeciesRules rules, Mode mode) {
//		super(rules, mode);
//		rules.setMelodyPentultimates(this);
//	}
	
	public void setTritoneResolutionNeeded() {
		tritoneResolutionNeeded = true;
	}
	
	public void setTritoneResolved() {
		if(tritoneResolutionNeeded) {
			log("RESOLVING TRITTTTONE!" + getAll());
			log("w/ parent: " + parentMelody.getAll());
			
		}
		tritoneResolutionNeeded = false;
	}
	
	public boolean isTritoneResolutionNeeded() {
		return tritoneResolutionNeeded;
	}
	
	private void log(String msg) {
		System.out.println("MelodyInProgLog:      " + msg + "     maxNote: " + rules.maxMeasures);
	}
	
	public void addNote(int newNote) {
		super.addNote(newNote);
		
		if(!upperVoice && !lowerVoice && hasParentMelody()) {
			int cfNote = parentMelody.getFirst();
			if(newNote > cfNote) {
//				log("setting upper voice to true in MIP:" + newNote + " vs cf note: " + cfNote);
				upperVoice = true;
				pruneIndexArraysForVoiceDisposition = true;
			} else if(newNote< cfNote) {
//				log("setting lower voice to true in MIP:" + newNote + " vs cf note: " + cfNote);
				lowerVoice = true;
				pruneIndexArraysForVoiceDisposition = true;
			}
		}
	}

	public boolean pruneIndexArraysForVoiceDisposition() {
		return pruneIndexArraysForVoiceDisposition;
	}

	public void indexArraysPrunedForVoiceDisposition() {
//		log("index arrays have been pruned!!");
		pruneIndexArraysForVoiceDisposition = false;
	}
	
	public boolean isPentultimateFound() {
		return pentultimateFound;
	}
	
	public void setPentultimateFound(boolean pentultimateFound) {
//		log("actually seting P found");
		this.pentultimateFound = pentultimateFound;
	}

	public void addValidPentultimate(int noteIndex) {
		validPentultimates.add(noteIndex);
	}

	public ArrayList<Integer> getValidPentultimates() {
		
		return validPentultimates;
	}

	public void setFinalNoteReady() {
		finalNoteReady = true;
	}
	
	public void setFinalNoteNotReady() {
		finalNoteReady = false;
	}

	public boolean finalNoteIsReady() {
		return finalNoteReady;
	}

	public int getParentNote(int i) {
		return parentMelody.get(i);
	}

	public int parentSize() {
		return parentMelody.size();
	}



}
