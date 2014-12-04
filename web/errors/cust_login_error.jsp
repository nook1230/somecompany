<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String errorCode = "";
	if(request.getParameter("error_code") != null)
		errorCode = request.getParameter("error_code");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>로그인/아웃 처리 오류</title>
	<style>
		body {
			font-family: "Malgun Gothic", "굴림", "Gulim", "Arial";
			font-size: 10pt;
		}
		
		#main {
			margin-top: 150px;
			width: 450px;
			height: 250px;
			text-align: center;
			margin-left: auto;
			margin-right: auto;
			border: 1px solid gray;
		}
		
		a:visited {
			color: #696969;
			text-decoration: none; 
		}

		a:link {
			color: #696969;
			text-decoration: none; 
		}
		
		a:hoover {
			color: #696969;
			text-decoration: none; 
		}
	</style>
</head>
<body>
	<c:set var="errorCode" value="<%=errorCode%>" />
	
	<div id="main">
	
	<h3 style="margin-top: 50px">:( 오류</h3>
	
	<c:choose>
		<c:when test="${errorCode == 'invalid_user_id_or_password'}">
		<p>아이디와 비밀번호가 일치하지 않습니다.</p>
		</c:when>
		
		<c:when test="${errorCode == 'login_no_user_id'}">
		<p>아이디가 입력되지 않음</p>
		</c:when>
		
		<c:when test="${errorCode == 'login_no_password'}">
		<p>비밀번호가 입력되지 않음</p>
		</c:when>
		
		<c:when test="${errorCode == 'logout_failure'}">
		<p>로그아웃 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'logout_no_login'}">
		<p>로그인 상태가 아닙니다</p>
		</c:when>
		
		<c:otherwise>
		<p>알 수 없는 오류</p>
		</c:otherwise>
	</c:choose>
	
		<div style="margin-top: 20px;">
			<a href="/index.jsp">메인으로</a>
		</div>
	
	</div>
</body>
</html>