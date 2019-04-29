package cmp.common.util.page;

import java.io.Serializable;

/**
 * 分页请求类
 * 
 * @author 郭庆宝
 * @version 1.0
 * @param <T>
 */
public class PageParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5761924394495776273L;

	/**
	 * 缺省的每页记录数
	 */
	public static final int DEFAULT_PAGESIZE = 15;

	/**
	 * 缺省的开始页
	 */
	public static final int DEFAULT_PAGE = 1;

	/**
	 * 页码
	 */
	private int pageNo = DEFAULT_PAGE;

	/**
	 * 每页记录数
	 */
	private int pageSize = DEFAULT_PAGESIZE;
	
	/**
	 * 默认构造方法
	 */
	public PageParams() {
	}

	/**
	 * 构造方法,用指定的页码和页内记录数创建Page对象.
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页的记录数
	 */
	public PageParams(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	

	/**
	 * 返回每页的记录数量.
	 * 
	 * @return 每页的记录数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设定每页的记录数．
	 * 
	 * @param pageSize
	 *            每页的记录数
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 取得当前页的页码,序号从1开始.
	 * 
	 * @return 当前页码
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设定当前页码
	 * 
	 * @param page
	 *            页码
	 */
	public void setPageNo(int page) {
		this.pageNo = page;
	}
}
