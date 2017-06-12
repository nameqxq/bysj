package com.quxiqi.bysj.bean;

import java.io.Serializable;

import com.quxiqi.bysj.common.IMyPage;

public class MyPage implements Serializable,IMyPage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8978226022173626793L;
	protected Integer pageNumber = 1;
	protected Integer pageSize = 10;
	protected Long total;
	protected Integer pages;
	protected String orderBy;
	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
