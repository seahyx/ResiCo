package com.example.resico;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.data.LoginRepository;
import com.example.resico.data.model.ForumComment;
import com.example.resico.data.model.User;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.FragmentForumDetailBinding;
import com.example.resico.ui.SpacesItemDecoration;
import com.example.resico.ui.adapters.ForumDetailAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ForumDetailFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    private FragmentForumDetailBinding binding;

    private RecyclerView recyclerView;
    private ArrayList<ForumComment> forumComments = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentForumDetailBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();


        // set adapter to recycle view
        recyclerView = binding.commentRecycle;
        ForumDetailAdapter adapter = new ForumDetailAdapter(forumComments);
        recyclerView.setAdapter(adapter);


        // Retrieve event data from API
        User user =  LoginRepository.getUser();
        if (user != null) {
            apiHandler.getForumComments("0", forumComments -> {
                if (forumComments == null) return;
                this.forumComments.clear();
                this.forumComments.addAll(Arrays.asList(forumComments));
                binding.getRoot().post(() -> adapter.notifyDataSetChanged());
            });
        }

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
