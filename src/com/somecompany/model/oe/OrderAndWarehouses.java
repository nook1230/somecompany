package com.somecompany.model.oe;

import java.util.List;

public class OrderAndWarehouses {
	private Order order;
	private List<Warehouse> warehouses;
	
	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public List<Warehouse> getWarehouses() {
		return warehouses;
	}
	
	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}
}
