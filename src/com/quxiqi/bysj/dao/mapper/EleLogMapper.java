package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.EleLog;

public interface EleLogMapper {
	List<EleLog> select(EleLog eleLog);
	void insert(EleLog eleLog);
	void insertList(List<EleLog> list);
}
