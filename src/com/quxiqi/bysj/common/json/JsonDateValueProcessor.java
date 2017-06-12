package com.quxiqi.bysj.common.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateValueProcessor implements JsonValueProcessor {

	private String formatDateTime = "yyyy-MM-dd HH:mm:ss";
	private String formatDate = "yyyy-MM-dd HH:mm";
	public JsonDateValueProcessor() {
	}
	public JsonDateValueProcessor(String format) {
		this.formatDateTime = format;
	}
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value, jsonConfig);
	}
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value, jsonConfig);
	}
	// 主要方法,处理类型为date的对象,将其转换为字符类型(不足:通过判断字段长度不能完全辨别日期和长日期,求改进)
	private Object process(Object value, JsonConfig jsonConfig) {
		if (value instanceof Date) {
			String str = "";
			if (String.valueOf(value).length() > 10) {
				str = new SimpleDateFormat(formatDateTime).format((Date) value);
			} else {
				str = new SimpleDateFormat(formatDate).format((Date) value);
			}
			return str;
		}else if (value instanceof String) {
			if (String.valueOf(value).toUpperCase().equals("NULL")) {
				return "";
			}else{
				String v =String.valueOf(value);
				if(v.contains("<") || v.contains(">")){
					v = v.replaceAll("<", "&lt").replaceAll(">", "&gt");
					return v;
				}
			}
		}
		return value == null ? null : value.toString();
	}
	public String getFormatDateTime() {
		return formatDateTime;
	}
	public void setFormatDateTime(String formatDateTime) {
		this.formatDateTime = formatDateTime;
	}
	public String getFormatDate() {
		return formatDate;
	}
	public void setFormatDate(String formatDate) {
		this.formatDate = formatDate;
	}
}
