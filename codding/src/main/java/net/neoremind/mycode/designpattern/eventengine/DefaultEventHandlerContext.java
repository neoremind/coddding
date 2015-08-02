package net.neoremind.mycode.designpattern.eventengine;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEventHandlerContext implements EventHandlerContext {

    private static Logger logger = LoggerFactory.getLogger(DefaultEventHandlerContext.class);

    public DefaultEventHandlerContext(EventHandlerStack stack) {
        this.stack = stack;
        this.current = stack.top;
        this.attributes = new HashMap<>();
    }

    protected EventHandlerStack stack;
    protected EventHandlerElement current;
    protected Map<String, Object> attributes;

    /* true=downstream,false=upstream */
    protected boolean streamFlag = true;

    @Override
    public void passThrough(final Event<?> event) throws EventHandleException {

        if (current == null) {
            return;
        }

        if (streamFlag) {
            doPassThrough(event);
        }

    }

    protected void doPassThrough(Event event) throws EventHandleException {
        EventHandlerElement _current = current;

        if (_current != null) {
            current = _current.next;
            down(_current, event);
        }

        streamFlag = false;
        current = _current;
        up(current, event);
    }

    protected void down(EventHandlerElement handler, Event event) throws EventHandleException {

        if (handler.accept(event)) {

            if (logger.isTraceEnabled()) {
                logger.trace("[{}] downstream on event:[{}]", handler.getName(), event);
            }

            try {
                handler.downstream(this, event);
            } catch (Throwable throwable) {
                handler.onException(this, event, throwable);
                throw throwable instanceof EventHandleException
                        ? (EventHandleException) throwable
                        : new EventHandleException(String.format("downstream with handler:[%s] due to error", handler),
                                throwable);
            }

        } else {
            passThrough(event);
        }

    }

    protected void up(EventHandlerElement handler, Event event) throws EventHandleException {

        if (handler.accept(event)) {

            if (logger.isTraceEnabled()) {
                logger.trace("[{}] upstream on event:[{}]", handler.getName(), event);
            }

            try {

                handler.upstream(this, event);
            } catch (Throwable throwable) {
                handler.onException(this, event, throwable);
                throw throwable instanceof EventHandleException
                        ? (EventHandleException) throwable
                        : new EventHandleException(String.format("upstream with handler:[%s] due to error", current),
                                throwable);
            }
        }

    }

    @Override
    public EventHandler getCurrentHandler() {
        return current != null ? current.delegate : null;
    }

    @Override
    public <T> T getAttribute(Object key) {
        return (T) attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public boolean containsAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public void destroy() {
        if (attributes != null) {
            attributes.clear();
        }

        stack = null;
        current = null;
        attributes = null;
    }

}
