package com.quxiqi.bysj.bean;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.quxiqi.bysj.util.DateUtil;
@Alias("WaterLog")
public class WaterLog extends MyPage implements Comparable<WaterLog>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5964345119518628702L;
	private String dormitoryNumber;
	private String buildingNumber;
	private String layerNumber;
	private String guid;
	private String preGuid;
	private String year;
	private String month;
	private String enteringTime;
	private BigDecimal waterNumber;
	private String enteringPerson;
	public WaterLog() {
	}

	public String getEnteringPerson() {
		return enteringPerson;
	}


	public void setEnteringPerson(String enteringPerson) {
		this.enteringPerson = enteringPerson;
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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getEnteringTime() {
		return enteringTime;
	}

	public void setEnteringTime(Date enteringTime) {
		this.enteringTime = 
				DateUtil.toDateString(enteringTime, DateUtil.DEFAULT_DATETIME_PATTERN);
	}


	public BigDecimal getWaterNumber() {
		return waterNumber;
	}

	public void setWaterNumber(BigDecimal waterNumber) {
		this.waterNumber = waterNumber;
	}


	@Override
	public String toString() {
		return "WaterLog [dormitoryNumber=" + dormitoryNumber + ", buildingNumber=" + buildingNumber + ", layerNumber="
				+ layerNumber + ", guid=" + guid + ", preGuid=" + preGuid + ", year=" + year + ", month=" + month
				+ ", enteringTime=" + enteringTime + ", waterNumber=" + waterNumber + ", enteringPerson="
				+ enteringPerson + "]";
	}

	@Override
	public int compareTo(WaterLog o) {
		return dormitoryNumber.compareTo(o.dormitoryNumber);
	}
	
	
}
