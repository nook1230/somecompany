package com.somecompany.repository.dao.templates;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/***********************************************
 * DataSourceUtil
 * 트랜잭션 동기화 기능을 위한 데이터 리소스 관리 클래스
 * spring의 DataSourceUtils를 흉내낸 클래스
************************************************/

public class DataSourceUtil {
	
	/* getConnection: 트랜잭션 활성화 여부에 따라 적당한 커넥션을 리턴해준다 */
	public static Connection getConnection(DataSource dataSource) throws SQLException {
		if(TransactionSyncManager.isSynchronizationActive()) {
			// 트랜잭션 동기화가 활성화되어 있다면
			if(TransactionSyncManager.hasResource(dataSource)) {
				// dataSource를 키로 하는 value가 등록되어 있다면 그대로 value(DB 커넥션 객체)를 리턴
				return (Connection) TransactionSyncManager.getResource(dataSource);
			} else {
				// 등록되어 있지 않다면 bind(맵에 등록)해준다. 그리고 value 리턴
				TransactionSyncManager.bindResource(dataSource, dataSource.getConnection());
				return (Connection) TransactionSyncManager.getResource(dataSource);
			}
		} 
		
		// 트랜잭션 동기화가 활성화되어 있지 않으면 직접 커넥션 생성 후 리턴
		return dataSource.getConnection();
	}
	
	
	/* ReleaseConnection: 커넥션 종료  */
	// data source를 넣어주는 이유는? 아마도 동기화 문제 때문에?
	public static void ReleaseConnection(Connection c, DataSource dataSource) {
				
		if(c != null) { 
			try {
				c.close();
			} catch (SQLException e) { }
		} 
	}
}
