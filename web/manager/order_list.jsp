<%@page import="com.somecompany.utils.QueryCallback"%>
<%@page import="com.somecompany.utils.QueryProcessor"%>
<%@page import="com.somecompany.utils.ListPagingHelper"%>
<%@page import="com.somecompany.service.ORDER_STATUS"%>
<%@page import="com.somecompany.model.oe.Order"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.utils.ListHelper"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ListHelper<Order> listHelper = CommonUtils.getTypeObject(
			request.getAttribute("list_helper"), ListHelper.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
	short status = CommonUtils.getShortParameter(request.getParameter("status"), (short) -1);
	
	// 쿼리 설정(주문 상태 검색을 위해)
	String[] droppedParams = {"page"};
	String queryStr = QueryProcessor.setQuery(request.getQueryString(), droppedParams, null, new QueryCallback() {
		public Object doSomething(String str) { return 0; }
	});
	
	queryStr += "page=";
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>주문 목록</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
	<style>
		#manager_box {
			background-color: skyblue; 
			margin-top: 20px; 
			margin-bottom: 10px; 
			text-align: right; 
			padding: 5px;
		}
		
		.hover {
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#manager_box').click(function(event) {
				$('#inner_contents').toggle();
				$('#toggle_elem').toggle();
				
				event.stopPropagation();
			});
			
			$('#manager_box').hover(function() {
				$('#manager_box').addClass('hover');
			});
			
			$('#sel_new_status').click(function(event) {
				event.stopPropagation();
			});
			
			// 관리자 용 버튼 이벤트 컨트롤
			$('#inner_contents button').click(function(event) {
				event.stopPropagation();
				event.preventDefault();
				
				var form = $('#f_order').get(0);
				var onlyone = false;
				
				switch(event.target.id) {
				case 'btn_cancel': // 주문 취소(관리자에 의해)
					form.action = '/manager/cancel_order.do';
					break;
					
				case 'btn_pay':	// 결제 확인
					form.action = '/manager/pay_order_ok.do';
					break;
					
				case 'btn_delivery': // 발송(선적)
					onlyone = true;		// id는 하나만 입력 받는다 
					form.action = '/manager/ship_order_first.do';		// 처리 전 단계로 이동
					break;
					
				case 'btn_complete': // 배송 완료(주문 처리 완료)
					onlyone = true;		// id는 하나만 입력 받는다 
					form.action = '/manager/complete_order.do';
					break;
					
				default:
					return false;
				}
				
				if(checkSubmit(onlyone))
					form.submit();
			});
		});
		
		// 서브밋 전 검정
		function checkSubmit(onlyone) {
			var $checked = $(':[checked]');
			
			if(!onlyone) {
				// 한 개 이상의 id를 입력해야 할 때
				if($checked.length != 0) {
					return true;
				} else {
					alert('하나 이상의 id를 체크해주세요');
					return false;
				}
			} else {
				// 하나의 id만 입력해야 할 때
				if($checked.length == 1) {
					return true;
				} else {
					alert('정확히 하나의 id를 체크해주세요');
					return false;
				}
			}
		}
		
		// 주문 상태에 따라 주문 목록 표시
		function go() {
			var selStatus = document.getElementById('sel_status');
			var status = selStatus.options[selStatus.selectedIndex].value;
			
			location.assign('/manager/order_list.do?status=' + status);
		}
		
		// id 전체 선택 체크 토글
		function toggleCheck() {
			var chkAll = document.getElementById('all_check');
			
			if(chkAll.checked)
				$('.id_check').attr('checked', 'true');
			else
				$('.id_check').removeAttr('checked');
		}
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
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL' || authority == 'GUEST')}">
	
	<% if(listHelper != null && listHelper.getList().size() != 0) { %>
		<h3 style="margin-top: 30px; margin-bottom: 25px;">주문 목록</h3>
		
		<form method="post" id="f_order">
		
		<table align="center" width="600px">
			<tr>
				<td width="5%"><input id="all_check" type="checkbox" onclick="toggleCheck();" /></td>
				<td width="15%">주문번호</td>
				<td width="15%">주문방식</td>
				<td width="25%">주문총액</td>
				<td width="25%">주문상태</td>
				<td width="15%">주문날짜</td>
			</tr>
			
			<% for(Order order : listHelper.getList()) { %>
			<c:set var="order" value="<%=order%>" />
			<tr>
				<td><input type="checkbox" name="order_id_${order.orderId}" value="${order.orderId}" class="id_check"  /></td>
				<td><a href="/manager/order_detail.do?id=${order.orderId}&${queryStr}${curPage}">${order.orderId}</a></td>
				<td>${order.orderMode}</td>
				<td>$${order.orderTotal}</td>
				<td><%=ORDER_STATUS.getOrderStatus(order.getOrderStatus()).order_status_desc%></td>
				<td><%=order.getOrderDate().toString().substring(0, 10) %></td>
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
				<a href="/manager/order_list.do?${queryStr}${startPage-pagePerList}">
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
						<span><a href="/manager/order_list.do?${queryStr}${i}">${i}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${endPage != totalPage}">
			<span>
				<a href="/manager/order_list.do?${queryStr}${startPage+pagePerList}">
					[다음]
				</a>
			</span>
			</c:if>
						
		</div>
		
		<div style="padding-left: 425px;">
			<select id="sel_status" onchange="go();" style="width: 100px;">
			<% for(int i = -1; i <= 12; i++) { %>
				<option value="<%=i%>" <% if(status == i) {%> selected <%}%>>
					[<%=i%>] <%=ORDER_STATUS.getOrderStatus(i).order_status_desc%>
				</option>
			<% } %>
		</select>
		</div>
		
		<c:if test="${authority == 'PURCHASE' || authority == 'ALL'}">
		<div id="manager_box">
			<div id="toggle_elem">관리자 메뉴</div>
			<div id="inner_contents" style="display:none;">
				선택된 주문을 
				<button id="btn_cancel">취소</button>&nbsp;
				<button id="btn_pay">결제확인</button><br />
				
				선택된 주문을 
				<select name="new_status" id="sel_new_status">
				<% for(int i = 4; i <= 10; i++) { %>
					<option value="<%=i%>">
						<%=ORDER_STATUS.getOrderStatus(i).order_status_desc%>
					</option>
				<% } %>
				</select>(으)로&nbsp;
				<button id="btn_delivery">발송</button><br />
				
				선택된 주문을 <button id="btn_complete">배송완료 확인</button>
			</div>
		</div>
		</c:if>
		
		</form>
		
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