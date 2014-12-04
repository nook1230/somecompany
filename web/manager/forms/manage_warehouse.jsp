<%@page import="com.somecompany.utils.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	int warehouseId = CommonUtils.getIntParameter(request.getParameter("ware_id"), 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>입출고 관리</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#btn_in').click(function(event) {
				event.preventDefault();
				checkSubmit(true);
			});
			
			$('#btn_out').click(function(event) {
				event.preventDefault();
				checkSubmit(false);
			});
		});
	
		function checkSubmit(stockIn) {
			var form = $('#f_warehouse').get(0);
			var prod_id = $('#prod_id').attr('value');
			var qty = $('#quantity').attr('value');
			
			if(stockIn) {
				$('#in_or_out').attr('value', 'in');
			} else {
				$('#in_or_out').attr('value', 'out');
			}
			
			if(!isNaN(prod_id) && prod_id != '' && !isNaN(qty) && qty != '')
				form.submit();
			else
				alert('제품 번호와 수량에는 숫자를 입력하셔야 합니다.');
		}
	</script>
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
	<c:set var="warehouseId" value="<%=warehouseId%>" />
	
	<h3 style="margin-top: 45px; margin-bottom: 35px;">창고 관리: 입출고</h3>
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL')}">
		<div style="margin-top: 10px; margin-bottom: 15px;">
			입출고를 원하는 제품의 식별 번호와 수량을 입력해주세요
		</div>
		
		<form id="f_warehouse" action="/manager/warehousing.do" method="post">
			<label for="prod_id">제품 번호</label>
			<input type="text" size="5" id="prod_id" name="prod_id" />
			
			<label for="quantity">수량</label>
			<input type="text" size="5" id="quantity" name="quantity" />
			
			<input type="hidden" id="in_or_out" name="in_or_out" />
			<input type="hidden" id="ware_id" name="ware_id" value="${warehouseId}" />
			
			<button id="btn_in">입고</button>
			<button id="btn_out">출고</button>
		</form>
		
		<div style="margin-top: 55px;">
			<a href="/manager/inventories.do?id=${warehouseId}">재고 목록 보기</a>
		</div>
		
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