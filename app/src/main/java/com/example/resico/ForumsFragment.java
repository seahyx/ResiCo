package com.example.resico;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resico.databinding.FragmentEventsBinding;
import com.example.resico.databinding.FragmentForumsBinding;

public class ForumsFragment extends Fragment {

	private final String TAG = this.getClass().getSimpleName();
	private FragmentForumsBinding binding;

	private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> Log.d(TAG, "Returned from details page."));

	public ForumsFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentForumsBinding.inflate(inflater,container,false);

		return binding.getRoot();
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		binding.buttonToForum.setOnClickListener(v -> {
			onEventClick("0");
		});
	}

	/**
	 * OnClick function for {@link RecyclerView} event cards.
	 * @param postId String ID of the event clicked.
	 */
	private void onEventClick(String postId) {
		// Navigate to the specific event page
		Intent detailIntent = new Intent(getActivity(), ForumDetailActivity.class);
		detailIntent.putExtra(getString(R.string.post_id_key), postId);
		detailLauncher.launch(detailIntent);
	}

}