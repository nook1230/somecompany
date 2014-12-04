package com.somecompany.model.hr;

public class Authority {
	private int employeeId;
	private int authorityId;
	private String employeeName;
	private String authorityTitle;
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getAuthorityId() {
		return authorityId;
	}
	public void setAuthorityId(int authorityId) {
		this.authorityId = authorityId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getAuthorityTitle() {
		return authorityTitle;
	}
	public void setAuthorityTitle(String authorityTitle) {
		this.authorityTitle = authorityTitle;
	}
}
