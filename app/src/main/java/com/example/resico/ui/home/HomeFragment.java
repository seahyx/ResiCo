package com.example.resico.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.R;
import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.Event;
import com.example.resico.data.model.ForumPost;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.FragmentHomeBinding;
import com.example.resico.ui.SpacesItemDecoration;
import com.example.resico.ui.event.EventDetailActivity;
import com.example.resico.ui.event.EventsAdapter;
import com.example.resico.ui.forum.ForumDetailActivity;
import com.example.resico.ui.forum.ForumPostAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
	private final String TAG = this.getClass().getSimpleName();
	private FragmentHomeBinding binding;

	private ArrayList<Event> events = new ArrayList<>();
	private ArrayList<ForumPost> forumPosts = new ArrayList<>();

	private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> Log.d(TAG, "Returned from details page."));

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentHomeBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();

		// Setup RecyclerView for events

		// Attach event adapter to RecyclerView
		RecyclerView eventView = binding.homeEventList;
		EventsAdapter eventsAdapter = new EventsAdapter(this.events, this::onEventClick);
		eventView.setAdapter(eventsAdapter);

		// Horizontal LayoutManager for carousel
		LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
				eventView.getContext(),
				LinearLayoutManager.HORIZONTAL,
				false);
		eventView.setLayoutManager(horizontalLayoutManager);

		// Add spacing between cards
		SpacesItemDecoration eventCardSpacing = new SpacesItemDecoration(
				(int) getResources().getDimension(R.dimen.component_medium_margin),
				horizontalLayoutManager.getOrientation());
		eventView.addItemDecoration(eventCardSpacing);

		// Apply snapping to the cards
		PagerSnapHelper snapHelper = new PagerSnapHelper();
		snapHelper.attachToRecyclerView(eventView);

		// Retrieve event data from API
		User user = LoginRepository.getUser();
		if (user != null) {
			apiHandler.getEventList(user.getUserId(), events -> {
				if (events == null) return;
				this.events.clear();
				this.events.addAll(events.values());
				this.events.sort(new Event.CompareMostRecent());
				binding.getRoot().post(eventsAdapter::notifyDataSetChanged);
			});
		}


		// Setup RecyclerView for forums
		RecyclerView forumsView = binding.homeForumsList;
		ForumPostAdapter postAdapter = new ForumPostAdapter(forumPosts, this::onPostClick);
		forumsView.setAdapter(postAdapter);

		// LinearLayoutManager by default has vertical orientation
		LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(forumsView.getContext());
		forumsView.setLayoutManager(verticalLayoutManager);

		// Add spacing for RecyclerView items
		SpacesItemDecoration forumPostSpacing = new SpacesItemDecoration(
				(int) getResources().getDimension(R.dimen.component_medium_margin),
				verticalLayoutManager.getOrientation());
		forumsView.addItemDecoration(forumPostSpacing);

		// Retrieve forum data from API
		apiHandler.getForumPosts(posts -> {
			if (posts == null) return;
			this.forumPosts.clear();
			this.forumPosts.addAll(posts.values());
			this.forumPosts.sort(new ForumPost.CompareMostRecent());
			binding.getRoot().post(postAdapter::notifyDataSetChanged);
		});

		// Update profile image and username
		if (user != null) {
			binding.homeUsername.setText(user.getUsername());
			Picasso.get().load(user.getImageUrl()).error(R.drawable.placeholder_profile).fit().centerCrop().into(binding.homeProfile);
		}

		// Link the "see all" texts to the respective fragments
		binding.homeEventsMore.setOnClickListener(view1 -> NavHostFragment.findNavController(this).navigate(HomeFragmentDirections.actionNavHomeToNavEvents()));
		binding.homeForumPostsMore.setOnClickListener(view1 -> NavHostFragment.findNavController(this).navigate(HomeFragmentDirections.actionNavHomeToNavForums()));
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	/**
	 * OnClick function for {@link RecyclerView} event cards.
	 *
	 * @param eventId String ID of the event clicked.
	 */
	private void onEventClick(String eventId) {
		// Navigate to the specific event page
		Intent detailIntent = new Intent(getActivity(), EventDetailActivity.class);
		detailIntent.putExtra(getString(R.string.event_id_key), eventId);
		detailLauncher.launch(detailIntent);
	}

	/**
	 * OnClick function for {@link RecyclerView} event cards.
	 *
	 * @param postId String ID of the post clicked.
	 */
	private void onPostClick(String postId) {
		// Navigate to the specific post page
		Intent detailIntent = new Intent(getActivity(), ForumDetailActivity.class);
		detailIntent.putExtra(getString(R.string.post_id_key), postId);
		detailLauncher.launch(detailIntent);
	}
}