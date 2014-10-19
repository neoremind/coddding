package net.neoremind.mycode.guava;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class ObjectsTest {

	public static void main(String[] args) {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setName("abc");
		System.out.println(customer);
		
		Customer customer2 = new Customer();
		customer2.setId(1);
		customer2.setName("abc");
		
		System.out.println(customer.equals(customer2));
		
		Set<Customer> set = new HashSet<Customer>();
		set.add(customer);
		set.add(customer2);
		System.out.println(set.size());
	}

}

class Customer implements Comparable<Customer> {

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("name", name).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Customer) {
			Customer that = (Customer) obj;
			return Objects.equal(id, that.id) && Objects.equal(name, that.name);
		}
		return false;
	}

	@Override
	public int compareTo(Customer other) {
		return ComparisonChain.start().compare(id, other.id).compare(name, other.name).result();
	}

	private int id;

	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
