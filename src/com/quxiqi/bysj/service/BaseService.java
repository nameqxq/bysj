package com.quxiqi.bysj.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.common.SpringBeanInterface;

import net.sf.json.JSONObject;

public interface BaseService extends SpringBeanInterface {
	/**
	 * 
	 * @param serviceKey 简单类名.方法名
	 * @param sh 方法入参类型及入参
	 * @return 返回值
	 */
	ResultMsg execute(String serviceKey,ServiceHandler sh);
	/**
	 * 
	 * @param serviceKey 对象名.方法名
	 * @param i 入参类型
	 * @param sqlName sql名，如果此指不为空，则代表为getJsonArray/Object方法
	 * 发送的请求，其对应的service对象必为CommonService
	 * @param param 方法入参
	 * @param user 当前登录用户对象
	 * @param 文件 
	 * @return
	 */
	<I> ResultMsg execute(String serviceKey,Class<I> i,
			String sqlName,JSONObject param,User user,List<MultipartFile> files);
	
}
