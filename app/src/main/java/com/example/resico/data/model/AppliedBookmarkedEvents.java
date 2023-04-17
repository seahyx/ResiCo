package com.example.resico.data.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppliedBookmarkedEvents {
	private final String[] appliedEvents;
	private final String[] bookmarkedEvents;
	public AppliedBookmarkedEvents(String[] appliedEvents, String[] bookmarkedEvents) {
		this.appliedEvents = appliedEvents;
		this.bookmarkedEvents = bookmarkedEvents;
	}

	public static AppliedBookmarkedEvents buildFromJSONObject(JSONObject jsonObject) {
		// Build list of event ids that the user has applied to
		JSONArray appliedEventsJson;
		String[] appliedEvents = new String[0];
		try {
			appliedEventsJson = jsonObject.getJSONArray(API_FIELDS.APPLIED_EVENTS_LIST);
			appliedEvents = new String[appliedEventsJson.length()];
			for (int i = 0; i < appliedEventsJson.length(); i++) {
				appliedEvents[i] = appliedEventsJson.getString(i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Build list of event ids that the user has bookmarked
		JSONArray bookmarkedEventsJson;
		String[] bookmarkedEvents = new String[0];
		try {
			bookmarkedEventsJson = jsonObject.getJSONArray(API_FIELDS.BOOKMARKED_EVENTS_LIST);
			bookmarkedEvents = new String[bookmarkedEventsJson.length()];
			for (int i = 0; i < bookmarkedEventsJson.length(); i++) {
				bookmarkedEvents[i] = bookmarkedEventsJson.getString(i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new AppliedBookmarkedEvents(appliedEvents, bookmarkedEvents);
	}

	public String[] getAppliedEvents() {
		return appliedEvents;
	}

	public String[] getBookmarkedEvents() {
		return bookmarkedEvents;
	}

	@NonNull
	@Override
	public String toString() {
		final String den = ": ";
		final String sep = ", ";
		return "{" +
				API_FIELDS.APPLIED_EVENTS_LIST + den + String.join(", ", appliedEvents) + sep +
				API_FIELDS.BOOKMARKED_EVENTS_LIST + den + String.join(", ", bookmarkedEvents) +
				"}";
	}

	public interface API_FIELDS {
		String APPLIED_EVENTS_LIST = "appliedEventsId";
		String BOOKMARKED_EVENTS_LIST = "likedEventId";
	}
}
