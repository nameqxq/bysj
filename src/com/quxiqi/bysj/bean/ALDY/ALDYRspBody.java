package com.quxiqi.bysj.bean.ALDY;
//阿里大于返回的body
public class ALDYRspBody {
	private ALDYMsgResult result;
	public ALDYRspBody() {
	}
	
	public ALDYRspBody(ALDYMsgResult result) {
		super();
		this.result = result;
	}

	public ALDYMsgResult getResult() {
		return result;
	}
	public void setResult(ALDYMsgResult result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "ALDYRspBody [result=" + result + "]";
	}
	
}
