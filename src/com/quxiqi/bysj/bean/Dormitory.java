package com.quxiqi.bysj.bean;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;
@Alias("Dormitory")
public class Dormitory extends MyPage implements Comparable<Dormitory> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2259338221319318764L;
	private String dormitoryNumber;
	private String buildingNumber;
	private String layerNumber;
	private BigDecimal money;
	private BigDecimal eleNumber;
	private BigDecimal waterNumber;
	
	public Dormitory() {
	}
	
	


	public String getDormitoryNumber() {
		return dormitoryNumber;
	}


	public void setDormitoryNumber(String dormitoryNumber) {
		this.dormitoryNumber = dormitoryNumber;
	}
	
	public void setDormitoryNumber(String dormitoryNumber,Boolean flag) {
		if(flag){
			String[] split = dormitoryNumber.split("#");
			buildingNumber = split[0];
			layerNumber = split[1];
		}
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


	public BigDecimal getMoney() {
		return money;
	}


	public void setMoney(BigDecimal money) {
		this.money = money;
	}




	public BigDecimal getEleNumber() {
		return eleNumber;
	}


	public void setEleNumber(BigDecimal eleNumber) {
		this.eleNumber = eleNumber;
	}


	public BigDecimal getWaterNumber() {
		return waterNumber;
	}


	public void setWaterNumber(BigDecimal waterNumber) {
		this.waterNumber = waterNumber;
	}


	@Override
	public int compareTo(Dormitory o) {
		return dormitoryNumber.compareTo(o.dormitoryNumber);
	}




	@Override
	public String toString() {
		return "Dormitory [dormitoryNumber=" + dormitoryNumber + ", buildingNumber=" + buildingNumber + ", layerNumber="
				+ layerNumber + ", money=" + money + ", eleNumber=" + eleNumber + ", waterNumber=" + waterNumber + "]";
	}


	
	
}
