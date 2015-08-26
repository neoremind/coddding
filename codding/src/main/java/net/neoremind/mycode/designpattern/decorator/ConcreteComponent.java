package net.neoremind.mycode.designpattern.decorator;

/**
 * @author zhangxu
 */
public class ConcreteComponent implements Component {

    @Override
    public void execute() {
        System.out.println("I am concrete!");
    }

}
