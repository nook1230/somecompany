<%@page import="com.somecompany.service.oe.CustomerService"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	boolean custLogin = CustomerService.isLogin(session);
	String loginCustUserName = "";
	int loginCustUserId = 0;
	
	if(custLogin) {
		loginCustUserName = (String) session.getAttribute(CustomerService.loginUserNameAttr);
		loginCustUserId = (Integer) session.getAttribute(CustomerService.loginUserIdAttr);
	}
%>
	<c:set var="custLogin" value="<%=custLogin%>" />
	<c:set var="custUserName" value="<%=loginCustUserName%>" />
	<c:set var="custUserId" value="<%=loginCustUserId%>" />
	
		<div id="cust_nav">
			<ul>
				<c:choose>
				
				<c:when test="${custLogin != true}">
				
				<li><a href="/cust/login.do" style="color: darkred; font-style: italic; font-size: 11pt;">
					login</a>
				</li>
				<li><a href="/cust/signup.do" style="color: darkred; font-style: italic; font-size: 11pt;">
					signUp</a>
				<li>
				
				</c:when>
				
				<c:otherwise>
				<li>${custUserName}님</li>
				<li><a href="/cust/logout.do" style="color: darkred; font-style: italic; font-size: 11pt;">
				logout</a>
				</li>
				<li><a href="/cust/mypage.do" style="color: darkred; font-style: italic; font-size: 11pt;">
					myPage</a>
				</li>		
				</c:otherwise>
				
				</c:choose>
				
				<li>
					<a href="/cust/cart.do" 
						style="color: darkred; font-style: italic; font-size: 11pt;">
						cart
					</a>
				</li>
			</ul>
		</div>