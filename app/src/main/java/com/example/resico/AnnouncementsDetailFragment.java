package com.example.resico;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resico.databinding.FragmentAnnouncementsSpecificBinding;

public class AnnouncementsDetailFragment extends Fragment {

    private FragmentAnnouncementsSpecificBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAnnouncementsSpecificBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String id = AnnouncementsDetailFragmentArgs.fromBundle(getArguments()).getId();
        binding.topAppBar.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.topAppBar.setNavigationOnClickListener(view1 -> NavHostFragment.findNavController(AnnouncementsDetailFragment.this).navigate(R.id.action_announcementsDetailFragment_to_nav_announcements));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}