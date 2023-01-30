package ru.itmo.squadapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.itmo.squadapp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {	
	List<User> findAll();
	
	User findByVkId(String vkId);
	
	User findByPhoneNumber(String phoneNumber);
	
	User findByEmail(String email);
	
	@Query(value = "SELECT * FROM count_user_points_for_all_events(:_user_id);", nativeQuery = true)
	double countPointsForEvents(@Param("_user_id") Integer id);
	
	@Modifying
	@Query(value = "INSERT INTO users values(:id, :name, :surname, :middle_name, CAST(:role AS user_role), :vk_id, :phone_number, :email)", nativeQuery = true)
	void addUser(int id, String name, String surname, @Param("middle_name") String middleName, String role, @Param("vk_id") String vkId, @Param("phone_number") String phoneNumber, String email);

	@Query(value = "SELECT users.* FROM users_events JOIN users ON users_events.user_id = users.id WHERE users_events.event_id = :event_id", nativeQuery = true)
	List<User> getParticipants(@Param("event_id") int eventId);
}
