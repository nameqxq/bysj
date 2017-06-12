package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.Dormitory;

public interface DormitoryMapper {
	void update(Dormitory dormitory);
	List<Dormitory> select(Dormitory dormitory);
	Dormitory selectOne(Dormitory dormitory);
	void updateList(List<Dormitory> list);
	void insertList(List<Dormitory> list);
}
