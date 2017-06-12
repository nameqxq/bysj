package com.quxiqi.bysj.bean;

import com.quxiqi.bysj.common.IMyPage;

public class ResultMsg {
	
	public static final String SUCCESS = "11111";
	public static final String FAIL = "00000";
	public static final String NO_ACCESS = "99999";
	//状态码  暂定00000为失败,11111为成功,99999
	private String code;
	//失败理由（可为空）
	private String reason;
	//返回数据
	private Object data;
	//
	private IMyPage myPage;
	public ResultMsg() {
	}
	

	public ResultMsg(String code, String reason, Object data, MyPage myPage) {
		super();
		this.code = code;
		this.reason = reason;
		this.data = data;
		this.myPage = myPage;
	}




	public IMyPage getMyPage() {
		return myPage;
	}


	public void setMyPage(IMyPage myPage) {
		this.myPage = myPage;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return "ResultMsg [code=" + code + ", reason=" + reason + ", data=" + data + "]";
	}
}
