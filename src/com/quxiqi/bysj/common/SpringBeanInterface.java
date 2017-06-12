package com.quxiqi.bysj.common;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public interface SpringBeanInterface {
	ClassPathXmlApplicationContext APPLICATION_CONTEXT 
		= new ClassPathXmlApplicationContext("springBean.xml","springMvc.xml");
}
