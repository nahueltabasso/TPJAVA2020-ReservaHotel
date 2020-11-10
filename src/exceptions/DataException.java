package exceptions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DataException() {
		super("Error en la base de datos");
	}
	
	public DataException(String message, Level levelError) {
		this(null, message, levelError);
	}
	
	public DataException(Throwable cause) {
		super(cause);
	}
	
	private DataException(Throwable e, String message) {
		super(message, e);
	}
	
	public DataException(Throwable e, String message, Level levelError) {
		this(e, message);
		Logger logger = LogManager.getLogger(getClass());
		logger.log(levelError, message);
	}
	
}
