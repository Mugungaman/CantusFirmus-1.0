package test;

import com.mugunga.counterpoint.CounterPointRunner;
import com.mugunga.counterpoint.Mode;
import com.mugunga.counterpoint.NoteLength;
import com.mugunga.counterpoint.SpeciesSystem;
import com.mugunga.counterpoint.TestMelody;

public class FuxianCounterPointSingleMelodyTest {
	
	private TestMelody testBaseMelody;
	private TestMelody testFirstSpeciesMelody;
	private boolean test1S = false;
	private CounterPointRunner cpr;
	
	public FuxianCounterPointSingleMelodyTest() {
		cpr = new CounterPointRunner(SpeciesSystem.FUXIAN_COUNTERPOINT);
	}

	public void testMelody() {
		
		cpr.setTestBaseMelody(testBaseMelody);
		if(test1S) {
			cpr.setTestFirstSpeciesMelody(testFirstSpeciesMelody);
		}
		cpr.generateMusic();
	}	
	

	public void setTestCantusFirmus(int[] testCFMelody) {
		cpr.setTestBaseMelody(new TestMelody(testCFMelody, NoteLength.WHOLE_NOTE));
//		this.testBaseMelody = ;
		
	}

	public void setTestFirstSpecies(int[] test1SMelody) {
		cpr.setTestFirstSpeciesMelody(new TestMelody(test1SMelody, NoteLength.WHOLE_NOTE));
		//this.testFirstSpeciesMelody = ;
		//this.test1S = true;
		
	}

	public void setMode(Mode mode) {
		cpr.setMode(mode);
//		this.mode = mode;
		
	}

	public boolean validCantusFirmus() {
		return cpr.getBaseSpeciesCount() == 1 ? true : false;
	}
	
	public boolean validFirstSpecies() {
		return cpr.getFirstSpeciesCount() == 1 ? true : false;
	}
}
