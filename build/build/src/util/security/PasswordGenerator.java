package util.security;

import java.security.SecureRandom;
import java.util.Objects;

public class PasswordGenerator {

	private static PasswordPolicy POLICY;

	private PasswordGenerator() {
	}

	public PasswordPolicy getPolicy() {
		return POLICY;
	}

	public static void setPolicy(final PasswordPolicy policy) {
		Objects.requireNonNull(policy);
		POLICY = policy;
	}

	private static void shuffleArray(char[] array) {
		final SecureRandom rand = new SecureRandom();
		for (int i = array.length; i > 1; i--) {
			int j = rand.nextInt(i);
			char temp = array[j];
			array[j] = array[i - 1];
			array[i - 1] = temp;
		}
	}

	public static String generatePassword() {
		if (POLICY == null) {
			throw new IllegalStateException("The PasswordPolicy was never set.");
		}
		return generatePassword(POLICY.getMinChars(), POLICY.getMaxChars(), POLICY.getUpperAlpha(),
				POLICY.getLowerAlpha(), POLICY.getNums(), POLICY.getSpecialChars());
	}

	public static String generatePassword(final PasswordPolicy policy) {
		Objects.requireNonNull(policy);
		return generatePassword(policy.getMinChars(), policy.getMaxChars(), policy.getUpperAlpha(),
				policy.getLowerAlpha(), policy.getNums(), policy.getSpecialChars());
	}

	private static String generatePassword(int minLen, int maxLen, int minNumUpper, int minNumLower, int minNumDigits,
			int minNumSpecial) {
		final SecureRandom rand = new SecureRandom();
		final int length = minLen + rand.nextInt(maxLen - minLen + 1);
		final char[] pass = new char[length];
		int extra = length - (minNumUpper + minNumLower + minNumDigits + minNumSpecial);

		while (extra > 0) {
			int temp;
			if (minNumUpper != -1) {
				temp = rand.nextInt(extra + 1);
				minNumUpper += temp;
				extra -= temp;
			}
			if (minNumLower != -1) {
				temp = rand.nextInt(extra + 1);
				minNumLower += temp;
				extra -= temp;
			}
			if (minNumDigits != -1) {
				temp = rand.nextInt(extra + 1);
				minNumDigits += temp;
				extra -= temp;
			}
			if (minNumSpecial != -1) {
				temp = rand.nextInt(extra + 1);
				minNumSpecial += temp;
				extra -= temp;
			}
		}

		for (int i = 0; i < length; i++) {
			if (minNumUpper > 0) {
				pass[i] = (char) ('A' + rand.nextInt('Z' - 'A' + 1));
				minNumUpper--;
			} else if (minNumLower > 0) {
				pass[i] = (char) ('a' + rand.nextInt('z' - 'a' + 1));
				minNumLower--;
			} else if (minNumDigits > 0) {
				pass[i] = (char) ('0' + rand.nextInt('9' - '0' + 1));
				minNumDigits--;
			} else if (minNumSpecial > 0) {
				pass[i] = (char) ('!' + rand.nextInt('!' - '/' + 1));
				minNumSpecial--;
			}
		}

		shuffleArray(pass);

		return String.valueOf(pass);
	}

}
