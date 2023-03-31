package com.example.resico.data.network;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.resico.data.Result;
import com.example.resico.data.model.Announcement;
import com.example.resico.data.model.AppliedBookmarkedEvents;
import com.example.resico.data.model.Event;
import com.example.resico.data.model.ForumComment;
import com.example.resico.data.model.ForumPost;
import com.example.resico.data.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Singleton class that handles network calls to our API.
 */
public class ResiCoAPIHandler {
	private static final String TAG = "ResiCoAPIHandler";
	private static ResiCoAPIHandler instance;

	// Authentication API
	private static final String BASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
	private static final String LOGIN_AUTH_QUERY = "key";
	private static final String LOGIN_AUTH_TOKEN = "AIzaSyCgYb1trzgtA0X54Escrne3S9PdtA_peXA";
	private static final String AUTH_RESPONSE_ID_KEY = "localId";

	// List of API endpoints, some of them require string formatting with IDs before they can be used.
	public static final String BASE_URL = "https://resico-7e187-default-rtdb.asia-southeast1.firebasedatabase.app";
	public static final String EVENT_LIST_ENDPOINT = "/events.json";
	public static final String EVENT_DETAIL_QUERY_ENDPOINT = "/eventDetail/%s.json"; // Format with event id
	public static final String ANNOUNCEMENT_LIST_ENDPOINT = "/announcements.json";
	public static final String ANNOUNCEMENT_QUERY_ENDPOINT = "/announcements/%s.json"; // Format with announcement id
	public static final String FORUM_LIST_ENDPOINT = "/forums.json";// Forum
	public static final String FORUM_COMMENTS_QUERY_ENDPOINT = "/forumComments/%s.json"; // Format with forum post id
	public static final String USER_QUERY_ENDPOINT = "/user/%s.json"; // Format with userId
	public static final String APPLIED_BOOKMARKED_EVENTS_ENDPOINT = "/appliedLikedEvents/%s.json"; // Format with userId

	private static final String AUTH_QUERY = "auth";
	private static final String AUTH_TOKEN = "DHijldnvaIJWKxfOHqfGgsz7A9tzNQRebXEkZvNN";

	private static final String requestBody = "body";


	/**
	 * Temporary storage of event list, with the eventId as key
	 */
	private static Hashtable<String, Event> eventTable;

	/**
	 * Temporary storage of announcement list, with the announcementId as key
	 */
	private static Hashtable<String, Announcement> announcementTable;

	/**
	 * Temporary storage of forum posts list, with the postId as key
	 */
	private static Hashtable<String, ForumPost> forumPostTable;

	/**
	 * Private access: Singleton pattern.
	 */
	private ResiCoAPIHandler() {}

	public static ResiCoAPIHandler getInstance() {
		if (instance == null) {
			instance = new ResiCoAPIHandler();
		}
		return instance;
	}

	public void getLogin(String email, String password, OnFinishRequest<Result<User>> onFinishRequest) {
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONObject data = getResponseBodyJSONObject(result);
			// If login fails (e.g. wrong password/email, status code 400 will be given)
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(new Result.Error(new IOException("Invalid email or password.")));
				return;
			}
			// Get user ID from login
			try {
				String userId = data.getString(AUTH_RESPONSE_ID_KEY);
				getUser(userId, user -> {
					onFinishRequest.onFinishRequest(new Result.Success<User>(user));
				});
				return;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			onFinishRequest.onFinishRequest(new Result.Error(new IOException("Invalid server response.")));
		});
		JSONObject loginPayload = new JSONObject();
		try {
			loginPayload.put("email", email);
			loginPayload.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		NetworkRequest.post(NetworkRequest.addQueryParameter(BASE_AUTH_URL, LOGIN_AUTH_QUERY, LOGIN_AUTH_TOKEN), loginPayload.toString(), callback);
	}

	public void getUser(String userId, OnFinishRequest<User> onFinishRequest) {
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONObject data = getResponseBodyJSONObject(result);
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(null);
				return;
			}
			// Build user object from data
			onFinishRequest.onFinishRequest(User.buildFromJSONObject(data, userId));
		});
		NetworkRequest.get(NetworkRequest.addQueryParameter(BASE_URL + String.format(USER_QUERY_ENDPOINT, userId), AUTH_QUERY, AUTH_TOKEN), callback);
	}

	public void getEvent(String userId, String eventId, OnFinishRequest<Event> onFinishRequest) {
		// Event information is split between event and eventDetails, we need to fetch from both sides
		// We can fetch the details first
		getEventDetail(eventId, detail -> {
			// After details is successfully fetched
			// Check if event data already exists
			if (eventTable != null && eventTable.containsKey(eventId)) {
				// Event data already exists, append the details to it
				Event event;
				event = eventTable.get(eventId);
				if (event == null) {
					Log.e(TAG, "Local eventTable has key for event: " + eventId + ", but the event is null!");
					return;
				}
				event.setDetail(detail);
				onFinishRequest.onFinishRequest(event);
			} else {
				// No existing event list data, we need to retrieve it
				getEventList(userId, table -> {
					if (table != null && table.containsKey(eventId)) {
						Event event;
						event = table.get(eventId);
						if (event == null) {
							Log.e(TAG, "Retrieved eventTable has key for event: " + eventId + ", but the event is null!");
							return;
						}
						event.setDetail(detail);
						onFinishRequest.onFinishRequest(event);
					}
				});
			}
		});
	}

	private void getEventDetail(String eventId, OnFinishRequest<String> onFinishRequest) {
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONObject data = getResponseBodyJSONObject(result);
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(null);
				return;
			}
			// Attempt to get the detail field from eventDetails
			onFinishRequest.onFinishRequest(data.optString(Event.API_FIELDS.DETAIL));
		});
		NetworkRequest.get(NetworkRequest.addQueryParameter(BASE_URL + String.format(EVENT_DETAIL_QUERY_ENDPOINT, eventId), AUTH_QUERY, AUTH_TOKEN), callback);
	}

	public void getEventList(String userId, OnFinishRequest<Hashtable<String, Event>> onFinishRequest) {
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONArray data = getResponseBodyJSONArray(result);
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(null);
				return;
			}
			// Store the event list
			Hashtable<String, Event> events = new Hashtable<>(data.length());
			for (int i = 0; i < data.length(); i++) {
				try {
					JSONObject eventObj = data.getJSONObject(i);
					Event event = Event.buildFromJSONObject(eventObj);
					if (event == null ||  event.getEventId() == null) continue;
					events.put(event.getEventId(), event);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// We should get which events that the user has bookmarked and applied to, which is separate
			getAppliedBookmarkedEvents(userId, appliedBookmarkedEvents -> {
				if (appliedBookmarkedEvents != null) {
					for (String appliedEvent : appliedBookmarkedEvents.getAppliedEvents()) {
						if (events.get(appliedEvent) == null) continue;
						events.get(appliedEvent).setHasApplied(true);
					}
					for (String bookmarkedEvent : appliedBookmarkedEvents.getBookmarkedEvents()) {
						if (events.get(bookmarkedEvent) == null) continue;
						events.get(bookmarkedEvent).setHasApplied(true);
					}
				}
				// Regardless of whether we get the applied and bookmarked status or not,
				// we should at least return the event data we already got
				onFinishRequest.onFinishRequest(events);
				eventTable = events;
			});
		});
		NetworkRequest.get(NetworkRequest.addQueryParameter(BASE_URL + EVENT_LIST_ENDPOINT, AUTH_QUERY, AUTH_TOKEN), callback);
	}

	public void getAnnouncement(String announcementId, OnFinishRequest<Announcement> onFinishRequest) {
		// Get announcement from table if already retrieved, otherwise retrieve specific announcement
		if (announcementTable != null && announcementTable.containsKey(announcementId)) {
			// Announcement data already exists
			onFinishRequest.onFinishRequest(announcementTable.get(announcementId));
			return;
		}
		// No existing announcement list data, we need to retrieve the specific announcement
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONObject data = getResponseBodyJSONObject(result);
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(null);
				return;
			}
			Announcement announcement = Announcement.buildFromJSONObject(data, announcementId);
			onFinishRequest.onFinishRequest(announcement);
		});
		NetworkRequest.get(NetworkRequest.addQueryParameter(BASE_URL + String.format(ANNOUNCEMENT_QUERY_ENDPOINT, announcementId), AUTH_QUERY, AUTH_TOKEN), callback);
	}

	public void getAnnouncementList(OnFinishRequest<Hashtable<String, Announcement>> onFinishRequest) {
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONArray data = getResponseBodyJSONArray(result);
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(null);
				return;
			}
			Hashtable<String, Announcement> announcements = new Hashtable<>(data.length());
			for (int i = 0; i < data.length(); i++) {
				try {
					JSONObject announcementObj = data.getJSONObject(i);
					Announcement announcement = Announcement.buildFromJSONObject(announcementObj, String.valueOf(i));
					if (announcement == null ||  announcement.getAnnouncementId() == null) continue;
					announcements.put(announcement.getAnnouncementId(), announcement);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			announcementTable = announcements;
			onFinishRequest.onFinishRequest(announcements);
		});
		NetworkRequest.get(NetworkRequest.addQueryParameter(BASE_URL + ANNOUNCEMENT_LIST_ENDPOINT, AUTH_QUERY, AUTH_TOKEN), callback);
	}

	public void getForumPosts(OnFinishRequest<Hashtable<String, ForumPost>> onFinishRequest) {
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONArray data = getResponseBodyJSONArray(result);
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(null);
				return;
			}
			Hashtable<String, ForumPost> postTable = new Hashtable<>(data.length());
			for (int i = 0; i < data.length(); i++) {
				try {
					JSONObject postObj = data.getJSONObject(i);
					ForumPost post = ForumPost.buildFromJSONObject(postObj);
					if (post == null ||  post.getPostId() == null) continue;
					postTable.put(post.getPostId(), post);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			onFinishRequest.onFinishRequest(postTable);
			forumPostTable = postTable;
		});
		NetworkRequest.get(NetworkRequest.addQueryParameter(BASE_URL + FORUM_LIST_ENDPOINT, AUTH_QUERY, AUTH_TOKEN), callback);
	}

	public void getForumPost(String postId, OnFinishRequest<ForumPost> onFinishRequest) {
		// Get forum post from table if already retrieved, otherwise need to fetch all forum posts first
		if (forumPostTable != null && forumPostTable.containsKey(postId)) {
			// Post table already exists, fetch from there
			onFinishRequest.onFinishRequest(forumPostTable.get(postId));
			return;
		}
		// No existing forum post list data, we need to retrieve the forum post list data first
		getForumPosts(table -> {
			if (table != null && table.containsKey(postId)) {
				onFinishRequest.onFinishRequest(table.get(postId));
				return;
			}
			// No post of the specified ID can be found on server or local
			onFinishRequest.onFinishRequest(null);
		});
	}

	public void getForumComments(String postId, OnFinishRequest<ForumComment[]> onFinishRequest) {
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONArray data = getResponseBodyJSONArray(result);
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(null);
				return;
			}
			ForumComment[] comments = new ForumComment[data.length()];
			for (int i = 0; i < data.length(); i++) {
				try {
					JSONObject commentObj = data.getJSONObject(i);
					comments[i] = ForumComment.buildFromJSONObject(commentObj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			onFinishRequest.onFinishRequest(comments);
		});
		NetworkRequest.get(NetworkRequest.addQueryParameter(BASE_URL + String.format(FORUM_COMMENTS_QUERY_ENDPOINT, postId), AUTH_QUERY, AUTH_TOKEN), callback);
	}

	public void getAppliedBookmarkedEvents(String userId, OnFinishRequest<AppliedBookmarkedEvents> onFinishRequest) {
		UrlRequestCallback callback = new UrlRequestCallback(result -> {
			// Verify data
			JSONObject data = getResponseBodyJSONObject(result);
			if (!checkRequestSuccess(result) || data == null) {
				onFinishRequest.onFinishRequest(null);
				return;
			}
			// Build list of event ids that the user has applied/bookmarked to
			onFinishRequest.onFinishRequest(AppliedBookmarkedEvents.buildFromJSONObject(data));
		});
		NetworkRequest.get(NetworkRequest.addQueryParameter(BASE_URL + String.format(APPLIED_BOOKMARKED_EVENTS_ENDPOINT, userId), AUTH_QUERY, AUTH_TOKEN), callback);
	}

	/**
	 * Check whether the network request is successful from the result data.
	 * @param result Network request data.
	 * @return True if network request is successful with status code <399, false otherwise.
	 */
	private boolean checkRequestSuccess(JSONObject result) {
		try {
			int statusCode = result.getInt("statusCode");
			if (statusCode < 399) { //Some kind of success
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the response body of the request result if available, null if otherwise.
	 * @param result Network request data.
	 * @return True if the response body is a {@link JSONObject}, false if is {@link JSONArray}. Returns null if neither.
	 */
	@Nullable
	private Boolean checkResponseBodyIsJSONObject(JSONObject result) {
		try {
			Object body = result.get(requestBody);
			if (body instanceof JSONObject) {
				return true;
			} else if (body instanceof JSONArray) {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the response body of the request result in {@link JSONObject}, null if otherwise.
	 * @param result Network request data.
	 * @return Response body if the request in {@link JSONObject}. Null if otherwise.
	 */
	@Nullable
	private JSONObject getResponseBodyJSONObject(JSONObject result) {
		try {
			Log.d(TAG, result.getString(requestBody));
			return new JSONObject(result.getString(requestBody));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the response body of the request result in {@link JSONArray}, null if otherwise.
	 * @param result Network request data.
	 * @return Response body if the request in {@link JSONArray}. Null if otherwise.
	 */
	@Nullable
	private JSONArray getResponseBodyJSONArray(JSONObject result) {
		try {
			return new JSONArray(result.getString(requestBody));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Delegate to be executed when a network request is completed.
	 * @param <T> Processed object to return.
	 */
	public interface OnFinishRequest<T> {
		/**
		 * Method invoked when network request is completed. If network request is unsuccessful,
		 * null is returned.
		 * @param data Converted object data from the network request. Null if request is unsuccessful or empty.
		 */
		void onFinishRequest(@Nullable T data);
	}
}
