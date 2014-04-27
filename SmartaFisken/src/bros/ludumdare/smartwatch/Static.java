package bros.ludumdare.smartwatch;

import java.util.Random;

public class Static {

	public static final int[] ON_START_WAIT_TIME = {500, 1000, 1500, 2000, 2500, 3000};
	public static final String[] FISH_ARRAY = {"R.drawable.ic_fisharray_fish_1"};
	public static final String[] GOOD_HUNTING_MESSAGES = {"Vilken baddare!"};
	
	
	public static int randomInt(int min, int max) {
		Random random = new Random();
		int randomNum = random.nextInt((max - min) + 1) - min;
		return randomNum;
	}
}