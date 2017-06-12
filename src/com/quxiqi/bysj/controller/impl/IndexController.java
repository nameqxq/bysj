package com.quxiqi.bysj.controller.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxiqi.bysj.bean.RegisterMsg;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.service.BaseService;
import com.quxiqi.bysj.service.ServiceHandler;
import com.quxiqi.bysj.util.MessageUtil;
import com.quxiqi.bysj.util.ResultMsgUtil;
import com.quxiqi.bysj.util.ServiceHandlerFactory;
import com.taobao.api.ApiException;

@Controller
public class IndexController {
	@Autowired
	private BaseService baseService;
	
	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@RequestMapping(value="/index")
	public String index(){
		
		return "index/index";
	}
	
	@ResponseBody
	@RequestMapping("/login")
	public ResultMsg login(@ModelAttribute User user,HttpServletRequest req,HttpSession session) throws Exception{
		ServiceHandler sh = ServiceHandlerFactory.getServiceHandler();
		String ip = getIp(req);
		sh.putParam(User.class, user);
		sh.putParam(HttpSession.class, session);
		sh.putParam(String.class, ip);
		return  baseService.execute("IndexService.login", sh);
	}
	@ResponseBody
	@RequestMapping("/logout")
	public ResultMsg logout(HttpSession session) throws Exception{
		ServiceHandler sh = ServiceHandlerFactory.getServiceHandler();
		sh.putParam(HttpSession.class, session);
		return baseService.execute("IndexService.logout", sh);
	}
	@ResponseBody
	@RequestMapping("/register")
	public ResultMsg register(@ModelAttribute User user,HttpServletRequest req,HttpSession session) throws Exception{
		ServiceHandler sh = ServiceHandlerFactory.getServiceHandler();
		String ip = getIp(req);
		sh.putParam(User.class, user);
		sh.putParam(String.class, req.getParameter("code"));
		sh.putParam(HttpSession.class, session);
		sh.putParam(String.class, ip);
		return baseService.execute("IndexService.register",sh);
	}
	@ResponseBody
	@RequestMapping("/sendMsg")
	public ResultMsg sendMsg(@ModelAttribute RegisterMsg rm) throws ApiException{
		return  MessageUtil.sendRegisterVerificationCode(rm);
	}
	
	//用于common.js获取在线用户信息
	@ResponseBody
	@RequestMapping("/onlineUserInfo")
	public ResultMsg onlineUserInfo(HttpSession session){
		return ResultMsgUtil.getResultMsg(ResultMsg.SUCCESS, "", session.getAttribute("user"), null);
	}
	
	@ResponseBody
	@RequestMapping(value="/no_access",params="webUrl")
	public ResultMsg noAccess(){
		return ResultMsgUtil.getResultMsg(ResultMsg.NO_ACCESS, "权限不足！", null, null);
	}
	@RequestMapping(value="/no_access",params="!webUrl")
	public String noAccess(HttpServletResponse resp){
//		resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
		return "no_access/no_access";
	}
	
	private String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if(ip!=null&&!"".equals(ip)&&!"unKnown".equalsIgnoreCase(ip)){
		   //多次反向代理后会有多个ip值，第一个ip才是真实ip
		int index = ip.indexOf(",");
		    if(index != -1){
		        return ip.substring(0,index);
		    }else{
		        return ip;
		   }
		}
		ip = request.getHeader("X-Real-IP");
		if(ip!=null&&!"".equals(ip)&&!"unKnown".equalsIgnoreCase(ip))
		        return ip;
		return request.getRemoteAddr();
	}
	

}
