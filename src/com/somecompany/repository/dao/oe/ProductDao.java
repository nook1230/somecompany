package com.somecompany.repository.dao.oe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.somecompany.model.oe.Inventory;
import com.somecompany.model.oe.Product;
import com.somecompany.model.oe.Warehouse;
import com.somecompany.repository.dao.oe.callbacks.PreparedStatementCreator;
import com.somecompany.repository.dao.oe.callbacks.RowMapper;
import com.somecompany.repository.dao.templates.DataSourceUtil;
import com.somecompany.repository.dao.templates.JdbcDaoTemplate;
import com.somecompany.repository.dao.templates.TransactionSyncManager;
import com.somecompany.repository.exceptions.DataAccessException;

/**************************************
 * ProductDao
 * 
 * [주요 관리 테이블]
 * oe.product_information
 * oe.product_description
 * oe.inventories
 * 
 * 템플릿/콜백 패턴 사용: JdbcDaoTemplate
 * - 자체 제작한 JdbcTemplate(스프링 참고)
 * 
 * singleton pattern
 * 리소스 관리 기능과 편의 메소드 제공
***************************************/

public class ProductDao {
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	// 필드: 템플릿
	private static JdbcDaoTemplate template = new JdbcDaoTemplate();
	
	private static DataSource dataSource;
	
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	// 생성자 & getInstance
	
	private static ProductDao productDao = new ProductDao();
	
	private ProductDao() { }
	
	public static ProductDao getInstance(DataSource ds) {
		dataSource = ds;
		template.setDataSource(dataSource);
		return productDao;
	}
	
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	// 상수
	public static final int PRODUCT_ID = 1;
	public static final int LIST_PRICE = 2;
	public static final int LIST_PRICE_DESC = 3;
	
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	// 쿼리 메소드
	
	/**************************************************************
	 ********************** 조회 메소드(Read) *************************
	 **************************************************************/
	
	/* getCount: product_information의 전체 레코드 수 조회  */
	public int getCount() {
		String sql = "SELECT COUNT(*) FROM product_information";
		return template.queryForInt(sql);
	}
	
	public int getCount(boolean orderable, int categoryId) {
		String sql = "SELECT COUNT(*) FROM product_information WHERE product_status = 'orderable'";
		
		if(categoryId != -1) {
			sql += (" AND category_id = " + categoryId);
		}
		
		return template.queryForInt(sql);
	}
	
	
	/* getMaxId/getMinId: product_information의 가장 큰/작은 product_id 조회  */
	public int getMaxId() {
		String sql = "SELECT MAX(product_id) FROM product_information";
		return template.queryForInt(sql);
	}
	
	public int getMinId() {
		String sql = "SELECT MIN(product_id) FROM product_information";
		return template.queryForInt(sql);
	}
	
	
	/* get: 제품에 대한 기본 정보 조회  */
	public Product get(final int productId) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT pi.product_id, pi.product_name, pi.product_description, ")
		.append("pi.category_id, pi.warranty_period, pi.supplier_id, ")
		.append("pi.product_status, pi.list_price, pi.min_price, ")
		.append("pi.catalog_url, c.category_name, c.category_description, ")
		.append("SUM(inv.quantity_on_hand) qty ")
		.append("FROM product_information pi, categories_tab c, inventories inv ")
		.append("WHERE pi.category_id = c.category_id ")
		.append("AND pi.product_id = inv.product_id (+) ")
		.append("AND pi.product_id = ?")
		.append("GROUP BY pi.product_id, pi.product_name, pi.product_description, ")
		.append("pi.category_id, pi.warranty_period, pi.supplier_id, ")
		.append("pi.product_status, pi.list_price, pi.min_price, ")
		.append("pi.catalog_url, c.category_name, c.category_description");
		
		final String sql = sqlBuilder.toString();
				
		return (Product) template.query(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setInt(1, productId);
				return ps;
			}
		}, productMapper);	
	}
	
	
	/* getList: 제품의 리스트 조회  */
	public List<Product> getList(int offset, int limit) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT pi.product_id, pi.product_name, ")
		.append("pi.list_price, pi.category_id, cat.category_name, ")
		.append("SUM(inv.quantity_on_hand) qty ")
		.append("FROM product_information pi, categories_tab cat, inventories inv ")
		.append("WHERE pi.category_id = cat.category_id ")
		.append("AND pi.product_id = inv.product_id (+) ")
		.append("GROUP BY pi.product_id, pi.product_name, ")
		.append("pi.list_price, pi.category_id, cat.category_name ")
		.append("ORDER BY pi.product_id DESC");
		
		String sql = sqlBuilder.toString();
		
		return template.queryForList(sql, offset, limit, productListMapper);
	}
	
	
	/* getList: 제품의 리스트 조회  */
	public List<Product> getList(int offset, int limit, int orderby, boolean orderable, int categoryId) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT pi.product_id, pi.product_name, pi.product_status, ")
		.append("pi.list_price, pi.min_price, pi.category_id, cat.category_name, ")
		.append("SUM(inv.quantity_on_hand) qty ")
		.append("FROM product_information pi, categories_tab cat, inventories inv ")
		.append("WHERE pi.category_id = cat.category_id ")
		.append("AND pi.product_id = inv.product_id (+) ");
		
		if(orderable) {
			sqlBuilder.append(" AND pi.product_status = 'orderable'");
		}
		
		if(categoryId != -1) {
			// PreparedStatement 사용 대신 직접 파라미터를 연결
			sqlBuilder.append(" AND pi.category_id = ").append(categoryId);
		}
		
		sqlBuilder.append(" GROUP BY pi.product_id, pi.product_name, pi.product_status, ")
		.append("pi.list_price, pi.min_price, pi.category_id, cat.category_name");
		
		// 정렬 조건
		switch(orderby) {
		case LIST_PRICE:	// 가격 순서 
			sqlBuilder.append(" ORDER BY pi.list_price"); 
			break;
			
		case LIST_PRICE_DESC:		// 높은 가격 순서 
			sqlBuilder.append(" ORDER BY pi.list_price DESC"); 
			break;
			
		case PRODUCT_ID: 	// id 순서(디폴트)
		default: 
			sqlBuilder.append(" ORDER BY pi.product_id DESC");
		}
		
		final String sql = sqlBuilder.toString();
		
		return template.queryForList(sql, offset, limit, productListMapper);
	}
	
	
	/* isSoldOut: 재고 유무 파악 */
	public boolean isSoldOut(final int productId) {
		final String sql = 
				"SELECT SUM(quantity_on_hand) FROM inventories WHERE product_id = ?";
		int quantitySum = template.queryForInt(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setInt(1, productId);
				return ps;
			}
		});
		
		if(quantitySum == 0)
			return true;
		else
			return false;
	}
	
	
	/* isRegisterInInventories: 재고 정보 등록 여부 */
	public boolean isRegisterInInventories(final int productId, final int warehouseId) {
		final String sql = "SELECT COUNT(*) FROM inventories WHERE product_id = ? AND warehouse_id = ?";
		int count = template.queryForInt(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setInt(1, productId);
				ps.setInt(2, warehouseId);
				return ps;
			}
		});
		
		if(count == 0)
			return false;
		else
			return true;
	}
	
	
	/* getQunatityOnHand: 특정 창고에 있는 제품의 수량 조회 */
	public long getQunatityOnHand(final int warehouseId, final int productId) {
		final String sql = 
				"SELECT quantity_on_hand FROM inventories WHERE product_id = ? AND warehouse_id = ?";
		
		return template.queryForLong(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setInt(1, productId);
				ps.setInt(2, warehouseId);
				
				return ps;
			}
		});
	}
	
	
	/* getQunatityOnHand: 제품의 총 수량 조회 */
	public long getQunatityOnHandSum(final int productId) {
		final String sql = 
				"SELECT SUM(quantity_on_hand) FROM inventories WHERE product_id = ?";
		
		return template.queryForLong(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setInt(1, productId);
				
				return ps;
			}
		});
	}
	
	/* getInventories: 해당 제품의 재고 정보 조회 */
	public List<Inventory> getInventories(int productId) {
		StringBuilder sqlBuilder = new StringBuilder();
		
		sqlBuilder
		.append("SELECT i.product_id, pi.product_name, i.warehouse_id, w.warehouse_name, ")
		.append("i.quantity_on_hand, w.location_id ")
		.append("FROM inventories i, warehouses w, product_information pi ")
		.append("WHERE i.warehouse_id = w.warehouse_id ")
		.append("AND i.product_id = pi.product_id ")
		.append("AND i.product_id = ?");
		
		String sql = sqlBuilder.toString();
		return template.queryForList(sql, productId, new RowMapper<Inventory>() {
			
			@Override
			public Inventory mapRow(ResultSet rs, int rowNum) throws SQLException {
				Inventory inventory = new Inventory();
				
				inventory.setProductId(rs.getInt("product_id"));
				inventory.setProductName(rs.getString("product_name"));
				inventory.setWarehouseId(rs.getInt("warehouse_id"));
				inventory.setWarehouseName(rs.getString("warehouse_name"));
				inventory.setQuantityOnHand(rs.getLong("quantity_on_hand"));
				inventory.setLocationId(rs.getInt("location_id"));
				return inventory;
			}
		});
	}
	
	
	/* getWarehouseList: 창고 목록 조회 */
	public List<Warehouse> getWarehouseList() {
		String sql = "SELECT * FROM warehouses";
		
		return template.queryForList(sql, new RowMapper<Warehouse>() {

			@Override
			public Warehouse mapRow(ResultSet rs, int rowNum) throws SQLException {
				Warehouse warehouse = new Warehouse();
				warehouse.setWarehouseId(rs.getInt("warehouse_id"));
				warehouse.setWarehouseName(rs.getString("warehouse_name"));
				warehouse.setLocationId(rs.getInt("location_id"));
				
				return warehouse;
			}
		});
	}
	
	
	/* getCountInWarehouse */
	public int getCountInWarehouse(final int warehouseId) {
		final String sql = "SELECT COUNT(*) FROM inventories WHERE warehouse_id = ?";
		return template.queryForInt(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setInt(1, warehouseId);
				return ps;
			}
		});
	}
	
	/* getInventoriesInWarehouse: */
	public List<Inventory> getInventoriesInWarehouse(int warehouseId, int offset, int limit) {
		StringBuilder sqlBuilder = new StringBuilder();
		
		sqlBuilder.append("SELECT w.warehouse_id, w.warehouse_name, ")
		.append("inv.product_id, pi.product_name, inv.quantity_on_hand ")
		.append("FROM inventories inv, product_information pi, warehouses w ")
		.append("WHERE inv.product_id = pi.product_id ")
		.append("AND inv.warehouse_id = w.warehouse_id ")
		.append("AND inv.warehouse_id = ? ORDER BY inv.product_id");

		return template.queryForList(sqlBuilder.toString(), warehouseId, offset, limit, new RowMapper<Inventory>() {

			@Override
			public Inventory mapRow(ResultSet rs, int rowNum) throws SQLException {
				Inventory inventory = new Inventory();
				inventory.setWarehouseId(rs.getInt("warehouse_id"));
				inventory.setWarehouseName(rs.getString("warehouse_name"));
				inventory.setProductId(rs.getInt("product_id"));
				inventory.setProductName(rs.getString("product_name"));
				inventory.setQuantityOnHand(rs.getLong("quantity_on_hand"));
				
				return inventory;
			}
		});
	}
	
	
	/**************************************************************
	 ************* 갱신 메소드(Create, Update, Delete) ****************
	 **************************************************************/
	
	/* add: 새로운 제품 정보 추가  */
	public int add(Product product) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("INSERT INTO product_information ")
		.append("(product_id, product_name, product_description, ")
		.append("warranty_period, category_id, product_status, list_price, min_price) ")
		.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		
		String sql = sqlBuilder.toString();
		int productId = (getMaxId() + 1);
		
		return template.update(sql, productId, 
				product.getProductName(), product.getProductDescription(),
				product.getWarrantyPeriod(), product.getCategoryId(), 
				product.getProductStatus(), product.getListPrice(), product.getMinPrice());
	}
	
	
	/* delete: 제품 정보 삭제(수동 트랜잭션) */
	public int delete(int productId) {
		String sql = "DELETE FROM product_information WHERE product_id = ?";
		String sqlDesc = "DELETE FROM product_descriptions WHERE product_id = ?";
		String sqlInv = "DELETE FROM inventories WHERE product_id = ?";
		
		Connection c = null;
		
		try {
			TransactionSyncManager.initSynchronization();	// 트랜잭션 경계 설정
			c = DataSourceUtil.getConnection(dataSource);
			c.setAutoCommit(false);
			
			template.update(sqlInv, productId);		// 재고 정보 삭제
			int result = template.update(sql, productId);	// 제품 정보 삭제
			
			if(result == 1) {
				// 삭제된 레코드 수가 1인 경우에만 커밋(기 데이터 손상 방지를 위해)
				template.update(sqlDesc, productId);	// 제품 설명(번역) table에서 데이터 삭제
				c.commit();
			} else {
				// 그 외는 롤백
				c.rollback();
			}
			
			return result;
			
		} catch (Exception e) {
			template.rollback(c);
			throw new DataAccessException(e);
		} finally {
			// 트랜잭션 리소스 해제
			
			// memo: 원활한 트랜잭션 사용을 위해 
			// clearSynchronization()이 호출되어야 한다.
			
			DataSourceUtil.ReleaseConnection(c, dataSource);
			TransactionSyncManager.unbindResource(dataSource);
			TransactionSyncManager.clearSynchronization();
			
			/*********************************************************
			 * memo: 트랜잭션 리소스 해제 때문에 사실 템플릿 적용이 의미가 없어진다.
			 * 
			 * 그러나 이러한 트랜잭션이 필요한 이유는 서비스 클래스 등의 클라이언트에서 
			 * 여러 개의 DAO 메소드를 하나의 트랜잭션으로 묶기 위해서이다.
			 * 
			 * 예를 들면, delete()를 여러 번 호출한 후, 최종적으로 하나의 트랜잭션에 대해
			 * 커밋이나 롤백을 해주는 경우(deleteById 등의 메소드를 작성한다고 하면)
			 * 이러한 트랜잭션 관리가 필요하다.
			 * 
			 * 하지만 하나의 DAO 메소드에서만 트랜잭션 경계가 설정될 것이라면
			 * 전통적인 JDBC 코드나 템플릿(+트랜잭션 매니저) 코드나 
			 * 큰 차이가 있는 것 같지는 않다. 개인적으로는 전자의 코드가 작성하기 더 편하다.
			**********************************************************/
		}
	}
	
	
	/* update: 제품 정보 변경(수동 트랜잭션) */
	public int update(final Product product) {
		StringBuilder sqlBuilder = new StringBuilder();
		
		final boolean fullUpdate = product.getWeightClass() != 0 
				&& product.getSupplierId() != 0;
		
		// 쿼리 작성
		if(fullUpdate) {
			// 전체 칼럼 업데이트
			sqlBuilder.append("UPDATE product_information SET ")
			.append("product_name = ?, product_description = ?, ")
			.append("category_id = ?, weight_class = ?, warranty_period = ?, ")
			.append("supplier_id = ?, product_status = ?, ")
			.append("list_price = ?, min_price = ?, catalog_url = ? ")
			.append("WHERE product_id = ?");
		} else {
			// 일부 칼럼 업데이트
			sqlBuilder.append("UPDATE product_information SET ")
			.append("product_name = ?, product_description = ?, ")
			.append("category_id = ?, warranty_period = ?, product_status = ?, ")
			.append("list_price = ?, min_price = ?,  catalog_url = ? ")
			.append("WHERE product_id = ?");
		}
		
		final String sql = sqlBuilder.toString();
		Connection c = null;
		
		try {
			TransactionSyncManager.initSynchronization();	// 트랜잭션 경계 설정
			c = DataSourceUtil.getConnection(dataSource);
			c.setAutoCommit(false);
			
			int result = template.update(sql, new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection c)
						throws SQLException {
					PreparedStatement ps = c.prepareStatement(sql);
					
					if(fullUpdate) {
						// 전체 칼럼 업데이트
						ps.setString(1, product.getProductName());
						ps.setString(2, product.getProductDescription());
						ps.setShort(3, product.getCategoryId());
						ps.setShort(4, product.getWeightClass());
						ps.setString(5, product.getWarrantyPeriod());
						ps.setInt(6, product.getSupplierId());
						ps.setString(7, product.getProductStatus());
						ps.setDouble(8, product.getListPrice());
						ps.setString(9, product.getCatalogUrl());
						ps.setDouble(10, product.getMinPrice());
						
						ps.setInt(11, product.getProductId());	
					} else {
						// 일부 칼럼 업데이트
						ps.setString(1, product.getProductName());
						ps.setString(2, product.getProductDescription());
						ps.setShort(3, product.getCategoryId());
						ps.setString(4, product.getWarrantyPeriod());
						ps.setString(5, product.getProductStatus());
						ps.setDouble(6, product.getListPrice());
						ps.setDouble(7, product.getMinPrice());
						ps.setString(8, product.getCatalogUrl());
						ps.setInt(9, product.getProductId());
					}
					
					return ps;
				}
			});
			
			if(result == 1) {
				// 변경된 레코드 수가 1인 경우에만 커밋(기 데이터 손상 방지를 위해)
				c.commit();
			} else {
				// 그 외는 롤백
				c.rollback();
			}
			
			return result;
			
		} catch (Exception e) {
			template.rollback(c);
			throw new DataAccessException(e);
		} finally {
			// 트랜잭션 리소스 해제
			DataSourceUtil.ReleaseConnection(c, dataSource);
			TransactionSyncManager.unbindResource(dataSource);
			TransactionSyncManager.clearSynchronization();
		}
	}
	
	
	/* stock: 창고에 처음 입고할 때  */
	public int stock(final int productId, final int warehouseId, final long quantity) {
		StringBuilder sqlBulider = new StringBuilder();
		
		sqlBulider.append("INSERT INTO inventories ")
		.append("(product_id, warehouse_id, quantity_on_hand) VALUES (?, ?, ?)");
		
		final String sql = sqlBulider.toString();
		
		return template.update(sql, productId, warehouseId, quantity);
	}
	
	
	/* warehousing: 재고 정보가 등록된 제품에 대한 입출고 관리(수동 트랜잭션)  */
	public int warehousing(int productId, int warehouseId, long quantity) {
		StringBuilder sqlBulider = new StringBuilder();
		
		sqlBulider.append("UPDATE inventories SET ")
		.append("quantity_on_hand = ? WHERE product_id = ? AND warehouse_id = ?");
		
		String sql = sqlBulider.toString();
		
		Connection c = null;
		
		try {
			TransactionSyncManager.initSynchronization();	// 트랜잭션 경계 설정
			c = DataSourceUtil.getConnection(dataSource);
			c.setAutoCommit(false);
			
			int result = template.update(sql, quantity, productId, warehouseId);
			
			if(result == 1) {
				// 변경된 레코드 수가 1인 경우에만 커밋(기 데이터 손상 방지를 위해)
				c.commit();
			} else {
				// 그 외는 롤백
				c.rollback();
			}
			
			return result;
			
		} catch (Exception e) {
			template.rollback(c);
			throw new DataAccessException(e);
		} finally {
			// 트랜잭션 리소스 해제
			DataSourceUtil.ReleaseConnection(c, dataSource);
			TransactionSyncManager.unbindResource(dataSource);
			TransactionSyncManager.clearSynchronization();
		}
	}
	
	
	/* updateListPrice: 상품 가격 수정 */
	public int updateListPrice(final int product_id, double listPrice) {
		final String sqlMinPrice = "SELECT min_pirce FROM product_information WHERE product_id = ?";
		double minPrice = template.queryForDouble(sqlMinPrice, 
				new PreparedStatementCreator() {
			
					@Override
					public PreparedStatement createPreparedStatement(Connection c)
							throws SQLException {
						PreparedStatement ps = c.prepareStatement(sqlMinPrice);
						ps.setInt(1, product_id);
						return ps;
					}
				});
		
		String sql = "UPDATE product_information SET list_price = ? WHERE product_id = ?";
		
		Connection c = null;
		
		try {
			TransactionSyncManager.initSynchronization();	// 트랜잭션 경계 설정
			c = DataSourceUtil.getConnection(dataSource);
			c.setAutoCommit(false);
			
			// 제약조건
			if(minPrice > listPrice) {
				// listPrice가 최소가격보다 작다면
				if(minPrice >= 0)
					listPrice = minPrice;	// listPrice를 최소가격으로 설정
				else
					listPrice = 0;			// 만약 최소가격이 0보다 작다면, 0으로 설정
			}
			
			int result = template.update(sql, listPrice, product_id);
			
			if(result == 1) {
				// 변경된 레코드 수가 1인 경우에만 커밋(기 데이터 손상 방지를 위해)
				c.commit();
			} else {
				// 그 외는 롤백
				c.rollback();
			}
			
			return result;
			
		} catch (Exception e) {
			template.rollback(c);
			throw new DataAccessException(e);
		} finally {
			// 트랜잭션 리소스 해제
			DataSourceUtil.ReleaseConnection(c, dataSource);
			TransactionSyncManager.unbindResource(dataSource);
			TransactionSyncManager.clearSynchronization();
		}
	}	
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// RowMapper
	
	/* productMapper: 제품 정보 매퍼 */
	private RowMapper<Product> productMapper = new RowMapper<Product>() {

		@Override
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			Product product = new Product(rs.getInt("product_id"));
			product.setProductName(rs.getString("product_name"));
			product.setProductDescription(rs.getString("product_description"));
			product.setCategoryId(rs.getShort("category_id"));
			product.setWarrantyPeriod(rs.getString("warranty_period"));
			product.setSupplierId(rs.getInt("supplier_id"));
			product.setProductStatus(rs.getString("product_status"));
			product.setListPrice(rs.getDouble("list_price"));
			product.setMinPrice(rs.getDouble("min_price"));
			product.setCatalogUrl(rs.getString("catalog_url"));
			product.setCategoryName(rs.getString("category_name"));
			product.setCategorydescription(rs.getString("category_description"));
			product.setQtySum(rs.getLong("qty"));
			
			return product;
		}
	};
	
	
	/* productListMapper: 제품 정보 리스트 매퍼 */
	private RowMapper<Product> productListMapper = new RowMapper<Product>() {
		
		@Override
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			Product product = new Product(rs.getInt("product_id"));
			product.setProductName(rs.getString("product_name"));
			product.setProductStatus(rs.getString("product_status"));
			product.setListPrice(rs.getDouble("list_price"));
			product.setMinPrice(rs.getDouble("min_price"));
			product.setCategoryId(rs.getShort("category_id"));
			product.setCategoryName(rs.getString("category_name"));
			product.setQtySum(rs.getLong("qty"));
			
			return product;
		}
	};

	
}
