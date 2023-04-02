package com.example.resico.data.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data class that holds per-announcement data.
 */
public class Announcement {
	public interface API_FIELDS {
		String ANNOUNCEMENT_ID = "announcementId";
		String START_DATE = "startDate";
		String START_TIME = "startTime";
		String DETAIL = "detail";
		String TITLE = "title";
	}

	private static final String DATE_FORMAT = "dd MMM yyyy";
	private static final String TIME_FORMAT = "h:mm a";
	private final String announcementId;
	private LocalDateTime postDateTime;
	private final String title;
	private String detail;

	public Announcement(String announcementId, LocalDateTime postDateTime, String title, String detail) {
		this.announcementId = announcementId;
		this.postDateTime = postDateTime;
		this.title = title;
		this.detail = detail;
	}

	public Announcement(String announcementId, String postDate, String postTime, String title, String detail) {
		this.announcementId = announcementId;
		this.postDateTime = Event.convertDateTimeIntToDate(postDate, postTime);
		this.title = title;
		this.detail = detail;
	}

	public static Announcement buildFromJSONObject(JSONObject jsonObject, String announcementId) {
		try {
			return new Announcement(
					announcementId,
					jsonObject.getString(API_FIELDS.START_DATE),
					jsonObject.getString(API_FIELDS.START_TIME),
					jsonObject.getString(API_FIELDS.TITLE),
					jsonObject.optString(API_FIELDS.DETAIL, "")
			);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getAnnouncementId() {
		return announcementId;
	}

	public LocalDateTime getPostDateTime() {
		return postDateTime;
	}

	public String getPostDateFormatted() {
		return postDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
	}

	public String getPostTimeFormatted() {
		return postDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
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

	@NonNull
	@Override
	public String toString() {
		final String den = ": ";
		final String sep = ", ";
		return "{" +
				API_FIELDS.ANNOUNCEMENT_ID + den + announcementId + sep +
				API_FIELDS.TITLE + den + title + sep +
				API_FIELDS.START_DATE + den + postDateTime.toString() + sep +
				API_FIELDS.DETAIL + den + detail +
				"}";
	}
}
