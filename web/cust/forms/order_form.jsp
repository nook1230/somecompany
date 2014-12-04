<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Customer"%>
<%@page import="com.somecompany.repository.dao.oe.CustomerDao"%>
<%@page import="com.somecompany.controller.oe.CustomerController"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.somecompany.model.oe.Product"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	List<Product> products = CommonUtils.getTypeObject(request.getAttribute("products"), ArrayList.class);
	double total = 0.0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>주문서 작성</title>
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
		
		.hover {
			cursor: pointer;
		}
	</style>
</head>
<body>
	<div id="allcontent">
	
	<c:set var="products" value="<%=products%>" />
	<c:if test="${products == null}">
		<script>alert('추가된 상품이 없습니다.'); location.assign('/cust/cart.do');</script>
	</c:if>
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	<h3 style="margin-top: 30px; margin-bottom: 20px;">주문서</h3>
	
	<%
		// 고객 정보를 가져오기 위해서 "어쩔 수 없이" 직접 CustomerDao를 이용한다
		CustomerDao customerDao = CustomerDao.getInstance("oe-mybatis-config.xml");
		
		Customer customer = customerDao.get(loginCustUserId);
	%>
	
	<form action="/cust/order.do" method="post">
	
	<c:set var="cust" value="<%=customer%>" />
	<input type="hidden" name="cust_id" value="${cust.customerId}" />
	
	<c:if test="${cust == null}">
		<script>alert('오류: 고객 정보가 없습니다.'); location.assign('/cust/cart.do');</script>
	</c:if>
	
	<table align="center" width="700px">
		<tr>
			<td colspan="2">고객 정보</td>
		</tr>
			
		<tr>
			<td>이름</td><td>${cust.customerFirstName} ${cust.customerLastName}</td>
		</tr>
		
		<tr>
			<td>주소</td><td><%=customer.getCustomerAddress()%></td>
		</tr>
		
		<tr>
			<td>연락처</td><td>${cust.phoneNumber}</td>
		</tr>
		
		<tr>
			<td>이메일</td><td>${cust.customerEmail}</td>
		</tr>
	</table>
	
	<br /><br />
	
	<table align="center" width="700px" style="margin-bottom: 20px;">
		<tr>
			<td colspan="4">주문 상품 정보</td>
		</tr>
		
		<tr>
			<td width="20%">제품이름</td>
			<td width="50%">상세</td>
			<td width="15%">가격</td>
			<td width="15%">수량</td>
		</tr>
		<% for(Product product : products) { %>
		<c:set var="prod" value="<%=product%>" />
		
		<tr id="${prod.productId}">
			<td><a href="/cust/prod.do?id=${prod.productId}">${prod.productName}</a></td>
			<td>
				<%
					String prodDesc = product.getProductDescription();
					int originalLen = product.getProductDescription().length();
					int len = (prodDesc.length() > 100) ? 100 : prodDesc.length();
					String ellipsis = " ...";
					
					prodDesc = prodDesc.substring(0, len);
					
					if(len < originalLen)
						prodDesc += ellipsis;
				%>
				<%=prodDesc%>
			</td>
			<td>$${prod.listPrice}</td>
			<td>${prod.qtySum}</td>
			<%
				total += (product.getListPrice() * product.getQtySum()); 
			%>
		</tr>
		<% } %>
		
		
		<tr>
			<td colspan="2"></td>
			<td>총액</td>
			<td colspan="2">$<%=total%></td>
		</tr>
		
		<tr>
			<td colspan="5">
				<input type="submit" value="주문하기" />
				<input type="button" value="취소" onclick="location.assign('/cust/cart.do')" />
			</td>
		</tr>
	</table>
	
	</form>
	
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>