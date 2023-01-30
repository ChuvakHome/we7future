package ru.itmo.squadapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.itmo.squadapp.model.User;
import ru.itmo.squadapp.repositories.UserRepository;

@Service
public class UserService {
	private final UserRepository repo;
	
	public UserService(UserRepository userRepository) {
		repo = userRepository;
	}
	
	@Transactional
	public List<User> getAll() {
		return repo.findAll();
	}
	
	@Transactional
	public User getById(int id) {
		Optional<User> user = repo.findById(id);
		
		if (user.isPresent())
			return user.get();
		else
			throw new RuntimeException("No user with id: " + id);
	}
	
	@Transactional
	public User getByVkId(String vkId) {
		return repo.findByVkId(vkId);
	}
	
	@Transactional
	public double getUserPointsForEvents(int id) {
		return repo.countPointsForEvents(id);
	}
	
	@Transactional
	public int addUser(User user) {
		int id = user.getId();
		
		if (id == 0)
			id = (int) (countAll() + 1);
		
		User u = repo.findByVkId(user.getVkId());
		
		if (u == null) {
			u = repo.findByPhoneNumber(user.getPhoneNumber());
			
			if (u == null) {
				u = repo.findByEmail(user.getEmail());
			}
		}
		
		if (u != null)
			return -1;
		
		repo.addUser(id, user.getName(), user.getSurname(), user.getMiddleName(), user.getRole().name(), user.getVkId(), user.getPhoneNumber(), user.getEmail());
		
		return id;
	}
	
	@Transactional
	public int countAll() {
		return (int) repo.count();
	}
	
	@Transactional
	public List<User> getParticipants(int eventId) {
		return repo.getParticipants(eventId);
	}
	
	@Transactional
	public boolean removeUser(int id) {
		repo.deleteById(id);
		
		return true;
	}
}
