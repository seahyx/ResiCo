package com.example.resico.ui.events;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.App;
import com.example.resico.R;
import com.example.resico.data.model.Event;
import com.example.resico.databinding.EventCardBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

	private Event[] events;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final EventCardBinding binding;

		private final MaterialCardView cardView;
		private final ImageView backgroundImageView;
		private final TextView participantsView;
		private final MaterialCheckBox bookmarkView;
		private final TextView eventTitleView;
		private final CircleImageView hostProfileView;
		private final TextView hostNameView;
		private final TextView cardDateView;

		public MaterialCardView getCardView() {
			return cardView;
		}

		public ImageView getBackgroundImageView() {
			return backgroundImageView;
		}

		public TextView getParticipantsView() {
			return participantsView;
		}

		public MaterialCheckBox getBookmarkView() {
			return bookmarkView;
		}

		public TextView getEventTitleView() {
			return eventTitleView;
		}

		public CircleImageView getHostProfileView() {
			return hostProfileView;
		}

		public TextView getHostNameView() {
			return hostNameView;
		}

		public TextView getCardDateView() {
			return cardDateView;
		}

		public ViewHolder(EventCardBinding binding) {
			super(binding.getRoot());
			this.binding = binding;

			cardView = binding.eventCard;
			backgroundImageView = binding.eventCardBackground;
			participantsView = binding.eventCardParticipants;
			bookmarkView = binding.eventCardBookmark;
			eventTitleView = binding.eventCardTitle;
			hostProfileView = binding.eventCardHostProfile;
			hostNameView = binding.eventCardHostName;
			cardDateView = binding.eventCardDate;
		}
	}

	/**
	 * Initialize dataset of the Adapter.
	 *
	 * */
	public EventsAdapter(Event[] events) {
		this.events = events;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(EventCardBinding.inflate(LayoutInflater.from(parent.getContext())));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		// Bind the data to the element in the specified position
		Event event = events[position];
		holder.getEventTitleView().setText(event.getTitle());
		holder.getParticipantsView().setText(App.getContext().getString(R.string.event_participants, event.getParticipantCount()));
		holder.getEventTitleView().setText(event.getTitle());
	}

	@Override
	public int getItemCount() {
		return events.length;
	}
}
