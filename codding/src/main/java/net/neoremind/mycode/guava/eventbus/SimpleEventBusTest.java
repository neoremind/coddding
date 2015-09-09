package net.neoremind.mycode.guava.eventbus;

import org.junit.Test;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * @author zhangxu
 */
public class SimpleEventBusTest {

    @Test
    public void testReceiveEvent() throws Exception {
        EventBus eventBus = new EventBus("test");
        EventListener listener = new EventListener();

        eventBus.register(listener);

        eventBus.post(new TestEvent(200));
        eventBus.post(new TestEvent(300));
        eventBus.post(new TestEvent(400));
        eventBus.post(400L);
    }

}

class EventListener {

    public int lastMessage = 0;

    @Subscribe
    public void listen(TestEvent event) {
        lastMessage = event.getMessage();
        System.out.println("Got message:" + lastMessage);
    }

    @Subscribe
    public void listenNumber(Number number) {
        System.out.println("Got event number:" + number);
    }

    @Subscribe
    public void listenLong(Long event) {
        System.out.println("Got event Long:" + event);
    }

    public int getLastMessage() {
        return lastMessage;
    }

}

class TestEvent {

    private final int message;

    public TestEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

}

