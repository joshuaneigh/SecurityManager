package test;

import static org.junit.Assert.*;

import org.junit.Test;

import util.security.EncryptedString;
import util.security.PasswordGenerator;
import util.security.PasswordPolicy;

public final class EncryptedStringTest {
	
	private static final String MESSAGE;
	private static final String PHRASE;
	private static final EncryptedString TEST;

	static {
		PasswordGenerator.setPolicy(PasswordPolicy.NO_SPECIAL_EIGHT_TO_TWELVE);
		
		MESSAGE = "Look! A Secret Message...";
		PHRASE = PasswordGenerator.generatePassword();
		TEST = new EncryptedString(MESSAGE, PHRASE);
	}
	
	@Test (expected = NullPointerException.class)
	public void testDecryptNull() {
		TEST.decrypt(null);
	}
	
	@Test
	public void testDecryptWrongPhrase() {
		assertNull(TEST.decrypt("!@#$%^*()"));
	}
	
	@Test
	public void testDecryptSuccess() {
		assertEquals(MESSAGE, TEST.decrypt(PHRASE));
	}
	
	@Test
	public void testHashGenerator() {
		assertEquals(EncryptedString.generateHash(PHRASE), EncryptedString.generateHash(PHRASE));
		assertEquals(EncryptedString.generateHash(MESSAGE), EncryptedString.generateHash(MESSAGE));
		assertNotEquals(EncryptedString.generateHash(MESSAGE), EncryptedString.generateHash(PHRASE));
	}
	
	@Test (expected = NullPointerException.class)
	public void testHashGeneratorNull() {
		EncryptedString.generateHash(null);
	}
	
}
