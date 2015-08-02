package net.neoremind.mycode.designpattern.eventengine;

public interface EventHandleExceptionListener {

    /**
     * listen event process exception
     *
     * @param context
     * @param event
     * @param throwable
     */
    void onException(EventHandlerContext context, Event event, Throwable throwable);

}
