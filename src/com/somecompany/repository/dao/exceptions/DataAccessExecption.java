package com.somecompany.repository.dao.exceptions;

@SuppressWarnings("serial")
public class DataAccessExecption extends RuntimeException {
	public DataAccessExecption(Throwable cause) {
		super(cause);
	}
}
