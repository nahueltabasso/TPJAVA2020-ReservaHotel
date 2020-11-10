package exceptions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public LoginException() {
		super("Error en el login");
	}
	
	public LoginException(String message, Level levelError) {
		this(null, message, levelError);
	}
	
	public LoginException(Throwable cause) {
		super(cause);
	}
	
	private LoginException(String message, Throwable e) {
		super(message, e);
	}
	
	public LoginException(Throwable e, String message, Level levelError) {
		this(message, e);
		Logger logger =  LogManager.getLogger(getClass());
		logger.log(levelError, message);
	}
}
