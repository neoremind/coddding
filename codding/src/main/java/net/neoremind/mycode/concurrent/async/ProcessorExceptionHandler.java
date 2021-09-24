package net.neoremind.mycode.concurrent.async;

@FunctionalInterface
public interface ProcessorExceptionHandler {

	void onError(Throwable e);

}
