package org.springframework.demo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 一级+二级缓存实现AOP
 */
public class AopContent {
	// 一级缓存
	static Map<String, Object> m1 = new HashMap<>();
	// 二级缓存
	static Map<String, Object> m2 = new HashMap<>();

	public static void main(String[] args) throws Exception {
		// 创建dx
		AopContent aopContent = new AopContent();
		Dx dx = aopContent.create(Dx.class);
		Dy dy = aopContent.create(Dy.class);
		System.out.println(dy.getDx().getDy().getDx());
	}

	public <T> T create(Class<T> beanClass) throws Exception {
		// 如果一级缓存存在 直接返回
		String beanName = beanClass.getSimpleName().toLowerCase();
		Object cache = m1.get(beanName);
		if (cache != null) {
			return (T) cache;
		}
		T bean = beanClass.newInstance();
		// 执行aop之后进缓存
		bean = (T) aop(bean);
		// 加入到二级缓存
		m2.put(beanName, bean);
		injection(bean);
		// 删除二级缓存
		m2.remove(beanName, bean);
		m1.put(beanName, bean);
		return bean;
	}

	public <T> void injection(T bean) throws Exception {
		Class<?> clazz = bean.getClass().getSuperclass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Autowired annotationsByType = field.getAnnotation(Autowired.class);
			if (annotationsByType == null) {
				continue;
			}
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
			Method method = clazz.getMethod(methodName, aClass1);
			method.invoke(bean, cache);
		}
	}

	public Object aop(Object bean) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(bean.getClass());
		enhancer.setCallback(new MyMethodInterceptor(bean));
		Object proxyObj = enhancer.create();
		// 返回代理对象
		return proxyObj;
	}

	class MyMethodInterceptor implements MethodInterceptor {
		Object obj;

		public MyMethodInterceptor(Object obj) {
			this.obj = obj;
		}

		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			if ("print".equals(method.getName())) {
				System.out.println("aop after");
			}
			Object invoke = method.invoke(obj, objects);
			if ("print".equals(method.getName())) {
				System.out.println("aop before");
			}
			return invoke;
		}
	}
}
