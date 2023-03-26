package com.example.resico.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resico.R;
import com.example.resico.ui.login.LoginViewModel;
import com.example.resico.ui.login.LoginViewModelFactory;
import com.example.resico.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

	private LoginViewModel loginViewModel;
	private ActivityLoginBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
				.get(LoginViewModel.class);

		final EditText usernameEditText = ((TextInputLayout) binding.username).getEditText();
		final EditText passwordEditText = ((TextInputLayout) binding.password).getEditText();
		final Button loginButton = binding.login;
		final ProgressBar loadingProgressBar = binding.loading;

		loginViewModel.getLoginFormState().observe(this, loginFormState -> {
			if (loginFormState == null) {
				return;
			}
			loginButton.setEnabled(loginFormState.isDataValid());
			if (loginFormState.getUsernameError() != null) {
				if (usernameEditText != null) {
					usernameEditText.setError(getString(loginFormState.getUsernameError()));
				}
			}
			if (loginFormState.getPasswordError() != null) {
				if (passwordEditText != null) {
					passwordEditText.setError(getString(loginFormState.getPasswordError()));
				}
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
				loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
						passwordEditText.getText().toString());
			}
		};
		usernameEditText.addTextChangedListener(afterTextChangedListener);
		passwordEditText.addTextChangedListener(afterTextChangedListener);
		passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				loginViewModel.login(usernameEditText.getText().toString(),
						passwordEditText.getText().toString());
			}
			return false;
		});

		loginButton.setOnClickListener(v -> {
			loadingProgressBar.setVisibility(View.VISIBLE);
			loginViewModel.login(usernameEditText.getText().toString(),
					passwordEditText.getText().toString());
		});
	}

	private void updateUiWithUser(LoggedInUserView model) {
		String welcome = getString(R.string.welcome) + model.getDisplayName();
		// TODO : initiate successful logged in experience
		Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
	}

	private void showLoginFailed(@StringRes Integer errorString) {
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
	}
}