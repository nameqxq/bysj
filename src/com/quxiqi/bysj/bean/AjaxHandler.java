package com.quxiqi.bysj.bean;

import org.springframework.web.multipart.MultipartFile;

public class AjaxHandler{
	//类名.方法名
	private String serviceKey;
	//sql映射名
	private String sqlName;
	//请求异步交换的页面的url
	private String webUrl;
	//入参
	private String param;
	//入参类型(类名)
	private String paramClass;
	//是否有文件
	private boolean haveFiles;
	
	private MultipartFile file1;
	
	private MultipartFile file2;
	
	private MultipartFile file3;
	
	public boolean getHaveFiles() {
		return haveFiles;
	}
	public void setHaveFiles(boolean haveFiles) {
		this.haveFiles = haveFiles;
	}
	public MultipartFile getFile1() {
		return file1;
	}
	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}
	public MultipartFile getFile2() {
		return file2;
	}
	public void setFile2(MultipartFile file2) {
		this.file2 = file2;
	}
	public MultipartFile getFile3() {
		return file3;
	}
	public void setFile3(MultipartFile file3) {
		this.file3 = file3;
	}
	public String getServiceKey() {
		return serviceKey;
	}
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getParamClass() {
		return paramClass;
	}
	public void setParamClass(String pClass) {
		if(pClass==null||"null".equals(pClass)||"".equals(pClass))
			return;
		if("map".equals(pClass)){
			this.paramClass = "java.util.HashMap";
			return;
		}
		if(!pClass.startsWith("com.quxiqi.bysj"))
			this.paramClass = "com.quxiqi.bysj.bean."+pClass;
		else
			this.paramClass = pClass;
	}
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	@Override
	public String toString() {
		return "AjaxHandler [serviceKey=" + serviceKey + ", sqlName=" + sqlName + ", webUrl=" + webUrl + ", param="
				+ param + ", paramClass=" + paramClass + ", haveFiles=" + haveFiles + ", file1=" + file1 + ", file2="
				+ file2 + ", file3=" + file3 + "]";
	}

}
