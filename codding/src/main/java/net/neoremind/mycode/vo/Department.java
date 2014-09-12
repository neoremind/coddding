package net.neoremind.mycode.vo;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class Department {

	private Integer id;

	private String name;

	public Department() {
		
	}
	
	public Department(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", id)
		.append("name", name)
		.toString();
	}

}

