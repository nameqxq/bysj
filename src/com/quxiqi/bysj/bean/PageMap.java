package com.quxiqi.bysj.bean;

import java.util.HashMap;

import com.quxiqi.bysj.common.IMyPage;
import com.quxiqi.bysj.util.StringUtil;

public class PageMap extends HashMap<String,Object> implements IMyPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3641077858137688945L;

	@Override
	public String getOrderBy() {
		return StringUtil.getString(this, "orderBy");
	}

	@Override
	public Integer getPages() {
		return new Integer(StringUtil.getString(this, "pages"));
	}

	@Override
	public Long getTotal() {
		return new Long(StringUtil.getString(this, "total"));
	}

	@Override
	public Integer getPageNumber() {
		return new Integer(StringUtil.getString(this, "pageNumber"));
	}

	@Override
	public Integer getPageSize() {
		return new Integer(StringUtil.getString(this, "pageSize"));
	}

	@Override
	public void setOrderBy(String orderBy) {
		this.put("orderBy", orderBy);
	}

	@Override
	public void setPages(Integer pages) {
		this.put("pages", pages);
	}

	@Override
	public void setTotal(Long total) {
		this.put("total", total);
	}

	@Override
	public void setPageNumber(Integer pageNumber) {
		this.put("pageNumber", pageNumber);
	}

	@Override
	public void setPageSize(Integer pageSize) {
		this.put("pageSize", pageSize);
	}

}
