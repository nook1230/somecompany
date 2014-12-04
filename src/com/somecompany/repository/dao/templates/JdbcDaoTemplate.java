package com.somecompany.repository.dao.templates;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import com.somecompany.repository.dao.oe.callbacks.PreparedStatementCreator;
import com.somecompany.repository.dao.oe.callbacks.ResultSetExtractor;
import com.somecompany.repository.dao.oe.callbacks.RowMapper;
import com.somecompany.repository.exceptions.DataAccessException;

/*******************************************
 * JdbcDaoTemplate
 * 리소스 관리 기능과 편의 메소드 제공
 * 
 * Spring의 JdbcTemplate 클래스를 보고
 * 유추하여 만든 모방 클래스
 * 
 * 트랜잭션 동기화 관리자(TransactionSyncManager)와
 * 함께 사용할 수 있도록 설계되었다.
*******************************************/

public class JdbcDaoTemplate {
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	// 필드: 데이터 소스
	private DataSource dataSource;
	
	public JdbcDaoTemplate() { }
	
	public JdbcDaoTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/* 클라이언트에게 DB 커넥션을 돌려주는 getter 
	 * (트랜잭션은 고려되지 않음) 특별히 사용할 일은 없다. */
	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	// 템플릿 메소드
	
	/* query(ResultSetExtractor): 조회 템플릿 함수 */
	public <T> T query(String sql, PreparedStatementCreator psc, ResultSetExtractor<T> rse) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			ps = psc.createPreparedStatement(c);
			rs = ps.executeQuery();
			return rse.extractData(rs);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			if(rs != null) { try { rs.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(ps != null) { try { ps.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(c != null) { try { c.close(); } catch (SQLException e) { throw new DataAccessException(e); } }
		}
	}
	
	
	/* query(RowMapper): 조회 템플릿 함수(제네릭스 사용) */
	public <T> T query(String sql, PreparedStatementCreator psc, RowMapper<T> rm) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			ps = psc.createPreparedStatement(c);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return rm.mapRow(rs, 1);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			if(rs != null) { try { rs.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(ps != null) { try { ps.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(c != null) { try { c.close(); } catch (SQLException e) { throw new DataAccessException(e); } }
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	// 편의 쿼리 메소드
	
	/* queryForInt1: sql에 따라 하나의 정수를 반환하는 함수 */
	public Integer queryForInt(final String sql) {
		
		return query(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				return c.prepareStatement(sql);
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException {
				rs.next();
				return rs.getInt(1);
			}
		});
	}
	
	/* queryForInt2: sql에 따라 조건을 만족하는 정수를 반환하는 함수 */
	public int queryForInt(String sql,
			PreparedStatementCreator psc) {
		return query(sql, psc, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException {
				rs.next();
				return rs.getInt(1);
			}
		});
	}
	
	
	/* queryForLong: sql에 따라 조건을 만족하는 정수를 반환하는 함수 */
	public long queryForLong(String sql,
			PreparedStatementCreator psc) {
		return query(sql, psc, new ResultSetExtractor<Long>() {

			@Override
			public Long extractData(ResultSet rs) throws SQLException {
				rs.next();
				return rs.getLong(1);
			}
		});
	}
	
	/* queryForFloat: sql에 따라 조건을 만족하는 실수를 반환하는 함수 */
	public float queryForFloat(String sql,
			PreparedStatementCreator psc) {
		return query(sql, psc, new ResultSetExtractor<Float>() {

			@Override
			public Float extractData(ResultSet rs) throws SQLException {
				rs.next();
				return rs.getFloat(1);
			}
		});
	}
	
	
	/* queryForDouble: sql에 따라 조건을 만족하는 실수를 반환하는 함수 */
	public double queryForDouble(String sql,
			PreparedStatementCreator psc) {
		return query(sql, psc, new ResultSetExtractor<Double>() {

			@Override
			public Double extractData(ResultSet rs) throws SQLException {
				rs.next();
				return rs.getDouble(1);
			}
		});
	}
	
	/* queryForList: sql에 따라 다수의 객체를 조회하는 함수 */
	public <T> List<T> queryForList(String sql, RowMapper<T> rm) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<T> objList;
		
		int rowNum = 1;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				objList = new ArrayList<T>();
				
				do {
					objList.add(rm.mapRow(rs, rowNum));
					
					rowNum++;
				} while(rs.next());
				
			} else {
				objList = Collections.emptyList();
			}
			
			return objList;
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			if(rs != null) { try { rs.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(ps != null) { try { ps.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(c != null) { try { c.close(); } catch (SQLException e) { throw new DataAccessException(e); } }
		}
	}
	
	
	/* queryForList: sql에 따라 다수의 객체를 조회하는 함수 */
	public <T> List<T> queryForList(String sql, int offset, int limit, RowMapper<T> rm) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<T> objList; 
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM (")
		.append(sql).append(") WHERE ROWNUM <= ?");
		
		int rnum = offset + limit;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(sqlBuilder.toString());
			ps.setInt(1, rnum);
			
			rs = ps.executeQuery();
			
			int rowNum = 1;
			if(rs.next()) {
				objList = new ArrayList<T>();
				
				do {
					if(offset < rowNum)
						objList.add(rm.mapRow(rs, rowNum));
					
					rowNum++;
				} while(rs.next());
				
			} else {
				objList = Collections.emptyList();
			}
			
			return objList;
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			if(rs != null) { try { rs.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(ps != null) { try { ps.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(c != null) { try { c.close(); } catch (SQLException e) { throw new DataAccessException(e); } }
		}
	}
	
	
	/* queryForList: id를 조건으로 하여 다수의 객체를 조회하는 함수 */
	public <T> List<T> queryForList(String sql, int id, int offset, int limit, RowMapper<T> rm) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<T> objList; 
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM (")
		.append(sql).append(") WHERE ROWNUM <= ?");
		
		int rnum = offset + limit;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(sqlBuilder.toString());
			ps.setInt(1, id);
			ps.setInt(2, rnum);
			
			rs = ps.executeQuery();
			
			int rowNum = 1;
			if(rs.next()) {
				objList = new ArrayList<T>();
				
				do {
					if(offset < rowNum)
						objList.add(rm.mapRow(rs, rowNum));
					
					rowNum++;
				} while(rs.next());
				
			} else {
				objList = Collections.emptyList();
			}
			
			return objList;
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			if(rs != null) { try { rs.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(ps != null) { try { ps.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(c != null) { try { c.close(); } catch (SQLException e) { throw new DataAccessException(e); } }
		}
	}
	
	
	/* queryForList: id를 조건으로 하여 다수의 객체를 조회하는 함수 */
	public <T> List<T> queryForList(String sql, int id , RowMapper<T> rm) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<T> objList;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			int rowNum = 1;
			if(rs.next()) {
				objList = new ArrayList<T>();
				do {
					objList.add(rm.mapRow(rs, rowNum++));
				} while(rs.next());
			} else {
				objList = Collections.emptyList();
			}
			
			return objList;
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			if(rs != null) { try { rs.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(ps != null) { try { ps.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			if(c != null) { try { c.close(); } catch (SQLException e) { throw new DataAccessException(e); } }
		}
	}
	
	
	/* update1: 파라미터 설정이 없는 update */
	public Integer update(String sql) {
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = setConnection();	// 커넥션 설정(트랜잭션 지원)
			ps = c.prepareStatement(sql);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			/* 리소스 해제 */
			
			// PreparedStatement 해제
			if(ps != null) { try { ps.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
			
			// 커넥션 해제(트랜잭션 지원)
			releaseConnection(c);
			
		}
	}
	
	
	/* update2: PreparedStatementCreator를 사용하여 파라미터를 설정하는 update */
	public Integer update(String sql, PreparedStatementCreator psc) {
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = setConnection();	// 커넥션 설정(트랜잭션 지원)
			ps = psc.createPreparedStatement(c);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			// PreparedStatement 해제
			if(ps != null) { try { ps.close(); } catch( SQLException e) { throw new DataAccessException(e); } }
						
			// 커넥션 해제(트랜잭션 지원)
			releaseConnection(c);
		}
	}
	
	
	/* update3: 가변인자를 사용하여 파라미터를 설정하는 update */
	public Integer update(final String sql, final Object ... params) {
		return update(sql, new PreparedStatementCreator(){

			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				setParams(ps, params);
				return ps;
			}
				
		});
	}
	
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	// helpers(리소스 관리)
	
	/* close: ResultSet */
	public void close(ResultSet rs) { 
		if(rs != null) { 
			try { rs.close(); 
			} catch( SQLException e) { 
				throw new DataAccessException(e); 
			}
		}
	}
	
	
	/* close: PreparedStatement */
	public void close(PreparedStatement ps) {
		if(ps != null) { 
			try { 
				ps.close(); 
			} catch( SQLException e) { 
				throw new DataAccessException(e); 
			}
		}
	}
	
	
	/* close: Connection */
	public void close(Connection c) {
		if(c != null) { 
			try {
				c.close(); 
			} catch( SQLException e) { 
				throw new DataAccessException(e); 
			}
		}
	}
	
	
	/* commit */
	public void commit(Connection c) {
		try {
			c.commit();
		} catch (SQLException e) {
			throw new DataAccessException(e); 
		}
	}
	
	
	/* rollback */
	public void rollback(Connection c) {
		try {
			c.rollback();
		} catch (SQLException e) {
			throw new DataAccessException(e); 
		}
	}
	
	
	/* setTransactionStart: 트랜잭션 경계 설정 */
	public void setTransactionStart(Connection c) {
		try {
			c.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DataAccessException(e); 
		}
	}
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	// private 메소드
	
	/* setConnection: 트랜잭션 활성화 여부에 따라 적절한 커넥션을 리턴 */
	private Connection setConnection() throws SQLException {
		Connection c;
		if(TransactionSyncManager.isSynchronizationActive()) {
			// 트랜잭션 동기화가 활성화되어 있다면, 현재 dataSource에 binding되어 있는 커넥션을 받아온다
			c = DataSourceUtil.getConnection(dataSource);
		} else {
			// 비활성화 상태라면 그냥 받아온다
			c = dataSource.getConnection();
		}
		
		return c;
	}
	
	
	/* releaseConnection: 트랜잭션 활성화 여부에 따라 적절하게 커넥션 종료 */
	private void releaseConnection(Connection c) {
		if(!TransactionSyncManager.isSynchronizationActive()) {
			// 트랜잭션 동기화가 활성화되어 있다면, 받아온 커넥션은 그대로 둔다
			// 활성화 상태가 아닌 경우에만 커넥션을 종료한다
			if(c != null) { try { c.close(); } catch (SQLException e) { throw new DataAccessException(e); } }
		}	
	}
	
	
	/* setParams: update3에서 사용하는 내부 함수(일부 자료형에 대해서만 완성됨) */
	private void setParams(PreparedStatement ps, Object[] params) 
			throws SQLException {
		for(int i = 0; i < params.length; i++) {
			String typeName = params[i].getClass().getName();
			
			switch(typeName) {
			case "java.lang.Integer":
				ps.setInt(i+1, (Integer) params[i]);
				break;
			case "java.lang.Short":
				ps.setShort(i+1, (Short) params[i]);
				break;
			case "java.lang.Long":
				ps.setLong(i+1, (Long) params[i]);
				break;
			case "java.lang.Float":
				ps.setFloat(i+1, (Float) params[i]);
				break;
			case "java.lang.Double":
				ps.setDouble(i+1, (Double) params[i]);
				break;
			case "java.lang.Byte":
				ps.setByte(i+1, (Byte) params[i]);
				break;
			case "java.lang.String":
				ps.setString(i+1, (String) params[i]);
				break;
			case "java.sql.Date":
				ps.setDate(i+1, (Date) params[i]);
				break;
			case "java.sql.Timestamp":
				ps.setTimestamp(i+1, (Timestamp) params[i]);
				break;
			// so on......
			default:
				// do nothing
				// 예외를 발생시킬 수도 있을 것
			}
		}
	}
}
