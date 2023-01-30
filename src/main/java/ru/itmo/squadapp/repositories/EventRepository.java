package ru.itmo.squadapp.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.itmo.squadapp.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
	Event findByName(String name);
	
	@Modifying
	@Query(value = "INSERT INTO events values(:id, :name, :date_and_time, CAST(:level AS event_type), :description)", nativeQuery = true)
	void addEvent(int id, String name, @Param("date_and_time") Timestamp datetime, @Param("level") String eventType, String description);

	@Query(value = "SELECT MAX(id) from events", nativeQuery = true)
	int getMaxId();
	
	@Query(value = "SELECT id, name, date_and_time, level, description FROM events WHERE CAST(date_and_time AS date) >= CAST(:start AS date) AND CAST(date_and_time AS date) <= CAST(:end AS date)", nativeQuery = true)
	List<Event> getEventsForPeriod(String start, String end);
	
	@Modifying
	@Query(value = "UPDATE events SET name = :name, date_and_time = :date_and_time, level = CAST(:level AS event_type), description = :description WHERE events.id = :id", nativeQuery = true)
	void updateEvent(int id, String name, @Param("date_and_time") Timestamp datetime, @Param("level") String eventType, String description);
}
