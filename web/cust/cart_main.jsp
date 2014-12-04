<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.somecompany.model.oe.Product"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<Product> products = CommonUtils.getTypeObject(request.getAttribute("cart_products"), ArrayList.class);
	double total = 0.0;
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>장바구니</title>
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
		
		.delBtn {
			color: darkred;
		}
	</style>
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			$('.delBtn').hover(function() {
				$('.delBtn').addClass('hover');
			});
		});
		
		function deleteProd(prodId) {
			location.assign('/cust/del_cart.do?id=' + prodId);
		} 
	</script>
</head>
<body>
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	<h3 style="margin-top: 30px; margin-bottom: 20px;">장바구니</h3>
	
	<table align="center" width="700px;">
	<% if(products != null && products.size() != 0) { %>
		<tr>
			<td width="15%">제품이름</td>
			<td width="45%">상세</td>
			<td width="15%">가격</td>
			<td width="15%">수량</td>
			<td></td>
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
			<td class="delBtn" onclick="deleteProd(${prod.productId});">삭제</td>
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
				<button onclick="location.assign('/cust/prod_list.do')">쇼핑 계속하기</button>&nbsp;
				<button onclick="location.assign('/cust/order_first.do')">주문하기</button>
			</td>
		</tr>
	<% } else { %>
		<tr>
			<td>제품이름</td>
			<td>상세</td>
			<td>가격</td>
			<td>수량</td>
			<td></td>
		</tr>
		
		<tr><td colspan="5">장바구니가 비어있습니다.</td></tr>
	<% } %>
	</table>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>