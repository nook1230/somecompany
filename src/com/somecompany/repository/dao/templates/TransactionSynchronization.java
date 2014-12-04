package com.somecompany.repository.dao.templates;

/***********************************************
 * TransactionSynchronization
 * 트랜잭션 동기화에 대한 간단한 정보를 담는 자바 빈
************************************************/

public class TransactionSynchronization {
	private String name = this.toString();	// 객체의 해시 값을 객체 이름으로 지정
	private boolean active = false;			// 활성화된 트랜잭션인지
	private int isolationLevel = 0;			// 트랜잭션의 isolation level
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// getters and setters
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getIsolationLevel() {
		return isolationLevel;
	}
	
	public void setIsolationLevel(int isolationLevel) {
		this.isolationLevel = isolationLevel;
	}
}
