package com.quxiqi.bysj.bean;

import java.util.Date;

import com.quxiqi.bysj.util.DateUtil;

public class RepairBill extends MyPage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4913215983252703668L;
	private String guid;
	//标题
	private String title;
	
	private String dormitoryNumber;
	//联系人
	private String personName;
	//联系电话
	private String phoneNumber;
	//详细描述
	private String context;
	//状态
	private String status;
	//申请用户
	private String userName;
	//创建时间
	private String createDate;
	//拒绝时间
	private String refuseDate;
	//预检修理时间
	private String repairDate;
	//真正修理时间
	private String realRepairDate;
	//最后修改时间
	private String modifiDate;
	//修改人
	private String modifiPerson;
	//修理人
	private String handlerPerson;
	//修理人号码
	private String handlerPhone;
	//备注
	private String remark;
	
	private String image1;
	private String image2;
	private String image3;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public String getImage3() {
		return image3;
	}
	public void setImage3(String image3) {
		this.image3 = image3;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = 
				DateUtil.toDateString(createDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	public String getRefuseDate() {
		return refuseDate;
	}
	public void setRefuseDate(Date refuseDate) {
		this.refuseDate = 
				DateUtil.toDateString(refuseDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	public String getRepairDate() {
		return repairDate;
	}
	public void setRepairDate(Date repairDate) {
		this.repairDate = 
				DateUtil.toDateString(repairDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	public void setRepairDate(String repairDate) {
		this.repairDate = repairDate;
	}
	public String getRealRepairDate() {
		return realRepairDate;
	}
	public void setRealRepairDate(String realRepairDate) {
		this.realRepairDate = realRepairDate;
	}
	public void setRealRepairDate(Date realRepairDate) {
		this.realRepairDate = 
				DateUtil.toDateString(realRepairDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	public String getModifiDate() {
		return modifiDate;
	}
	public void setModifiDate(Date modifiDate) {
		this.modifiDate = 
				DateUtil.toDateString(modifiDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	public String getModifiPerson() {
		return modifiPerson;
	}
	public void setModifiPerson(String modifiPerson) {
		this.modifiPerson = modifiPerson;
	}
	public String getHandlerPerson() {
		return handlerPerson;
	}
	public void setHandlerPerson(String handlerPerson) {
		this.handlerPerson = handlerPerson;
	}
	public String getHandlerPhone() {
		return handlerPhone;
	}
	public void setHandlerPhone(String handlerPhone) {
		this.handlerPhone = handlerPhone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String mark) {
		this.remark = mark;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getDormitoryNumber() {
		return dormitoryNumber;
	}
	public void setDormitoryNumber(String dormitoryNumber) {
		this.dormitoryNumber = dormitoryNumber;
	}
	@Override
	public String toString() {
		return "RepairBill [guid=" + guid + ", title=" + title + ", dormitoryNumber=" + dormitoryNumber
				+ ", personName=" + personName + ", phoneNumber=" + phoneNumber + ", context=" + context + ", status="
				+ status + ", userName=" + userName + ", createDate=" + createDate + ", refuseDate=" + refuseDate
				+ ", repairDate=" + repairDate + ", realRepairDate=" + realRepairDate + ", modifiDate=" + modifiDate
				+ ", modifiPerson=" + modifiPerson + ", handlerPerson=" + handlerPerson + ", handlerPhone="
				+ handlerPhone + ", remark=" + remark + ", image1=" + image1 + ", image2=" + image2 + ", image3="
				+ image3 + "]";
	}
}
