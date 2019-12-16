package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.jupiter.api.Test;
import com.mugunga.counterpoint.*;


class CounterpointLoadMetricTests {
	
	FuxianCounterPointSingleMelodyTest cpt;
	
	//TODO should not have 3 steps of parallel motion
	@Test
	void parallelMotionViolationTest() {
		FuxianCounterPointLoadMetricsTest cpt = new FuxianCounterPointLoadMetricsTest(Mode.PHYRGIAN, 200);
		cpt.testCantusFirmus();
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
	}
	
	
	
	

}
