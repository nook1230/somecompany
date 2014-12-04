<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.utils.QueryCallback"%>
<%@page import="com.somecompany.utils.QueryProcessor"%>
<%@page import="java.util.Map"%>
<%@page import="com.somecompany.service.CommonService"%>
<%@page import="com.somecompany.utils.ListPagingHelper"%>
<%@page import="com.somecompany.model.hr.Employee"%>
<%@page import="java.util.List"%>
<%@page import="com.somecompany.utils.ListHelper"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	
	ListHelper<Employee> listHelper = CommonUtils.getTypeObject(request.getAttribute("list_helper"), ListHelper.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);

	// 부서와 직무 정보를 얻어온다
	Map<Integer, String> departmentMap = CommonService.getDepartmentMap();
	Map<String, String> jobMap = CommonService.getJobMap();
	
	// 쿼리 설정(검색을 위해)
	String[] droppedParams = {"page"};
	String queryStr = QueryProcessor.setQuery(request.getQueryString(), droppedParams, null, new QueryCallback() {
		public Object doSomething(String str) { return 0; }
	});
	
	queryStr += "page=";		
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>사원 명부</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#sel_searchby').bind('change', sel_click);
		});
		
		// sel_searchby의 이벤트 핸들러
		sel_click = function() {
			var selSearchby = $('#sel_searchby').get(0);
			var selectedIdx = selSearchby.selectedIndex;
			var visibleElemName = "";
			
			// 보여질 요소의 이름
			switch(selectedIdx) {
			case 0:
				visibleElemName = "#sel_dep_keyword";
				break;
				
			case 1:
				visibleElemName = "#sel_job_keyword";
				break;
				
			case 2:
				visibleElemName = "#txt_name_keyword";
				break;
				
			case 3:
				visibleElemName = "#txt_name_keyword";
				break;
			}
			
			// 보여질 요소를 설정한다
			setSearchElements(visibleElemName);
		}
		
		function setSearchElements(visibleElemName) {
			var $name = $('#txt_name_keyword');
			var $dep = $('#sel_job_keyword');
			var $job = $('#sel_dep_keyword');
			
			// 일단 모두 감춘 후
			$name.css("display", "none");
			$dep.css("display", "none");
			$job.css("display", "none");
			
			// 하나의 요소만 보여준다
			$(visibleElemName).css("display", "inline");
		}
	</script>
</head>
	
<body>
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id = "main">
	
	<h3>사원 명부</h3>
	
	<% if(login) { %>
		<!------------------------------- 리스트 ------------------------------------->
		<% if(listHelper != null && listHelper.getList().size() != 0) { %>
		<table align="center" width="800px">
		
		<tr>
			<td width="10%">아이디</td>
			<td width="20%">이름</td>
			<td width="20%">직책</td>
			<td width="20%">부서</td>
			<td width="15%">이메일</td>
			<td width="15%">연락처</td>
		</tr>
		
		<% List<Employee> empList = listHelper.getList(); %>
		
		<%
		
			for(Employee emp : empList) {
				if(emp != null) {
		%>
		
		<c:set var="emp" value="<%=emp%>" />
		<c:set var="curPage" value="<%=curPage%>" />
		<tr>
			<td>${emp.employeeId}</td>
			<td><a href="/human_res/get.do?id=${emp.employeeId}&page=${curPage}">
				${emp.firstName}&nbsp;${emp.lastName}</a>
			</td>
			<td>${emp.jobId}</td>
			<td>${emp.departmentName}</td>
			<td>${emp.email}</td>
			<td>${emp.phoneNumber}</td>
		</tr>	
					
			<% }
			} %>
			
		<% } else { %>
		<!-- 결과가 없는 경우 -->
		<tr><td colspan="6">조회된 결과가 없습니다</td></tr>
		
		<% } %>
		
		</table>
		
		<!-------------------------------------------------------------------------------->
		
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
		<c:set var="queryStr" value="<%=queryStr%>" />
		
		<!-------------------------------- 페이지 이동 링크 ----------------------------------->
		<div id="page_list">
			<c:if test="${startPage != 1}">
			<span>
				<a href="/human_res/list.do?${queryStr}${startPage-pagePerList}">
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
						<span><a href="/human_res/list.do?${queryStr}${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/human_res/list.do?${queryStr}${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>
			
		</div>	
		<!----------------------------------------------------------------------------->
		
		<!---------------------------------- 검색 창 ------------------------------------->
		<div id="search">
			<form method="POST" action="/human_res/list.do">
			<select id="sel_searchby" name="searchby">
				<option value="dep">department</option>
				<option value="job">job</option>
				<option value="name">name</option>
				<option value="all" selected>all</option>
			</select>
			
			<!------------------- 검색 조건에 따라 보여지는 요소가 선택됨 ------------------------->
			<select id="sel_job_keyword" name="job_keyword" style="display: none;">
				<%
				for(Map.Entry<String, String> entry: jobMap.entrySet()) { %>
				<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
				<% } %>
			</select>
			
			<select id="sel_dep_keyword" name="department_keyword" style="display: none;">
				<%
				for(Map.Entry<Integer, String> entry: departmentMap.entrySet()) { %>
				<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
				<% } %>
			</select>
			
			<input type="text" id="txt_name_keyword" name="name_keyword" size="30" />
			<!------------------------------------------------------------------------>
			
			<input type="submit" value="검색" />
			
			</form>
		</div>
		
		<!------------------------------------------------------------------------------------>
		
		<c:set var="authority" value="<%=authority%>" />
		<c:if test="${authority == 'ALL' || authority == 'HR'}">
			<!-- 사원 정보 추가(링크) & 권한 설정 링크 -->
			<div style="text-align: right; padding-right: 10px;">
				<p>
					<a href="/human_res/add.do">사원 정보 추가</a>&nbsp;
					<a href="/human_res/admin_auth.do">권한 정보 설정
				</a>
			</div>			
		</c:if>
		
	<% } else { %>
		
		<p>해당 정보를 조회하기 위해서는 로그인이 필요합니다</p>
		<script>alert('해당 정보를 조회하기 위해서는 로그인이 필요합니다!'); location.assign('/human_res/login.do');</script>
	<% } %>
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
	
</body>
</html>