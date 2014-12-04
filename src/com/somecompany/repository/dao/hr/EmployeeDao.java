package com.somecompany.repository.dao.hr;

import java.io.File;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import com.somecompany.model.hr.Employee;
import com.somecompany.model.hr.JobHistory;
import com.somecompany.model.hr.ProfilePicture;
import com.somecompany.model.hr.ProfilePictureManager;
import com.somecompany.model.hr.SalaryMinMax;
import com.somecompany.model.hr.Workplace;
import com.somecompany.repository.dao.SessionGetter;

/**************************************
 * EmployeeDao
 * hr.employees 테이블을 관리하는 DAO 클래스
 * MyBatis 3.2.4 적용
 * 
 * singleton pattern
***************************************/

public class EmployeeDao {
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 일반 프로퍼티
	private static SessionGetter sessionGetter = new SessionGetter();
	
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 생성자 & getInstance
	private static EmployeeDao employeeDao = new EmployeeDao();
	
	private EmployeeDao() { }

	public static EmployeeDao getInstance(String resource) {
		sessionGetter.setSqlSessionFactory(resource);
		return employeeDao;
	}

	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// 상수 필드
	
	// MyBatis xml mapper 네임스페이스
	private final String NAMESPACE = 
			"com.somecompany.repository.mapper.EmployeeMapper";
	
	private final String PROFILE_PICTURE_NAMESPACE = 
			"com.somecompany.repository.mapper.ProfilePictureMapper";
	
	private static final int  SEARCH_ALL = 0;
	private static final int  SEARCH_DEPARTMENT = 1;
	private static final int  SEARCH_JOB = 2;
	private static final int  SEARCH_NAME = 3;
	
	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// 쿼리 메소드
	
	/**************************************************************
	 * 조회 쿼리(Read)
	/**************************************************************/
	
	/* getCount: 전체 데이터 레코드 수 조회 */
	public int getCount() {	
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		try {
			String sql = NAMESPACE + ".selectCount" + SEARCH_ALL;	
			return (int) sqlSession.selectOne(sql);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getCount: 데이터 레코드 수 조회(부서별) */
	public int getCountByDepartmentId(int departmentId) {	
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		try {
			String sql = NAMESPACE + ".selectCount" + SEARCH_DEPARTMENT;	
			return (int) sqlSession.selectOne(sql, departmentId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getCount: 데이터 레코드 수 조회(직위별) */
	public int getCountByJobId(String JobId) {	
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		try {
			String sql = NAMESPACE + ".selectCount" + SEARCH_JOB;	
			return (int) sqlSession.selectOne(sql, JobId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getCount: 데이터 레코드 수 조회(이름 검색) */
	public int getCountByName(String name) {	
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		try {
			String sql = NAMESPACE + ".selectCount" + SEARCH_NAME;	
			return (int) sqlSession.selectOne(sql, name);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getBasic: employeeId에 해당하는 레코드 조회 */
	public Employee getBasic(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectBasic";
			return (Employee) sqlSession.selectOne(sql, employeeId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getEmployeeName: 근로자 이름 조회 */
	public String getEmployeeName(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectEmployeeName";	
			return (String) sqlSession.selectOne(sql, employeeId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getWorkplace: 근로자의 근무지 조회 */
	public Workplace getWorkplace(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectEmployeeWorkspace";	
			return (Workplace) sqlSession.selectOne(sql, employeeId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getWorkplace: 근로자의 근무지 조회 */
	public List<JobHistory> getJobHistory(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectEmployeeJobHistory";	
			return sqlSession.selectList(sql, employeeId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* get: employees 외의 다른 테이블까지 이용해 
	 * 근로자에 대한 모든 정보를 조회한다 */
	public Employee get(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectBasic";
			Employee employee = sqlSession.selectOne(sql, employeeId);
			
			if(employee != null) {
				employee.setManagerName(getEmployeeName(employee.getManagerId()));
				employee.setWorkplace(getWorkplace(employeeId));
				employee.setJobHistory(getJobHistory(employeeId));
			}
			
			return employee;
			
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getList: 근로자에 대한 간략한 정보를 목록 형태로 조회한다 */
	public List<Employee> getList(int offset, int limit) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectList";
			RowBounds bounds = new RowBounds(offset, limit);
			return sqlSession.selectList(sql, null, bounds);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getList: 근로자에 대한 간략한 정보를 목록 형태로 조회한다(부서 필터링) */
	public List<Employee> getListByDepartmentId(int departmentId, int offset, int limit) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectListByDep";
			RowBounds bounds = new RowBounds(offset, limit);
			return sqlSession.selectList(sql, departmentId, bounds);
		} finally {
			sqlSession.close();
		}
	}
	
	/* getList: 근로자에 대한 간략한 정보를 목록 형태로 조회한다(직위 필터링) */
	public List<Employee> getListByJobId(String jobId, int offset, int limit) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectListByJob";
			RowBounds bounds = new RowBounds(offset, limit);
			return sqlSession.selectList(sql, jobId, bounds);
		} finally {
			sqlSession.close();
		}
	}
	
	/* getList: 근로자에 대한 간략한 정보를 목록 형태로 조회한다(이름 검색) */
	public List<Employee> getListByName(String name, int offset, int limit) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectListByName";
			
			RowBounds bounds = new RowBounds(offset, limit);
			return sqlSession.selectList(sql, name, bounds);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getMaxId: employee_id 중 최대값 구하기 
	 * 가장 최근에 등록된 근로자 정보를 조회하기 위해 */
	public int getMaxId() {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectMaxId";
			return sqlSession.selectOne(sql);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getMinId: employee_id 중 최소값 구하기 
	 * id 필터링을 위해 */
	public int getMinId() {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectMinId";
			return sqlSession.selectOne(sql);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/**************************************************************
	 * 갱신 쿼리: name space = EmployeeMapper
	/**************************************************************/
	
	/* add: 새로운 근로자 정보를 등록한다 */
	public int add(Employee employee) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".insert";
			
			// 연봉을 각 직책의 상한에 맞게 조절
			SalaryMinMax salaryMinMax = getSalaryMinMax(employee.getJobId());
			setSalary(employee, salaryMinMax); 
			
			int result = sqlSession.insert(sql, employee);
			
			if(result > 0) {
				sqlSession.commit();
			} else {
				sqlSession.rollback();
			}
			return result;
			
		} finally {
			sqlSession.close();
		}
	}
	
	/*********************************************************************/
	
	/* update: 기존 근로자 정보를 변경한다 */
	public int update(Employee employee, ProfilePicture newPicture, 
			String targetPath, String tempPath) throws Exception {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		try {
			int result = 0;
			
			if(updateEmployee(sqlSession, employee) == 1) {
					
				result = updateEmployeePicture(sqlSession, 
						employee.getEmployeeId(), newPicture, targetPath, tempPath);
					
				if(result == 1) {
					sqlSession.commit();
				} else {
					sqlSession.rollback();
				}
					
			} else {
				sqlSession.rollback();
			}
			
			return result;
		} finally {
			sqlSession.close();
		}
	}
	
	/* updateEmployee: employees의 사원 정보 수정 */
	private int updateEmployee(SqlSession sqlSession, Employee employee) {
		try {
			// 근로자의 기존 직책과 부서를 조회한다
			String sql = NAMESPACE;
			Employee empGet = getBasic(employee.getEmployeeId());
			
			if(empGet != null) {
				
				if(empGet.getJobId().equals(employee.getJobId()) &&
					empGet.getDepartmentId() == employee.getDepartmentId()) {
					// 직책이나 부서에 변화가 없는 경우
					sql += ".updateNoChange";
				} else {
					// 직책이나 부서에 변화가 있는 경우
					sql += ".update";
				}
			} else {
				return 0;
			}
			
			// 연봉을 각 직책의 상한에 맞게 조절
			SalaryMinMax salaryMinMax = getSalaryMinMax(employee.getJobId());
			setSalary(employee, salaryMinMax);
			
			return sqlSession.update(sql, employee);
		} finally {
			
		}
	}
	
	
	/* updateEmployeePicture: profile_pic의 사원 프로필 사진 정보 추가/삭제 */
	private int updateEmployeePicture(SqlSession sqlSession, int employeeId, 
						ProfilePicture newPicture, String targetPath, String tempPath) throws Exception {
		int result = 0;
		
		if(!newPicture.getPictureFileName().isEmpty()) {
			ProfilePicture oldPicture = getPicture(sqlSession, employeeId);
			
			File newPictureFile = new File(tempPath + "/" + newPicture.getPictureFileName());
			
			if(oldPicture == null) {
				result = insertPicture(sqlSession, newPicture);
				ProfilePictureManager.uploadNewProfilePictureFile(
						newPictureFile, employeeId, targetPath);
			} else {
				result = updatePicture(sqlSession, newPicture);
				ProfilePictureManager.updateProfilePictureFile(
					newPictureFile, employeeId, targetPath, oldPicture);
			}
		} else {
			result = 1;
		}
		
		return result;
	}
	
	
	/*********************************************************************/
	
	/* delete: 기존 근로자 정보를 삭제한다 */
	public int delete(int employeeId, String target) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sqlJHist = NAMESPACE + ".deleteJobHistory";
			String sqlEmp = NAMESPACE + ".delete";
			String sqlPic = PROFILE_PICTURE_NAMESPACE + ".delete";
			
			sqlSession.delete(sqlJHist, employeeId);	// 직무 이력 정보 삭제
			/* memo: 삭제되는 job_history 레코드는 0개 이상이다.
			 * 그러므로 결과 레코드 수를 체크하는 것이 무의미하다.
			 * 만일 이 과정에서 예외가 발생해 job_history가 적절히 삭제되지 못하면
			 * 사원 정보에 대한 삭제도 (무결성 제약 때문에) 불가능하다. */
			ProfilePicture profilePicture = getPicture(sqlSession, employeeId);
			
			if(sqlSession.delete(sqlPic, employeeId) == 1) {
				String filePath = target + "/" + employeeId + "_" + profilePicture.getPictureFileName();
				ProfilePictureManager.deleteProfilePictureFile(filePath);
			}
			
			int result = sqlSession.delete(sqlEmp, employeeId);
				
			if(result == 1) {
				// 1개의 레코드만 삭제되어야 정상이다. 커밋
				sqlSession.commit();
			} else {
				// 그 외의 경우는 롤백
				sqlSession.rollback();
			}
						
			return result;
			
		} finally {
			sqlSession.close();
		}
	}
		
	
	/**************************************************************
	 * helper methods
	/**************************************************************/
	
	/* 직책에 따른 최소 연봉과 최대 연봉을 구한다.
	 * 데이터 무결성을 위해서(직책에 따라 연봉 상한과 하한이 존재한다) */
	private SalaryMinMax getSalaryMinMax(String jobId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectSalaryMinMax";
			return (SalaryMinMax) sqlSession.selectOne(sql, jobId);
		} finally {
			sqlSession.close();
		}
	}
	
	/* 연봉을 각 직책의 상한에 맞게 조절해주는 메소드 */
	private void setSalary(Employee employee, SalaryMinMax salaryMinMax) {
		double salary = employee.getSalary();
		
		if(salary > salaryMinMax.getMaxSalary()) 
			employee.setSalary(salaryMinMax.getMaxSalary());
		else if(salary < salaryMinMax.getMinSalary())
			employee.setSalary(salaryMinMax.getMinSalary());
	}

	
	/* getMaxDepartmentId: 부서 id 최대값 */
	public int getMaxDepartmentId() {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
			
		try {
			String sql = NAMESPACE + ".selectDepMaxId";
			return sqlSession.selectOne(sql);
		} finally {
			sqlSession.close();
		}
	}
	
	/* getMinDepartmentId: 부서 id 최소값 */
	public int getMinDepartmentId() {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
			
		try {
			String sql = NAMESPACE + ".selectDepMinId";
			return sqlSession.selectOne(sql);
		} finally {
			sqlSession.close();
		}
	}
	
	
	
	////////////////////////////////////////////////////////////
	// 프로필 사진 관련 Dao //////////////////////////////////////////
	
	// getPicture
	public ProfilePicture getPicture(SqlSession sqlSession, int employeeId) {
		try {
			String sql = PROFILE_PICTURE_NAMESPACE + ".selectById";	
			
			return (ProfilePicture) sqlSession.selectOne(sql, employeeId);
		} finally {
			
		}
	}
	
	// insert
	public int insertPicture(SqlSession sqlSession, ProfilePicture profilePicture) {
		try {
			String sql = PROFILE_PICTURE_NAMESPACE + ".insert";	
			return sqlSession.insert(sql, profilePicture);	
		} finally {
			
		}
	}
	
	// update
	public int updatePicture(SqlSession sqlSession, ProfilePicture profilePicture) {
		try {
			String sql = PROFILE_PICTURE_NAMESPACE + ".update";	
			return sqlSession.update(sql, profilePicture);
			
		} finally {
			
		}
	}
		
	// delete
	public int deletePicture(SqlSession sqlSession, int employeeId) {
		try {
			String sql = PROFILE_PICTURE_NAMESPACE + ".delete";	
			return sqlSession.delete(sql, employeeId);
		} finally {
			
		}
	}
}
