package net.neoremind.mycode.designpattern.simplexec;

/**
 * Created by helechen on 2017/1/7.
 */
public interface Closure<T> {

    /**
     * Performs an action on the specified input object.
     *
     * @param input the input to execute on
     */
    void execute(T input);

}
