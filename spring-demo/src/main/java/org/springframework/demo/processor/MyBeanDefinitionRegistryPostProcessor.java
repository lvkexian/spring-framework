package org.springframework.demo.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.demo.domain.MyTestBean;
import org.springframework.stereotype.Component;

/**
 * className MyBeanDefinitionRegistryPostProcessor
 * description BeanDefinition 后置处理
 *
 * @author lvkexian
 * @date 2024/1/17
 **/
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("MyBeanDefinitionRegistryPostProcessor.postProcessBeanFactory");
		BeanDefinition beanDefinition = beanFactory.getBeanDefinition("test");
		beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println("MyBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry");
		BeanDefinition beanDefinition = new RootBeanDefinition(MyTestBean.class);
		registry.registerBeanDefinition("test", beanDefinition);
	}
}
