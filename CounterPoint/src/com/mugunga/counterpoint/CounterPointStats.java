package com.mugunga.counterpoint;

public class CounterPointStats {
	
	private long startTime;
	private long endTime;
	private int totalBaseMelodies;
	private int baseFailCount;
	private int totalFirstSpeciesMelodies;
	
	public CounterPointStats() {
		totalBaseMelodies = 0;
		totalFirstSpeciesMelodies = 0;
	}

	public void setStartTime(long currentTimeMillis) {
		this.startTime = currentTimeMillis;
	}
	
	public void setEndTime(long currentTimeMillis) {
		this.endTime = currentTimeMillis;
	}
	
	public double firstSpeciesperBaseSpecies() {
		return (double)totalFirstSpeciesMelodies/(double)totalBaseMelodies;
	}
	
	public int baseMelodyCount() {
		return totalBaseMelodies;
	}
	
	public int getFirstSpeciesCount() {
		return totalFirstSpeciesMelodies;
	}
	
	public void tallyFirstSpecies(int firstSpecies) {
		totalFirstSpeciesMelodies += firstSpecies; 
	}
	
	public void setBaseMeldies(int baseMelodies) {
		totalBaseMelodies += baseMelodies; 
	}

	public void logStats() {
		long totalTime = endTime-startTime;
		log("totalTime:" + totalTime);
		double CF1s = (double)totalFirstSpeciesMelodies/(double)totalBaseMelodies;
		log("1S per CF" + CF1s);
		log(" Success Total: " + totalBaseMelodies);
		log(" Failtotal" + baseFailCount);
		log("Success Rate " + (double)totalBaseMelodies/(double)baseFailCount);
	}
	
	private static void log(String msg) {
		System.out.println("Stats Log:           " + msg);
	}

	public void setBaseFailCount(int baseFailCount) {
		this.baseFailCount = baseFailCount;
	}

	public double baseSpeciesSuccessRate() {
		return (double)totalBaseMelodies/(double)baseFailCount;
	}

}
