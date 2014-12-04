<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String errorName = "";
	if(request.getParameter("error_name") != null)
		errorName = request.getParameter("error_name");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>:( error</title>
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
	<c:set var="errorName" value="<%=errorName%>" />
	
	<div id="main">
	
	<h3 style="margin-top: 50px">:( 오류 - Human Resources</h3>
	
	<c:choose>
		<c:when test="${errorName == 'java.sql.SQLException'}">
		<p>작업 실패: DB 오류</p>
		</c:when>
		
		<c:when test="${errorName == 'java.lang.reflect.InvocationTargetException'}">
		<p>작업 실패: DB 오류</p>
		</c:when>
		
		<c:when test="${errorName == 'com.somecompany.repository.exceptions.DataAccessException'}">
		<p>작업 실패: DB 오류</p>
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