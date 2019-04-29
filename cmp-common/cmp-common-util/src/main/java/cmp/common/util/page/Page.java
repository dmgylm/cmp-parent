package cmp.common.util.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页类
 * 
 * @author 郭庆宝
 * @version 1.0
 * @param <T>
 */
public class Page<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2452468795266235052L;

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
	 * 总记录数
	 */
	private int totalCount = -1;

	/**
	 * 页内记录集
	 */
	private List<T> list = null;

	/**
	 * 默认构造方法
	 */
	public Page() {
	}

	/**
	 * 构造方法,用指定的页码和页内记录数创建Page对象.
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页的记录数
	 */
	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	/**
	 * 构造方法,用指定页码, 页内记录集,每页记录数和总记录数创建Page对象.
	 * 
	 * @param pageNo
	 *            页码
	 * @param list
	 *            页内记录集
	 * @param pageSize
	 *            每页的记录数
	 * @param totalCount
	 *            总记录数
	 */
	public Page(int pageNo, List<T> list, int pageSize, int totalCount) {
		this.pageNo = pageNo;
		this.list = list;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
	}

	/**
	 * 第一条记录在结果集中的位置,序号从0开始
	 * 
	 * @return 起始页
	 */
	public int getStart() {
		if (pageNo < 0 || pageSize < 0) {
			return -1;
		} else {
			return ((pageNo - 1) * pageSize);
		}
	}

	/**
	 * 获取总页数.
	 * 
	 * @return 总页数
	 */
	public int getTotalPageCount() {
		if (totalCount <= 0) {
			return 1;
		}

		int count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 * 
	 * @return 布尔值 ,true 有下一页，false 无下一页已到末页
	 */
	public boolean isHasNextPage() {
		return (pageNo + 1 <= getTotalPageCount());
	}

	/**
	 * 返回下页的页码,序号从1开始．
	 * 
	 * @return 下页的页码
	 */
	public int getNextPage() {
		if (isHasNextPage()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 是否还有上一页.
	 * 
	 * @return 布尔值 ,true 有上一页，false 无上一页已到首页
	 */
	public boolean isHasPrePage() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 返回上页的页码,序号从1开始.
	 * 
	 * @return 上页的页码
	 */
	public int getPrePage() {
		if (isHasPrePage()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
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

	/**
	 * 返回页内的记录集.
	 * 
	 * @return 页内记录集
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设定页内记录集
	 * 
	 * @param list
	 *            页内记录集
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * 返回总记录数量.
	 * 
	 * @return 总记录数
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 设定总记录数
	 * 
	 * @param totalCount
	 *            总记录数
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + ", totalCount=" + totalCount + ", list=" + list
				+ "]";
	}

}
