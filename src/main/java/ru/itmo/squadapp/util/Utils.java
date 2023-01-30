package ru.itmo.squadapp.util;

import java.util.Date;

public class Utils {
	public static String formatDate(Date d) {
		return String.format("%td.%tm.%tY %tH:%tM", d, d, d, d, d);
	}
}
