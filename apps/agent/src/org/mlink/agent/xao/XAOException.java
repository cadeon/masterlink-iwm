package org.mlink.agent.xao;

public class XAOException extends RuntimeException {

	public XAOException() {
		super();
	}

	public XAOException(String message) {
		super(message);
	}

	public XAOException(Throwable cause) {
		super(cause);
	}

	public XAOException(String message, Throwable cause) {
		super(message, cause);
	}
}
