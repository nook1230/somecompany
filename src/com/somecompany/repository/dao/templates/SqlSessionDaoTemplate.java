package com.somecompany.repository.dao.templates;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.somecompany.repository.dao.oe.callbacks.OrderSessionDaoCallback;

/**************************************
 * SqlSessionDaoTemplate
 * MyBatis Session을 관리하기 위한 템플릿 클래스
***************************************/

public class SqlSessionDaoTemplate {
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	// 필드: MyBatis SqlSession
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public SqlSession getSqlSession() {
		return sqlSession;
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	// 콜백 호출 메소드(템플릿)
	public <T> T doSQL(OrderSessionDaoCallback<T> callback) {
		try {
			return callback.executeSql();
		} finally {
			if(sqlSession != null) 
				sqlSession.close();
		}
	}
	
	
	/**************************************************************
	 ********************** 조회 메소드(Read) *************************
	 **************************************************************/
	
	/* query: 하나의 레코드를 받아오는 조회 메소드(파라미터 없음)  */
	public <T> T query(final String sql) {
		try {
			return (T) doSQL(new OrderSessionDaoCallback<T>() {
				
				@Override
				public T executeSql() {
					return sqlSession.selectOne(sql);
				}
			});
		} finally {
			if(sqlSession != null) 
				sqlSession.close();
		}
	}
	
	
	/* query: 하나의 레코드를 받아오는 조회 메소드  */
	public <T> T query(final String sql, final Object obj) {
		try {
			return (T) doSQL(new OrderSessionDaoCallback<T>() {
				
				@Override
				public T executeSql() {
					return sqlSession.selectOne(sql, obj);
				}
			});
		} finally {
			if(sqlSession != null) 
				sqlSession.close();
		}
	}
	
	
	/* queryForInt: 하나의 정수를 받아오는 조회 메소드(파라미터 없음)  */
	public Integer queryForInt(final String sql) {
		try {
			return (Integer) doSQL(new OrderSessionDaoCallback<Integer>() {
				
				@Override
				public Integer executeSql() {
					return sqlSession.selectOne(sql);
				}
			});
		} finally {
			if(sqlSession != null) 
				sqlSession.close();
		}
	}
	
	
	/* queryForInt: 하나의 레코드를 받아오는 조회 메소드  */
	public Integer queryForInt(final String sql, final Object obj) {
		try {
			return doSQL(new OrderSessionDaoCallback<Integer>() {
				
				@Override
				public Integer executeSql() {
					return sqlSession.selectOne(sql, obj);
				}
			});
		} finally {
			if(sqlSession != null) 
				sqlSession.close();
		}
	}
	
	/* queryForList: 여러 레코드의 리스트를 받아오는 조회 메소드  */
	public <T> List<T> queryForList(final String sql) {
		
		return (List<T>) doSQL(new OrderSessionDaoCallback<List<T>>() {

			@Override
			public List<T> executeSql() {
				return sqlSession.selectList(sql);
			}
		});		
	}	
	
	/* queryForList: 여러 레코드의 리스트를 받아오는 조회 메소드  */
	public <T> List<T> queryForList(final String sql, final Object obj) {
		
		return (List<T>) doSQL(new OrderSessionDaoCallback<List<T>>() {

			@Override
			public List<T> executeSql() {
				return sqlSession.selectList(sql, obj);
			}
		});		
	}
	
	
	/* queryForList: 여러 레코드의 리스트를 받아오는 조회 메소드(레코드 제한 가능)  */
	public <T> List<T> queryForList(final String sql, final Object obj, final RowBounds bounds) {
		
		return (List<T>) doSQL(new OrderSessionDaoCallback<List<T>>() {

			@Override
			public List<T> executeSql() {
				return sqlSession.selectList(sql, obj, bounds);
			}
		});	
	}
}
