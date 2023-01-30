package ru.itmo.squadapp.model;

public class UserEvent {
	private int userId;
	private int eventId;
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
	public int getEventId() {
		return this.eventId;
	}
}
