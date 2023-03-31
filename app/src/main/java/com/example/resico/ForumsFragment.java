package com.example.resico;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resico.databinding.FragmentEventsBinding;
import com.example.resico.databinding.FragmentForumsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumsFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumsFragment extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private FragmentForumsBinding binding;

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
		binding.buttonToForum.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				com.example.resico.ForumsFragmentDirections.ActionNavForumsToNavForumsDetail action = ForumsFragmentDirections.actionNavForumsToNavForumsDetail("0");
				NavHostFragment.findNavController(ForumsFragment.this).navigate(action);
			}
		});
	}

}