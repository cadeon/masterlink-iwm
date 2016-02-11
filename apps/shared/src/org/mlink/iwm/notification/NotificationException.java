package org.mlink.iwm.notification;

/**
 * Created by Andrei Povodyrev
 * Date: Jun 15, 2006
 * General purpose exception for issues related to notification module
 */
public class NotificationException extends Exception{
    public NotificationException() {
        super();
    }

    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(Throwable cause) {
        super(cause);
    }

    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
