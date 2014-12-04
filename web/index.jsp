<%@page import="com.somecompany.service.hr.SecurityService"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>미래를 선도하는 기업 Some Company</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<style></style>
</head>

<body>
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
		<h3>Welcome to Some Company's Home!</h3>
		
		<img src="/images/welcome.jpg" />
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>