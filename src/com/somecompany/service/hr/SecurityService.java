package com.somecompany.service.hr;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.somecompany.model.hr.Authority;
import com.somecompany.repository.dao.hr.EmployeeDao;
import com.somecompany.repository.dao.hr.SecurityDao;
import com.somecompany.utils.ListHelper;

/********************************************
 * SecurityService
 * 로그인/아웃, 권한 정보 등에 대한 처리를 담당하는 서비스 클래스
 * 
 * static 메소드로 구성
*********************************************/

public class SecurityService {
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	// DAO 
	private static SecurityDao securityDao;
	private static EmployeeDao employeeDao;
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	// 상수 필드
	
	// 세션 속성 이름
	public static final String loginStatusAttr = "login_status";
	public static final String loginUserIdAttr = "login_user_id";
	public static final String loginUserNameAttr = "login_user_name";
	public static final String loginUserAuthAttr = "login_user_auth";
	
	// 세션 속성 값
	public static final String loginSuccess = "login_success";
	public static final String loginFailed = "login_failed";
	public static final String logoutStatus = "logout_status";
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	
	static {
		securityDao = SecurityDao.getInstance("hr-mybatis-config.xml");
		employeeDao = EmployeeDao.getInstance("hr-mybatis-config.xml");
	}
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	// static methods
	
	
	/* isLogin: 로그인 상태인지 확인 */
	public static boolean isLogin(HttpSession session) {
		return session != null && session.getAttribute(loginStatusAttr) != null
				 && session.getAttribute(loginStatusAttr).equals(loginSuccess);
	}
	
	
	/* login: 로그인 */
	public static boolean login(int userId, String password, HttpSession session) {
		boolean login = false;
		
		if(!isLogin(session) && session != null) {
			// 로그인 상태가 아니라면

			// 입력된 id와 패스워드가 유효한지 확인하고 
			login = securityDao.isValidPassword(userId, password);
			
			if(login) {
				// 유효하다면 세션을 설정한다(로그인 성공)
				session.setAttribute(loginStatusAttr, loginSuccess);	// 로그인 상태를 성공으로
				session.setAttribute(loginUserIdAttr, userId);			// 로그인 사용자 id
				
				String userName = employeeDao.getEmployeeName(userId);
				String userAuth = securityDao.get(userId);
				
				session.setAttribute(loginUserNameAttr, userName);		// 로그인 사용자 이름
				
				if(userAuth != null) {
					session.setAttribute(loginUserAuthAttr, userAuth);		// 로그인 사용자 권한
				} else {
					session.setAttribute(loginUserAuthAttr, "");			// 로그인 사용자 권한(권한 없음)
				}
				
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
			session.setAttribute(loginUserIdAttr, "");
			session.setAttribute(loginUserNameAttr, "");
			session.setAttribute(loginUserAuthAttr, "");
			
			session.invalidate();	// 세션 해제
			
			return true;
		}
		
		return false;
	}
	
	
	public static String getAuthority(int userId) {
		return (String) securityDao.get(userId);
	}
	
	
	public static String getAuthorityLoginUser(HttpSession session) {
		if(isLogin(session)) {
			return getAuthority((int) session.getAttribute(loginUserIdAttr));
		}
		
		return "";
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	public static ListHelper<Authority> getList(int page) {
		int totalCount = securityDao.getCount();
		
		// ListHelper 생성 및 설정
		ListHelper<Authority> listHelper = new ListHelper<Authority>(
				totalCount, page, 10);
		
		List<Authority> list = securityDao.getList(listHelper.getOffset(), 10);
		
		listHelper.setList(list);
		
		return listHelper;
	}
	
	
	public static boolean addAuthority(int employeeId, int authorityId) {
		Authority authority = new Authority();
		authority.setEmployeeId(employeeId);
		authority.setAuthorityId(authorityId);
		
		int result = securityDao.addAuthority(authority);
		
		if(result == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean setAuthority(int employeeId, int authorityId) {
		Authority authority = new Authority();
		authority.setEmployeeId(employeeId);
		authority.setAuthorityId(authorityId);
		
		int result = securityDao.setAuthority(authority);
		
		if(result == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean deleteAuthority(int employeeId) {
		int result = securityDao.delete(employeeId);
		
		if(result == 1) {
			return true;
		} else {
			return false;
		}
	}
}
