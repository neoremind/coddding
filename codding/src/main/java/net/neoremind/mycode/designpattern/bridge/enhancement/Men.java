package net.neoremind.mycode.designpattern.bridge.enhancement;

import net.neoremind.mycode.designpattern.bridge.Road;

/**
 * @author zhangxu
 */
public class Men extends AbstractPerson implements Person {

    public Men(Road road) {
        super(road);
    }

    @Override
    public void work() {
        System.out.print("男人");
        super.work();
        road.run();
    }
}

