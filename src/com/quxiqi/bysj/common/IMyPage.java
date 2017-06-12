package com.quxiqi.bysj.common;

public interface IMyPage {
	public String getOrderBy();
	public void setOrderBy(String orderBy);
	public Integer getPages() ;
	public void setPages(Integer pages) ;
	public Long getTotal() ;
	public void setTotal(Long total) ;
	public Integer getPageNumber() ;
	public void setPageNumber(Integer pageNumber) ;
	public Integer getPageSize() ;
	public void setPageSize(Integer pageSize) ;
}
