package com.example.resico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.AnnouncementModel;
import com.example.resico.AnnouncementRecyclerViewAdapter;
import com.example.resico.R;
import com.example.resico.RecyclerViewInterface;
import com.example.resico.databinding.FragmentNoticeBinding;

import java.util.ArrayList;

public class NoticeFragment extends Fragment implements RecyclerViewInterface {

    private FragmentNoticeBinding binding;


    ArrayList<AnnouncementModel> announcementModels = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentNoticeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.mRecyclerView);

        setUpAnnouncementModels();
        // instantiate adapter only after we have set up the models
        AnnouncementRecyclerViewAdapter adapter = new AnnouncementRecyclerViewAdapter(this.getActivity(),
                announcementModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpAnnouncementModels(){
        String[] announcementTitles = getResources().getStringArray(R.array.announcement_titles);
        String[] announcementDates = getResources().getStringArray(R.array.announcement_date);
        String[] announcementMessages = getResources().getStringArray(R.array.announcement_description);

        for (int i =0; i< announcementTitles.length; i++){
            announcementModels.add(new AnnouncementModel(announcementTitles[i],
                    announcementDates[i],announcementMessages[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
//        Fragment fragment = new ThirdFragment();
//        Bundle bundle = new Bundle();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        bundle.putString("TITLE",announcementModels.get(position).getAnnouncementTitle());
//        bundle.putString("DESCRIPTION",announcementModels.get(position).getAnnouncementMessage());
//        fragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.fragment_container_view, fragment, null);
        //fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        String title = announcementModels.get(position).getAnnouncementTitle();
        String description = announcementModels.get(position).getAnnouncementMessage();




    }
}