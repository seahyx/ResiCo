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
		if (mOrientation == HORIZONTAL) {
			outRect.left = space;

			// Remove left margin only for the first item to ensure there is only spacing between items
			if (parent.getChildAdapterPosition(view) == 0) {
				outRect.left = 0;
			}
			return;
		}
		else if (mOrientation == VERTICAL) {
			outRect.top = space;

			// Remove top margin only for the first item to ensure there is only spacing between items
			if (parent.getChildAdapterPosition(view) == 0) {
				outRect.top = 0;
			}
			return;
		}
		super.getItemOffsets(outRect, view, parent, state);
	}
}
