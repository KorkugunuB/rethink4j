package org.nutz.rethink4j;

public class RethinkRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -5467861566793646644L;

	public RethinkRuntimeException() {
		super();
	}

	public RethinkRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RethinkRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RethinkRuntimeException(String message) {
		super(message);
	}

	public RethinkRuntimeException(Throwable cause) {
		super(cause);
	}

}
