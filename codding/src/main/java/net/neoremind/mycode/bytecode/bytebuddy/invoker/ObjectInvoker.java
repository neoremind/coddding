package net.neoremind.mycode.bytecode.bytebuddy.invoker;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author zhangxu
 */
public interface ObjectInvoker extends Serializable {

    Object invoke(Object proxy, Method method, Object... arguments) throws Throwable;

}
