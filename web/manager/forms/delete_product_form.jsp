<%@page import="com.somecompany.utils.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	int productId = CommonUtils.getIntParameter(request.getParameter("id"), 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>제품 삭제 처리</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#confirm').bind('click', function() {
				return confirm('한 번 삭제한 자료는 복구할 수 없습니다. 정말 삭제하시겠습니까?');
			});
		});
	</script>
</head>
<body>
	<div id="allcontent">
	
	<c:set var="productId" value="<%=productId%>" />
	
	<c:if test="${productId == null}">
		<script>alert('오류: 전달된 제품 정보가 없습니다!'); location.assign('/manager/prod_list.do');</script>
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
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL')}">
	
		<h3 style="margin-top: 30px; margin-bottom: 20px;">제품 정보 삭제</h3>
		
		<p>정말로 제품번호: ${productId}(을)를 삭제하시겠습니까?</p>
		
		<form method="post" action="/manager/del_prod.do">
			<input type="hidden" name="id" value="${productId}" />
			<input type="submit" id="confirm" value="예" />
			<input type="button" value="아니오" onclick="history.go(-1);" />
		</form>
		
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