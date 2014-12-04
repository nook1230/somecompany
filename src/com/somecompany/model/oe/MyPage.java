package com.somecompany.model.oe;

import com.somecompany.utils.ListHelper;

public class MyPage {
	private Customer customer;
	private ListHelper<Order> orderListHelper;
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public ListHelper<Order> getOrderListHelper() {
		return orderListHelper;
	}
	
	public void setOrderListHelper(ListHelper<Order> orderListHelper) {
		this.orderListHelper = orderListHelper;
	}
}
