package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.Announcement;

public interface AnnouncementMapper {
	List<Announcement> select(Announcement anno);
	List<Announcement> selectLike(Announcement anno);
	Announcement selectByGuid(Announcement anno);
	void insert(Announcement anno);
	void update(Announcement anno);
}
