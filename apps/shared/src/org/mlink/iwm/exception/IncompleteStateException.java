package org.mlink.iwm.exception;

/**
 * Created by Andrei Povodyrev
 * Date: Jul 17, 2004
 */
public class IncompleteStateException
	extends IWMException
{
    public IncompleteStateException() {
        super(null,null);
    }

    public IncompleteStateException(String message) {
        super(message,null);
    }

    public IncompleteStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteStateException(Throwable cause) {
        super(null,cause);
    }
}
