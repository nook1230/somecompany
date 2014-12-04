package com.somecompany.model.oe;

public class Warehouse {
	private int warehouseId;
	private String warehouseName;
	private int locationId;
	private int productId;
	private long quantity;
	
	public int getWarehouseId() {
		return warehouseId;
	}
	
	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	public String getWarehouseName() {
		return warehouseName;
	}
	
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	public int getLocationId() {
		return locationId;
	}
	
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
}
