package com.somecompany.repository.dao.oe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import com.somecompany.repository.dao.oe.callbacks.PreparedStatementCreator;
import com.somecompany.repository.dao.templates.JdbcDaoTemplate;
import com.somecompany.repository.exceptions.DataAccessException;

public class CategoryDao {
	private static JdbcDaoTemplate template;
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	static {
		template = new JdbcDaoTemplate();
		template.setDataSource(getOracleDataSource());
	}
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	
	
	public static int getCountByCategoryId(final short categoryId) {
		final String sql = "SELECT COUNT(*) FROM categories_tab WHERE category_id = ?";
		return template.queryForInt(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setShort(1, categoryId);
				return ps;
			}
		});
	}
	
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	
	private static DataSource getOracleDataSource() throws DataAccessException {
		OracleDataSource dataSource = null;
		
		try {
			dataSource = new OracleDataSource();
			
			dataSource.setURL("jdbc:oracle:thin:@127.0.0.1:1521:Oracle");
			dataSource.setUser("oe");
			dataSource.setPassword("oe");
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return dataSource;
	}
}
