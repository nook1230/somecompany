<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Product"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Product product = CommonUtils.getTypeObject(request.getAttribute("product"), Product.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
	short orderby = CommonUtils.getShortParameter(request.getParameter("orderby"), (short) -1);
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>제품 정보</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/cust.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<style>
		#prod_table td {
			padding: 10px;
		}
		
		.order {
			font-size: 11pt; 
			padding: 2px;
			color: darkred;
		}
		
		.hover {
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			// 클릭 이벤트
			$('#plusQty').click(function() {
				qty = parseInt($('#qty').attr('value'));
				
				if(++qty > Number.MAX_VALUE) {
					qty = Number.MAX_VALUE;
				}
				
				$('#qty').attr('value', qty.toString());
			});
			
			$('#minusQty').click(function() {
				qty = parseInt($('#qty').attr('value'));
				
				if(--qty < 0) {
					qty = 0;
				}
				
				$('#qty').attr('value', qty.toString());
			});
			
			$('#toCart').click(function() {
				var $id = $('#prod_id').attr('value');
				var $qty = $('#qty').attr('value');
				
				location.assign('/cust/add_cart.do?prod_id=' + $id + '&qty=' + $qty);
			});
			
			// hover 이벤트
			$('#plusQty').hover(function() {
				$('#plusQty').addClass('hover');
			});
			
			$('#minusQty').hover(function() {
				$('#minusQty').addClass('hover');
			});
			
			$('#toCart').hover(function() {
				$('#toCart').addClass('hover');
			});
		});
	</script>
</head>
<body>
	
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id = "main">
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	
	<h3 style="margin-bottom: 30px;">제품 정보</h3>		
		<c:set var="prod" value="<%=product%>" />
		
		<c:if test="${prod == null}">
			<script>alert('제품 정보가 없습니다'); location.assign('/cust/prod_list.do');</script>
		</c:if>
		
		<table id="prod_table" align="center" width="600px">
			<tr>
				<td width="25%">제품 번호</td>
				<td width="25%">
					${prod.productId}
				</td>
				<td width="25%">제품명</td>
				<td width="25%">${prod.productName}</td>
			</tr>
			
			<tr>
				<td>카테고리</td>
				<td>
					<a href="/cust/prod_list.do?category=${prod.categoryId}">
						${prod.categoryName}
					</a>
				</td>
				<td>가격 </td>
				<td>$${prod.listPrice}</td>
			</tr>
			
			<tr>
				<td>제품 설명</td>
				<td colspan="3">${prod.productDescription}</td>
			</tr>
			
			<tr>
				<td>보증기간</td>
				<td>year to month: ${prod.warrantyPeriod}</td>
				<td>재고</td>
				<td>
				<c:if test="${prod.qtySum == 0}">
					<span style="color: orange;">sold out!</span>
				</c:if>
				<c:if test="${prod.qtySum != 0}">
					<span style="color: blue;">OK!</span>
				</c:if>
				</td>
			</tr>
			
		</table>
		
		<div style="margin-top: 35px;">
		<c:if test="${prod.qtySum != 0}">
			<label for="qyt">주문수량</label>
			<input type="hidden" id="prod_id" value="${prod.productId}"/>
			<input type="text" size="2" id="qty" value="1" />&nbsp;
			<span id="plusQty" class="order">+</span>&nbsp;
			<span id="minusQty" class="order">-</span>&nbsp;
			<span id="toCart" style="border: 1px solid silver; padding: 2px;">장바구니에 담기</span>&nbsp;
		</c:if>
		</div>
		
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
	
</body>
</html>