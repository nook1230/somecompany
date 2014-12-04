package com.somecompany.repository.dao.templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/***********************************************
 * TransactionSyncManager
 * 미개한 트랜잭션 동기화 기능 제공
 * Spring의 TransactionSynchronizationManager를 
 * 보고 그 내용을 유추하여 만들어본 모방 클래스
 * 
 * memo: 이 방법보다는 트랜잭션 이름을 이용한 방식을 
 * 고려해보는 것이 좋을 듯 하다.
************************************************/

public class TransactionSyncManager {
	/***********************************************
	 * 설계상 TransactionSyncManager에 활성화된 트랜잭션은
	 * 1개만 존재하도록 한다. 하지만 강제로 활성화시키는 경우
	 * 2개 이상의 트랜잭션이 활성화될 수 있다.
	************************************************/
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	// 상수 필드
	private static final int TIMEOUT_EX = 1;
	private static final int TIMEOUT_INACT = 2;
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	// static 필드
	
	// 타임아웃 시 처리 방법
	private static int timeout_method  = TIMEOUT_INACT;
	
	// 리소스 맵
	private static Map<Object, Object> resourceMap = new HashMap<Object, Object>();
	
	// 동기화 객체 리스트
	private static List<TransactionSynchronization> synchronization 
		= new Vector<TransactionSynchronization>();
	
	// 현재 트랜잭션
	private static TransactionSynchronization currentTransaction;
	
	// initSynchronization 타임아웃, 디폴트: 60초
	private static long timeout = 60000;
	
	// getters
	public static Map<Object, Object> getResourceMap() {
		return resourceMap;
	}
	
	public static List<TransactionSynchronization> getSynchronization() {
		return synchronization;
	}

	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	
	/* resetTransactionSyncManager: 
	 * TransactionSyncManager를 초기화함
	 * (비정상적인 사용을 대비하여) */
	synchronized public static void resetTransactionSyncManager() {
		resourceMap = new HashMap<Object, Object>();
		synchronization = new Vector<TransactionSynchronization>();
		currentTransaction = null;
	}
	
	
	/* initSynchronization: 트랜잭션 동기화 초기화 */
	synchronized public static void initSynchronization() throws Exception {
		// 새 동기화 객체를 생성
		TransactionSynchronization nts = new TransactionSynchronization();
		long elapsedTimeinMillisecond = 0;	// 경과시간 측정 변수
		
		// 동기화 객체 리스트에 새로 만든 객체를 추가
		synchronization.add(nts);
		
		for(TransactionSynchronization ts : synchronization) {
			// 리스트를 순회하면서 활성화된 트랜잭션을 찾는다
			if(ts.isActive()) {
				// 활성화된 트랜잭션이 있다면 기다린다
				while(true) {
					Thread.sleep(5000);		// 5초 대기
					elapsedTimeinMillisecond += 5000;	// 경과시간 누적
					if(!ts.isActive())
						break;	// 트랜잭션이 비활성화되면 루프 탈출
					
					if(elapsedTimeinMillisecond > timeout) {
						
						switch(timeout_method) {
						// 방법 1: 경과 시간 초과 시 예외를 던진다. 다른 트랜잭션을 방해하지 않는 방법 
						case TIMEOUT_EX:
							throw new Exception("initSynchronization timeout!");
							
						// 방법 2: 경과 시간 초과 시 ts를 비활성화한다. 지나치게 오래 작업하는 트랜잭션을 비활성화시킨다
						case TIMEOUT_INACT: default:
							ts.setActive(false);							
						}
					}
					
					/****************************************************
					 * memo: 트랜잭션 경계를 설정한 클라이언트는
					 * 되도록 빨리 트랜잭션 경계를 해제해야 한다.
					 * 
					 * TODO: 차후에 initSynchronization()를 보완하도록 할 것.
					 ****************************************************/
					
				}
			}
		}
		
		// 현재 트랜잭션으로 설정하고, 활성화시킨다
		currentTransaction = nts;
		currentTransaction.setActive(true);
	}
	
	
	/* clearSynchronization: 트랜잭션 동기화 해제 
	 * 트랜잭션을 사용한 클라이언트가 이 메소드를 호출하지 않으면
	 * 다른 클라이언트들이 트랜잭션을 사용할 수 없게 된다. 
	 * init가 성공하기 위해서는 이 메소드가 반드시 호출되어야 한다. */
	synchronized public static void clearSynchronization() {
		if(currentTransaction != null) {
			currentTransaction.setActive(false);
			synchronization.remove(currentTransaction);
			currentTransaction = null;
		}
	}
	
	
	/* isSynchronizationActive: 현재 트랜잭션이 활성화되어 있는지 */
	public static boolean isSynchronizationActive() {
		if(currentTransaction != null)
			return currentTransaction.isActive();
		
		return false;
	}
	
	
	/* clreaSynchronization: 동기화 객체 리스트를 비운다 */
	synchronized public void clreaSynchronization() {
		int size = synchronization.size();
		for(int i = 0; i < size; i++)
			synchronization.remove(i);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	// 리소스 관리 메소드
	
	/* bindResource: key로 전달된 객체를 value 객체와 연결  */
	synchronized public static void bindResource(Object key, Object value) {
		if(!hasResource(key))
			resourceMap.put(key, value);
	}
	
	
	/* unbindResource */
	synchronized public static void unbindResource(Object key) {
		resourceMap.remove(key);
	}
	
	
	/* getter */
	public static Object getResource(Object key) {
		return resourceMap.get(key);
	}
	
	
	/* hasResource: key가 리소스 맵에 존재하는지 */
	public static boolean hasResource(Object key) {
		return resourceMap.containsKey(key);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	
	
	/* setCurrentTransactionInactive: 강제로 현재 트랜잭션을 비활성화한다  */
	synchronized public static void setCurrentTransactionInactive() {
		if(currentTransaction != null) {
			currentTransaction.setActive(false);
		}
	}
	
	
	/* timeout getter and setter */
	public static long getTimeout() {
		return timeout;
	}
	
	public static void setTimeout(long timeout) {
		TransactionSyncManager.timeout = timeout;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	// getters	

	public TransactionSynchronization getTransactionSynchronizationByName(String name) {
		TransactionSynchronization ret = null;
		
		for(TransactionSynchronization ts : synchronization) {
			if(ts.getName().equals(name))
				ret = ts;
		}
		
		return ret;
	}
	
	public static String getCurrentTransactionName() {
		String name = null;
		
		if(currentTransaction != null)
			name = currentTransaction.getName();
		
		return name;
	}
	
	public static int getCurrentTransactionIsolationLevel() {
		int level = -1;
		
		if(currentTransaction != null)
			level = currentTransaction.getIsolationLevel();
		
		return level;
	}
	
	
}
