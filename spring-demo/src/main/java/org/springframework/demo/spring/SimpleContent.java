package org.springframework.demo.spring;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于一级+二级缓存手写循环依赖：
 */
public class SimpleContent {
	// 一级缓存
	static Map<String, Object> m1 = new HashMap<>();
	// 二级缓存
	static Map<String, Object> m2 = new HashMap<>();

	public static void main(String[] args) throws Exception {
		// 创建dx
		Dx dx = create(Dx.class);
		Dy dy = create(Dy.class);
		System.out.println();
	}

	public static <T> T create(Class<T> beanClass) throws Exception {
		// 如果一级缓存存在 直接返回
		String beanName = beanClass.getSimpleName().toLowerCase();
		Object cache = m1.get(beanName);
		if (cache != null) {
			return (T) cache;
		}
		T bean = beanClass.newInstance();
		// 加入到二级缓存
		m2.put(beanName, bean);
		injection(bean);
		// 删除二级缓存
		m2.remove(beanName, bean);
		m1.put(beanName, bean);
		return bean;
	}

	public static void injection(Object bean) throws Exception {
		Class<?> aClass = bean.getClass();
		Field[] fields = aClass.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			String fieldClass = field.getType().getName();
			Class<?> aClass1 = Class.forName(fieldClass);
			Object cache = m1.get(field.getName());
			if (cache == null) {
				cache = m2.get(field.getName());
				if (cache == null) {
					cache = create(aClass1);
				}
			}
			Method method = aClass.getMethod(methodName, aClass1);
			method.invoke(bean, cache);
		}
	}
}
