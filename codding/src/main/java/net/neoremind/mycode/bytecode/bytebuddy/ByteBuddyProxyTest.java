package net.neoremind.mycode.bytecode.bytebuddy;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.returns;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;

/**
 * @author zhangxu
 */
public class ByteBuddyProxyTest {

    @Test
    public void testSimple() throws Exception {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();

        assertThat(dynamicType.newInstance().toString(), is("Hello World!"));
    }

    @Test
    public void testSimple2() throws Exception {
        Object obj = new ByteBuddy()
                .subclass(Object.class)
                .name("example.Type")
                .method(named("toString")).intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
        assertThat(obj.toString(), is("Hello World!"));
    }

    @Test
    public void testSimple3() throws Exception {
        Source helloWorld = new ByteBuddy()
                .subclass(Source.class)
                .method(named("hello")).intercept(MethodDelegation.to(Target.class))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
        assertThat(helloWorld.hello("World"), is("Hello World!"));
    }

    @Test
    public void testInterceptor() throws Exception {
        MemoryDatabase loggingDatabase = new ByteBuddy()
                .subclass(MemoryDatabase.class)
                .method(named("load")).intercept(MethodDelegation.to(LoggerInterceptor.class))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
        assertThat(loggingDatabase.load("hello").size(), is(2));
    }

    @Test
    public void testFixedValueEcho() throws Exception {
        Echo echo = new ByteBuddy()
                .subclass(Echo.class)
                .name("ByteBuddy$1002")
                .method(named("echo")).intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
        assertThat(echo.echo("123"), is("Hello World!"));
    }

    @Test
    public void testFixedValueEcho2() throws Exception {
        Echo echo = new ByteBuddy()
                .subclass(Echo.class)
                .name("ByteBuddy$1003")
                .method(named("echo").and(returns(String.class)).and(takesArguments(1)))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
        assertThat(echo.echo("123"), is("Hello World!"));
    }

}
