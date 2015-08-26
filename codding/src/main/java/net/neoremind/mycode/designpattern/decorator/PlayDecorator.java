package net.neoremind.mycode.designpattern.decorator;

/**
 * @author zhangxu
 */
public class PlayDecorator extends Decorator implements Component {

    public PlayDecorator(Component component) {
        super(component);
    }

    @Override
    public void execute() {
        System.out.println("I am playing");
        super.execute();
    }

}
