package com.quxiqi.bysj.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quxiqi.bysj.bean.CommonMsg;
import com.quxiqi.bysj.bean.Dormitory;
import com.quxiqi.bysj.bean.EleLog;
import com.quxiqi.bysj.bean.ElePrice;
import com.quxiqi.bysj.bean.MoneyLog;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.bean.WaterLog;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.service.DefaultService;
import com.quxiqi.bysj.util.DateUtil;
import com.quxiqi.bysj.util.MessageUtil;
import com.quxiqi.bysj.util.MyBeanFactory;
import com.quxiqi.bysj.util.ResultMsgUtil;
import com.quxiqi.bysj.util.StringUtil;

@Service
public class WaterEleService extends DefaultService {
	@Override
	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		super.setBaseDao(baseDao);
	}
	//stu
	public ResultMsg pay(MoneyLog moneyLog) throws Exception{
		User user = getUser();
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		String dormitoryNumber = user.getDormitory().getDormitoryNumber();
		Class<Dormitory> d= Dormitory.class;
		Dormitory nowDorInsert = MyBeanFactory.getBean(d);
		nowDorInsert.setDormitoryNumber(dormitoryNumber);
		Dormitory nowDor = baseDao.execute("DormitoryMapper.selectOne", nowDorInsert, d, d);

		moneyLog.setDormitoryNumber(dormitoryNumber);
		moneyLog.setPayType("10");
		moneyLog.setPayUser(user.getUserName());
		BigDecimal nowMoney = moneyLog.getPayMoney().add(nowDor.getMoney());
		nowDor.setMoney(nowMoney);
		baseDao.execute("DormitoryMapper.update", nowDor, d, null);
		moneyLog.setMoney(nowMoney);
		moneyLog.setBuildingNumber(nowDor.getBuildingNumber());
		moneyLog.setLayerNumber(nowDor.getLayerNumber());
		baseDao.execute("MoneyLogMapper.insert", moneyLog, MoneyLog.class, null);
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData(moneyLog.getPayMoney());
		return rs;
	}
	
	//admin  录入信息
	public ResultMsg enteringData() throws Exception {
		return enteringData(null);
	} 
	//admin  录入信息
	public ResultMsg enteringData(List<MultipartFile> files) throws Exception {
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		if(files==null||files.isEmpty()){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("文件上传失败！");
			return rs;
		}
		MultipartFile mf = files.get(0);
		String filename = mf.getOriginalFilename();
		InputStream is = mf.getInputStream();
		
		
		
		FutureTask<Iterator<Row>> future = new FutureTask<>(new Callable<Iterator<Row>>(){

			@Override
			public Iterator<Row> call() throws Exception {
				Iterator<Row> iterator= null;
				if(filename.endsWith("xls")){
					HSSFWorkbook swb = new HSSFWorkbook(is);
					HSSFSheet sheetAt = swb.getSheetAt(0);
					iterator = sheetAt.iterator();
				}else if(filename.endsWith("xlsx")){
					XSSFWorkbook xwb = new XSSFWorkbook(is);
					XSSFSheet sheetAt = xwb.getSheetAt(0);
					iterator = sheetAt.iterator();
				}
				return iterator;
			}
			
		});
		
		new Thread(future).start();
		
		List<Integer> badLine = new ArrayList<>();
		List<EleLog> elList = new ArrayList<>();
		List<WaterLog> wlList = new ArrayList<>();
		Map<String,EleLog> elMap = new HashMap<>();
		Map<String,WaterLog> wlMap = new HashMap<>();
		boolean flag = true;
		//获取用户信息
		String userName = getUser().getUserName();
		Calendar c=Calendar.getInstance();
		String year = c.get(Calendar.YEAR)+"";
		String month = c.get(Calendar.MONTH)+1+"";
		Iterator<Row> iterator = future.get();
		if(iterator==null){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("文件格式错误！");
			return rs;
		}
		
		List<Callable<Boolean>> callableList = new LinkedList<>();
		while(iterator.hasNext()){
			Row row = iterator.next();
			
			if(flag){
				//标题跳过
				flag = false;
				continue;
			}
			
			/*EleLog el = MyBeanFactory.getBean(EleLog.class);
			WaterLog wl = MyBeanFactory.getBean(WaterLog.class);
			String dormitoryNumber = row.getCell(0).toString();
			if(dormitoryNumber==null||"".equals(dormitoryNumber)){
				break;
			}
			if(!dormitoryNumber.matches("^(\\d{2}#){2}\\d{2}$")){
				badLine.add(index);
				continue;
			}*/
			
			
			callableList.add(
					new WaterEleHandler(row, badLine, elList, wlList, elMap, wlMap, year, month, userName));
			
			
			
			/*try{
				String eleNumber = row.getCell(1).toString();
				String waterNumber = row.getCell(2).toString();
				BigDecimal ele = new BigDecimal(eleNumber);
				BigDecimal water = new BigDecimal(waterNumber);
				String[] split = dormitoryNumber.split("#");
				el.setDormitoryNumber(dormitoryNumber);
				el.setEleNumber(ele);
				el.setYear(year);
				el.setMonth(month);
				el.setEnteringPerson(userName);
				el.setBuildingNumber(split[0]);
				el.setLayerNumber(split[1]);
				
				wl.setDormitoryNumber(dormitoryNumber);
				wl.setWaterNumber(water);
				wl.setYear(year+"");
				wl.setMonth(month+"");
				wl.setEnteringPerson(userName);
				wl.setBuildingNumber(split[0]);
				wl.setLayerNumber(split[1]);
				
				elList.add(el);
				wlList.add(wl);
				elMap.put(dormitoryNumber, el);
				wlMap.put(dormitoryNumber, wl);
			}catch(Exception e){
				badLine.add(index);
				continue;
			}*/
		}
		ExecutorService es = Executors.newCachedThreadPool();
		es.invokeAll(callableList);
		es.shutdown();
		
		if(!badLine.isEmpty()){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("信息录入失败，第"+badLine+"行的信息不符合规范！");
			return rs;
		}
		
		
		if(elList.isEmpty()){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("传入信息为空！");
			return rs;
		}
		
		//获取本月水电收费标准
		Class<ElePrice> epClass = ElePrice.class;
		ElePrice aPrice = MyBeanFactory.getBean(epClass);
		aPrice.setMonth(month);
		aPrice.setYear(year);
		@SuppressWarnings("unchecked")
		List<ElePrice> priceList = baseDao.execute("ElePriceMapper.select", aPrice, epClass, List.class);
		ElePrice price = null;
		if(priceList!=null&&!priceList.isEmpty()) 
			price = priceList.get(0);
		if(price==null){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("尚未上传本月收费标准！（"+year+"年"+month+"月）");
			return rs;
		}
		BigDecimal elePrice = price.getElePrice();
		BigDecimal waterPrice = price.getWaterPrice();
		
		//获取现有水电钱信息
		Class<Dormitory> dClass = Dormitory.class;
		Dormitory dormitory = MyBeanFactory.getBean(dClass);
		@SuppressWarnings("unchecked")
		List<Dormitory> dorList = baseDao.execute("DormitoryMapper.select", dormitory, dClass, List.class);
		
		//处理水电等信息
		List<String> badDorNumber2 = new ArrayList<>();
		List<Dormitory> insertDors = new ArrayList<>();
		List<MoneyLog> insertMl = new ArrayList<>(); 
		for(Dormitory dor :dorList){
			String dormitoryNumber = dor.getDormitoryNumber();
			
			EleLog eleLog = elMap.get(dormitoryNumber);
			WaterLog waterLog = wlMap.get(dormitoryNumber);
			
			if(eleLog==null||waterLog==null)
				continue;
			BigDecimal newEleNum = eleLog.getEleNumber();
			BigDecimal oldEleNum = dor.getEleNumber();
			BigDecimal newWaterNum = waterLog.getWaterNumber();
			BigDecimal oldWaterNum = dor.getWaterNumber();
			if(newEleNum.compareTo(oldEleNum)<0||
					newWaterNum.compareTo(oldWaterNum)<0){
				badDorNumber2.add(dormitoryNumber);
				continue;
			}
			
			BigDecimal eleMoney = newEleNum.subtract(oldEleNum).multiply(elePrice);
			BigDecimal waterMoney = newWaterNum.subtract(oldWaterNum).multiply(waterPrice);
			BigDecimal payMoney = eleMoney.add(waterMoney);
			BigDecimal newMoney = dor.getMoney().subtract(payMoney);
			
			Dormitory newDor = MyBeanFactory.getBean(dClass);
			MoneyLog moneyLog = MyBeanFactory.getBean(MoneyLog.class);
			newDor.setDormitoryNumber(dormitoryNumber);
			newDor.setEleNumber(newEleNum);
			newDor.setWaterNumber(newWaterNum);
			newDor.setMoney(newMoney);
			
			moneyLog.setDormitoryNumber(dormitoryNumber);
			moneyLog.setMoney(newMoney);
			moneyLog.setPayMoney(payMoney);
			moneyLog.setPayType("00");
			moneyLog.setPayUser(userName);
			String[] split = dormitoryNumber.split("#");
			moneyLog.setBuildingNumber(split[0]);
			moneyLog.setLayerNumber(split[1]);
			
			insertDors.add(newDor);
			insertMl.add(moneyLog);
		}
		
		if(!badDorNumber2.isEmpty()){
			rs.setCode(ResultMsg.FAIL);
			rs.setReason("信息录入失败，宿舍"+badDorNumber2+"的水电读数小于原读数！");
			return rs;
		}
		
		//更新宿舍表信息
		baseDao.execute("DormitoryMapper.updateList", insertDors, List.class, null);
		//更新费用日志信息
		baseDao.execute("MoneyLogMapper.insertList", insertMl, List.class, null);
		//更新水表日志信息
		baseDao.execute("WaterLogMapper.insertList", wlList, List.class, null);
		//更新电表日志信息
		baseDao.execute("EleLogMapper.insertList", elList, List.class, null);
		
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData("录入成功！");
		return rs;
	}
	
	
	public ResultMsg sendMsg(HashMap<String,Object> map) throws Exception{
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		String minStr = StringUtil.getString(map, "min");
		BigDecimal min = new BigDecimal(minStr);
		
		Dormitory dor = MyBeanFactory.getBean(Dormitory.class);
		dor.setMoney(min);
		
		@SuppressWarnings("unchecked")
		List<User> userList = baseDao.execute("UserMapper.selectForSendMsg", dor, Dormitory.class, List.class);
		if(userList==null||userList.isEmpty()){
			rs.setCode(ResultMsg.SUCCESS);
			rs.setData("没有符合条件的用户！");
			return rs;
		}
		CommonMsg comMsg = MyBeanFactory.getBean(CommonMsg.class);
		for(User u:userList)
			comMsg.addPhoneNumber(u.getPhoneNumber());
		comMsg.putParam("sendTime", DateUtil.nowDate(DateUtil.DEFAULT_DATE_PATTERN));
		comMsg.putParam("minMoney", minStr);
		comMsg.setType("send");
		return MessageUtil.commonSendMsg(comMsg);
	}
	
	
	//admin 保存水电费修改
	public ResultMsg updateElePrice(ElePrice elePrice) throws Exception{
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		baseDao.execute("ElePriceMapper.update", elePrice, ElePrice.class, null);
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData("修改成功！");
		return rs;
	}
	//admin 新增水电费信息
	public ResultMsg addElePrice(ElePrice elePrice) throws Exception{
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		baseDao.execute("ElePriceMapper.insert", elePrice, ElePrice.class, null);
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData("新增成功！");
		return rs;
	}
	
	private class WaterEleHandler implements Callable<Boolean>{
		private final Row row;
		private final List<Integer> badLine;
		private final List<EleLog> elList;
		private final List<WaterLog> wlList;
		private final Map<String,EleLog> elMap;
		private final Map<String,WaterLog> wlMap;
		private final String year;
		private final String month;
		private final String userName;
		
		public WaterEleHandler(Row row, List<Integer> badLine, List<EleLog> elList, List<WaterLog> wlList,
				Map<String, EleLog> elMap, Map<String, WaterLog> wlMap, String year, String month, String userName) {
			super();
			this.row = row;
			this.badLine = badLine;
			this.elList = elList;
			this.wlList = wlList;
			this.elMap = elMap;
			this.wlMap = wlMap;
			this.year = year;
			this.month = month;
			this.userName = userName;
		}

		@Override
		public Boolean call() throws Exception {
			int index = row.getRowNum();
			EleLog el = MyBeanFactory.getBean(EleLog.class);
			WaterLog wl = MyBeanFactory.getBean(WaterLog.class);
			String dormitoryNumber = row.getCell(0).toString();
			if(dormitoryNumber==null||"".equals(dormitoryNumber)){
				return false;
			}
			if(!dormitoryNumber.matches("^(\\d{2}#){2}\\d{2}$")){
				badLine.add(index);
			}
			
			try{
				String eleNumber = row.getCell(2).toString();
				String waterNumber = row.getCell(1).toString();
				BigDecimal ele = new BigDecimal(eleNumber);
				BigDecimal water = new BigDecimal(waterNumber);
				String[] split = dormitoryNumber.split("#");
				el.setDormitoryNumber(dormitoryNumber);
				el.setEleNumber(ele);
				el.setYear(year);
				el.setMonth(month);
				el.setEnteringPerson(userName);
				el.setBuildingNumber(split[0]);
				el.setLayerNumber(split[1]);
				
				wl.setDormitoryNumber(dormitoryNumber);
				wl.setWaterNumber(water);
				wl.setYear(year+"");
				wl.setMonth(month+"");
				wl.setEnteringPerson(userName);
				wl.setBuildingNumber(split[0]);
				wl.setLayerNumber(split[1]);
				
				elList.add(el);
				wlList.add(wl);
				elMap.put(dormitoryNumber, el);
				wlMap.put(dormitoryNumber, wl);
			}catch(Exception e){
				badLine.add(index);
				return false;
			}
			return true;
		}
	}
}
