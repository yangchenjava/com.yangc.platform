package com.yangc.bean;

import java.util.List;

import com.yangc.common.Pagination;
import com.yangc.common.PaginationThreadUtils;

public class DataGridBean {

	private List<?> dataGrid;
	private int totalCount;

	public DataGridBean() {
		this(null);
	}

	public DataGridBean(List<?> dataGrid) {
		this.dataGrid = dataGrid;
		Pagination pagination = PaginationThreadUtils.get();
		this.totalCount = pagination.getTotalCount();
	}

	public List<?> getDataGrid() {
		return dataGrid;
	}

	public void setDataGrid(List<?> dataGrid) {
		this.dataGrid = dataGrid;
	}

	public int getTotalCount() {
		return totalCount;
	}

}
