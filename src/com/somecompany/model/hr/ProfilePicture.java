package com.somecompany.model.hr;

public class ProfilePicture {
	private int profilePictureId;
	private int employeeId;
	private String pictureFileName;
	
	public int getProfilePictureId() {
		return profilePictureId;
	}
	
	public void setProfilePictureId(int profilePictureId) {
		this.profilePictureId = profilePictureId;
	}
	
	public int getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getPictureFileName() {
		return pictureFileName;
	}
	
	public void setPictureFileName(String pictureFileName) {
		this.pictureFileName = pictureFileName;
	}
}
