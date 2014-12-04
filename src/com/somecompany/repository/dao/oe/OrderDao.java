package com.somecompany.repository.dao.oe;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import com.somecompany.model.oe.Order;
import com.somecompany.model.oe.OrderItem;
import com.somecompany.model.oe.Warehouse;
import com.somecompany.repository.dao.SessionGetter;
import com.somecompany.repository.dao.templates.SqlSessionDaoTemplate;

/**************************************
 * OrderDao
 * oe.orders와 oe.order_items를 
 * 주 테이블로 관리하는 DAO 클래스
 * MyBatis 3.2.4 적용
 * 
 * 템플릿/콜백 패턴 사용: SqlSessionDaoTemplate
 * singleton pattern
 * 
 * SqlSession의 안전한 closing을 위해
***************************************/

public class OrderDao {
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 일반 프로퍼티
	
	// sessionGetter
	private static SessionGetter sessionGetter = new SessionGetter();
	
	// 마이바티스가 적용된 Dao를 위한 템플릿
	private static SqlSessionDaoTemplate sqlTemplate = new SqlSessionDaoTemplate();
	
	// 새 세션 연결을 위한 래핑 함수
	public void openSqlSession() {
		sqlTemplate.setSqlSession(sessionGetter.openNewSqlSession());
	}
	
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 생성자 & getInstance
	private static OrderDao orderDao = new OrderDao();
	
	private OrderDao() { }

	public static OrderDao getInstance(String resource) {
		sessionGetter.setSqlSessionFactory(resource);
		return orderDao;
	}
	
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 상수 필드

	// MyBatis xml mapper 네임스페이스
	private final String NAMESPACE = 
			"com.somecompany.repository.mapper.OrderMapper";
	
	
	
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// 쿼리 메소드

	/**************************************************************
	 ********************** 조회 메소드(Read) *************************
	 **************************************************************/
	
	/* getCount: 전체 데이터 레코드 수 조회 */
	public int getCount() {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectCount";
		
		return sqlTemplate.queryForInt(sql);
	}
	
	
	/* getCount: 해당 customerId의 주문 레코드 수 조회 */
	public int getCount(int customerId) {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectCountByCustomerId";
		
		return sqlTemplate.queryForInt(sql, customerId);
	}
	
	
	/* getCountByOrderStatus: 주문 상태에 따른 주문 수 조회 */
	public int getCountByOrderStatus(short orderStatus) {
		openSqlSession();	// 새 세션 열기
		
		String sql = NAMESPACE + ".selectCountByOrderStatus";
		
		return sqlTemplate.queryForInt(sql, orderStatus);
	}
	
	/* get: orderId 해당하는 레코드 조회 */
	public Order get(int orderId) {
		
		String sqlOrder = NAMESPACE + ".selectOrder";
		String sqlItems = NAMESPACE + ".selectOrderItems";
		
		openSqlSession();	// 새 세션 열기
		
		Order order = null;
		order = sqlTemplate.query(sqlOrder, orderId);
		
		if(order != null) {
			openSqlSession();	// 새 세션 열기			
			List<OrderItem> orderItems = sqlTemplate.queryForList(sqlItems, order.getOrderId());
			order.setOrderItems(orderItems);
		}
		
		return order;
	}
	
	
	/* getList: 주문 정보 리스트 조회 */
	public List<Order> getList(int offset, int limit) {
		openSqlSession();	// 새 세션 열기
		
		String sql = NAMESPACE + ".selectListAll";
		final RowBounds bounds = new RowBounds(offset, limit);
		
		return sqlTemplate.queryForList(sql, null, bounds);
	}
	
	
	/* getList: 주문 정보 리스트 조회(by customerId) */
	public List<Order> getList(int customerId, int offset, int limit) {
		openSqlSession();	// 새 세션 열기
		
		String sql = NAMESPACE + ".selectListByCustomerId";
		final RowBounds bounds = new RowBounds(offset, limit);
		
		return sqlTemplate.queryForList(sql, customerId, bounds);
	}
	
	
	/* getList: 주문 정보 리스트 조회(필터링) */
	public List<Order> getList(int offset, int limit, short orderStatus) {
		openSqlSession();	// 새 세션 열기
		
		String sql = NAMESPACE + ".selectList";
		final RowBounds bounds = new RowBounds(offset, limit);
		
		return sqlTemplate.queryForList(sql, orderStatus, bounds);
	}
	
	
	/* getMaxId: 주문 번호 최대값 구하기 */
	public int getMaxId() {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectMaxId";

		return sqlTemplate.queryForInt(sql);
	}
	
	
	/* getMaxId: 주문 번호 최소값 구하기 */
	public int getMinId() {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectMinId";

		return sqlTemplate.queryForInt(sql);
	}
	
	
	/* getOrderStatus: 주문 상태 가져오기 */
	public short getOrderStatus(int orderId) {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectOrderStatus";
		
		return sqlTemplate.query(sql, orderId);
	}
	
	/* getWarehouses: 출고 가능한 창고 번호 조회 */
	public Warehouse getWarehouses(int warehouseId) {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectWarehouses";
		
		return sqlTemplate.query(sql, warehouseId);
	}
	
	
	public List<Integer> getWarehouseIds() {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectWarehouseIds";
		
		return sqlTemplate.queryForList(sql);
	}
	
	
	/* getQunatityOnHand: 특정 창고에 있는 제품의 수량 조회 */
	public long getQunatityOnHand(int warehouseId, int productId) {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectQuantity";
		
		// 파라미터 생성
		Warehouse param = new Warehouse();
		param.setWarehouseId(warehouseId);
		param.setProductId(productId);		
		
		return sqlTemplate.query(sql, param);
	}
	
	public int getCountQty(int warehouseId, int productId) {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectQuantityCount";
		
		// 파라미터 생성
		Warehouse param = new Warehouse();
		param.setWarehouseId(warehouseId);
		param.setProductId(productId);		
		
		return sqlTemplate.query(sql, param);
	}
	
	
	/* getOrderIds: customerId에 해당하는 주문 번호 조회 */
	public List<Integer> getOrderIds(int customerId) {
		openSqlSession();	// 새 세션 열기
		String sql = NAMESPACE + ".selectOrderIdByCustomerId";
		
		return sqlTemplate.queryForList(sql, customerId);
	}
	
	
	/**************************************************************
	 ************* 갱신 메소드(Create, Update, Delete) ****************
	 ******************* 트랜잭션을 위해 템플릿 미적용 **********************
	 **************************************************************/
	
	/* add: 새로운 주문 정보 추가 */
	public int add(Order order) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			int orderId = getMaxId() + 1;
			order.setOrderId(orderId);
			
			String sqlOrder = NAMESPACE + ".insertOrder";
			String sqlOrderItem = NAMESPACE + ".insertOrderItem";
			
			// 주문 본 정보 추가
			int resultOrder = sqlSession.insert(sqlOrder, order);
			
			if(resultOrder > 0) {
				// 주문이 추가되었다면 주문 항목 추가
				int resultCount = 0;	// 몇 개의 항목이 추가되었는지를 센다
				int idx = 1;
				List<OrderItem> items = order.getOrderItems();
				for(OrderItem item : items) {
					// items에 들어 있는 모든 항목을 순회하면서 추가
					item.setLineItemId(idx++);
					int resultItem = sqlSession.insert(sqlOrderItem, item);
					
					if(resultItem > 0) {
						// 항목이 추가될 때마다 카운터를 증가
						resultCount += resultItem;
					}
				}
				
				if(resultCount == items.size()) {
					// 추가된 항목 수와 항목 리스트의 항목 수가 같으면 커밋
					sqlSession.commit();
					return resultCount;
				} else {
					// 수가 다르면 롤백
					sqlSession.rollback();
					return 0;
				}
			} else {
				sqlSession.rollback();
				return 0;
			}
		} finally {
			sqlSession.close();
		}
		
	}
	
	
	/* delete: 주문 삭제 */
	public int delete(final int orderId) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sqlOrder = NAMESPACE + ".deleteOrder";
			String sqlOrderItems = NAMESPACE + ".deleteOrderItems";
			int result = 0;
			
			openSqlSession();	// 새 세션 열기
			Order order = get(orderId);	
			
			if(order != null) {
				openSqlSession();	// 새 세션 열기
				int itemCount = order.getOrderItems().size();	// 항목 수
				
				// 주문의 모든 항목을 삭제하고, 삭제 개수를 얻어온다
				// 트랜잭션 commit은 주문 정보가 삭제될 때까지 하지 않아야 한다.
				int ItemsResult = sqlSession.delete(sqlOrderItems, order.getOrderId());
				
				if(ItemsResult == itemCount) {
					// 항목 수와 삭제 수가 일치한다면 주문 정보를 삭제한다.
					result = sqlSession.delete(sqlOrder, order.getOrderId());
					if(result == 1) {
						// 삭제되는 주문은 반드시 1개여야만 한다. 1개라면 커밋
						sqlSession.commit();
						
					} else {
						// 1개 이상이 삭제되는 경우 뭔가 문제가 있는 것이다. 롤백한다.
						sqlSession.rollback();		// 삭제된 레코드 수가 리턴됨(롤백됨)
					}
				} else {
					// 일치하지 않으면 롤백
					sqlSession.rollback();	// 0이 리턴됨(아무 작업도 하지 않음)
				}
			}
			
			return result;
		} finally {
			sqlSession.close();
		}
		
	}
	
	
	/* deleteOrderByCustomerId: 고객 회원 탈퇴 시 주문 정보 삭제  */
	public boolean deleteOrderByCustomerId(int customerId) {
		List<Integer> orderIds = getOrderIds(customerId);
		int count = 0;
		
		for(int id : orderIds) {
			if(delete(id) == 1)
				count++;
		}
		
		if(count == orderIds.size())
			return true;
		
		return false;
	}
	
	
	/* deleteOrderItem: 주문 항목 1개 삭제 */
	public int deleteOrderItem(final OrderItem orderItem) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".deleteOrderItemsByLineItemId";
			
			int result = sqlSession.delete(sql, orderItem);
			
			if(result == 1) {
				// 1개의 레코드만 삭제되어야 정상. 커밋
				sqlSession.commit();
			} else {
				// 그 외는 롤백
				sqlSession.rollback();
			}
			
			return result;
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* updateOrder: 기본 주문 정보 변경 */
	public int update(Order order) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".updateOrder";
			int result = sqlSession.update(sql, order);
			
			if(result == 1) {
				// 1개의 레코드만 변경되어야 정상. 커밋
				sqlSession.commit();
			} else {
				// 그 외는 롤백
				sqlSession.rollback();
			}
			
			return result;
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* updateOrderStatus: 주문 상태 변경 */
	public int updateOrderStatus(int orderId, short orderStatus) {
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		try {
			String sql = NAMESPACE + ".updateOrderStatus";
						
			Order order = new Order();
			
			order.setOrderId(orderId);
			order.setOrderStatus(orderStatus);
			
			int result = sqlSession.update(sql, order);
			
			if(result == 1) {
				// 변경되는 주문은 반드시 1개여야만 한다.
				sqlSession.commit();
				return result;
			} else {
				// 1개 이상이 변경되는 경우 뭔가 문제가 있는 것이다. 롤백한다.
				sqlSession.rollback();
			}
			
			return 0;
		} finally {
			sqlSession.close();
		}
	}
	
	
	
	/* shipOrder: 주문 선적 처리 */
	public int shipOrder(int orderId, short orderStatus, int warehouseId) {
		// 선적 처리시 재고 변경도 동반됨.
		SqlSession sqlSession = sessionGetter.openNewSqlSession();
		
		int resultCount = 0;
		
		Order order = get(orderId);
		order.setOrderStatus(orderStatus);
		order.resetOrderItems();
		List<OrderItem> items = order.getOrderItems();	// 주문 항목 리스트
		
		String sql = NAMESPACE + ".updateOrderStatus";
		
		order.setOrderStatus(orderStatus);	// update에 넘기기 전에 orderStatus를 새 상태 번호로 변경한다
		
		try {
			int resultStatus = sqlSession.update(sql, order);
				
			if(resultStatus == 1) {
				// 변경되는(선적되는) 주문은 반드시 1개여야만 한다.
				int size = items.size();
				
				for(OrderItem item: items) {
					
					// 재고 수량 변경
					int resultItem = setInventoryQty(sqlSession, item, warehouseId);
					
					if(resultItem > 0) {
						// 업데이트 성공시 카운트 증가
						resultCount++;
					}
				}
					
				if(resultCount == size) {
					sqlSession.commit();
					return resultStatus;		// 정상적으로 변경된 정보의 수를 리턴
				} else {
					// 일치하지 않으면 롤백
					sqlSession.rollback();
					return 0;	// 롤백: 0 리턴
				}				
			} else {
				// 1개 이상의 주문이 변경되거나 아무것도 변경되지 않는다면 롤백
				sqlSession.rollback();
				return 0;	// 롤백: 0 리턴
			}
			
		} finally {
			sqlSession.close();
		}
	}
	
	
	/* setInventoryQty: 선적 시 재고 수정 처리 */
	private int setInventoryQty(SqlSession sqlSession, OrderItem item, int warehouseId) {
		String sql = NAMESPACE + ".updateInventoryQty";
		int result = 0;
		
		// quantity
		long qtyOnHand = getQunatityOnHand(warehouseId, item.getProductId());	// 재고 수량
		long orderQty = item.getQuantity();										// 주문 수량
		
		// 재고 수량 수정(재고 수량 - 주문 수량)
		long revisedQty = qtyOnHand - orderQty;
		
		if(revisedQty >= 0) {
			// 조정된 수량이 0 이상인 경우 쿼리 실행
			Warehouse param = new Warehouse();	// inventory 업데이트를 위한 파라미터
			
			// 파라미터 설정
			param.setProductId(item.getProductId());
			param.setWarehouseId(warehouseId);
			param.setQuantity(revisedQty);
			
			// 쿼리 실행
			result = sqlSession.update(sql, param);
		}
		
		return result;
	}
}
