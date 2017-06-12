package com.quxiqi.bysj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.Student;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.service.DefaultService;
import com.quxiqi.bysj.util.MyBeanFactory;
import com.quxiqi.bysj.util.ResultMsgUtil;
@Service
@Scope("prototype")
public class PersonalDataService extends DefaultService{
	@Override
	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		super.setBaseDao(baseDao);
	}
	public ResultMsg saveStudent(Student student) throws Exception{
		ResultMsg rm = ResultMsgUtil.getResultMsg();
		Class<User> c = User.class;
		User u = MyBeanFactory.getBean(c);
		u.setStudent(student);
		User checkUser = baseDao.execute("UserMapper.select", u, c, c); 
		User user = getUser();
		if(checkUser!=null){
			rm.setCode("00000");
			if(user.getUserName().equals(checkUser.getUserName())){
				rm.setReason("嗷，请不要重复绑定~~！");
			}else{
				rm.setReason("该学生已被用户"+checkUser.getUserName()+"绑定！");
			}
			return rm;
		}
		User uu = MyBeanFactory.getBean(c);
		uu.setUserName(user.getUserName());
		uu.setStudent(u.getStudent());
		baseDao.execute("UserMapper.binding", uu, c, null);
		
		User uu2 = baseDao.execute("UserMapper.select", uu, c, c);
		user.setDormitory(uu2.getDormitory());
		user.setStudent(uu2.getStudent());
		rm.setCode("11111");
		rm.setReason("绑定成功！");
		return rm;
	}
	
	public ResultMsg saveUser(User user) throws Exception{
		ResultMsg rm = ResultMsgUtil.getResultMsg();
		Class<User> c = User.class;
		baseDao.execute("UserMapper.update", user, c, null);
		user = baseDao.execute("UserMapper.select", user, c, c);
		rm.setCode("11111");
		rm.setData(user);
		return rm;
	}
}
