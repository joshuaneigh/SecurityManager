package util.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Properties;

/**
 * This class extends the java.util.Properties class to provide greater
 * accessibility to the standard Properties class. Prior to implementation of
 * this class, the user was required to manually convert the value to primitive
 * types and select non-primitive types (including BigInteger and BigDecimal).
 * This class will also allow the user to easily get several primitive arrays
 * from the XML file used.
 * 
 * @author Joshua Neighbarger | jneigh@uw.edu
 */
public class XMLProperties extends Properties {
	
	/** Automatically generated serial version. */
	private static final long serialVersionUID = -5993969900932246169L;

	/**
	 * Will allow setting of any object, rather than only Strings. While one
	 * would be able to implicitly call the object.toString() or
	 * String.valueOf(primitive), the inclusion of this method will make the use
	 * of the XMLProperties easier in implementation.
	 * 
	 * @param key the name of the entry to be modified.
	 * @param value the value to set.
	 */
	public void setProperty(final String key, final Object value) {
		super.setProperty(key, value.toString());
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the boolean value of the XML entry.
	 */
	public boolean getBoolean(final String key) {
		return Boolean.valueOf(getProperty(key));
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the byte value of the XML entry.
	 */
	public byte getByte(final String key) {
		return Byte.valueOf(getProperty(key));
	}

	/**
	 * @param key the name of the entry to get.
	 * @return the char value of the XML entry.
	 */
	public char getChar(final String key) {
		return Character.valueOf(getProperty(key).charAt(0));
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the short value of the XML entry.
	 */
	public short getShort(final String key) {
		return Short.valueOf(getProperty(key));
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the int value of the XML entry.
	 */
	public int getInt(final String key) {
		return Integer.valueOf(getProperty(key));
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the float value of the XML entry.
	 */
	public float getFloat(final String key) {
		return Float.valueOf(getProperty(key));
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the long value of the XML entry.
	 */
	public long getLong(final String key) {
		return Long.valueOf(getProperty(key));
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the double value of the XML entry.
	 */
	public double getDouble(final String key) {
		return Double.valueOf(getProperty(key));
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the String value of the XML entry.
	 */
	public String getString(final String key) {
		return getProperty(key);
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the BigDecimal value of the XML entry.
	 */
	public BigDecimal getBigDecimal(final String key) {
		return new BigDecimal(getProperty(key));
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @return the BigInteger value of the XML entry.
	 */
	public BigInteger getBigInteger(final String key) {
		return new BigInteger(getProperty(key));
	}
	
	/**
	 * Parses and returns an int[] from the given key. The array must contain
	 * only numbers, separated by commas; whitespace will be ignored.
	 * 
	 * @param key the name of the entry to get.
	 * @return the int[] value of the XML entry.
	 */
	public int[] getIntArray(final String key) {
		final String[] strs = getProperty(key).replaceAll("\\s", "").split(",");
		final int[] ints = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			ints[i] = Integer.parseInt(strs[i]);
		}
		return ints;
	}
	
	/**
	 * Parses and returns a double[] from the given key. The array must contain
	 * only numbers, separated by commas; whitespace will be ignored.
	 * 
	 * @param key the name of the entry to get.
	 * @return the double[] value of the XML entry.
	 */
	public double[] getDoubleArray(final String key) {
		final String[] strs = getProperty(key).replaceAll("\\s", "").split(",");
		final double[] doubles = new double[strs.length];
		for (int i = 0; i < strs.length; i++) {
			doubles[i] = Double.parseDouble(strs[i]);
		}
		return doubles;
	}
	
	/**
	 * @param key the name of the entry to get.
	 * @param type the type of Object to get.
	 * @return the Object value of the XML entry.
	 */
	public <T extends Serializable> T getObject(final String key, final Class<T> type) {
		try {
			final byte[] bytes = Base64.getDecoder().decode(getProperty(key).getBytes());
			final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			return type.cast(ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param object the Object to serialize.
	 * @return the String value of the Object.
	 */
	public static <T extends Serializable> String serialize(final T object) {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.close();
			return new String(Base64.getEncoder().encode(baos.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}