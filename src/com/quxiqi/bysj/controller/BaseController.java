package com.quxiqi.bysj.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.quxiqi.bysj.bean.AjaxHandler;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.common.SpringBeanInterface;

public interface BaseController extends SpringBeanInterface{
	/**
	 * 异步请求的公共入口
	 * @param ah ajax请求的辅助类
	 * @param session session
	 * @return
	 */
	ResultMsg ajax(AjaxHandler ah,HttpSession session);
	/**
	 * 文件上传
	 * @param ah ajax请求的辅助类
	 * @param session session
	 * @return
	 */
	ResultMsg upload(AjaxHandler ah,HttpSession session);
	/**
	 * 非异步资源下载的公共入口
	 * @param fileName 文件名
	 * @return
	 */
	ResponseEntity<byte[]> download(String fileName);
	/**
	 * 学生端页面跳转
	 * @param file
	 * @param path
	 * @return
	 */
	String accessStu(String file,String path );
	/**
	 * 管理员端页面跳转
	 * @param file
	 * @param path
	 * @return
	 */
	String accessAdmin(String file,String path );
	/**
	 * 公告详情页
	 * @param guid
	 * @return
	 */
	String accessAnnouncement(String guid);
	/**
	 * 维修详情页
	 * @param guid
	 * @return
	 */
	String accessRepairBill(String guid);
}
