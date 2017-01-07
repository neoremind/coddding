package net.neoremind.mycode.guice.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.springframework.beans.BeansException;

/**
 * Created by xu.zhang on 12/18/16.
 */
public class GuiceBeanFactory implements BeanFactory {

    private Injector injector;

    public GuiceBeanFactory(Module module) {
        injector = Guice.createInjector(module);
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) throws BeansException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws BeansException {
        return injector.getInstance(clazz);
    }
}
