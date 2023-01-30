package ru.itmo.squadapp.model;

import java.io.Serializable;
import java.net.URI;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = -2432391090692020069L;
	
	private int id;
	private String name;
	private String surname;
	private String middleName;
	private UserRole role;
	private String vkId;
	private String phoneNumber;
	private String email;
	private double points;
	private URI photoURI;
	
	public UserInfo(User user) {
		if (user != null) {
			this.id = user.getId();
			this.name = user.getName();
			this.surname = user.getSurname();
			this.middleName = user.getMiddleName();
			this.role = user.getRole();
			this.vkId = user.getVkId();
			this.phoneNumber = user.getPhoneNumber();
			this.email = user.getEmail();
		}
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	public String getMiddleName() {
		return this.middleName;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}
	
	public UserRole getRole() {
		return this.role;
	}
	
	public void setVkId(String vkId) {
		this.vkId = vkId;
	}
	
	public String getVkId() {
		return this.vkId;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setPoints(double points) {
		this.points = points;
	}
	
	public double getPoints() {
		return this.points;
	}
	
	public void setPhotoURI(URI photoURI) {
		this.photoURI = photoURI;
	}
	
	public URI getPhotoURI() {
		return this.photoURI;
	}
}
