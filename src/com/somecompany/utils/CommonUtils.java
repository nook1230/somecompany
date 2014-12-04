package com.somecompany.utils;

public class CommonUtils {
	public static int getIntParameter(String param, int defaultValue) {
		if(param != null && !param.isEmpty())
			return Integer.parseInt(param);
		
		return defaultValue;
	}
	
	public static short getShortParameter(String param, short defaultValue) {
		if(param != null && !param.isEmpty())
			return Short.parseShort(param);
		
		return defaultValue;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getTypeObject(Object attr, Class<?> type) {
		if(attr != null && attr.getClass() == type) {
			
			return (T) attr;
		} else { 
			System.out.println("옴마!");
			return null; 
		}
	}
	
	public static String getStringParameter(String param, String defaultValue) {
		if(param != null)
			return param;
		
		return defaultValue;
	}
}
