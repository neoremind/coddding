package net.neoremind.mycode.javassist;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import net.neoremind.mycode.javassist.impl.HelloServiceImpl;
import net.neoremind.mycode.javassist.impl.MethodExecutorImpl;

/**
 * @author zhangxu
 */
public class MethodExecutorTest {

    @Test
    public void testExecute() throws Exception {
        Method method = ReflectionUtils.findMethod(HelloService.class, "hi", String.class);
        MethodExecutorImpl methodExecutor = new MethodExecutorImpl(new HelloServiceImpl(), method);
        methodExecutor.init();
        String result = methodExecutor.execute(new Object[] {"hehe"});
        assertThat(result, is("hehe"));
    }

    @Test
    public void testProxyFactory() throws Exception {
        // 实例化代理类工厂
        ProxyFactory factory = new ProxyFactory();

        //设置父类，ProxyFactory将会动态生成一个类，继承该父类
        factory.setSuperclass(HelloServiceImpl.class);

        //设置过滤器，判断哪些方法调用需要被拦截
        factory.setFilter(new MethodFilter() {
            @Override
            public boolean isHandled(Method m) {
                return true;
            }
        });

        Class<?> clazz = factory.createClass();
        HelloService proxy = (HelloService) clazz.newInstance();
        ((ProxyObject) proxy).setHandler(new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                //拦截后前置处理，改写name属性的内容
                //实际情况可根据需求修改
                System.out.println(thisMethod.getName() + "被调用");
                try {
                    Object ret = proceed.invoke(self, args);
                    System.out.println("返回值: " + ret);
                    return ret;
                } finally {
                    System.out.println(thisMethod.getName() + "调用完毕");
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            String result = proxy.hi("hehe");
            System.out.println(result);
            System.out.println(System.nanoTime() - start);
        }
    }
}