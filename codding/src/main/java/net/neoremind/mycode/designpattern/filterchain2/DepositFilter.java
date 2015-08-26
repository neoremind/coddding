package net.neoremind.mycode.designpattern.filterchain2;

/**
 * Concrete implementation of filter
 * This checks for the deposit code
 */
public class DepositFilter extends AbstractFilter {

    @Override
    public String execute(Order order) {
        String result = super.execute(order);
        if (order.getDepositNumber() == null || order.getDepositNumber().isEmpty()) {
            return result + "Invalid deposit number! ";
        } else {
            return result;
        }
    }
}
