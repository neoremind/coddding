package net.neoremind.mycode.concurrent.async;

import com.google.common.collect.Lists;
import org.apache.commons.lang.math.RandomUtils;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * @author xu.zx
 */
public class AsyncProcessorTest {

    public static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final Object DUMMY1 = new Object();

    private static final Object DUMMY2 = new Object();

    private static final Object DUMMY3 = new Object();

    @Test
    public void testOne() throws InterruptedException {
        testPutHandle(4, 2000, 1, 1, Optional.empty());
    }

    @Test(timeout = 20000)
    public void testTimeout() throws InterruptedException {
        testPutHandle(Integer.MAX_VALUE, 2000, 10000, 1, Optional.of(1000));
    }

    @Test(timeout = 20000)
    public void testCapacity() throws InterruptedException {
        testPutHandle(1000, Integer.MAX_VALUE, 4000, 1, Optional.of(100));
    }

    @Test
    public void testSimple() throws InterruptedException {
        testPutHandle(4, 2000, 99, 1, Optional.empty());
    }

    @Test
    public void testSimpleSlowProcess() throws InterruptedException {
        testPutHandle(4, 2000, 99, 1, Optional.of(100));
    }

    @Test
    public void testMultiThreadPut() throws InterruptedException {
        testPutHandle(32, 1000, 518, 6, Optional.empty());
    }

    @Test
    public void testMultiThreadPutSlowProcess() throws InterruptedException {
        testPutHandle(64, 2000, 999, 4, Optional.of(100));
    }

    // slow test
    @Test
    @Ignore
    public void testMultiThreadHugePut() throws InterruptedException {
        testPutHandle(1024, 2000, 999999, 8, Optional.of(1));
    }

    @Test
    public void testMultiThreadSlowPut() throws InterruptedException {
        testPutHandle(4, 2000, 99, 4, Optional.empty(), Optional.of(100));
    }

    @Test
    public void testShutdownGracefully() throws InterruptedException {
        int capacity = 4;
        int threadNum = 4;
        int totalPutNumPerThread = 100000;
        int maxPollTimeoutMs = 2000;

        final List<String> exptected = Collections.synchronizedList(Lists.newArrayList());
        final List<String> result = Collections.synchronizedList(Lists.newArrayList());
        CountDownLatch shouldCheck = new CountDownLatch(1);
        AsyncProcessor<String, Object, Object, Object> asyncExecutor =
                new SimpleAsyncProcessor("1", capacity, maxPollTimeoutMs) {
                    @Override
                    protected int doHandle() {
                        result.addAll(ready);
                        if (result.size() >= totalPutNumPerThread * threadNum) {
                            shouldCheck.countDown();
                        }
                        // System.out.println(String.join(",", ready));
                        return ready.size();
                    }
                };
        asyncExecutor.start();

        long base = 1000000L;

        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            long start = i * base;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int k = 0; k < totalPutNumPerThread; k++) {
                            if (asyncExecutor.put(String.valueOf(start + k), DUMMY1, DUMMY2, DUMMY3)) {
                                exptected.add(String.valueOf(start + k));
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            });
            threads[i] = t;
            t.start();
        }

        Thread.sleep(2000);

//        for (Thread thread : threads) {
//            thread.interrupt();
//        }

        asyncExecutor.stop(5000);

        Collections.sort(exptected);
        Collections.sort(result);
        assertThat(result.size(), is(exptected.size()));
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i), is(exptected.get(i)));
        }
    }

    @Test
    public void testProducerThreadInterrupted() throws InterruptedException {
        int capacity = 4;
        int threadNum = 4;
        int totalPutNumPerThread = 100000;
        int maxPollTimeoutMs = 2000;

        final List<String> exptected = Collections.synchronizedList(Lists.newArrayList());
        final List<String> result = Collections.synchronizedList(Lists.newArrayList());
        CountDownLatch shouldCheck = new CountDownLatch(1);
        AsyncProcessor<String, Object, Object, Object> asyncExecutor =
                new SimpleAsyncProcessor("1", capacity, maxPollTimeoutMs) {
                    @Override
                    protected int doHandle() {
                        result.addAll(ready);
                        if (result.size() >= totalPutNumPerThread * threadNum) {
                            shouldCheck.countDown();
                        }
                        // System.out.println(String.join(",", ready));
                        return ready.size();
                    }
                };
        asyncExecutor.start();

        long base = 1000000L;

        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            long start = i * base;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int k = 0; k < totalPutNumPerThread; k++) {
                            if (asyncExecutor.put(String.valueOf(start + k), DUMMY1, DUMMY2, DUMMY3)) {
                                exptected.add(String.valueOf(start + k));
                            }
                        }
                    } catch (Exception e) {
                        // System.err.println(e.getMessage());
                    }
                }
            });
            threads[i] = t;
            t.start();
        }

        Thread.sleep(2000);

        for (Thread thread : threads) {
            thread.interrupt();
        }

        Thread.sleep(1000);

        asyncExecutor.stop(5000);

        Collections.sort(exptected);
        Collections.sort(result);
        assertThat(result.size(), is(exptected.size()));
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i), is(exptected.get(i)));
        }
    }

    @Test
    public void testAsyncProcessorHangAndProducerBackPressure() throws InterruptedException {
        int capacity = 4;
        int threadNum = 4;
        int totalPutNumPerThread = 100000;
        int maxPollTimeoutMs = 2000;

        final List<String> exptected = Collections.synchronizedList(Lists.newArrayList());
        final List<String> result = Collections.synchronizedList(Lists.newArrayList());
        CountDownLatch shouldCheck = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger(0);
        ProcessorExceptionHandler exceptionHandler = e -> {
            throw new IllegalStateException(e);
        };
        AsyncProcessor<String, Object, Object, Object> asyncExecutor =
                new SimpleAsyncProcessor("1", capacity, maxPollTimeoutMs, exceptionHandler, new NopHandleCompletionCallback()) {
                    @Override
                    protected int doHandle() {
                        result.addAll(ready);
                        if (result.size() >= totalPutNumPerThread * threadNum) {
                            shouldCheck.countDown();
                        }
                        if (counter.addAndGet(ready.size()) >= 100_000) {
                            throw new RuntimeException("Fail!!!");
                        }
                        // System.out.println(String.join(",", ready));
                        return ready.size();
                    }
                };
        asyncExecutor.start();

        long base = 1000000L;

        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            long start = i * base;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int k = 0; k < totalPutNumPerThread; k++) {
                            if (asyncExecutor.put(String.valueOf(start + k), DUMMY1, DUMMY2, DUMMY3)) {
                                exptected.add(String.valueOf(start + k));
                            }
                        }
                    } catch (Exception e) {
                        // System.err.println(e.getMessage());
                    }
                }
            });
            threads[i] = t;
            t.start();
        }

        Thread.sleep(2000);

        assertThat(result.size(), is(100_000));

        // there are extra capacity in buffer which will be lost
        assertThat(exptected.size(), is(100_000 + capacity));

        asyncExecutor.stop(5000);
    }

    public void testPutHandle(int capacity, int maxPollTimeoutMs, int totalPutNumPerThread, int threadNum,
                              Optional<Integer> asyncProcessRandomSleepMs) throws InterruptedException {
        testPutHandle(capacity, maxPollTimeoutMs, totalPutNumPerThread, threadNum, asyncProcessRandomSleepMs, Optional.empty());
    }

    public void testPutHandle(int capacity, int maxPollTimeoutMs, int totalPutNumPerThread, int threadNum,
                              Optional<Integer> asyncProcessRandomSleepMs, Optional<Integer> putRandomSleepMs) throws InterruptedException {
        final List<String> result = Collections.synchronizedList(Lists.newArrayList());
        CountDownLatch shouldCheck = new CountDownLatch(1);
        AsyncProcessor<String, Object, Object, Object> asyncExecutor =
                new SimpleAsyncProcessor("1", capacity, maxPollTimeoutMs) {
                    @Override
                    protected int doHandle() {
                        slowDown(asyncProcessRandomSleepMs);
                        result.addAll(ready);
                        if (result.size() >= totalPutNumPerThread * threadNum) {
                            shouldCheck.countDown();
                        }
                        // System.out.println(String.join(",", ready));
                        return ready.size();
                    }
                };
        asyncExecutor.start();

        long base = 1000000L;

        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            long start = i * base;
            Thread t;
            if (putRandomSleepMs.isPresent()) {
                t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int k = 0; k < totalPutNumPerThread; k++) {
                            slowDown(putRandomSleepMs);
                            asyncExecutor.put(String.valueOf(start + k), DUMMY1, DUMMY2, DUMMY3);
                        }
                    }
                });
            } else {
                t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int k = 0; k < totalPutNumPerThread; k++) {
                            asyncExecutor.put(String.valueOf(start + k), DUMMY1, DUMMY2, DUMMY3);
                        }
                    }
                });
            }
            threads[i] = t;
            t.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        shouldCheck.await();
        Map<Long, List<String>> map = new TreeMap<>(new ArrayList<>(result).stream().collect(Collectors.groupingBy(s -> Long.parseLong(s) / base)));
        assertThat(map.size(), is(threadNum));
        for (int k = 0; k < threadNum; k++) {
            List<String> lists = map.get((long) k);
            assertThat(lists.size(), is(totalPutNumPerThread));
            for (int i = 0; i < totalPutNumPerThread; i++) {
                assertThat(lists.get(i), is(String.valueOf(base * k + i)));
            }
        }
        asyncExecutor.fastStop();
    }

    private void slowDown(Optional<Integer> asyncProcessRandomSleepMs) {
        if (asyncProcessRandomSleepMs.isPresent()) {
            LockSupport.parkNanos(RandomUtils.nextInt(RANDOM, asyncProcessRandomSleepMs.get()) * 1000000);
        }
    }

}
