package com.quxiqi.bysj.dao;

import com.quxiqi.bysj.common.SpringBeanInterface;

public interface BaseDao extends SpringBeanInterface{
	/**
	 * 数据库CRDU的通用方法。
	 * @param s sql映射关系，期待的值为：mapper类名.方法名
	 * @param obj mapper接口方法入参
	 * @param i 入参类型
	 * @param r mapper接口返回值类型
	 * @return 返回mapper接口方法返回值
	 * @throws Exception
	 */
	<R, I> R execute(String s, Object obj, Class<I> i,Class<R> r) throws Exception; 

}
