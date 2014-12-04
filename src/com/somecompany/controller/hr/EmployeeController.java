package com.somecompany.controller.hr;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.somecompany.controller.Controller;
import com.somecompany.controller.Model;
import com.somecompany.model.hr.Authority;
import com.somecompany.model.hr.Employee;
import com.somecompany.service.hr.EmployeeService;
import com.somecompany.service.hr.SecurityService;
import com.somecompany.utils.ListHelper;
import com.somecompany.utils.Validation;

/********************************************
 * EmployeeController
 * /human_res로 시작되는 uri는 이 컨트롤러가 담당
*********************************************/

public class EmployeeController extends Controller {
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 서비스 객체
	EmployeeService service;
	
	public void setService(EmployeeService service) {
		this.service = service;
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 구현 메소드
	
	/********************* 매핑 함수들 ************************/
	// uri와 결과 바인딩 정보를 여기에서 매핑한다
	
	/* mapping */
	@Override
	public void mapping() {
		/*** GET ***/
		// requestAction ---> requestMethodName
		MAP_GET("list.do", "list");					// 리스트 조회
		MAP_GET("add.do", "addForm");				// 사원 정보 추가 폼
		MAP_GET("login.do", "login_form");			// 로그인 폼
		MAP_GET("logout.do", "logout");				// 로그아웃 처리
		MAP_GET("get.do", "get");					// 사원 상세 보기
		MAP_GET("update.do", "updateForm");			// 사원 정보 갱신 폼
		MAP_GET("delete.do", "deleteForm");			// 사원 정보 삭제 폼
		MAP_GET("search_list.do", "searchList");	// 매니저 id 찾기
		MAP_GET("admin_auth.do", "authorityList");	// 권한 테이블
		
		/*** POST ***/
		// requestAction ---> requestMethodName
		MAP_POST("add.do", "add");						// 사원 정보 추가 처리
		MAP_POST("login.do", "login");					// 로그인 처리	
		MAP_POST("list.do", "list_pre_search");			// 검색 조건 설정(리스트)
		MAP_POST("update.do", "update");				// 사원 정보 갱신 처리
		MAP_POST("delete.do", "delete");				// 사원 정보 삭제 처리
		MAP_POST("add_auth.do", "addAuthority");		// 권한 추가
		MAP_POST("set_auth.do", "setAuthority");		// 권한 설정
		MAP_POST("delete_auth.do", "deleteAuthority");	// 권한 삭제
		
		/*** Result Binding ***/
		/* 요청 액션(커맨드)/(바인딩 될) 속성 이름/(바인딩 될) 객체의 클래스 정보 */
		// /hr/list.jsp에서 사용될 리스트 헬퍼
		MAP_RESULT_BINDING("list.do", "list_helper", ListHelper.class);
		
		// /hr/employee_detail.jsp에서 사용될 employee
		MAP_RESULT_BINDING("get.do", "employee", Employee.class);
		
		// /hr/forms/update_employee_form.jsp에서 사용될 employee
		MAP_RESULT_BINDING("update.do", "employee", Employee.class);
		
		// /hr/forms/search_list.jsp에서 사용될 리스트 헬퍼
		MAP_RESULT_BINDING("search_list.do", "list_helper", ListHelper.class);
		
		// /hr/admin_authority.jsp에서 사용될 리스트 헬퍼
		MAP_RESULT_BINDING("admin_auth.do", "list_helper", ListHelper.class);
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 작업 처리 메소드
	
	/******************************************************************/
	
	/* login: GET */
	public String login_form(Map<String, String> paramsMap, Model model) {
		return "/hr/forms/login_form.jsp";	// 로그인 폼 페이지로 이동
	}
	
	/* login: Post */
	public String login(Map<String, String> paramsMap, Model model) {
		boolean loginSuccess = false;
		String viewPage = ":redirect/index.jsp";		// 디폴트 페이지
		String error_code = "invalid_user_id_or_password";		// 에러 코드(디폴트)
		
		if(paramsMap.get("user_id") != null && paramsMap.get("passwd") != null && 
				model.getAttribute("request") != null) {
			// 아이디와 비밀번호
			int userId = Integer.parseInt(paramsMap.get("user_id"));
			String password = paramsMap.get("passwd");
			
			// MainController가 전달해준 model 객체로부터 HttpServletRequest 객체를 받아온다
			// 로그인 시에 사용할 session 객체를 받아오기 위해
			HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
			
			// 로그인 처리
			loginSuccess = SecurityService.login(userId, password, req.getSession());
			
		} else {
			// 로그인 실패 에러 코드 설정
			if(paramsMap.get("user_id") == null || paramsMap.get("user_id").isEmpty())
				error_code = "login_no_user_id";
			else if(paramsMap.get("passwd") == null || paramsMap.get("passwd").isEmpty())
				error_code = "login_no_password";
		}
		
		if(loginSuccess)
			return viewPage;	// 로그인 성공시 메인 페이지로
		else					// 실패시 에러 페이지로
			return ":redirect/errors/login_error.jsp?error_code=" + error_code;
	}
	
	/******************************************************************/
	
	/* logout: GET */
	public String logout(Map<String, String> paramsMap, Model model) {
		boolean logoutSuccess = false;
		String viewPage = ":redirect/index.jsp";
		
		// HttpServletRequest: session 객체를 가져오기 위해
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		if(SecurityService.isLogin(req.getSession())) {
			// 로그아웃: 로그인 되어 있는 상태에서만 처리
			logoutSuccess = SecurityService.logout(req.getSession());
			
			if(!logoutSuccess) {
				// 실패시 에러페이지로 이동: 로그아웃 처리 실패
				viewPage = ":redirect/errors/login_error.jsp?error_code=logout_failure";
			}
			
		} else {
			// 실패시 에러페이지로 이동: 로그인 상태가 아님
			viewPage = ":redirect/errors/login_error.jsp?error_code=logout_no_login";
		}
		
		return viewPage;
	}
	
	/******************************************************************/
	
	/* get: 사원 상세 정보 제공 */
	public String get(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		int employeeId = getIntParam(paramsMap, "id", 0);
		
		// 뷰
		String view = "/hr/employee_detail.jsp?";
		
		// 파라미터 설정(포워드될 페이지)
		view += ("id=" + employeeId + "&page=" + page);
		
		// 작업 처리
		Employee employee = service.get(employeeId);
		
		// 결과 객체 바인딩
		model.addAttribute("employee", employee);
		
		return view;
	}
	
	/******************************************************************/
	
	/* list: 사원 정보 리스트 제공 */
	public String list(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		String searchby;
		int search;
		String keyword = "";
		
		String view = "/hr/list.jsp";
				
		if(paramsMap.containsKey("searchby")) {
			// 검색 조건
			searchby = paramsMap.get("searchby");
			
			if(paramsMap.containsKey("keyword")) {
				keyword = paramsMap.get("keyword");
			} else {
				searchby = "all";
			}
			
			switch(searchby) {
			case "dep": 
				search = EmployeeService.SEARCH_DEPARTMENT;
				break;
				
			case "job": 
				search = EmployeeService.SEARCH_JOB;
				break;
				
			case "name": 
				search = EmployeeService.SEARCH_NAME;
				break;
				
			case "all": default: 
				search = EmployeeService.SEARCH_ALL;
			}
			
			
		} else {
			search = EmployeeService.SEARCH_ALL;
		}
		
		ListHelper<Employee> listHelper = service.getList(page, search, keyword);
		
		// 결과 객체 바인딩
		model.addAttribute("list_helper", listHelper);
		
		return view;
	}
	
	/* searchList: 매니저 id 검색용 */
	public String searchList(Map<String, String> paramsMap, Model model) {
		String viewPage = list(paramsMap, model);
		viewPage = "/hr/forms/search_list.jsp";
		
		return viewPage;
	}
	
	/* list_pre_search: 리스트 검색 사전 작업, POST  */
	public String list_pre_search(Map<String, String> paramsMap, Model model) {
		String searchby;
		String keyword = "";

		String view = ":redirect/human_res/list.do?";
		
		if(paramsMap.containsKey("searchby")) {
			// 검색 조건
			searchby = paramsMap.get("searchby");
			
			view += ("searchby=" + searchby);
			
			// 조건에 따라 keyword를 설정한다
			switch(searchby) {
			case "dep": 
				keyword = paramsMap.get("department_keyword"); 
				break;
				
			case "job":
				keyword = paramsMap.get("job_keyword"); 
				break;
				
			case "name":
				keyword = paramsMap.get("name_keyword");  
				break;
				
			case "all": default: 
			}
			
		}
		
		view += ("&keyword=" + keyword);
		return view;
	}
	
	/******************************************************************/
	
	/* addForm: get로 들어오는 사원 추가 요청 처리 */
	// 폼 페이지 이름을 리턴한다
	public String addForm(Map<String, String> paramsMap, Model model) {
		return "/hr/forms/add_new_employee.jsp";
	}
	
	/* add: post로 들어오는 사원 추가 요청 처리 */
	public String add(Map<String, String> paramsMap, Model model) {
		// 전달받은 파라미터를 이용해 새 Employee 객체를 생성
		Employee employee = createEmployeeParam(paramsMap);
		
		String view = "";
		boolean result = false;
		
		// 추가 작업
		result = service.add(employee);
		
		// 결과에 따라 포워드
		if(result)
			view = ":redirect/human_res/list.do";
		else
			view = ":redirect/errors/hr_error.jsp?error_code=add_failure";
		
		return view;
	}
	
	/******************************************************************/
	
	/* updateForm: GET - update 폼 */
	public String updateForm(Map<String, String> paramsMap, Model model) {
		// form에서 Employee 객체를 필요로 하므로 전달된 id를 통해 
		// data를 가져와서 바인딩해준다
		
		Employee employee = null;
		
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		int employeeId = getIntParam(paramsMap, "id", 0);
		
		String viewPage = "/hr/forms/update_employee_form.jsp?";
		
		// id에 해당하는 사용자의 정보를 가져온다
		employee = service.get(employeeId);
		
		if(employee == null) {
			// 전달된 파라미터가 없으면 에러 페이지로 이동시킨다
			return "/errors/hr_error.jsp?error_code=update_no_id";
		}
		
		// 사용자 정보를 모델 객체에 바인딩한다
		model.addAttribute("employee", employee);
		
		// viewPage에 파라미터 설정
		viewPage += new StringBuilder().append(viewPage).append("id=")
				.append(employeeId).append("&page=").append(page).toString();
		
		return viewPage;
	}
	
	/* update: update 처리(POST) */
	public String update(Map<String, String> paramsMap, Model model) throws Exception {
		// 전달받은 파라미터를 이용해 새 Employee 객체를 생성
		Employee employee = createEmployeeParam(paramsMap);
		
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		int employeeId = getIntParam(paramsMap, "employee_id", 0);
		String fileName = getStringParam(paramsMap, "profile_pic", "");
				
		// HttpServletRequest: for MultiPartRequest
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		String tempPath = "c:\\webtemp";		
		String targetPath = req.getServletContext().getRealPath("/hr/profile_pic/");
				
		// employee 객체의 id를 설정한다
		if(employeeId == 0)
			return "/errors/hr_error.jsp?error_code=update_no_id";
		
		employee.setEmployeeId(employeeId);
		
		// 작업 변수들
		String view = "";
		boolean result = false;
		
		// 갱신 작업
		result = service.updateItem(employee, fileName, targetPath, tempPath);
		
		// 결과에 따라 포워드
		if(result)
			view = ":redirect/human_res/get.do?page=" + page + "&id=" + employeeId;
		else
			view = ":redirect/errors/hr_error.jsp?error_code=update_failure";
				
		return view;
	}
	
	/******************************************************************/
	
	/* deleteForm: GET - delete 폼 */
	public String deleteForm(Map<String, String> paramsMap, Model model) {
		return "/hr/forms/delete_employee_form.jsp";
	}
	
	/* delete: delete 처리(POST) */
	public String delete(Map<String, String> paramsMap, Model model) {
		boolean delSuccess = false;
		
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		int employeeId = getIntParam(paramsMap, "id", 0);
		
		String viewPage = ":redirect/human_res/list.do";
		
		// HttpServletRequest: for MultiPartRequest
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		String targetPath = req.getServletContext().getRealPath("/hr/profile_pic/");
		
		if(employeeId == 0) {
			// 전달된 파라미터가 없다면 에러페이지로 이동
			return ":redirect/errors/hr_error.jsp?error_code=delete_no_id";
		}
		
		// 포워드 페이지에 파라미터 설정(page): 페이지 유지 기능
		viewPage += ("?page=" + page);
		
		// 삭제 처리
		delSuccess = service.deleteItem(employeeId, targetPath);
		
		if(!delSuccess) {
			viewPage = ":redirect/errors/hr_error.jsp?error_code=delete_failure";
		} 
		
		return viewPage;
	}
	
	
	/******************************************************************/
	/* authorityList: 권한 테이블 조회(GET) */
	public String authorityList(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		
		String view = "/hr/admin_authority.jsp";
		
		ListHelper<Authority> listHelper = SecurityService.getList(page);
		
		// 결과 객체 바인딩
		model.addAttribute("list_helper", listHelper);
		
		return view;
	}
	
	
	/* addAuthority(POST) */
	public String addAuthority(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int employeeId = getIntParam(paramsMap, "employee_id", 0);
		short authorityId = getShortParam(paramsMap, "add_authority_id", (short) 0);
		
		if(employeeId == 0) {
			// 전달된 파라미터가 없다면 에러페이지로 이동
			return ":redirect/errors/hr_error.jsp?error_code=add_auth_no_emp_id";
		}
		
		if(authorityId == 0) {
			// 전달된 파라미터가 없다면 에러페이지로 이동
			return ":redirect/errors/hr_error.jsp?error_code=add_auth_no_auth_id";
		}
		
		boolean result = SecurityService.addAuthority(employeeId, authorityId);
		
		if(result)
			return ":redirect/human_res/admin_auth.do";
		else {
			return ":redirect/errors/hr_error.jsp?error_code=grant_auth_failure";
		}
	}
	
	
	/* setAuthority(POST) */
	public String setAuthority(Map<String, String> paramsMap, Model model) {
		short authorityId = getShortParam(paramsMap, "set_authority_id", (short) 0);
		
		if(authorityId == 0) {
			// 전달된 파라미터가 없다면 에러페이지로 이동
			return ":redirect/errors/hr_error.jsp?error_code=add_auth_no_auth_id";
		}
		
		// 권한이 변경될 사원들의 식별 번호
		List<Integer> employeeIds = new ArrayList<Integer>(); 
		
		for(Map.Entry<String, String> entry : paramsMap.entrySet()) {
			if(!entry.getKey().equals("set_authority_id") 
					&& !entry.getKey().equals("add_authority_id")
					&& !entry.getKey().equals("employee_id")) {
				employeeIds.add(Integer.parseInt(entry.getValue()));
			}
		}
		
		int count = 0;
		for(int id : employeeIds) {
			boolean result = SecurityService.setAuthority(id, authorityId);
			
			if(result)
				count++;
		}
		
		if(count != 0)
			return ":redirect/human_res/admin_auth.do";
		else {
			return ":redirect/errors/hr_error.jsp?error_code=grant_auth_failure";
		}
		
	}
	
	
	/* deleteAuthority(POST) */
	public String deleteAuthority(Map<String, String> paramsMap, Model model) {
		
		List<Integer> employeeIds = new ArrayList<Integer>(); 
		
		for(Map.Entry<String, String> entry : paramsMap.entrySet()) {
			if(!entry.getKey().equals("set_authority_id") 
					&& !entry.getKey().equals("add_authority_id")
					&& !entry.getKey().equals("employee_id")) {
				employeeIds.add(Integer.parseInt(entry.getValue()));
			}
		}
		
		int count = 0;
		for(int id : employeeIds) {
			boolean result = SecurityService.deleteAuthority(id);
			
			if(result)
				count++;
		}
		
		if(count != 0)
			return ":redirect/human_res/admin_auth.do";
		else
			return ":redirect/errors/hr_error.jsp?error_code=delete_auth_failure";
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private Employee createEmployeeParam(Map<String, String> paramsMap) {
		Employee employee = new Employee();
		
		// 문자열 파라미터: 부적절한 문자들을 걸러낸다(xss 공격 등을 대비하기 위해)
		String firstName = "";
		if(Validation.notEmptyString(paramsMap.get("first_name"))) {
			firstName = Validation.replaceHtmlCharacter(paramsMap.get("first_name").trim());
		}
		
		String lastName = "";
		if(Validation.notEmptyString(paramsMap.get("last_name"))) {
			lastName = Validation.replaceHtmlCharacter(paramsMap.get("last_name").trim());
		}
		
		String email = "";
		if(Validation.notEmptyString(paramsMap.get("email"))) {
			email = Validation.replaceHtmlCharacter(paramsMap.get("email").trim());
		}
		
		String phoneNumber = "";
		if(Validation.notEmptyString(paramsMap.get("phone_number"))) {
			phoneNumber = Validation.replaceHtmlCharacter(paramsMap.get("phone_number").trim());
		}
		
		String jobId = "";
		if(Validation.notEmptyString(paramsMap.get("job_id"))) {
			jobId = Validation.replaceHtmlCharacter(paramsMap.get("job_id").trim());
		}
		
		String password = "";
		if(Validation.notEmptyString(paramsMap.get("passwd"))) {
			password = Validation.replaceHtmlCharacter(paramsMap.get("passwd").trim());
		}
		
		String profileComment = "";
		if(Validation.notEmptyString(paramsMap.get("profile_comment"))) {
			profileComment = Validation.replaceHtmlCharacter(paramsMap.get("profile_comment").trim());
		}
		
		// 날짜형 파라미터
		Date hireDate = null;
		if(Validation.notEmptyString(paramsMap.get("hire_date"))) {
			hireDate = Validation.strToDate(
					Validation.replaceHtmlCharacter(paramsMap.get("hire_date").trim()));
		}
		
		// 숫자형 파라미터: 적절한 파라미터의 경우만 값을 받아온다(파싱 예외는 걸러내지 못한다)
		double salary = 0.0;
		if(Validation.notEmptyString(paramsMap.get("salary")))
			salary = Double.parseDouble(paramsMap.get("salary"));
		
		float commissionPct = 0.0f;
		if(Validation.notEmptyString(paramsMap.get("commission_pct")))
			commissionPct = Float.parseFloat(paramsMap.get("commission_pct"));
		
		int managerId = 0;
		if(Validation.notEmptyString(paramsMap.get("manager_id")))
			managerId = Integer.parseInt(paramsMap.get("manager_id"));
		
		int departmentId = 0;
		if(Validation.notEmptyString(paramsMap.get("department_id")))
			departmentId = Integer.parseInt(paramsMap.get("department_id"));
		
		// 객체의 프로퍼티 값 설정
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employee.setPhoneNumber(phoneNumber);
		employee.setJobId(jobId);
		employee.setHireDate(hireDate);
		employee.setSalary(salary);
		employee.setCommissionPct(commissionPct);
		employee.setManagerId(managerId);
		employee.setDepartmentId(departmentId);
		employee.setPassword(password);
		employee.setProfileComment(profileComment);
		
		return employee;
	}
}
