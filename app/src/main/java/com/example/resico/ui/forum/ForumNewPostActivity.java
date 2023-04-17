package com.example.resico.ui.forum;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.resico.databinding.ActivityForumNewPostBinding;

public class ForumNewPostActivity extends AppCompatActivity {
	private final String TAG = this.getClass().getSimpleName();

	private ActivityForumNewPostBinding binding;

	ActivityResultLauncher<String> getImageResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
		// Handle returned uri
		if (uri == null) return;
		binding.forumCreateAddImage.setImageURI(uri);
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityForumNewPostBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		setSupportActionBar(binding.toolbar);
		binding.toolbar.setNavigationOnClickListener(view -> finish());

		binding.forumCreatePostButton.setOnClickListener(view -> finish());
		binding.forumCreateAddImage.setOnClickListener(view -> getImageResultLauncher.launch("image/*"));
	}
}
