package com.example.resico.ui.forum;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.databinding.ForumTagBinding;

import java.util.ArrayList;


public class ForumTagsAdapter extends RecyclerView.Adapter<ForumTagsAdapter.ViewHolder> {

    private final ArrayList<String> forumTags;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ForumTagBinding binding;
        private final TextView forumTagView;

        public ForumTagBinding getBinding() {
            return binding;
        }

        public TextView getForumTagView() {
            return forumTagView;
        }

        public ViewHolder(ForumTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.forumTagView = binding.forumTag;
        }
    }

    public ForumTagsAdapter(ArrayList<String> forumTags) {
        this.forumTags = forumTags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ForumTagBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getForumTagView().setText(forumTags.get(position));
    }

    @Override
    public int getItemCount() {
        return forumTags.size();
    }
}
