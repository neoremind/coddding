package net.neoremind.mycode.bytecode.bytebuddy.invoker;

import java.lang.reflect.Method;

/**
 * @author zhangxu
 */
public class InvokerTester implements ObjectInvoker {

    private static final long serialVersionUID = -8586595308078627409L;

    private Object method;
    private Object[] args;
    private Object proxy;

    @Override
    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        System.out.println("Calling " + this.getClass().getSimpleName());
        System.out.println("proxy=" + proxy);
        System.out.println("method=" + method);
        System.out.println("args=" + args);
        return "this is test";
    }
}
