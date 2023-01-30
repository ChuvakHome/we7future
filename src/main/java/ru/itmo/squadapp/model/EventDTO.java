package ru.itmo.squadapp.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import ru.itmo.squadapp.util.EventUtil;

public class EventDTO {
	private String name;

	private String dateTime;
	
	private String eventType;
	private String translatedEventType;
	
	private String description;
	
	private int creatorId;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDateTime(String datetime) {
		this.dateTime = datetime;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType;
		this.translatedEventType = EventUtil.translateEventType(EventType.valueOf(eventType));
	}
	
	public String getEventType() {
		return eventType;
	}
	
	public String getTranslatedEventType() {
		return translatedEventType;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	
	public int getCreatorId() {
		return creatorId;
	}
	
	public Event toEvent() {
		Event event = new Event();
		event.setName(name);
		event.setDateTime(Timestamp.valueOf(LocalDateTime.parse(dateTime)));
		event.setEventType(EventType.valueOf(eventType));
		event.setDescription(description);
		
		return event;
	}
}
