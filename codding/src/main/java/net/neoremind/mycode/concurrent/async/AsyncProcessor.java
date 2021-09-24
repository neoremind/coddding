package net.neoremind.mycode.concurrent.async;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AsyncProcessor
 *
 * @param <T>  the type of elements to put
 * @param <K1> the first attachment that is useful when putting
 * @param <K2> the second attachment that is useful when putting
 * @author xu.zx
 */
public abstract class AsyncProcessor<T, K1, K2, K3> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class);

	/**
	 * This will append in asynchronous thread name to be better identified.
	 */
	private final String name;

	/**
	 * Asynchronous thread max poll timeout in millis.
	 */
	private int maxPollTimeoutMs;

	private final Lock lock = new ReentrantLock();

	private final Condition notFull = lock.newCondition();

	private final Condition notEmpty = lock.newCondition();

	/**
	 * Current buffer size, which is also elements' added count.
	 */
	private int size;

	/**
	 * Maximum capacity buffered.
	 */
	private int capacity;

	/**
	 * Signal to asynchronous thread which executes {@link #runLoop()} to stop gracefully.
	 */
	private volatile boolean stop = false;

	/**
	 * Asynchronous thread uncaught exception handler, whether to preform fail-fast or fail-over strategy
	 * will hand over to the implementation.
	 */
	private final ProcessorExceptionHandler processorExceptionHandler;

	/**
	 * After {@link #doHandle()} done, this is the callback to indicates how many stuff in batch are handled.
	 */
	private final HandleCompletionCallback handleCompletionCallback;

	/**
	 * Use in {@link #stop(long)} to await shutdown gracefully.
	 */
	private final CountDownLatch stopLatch;

	public AsyncProcessor(String name, int capacity, int maxPollTimeoutMs) {
		this(name, capacity, maxPollTimeoutMs, new NopProcessorExceptionHandler(), new NopHandleCompletionCallback());
	}

	public AsyncProcessor(String name, int capacity, int maxPollTimeoutMs, ProcessorExceptionHandler processorExceptionHandler, HandleCompletionCallback handleCompletionCallback) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(name));
		Preconditions.checkArgument(capacity > 0);
		// to avoid burning cpu, we have to restrict the max poll timeout to be greater than 1 second
		Preconditions.checkArgument(maxPollTimeoutMs > 999);
		this.name = name;
		this.capacity = capacity;
		this.maxPollTimeoutMs = maxPollTimeoutMs;
		this.processorExceptionHandler = processorExceptionHandler;
		this.handleCompletionCallback = handleCompletionCallback;
		this.stopLatch = new CountDownLatch(1);
	}

	public void start() {
		createThreadFactory("async-processor-bg-" + name).newThread(this::runLoop).start();
	}

	/**
	 * Only mark stop flag and return.
	 */
	public void fastStop() {
		stop = true;
	}

	/**
	 * Stop in blocking way.
	 */
	public void stop(long timeoutMs) {
		stop = true;
		try {
			stopLatch.await(timeoutMs, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Abstract method.
	 *
	 * <p>Add element in blocking way.
	 *
	 * @param t element
	 */
	protected abstract void doPut(T t, K1 k1, K2 k2, K3 k3);

	/**
	 * Abstract method.
	 *
	 * <p>After buffer is full or poll timeout, drain the objects.
	 */
	protected abstract void doDrain();

	/**
	 * Abstract method.
	 *
	 * <p>After draining, do handling stuff out of lock scope, probably time-consuming.
	 */
	protected abstract int doHandle();

	/**
	 * Return true when element is put successfully, or else if stopped.
	 */
	public boolean put(T t, K1 k1, K2 k2, K3 k3) {
		lock.lock();
		try {
			while (size == capacity) {
				notFull.await();
			}
			// we have to check stop here since gracefully shutdown
			if (stop) {
				return false;
			}
			doPut(t, k1, k2, k3);
			size++;
			if (size == capacity) {
				notEmpty.signal();
			}
			return true;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Background task executable.
	 */
	public void runLoop() {
		LOGGER.info("Start to run background task to drain");
		while (true) {
			if (stop) {
				shutdownGracefully();
				break;
			}

			try {
				lock.lock();
				try {
					while (size < capacity) {
						// LOGGER.info("not enough, will wait up to {} millis and check if not empty", maxPollTimeoutMs);
						// if the waiting time detectably elapsed meets, return false
						// then we know that there are less than capacity elements to handle
						if (!notEmpty.await(maxPollTimeoutMs, TimeUnit.MILLISECONDS)) {
							break;
						}
					}
					// LOGGER.info("there are {} to handle", size);
					doDrain();
					size = 0;
					notFull.signal();
				} catch (InterruptedException e) {
					LOGGER.error("Interrupted, break running loop, this should not happen");
					break;
				} finally {
					lock.unlock();
				}

				int batchProcessed = doHandle();
				handleCompletionCallback.onComplete(batchProcessed);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				processorExceptionHandler.onError(e);
			}
		}
	}

	@VisibleForTesting
	public void flush() {
		lock.lock();
		try {
			doDrain();
			doHandle();
		} finally {
			lock.unlock();
		}
	}

	private void shutdownGracefully() {
		lock.lock();
		try {
			LOGGER.info("Shutdown background task...");
			doDrain();
			int batchProcessed = doHandle();
			handleCompletionCallback.onComplete(batchProcessed);
			stopLatch.countDown();
			LOGGER.info("Shutdown background task done");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			processorExceptionHandler.onError(e);
		} finally {
			lock.unlock();
		}
	}

	public void updateMaxPollTimeoutMs(int newMaxPollTimeoutMs) {
		lock.lock();
		try {
			this.maxPollTimeoutMs = newMaxPollTimeoutMs;
		} finally {
			lock.unlock();
		}
	}

	public void updateCapacity(int newCapacity) {
		lock.lock();
		try {
			this.capacity = newCapacity;
		} finally {
			lock.unlock();
		}
	}

	private ThreadFactory createThreadFactory(String nameFormat) {
		return new ThreadFactoryBuilder().setDaemon(true).setNameFormat(nameFormat).setUncaughtExceptionHandler((t, e) -> LOGGER.error("FATAL ERROR!!! Uncaught exception " + e.getMessage(), e)).build();
	}
}
