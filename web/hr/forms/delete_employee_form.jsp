<%@page import="com.somecompany.service.hr.SecurityService"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String id = request.getParameter("id");
	String curPage = request.getParameter("page");
	
	//로그인 여부와 권한 정보를 얻어온다
	boolean login = SecurityService.isLogin(session);
	String authority = "";
		
	if(SecurityService.getAuthorityLoginUser(session) != null)
		authority = SecurityService.getAuthorityLoginUser(session);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>delete</title>
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#confirm').bind('click', function() {
				return confirm('한 번 삭제한 자료는 복구할 수 없습니다. 정말 삭제하시겠습니까?');
			});
		});
	</script>
	
	<style>
		body {
			font-family: "Malgun Gothic", "굴림", "Gulim", "Arial";
			font-size: 10pt;
		}
		
		#main {
			margin-top: 150px;
			width: 600px;
			height: 300px;
			text-align: center;
			margin-left: auto;
			margin-right: auto;
			border: 1px solid silver;
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
	
	<c:set var="login" value="<%=login%>" />
	<c:set var="authority" value="<%=authority%>" />
	<c:set var="id" value="<%=id%>" />
	<c:set var="curPage" value="<%=curPage%>" />
	
	<div id="main">
		<h2 style="margin-top:70px;">사원 정보 삭제</h2>
		
		<c:choose>
		
		<c:when test="${login == true && (authority == 'ALL' || authority == 'HR')}">

		<p>정말로 아이디 ${id}의 사원정보를 삭제하시겠습니까?</p>
		<form method="post" action="/human_res/delete.do">
			<input type="hidden" name="id" value="${id}" />
			<input type="hidden" name="page" value="${curPage}" />
			
			<input type="submit" id="confirm" value="예" />
			<input type="button" value="아니오" onclick="history.go(-1);" />
		</form>
		
		</c:when>
		
		<c:otherwise>
		
		<p>
			잘못된 접근이우다! - 권한 없는 사용자<br /><br />
			<a href="/index.jsp">메인으로</a>
		</p>
		
		</c:otherwise>
		
		</c:choose>
	</div>
</body>
</html>