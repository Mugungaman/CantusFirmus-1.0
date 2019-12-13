
public final class MathUtility {
	public static double standardDeviation(int[] numbers) {
		double mean = mean(numbers);
		double standard_deviation;
		double accumulator = 0;
		for(int i = 0; i < numbers.length; i++) {
			accumulator += Math.pow((numbers[i] - mean), 2);
		}
		return Math.sqrt(accumulator/numbers.length);
		
	}
	
	public static double mean(int[] numbers) {
		double sum = 0;
		for(int i = 0; i < numbers.length; i++) {
			sum += numbers[i];
			//System.out.println(sum);
		}
		return (sum/numbers.length);
		
	}
	
}
