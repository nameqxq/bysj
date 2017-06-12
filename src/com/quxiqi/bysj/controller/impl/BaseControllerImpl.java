package com.quxiqi.bysj.controller.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.quxiqi.bysj.bean.AjaxHandler;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.controller.BaseController;
import com.quxiqi.bysj.service.BaseService;
import com.quxiqi.bysj.service.CommonService;
import com.quxiqi.bysj.util.ResultMsgUtil;

import net.sf.json.JSONObject;

@Controller
public class BaseControllerImpl implements BaseController {
	@Autowired
	private BaseService baseService;
	@Autowired
	private CommonService commonService;
	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
	
	@Override
	@ResponseBody
	@RequestMapping("/common/ajax")
	public ResultMsg ajax(@ModelAttribute AjaxHandler ah,HttpSession session) {
		User user = (User) session.getAttribute("user");
		ResultMsg rm = null;;
		try {
			//获取具体service方法的入参类型，便于BaseService中调用方法
			String pClass = ah.getParamClass();
			Class<?> c = null;
			if(pClass!=null){
				c = Class.forName(pClass);
			}
			//service层统一入口，其中将通过serviceKey找到具体service对象并执行相应方法
			rm = baseService.execute(ah.getServiceKey(), 
					c,
						ah.getSqlName(), JSONObject.fromObject(ah.getParam()),user,null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			rm = ResultMsgUtil.getResultMsg(ResultMsg.FAIL, e.getMessage(), null, null);
		}
		return rm;
	};
	
	@Override
	@ResponseBody
	@RequestMapping("/common/upload")
	public ResultMsg upload(AjaxHandler ah, HttpSession session) {
		User user = (User) session.getAttribute("user");
		ResultMsg rm = null;;
		try {
			List<MultipartFile>  files = null;
			if(ah.getHaveFiles()){
				files = new ArrayList<>();
				if(ah.getFile1()!=null)
					files.add(ah.getFile1());
				if(ah.getFile2()!=null)
					files.add(ah.getFile2());
				if(ah.getFile3()!=null)
					files.add(ah.getFile3());
			}
			String pClass = ah.getParamClass();
			Class<?> c = null;
			if(pClass!=null){
				c = Class.forName(pClass);
			}
			rm = baseService.execute(ah.getServiceKey(), 
					c,
						ah.getSqlName(), JSONObject.fromObject(ah.getParam()),user,files);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			rm = ResultMsgUtil.getResultMsg(ResultMsg.FAIL, e.getMessage(), null, null);
		}
		return rm;
	}
	
	@Override
	@RequestMapping("/common/download")
	public ResponseEntity<byte[]> download(@RequestParam("fileName")String fileName){
		ResponseEntity<byte[]> re = null;
		try {
			re = commonService.doDownload(fileName);
		} catch (IOException e) {
			re = new ResponseEntity<byte[]>(e.getMessage().getBytes(), HttpStatus.ACCEPTED);
			e.printStackTrace();
		}
		return re;
	}
	
	@Override
	@RequestMapping("/stu/{file}/{path}")
	public String accessStu(@PathVariable("file")String file,
			@PathVariable("path")String path ){
		String realPath = "stu/"+file+"/"+path+"/"+path;
		return realPath;
	}
	
	@Override
	@RequestMapping("/admin/{file}/{path}")
	public String accessAdmin(@PathVariable("file")String file,
			@PathVariable("path")String path ){
		String realPath = "admin/"+file+"/"+path+"/"+path;
		return realPath;
	}
	
	@Override
	@RequestMapping("/common/announcementContext/{guid:^\\w{32}$}/context")
	public String accessAnnouncement(@PathVariable("guid")String guid) {
		return "common/announcementContext/announcementContext";
	}

	@Override
	@RequestMapping("/common/repairBillContext/{guid:^\\w{32}$}/context")
	public String accessRepairBill(@PathVariable("guid")String guid) {
		return "common/repairBillContext/repairBillContext";
	}
}
