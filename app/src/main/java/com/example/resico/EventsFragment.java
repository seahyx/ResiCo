package com.example.resico;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.Announcement;
import com.example.resico.data.model.Event;
import com.example.resico.data.model.ForumComment;
import com.example.resico.data.model.ForumPost;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.FragmentEventsBinding;
import com.example.resico.ui.SpacesItemDecoration;
import com.example.resico.ui.events.EventsAdapter;

public class EventsFragment extends Fragment {
	private final String TAG = this.getClass().getSimpleName();

	private FragmentEventsBinding binding;
	private RecyclerView recyclerView;
	private Event[] events;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
	) {

		binding = FragmentEventsBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
		apiHandler.getEventList(LoginRepository.getUser().getUserId(), eventTable -> {
			if (eventTable == null) {
				Log.w(TAG, "eventTable is NULL!!!");
				return;
			}
			for (Event event: eventTable.values()) {
				Log.i(TAG, event.toString());
			}
		});
		Log.i(TAG, "Code ran here first.");
		apiHandler.getEvent(LoginRepository.getUser().getUserId(), "2", event -> {
			if (event == null) {
				Log.w(TAG, "Event is null!!!!");
				return;
			}
			Log.i(TAG, event.toString());
		});
		Log.i(TAG, "Code ran here second.");
		apiHandler.getAnnouncementList(announcementTable -> {
			if (announcementTable == null) {
				Log.w(TAG, "announcementTable is NULL!!!");
				return;
			}
			for (Announcement announcement: announcementTable.values()) {
				Log.i(TAG, announcement.toString());
			}
		});
		Log.i(TAG, "Code ran here third.");
		apiHandler.getAnnouncement("1", announcement -> {
			if (announcement == null) {
				Log.w(TAG, "Announcement is null!!!!");
				return;
			}
			Log.i(TAG, announcement.toString());
		});
		Log.i(TAG, "Code ran here fourth.");
		apiHandler.getUser(LoginRepository.getUser().getUserId(), user -> {
			if (user == null) {
				Log.w(TAG, "User is null!!!!");
				return;
			}
			Log.i(TAG, user.toString());
		});
		Log.i(TAG, "Code ran here fifth.");
		apiHandler.getForumPosts(postTable -> {
			if (postTable == null) {
				Log.w(TAG, "postTable is NULL!!!");
				return;
			}
			for (ForumPost post: postTable.values()) {
				Log.i(TAG, post.toString());
			}
		});
		Log.i(TAG, "Code ran here sixth.");
		apiHandler.getForumPost("2", post -> {
			if (post == null) {
				Log.w(TAG, "Post is null!!!!");
				return;
			}
			Log.i(TAG, post.toString());
		});
		Log.i(TAG, "Code ran here seventh.");
		apiHandler.getForumComments("2", comments -> {
			if (comments == null) {
				Log.w(TAG, "comments is NULL!!!");
				return;
			}
			for (ForumComment post: comments) {
				Log.i(TAG, post.toString());
			}
		});
		Log.i(TAG, "Code ran here eighth.");

		recyclerView = binding.eventsList;

		// Generate dummy events for testing
		events = new Event[5];
		for (int i = 0; i < events.length; i++) {
			events[i] = new Event(
					String.valueOf(i),
					Event.convertDateTimeIntToDate("10052023", "1200"),
					Event.convertDateTimeIntToDate("10052023",  "1400"),
					"Salad Making Workshop " + i,
					"Balsa",
					"Home",
					"",
					String.valueOf(i),
					String.valueOf(i),
					false,
					false
			);
		}

		// Attach event adapter to RecyclerView
		EventsAdapter adapter = new EventsAdapter(events);
		recyclerView.setAdapter(adapter);
		// LinearLayoutManager by default has vertical orientation
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		// Add spacing between cards
		SpacesItemDecoration itemDecoration = new SpacesItemDecoration(
				(int) getResources().getDimension(R.dimen.component_medium_margin),
				linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(itemDecoration);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

}