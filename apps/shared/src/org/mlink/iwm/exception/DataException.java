package org.mlink.iwm.exception;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class DataException extends IWMException{

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataException(Throwable cause) {
        super(cause);
    }
}
