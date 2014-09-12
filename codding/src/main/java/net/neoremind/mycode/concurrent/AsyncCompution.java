package net.neoremind.mycode.concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncCompution {

	private ExecutorService executorService;

	private FutureContext<Integer> context;
	
	private static final int CONCURRENT_COMPUTE_THREAD_COUNT = 50;

	public AsyncCompution() {
		this.executorService = Executors.newFixedThreadPool(CONCURRENT_COMPUTE_THREAD_COUNT);
		this.context = new FutureContext<Integer>();
	}

	public static void main(String[] args) {
		AsyncCompution controller = new AsyncCompution();
		controller.startAsyncCompution();

		// 启动异步计算结果输出线程，该线程扫描异步计算Futrue的状态，如果已经完成，则输出异步计算结果
		OutputResult output = new OutputResult();
		output.setFutureContext(controller.getFutureContext());
		Thread resultThread = new Thread(output);
		resultThread.start();
	}

	public FutureContext<Integer> getFutureContext() {
		return this.context;
	}

	public void startAsyncCompution() {
		/**
		 * 开启100个异步计算，每个异步计算线程随机sleep几秒来模拟计算耗时。
		 */
		
		for (int i = 0; i < 10; i++) {
			Future<Integer> future = this.executorService.submit(new FindTop100(new int[]{1}));
			// 每个异步计算的结果存放在context中
			this.context.addFuture(future);
		}
	}
	
	final Random random = new Random();  
	public class FindTop100 implements Callable<Integer> {
		
		private int[] arr;
		
		public FindTop100(int[] arr) {
			this.arr = arr;
		}
		
		@Override
		public Integer call() throws Exception {
			System.out.println(Arrays.toString(arr));
			int randomInt = random.nextInt(20);  
            Thread.sleep(randomInt * 1000);  
			return 1;
		}
		
	}

	public static class FutureContext<T> {

		private List<Future<T>> futureList = new ArrayList<Future<T>>();

		public void addFuture(Future<T> future) {
			this.futureList.add(future);
		}

		public List<Future<T>> getFutureList() {
			return this.futureList;
		}
	}

	public static class OutputResult implements Runnable {

		private FutureContext<Integer> context;

		public void setFutureContext(FutureContext<Integer> context) {
			this.context = context;
		}

		@Override
		public void run() {
			System.out.println("start to output result:");
			List<Future<Integer>> list = this.context.getFutureList();

			for (Future<Integer> future : list) {
				this.outputResultFromFuture(future);
			}

			System.out.println("finish to output result.");
		}

		private void outputResultFromFuture(Future<Integer> future) {
			try {
				while (true) {
					if (future.isDone() && !future.isCancelled()) {
						System.out.println("Future:" + future + ",Result:"
								+ future.get());
						break;
					} else {
						Thread.sleep(1000);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
