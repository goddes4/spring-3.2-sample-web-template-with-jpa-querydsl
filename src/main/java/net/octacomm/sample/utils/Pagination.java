package net.octacomm.sample.utils;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Pagination {

	private static final int PAGE_SIZE = 10;
	private static final int PAGE_GROUP_SIZE = 10;
	
	@Getter
	private int pageSize;
	private int pageGroupSize;
	private int totalCount;
	@Getter
	private int currentPage;

	@Getter
	private int pageCount;
	private int currentPageGroupNo;
 	
	public Pagination(int totalCount, int currentPage) {
		this(PAGE_SIZE, PAGE_GROUP_SIZE, totalCount, currentPage);
	}
	
	public Pagination(int pageSize, int pageGroupSize, int totalCount, int currentPage) {
		pageCount = pageCount(totalCount, pageSize);
		currentPageGroupNo = pageGroupNo(currentPage, pageGroupSize);
		
		this.pageSize = pageSize;
		this.pageGroupSize = pageGroupSize;
		this.totalCount = totalCount;
		this.currentPage = currentPage;
	}
	
	public int getArticleNum() {
		return totalCount - (currentPage - 1) * pageSize + 1;
	}
	
	/**
	 * 데이터베이스에 요청할 시작 ROW
	 * 
	 * @return
	 */
	public int getStartRow() {
		return (currentPage - 1) * pageSize;
	}
	
	public int getEndRow() {
		return currentPage * pageSize;
	}
	
	public int getStartPage() {
		return (currentPageGroupNo - 1) * pageGroupSize + 1;
	}

	public int getEndPage() {
		int endPage = currentPageGroupNo * pageGroupSize;
		if (endPage > pageCount) {
			endPage = pageCount; 
		}
		return endPage;
	}
	
	/**
	 * 현재 페이지 그룹이 2 이상일때만 이전 페이지 그룹으로 이동할 수 있다.
	 * 
	 * @return
	 */
	public int getPriorPageGroup() {
		if (currentPageGroupNo > 1) {
			return (currentPageGroupNo - 2) * pageGroupSize + 1;
		} else {
			return 0; 
		}
	}
	
	public int getNextPageGroup() {
		if (currentPageGroupNo < pageGroupCount()) {
			return (currentPageGroupNo * pageGroupSize) + 1;
		} else {
			return 0;
		}
	}

	/**
	 * 마지막 페이지 그룹 이동 활성화를 위해 필요
	 * 
	 * @return
	 */
	private int pageGroupCount() {
		return pageCount / pageGroupSize + (pageCount % pageGroupSize == 0 ? 0 : 1); 
	}
	
	/**
	 * 현재 페이지의 페이지 그룹 위치
	 * 
	 * @return
	 */
	private int pageGroupNo(int currentPage, int pageGroupSize) {
		return (int)Math.ceil((double) currentPage / pageGroupSize);
	}
	
	/**
	 * 전체 페이지 수 (endPage 구할 때 사용)
	 * @return
	 * 
	 */
	private int pageCount(int totalCount, int pageSize) {
		return totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
	}
	
}
