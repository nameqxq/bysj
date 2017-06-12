package com.quxiqi.bysj.bean;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.quxiqi.bysj.util.DateUtil;
@Alias("UrlCode")
public class UrlCode {
	/**
	 * url
	 */
	private String url;
	/**
	 * 对应页面代码
	 */
	private String code;
	/**
	 * 创建时间
	 */
	private String createDate;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = 
				DateUtil.toDateString(createDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	@Override
	public String toString() {
		return "UrlCode [url=" + url + ", code=" + code + ", createDate=" + createDate + "]";
	}
	
}
