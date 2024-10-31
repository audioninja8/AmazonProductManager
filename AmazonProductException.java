package amazonproducts;

public class AmazonProductException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AmazonProductException(String errorMessage) {
		super();
		System.err.println("AmazonProductException: " + errorMessage);

	}
}
