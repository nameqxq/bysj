package com.quxiqi.bysj.bean;

import java.util.Random;

public class RegisterMsg {
	//用户名
	private String userName;
	//验证码
	private String code;
	//手机号
	private String phoneNumber;
	
	public RegisterMsg() {
		Random rd = new Random();
		String tempCode = ""+rd.nextInt(1000000);
		int len = tempCode.length();
		StringBuilder s = new StringBuilder();
		for(int i=0;i<6-len;i++){
			s.append("0");
		}
		code = s.append(tempCode).toString();
	}

	
	public RegisterMsg(String userName, String phoneNumber) {
		this();
		this.userName = userName;
		this.phoneNumber = phoneNumber;
	}


	public RegisterMsg(String userName, String code, String phoneNumber) {
		super();
		this.userName = userName;
		this.code = code;
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	@Override
	public String toString() {
		return "RegisterMsg [userName=" + userName + ", code=" + code + ", phoneNumber=" + phoneNumber + "]";
	}
	
	public String toJsonString(){
		return "{\"userName\":\""+userName+"\",\"code\":\""+code+"\"}";
	}
	
}
