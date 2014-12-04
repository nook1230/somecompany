package com.somecompany.model.hr;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class JobHistory {
	private Date startDate;
	private Date endDate;
	private String jobTitle;
	private String departmentName;
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getJobTitle() {
		return jobTitle;
	}
	
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		builder.append(dateFormat.format(startDate)).append("~")
		.append(dateFormat.format(endDate)).append(": ")
		.append(jobTitle).append(", ")
		.append(departmentName);
		
		return builder.toString();
	}
}
