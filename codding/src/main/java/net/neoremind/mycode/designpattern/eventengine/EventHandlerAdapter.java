package net.neoremind.mycode.designpattern.eventengine;

public class EventHandlerAdapter<E extends Event<?>> implements EventHandler<E> {

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean accept(Event<?> event) {
        return true;
    }

    @Override
    public void downstream(EventHandlerContext context, E event) throws Throwable {
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, E event) throws Throwable {

    }

    @Override
    public void onException(EventHandlerContext context, E event, Throwable throwable) {

    }
}
