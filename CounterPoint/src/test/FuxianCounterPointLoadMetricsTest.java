package test;

import java.io.IOException;
import java.util.ArrayList;

import com.mugunga.counterpoint.CantusFirmus;
import com.mugunga.counterpoint.CounterPointRunner;
import com.mugunga.counterpoint.CounterPointStats;
import com.mugunga.counterpoint.Mode;
import com.mugunga.counterpoint.NoteLength;
import com.mugunga.counterpoint.SpeciesBuilder;
import com.mugunga.counterpoint.SpeciesSystem;
import com.mugunga.counterpoint.SpeciesType;
import com.mugunga.counterpoint.TestMelody;

/**
 * This Test class is for large batch monitoring of algorithm. When running many Cantus/Firmus, metrics like 
 * Standard Deviation, # of empty Cantus Firmus, # of First Species per Cantus Firmus, etc, should
 * remain steady. A large swing means something drastic happened to the algorithm.
 * 
 * @author laurencemarrin
 *
 */
public class FuxianCounterPointLoadMetricsTest {
	
	private Mode mode;
	private int targetCantusFirmusCount;
	private CounterPointRunner cpr;
	
	public FuxianCounterPointLoadMetricsTest() {
		mode = Mode.IONIAN;
		targetCantusFirmusCount = 250;
	}
	
	public FuxianCounterPointLoadMetricsTest(Mode mode) {
		this.mode = mode;
		targetCantusFirmusCount = 250;
	}
	
	public FuxianCounterPointLoadMetricsTest(Mode mode, int cantusFirmusCount) {
		this.mode = mode;
		targetCantusFirmusCount = cantusFirmusCount;
	}
	
	protected void run() {
		cpr = new CounterPointRunner(SpeciesSystem.FUXIAN_COUNTERPOINT);
		cpr.setMode(mode);
		cpr.setTargetBaseSpeciesCount(targetCantusFirmusCount);
		cpr.generateMusic();
	}
	

//	public void setTestCantusFirmus(int[] testCFMelody) {
//		this.testCFMelody = new TestMelody(testCFMelody, NoteLength.WHOLE_NOTE);
//		
//	}
//
//	public void setTestFirstSpecies(int[] test1SMelody) {
//		this.test1SMelody = new TestMelody(test1SMelody, NoteLength.WHOLE_NOTE);
//		this.test1S = true;
//		
//	}

	public void setMode(Mode mode) {
		this.mode = mode;
		
	}

	public int getBaseMelodyCount() {
		
		return cpr.getBaseSpeciesCount();
	}

	public CounterPointStats getStats() {
		return cpr.getStats();
	}

//	public boolean validCantusFirmus() {
//		return cantusFirmusCount == 1 ? true : false;
//	}
//	
//	public boolean validFirstSpecies() {
//		return firstSpeciesCount == 1 ? true : false;
//	}
}
