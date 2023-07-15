package com.springframework.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.demo.domain.MyTestBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * className BeanFactoryTest
 * description TODO 类描述
 *
 * @author lvkexian
 * @date 2023/7/15
 **/
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
public class BeanFactoryTest {

	@Test
	public void testSimpleLoad(){
		BeanFactory bf = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));
		MyTestBean bean=(MyTestBean) bf.getBean("myTestBean");
		assertEquals("testStr",bean.getTestStr());
	}
}