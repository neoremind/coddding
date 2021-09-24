package net.neoremind.mycode.concurrent.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * SimpleAsyncProcessor.
 *
 * <p>For demonstration only.
 *
 * @author xu.zx
 */
public class SimpleAsyncProcessor extends AsyncProcessor<String, Object, Object, Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAsyncProcessor.class);

	protected List<String> lists = new ArrayList<>();

	protected List<String> ready;

	public SimpleAsyncProcessor(String name, int capacity, int maxPollTimeoutMs) {
		super(name, capacity, maxPollTimeoutMs);
	}

	public SimpleAsyncProcessor(final String name, final int capacity, final int maxPollTimeoutMs,
			final ProcessorExceptionHandler processorExceptionHandler, final HandleCompletionCallback handleCompletionCallback) {
		super(name, capacity, maxPollTimeoutMs, processorExceptionHandler, handleCompletionCallback);
	}

	@Override
	protected void doPut(String s, Object o1, Object o2, Object o3) {
		lists.add(s);
	}

	@Override
	protected void doDrain() {
		ready = lists;
		lists = new ArrayList<>();
	}

	@Override
	protected int doHandle() {
		LOGGER.info(String.join(",", ready));
		return ready.size();
	}

}
