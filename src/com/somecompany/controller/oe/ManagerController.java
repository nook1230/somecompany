package com.somecompany.controller.oe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.somecompany.controller.Controller;
import com.somecompany.controller.Model;
import com.somecompany.model.oe.Customer;
import com.somecompany.model.oe.Inventory;
import com.somecompany.model.oe.Order;
import com.somecompany.model.oe.OrderAndWarehouses;
import com.somecompany.model.oe.Product;
import com.somecompany.model.oe.Warehouse;
import com.somecompany.service.hr.SecurityService;
import com.somecompany.service.oe.CustomerService;
import com.somecompany.service.oe.OrderService;
import com.somecompany.service.oe.ProductService;
import com.somecompany.utils.ListHelper;
import com.somecompany.utils.Validation;

/********************************************
 * ManagerController
 * /manager로 시작되는 uri는 이 컨트롤러가 담당
*********************************************/

public class ManagerController extends Controller {
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
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 매핑

	@Override
	protected void mapping() {
		/*** GET ***/
		MAP_GET("order_list.do", "getOrderList");			// 주문 목록
		MAP_GET("order_detail.do", "getOrder");				// 주문 정보 조회
		MAP_GET("delete_order.do", "deleteOrderForm");		// 주문 삭제 폼
		MAP_GET("warehouse_list.do", "getWarehouseList");	// 창고 목록
		MAP_GET("inventories.do", "getInventories");		// 개별 창고 재고 조회 
		MAP_GET("search_prod_inv.do", "searchInventory");	// 제품 재고 정보 검색
		MAP_GET("warehousing.do", "manageWarehouseForm");	// 입출고 관리 폼
		MAP_GET("prod_list.do", "productList");				// 제품 목록
		MAP_GET("prod.do", "productGet");					// 제품 정보 조회
		MAP_GET("add_prod.do", "addProductForm");			// 제품 추가 폼
		MAP_GET("update_prod.do", "updateProductForm");		// 제품 정보 변경 폼
		MAP_GET("del_prod.do", "deleteProductForm");		// 제품 삭제 폼
		MAP_GET("cust_list.do", "customerList");			// 고객 목록
		MAP_GET("cust_detail.do", "getCustomer");			// 고객 정보 조회
		MAP_GET("del_cust.do", "deleteCustomerForm");		// 고객 강제 탈퇴 폼
		
		/*** POST ***/
		MAP_POST("cancel_order.do", "cancelOrder");			// 주문 취소
		MAP_POST("pay_order_ok.do", "payOrder");			// 결제 완료 처리
		MAP_POST("ship_order_first.do", "shipOrderFirst");	// 발송(선적) 1단계 처리
		MAP_POST("ship_order.do", "shipOrder");				// 선적 처리
		MAP_POST("complete_order.do", "completeOrder");		// 배송(주문) 완료 처리
		MAP_POST("delete_order.do", "deleteOrder");			// 주문 삭제
		MAP_POST("warehousing.do", "manageWarehouse");		// 입출고 관리
		MAP_POST("add_prod.do", "addProduct");				// 제품 추가
		MAP_POST("update_prod.do", "updateProduct");		// 제품 정보 변경
		MAP_POST("del_prod.do", "deleteProduct");			// 제품 삭제
		MAP_POST("del_cust.do", "deleteCustomer");			// 회원 강제 탈퇴 처리
		
		/*** Result Binding ***/
		// 주문 목록
		MAP_RESULT_BINDING("order_list.do", "list_helper", ListHelper.class);
		
		// 주문 내역
		MAP_RESULT_BINDING("order_detail.do", "order", Order.class);
		
		// 주문과 출고 가능한 창고 목록
		MAP_RESULT_BINDING("ship_order_first.do", "order_and_warehouses", OrderAndWarehouses.class);
		
		// 창고 목록
		MAP_RESULT_BINDING("warehouse_list.do", "warehouses", ArrayList.class);
		
		// 재고 목록
		MAP_RESULT_BINDING("inventories.do", "list_helper", ListHelper.class);
		
		// 재고 목록
		MAP_RESULT_BINDING("search_prod_inv.do", "inventories", ArrayList.class);
		
		// 제품 목록
		MAP_RESULT_BINDING("prod_list.do", "list_helper", ListHelper.class);
		
		// 제품 정보
		MAP_RESULT_BINDING("prod.do", "product", Product.class);
		
		// 제품 정보(업데이트 용)
		MAP_RESULT_BINDING("update_prod.do", "product", Product.class);
		
		// 고객 목록
		MAP_RESULT_BINDING("cust_list.do", "list_helper", ListHelper.class);
		
		// 고객 목록
		MAP_RESULT_BINDING("cust_detail.do", "customer", Customer.class);
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	//  작업 처리 메소드
	
	/******************************************************************/
	// 주문 리스트
	public String getOrderList(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		short orderStatus = getShortParam(paramsMap, "status", (short) -1);
		
		// 리스트 헬퍼(주문)
		ListHelper<Order> listHelper = orderService.getList(page, orderStatus);
		
		// 결과 바인딩
		model.addAttribute("list_helper", listHelper);
		
		return "/manager/order_list.jsp";	// 뷰 호출
	}
	
	/******************************************************************/
	// 주문 내역
	public String getOrder(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		short orderStatus = getShortParam(paramsMap, "status", (short) -1);
		
		int orderId = getIntParam(paramsMap, "id", 0);
		
		// 주문 정보
		Order order = orderService.get(orderId);
		
		// 결과 바인딩
		model.addAttribute("order", order);
		
		return "/manager/order_detail.jsp?page=" + page + "&status=" + orderStatus;	// 뷰 호출
	}
	
	/******************************************************************/
	// 주문 update
	
	// 주문 취소
	public String cancelOrder(Map<String, String> paramsMap, Model model) {		
		return updateOrder(paramsMap, model, new UpdateOrderCallback() {

			@Override
			public void proceed(int id) {
				orderService.cancelOrder(id, false);
			}
			
		}, "order_list.do", "cancel_order_no_auth");
	}
	
	// 결제 확인
	public String payOrder(Map<String, String> paramsMap, Model model) {
		return updateOrder(paramsMap, model, new UpdateOrderCallback() {

			@Override
			public void proceed(int id) {
				orderService.payOrder(id);
			}
			
		}, "order_list.do", "pay_order_no_auth");
	}
	
	// 선적 처리 1단계(POST)
	public String shipOrderFirst(Map<String, String> paramsMap, Model model) {
		short newOrderStatus = getShortParam(paramsMap, "new_status", (short) -1);
		
		String filteredParamName = "order_id_";
		List<Integer> orderIds = filterOutIntParameter(paramsMap, filteredParamName);
		
		if(orderIds.size() != 1)
			return "/errors/manager_error.jsp?error_code=ship_order_dup_ids";
		
		int orderId = orderIds.get(0);	// id는 하나만 전달된다
		
		Order order = orderService.get(orderId); 	// 주문 정보
		List<Warehouse> warehouses = null;			// 재고 정보
		if(order != null)
			warehouses = orderService.getAvailableWarehouses(order.getOrderItems());
		
		// 주문과 재고 정보를 함께 담는 객체
		OrderAndWarehouses oaw = new OrderAndWarehouses();
		oaw.setOrder(order);
		oaw.setWarehouses(warehouses);
		
		// 결과 바인딩
		model.addAttribute("order_and_warehouses", oaw);
		
		return "/manager/forms/ship_order_form.jsp?status=" + newOrderStatus;
	}
	
	// 선적 처리 2단계(POST)
	public String shipOrder(Map<String, String> paramsMap, Model model) {
		int orderId = getIntParam(paramsMap, "order_id", 0);
		short OrderStatus = getShortParam(paramsMap, "status", (short) -1);
		int warehouseId = getIntParam(paramsMap, "warehouse_id", 0);
		
		boolean result = orderService.shipOrder(orderId, OrderStatus, warehouseId);
		
		if(!result)
			return ":redirect/errors/manager_error.jsp?error_code=ship_order_failure";
		
		return ":redirect/manager/order_list.do";
	}
	
	// 주문 처리 완료(배송완료)
	public String completeOrder(Map<String, String> paramsMap, Model model) {
		return updateOrder(paramsMap, model, new UpdateOrderCallback() {

			@Override
			public void proceed(int id) {
				orderService.completeOrder(id);
			}
			
		}, "order_list.do", "complete_order_no_auth");
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////
	
	// checkAuthority: 권한 체크
	private boolean checkAuthority(HttpSession session, String[] authorities) {
		if(SecurityService.isLogin(session)) {
			String loginUserAuthority = SecurityService.getAuthorityLoginUser(session);
			
			for(String authority : authorities) {
				if(loginUserAuthority.equals(authority))
					return true;
			}
			
			return false;
		} else {
			return false;
		}
	}
	
	// updateOrder: 주문 업데이트에 공통적인 로직을 담고 있는 템플릿
	private String updateOrder(Map<String, String> paramsMap, Model model, 
			UpdateOrderCallback callback, String returnPage, String error_code) {
		// HttpServletRequest: session 객체를 가져오기 위해
		HttpServletRequest req = (HttpServletRequest) model.getAttribute("request");
		
		// 이 작업을 시행하는데 필요한 권한 목록
		String[] authorities = {"PURCHASE", "ALL"};
		
		// 뷰 페이지
		String viewPage = ":redirect/manager/" + returnPage;
		
		if(checkAuthority(req.getSession(), authorities)) {
			// 파라미터 필터링: 체크된 아이디를 모두 가져온다
			String filteredParamName = "order_id_";
			List<Integer> orderIds = filterOutIntParameter(paramsMap, filteredParamName);
			
			for(int id : orderIds) {
				callback.proceed(id);
			}
				
		} else {
			viewPage = ":redirect/errors/manager_error.jsp?error_code=" + error_code;
		}
		
		return viewPage;
	}
	
	// UpdateOrderCallback: 위의 updateOrder 메소드에서 사용되는 콜백 인터페이스.
	private interface UpdateOrderCallback {
		void proceed(int id);
	}
	
	/******************************************************************/
	// 주문 삭제
	
	// 주문 삭제 폼(GET)
	public String deleteOrderForm(Map<String, String> paramsMap, Model model) {
		int orderId = getIntParam(paramsMap, "id", 0);
		return "/manager/forms/delete_order_form.jsp?id=" + orderId;
	}
	
	// 주문 삭제 처리(POST)
	public String deleteOrder(Map<String, String> paramsMap, Model model) {
		int orderId = getIntParam(paramsMap, "id", 0);
		String viewPage = ":redirect/manager/order_list.do";
		
		boolean result = orderService.delete(orderId);
		
		if(!result)
			viewPage = ":redirect/errors/manager_error.jsp?error_code=delete_order_failure";
		
		return viewPage;
	}
	
	/******************************************************************/
	// 재고 관련
	
	// 창고 목록
	public String getWarehouseList(Map<String, String> paramsMap, Model model) {
		List<Warehouse> warehouses = prodService.getWarehouseList();
		model.addAttribute("warehouses", warehouses);
		
		return "/manager/warehouse_list.jsp";
	}
	
	// 재고 목록
	public String getInventories(Map<String, String> paramsMap, Model model) {
		int page = getIntParam(paramsMap, "page", 1);
		int warehouseId = getIntParam(paramsMap, "id", 0);
		
		ListHelper<Inventory> listHelper = prodService.getInventoryInWarehouse(warehouseId, page);
		
		model.addAttribute("list_helper", listHelper);
		
		return "/manager/inventories.jsp";
	}
	
	// 재고 검색(상품 번호로)
	public String searchInventory(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "prod_id", -1);
		List<Inventory> inventories = prodService.getInventory(productId);
		
		model.addAttribute("inventories", inventories);
		
		return "/manager/prod_inventories.jsp";
	}
	
	// 입출고 관리 폼(GET)
	public String manageWarehouseForm(Map<String, String> paramsMap, Model model) {
		return "/manager/forms/manage_warehouse.jsp";
	}
	
	// 입출고 관리(POST)
	public String manageWarehouse(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "prod_id", 0);
		long quantity = getLongParam(paramsMap, "quantity", 0);
		int warehouseId = getIntParam(paramsMap, "ware_id", 0);
		String inOrOut = getStringParam(paramsMap, "in_or_out", "in");
		
		if(quantity < 0)
			quantity = -quantity;	// 전달된 quantity가 0보다 작다면 부호를 바꿔준다
		
		if(inOrOut.equals("out"))
			quantity = -quantity;	// 출고 요청의 경우 quantity의 부호를 바꿔준다
		
		boolean result = prodService.stockAndWarehousing(productId, warehouseId, quantity);
		
		if(!result)
			return ":redirect/errors/manager_error.jsp?error_code=manage_warehouse_failure";
		
		return ":redirect/manager/warehousing.do?ware_id=" + warehouseId;
	}
	
	/******************************************************************/
	// 제품 관련
	
	/* productList: 제품 목록 */
	public String productList(Map<String, String> paramsMap, Model model) {
		//파라미터 설정 
		int page = getIntParam(paramsMap, "page", 1);
		String viewPage = "/manager/product_list.jsp";
		
		// 리스트 가져오기: page/정렬 없음/카테고리 없음/페이지 당 10개/모든 제품 상태 보기
		ListHelper<Product> listHelper = prodService.getList(page, (short) -1, -1, 10, false);
		
		// 결과 바인딩
		model.addAttribute("list_helper", listHelper);
		
		return viewPage;
	}
	
	/* productGet: 고객용 제품 정보 */
	public String productGet(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "id", 0);
		int page = getIntParam(paramsMap, "page", 1);
		
		String viewPage = "/manager/product_detail.jsp";
				
		Product prod = prodService.get(productId);
		
		model.addAttribute("product", prod);
		
		viewPage += ("?page=" + page);
		
		return viewPage;
	}
	
	/* addProductForm: 제품 추가 폼 */
	public String addProductForm(Map<String, String> paramsMap, Model model) {
		return "/manager/forms/add_product_form.jsp";
	}
	
	/* addProduct: 제품 추가 */
	public String addProduct(Map<String, String> paramsMap, Model model) {
		Product product = createProduct(paramsMap);
		String viewPage = ":redirect/manager/prod_list.do";
		
		if(product != null) {
			boolean result = prodService.add(product);
			
			if(!result)
				viewPage = ":redirect/errors/manager_error.jsp?error_code=add_prod_failure";
		} else {
			viewPage = ":redirect/errors/manager_error.jsp?error_code=add_prod_null_prod";
		}
		
		return viewPage;
	}
	
	/* updateProductForm: 제품 정보 변경 폼 */
	public String updateProductForm(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "id", 0);
		int page = getIntParam(paramsMap, "page", 1);
		
		String viewPage = "/manager/forms/update_product_form.jsp";
		
		Product prod = prodService.get(productId);
		
		model.addAttribute("product", prod);
		
		viewPage += ("?page=" + page);
		
		return viewPage;
	}
	
	/* updateProduct: 제품 정보 변경 폼 */
	public String updateProduct(Map<String, String> paramsMap, Model model) {
		Product product = createProduct(paramsMap);
		int page = getIntParam(paramsMap, "page", 1);
		String viewPage = ":redirect/manager/prod.do?id=";
		
		if(product != null) {
			boolean result = prodService.update(product);
			
			if(result)
				viewPage += (product.getProductId() + "&page=" + page);
			else
				viewPage = ":redirect/errors/manager_error.jsp?error_code=update_prod_failure";
		} else {
			viewPage = ":redirect/errors/manager_error.jsp?error_code=update_prod_null_prod";
		}
		
		return viewPage;
	}
	
	/* deleteProductForm: 제품 정보 삭제(GET) */
	public String deleteProductForm(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "id", 0);
		return "/manager/forms/delete_product_form.jsp?id=" + productId;
	}
	
	/* deleteProduct: 제품 정보 삭제(POST) */
	public String deleteProduct(Map<String, String> paramsMap, Model model) {
		int productId = getIntParam(paramsMap, "id", 0);
		String viewPage = ":redirect/manager/prod_list.do";
		
		boolean result = false;
		
		result = prodService.delete(productId);
		
		if(!result)
			viewPage = ":redirect/errors/manager_error.jsp?error_code=delete_prod_failure";
		
		return viewPage;
	}	
	
	//////////////////////////////////////////////////////////////////////////////////
	
	// createProduct: 파라미터를 받아서 Product 객체를 생성
	private Product createProduct(Map<String, String> paramsMap) {
		Product product = new Product();
		
		int productId = 0;
		if(Validation.notEmptyString(paramsMap.get("prod_id"))) {
			productId = Integer.parseInt(paramsMap.get("prod_id"));
		}
		
		// 문자열 파라미터: 부적절한 문자들을 걸러낸다(xss 공격 등을 대비하기 위해)		
		String productName = "";
		if(Validation.notEmptyString(paramsMap.get("prod_name"))) {
			productName = Validation.replaceHtmlCharacter(paramsMap.get("prod_name").trim());
		}
		
		String productStatus = "";
		if(Validation.notEmptyString(paramsMap.get("prod_status"))) {
			productStatus = Validation.replaceHtmlCharacter(paramsMap.get("prod_status").trim());
		}
		
		String productWarranty = "";
		if(Validation.notEmptyString(paramsMap.get("prod_warranty"))) {
			productWarranty = Validation.replaceHtmlCharacter(paramsMap.get("prod_warranty").trim());
		}
		
		String productDescription = "";
		if(Validation.notEmptyString(paramsMap.get("prod_desc"))) {
			productDescription = Validation.replaceHtmlCharacter(paramsMap.get("prod_desc").trim());
		}
		
		// 숫자형 파라미터
		short categoryId = 0;
		if(Validation.notEmptyString(paramsMap.get("prod_cat"))) {
			categoryId = Short.parseShort(paramsMap.get("prod_cat"));
		}
		
		double listPrice = 0.0;
		if(Validation.notEmptyString(paramsMap.get("prod_list_price"))) {
			listPrice = Double.parseDouble(paramsMap.get("prod_list_price"));
		}
		
		double minPrice = 0.0;
		if(Validation.notEmptyString(paramsMap.get("prod_min_price"))) {
			minPrice = Double.parseDouble(paramsMap.get("prod_min_price"));
		}
		
		product.setProductId(productId);
		product.setProductName(productName);
		product.setProductStatus(productStatus);
		product.setWarrantyPeriod(productWarranty);
		product.setProductDescription(productDescription);
		product.setCategoryId(categoryId);
		product.setListPrice(listPrice);
		product.setMinPrice(minPrice);
		
		return product;
	}
	
	/******************************************************************/
	// 고객 관련
	public String customerList(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int page = getIntParam(paramsMap, "page", 1);
		int searchby = getIntParam(paramsMap, "searchby", 0);
		String keyword = getStringParam(paramsMap, "keyword", "");
		
		String viewPage = "/manager/cust_list.jsp";
		
		// 리스트 헬퍼
		ListHelper<Customer> listHelper = custService.getList(page, searchby, keyword);
		
		// 결과 바인딩
		model.addAttribute("list_helper", listHelper);
		
		return viewPage;
	}
	
	// 고객 정보 보기
	public String getCustomer(Map<String, String> paramsMap, Model model) {
		// 파라미터
		int customerId = getIntParam(paramsMap, "id", 0);
		int page = getIntParam(paramsMap, "page", 1);
		int searchby = getIntParam(paramsMap, "searchby", 0);
		String keyword = getStringParam(paramsMap, "keyword", "");
		
		// 뷰 페이지
		String viewPage = new StringBuilder().append("/manager/cust_detail.jsp?page=").append(page)
				.append("&searchby=").append(searchby).append("&keyword=").append(keyword).toString();
		
		// 고객 정보 객체
		Customer customer = custService.get(customerId);
		
		// 결과 바인딩
		model.addAttribute("customer", customer);
		
		return viewPage;
	}
	
	// 고객 정보 삭제(강제 탈퇴): GET
	public String deleteCustomerForm(Map<String, String> paramsMap, Model model) {
		int customerId = getIntParam(paramsMap, "id", 0);
		return "/manager/forms/delete_cust_form.jsp?id=" + customerId;
	}
	
	// 고객 정보 삭제(강제 탈퇴): POST
	public String deleteCustomer(Map<String, String> paramsMap, Model model) {
		int customerId = getIntParam(paramsMap, "id", 0);
		String viewPage = ":redirect/manager/cust_list.do";
		
		// 해당 고객의 주문 정보를 삭제한다
		boolean	resultOrder = orderService.deleteByCustomerId(customerId);
				
		if(resultOrder) {
			// 성공하면 고객 정보를 삭제한다
			boolean resultCust = custService.delete(customerId);
			
			if(!resultCust) {
				// 실패한 경우 에러 페이지로 이동시킨다: 주문 정보는 삭제되었지만 고객 정보만 남는다
				viewPage = ":redirect/errors/manager_error.jsp?error_code=delete_cust_failure";
			}
		} else {
			// 실패하면 고객 탈퇴 절차를 중지(주문 정보 삭제도 롤백된 상태)
			viewPage = ":redirect/errors/manager_error.jsp?error_code=delete_cust_failure";
		}
		
		return viewPage;
	}
}
