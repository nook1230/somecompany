package com.somecompany.utils;

import java.util.List;

/*****************************************
 * QueryProcessor
 * 
 * request의 쿼리 문자열 중
 * droppedParamName로 지정된 파라미터를 삭제해주고
 * returnedData에 doSomething의 결과를 반환한다
******************************************/

public class QueryProcessor {
	public static String setQuery(String queryStr, String[] droppedParamsName,
			List<Object> returnedDataList, QueryCallback callback) {
		StringBuilder builder = new StringBuilder();
		
		if(queryStr != null) {
			// &로 파싱
			String[] splitedQuery = queryStr.split("&");
			
			for(String str : splitedQuery) {
				// droppedParamName인 경우 무시
				boolean isDroppedParam = false;
				
				for(int i = 0; i < droppedParamsName.length; i++) {
					if(str.startsWith(droppedParamsName[i] + "="))
						isDroppedParam = true;
				}
				
				if(!isDroppedParam)
					builder.append(str).append("&");
				
				// doSomething을 이용해서 부가 작업을 실시
				if(returnedDataList != null) {
					Object temp = callback.doSomething(str);
					if(temp != null) {
						returnedDataList.add(temp);
					}
				}
			}
		}
		
		queryStr = builder.toString();
		
		return queryStr;
	}
}
