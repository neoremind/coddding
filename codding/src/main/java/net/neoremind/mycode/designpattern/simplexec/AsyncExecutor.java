package net.neoremind.mycode.designpattern.simplexec;


import java.util.concurrent.Callable;

/**
 * Created by helechen on 2017/1/7.
 */
public interface AsyncExecutor<T> {

    /**
     * Starts processing of an async task. Returns immediately with async result.
     *
     * @param task task to be executed asynchronously
     * @return async result for the task
     */
    <T> AsyncResult<T> run(Callable<T> task);

    /**
     * Starts processing of an async task. Returns immediately with async result. Executes callback
     * when the task is completed.
     *
     * @param task     task to be executed asynchronously
     * @param callback callback to be executed on task completion
     * @return async result for the task
     */
    <T> AsyncResult<T> run(Callable<T> task, AsyncCallback<T> callback);

}
