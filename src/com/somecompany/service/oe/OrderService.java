package com.somecompany.service.oe;

import java.util.ArrayList;
import java.util.List;

import com.somecompany.model.oe.Order;
import com.somecompany.model.oe.OrderItem;
import com.somecompany.model.oe.Warehouse;
import com.somecompany.repository.dao.oe.OrderDao;
import com.somecompany.utils.ListHelper;
import com.somecompany.utils.Validation;

/**********************************************
 * OrderService
 * OrderDao를 이용해 요청된 정보를 처리하는 서비스 클래스
 * 
 * 싱글턴 패턴
 * 주의: employeePerPage는 다른 클래스에 의해 변경될 수 있음
***********************************************/

public class OrderService {
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 필드
	
	// 페이지 당 보여지는 주문 정보의 수(리스트)
	private static int orderPerPage = 10;
	
	// DAO 객체
	private static OrderDao orderDao;
	
	// 상수 필드	
	private static final int MIN_ORDER_STATUS = 0;
	private static final int MAX_ORDER_STATUS = 12;
	public static final short DEFAULT_ORDER_STATUS = -1;
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 생성자 및 인스턴스
	
	private static OrderService orderService = new OrderService();
	
	private OrderService() { }
	
	public static OrderService getInstance(String resource) {
		orderDao = OrderDao.getInstance(resource);
		return orderService;
	}
	
	public static OrderService getInstance(String resource, int perPage) {
		orderDao = OrderDao.getInstance(resource);
		orderPerPage = perPage;
		
		return orderService;
	}
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	

	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 서비스 메소드
	
	/* get: 해당 id의 주문 정보 조회 */
	public Order get(int orderId) {
		Order order = orderDao.get(orderId);
		order.resetOrderItems();
		return order;
	}
	
	
	/* getList: 주문 정보 리스트 조회 */
	public ListHelper<Order> getList(int page, short orderStatus) {
		
		// 파라미터 필터링: orderStatus, 범위를 벗어나면 default로 설정
		if(!Validation.checkIntegerSize(orderStatus, MIN_ORDER_STATUS, MAX_ORDER_STATUS))
			orderStatus = DEFAULT_ORDER_STATUS;
		
		// 총 주문 수 계산(조건에 맞게)
		int totalCount = 0;
		if(orderStatus == DEFAULT_ORDER_STATUS) {
			// orderStatus가 디폴트이면 전체 주문 수를 조회
			totalCount = orderDao.getCount();
		} else {
			// 아니라면 조건에 맞게 주문 수 조회
			totalCount = ((OrderDao) orderDao).getCountByOrderStatus(orderStatus);
		}
		
		// ListHelper 생성
		ListHelper<Order> listHelper = new ListHelper<Order>(totalCount, page, orderPerPage);
		
		// 리스트 가져오기
		List<Order> orders;
		if(orderStatus == DEFAULT_ORDER_STATUS) {
			orders = orderDao.getList(listHelper.getOffset(), orderPerPage);
		} else {
			orders = orderDao.getList(listHelper.getOffset(), orderPerPage, orderStatus);
		}
		
		// 리스트 설정
		listHelper.setList(orders);
		
		return listHelper;
	}
	
	
	/* getList: 주문 정보 리스트 조회(by customerId) */
	public ListHelper<Order> getList(int customerId, int page) {
		// 총 주문 수 계산
		int totalCount = orderDao.getCount(customerId);
		
		// ListHelper 생성
		ListHelper<Order> listHelper = new ListHelper<Order>(totalCount, page, 5);
				
		// 리스트 가져오기
		List<Order> orders = orderDao.getList(customerId, listHelper.getOffset(), 5);
		
		// 리스트 설정
		listHelper.setList(orders);
				
		return listHelper;
	}
	
	
	/* getMaxId() & getMinId() */
	public int getMaxId() {
		return orderDao.getMaxId();
	}
	
	public int getMinId() {
		return orderDao.getMinId();
	}

	
	/* add */
	public boolean add(Order order) {
		int result = 0;
		
		// 파라미터 필터링
		if(checkParameter(order)) {
			result = orderDao.add(order);
			
			if(result == order.getOrderItems().size()) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/* delete */
	public boolean delete(int orderId) {
		int result = 0;
		
		result = orderDao.delete(orderId);
		
		if(result == 1) {
			return true;
		}
		
		return false;
	}
	
	
	/* deleteByCustomerId */
	public boolean deleteByCustomerId(int customerId) {
		return orderDao.deleteOrderByCustomerId(customerId);
	}
	
	/************************************************************************/
	// 주문 상태 변경/선적 등
	
	/* cancelOrder: 주문 취소 */
	public boolean cancelOrder(int orderId, boolean byCustomer) {
		short orderStatus = orderDao.getOrderStatus(orderId);
		short newOrderStatus;
		int result = 0;
		
		if(byCustomer) {
			newOrderStatus = 3;
		} else {
			newOrderStatus = 2;
		}

		if(canCanceled(orderStatus)) {
			result = orderDao.updateOrderStatus(orderId, newOrderStatus);
		} else {
			return false;
		}
		
		if(result == 1)
			return true;
		else 
			return false;	
	}
	
	
	/* completeOrder: 배송까지 완료  */
	public boolean completeOrder(int orderId) {
		short orderStatus = orderDao.getOrderStatus(orderId);
		int result = 0;
		
		if(canComplete(orderStatus)) {
			result = orderDao.updateOrderStatus(orderId, (short) 12);
		} else {
			return false;
		}
		
		if(result == 1)
			return true;
		else 
			return false;
	}
	
	
	/* payOrder: 주문 결제 */
	public boolean payOrder(int orderId) {
		short orderStatus = orderDao.getOrderStatus(orderId);
		int result = 0;
		
		if(canPaid(orderStatus)) {
			result = orderDao.updateOrderStatus(orderId, (short) 11);
		} else {
			return false;
		}
		
		if(result == 1)
			return true;
		else 
			return false;
	}
	
	
	/* shipOrder: 주문 발송 */
	public boolean shipOrder(int orderId, short newOrderStatus, int warehouseId) {
		short orderStatus = orderDao.getOrderStatus(orderId);
		int result = 0;
		
		if(canShipped(orderStatus, newOrderStatus)) {
			result = orderDao.shipOrder(orderId, newOrderStatus, warehouseId);
		} else {
			return false;
		}
		
		if(result == 1)
			return true;
		else 
			return false;
	}
	
	
	/* changeOrderStatus: 관리자용 메서드, 주의! - 파라미터 체크가 없다 */
	public boolean changeOrderStatus(int orderId, short newOrderStatus) {
		int result = 0;
		
		result = orderDao.updateOrderStatus(orderId, newOrderStatus);
		
		if(result == 1)
			return true;
		else 
			return false;
	}
	
	/* getAvailableWarehouses: 출고 가능한 창고 목록 조회(항목 모두를 출고 가능한 창고만 포함된다) */
	public List<Warehouse> getAvailableWarehouses(List<OrderItem> items) {
		List<Warehouse> warehouses = new ArrayList<Warehouse>();
		List<Integer> ids = orderDao.getWarehouseIds();
		
		for(int warehouseId : ids) {
			int count = 0;
			
			for(OrderItem item : items) {
				long quantityOnHand = 0;
				if(orderDao.getCountQty(warehouseId, item.getProductId()) != 0) {
					quantityOnHand = orderDao.getQunatityOnHand(warehouseId, item.getProductId());
				}
				
				if(item.getQuantity() <= quantityOnHand)
					count++;
			}
			
			if(count == items.size()) {
				Warehouse warehouse = orderDao.getWarehouses(warehouseId);
				warehouses.add(warehouse);
			}
		}
		
		return warehouses;
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	// 주문 상태 변경 가능 체크 함수들
	
	// 선적 가능 여부
	private boolean canShipped(short oldOrderStatus,  short newOrderStatus) {
		// 주문 상태가 11(결제완료)인 경우만. 4~10까지 변경됨.
		if((oldOrderStatus == 11 && 
				(newOrderStatus >= 4 && newOrderStatus <= 10))) {
			return true;
		}
		
		return false;
	}
		
	// 주문 취소 가능 여부
	private boolean canCanceled(short oldOrderStatus) {
		// 0: 주문대기, 1: 결제대기, 11: 결제완료	
		switch(oldOrderStatus) {
		case 0:	case 1: case 11:
				return true;
		default:
			return false;
		}
	}
	
	// 결제 완료 여부
	private boolean canPaid(short oldOrderStatus) {
		if(oldOrderStatus == 1) {
			return true;
		}
		
		return false;
	}
	
	// 주문 처리 완료 여부
	private boolean canComplete(short oldOrderStatus) {
		if((oldOrderStatus >= 4 && oldOrderStatus <= 11)) {
			return true;
		}
		
		return false;
	}

	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 필터링 함수
	public boolean checkParameter(Order order) {
		if(Validation.notNullObject(order)) {
			
			/****************** not null 칼럼 ******************/
			
			/***** orders *****/
			// order_id: 없음. OrderDao.add()에서 자동으로 처리
			
			// order_date: 없음. SYSDATE가 들어간다
			
			// customer_id: 없음. customers에 없는 id인 경우 SQLException 발생
			
			/***** order_items *****/
			
			for(OrderItem item : order.getOrderItems()) {
				// not null 칼럼
				
				// order_id: 없음. OrderDao.add()에서 자동으로 처리
				// line_item_id: 없음. OrderDao.add()에서 자동으로 처리
				// product_id: 없음. product_inforamtion에 없는 id인 경우  SQLException 발생
				
				// null 허용 칼럼
				// uint_price
				if(item.getUnitPrice() < 0)
					item.setUnitPrice(0);
				
				// quantity
				item.setQuantity(Validation.ProcrustesBedHead(item.getQuantity(), 0));
			}
			
			/****************** null 허용 칼럼 ******************/
			
			// order_mode: 8byte, direct/online
			if(Validation.checkStringLength(order.getOrderMode(), 0, 8)) {
				if(!order.getOrderMode().equals("direct") && !order.getOrderMode().equals("online"))
				order.setOrderMode("online");
			} else {
				order.setOrderMode("online");
			}
			
			// order_status(0~10)
			if(!Validation.checkIntegerSize(order.getOrderStatus(), MIN_ORDER_STATUS, MAX_ORDER_STATUS)) {
					order.setOrderStatus((short) 0);
				}
			
			// order_total
			if(order.getOrderTotal() < 0)
				order.setOrderTotal(0);
			
			return true;
			
		}
		
		return false;
	}
}
