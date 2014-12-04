package com.somecompany.model.oe;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class Order {
	//////////////////////////////////////////////////
	// Fields
	
	private int orderId = 0;
	private Timestamp orderDate = null;
	private String orderMode = "";
	private int customerId = 0;
	private String customerName = "";
	private String customerAddress = "";
	private String phoneNumber = "";
	private short orderStatus = 0;
	private double orderTotal = 0.0;
	private int salesRepId = 0;
	private int promotionId = 0;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	//////////////////////////////////////////////////
	// Constructor	
	public Order() { }
	
	public Order(int orderId) {
		this.orderId = orderId;
	}
	
	//////////////////////////////////////////////////
	// Getters and setters
	
	// 주문번호
	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	// 주문일자
	public Timestamp getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	
	// 주문모드: direct or online
	public String getOrderMode() {
		return orderMode;
	}
	
	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}
	
	// 주문자 번호
	public int getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	// 주문자 이름
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	// 주문자 주소
	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	
	// 주문자 연락처
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	// 주문상태
	/*
	 * 0: 주문정보 부족, 1: 주문완료, 2: 취소(bad credit), 3: 취소(고객에 의해),
	 * 4: 전체 물품 선적됨, 5: 선적 - 교체 물품(replacement items), 
	 * 6: 선적 - 연착 배송(backlog on items), 7: 선적 - 특송(special delivery), 
	 * 8: 선적 - 청구됨, 9: 선적 - 할부(payment plan),
	 * 10: 선적 - 계산됨
	*/
	public short getOrderStatus() {
		return orderStatus;
	}
	
	public void setOrderStatus(short orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	// 주문 총액(가격)
	public double getOrderTotal() {
		return orderTotal;
	}
	
	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}
	
	public void setOrderTotal() {
		
		int size = this.orderItems.size();
		double result = 0.0;
		OrderItem item = null;
		
		for(int i = 0; i < size; i++) {
			item = this.orderItems.get(i);
			result += item.getUnitPrice() * item.getQuantity();
		}
		
		this.orderTotal = result;
	}
	
	// 판매사원(hr스키마)
	public int getSalesRepId() {
		return salesRepId;
	}
	
	public void setSalesRepId(int salesRepId) {
		this.salesRepId = salesRepId;
	}
	
	// 프로모션 번호(SH스키마)
	public int getPromotionId() {
		return promotionId;
	}
	
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public List<OrderItem> getOrderItems() {
		resetOrderItems();
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		resetOrderItems();		
		this.orderItems = orderItems;
	}
	
	public void resetOrderItems() {
		for(OrderItem item: orderItems) {
			item.setOrderId(orderId);
		}
		
		setOrderTotal();
	}
}
