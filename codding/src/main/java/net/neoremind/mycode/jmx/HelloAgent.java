package net.neoremind.mycode.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * 使用如下命令开启JMX监控。
 * <pre>
 * #!/bin/sh
 *
 * java -Dcom.sun.management.jmxremote \
 * -Dcom.sun.management.jmxremote.port=1617 \
 * -Dcom.sun.management.jmxremote.authenticate=false \
 * -Dcom.sun.management.jmxremote.ssl=false \
 * SimpleAgent
 * </pre>
 * 直接打开<tt>jconsole</tt>在MBean中可以执行{@link HelloMBean#printHello()}方法，
 * 执行{@link HelloMBean#setName(String)}设置name值。
 */
public class HelloAgent {

    private MBeanServer mbs = null;

    public HelloAgent() {

        // Get the platform MBeanServer
        mbs = ManagementFactory.getPlatformMBeanServer();

        // Unique identification of MBeans
        Hello helloBean = new Hello();
        ObjectName helloName = null;

        try {
            // Uniquely identify the MBeans and register them with the platform MBeanServer
            helloName = new ObjectName("FOO:name=HelloBean");
            mbs.registerMBean(helloBean, helloName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility method: so that the application continues to run
    private static void waitForEnterPressed() {
        try {
            System.out.println("Press  to continue...");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String argv[]) throws Exception {
        HelloAgent agent = new HelloAgent();
        System.out.println("SimpleAgent is running...");
        HelloAgent.waitForEnterPressed();
    }
}
