<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.service.ORDER_STATUS"%>
<%@page import="com.somecompany.utils.ListPagingHelper"%>
<%@page import="com.somecompany.model.oe.Order"%>
<%@page import="com.somecompany.model.oe.MyPage"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	MyPage myPage = CommonUtils.getTypeObject(request.getAttribute("myPage"), MyPage.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>마이 페이지</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/cust.css" />
	<style>
		table {
			border-collapse: collapse;
		}
	
		td {
			padding: 10px;
			border: 1px solid black;
		}
		
		.hover {
			cursor: pointer;
		}
		
		.delBtn {
			font-size: 9pt;
			color: silver;
		}
		
		.setBtn {
			font-size: 11pt;
			color: teal;
		}
		
		.signOutBtn {
			font-size: 8pt;
			color: teal;
			float: right;
			padding: 5px;
		}
	</style>
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			$('.delBtn').hover(function() {
				$('.delBtn').addClass('hover');
			});
			
			$('.setBtn').hover(function() {
				$('.setBtn').addClass('hover');
			});
			
			$('.signOutBtn').hover(function() {
				$('.signOutBtn').addClass('hover');
			});
		});
		
		function cancelOrder(orderId, custId) {
			var yes = confirm('주문 ' + orderId + '을 취소하시겠습니까?');
			
			if(yes) 
				location.assign('/cust/cancel_order.do?id=' + orderId + '&cust_id=' + custId);
		}
	</script>
</head>
<body>
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	
	<h3 style="margin-top: 30px; margin-bottom: 20px;">마이 페이지</h3>
	
	<c:set var="myPage" value="<%=myPage%>" />
	<c:set var="cust" value="<%=myPage.getCustomer()%>" />
	
	<c:choose>
	
	<c:when test="${myPage != null && custLogin == true}">
	
	<table align="center" width="700px">
		<tr>
			<td colspan="2">내 정보</td>
		</tr>
			
		<tr>
			<td>이름</td><td>${cust.customerFirstName} ${cust.customerLastName}</td>
		</tr>
		
		<tr>
			<td>주소</td><td><%=myPage.getCustomer().getCustomerAddress()%></td>
		</tr>
		
		<tr>
			<td>연락처</td><td>${cust.phoneNumber}</td>
		</tr>
		
		<tr>
			<td>이메일</td><td>${cust.customerEmail}</td>
		</tr>
		
		<tr>
			<td>생년월일</td><td>${cust.dateOfBirth}</td>
		</tr>
		
		<tr>
			<td>성별</td><td><%= myPage.getCustomer().getGender().equals("M") ? "남" : "여"%></td>
		</tr>
		
		<tr>
			<td>결혼상태</td><td>${cust.maritalStatus}</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<span class="setBtn" style="margin-right: 10px;"
					onclick="location.assign('/cust/set_myinfo.do?id=${cust.customerId}');">
					정보 수정
				</span>
				
				<span class="setBtn" 
					onclick="location.assign('/cust/set_mypasswd.do?id=${cust.customerId}');">
					비밀번호 변경
				</span>
			</td>
		</tr>
	</table>
	
	<br /><br />
	
	<table align="center" width="700px" style="margin-bottom: 20px;">
		<tr>
			<td colspan="6">주문 정보</td>
		</tr>
		
		<tr>
			<td width="15%">주문번호</td>
			<td width="25%">주문일시</td>
			<td width="15%">주문방법</td>
			<td width="15%">주문총액</td>
			<td width="20%">주문상태</td>
			<td width="10%"></td>
		</tr>
		
		<% for(Order order: myPage.getOrderListHelper().getList()) {
			if(order != null) {
		%>
		
		<c:set var="order" value="<%=order%>" />
		<tr>
			<td><a href="/cust/order_detail.do?id=${order.orderId}">${order.orderId}</a></td>
			<td>${order.orderDate}</td>	
			<td>${order.orderMode}</td>
			<td>${order.orderTotal}</td>
			<td><%=ORDER_STATUS.getOrderStatus(order.getOrderStatus()).order_status_desc%></td>
			<td>
				<c:if test="${order.orderStatus == 1}">
				<span class="delBtn" onclick="cancelOrder(${order.orderId}, ${cust.customerId});">주문취소</span>
				</c:if>
			</td>
		</tr>
		<% 	}
		} %>
		
	</table>
	
	
	<%
			// 페이지 계산(시작 페이지, 끝 페이지)
			int totalPage = myPage.getOrderListHelper().getTotalPageCount();
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
				<a href="/cust/mypage.do?page=${startPage-pagePerList}">
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
						<span><a href="/cust/mypage.do?page=${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/cust/mypage.do?page=${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>
			
		</div>
		
		<div style="width: 1000px;">
			<span style="font-size: 8pt;">알림: 주문 취소는 발송 전까지만 가능합니다.</span>
			
			<span class="signOutBtn" onclick="location.assign('/cust/sign_out.do');">
				회원탈퇴
			</span>
		</div>
	
	</c:when>
	
	<c:when test="${custLogin == false}">
		<script>alert('로그인해야 정보를 볼 수 있습니다.'); location.assign('/cust/login.do');</script>
	</c:when>
	
	<c:otherwise>
		<p>오류: 죄송합니다. 정보를 찾을 수 없습니다 :(</p>
	</c:otherwise>
	
	</c:choose>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
	
</body>
</html>