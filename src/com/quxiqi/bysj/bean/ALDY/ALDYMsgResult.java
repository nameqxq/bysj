package com.quxiqi.bysj.bean.ALDY;
//阿里大于短信发送返回值
public class ALDYMsgResult {
	private String err_code;
	private String model;
	private Boolean success;
	private String msg;
	public ALDYMsgResult() {
	}
	public ALDYMsgResult(String err_code, String model, Boolean success, String msg) {
		super();
		this.err_code = err_code;
		this.model = model;
		this.success = success;
		this.msg = msg;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	@Override
	public String toString() {
		return "ALDYMsgResult [err_code=" + err_code + ", model=" + model + ", success=" + success + ", msg=" + msg
				+ "]";
	}
	
}
