package com.example.resico.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.resico.App;
import com.example.resico.R;
import com.example.resico.data.model.LoggedInUser;

import java.util.Calendar;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {
	private final String TAG = this.getClass().getSimpleName();

	private static volatile LoginRepository instance;

	private LoginDataSource dataSource;
	private final Context context;
	private final SharedPreferences sharedPref;
	private final SharedPreferences.Editor editor;

	// If user credentials will be cached in local storage, it is recommended it be encrypted
	// @see https://developer.android.com/training/articles/keystore
	private LoggedInUser user = null;

	// private constructor : singleton access
	private LoginRepository(LoginDataSource dataSource) {
		this.dataSource = dataSource;
		this.context = App.getContext();
		this.sharedPref = context.getSharedPreferences(context.getString(R.string.main_store), Context.MODE_PRIVATE);
		this.editor = sharedPref.edit();
		getLastLogin();
	}

	public static LoginRepository getInstance(LoginDataSource dataSource) {
		if (instance == null) {
			instance = new LoginRepository(dataSource);
		}
		return instance;
	}

	public boolean isLoggedIn() {
		return user != null;
	}

	@Nullable
	public LoggedInUser getUser() {
		return user;
	}

	public void logout() {
		user = null;

		// Clear cache
		editor.putString(context.getString(R.string.user_id_key), null);
		editor.putString(context.getString(R.string.user_display_name_key), null);
		editor.apply();

		dataSource.logout();
	}

	private void setLoggedInUser(LoggedInUser user) {
		this.user = user;

		// Cache user data on login
		editor.putLong(context.getString(R.string.last_login_key), Calendar.getInstance().getTimeInMillis());
		editor.putString(context.getString(R.string.user_id_key), user.getUserId());
		editor.putString(context.getString(R.string.user_display_name_key), user.getDisplayName());
		editor.apply();
		Log.d(TAG, "User is logged in with userId: " + user.getUserId() + ", displayName: " + user.getDisplayName());
	}

	/**
	* Get the last cached login details from SharedPreferences.
	 * @return True if a previous login exists, false if otherwise.
	* */
	private boolean getLastLogin() {
		String userId = sharedPref.getString(context.getString(R.string.user_id_key), "");
		if (!userId.equals("")) {
			String displayName = sharedPref.getString(context.getString(R.string.user_display_name_key), "NO DISPLAY NAME FOUND");
			Log.d(TAG, "Previous login detected, updating logged in user with userId: " + userId + ", displayName: " + displayName);
			user = new LoggedInUser(userId, displayName);
			return true;
		}
		return false;
	}

	public Result<LoggedInUser> login(String username, String password) {
		// handle login
		Result<LoggedInUser> result = dataSource.login(username, password);
		if (result instanceof Result.Success) {
			setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
		}
		return result;
	}
}