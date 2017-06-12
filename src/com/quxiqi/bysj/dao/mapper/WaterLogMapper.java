package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.WaterLog;

public interface WaterLogMapper {
	List<WaterLog> select(WaterLog waterLog);
	void insert(WaterLog waterLog);
	void insertList(List<WaterLog> list);
}
