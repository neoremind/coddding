package net.neoremind.mycode;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Lock lock = new ReentrantLock();
        Condition cond = lock.newCondition();
        AtomicInteger flag = new AtomicInteger(0);
        Counter c1 = new Counter("A");
        Counter c2 = new Counter("B");
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<String> f1 = executorService.submit(c1);
        Future<String> f2 = executorService.submit(c2);
        System.out.println(f1.get());
        System.out.println(f2.get());
        executorService.shutdown();
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Counter c1 = new Counter("A");
        Counter c2 = new Counter("B");
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<String> f1 = executorService.submit(c1);
        Future<String> f2 = executorService.submit(c2);
        assertThat(f1.get(), is("A"));
        assertThat(f2.get(), is("B"));
        executorService.shutdown();
    }

    static class Counter implements Callable<String> {

        private String name;

        public Counter(String name) {
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            return name;
        }
    }

}
