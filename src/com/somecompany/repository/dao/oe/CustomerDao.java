package com.somecompany.repository.dao.oe;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import com.somecompany.model.oe.Customer;
import com.somecompany.repository.dao.SessionGetter;

/**************************************
 * CustomerDao
 * oe.customers 테이블을 관리하는 DAO 클래스
 * MyBatis 3.2.4 적용
 * 
 * singleton pattern
***************************************/

public class CustomerDao {
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 일반 프로퍼티
	private static SessionGetter sessionGetter = new SessionGetter();
	
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 생성자 & getInstance
	private static CustomerDao customerDao = new CustomerDao();
	
	private CustomerDao() { }

	public static CustomerDao getInstance(String resource) {
		sessionGetter.setSqlSessionFactory(resource);
		return customerDao;
	}
	
	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// 상수 필드
	
	// MyBatis xml mapper 네임스페이스
	private final String NAMESPACE = 
			"com.somecompany.repository.mapper.CustomerMapper";
	
	public static final int SEARCH_BY_FNAME = 1;
	public static final int SEARCH_BY_LNAME = 2;
	public static final int SEARCH_BY_EMAIL = 3;
	public static final int SEARCH_BY_ALL = 4;
	
	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// 쿼리 메소드
	
	/**************************************************************
	 ********************** 조회 메소드(Read) *************************
	 **************************************************************/
	
	/* getCount: 전체 레코드 수 조회 */
	public int getCount() {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectCount";	
			return (int) sqlSession.selectOne(sql);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getCountForSearch: 전체 레코드 수 조회 */
	public int getCountForSearch(String sqlMapName, String keyword) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectCount" + sqlMapName;	
			return (int) sqlSession.selectOne(sql, keyword);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* get: id에 따른 고객 정보 조회 */
	public Customer get(int customerId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectCustomer";	
			return (Customer) sqlSession.selectOne(sql, customerId);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getList: 고객 정보 리스트 조회 */
	public List<Customer> getList(int offset, int limit) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectList";
			RowBounds bounds = new RowBounds(offset, limit);
			return sqlSession.selectList(sql, null, bounds);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getList: 고객 정보 리스트 조회(검색용) */
	public List<Customer> getList(int searchBy, String keyword, int offset, int limit) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE;
			
			// 검색 조건에 따라 쿼리를 선택한다
			switch(searchBy) {
			case SEARCH_BY_FNAME:	// 이름으로 조회
				sql += ".selectListByFName"; break;
			case SEARCH_BY_LNAME:	// 성(姓)으로 조회
				sql += ".selectListByLName"; break;
			case SEARCH_BY_EMAIL:	// 이메일 주소로 조회
				sql += ".selectListByEmail"; break;
			case SEARCH_BY_ALL: default:	// 위 세 가지 조건 모두로 검색(디폴트)
				sql += ".selectListByAll"; break;
			}
			
			RowBounds bounds = new RowBounds(offset, limit);
			return sqlSession.selectList(sql, keyword, bounds);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getMaxId: 가장 큰 customer_id 구하기 - 가장 최근에 추가된 고객 정보를 조회할 때 */
	public int getMaxId() {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectMaxId";
			return sqlSession.selectOne(sql);
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* isExistentUserName: 존재하는 사용자 이름인지 확인 */
	public boolean isExistentUserName(String userName) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".isExistentUserName";
			int count = sqlSession.selectOne(sql, userName);
			
			if(count > 0)
				return true;
			else 
				return false;
			
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* checkLogin: 사용자 이름과 패스워드 일치 여부 */
	public boolean checkLogin(String userName, String passwd) {
		Customer cust = new Customer();
		cust.setCustomerUserName(userName);
		cust.setCustomerPassword(passwd);
		
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".checkLogin";
			int count = sqlSession.selectOne(sql, cust);
			
			if(count == 1)
				return true;
			else 
				return false;
			
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* getCustomerId: 고객 사용자 이름을 이용해 고객 번호 가져오기 */
	public int getCustomerId(String userName) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".selectCustomerId";
			return sqlSession.selectOne(sql, userName);
			
		} finally {
			sqlSession.close();
		}
	}
	
	
	/**************************************************************
	 ************* 갱신 메소드(Create, Update, Delete) ****************
	 **************************************************************/
	
	/* add: 고객 정보 추가 */
	public int add(Customer customer) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".insertCustomer";
			
			int maxId = getMaxId();
			customer.setCustomerId(++maxId);
			
			int result = sqlSession.insert(sql, customer);
			
			if(result == 1) {
				// 추가된 레코드가 1개이면 커밋
				sqlSession.commit();
			} else {
				// 아니면 롤백
				sqlSession.rollback();
			}
			
			return result;
			
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* delete: 고객 정보 삭제 */
	public int delete(int customerId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".deleteCustomer";
			int result = sqlSession.insert(sql, customerId);
			
			if(result == 1) {
				// 삭제된 레코드가 1개이면 커밋
				sqlSession.commit();
			} else {
				// 아니면 롤백
				sqlSession.rollback();
			}
			
			return result;
			
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* update: 고객 정보 변경(기본 정보) */
	public int update(Customer customer) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".updateCustomerByCustomer";
			int result = sqlSession.update(sql, customer);
			
			if(result == 1) {
				// 변경된 레코드가 1개이면 커밋
				sqlSession.commit();
			} else {
				// 아니면 롤백
				sqlSession.rollback();
			}
			
			return result;
			
		} finally {
			sqlSession.close();
		}
	}
	
	/* changePasswd: 비밀번호 변경 */
	public int changePasswd(Customer customer) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
				
		try {
			String sql = NAMESPACE + ".changePasswd";
			int result = sqlSession.update(sql, customer);
			
			if(result == 1) {
				// 변경된 레코드가 1개이면 커밋
				sqlSession.commit();
			} else {
				// 아니면 롤백
				sqlSession.rollback();
			}
			
			return result;
			
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* update: 고객 정보 변경(부가 정보) */
	public int updateOptional(Customer customer) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".updateCustomerByAdmin";
			
			int result = sqlSession.update(sql, customer);
			
			if(result == 1) {
				// 변경된 레코드가 1개이면 커밋
				sqlSession.commit();
			} else {
				// 아니면 롤백
				sqlSession.rollback();
			}
			
			return result;
			
		}  finally {
			sqlSession.close();
		}
	}
}
