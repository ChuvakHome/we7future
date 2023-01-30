package ru.itmo.squadapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.itmo.squadapp.model.Event;
import ru.itmo.squadapp.model.UserEventStatus;
import ru.itmo.squadapp.repositories.UserEventRepository;

@Service
public class UserEventService {
	private final UserEventRepository repo;
	
	public UserEventService(UserEventRepository userEventRepository) {
		repo = userEventRepository;
	}
	
	@Transactional
	public List<Event> getEventsForUserByVkId(String vkId) {
		return repo.findEventsForUserByVkId(vkId);
	}

	@Transactional
	public List<Event> getEventsForUserById(int id) {
		return repo.findEventsForUserById(id);
	}
	
	@Transactional
	public boolean isEventPresentForUser(int id, String vkId) {
		return repo.findEventForUser(id, vkId).isPresent();
	}
	
	@Transactional
	public UserEventStatus getUserStatusForEvent(int eventId, int userId) {
		Optional<String> statusOptional = repo.findUserStatusForEvent(userId, eventId);
		
		return statusOptional.isPresent() ? UserEventStatus.valueOf(statusOptional.get()) : null;
	}
	
	@Transactional
	public boolean removeEventForAllUsers(int eventId) {
		repo.removeEventForAllUsers(eventId);
		
		return true;
	}
	
	@Transactional
	public boolean addEventForUser(int userId, int eventId, UserEventStatus status) {	
		repo.addUserEvent(repo.getMaxId() + 1, userId, eventId, status.name());
		
		return true;
	}
	
	@Transactional
	public boolean removeEventForUser(int userId, int eventId) {
		repo.removeEventForUser(eventId, userId);
		
		return true;
	}
	
	@Transactional
	public boolean removeEventsForUser(int userId) {
		repo.removeEventsForUser(userId);
		
		return true;
	}
}
