package com.example.resico.ui.announcements;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resico.R;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.ActivityAnnouncementDetailBinding;

public class AnnouncementDetailActivity extends AppCompatActivity {
	private final String TAG = this.getClass().getSimpleName();

	private ActivityAnnouncementDetailBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityAnnouncementDetailBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		setSupportActionBar(binding.toolbar);
		binding.toolbar.setNavigationOnClickListener(view -> finish());

		// Get specific announcement details from announcement id
		ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
		String announcementId = getIntent().getStringExtra(getString(R.string.announcement_id_key));
		apiHandler.getAnnouncement(announcementId, announcement -> {
			if (announcement == null) return;
			// Update the UI in the UI thread
			binding.getRoot().post(() -> {
				binding.announcementsDetailTitle.setText(announcement.getTitle());
				binding.announcementsDetailDate.setText(announcement.getPostDateFormatted());
				binding.announcementsDetail.setText(announcement.getDetail());
			});
		});
	}
}