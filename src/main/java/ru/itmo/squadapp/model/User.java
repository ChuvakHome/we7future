package ru.itmo.squadapp.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {
	private static final long serialVersionUID = 2553485233418017225L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column 
	private String name;
	
	@Column
	private String surname;
	
	@Column(name = "middle_name")
	private String middleName;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "user_role")
	private UserRole role; 
	
	@Column(name = "vk_id")
	private String vkId;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column
	private String email;
	
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
}
