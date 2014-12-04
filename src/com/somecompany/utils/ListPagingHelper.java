package com.somecompany.utils;

public class ListPagingHelper {
	
	private int startPage;
	private int endPage;
	private int totalPage;
	private int curPage;
	private int pagePerList;
	
	public ListPagingHelper(int totalPage, int curPage, int pagePerPage) {
		this.totalPage = totalPage;
		this.curPage = curPage;
		this.pagePerList = pagePerPage;
		
		calculatePage();
	}

	public int getStartPage() {
		return startPage;
	}
	
	
	public int getEndPage() {
		return endPage;
	}
	
	
	public int getPagePerList() {
		return pagePerList;
	}
	
	public void setPagePerList(int pagePerList) {
		this.pagePerList = pagePerList;
	}
	
	
	private void calculatePage() {
		int startBlock = (curPage - 1) / pagePerList + 1;
		startPage = (startBlock - 1) * pagePerList + 1;
		endPage = startPage + pagePerList - 1;
		
		if(endPage > totalPage)
			endPage = totalPage;
		
		if(curPage > totalPage)
			curPage = totalPage;
	}
	
}
