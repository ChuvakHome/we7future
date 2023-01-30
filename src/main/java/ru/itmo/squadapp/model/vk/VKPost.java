package ru.itmo.squadapp.model.vk;

import java.util.Date;

public class VKPost {
	private int id;
	private String text;
	private Date date;
	private int ownerId;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	public int getOwnerId() {
		return this.ownerId;
	}
}
