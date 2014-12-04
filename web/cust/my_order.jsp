<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.OrderItem"%>
<%@page import="com.somecompany.service.ORDER_STATUS"%>
<%@page import="com.somecompany.model.oe.Order"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	Order order = CommonUtils.getTypeObject(request.getAttribute("order"), Order.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>주문 내역</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/cust.css" />
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
		<script>alert('오류: 전달된 주문 내역이 없습니다!'); location.assign('/cust/mypage.do');</script>
	</c:if>
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	
	<c:choose>
	
	<c:when test="${custLogin == true && (custUserId == order.customerId)}">
	
	<h3 style="margin-top: 30px; margin-bottom: 20px;">마이 페이지: 주문내역</h3>
	
	<table align="center" width="600px">
		<tr>
			<td width="20%">주문번호</td>
			<td width="30%">${order.orderId}</td>
			<td width="20%">주문일시</td>
			<td width="30%">${order.orderDate}</td>
		</tr>
		
		<tr>
			<td>주문방식</td>
			<td>${order.orderMode}</td>
			<td>주문상태</td>
			<td><%=ORDER_STATUS.getOrderStatus(order.getOrderStatus()).order_status_desc%></td>
		</tr>
		
		<tr>
			<td colspan="2">주문총액</td>
			<td colspan="2">$${order.orderTotal}</td>
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
	
	</c:when>
	
	<c:otherwise>
		<script>alert('오류: 부적절한 접근입니다'); location.assign('/cust/mypage.do');</script>
	</c:otherwise>
	
	</c:choose>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	
	</div>
</body>
</html>