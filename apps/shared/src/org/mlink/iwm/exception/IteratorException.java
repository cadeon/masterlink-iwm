package org.mlink.iwm.exception;


public class IteratorException extends IWMException {
    public IteratorException() {
        super(null,null);

    }
     public IteratorException(String message) {
        super(message,null);

    }
     public IteratorException(String message, Throwable cause) {
        super(message, cause);

    }
    public IteratorException(Throwable cause) {
        super("Iterator Exception", cause);
    }
}
