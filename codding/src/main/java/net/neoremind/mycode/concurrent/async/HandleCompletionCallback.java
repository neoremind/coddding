package net.neoremind.mycode.concurrent.async;

@FunctionalInterface
public interface HandleCompletionCallback {

	void onComplete(int batchProcessCount);

}
