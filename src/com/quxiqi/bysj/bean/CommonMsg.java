package com.quxiqi.bysj.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonMsg {
	private List<String> phoneNumbers = new ArrayList<>();
	
	private Map<String,String> params = new HashMap<>();
	//类型 配置文件里的前缀
	private String type;
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "CommonMsg [phoneNumbers=" + phoneNumbers + ", params=" + params + ", type=" + type + "]";
	}
	//针对电话号码的操作
	public void addPhoneNumber(String phoneNumber){
		phoneNumbers.add(phoneNumber);
	}
	public void addPhoneNumber(List<String> phoneNumber){
		phoneNumbers.addAll(phoneNumber);
	}
	public boolean removePhoneNumber(String phoneNumber){
		return phoneNumbers.remove(phoneNumber);
	}
	
	
	//针对参数的操作
	public String putParam(String k,String v){
		return params.put(k, v);
	}
	public String removeParam(String k ){
		return params.remove(k);
	}
	
}
