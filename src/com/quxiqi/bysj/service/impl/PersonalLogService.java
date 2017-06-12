package com.quxiqi.bysj.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxiqi.bysj.bean.RegisterMsg;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.service.DefaultService;
import com.quxiqi.bysj.util.MessageHandler;
import com.quxiqi.bysj.util.MessageUtil;
import com.quxiqi.bysj.util.MyBeanFactory;
import com.quxiqi.bysj.util.ResultMsgUtil;
import com.quxiqi.bysj.util.StringUtil;
@Service
public class PersonalLogService extends DefaultService {
	@Override
	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		super.setBaseDao(baseDao);
	}
	
	public ResultMsg changePassword(User u) throws Exception{
		User user = getUser();
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		if(!user.getPassword().equals(u.getPassword())){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("密码错误！");
			return rs;
		}
		String newPassword = u.getUserName();
		if(newPassword==null||!newPassword.matches("^[a-zA-Z0-9_]{3,16}$")){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("新密码格式错误！");
			return rs;
		}
		u.setPassword(newPassword);
		u.setUserName(user.getUserName());
		
		baseDao.execute("UserMapper.update", u, User.class, null);
		user.setPassword(newPassword);
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData("操作成功！");
		return rs;
	}
	//发送重置验证码
	public ResultMsg sendResetMsg(User u) throws Exception{
		ResultMsg rm= ResultMsgUtil.getResultMsg();
		User user = getUser();
		if(!user.getPhoneNumber().equals(u.getUserName())){
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("原号码错误！");
			return rm;
		}
		Class<User> uc = User.class;
		User u2 = MyBeanFactory.getBean(uc);
		String phoneNumber = u.getPhoneNumber();
		u2.setPhoneNumber(phoneNumber);
		User ru = baseDao.execute("UserMapper.select", u2, uc, uc);
		if(ru!=null){
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("该号码已被使用！");
			return rm;
		}
		
		RegisterMsg registerMsg = new RegisterMsg(user.getUserName(), phoneNumber);
		
		return MessageUtil.sendResetVerificationCode(registerMsg);
	}
	//重置手机号码
	public ResultMsg resetPhone(HashMap<String,String> map) throws Exception{
		User user = getUser();
		ResultMsg rm= ResultMsgUtil.getResultMsg();
		String oldPhoneNumber = StringUtil.getString(map, "oldPhoneNumber");
		String newPhone = StringUtil.getString(map, "newPhone");
		String code = StringUtil.getString(map, "code");
		if(!user.getPhoneNumber().equals(oldPhoneNumber)){
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("原号码错误！");
			return rm;
		}
		
		
		Class<User> uc = User.class;
		User u2 = MyBeanFactory.getBean(uc);
		u2.setPhoneNumber(newPhone);
		User ru = baseDao.execute("UserMapper.select", u2, uc, uc);
		if(ru!=null){
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("该号码已被使用！");
			return rm;
		}
		RegisterMsg registerMsg = MessageHandler.getInstance().getRegisterMsg(newPhone);
		if(code==null||!code.equals(registerMsg.getCode())){
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("验证码错误！");
			return rm;
		}
		u2.setUserName(user.getUserName());
		baseDao.execute("UserMapper.update", u2, uc, null);
		user.setPhoneNumber(newPhone);
		
		rm.setCode(ResultMsg.SUCCESS);
		rm.setData("操作成功");
		return rm;
	}
	
}
