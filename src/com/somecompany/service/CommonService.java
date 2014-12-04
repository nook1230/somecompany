package com.somecompany.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import com.somecompany.repository.dao.oe.callbacks.PreparedStatementCreator;
import com.somecompany.repository.dao.oe.callbacks.ResultSetExtractor;
import com.somecompany.repository.dao.templates.JdbcDaoTemplate;
import com.somecompany.utils.DataSourceFactory;

/**************************************
 * CommonService
 * view에서 사용될 map 정보를 가져오는 서비스 클래스
 * DAO 없이 바로 DB에 접근함
***************************************/

@SuppressWarnings("unchecked")
public class CommonService {
	// jdbc template
	private static JdbcDaoTemplate template = new JdbcDaoTemplate();
	
	// DB 접속 정보
	private static final String hrSheme = "hr";
	private static final String hrPass = "hr";
	private static final String oeSheme = "oe";
	private static final String oePass = "oe";
	
	// 맵에 사용될 key의 타입(정수형, 문자형)
	private static final int KEY_TYPE_INT = 0;
	private static final int KEY_TYPE_STR = 1;
	
	
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	// 맵 획득 메소드
	
	/* 부서 id & 부서 이름 */
	public static Map<Integer, String> getDepartmentMap() {
		return getMap("departments", hrSheme, hrPass, 
				"department_id", "department_name", KEY_TYPE_INT);
	}
	
	/* 직무 id & 직무 이름 */
	public static Map<String, String> getJobMap() {
		return getMap("jobs", hrSheme, hrPass, 
				"job_id", "job_title", KEY_TYPE_STR);
	}
	
	/* 카테고리 id & 카테고리 이름 */
	public static Map<Short, String> getCategoryMap() {
		return getMap("categories_tab", oeSheme, oePass, 
				"category_id", "category_description", KEY_TYPE_INT);
	}
	
	/* 권한 id & 권한 이름 */
	public static Map<Short, String> getAuthorityMap() {
		return getMap("authority", hrSheme, hrPass, 
				"authority_id", "authority_title", KEY_TYPE_INT);
	}
	
	
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	// getMap: 파라미터에 따라 적절한 map을 생성 및 리턴
	// 파라미터: 테이블 이름/스키마 이름/스키마 패스워드/키 칼럼 이름/값 칼럼 이름/키의 자료형 타입
	public static <T> Map<T, String> getMap(String table, String schem, String pass, 
			final String keyColum, final String valueColumn, final int keytype) {
		
		// sql문 생성
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ").append(table);
		final String sql = builder.toString();
		
		// 데이터 소스 획득
		template.setDataSource(DataSourceFactory.getOracleDataSource(schem, pass));
		
		return (Map<T, String>) template.query(sql, new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement(sql);
				return ps;
			}
		}, new ResultSetExtractor<TreeMap<T, String>>() {
			
			@Override
			public TreeMap<T, String> extractData(ResultSet rs) throws SQLException {
				// id에 따른 정렬을 위해 TreeMap을 사용한다
				TreeMap<T, String> map = new TreeMap<T, String>();
				
				while(rs.next()) {
					// 키의 자료형에 따라 result set으로부터 자료를 가져온다
					switch(keytype) {
					case KEY_TYPE_INT:
						Integer key = rs.getInt(keyColum);
						map.put((T) key, rs.getString(valueColumn));
						break;
					case KEY_TYPE_STR: default:
						String strKey = rs.getString(keyColum);
						map.put((T) strKey, rs.getString(valueColumn));
					}
					
				}
				
				return map;
			}
		});
	}
}
