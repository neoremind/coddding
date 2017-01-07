package net.neoremind.mycode.guice.order;

import java.util.Date;

public class Order {

    private int id;

    private String customer;

    private Date createDate;

    public Order(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return String.format("Order{id=%d, customer='%s', createDate=%s}", id, customer, createDate);
    }
}
