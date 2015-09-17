package net.neoremind.mycode.javassist.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import net.neoremind.mycode.javassist.ContainerClassPool;
import net.neoremind.mycode.javassist.MethodExecutor;

/**
 * @author zhangxu
 */
public class MethodExecutorImpl implements MethodExecutor {

    private static Logger logger = LoggerFactory.getLogger(MethodExecutorImpl.class);

    public MethodExecutorImpl() {

    }

    public MethodExecutorImpl(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public void init() {
        logger.info("begin init method executor for method:[{}#{}]", target.getClass().getName(), method.getName());
        this.fastMethod = FastClass.create(target.getClass()).getMethod(method);
        try {
            this.delegate = createDelegate();
        } catch (Exception e) {
            logger.warn("create method proxy due to error,use reflect by default", e);
        }
        logger.info("finish init method executor for method:[{}#{}]", target.getClass().getName(), method.getName());
    }

    private Object target;
    private Method method;
    private FastMethod fastMethod;
    private MethodExecutor delegate;

    @Override
    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public <T> T execute(Object[] args) {

        try {
            return (T) (delegate != null ? delegate.execute(args) : reflect(args));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected <T> T reflect(Object[] args) throws Exception {
        return (T) method.invoke(target, args);
    }

    protected <T> T cglib(Object[] args) throws Exception {
        return (T) fastMethod.invoke(target, args);
    }

    protected MethodExecutor createDelegate() throws Exception {
        MethodExecutor executor = createDelegateClass().newInstance();
        executor.setTarget(target);
        return executor;
    }

    private Class<? extends MethodExecutor> createDelegateClass()
            throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ContainerClassPool.getDefault();
        String clazz = String.format("%s_%s_%s_ExecutorImpl",
                target.getClass().getName(),
                method.getName(),
                Arrays.hashCode(method.getParameterTypes()));

        CtClass classDeclaring = pool.getAndRename(Stub.class.getName(), clazz);
        classDeclaring.setSuperclass(pool.get(target.getClass().getName()));

        String field = String.format("public %s target;", target.getClass().getName());
        classDeclaring.addField(CtField.make(field, classDeclaring));
        logger.info("add field:[{}] to class:[{}]", field, clazz);

        String setTarget = String.format("{ this.target = (%s) $1;}", target.getClass().getName());
        classDeclaring.getDeclaredMethod("setTarget").setBody(setTarget);
        logger.info("add method:[{}] to class:[{}]", setTarget, clazz);

        StringBuilder execute = new StringBuilder("{")
                .append("System.out.println(")
                .append("$1")
                .append(");")
                .append("void".equals(method.getReturnType().getName()) ? "" : "return ")
                .append("this.target.")
                .append(method.getName())
                .append("(");

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            Class<?> argType = method.getParameterTypes()[i];
            execute.append(String.format("(%s) $1[%s]", argType.getName(), i));
            if (i != (method.getParameterTypes().length - 1)) {
                execute.append(",");
            }
        }

        execute.append(");").append("void".equals(method.getReturnType().getName()) ? "return null;}" : "}");
        logger.debug("add method:[{}] to class:[{}]", execute, clazz);
        classDeclaring.getDeclaredMethod("execute").setBody(execute.toString());
        //        classDeclaring.writeFile();
        logger.info("create method executor class:[{}]", clazz);

        return classDeclaring.toClass();
    }
}