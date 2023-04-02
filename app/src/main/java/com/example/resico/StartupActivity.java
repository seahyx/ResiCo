package com.example.resico;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.resico.data.LoginRepository;
import com.example.resico.databinding.ActivityStartupBinding;
import com.example.resico.ui.login.LoginActivity;

/**
 * Starter activity that will check whether the user is logged in, and if so, direct the user to the MainActivity.
 * If the user is not logged in, the user will be directed to the LoginActivity.
 */
public class StartupActivity extends AppCompatActivity {
	private final String TAG = this.getClass().getSimpleName();

	private ActivityStartupBinding binding;

	private final ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				Log.d(TAG, "Returned from login page, checking if user is logged in...");
				handleStartupCheck();
			});
	private final LoginRepository loginRepository = LoginRepository.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityStartupBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		// Startup image align
		binding.startupImage.post(() -> {
			final ImageView imageView = binding.startupImage;
			final Matrix matrix = imageView.getImageMatrix();
			final double imageWidth = imageView.getDrawable().getIntrinsicWidth();
			final int imageHeight = imageView.getDrawable().getIntrinsicHeight();
			final double containerWidth = imageView.getMeasuredWidth();
			final int containerHeight = imageView.getMeasuredHeight();
			Log.d(TAG, String.format("imageWidth: %s, containerWidth: %s", imageWidth, containerWidth));
			final double scaleRatio = imageWidth / containerWidth;
			matrix.postScale((float) scaleRatio, (float) scaleRatio);
			// This image is aligned top left, but if there is space at the bottom, we want to
			// align bottom instead, which can be done by translation
			if (imageHeight * scaleRatio < containerHeight) {
				matrix.setTranslate(0, (float) (containerHeight - (imageHeight * scaleRatio) + 3)); // +3 for good measure
			}
			imageView.setImageMatrix(matrix);
		});

		// Short delay
		final Handler handler = new Handler();
		handler.postDelayed(this::handleStartupCheck, 1500);
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
