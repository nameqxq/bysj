package com.quxiqi.bysj.service;

public abstract class AbstractService implements BaseService{
	/**
	 * 处理serviceKey，返回包含service全类名及方法名的字符串数组
	 * @param s serviceKey
	 * @return 
	 */
	protected static String[] getServiceName(String s){
		String[] serviceNames = new String[2];
		String[] split = s.split("[.]");
		serviceNames[0]="com.quxiqi.bysj.service.impl."+split[0];
		serviceNames[1]=split[1];
		return serviceNames;
	}
}
