package net.neoremind.mycode.designpattern.bridge;

/**
 * @author zhangxu
 */
public abstract class AbstractRoad implements Road {

    protected Car car;

    public AbstractRoad(Car car) {
        this.car = car;
    }

    @Override
    public void run() {
        System.out.println("行驶");
    }

}
