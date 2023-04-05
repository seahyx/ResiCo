package com.example.resico.ui.forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.R;
import com.example.resico.data.model.ForumPost;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.ForumCardBinding;
import com.example.resico.utils.DateTimeUtils;
import com.example.resico.utils.ListOnClickInterface;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.ViewHolder> {

	private final ArrayList<ForumPost> forumPosts;
	private final ListOnClickInterface delegate;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final ForumCardBinding binding;

		private final MaterialCardView cardView;
		private final ShapeableImageView forumImage;
		private final TextView titleView;
		private final CircleImageView profileView;
		private final TextView usernameView;
		private final TextView postDateView;
		private final TextView likeView;
		private final TextView commentView;

		public ForumCardBinding getBinding() {
			return binding;
		}

		public MaterialCardView getCardView() {
			return cardView;
		}

		public ShapeableImageView getForumImage() {
			return forumImage;
		}

		public TextView getTitleView() {
			return titleView;
		}

		public CircleImageView getProfileView() {
			return profileView;
		}

		public TextView getUsernameView() {
			return usernameView;
		}

		public TextView getPostDateView() {
			return postDateView;
		}

		public TextView getLikeView() {
			return likeView;
		}

		public TextView getCommentView() {
			return commentView;
		}

		public ViewHolder(ForumCardBinding binding) {
			super(binding.getRoot());
			this.binding = binding;

			cardView = binding.forumCard;
			forumImage = binding.forumCardImage;
			titleView = binding.forumCardTitle;
			profileView = binding.forumCardProfile;
			usernameView = binding.forumCardUsername;
			postDateView = binding.forumCardPostTime;
			likeView = binding.forumCardLikeAmount;
			commentView = binding.forumCardCommentAmount;
		}
	}

	public ForumPostAdapter(ArrayList<ForumPost> forumPosts, ListOnClickInterface onClickInterface) {
		this.forumPosts = forumPosts;
		delegate = onClickInterface;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(ForumCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		ForumPost post = forumPosts.get(position);
		holder.getTitleView().setText(post.getTitle());
		holder.getPostDateView().setText(" ∙ " + DateTimeUtils.getDurationToNow(post.getPostDateTime()) + " ago");
		holder.getLikeView().setText(String.valueOf(post.getLikeUserId().length));
		holder.getCommentView().setText(String.valueOf(post.getCommentCount()));

		if (post.getImageUrl() == null || post.getImageUrl().equals("")) {
			holder.getForumImage().setVisibility(View.GONE);
		} else {
			holder.getForumImage().post(() -> {
				Picasso.get().load(post.getImageUrl()).error(R.drawable.placeholder_broken_image).into(holder.getForumImage());
			});
		}

		// Get post user information
		ResiCoAPIHandler apiHandler = ResiCoAPIHandler.getInstance();
		apiHandler.getUser(post.getPosterUserId(), user -> {
			if (user == null) return;
			// Run UI updates on the UI thread
			holder.getCardView().post(() -> {
				holder.getUsernameView().setText(user.getUsername());
				Picasso.get().load(user.getImageUrl()).error(R.drawable.placeholder_profile).fit().centerCrop().into(holder.profileView);
			});
		});

		// Handle onClick
		holder.getCardView().setOnClickListener(view -> delegate.onItemClick(post.getPostId()));
	}

	@Override
	public int getItemCount() {
		return forumPosts.size();
	}
}
