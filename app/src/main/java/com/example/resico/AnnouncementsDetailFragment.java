package com.example.resico;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.Announcement;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.FragmentAnnouncementsDetailBinding;

public class AnnouncementsDetailFragment extends Fragment {

    private FragmentAnnouncementsDetailBinding binding;
    private Announcement announcement;

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
        binding.toolBar.setNavigationOnClickListener(view1 ->
                NavHostFragment.findNavController(AnnouncementsDetailFragment.this)
                        .navigate(R.id.action_announcements_detail_to_nav_announcements));
        ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
        User user =  LoginRepository.getUser();
        if (user != null) {
            //get specific announcement details from announcement id
            apiHandler.getAnnouncement(id,announcement -> {
                if (announcement == null) return;
                binding.collapsingBar.setTitle("This is a very very very very very very very very very long title that will span 3 lines maximum");
                binding.announcementsDetailDate.setText(announcement.getPostDateFormatted());
                //binding.announcementTitle.setText(announcement.getTitle());
                binding.announcementsDetail.setText(R.string.long_text);
                //binding.announcementDetail.setText(announcement.getDetail());
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}