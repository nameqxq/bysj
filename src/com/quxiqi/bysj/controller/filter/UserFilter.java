package com.quxiqi.bysj.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.quxiqi.bysj.bean.User;
@WebFilter(urlPatterns={"/stu/*","/common/ajax","/common/download","/common/upload","/admin/*"})
public class UserFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		HttpServletResponse resp = (HttpServletResponse) arg1;
		//异步请求将携带webUrl参数
		String webUrl = req.getParameter("webUrl");
		//请求发起时页面对应的url文件路径
		String servletPath = req.getServletPath();
		//新闻和主页无需登录
		if(webUrl!=null&&(webUrl.indexOf("/index")>=0||webUrl.contains("/announcementContext"))){
			arg2.doFilter(arg0, arg1);
			return;
		}
		//无权限页面当然不需要权限
		if(servletPath!=null&&servletPath.endsWith("/no_access")){
			arg2.doFilter(arg0, arg1);
			return;
		}
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		//用户已登录
		if(user!=null&&!"".equals(user.getUserName())
				&&!"".equals(user.getPhoneNumber())){
			String usertypes = user.getUsertypes();
			if(usertypes!=null){
				if(webUrl==null){
					//同步请求时，上传下载只需要用户登录状态即可进行
					if((servletPath!=null&&
							(servletPath.endsWith("/download")||servletPath.endsWith("/upload")))){
						arg2.doFilter(arg0, arg1);
						return;
					}
					if((servletPath.contains("/stu/")&&"10".equals(usertypes))||
							servletPath.contains("/admin/")&&"20".equals(usertypes)
								||servletPath.contains("/repairBillContext"))
						arg2.doFilter(arg0, arg1);
					else
						resp.sendRedirect("/no_access");
				}else{
					//异步权限筛选
					if((webUrl.contains("/stu/")&&"10".equals(usertypes))||
							webUrl.contains("/admin/")&&"20".equals(usertypes)
								||webUrl.contains("/repairBillContext"))
						arg2.doFilter(arg0, arg1);
					else
						resp.sendRedirect("/no_access?webUrl="+webUrl);
				}
			}else{
				//用户未登录
				if(webUrl==null||"".equals(webUrl))
					resp.sendRedirect("/index");
				else
					resp.sendRedirect("/no_access?webUrl="+webUrl);
			}
		}else{
			//用户未登录
			if(webUrl==null||"".equals(webUrl))
				resp.sendRedirect("/index");
			else
				resp.sendRedirect("/no_access?webUrl="+webUrl);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
