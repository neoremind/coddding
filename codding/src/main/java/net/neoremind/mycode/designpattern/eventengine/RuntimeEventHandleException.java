package net.neoremind.mycode.designpattern.eventengine;

public class RuntimeEventHandleException extends RuntimeException {

    public RuntimeEventHandleException() {
    }

    public RuntimeEventHandleException(String message) {
        super(message);
    }

    public RuntimeEventHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeEventHandleException(Throwable cause) {
        super(cause);
    }

    public RuntimeEventHandleException(String message,
                                       Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
