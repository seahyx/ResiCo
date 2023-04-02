package com.example.resico.ui.announcements;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resico.AnnouncementsDetailFragmentArgs;
import com.example.resico.R;
import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AnnouncementsDetailFragment extends Fragment {

    private FragmentAnnouncementsDetailBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAnnouncementsDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get id from previous announcement fragment
        String id = AnnouncementsDetailFragmentArgs.fromBundle(getArguments()).getId();
        binding.toolBar.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.toolBar.setNavigationOnClickListener(view1 ->{
                NavHostFragment.findNavController(AnnouncementsDetailFragment.this)
                        .navigate(R.id.action_announcements_detail_to_nav_announcements);
                BottomNavigationView btmNav = getActivity().findViewById(R.id.bottom_nav);
                btmNav.setVisibility(View.VISIBLE);
        });
        ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
        User user =  LoginRepository.getUser();
        if (user != null) {
            //get specific announcement details from announcement id
            apiHandler.getAnnouncement(id,announcement -> {
                if (announcement == null) return;
                binding.announcementsDetailTitle.setText(announcement.getTitle());
                binding.announcementsDetailDate.setText(announcement.getPostDateFormatted());
                binding.announcementsDetail.setText(announcement.getDetail());
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}