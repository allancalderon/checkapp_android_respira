package mobi.checkapp.epoc.chat.exceptions;

public class YaximXMPPException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public YaximXMPPException(String message) {
		super(message);
	}

	public YaximXMPPException(String message, Throwable cause) {
		super(message, cause);
	}
}
