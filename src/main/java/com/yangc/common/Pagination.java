package com.yangc.common;

public class Pagination {

	/** 当不需要分页时返回这个实例 */
	public static final Pagination NOT_PAGINATED = new Pagination() {
		public String toString() {
			return "not paginated";
		}
	};

	public static final int DEFAULT_PAGE_SIZE = 10;

	/** 每页记录数, 通常在页面中可以修改这个参数 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/** 当前是第几页, 从<b>1</b>开始计算 */
	private int pageNow;

	/** 总记录数 */
	private int totalCount;

	/**
	 * 不执行分页操作的标志
	 * @see #setCount(int)
	 * @see #setPage(int)
	 * @see #setPageSize(int)
	 */
	private boolean notPaginated = true;

	/** 返回一页中展现多少行数据 */
	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		this.notPaginated = false;
		if (this.pageSize < 1) {
			this.pageSize = DEFAULT_PAGE_SIZE;
		}
	}

	/** 返回当前实例在第几页 */
	public int getPageNow() {
		return pageNow;
	}

	/** 设置当前实例在第<code>pageNow</code>页 */
	public void setPageNow(int pageNow) {
		this.notPaginated = false;
		this.pageNow = pageNow;
	}

	/** 返回总记录数 */
	public int getTotalCount() {
		return totalCount;
	}

	/** 设置总记录数 */
	public void setTotalCount(int totalCount) {
		this.notPaginated = false;
		this.totalCount = totalCount;
	}

	/** 返回当前实例总共的页数, 如果没有数据返回<b>0</b>, 如果数据不足一页返回<b>1</b> */
	public final int getPageCount() {
		int remainder = totalCount % pageSize;
		if (totalCount != 0 && remainder == totalCount) {
			return 1;
		} else if (remainder == 0) {
			return totalCount / pageSize;
		} else {
			return totalCount / pageSize + 1;
		}
	}

	/** 判断是否是第一页, 如果当前实例没有数据或只有一页返回<code>true</code> */
	public boolean isFirst() {
		return getPageCount() < 2 || pageNow == 1;
	}

	/** 判断是否是最后一页, 如果当前实例没有数据或只有一页返回<code>true</code> */
	public boolean isLast() {
		return getPageCount() < 2 || pageNow == getPageCount();
	}

	/** 判断是否存在上一页 */
	public boolean hasPrevPage() {
		return getPageCount() > 1 && pageNow > 1;
	}

	/** 判断是否存在下一页 */
	public boolean hasNextPage() {
		return getPageCount() > 1 && pageNow < getPageCount();
	}

	public boolean isNotPaginated() {
		return this.notPaginated;
	}

	/**
	 * @功能: 返回json串,用于jqGrid
	 * @作者: yangc
	 * @创建日期: 2011-12-9 下午06:00:23
	 * @return
	 */
	public String toString() {
		String str = "\"total\":" + getPageCount() + ",\"page\":" + pageNow + ",\"records\":" + totalCount;
		return str;
	}

}
