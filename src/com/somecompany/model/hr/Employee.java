

package com.somecompany.model.hr;

import java.sql.Date;
import java.util.List;

/**************************************
 * hr.employees 테이블에 적용될
 * plain java bean object
***************************************/

public class Employee {
	
	//////////////////////////////////////////////////
	// Fields
	//////////////////////////////////////////////////
	
	// 필수항목: employees의 칼럼(11개)
	private int employeeId = 0;
	private String firstName = "";
	private String lastName = "";
	private String email = "";
	private String phoneNumber = "";
	private Date hireDate = null;
	private String jobId  = "";	
	private double salary = 0.0;
	private float commissionPct = 0.0f;
	private int managerId = 0; 	
	private int departmentId = 0;
	
	// 로그인 비밀번호(NULL 허용: 하지만 NULL인 비밀번호로는 로그인 불가)
	private String password = "";
	
	// 부가정보
	private String managerName = "";
	private String jobTitle = "";
	private String departmentName = "";
	private Workplace workplace = null;
	private List<JobHistory> jobHistory = null;
	private String profileComment = "";
	
	private String profilePictureName = "";
	
	//////////////////////////////////////////////////
	// Constructor
	
	public Employee() {
	}
	
	public Employee(int employeeId) {
		this.employeeId = employeeId;
	}
	
	//////////////////////////////////////////////////
	// Getters and setters
	//////////////////////////////////////////////////
	
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public float getCommissionPct() {
		return commissionPct;
	}

	public void setCommissionPct(float commissionPct) {
		this.commissionPct = commissionPct;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
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

	public Workplace getWorkplace() {
		return workplace;
	}

	public void setWorkplace(Workplace workplace) {
		this.workplace = workplace;
	}

	public String getJobHistory() {
		StringBuilder builder = new StringBuilder();
		int size = 0;
		
		if(jobHistory != null && jobHistory.size() != 0) {
			
			size = jobHistory.size();
			
			for(int i = 0; i < size; i++) {
				JobHistory hist = jobHistory.get(i);
				builder.append(hist.toString());
				
				if(i != size - 1)
					builder.append("<br />");
			}
		} else {
			builder.append("근무 이력이 없습니다.");
		}
		
		return builder.toString();
	}

	public void setJobHistory(List<JobHistory> jobHistory) {
		this.jobHistory = jobHistory;
	}

	public String getProfileComment() {
		return profileComment;
	}

	public void setProfileComment(String profileComment) {
		this.profileComment = profileComment;
	}

	public String getProfilePictureName() {
		return profilePictureName;
	}

	public void setProfilePictureName(String profilePictureName) {
		this.profilePictureName = profilePictureName;
	}
}
