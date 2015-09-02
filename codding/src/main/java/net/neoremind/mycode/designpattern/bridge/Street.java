package net.neoremind.mycode.designpattern.bridge;

/**
 * @author zhangxu
 */
public class Street extends AbstractRoad implements Road {

    public Street(Car car) {
        super(car);
    }

    @Override
    public void run() {
        car.drive();
        System.out.print("在街道");
        super.run();
    }

}
