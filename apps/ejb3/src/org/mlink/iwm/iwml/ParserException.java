package org.mlink.iwm.iwml;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 6, 2007
 */
public class ParserException extends Exception{
    public ParserException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public ParserException(Throwable throwable) {
        super(throwable);
    }

    public ParserException(String string) {
        super(string);
    }
}
