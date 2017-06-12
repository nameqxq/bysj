package com.quxiqi.bysj.dao;

public abstract class AbstractDao implements BaseDao {
	/**
	 * 用于将传入的简易sqlName替换为真正的sql映射名
	 * @param s 传入dao的sqlName
	 * @return 0号位置为mapper映射全类名，1号位置为对应方法名
	 */
	protected String[] getSqlName(String s){
		String[] sqlNames = new String[2];
		String[] split = s.split("[.]");
		sqlNames[0]="com.quxiqi.bysj.dao.mapper."+split[0];
		sqlNames[1]=split[1];
		return sqlNames;
	}
}
