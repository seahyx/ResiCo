package com.example.resico.ui;

import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
	public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
	public static final int VERTICAL = LinearLayout.VERTICAL;
	private int space;

	/**
	 * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
	 */
	private int mOrientation;

	/**
	 * Creates a spacer {@link RecyclerView.ItemDecoration} that can be used with a
	 * {@link LinearLayoutManager}.
	 *
	 * @param space Current context, it will be used to access resources.
	 * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
	 */
	public SpacesItemDecoration(int space, int orientation) {
		this.space = space;
		this.mOrientation = orientation;
	}

	@Override
	public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		int position = parent.getChildAdapterPosition(view);

		if (mOrientation == HORIZONTAL) {
			outRect.left = space / 2;
			outRect.right = space / 2;

			// Remove left margin only for the first item to ensure there is only spacing between items
			if (position == 0) {
				outRect.left = 0;
			}
			// Remove right margin only for the last item to ensure there is only spacing between items
			if (position == parent.getAdapter().getItemCount() - 1) {
				outRect.right = 0;
			}
			return;
		}
		else if (mOrientation == VERTICAL) {
			outRect.top = space / 2;
			outRect.bottom = space / 2;

			// Remove top margin only for the first item to ensure there is only spacing between items
			if (position == 0) {
				outRect.top = 0;
			}
			// Remove bottom margin only for the last item to ensure there is only spacing between items
			if (position == parent.getAdapter().getItemCount() - 1) {
				outRect.bottom = 0;
			}
			return;
		}
		super.getItemOffsets(outRect, view, parent, state);
	}
}
