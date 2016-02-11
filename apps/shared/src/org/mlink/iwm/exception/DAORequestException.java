package org.mlink.iwm.exception;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class DAORequestException extends IWMException{

    public DAORequestException(String message) {
        super(message);
    }

    public DAORequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAORequestException(Throwable cause) {
        super(cause);
    }
}
