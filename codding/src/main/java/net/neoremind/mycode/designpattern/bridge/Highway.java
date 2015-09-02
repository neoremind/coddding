package net.neoremind.mycode.designpattern.bridge;

/**
 * @author zhangxu
 */
public class Highway extends AbstractRoad implements Road {

    public Highway(Car car) {
        super(car);
    }

    @Override
    public void run() {
        car.drive();
        System.out.print("在高速公路");
        super.run();
    }
}
