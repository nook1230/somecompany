<%@page import="com.somecompany.service.hr.SecurityService"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%	
	//로그인 여부와 권한 정보를 얻어온다
	boolean login = SecurityService.isLogin(session);
	String authority = "";
	int loginUserId = 0;
	String loginUserName = "";
	
	if(SecurityService.getAuthorityLoginUser(session) != null)
		authority = SecurityService.getAuthorityLoginUser(session);
	
	if(login) {
		loginUserId = (Integer) session.getAttribute(SecurityService.loginUserIdAttr);
		loginUserName = (String) session.getAttribute(SecurityService.loginUserNameAttr);
	}
%>
	<div id="titlebar">
		<div id="login">
		<% if(login) { %>
			<p>
				<%= loginUserName %>님으로 로그인하셨습니다.&nbsp;
				<a href="/human_res/logout.do">로그아웃(사원)</a>
			</p>
		<% } else { %>
			<a href="/human_res/login.do">로그인(사원)</a>
		<% } %>
		</div>
		
		<h1>SOME COMPANY</h1>
	
	</div>

	<div id="nav">
		<ul>
			<li><a href="/index.jsp">Home</a></li>
			<li><a href="/human_res/list.do">Human Resources</a></li>
			<li><a href="/manager/order_list.do">Purchase Management</a></li>
			<li><a href="/cust/prod_list.do">Customer's Page</a></li>
			<li><a href="#">Free Board</a></li>
		</ul>
	</div>
