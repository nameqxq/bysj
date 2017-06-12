package com.quxiqi.bysj.bean.ALDY;

public class ALDYRsp {
	private String request_id;
	private ALDYRspBody alibaba_aliqin_fc_sms_num_send_response;
	private ALDYError error_response;
	public ALDYRsp() {
	}
	
	public ALDYRsp(String request_id, ALDYRspBody alibaba_aliqin_fc_sms_num_send_response, ALDYError error_response) {
		super();
		this.request_id = request_id;
		this.alibaba_aliqin_fc_sms_num_send_response = alibaba_aliqin_fc_sms_num_send_response;
		this.error_response = error_response;
	}

	public ALDYError getError_response() {
		return error_response;
	}
	public void setError_response(ALDYError error_response) {
		this.error_response = error_response;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public ALDYRspBody getAlibaba_aliqin_fc_sms_num_send_response() {
		return alibaba_aliqin_fc_sms_num_send_response;
	}
	public void setAlibaba_aliqin_fc_sms_num_send_response(ALDYRspBody alibaba_aliqin_fc_sms_num_send_response) {
		this.alibaba_aliqin_fc_sms_num_send_response = alibaba_aliqin_fc_sms_num_send_response;
	}

	@Override
	public String toString() {
		return "ALDYRsp [request_id=" + request_id + ", alibaba_aliqin_fc_sms_num_send_response="
				+ alibaba_aliqin_fc_sms_num_send_response + ", error_response=" + error_response + "]";
	}
	
}
