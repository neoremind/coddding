package net.neoremind.mycode.guice.order;


public interface OrderService {

    void add(Order order);

    void remove(Order order);

    Order get(int id);
}
