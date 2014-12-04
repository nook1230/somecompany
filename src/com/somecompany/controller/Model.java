package com.somecompany.controller;

import java.util.HashMap;
import java.util.Map;

public class Model {
	private Map<String, Object> innerMap = new HashMap<String, Object>();
	
	public void addAttribute(String name, Object value) {
		innerMap.put(name, value);
	}
	
	public Object getAttribute(String name) {
		return innerMap.get(name);
	}
	
	public int size() {
		return innerMap.size();
	}
}
