package com.quxiqi.bysj.util;

import com.quxiqi.bysj.service.ServiceHandler;

public class ServiceHandlerFactory {
	public static ServiceHandler getServiceHandler(){
		return new ServiceHandler();
	}
}
