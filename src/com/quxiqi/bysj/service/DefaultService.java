package com.quxiqi.bysj.service;

import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.util.Property;

public abstract class DefaultService {
	public static final String UPLOAD_PATH; 
	static{
		Property pp = new Property("config/config.properties");
		UPLOAD_PATH = pp.getValue("uploadPath");
	}
	protected BaseDao baseDao;
	protected final ThreadLocal<User> userLocal = new ThreadLocal<>();
	public BaseDao getBaseDao() {
		return baseDao;
	}
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	public User getUser() {
		return userLocal.get();
	}
	public void setUser(User user) {
		userLocal.set(user);
	}
}
