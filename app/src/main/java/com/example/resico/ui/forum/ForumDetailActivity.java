package com.example.resico.ui.forum;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.R;
import com.example.resico.data.model.ForumComment;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.ActivityForumDetailBinding;
import com.example.resico.utils.DateTimeCalc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class ForumDetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private ActivityForumDetailBinding binding;

    private RecyclerView recyclerView;
    private ArrayList<ForumComment> forumComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForumDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        // Set adapter to recycle view
        recyclerView = binding.commentRecycle;
        ForumCommentAdapter adapter = new ForumCommentAdapter(forumComments);
        recyclerView.setAdapter(adapter);

        // Set coordinator height
//        int makeCommentHeight = binding.makeCommentConstraintLayout.getMeasuredHeight();
//        int makeCommentWidth = binding.makeCommentConstraintLayout.getMeasuredWidth();
//        int parentHeight = binding.forumDetailConstraintLayout.getMeasuredHeight();
//        int parentWidth = binding.forumDetailConstraintLayout.getMeasuredWidth();
//        Log.i(TAG, "onCreate: "+makeCommentHeight+makeCommentWidth+parentHeight+parentWidth);
//        binding.forumDetailCoordinator.setLayoutParams(new ConstraintLayout.LayoutParams(parentWidth,parentHeight-makeCommentHeight));

        // Retrieve event data from API
        ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
        String postId = getIntent().getStringExtra(getString(R.string.post_id_key));
        if (postId != null) {
            // Attach data to the corresponding component
            apiHandler.getForumPost(postId, post -> {
                if (post == null) return;
                apiHandler.getUser(post.getPosterUserId(), user -> {
                    binding.getRoot().post(() -> {
                        binding.postUserName.setText(user.getUsername());
                        Picasso.get().load(user.getImageUrl()).fit().centerCrop().into(binding.postUserImage);
                    });
                });
                binding.getRoot().post(() -> {
                    binding.forumTitle.setText(post.getTitle());
                    binding.forumDetail.setText(post.getContent());
                    Picasso.get().load(post.getImageUrl()).fit().centerCrop().into(binding.forumImage);
                    binding.likeAmount.setText(String.valueOf(post.getLikeUserId().length));
                    binding.postPostingTime.setText(" ∙ " + DateTimeCalc.getDurationToNow(post.getPostDateTime()) + " ago");
                });
            });
            apiHandler.getForumComments(postId, forumComments -> {
                if (forumComments == null) return;
                this.forumComments.clear();
                this.forumComments.addAll(Arrays.asList(forumComments));
                binding.getRoot().post(() -> adapter.notifyDataSetChanged());
            });
        }

        // LinearLayoutManager by default has vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
