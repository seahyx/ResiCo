package com.example.resico.data.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class ForumPost {
	public interface API_FIELDS {
		String POST_ID = "postId";
		String TITLE = "title";
		String CONTENT = "content";
		String IMAGE_URL = "imageUrl";
		String POST_DATE = "postDate";
		String POST_TIME = "postTime";
		String POSTER_USER_ID = "posterUserId";
		String LIKE_USER_ID = "likeUserId";
		String COMMENT_COUNT = "commentCount";
	}

	private final String postId;
	private final String title;
	private final String content;
	private final String imageUrl;
	private final LocalDateTime postDateTime;
	private final String posterUserId;
	private final String[] likeUserId;
	private final Integer commentCount;

	public ForumPost(
			String postId,
			String title,
			String content,
			String imageUrl,
			LocalDateTime postDateTime,
			String posterUserId,
			String[] likeUserId,
			Integer commentCount) {
		this.postId = postId;
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
		this.postDateTime = postDateTime;
		this.posterUserId = posterUserId;
		this.likeUserId = likeUserId;
		this.commentCount = commentCount;
	}

	public ForumPost(
			String postId,
			String title,
			String content,
			String imageUrl,
			String postDate,
			String postTime,
			String posterUserId,
			String[] likeUserId,
			Integer commentCount) {
		this.postId = postId;
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
		this.postDateTime = Event.convertDateTimeIntToDate(postDate, postTime);
		this.posterUserId = posterUserId;
		this.likeUserId = likeUserId;
		this.commentCount = commentCount;
	}

	public static ForumPost buildFromJSONObject(JSONObject jsonObject) {
		try {
			// likeUserId is an array, we must extract it
			JSONArray likeUserIdJSONArray = jsonObject.getJSONArray(API_FIELDS.LIKE_USER_ID);
			String[] likeStrArray = new String[likeUserIdJSONArray.length()];
			for (int i = 0; i < likeStrArray.length; i++) {
				likeStrArray[i] = likeUserIdJSONArray.getString(i);
			}
			return new ForumPost(
					jsonObject.getString(API_FIELDS.POST_ID),
					jsonObject.getString(API_FIELDS.TITLE),
					jsonObject.getString(API_FIELDS.CONTENT),
					jsonObject.getString(API_FIELDS.IMAGE_URL),
					jsonObject.getString(API_FIELDS.POST_DATE),
					jsonObject.getString(API_FIELDS.POST_TIME),
					jsonObject.getString(API_FIELDS.POSTER_USER_ID),
					likeStrArray,
					jsonObject.getInt(API_FIELDS.COMMENT_COUNT)
			);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPostId() {
		return postId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getImageUrl() {
		if (Objects.equals(imageUrl, "-")) return "";
		return imageUrl;
	}

	public LocalDateTime getPostDateTime() {
		return postDateTime;
	}

	public String getPosterUserId() {
		return posterUserId;
	}

	public String[] getLikeUserId() {
		return likeUserId;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public static class CompareMostRecent implements Comparator<ForumPost> {
		@Override
		public int compare(ForumPost forumPost, ForumPost t1) {
			if (forumPost.getPostDateTime().isAfter(t1.getPostDateTime())) return -1;
			if (forumPost.getPostDateTime().isEqual(t1.getPostDateTime())) return 0;
			return 1;
		}
	}

	@NonNull
	@Override
	public String toString() {
		final String den = ": ";
		final String sep = ", ";
		return "{" +
				API_FIELDS.POST_ID + den + postId + sep +
				API_FIELDS.TITLE + den + title + sep +
				API_FIELDS.CONTENT + den + content + sep +
				API_FIELDS.POST_DATE + den + postDateTime + sep +
				API_FIELDS.POSTER_USER_ID + den + posterUserId + sep +
				API_FIELDS.IMAGE_URL + den + imageUrl + sep +
				API_FIELDS.LIKE_USER_ID + den + String.join(", ", likeUserId) + sep +
				API_FIELDS.COMMENT_COUNT + den +
				"}";
	}
}
