package com.example.resico.ui.forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resico.R;
import com.example.resico.data.model.ForumPost;
import com.example.resico.data.network.ResiCoAPIHandler;
import com.example.resico.databinding.ForumCardBinding;
import com.example.resico.ui.SpacesItemDecoration;
import com.example.resico.utils.App;
import com.example.resico.utils.DateTimeUtils;
import com.example.resico.utils.ListOnClickInterface;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.ViewHolder> {

	private final ArrayList<ForumPost> forumPosts;
	private final ListOnClickInterface delegate;

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

		// Set adapter to forum tags recycle view
		RecyclerView forumTagsRecycleView = holder.getForumTagsView();
		ForumTagsAdapter adapter = new ForumTagsAdapter(new ArrayList<>(Arrays.asList(post.getForumTags())));
		forumTagsRecycleView.setAdapter(adapter);

		// Flexbox layout
		FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(forumTagsRecycleView.getContext());
		forumTagsRecycleView.setLayoutManager(flexboxLayoutManager);

		// Add spacing between recycle items
		FlexboxItemDecoration itemDecoration = new FlexboxItemDecoration(forumTagsRecycleView.getContext());
		itemDecoration.setOrientation(FlexboxItemDecoration.HORIZONTAL); // or VERTICAL or BOTH
		forumTagsRecycleView.addItemDecoration(itemDecoration);

		SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(
				(int) App.getContext().getResources().getDimension(R.dimen.component_small_margin),
				LinearLayoutManager.HORIZONTAL);
		forumTagsRecycleView.addItemDecoration(spacesItemDecoration);


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

		// Handle onClick on like button
		holder.getLikeIcView().setOnClickListener(v -> {
			apiHandler.putForumLike(post.getPostId(), post.getLikeUserId(), data -> {
				if (data == null) return;
			});
//			holder.getLikeIcView().setImageDrawable();
		});

	}

	@Override
	public int getItemCount() {
		return forumPosts.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final ForumCardBinding binding;

		private final MaterialCardView cardView;
		private final RecyclerView forumTagsView;
		private final ShapeableImageView forumImage;
		private final TextView titleView;
		private final CircleImageView profileView;
		private final TextView usernameView;
		private final TextView postDateView;
		private final TextView likeView;
		private final TextView commentView;
		private final ImageView likeIcView;

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
			forumTagsView = binding.forumCardTags;
			likeIcView = binding.forumCardLikeIc;
		}

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

		public RecyclerView getForumTagsView() {
			return forumTagsView;
		}

		public ImageView getLikeIcView() {
			return likeIcView;
		}
	}
}
