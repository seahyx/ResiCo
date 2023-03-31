package com.example.resico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.data.model.Event;
import com.example.resico.data.model.ForumComment;
import com.example.resico.databinding.FragmentForumDetailBinding;
import com.example.resico.databinding.FragmentHomeBinding;
import com.example.resico.ui.SpacesItemDecoration;
import com.example.resico.ui.adapters.ForumDetailAdapter;

public class ForumDetailFragment extends Fragment {

    private FragmentForumDetailBinding binding;

    private RecyclerView recyclerView;
    private ForumComment[] forumComments;

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

        // SET UP RECYCLE VIEW
        recyclerView = binding.commentRecycle;
        ForumDetailAdapter adapter = new ForumDetailAdapter(forumComments);
        // LinearLayoutManager by default has vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Add spacing between cards
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(
                (int) getResources().getDimension(R.dimen.component_medium_margin),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);
        // FINISH SET UP

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
