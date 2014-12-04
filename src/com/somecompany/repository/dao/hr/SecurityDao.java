package com.somecompany.repository.dao.hr;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import com.somecompany.model.hr.Authority;
import com.somecompany.model.hr.Employee;
import com.somecompany.repository.dao.SessionGetter;

/**************************************
 * SecurityDao
 * 로그인 정보 및 사원 권한 정보 관리 DAO
 * MyBatis 3.2.4 적용
 * 
 * singleton pattern
***************************************/

public class SecurityDao {
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 일반 프로퍼티
	private static SessionGetter sessionGetter = new SessionGetter();
	
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 생성자 & getInstance
	private static SecurityDao securityDao = new SecurityDao();
	
	private SecurityDao() { }

	public static SecurityDao getInstance(String resource) {
		sessionGetter.setSqlSessionFactory(resource);
		return securityDao;
	}
	
	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// 상수 필드

	// MyBatis xml mapper 네임스페이스
	private final String NAMESPACE = 
			"com.somecompany.repository.mapper.SecurityMapper";
	
	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// 쿼리 메소드

	/**************************************************************
	 * 조회 쿼리
	/**************************************************************/
	
	/* getCount */
	public int getCount() {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectCount";	
			return (int) sqlSession.selectOne(sql);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getCountById */
	public int getCountById(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectCountById";	
			return (int) sqlSession.selectOne(sql, employeeId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* get: 해당 사원의 권한 조회 */
	public String get(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectAuthority";	
			return sqlSession.selectOne(sql, employeeId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getList: 권한 목록 조회 */
	public List<Authority> getList(int offset, int limit) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectList";
			RowBounds bounds = new RowBounds(offset, limit);
			return sqlSession.selectList(sql, null, bounds);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getPassword: 해당 사원의 비밀번호 가져오기 */
	public String getPassword(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectPassword";
			return sqlSession.selectOne(sql, employeeId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* isValidPassword: 입력된 로그인가 유효한지 확인 */
	public boolean isValidPassword(int employeeId, String password) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		Employee employee = new Employee();
		try {
			String sql = NAMESPACE + ".selectLoginCheck";
			
			employee.setEmployeeId(employeeId);
			employee.setPassword(password);
			
			int count = sqlSession.selectOne(sql, employee);
			
			if(count == 1) {
				return true;
			}
			
			return false;
		} finally {
			sqlSession.close();
		}
	}
	
	
	/**************************************************************
	 * 갱신 쿼리
	/**************************************************************/
	
	/* setAuthority: 권한 정보 추가 */
	public int addAuthority(Authority authority) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		String sql = NAMESPACE + ".insertAuthority";
		
		try {
			int result = sqlSession.insert(sql, authority);
			
			if(result == 1) {
				sqlSession.commit();
			}
			else {
				sqlSession.rollback();
			}
			
			return result;
		} finally {
			sqlSession.close();
		}
	}
	
	
	
	/* setAuthority: 권한 정보 추가 및 갱신 */
	public int setAuthority(Authority authority) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		String sql = NAMESPACE + ".updateAuthority";
		
		try {
			int result = sqlSession.update(sql, authority);
			
			if(result == 1) {
				sqlSession.commit();
			}
			else {
				sqlSession.rollback();
			}
			
			return result;
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* delete: 권한 정보 삭제 */
	public int delete(int employeeId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".deleteAuthority";	
			int result = sqlSession.delete(sql, employeeId);
			
			if(result == 1) {
				sqlSession.commit();
			}
			else {
				sqlSession.rollback();
			}
			
			return result;
		} finally {
			sqlSession.close();
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
}
