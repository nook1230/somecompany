<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>회원 탈퇴</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/cust.css" />
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#confirm').bind('click', function() {
				return confirm('한 번 삭제한 자료는 복구할 수 없습니다. 정말 탈퇴하시겠습니까?');
			});
		});
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
	
		<h2 style="margin-top:70px; margin-bottom: 20px;">회원 탈퇴</h2>
			
	<c:choose>
		
	<c:when test="${custLogin == true}">

		<p>정말로 탈퇴하시겠습니까?</p>
		
		<form method="post" action="/cust/sign_out.do">
			<input type="hidden" name="cust_id" value="${custUserId}" />
			
			<input type="submit" id="confirm" value="예" />
			<input type="button" value="아니오" onclick="history.go(-1);" />
		</form>
	
	</c:when>
	
	<c:otherwise>
	
		<p>
			잘못된 접근입니다 - 권한 없는 사용자<br /><br />
			<a href="/cust/prod_list.do">고객 페이지로</a>
		</p>
			
	</c:otherwise>
			
	</c:choose>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>