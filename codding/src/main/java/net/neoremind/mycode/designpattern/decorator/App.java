package net.neoremind.mycode.designpattern.decorator;

import org.junit.Test;

/**
 * 装饰模式：装饰模式是在不必改变原类文件和不使用继承的情况下，而是使用组合的方式动态的扩展一个对象的功能。
 * 它是通过创建一个包装对象，也就是装饰来包裹真实的对象。
 * <p/>
 * 与继承的对比：
 * <ul>
 * <li>扩展特定对象 vs 扩展某类对象</li>
 * <li>不需要子类 vs 需要子类，不容易维护，太多层次继承比较混乱</li>
 * <li>动态运行时分配职责 vs 编译时就分配好职责</li>
 * <li>可扩展、灵活 vs :-(</li>
 * </ul>
 *
 * @author zhangxu
 */
public class App {

    @Test
    public void testDecorate() {
        Component c = new ConcreteComponent();
        c.execute();

        Component d = new PlayDecorator(new ConcreteComponent());
        d.execute();
    }

}
