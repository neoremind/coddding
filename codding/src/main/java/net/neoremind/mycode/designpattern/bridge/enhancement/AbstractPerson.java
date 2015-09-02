package net.neoremind.mycode.designpattern.bridge.enhancement;

import net.neoremind.mycode.designpattern.bridge.Road;

/**
 * @author zhangxu
 */
public abstract class AbstractPerson implements Person {

    protected Road road;

    public AbstractPerson(Road road) {
        this.road = road;
    }

    @Override
    public void work() {
        System.out.print("开着");
    }

}
