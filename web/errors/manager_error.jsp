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
	
	<h3 style="margin-top: 50px">:( 오류 - 관리자</h3>
	
	<c:choose>
		<c:when test="${errorCode == 'ship_order_dup_ids'}">
		<p>선적 처리 실패 - 중복된 id 전달</p>
		</c:when>
		
		<c:when test="${errorCode == 'ship_order_failure'}">
		<p>선적 처리 실패 - 알 수 없는 오류</p>
		</c:when>
		
		<c:when test="${errorCode == 'complete_order_no_auth'}">
		<p>주문 상태 변경 실패: 배송 완료 - 권한 없음</p>
		</c:when>
		
		<c:when test="${errorCode == 'cancel_order_no_auth'}">
		<p>주문 상태 변경 실패: 주문 취소 - 권한 없음</p>
		</c:when>
		
		<c:when test="${errorCode == 'pay_order_no_auth'}">
		<p>주문 상태 변경 실패: 결제 완료 - 권한 없음</p>
		</c:when>
		
		<c:when test="${errorCode == 'delete_order_failure'}">
		<p>주문 삭제 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'manage_warehouse_failure'}">
		<p>입출고 처리 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'add_prod_failure'}">
		<p>제품 추가 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'add_prod_null_prod'}">
		<p>제품 추가 실패 - null 객체 전달</p>
		</c:when>
		
		<c:when test="${errorCode == 'update_prod_failure'}">
		<p>제품 정보 변경 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'update_prod_null_prod'}">
		<p>제품 정보 변경 실패 - null 객체 전달</p>
		</c:when>
		
		<c:when test="${errorCode == 'delete_prod_failure'}">
		<p>제품 정보 삭제 실패</p>
		</c:when>
		
		<c:when test="${errorCode == 'delete_cust_failure'}">
		<p>회원 강제 탈퇴 처리 실패</p>
		</c:when>
		
		<c:otherwise>
		<p>알 수 없는 오류</p>
		</c:otherwise>
	</c:choose>
	
		<div style="margin-top: 20px;">
			<a href="/index.jsp">메인으로</a>
		</div>
	
	</div>
</body>
</html>