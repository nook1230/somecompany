<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.service.hr.SecurityService"%>
<%@page import="com.somecompany.service.CommonService"%>
<%@page import="java.util.Map"%>
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
	
	// 부서와 직무 정보를 얻어온다
	Map<Integer, String> departmentMap = CommonService.getDepartmentMap();
	Map<String, String> jobMap = CommonService.getJobMap();
	
	// 쿼리 설정(검색을 위해)
	String queryStr = "";
	StringBuilder builder = new StringBuilder();
	
	if(request.getQueryString() != null) {
		queryStr = request.getQueryString();
		
		String[] splitedQuery = queryStr.split("&");
		
		
		for(String str : splitedQuery) {
			if(!str.startsWith("page="))
				builder.append(str).append("&");
		}
	}
	
	builder.append("page=");
	
	queryStr = builder.toString();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>사원 명부</title>
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<style>
		body {
			font-family: "Malgun Gothic", "굴림", "Gulim", "Arial";
			font-size: 10pt;
		}
		#main {
			margin-top: 10px;
			width: 600px;
			height: 400px;
			text-align: center;
			margin-left: auto;
			margin-right: auto;
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
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		function transferId(id) {
			var searchId = opener.document.getElementById("search_id");
			searchId.value = id;
			self.close();
		}
	</script>
</head>
	
<body>
	<div id="main">
	<% if(login) { %>
		<!------------------------------- 리스트 ------------------------------------->
		<% if(listHelper != null && listHelper.getList().size() != 0) { %>
		<table align="center" width="500px">
		
		<tr>
			<td width="20%">아이디</td>
			<td width="30%">이름</td>
			<td width="20%">직책</td>
			<td width="30%">부서</td>
		</tr>
		
		<% List<Employee> empList = listHelper.getList(); %>
		
		<%
			for(Employee emp : empList) {
				if(emp != null) {
		%>
		
		<c:set var="emp" value="<%=emp%>" />
		<c:set var="curPage" value="<%=curPage%>" />
		<tr onclick="transferId(${emp.employeeId})">
			<td>${emp.employeeId}</td>
			<td>${emp.firstName}&nbsp;${emp.lastName}</td>
			<td>${emp.jobId}</td>
			<td>${emp.departmentName}</td>
		</tr>	
					
			<% }
			} %>
			
		<% } else { %>
		<!-- 결과가 없는 경우 -->
		<tr><td colspan="6">결과가 없어서 미안함메!</td></tr>
		
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
				<a href="/human_res/search_list.do?${queryStr}${startPage-pagePerList}">
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
						<span><a href="/human_res/search_list.do?${queryStr}${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/human_res/search_list.do?${queryStr}${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>
			
		</div>	
		<!----------------------------------------------------------------------------->
		
		<!------------------------------------------------------------------------------------>
	<% } else { %>
		
		<p>로그인 해야 볼 수 있슴메!</p>
		
	<% } %>
	</div>
</body>
</html>