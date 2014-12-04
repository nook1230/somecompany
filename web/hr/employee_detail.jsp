<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.hr.Employee"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
	Employee employee = CommonUtils.getTypeObject(request.getAttribute("employee"), Employee.class);
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			
			var search = location.search;
			
			$('#btn_update').bind('click', function() {
				location.assign('/human_res/update.do' + search);
			});
			
			$('#btn_delete').bind('click', function() {
				location.assign('/human_res/delete.do' + search);
			});
		});
	</script>
	
	<style>
		table {
			border-collapse: collapse;
		}
		
		tr {
			border-top: 1px solid gray;
			border-bottom: 1px solid gray;
		}
		
		td {
			padding: 5px;
			text-align: left;
		}
	</style>
<title>사원 정보 상세 보기</title>
</head>
<body>
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id = "main">
	
	<c:set var="login" value="<%=login%>" />
	<c:set var="authority" value="<%=authority%>" />
	<c:set var="curPage" value="<%=curPage%>" />
	<c:set var="emp" value="<%=employee%>" />
	<c:set var="loginUserId" value="<%=loginUserId%>" />
	
	<c:choose>
	
	<c:when test="${login == true}">
		
		<c:if test="${emp == null}">
			<script>
				alert("존재하지 않는 id입니다.");
				location.assign("/human_res/list.do");
			</script>
		</c:if>
		
		<c:if test="${emp != null}">
		
		<%
			String profilePictureName = "profile_default_pic.jpg";
			if(!employee.getProfilePictureName().isEmpty()) {
				profilePictureName = employee.getEmployeeId() + "_" + employee.getProfilePictureName();
			}
		%>
		<c:set var="profilePictureName" value="<%=profilePictureName%>" />
		
		<h2>사원 정보 보기</h2>
		
		<table align="center" width="800px" style="margin-top: 50px;">
			<tr>
				<td><img src="/hr/profile_pic/${profilePictureName}" width="100px" height="133px" /></td>
				<td colspan="5" style="font-size: 11pt;">${emp.profileComment}</td>
			</tr>
			<tr>
				<td>이름</td><td>${emp.firstName}&nbsp;${emp.lastName}</td>
				<td>부서</td><td>${emp.departmentName}</td>
				<td>직책</td><td>${emp.jobTitle}</td>
				
			</tr>
			
			<tr>
				<td>이메일</td><td>${emp.email}</td>
				<td>연락처</td><td>${emp.phoneNumber}</td>
				<td>입사일</td><td>${emp.hireDate}</td>
			</tr>
			
			<tr>
				<td>봉급</td><td>${emp.salary}</td>
				<td>커미션</td><td>${emp.commissionPct}</td>
				<td>담당자</td><td>${emp.managerName}</td>
			</tr>
			
			<tr>
				<td colspan="6">근무 이력</td>
			</tr>
			
			<tr>
				<td colspan="6"style="text-align: left;">${emp.jobHistory}</td>
			</tr>
		</table>
		
		</c:if>
		
		<div style="margin-top: 15px;"><a href="/human_res/list.do?page=${curPage}">리스트 보기</a></div>
		
		
		<!-- 사원 정보 수정/삭제 버튼 -->
		<div style="margin-top: 30px; width: 900px; text-align: right;">
			<c:if test="${authority == 'ALL' || authority == 'HR' || emp.employeeId == loginUserId}">
			<button id="btn_update">수정</button>&nbsp;
			</c:if>
			
			<c:if test="${authority == 'ALL' || authority == 'HR'}">
			<button id="btn_delete">삭제</button>
			</c:if>
			
		</div>
		
	</c:when>
	
	<c:otherwise>
	<!------- 로그인 안 한 경우 --------->
		<p>로그인 해야 볼 수 있슴메!</p>
	</c:otherwise>
	
	</c:choose>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>