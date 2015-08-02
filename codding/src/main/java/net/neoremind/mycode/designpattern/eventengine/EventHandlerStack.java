package net.neoremind.mycode.designpattern.eventengine;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NotThreadSafe
 */
public class EventHandlerStack {

    private static Logger logger = LoggerFactory.getLogger(EventHandlerStack.class);

    public enum Position {
        ABOVE,
        BELOW,
    }

    protected EventHandlerElement top;
    protected EventHandlerElement bottom;

    /**
     * @return top handler
     */
    public EventHandlerElement getTop() {
        return top;
    }

    /**
     * @return bottom handler
     */
    public EventHandlerElement getBottom() {
        return bottom;
    }

    /**
     * @return all handlers
     */
    public List<EventHandler> getHandlers() {
        List<EventHandler> handlers = new ArrayList<>(15);
        EventHandlerElement element = top;
        while (element != null) {
            handlers.add(element.delegate);
            element = element.next;
        }
        return handlers;
    }

    /**
     * insert handler at top
     *
     * @param handler
     *
     * @return stack for chain invoke
     */
    public EventHandlerStack top(EventHandler handler) {
        EventHandlerElement element = new EventHandlerElement(handler);

        /* 当前stack是空的,只需要将头尾都置为所给handler就可以 */
        if (bottom == null) {
            top = bottom = element;
            return this;
        }

        top.previous = element;
        element.next = top;
        top = element;

        return this;
    }

    /**
     * insert handler at bottom
     *
     * @param handler
     *
     * @return stack for chain invoke
     */
    public EventHandlerStack bottom(EventHandler handler) {
        EventHandlerElement element = new EventHandlerElement(handler);

        /* 当前stack是空的,只需要将头尾都置为所给handler就可以 */
        if (bottom == null) {
            top = bottom = element;
            return this;
        }

        bottom.next = element;
        element.previous = bottom;
        bottom = element;

        return this;
    }

    /**
     * insert handler at top
     *
     * @param handler
     *
     * @return stack for chain invoke
     */
    public EventHandlerStack add(EventHandler handler) {
        if (handler == null) {
            logger.warn("the handler can not be null, ignore add operation");
            return this;
        }

        EventHandlerElement element = new EventHandlerElement(handler);

        /* 当前stack是空的,只需要将头尾都置为所给handler就可以 */
        if (bottom == null) {
            top = bottom = element;
            return this;
        }

        top.previous = element;
        element.next = top;
        top = element;

        return this;
    }

    /**
     * insert handlers at top
     *
     * @param handlers
     *
     * @return stack for chain invoke
     */
    public EventHandlerStack add(EventHandler... handlers) {
        if (handlers != null) {
            for (EventHandler handler : handlers) {
                add(handler);
            }
        }

        return this;
    }

    /**
     * insert handler at position
     *
     * @param handler
     *
     * @return stack for chain invoke
     */
    public EventHandlerStack insert(EventHandler handler, Position position, String name) {
        if (handler == null || position == null || name == null) {
            logger.warn("handler|position|name can not be null, ignore insert operation");
            return this;
        }

        EventHandlerElement element = findElement(name);
        if (element == null) {
            logger.warn("can not find handler with name:[{}], ignore insert operation", name);
            return this;
        }

        EventHandlerElement insert = new EventHandlerElement(handler);
        switch (position) {
            case ABOVE:
                EventHandlerElement above = element.previous;

                /* element在栈顶 */
                if (above == null) {
                    return add(handler);
                }

                above.next = insert;
                insert.previous = above;
                insert.next = element;
                element.previous = insert;
                break;

            case BELOW:
                EventHandlerElement below = element.next;

                /* element在栈底 */
                if (below == null) {
                    return bottom(handler);
                }

                element.next = insert;
                insert.previous = element;
                insert.next = below;
                below.previous = insert;
        }

        return this;
    }

    /**
     * remove handler with name
     *
     * @param name
     *
     * @return stack for chain invoke
     */
    public EventHandlerStack remove(String name) {
        if (name == null) {
            logger.warn("name can not be null, ignore remove operation");
            return this;
        }

        EventHandlerElement element = findElement(name);
        if (element == null) {
            logger.warn("can not find handler with name:[{}], ignore insert operation", name);
            return this;
        }

        EventHandlerElement above = element.previous;
        EventHandlerElement below = element.next;

        element.previous = null;
        element.next = null;

        /* element在栈顶 */
        if (above == null) {
            top = below;
        } else {
            above.next = below;
        }

        /* element在栈底 */
        if (below == null) {
            bottom = above;
        } else {
            below.previous = above;
        }

        return this;
    }

    protected EventHandler find(String name) {
        EventHandlerElement element = findElement(name);
        return element != null ? element.delegate : null;
    }

    protected EventHandlerElement findElement(String name) {
        EventHandlerElement tmp = top;
        String handlerName;

        while (tmp != null) {
            handlerName = tmp.getName();

            if (handlerName != null && handlerName.equals(name)) {
                return tmp;
            }

            tmp = tmp.next;
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("head->");
        EventHandlerElement element = top;
        while (element != null) {
            sb.append(element.getName()).append("->");
            element = element.next;
        }
        sb.append("tail");
        return sb.toString();
    }
}
