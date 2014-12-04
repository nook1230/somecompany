package com.somecompany.repository.exceptions;

import java.sql.SQLException;

@SuppressWarnings("serial")
public class DataAccessException extends RuntimeException {
	private Throwable cause;
	
	public DataAccessException(Throwable cause) {
		super(cause);
		this.cause = cause;
	}
	
	public String getSQLState() {
		if(cause instanceof SQLException) {
			return ((SQLException) cause).getSQLState();
		}
		
		return "not SQLException";
	}
	
	public int getErrorCode() {
		if(cause instanceof SQLException) {
			return ((SQLException) cause).getErrorCode();
		}
		
		return -1;
	}
}
