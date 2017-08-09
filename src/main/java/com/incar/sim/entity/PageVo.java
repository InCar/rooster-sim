package com.incar.sim.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果模型
 * @author Administrator
 *
 */
public class PageVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9025772527725893078L;
	
	
	
	/**
	 * 页号
	 */
	private int pageNo;
	/**
	 * 页大小
	 */
	private int pageSize;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 总记录数
	 */
	private int totalRecords;
	/**
	 * 当前页数据
	 */
	private List<?> list;
	
	
	/** 
	 * 获取 页号 
	 * @return pageNo 
	 */
	public int getPageNo() {
		return pageNo;
	}
	
	/** 
	 * 设置 页号 
	 * @param pageNo 页号 
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/** 
	 * 获取 页大小 
	 * @return pageSize 
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/** 
	 * 设置 页大小 
	 * @param pageSize 页大小 
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/** 
	 * 获取 总页数 
	 * @return totalPage 
	 */
	public int getTotalPage() {
		return totalPage;
	}
	
	/** 
	 * 设置 总页数 
	 * @param totalPage 总页数 
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	/** 
	 * 获取 总记录数 
	 * @return totalRecords 
	 */
	public int getTotalRecords() {
		return totalRecords;
	}
	
	/** 
	 * 设置 总记录数 
	 * @param totalRecords 总记录数 
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	/** 
	 * 获取 当前页数据 
	 * @return list 
	 */
	public List<?> getList() {
		return list;
	}
	
	/** 
	 * 设置 当前页数据 
	 * @param list 当前页数据 
	 */
	public void setList(List<?> list) {
		this.list = list;
	}
	
	

}
