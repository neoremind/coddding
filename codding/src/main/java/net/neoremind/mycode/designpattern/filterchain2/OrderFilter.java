package net.neoremind.mycode.designpattern.filterchain2;

/**
 * Concrete implementation of filter This checks for the order field
 */
public class OrderFilter extends AbstractFilter {

    @Override
    public String execute(Order order) {
        String result = super.execute(order);
        if (order.getOrder() == null || order.getOrder().isEmpty()) {
            return result + "Invalid order! ";
        } else {
            return result;
        }
    }
}
