package ru.itmo.squadapp;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ru.itmo.squadapp.model.User;
import ru.itmo.squadapp.model.UserInfo;
import ru.itmo.squadapp.model.UserRole;
import ru.itmo.squadapp.services.UserEventService;
import ru.itmo.squadapp.services.UserService;
import ru.itmo.squadapp.util.VKUtils;
import ru.itmo.squadapp.vk.VKBean;

@RestController
@RequestMapping("/")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserEventService userEventService;
	
	@Autowired
	private VKBean vk;
	
	@GetMapping("/api/users")
	public List<User> getAllUsers() {
		return userService.getAll();
	}
	
	@GetMapping("/api/user/{vk_id}")
	public User getUser(@RequestParam(name = "vk_id") String vkId) {	
		return userService.getByVkId(vkId);
	}
	
	@PostMapping(value = "/api/registration", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<User> registerUser(@RequestBody String payload) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			User user = mapper.readValue(payload, User.class);
			user.setVkId(VKUtils.getVKId(user.getVkId()));
			user.setRole(UserRole.CANDIDATE);
			
			user.setId(userService.addUser(user));
			
			return ResponseEntity.ok(user); 
		} catch (ConstraintViolationException e) {
			return ResponseEntity.badRequest().build();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/api/login")
	public ResponseEntity<User> authUserByVkId(@RequestParam(name = "vk_id") String vkId) {		
		User user = userService.getByVkId(VKUtils.getVKId(vkId));
		
		return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
	}
	
	@GetMapping("api/user/info")
	public UserInfo getUserInfo(@RequestParam(name = "vk_id") String vkId) {
		vkId = VKUtils.getVKId(vkId);
		User user = userService.getByVkId(vkId);
		
		if (user == null)
			return null;
		
		UserInfo userInfo = new UserInfo(user);
		userInfo.setPoints(userService.getUserPointsForEvents(user.getId()));
		userInfo.setPhotoURI(vk.getPhotoURI(vkId));
		
		return userInfo;
	}
	
	@DeleteMapping(value = "/api/user", consumes = MediaType.TEXT_PLAIN_VALUE)
	public boolean removeUser(@RequestBody String payload) {
		JsonObject eventJsonObject = JsonParser.parseString(payload).getAsJsonObject();
		
		int id = eventJsonObject.get("id").getAsInt();
		
		return userEventService.removeEventsForUser(id) && userService.removeUser(id);
	}
}
