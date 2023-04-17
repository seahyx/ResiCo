package com.example.resico.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {
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

	/**
	 * Converts a date integer in the form of day|month|full-year (e.g. 10052023) and time integer in the form of 24-hour time (e.g. 1800) into a Java {@link java.time.LocalDateTime} object.
	 *
	 * @param date Date integer in the form of day|month|full-year (e.g. 10052023)
	 * @param time Time integer in the form of 24-hour time (e.g. 1800)
	 * @return LocalDateTime object with the input date and time values.
	 */
	public static LocalDateTime convertDateTimeStringToLocalDateTime(String date, String time) {
		String dateTimeStr = date + time;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
		return LocalDateTime.parse(dateTimeStr, formatter);
	}

	/**
	 * Splits a {@link LocalDateTime} object into a date string and time string, with specified formats.
	 *
	 * @param localDateTime
	 * @param dateFormat
	 * @param timeFormat
	 * @return A string array with 2 elements, the date string and time string respectively.
	 */
	public static String[] convertLocalDateTimeToDateAndTime(LocalDateTime localDateTime, String dateFormat, String timeFormat) {
		String[] dateAndTime = new String[2];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat + timeFormat);
		String localDateTimeString = localDateTime.format(formatter);
		dateAndTime[0] = localDateTimeString.substring(0, dateFormat.length());
		dateAndTime[1] = localDateTimeString.substring(dateFormat.length(), dateFormat.length() + timeFormat.length());
		return dateAndTime;
	}
}
