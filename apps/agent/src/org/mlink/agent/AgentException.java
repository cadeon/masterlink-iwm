package org.mlink.agent;

public class AgentException extends RuntimeException {


	public AgentException() {
		super();
	}

	public AgentException(String message) {
		super(message);
	}

	public AgentException(Throwable cause) {
		super(cause);
	}

	public AgentException(String message, Throwable cause) {
		super(message, cause);
	}

}
