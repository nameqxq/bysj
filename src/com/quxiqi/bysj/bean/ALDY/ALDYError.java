package com.quxiqi.bysj.bean.ALDY;

public class ALDYError {
	private String code;
	private String msg;
	private String sub_code;
	private String sub_msg;
	private String request_id;
	
	public ALDYError() {
	}
	
	public ALDYError(String code, String msg, String sub_code, String sub_msg, String request_id) {
		super();
		this.code = code;
		this.msg = msg;
		this.sub_code = sub_code;
		this.sub_msg = sub_msg;
		this.request_id = request_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSub_code() {
		return sub_code;
	}
	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}
	public String getSub_msg() {
		return sub_msg;
	}
	public void setSub_msg(String sub_msg) {
		this.sub_msg = sub_msg;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	@Override
	public String toString() {
		return "ALDYError [code=" + code + ", msg=" + msg + ", sub_code=" + sub_code + ", sub_msg=" + sub_msg
				+ ", request_id=" + request_id + "]";
	}
	
	
}
