package com.somecompany.service;

public enum ORDER_STATUS {
	NOT_FULLY_ENTERED(0, "주문대기"),
	ENTERED(1, "결제대기"),
	CANCELED_BY_COM(2, "주문취소 - 한도부족"),
	CANCELED_BY_CUST(3, "주문취소"),
	SHIPPED_WHOLE(4, "발송"),
	SHIPPED_REPLACE(5, "발송 - replacement"),
	SHIPPED_BACKLOG(6, "발송 - backlog"),
	SHIPPED_SPECIAL(7, "특송"),
	SHIPPED_BILLED(8, "발송 - 청구"),
	SHIPPED_PAYMENT(9, "발송 - 할부"),
	SHIPPED_PAID(10, "발송 - 결제완료"),
	PAID(11, "결제완료"),
	SHIPPED_COMPLETE(12, "배송 완료"),
	ALL_ORDER(-1, "전체");
	
	
	public final int order_status;
	public final String order_status_desc;
	
	private ORDER_STATUS(int order_status, String order_status_desc) {
		this.order_status = order_status;
		this.order_status_desc = order_status_desc;
	}
	
	public static ORDER_STATUS getOrderStatus(int value) {
		switch(value) {
		case 0: return NOT_FULLY_ENTERED;
		case 1: return ENTERED;
		case 2: return CANCELED_BY_COM;
		case 3: return CANCELED_BY_CUST;
		case 4: return SHIPPED_WHOLE;
		case 5: return SHIPPED_REPLACE;
		case 6: return SHIPPED_BACKLOG;
		case 7: return SHIPPED_SPECIAL;
		case 8: return SHIPPED_BILLED;
		case 9: return SHIPPED_PAYMENT;
		case 10: return SHIPPED_PAID;
		case 11: return PAID;
		case 12: return SHIPPED_COMPLETE;
		default: return ALL_ORDER;
		}
	}
}
