package ru.itmo.squadapp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ru.itmo.squadapp.model.Event;
import ru.itmo.squadapp.model.EventType;
import ru.itmo.squadapp.model.User;
import ru.itmo.squadapp.model.UserEvent;
import ru.itmo.squadapp.model.UserEventStatus;
import ru.itmo.squadapp.services.EventService;
import ru.itmo.squadapp.services.UserEventService;
import ru.itmo.squadapp.services.UserService;

@RestController
public class UserEventController {
	@Autowired
	private UserEventService userEventService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@GetMapping("/api/user/events")
	public List<Event> getEventsForUser(@RequestParam(name = "vk_id") String vkId) {	
		return userEventService.getEventsForUserByVkId(vkId);
	}
	
	@PostMapping(value = "/api/user/events", consumes = MediaType.TEXT_PLAIN_VALUE)
	public boolean addEventToUser(@RequestBody String payload) {
		JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
		
		int userId = jsonObject.get("userId").getAsInt();
		int eventId = jsonObject.get("eventId").getAsInt();
		
		return userEventService.getUserStatusForEvent(eventId, userId) == null && userEventService.addEventForUser(userId, eventId, UserEventStatus.PARTICIPANT);
	}
	
	@DeleteMapping(value = "/api/user/events", consumes = MediaType.TEXT_PLAIN_VALUE)
	public boolean deleteEventToUser(@RequestBody String payload) {
		JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
		
		int userId = jsonObject.get("userId").getAsInt();
		int eventId = jsonObject.get("eventId").getAsInt();
		
		return userEventService.getUserStatusForEvent(eventId, userId) != null && userEventService.removeEventForUser(userId, eventId);
	}
	
	@PutMapping(value = "/api/user/events", consumes = MediaType.TEXT_PLAIN_VALUE)
	public boolean updateEvent(@RequestBody String payload) {
		JsonObject eventJsonObject = JsonParser.parseString(payload).getAsJsonObject();
		
		int userId = eventJsonObject.get("creatorId").getAsInt();
		int eventId = eventJsonObject.get("eventId").getAsInt();
		
		Event event = new Event();
		event.setId(eventId);
		event.setName(eventJsonObject.get("name").getAsString());
		event.setEventType(EventType.valueOf(eventJsonObject.get("eventType").getAsString()));
		event.setDateTime(Timestamp.valueOf(LocalDateTime.parse(eventJsonObject.get("dateTime").getAsString())));
		event.setDescription(eventJsonObject.get("description").getAsString());
		
		return UserEventStatus.ORGANIZER.equals(userEventService.getUserStatusForEvent(eventId, userId)) && eventService.updateEvent(eventId, event);
	}
	
	@GetMapping(value = "/api/user/events/participants")
	public List<User> getParticipants(@RequestParam("event_id") int id) {
		return userService.getParticipants(id);
	}
	
	@DeleteMapping(value = "/api/event", consumes = MediaType.TEXT_PLAIN_VALUE)
	public boolean deleteEvent(@RequestBody String payload) {		
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			UserEvent userEvent = mapper.readValue(payload, UserEvent.class);
			
			int userId = userEvent.getUserId();
			int eventId = userEvent.getEventId();
			
			if (UserEventStatus.ORGANIZER.equals(userEventService.getUserStatusForEvent(eventId, userId)))
				return userEventService.removeEventForAllUsers(eventId) && eventService.removeEvent(eventId);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@GetMapping("/api/user/events/is_organizer")
	public boolean isEventOrganizer(@RequestParam(name = "user_id") int userId, @RequestParam(name = "event_id") int eventId) {
		return UserEventStatus.ORGANIZER.equals(userEventService.getUserStatusForEvent(eventId, userId));
	}
	
	@GetMapping("/api/user/events/is_present")
	public boolean isEventPresent(@RequestParam(name = "event_id") int eventId, @RequestParam(name = "vk_id") String vkId) {
		return userEventService.isEventPresentForUser(eventId, vkId);
	}
}
