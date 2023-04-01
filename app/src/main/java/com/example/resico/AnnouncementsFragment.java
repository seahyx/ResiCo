package com.example.resico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.Announcement;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.FragmentAnnouncementsBinding;
import com.example.resico.ui.adapters.AnnouncementsAdapter;
import com.google.android.material.divider.MaterialDividerItemDecoration;


import java.util.ArrayList;
public class AnnouncementsFragment extends Fragment {
	private FragmentAnnouncementsBinding binding;
	ArrayList<Announcement> announcements = new ArrayList<>();

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
	) {

		binding = FragmentAnnouncementsBinding.inflate(inflater, container, false);
		return binding.getRoot();

	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.announcements_recyclerview);
		AnnouncementsAdapter adapter = new AnnouncementsAdapter(announcements, this::onItemClick);
		recyclerView.setAdapter(adapter);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		// Divider for recyclerView
		MaterialDividerItemDecoration dividerItemDecoration = new MaterialDividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);
		//get announcements list to load into recycler view
		ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
		User user =  LoginRepository.getUser();
		if (user != null) {
			apiHandler.getAnnouncementList(announcements -> {
				if (announcements == null) return;
				this.announcements.clear();
				this.announcements.addAll(announcements.values());
				binding.getRoot().post(adapter::notifyDataSetChanged);
			});
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	public void onItemClick(String id) {
		AnnouncementsFragmentDirections.ActionNavAnnouncementsToAnnouncementsSpecificFragment action =
				AnnouncementsFragmentDirections.actionNavAnnouncementsToAnnouncementsSpecificFragment(id);
		NavHostFragment.findNavController(this).navigate(action);
	}
}
