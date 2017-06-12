package com.quxiqi.bysj.common.aspect;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectForDao {
	
	private Log log;
	public AspectForDao() {
		log = LogFactory.getLog(getClass());
	}
	@Pointcut("execution(public * com.quxiqi.bysj.dao.BaseDao.*(..))")
	public void pointCut(){} 
	
	@Before("pointCut()")
	public void pointCut(JoinPoint joinPoint){
		Object[] args = joinPoint.getArgs();
		String methodName = joinPoint.getSignature().getName();
		String sqlName = null;
		for(int i=0,len=args.length;i<len;i++)
			if(args[i].getClass()==String.class){
				sqlName = String.class.cast(args[i]);
				break;
			}
		String[] splits = sqlName.split("[.]");
		if(splits.length!=2)
			throw new IllegalArgumentException("=====不合法的sql映射=====:"+sqlName);
		log.debug("====开始调用方法-->"+methodName+" ,关键参数：sqlName-->"+sqlName+" ,入参-->"+Arrays.toString(args));
	}
	
	/*@After("pointCut()")
	public void afterMethod(JoinPoint joinPoint){
		String methodName = joinPoint.getSignature().getName();
		log.info("====调用方法结束-->"+methodName);
	}*/
	
	
	@AfterReturning(value="pointCut()",
			returning="result")
	public void afterReturning(JoinPoint joinPoint, Object result){
		String methodName = joinPoint.getSignature().getName();
		log.debug("====调用方法结束--> " + methodName + " ,返回值--> " + result);
	}
	
	
	@AfterThrowing(value="pointCut()",
			throwing="e")
	public void afterThrowing(JoinPoint joinPoint, Exception e){
		String methodName = joinPoint.getSignature().getName();
		log.debug("====调用方法抛出异常--> " + methodName + " ,异常信息-->" + e);
	}
}
