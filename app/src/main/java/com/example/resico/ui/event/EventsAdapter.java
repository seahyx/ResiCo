package com.example.resico.ui.event;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.R;
import com.example.resico.data.model.Event;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.EventCardBinding;
import com.example.resico.utils.App;
import com.example.resico.utils.ListOnClickInterface;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

	private ArrayList<Event> events;

	private final ListOnClickInterface delegate;

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
	 */
	public EventsAdapter(ArrayList<Event> events, ListOnClickInterface onClickInterface) {
		this.events = events;
		delegate = onClickInterface;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(EventCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		// Bind the data to the element in the specified position
		Event event = events.get(position);
		holder.getEventTitleView().setText(event.getTitle());
		holder.getParticipantsView().setText(App.getContext().getString(R.string.event_participants, event.getParticipantCount()));
		holder.getBookmarkView().setChecked(event.getHasBookmarked());
		holder.getCardDateView().setText(event.getStartDateFormatted());
		Picasso.get().load(event.getImageUrl()).fit().centerCrop().into(holder.getBackgroundImageView());
		holder.cardView.setOnClickListener(view -> delegate.onItemClick(event.getEventId()));


		// Get host user information
		ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
		apiHandler.getUser(event.getHostId(), user -> {
			if (user == null) return;
			// Run UI updates on the UI thread
			holder.binding.getRoot().post(() -> {
				holder.getHostNameView().setText(user.getUsername());
				Picasso.get().load(user.getImageUrl()).fit().centerCrop().into(holder.hostProfileView);
			});
		});
	}

	@Override
	public int getItemCount() {
		return events.size();
	}
}
