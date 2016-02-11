package org.mlink.iwm.base;

import java.io.PrintWriter;

/**
 * Created by Andrei Povodyrev
 * Date: Jun 1, 2004
 */
public class WebException extends Exception{
    public WebException() {
    }

    public WebException(String message) {
        super(message);
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebException(Throwable cause) {
        super(cause);
        cause.printStackTrace();
    }

    public void printStackTrace() {
        if(getCause()!=null){
            getCause().printStackTrace();
        }else{
            super.printStackTrace();
        }
    }

    public void printStackTrace(PrintWriter s) {
        if(getCause()!=null){
            getCause().printStackTrace(s);
        }else{
            super.printStackTrace(s);
        }
    }
}
