package com.example.resico.data.model;

/**
 * Data class that captures user information for logged in users retrieved from {@link com.example.resico.data.LoginRepository}
 */
public class User {
	private final String userId;
	private final String username;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String imageUrl;

	public User(String userId,
	            String username,
	            String firstName,
	            String lastName,
	            String email,
	            String imageUrl) {
		this.userId = userId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.imageUrl = imageUrl;
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
}
