package net.sitina.bp.api;

import org.apache.log4j.Logger;

public class BatchProcessorException extends RuntimeException {

	private static final long serialVersionUID = -8859035247621979717L;
	
	private static Logger log = Logger.getLogger(BatchProcessorException.class);

	public BatchProcessorException() {
	}

	public BatchProcessorException(String message) {
		super(message);
	}

	public BatchProcessorException(Throwable cause) {
		super(cause);
	}

	public BatchProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public BatchProcessorException(Class<? extends Module> clazz, String item, Throwable e) {
		super(item, e);
		log.error(clazz.getName() + ";'" + item + "';" + e.getMessage());
	}

}
