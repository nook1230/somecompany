package com.somecompany.service.hr;

import java.util.List;

import com.somecompany.model.hr.Employee;
import com.somecompany.model.hr.ProfilePicture;
import com.somecompany.repository.dao.hr.EmployeeDao;
import com.somecompany.utils.ListHelper;
import com.somecompany.utils.Validation;

/**********************************************
 * EmployeeService
 * EmployeeDao를 이용해 요청된 정보를 처리하는 서비스 클래스
 * 
 * 싱글턴 패턴
 * 주의: employeePerPage는 다른 클래스에 의해 변경될 수 있음
***********************************************/

public class EmployeeService {
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 필드
	
	// 페이지 당 보여지는 사원 정보의 수(리스트)
	private static int employeePerPage = 10;
	
	// DAO 객체
	private static EmployeeDao employeeDao;		
	
	// 상수 필드: 필터링 및 검색 분류
	public static final int  SEARCH_ALL = 0;			// 모두 조회
	public static final int  SEARCH_DEPARTMENT = 1;		// 부서별 조회
	public static final int  SEARCH_JOB = 2;			// 직위별 조회
	public static final int  SEARCH_NAME = 3;			// 이름 검색
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 생성자 및 인스턴스
	
	private static EmployeeService employeeService = new EmployeeService();
	
	private EmployeeService() { }
	
	public static EmployeeService getInstance(String resource) {
		employeeDao = EmployeeDao.getInstance(resource);
		return employeeService;
	}
	
	public static EmployeeService getInstance(String resource, int perPage) {
		employeeDao = EmployeeDao.getInstance(resource);
		employeePerPage = perPage;
		
		return employeeService;
	}
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 서비스 메소드
	
	/* getItemDetail: id에 해당하는 사원의 상세 정보 */
	public Employee get(int employeeId) {
		return (Employee) employeeDao.get(employeeId);
	}
	
	
	/* getItemList: 필터링/검색 조건에 따른 사원 리스트 조회 */
	public ListHelper<Employee> getList(int page, int searchby, String keyword) {
		// 총 사원 수 계산(조건에 맞게)
		int totalCount = getEmployeeCount(searchby, keyword);
		
		// ListHelper 생성 및 설정(컨트롤러에서 ListHelper를 결과로 받아 속성에 바인딩한다)
		ListHelper<Employee> listHelper = new ListHelper<Employee>(
				totalCount, page, employeePerPage);
		
		// 리스트 가져오기
		List<Employee> employees = getEmployeeList(searchby, keyword, listHelper.getOffset());
		
		listHelper.setList(employees);	// 리스트 설정
		
		return listHelper;
	}
	
	
	/* addNewItem: 새로운 사원 정보 추가 */
	public boolean add(Employee employee) {
		int result = 0;
		
		// 파라미터 필터링
		if(checkParameter(employee))
			result = employeeDao.add(employee);
		
		if(result == 1)
			return true;
		
		return false;
	}
	
	/* deleteItem: 사원 정보 삭제 */
	public boolean deleteItem(int employeeId, String target) {
		int maxId = employeeDao.getMaxId();
		int minId = employeeDao.getMinId();
		int result = 0;
		
		// 파라미터 필터링(범위 안의 파라미터만 받는다)
		if(Validation.checkIntegerSize(employeeId, minId, maxId))
			result = employeeDao.delete(employeeId, target);
		
		if(result == 1)
			return true;
		
		return false;
	}
	
	/* updateItem: 사원 정보 수정(프로필 사진까지 포함) */
	public boolean updateItem(Employee employee, String fileName,  
			String targetPath, String tempPath) throws Exception {
		
		int result = 0;
		ProfilePicture newPicture = new ProfilePicture();
		
		// 사진 정보 설정
		newPicture.setEmployeeId(employee.getEmployeeId());
		newPicture.setPictureFileName(fileName);
		
		if(checkParameter(employee)) {
			result = employeeDao.update(employee, newPicture, targetPath, tempPath);
		}
		
		if(result == 1)
			return true;
		
		return false;
	}
	
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 주로 내부적으로 사용되는 메소드
	
	/* getEmployeeCount: 조건에 맞게 사원 수를 반환 */
	public int getEmployeeCount(int searchby, String keyword) {
		int totalCount = 0;
		switch(searchby) {
		case SEARCH_DEPARTMENT: 
			totalCount = employeeDao.getCountByDepartmentId(Integer.parseInt(keyword));
			break;
		case SEARCH_JOB:
			totalCount = employeeDao.getCountByJobId(keyword);
			break;
		case SEARCH_NAME:
			totalCount = employeeDao.getCountByName(keyword);
			break;
		case SEARCH_ALL: default:
			totalCount = employeeDao.getCount();
			break;
		}
		
		return totalCount;
	}
	
	
	/* getEmployeeList: 조건에 맞게 사원 정보가 담긴 리스트를 반환  */
	public List<Employee> getEmployeeList(int searchby, String keyword, int offset) {
		List<Employee> employees = null;
		
		switch(searchby) {
		case SEARCH_DEPARTMENT: 
			employees = employeeDao.getListByDepartmentId(Integer.parseInt(keyword), offset, employeePerPage);
			break;
		case SEARCH_JOB:
			employees = employeeDao.getListByJobId(keyword, offset, employeePerPage);
			break;
		case SEARCH_NAME:
			employees = employeeDao.getListByName(keyword, offset, employeePerPage);
			break;
		case SEARCH_ALL: default:
			employees = employeeDao.getList(offset, employeePerPage);
			break;
		}
		
		return employees;
	}
	
	
	/* getItemMaxId/getItemMinId: 최대/최소 id 값 조회  */
	public int getMaxId() {
		return employeeDao.getMaxId();
	}
	
	public int getMinId() {
		return employeeDao.getMinId();
	}
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 필터링 함수
	
	/* add와 update에 사용될 employee 객체에 대한 검증 */
	public boolean checkParameter(Employee employee) {
		if(Validation.notNullObject(employee)) {
			
			/****************** not null 칼럼 ******************/
			// 조건에 맞지 않으면 false를 리턴하며 종료
			
			// last_name: 25byte
			if(!Validation.notNullStringLength(employee.getLastName(),25))
				return false;
			
			// email: 25byte
			if(!Validation.notNullStringLength(employee.getEmail(), 25))
				return false;
			
			// hire_date: java.sql.Date
			if(!Validation.notNullObject(employee.getHireDate()) ||
				!Validation.isThisObjectType(employee.getHireDate(), "java.sql.Date"))
				return false;
			
			// job_id: 10byte
			if(!Validation.notNullStringLength(employee.getJobId(), 10))
				return false;
			
			/****************** null 칼럼 ******************/
			
			// manager_id: null 허용 칼럼이지만 제약조건(id가 employees에 존재해야 함)
			int minManagerId = employeeDao.getMinId();	// managerId 최소값
			int maxManagerId = employeeDao.getMaxId();	// managerId 최대값
			if(!Validation.checkIntegerSize(employee.getManagerId(), minManagerId, maxManagerId)) {
				// 범위를 벗어나면 최소값으로 맞추고
				int checkedManagerId = (int) 
						Validation.ProcrustesBedHead(employee.getManagerId(), minManagerId);
				// employee의 managerId를 다시 설정한다
				employee.setManagerId(checkedManagerId);
			}
			
			// first_name: 25byte
			if(!Validation.checkStringLength(employee.getFirstName(), 0, 25)) {
				// 25byte를 넘으면 빈문자열로 만든다
				employee.setFirstName("");
			}
			
			// salary는 dao에서 직접 필터링
			
			// department_id: 제약은 없지만, 적당한 부서 id가 들어가도록 해준다
			int minDepartmentId = employeeDao.getMinDepartmentId();	// departmentId 최소값
			int maxDepartmentId = employeeDao.getMaxDepartmentId();	// departmentId 최대값
			if(!Validation.checkIntegerSize(employee.getDepartmentId(), minDepartmentId, maxDepartmentId)) {
				// 범위를 벗어나면 적당히 수정하고
				int checkedDepartmentId = (int) Validation.ProcrustesBed(
								employee.getDepartmentId(), minDepartmentId, maxDepartmentId);
				// employee의 departmentId를 다시 설정한다.
				// department_id는 10의 배수로 설정되어 있으므로, 10의 배수로 적당히 조정해준다.
				employee.setDepartmentId((checkedDepartmentId / 10) * 10);
			}
			
			// password: 50byte
			if(!Validation.checkStringLength(employee.getPassword(), 0, 50)) {
				// 25byte를 넘으면 빈문자열로 만든다
				employee.setPassword("");
			}
			
			return true;	// 여기까지 모두 완료되었으면 검증 통과
		}
		
		return false;	// null 객체: false
	}
}
