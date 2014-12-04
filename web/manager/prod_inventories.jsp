<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.somecompany.utils.ListPagingHelper"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Inventory"%>
<%@page import="com.somecompany.utils.ListHelper"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	List<Inventory> inventories = CommonUtils.getTypeObject(
			request.getAttribute("inventories"), ArrayList.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>재고 목록</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
</head>
<body>
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	<%@ include file="/manager/forms/manager_nav.jsp" %>
	
	<c:set var="login" value="<%=login%>" />
	<c:set var="loginUserId" value="<%=loginUserId%>" />
	<c:set var="loginUserName" value="<%=loginUserName%>" />
	<c:set var="authority" value="<%=authority%>" />
	
	<h3 style="margin-top: 30px; margin-bottom: 25px;">재고 목록</h3>
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL' || authority == 'GUEST')}">
	<% if(inventories != null && inventories.size() != 0) { %>
		<table align="center" width="600px">
			<tr>
				<td width="25%">제품 번호</td>
				<td width="15%"><%=inventories.get(0).getProductId()%></td>
				<td width="25%">제품 이름</td>
				<td width="35%"><%=inventories.get(0).getProductName()%></td>
			</tr>
		</table>
		
		<br /><br />
		
		<table align="center" width="600px">
			<tr>
				<td width="20%">창고 번호</td>
				<td width="60%">창고 이름</td>
				<td width="20%">수량</td>
			</tr>
			
			<% for(Inventory inv : inventories) { %>
			<c:set var="inv" value="<%=inv%>" />
			<tr>
				<td>${inv.warehouseId}</td>
				<td>${inv.warehouseName}</td>
				<td>${inv.quantityOnHand}</td>
			</tr>
			<% } %>
		
		</table>
	<% } else { %>
		<div>목록이 없습니다</div>
	<% } %>
	</c:when>
	
	<c:otherwise>
		<script>alert('권한 없는 사용자입니다.'); location.assign('/index.jsp');</script>
	</c:otherwise>
	
	</c:choose>	 <!-- end of choose -->
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>