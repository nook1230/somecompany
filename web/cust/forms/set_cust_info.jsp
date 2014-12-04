<%@page import="com.somecompany.utils.CommonUtils"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.sql.Date"%>
<%@page import="com.somecompany.model.oe.Customer"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	Customer customer = CommonUtils.getTypeObject(request.getAttribute("customer"), Customer.class); 
	
	Calendar cal = new GregorianCalendar();
	int curYear = cal.get(Calendar.YEAR);
	
	Date dateOfBirth;
	int year = 0;
	int month = 0;
	int day = 0;
	
	if(customer != null && customer.getDateOfBirth() != null) {
		cal.setTime(customer.getDateOfBirth());
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		day = cal.get(Calendar.DAY_OF_MONTH);
	}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>내 정보 수정</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/cust.css" />
	<style>
		table {
			border-collapse: collapse;
		}
	
		td {
			text-align: left;
			padding: 10px;
			border: 1px solid black;
		}
		
		td.label {
			text-align: center;
		}
		
		.hover {
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/js/js_util.js"></script>
	<script type="text/javascript" src="/js/js_validation.js"></script>
	<script>
		function checkSubmit() {
			// 입력 검증
			var firstName = $('#first_name').attr('value');
			var lastName = $('#last_name').attr('value');
			var email = $('#email').attr('value');
			var phoneNumber = $('#phone_number').attr('value');
			
			var birthYear = $('#birth_year').attr('value');
			var birthMonth = $('#birth_month').attr('value');
			var birthDay = $('#birth_day').attr('value');
			
			var countryId = $('#country_id').attr('value');
			var postalCode = $('#postal_code').attr('value');
			var stateProvince = $('#state_province').attr('value');
			var city = $('#city').attr('value');
			var streetAddress = $('#street_address').attr('value');
			
			return stringCheckLength(firstName, 0, 20, "이름은 20글자 내로 작성해주세요") &&
				notEmptyStringCheckLength(lastName, 20, "성은 1글자 이상 20글자 내로 작성해주세요" ) &&
				notEmptyStringCheckLength(birthYear, 4, "연도는 1글자 이상 4글자 내로 작성해주세요" ) &&
				notEmptyStringCheckLength(birthMonth, 2, "월은 1글자 이상 2글자 내로 작성해주세요" ) &&
				notEmptyStringCheckLength(birthDay, 2, "일은 1글자 이상 2글자 내로 작성해주세요" ) &&
				stringCheckLength(email, 0, 30, "이메일 주소는 30글자 내로 작성해주세요" ) &&
				stringCheckLength(phoneNumber, 0, 25, "연락처는 25글자 내로 작성해주세요") &&
				stringCheckLength(countryId, 0, 2, "국가 ID는 2글자 내로 작성해주세요") &&
				stringCheckLength(postalCode, 0, 10, "우편번호는 10글자 내로 작성해주세요") &&
				stringCheckLength(stateProvince, 0, 10, "지역은 10글자 내로 작성해주세요") &&
				stringCheckLength(city, 0, 30, "시는 30글자 내로 작성해주세요") &&
				stringCheckLength(streetAddress, 0, 40, "주소는 25글자 내로 작성해주세요");				
		}
	</script>
</head>
<body>
	<div id="allcontent">
	
	<c:set var="cust" value="<%=customer%>" />
	
	<c:if test="${cust == null}">
		<script>alert('오류: 전달된 고객 정보가 없습니다.'); location.assign('/cust/prod_list.do');</script>
	</c:if>
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	
	<c:choose>
	
	<c:when test="${custLogin == true && custUserId == cust.customerId}">
	<form method="post" action="/cust/set_myinfo.do">
	<input type="hidden" name="cust_id" value="${cust.customerId}" />
	<table align="center" width="600px">
		<tr>
			<td colspan="6" style="text-align: center">고객 정보</td>
		</tr>
			
		<tr>
			<td width="20%" class="label">성</td>
			<td width="60%">
				<input type="text" id="last_name" name="last_name" size="30" value="${cust.customerLastName}" />
			</td>
			<td width="20%"></td>
		</tr>
		
		<tr>
			<td class="label">이름</td>
			<td>
				<input type="text" id="first_name" name="first_name" size="30" value="${cust.customerFirstName}" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">연락처</td>
			<td>
				<input type="text" id="phone_number" name="phone_number" size="30" value="${cust.phoneNumber}" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">이메일</td>
			<td>
				<input type="text" id="email" name="email" size="30" value="${cust.customerEmail}" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">생년월일</td>
			<td>
				<select id="birth_year" name="birth_year"
					onchange="setNumberOfDaysOfMonth('birth_day', getNumberOfDaysOfMonth('birth_year', 'birth_month'));">
					<% for(int i = curYear; i >= 1900 ; i--) { %>
					<option value="<%=i%>" <%if(i == year) {%>selected<%}%>>
						<%=i%>
					</option>
					<% } %>
				</select>
				
				<select id="birth_month" name="birth_month" 
					onchange="setNumberOfDaysOfMonth('birth_day', getNumberOfDaysOfMonth('birth_year', 'birth_month'));">
					<% for(int i = 1; i <= 12; i++) { %>
					<option value="<%=i%>" <%if(i == month) {%>selected<%}%>>
						<%=i%>
					</option>
					<% } %>
				</select>
				
				<select id="birth_day" name="birth_date" >
					<% for(int i = 1; i <= 31; i++) { %>
					<option value="<%=i%>" <%if(i == day) {%>selected<%}%>>
						<%=i%>
					</option>
					<% } %>
				</select>
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">성별</td>
			<td>
				여 <input type="radio" id="genderF" name="gender" value="F" <c:if test="${cust.gender == 'F'}">checked</c:if> />
				남 <input type="radio" id="genderM" name="gender" value="M" <c:if test="${cust.gender == 'M'}">checked</c:if> />
			</td>
			<td></td>
		</tr>
		
	</table>
	
	<br /><br />
	
	<table  align="center" width="600px">
		<tr>
			<td colspan="4" style="text-align: center">주소</td>
		</tr>
		
		
		<tr>
			<td width="20%" class="label">국가</td>
			<td width="30%">
				<input type="text" id="country_id" name="country_id" size="10" placeholder="ex:한국=KO" value="${cust.countryId}" />
			</td>
			<td width="20%" class="label">우편번호</td>
			<td width="30%">
				<input type="text" id="postal_code" name="postal_code" size="10" value="${cust.postalCode}" />
			</td>
		</tr>
		
		<tr>
			<td class="label">도/군/주(州)</td>
			<td><input type="text" id="state_province" name="state_province" size="10" value="${cust.stateProvince}" /></td>
			<td class="label">시</td>
			<td><input type="text" id="city" name="city" size="10" value="${cust.city}" /></td>
		</tr>
		
		<tr>
			<td colspan="4" class="label">상세주소</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<input type="text" id="street_address" name="street_address" size="80" value="${cust.streetAddress}" />
			</td>
		</tr>
		
		<tr>
			<td colspan="4" class="label">
				<input type="submit" value="수정" onclick="return checkSubmit();" />
				<input type="button" value="취소" onclick="history.go(-1);"/>
			</td>
		</tr>
	</table>
	
	
	</form>
		
	</c:when>
	
	<c:otherwise>
		<script>alert('오류: 잘못된 접근입니다.'); location.assign('/cust/prod_list.do');</script>
	</c:otherwise>
	
	</c:choose>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
	
</body>
</html>