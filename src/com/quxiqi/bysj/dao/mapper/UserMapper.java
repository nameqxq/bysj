package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.Dormitory;
import com.quxiqi.bysj.bean.User;

public interface UserMapper {
	User select(User u);
	void insert(User u);
	void update(User u);
	void binding(User u);
	List<User> selectForSendMsg(Dormitory dor);
	List<User> selectToDor(Dormitory dor);
	List<User> selectForLike(User user);
}
