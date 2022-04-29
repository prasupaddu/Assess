package com.equifax.AirPollutionApp.entity;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="userregistration")
public class UserRegistration {
    @Id
    @GeneratedValue
	private long id;
	/*
	 * @Column private String firstName;
	 * 
	 * @Column private String lastName;
	 */
    @Column
    private String username;
    @Column
    private String password;
    @Column
	private String city;
    @Column
	private String email;
    @Column
	private String mobile;
    @Column
    private String fileName;
    @Column
	@Lob
	private byte[] data;
    
    @Column(columnDefinition = "varchar default user")
    private String userType="admin";
    @Column(columnDefinition = "boolean default true")
   private boolean activated;
	public long getId() {
		return id;
	}
	
	
	
	public UserRegistration() {
		super();
	}



	public UserRegistration(String username, String password, String city, String email, String mobile, String fileName,
			byte[] data, String userType, boolean activated) {
		super();
		this.username = username;
		this.password = password;
		this.city = city;
		this.email = email;
		this.mobile = mobile;
		this.fileName = fileName;
		this.data = data;
		this.userType = userType;
		this.activated = activated;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return userType;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	
	
}
