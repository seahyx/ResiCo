package com.example.resico.ui.announcements;

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
import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.Announcement;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.FragmentAnnouncementsBinding;
import com.example.resico.ui.forum.ForumDetailActivity;
import com.google.android.material.divider.MaterialDividerItemDecoration;

import java.util.ArrayList;

public class AnnouncementsFragment extends Fragment {
	private final String TAG = this.getClass().getSimpleName();
	private FragmentAnnouncementsBinding binding;
	ArrayList<Announcement> announcements = new ArrayList<>();

	private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> Log.d(TAG, "Returned from details page."));

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentAnnouncementsBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		RecyclerView recyclerView = binding.announcementsList;
		AnnouncementsAdapter adapter = new AnnouncementsAdapter(announcements, this::onItemClick);
		recyclerView.setAdapter(adapter);

		// Setup layout manager for RecyclerView
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
		recyclerView.setLayoutManager(linearLayoutManager);

		// Divider for recyclerView
		MaterialDividerItemDecoration dividerItemDecoration = new MaterialDividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		// Get announcements list to load into recycler view
		ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
		apiHandler.getAnnouncementList(announcements -> {
			if (announcements == null) return;
			this.announcements.clear();
			this.announcements.addAll(announcements.values());
			binding.getRoot().post(adapter::notifyDataSetChanged);
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	public void onItemClick(String announcementId) {
		// Navigate to the specific announcement page
		Intent detailIntent = new Intent(getActivity(), AnnouncementDetailActivity.class);
		detailIntent.putExtra(getString(R.string.announcement_id_key), announcementId);
		detailLauncher.launch(detailIntent);
	}
}
