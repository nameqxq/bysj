package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.MoneyLog;

public interface MoneyLogMapper {
	List<MoneyLog> select(MoneyLog moneyLog);
	void insert(MoneyLog moneyLog);
	void insertList(List<MoneyLog> list);
}
