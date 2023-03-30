package com.example.resico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.data.model.Event;
import com.example.resico.databinding.FragmentEventsBinding;
import com.example.resico.ui.SpacesItemDecoration;
import com.example.resico.ui.events.EventsAdapter;

public class EventsFragment extends Fragment {

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

//		binding.buttonSecond.setOnClickListener(view1 -> NavHostFragment.findNavController(EventsFragment.this)
//				.navigate(R.id.action_SecondFragment_to_FirstFragment));

		recyclerView = binding.eventsList;

		// Generate dummy events for testing
		events = new Event[5];
		for (int i = 0; i < events.length; i++) {
			events[i] = new Event(
					i,
					Event.convertDateTimeIntToDate("10052023", "1200"),
					Event.convertDateTimeIntToDate("10052023",  "1400"),
					"Salad Making Workshop " + i,
					"Balsa",
					"Home",
					"",
					i,
					i,
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