<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	
	Calendar cal = new GregorianCalendar();
	int curYear = cal.get(Calendar.YEAR);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>회원 가입</title>
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
			var userName = $('#user_name').attr('value');
			var passwd1 = $('#passwd1').attr('value');
			var passwd2 = $('#passwd2').attr('value');
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
			
			return stringCheckLength(userName, 4, 20, "사용자 id는 5글자 이상 20글자 내로 작성해주세요") &&
				stringCheckLength(passwd1, 4, 50, "비밀번호는 4글자 이상 50글자 내로 작성해주세요") &&
				isEqual(passwd1, passwd2, "입력하신 비밀번호가 서로 다릅니다") &&
				stringCheckLength(firstName, 0, 20, "이름은 20글자 내로 작성해주세요") &&
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
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
	
	
	<div id="cust_toolbar">
		<%@ include file="/cust/forms/cust_toolbar.jsp" %>
	</div>
	
		
	<form method="post" action="/cust/signup.do">
	
	<table align="center" width="600px">
		<tr>
			<td colspan="6" style="text-align: center">고객 정보</td>
		</tr>
			
		<tr>
			<td width="20%" class="label">아이디</td>
			<td width="60%">
				<input type="text" id="user_name" name="user_name" size="30" />
			</td>
			<td width="20%"></td>
		</tr>
		
		<tr>
			<td class="label">비밀번호 1</td>
			<td>
				<input type="password" id="passwd1" name="passwd1" size="30" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">비밀번호 2</td>
			<td>
				<input type="password" id="passwd2" name="passwd2" size="30" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">성</td>
			<td>
				<input type="text" id="last_name" name="last_name" size="30" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">이름</td>
			<td>
				<input type="text" id="first_name" name="first_name" size="30" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">연락처</td>
			<td>
				<input type="text" id="phone_number" name="phone_number" size="30" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">이메일</td>
			<td>
				<input type="text" id="email" name="email" size="30" />
			</td>
			<td></td>
		</tr>
		
		<tr>
			<td class="label">생년월일</td>
			<td>
				<select id="birth_year" name="birth_year"
					onchange="setNumberOfDaysOfMonth('birth_day', getNumberOfDaysOfMonth('birth_year', 'birth_month'));">
					<% for(int i = curYear; i >= 1900 ; i--) { %>
					<option value="<%=i%>" <%if(i == curYear) {%>selected<%}%>>
						<%=i%>
					</option>
					<% } %>
				</select>
				
				<select id="birth_month" name="birth_month" 
					onchange="setNumberOfDaysOfMonth('birth_day', getNumberOfDaysOfMonth('birth_year', 'birth_month'));">
					<% for(int i = 1; i <= 12; i++) { %>
					<option value="<%=i%>" <%if(i == 1) {%>selected<%}%>>
						<%=i%>
					</option>
					<% } %>
				</select>
				
				<select id="birth_day" name="birth_date" >
					<% for(int i = 1; i <= 31; i++) { %>
					<option value="<%=i%>" <%if(i == 1) {%>selected<%}%>>
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
				여 <input type="radio" id="genderF" name="gender" value="F" checked />
				남 <input type="radio" id="genderM" name="gender" value="M" />
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
				<input type="text" id="country_id" name="country_id" size="10" placeholder="ex:한국=KO" />
			</td>
			<td width="20%" class="label">우편번호</td>
			<td width="30%">
				<input type="text" id="postal_code" name="postal_code" size="10" />
			</td>
		</tr>
		
		<tr>
			<td class="label">도/군/주(州)</td>
			<td><input type="text" id="state_province" name="state_province" size="10" /></td>
			<td class="label">시</td>
			<td><input type="text" id="city" name="city" size="10" /></td>
		</tr>
		
		<tr>
			<td colspan="4" class="label">상세주소</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<input type="text" id="street_address" name="street_address" size="80" />
			</td>
		</tr>
		
		<tr>
			<td colspan="4" class="label">
				<input type="submit" value="회원가입" onclick="return checkSubmit();" />
				<input type="button" value="취소" onclick="history.go(-1);"/>
			</td>
		</tr>
	</table>
	
	
	</form>
	
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
	
</body>
</html>