package net.neoremind.mycode.designpattern.eventengine;

public class EventHandlerElement<E extends Event<?>> implements EventHandler<E> {

    public EventHandlerElement(EventHandler delegate) {
        this.delegate = delegate;
    }

    protected EventHandler delegate;
    protected EventHandlerElement previous;
    protected EventHandlerElement next;

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public boolean accept(Event<?> event) {
        return delegate.accept(event);
    }

    @Override
    public void downstream(EventHandlerContext context, E event) throws Throwable {
        delegate.downstream(context, event);
    }

    @Override
    public void upstream(EventHandlerContext context, E event) throws Throwable {
        delegate.upstream(context, event);
    }

    @Override
    public void onException(EventHandlerContext context, E event, Throwable throwable) {
        delegate.onException(context, event, throwable);
    }

    @Override
    public String toString() {
        return String.format("{%s->[%s]->%s}",
                previous != null ? previous.delegate.getName() : "",
                delegate.getName(),
                next != null ? next.delegate.getName() : "");
    }

}
