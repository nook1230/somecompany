package com.somecompany.utils;

import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import com.somecompany.repository.exceptions.DataAccessException;

public class DataSourceFactory {
	
	public static DataSource getOracleDataSource(String schema, String password) throws DataAccessException {
		OracleDataSource dataSource = null;
		
		try {
			dataSource = new OracleDataSource();
			
			dataSource.setURL("jdbc:oracle:thin:@127.0.0.1:1521:Oracle");
			dataSource.setUser(schema);
			dataSource.setPassword(password);
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return dataSource;
	}
}
