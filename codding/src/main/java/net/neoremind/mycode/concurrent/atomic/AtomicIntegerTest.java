package net.neoremind.mycode.concurrent.atomic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class AtomicIntegerTest {

	@Test
	public void testAll() throws InterruptedException {
		final AtomicInteger value = new AtomicInteger(10);
		assertThat(value.compareAndSet(1, 2), is(false));
		assertThat(value.get(), is(10));
		assertThat(value.compareAndSet(10, 3), is(true));
		assertThat(value.get(), is(3));
		value.set(0);
		//
		assertThat(value.incrementAndGet(), is(1)); // ++i
		assertThat(value.getAndAdd(2), is(1)); // i++
		assertThat(value.getAndSet(5), is(3)); // x=i;i=5;return x
		assertThat(value.get(), is(5));
		//
		final int threadSize = 10;
		Thread[] ts = new Thread[threadSize];
		for (int i = 0; i < threadSize; i++) {
			ts[i] = new Thread() {
				public void run() {
					value.incrementAndGet(); // ++i
				}
			};
		}
		//
		for (Thread t : ts) {
			t.start();
		}
		// 必须有join，否则最后一个assert会执行失败，有可能value.get()<15
		// t.join();表示当前线程停止执行直到t线程运行完毕；
		// t.join(1000); 表示当前线程等待t线程运行1000后执行；
		for (Thread t : ts) {
			t.join();
		}
		//
		assertThat(value.get(), is(5 + threadSize));
	}

}
