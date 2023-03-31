package com.example.resico.ui.forums;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.App;
import com.example.resico.R;
import com.example.resico.data.model.Event;
import com.example.resico.data.model.ForumComment;
import com.example.resico.databinding.CommentBinding;
import com.example.resico.databinding.EventCardBinding;
import com.example.resico.ui.events.EventsAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumDetailAdapter extends RecyclerView.Adapter<ForumDetailAdapter.ForumDetailHolder> {

    private ForumComment[] forumComments;

    public static class ForumDetailHolder extends RecyclerView.ViewHolder {
        private final CommentBinding binding;
        private final TextView userNameView;


        public ForumDetailHolder(CommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            userNameView = binding.username;
        }

        public TextView getUserNameView() {
            return userNameView;
        }
    }

    /**
     * Initialize dataset of the Adapter.
     *
     * */
    public ForumDetailAdapter(ForumComment[] comment) {
        this.forumComments = comment;
    }

    @NonNull
    @Override
    public ForumDetailAdapter.ForumDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForumDetailAdapter.ForumDetailHolder(CommentBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ForumDetailAdapter.ForumDetailHolder holder, int position) {
        // Bind the data to the element in the specified position
        ForumComment comment = forumComments[position];
    }

    @Override
    public int getItemCount() {
        return forumComments.length;
    }
}
