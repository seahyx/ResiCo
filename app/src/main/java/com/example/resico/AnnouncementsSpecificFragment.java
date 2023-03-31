package com.example.resico;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resico.databinding.FragmentAnnouncementsSpecificBinding;

public class AnnouncementsSpecificFragment extends Fragment {

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
        String id = AnnouncementsSpecificFragmentArgs.fromBundle(getArguments()).getId();
        binding.topAppBar.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.topAppBar.setNavigationOnClickListener(view1 -> NavHostFragment.findNavController(AnnouncementsSpecificFragment.this).navigate(R.id.action_announcementsSpecificFragment_to_nav_announcements));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}