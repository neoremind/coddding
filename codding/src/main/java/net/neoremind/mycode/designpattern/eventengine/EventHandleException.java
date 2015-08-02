package net.neoremind.mycode.designpattern.eventengine;

public class EventHandleException extends Exception {

    public EventHandleException() {
    }

    public EventHandleException(String message) {
        super(message);
    }

    public EventHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventHandleException(Throwable cause) {
        super(cause);
    }

    public EventHandleException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
