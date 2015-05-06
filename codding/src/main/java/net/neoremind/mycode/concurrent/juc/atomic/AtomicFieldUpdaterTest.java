package net.neoremind.mycode.concurrent.juc.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicFieldUpdaterTest {

	class DemoData {
		public volatile int value1 = 1;
		volatile int value2 = 2;
		protected volatile int value3 = 3;
		@SuppressWarnings("unused")
		private volatile int value4 = 4;
	}

	AtomicIntegerFieldUpdater<DemoData> getUpdater(String fieldName) {
		return AtomicIntegerFieldUpdater.newUpdater(DemoData.class, fieldName);
	}

	void doit() {
		DemoData data = new DemoData();
		System.out.println("1 ==> " + getUpdater("value1").getAndSet(data, 10));
		System.out.println("3 ==> " + getUpdater("value2").incrementAndGet(data));
		System.out.println("2 ==> " + getUpdater("value3").decrementAndGet(data));
		System.out.println("true ==> " + getUpdater("value4").compareAndSet(data, 4, 5));
	}

	public static void main(String[] args) {
		AtomicFieldUpdaterTest demo = new AtomicFieldUpdaterTest();
		demo.doit();
	}
}
