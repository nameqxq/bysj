package com.quxiqi.bysj.common.json;

import com.quxiqi.bysj.util.StringUtil;

import net.sf.json.util.PropertyFilter;
/**
 * json属性过滤器,用于过滤属性或数据,可以在实例化的时候构造过滤的关键字
 * 
 */
@SuppressWarnings("unused")
public class JsonPropertyFilter implements PropertyFilter {
	private String formatValueKey = "{}";
	private String formatNameKey = "";
	public JsonPropertyFilter() {
	}
	public JsonPropertyFilter(String str) {
		this.formatValueKey = str;
	}
	//主要方法,返回true则为过滤,false为不过滤,目前默认过滤对象为花括号字符串,或者null
	public boolean apply(Object source, String name, Object value) {
		
		if (String.valueOf(value).equals(formatValueKey) || value == null || value.equals(null) || StringUtil.dnvString(value).equals("")) {
			// System.out.println("名字:" + name + ",值子:" + value);
			return true;
		}
		return false;
	}
}
