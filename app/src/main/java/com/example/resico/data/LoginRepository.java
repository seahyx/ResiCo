package com.example.resico.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.resico.App;
import com.example.resico.R;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;

import java.util.Calendar;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {
	private final String TAG = this.getClass().getSimpleName();

	private static volatile LoginRepository instance;

	private final ResiCoAPIHandler apiHandler;
	private final SharedPreferences sharedPref;
	private final SharedPreferences.Editor editor;

	// If user credentials will be cached in local storage, it is recommended it be encrypted
	// @see https://developer.android.com/training/articles/keystore
	private static User user = null;

	// private constructor : singleton access
	private LoginRepository() {
		this.apiHandler = ResiCoAPIHandler.getInstance();
		this.sharedPref = App.getContext().getSharedPreferences(App.getContext().getString(R.string.main_store), Context.MODE_PRIVATE);
		this.editor = sharedPref.edit();
	}

	public static LoginRepository getInstance() {
		if (instance == null) {
			instance = new LoginRepository();
		}
		return instance;
	}

	public void isLoggedIn(OnLoginReceived<Boolean> onLoginReceived) {
		getLastLogin(data -> onLoginReceived.onLoginReceived(user != null));
	}

	@Nullable
	public static User getUser() {
		// This function should not be run when the user is not logged in, aka user = null.
		// However, it is possible to get into this state where the user is null but this function
		// is called, most likely attempting to access the user's getter methods within.
		// We catch our own exception so we know when illegal state happens, if it happens.
		try {
			if (user == null) { throw new NullPointerException("user field is null! getUser() is called while the user is logged out, which is illegal."); }
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return user;
	}

	public static String getUserId() {
		try {
			return getUser().getUserId();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void logout() {
		user = null;

		// Clear cache
		editor.putString(App.getContext().getString(R.string.user_id_key), null);
		editor.putString(App.getContext().getString(R.string.user_display_name_key), null);
		editor.apply();
	}

	private void setLoggedInUser(User user) {
		this.user = user;

		// Cache user data on login
		editor.putLong(App.getContext().getString(R.string.last_login_key), Calendar.getInstance().getTimeInMillis());
		editor.putString(App.getContext().getString(R.string.user_id_key), user.getUserId());
		editor.apply();
		Log.d(TAG, "User is logged in with userId: " + user.getUserId());
	}

	/**
	* Get the last cached login details from SharedPreferences.
	 * @return True if a previous login exists, false if otherwise.
	* */
	public void getLastLogin(OnLoginReceived<Boolean> onLoginReceived) {
		String userId = sharedPref.getString(App.getContext().getString(R.string.user_id_key), "");
		if (!userId.equals("")) {
			Log.d(TAG, "Previous login detected, updating logged in user with userId: " + userId);
			apiHandler.getUser(userId, user -> {
				if (user != null) {
					setLoggedInUser(user);
					onLoginReceived.onLoginReceived(true);
					return;
				}
				onLoginReceived.onLoginReceived(false);
			});
			return;
		}
		onLoginReceived.onLoginReceived(false);
	}

	public void login(String username, String password, OnLoginReceived<User> onLoginReceived) {
		// handle login
		apiHandler.getLogin(username, password, result -> {
			if (result instanceof Result.Success) {
				User user = ((Result.Success<User>) result).getData();
				setLoggedInUser(user);
				onLoginReceived.onLoginReceived(user);
				return;
			}
			onLoginReceived.onLoginReceived(null);
		});
	}

	/**
	 * Delegate to be executed when a login is completed.
	 * @param <T> Processed object to return.
	 */
	public interface OnLoginReceived<T> {
		/**
		 * Method invoked when network request is completed. If network request is unsuccessful,
		 * null is returned.
		 * @param data Converted object data from the network request. Null if request is unsuccessful or empty.
		 */
		void onLoginReceived(@Nullable T data);
	}
}