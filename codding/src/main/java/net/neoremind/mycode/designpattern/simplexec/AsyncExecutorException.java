package net.neoremind.mycode.designpattern.simplexec;

public class AsyncExecutorException extends RuntimeException {

    public AsyncExecutorException() {
    }

    public AsyncExecutorException(String message) {
        super(message);
    }

    public AsyncExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsyncExecutorException(Throwable cause) {
        super(cause);
    }

    public AsyncExecutorException(String message,
                                  Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
