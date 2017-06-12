package com.quxiqi.bysj.util;

import java.lang.reflect.Constructor;

public class MyBeanFactory {
	
	public static <T> T getBean(Class<T> c) throws Exception {
		
		Constructor<T> constructor = c.getConstructor();
		
		return constructor.newInstance();
	}
}
