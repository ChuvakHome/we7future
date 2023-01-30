package ru.itmo.squadapp.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.itmo.squadapp.model.EventType;

public class EventUtil {
	private static Map<EventType, String> translatedEventTypes;
	private static List<String> translatedEvents;
	
	static {
		translatedEventTypes = new HashMap<EventType, String>();
		
		translatedEventTypes.put(EventType.SPBSO, "Мероприятие СПбСО");
		translatedEventTypes.put(EventType.SHSO, "Мероприятие ШСО");
		translatedEventTypes.put(EventType.COMISSARKA, "Комиссарка");
		translatedEventTypes.put(EventType.MEETING, "Собрание");
		translatedEventTypes.put(EventType.SCHOOL, "ШВМ");
		translatedEventTypes.put(EventType.PREPARATION, "Подготовка");
		translatedEventTypes.put(EventType.INTERSQUAD, "Межотрядка");
		translatedEventTypes.put(EventType.AGITATION, "Агитка");
		translatedEventTypes.put(EventType.VOLUNTEERING, "Волонтёрство");
		
		translatedEvents = Collections.unmodifiableList(new ArrayList<String>(translatedEventTypes.values()));
	}
	
	public static List<String> getAllTranslatedEventTypes() {
		return translatedEvents;
	}
	
	public static String translateEventType(EventType type) {
		return translatedEventTypes.get(type);
	}
	
	public static String formatDatetime(Timestamp timestamp) {
		return Utils.formatDate(timestamp);
	}
}
