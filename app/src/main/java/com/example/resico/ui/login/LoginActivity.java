package com.example.resico.ui.login;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.resico.R;
import com.example.resico.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
	private final String TAG = this.getClass().getSimpleName();

	private LoginViewModel loginViewModel;
	private ActivityLoginBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
				.get(LoginViewModel.class);

		final TextInputLayout usernameLayout = (TextInputLayout) binding.username;
		final TextInputLayout passwordLayout = (TextInputLayout) binding.password;
		final Button loginButton = binding.login;
		final ProgressBar loadingProgressBar = binding.loading;

		loginViewModel.getLoginFormState().observe(this, loginFormState -> {
			if (loginFormState == null) {
				return;
			}
			loginButton.setEnabled(loginFormState.isDataValid());
			if (loginFormState.getUsernameError() != null) {
				usernameLayout.setError(getString(loginFormState.getUsernameError()));
			} else {
				usernameLayout.setError(null);
			}
			if (loginFormState.getPasswordError() != null) {
				passwordLayout.setError(getString(loginFormState.getPasswordError()));
			} else {
				passwordLayout.setError(null);
			}
		});

		loginViewModel.getLoginResult().observe(this, loginResult -> {
			if (loginResult == null) {
				return;
			}
			loadingProgressBar.setVisibility(View.GONE);
			if (loginResult.getError() != null) {
				showLoginFailed(loginResult.getError());
			}
			if (loginResult.getSuccess() != null) {
				updateUiWithUser(loginResult.getSuccess());
			}
			setResult(Activity.RESULT_OK);

			//Complete and destroy login activity once successful
			finish();
		});

		TextWatcher afterTextChangedListener = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// ignore
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// ignore
			}

			@Override
			public void afterTextChanged(Editable s) {
				loginViewModel.loginDataChanged(usernameLayout.getEditText().getText().toString(),
						passwordLayout.getEditText().getText().toString());
			}
		};
		usernameLayout.getEditText().addTextChangedListener(afterTextChangedListener);
		passwordLayout.getEditText().addTextChangedListener(afterTextChangedListener);
		passwordLayout.getEditText().setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				loginViewModel.login(usernameLayout.getEditText().getText().toString(),
						passwordLayout.getEditText().getText().toString());
			}
			return false;
		});

		loginButton.setOnClickListener(v -> {
			loadingProgressBar.setVisibility(View.VISIBLE);
			loginViewModel.login(usernameLayout.getEditText().getText().toString(),
					passwordLayout.getEditText().getText().toString());
		});


		Log.d(TAG, "Starting this startup shit up.");
	}

	private void updateUiWithUser(LoggedInUserView model) {
		String welcome = getString(R.string.welcome) + model.getDisplayName();
		Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
	}

	private void showLoginFailed(@StringRes Integer errorString) {
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
	}
}