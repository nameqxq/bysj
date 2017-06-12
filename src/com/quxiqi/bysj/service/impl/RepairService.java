package com.quxiqi.bysj.service.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quxiqi.bysj.bean.RepairBill;
import com.quxiqi.bysj.bean.RepairBillLog;
import com.quxiqi.bysj.bean.ResultMsg;
import com.quxiqi.bysj.bean.User;
import com.quxiqi.bysj.dao.BaseDao;
import com.quxiqi.bysj.service.DefaultService;
import com.quxiqi.bysj.util.DateUtil;
import com.quxiqi.bysj.util.MyBeanFactory;
import com.quxiqi.bysj.util.ResultMsgUtil;
@Service
public class RepairService extends DefaultService {
	@Override
	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		super.setBaseDao(baseDao);
	}
	public ResultMsg submitRepairBill(RepairBill repairBill) throws Exception {
		return submitRepairBill(repairBill,null);
	}
	public ResultMsg submitRepairBill(RepairBill repairBill,List<MultipartFile> files) throws Exception {
		User user = getUser();
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		String guid = UUID.randomUUID().toString().replaceAll("-","");
		String userName = user.getUserName();
		String status = "10";
		String dormitoryNumber = user.getDormitory().getDormitoryNumber();
		repairBill.setStatus(status);
		repairBill.setModifiPerson(userName);
		repairBill.setGuid(guid);
		repairBill.setUserName(userName);
		repairBill.setDormitoryNumber(dormitoryNumber);
		if(files!=null){
			String dirPath = "/image/repairBill/"+user.getUserName()
					+"/"+DateUtil.nowDate(DateUtil.DEFAULT_DATE_PATTERN)+"/";
			for(int i=0,len=files.size();i<len;i++){
				MultipartFile mf = files.get(i);
				if(mf.isEmpty())
					continue;
				String fileName = guid+"_"+(i+1)+"."+mf.getOriginalFilename().split("[.]")[1];
				String realPath = UPLOAD_PATH+dirPath;
				String postPath = "/upload"+dirPath+fileName;
				File dir = new File(realPath);
				dir.mkdirs();
				File realFile = new File(dir,fileName);
				mf.transferTo(realFile);
				switch (i) {
				case 0:{
					repairBill.setImage1(postPath);
					break;
				}
				case 1:{
					repairBill.setImage2(postPath);
					break;
				}
				case 2:{
					repairBill.setImage3(postPath);
					break;
				}
				default:
					break;
				}
			}
		}
		
		baseDao.execute("RepairBillMapper.insert", repairBill, RepairBill.class, null);
		
		RepairBillLog rbl = MyBeanFactory.getBean(RepairBillLog.class);
		rbl.setCreatePerson(userName);
		rbl.setRepairBillGuid(guid);
		rbl.setStatus(status);
		
		baseDao.execute("RepairBillLogMapper.insert", rbl, RepairBillLog.class, null);
		
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData("提交成功！");
		return rs;
	}
	
	public ResultMsg finish(RepairBill repairBill) throws Exception{
		return updateHandler(repairBill,"30");
	}
	
	/**
	 * admin
	 */
	public ResultMsg cancellation(RepairBill repairBill) throws Exception{
		return updateHandler(repairBill,"00");
	}
	/**
	 *admin
	 */
	public ResultMsg pass(RepairBill repairBill) throws Exception{
		return updateHandler(repairBill,"20");
	}
	
	private ResultMsg updateHandler(RepairBill repairBill,String status) throws Exception {
		ResultMsg rs = ResultMsgUtil.getResultMsg();
		repairBill.setStatus(status);
		repairBill.setRefuseDate(DateUtil.currentDate());
		String userName = getUser().getUserName();
		repairBill.setModifiPerson(userName);
		baseDao.execute("RepairBillMapper.update", repairBill, RepairBill.class, null);
		
		RepairBillLog rbl = MyBeanFactory.getBean(RepairBillLog.class);
		rbl.setRepairBillGuid(repairBill.getGuid());
		rbl.setStatus(status);
		rbl.setCreatePerson(userName);
		rbl.setRemark(repairBill.getRemark());
		
		baseDao.execute("RepairBillLogMapper.insert", rbl, RepairBillLog.class, null);
		
		rs.setCode(ResultMsg.SUCCESS);
		rs.setData("操作成功！");
		return rs;
	}
}
