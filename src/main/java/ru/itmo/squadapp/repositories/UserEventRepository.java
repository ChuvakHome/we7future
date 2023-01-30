package ru.itmo.squadapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.itmo.squadapp.model.Event;

public interface UserEventRepository extends JpaRepository<Event, Integer> {
	// SELECT ev from users_events JOIN Event ev ON users_events.event_id = ev.id JOIN User u ON users_events.user_id = u.id WHERE u.vk_id = 
	@Query(value = "SELECT ev.id, ev.name, ev.date_and_time, ev.level, ev.description from users_events JOIN events ev ON users_events.event_id = ev.id JOIN users u ON users_events.user_id = u.id WHERE u.vk_id = :vk_id", nativeQuery = true)
	List<Event> findEventsForUserByVkId(@Param("vk_id") String vkId);
	
	@Query(value = "SELECT ev from users_events JOIN events ev ON users_events.event_id = ev.id WHERE users_events.user_id = :id", nativeQuery = true)
	List<Event> findEventsForUserById(int id);
	
	@Query(value = "SELECT users_events.event_id from users_events JOIN users u ON users_events.user_id = u.id WHERE u.vk_id = :vk_id AND users_events.event_id = :id", nativeQuery = true)
	Optional<Integer> findEventForUser(int id, @Param("vk_id") String vkId);
	
	@Query(value = "SELECT MAX(id) from users_events", nativeQuery = true)
	int getMaxId();
	
	@Modifying
	@Query(value = "INSERT INTO users_events VALUES (:id, :user_id, :event_id, CAST(:status AS participant_status))", nativeQuery = true)
	void addUserEvent(int id, @Param("user_id") int userId, @Param("event_id") int eventId, String status);
	
	@Query(value = "SELECT CAST(status AS text) FROM users_events ue WHERE ue.user_id = :user_id AND ue.event_id = :event_id", nativeQuery = true)
	Optional<String> findUserStatusForEvent(@Param("user_id") int userId, @Param("event_id") int eventId);
	
	@Modifying
	@Query(value = "DELETE FROM users_events WHERE users_events.event_id = :event_id AND users_events.user_id = :user_id", nativeQuery = true)
	void removeEventForUser(@Param("event_id") int eventId, @Param("user_id") int userId);
	
	@Modifying
	@Query(value = "DELETE FROM users_events WHERE users_events.user_id = :user_id", nativeQuery = true)
	void removeEventsForUser(@Param("user_id") int userId);
	
	@Modifying
	@Query(value = "DELETE FROM users_events WHERE users_events.event_id = :event_id", nativeQuery = true)
	void removeEventForAllUsers(@Param("event_id") int eventId);
}
