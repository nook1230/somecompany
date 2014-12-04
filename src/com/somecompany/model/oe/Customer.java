package com.somecompany.model.oe;

import java.sql.Date;

/**************************************
 * oe.customers 테이블에 적용될
 * plain java bean object
***************************************/

public class Customer {
	//////////////////////////////////////////////////
	// Fields
	
	private int customerId = 0;
	private String customerUserName = "";
	private String customerPassword = "";
	private String customerFirstName = "";
	private String customerLastName = "";
	private String streetAddress = "";
	private String postalCode = "";
	private String city = "";
	private String stateProvince = "";
	private String countryId = "";
	private String phoneNumber = "";
	private String nlsLanguage = "";
	private String nlsTerritory = "";
	private double creditLimit = 0.0;
	private String customerEmail = "";
	private int accountMgrId = 0;
	private String customerGeoLocation = "";
	private Date dateOfBirth = null;
	private String maritalStatus = "";
	private String gender = "";
	private String incomeLevel = "";
		
	//////////////////////////////////////////////////
	// Constructors
	
	public Customer() {
		
	}	
	
	public Customer(int customerId) {
		this.customerId = customerId;
	}
	
	//////////////////////////////////////////////////
	// Getters and setters
	
	// 고객번호
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	// 고객 사용자 이름과 비밀번호
	public String getCustomerUserName() {
		return customerUserName;
	}

	public void setCustomerUserName(String customerUserName) {
		this.customerUserName = customerUserName;
	}

	public String getCustomerPassword() {
		return customerPassword;
	}

	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}

	// 고객 이름
	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	
	// 고객 주소	
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	// 고객 전화번호
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	// 언어(?)
	public String getNlsLanguage() {
		return nlsLanguage;
	}

	public void setNlsLanguage(String nlsLanguage) {
		this.nlsLanguage = nlsLanguage;
	}
	
	// 지역(?)
	public String getNlsTerritory() {
		return nlsTerritory;
	}
	
	public void setNlsTerritory(String nlsTerritory) {
		this.nlsTerritory = nlsTerritory;
	}
	
	// 신용 한도
	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}
	
	// 고객 이메일
	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
	// 고객 관리자 번호(HR 스키마)
	public int getAccountMgrId() {
		return accountMgrId;
	}

	public void setAccountMgrId(int accountMgrId) {
		this.accountMgrId = accountMgrId;
	}
	
	// ?
	public String getCustomerGeoLocation() {
		return customerGeoLocation;
	}

	public void setCustomerGeoLocation(String customerGeoLocation) {
		this.customerGeoLocation = customerGeoLocation;
	}
	
	// 고객 생일
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	// 결혼 여부
	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	
	// 성별
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	// 소득 수준
	public String getIncomeLevel() {
		return incomeLevel;
	}

	public void setIncomeLevel(String incomeLevel) {
		this.incomeLevel = incomeLevel;
	}
	
	public String getCustomerAddress() {
		StringBuilder builder = new StringBuilder();

		builder.append(streetAddress).append(", ")
		.append(city).append(" ")
		.append(stateProvince).append(", ").append(countryId)
		.append(" (p:").append(postalCode).append(")");
		
		return builder.toString();
	}
	
}
