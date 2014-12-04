<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String errorCode = "";
	if(request.getParameter("error_code") != null)
		errorCode = request.getParameter("error_code");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Human Resource - error</title>
	<style>
		body {
			font-family: "Malgun Gothic", "굴림", "Gulim", "Arial";
			font-size: 10pt;
		}
		
		#main {
			margin-top: 150px;
			width: 450px;
			height: 250px;
			text-align: center;
			margin-left: auto;
			margin-right: auto;
			border: 1px solid gray;
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
</head>
<body>
	<c:set var="errorCode" value="<%=errorCode%>" />
	
	<div id="main">
	
	<h3 style="margin-top: 50px">:( 오류 - Human Resources</h3>
	
	<c:choose>
		<c:when test="${errorCode == 'add_null_cust'}">
		<p>회원 가입: null 객체 리턴</p>
		</c:when>
		
		<c:when test="${errorCode == 'add_failure'}">
		<p>회원 가입: 회원 가입 실패 - 알 수 없는 오류</p>
		</c:when>
		
		<c:when test="${errorCode == 'addcart_failure'}">
		<p>장바구니 오류: 장바구니에 추가 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'addcart_zero_qty'}">
		<p>장바구니 오류: 수량 0은 추가할 수 없습니다</p>
		</c:when>
		
		<c:when test="${errorCode == 'addcart_no_prod_id'}">
		<p>장바구니 오류: 제품 정보가 전달되지 않았습니다</p>
		</c:when>
		
		<c:when test="${errorCode == 'add_order_failure'}">
		<p>주문 오류: 주문 추가 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'cancel_order_invalid_access'}">
		<p>주문 오류: 주문취소 실패 -  잘못된 접근</p>
		</c:when>
		
		<c:when test="${errorCode == 'cancel_order_failure'}">
		<p>주문 오류: 주문취소 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'change_passwd_failure'}">
		<p>비밀번호 변경 오류: 알 수 없는 오류</p>
		</c:when>
		
		<c:when test="${errorCode == 'change_passwd_no_info'}">
		<p>비밀번호 변경 오류: 잘못된 정보</p>
		</c:when>
		
		<c:otherwise>
		<p>알 수 없는 오류</p>
		</c:otherwise>
	</c:choose>
	
		<div style="margin-top: 20px;">
			<a href="/cust/prod_list.do">메인으로</a>
		</div>
	
	</div>
</body>
</html>