<%@page import="com.somecompany.utils.ListPagingHelper"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Inventory"%>
<%@page import="com.somecompany.utils.ListHelper"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	ListHelper<Inventory> inventories = CommonUtils.getTypeObject(
			request.getAttribute("list_helper"), ListHelper.class);
	int warehouseId = CommonUtils.getIntParameter(request.getParameter("id"), 0);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>재고 목록</title>
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
	
	<c:set var="ware_id" value="<%=warehouseId%>" />
	<c:set var="curPage" value="<%=curPage%>" />
	
	<h3 style="margin-top: 30px; margin-bottom: 25px;">재고 목록</h3>
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL' || authority == 'GUEST')}">
	<% if(inventories != null && inventories.getList().size() != 0) { %>
		<table align="center" width="600px">
			<tr>
				<td width="15%">창고 번호</td>
				<td width="10%"><%=inventories.getList().get(0).getWarehouseId()%></td>
				<td width="15%">창고 이름</td>
				<td width="30%"><%=inventories.getList().get(0).getWarehouseName()%></td>
				<td width="15%">총 항목 수</td>
				<td width="15%"><%=inventories.getTotalCount()%></td>
			</tr>
		</table>
		
		<br /><br />
		
		<table align="center" width="600px">
			<tr>
				<td width="20%">제품 번호</td>
				<td width="60%">제품 이름</td>
				<td width="20%">수량</td>
			</tr>
			
			<% for(Inventory inv : inventories.getList()) { %>
			<c:set var="inv" value="<%=inv%>" />
			<tr>
				<td>${inv.productId}</td>
				<td>${inv.productName}</td>
				<td>${inv.quantityOnHand}</td>
			</tr>
			<% } %>
		
		</table>
		
		<%
			// 페이지 계산(시작 페이지, 끝 페이지)
			int totalPage = inventories.getTotalPageCount();
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
				<a href="/manager/inventories.do?id=${ware_id}&page=${startPage-pagePerList}">
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
						<span><a href="/manager/inventories.do?id=${ware_id}&page=${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/manager/inventories.do?id=${ware_id}&page=${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>
						
		</div>
		
		<div style="text-align: right; padding: 10px;">
		<c:if test="${login == true && (authority == 'PURCHASE' || authority == 'ALL')}">
			<button onclick="location.assign('/manager/warehousing.do?ware_id=${ware_id}');">입출고 관리</button>
		</c:if>
		</div>
		
	<% } else { %>
		<div>목록이 없습니다</div>
	<% } %>
	</c:when>
	
	<c:otherwise>
		<script>alert('권한 없는 사용자입니다.'); location.assign('/index.jsp');</script>
	</c:otherwise>
	
	</c:choose>	 <!-- end of choose -->
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	
	</div>
</body>
</html>