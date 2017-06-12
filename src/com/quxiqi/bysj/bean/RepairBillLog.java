package com.quxiqi.bysj.bean;

import java.util.Date;

import com.quxiqi.bysj.util.DateUtil;

public class RepairBillLog extends MyPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2387909897857083440L;
	private String guid;
	private String repairBillGuid;
	private String preGuid;
	private String status;
	private String createDate;
	private String createPerson;
	private String remark;
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getRepairBillGuid() {
		return repairBillGuid;
	}
	public void setRepairBillGuid(String repairBillGuid) {
		this.repairBillGuid = repairBillGuid;
	}
	public String getPreGuid() {
		return preGuid;
	}
	public void setPreGuid(String preGuid) {
		this.preGuid = preGuid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String mark) {
		this.remark = mark;
	}
	@Override
	public String toString() {
		return "BepairBillLog [guid=" + guid + ", repairBillGuid=" + repairBillGuid + ", preGuid=" + preGuid
				+ ", status=" + status + ", createDate=" + createDate + ", createPerson=" + createPerson + ", mark="
				+ remark + "]";
	}
	
}
