package com.example.resico.ui.forum;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.R;
import com.example.resico.data.model.ForumPost;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.FragmentForumsBinding;
import com.example.resico.ui.SpacesItemDecoration;

import java.util.ArrayList;

public class ForumsFragment extends Fragment {
	private final String TAG = this.getClass().getSimpleName();
	private FragmentForumsBinding binding;

	private ArrayList<ForumPost> forumPosts = new ArrayList<>();
	private RecyclerView recyclerView;

	private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> Log.d(TAG, "Returned from details page."));

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentForumsBinding.inflate(inflater, container, false);

		return binding.getRoot();
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();

		// Attach event adapter to RecyclerView
		recyclerView = binding.forumsPostList;
		ForumPostAdapter adapter = new ForumPostAdapter(forumPosts, this::onEventClick);
		recyclerView.setAdapter(adapter);

		// LinearLayoutManager by default has vertical orientation
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
		recyclerView.setLayoutManager(linearLayoutManager);

		// Add spacing for RecyclerView items
		SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(
				(int) getResources().getDimension(R.dimen.component_medium_margin),
				linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(spacesItemDecoration);

		apiHandler.getForumPosts(posts -> {
			if (posts == null) return;
			this.forumPosts.clear();
			this.forumPosts.addAll(posts.values());
			binding.getRoot().post(adapter::notifyDataSetChanged);
		});
	}

	/**
	 * OnClick function for {@link RecyclerView} event cards.
	 *
	 * @param postId String ID of the event clicked.
	 */
	private void onEventClick(String postId) {
		// Navigate to the specific post page
		Intent detailIntent = new Intent(getActivity(), ForumDetailActivity.class);
		detailIntent.putExtra(getString(R.string.post_id_key), postId);
		detailLauncher.launch(detailIntent);
	}

}