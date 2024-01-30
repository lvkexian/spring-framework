package org.springframework.demo.spring;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 一级+二级+三级缓存实现aop+性能优化
 */
public class EnhancerContent {
	// 一级缓存
	static Map<String, Object> m1 = new HashMap<>();
	// 二级缓存
	static Map<String, Object> m2 = new HashMap<>();
	// 三级缓存
	static Map<String, ObjectFactory<Object>> m3 = new HashMap<>();

	public static void main(String[] args) throws Exception {
		// 创建dx
		EnhancerContent content = new EnhancerContent();
		Dx dx = content.create(Dx.class);
		Dy dy = content.create(Dy.class);
		System.out.println(dx.getDy().getDx() == dy.getDx());
	}

	public <T> T create(Class<T> beanClass) throws Exception {
		// 如果一级缓存存在 直接返回
		String beanName = beanClass.getSimpleName().toLowerCase();
		Object cache = getSingleton(beanName);
		if (cache != null) {
			return (T) cache;
		}
		T bean = beanClass.newInstance();
		// 加入到三级缓存
		m3.put(beanName, () -> {
			return aop(bean);
		});
		T exposedObject = bean;
		injection(bean);
		exposedObject = initializeBean(bean);
		Object earlySingletonReference = getSingleton(beanName);

		if (exposedObject == bean) {
			exposedObject = (T) earlySingletonReference;
		} else {
			// 如果被别的bean依赖则报错

		}
		// 删除二级缓存和三级缓存
		if (m2.containsKey(beanName)) {
			m2.remove(beanName);
		}
		if (m3.containsKey(beanName)) {
			m3.remove(beanName);
		}
		// 进入一级缓存
		m1.put(beanName, bean);
		return exposedObject;
	}

	public <T> void injection(T bean) throws Exception {
		Class<?> clazz = bean.getClass();
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
			Object cache = getSingleton(field.getName());
			if (cache == null) {
				cache = create(aClass1);
			}
			Method method = clazz.getMethod(methodName, aClass1);
			method.invoke(bean, cache);
		}
	}

	/**
	 * 模拟bean初始化流程
	 *
	 * @param bean
	 * @param <T>
	 * @return
	 */
	public <T> T initializeBean(T bean) {
		//这个过程中可能被修改或者代理
		return bean;
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

	public Object getSingleton(String beanName) {
		Object bean = m1.get(beanName);
		if (bean != null) {
			return bean;
		}
		bean = m2.get(beanName);
		if (bean != null) {
			return bean;
		}
		ObjectFactory<Object> objectObjectFactory = m3.get(beanName);
		if (objectObjectFactory != null) {
			bean = objectObjectFactory.getObject();
			m3.remove(beanName);
			m2.put(beanName, bean);
			return bean;
		}
		return null;
	}
}
