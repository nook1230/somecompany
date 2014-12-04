package com.somecompany.service.oe;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.somecompany.model.oe.Customer;
import com.somecompany.repository.dao.oe.CustomerDao;
import com.somecompany.utils.ListHelper;
import com.somecompany.utils.Validation;

/**********************************************
 * CustomerService
 * CustomerDao를 이용해 요청된 정보를 처리하는 서비스 클래스
 * 
 * 싱글턴 패턴
 * 주의: employeePerPage는 다른 클래스에 의해 변경될 수 있음
***********************************************/

public class CustomerService {
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 필드

	// 페이지 당 보여지는 사원 정보의 수(리스트)
	private static int customerPerPage = 10;
	
	// DAO 객체
	private static CustomerDao customerDao;
	
	// 검색 조건
	public static final int SEARCH_BY_FNAME = 1;
	public static final int SEARCH_BY_LNAME = 2;
	public static final int SEARCH_BY_EMAIL = 3;
	public static final int SEARCH_BY_ALL = 4;
	
	// credit limit 최대값
	private static final int CREDIT_LIMIT_MAX = 5000;
	
	// 세션 속성 이름
	public static final String loginStatusAttr = "cust_login_status";
	public static final String loginUserIdAttr = "cust_login_user_id";
	public static final String loginUserNameAttr = "cust_login_user_name";
	
	// 세션 속성 값
	public static final String loginSuccess = "cust_login_success";
	public static final String loginFailed = "cust_login_failed";
	public static final String logoutStatus = "cust_logout_status";
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 생성자 및 인스턴스

	private static CustomerService customerService = new CustomerService();
	
	private CustomerService() { }
	
	public static CustomerService getInstance(String resource) {
		customerDao = CustomerDao.getInstance(resource);
		return customerService;
	}
	
	public static CustomerService getInstance(String resource, int perPage) {
		customerDao = CustomerDao.getInstance(resource);
		customerPerPage = perPage;
		
		return customerService;
	}
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 서비스 메소드
	
	/* get */
	public Customer get(int customerId) {
		return (Customer) customerDao.get(customerId);
	}

	
	/* getList */
	public ListHelper<Customer> getList(int page, int searchby, String keyword) {
		if(keyword.isEmpty())
			searchby = 0;
		
		// 총 주문 수 계산
		int totalCount = getCustomerCount(searchby, keyword);
		
		// ListHelper 생성 및 설정(컨트롤러에서 ListHelper를 결과로 받아 속성에 바인딩한다)
		ListHelper<Customer> listHelper = new ListHelper<Customer>(
				totalCount, page, customerPerPage);
		
		// 리스트 가져오기
		List<Customer> customers = getCustomerList(listHelper, searchby, keyword);
		
		listHelper.setList(customers);
		
		return listHelper;
	}
	
	
	/* add */
	public boolean add(Customer customer) {
		int result = 0;
		
		if(checkParameter(customer, true, false))
			result = customerDao.add(customer);
		
		if(result == 1)
			return true;
		
		return false;
	}
	
	
	/* delete */
	public boolean delete(int customerId) {
		int result = 0;
		
		result = customerDao.delete(customerId);
		
		if(result == 1)
			return true;
		
		return false;
	}
	
	
	/* update */
	public boolean update(Customer customer) {
		int result = 0;
		
		if(checkParameter(customer, false, false))
			result = customerDao.update(customer);
		
		if(result == 1)
			return true;
		
		return false;
	}
	
	
	/* changePasswd: 비밀번호 변경 */
	public boolean changePasswd(int userId, String oldPasswd, String newPasswd) {
		int result = 0;
		
		// 사용자 고객 정보를 가져온다
		Customer customer = customerDao.get(userId);
		
		if(customer != null) {
			// 입력한 기존 비밀번호가 정확한지
			boolean check = customerDao.checkLogin(customer.getCustomerUserName(), oldPasswd);
			
			if(check) {
				// 정확한 정보가 입력되었다면 update한다
				customer = new Customer();
				customer.setCustomerId(userId);
				customer.setCustomerPassword(newPasswd);
				
				result = customerDao.changePasswd(customer);
			}
		}
		
		if(result == 1)
			return true;
		
		return false;
	}
	
	
	/* updateOptional: 부가 칼럼들에 대한 update
	public boolean updateOptional(Object param) {
		int result = 0;
		
		// 파라미터 캐스팅
		Customer cust = castingObjectToCust(param);
		if(cust == null)
			return false;
		
		checkParameterOptional(cust);	// 파라미터 필터링
		result = customerDao.updateOptional(cust);
		
		if(result == 1)
			return true;
		
		return false;
	}
	 */
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////

	public int getMaxId() {
		return customerDao.getMaxId();
	}
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 주로 내부적으로 사용되는 메소드
	
	/* getCustomerCount: 주어진 조건에 따라 고객 정보의 수를 조회 */
	public int getCustomerCount(int searchby, String keyword) {
		int totalCount = 0;
		String sqlMapName = null;
		
		if(searchby == 0) {
			totalCount = customerDao.getCount();
		} else {
			switch(searchby) {
			case SEARCH_BY_FNAME:
				sqlMapName = "ByFName"; break;
			case SEARCH_BY_LNAME:
				sqlMapName = "ByLName"; break;
			case SEARCH_BY_EMAIL:
				sqlMapName = "ByEmail"; break;
			case SEARCH_BY_ALL: default:
				sqlMapName = "ByAll";
			}
			
			totalCount = customerDao.getCountForSearch(sqlMapName, keyword);
		}
		
		return totalCount;
	}
	
	
	/* getCustomerList: 주어진 조건에 따라 고객 정보의 리스트를 조회 */
	public List<Customer> getCustomerList(ListHelper<Customer> listHelper, int searchby, String keyword) {
		if(searchby == 0) {
			return customerDao.getList(listHelper.getOffset(), listHelper.getObjectPerPage());
		} else {
			return customerDao.getList(searchby, keyword, 
					listHelper.getOffset(), listHelper.getObjectPerPage());
		}
	}
	
	
	/* isExistentUserName: 중복된 아이디인지 체크(고객 가입시 체크용) */
	public boolean isExistentUserName(String userName) {
		return customerDao.isExistentUserName(userName);
	}
	
	
	/* isLogin: 로그인 상태인지 확인 */
	public static boolean isLogin(HttpSession session) {
		return session != null && session.getAttribute(loginStatusAttr) != null
				 && session.getAttribute(loginStatusAttr).equals(loginSuccess);
	}
	
	/* login: 로그인 */
	public boolean login(String userName, String passwd, HttpSession session) {
		boolean login = false;
		
		if(!isLogin(session) && session != null) {
			// 로그인 상태가 아니라면
			// 입력된 id와 패스워드가 유효한지 확인하고 
			login = customerDao.checkLogin(userName, passwd);
			
			if(login) {
				// 유효하다면 세션을 설정한다(로그인 성공)
				session.setAttribute(loginStatusAttr, loginSuccess);	// 로그인 상태를 성공으로
				
				int userId = customerDao.getCustomerId(userName);
				session.setAttribute(loginUserIdAttr, userId);			// 사용자 번호
				
				session.setAttribute(loginUserNameAttr, userName);		// 로그인 사용자 이름
			} else {
				// 로그인 실패
				session.setAttribute(loginStatusAttr, loginFailed);
			}
		}
			
		return login;
	}
	
	/* logout: 로그아웃 */
	public static boolean logout(HttpSession session) {
		if(isLogin(session)) {
			// 로그인 상태라면 세션 해제
			session.setAttribute(loginStatusAttr, logoutStatus);
			session.setAttribute(loginUserNameAttr, "");
			
			session.invalidate();	// 세션 해제
			
			return true;
		}
		
		return false;
	}
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 파라미터 필터링
	
	/* checkParameter: 파라미터 필터링 */
	public boolean checkParameter(Customer cust, boolean insert, boolean optional) {
		if(Validation.notNullObject(cust)) {
			
			if(cust == null)
				return false;
			
			////////////////////////// 필수 칼럼들 //////////////////////////
			
			// not null 칼럼(3개)
			// 1. customer_id: 체크 안 함(DAO에서 처리)
			
			if(insert) {
				// 2. customer_user_name(회원가입시 체크): 어플리케이션 수준에서 not null, 20byte
				if(!Validation.notNullStringLength(cust.getCustomerUserName(), 20)) {
					return false;
				}
				
				// cust_user_name 중복 체크
				if(customerDao.isExistentUserName(cust.getCustomerUserName())) {
					return false;
				}
				
				// 3. password: 어플리케이션 수준에서 not null, 50byte
				if(!Validation.notNullStringLength(cust.getCustomerPassword(), 50)) {
					return false;
				}
			}
			
			// 4. cust_first_name: 20byte
			if(!Validation.notNullStringLength(cust.getCustomerFirstName(), 20)) {
				return false;
			}
						
			// 5. cust_last_name: 20byte
			if(!Validation.notNullStringLength(cust.getCustomerLastName(), 20)) {
				return false;
			}
						
			// 6. date_of_birth: 어플리케이션 레벨에서 필수 칼럼
			if(!Validation.notNullObject(cust.getDateOfBirth())) {
				return false;
			}
			
			// null 허용 칼럼(3개)
			
			// 7. cust_address: 길이 초과시 모두 빈 문자열로
				// street_address
			if(!Validation.checkStringLength(cust.getStreetAddress(), 0, 40))
				cust.setStreetAddress("");
				// postal_code
			if(!Validation.checkStringLength(cust.getPostalCode(), 0, 10))
				cust.setPostalCode("");
				// city
			if(!Validation.checkStringLength(cust.getCity(), 0, 30))
				cust.setCity("");
				// state_province
			if(!Validation.checkStringLength(cust.getStateProvince(), 0, 10))
				cust.setStateProvince("");
				// country_id
			if(!Validation.checkStringLength(cust.getCountryId(), 0, 2))
				cust.setCountryId("");
			
			// 8. phone_number: 25byte
			if(!Validation.checkStringLength(cust.getPhoneNumber(), 0, 25))
				cust.setPhoneNumber("");
			
			// 9. gender: 1byte - default = F
			if(Validation.notNullString(cust.getGender())) {
				if(!Validation.checkStringLength(cust.getGender(), 0, 1))
					cust.setGender("F");
			} else {
				cust.setGender("F");
			}
						
			
			////////////////////////// 부가 칼럼들 //////////////////////////
			// update 시에는 조사하지 않는다
			
			if(optional) {
				checkParameterOptional(cust);
			}
			
			return true;
		}

		
		return false;
	}
	
	
	/* checkItemParameterOptional: 파라미터 필터링(부가 칼럼) */
	private void checkParameterOptional(Customer cust) {
		// nls_language: 3byte
		if(Validation.notNullString(cust.getNlsLanguage())) {
			if(!Validation.checkStringLength(cust.getNlsLanguage(), 0, 3))
				cust.setNlsLanguage("");
		} else {
			cust.setNlsLanguage("");
		}
		
		// nls_territory: 3byte
		if(Validation.notNullString(cust.getNlsTerritory())) {
			if(!Validation.checkStringLength(cust.getNlsTerritory(), 0, 3))
				cust.setNlsTerritory("");
		} else {
			cust.setNlsTerritory("");
		}
		
		// credit_limit: 0 이상 5000 이하
		if(cust.getCreditLimit() < 0)
			cust.setCreditLimit(0);
		else if(cust.getCreditLimit() > CREDIT_LIMIT_MAX)
			cust.setCreditLimit(CREDIT_LIMIT_MAX);
		
		// cust_email: 30byte
		if(Validation.notNullString(cust.getCustomerEmail())) {
			if(!Validation.checkStringLength(cust.getCustomerEmail(), 0, 3))
				cust.setCustomerEmail("");
		} else {
			cust.setCustomerEmail("");
		}
		
		// acount_mng_id
		cust.setAccountMgrId((int) 
				Validation.ProcrustesBed(cust.getAccountMgrId(), 0, Integer.MAX_VALUE));
		
		// date_of_birth: 체크 안 함
		
		// martial_status: 20byte
		if(Validation.notNullString(cust.getMaritalStatus())) {
			if(!Validation.checkStringLength(cust.getMaritalStatus(), 0, 20))
				cust.setMaritalStatus("");
		} else {
			cust.setMaritalStatus("");
		}
		
		
		// income_level: 20byte
		if(Validation.notNullString(cust.getIncomeLevel())) {
			if(!Validation.checkStringLength(cust.getIncomeLevel(), 0, 20))
				cust.setIncomeLevel("");
		} else {
			cust.setIncomeLevel("");
		}
	}
}
