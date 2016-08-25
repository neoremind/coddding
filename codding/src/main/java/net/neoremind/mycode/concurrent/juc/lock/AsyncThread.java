package net.neoremind.mycode.concurrent.juc.lock;

/**
 * 异步线程
 *
 * @author zhangxu
 */
public class AsyncThread {

    /**
     * 根据Task接口异步跑线程
     *
     * @param task
     */
    public static void run(final Runnable runnable) {
        new Thread(runnable).start();
    }

}
