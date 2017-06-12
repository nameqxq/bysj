package com.quxiqi.bysj.service.common;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.common.SpringBeanInterface;
import com.quxiqi.bysj.service.AbstractService;
import com.quxiqi.bysj.service.DefaultService;
import com.quxiqi.bysj.service.ServiceHandler;
import com.quxiqi.bysj.util.MyBeanFactory;
import com.quxiqi.bysj.util.ResultMsgUtil;

import net.sf.json.JSONObject;

@Service("baseService")
public class BaseServiceImpl extends AbstractService {

	@Override
	public ResultMsg execute(String serviceKey, ServiceHandler sh) {
		//处理serviceKey
		String[] serviceName = getServiceName(serviceKey);
		ResultMsg result = null;
		try {
			//获取class对象
			Class<?> forName = Class.forName(serviceName[0]);
			
			Object instance = 
					SpringBeanInterface.APPLICATION_CONTEXT.getBean(forName);
			//获取相应镜像方法
			Method method = forName.getMethod(serviceName[1],sh.getParamTypes());
			//方法自调用
			result = (ResultMsg) method.invoke(instance, sh.getParams());
		}catch (Exception e){
			result = ResultMsgUtil.getResultMsg();
			e.printStackTrace();
			result.setCode(ResultMsg.FAIL);
			Throwable cause = e.getCause();
			if(cause!=null)
				result.setReason(cause.getMessage());
			else
				result.setReason(e.getMessage());
		}
		
		return result;
	}

	@Override
	public <I> ResultMsg execute(String serviceKey, Class<I> i, String sqlName, 
			JSONObject param,User user,List<MultipartFile> files) {
		//处理serviceKey，返回包含service全类名及方法名的字符串数组
		String[] serviceName = getServiceName(serviceKey);
		ResultMsg result = null;
		try {
			//获取具体service的class对象
			Class<?> forName = Class.forName(serviceName[0]);
			//获取方法入参
			I paramObj = null;
			if(i!=null){
				paramObj = i.cast(JSONObject.toBean(param, i));
				//防止入参为null时传入null，应传空对象
				if(paramObj==null)
					paramObj = MyBeanFactory.getBean(i);
			}
			//获取service对象
			Object instance = APPLICATION_CONTEXT.getBean(forName);
			
			if(sqlName==null||"".equals(sqlName)){
				//常规调用（send方法）
				DefaultService defaultService = DefaultService.class.cast(instance);
				defaultService.setUser(user);
				if(files==null||files.isEmpty()){
					//无上传文件的普通调用
					if(i==null){
						//获取相应镜像方法
						Method method = forName.getMethod(serviceName[1]);
						//方法自调用
						result = (ResultMsg) method.invoke(defaultService);
					}else{
						//获取相应镜像方法
						Method method = forName.getMethod(serviceName[1],i);
						//方法自调用
						result = (ResultMsg) method.invoke(defaultService, paramObj);
					}
				}else{
					if(i==null){
						Method method = forName.getMethod(serviceName[1],List.class);
						result = (ResultMsg) method.invoke(defaultService,files);
					}else{
						Method method = forName.getMethod(serviceName[1],i,List.class);
						result = (ResultMsg) method.invoke(defaultService, paramObj,files);
					}
				}
			}else{
				//commonservice 
				//获取相应镜像方法
				Method method = forName.getMethod(serviceName[1],Object.class,String.class);
				//方法自调用
				result = (ResultMsg) method.invoke(instance, paramObj,sqlName);
			}
		}catch (Exception e){
			result = ResultMsgUtil.getResultMsg();
			e.printStackTrace();
			result.setCode(ResultMsg.FAIL);
			Throwable cause = e.getCause();
			if(cause!=null)
				result.setReason(cause.getMessage());
			else
				result.setReason(e.getMessage());
		}
		
		return result;
	}

}
