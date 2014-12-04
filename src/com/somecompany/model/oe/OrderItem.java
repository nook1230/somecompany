package com.somecompany.model.oe;

public class OrderItem {
	//////////////////////////////////////////////////
	// Fields
	private int orderId;
	private int lineItemId;
	private int productId;
	private String productName;
	private double unitPrice;
	private long quantity;
	private int warehouseId;
	
	//////////////////////////////////////////////////
	// Getters and setters
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getLineItemId() {
		return lineItemId;
	}
	
	public void setLineItemId(int lineItemId) {
		this.lineItemId = lineItemId;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public long getQuantity() {
		return quantity;
	}
	
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

}
