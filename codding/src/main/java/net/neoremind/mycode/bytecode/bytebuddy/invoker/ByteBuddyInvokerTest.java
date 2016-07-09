package net.neoremind.mycode.bytecode.bytebuddy.invoker;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.neoremind.mycode.bytecode.bytebuddy.Echo;

/**
 * @author zhangxu
 */
public class ByteBuddyInvokerTest {

    @Test
    public void testDynamicMethodCall() throws Exception {
        Class/*<? extends Echo>*/ dynamicUserType = new ByteBuddy()
                .subclass(Echo.class)
                .implement(ObjectInvokerAccessor.class).intercept(FieldAccessor.ofBeanProperty())
                .method(not(isDeclaredBy(Object.class)))
                .intercept(MethodDelegation.to(DynamicDelegator.class))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();

        InstanceCreator factory = new ByteBuddy()
                .subclass(InstanceCreator.class)
                .method(not(isDeclaredBy(Object.class)))
                .intercept(MethodDelegation.toConstructor(dynamicUserType))
                .make()
                .load(dynamicUserType.getClassLoader())
                .getLoaded().newInstance();

        Echo echo = (Echo) factory.makeInstance();
        ((ObjectInvokerAccessor) echo).setObjectInvoker(new InvokerTester());
        System.out.println(echo.echo("123"));
        assertThat(echo.echo("123"), is("this is test"));
    }

}
