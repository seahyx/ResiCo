package com.example.resico;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.resico.data.LoginRepository;
import com.example.resico.databinding.ActivityStartupBinding;
import com.example.resico.ui.login.LoginActivity;

/**
* Starter activity that will check whether the user is logged in, and if so, direct the user to the MainActivity.
* If the user is not logged in, the user will be directed to the LoginActivity.
* */
public class StartupActivity extends AppCompatActivity {
	private final String TAG = this.getClass().getSimpleName();

	private ActivityStartupBinding binding;

	private final ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			(ActivityResultCallback<ActivityResult>) result -> {
				Log.d(TAG, "Returned from login page, checking if user is logged in...");
				handleStartupCheck();
			});
	private final LoginRepository loginRepository = LoginRepository.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityStartupBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		// Short delay
		final Handler handler = new Handler();
		handler.postDelayed(this::handleStartupCheck, 2000);
	}

	private void handleStartupCheck() {
		// Check if user is logged in
		loginRepository.isLoggedIn(isLoggedIn -> {
			if (isLoggedIn != null && isLoggedIn) {
				Log.d(TAG, "User is logged in, redirecting to MainActivity...");
				Intent startupIntent = new Intent(this, MainActivity.class);
				startupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(startupIntent);
				finish();
				return;
			}
			Log.d(TAG, "User is not logged in, redirecting to LoginActivity...");
			loginLauncher.launch(new Intent(this, LoginActivity.class));
		});
	}
}
