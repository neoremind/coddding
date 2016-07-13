package net.neoremind.mycode.jmx;

/**
 * @author zhangxu
 */
public interface HelloMBean {

     String getName();
     void setName(String name);
     void printHello();
     void printHello(String whoName);
}
