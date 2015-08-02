package net.neoremind.mycode.designpattern.eventengine;

public interface EventCallback<E extends Event> {

    /**
     * event process before invocation callback
     *
     * @param event
     */
    void preInvoke(E event);

    /**
     * event process success callback
     *
     * @param event
     */
    void onSuccess(E event);

    /**
     * event process fail callback
     *
     * @param event
     * @param throwable
     */
    void onFail(E event, Throwable throwable);

}
