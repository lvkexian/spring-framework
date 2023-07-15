package org.springframework.demo;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.demo.domain.MyTestBean;

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
		BeanFactory bf = new ClassPathXmlApplicationContext("beanFactoryTest.xml");
		MyTestBean bean = (MyTestBean) bf.getBean("myTestBean");
		System.out.println(bean.getTestStr());
	}
}