package net.neoremind.mycode.designpattern.decorator;

/**
 * @author zhangxu
 */
public class SingDecorator extends Decorator implements Component {

    public SingDecorator(Component component) {
        super(component);
    }

    @Override
    public void execute() {
        System.out.println("I am singing");
        super.execute();
    }

}
