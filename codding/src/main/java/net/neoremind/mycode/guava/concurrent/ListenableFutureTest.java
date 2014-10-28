package net.neoremind.mycode.guava.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class ListenableFutureTest {

	public static void main(String[] args) {
		ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
		final ListenableFuture<Integer> listenableFuture = executorService.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println("call execute..");
				TimeUnit.SECONDS.sleep(1);
				return 7;
			}
		});

		listenableFuture.addListener(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("get listenable future's result " + listenableFuture.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}, executorService);

		Futures.addCallback(listenableFuture, new FutureCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				System.out.println("get listenable future's result with callback " + result);
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}
		});

	}

}
