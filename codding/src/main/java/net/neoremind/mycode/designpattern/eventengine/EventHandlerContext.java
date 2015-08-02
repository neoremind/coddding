package net.neoremind.mycode.designpattern.eventengine;

public interface EventHandlerContext {

    /**
     * fire next handler
     *
     * @param event
     *
     * @throws EventHandleException
     */
    void passThrough(Event<?> event) throws EventHandleException;

    /**
     * @return current handler
     */
    EventHandler getCurrentHandler();

    /**
     * @param key
     * @param <T>
     *
     * @return attribute value with given key
     */
    <T> T getAttribute(Object key);

    /**
     * set attribute value with given key
     *
     * @param key
     * @param value
     */
    void setAttribute(String key, Object value);

    /**
     * remove attribute value with given key
     *
     * @param key
     */
    void removeAttribute(String key);

    /**
     * @param key
     *
     * @return is attribute value exists with given key
     */
    boolean containsAttribute(String key);

    /**
     * destroy context
     */
    void destroy();

}
