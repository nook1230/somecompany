package com.somecompany.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/********************************************
 * Controller 추상 클래스
 * 
 * MainConroller 내부에서 레퍼런스로 사용
 * MainConroller를 제외한 Controller들은
 * 이 클래스를 상속 받는다
*********************************************/

public abstract class Controller {
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 추상 메소드: 매핑 함수들
	protected abstract void mapping();
		
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 생성자
	
	public Controller() {
		init();
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// 맵 객체들
	protected Map<String, String> methGetRequestMap;
	protected Map<String, String> methPostRequestMap;
	protected Map<String, ResultBindingType> resultBindingMap;
	
	// getters
	public Map<String, String> getMethGetRequestMap() {
		return methGetRequestMap;
	}
	
	public Map<String, String> getMethPostRequestMap() {
		return methPostRequestMap;
	}
	
	public Map<String, ResultBindingType> getResultBindingMap() {
		return resultBindingMap;
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// utils
	
	/* init: 맵 초기화 */
	public void init() {
		resultBindingMap = new HashMap<String, ResultBindingType>();
		methGetRequestMap = new HashMap<String, String>();
		methPostRequestMap = new HashMap<String, String>();
		
		mapping();	// 맵핑 시작
	}
	
	// 결과 객체를 바인딩하기 위한 맵 구조
	public void MAP_RESULT_BINDING(String requestAction, String attrName, Class<?> clazz) {
		ResultBindingType bindingType;
		
		bindingType = new ResultBindingType();
		bindingType.attrName = attrName;
		bindingType.attrType = clazz;
		resultBindingMap.put(requestAction, bindingType);
	}
	
	// GET 요청을 컨트롤러의 메소드에 매핑한다
	public void MAP_GET(String requestAction, String requestMethodName) {
		methGetRequestMap.put(requestAction, requestMethodName);
	}
	
	// POST 요청을 컨트롤러의 메소드에 매핑한다
	public void MAP_POST(String requestAction, String requestMethodName) {
		methPostRequestMap.put(requestAction, requestMethodName);
	}
	
	////////////////////////////////////////////////////////////////////
	// getParam: 파라미터를 쉽게 가져올 수 있게 해주는 유틸 메소드들
	
	// int
	public int getIntParam(Map<String, String> paramsMap, String paramName, int defaultValue) {
		if(paramsMap.containsKey(paramName) && !paramsMap.get(paramName).isEmpty()) {
			return Integer.parseInt(paramsMap.get(paramName));
		} else {
			return defaultValue;
		}
	}
	
	// long
	public long getLongParam(Map<String, String> paramsMap, String paramName, long defaultValue) {
		if(paramsMap.containsKey(paramName) && !paramsMap.get(paramName).isEmpty()) {
			return Long.parseLong(paramsMap.get(paramName));
		} else {
			return defaultValue;
		}
	}
	
	// short
	public short getShortParam(Map<String, String> paramsMap, String paramName, short defaultValue) {
		if(paramsMap.containsKey(paramName) && !paramsMap.get(paramName).isEmpty()) {
			return Short.parseShort(paramsMap.get(paramName));
		} else {
			return defaultValue;
		}
	}
	
	// float
	public double getFloatParam(Map<String, String> paramsMap, String paramName, float defaultValue) {
		if(paramsMap.containsKey(paramName) && !paramsMap.get(paramName).isEmpty()) {
			return Float.parseFloat(paramsMap.get(paramName));
		} else {
			return defaultValue;
		}
	}
	
	// double
	public double getDoubleParam(Map<String, String> paramsMap, String paramName, double defaultValue) {
		if(paramsMap.containsKey(paramName) && !paramsMap.get(paramName).isEmpty()) {
			return Double.parseDouble(paramsMap.get(paramName));
		} else {
			return defaultValue;
		}
	}
	
	// String: 빈 문자열 제외
	public String getNotEmptyStringParam(Map<String, String> paramsMap, String paramName, String defaultValue) {
		if(paramsMap.containsKey(paramName) && !paramsMap.get(paramName).isEmpty()) {
			return paramsMap.get(paramName);
		} else {
			return defaultValue;
		}
	}
	
	// String: 빈 문자열 포함
	public String getStringParam(Map<String, String> paramsMap, String paramName, String defaultValue) {
		if(paramsMap.containsKey(paramName)) {
			return paramsMap.get(paramName);
		} else {
			return defaultValue;
		}
	}
	
	// 파라미터 필터링(정수형): 특정한 파라미터 네임으로 시작되는 파라미터를 걸러서 리스트에 넣어준다
	// 주로 id를 걸러내기 위해서 사용
	public List<Integer> filterOutIntParameter(Map<String, String> paramsMap, String filteredParamName) {
		List<Integer> paramList = new ArrayList<Integer>();
		
		for(Map.Entry<String, String> entry : paramsMap.entrySet()) {
			if(entry.getKey().startsWith(filteredParamName)) {
				int paramValue = Integer.parseInt(entry.getValue());
				paramList.add(paramValue);
			}
		}
		
		return paramList;	
	}
}
