package net.neoremind.mycode.concurrent.juc.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicSingleton {

	private static AtomicReference<AtomicSingleton> atomicReference = new AtomicReference<AtomicSingleton>();

	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newFixedThreadPool(10);
		List<Callable<AtomicSingleton>> tasks = new ArrayList<Callable<AtomicSingleton>>();
		for (int i = 0; i < 5; i++) {
			tasks.add(new Callable<AtomicSingleton>() {
				public AtomicSingleton call() throws Exception {
					AtomicSingleton singleton = AtomicSingleton.getInstance();
					return singleton;
				}
			});
		}
		
		List<Future<AtomicSingleton>> futures = new ArrayList<Future<AtomicSingleton>>(10);
		for (Callable<AtomicSingleton> task : tasks) {
			futures.add(es.submit(task));
		}

		for (Future<AtomicSingleton> future : futures) {
			if (future.get() != null) {
				System.out.println(future.get());
			}
		}
		Thread.sleep(5000);
		es.shutdown();
	}

	// test how constructor get into by new AtomicSingleton statement
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	
	public AtomicSingleton() {
		int i = atomicInteger.get();
		atomicInteger.addAndGet(i++);
	}

	public static AtomicSingleton getInstance() {
		AtomicSingleton atomicSingleton = atomicReference.get();
		if (null == atomicSingleton) {
			System.out.println("init");
			synchronized (AtomicSingleton.class) {
				System.out.println("heie");
				// double checking the AtomicSingleton whether initial, ensure not new AtomicSingleton twice
				// to waste the system resource
				if (null != (atomicSingleton = atomicReference.get())) {
					return atomicSingleton;
				}
				System.out.println("ha");
				// ensure the locate the memory, construct and other instructs is atomic,
				// whatever the implementation of the JVM
				atomicReference.getAndSet(new AtomicSingleton());
				System.out.println("get");
				// atomicReference.compareAndSet(null, new AtomicSingleton());
				atomicSingleton = atomicReference.get();
			}
		} else {
			System.out.println("not null!");
		}
		return atomicSingleton;
	}

	public AtomicInteger getAtomicInteger() {
		return atomicInteger;
	}

	public void setAtomicInteger(AtomicInteger atomicInteger) {
		this.atomicInteger = atomicInteger;
	}

}
