<%@page import="com.somecompany.utils.QueryCallback"%>
<%@page import="com.somecompany.utils.QueryProcessor"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.OrderItem"%>
<%@page import="com.somecompany.service.ORDER_STATUS"%>
<%@page import="com.somecompany.model.oe.Order"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	Order order = CommonUtils.getTypeObject(request.getAttribute("order"), Order.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
	
	// 쿼리 설정(주문 상태 검색을 위해)
	String[] droppedParams = {"page"};
	String queryStr = QueryProcessor.setQuery(request.getQueryString(), droppedParams, null, new QueryCallback() {
		public Object doSomething(String str) { return 0; }
	});
	
	queryStr += "page=";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>주문 내역</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
	<style>
		table {
			border-collapse: collapse;
		}
	
		td {
			padding: 10px;
			border: 1px solid black;
		}
	</style>	
</head>
<body>
	<div id="allcontent">
	
	<c:set var="order" value="<%=order%>" />
	
	<c:if test="${order == null}">
		<script>alert('오류: 전달된 주문 내역이 없습니다!'); location.assign('/manager/order_list.do');</script>
	</c:if>
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	<%@ include file="/manager/forms/manager_nav.jsp" %>
	
	<c:set var="login" value="<%=login%>" />
	<c:set var="loginUserId" value="<%=loginUserId%>" />
	<c:set var="loginUserName" value="<%=loginUserName%>" />
	<c:set var="authority" value="<%=authority%>" />
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL' || authority == 'GUEST')}">
	
		<h3 style="margin-top: 30px; margin-bottom: 20px;">주문내역</h3>
		
		<table align="center" width="600px">
			<tr>
				<td width="15%">주문번호</td>
				<td width="20%">${order.orderId}</td>
				<td width="15%">주문일시</td>
				<td width="20%"><%=order.getOrderDate().toString().substring(0, 10) %></td>
				<td width="15%">주문방식</td>
				<td width="15%">${order.orderMode}</td>
			</tr>
			
			<tr>
				<td>주문인</td>
				<td>${order.customerName}</td>
				<td>주문상태</td>
				<td><%=ORDER_STATUS.getOrderStatus(order.getOrderStatus()).order_status_desc%></td>
				<td>주문총액</td>
				<td>$${order.orderTotal}</td>
			</tr>
		</table>
		
		<br /><br />
	
		<table align="center" width="600px">
			<tr>
				<td width="15%">번호</td>
				<td width="45%">제품명</td>
				<td width="20%">수량</td>
				<td width="20%">가격</td>
			</tr>
			
		<% for(OrderItem item : order.getOrderItems()) { %>
			<c:set var="item" value="<%=item%>" />
			<tr>
				<td>${item.lineItemId}</td>
				<td>${item.productName}</td>
				<td>${item.quantity}</td>
				<td>${item.unitPrice}</td>
			</tr>
		<% } %>
		
		</table>
		
		<c:set var="curPage" value="<%=curPage%>" />
		<c:set var="queryStr" value="<%=queryStr%>" />
		
		<br /><br />
		<div>
			<a href="/manager/order_list.do?${queryStr}${curPage}">주문 목록 보기</a>
		</div>
		
		
		<c:if test="${authority == 'PURCHASE' || authority == 'ALL'}">
		<div style="text-align: right; padding: 10px;">
			<button onclick="location.assign('/manager/delete_order.do?id=${order.orderId}')">
				주문 삭제
			</button>
		</div>
		</c:if>
		
	</c:when>
	
	<c:otherwise>
		<script>alert('권한 없는 사용자입니다.'); location.assign('/index.jsp');</script>
	</c:otherwise>
	
	</c:choose> <!-- end of choose -->
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	
	</div>
</body>
</html>