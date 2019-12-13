import java.util.ArrayList;
/**
 * 
 * @author laurencemarrin
 *
 *
 *  Second Species Rules:
 *  
 *  1) Two notes per each cantus firmus note
 *
 *  2) Enforce more stepwise motion: prefer a % to leap ratio in the SpeciesTypes? Or leap the leaps the same and have
 *     removal of step rules.....
 *  
 *  3) Try to leap from a strong beat to a weak to remove attention
 *  
 *  4) 1-2 Secondary climaxes? Section off parts of the melody and make sure each X amount of notes has a climax. 
 *  
 *  5) No unisons X
 *  
 *  6) First note must follow normal first notes but can be either the first half or second half. 
 *  
 *  7) Pentultimate note can either be one bar or the half bar (second half of the pentultimate bar)
 *  
 *  8) Strong beats are always consonant with same rules as 1st species
 *  
 *  9) Movement away treated like harmonizing with the Cantus Firmus downbeat. 
 *  So if it creates a perfect 5th it cannot	move to a perfect fifth. 
 *  
 *  10) Downbeat to downbeat must follow normal 1s rules.
 *  
 *  11) don't outline a 7th on consecutive downbeats (inhereited from 1S rules)
 *  
 *  12) Dissonances only occur on the passing tone, can't be leapt to, from, or change directions (see, 'passing tone') XY
 *  
 *  13) Consonant passing tone XY
 *  
 *  14) substitution: on a pass tone, you go from a 3rd to a 3rd but leap a 4th and then step back to the down beat. 
 *  All beats must be consonant.  XY
 *  
 *  15) skipped passing down outlines a 4th from D to D and must be consonant w/ the CF (always the second step or can be either? X
 *  
 *  16) Split a 5th/6th leap by dividing into a 3rd+3rd or 3rd+4th or 4th+3rd.     X
 *  	a) all three 2S notes must be consonant to cantus firmus
 *  	b) both intervals must be consonant in the 2s (can't split a 5th by a 2nd and a 4th, etc).   
 *  
 *  17) Change of register: 5th/6th/Octave leap that rebounds 1 step in the opposite direction to the next downbeat. 
 *  All consonant notes only:
 *  
 *  18) to move a step: delay of melodic progression, jump a 3rd and fall back.  xY
 *  
 *  19) consonant neighbor tone for a unison xx
 *  
 *  20) If the harmonic interval on the downbeat (beat 1) of a measure is a perfect unison, fifth, or octave, 
 *  the prior measure may not contain that same interval.
 *  
 *  21)
 *  
 */
public class SecondSpecies extends NoteMelody {
	
	boolean startOnRest = false;
	
	
	public SecondSpecies(NoteMelodyInProgress noteMelodyInProgress) {
		super(noteMelodyInProgress);
		//addAll(melody);
	
	}

}

/**
 *   1) STart on rest or beginning
 *   2) 
 * 
 * 
 * 
 * 
 * */