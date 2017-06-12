package com.quxiqi.bysj.util;

import java.util.TreeMap;

import com.quxiqi.bysj.bean.RegisterMsg;
/**
 * 
 * @author Quxq
 * 用于辅助短信的类，应当是单例的
 * 主要用来储存验证码
 */
public class MessageHandler {
	private final TreeMap<String,RegisterMsg> registerCodeMap = new TreeMap<>();
	private static MessageHandler instance = null;
	private MessageHandler(){
	}
	public static MessageHandler getInstance(){
		if(instance==null){
			synchronized (MessageHandler.class) {
				if(instance==null)
					instance =  new MessageHandler();
			}
		}
		return instance;
	}
	public TreeMap<String,RegisterMsg> getRegisterCodeMap(){
		return registerCodeMap;
	}
	public void addRegisterCode(RegisterMsg rm){
		registerCodeMap.put(rm.getPhoneNumber(), rm);
	}
	public RegisterMsg getRegisterMsg(String phoneNumber){
		return registerCodeMap.get(phoneNumber);
	}
	public String getRegisterCode(String phoneNumber){
		return getRegisterMsg(phoneNumber).getCode();
	}
	public RegisterMsg removeRegisterCode(String phoneNumber){
		return registerCodeMap.remove(phoneNumber);
	}
	public RegisterMsg removeRegisterCode(RegisterMsg rm){
		return registerCodeMap.remove(rm.getPhoneNumber());
	}
}
