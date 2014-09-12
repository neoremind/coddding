package net.neoremind.mycode.vo;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Company {

	private Integer id;

	private String name;

	private Date establishTime;

	private List<Department> departmentList;
	
	private Department[] departmentArray;
	
	private BigInteger[] bigInt;
	
	private Map<Company, Department> departmentMap;

	public Company() {
		super();
	}
	
	public Company(Integer id, String name, Date establishTime, List<Department> departmentList) {
		super();
		this.id = id;
		this.name = name;
		this.establishTime = establishTime;
		this.departmentList = departmentList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public Date getEstablishTime() {
		return establishTime;
	}

	public void setEstablishTime(Date establishTime) {
		this.establishTime = establishTime;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", id)
		.append("name", name)
		.append("establishTime", establishTime)
		.append("departmentList", departmentList)
		.toString();
	}
	
}
