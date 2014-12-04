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
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>제품 목록</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
	<style>
		#page_list {
			margin-top: 15px;
		}
		
		.hover {
			cursor: pointer;
		}
	</style>
</head>
<body>
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id = "main">
	
	<%@ include file="/manager/forms/manager_nav.jsp" %>
	
	<c:set var="login" value="<%=login%>" />
	<c:set var="loginUserId" value="<%=loginUserId%>" />
	<c:set var="loginUserName" value="<%=loginUserName%>" />
	<c:set var="authority" value="<%=authority%>" />
	
	<c:set var="curPage" value="<%=curPage%>" />
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL' || authority == 'GUEST')}">
	
	<h3 style="margin-bottom: 30px;">제품 목록</h3>
	
	<% if(listHelper != null && listHelper.getList().size() != 0) { %>
		<table align="center" width="850px">
			<tr>
				<td width="10%">제품 번호</td>
				<td width="30%">제품 이름</td>
				<td width="10%">카테고리</td>
				<td width="20%">제품 상태</td>
				<td width="10%">리스트 가격</td>
				<td width="10%">최소 가격</td>
				<td width="10%">총 수량</td>
			</tr>
			
		<% for(Product prod : listHelper.getList()) { %>
			<c:set var="prod" value="<%=prod%>" />
			
			<tr <c:if test="${prod.qtySum == 0}">style="color: orange;"</c:if>>
				<td><a href="/manager/prod.do?id=${prod.productId}&page=${curPage}">${prod.productId}</a></td>
				<td>${prod.productName}</td>
				<td>${prod.categoryName}</td>
				<td>${prod.productStatus}</td>
				<td <c:if test="${prod.listPrice == 0.0}">style="color: blue;"</c:if>>$${prod.listPrice}</td>
				<td <c:if test="${prod.minPrice == 0.0}">style="color: blue;"</c:if>>$${prod.minPrice}</td>
				<td>${prod.qtySum}</td>
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
				<a href="/manager/prod_list.do?page=${startPage-pagePerList}">
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
						<span><a href="/manager/prod_list.do?page=${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/manager/prod_list.do?page=${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>
			
		</div>
		
		<c:if test="${authority == 'PURCHASE' || authority == 'ALL'}">
		<div style="text-align: right; padding: 10px;">
			<button onclick="location.assign('/manager/add_prod.do');">새 상품 추가하기</button>
		</div>
		</c:if>
	
	</c:when>
	
	<c:otherwise>
		<script>alert('권한 없는 사용자입니다.'); location.assign('/index.jsp');</script>
	</c:otherwise>
	
	</c:choose>	
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>