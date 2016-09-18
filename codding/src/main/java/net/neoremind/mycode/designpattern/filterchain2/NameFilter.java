package net.neoremind.mycode.designpattern.filterchain2;

/**
 * Concrete implementation of filter This filter checks if the input in the Name
 * field is valid. (alphanumeric)
 */
public class NameFilter extends AbstractFilter {

    @Override
    public String execute(Order order) {
        System.out.println("enter" + this.getClass().getName());
        String result = super.execute(order);
        System.out.println("super done " + this.getClass().getName());
        if (order.getName() == null || order.getName().isEmpty() || order.getName().matches(".*[^\\w|\\s]+.*")) {
            return result + "Invalid order! ";
        } else {
            return result;
        }
    }
}
