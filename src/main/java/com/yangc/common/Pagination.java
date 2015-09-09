package com.yangc.common;

public class Pagination {

	private static final int DEFAULT_PAGE_SIZE = 20;

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
		this.pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
		this.notPaginated = false;
	}

	/** 返回当前实例在第几页 */
	public int getPageNow() {
		return pageNow;
	}

	/** 设置当前实例在第<code>pageNow</code>页 */
	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
		this.notPaginated = false;
	}

	/** 返回总记录数 */
	public int getTotalCount() {
		return totalCount;
	}

	/** 设置总记录数 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.notPaginated = false;
	}

	/** 返回当前实例总共的页数, 如果没有数据返回<b>0</b>, 如果数据不足一页返回<b>1</b> */
	public final int getPageCount() {
		int remainder = this.totalCount % this.pageSize;
		if (this.totalCount != 0 && remainder == this.totalCount) {
			return 1;
		} else if (remainder == 0) {
			return this.totalCount / this.pageSize;
		} else {
			return this.totalCount / this.pageSize + 1;
		}
	}

	/** 判断是否是第一页, 如果当前实例没有数据或只有一页返回<code>true</code> */
	public boolean isFirst() {
		return this.getPageCount() < 2 || this.pageNow == 1;
	}

	/** 判断是否是最后一页, 如果当前实例没有数据或只有一页返回<code>true</code> */
	public boolean isLast() {
		return this.getPageCount() < 2 || this.pageNow == this.getPageCount();
	}

	/** 判断是否存在上一页 */
	public boolean hasPrevPage() {
		return this.getPageCount() > 1 && this.pageNow > 1;
	}

	/** 判断是否存在下一页 */
	public boolean hasNextPage() {
		return this.getPageCount() > 1 && this.pageNow < this.getPageCount();
	}

	public boolean isNotPaginated() {
		return this.notPaginated;
	}

	public String toString() {
		return "[\"total\":" + this.getPageCount() + ", \"page\":" + this.pageNow + ", \"records\":" + this.totalCount + "]";
	}

}
