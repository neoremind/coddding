package net.neoremind.mycode.designpattern.eventengine;

public interface EventHandler<E extends Event<?>> {

    /**
     * @return event name
     */
    String getName();

    boolean accept(Event<?> event);

    /**
     * downstream process
     * invoke context.passThough will fire next handler.downstream,else fire upstream stack from this handler
     *
     * @param context
     * @param event
     * @throws EventHandleException
     */
    void downstream(EventHandlerContext context, E event) throws Throwable;

    /**
     * upstream process
     *
     * @param context
     * @param event
     * @throws EventHandleException
     */
    void upstream(EventHandlerContext context, E event) throws Throwable;

    /**
     * process on exception
     *
     * @param context
     * @param event
     */
    void onException(EventHandlerContext context, E event, Throwable throwable);

}
