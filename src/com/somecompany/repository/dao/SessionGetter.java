package com.somecompany.repository.dao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionGetter {
	private SqlSessionFactory sqlSessionFactory; // MyBatis 세션 팩토리
	
	public void setSqlSessionFactory(String resource) {
		this.sqlSessionFactory = getSqlSessionFactoryFromResource(resource);
	}
	
	/* getSqlSession: sql session 취득 */
	public SqlSession openNewSqlSession() {
		return sqlSessionFactory.openSession();
	}
	
	private static SqlSessionFactory getSqlSessionFactoryFromResource(String resource) {
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return new SqlSessionFactoryBuilder().build(inputStream);
	}
}
