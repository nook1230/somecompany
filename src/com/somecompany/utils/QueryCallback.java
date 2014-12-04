package com.somecompany.utils;

/*******************************
 * QueryCallback
 * QueryProcessor 작업 중에 해야 하는
 * 부가작업을 추가하기 위한 인터페이스
********************************/

public interface QueryCallback {
	Object doSomething(String str);
}
