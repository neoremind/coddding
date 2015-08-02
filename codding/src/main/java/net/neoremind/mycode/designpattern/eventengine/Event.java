package net.neoremind.mycode.designpattern.eventengine;

public interface Event<T> {

    /**
     * get event type
     *
     * @return
     */
    int getType();

    /**
     * @return event payload
     */
    T getPayload();

    /**
     * set event payload
     *
     * @param t
     */
    void setPayload(T t);

}
