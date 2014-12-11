package net.neoremind.mycode.vo;

import java.util.List;

public class Car extends Machine {

	private String brand;

	private int[] array;

	private List<Integer> list;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int[] getArray() {
		return array;
	}

	public void setArray(int[] array) {
		this.array = array;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

}
