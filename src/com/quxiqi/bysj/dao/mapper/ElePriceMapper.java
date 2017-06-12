package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.ElePrice;

public interface ElePriceMapper {
	List<ElePrice> select(ElePrice elePrice);
	void update(ElePrice elePrice);
	void insert(ElePrice elePrice);
}
