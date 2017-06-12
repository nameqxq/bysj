package com.quxiqi.bysj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxiqi.bysj.bean.Announcement;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.service.DefaultService;
import com.quxiqi.bysj.util.ResultMsgUtil;
@Service
public class AnnouncementService extends DefaultService {
	@Override
	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		super.setBaseDao(baseDao);
	}
	public ResultMsg add(Announcement anno) throws Exception{
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		anno.setCreatePerson(getUser().getUserName());
		baseDao.execute("AnnouncementMapper.insert", anno, Announcement.class, null);
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData("发布公告成功！");
		return rs;
	}
	
	public ResultMsg cancellation(Announcement anno) throws Exception{
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		anno.setAliveFlag("0");
		anno.setChangePerson(getUser().getUserName());
		baseDao.execute("AnnouncementMapper.update", anno, Announcement.class, null);
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData("作废成功！");
		return rs;
	}
}
