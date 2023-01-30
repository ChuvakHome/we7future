package ru.itmo.squadapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.itmo.squadapp.model.Event;
import ru.itmo.squadapp.model.EventDTO;
import ru.itmo.squadapp.model.UserEventStatus;
import ru.itmo.squadapp.services.EventService;
import ru.itmo.squadapp.services.UserEventService;

@RestController
public class EventController {
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserEventService userEventService;
	
	@GetMapping("/api/events")
	public List<Event> getEvents() {
		return eventService.getAll();
	}
	
	@GetMapping("/api/events/get_for_period")
	public List<Event> getEventForPeriod(@RequestParam String start, @RequestParam String end) {
		return eventService.getEventsForPeriod(start, end);
	}
	
	@GetMapping("/api/events/get") 
	public Event getEvent(@RequestParam("event_id") int eventId) {
		return eventService.getById(eventId);
	}
	
	@PostMapping(value = "/api/event", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<EventDTO> addEvent(@RequestBody String payload) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			EventDTO eventDTO = (EventDTO) objectMapper.readValue(payload, EventDTO.class);
			
			Event addedEvent = eventService.addEvent(eventDTO.toEvent()); 
			
			if (addedEvent == null)
				return ResponseEntity.badRequest().build();
			
			EventDTO resultDTO = new EventDTO();
			resultDTO.setName(addedEvent.getName());
			resultDTO.setDateTime(addedEvent.getDateTime().toString());
			resultDTO.setEventType(addedEvent.getEventType().name());
			resultDTO.setDescription(addedEvent.getDescription());
			resultDTO.setCreatorId(eventDTO.getCreatorId());
			
			userEventService.addEventForUser(eventDTO.getCreatorId(), addedEvent.getId(), UserEventStatus.ORGANIZER);
			
			return ResponseEntity.ok(resultDTO);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.badRequest().build();
	}
}
