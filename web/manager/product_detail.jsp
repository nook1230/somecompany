<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Product"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Product product = CommonUtils.getTypeObject(request.getAttribute("product"), Product.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>제품 정보</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
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
	
	<h3 style="margin-bottom: 30px;">제품 정보</h3>
		
		<c:set var="prod" value="<%=product%>" />
		
		<c:if test="${prod == null}">
			<script>alert('제품 정보가 없습니다'); location.assign('/manager/prod_list.do');</script>
		</c:if>
		
		<table align="center" width="600px">
			<tr>
				<td>제품번호</td>
				<td>${prod.productId}</td>
				<td>제품명</td>
				<td>${prod.productName}</td>
				
			</tr>
			
			<tr>
				<td>제품 상태</td>
				<td>${prod.productStatus}</td>
				<td>보증기간</td>
				<td>year to month: ${prod.warrantyPeriod}</td>
			</tr>
			
			<tr>
				<td>카테고리</td>
				<td>${prod.categoryName}</td>
				<td>총 수량</td>
				<td>${prod.qtySum}</td>
			</tr>
			
			<tr>
				<td>가격 </td>
				<td>$${prod.listPrice}</td>
				<td>최소 가격 </td>
				<td>$${prod.minPrice}</td>
			</tr>
						
		</table>
		
		<br />
		
		<table align="center" width="600px">
			<tr>
				<td width="20%">제품 설명</td>
				<td style="text-align: left; padding: 10px;">${prod.productDescription}</td>
			</tr>
		</table>
		
		<div style="margin-top: 25px; padding: 10px;">
		<c:if test="${authority == 'PURCHASE' || authority == 'ALL'}">
			<div>
				<button onclick="location.assign('/manager/update_prod.do?id=${prod.productId}')">수정</button>&nbsp;
				<button onclick="location.assign('/manager/del_prod.do?id=${prod.productId}')">삭제</button>
			</div>
		</c:if>
			<div style="margin-top: 15px;">
				<a href="/manager/prod_list.do?page=${curPage}">목록 보기</a>
			</div>
		</div>
		
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