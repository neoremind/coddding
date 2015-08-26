package net.neoremind.mycode.designpattern.async.method.invocation;

import java.util.concurrent.Callable;

import com.google.common.base.Optional;

/**
 * <p>
 * This application demonstrates the async method invocation pattern. Key parts of the pattern are
 * <code>AsyncResult</code> which is an intermediate container for an asynchronously evaluated value,
 * <code>AsyncCallback</code> which can be provided to be executed on task completion and
 * <code>AsyncExecutor</code> that manages the execution of the async tasks.
 * </p>
 * <p>
 * The main method shows example flow of async invocations. The main thread starts multiple tasks with
 * variable durations and then continues its own work. When the main thread has done it's job it collects
 * the results of the async tasks. Two of the tasks are handled with callbacks, meaning the callbacks are
 * executed immediately when the tasks complete.
 * </p>
 * <p>
 * Noteworthy difference of thread usage between the async results and callbacks is that the async results
 * are collected in the main thread but the callbacks are executed within the worker threads. This should be
 * noted when working with thread pools.
 * </p>
 * <p>
 * Java provides its own implementations of async method invocation pattern. FutureTask, CompletableFuture
 * and ExecutorService are the real world implementations of this pattern. But due to the nature of parallel
 * programming, the implementations are not trivial. This example does not take all possible scenarios into
 * account but rather provides a simple version that helps to understand the pattern.
 * </p>
 *
 * @see AsyncResult
 * @see AsyncCallback
 * @see AsyncExecutor
 * @see java.util.concurrent.FutureTask
 * @see java.util.concurrent.CompletableFuture
 * @see java.util.concurrent.ExecutorService
 */
public class App {

    /**
     * result should be :
     * <pre>
     * [executor-2] - Task completed with: test
     * [main      ] - Some hard work done
     * [executor-4] - Task completed with: 20
     * [executor-4] - 9999: 20
     * [executor-1] - Task completed with: 10
     * [executor-5] - Task completed with: I am result5
     * [executor-5] - Callback result 5: I am result5
     * [executor-3] - Task completed with: 50
     * [main      ] - Result 1: 10
     * [main      ] - Result 2: test
     * [main      ] - Result 3: 50
     * [main      ] - Result 4: 20
     * [main      ] - Result 5: I am result5
     * </pre>
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // construct a new executor that will run async tasks
        AsyncExecutor executor = new ThreadAsyncExecutor();

        // start few async tasks with varying processing times, two last with callback handlers
        AsyncResult<Integer> asyncResult1 = executor.startProcess(lazyval(10, 5000));
        AsyncResult<String> asyncResult2 = executor.startProcess(lazyval("test", 3000));
        AsyncResult<Long> asyncResult3 = executor.startProcess(lazyval(50L, 7000));
        AsyncResult<Integer> asyncResult4 = executor.startProcess(lazyval(20, 4000), callback(9999));
        AsyncResult<String> asyncResult5 =
                executor.startProcess(lazyval("I am result5", 6000), callback("Callback result 5"));

        // emulate processing in the current thread while async tasks are running in their own threads
        Thread.sleep(3500); // Oh boy I'm working hard here
        log("Some hard work done");

        // wait for completion of the tasks
        Integer result1 = executor.endProcess(asyncResult1);
        String result2 = executor.endProcess(asyncResult2);
        Long result3 = executor.endProcess(asyncResult3);
        asyncResult4.await();
        asyncResult5.await();

        // log the results of the tasks, callbacks log immediately when complete
        log("Result 1: " + result1);
        log("Result 2: " + result2);
        log("Result 3: " + result3);
        log("Result 4: " + asyncResult4.getValue());
        log("Result 5: " + asyncResult5.getValue());
    }

    /**
     * Creates a callable that lazily evaluates to given value with artificial delay.
     *
     * @param value       value to evaluate
     * @param delayMillis artificial delay in milliseconds
     *
     * @return new callable for lazy evaluation
     */
    private static <T> Callable<T> lazyval(final T value, final long delayMillis) {
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                Thread.sleep(delayMillis);
                log("Task completed with: " + value);
                return value;
            }
        };
        //        return () -> {
        //            Thread.sleep(delayMillis);
        //            log("Task completed with: " + value);
        //            return value;
        //        };
    }

    /**
     * Creates a simple callback that logs the complete status of the async result.
     *
     * @param name callback name
     *
     * @return new async callback
     */
    private static <T> AsyncCallback<T> callback(final T name) {
        return new AsyncCallback<T>() {
            @Override
            public void onComplete(T value, Optional<Exception> ex) {
                if (ex.isPresent()) {
                    log(name + " failed: " + ex.get().getMessage());
                } else {
                    log(name + ": " + value);
                }
            }
        };
        //        return (value, ex) -> {
        //            if (ex.isPresent()) {
        //                log(name + " failed: " + ex.map(Exception::getMessage).orElse(""));
        //            } else {
        //                log(name + ": " + value);
        //            }
        //        };
    }

    private static void log(String msg) {
        System.out.println(String.format("[%1$-10s] - %2$s", Thread.currentThread().getName(), msg));
    }
}
