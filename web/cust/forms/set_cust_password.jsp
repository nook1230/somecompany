<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Customer"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Customer customer = CommonUtils.getTypeObject(request.getAttribute("customer"), Customer.class); 
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>비밀번호 변경</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/cust.css" />
	<style>
		table {
			border-collapse: collapse;
		}
	
		td {
			text-align: left;
			padding: 10px;
			border: 1px solid black;
		}
		
		td.label {
			text-align: center;
		}
		
		.hover {
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/js/js_validation.js"></script>
	<script>
		function checkSubmit() {
			// 입력 검증
			var oldPasswd = $('#old_passwd').attr('value');
			var passwd1 = $('#passwd1').attr('value');
			var passwd2 = $('#passwd2').attr('value');
			
			return isNotEmptyString(oldPasswd, "기존 비밀번호를 입력해주세요") &&
				stringCheckLength(passwd1, 4, 50, "비밀번호는 4글자 이상 50글자 내로 작성해주세요") &&
				isEqual(passwd1, passwd2, "입력하신 비밀번호가 서로 다릅니다");
		}
	</script>
</head>
<body>
	<div id="allcontent">
	
	<c:set var="cust" value="<%=customer%>" />
	
	<c:if test="${cust == null}">
		<script>alert('오류: 전달된 고객 정보가 없습니다.'); location.assign('/cust/prod_list.do');</script>
	</c:if>
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	
	<h3 style="margin-top: 30px; margin-bottom: 20px;">비밀번호 변경</h3>
	
	<c:choose>
				
	<c:when test="${custLogin == true && custUserId == cust.customerId}"> 
		
	<form method="post" action="/cust/set_mypasswd.do">
		<input type="hidden" name="cust_id" value="${cust.customerId}" />
		<table align="center" width="450px">
			<tr>
				<td><label for="old_passwd">기존 비밀번호</label></td>
				<td><input type="password" id="old_passwd" name="old_passwd" size="40" /></td>
			</tr>
			
			<tr>
				<td><label for="passwd1">비밀번호 1</label></td>
				<td><input type="password" id="passwd1" name="passwd1" size="40" /></td>
			</tr>
			
			<tr>
				<td><label for="passwd2">비밀번호 2</label></td>
				<td><input type="password" id="passwd2" name="passwd2" size="40" /></td>
			</tr>
			
			<tr>
				<td colspan="2" style="text-align: right;">
					<input type="submit" name="submit" value="변경" onclick="return checkSubmit();" />
					<input type="button" value="취소" onclick="history.go(-1);" />
				</td>
			</tr>
		</table>
		
	</form>
	
	</c:when>
	
	<c:otherwise>
		<script>alert('오류: 잘못된 접근입니다.'); location.assign('/cust/prod_list.do');</script>	
	</c:otherwise>
	
	</c:choose>
				
	<div style="margin-top: 20px;">
		<a href="/cust/prod_list.do">사용자 메인으로</a>
	</div>
		
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>