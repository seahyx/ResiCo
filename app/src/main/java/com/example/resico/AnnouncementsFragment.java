package com.example.resico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.resico.databinding.FragmentAnnouncementsBinding;

import java.util.ArrayList;
public class AnnouncementsFragment extends Fragment implements AnnouncementRecyclerViewInterface {
	private FragmentAnnouncementsBinding binding;
	ArrayList<AnnouncementModel> announcementModels = new ArrayList<>();

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
		RecyclerView recyclerView = view.findViewById(R.id.announcementRecyclerView);

		setUpAnnouncementModels();
		// instantiate adapter only after we have set up the models
		AnnouncementRecyclerViewAdapter adapter = new AnnouncementRecyclerViewAdapter(this.getActivity(),
				announcementModels, this);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	private void setUpAnnouncementModels(){
		String[] announcementTitles = getResources().getStringArray(R.array.announcement_titles);
		String[] announcementDates = getResources().getStringArray(R.array.announcement_date);
		String[] announcementMessages = getResources().getStringArray(R.array.announcement_description);
		announcementModels.clear();

		for (int i =0; i< announcementTitles.length; i++){
			announcementModels.add(new AnnouncementModel(announcementTitles[i],
					announcementDates[i],announcementMessages[i]));
		}
	}

	@Override
	public void onItemClick(int position) {

		String title = announcementModels.get(position).getAnnouncementTitle();
		String description = announcementModels.get(position).getAnnouncementMessage();
		AnnouncementsFragmentDirections.ActionNavAnnouncementsToAnnouncementsSpecificFragment action =
				AnnouncementsFragmentDirections.actionNavAnnouncementsToAnnouncementsSpecificFragment(title,description);
		Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(action);
		//NavHostFragment.findNavController(AnnouncementsFragment.this).navigate(action);




	}
}