package ru.itmo.squadapp.util;

import java.util.HashMap;
import java.util.Map;

import ru.itmo.squadapp.model.UserRole;

public class UserUtil {
	private static Map<UserRole, String> translatedRoles;
	
	static {
		translatedRoles = new HashMap<UserRole, String>();
		
		translatedRoles.put(UserRole.CANDIDATE, "Кандидат");
		translatedRoles.put(UserRole.BUDUSCHANIN, "Будущанин");
		translatedRoles.put(UserRole.FIGHTER, "Боец");
		translatedRoles.put(UserRole.COMMANDER, "Командир");
		translatedRoles.put(UserRole.EXTERNAL_COMMISSAR, "Внешний комиссар");
		translatedRoles.put(UserRole.INTERIOR_COMMISSAR, "Внутренний комиссар");
		translatedRoles.put(UserRole.METHODIST, "Методист");
		translatedRoles.put(UserRole.COMMANDANT, "Комендант");
		translatedRoles.put(UserRole.SMM_HEAD, "РПЦ");
		translatedRoles.put(UserRole.FLAG_BEARER, "Флагоносец");
		translatedRoles.put(UserRole.SMM, "ПЦ");
	}
	
	public static String translateRole(UserRole role) {
		return translatedRoles.get(role);
	}
}
