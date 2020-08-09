package com.mugunga.utils;

import java.util.List;

import com.mugunga.counterpoint.FirstSpecies;

public class JChops {

	public static boolean compare(int[] intArray, List<Integer> intList) {
		if(intList.size() != intArray.length) {
			return false;
		}
		for(int i =0; i < intArray.length; i++) {
			if(intList.get(i) != intArray[i]) {
				return false;
			}
		}
		return true;
	}
	
	
}
