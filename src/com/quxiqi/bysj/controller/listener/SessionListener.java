package com.quxiqi.bysj.controller.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.bean.UserLog;
import com.quxiqi.bysj.common.SpringBeanInterface;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.dao.impl.BaseDaoImpl;

public class SessionListener implements HttpSessionListener {
	private static BaseDao baseDao = 
			SpringBeanInterface.APPLICATION_CONTEXT.getBean(BaseDaoImpl.class);

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		User user = (User)session.getAttribute("user");
		if(user==null)
			return;
		try {
			UserLog ul = new UserLog();
			ul.setUserName(user.getUserName());
			baseDao.execute("UserLogMapper.update", ul, UserLog.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
