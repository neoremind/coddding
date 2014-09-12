package net.neoremind.mycode.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import net.neoremind.mycode.vo.Company;

public class PrintAllAttributes {

	public static void main(String[] args) throws Exception {
		Company model = new Company();
		getBeanDefination(model.getClass());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void getBeanDefination(Class<T> clazz) throws Exception {
		// 根据属性名取得该属性对应的Field对象
		for (Field mapField : clazz.getDeclaredFields()) {
			// 示范第一个方法：直接通过getType()取出Field的类型，只对普通类型的Field有效
			System.out.println("属性名为：" + mapField.getName());
			Class<?> class2 = mapField.getType();
			// 输出查看
			System.out.println("属性类型为：" + class2);

			// 示范第二种方法：
			Type mapMainType = mapField.getGenericType();
			// 为了确保安全转换，使用instanceof
			if (mapMainType instanceof ParameterizedType) {
				// 执行强制类型转换
				ParameterizedType parameterizedType = (ParameterizedType) mapMainType;
				// 获取基本类型信息，即Map
				// Type basicType = parameterizedType.getRawType();
				// System.out.println("基本类型为：" + basicType);
				// 获取泛型类型的泛型参数
				Type[] types = parameterizedType.getActualTypeArguments();
				for (int i = 0; i < types.length; i++) {
					Class gc = getGenericClass(parameterizedType, i);
					System.out.println("第" + (i + 1) + "个泛型类型是：" + gc.getName());
					// System.out.println(c.getCanonicalName());
					getBeanDefination(gc);
				}

			}
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private static Class getClass(Type type, int i) {
		if (type instanceof ParameterizedType) { // 处理泛型类型
			return getGenericClass((ParameterizedType) type, i);
		} else if (type instanceof TypeVariable) {
			return (Class) getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
		} else {// class本身也是type，强制转型
			return (Class) type;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private static Class getGenericClass(ParameterizedType parameterizedType,
			int i) {
		Object genericClass = parameterizedType.getActualTypeArguments()[i];
		if (genericClass instanceof ParameterizedType) { // 处理多级泛型
			return (Class) ((ParameterizedType) genericClass).getRawType();
		} else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
			return (Class) ((GenericArrayType) genericClass)
					.getGenericComponentType();
		} else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
			return (Class) getClass(
					((TypeVariable) genericClass).getBounds()[0], 0);
		} else {
			return (Class) genericClass;
		}
	}

}
