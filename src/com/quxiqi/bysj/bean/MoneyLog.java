package com.quxiqi.bysj.bean;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.quxiqi.bysj.util.DateUtil;
@Alias("MoneyLog")
public class MoneyLog extends MyPage implements Comparable<MoneyLog>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3440112381964625439L;
	private String dormitoryNumber;
	private String buildingNumber;
	private String layerNumber;
	private String guid;
	private String preGuid;
	private BigDecimal payMoney;
	private String payUser;
	private BigDecimal money;
	private String payDate;
	
	private String payType;
	
	public String getPayUser() {
		return payUser;
	}
	public void setPayUser(String payUser) {
		this.payUser = payUser;
	}
	public String getDormitoryNumber() {
		return dormitoryNumber;
	}
	public void setDormitoryNumber(String dormitoryNumber) {
		this.dormitoryNumber = dormitoryNumber;
	}
	public String getBuildingNumber() {
		return buildingNumber;
	}
	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}
	public String getLayerNumber() {
		return layerNumber;
	}
	public void setLayerNumber(String layerNumber) {
		this.layerNumber = layerNumber;
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
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = 
				DateUtil.toDateString(payDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Override
	public String toString() {
		return "MoneyLog [dormitoryNumber=" + dormitoryNumber + ", buildingNumber=" + buildingNumber + ", layerNumber="
				+ layerNumber + ", guid=" + guid + ", preGuid=" + preGuid + ", payMoney=" + payMoney + ", payUser="
				+ payUser + ", money=" + money + ", payDate=" + payDate + ", payType=" + payType + "]";
	}
	@Override
	public int compareTo(MoneyLog o) {
		return dormitoryNumber.compareTo(o.dormitoryNumber);
	}
	
}
