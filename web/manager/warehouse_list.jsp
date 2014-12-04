<%@page import="java.util.ArrayList"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Warehouse"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	List<Warehouse> warehouses = CommonUtils.getTypeObject(request.getAttribute("warehouses"), ArrayList.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>창고 목록</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		function go() {
			var id = $('#prod_id').attr('value');
			
			if(!isNaN(id) && id != '')
				location.assign('/manager/search_prod_inv.do?prod_id=' +id);
			else
				alert('검색을 위해서는 숫자를 입력하셔야 합니다.');
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
	
	<h3 style="margin-top: 30px; margin-bottom: 25px;">창고 관리</h3>
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL' || authority == 'GUEST')}">
	
	<% if(warehouses != null && warehouses.size() != 0) { %>
		<table align="center" width="400px">
			<tr>
				<td>창고 번호</td>
				<td>창고 이름</td>
			</tr>
		
		<% for(Warehouse warehouse : warehouses) { %>
			<c:set var="warehouse" value="<%=warehouse%>" />
			<c:if test="${warehouse != null }">
			<tr>
				<td>#${warehouse.warehouseId}</td>
				<td>
					<a href="/manager/inventories.do?id=${warehouse.warehouseId}">
						${warehouse.warehouseName}
					</a>
				</td>
			</tr>
			</c:if>
		<% } %>
		
		</table>
	
	
	<% } else { %>
		<div>목록이 없습니다</div>
	<% } %>
	
		<div style="margin-top: 10px;">
			<input type="text" id="prod_id" placeholder="제품번호" size="5" />
			<input type="submit" value="찾기" onclick="go();" />
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