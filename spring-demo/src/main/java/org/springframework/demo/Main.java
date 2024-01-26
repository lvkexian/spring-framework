package org.springframework.demo;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.demo.domain.MyTestBean;

import java.util.concurrent.CompletableFuture;

/**
 * className ${NAME}
 * description TODO 类描述
 *
 * @author lvkexian
 * @date ${DATE}
 **/
@SuppressWarnings("deprecation")
public class Main {

	public static void main(String[] args) {
//		BeanFactory bf = new ClassPathXmlApplicationContext("beanFactoryTest.xml");
//		MyTestBean bean = (MyTestBean) bf.getBean("myTestBean");
//		System.out.println(bean.getTestStr());

		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext("org.springframework.demo");
//		ac.register(MyTestBean.class);
//		ac.refresh();
		MyTestBean bean1 = (MyTestBean) ac.getBean("myTestBean");
		System.out.println(bean1.getTestStr());

//		CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
//			String a = "result";
//			return a;
//		});
	}
}