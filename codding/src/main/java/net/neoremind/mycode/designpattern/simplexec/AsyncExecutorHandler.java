package net.neoremind.mycode.designpattern.simplexec;


import com.google.common.base.Optional;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by helechen on 2017/1/7.
 */
public class AsyncExecutorHandler<T> implements AsyncExecutor<T> {

    /**
     * Index for thread naming
     */
    private final AtomicInteger idx = new AtomicInteger(0);

    @Override
    public <T> AsyncResult<T> run(Callable<T> task) {
        return run(task, null);
    }

    @Override
    public <T> AsyncResult<T> run(Callable<T> task, AsyncCallback<T> callback) {
        final AsyncResult<T> result = new ConcreteAsyncResult<T>();
        new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        }, "async-executor-" + idx.incrementAndGet()).start();
        return result;
    }
}
