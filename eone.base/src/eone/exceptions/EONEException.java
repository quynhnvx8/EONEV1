
package eone.exceptions;

import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;


public class EONEException extends RuntimeException {
	
	private static final long serialVersionUID = 2340179640558569534L;

	public EONEException() {
		this(getMessageFromLogger());
	}
	

	public EONEException(String message) {
		super(message);
	}

	public EONEException(Throwable cause) {
		super(cause);
	}

	public EONEException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getLocalizedMessage() {
		String msg = super.getLocalizedMessage();
		msg = Msg.parseTranslation(getCtx(), msg);
		return msg;
	}
	
	protected Properties getCtx() {
		return Env.getCtx();
	}


	
	private static String getMessageFromLogger() {
		org.compiere.util.ValueNamePair err = CLogger.retrieveError();
		String msg = null;
		if (err != null)
			msg = err.getName();
		if (msg == null)
			msg = "UnknownError";
		return msg;
	}
}
