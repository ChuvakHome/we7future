package ru.itmo.squadapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.itmo.squadapp.model.Event;
import ru.itmo.squadapp.repositories.EventRepository;

@Service
public class EventService {
	private final EventRepository repo;
	
	public EventService(EventRepository eventRepository) {
		repo = eventRepository;
	}
	
	@Transactional
	public List<Event> getAll() {
		return repo.findAll();
	}
	
	@Transactional
	public List<Event> getEventsForPeriod(String start, String end) {
		return repo.getEventsForPeriod(start, end);
	}
	
	@Transactional
	public Event addEvent(Event event) {
		int id = event.getId();
		
		if (id == 0)
			id = (int) (repo.getMaxId() + 1);
		
		Event temp = repo.findByName(event.getName());
		
		if (temp != null)
			return null;
		
		repo.addEvent(id, event.getName(), event.getDateTime(), event.getEventType().name(), event.getDescription());
		event.setId(id);
		
		return event;
	}
	
	@Transactional
	public boolean removeEvent(int id) {
		repo.deleteById(id);
		
		return true;
	}
	
	@Transactional
	public boolean updateEvent(int eventId, Event newEvent) {
		repo.updateEvent(eventId, newEvent.getName(), newEvent.getDateTime(), newEvent.getEventType().name(), newEvent.getDescription());
		
		return true;
	}
	
	@Transactional
	public Event getById(int id) {
		Optional<Event> eventOptional = repo.findById(id);
		
		return eventOptional.isPresent() ? eventOptional.get() : null;
	}
}
