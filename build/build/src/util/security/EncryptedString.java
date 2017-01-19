package util.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptedString {

	private static final String ENCODING = "UTF-8";
	private static final String ALGORITHM = "AES";
	private static final String HASH = "HmacSHA384";

	private final byte[] encryptedString;

	public EncryptedString(final String string, final String phrase) {
		this.encryptedString = encrypt(Objects.requireNonNull(string), generateHash(Objects.requireNonNull(phrase)));
	}

	public String decrypt(final String phrase) {
		return decrypt(encryptedString, generateHash(Objects.requireNonNull(phrase)));
	}

	public static String generateHash(final String phrase) {
		try {
			final Mac sha256_HMAC = Mac.getInstance(HASH);
			final SecretKeySpec secret_key = new SecretKeySpec(Objects.requireNonNull(phrase).getBytes(ENCODING), HASH);
			sha256_HMAC.init(secret_key);
			return new String(Base64.getEncoder().encode(sha256_HMAC.doFinal(phrase.getBytes(ENCODING))), ENCODING)
					.substring(0, 32);
		} catch (InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException
				| IllegalStateException e) {
			e.printStackTrace();
			return null;
		}
	}

	private byte[] encrypt(final String string, final String phrase) {
		try {
			final Key secretKey = new SecretKeySpec(Objects.requireNonNull(phrase).getBytes(ENCODING), ALGORITHM);
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(string.getBytes(ENCODING));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String decrypt(final byte[] encoded, final String phrase) {
		try {
			final Key secretKey = new SecretKeySpec(Objects.requireNonNull(phrase).getBytes(ENCODING), ALGORITHM);
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(encoded));
		} catch (final InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (final BadPaddingException e) {
			return null;
		}
	}

}
