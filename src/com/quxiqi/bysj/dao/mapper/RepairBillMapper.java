package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.PageMap;
import com.quxiqi.bysj.bean.RepairBill;

public interface RepairBillMapper {
	void insert(RepairBill repairBill);
	void update(RepairBill repairBill);
	List<RepairBill> select(RepairBill repairBill);
	RepairBill selectByGuid(RepairBill repairBill);
	List<RepairBill> selectStatusIn(PageMap map);
}
