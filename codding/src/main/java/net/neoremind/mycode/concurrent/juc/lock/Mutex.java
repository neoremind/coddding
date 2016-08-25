/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package net.neoremind.mycode.concurrent.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Lock free的互斥锁，简单实现，不可重入锁
 */
public class Mutex implements Lock {

    private static final int FREE = 0;
    private static final int BUSY = 1;

    private static class LockSync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = 4689388770786922019L;

        protected boolean isHeldExclusively() {
            return getState() == BUSY;
        }

        public boolean tryAcquire(int acquires) {
            return compareAndSetState(FREE, BUSY);
        }

        protected boolean tryRelease(int releases) {
            if (getState() == FREE) {
                throw new IllegalMonitorStateException();
            }

            setState(FREE);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }

    }

    private final LockSync sync = new LockSync();

    public void lock() {
        sync.acquire(0);
    }

    public boolean tryLock() {
        return sync.tryAcquire(0);
    }

    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    public void unlock() {
        sync.release(0);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(0);
    }

}
