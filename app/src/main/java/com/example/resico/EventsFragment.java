package com.example.resico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.Event;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.FragmentEventsBinding;
import com.example.resico.ui.SpacesItemDecoration;
import com.example.resico.ui.adapters.EventsAdapter;

import java.util.ArrayList;

public class EventsFragment extends Fragment {
	private final String TAG = this.getClass().getSimpleName();

	private FragmentEventsBinding binding;
	private RecyclerView recyclerView;
	private ArrayList<Event> events = new ArrayList<>();

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

		// Attach event adapter to RecyclerView
		recyclerView = binding.eventsList;
		EventsAdapter adapter = new EventsAdapter(this.events);
		recyclerView.setAdapter(adapter);

		// Retrieve event data from API
		User user =  LoginRepository.getUser();
		if (user != null) {
			apiHandler.getEventList(user.getUserId(), events -> {
				if (events == null) return;
				this.events.clear();
				this.events.addAll(events.values());
				binding.getRoot().post(() -> adapter.notifyDataSetChanged());
			});
		}

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