<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="java.util.Map"%>
<%@page import="com.somecompany.service.CommonService"%>
<%@page import="com.somecompany.utils.ListPagingHelper"%>
<%@page import="com.somecompany.model.hr.Authority"%>
<%@page import="com.somecompany.utils.ListHelper"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ListHelper<Authority> listHelper = CommonUtils.getTypeObject(request.getAttribute("list_helper"), ListHelper.class); 
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
	
	// 부서와 직무 정보를 얻어온다
	Map<Short, String> authMap = CommonService.getAuthorityMap();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>권한 설정</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			var form = $('#auth_form').get(0);
			
			$('#search_id').click(function(event) {
				open('/human_res/search_list.do', 'search',
					'width=600, height=400, top=300, left=400, location=no');
			});
			
			$('#btn_add').click(function(event) {
				event.preventDefault();
				event.stopPropagation();
				
				if($('#search_id').get(0).value != '') {
					form.action = "/human_res/add_auth.do";
					form.submit();
				} else {
					alert('추가할 id를 입력해주세요');
				}
			});
			
			$('#btn_modify').click(function(event) {
				event.preventDefault();
				event.stopPropagation();
				
				var $checked = $(':[checked]');
				
				if($checked.length != 0) {
					form.action = "/human_res/set_auth.do";
					form.submit();
				} else {
					alert('하나 이상의 id를 체크해주세요');
				}
			});
			
			$('#btn_delete').click(function(event) {
				event.preventDefault();
				event.stopPropagation();
				
				var $checked = $(':[checked]');
				
				if($checked.length != 0) {
					form.action = "/human_res/delete_auth.do";
					form.submit();
				} else {
					alert('하나 이상의 id를 체크해주세요');
				}
			});
		});
	</script>
</head>
<body>
	
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<c:set var="login" value="<%=login%>" />
	<c:set var="authority" value="<%=authority%>" />
	<c:set var="curPage" value="<%=curPage%>" />
	
	<div id = "main">
	
	<h3 style="margin-top: 35px;">설정된 권한</h3>
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'ALL' || authority == 'HR')}">
		
	<form id="auth_form" method="post">
	
		<table align="center" width="400px">
			<tr>
				<td></td><td>아이디</td><td>이름</td><td>권한</td>
			</tr>
		
		<% 
		if(listHelper != null && listHelper.getList().size() != 0) {	
			for(Authority authObj : listHelper.getList()) {
				if(authObj != null) {
				
		%>
			<c:set var="auth" value="<%=authObj%>" />
			
			<tr>
				<td><input type="checkbox" name="employee_id_${auth.employeeId}" 
						value="${auth.employeeId}" class="check" /></td>
				<td>${auth.employeeId}</td>
				<td>${auth.employeeName}</td>
				<td>${auth.authorityTitle}</td>
			</tr>
		<%		}
			}
		}
		%>
		
		</table>
				
		<br />
		
		<table align="center" width="400px" style="border: none;">
			<tr style="border: none;">
				<td style="border: none; text-align: left;">
					<input type="text" id="search_id" name="employee_id" 
						size="5" placeholder="click" readonly/>에게 
					<select name="add_authority_id">
					<%
					for(Map.Entry<Short, String> entry: authMap.entrySet()) { %>
						<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
					<% } %>
					</select> 권한 <button id="btn_add">추가</button>
				</td>
			</tr>
			
			<tr style="border: none;">
				<td style="border: none; text-align: left;">
					선택된 사원의 권한을
					<select name="set_authority_id">
					<%
					for(Map.Entry<Short, String> entry: authMap.entrySet()) { %>
						<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
					<% } %>
					</select>
					으로 <button id="btn_modify">변경</button>
				</td>
			</tr>
			
			<tr style="border: none;">
				<td style="border: none; text-align: left;">
					선택된 사원의 권한을 <button id="btn_delete">삭제</button>
				</td>
			</tr>
		</table>
		
	</form>
	
	<%
			// 페이지 계산(시작 페이지, 끝 페이지)
			int totalPage = listHelper.getTotalPageCount();
			int pagePerList = 10;	// 한 번에 보여줄 페이지 수
			
			ListPagingHelper pageHelper = new ListPagingHelper(totalPage, curPage, pagePerList);
			
			int startPage = pageHelper.getStartPage();
			int endPage = pageHelper.getEndPage();
		%>
		
		<c:set var="totalPage" value="<%=totalPage%>" />
		<c:set var="startPage" value="<%=startPage%>" />
		<c:set var="endPage" value="<%=endPage%>" />
		<c:set var="pagePerList" value="<%=pagePerList%>" />
		
		<!-------------------------------- 페이지 이동 링크 ----------------------------------->
		<div id="page_list">
			<c:if test="${startPage != 1}">
			<span>
				<a href="/human_res/admin_auth.do?page=${startPage-pagePerList}">
					[이전]
				</a>
			</span>
			</c:if>
			
			<c:forEach var="i" begin="${startPage}" end="${endPage}">
				<c:choose>
					<c:when test="${i == curPage}">
						<span class="selected">${i}</span>
					</c:when>
					
					<c:otherwise>
						<span><a href="/human_res/admin_auth.do?page=${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/human_res/admin_auth.do?page=${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>
			
		</div>
	
	</c:when>
	
	<c:otherwise>
		<p>권한이 없습니다</p>
		<script>alert('권한이 없습니다'); location.assign('/index.jsp');</script>
	</c:otherwise>
	
	</c:choose>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>