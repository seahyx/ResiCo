package com.example.resico.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class ForumComment {
	public interface API_FIELDS {
		String USER_ID = "userId";
		String COMMENT = "comment";
		String POST_DATE = "postDate";
		String POST_TIME = "postTime";
		String LIKE_USER_ID = "likeUserId";
	}
	private final String userId;
	private final String comment;
	private final LocalDateTime postDateTime;
	@Nullable
	private final String[] likeUserId;

	public ForumComment(String userId, String comment, LocalDateTime postDateTime, String[] likeUserId) {
		this.userId = userId;
		this.comment = comment;
		this.postDateTime = postDateTime;
		this.likeUserId = likeUserId;
	}

	public ForumComment(String userId, String comment, String postDate, String postTime, String[] likeUserId) {
		this.userId = userId;
		this.comment = comment;
		this.postDateTime = Event.convertDateTimeIntToDate(postDate, postTime);
		this.likeUserId = likeUserId;
	}

	public static ForumComment buildFromJSONObject(JSONObject jsonObject) {
		try {
			// likeUserId is an array, we must extract it
			JSONArray likeUserIdJSONArray = jsonObject.optJSONArray(API_FIELDS.LIKE_USER_ID);
			String[] likeStrArray = null;
			if (likeUserIdJSONArray != null) {
				likeStrArray = new String[likeUserIdJSONArray.length()];
				for (int i = 0; i < likeStrArray.length; i++) {
					likeStrArray[i] = likeUserIdJSONArray.getString(i);
				}
			}
			return new ForumComment(
					jsonObject.optString(API_FIELDS.USER_ID),
					jsonObject.getString(API_FIELDS.COMMENT),
					jsonObject.getString(API_FIELDS.POST_DATE),
					jsonObject.getString(API_FIELDS.POST_TIME),
					likeStrArray
			);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getUserId() {
		return userId;
	}

	public String getComment() {
		return comment;
	}

	public LocalDateTime getPostDateTime() {
		return postDateTime;
	}

	public String[] getLikeUserId() {
		return likeUserId;
	}

	@NonNull
	@Override
	public String toString() {
		final String den = ": ";
		final String sep = ", ";
		final String likeUserStr = likeUserId != null ? String.join(", ", likeUserId) : "";
		return "{" +
				API_FIELDS.USER_ID + den + userId + sep +
				API_FIELDS.COMMENT + den + comment + sep +
				API_FIELDS.POST_DATE + den + postDateTime + sep +
				API_FIELDS.LIKE_USER_ID + den + likeUserStr +
				"}";
	}
}
