package net.neoremind.mycode.designpattern.decorator;

/**
 * @author zhangxu
 */
public class Decorator implements Component {

    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void execute() {
        this.component.execute();
    }

}
