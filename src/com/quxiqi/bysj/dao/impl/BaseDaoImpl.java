package com.quxiqi.bysj.dao.impl;

import java.lang.reflect.Method;

import org.springframework.stereotype.Repository;

import com.quxiqi.bysj.Exception.DaoException;
import com.quxiqi.bysj.dao.AbstractDao;

@Repository("baseDao")
public class BaseDaoImpl extends AbstractDao {

	@Override
	public <R, I> R execute(String s, Object obj, Class<I> i, Class<R> r) throws Exception {
		String[] sqlNames = getSqlName(s);
		//获取mapper接口的Class对象
		Object result = null;
		try{
			//获取class对象
			Class<?> forName = Class.forName(sqlNames[0]);
			//获取mapper对应的方法
			Method method = forName.getMethod(sqlNames[1], i);
			//调用mapper对应方法的镜像方法
			Object mapper = APPLICATION_CONTEXT.getBean(forName);
			result = method.invoke(mapper, obj);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			throw new ClassNotFoundException("动态调用失败,原因:类没有找到======类名:" + sqlNames[0] + "方法名:" + sqlNames[1] + "======");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new NoSuchMethodException("动态调用失败,原因:没有这样的方法======类名:" + sqlNames[0] + "方法名:" + sqlNames[1] + "======");
		}catch(Exception e){
			String errorMsg = "====动态调用dao失败===>mapper映射："+sqlNames[0]+",方法："+sqlNames[1]+",参数："+obj;
			DaoException d = new DaoException(errorMsg);
			d.initCause(e);
			throw d;
 		}
		if(r!=null&&result!=null)
			return  r.cast(result);
		return null;
	}
	
}
