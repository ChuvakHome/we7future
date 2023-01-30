package ru.itmo.squadapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.itmo.squadapp.model.EventType;
import ru.itmo.squadapp.model.User;
import ru.itmo.squadapp.model.vk.VKGroup;
import ru.itmo.squadapp.model.vk.VKPost;
import ru.itmo.squadapp.services.EventService;
import ru.itmo.squadapp.services.UserEventService;
import ru.itmo.squadapp.services.UserService;
import ru.itmo.squadapp.util.EventUtil;
import ru.itmo.squadapp.util.UserUtil;
import ru.itmo.squadapp.util.Utils;
import ru.itmo.squadapp.util.VKUtils;
import ru.itmo.squadapp.vk.VKBean;

@Controller
@RequestMapping("/")
public class ViewController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserEventService userEventService;
	
	@Autowired
	private VKBean vk;
	
	@GetMapping(value = "/users")
	public String getUsersPage(Model model) {		
		model.addAttribute("users_list", userService.getAll());
		model.addAttribute("vk_utils", VKUtils.class);
		
		return "users";
	}
	
	@GetMapping(value = "/user/{vk_id}")
	public String getUserPage(@PathVariable(name = "vk_id") String vkId, Model model) {
		User user = userService.getByVkId(vkId);
		
		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("user_display_name", String.format("%s %s", user.getName(), user.getSurname()));
			model.addAttribute("user_display_role", UserUtil.translateRole(user.getRole()));
			model.addAttribute("user_events_points", userService.getUserPointsForEvents(user.getId()));
			model.addAttribute("user_photo_uri", vk.getPhotoURI(vkId));
			model.addAttribute("utils", EventUtil.class);
			model.addAttribute("user_events", userEventService.getEventsForUserByVkId(vkId));
			
			return "user";
		}
		else
			return "redirect:/users";
	}
	
	@GetMapping(value = "/profile/{vk_id}")
	public String geProfilePage(@PathVariable(name = "vk_id") String vkId, Model model) {
		User user = userService.getByVkId(vkId);
		
		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("user_display_name", String.format("%s %s", user.getName(), user.getSurname()));
			model.addAttribute("user_display_role", UserUtil.translateRole(user.getRole()));
			model.addAttribute("user_events_points", userService.getUserPointsForEvents(user.getId()));
			model.addAttribute("user_photo_uri", vk.getPhotoURI(vkId));
			model.addAttribute("utils", EventUtil.class);
			model.addAttribute("user_events", userEventService.getEventsForUserByVkId(vkId));
			
			return "profile";
		}
		else
			return "redirect:/users";
	}
	
	@GetMapping(value = "/register")
	public String getRegisterPage() {		
		return "register";
	}
	
	@GetMapping(value = "/login")
	public String getLoginPage() {		
		return "login";
	}
	
	@GetMapping(value = "/events")
	public String getEventsPage(Model model) {
		model.addAttribute("events", eventService.getAll());
		model.addAttribute("event_types", EventType.values());
		model.addAttribute("utils", EventUtil.class);
		
		return "events";
	}
	
	@GetMapping(value = "/locations")
	public String getLocationPage() {
		return "locations";
	}
	
	@GetMapping(value = "/")
	public String getStartPage(Model model) {
		VKGroup vkGroup = vk.getGroupInfo("we7future_shvm");
		List<VKPost> postList = vk.getPosts(vkGroup.getGroupId());
		
		model.addAttribute("group", vkGroup);
		model.addAttribute("posts", postList);
		model.addAttribute("utils", Utils.class);
		model.addAttribute("vk_utils", VKUtils.class);
		
		return "index";
	}
	
	@GetMapping(value = "/calendar")
	public String getCalendarPage(Model model) {
		model.addAttribute("event_types", EventType.values());
		model.addAttribute("utils", EventUtil.class);
		
		return "calendar";
	}
}
