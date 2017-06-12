package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.UserLog;

public interface UserLogMapper {
	void insert(UserLog userLog);
	List<UserLog> select(UserLog userLog);
	void update(UserLog userLog);
}
