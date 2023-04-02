package com.example.resico.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeCalc {
	/**
	 * Calculate duration of a date until now.
	 *
	 * @param fromDateTime A time in the past!
	 * @return most significant duration from input dateTime to now.
	 */
	public static String getDurationToNow(LocalDateTime fromDateTime) {
		LocalDateTime toDateTime = LocalDateTime.now();
		LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

		long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
		tempDateTime = tempDateTime.plusYears(years);
		if (years != 0) return years + "y";

		long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
		tempDateTime = tempDateTime.plusMonths(months);
		if (months != 0) return months + "mo";

		long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
		tempDateTime = tempDateTime.plusDays(days);
		if (days != 0) return days + "d";

		long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
		tempDateTime = tempDateTime.plusHours(hours);
		if (hours != 0) return hours + "h";

		long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
		tempDateTime = tempDateTime.plusMinutes(minutes);
		if (minutes != 0) return minutes + "m";

		long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);
		return seconds + "s";
	}
}
