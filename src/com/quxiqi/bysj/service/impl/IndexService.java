package com.quxiqi.bysj.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.quxiqi.bysj.bean.RegisterMsg;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.bean.UserLog;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.service.DefaultService;
import com.quxiqi.bysj.util.MessageHandler;
import com.quxiqi.bysj.util.ResultMsgUtil;
import com.quxiqi.bysj.util.StringUtil;
@Service
@Scope("prototype")
public class IndexService extends DefaultService{
	@Override
	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		super.setBaseDao(baseDao);
	}
	
	public ResultMsg login(User user,HttpSession session,String ip) throws Exception{
		Class<User> uClass = User.class;
		String psd = StringUtil.dnvString(user.getPassword());
		String userName = StringUtil.dnvString(user.getUserName());
		//匹配是否为手机号码
		boolean matches = userName.matches("^1[0-9]{10}$");
		if(matches){
			user.setUserName(null);
			user.setPhoneNumber(userName);
		}
		ResultMsg rm = ResultMsgUtil.getResultMsg();
		//账号/手机号不为空且密码不为空
		if(userName!=null&&!("").equals(userName)&&psd!=null&&!("").equals(psd)){
			User u = baseDao.execute("UserMapper.select", user, uClass, uClass);
			if(u==null){
				rm.setCode(ResultMsg.FAIL);
				rm.setReason("用户名或密码错误！");
			}else{
				UserLog ul = new UserLog();
				Class<UserLog> ulClass = UserLog.class;
				ul.setUserName(u.getUserName());
				ul.setOnlineFlag("1");
				//查询在线用户信息
				@SuppressWarnings("unchecked")
				List<UserLog> uls = baseDao.execute("UserLogMapper.select", ul, ulClass, List.class);
				if(!uls.isEmpty()){
					UserLog ul2 = uls.get(0);
					User us = (User) session.getAttribute("user");
					if(us!=null&&ul2.getUserName().equals(us.getUserName())){
						rm.setCode(ResultMsg.SUCCESS);
						rm.setData(us);
						rm.setReason("用户已登录，无需重新登录！");
					}else{
						rm.setCode(ResultMsg.FAIL);
						rm.setReason("该用户已在其他地方登录！");
					}
				}else{
					//将登陆用户信息存入session并新增登陆日志
					setUser(session, u,ip);
					rm.setCode(ResultMsg.SUCCESS);
					rm.setData(session.getAttribute("user"));
					rm.setReason("登陆成功！");
				}
			}
		}else{
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("用户名或密码不能为空！");
		}
		return rm;
	}
	//注册
	public ResultMsg register(User user,String code,HttpSession session,String ip) throws Exception{
		//后台校验，防止恶意用户篡改前台信息
		Class<User> uClass = User.class;
		ResultMsg rm = ResultMsgUtil.getResultMsg();
		String userName = user.getUserName();
		String phoneNumber = user.getPhoneNumber();
		String password = user.getPassword();
		if(userName==null||"".equals(userName)||!userName.matches("^[a-zA-Z][a-zA-Z0-9_]{2,16}$")||
				phoneNumber==null||"".equals(phoneNumber)||!phoneNumber.matches("^1[0-9]{10}$")||
				password==null||"".equals(password)||!password.matches("^[a-zA-Z0-9_]{3,16}$")){
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("用户名、密码或手机号格式错误！");
			return rm;
		}
		//以下验证验证码
		RegisterMsg registerMsg = MessageHandler.getInstance().getRegisterMsg(phoneNumber);
		boolean flag = true;
		if(code==null||code.length()!=6){
			flag = false;
			rm.setReason("验证码格式错误！");
		}else if(registerMsg==null){
			flag = false;
			rm.setReason("尚未向该手机号发送验证码！");
		}else if(!code.equals(registerMsg.getCode())){
			flag = false;
			rm.setReason("验证码有误！");
		}
		if(!flag){
			rm.setCode(ResultMsg.FAIL);
			return rm;
		}
		User u1 = new User();
		u1.setUserName(userName);
		User u2 = new User();
		u2.setPhoneNumber(phoneNumber);
		User s1 = baseDao.execute("UserMapper.select", u1, uClass, uClass);
		User s2 = baseDao.execute("UserMapper.select", u2, uClass, uClass);
		//均为空时则表示该用户尚未被注册，可以写入
		if(s1==null&&s2==null){
			baseDao.execute("UserMapper.insert", user, uClass, null);
			User uu = baseDao.execute("UserMapper.select", user, uClass, uClass);
			setUser(session, uu,ip);
			rm.setCode(ResultMsg.SUCCESS);
			rm.setReason("注册成功！");
		}else{
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("用户名或手机号已存在！");
		}
		return rm;
	}
	
	public ResultMsg checkData(User user) throws Exception{
		User u = baseDao.execute("UserMapper.select", user,User.class ,User.class );
		ResultMsg rm = ResultMsgUtil.getResultMsg();
		if(u!=null){
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("已存在！");
		}else{
			rm.setCode(ResultMsg.SUCCESS);
			rm.setReason("可使用！");
		}
		return rm;
	}
	
	public ResultMsg logout(HttpSession session){
		ResultMsg rm = ResultMsgUtil.getResultMsg();
		session.removeAttribute("user");
		rm.setCode(ResultMsg.SUCCESS);
		rm.setReason("注销怎么可能失败嘛~~");
		return rm;
	}
	
	/**
	 * 将用户信息存入session，并入表
	 * @param session
	 * @param user
	 * @throws Exception 
	 */
	private void setUser(HttpSession session,User user,String ip) throws Exception{
		//session中已有则不进行以下操作
		User oldUser = (User) session.getAttribute("user");
		session.setAttribute("user", user);
		if(oldUser!=null&&oldUser.getUserName().equals(user.getUserName())){
			return;
		}
		UserLog ul = new UserLog();
		Class<UserLog> ulClass = UserLog.class;
		ul.setUserName(user.getUserName());
		ul.setLoginIp(ip);
		/*
		//获取最后一条记录的guid
		UserLog ul2 = baseDao.execute("UserLogMapper.selectLast", ul, ulClass, ulClass);
		if(ul2!=null)
			ul.setPreGuid(ul2.getGuid());*/
		baseDao.execute("UserLogMapper.insert", ul, ulClass, null);
	}
}
