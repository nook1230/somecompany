<%@page import="com.somecompany.utils.ListPagingHelper"%>
<%@page import="com.somecompany.utils.QueryCallback"%>
<%@page import="com.somecompany.utils.QueryProcessor"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Customer"%>
<%@page import="com.somecompany.utils.ListHelper"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ListHelper<Customer> listHelper = CommonUtils.getTypeObject(
			request.getAttribute("list_helper"), ListHelper.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
	int searchby = CommonUtils.getIntParameter(request.getParameter("searchby"), 0);
	String keyword = CommonUtils.getStringParameter(request.getParameter("keyword"), "");
	
	// 쿼리 설정(페이지 유지)
	String[] droppedParams = {"page"};
	String queryStr = QueryProcessor.setQuery(request.getQueryString(), droppedParams, null, new QueryCallback() {
		public Object doSomething(String str) { return 0; }
	});
	
	queryStr += "page=";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>고객 목록</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
	<style>
		.center {
			text-align: center;
		}
		
		.left {
			text-align: left;
		}
	</style>
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/js/js_util.js"></script>
	<script>
		$(document).ready(function() {
			$(document).keydown(function(event) {
				if(event.keyCode == 13)
					$('#btn_search').click();
			});
			
			$('#btn_search').click(function() {
				var selSearchby = $('#sel_searchby').get(0);
				
				var searchby = selSearchby.options[selSearchby.selectedIndex].value;
				var keyword = $('#keyword').attr('value');
				var page = extractParameter(location.search, 'page');
				
				goPage = '/manager/cust_list.do?searchby=' + searchby + 
						'&keyword=' + keyword + '&page=' + page;
				
				location.assign(goPage);
			});
		});
	</script>
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
	
	<c:set var="queryStr" value="<%=queryStr%>" />
	<c:set var="curPage" value="<%=curPage%>" />
	
	<h3 style="margin-top: 30px; margin-bottom: 25px;">고객 목록</h3>
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL' || authority == 'GUEST')}">
	<% if(listHelper != null && listHelper.getList().size() != 0) { %>
		
		<table align="center" width="800px">
			<tr>
				<td width="10%" class="center">고객번호</td>
				<td width="20%" class="center">고객 이름</td>
				<td width="20%" class="center">연락처</td>
				<td width="20%" class="center">이메일</td>
				<td width="30%" class="center">주소</td>
			</tr>
			
			<% for(Customer cust : listHelper.getList()) { %>
			<c:set var="cust" value="<%=cust%>" />
			<tr>
				<td class="center">
					<a href="/manager/cust_detail.do?id=${cust.customerId}&${queryStr}${curPage}">
						${cust.customerId}
					</a>
				</td>
				<td class="center">
					<a href="/manager/cust_detail.do?id=${cust.customerId}&${queryStr}${curPage}">
						${cust.customerFirstName} ${cust.customerLastName}
					</a>
				</td>
				<td class="center">${cust.phoneNumber}</td>
				<td class="left">${cust.customerEmail}</td>
				<td class="left"><%=cust.getCustomerAddress()%></td>
			</tr>
			<% } %>
		
		</table>
		
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
				<a href="/manager/cust_list.do?${queryStr}${startPage-pagePerList}">
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
						<span><a href="/manager/cust_list.do?${queryStr}${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/manager/cust_list.do?${queryStr}${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>			
		</div>
		
		<div style="margin-top: 25px; margin-bottom: 20px;">
			<select id="sel_searchby" name="searchby">
				<option value="2">성</option>
				<option value="1">이름</option>
				<option value="3">이메일</option>
				<option value="4" selected>모두</option>
			</select>
			
			<input type="text" id="keyword" name="keyword" value="<%=keyword%>" size="45" />
			
			<button id="btn_search">검색</button> 
		</div>
		
	
	<% } else { %>
		<div>주문 목록이 없습니다</div>
	<% } %>
	
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