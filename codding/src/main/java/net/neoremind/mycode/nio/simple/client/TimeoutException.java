package net.neoremind.mycode.nio.simple.client;

public class TimeoutException extends RuntimeException {

    private static final long serialVersionUID = 5196421433506179782L;

    public TimeoutException() {
        super();
    }

    public TimeoutException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public TimeoutException(String arg0) {
        super(arg0);
    }

    public TimeoutException(Throwable arg0) {
        super(arg0);
    }
}
