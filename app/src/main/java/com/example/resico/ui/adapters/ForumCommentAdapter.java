package com.example.resico.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.data.model.ForumComment;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.CommentBinding;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumCommentAdapter extends RecyclerView.Adapter<ForumCommentAdapter.ForumDetailHolder> {

    private ArrayList<ForumComment> forumComments;

    public static class ForumDetailHolder extends RecyclerView.ViewHolder {
        private final CommentBinding binding;
        private final CircleImageView userImageView;
        private final TextView userNameView;
        private final TextView postDateTimeView;
        private final TextView commentView;

        public ForumDetailHolder(CommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            userNameView = binding.username;
            postDateTimeView = binding.postTime;
            commentView = binding.commentBody;
            userImageView = binding.commentProfileImage;

        }

        public TextView getUserNameView() {
            return userNameView;
        }

        public CircleImageView getUserImageView() {
            return userImageView;
        }

        public TextView getCommentView() {
            return commentView;
        }

        public TextView getPostDateTimeView() {
            return postDateTimeView;
        }
    }

    /**
     * Initialize dataset of the Adapter.
     *
     * */
    public ForumCommentAdapter(ArrayList<ForumComment> comment) {
        this.forumComments = comment;
    }

    @NonNull
    @Override
    public ForumCommentAdapter.ForumDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForumCommentAdapter.ForumDetailHolder(CommentBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ForumCommentAdapter.ForumDetailHolder holder, int position) {
        // Bind the data to the element in the specified position
        ForumComment comment = forumComments.get(position);
        holder.getCommentView().setText(comment.getComment());
        holder.getPostDateTimeView().setText(" ∙ " + getDurationToNow(comment.getPostDateTime()) + " ago");


        // Get host user information
        ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
        apiHandler.getUser(comment.getUserId(), user -> {
            if (user == null) return;
            // Run UI updates on the UI thread
            holder.binding.getRoot().post(() -> {
                holder.userNameView.setText(user.getUsername());
                Picasso.get().load(user.getImageUrl()).fit().centerCrop().into(holder.userImageView);
            });
        });
    }

    /**
     * Calculate duration of a date until now.
     * @param fromDateTime A time in the past!
     * @return most significant duration from input dateTime to now.
     */
    private String getDurationToNow(LocalDateTime fromDateTime) {
        LocalDateTime toDateTime = LocalDateTime.now();
        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);
        if (years != 0) return years + "y";

        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);
        if (months != 0) return months + "mo";

        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);
        if (days != 0) return days + "d";

        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);
        if (hours != 0) return hours + "h";

        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);
        if (minutes != 0) return minutes + "m";

        long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);
        return seconds + "s";
    }
    @Override
    public int getItemCount() {
        return forumComments.size();
    }
}
