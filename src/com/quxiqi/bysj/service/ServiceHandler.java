package com.quxiqi.bysj.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceHandler {
	private List<Class<?>> paramTypes = new ArrayList<>();
	private List<Object> params = new ArrayList<>();
	
	public <T> void putParam(Class<T> c,T t){
		paramTypes.add(c);
		params.add(t);
	}
	
	public Class<?>[] getParamTypes(){
		return paramTypes.toArray(new Class<?>[paramTypes.size()]);
	}
	
	public String paramTypesToString(){
		return Arrays.toString(getParamTypes());
	}
	
	public Object[] getParams(){
		return params.toArray(new Object[params.size()]);
	}
	
	public String paramsToString(){
		return Arrays.toString(getParams());
	}
	@Override
	public String toString() {
		return "ServiceHandler [paramType=" + paramTypes + ", param=" + params + "]";
	}
}
