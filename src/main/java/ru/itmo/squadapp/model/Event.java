package ru.itmo.squadapp.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column 
	private String name;
	
	@Column(name = "date_and_time")
	private Timestamp dateTime;
	
	@Column(name = "level")
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	@Column
	private String description;

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
		return name;
	}
	
	public void setDateTime(Timestamp datetime) {
		this.dateTime = datetime;
	}
	
	public Timestamp getDateTime() {
		return dateTime;
	}
	
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
