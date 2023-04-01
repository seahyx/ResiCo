package com.example.resico;

import android.os.Bundle;

import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.Event;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.resico.databinding.ActivityEventDetailBinding;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {

	private ActivityEventDetailBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		setSupportActionBar(binding.toolbar);
		binding.toolbar.setNavigationOnClickListener(view -> finish());

		// Get event id from intent
		String eventId = getIntent().getStringExtra(getString(R.string.event_id_key));
		User user = LoginRepository.getUser();
		if (user != null) {
			ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
			// Get the event details from the API using the event id.
			apiHandler.getEvent(user.getUserId(), eventId, event -> {
				if (event == null) return;
				// Update the UI in the ui thread
				binding.getRoot().post(() -> {
					binding.eventDetailTitle.setText(event.getTitle());
					binding.eventDetailDate.setText(event.getDateTimeRangeFormatted());
					binding.eventDetailAbout.setText(event.getDetail());
					binding.eventDetailVenue.setText(event.getVenue());
					binding.eventDetailParticipants.setText(App.getContext().getString(R.string.event_participants, event.getParticipantCount()));
					Picasso.get().load(event.getImageUrl()).fit().centerCrop().into(binding.eventDetailImage);
				});
				// Get the host user data (requiring another API call)
				apiHandler.getUser(event.getHostId(), host -> {
					binding.getRoot().post(() -> {
						// Update UI in ui thread
						binding.eventDetailContact.setText(host.getUsername());
					});
				});
			});
		}

	}
}