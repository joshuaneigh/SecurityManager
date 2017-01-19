package util.security;

/**
 * This class sets the rules for password generation. All {@link PasswordPolicy}
 * objects are immutable in nature, and must be re-instantiated in every
 * instance.
 *
 * @author Joshua Neighbarger | jneigh@uw.edu
 * @version 16 Nov 2016
 */
public class PasswordPolicy {

	public static final PasswordPolicy NO_SPECIAL_EIGHT_TO_TWELVE;
	public static final PasswordPolicy ONLY_NUMS_SIX_TO_TEN;
	
	private final int upperAlpha;
	private final int lowerAlpha;
	private final int nums;
	private final int specialChars;
	private final int maxChars;
	private final int minChars;

	static {
		NO_SPECIAL_EIGHT_TO_TWELVE = new PasswordPolicy(2, 2, 2, -1, 12, 8);
		ONLY_NUMS_SIX_TO_TEN = new PasswordPolicy(-1, -1, 1, -1, 10, 6);
	}
	
	public PasswordPolicy(int upperAlpha, int lowerAlpha, int nums, int specialChars, int maxChars, int minChars) {
		this.upperAlpha = upperAlpha;
		this.lowerAlpha = lowerAlpha;
		this.nums = nums;
		this.specialChars = specialChars;
		this.maxChars = maxChars;
		this.minChars = minChars;
	}

	public int getUpperAlpha() {
		return upperAlpha;
	}

	public int getLowerAlpha() {
		return lowerAlpha;
	}

	public int getNums() {
		return nums;
	}

	public int getSpecialChars() {
		return specialChars;
	}

	public int getMaxChars() {
		return maxChars;
	}

	public int getMinChars() {
		return minChars;
	}

}
