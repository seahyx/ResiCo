package com.example.resico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.resico.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

	private FragmentHomeBinding binding;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
	) {
		// Access the bottom nav on the main activity

		binding = FragmentHomeBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

//		binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				NavHostFragment.findNavController(HomeFragment.this)
//						.navigate(R.id.action_FirstFragment_to_SecondFragment);
//			}
//		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

}