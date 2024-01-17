package org.springframework.demo.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * className MyBeanFactoryPostProcessor
 * description BeanFactory后置处理器
 *
 * @author lvkexian
 * @date 2024/1/17
 **/
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("MyBeanFactoryPostProcessor.postProcessBeanFactory");
		BeanDefinition beanDefinition = beanFactory.getBeanDefinition("test");
		beanDefinition.setLazyInit(true);
	}
}
