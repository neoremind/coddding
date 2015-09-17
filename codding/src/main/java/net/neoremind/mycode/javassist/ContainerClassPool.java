package net.neoremind.mycode.javassist;

import javassist.ClassClassPath;
import javassist.ClassPool;

/**
 * Created by pippo on 15/7/8.
 */
public class ContainerClassPool {

    static ClassPool pool = ClassPool.getDefault();

    static {
        ClassClassPath classPath = new ClassClassPath(ContainerClassPool.class);
        pool.insertClassPath(classPath);
    }

    /**
     * javaassist 默认的classpool默认搜索路径是使用jvm的System ClassLoader的ClassPath
     * 当在容器中启动时会搜索不到lib
     *
     * @return 已经添加了容器lib路径的javaassist pool
     */
    public static ClassPool getDefault() {
        return pool;
    }

}
