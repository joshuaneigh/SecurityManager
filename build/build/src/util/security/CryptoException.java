package util.security;

public class CryptoException extends Exception {
	 
	private static final long serialVersionUID = -229594106087154685L;

	public CryptoException() {
    }
 
    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}