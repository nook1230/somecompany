<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>로그인</title>
	<style>
		body {
			font-family: "Malgun Gothic", "굴림", "Gulim", "Arial";
			font-size: 10pt;
		}
	
		td {
			text-align: letf;
		}
		
		#main {
			margin-top: 150px;
			width: 250px;
			height: 150px;
			text-align: center;
			margin-left: auto;
			margin-right: auto;
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
	
	<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/js/js_validation.js"></script>
	<script>		
		function checkSubmit() {
			var id = $('#user_id').get(0).value;
			var passwd = $('#passwd').get(0).value;
			
			return isEffectiveNumber(id, "숫자 형식의 id를 입력해주세요.") 
					&& isNotEmptyString(passwd, "비밀번호를 입력해주세요.");
		}
	</script>
</head>
<body>

	<div id="main">
	
		<h3>Login</h3>
		<hr />
		
		<form method="post" action="/human_res/login.do">
		
		<table align="center">
			<tr>
				<td><label for="user_id">아이디</label></td>
				<td><input type="text" id="user_id" name="user_id" /></td>
			</tr>
			
			<tr>
				<td><label for="passwd">비밀번호</label></td>
				<td><input type="password" id="passwd" name="passwd" /></td>
			</tr>
			
			<tr>
				<td colspan="2" style="text-align: right;">
					<input type="submit" name="submit" value="Login" onclick="return checkSubmit();" />
				</td>
			</tr>
		</table>
		
		</form>
				
		<div style="margin-top: 20px;">
			<a href="/index.jsp">메인으로</a>
		</div>
		
	</div>
</body>
</html>