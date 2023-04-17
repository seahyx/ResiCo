package com.example.resico.data.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data class that captures user information for logged in users retrieved from {@link com.example.resico.data.LoginRepository}
 */
public class User {
	private final String userId;
	private final String username;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String phoneNo;
	private final String imageUrl;
	public User(String userId,
	            String username,
	            String firstName,
	            String lastName,
	            String email,
	            String phoneNo,
	            String imageUrl) {
		this.userId = userId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNo = phoneNo;
		this.imageUrl = imageUrl;
	}

	public static User buildFromJSONObject(JSONObject jsonObject, String userId) {
		try {
			return new User(
					userId,
					jsonObject.getString(API_FIELDS.USERNAME),
					jsonObject.getString(API_FIELDS.FIRST_NAME),
					jsonObject.getString(API_FIELDS.LAST_NAME),
					jsonObject.getString(API_FIELDS.EMAIL),
					jsonObject.getString(API_FIELDS.PHONE_NO),
					jsonObject.getString(API_FIELDS.IMAGE_URL)
			);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName() {
		return lastName + " " + firstName;
	}

	public String getEmail() {
		return email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	@NonNull
	@Override
	public String toString() {
		final String den = ": ";
		final String sep = ", ";
		return "{" +
				API_FIELDS.USER_ID + den + userId + sep +
				API_FIELDS.USERNAME + den + username + sep +
				API_FIELDS.FIRST_NAME + den + firstName + sep +
				API_FIELDS.LAST_NAME + den + lastName + sep +
				API_FIELDS.EMAIL + den + email + sep +
				API_FIELDS.PHONE_NO + den + phoneNo + sep +
				API_FIELDS.IMAGE_URL + den + imageUrl +
				"}";
	}

	public interface API_FIELDS {
		String USER_ID = "userId";
		String USERNAME = "username";
		String FIRST_NAME = "firstName";
		String LAST_NAME = "lastName";
		String EMAIL = "email";
		String PHONE_NO = "phoneNo";
		String IMAGE_URL = "imageUrl";
	}
}
