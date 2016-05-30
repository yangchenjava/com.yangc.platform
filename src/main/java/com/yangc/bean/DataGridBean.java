package com.yangc.bean;

import java.util.List;

import com.yangc.common.Pagination;
import com.yangc.common.PaginationThreadUtils;

public class DataGridBean {

	private List<?> dataGrid;
	private int pageCount;
	private int pageNow;
	private int totalCount;

	public DataGridBean() {
	}

	public DataGridBean(List<?> dataGrid) {
		this.setDataGrid(dataGrid);
	}

	public List<?> getDataGrid() {
		return dataGrid;
	}

	public void setDataGrid(List<?> dataGrid) {
		this.dataGrid = dataGrid;
		Pagination pagination = PaginationThreadUtils.get();
		if (pagination != null) {
			this.pageCount = pagination.getPageCount();
			this.pageNow = pagination.getPageNow();
			this.totalCount = pagination.getTotalCount();
		}
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getPageNow() {
		return pageNow;
	}

	public int getTotalCount() {
		return totalCount;
	}

}
