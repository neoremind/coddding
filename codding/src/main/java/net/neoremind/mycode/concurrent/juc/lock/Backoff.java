/**
 * 
 */
package net.neoremind.mycode.concurrent.juc.lock;

import java.util.Random;

/**
 * 回退休眠，减少锁竞争
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月1日 下午2:04:07
 */
public class Backoff {
    private final int minDelay, maxDelay;
    private int limit; // wait between limit and 2*limit
    private final Random random; // add randomness to wait

    /**
     * Prepare to pause for random duration.
     * 
     * @param min smallest back-off
     * @param max largest back-off
     */
    public Backoff(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        minDelay = min;
        maxDelay = min;
        limit = minDelay;
        random = new Random();
    }

    /**
     * Backoff for random duration.
     * 
     * @throws java.lang.InterruptedException
     */
    public void backoff() throws InterruptedException {
        int delay = random.nextInt(limit);
        if (limit < maxDelay) { // double limit if less than max
            limit = 2 * limit;
        }
        Thread.sleep(delay);
    }
}
