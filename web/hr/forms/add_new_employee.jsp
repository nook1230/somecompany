<%@page import="com.somecompany.service.hr.SecurityService"%>
<%@page import="java.util.Map"%>
<%@page import="com.somecompany.service.CommonService"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	// 부서와 직무 정보를 얻어온다
	Map<Integer, String> departmentMap = CommonService.getDepartmentMap();
	Map<String, String> jobMap = CommonService.getJobMap();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>사원 정보 추가</title>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<style>
		table {
			border-collapse: collapse;
		}
		
		tr {
			border-top: 1px solid gray;
			border-bottom: 1px solid gray;
		}
		
		td {
			padding: 5px;
			text-align: left;
		}
		
		.stress {
			color: red;
		}
	</style>
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/js/js_validation.js"></script>
	<script>
		$(document).ready(function() {
			$('#btn_search_manager').click(function(event) {
				event.preventDefault();
				event.stopPropagation();
				open('/human_res/search_list.do', 'search',
						'width=600, height=400, top=300, left=400, location=no');
			});
		});
	
		function checkSubmit() {
			// 입력 검증
			var firstName = $('#first_name').get(0).value;
			var lastName = $('#last_name').get(0).value;
			var jobId = $('#job_id').get(0).value;
			var departmentId = $('#department_id').get(0).value;
			var email = $('#email').get(0).value;
			var phoneNumber = $('#phone_number').get(0).value;
			var hireDate = $('#hire_date').get(0).value;
			var managerId = $('#manager_id').get(0).value;
			
			var regex = /^\d{4}-\d{1,2}-\d{1,2}$/;
			
			return stringCheckLength(firstName, 0, 20, "이름은 20글자 내로 작성해주세요") &&
				notEmptyStringCheckLength(lastName, 25, "성은 1글자 이상 25글자 내로 작성해주세요" ) &&
				notEmptyStringCheckLength(jobId, 25, "job_id는 1글자 이상 10글자 내로 작성해주세요" ) &&
				isEffectiveOrEmptyNumber(departmentId, "숫자 형식의 부서번호를 입력해주세요.") &&
				notEmptyStringCheckLength(email, 25, "이메일 주소는 1글자 이상 25글자 내로 작성해주세요" ) &&
				stringCheckLength(phoneNumber, 0, 20, "연락처는 20글자 내로 작성해주세요") &&
				regexTest(hireDate, regex, "입사일은 xxxx-xx-xx의 형식으로 입력해주세요") &&
				isEffectiveNumber(managerId, "숫자 형식의 매니저 번호를 입력해주세요.");
		}
	</script>
</head>
<body>
	<div id="allcontent">
	
	<div id="header">
		<%@ include file="/forms/header.jsp" %>
	</div>
	
	<div id="main">
		
		<c:set var="login" value="<%=login%>" />
		<c:set var="authority" value="<%=authority%>" />
		
		<h3>사원 정보 추가</h3>
		
		<form action="/human_res/add.do" method="post">

			<table align="center" width="600px">
				<c:choose>
				<c:when test="${login == true && (authority == 'ALL' || authority == 'HR')}">
				<!-- 사원 정보의 추가는 권한 있는 사용자만이 가능하다 -->
				
				<tr>
					<td colspan="3" style="text-align: right;">
						<span class="stress">*</span>&nbsp;필수항목
					</td>
				</tr>
				
				<tr>
					<td width="20%">이름</td>
					<td width="60%"><input type="text" id="first_name" name="first_name" size="35" /></td>
					<td width="20"></td>
				</tr>
				
				<tr>
					<td>성<span class="stress">*</span></td>
					<td><input type="text" id="last_name" name="last_name" size="35" /></td>
					<td></td>
				</tr>

				<tr>
					<td>직책<span class="stress">*</span></td>
					<td>
						<select id="job_id" name="job_id">
							<%
							for(Map.Entry<String, String> entry: jobMap.entrySet()) { %>
							<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
							<% } %>
						</select>
					</td>
					<td></td>
				</tr>

				<tr>
					<td>부서</td>
					<td>
						<select id="department_id" name="department_id">
							<%
							for(Map.Entry<Integer, String> entry: departmentMap.entrySet()) { %>
							<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
							<% } %>
						</select>
					</td>
					<td></td>
				</tr>

				<tr>
					<td>이메일<span class="stress">*</span></td>
					<td><input type="text" id="email" name="email" size="35" /></td>
					<td></td>
				</tr>

				<tr>
					<td>전화번호</td>
					<td><input type="text" id="phone_number" name="phone_number" size="35" /></td>
					<td></td>
				</tr>

				<tr>
					<td>입사일<span class="stress">*</span></td>
					<td><input type="text" id="hire_date" name="hire_date" size="35" /></td>
					<td>예시: 2014-4-2</td>
				</tr>

				<tr>
					<td>봉급</td>
					<td>&#36;<input type="text" name="salary" size="34" /></td>
					<td></td>
				</tr>
				
				<tr>
					<td>커미션</td>
					<td><input type="text" name="commission_pct" size="35" /></td>
					<td></td>
				</tr>

				<tr>
					<td>매니저 사번<span class="stress">*</span></td>
					<td>
						<input type="text" id="search_id" name="manager_id" size="35" readonly />&nbsp;
						<button id="btn_search_manager">
							검색
						</button>
					</td>
					<td></td>
				</tr>

				<tr>
					<td colspan="3" style="text-align: center;">
						<input type="submit" name="submit" value="추가" onclick="return checkSubmit();" />&nbsp; 
						<input type="button" value="취소" onclick="history.go(-1);" />
					</td>
				</tr>
				
				<!-- 근무지 정보는 부서 정보에 따라 결정되므로 수정 사항에 포함시킬 필요가 없다 -->
				</c:when>
				
				<c:otherwise>
				<tr>
					<td colspan="3" style="text-align: center;">접근 권한이 없습니다.</td>
				</tr>
				</c:otherwise>
				
				</c:choose>
			</table>
		</form>
	</div>
	
	<div id="footer">
		<%@ include file="/forms/footer.jsp" %>
	</div>
	
	</div>
</body>
</html>