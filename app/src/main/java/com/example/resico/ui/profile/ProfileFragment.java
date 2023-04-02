package com.example.resico.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resico.StartupActivity;
import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.User;
import com.example.resico.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

	private FragmentProfileBinding binding;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
	) {
		binding = FragmentProfileBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		User user = LoginRepository.getUser();
		if (user != null) {
			binding.profileDisplayName.setText(user.getFullName());
			binding.profileUsername.setText(user.getUsername());
			binding.profileEmail.setText(user.getEmail());
			binding.profilePhone.setText(user.getPhoneNo());
			Picasso.get().load(user.getImageUrl()).fit().centerCrop().into(binding.profileImage);
		}

		binding.profileLogout.setOnClickListener(view1 -> onLogoutClick());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	private void onLogoutClick() {
		LoginRepository.getInstance().logout();
		Intent logoutIntent = new Intent(getContext(), StartupActivity.class);
		startActivity(logoutIntent);
	}
}