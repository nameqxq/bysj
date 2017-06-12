package com.quxiqi.bysj.bean;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.quxiqi.bysj.util.DateUtil;
@Alias("UserLog")
public class UserLog extends MyPage implements Comparable<UserLog>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2122031264721364577L;
	private String userName;
	private Integer onlineTime;
	private String loginDate;
	private String logoutDate;
	private String loginIp;
	private String guid;
	private String preGuid;
	private String onlineFlag;
	public UserLog() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Integer onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = 
				DateUtil.toDateString(loginDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}

	public String getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = 
				DateUtil.toDateString(logoutDate, DateUtil.DEFAULT_DATETIME_PATTERN);;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPreGuid() {
		return preGuid;
	}

	public void setPreGuid(String preGuid) {
		this.preGuid = preGuid;
	}
	
	public String getOnlineFlag() {
		return onlineFlag;
	}

	public void setOnlineFlag(String onlineFlag) {
		this.onlineFlag = onlineFlag;
	}


	@Override
	public String toString() {
		return "UserLog [userName=" + userName + ", onlineTime=" + onlineTime + ", loginDate=" + loginDate
				+ ", logoutDate=" + logoutDate + ", loginIp=" + loginIp + ", guid=" + guid + ", preGuid=" + preGuid
				+ ", onlineFlag=" + onlineFlag + "]";
	}

	@Override
	public int compareTo(UserLog o) {
		return logoutDate.compareTo(o.logoutDate);
	}

}
