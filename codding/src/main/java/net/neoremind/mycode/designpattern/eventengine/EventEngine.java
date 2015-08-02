package net.neoremind.mycode.designpattern.eventengine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventEngine {

    private static Logger logger = LoggerFactory.getLogger(EventEngine.class);
    private static ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 100);

    /**
     * sync process event
     *
     * @param event
     */
    public void process(Event event) {
        EventHandlerContext context = new DefaultEventHandlerContext(stack);

        try {
            context.passThrough(event);
        } catch (Throwable throwable) {
            exceptionListener.onException(context, event, throwable);
            throw new RuntimeEventHandleException(throwable);
        } finally {
            context.destroy();
        }
    }

    /**
     * async process event
     *
     * @param event
     * @param callback
     */
    public void process(final Event event, final EventCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.preInvoke(event);
                    process(event);
                    callback.onSuccess(event);
                } catch (Exception e) {
                    callback.onFail(event, e);
                }
            }
        });

    }

    protected EventHandlerStack stack = new EventHandlerStack();
    protected EventHandleExceptionListener exceptionListener = new DefaultEventHandleExceptionListener();

    /**
     * @return event process handler stack
     */
    public EventHandlerStack getStack() {
        return stack;
    }

    /**
     * set event process handler stack
     *
     * @param stack
     */
    public void setStack(EventHandlerStack stack) {
        this.stack = stack;
    }

    /**
     * @return event process exception listener
     */
    public EventHandleExceptionListener getExceptionListener() {
        return exceptionListener;
    }

    /**
     * set event process exception listener
     *
     * @param exceptionListener
     */
    public void setExceptionListener(EventHandleExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    private class DefaultEventHandleExceptionListener implements EventHandleExceptionListener {

        @Override
        public void onException(EventHandlerContext context, Event event, Throwable t) {
            logger.error(String.format("handler:[{}] process event:[{}] due to error:[{}]",
                    context.getCurrentHandler(),
                    event, t.getMessage()), t);

            throw new RuntimeEventHandleException("process event due to error", t);
        }
    }

}
