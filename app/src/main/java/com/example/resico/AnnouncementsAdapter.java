package com.example.resico;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.data.model.Announcement;
import com.example.resico.databinding.AnnouncementListItemBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder> {
    ArrayList<Announcement> announcements;
    private final ListOnClickInterface listOnClickInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AnnouncementListItemBinding binding;
        private final MaterialCardView cardView;
        private final TextView titleView;
        private final TextView dateView;
        private final TextView previewView;

        public AnnouncementListItemBinding getBinding() {
            return binding;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public MaterialCardView getCardView() {
            return cardView;
        }

        public TextView getDateView() {
            return dateView;
        }

        public TextView getPreviewView() {
            return previewView;
        }

        public ViewHolder(AnnouncementListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            titleView = binding.announcementTitle;
            dateView = binding.announcementDate;
            previewView = binding.announcementPreview;
            cardView = binding.announcementCard;

        }
    }


    public AnnouncementsAdapter(ArrayList<Announcement> announcements, ListOnClickInterface listOnClickInterface){
        this.announcements = announcements;
        this.listOnClickInterface = listOnClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AnnouncementListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // assigning values to views we created in the recycler_view_row layout file
        // based on the position of the recycler view
        Announcement announcement = announcements.get(position);
        holder.getTitleView().setText(announcement.getTitle());
        holder.getDateView().setText(announcement.getPostDateFormatted());
        holder.getPreviewView().setText(announcement.getDetail());
        holder.getCardView().setOnClickListener(view -> listOnClickInterface.onItemClick(announcement.getAnnouncementId()));
    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        return announcements.size();
    }
}
