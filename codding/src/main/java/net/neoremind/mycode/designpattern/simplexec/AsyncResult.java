package net.neoremind.mycode.designpattern.simplexec;

import java.util.concurrent.Future;

public interface AsyncResult<T> extends Future<T> {

    void handleResult(T result);

    void handleError(Throwable error);

    T getResult() throws AsyncExecutorException;

    Throwable getError();
}
