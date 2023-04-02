package com.example.resico.ui.event;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resico.R;
import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.ActivityEventDetailBinding;
import com.example.resico.utils.App;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {

	private ActivityEventDetailBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		setSupportActionBar(binding.toolbar);
		binding.toolbar.showOverflowMenu();
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
					Picasso.get().load(event.getImageUrl()).error(R.drawable.placeholder_broken_image).fit().centerCrop().into(binding.eventDetailImage);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_event_detail, menu);

		// Update checkbox
		MaterialCheckBox materialCheckBox = (MaterialCheckBox) menu.findItem(R.id.menu_bookmark).getActionView();
		materialCheckBox.setButtonDrawable(R.drawable.sl_bookmark);

		return true;
	}
}