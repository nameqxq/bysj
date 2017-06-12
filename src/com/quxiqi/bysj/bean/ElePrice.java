package com.quxiqi.bysj.bean;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;
@Alias("ElePrice")
public class ElePrice extends MyPage implements Comparable<ElePrice> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4107939190076892374L;
	private String year;
	private String month;
	private BigDecimal elePrice;
	private BigDecimal waterPrice ;
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
	public BigDecimal getElePrice() {
		return elePrice;
	}
	public void setElePrice(BigDecimal elePrice) {
		this.elePrice = elePrice;
	}
	public BigDecimal getWaterPrice() {
		return waterPrice;
	}
	public void setWaterPrice(BigDecimal waterPrice) {
		this.waterPrice = waterPrice;
	}
	@Override
	public String toString() {
		return "ElePrice [year=" + year + ", month=" + month + ", elePrice=" + elePrice + ", waterPrice=" + waterPrice
				+ "]";
	}
	@Override
	public int compareTo(ElePrice o) {
		return (year+month).compareTo(o.year+o.month);
	}
	
}
