package bros.ludumdare.smartwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Static {

	//public static final int[] ON_START_WAIT_TIME = { 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000 };
	public static final int[] ON_START_WAIT_TIME = { 4000, 6000, 8000, 10000, 12000, 14000, 1800, 5000, 7000, 9000 };
	public static final int[] FISH_ARRAY = { R.drawable.ic_fisharray_fish_1, R.drawable.ic_fisharray_fish_2, R.drawable.ic_fisharray_fish_3, R.drawable.ic_fisharray_fish_4, R.drawable.ic_fisharray_fish_5, R.drawable.ic_fisharray_fish_6, R.drawable.ic_fisharray_fish_7, R.drawable.ic_fisharray_fish_8, R.drawable.ic_fisharray_fish_9, R.drawable.ic_fisharray_fish_10  };
	public static final String[] GOOD_HUNTING_MESSAGES = { "Vilken baddare!", "Mäktigt!", "Sicken lax!", "Fiskelycka!", "Kvällsmat!", "Storfiskarn!", "Den du!", "Mästerligt!", "Tur i oturen!", "Härligt!" };
	
	public static List <Integer> doneFish = new ArrayList<Integer>();
	
	public static int randomInt(int min, int max) {
		Random random = new Random();
		int randomNum = random.nextInt((max - min) + 1) - min;
		doneFish.add(randomNum);
		return randomNum;
	}
}