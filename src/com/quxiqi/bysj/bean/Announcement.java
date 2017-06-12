package com.quxiqi.bysj.bean;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.quxiqi.bysj.util.DateUtil;
@Alias("Announcement")
public class Announcement extends MyPage implements Comparable<Announcement> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7876188912739004608L;
	private String title;
	private String context;
	private String guid;
	private String createDate;
	private String createPerson;
	private String aliveFlag;
	private String changeDate;
	private String changePerson;
	private String remark;
	
	public String getTitle() {
		return title;
	}




	public void setTitle(String title) {
		this.title = title;
	}




	public String getContext() {
		return context;
	}




	public void setContext(String context) {
		this.context = context;
	}




	public String getGuid() {
		return guid;
	}




	public void setGuid(String guid) {
		this.guid = guid;
	}




	public String getCreateDate() {
		return createDate;
	}




	public void setCreateDate(Date createDate) {
		this.createDate = 
				DateUtil.toDateString(createDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}




	public String getCreatePerson() {
		return createPerson;
	}




	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}




	public String getAliveFlag() {
		return aliveFlag;
	}




	public void setAliveFlag(String aliveFlag) {
		this.aliveFlag = aliveFlag;
	}




	public String getChangeDate() {
		return changeDate;
	}




	public void setChangeDate(Date changeDate) {
		this.changeDate = 
				DateUtil.toDateString(changeDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}




	public String getChangePerson() {
		return changePerson;
	}




	public void setChangePerson(String changePerson) {
		this.changePerson = changePerson;
	}




	public String getRemark() {
		return remark;
	}




	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Override
	public String toString() {
		return "Announcement [title=" + title + ", context=" + context + ", guid=" + guid + ", createDate=" + createDate
				+ ", createPerson=" + createPerson + ", aliveFlag=" + aliveFlag + ", changeDate=" + changeDate
				+ ", changePerson=" + changePerson + ", remark=" + remark + "]";
	}


	@Override
	public int compareTo(Announcement o) {
		return this.title.compareTo(o.title);
	}
}
