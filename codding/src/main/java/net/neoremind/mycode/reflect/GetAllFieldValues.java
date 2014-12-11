package net.neoremind.mycode.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.neoremind.mycode.vo.Company;

public class GetAllFieldValues {

	public static void main(String[] args) throws Exception {
//		Car c = new Car();
//		c.setId(1);
//		c.setNew(true);
//		c.setBrand("bmw");
//		c.setArray(new int[]{1,2,3});
//		c.setList(Lists.newArrayList(4,5,6));
		Company c = new Company();
		c.setId(1);
		c.setName("abc");
		printFieldValues(c);
	}

	public static <T> void printFieldValues(T obj) throws Exception {
		if (obj == null) {
			return;
		}
		//Class<?> clazz = Class.forName(obj.toString().split("@")[0]); 
		Class<?> clazz = obj.getClass();
		Class<?> superClazz = clazz.getSuperclass();
		if (!superClazz.getName().equals("java.lang.Object")) {
			throw new RuntimeException("Cannot work with object having custom super class");
		}
		Method[] mths = obj.getClass().getMethods();
		for (Method method : mths) {
			if (method.getName().startsWith("get")) {
				System.out.println(method);
			}
		}
		Field[] fields = obj.getClass().getFields(); // 获得对象方法集合
		String fieldName = null;
		Method method = null;
		for (Field field : fields) {
			fieldName = field.getName(); // 得到字段名
			method = clazz.getMethod("get" + getMethodSuffix(fieldName)); // 根据字段名找到对应的get方法，null表示无参数
			Object value = method.invoke(obj); // 调用该字段的get方法
			if (value != null) {
				System.out.println(fieldName + "\t" + value);
			}
		}
	}

	private static String getMethodSuffix(String fieldName) {
		if (fieldName == null) {
			throw new IllegalArgumentException("field name is null");
		}
		StringBuffer sb = new StringBuffer(fieldName);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

}
