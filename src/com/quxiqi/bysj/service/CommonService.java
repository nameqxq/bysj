package com.quxiqi.bysj.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import com.quxiqi.bysj.bean.ResultMsg;

/**
 * 用来处理常规异步简单查询请求,及非异步文件下载
 * @author Quxq
 *
 */
public interface CommonService {
	/**
	 * 获取单条数据
	 * @param t 入参类型
	 * @param sqlName 查询sql映射名
	 * @return
	 * @throws Exception
	 */
	<T> ResultMsg getJsonObject(T t,String sqlName)throws Exception;
	/**
	 * 获取数组数据
	 * @param t 入参类型
	 * @param sqlName 查询sql映射名
	 * @return
	 * @throws Exception
	 */
	<T> ResultMsg getJsonArray(T t,String sqlName)throws Exception;
	/**
	 * @param path 文件路径
	 * @return
	 */
	ResponseEntity<byte[]> doDownload(String path)throws IOException;
	/**
	 * 获取数量
	 * @param t 入参类型
	 * @param sqlName 查询sql映射名
	 * @return
	 * @throws Exception
	<T> ResultMsg getJsonCount(T t,String sqlName)throws Exception;
	 */
}
