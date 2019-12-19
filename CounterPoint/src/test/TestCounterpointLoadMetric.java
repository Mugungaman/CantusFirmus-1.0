package test;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mugunga.counterpoint.CounterPointStats;
import com.mugunga.counterpoint.Mode;


class TestCounterpointLoadMetric {
	
	private FuxianCounterPointSingleMelodyTest cpt;
	private CounterPointStats stats;
	
	/*TODO
	 * 
	 * Create assertions for the following:
	 * 
	 * Standard Deviation of CFs
	 * Mean 1S per CFs
	 * % of empty CFs
	 * Max 1S per CFs
	 * 
	 * 
	 */
	
	@Test
	void phyrgianLoadTest() {
		int baseMelodyCount = 1000;
		FuxianCounterPointLoadMetricsTest cpt = new FuxianCounterPointLoadMetricsTest(Mode.PHYRGIAN, baseMelodyCount);
		cpt.run();
		stats = cpt.getStats();
		
		//Ensure we are creating exactly as many base Melodies as we think we are
		assertEquals(stats.baseMelodyCount(), baseMelodyCount);
		
		//Ensure the # of firstSpecies create per each Base Species is in a proper range
		//TODO store these min/max type variables in a metrics class or something and use them
		assertTrue(stats.firstSpeciesperBaseSpecies() > 13 && stats.firstSpeciesperBaseSpecies() < 35);
		assertTrue(stats.baseSpeciesSuccessRate() > .0045 && stats.baseSpeciesSuccessRate() < .01 );
		
	}
	
	@Test
	void IonianLoadTest() {
		int baseMelodyCount = 100;
		FuxianCounterPointLoadMetricsTest cpt = new FuxianCounterPointLoadMetricsTest(Mode.IONIAN, baseMelodyCount);
		cpt.run();
		stats = cpt.getStats();
		
		//Ensure we are creating exactly as many base Melodies as we think we are
		assertEquals(stats.baseMelodyCount(), baseMelodyCount);
		
		//Ensure the # of firstSpecies create per each Base Species is in a proper range
		//TODO store these min/max type variables in a metrics class or something and use them
		assertTrue(stats.firstSpeciesperBaseSpecies() > 25 && stats.firstSpeciesperBaseSpecies() < 80);
		assertTrue(stats.baseSpeciesSuccessRate() > .0045 && stats.baseSpeciesSuccessRate() < .011 );
		
	}
	
	
	
	

}
