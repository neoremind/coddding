package net.neoremind.mycode.designpattern.filterchain2;

/**
 * Concrete implementation of filter
 * This filter is responsible for checking/filtering the input in the address field.
 */
public class AddressFilter extends AbstractFilter {

    @Override
	public String execute(Order order) {
		System.out.println("enter" + this.getClass().getName());
        String result = super.execute(order);
		System.out.println("super done " + this.getClass().getName());
		if (order.getAddress() == null || order.getAddress().isEmpty()) {
			return result + "Invalid address! ";
		} else {
			return result;
		}
    }
}
