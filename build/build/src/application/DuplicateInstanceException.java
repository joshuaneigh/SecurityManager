package application;

public class DuplicateInstanceException extends RuntimeException {

	/** Default, generated serial version ID. */
	private static final long serialVersionUID = -681957268072677746L;
	
	public DuplicateInstanceException() {
		super();
		System.exit(0);
	}

	public DuplicateInstanceException(final String message) {
		super(message);
		System.exit(0);
	}
	
}
