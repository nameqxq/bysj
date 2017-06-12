package com.quxiqi.bysj.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quxiqi.bysj.bean.Dormitory;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.Student;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.service.DefaultService;
import com.quxiqi.bysj.util.MyBeanFactory;
import com.quxiqi.bysj.util.ResultMsgUtil;
import com.quxiqi.bysj.util.StringUtil;
@Service
@Scope("prototype")
public class CheckUserService extends DefaultService {
	@Override
	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		super.setBaseDao(baseDao);
	}
	public ResultMsg enteringData(HashMap<String,Object> map) throws Exception{
		return enteringData(map,null);
	} 
	public ResultMsg enteringData(HashMap<String,Object> map,List<MultipartFile> files) throws Exception{
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		if(files==null||files.isEmpty()){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("文件上传失败！");
			return rs;
		}
		if(map==null||map.get("fileFor")==null){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("参数类型上传失败！");
			return rs;
		}
		String fileFor = StringUtil.getString(map, "fileFor");
		if(!("uploadExcelForDor".equals(fileFor)||"uploadExcelForStu".equals(fileFor))){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("未定义的参数类型！");
			return rs;
		}
		MultipartFile mf = files.get(0);
		String filename = mf.getOriginalFilename();
		InputStream is = mf.getInputStream();
		Iterator<Row> iterator= null;
		if(filename.endsWith("xls")){
			HSSFWorkbook swb = new HSSFWorkbook(is);
			HSSFSheet sheetAt = swb.getSheetAt(0);
			iterator = sheetAt.iterator();
		}else if(filename.endsWith("xlsx")){
			XSSFWorkbook xwb = new XSSFWorkbook(is);
			XSSFSheet sheetAt = xwb.getSheetAt(0);
			iterator = sheetAt.iterator();
		}else{
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("文件格式错误！");
			return rs;
		}
		
		boolean flag = true;
		List<Integer> badRow = new ArrayList<>();
		List<Student> stuList = new ArrayList<>();
		List<Dormitory> dorList = new ArrayList<>();
		//数据解析
		while(iterator.hasNext()){
			Row row = iterator.next();
			int rowNum = row.getRowNum();
			if(flag){
				//标题跳过
				flag = false;
				continue;
			}
			if("uploadExcelForStu".equals(fileFor)){
				try{
					Cell cell0 = row.getCell(0);
					if(cell0==null)
						continue;
					String id = cell0.toString();
					if(!id.matches("^\\d{8}$")){
						badRow.add(rowNum);
						continue;
					}
					String name = row.getCell(1).getStringCellValue();
					int age = (int) row.getCell(2).getNumericCellValue();
					String personNumber = row.getCell(3).toString();
					if(!personNumber.matches("^[1-9]{1}\\d{16}[X,x,0-9]{1}$")){
						badRow.add(rowNum);
						continue;
					}
					String gender = row.getCell(4).toString();
					gender = "男".equals(gender)?"1":"0";
					int schoolYear = (int) row.getCell(5).getNumericCellValue();
					Date admissionDate = row.getCell(6).getDateCellValue();
					String className = row.getCell(7).toString();
					String dormitoryNumber = row.getCell(8).toString();
					if(!dormitoryNumber.matches("^(\\d{2}#){2}\\d{2}$")){
						badRow.add(rowNum);
						continue;
					}
					if(!badRow.isEmpty())
						continue;
					Student stu = MyBeanFactory.getBean(Student.class);
					stu.setAdmissionDate(admissionDate);
					stu.setAge(age);
					stu.setDormitoryNumber(dormitoryNumber);
					stu.setGender(gender);
					stu.setId(id);
					stu.setName(name);
					stu.setClassName(className);
					stu.setPersonNumber(personNumber);
					stu.setSchoolYear(schoolYear);
					stuList.add(stu);
				}catch(RuntimeException e){
					e.printStackTrace();
					badRow.add(rowNum);
					continue;
				}
			}else if("uploadExcelForDor".equals(fileFor)){
				try{
					Cell cell0 = row.getCell(0);
					if(cell0==null)
						continue;
					String dormitoryNumber = cell0.toString();
					if(!dormitoryNumber.matches("^(\\d{2}#){2}\\d{2}$")){
						badRow.add(rowNum);
						continue;
					}
					double money = row.getCell(1).getNumericCellValue();
					double water = row.getCell(2).getNumericCellValue();
					double ele = row.getCell(3).getNumericCellValue();
					if(money<0||water<0||ele<0){
						badRow.add(rowNum);
						continue;
					}
					if(!badRow.isEmpty())
						continue;
					Dormitory dor = MyBeanFactory.getBean(Dormitory.class);
					dor.setDormitoryNumber(dormitoryNumber,true);
					dor.setMoney(new BigDecimal(money));
					dor.setEleNumber(new BigDecimal(ele));
					dor.setWaterNumber(new BigDecimal(water));
					dorList.add(dor);
				}catch(RuntimeException e){
					e.printStackTrace();
					badRow.add(rowNum);
					continue;
				}
			}
		}
		
		if(!badRow.isEmpty()){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("第"+badRow+"行数据有误,请校验！");
			return rs;
		}
		
		if("uploadExcelForStu".equals(fileFor)){
			baseDao.execute("StudentMapper.insertList", stuList, List.class, null);
		}else{
			baseDao.execute("DormitoryMapper.insertList", dorList, List.class, null);
		}
		rs.setData("数据导入成功！");
		rs.setCode(ResultMsg.SUCCESS);
		return rs;
	}
}
