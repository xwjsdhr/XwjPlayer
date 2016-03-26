package com.hw.common.db;

import java.util.List;

public class Pager {
	public static Integer MAX_PAGE_SIZE=10;
	
	private Integer page = 1;// 页数
	private Integer rows = MAX_PAGE_SIZE;// 每页显示多少条

	private Integer totalCount = 0;// 总条数
	private Integer pageCount = 0;// 总页数
	private String sort = "id";// 排序
	private String order = "asc";// 排序方式 desc
	private List<?> list;// 数据List
	
	public Pager (Integer page,Integer rows){
		setPage(page);
		setRows(rows);
	}
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		if (page < 1) {
			page = 1;
		}
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		if (rows < 1) {
			rows = 1;
		} else if(rows > MAX_PAGE_SIZE) {
			rows = MAX_PAGE_SIZE;
		}
		this.rows = rows;
	}
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		pageCount = totalCount / rows;
		if (totalCount % rows > 0) {
			pageCount ++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
	public int getStartItem(){
		return (page-1)*rows;
	}
	
	public int getEndItem(){
		return page*rows;
	}
}
