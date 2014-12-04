package com.somecompany.controller.oe;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somecompany.controller.Controller;
import com.somecompany.controller.Model;
import com.somecompany.model.oe.Customer;
import com.somecompany.model.oe.MyPage;
import com.somecompany.model.oe.Order;
import com.somecompany.model.oe.OrderItem;
import com.somecompany.model.oe.Product;
import com.somecompany.service.oe.CustomerService;
import com.somecompany.service.oe.OrderService;
import com.somecompany.service.oe.ProductService;
import com.somecompany.utils.ListHelper;
import com.somecompany.utils.Validation;

/********************************************
 * CustomerController
 * /cust로 시작되는 uri는 이 컨트롤러가 담당
*********************************************/

public class CustomerController extends Controller {
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 서비스 객체
	
	private CustomerService custService;	// 고객 정보 관련 서비스
	private ProductService prodService;		// 제품 정보 관련 서비스
	private OrderService orderService;		// 주문 정보 관련 서비스
	
	// setters
	public void setCustService(CustomerService custService) {
		this.custService = custService;
	}

	public void setProdService(ProductService prodService) {
		this.prodService = prodService;
	}
	
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 상수
	
	// 장바구니 용 쿠키를 위한 식별자
	public static final String CART_INDEX = "somecompany_cart_prod_";

	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 매핑
	
	@Override
	protected void mapping() {
		/*** GET ***/
		MAP_GET("prod_list.do", "productList");			// 제품 리스트
		MAP_GET("prod.do", "productGet");				// 제품 상세 정보
		MAP_GET("add_cart.do", "addToCart");			// 제품 장바구니에 추가
		MAP_GET("cart.do", "cart");						// 장바구니 메인
		MAP_GET("del_cart.do", "deleteFromCart");		// 제품을 장바구니에서 제거
		MAP_GET("login.do", "loginForm");				// 로그인 폼
		MAP_GET("logout.do", "logout");					// 로그아웃
		MAP_GET("signup.do", "signupForm");				// 회원 가입 양식
		MAP_GET("mypage.do", "myPage");					// 마이페이지
		MAP_GET("order_first.do", "orderFirst");		// 주문 1단계
		MAP_GET("cancel_order.do", "cancelOrder");		// 주문 취소
		MAP_GET("set_myinfo.do", "setCustomerInfoForm");	// 고객 정보 수정 폼
		MAP_GET("set_mypasswd.do", "setPasswordForm");	// 비밀번호 변경 폼
		MAP_GET("sign_out.do", "signoutForm");			// 회원탈퇴 폼
		MAP_GET("order_detail.do", "getMyOrder");		// 주문 내역 보기
		
		/*** POST ***/
		MAP_POST("login.do", "login");			// 로그인 처리
		MAP_POST("signup.do", "signup");		// 회원가입 처리
		MAP_POST("order.do", "addOrder");		// 주문 처리
		MAP_POST("set_myinfo.do", "setCustomerInformation");	// 고객 정보 수정 처리
		MAP_POST("set_mypasswd.do", "setPassword");		// 비밀번호 변경 처리
		MAP_POST("sign_out.do", "signout");		// 회원 탈퇴 처리
		
		/*** Result Binding ***/
		// 상품 리스트
		MAP_RESULT_BINDING("prod_list.do", "list_helper", ListHelper.class);
		
		// 상품 정보
		MAP_RESULT_BINDING("prod.do", "product", Product.class);
		
		// 장바구니
		MAP_RESULT_BINDING("cart.do", "cart_products", ArrayList.class);
		
		// 주문서
		MAP_RESULT_BINDING("order_first.do", "products", ArrayList.class);
		
		// 마이페이지 정보(고객 + 주문)
		MAP_RESULT_BINDING("mypage.do", "myPage", MyPage.class);
		
		// 내 정보 수정용 고객 정보
		MAP_RESULT_BINDING("set_myinfo.do", "customer", Customer.class);
		
		// 비밀번호 수정용 고객 정보
		MAP_RESULT_BINDING("set_mypasswd.do", "customer", Customer.class);
		
		// 주문 내역 보기 용 정보
		MAP_RESULT_BINDING("order_detail.do", "order", Order.class);
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	//  작업 처리 메소드
	
	/******************************************************************/
	// 로그인 처리
	
	/* loginForm: 로그인 폼 출력(GET) */
	public String loginForm(Map<String, String> paramsMap, Model model) {
		return "/cust/forms/login_form.jsp";
	}
	
	/* login: 로그인 처리(POST) */
	public String login(Map<String, String> paramsMap, Model model) {
		boolean loginSuccess = false;
		String viewPage = ":redirect/cust/prod_list.do";		// 디폴트 페이지
		String error_code = "invalid_user_id_or_password";		// 에러 코드(디폴트)
		
		String redirect = getStringParam(paramsMap, "redirect", "");
		
		if(!redirect.isEmpty())
			viewPage = ":redirect/cust/" + redirect;
		
		if(paramsMap.get("user_name") != null && paramsMap.get("passwd") != null && 
				model.getAttribute("request") != null) {
			// 아이디와 비밀번호
			String userName = paramsMap.get("user_name");
			String password = paramsMap.get("passwd");
			
			// MainController가 전달해준 model 객체로부터 HttpServletRequest 객체를 받아온다
			// 로그인 시에 사용할 session 객체를 받아오기 위해
			HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
			
			// 로그인 처리
			loginSuccess = custService.login(userName, password, req.getSession());			
		} else {
			// 로그인 실패 에러 코드 설정
			if(paramsMap.get("user_name") == null || paramsMap.get("user_name").isEmpty())
				error_code = "login_no_user_name";
			else if(paramsMap.get("passwd") == null || paramsMap.get("passwd").isEmpty())
				error_code = "login_no_password";
		}
		
		if(loginSuccess)
			return viewPage;	// 로그인 성공시 메인 페이지로
		else					// 실패시 에러 페이지로
			return ":redirect/errors/cust_login_error.jsp?error_code=" + error_code;
	}
	
	
	/* logout: */
	public String logout(Map<String, String> paramsMap, Model model) {
		boolean logoutSuccess = false;
		String viewPage = ":redirect/cust/prod_list.do";
		
		// HttpServletRequest: session 객체를 가져오기 위해
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		if(CustomerService.isLogin(req.getSession())) {
			// 로그아웃: 로그인 되어 있는 상태에서만 처리
			logoutSuccess = CustomerService.logout(req.getSession());
			
			if(!logoutSuccess) {
				// 실패시 에러페이지로 이동: 로그아웃 처리 실패
				viewPage = "/errors/cust_login_error.jsp?error_code=logout_failure";
			}
			
		} else {
			// 실패시 에러페이지로 이동: 로그인 상태가 아님
			viewPage = "/errors/cust_login_error.jsp?error_code=logout_no_login";
		}
		
		return viewPage;
	}
	
	/******************************************************************/
	// 회원 가입
	public String signupForm(Map<String, String> paramsMap, Model model) {
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		if(CustomerService.isLogin(req.getSession())) {
			// 만일 로그인 상태라면 로그아웃 시킨다
			CustomerService.logout(req.getSession());
		}
		
		return "/cust/forms/signup_form.jsp";
	}
	
	public String signup(Map<String, String> paramsMap, Model model) {
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		boolean result = false;
		String viewPage = ":redirect/cust/mypage.do";
		
		if(CustomerService.isLogin(req.getSession())) {
			// 만일 로그인 상태라면 로그아웃 시킨다
			CustomerService.logout(req.getSession());
		}
		
		Customer customer = createEmployeeParam(paramsMap, true);
		
		if(customer != null) {
			result = custService.add(customer);
		} else {
			viewPage = "/errors/cust_error.jsp?error_code=add_null_cust";
		}
		
		if(!result)
			viewPage = "/errors/cust_error.jsp?error_code=add_failure";
		
		return viewPage;
	}
	
	/******************************************************************/
	// 회원 탈퇴
	
	public String signoutForm(Map<String, String> paramsMap, Model model) {
		return "/cust/forms/sign_out_form.jsp";
	}
	
	public String signout(Map<String, String> paramsMap, Model model) {
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		int customerId = getIntParam(paramsMap, "cust_id", 0);
		boolean resultOrder = false;
		boolean resultCust = false;
		String viewPage = ":redirect/cust/prod_list.do";
		
		// 해당 고객의 주문 정보를 삭제한다
		resultOrder = orderService.deleteByCustomerId(customerId);
		
		if(resultOrder) {
			// 성공하면 고객 정보를 삭제한다
			resultCust = custService.delete(customerId);
			
			if(!resultCust) {
				// 실패한 경우 에러 페이지로 이동시킨다: 주문 정보는 삭제되었지만 고객 정보만 남는다
				viewPage = ":redirect/errors/cust_error.jsp?error_code=signout_failure";
			}
		} else {
			// 실패하면 고객 탈퇴 절차를 중지(주문 정보 삭제도 롤백된 상태)
			viewPage = ":redirect/errors/cust_error.jsp?error_code=signout_delete_order_failure";
		}
		
		// 성공하면 로그아웃처리
		if(CustomerService.isLogin(req.getSession())) {
			// 로그인 상태라면 로그아웃
			CustomerService.logout(req.getSession());
		}
		
		return viewPage;
	}
	
	/******************************************************************/
	// 고객 정보 수정
	
	public String setCustomerInfoForm(Map<String, String> paramsMap, Model model) {
		int customerId = getIntParam(paramsMap, "id", 0);
		String viewPage = "/cust/forms/set_cust_info.jsp?id=" + customerId;
		
		// form에서 사용할 Customer 객체를 가져온다
		Customer customer = custService.get(customerId);
		
		// 결과 바인딩
		model.addAttribute("customer", customer);
		
		
		return viewPage;
	}
	
	
	public String setCustomerInformation(Map<String, String> paramsMap, Model model) {
		Customer customer = createEmployeeParam(paramsMap, false);
		String viewPage = ":redirect/cust/mypage.do";
		
		boolean result = custService.update(customer);
		
		if(!result)
			viewPage = "/errors/cust_error.jsp?error_code=update_failure";
		
		return viewPage;
	}
	
	/******************************************************************/
	// 비밀번호 수정
	
	public String setPasswordForm(Map<String, String> paramsMap, Model model) {
		int customerId = getIntParam(paramsMap, "id", 0);
		String viewPage = "/cust/forms/set_cust_password.jsp?id=" + customerId;
		
		// form에서 사용할 Customer 객체를 가져온다
		Customer customer = custService.get(customerId);
		
		// 결과 바인딩
		model.addAttribute("customer", customer);
		
		return viewPage;
	}
	
	public String setPassword(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int customerId = getIntParam(paramsMap, "cust_id", 0);
		String oldPasswd = getStringParam(paramsMap, "old_passwd", "");
		String newPasswd1 = getStringParam(paramsMap, "passwd1", "");
		String newPasswd2 = getStringParam(paramsMap, "passwd2", "");
		
		String viewPage = ":redirect/cust/mypage.do";
		boolean result = false;
		
		if(customerId != 0 && !oldPasswd.isEmpty() && !newPasswd1.isEmpty() && newPasswd1.equals(newPasswd2)) {
			// 정보가 적절히 전달되었다면 update
			result = custService.changePasswd(customerId, oldPasswd, newPasswd1);
			
			if(!result)
				viewPage = ":redirect/errors/cust_error.jsp?error_code=change_passwd_failure";			
		} else {
			viewPage = ":redirect/errors/cust_error.jsp?error_code=change_passwd_no_info";
		}		
		
		return viewPage;
	}
	
	/******************************************************************/
	
	/* productList: 고객용 제품 카달로그 */
	public String productList(Map<String, String> paramsMap, Model model) {
		//파라미터 설정 
		int page = getIntParam(paramsMap, "page", 1);
		int categoryId = getIntParam(paramsMap, "category", -1);
		short orderby = getShortParam(paramsMap, "orderby", (short) -1);
		
		String viewPage = "/cust/product_list.jsp";
		
		// 리스트 가져오기
		ListHelper<Product> listHelper = prodService.getList(page, orderby, categoryId, 10, true);
		
		// 결과 바인딩
		model.addAttribute("list_helper", listHelper);
		
		return viewPage;
	}
	
	/******************************************************************/
	
	/* productGet: 고객용 제품 정보 */
	public String productGet(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "id", 0);
		int page = getIntParam(paramsMap, "page", 1);
		Short orderby = getShortParam(paramsMap, "orderby", (short) -1);
		
		String viewPage = "/cust/product_detail.jsp";
				
		Product prod = prodService.get(productId);
		
		model.addAttribute("product", prod);
		
		viewPage += ("?page=" + page + "&orderby=" + orderby);
		
		return viewPage;
	}
	
	/******************************************************************/
	
	/* addToCart: 장바구니에 담기(GET) */
	public String addToCart(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "prod_id", 0);
		long quantity = 0;
		
		String viewPage = ":redirect/cust/cart.do";
		
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		HttpServletResponse resp = (HttpServletResponse) model.getAttribute("response");
		
		if(productId != 0) {
			quantity = getLongParam(paramsMap, "qty", 0);
			if(quantity != 0) {
				quantity = Long.parseLong(paramsMap.get("qty"));
				
				for(Cookie cookie : req.getCookies()) {
					if(cookie != null) {
						if(hasThisCookie(productId, cookie)) {
							// 동일 쿠기 존재시 기존 쿠키 값 변경
							String qtyString = cookie.getValue();
							quantity += Long.parseLong(qtyString);
							
							viewPage = setCookie(productId, quantity, resp);
						} else {
							// 존재하지 않는 경우 새 쿠키 생성 후 추가
							viewPage = setCookie(productId, quantity, resp);
						}
					} else {
						// 실패: 제품 담기 실패
						return ":redirect/errors/cust_error.jsp?error_code=addcart_failure";
					}
				}				
			} else {
				// 실패: 수량이 0이거나 잘못된 정보가 전달됨
				return ":redirect/errors/cust_error.jsp?error_code=addcart_zero_qty";
			}
		} else {
			// 실패: id가 전달되지 않음
			return ":redirect/errors/cust_error.jsp?error_code=addcart_no_prod_id";
		}
		
		return viewPage;
	}
	
	/* setCookie & hasThisCookie: 내부 함수 */
	/* 쿠키: 이름 - somecompany_cart_prod_[제품번호], 값 - 주문수량  */
	private String setCookie(int productId, long quantity, HttpServletResponse resp) {
		String productIdStr = CART_INDEX + Integer.toString(productId);
		
		if(quantity > prodService.getQunatitySum(productId)) {
			return ":redirect/errors/cust_error.jsp?error_code=addcart_excess_qty";
		}
		
		Cookie newCookie = new Cookie(productIdStr,
				Long.toString(quantity));
		
		newCookie.setMaxAge(60 * 60 * 24);		// 유효시간: 60 * 60 * 24 = 24H  
		
		resp.addCookie(newCookie);
		
		return ":redirect/cust/cart.do";	// 새로고침 시 제품이 의도치 않게 재추가되는 것을 방지하기 위해 redirect
	}
	
	/* hasThisCookie: 지정된 쿠키가 존재하는가 */
	private boolean hasThisCookie(int productId, Cookie cookie) {
		String productIdStr = CART_INDEX + Integer.toString(productId);
		
		if(cookie.getName().equals(productIdStr)) 
			return true;
		else 
			return false;
	}
	
	/******************************************************************/
	
	/* cart: 장바구니 메인 */
	public String cart(Map<String, String> paramsMap, Model model) {
		List<Product> products = null;	// 바인딩할 결과 리스트(제품)
		String viewPage = "/cust/cart_main.jsp";
		
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		// 쿠키 정보를 바탕으로 해당 제품의 정보(리스트)를 가져오기
		products = makeProductsFromCookies(req.getCookies());
		
		// 결과 바인딩
		model.addAttribute("cart_products", products);
		
		return viewPage;
	}
	
	
	/* isCartCookie: 해당 쿠키가 장바구니 용 쿠키인가 */
	private boolean isCartCookie(Cookie cookie) {
		if(cookie.getName().startsWith(CART_INDEX)) 
			return true;
		else 
			return false;
	}
	
	
	/* makeProducts: 쿠키로부터 주문 제품 목록을 가져온다 */
	private List<Product> makeProductsFromCookies(Cookie[] cookies) {
		List<Product> products = new ArrayList<Product>();
		
		for(Cookie cookie : cookies) {
			if(isCartCookie(cookie)) {
				// id 파싱
				int productId = Integer.parseInt(cookie.getName().replace(CART_INDEX, ""));
				
				// 제품 정보(필요한 정보: id/이름/가격/설명 등)
				Product prod = prodService.get(productId);
				
				// 주문 수량 설정
				prod.setQtySum(Long.parseLong(cookie.getValue()));
				
				// 리스트에 추가
				products.add(prod);
			}
		}
		
		return products;
	}
	
	/******************************************************************/
	
	/* deleteFromCart: id에 해당하는 제품의 카트 정보(쿠키) 삭제(GET) */
	public String deleteFromCart(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "id", 0);
		String viewPage = ":redirect/cust/cart.do";
		
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		HttpServletResponse resp = (HttpServletResponse) model.getAttribute("response");
		
		for(Cookie cookie : req.getCookies()) {
			if(cookie != null) {
				if(hasThisCookie(productId, cookie)) {
					viewPage = deleteCookie(productId, resp);
				}
			} else {
				return ":redirect/errors/cust_error.jsp?error_code=addcart_failure";
			}
		}
		
		return viewPage;
	}
	
	/* deleteCookie: productId에 해당하는 쿠키 삭제 */
	private String deleteCookie(int productId, HttpServletResponse resp) {
		String productIdStr = CART_INDEX + Integer.toString(productId);
		
		Cookie newCookie = new Cookie(productIdStr, "");
		newCookie.setMaxAge(0);
		
		resp.addCookie(newCookie);
		
		return ":redirect/cust/cart.do";
	}
	
	/******************************************************************/
	// 주문
	
	/* orderFirst: 주문 1단계 */
	public String orderFirst(Map<String, String> paramsMap, Model model) {
		String viewPage = "/cust/forms/order_form.jsp";
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		if(!CustomerService.isLogin(req.getSession())) {
			// 로그인 상태가 아니면 로그인 페이지로 보낸다
			viewPage = "/cust/login.do?redirect=order_first.do";
		} else {
			// 주문서에서 사용할 제품 목록 가져오기
			List<Product> products = null;
			products = makeProductsFromCookies(req.getCookies());
			
			model.addAttribute("products", products);	// 바인딩
		}

		return viewPage;
	}
	
	
	/* addOrder: 주문하기 */
	public String addOrder(Map<String, String> paramsMap, Model model) {
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		HttpServletResponse resp = (HttpServletResponse) model.getAttribute("response");
		
		String viewPage = ":redirect/cust/mypage.do";
		int customerId = getIntParam(paramsMap, "cust_id", 0);
		
		if(customerId == 0) {
			return "/errors/cust_error.jsp?error_code=add_order_failure";
		}
		
		// 추가될 상품 정보
		List<Product> products = makeProductsFromCookies(req.getCookies());
		
		// 주문 정보
		Order order = new Order();
		order.setCustomerId(customerId);
		order.setOrderMode("online");
		order.setOrderStatus((short) 1);
		
		// 주문 항목 정보
		List<OrderItem> items = new ArrayList<OrderItem>();
		
		// OrderItem <--- Product
		for(int i = 0; i < products.size(); i++) {
			OrderItem item = new OrderItem();
			Product product = products.get(i);
			
			item.setLineItemId(i+1);
			item.setProductId(product.getProductId());
			item.setUnitPrice(product.getListPrice());
			item.setQuantity(product.getQtySum());
			
			items.add(item);
		}
		
		// OrderItem 설정: 자동으로 총액수가 계산된다
		// OrderItem의 orderId도 이 과정에서 자동으로 설정됨
		order.setOrderItems(items);
		
		boolean result = orderService.add(order);
		
		if(!result) {
			viewPage = "/errors/cust_error.jsp?error_code=add_order_failure";
		} else {
			// 주문 추가에 성공했으면, 장바구니를 비운다
			for(OrderItem item: items) {
				deleteCookie(item.getProductId(), resp);
			}
		}
		
		return viewPage;
	}
	
	/******************************************************************/
	// 마이페이지(내 정보 조회, 내 주문 정보 조회)
	
	public String myPage(Map<String, String> paramsMap, Model model) {
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		int page = getIntParam(paramsMap, "page", 1);
		int customerId = 0;
		MyPage myPage = new MyPage();
		
		String viewPage = "/cust/my_page.jsp";
		
		if(!CustomerService.isLogin(req.getSession())) {
			// 로그인 상태가 아니면 로그인 페이지로 보낸다
			viewPage = "/cust/login.do?redirect=mypage.do";
		} else {
			// 로그인 상태라면 my page 정보를 가져온다
			// 고객 번호
			customerId = (Integer) req.getSession().getAttribute(CustomerService.loginUserIdAttr);
			
			// 고객 정보
			myPage.setCustomer(custService.get(customerId));
			
			// 주문 리스트를 보여줄 list helper
			myPage.setOrderListHelper(orderService.getList(customerId, page));
			
			viewPage += ("?page=" + page);
		}
		
		// 결과 바인딩
		model.addAttribute("myPage", myPage);
		
		return viewPage;
	}	
	
	/******************************************************************/
	
	public String cancelOrder(Map<String, String> paramsMap, Model model) {
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		int orderId = getIntParam(paramsMap, "id", 0);
		int customerId = getIntParam(paramsMap, "cust_id", 0);
		int sessionUserId = (int) req.getSession().getAttribute(CustomerService.loginUserIdAttr);
		boolean cancelSuccess = false;
		
		String viewPage = ":redirect/cust/mypage.do?id=" + customerId;
		
		if(!CustomerService.isLogin(req.getSession()) || customerId != sessionUserId) {
			// 로그인 상태가 아니거나 로그인 유저의 id가 지우려는 주문의 고객 id와 동일하지 않을 때(다른 사용자의 주문을 취소하려고 할 때) 실패
			viewPage = ":redirect/errors/cust_error.jsp?error_code=cancel_order_invalid_access";
		} else {
			cancelSuccess = orderService.cancelOrder(orderId, true);
			
			if(!cancelSuccess)
				viewPage = ":redirect/errors/cust_error.jsp?error_code=cancel_order_failure";
		}
		
		return viewPage;
	}
	
	/******************************************************************/
	// 주문 내역 조회
	
	public String getMyOrder(Map<String, String> paramsMap, Model model) {
		int orderId = getIntParam(paramsMap, "id", 0);
		String viewPage = "/cust/my_order.jsp";
		
		Order order = orderService.get(orderId);
		
		model.addAttribute("order", order);
		
		return viewPage;
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	private Customer createEmployeeParam(Map<String, String> paramsMap, boolean add) {
		Customer cust = new Customer();
		
		String userName = "";
		String passwd1 = "";
		String passwd2 = "";
		
		if(add) {
			if(Validation.notEmptyString(paramsMap.get("user_name"))) {
				userName = Validation.replaceHtmlCharacter(paramsMap.get("user_name").trim());
			}
			
			if(Validation.notEmptyString(paramsMap.get("passwd1"))) {
				passwd1 = Validation.replaceHtmlCharacter(paramsMap.get("passwd1").trim());
			}
			
			if(Validation.notEmptyString(paramsMap.get("passwd2"))) {
				passwd2 = Validation.replaceHtmlCharacter(paramsMap.get("passwd2").trim());
			}
			
			if(!passwd1.equals(passwd2)) {
				return null;	// 전달받은 비밀번호 두 개가 일치하지 않으면 null 객체를 리턴한다
			}
		}
		
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
		
		String gender = "";
		if(Validation.notEmptyString(paramsMap.get("gender"))) {
			gender = Validation.replaceHtmlCharacter(paramsMap.get("gender").trim());
		}
		
		
		// 주소
		String streetAddress = "";
		if(Validation.notEmptyString(paramsMap.get("street_address"))) {
			streetAddress = Validation.replaceHtmlCharacter(paramsMap.get("street_address").trim());
		}
		
		String postalCode = "";
		if(Validation.notEmptyString(paramsMap.get("postal_code"))) {
			postalCode = Validation.replaceHtmlCharacter(paramsMap.get("postal_code").trim());
		}
		
		String city = "";
		if(Validation.notEmptyString(paramsMap.get("city"))) {
			city = Validation.replaceHtmlCharacter(paramsMap.get("city").trim());
		}
		
		String stateProvince = "";
		if(Validation.notEmptyString(paramsMap.get("state_province"))) {
			stateProvince = Validation.replaceHtmlCharacter(paramsMap.get("state_province").trim());
		}
		
		String countryId = "";
		if(Validation.notEmptyString(paramsMap.get("country_id"))) {
			countryId = Validation.replaceHtmlCharacter(paramsMap.get("country_id").trim());
		}
				
		// 숫자형 파라미터: 적절한 파라미터의 경우만 값을 받아온다(파싱 예외는 걸러내지 못한다)
		int customerId = 0;
		if(Validation.notEmptyString(paramsMap.get("cust_id")))
			customerId = Integer.parseInt(paramsMap.get("cust_id"));
		
		int birthYear = 0;
		if(Validation.notEmptyString(paramsMap.get("birth_year")))
			birthYear = Integer.parseInt(paramsMap.get("birth_year"));
		
		int birthMonth = 0;
		if(Validation.notEmptyString(paramsMap.get("birth_month")))
			birthMonth = Integer.parseInt(paramsMap.get("birth_month"));
		
		int birthDate = 0;
		if(Validation.notEmptyString(paramsMap.get("birth_date")))
			birthDate = Integer.parseInt(paramsMap.get("birth_date"));
		
		cust.setCustomerUserName(userName);
		cust.setCustomerPassword(passwd1);
		cust.setCustomerId(customerId);
		cust.setCustomerFirstName(firstName);
		cust.setCustomerLastName(lastName);
		cust.setCustomerEmail(email);
		cust.setPhoneNumber(phoneNumber);
		cust.setGender(gender);
		cust.setStreetAddress(streetAddress);
		cust.setPostalCode(postalCode);
		cust.setCity(city);
		cust.setStateProvince(stateProvince);
		cust.setCountryId(countryId);
		
		Calendar cal = new GregorianCalendar(birthYear, birthMonth - 1, birthDate);
		cust.setDateOfBirth(new Date(cal.getTimeInMillis()));
		
		return cust;
	}
}
