package net.neoremind.mycode.guice.app;

import org.springframework.beans.BeansException;

/**
 * Created by xu.zhang on 12/18/16.
 */
public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;

    <T> T getBean(String beanName, Class<T> clazz) throws BeansException;

    <T> T getBean(Class<T> clazz) throws BeansException;

//    Object getBean(String var1, Object... var2) throws BeansException;
//
//    <T> T getBean(Class<T> var1, Object... var2) throws BeansException;
//
//    boolean containsBean(String var1);
//
//    boolean isSingleton(String var1) throws NoSuchBeanDefinitionException;
//
//    boolean isPrototype(String var1) throws NoSuchBeanDefinitionException;
//
//    boolean isTypeMatch(String var1, Class<?> var2) throws NoSuchBeanDefinitionException;
//
//    Class<?> getType(String var1) throws NoSuchBeanDefinitionException;
//
//    String[] getAliases(String var1);
}
