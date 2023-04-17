package com.example.resico.data.model;

import androidx.annotation.NonNull;

import com.example.resico.utils.DateTimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Data class that holds per-event data.
 */
public class Event {
	private static final String DATE_FORMAT = "dd MMM yyyy";
	private static final String TIME_FORMAT = "h:mm a";
	private final String eventId;
	private final String title;
	private final String venue;
	private final String imageUrl;
	private final String hostId;
	private final String participantCount;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String detail;
	private Boolean hasApplied;
	private Boolean hasBookmarked;
	public Event(String eventId,
	             LocalDateTime startDateTime,
	             LocalDateTime endDateTime,
	             String title,
	             String detail,
	             String venue,
	             String imageUrl,
	             String hostId,
	             String participantCount,
	             Boolean hasApplied,
	             Boolean hasBookmarked) {
		this.eventId = eventId;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.title = title;
		this.detail = detail;
		this.venue = venue;
		this.imageUrl = imageUrl;
		this.hostId = hostId;
		this.participantCount = participantCount;
		this.hasApplied = hasApplied;
		this.hasBookmarked = hasBookmarked;
	}

	public Event(String eventId,
	             String startDate,
	             String startTime,
	             String endDate,
	             String endTime,
	             String title,
	             String detail,
	             String venue,
	             String imageUrl,
	             String hostId,
	             String participantCount,
	             Boolean hasApplied,
	             Boolean hasBookmarked) {
		this.eventId = eventId;
		this.startDateTime = DateTimeUtils.convertDateTimeStringToLocalDateTime(startDate, startTime);
		this.endDateTime = DateTimeUtils.convertDateTimeStringToLocalDateTime(endDate, endTime);
		this.title = title;
		this.detail = detail;
		this.venue = venue;
		this.imageUrl = imageUrl;
		this.hostId = hostId;
		this.participantCount = participantCount;
		this.hasApplied = hasApplied;
		this.hasBookmarked = hasBookmarked;
	}

	/**
	 * Creates an event object from a {@link JSONObject}.
	 *
	 * @param jsonObject {@link JSONObject} obtained from an API request response.
	 * @return An {@link Event} object with fields filled from the {@link JSONObject}. Returns null if any exception is encountered.
	 */
	public static Event buildFromJSONObject(JSONObject jsonObject) {
		try {
			return new Event(
					jsonObject.getString(API_FIELDS.EVENT_ID),
					jsonObject.getString(API_FIELDS.START_DATE),
					jsonObject.getString(API_FIELDS.START_TIME),
					jsonObject.getString(API_FIELDS.END_DATE),
					jsonObject.getString(API_FIELDS.END_TIME),
					jsonObject.getString(API_FIELDS.TITLE),
					jsonObject.optString(API_FIELDS.DETAIL, ""),
					jsonObject.getString(API_FIELDS.VENUE),
					jsonObject.getString(API_FIELDS.IMAGE_URL),
					jsonObject.getString(API_FIELDS.HOST_ID),
					jsonObject.getString(API_FIELDS.PARTICIPANT_COUNT),
					false, false
			);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getEventId() {
		return eventId;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setStartDateTime(String date, String time) {
		startDateTime = DateTimeUtils.convertDateTimeStringToLocalDateTime(date, time);
	}

	public String getStartDateFormatted() {
		return startDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
	}

	public String getStartTimeFormatted() {
		return startDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public void setEndDateTime(String date, String time) {
		endDateTime = DateTimeUtils.convertDateTimeStringToLocalDateTime(date, time);
	}

	public String getEndDateFormatted() {
		return endDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
	}

	public String getEndTimeFormatted() {
		return endDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
	}

	public String getDateTimeRangeFormatted() {
		if (getStartDateFormatted().equals(getEndDateFormatted())) {
			// Same day, show event time range
			return String.format("%s %s - %s", getStartDateFormatted(), getStartTimeFormatted(), getEndTimeFormatted());
		}
		// Across multiple days
		return String.format("%s %s - %s %s", getStartDateFormatted(), getStartTimeFormatted(), getEndDateFormatted(), getEndTimeFormatted());
	}

	public String getTitle() {
		return title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getVenue() {
		return venue;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getHostId() {
		return hostId;
	}

	public String getParticipantCount() {
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

	@NonNull
	@Override
	public String toString() {
		final String den = ": ";
		final String sep = ", ";
		return "{" +
				API_FIELDS.EVENT_ID + den + eventId + sep +
				API_FIELDS.TITLE + den + title + sep +
				API_FIELDS.START_DATE + den + startDateTime.toString() + sep +
				API_FIELDS.END_DATE + den + endDateTime.toString() + sep +
				API_FIELDS.DETAIL + den + detail + sep +
				API_FIELDS.VENUE + den + venue + sep +
				API_FIELDS.IMAGE_URL + den + imageUrl + sep +
				API_FIELDS.HOST_ID + den + hostId + sep +
				API_FIELDS.PARTICIPANT_COUNT + den + participantCount + sep +
				API_FIELDS.HAS_APPLIED + den + hasApplied + sep +
				API_FIELDS.HAS_BOOKMARKED + den + hasBookmarked +
				"}";
	}

	public interface API_FIELDS {
		String EVENT_ID = "eventId";
		String START_DATE = "startDate";
		String START_TIME = "startTime";
		String END_DATE = "endDate";
		String END_TIME = "endTime";
		String TITLE = "title";
		String VENUE = "venue";
		String DETAIL = "detail";
		String IMAGE_URL = "imageUrl";
		String HOST_ID = "hostUserId";
		String PARTICIPANT_COUNT = "participants";
		String HAS_APPLIED = "isApplied";
		String HAS_BOOKMARKED = "isBookmarked";
	}

	public static class CompareMostRecent implements Comparator<Event> {
		@Override
		public int compare(Event event, Event t1) {
			if (event.getStartDateTime().isBefore(t1.getStartDateTime())) return -1;
			if (event.getStartDateTime().isEqual(t1.getStartDateTime())) return 0;
			return 1;
		}
	}

	public static class CompareMostParticipants implements Comparator<Event> {
		@Override
		public int compare(Event event, Event t1) {
			return Integer.parseInt(t1.getParticipantCount()) - Integer.parseInt(event.getParticipantCount());
		}
	}
}
