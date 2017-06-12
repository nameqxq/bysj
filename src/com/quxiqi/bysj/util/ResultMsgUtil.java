package com.quxiqi.bysj.util;

import com.quxiqi.bysj.bean.MyPage;
import com.quxiqi.bysj.bean.ResultMsg;

public class ResultMsgUtil {
	public static ResultMsg getResultMsg(){
		return new ResultMsg();
	}
	public static ResultMsg getResultMsg(String code,String reason ,Object data,MyPage myPage){
		return new ResultMsg( code, reason , data, myPage);
	}
}
