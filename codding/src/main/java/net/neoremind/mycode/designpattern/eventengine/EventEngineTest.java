package net.neoremind.mycode.designpattern.eventengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class EventEngineTest {

    private EventEngine engine = new EventEngine();
    private List<String> down_flags = new ArrayList<>();
    private List<String> up_flags = new ArrayList<>();

    @Before
    public void init() {

    }

    @Test
    public void passThrough() {
        down_flags.clear();
        up_flags.clear();
        engine.getStack()
                .add(new EchoEventHandler("f"))
                .add(new EchoEventHandler("e"))
                .add(new EchoEventHandler("d"))
                .add(new EchoEventHandler("c"))
                .add(new EchoEventHandler("b"))
                .add(new EchoEventHandler("a"));
        System.out.println(engine.getStack());
        engine.process(new TestEvent());

        System.out.println(Arrays.toString(down_flags.toArray()));
        Assert.assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f"}, down_flags.toArray());

        System.out.println(Arrays.toString(up_flags.toArray()));
        Assert.assertArrayEquals(new String[] {"f", "e", "d", "c", "b", "a"}, up_flags.toArray());
    }

    @Test
    public void end() {
        down_flags.clear();
        up_flags.clear();
        engine.getStack()
                .add(new EndEventHandler("f"))
                .add(new EchoEventHandler("e"))
                .add(new EchoEventHandler("d"))
                .add(new EchoEventHandler("c"))
                .add(new EchoEventHandler("b"))
                .add(new EchoEventHandler("a"));

        engine.process(new TestEvent());

        System.out.println(Arrays.toString(down_flags.toArray()));
        Assert.assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f"}, down_flags.toArray());

        System.out.println(Arrays.toString(up_flags.toArray()));
        Assert.assertArrayEquals(new String[] {"f", "e", "d", "c", "b", "a"}, up_flags.toArray());
    }

    @Test
    public void interrupt() {
        engine.getStack()
                .add(new EchoEventHandler("f"))
                .add(new EchoEventHandler("e"))
                .add(new EndEventHandler("d"))
                .add(new EchoEventHandler("c"))
                .add(new EchoEventHandler("b"))
                .add(new EchoEventHandler("a"));

        engine.process(new TestEvent());

        System.out.println(Arrays.toString(down_flags.toArray()));
        Assert.assertArrayEquals(new String[] {"a", "b", "c", "d"}, down_flags.toArray());

        System.out.println(Arrays.toString(up_flags.toArray()));
        Assert.assertArrayEquals(new String[] {"d", "c", "b", "a"}, up_flags.toArray());
    }

    private class EchoEventHandler extends EventHandlerAdapter {

        public EchoEventHandler(String name) {
            this.name = name;
        }

        private String name;

        @Override
        public boolean accept(Event event) {
            return true;
        }

        @Override
        public void downstream(EventHandlerContext context, Event event) throws Throwable {
            System.out.println("downstream: " + name);
            down_flags.add(name);
            context.passThrough(event);
        }

        @Override
        public void upstream(EventHandlerContext context, Event event) throws Throwable {
            System.out.println("upstream: " + name);
            up_flags.add(name);
            context.passThrough(event);
        }
    }

    private class EndEventHandler extends EventHandlerAdapter {

        public EndEventHandler(String name) {
            this.name = name;
        }

        private String name;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean accept(Event event) {
            return true;
        }

        @Override
        public void downstream(EventHandlerContext context, Event event) throws EventHandleException {
            System.out.println("downstream: " + name);
            down_flags.add(name);
        }

        @Override
        public void upstream(EventHandlerContext context, Event event) throws EventHandleException {
            System.out.println("upstream: " + name);
            up_flags.add(name);
        }
    }

    private class TestEvent implements Event<String> {

        @Override
        public int getType() {
            return 0;
        }

        @Override
        public String getPayload() {
            return "test";
        }

        @Override
        public void setPayload(String s) {

        }
    }

}