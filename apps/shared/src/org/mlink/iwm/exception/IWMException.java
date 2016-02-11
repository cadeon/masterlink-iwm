package org.mlink.iwm.exception;

public class IWMException extends RuntimeException {
    public IWMException(Throwable cause) {
        super(cause);
    }

    public IWMException(String message, Throwable cause) {
        super(message,cause);
    }

    public IWMException(String message) {
        super(message);
    }

}
