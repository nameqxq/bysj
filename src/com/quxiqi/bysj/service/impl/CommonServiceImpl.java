package com.quxiqi.bysj.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.quxiqi.bysj.bean.MyPage;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.common.IMyPage;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.service.CommonService;
import com.quxiqi.bysj.util.MyBeanFactory;
import com.quxiqi.bysj.util.Property;
import com.quxiqi.bysj.util.ResultMsgUtil;

@Service("commonService")
@Scope("prototype")
public class CommonServiceImpl implements CommonService{
	private static final Property PP = new Property("config/fileMapping.properties"); 
	@Autowired
	private BaseDao baseDao;
	@Override
	public <T> ResultMsg getJsonObject(T t,String sqlName)throws Exception{
		return getJsonHandler(t, sqlName, "object");
	}
	@Override
	public <T> ResultMsg getJsonArray(T t,String sqlName)throws Exception{
		return getJsonHandler( t, sqlName,"array");
	}
	
	private <T> ResultMsg getJsonHandler(T t, String sqlName,String type) throws Exception {
		ResultMsg rm = ResultMsgUtil.getResultMsg();
		@SuppressWarnings("unchecked")
		Class<T> tClass = (Class<T>)t.getClass();
		switch(type){
			case "object":{
				T result = baseDao.execute(sqlName, t, tClass, tClass);
				rm.setData(result);
				break;
			}
			case "array":{
				if(!(t instanceof IMyPage))
					throw new Exception(tClass+"不是可分页的类型！");
				IMyPage thisPage = IMyPage.class.cast(t);
				PageHelper.startPage(thisPage.getPageNumber(), thisPage.getPageSize());
				@SuppressWarnings("unchecked")
				Page<T> resultList = baseDao.execute(sqlName, t, tClass, Page.class);
				MyPage myPage = MyBeanFactory.getBean(MyPage.class);
				myPage.setTotal(resultList.getTotal());
				myPage.setPageNumber(resultList.getPageNum());
				myPage.setPageSize(resultList.getPageSize());
				myPage.setPages(resultList.getPages());
				rm.setMyPage(myPage);
				rm.setData(resultList);
				break;
			}
			/*case "count":{
				Integer count = baseDao.execute(sqlName, t, tClass, Integer.class);
				rm.setData(count);
				break;
			}*/
			default :{
				throw new Exception("CommonService--switch没找到匹配的case！");
			}
		}
		rm.setCode("11111");
		rm.setReason("查询成功！");
		return rm;
	}
	
	@Override
	public ResponseEntity<byte[]> doDownload(String path) throws IOException{
		String p = this.getClass().getResource("/").toString().split("/WEB-INF")[0];
		String temp =p + PP.getValue(path);
		String relPath = temp.substring(6);
		
		if(relPath==null||"".equals(relPath))
			return null;
		File file=new File(relPath); 
		if(!file.isFile())
			return null;
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String(file.getName());
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                headers, HttpStatus.CREATED);   
	}
	
}
