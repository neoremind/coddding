package net.neoremind.mycode.guava.reflection;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertThat;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.hamcrest.Matchers;

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class AbstractInvocationHandlerTest extends TestCase {

    public void test() {
        SomeAPI<String, Integer> someAPI = new SomeAPI<String, Integer>() {
            @Override
            public Integer call(String s) {
                return s.length();
            }
        };
        SomeAPI<String, Integer> delegatingAPI = newDelegatingAPI(someAPI);
        assertThat(delegatingAPI.call("abc"), Matchers.is(3));
        assertThat(delegatingAPI.toString(), Matchers.is("some arbitrary string"));

        SomeAPI<String, Integer> mockRpcAPI = newMockRpcAPI();
        assertThat(mockRpcAPI.call("abc"), Matchers.is(3));
    }

    private <REQ, RES> SomeAPI<REQ, RES> newDelegatingAPI(SomeAPI<REQ, RES> delegate) {
        return Reflection.newProxy(SomeAPI.class, new DelegatingInvocationHandler(delegate));
    }

    private <REQ, RES> SomeAPI<REQ, RES> newMockRpcAPI() {
        return Reflection.newProxy(SomeAPI.class, new MockRPCInvocationHandler());
    }

    private static class DelegatingInvocationHandler extends AbstractInvocationHandler
            implements Serializable {
        final Object delegate;

        DelegatingInvocationHandler(Object delegate) {
            this.delegate = checkNotNull(delegate);
        }

        @Override
        protected Object handleInvocation(Object proxy, Method method, Object[] args)
                throws Throwable {
            System.out.println("I'm here");
            return method.invoke(delegate, args);
        }

        @Override
        public String toString() {
            return "some arbitrary string";
        }
    }

    private static class MockRPCInvocationHandler extends AbstractInvocationHandler
            implements Serializable {

        @Override
        protected Object handleInvocation(Object proxy, Method method, Object[] args)
                throws Throwable {
            System.out.println(proxy);
            System.out.println(method.getName());
            System.out.println(ArrayUtils.toString(args));
            return ((String) args[0]).length();
        }

    }

    interface SomeAPI<REQ, RES> {

        RES call(REQ req);
    }

}
