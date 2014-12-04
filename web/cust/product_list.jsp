<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.somecompany.utils.QueryCallback"%>
<%@page import="com.somecompany.utils.QueryProcessor"%>
<%@page import="java.util.Map"%>
<%@page import="com.somecompany.service.CommonService"%>
<%@page import="com.somecompany.utils.ListPagingHelper"%>
<%@page import="com.somecompany.model.oe.Product"%>
<%@page import="java.util.List"%>
<%@page import="com.somecompany.utils.ListHelper"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ListHelper<Product> listHelper = CommonUtils.getTypeObject(request.getAttribute("list_helper"), ListHelper.class); 	
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);		//현재 페이지
	
	List<Object> returnedDatas = new ArrayList<Object>();
	
	int categoryId = -1;
	
	// 쿼리 설정(검색을 위해)
	String[] droppedParams = {"page"};
	String queryStr = QueryProcessor.setQuery(request.getQueryString(), droppedParams, 
			returnedDatas, new QueryCallback() {
		public Integer doSomething(String str) {
			Integer catId = null;
			
			if(str.startsWith("category=")) {
				String[] catStr =  str.split("=");
				
				if(catStr.length == 2)
					catId = Integer.parseInt(catStr[1]);
			}
			return catId;
		}
	});
	
	queryStr += "page=";
	
	if(returnedDatas.size() != 0) {
		categoryId = (Integer) returnedDatas.get(0);
	}
	
	// 카테고리 정보
	Map<Short, String> categoryMap = CommonService.getCategoryMap();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>제품 목록</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/cust.css" />
	<style>
		#page_list {
			margin-top: 15px;
		}
		
		.hover {
			cursor: pointer;
		}
	</style>
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/js/js_util.js"></script>
	<script>
		$(document).ready(function() {
			$('#catSel').bind('change', selectEventHandler);
			
			$('#low').bind('click', function(event) {
				event.preventDefault();
				
				var search = location.search;
				
				search = replaceParameter(search, 'orderby');
				
				location.assign('/cust/prod_list.do' + search + 'orderby=2');
			});
			
			$('#high').bind('click', function(event) {
				event.preventDefault();
				
				var search = location.search;
				
				search = replaceParameter(search, 'orderby');
				
				location.assign('/cust/prod_list.do' + search + 'orderby=3');
			});
			
			$('#low').hover(function() {
				$('#low').addClass('hover');
			});
			
			$('#high').hover(function() {
				$('#high').addClass('hover');
			});
		});
		
		var selectEventHandler = function() {
			var catSel = $('#catSel').get(0);
			var search = location.search;
			
			search = replaceParameter(search, 'category');
			
			var selectedIdx = catSel.selectedIndex;
			var categoryId = catSel.options[selectedIdx].value;
			
			location.assign('/cust/prod_list.do' + search + 'category=' + categoryId);
		};
	</script>
</head>
<body>
	<div id="allcontent">
	
	<c:set var="queryStr" value="<%=queryStr%>" />
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id = "main">
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	
	<div style="font-size: 10pt; text-align: left; margin-top: 5px; padding: 10px; margin-bottom: 10px;">
		<div id="order">
			정렬 순서&nbsp;&nbsp;<a id="low" style="color: darkred">낮은가격</a>&nbsp;
			<a id="high"  style="color: darkred">높은가격</a><br>
		</div>
		
		<div>
			카테고리 
			<c:set var="categoryId" value="<%=categoryId%>" />
			
			<select id="catSel" style="width: 150px;">
			<% for(Map.Entry<Short, String> entry : categoryMap.entrySet()) {%>
				<c:set var="key" value="<%=entry.getKey()%>" />
				<c:set var="value" value="<%=entry.getValue()%>" />
				
				<option value="${key}" <c:if test="${categoryId == key}">selected</c:if>>
					${value}
				</option>
			<% } %>
			</select>
		</div>
	</div>
	
	<h3 style="margin-bottom: 30px;">제품 목록</h3>
	
	<c:set var="curPage" value="<%=curPage%>" />
	
	<% if(listHelper != null && listHelper.getList().size() != 0) { %>
		<table align="center" width="800px">
			<tr>
				<td width="15%">제품 번호</td>
				<td width="40%">제품 이름</td>
				<td width="15%">카테고리</td>
				<td width="15%">가격</td>
				<td width="15%">재고</td>
			</tr>
					
			<% for(Product prod : listHelper.getList()) { %>
			<c:set var="prod" value="<%=prod%>" />
			
			<tr>
				<td><a href="/cust/prod.do?id=${prod.productId}&page=${curPage}">${prod.productId}</a></td>
				<td>${prod.productName}</td>
				<td>${prod.categoryName}</td>
				<td>$${prod.listPrice}</td>
				<td>
				<c:if test="${prod.qtySum == 0}">
					<span class="soldout">sold out!</span>
				</c:if>
				</td>
			</tr>
			<% } %>
	</table>
		
	<% } else { %>
		<p>결과가 없습니다</p>
	<% } %>
	
	
	<%
			// 페이지 계산(시작 페이지, 끝 페이지)
			int totalPage = listHelper.getTotalPageCount();
			int pagePerList = 10;	// 한 번에 보여줄 페이지 수
			
			ListPagingHelper pageHelper = new ListPagingHelper(totalPage, curPage, pagePerList);
			
			int startPage = pageHelper.getStartPage();
			int endPage = pageHelper.getEndPage();
		%>
		
		<c:set var="totalPage" value="<%=totalPage%>" />
		<c:set var="startPage" value="<%=startPage%>" />
		<c:set var="endPage" value="<%=endPage%>" />
		<c:set var="pagePerList" value="<%=pagePerList%>" />
		
		<br />
		<!-------------------------------- 페이지 이동 링크 ----------------------------------->
		<div id="page_list">
			<c:if test="${startPage != 1}">
			<span>
				<a href="/cust/prod_list.do?${queryStr}${startPage-pagePerList}">
					[이전]
				</a>
			</span>
			</c:if>
			
			<c:forEach var="i" begin="${startPage}" end="${endPage}">
				<c:choose>
					<c:when test="${i == curPage}">
						<span class="selected">${i}</span>
					</c:when>
					
					<c:otherwise>
						<span><a href="/cust/prod_list.do?${queryStr}${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/cust/prod_list.do?${queryStr}${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>
			
		</div>
		
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>