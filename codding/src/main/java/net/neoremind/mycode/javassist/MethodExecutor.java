package net.neoremind.mycode.javassist;

/**
 * @author zhangxu
 */
public interface MethodExecutor {

    /**
     * execute method
     *
     * @param args
     * @param <T>
     *
     * @return result
     */
    <T> T execute(Object[] args);

    /**
     * set target service
     *
     * @param target
     */
    void setTarget(Object target);

    /**
     * @return method call areument types
     */
    Class<?>[] getParameterTypes();

    class Stub implements MethodExecutor {

        @Override
        public <T> T execute(Object[] args) {
            return null;
        }

        @Override
        public void setTarget(Object target) {

        }

        @Override
        public Class<?>[] getParameterTypes() {
            return null;
        }
    }

}

