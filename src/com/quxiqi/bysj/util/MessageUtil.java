package com.quxiqi.bysj.util;


import com.quxiqi.bysj.bean.CommonMsg;
import com.quxiqi.bysj.bean.RegisterMsg;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.ALDY.ALDYError;
import com.quxiqi.bysj.bean.ALDY.ALDYMsgResult;
import com.quxiqi.bysj.bean.ALDY.ALDYRsp;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import net.sf.json.JSONObject;

public class MessageUtil {
	private static String url;
	private static Property pp;
	private static MessageHandler messageHandler 
		= MessageHandler.getInstance();
	static{
		pp = new Property("config/message.properties");
		url = pp.getValue("url");
	}
	//发送短信
	public static ResultMsg sendMsg(String appkey,String secret,String smsParam,String phoneNumber
			,String smsFreeSignName,String smsTemplateCode){
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//		boolean matches = phoneNumber.matches("^1[0-9]{10}$");
		ResultMsg rm =ResultMsgUtil.getResultMsg();
		req.setSmsType("normal");
		req.setSmsFreeSignName(smsFreeSignName);
		req.setSmsParamString(smsParam);
		req.setRecNum(phoneNumber);
		req.setSmsTemplateCode(smsTemplateCode);
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			String bodyStr = rsp.getBody();
			//TODO 解析返回数据
			ALDYRsp body = (ALDYRsp)JSONObject.toBean(JSONObject.fromObject(bodyStr),ALDYRsp.class);
			ALDYError errorRsp = body.getError_response();
			if(errorRsp==null){
				ALDYMsgResult result = body.getAlibaba_aliqin_fc_sms_num_send_response().getResult();
				if(result.getSuccess()){
					rm.setCode(ResultMsg.SUCCESS);
					rm.setData("短信发送成功!");
					rm.setReason(rsp.getBody());
				}else{
					rm.setCode(ResultMsg.FAIL);
					rm.setData(rsp.getBody());
					rm.setReason("短信发送失败："+"阿里大于状态码-->"+result.getErr_code());
				}
			}else{
				rm.setCode(ResultMsg.FAIL);
				rm.setData(rsp.getSubMsg());
				rm.setReason(rsp.getBody());
			}
		} catch (ApiException e) {
			rm.setCode(ResultMsg.FAIL);
			rm.setReason("短信接口调用失败！");
		} catch (Exception e1){
			rm.setCode(ResultMsg.FAIL);
			rm.setReason(e1.getMessage());
		}
		return rm;
	}
	//发送验证码
	public static ResultMsg sendRegisterVerificationCode(RegisterMsg rm) throws ApiException{
		return verificationCodeHandler(rm,"register");
	}
	//发送重新绑定验证码
	public static ResultMsg sendResetVerificationCode(RegisterMsg rm) throws ApiException{
		return verificationCodeHandler(rm,"reset");
	}
	
	private static ResultMsg verificationCodeHandler(RegisterMsg rm,String type) {
		messageHandler.addRegisterCode(rm);
		String phoneNumber = rm.getPhoneNumber();
		if(!phoneNumber.matches("^1[0-9]{10}$"))
			return ResultMsgUtil.getResultMsg(ResultMsg.FAIL, "手机号格式不正确！", null, null);
		return sendMsg(pp.getValue(type+".app_key"), 
				pp.getValue(type+".secret"), 
				rm.toJsonString(), 
				phoneNumber, 
				pp.getValue(type+".SmsFreeSignName"), 
				pp.getValue(type+".smsTemplateCode"));
	}
	
	/**
	 * 公用模板，支持非注册码，支持群发
	 */
	public static ResultMsg commonSendMsg(CommonMsg commonMsg)throws ApiException{
		StringBuilder sb = new StringBuilder();
		for(String phoneNumber :commonMsg.getPhoneNumbers()){
			if(!phoneNumber.matches(phoneNumber))
				return ResultMsgUtil.getResultMsg(ResultMsg.FAIL, phoneNumber+"不是一个合法的手机号！", null, null);
			sb.append(phoneNumber);
			sb.append(",");
		}
		String type = commonMsg.getType();
		String phones = sb.deleteCharAt(sb.length()-1).toString();
		
		return sendMsg(pp.getValue(type+".app_key") ,pp.getValue(type+".secret"),
				JsonUtil.getJsonString(commonMsg.getParams()),phones
					,pp.getValue(type+".SmsFreeSignName"),pp.getValue(type+".smsTemplateCode"));
		
	}
}
