<%@page import="com.somecompany.service.CommonService"%>
<%@page import="java.util.Map"%>
<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="com.somecompany.model.oe.Product"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Product product = CommonUtils.getTypeObject(request.getAttribute("product"), Product.class);
	int curPage = CommonUtils.getIntParameter(request.getParameter("page"), 1);
	Map<Short, String> categoryMap = CommonService.getCategoryMap();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>제품 정보 수정</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/list.css" />
	<link rel="stylesheet" type="text/css" href="/css/manager.css" />
	<style>
		td {
			text-align: left;
		}
		
		.label {
			text-align: center;
		}
	</style>
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/js/js_validation.js"></script>
	<script>
		function checkSubmit() {
			// 입력 검증
			var prodName = $('#prod_name').attr('value');
			var prodWarranty = $('#prod_warranty').attr('value');
			var listPrice = $('#prod_list_price').attr('value');
			var minPrice = $('#prod_min_price').attr('value');
			var prodDesc = $('#prod_desc').attr('value');
			
			if(listPrice == '') listPrice = '0.0';
			if(minPrice == '') minPrice = '0.0';
			
			var regex = /^\d{1,2}-\d{1}$/;
			
			return stringCheckLength(prodName, 2, 50, "제품 명은 2글자 이상 50글자 내로 작성해주세요") &&
				stringCheckLength(prodDesc, 0, 2000, "제품 설명은 2000글자 내로 작성해주세요") &&
				isEffectiveOrEmptyNumber(listPrice, "가격은 숫자를 입력해주세요") &&
				isEffectiveOrEmptyNumber(minPrice, "최소 가격은 숫자를 입력해주세요") &&
				gteq(parseFloat(listPrice), parseFloat(minPrice), "가격은 최소가격보다 작을 수 없습니다") &&
				(prodWarranty == '' || regexTest(prodWarranty, regex, "보증기간의 형식은 x-x 혹은 xx-x입니다"));
		}
	</script>
</head>
<body>
	
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id = "main">
	
	<%@ include file="/manager/forms/manager_nav.jsp" %>
	
	<c:set var="login" value="<%=login%>" />
	<c:set var="loginUserId" value="<%=loginUserId%>" />
	<c:set var="loginUserName" value="<%=loginUserName%>" />
	<c:set var="authority" value="<%=authority%>" />
	
	<c:set var="curPage" value="<%=curPage%>" />
	
	<c:choose>
	
	<c:when test="${login == true && (authority == 'PURCHASE' || authority == 'ALL')}">
	
	<h3 style="margin-bottom: 30px;">제품 정보 수정</h3>
		
		<c:set var="prod" value="<%=product%>" />
		
		<c:if test="${prod == null}">
			<script>alert('제품 정보가 없습니다'); location.assign('/manager/prod_list.do');</script>
		</c:if>
		
		<form action="/manager/update_prod.do" method="post" enctype="multipart/form-data">
		
		<input type="hidden" name="prod_id" value="${prod.productId}" />
		
		<table align="center" width="800px">		
			<tr>
				<td class="label">제품번호</td>
				<td>${prod.productId}</td>
				<td class="label">제품명</td>
				<td><input type="text" id="prod_name" name="prod_name" size="30" value="${prod.productName}" /></td>
				
			</tr>
			
			<tr>
				<td class="label">제품 상태</td>
				<td>
					<select id="prod_status" name="prod_status">
						<option value="orderable" <c:if test="${prod.productStatus == 'orderable'}"></c:if>>
							orderable
						</option>
						<option value="planned" <c:if test="${prod.productStatus == 'planned'}"></c:if>>
							planned
						</option>
						<option value="under development" <c:if test="${prod.productStatus == 'under development'}"></c:if>>
							under development
						</option>
						<option value="obsolete" <c:if test="${prod.productStatus == 'obsolete'}"></c:if>>
							obsolete
						</option>
					</select>
				</td>
				<td class="label">보증기간</td>
				<td>
					y-m:
					<input type="text" id="prod_warranty" name="prod_warranty"size="30" value="${prod.warrantyPeriod}" />
				</td>
			</tr>
			
			<tr>
				<td class="label">카테고리</td>
				<td>
					<select id="prod_cat" name="prod_cat" style="width: 150px;">
					<% for(Map.Entry<Short, String> entry : categoryMap.entrySet()) { %>
						<c:set var="catId" value="<%=entry.getKey()%>" />
						<c:set var="prodCatId" value="<%=product.getCategoryId()%>" />
						<option value="${catId}" <c:if test="${catId == prodCatId}">selected</c:if>>
							<%=entry.getValue()%>
						</option>
					<% } %>
					</select>
				</td>
				<td class="label">총 수량</td>
				<td>${prod.qtySum}</td>
			</tr>
			
			<tr>
				<td class="label">가격 </td>
				<td>$<input type="text" id="prod_list_price" name="prod_list_price"size="5"  value="${prod.listPrice}" /></td>
				<td class="label">최소 가격 </td>
				<td>$<input type="text" id="prod_min_price" name="prod_min_price" size="5" value="${prod.minPrice}" /></td>
			</tr>
						
		</table>
		
		<br />
		
		<table align="center" width="800px">
			<tr>
				<td width="20%" class="label">제품 설명</td>
				<td style="text-align: left; padding: 10px;">
					<textarea rows="3" cols="80" id="prod_desc" name="prod_desc">${prod.productDescription}</textarea>
				</td>
			</tr>
		</table>
		
		<br />
		
		<table align="center" width="800px">
			<tr>
				<td class="label">
					<input type="submit" value="수정" onclick="return checkSubmit();" />
					<input type="button" value="취소" onclick="history.go(-1);" />
				</td>
			</tr>
		</table>
		
		</form> <!-- end of form -->
		
		</c:when>
		
		<c:otherwise>
			<script>alert('권한 없는 사용자입니다.'); location.assign('/index.jsp');</script>
		</c:otherwise>
		
		</c:choose>
		
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
	
</body>
</html>