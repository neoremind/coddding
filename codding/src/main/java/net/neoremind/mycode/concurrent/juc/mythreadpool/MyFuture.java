package net.neoremind.mycode.concurrent.juc.mythreadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 自己依据理解根据{@link java.util.concurrent.Future}写的一个简单的实现
 *
 * @author zhangxu
 */
public class MyFuture<T> {

    private Callable<T> callable;

    private T result;

    private Throwable throwable;

    private CountDownLatch cdl = new CountDownLatch(1);

    public MyFuture(Callable<T> callable) {
        this.callable = callable;
    }

    public T get() throws InterruptedException {
        cdl.await();
        return result;
    }

    public void run() {
        try {
            T t = callable.call();
            result = t;
        } catch (Exception e) {
            throwable = e;
        } finally {
            done();
        }
    }

    private void done() {
        cdl.countDown();
    }

}
