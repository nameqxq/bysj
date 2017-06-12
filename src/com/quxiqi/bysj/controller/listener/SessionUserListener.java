package com.quxiqi.bysj.controller.listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.bean.UserLog;
import com.quxiqi.bysj.common.SpringBeanInterface;
import com.quxiqi.bysj.dao.BaseDao;

public class SessionUserListener implements HttpSessionAttributeListener {
	private static BaseDao baseDao = 
			(BaseDao) SpringBeanInterface.APPLICATION_CONTEXT.getBean("baseDao");

	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		UserListenerHandler(arg0);
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		UserListenerHandler(arg0);
	}
	
	private void UserListenerHandler(HttpSessionBindingEvent arg0) {
		if("user".equals(arg0.getName())){
			User user = (User) arg0.getValue();
			try {
				UserLog ul = new UserLog();
				ul.setUserName(user.getUserName());
				baseDao.execute("UserLogMapper.update", ul, UserLog.class, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

}
