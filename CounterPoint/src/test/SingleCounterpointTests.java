package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.jupiter.api.Test;
import com.mugunga.counterpoint.*;


class SingleCounterpointTests {
	
	FuxianCounterPointSingleMelodyTest cpt;
	
	//TODO should not have 3 steps of parallel motion
	@Test
	void parallelMotionViolationTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, -2, 0, 1, 2, 4, 3, 2, 1, 2, 3, 1, 0}; //Andrew's cantus firmus, gets melodic minor
		cpt.setTestCantusFirmus(testCFMelody);
		int[] test1SMelody  =  {4, 3, 2, 3, 7, 6, 8, 7, 3, 4, 5, 6, 7};  //also need to raise leading tone in final bar.
		cpt.setTestFirstSpecies(test1SMelody);
		cpt.setMode(Mode.PHYRGIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), true);
		assertEquals(cpt.validFirstSpecies(), false);
	}
	
	//TODO Melody shouldn't jump down a third and right back up to the tonic
	@Test
	void repeatThirdPatternTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, -2, 0, 1, -2, -3, -4, -5, 2, 1, 0}; 
		cpt.setTestCantusFirmus(testCFMelody);
		cpt.setMode(Mode.PHYRGIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), false);
	}
	
	//TODO This melody needs to fail because the pentultimate note is not the leading tone
	@Test
	void phyrgianIllegalEndWithLeapTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, 3, 5, 4, 3, 1, 0, -1, -2, 0}; 
		cpt.setTestCantusFirmus(testCFMelody);
		cpt.setMode(Mode.PHYRGIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), false);
	}
	
	@Test
	void andrewStevensonsAeolianMelodicTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, 2, 1, 3, 2, 4, 5, 4, 3, 1, 0 }; //Andrew's cantus firmus, gets melodic minor
		cpt.setTestCantusFirmus(testCFMelody);
		int[] test1SMelody  =  {0, 4, 6, 5, 6, 9, 7, 6, 5, 6, 7};  //also need to raise leading tone in final bar.
		cpt.setTestFirstSpecies(test1SMelody);
		cpt.setMode(Mode.AEOLIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), true);
		assertEquals(cpt.validFirstSpecies(), true);
	}
	
	@Test
	void ionianClimaxBugTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, 2, 1, 3, 2, 4, 5, 4, 3, 2, 1, 0 }; 
		cpt.setTestCantusFirmus(testCFMelody);
		int[] test1SMelody  =  {7, 6, 8, 7, 9, 8, 7, 6, 5, 6, 6,  7,  };  
		cpt.setTestFirstSpecies(test1SMelody);
		cpt.setMode(Mode.IONIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), true);
		assertEquals(cpt.validFirstSpecies(), true);
	}
	
	@Test
	void leapCheckTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, -2, -5, -4, 0, -1, 2, 0, -1, 1, 0}; 
		cpt.setTestCantusFirmus(testCFMelody);
		cpt.setMode(Mode.IONIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), true);
	}
	
	@Test
	void lydianVanillaTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, -2, 1, 2, -1, 0, 2, 1, 0}; 
		cpt.setTestCantusFirmus(testCFMelody);
		cpt.setMode(Mode.LYDIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), true);
	}
	
	/*
	 * Need to add a fail code to the Melody Test so we can verify that it failed for the reason we thought it would
	 */
	@Test
	void aeolianVoiceCrossingTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, 5, 4, 1,  0, 3,  4,  5,  -2, -1, 0, 1, 0}; 
		cpt.setTestCantusFirmus(testCFMelody);
		int[] test1SMelody  =  {4, 7, 8, 10, 9, 12, 11, 10, 7, 6, 5, 6, 7 };  
		cpt.setTestFirstSpecies(test1SMelody);
		cpt.setMode(Mode.AEOLIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), true);
		assertEquals(cpt.validFirstSpecies(), false);
		//TODO assertEquals(cpy.failCode,FailCode.ILLEGAL_VOICE_CROSS), etc
	}
	
	@Test
	void weirdAeolianTest() {
		FuxianCounterPointSingleMelodyTest cpt = new FuxianCounterPointSingleMelodyTest();
		
		int[] testCFMelody =   {0, -2, 0, 1, 4, 3, 2, 1, 2, 3, 1, 0}; 
		cpt.setTestCantusFirmus(testCFMelody);
		int[] test1SMelody  =  {4, 5, 5, 10, 9, 8, 11, 10, 9, 5, 6, 7 };  
		cpt.setTestFirstSpecies(test1SMelody);
		cpt.setMode(Mode.AEOLIAN);
		cpt.testCantusFirmus();
		assertEquals(cpt.validCantusFirmus(), true);
		assertEquals(cpt.validFirstSpecies(), false);
		//TODO assertEquals(cpy.failCode,FailCode.ILLEGAL_VOICE_CROSS), etc
	}
	
	

}
