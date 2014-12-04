package com.somecompany.service.oe;

import java.util.List;

import javax.sql.DataSource;

import com.somecompany.model.oe.Inventory;
import com.somecompany.model.oe.Product;
import com.somecompany.model.oe.Warehouse;
import com.somecompany.repository.dao.oe.CategoryDao;
import com.somecompany.repository.dao.oe.ProductDao;
import com.somecompany.utils.ListHelper;
import com.somecompany.utils.Validation;

/**********************************************
 * ProductService
 * ProductDao를 이용해 요청된 정보를 처리하는 서비스 클래스
 * 
 * 싱글턴 패턴
 * 주의: employeePerPage는 다른 클래스에 의해 변경될 수 있음
***********************************************/

public class ProductService {
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	// 필드
	
	// DAO 객체
	private static ProductDao productDao;
	
	// 상수
	public static final int PRODUCT_ID = 1;
	public static final int LIST_PRICE = 2;
	public static final int LIST_PRICE_DESC = 3;
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// 생성자 및 인스턴스
	
	private static ProductService prodService = new ProductService();
	
	private ProductService() { }
	
	public static ProductService getInstance(DataSource dataSource) {
		productDao = ProductDao.getInstance(dataSource);
		return prodService;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	// 서비스 메소드
	
	/* get */
	public Product get(int productId) {
		// 제품 기본 정보 얻기
		Product product = (Product) productDao.get(productId);
		
		// 제품 재고 정보 리스트 얻기
		List<Inventory> inventories = productDao.getInventories(productId);	// 재고 정보를 캐스팅해서 담을 리스트
		
		product.setInventories(inventories);	// 재고 리스트 설정
		
		return product;
	}
	
	/* getList*/
	public ListHelper<Product> getList(int page, short orderby, int categoryId, int productPerPage, boolean orderable) {
		// 총 주문 수 계산
		int totalCount = 0;
		totalCount = productDao.getCount(orderable, categoryId);
		
		// ListHelper 생성
		ListHelper<Product> listHelper = new ListHelper<Product>(totalCount, page, productPerPage);
		
		// product list 가져오기
		List<Product> prodList;
		
		// 전달된 파라미터가 정상이라면 정렬 조건에 따라 출력
		prodList = productDao.getList(
				listHelper.getOffset(), listHelper.getObjectPerPage(), 
				orderby, orderable, categoryId);
		
		// 리스트 설정
		listHelper.setList(prodList);
		
		return listHelper;
	}
	
	
	/* getWarehouseList: 창고 목록 조회 */ 
	public List<Warehouse> getWarehouseList() {
		return productDao.getWarehouseList();
	}
	
	
	/* getInventoryInWarehouse: 특정 창고 안의 재고 목록 보기 */
	public ListHelper<Inventory> getInventoryInWarehouse(int warehouseId, int page) {
		// 총 주문 수 계산
		int totalCount = 0;
		totalCount = productDao.getCountInWarehouse(warehouseId);
		
		// ListHelper 생성
		ListHelper<Inventory> listHelper = new ListHelper<Inventory>(totalCount, page, 10);
		
		List<Inventory> invList = productDao.getInventoriesInWarehouse(
				warehouseId, listHelper.getOffset(), 10);
		
		listHelper.setList(invList);
		
		return listHelper;		
	}
	
	
	public List<Inventory> getInventory(int productId) {
		return productDao.getInventories(productId);
	}
	
	
	///////////////////////////////////////////////////////////////
	// 기타 조회 메소드
	public boolean isSoldOut(int productId) {
		return productDao.isSoldOut(productId);
	}
	
	public long getQunatitySum(int productId) {
		return productDao.getQunatityOnHandSum(productId);
	}
	
	///////////////////////////////////////////////////////////////
	// 갱신 메소드
	
	/* add */
	public boolean add(Product product) {
		int result = 0;
		
		if(checkParameter(product))
			result = productDao.add(product);
		
		if(result == 1) {
			return true;
		}
		
		return false;
	}

	
	/* delete */
	public boolean delete(int productId) {
		int maxId = productDao.getMaxId();
		int minId = productDao.getMinId();
		
		if(productId > maxId || productId < minId)
			return false;
		
		int result = 0;
		
		result = productDao.delete(productId);
		
		if(result == 1) {
			return true;
		}
		
		return false;
	}

	
	/* update */
	public boolean update(Product product) {
		int result = 0;
		
		if(checkUpdateParameter(product))
			result = productDao.update(product);
		
		if(result == 1) {
			return true;
		}
		
		return false;
	}
	
	
	/* updateProductPrice: 가격 설정 */
	public boolean updateProductPrice(int productId, double listPrice) {
		
		int result = productDao.updateListPrice(productId, listPrice);
		
		if(result == 1) {
			return true;
		}
		
		return false;
	}
	
	
	/* stockAndWarehousing: 재고 관리 */
	// quantity는 증감 수량: +는 입고, -는 출고
	public boolean stockAndWarehousing(int productId, int warehouseId, long quantity) {
		boolean isRegisterInInventories = productDao.isRegisterInInventories(productId, warehouseId);
		
		// 현재 재고 수량: 재고 목록에 없으면 0
		long quantityOnHand = 0; 
		
		if(isRegisterInInventories)
			quantityOnHand = productDao.getQunatityOnHand(warehouseId, productId);
		
		// 최종 수량
		long quantityRevised = quantityOnHand + quantity;
		
		// 수정된 수량이 0보다 작으면 오류: 리턴
		if(quantityRevised < 0)
			return false;
		
		int result = 0;
		if(!isRegisterInInventories) {
			// 등록되지 않은 재고이면, 새로 등록해준다
			result = productDao.stock(productId, warehouseId, quantityRevised);
		} else {
			// 이미 존재하는 항목이면 수정
			result = productDao.warehousing(productId, warehouseId, quantityRevised);
		}
		
		if(result == 1) {
			return true;
		}
		
		return false;
	}	
	
	//////////////////////////////////////////////////////////////////////////////////////
	
	/* getMaxId */
	public int getMaxId() {
		return productDao.getMaxId();
	}
	
	/* getMinId */
	public int getMinId() {
		return productDao.getMinId();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	// 필터링 메소드
	
	/* checkParameter: 상품 정보 추가를 위한 필터링 메소드 
	 * 5개의 칼럼 필터링 */
	public boolean checkParameter(Product product) {
		
		if(Validation.notNullObject(product)) {
			
			// 어플리케이션 차원에서의 필수 칼럼들을 체크한다
			// 1. product_id: not null. 필터링 안 함. dao에서 처리
			
			// 2. product_name: 50byte(어플리케이션 제약: 2byte ~ 50byte)
			if(!Validation.checkStringLength(product.getProductName(), 2, 50))
				return false;
			
			// 3. product_status
			// 총 네 가지 상태 가능: orderable, planned, under development, obsolete
			String prodStatus = product.getProductStatus();
			if(Validation.notNullString(prodStatus)) {
				if(!prodStatus.equals("orderable") && !prodStatus.equals("planned") && 
						!prodStatus.equals("under development") && !prodStatus.equals("obsolete")) {
					return false;
				}
			} else {
				return false;
			}
			
			// 4. min_price: 0보다 크다
			if(product.getMinPrice() < 0)
				product.setMinPrice(0);
			
			// 5. category_id: categories_tab에 없는 id인 경우 false 리턴
			int catCount = CategoryDao.getCountByCategoryId(product.getCategoryId());
			if(catCount == 0)
				return false;
			
			// 여기까지 모두 통과하면 true를 리턴
			return true;
		}
		
		return false;	// null 파라미터: false 리턴
	}
	
	
	/* checkItemUpdateParameter: update를 위한 필터링 메소드 */
	public boolean checkUpdateParameter(Product product) {
		if(!checkParameter(product)) {
			return false;
		}
		
		///////// 부가 칼럼들 검증 /////////
		
		// product_description: 2000byte(null 허용)
		if(product.getProductDescription() != null) {
			if(!Validation.checkStringLength(product.getProductDescription(), 0, 2000))
				return false;
		} else {
			product.setProductDescription("");
		}
				
		
		// warranty_period: 패턴 검증, ?-? 형태
		if(product.getWarrantyPeriod() != null) {
			// 정규표현식과 일치하지 않으면 빈문자열로 설정
			// 정규표현식: \d-\d ---> digit(2자리)-digit(1자리)
			if(!product.getWarrantyPeriod().matches("\\d{1,2}-\\d{1}"))
				product.setWarrantyPeriod("");
		} else {
			product.setWarrantyPeriod("");
		}
		
		// list_price
		Product prodCheck = (Product) productDao.get(product.getProductId());
		if(prodCheck != null) {
			if(prodCheck.getMinPrice() > product.getListPrice())
				// min_price보다 작은 경우
				product.setListPrice(prodCheck.getMinPrice());	// min_price로 설정
		} else {
			return false;
		}
		
		// weight_class: 0이면 통과
		if(product.getWeightClass() != 0) {
			// 0이 아닌 경우는 1자리 숫자인 경우만 통과(1~9)
			if(product.getWeightClass() > 9 || product.getWeightClass() < 1 )
				return false;
		}
		
		// supplier_id: ?
		
		return true;
	}
}
