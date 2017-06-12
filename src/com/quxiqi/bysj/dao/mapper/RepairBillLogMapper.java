package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.RepairBillLog;

public interface RepairBillLogMapper {
	void insert(RepairBillLog repairBillLog);
	List<RepairBillLog> select(RepairBillLog repairBillLog);
}
