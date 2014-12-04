package com.somecompany.model.oe;

public class Inventory {
	// product_information
	private int productId;
	private String productName;
	
	// inventories table
	private long quantityOnHand;

	// warehouse table
	private int warehouseId;
	private String warehouseName;
	private int locationId; // hr스키마의 locations table
	private String locationAddress;

	
	// ////////////////////////////////////////////////
	// 제품 번호
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	// 제품 이름
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	// ////////////////////////////////////////////////
	// 재고로 가지고 있는 수량
	public long getQuantityOnHand() {
		return quantityOnHand;
	}

	public void setQuantityOnHand(long quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}

	// ////////////////////////////////////////////////

	// 창고 번호
	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	// 창고 이름
	public String getWarehouseName() {
		return warehouseName;
	}
	
	public void setWarehouseName(String wareHouseName) {
		this.warehouseName = wareHouseName;
	}

	// 창고 지역 번호
	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	// 창고 주소
	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
}
