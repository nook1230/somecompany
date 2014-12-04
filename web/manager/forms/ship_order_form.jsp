<%@page import="com.somecompany.model.oe.OrderItem"%>
<%@page import="com.somecompany.model.oe.Order"%>
<%@page import="com.somecompany.service.ORDER_STATUS"%>
<%@page import="com.somecompany.model.oe.Warehouse"%>
<%@page import="java.util.List"%>
<%@page import="com.somecompany.model.oe.OrderAndWarehouses"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	short orderStatus = CommonUtils.getShortParameter(request.getParameter("status"), (short) -1);
	OrderAndWarehouses oaw = CommonUtils.getTypeObject(
			request.getAttribute("order_and_warehouses"), OrderAndWarehouses.class);
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>출고 작업</title>
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
	<c:set var="status" value="<%=orderStatus%>" />
	
	<h3 style="margin-top: 30px; margin-bottom: 25px;">주문 출고 프로세스</h3>
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL')}">
	<% if(oaw != null) { %>
	
	<% Order order = oaw.getOrder(); %>
	<c:set var="order" value="<%=order%>" />
	
	<% if(order != null) { %>
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
		
		<% List<Warehouse> warehouses = oaw.getWarehouses(); %>
		
		<br /><br />
		
		<form action="/manager/ship_order.do" method="post">
			<input type="hidden" name="order_id" value="${order.orderId}" />
			<input type="hidden" name="status" value="${status}" />
		<% if(warehouses != null && warehouses.size() != 0) { %>	
		
			출고 가능한 창고입니다<br />
			<select name="warehouse_id">
			<% for(Warehouse warehouse : warehouses) { %>
				<option value="<%=warehouse.getWarehouseId()%>"><%=warehouse.getWarehouseName()%></option>
			<% } %>
			</select>
			에서 <input type="submit" value="출고" />&nbsp;<input type="button" value="취소" onclick="history.go(-1);"/>
		<% } else { %>
			출고 가능한 창고가 없습니다. <input type="button" value="돌아가기" onclick="history.go(-1);"/>
		<% } %>
		</form>
	<% } %>
	
	
	<% } else { %>
		<script>alert('전달된 정보가 없습니다'); location.assign('/manager/order_list.do');</script>
	<% } %>
	</c:when>
	
	<c:otherwise>
	
	</c:otherwise>
	
	</c:choose>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>