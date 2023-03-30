package com.example.resico.data.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data class that holds per-event data.
 */
public class Event {
	private final Integer eventId;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private final String title;
	private final String overview;
	private final String venue;
	private final String imageUrl;
	private final Integer hostId;
	private final Integer participantCount;
	private Boolean hasApplied;
	private Boolean hasBookmarked;

	public Event(Integer eventId,
	             LocalDateTime startDateTime,
	             LocalDateTime endDateTime,
	             String title,
	             String overview,
	             String venue,
	             String imageUrl,
	             Integer hostId,
	             Integer participantCount,
	             Boolean hasApplied,
	             Boolean hasBookmarked) {
		this.eventId = eventId;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.title = title;
		this.overview = overview;
		this.venue = venue;
		this.imageUrl = imageUrl;
		this.hostId = hostId;
		this.participantCount = participantCount;
		this.hasApplied = hasApplied;
		this.hasBookmarked = hasBookmarked;
	}

	public Integer getEventId() {
		return eventId;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setStartDateTime(String date, String time) {
		startDateTime = convertDateTimeIntToDate(date, time);
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public void setEndDateTime(String date, String time) {
		endDateTime = convertDateTimeIntToDate(date, time);
	}

	public String getTitle() {
		return title;
	}

	public String getOverview() {
		return overview;
	}

	public String getVenue() {
		return venue;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Integer getHostId() {
		return hostId;
	}

	public Integer getParticipantCount() {
		return participantCount;
	}

	public Boolean getHasApplied() {
		return hasApplied;
	}

	public void setHasApplied(Boolean hasApplied) {
		this.hasApplied = hasApplied;
	}

	public Boolean getHasBookmarked() {
		return hasBookmarked;
	}

	public void setHasBookmarked(Boolean hasBookmarked) {
		this.hasBookmarked = hasBookmarked;
	}

	/**
	 * Converts a date integer in the form of day|month|full-year (e.g. 10052023) and time integer in the form of 24-hour time (e.g. 1800) into a Java {@link java.time.LocalDateTime} object.
	 * @param date Date integer in the form of day|month|full-year (e.g. 10052023)
	 * @param time Time integer in the form of 24-hour time (e.g. 1800)
	 * @return LocalDateTime object with the input date and time values.
	 */
	public static LocalDateTime convertDateTimeIntToDate(String date, String time) {
		String dateTimeStr = date + time;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
		return LocalDateTime.parse(dateTimeStr, formatter);
	}
}
