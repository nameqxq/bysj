package com.quxiqi.bysj.util;

import java.sql.Date;
import java.util.List;

import com.quxiqi.bysj.common.json.JsonDateValueProcessor;
import com.quxiqi.bysj.common.json.JsonPropertyFilter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonUtil {
	private static JsonConfig jc = new JsonConfig();
	static{
		jc.setJsonPropertyFilter(new JsonPropertyFilter());
		jc.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
	}
	public static JsonConfig getJsonConfig(){
		return jc;
	}
	
	public static String getJsonString(Object obj){
		
		return JSONObject.fromObject(obj,jc).toString();
	}
	
	public static String getJsonArrayString(List<?> list){
		return JSONArray.fromObject(list).toString();
	}
}
