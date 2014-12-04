<%@page import="com.somecompany.model.oe.Customer"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	Customer customer = CommonUtils.getTypeObject(request.getAttribute("customer"), Customer.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
	int searchby = CommonUtils.getIntParameter(request.getParameter("searchby"), 0);
	String keyword = CommonUtils.getStringParameter(request.getParameter("keyword"), "");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>고객 정보</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
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
	<c:set var="cust" value="<%=customer%>" />
	
	<c:set var="curPage" value="<%=curPage%>" />
	<c:set var="searchby" value="<%=searchby%>" />
	<c:set var="keyword" value="<%=keyword%>" />
	
	<h3 style="margin-top: 30px; margin-bottom: 25px;">고객 정보</h3>
	
	<c:if test="${cust == null }">
		<script>alert('전달 받은 정보가 없습니다'); location.assign('/manager/cust_list.do');</script>
	</c:if>
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL' || authority == 'GUEST')}">
		<table align="center" width="800px" height="300px;">
			<tr>
				<td>번호</td>
				<td>${cust.customerId}</td>
				<td>이름</td>
				<td colspan="3">${cust.customerFirstName} ${cust.customerLastName}</td>
			</tr>
			
			<tr>
				<td width="10%">이메일</td>
				<td width="25%">${cust.customerEmail}</td>
				<td width="10%">연락처</td>
				<td width="25%">${cust.phoneNumber}</td>
				<td width="10%">성별</td>
				<% String gender = (customer.getGender().equals("M")) ? "남자" : "여자"; %>
				<td width="15%"><%=gender%></td>
			</tr>
			
			<tr>
				<td>주소</td>
				<td colspan="5"><%=customer.getCustomerAddress()%></td>
			</tr>
			
			<tr>
				<td>결혼상태</td>
				<td>${cust.maritalStatus}</td>
				<td>생년월일</td>
				<td>${cust.dateOfBirth}</td>
				<td>신용한도</td>
				<td>${cust.incomeLevel}</td>
			</tr>
		</table>
		
		<div style="margin-top: 25px; padding: 10px;">
		<c:if test="${authority == 'PURCHASE' || authority == 'ALL'}">
			<div>
				<button onclick="location.assign('/manager/del_cust.do?id=${cust.customerId}')">강제 탈퇴</button>
			</div>
		</c:if>	
			<div style="margin-top: 15px;">
				<a href="/manager/cust_list.do?page=${curPage}&searchby=${searchby}&keyword=${keyword}">목록 보기</a>
			</div>
		</div>
		
		
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