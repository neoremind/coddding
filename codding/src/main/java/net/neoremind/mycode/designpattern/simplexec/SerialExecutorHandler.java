package net.neoremind.mycode.designpattern.simplexec;


import com.google.common.base.Optional;

import java.util.concurrent.Callable;

/**
 * Created by helechen on 2017/1/7.
 */
public class SerialExecutorHandler<T> implements AsyncExecutor<T> {

    @Override
    public <T> AsyncResult<T> run(Callable<T> task) {
        return run(task, null);
    }

    @Override
    public <T> AsyncResult<T> run(Callable<T> task, AsyncCallback<T> callback) {
        AsyncResult<T> result = new ConcreteAsyncResult<T>();
        T value = null;
        Optional<Exception> e = null;
        try {
            value = task.call();
            result.handleResult(value);
        } catch (Exception e1) {
            e = Optional.of(e1);
            result.handleError(e1);
        }
        if (callback != null) {
            callback.onComplete(value, e);
        }
        return result;
    }
}
