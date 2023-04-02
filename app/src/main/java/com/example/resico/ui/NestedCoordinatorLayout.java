package com.example.resico.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.coordinatorlayout.R.attr;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.JvmOverloads;

public final class NestedCoordinatorLayout extends CoordinatorLayout implements NestedScrollingChild3 {
	private final NestedScrollingChildHelper helper;

	public boolean isNestedScrollingEnabled() {
		return this.helper.isNestedScrollingEnabled();
	}

	public void setNestedScrollingEnabled(boolean enabled) {
		this.helper.setNestedScrollingEnabled(enabled);
	}

	public boolean hasNestedScrollingParent(int type) {
		return this.helper.hasNestedScrollingParent(type);
	}

	public boolean hasNestedScrollingParent() {
		return this.helper.hasNestedScrollingParent();
	}

	public boolean onStartNestedScroll(@NotNull View child, @NotNull View target, int axes, int type) {
		boolean superResult = super.onStartNestedScroll(child, target, axes, type);
		return this.startNestedScroll(axes, type) || superResult;
	}

	public boolean onStartNestedScroll(@NotNull View child, @NotNull View target, int axes) {
		boolean superResult = super.onStartNestedScroll(child, target, axes);
		return this.startNestedScroll(axes) || superResult;
	}

	public void onNestedPreScroll(@NotNull View target, int dx, int dy, @NotNull int[] consumed, int type) {
		int[] superConsumed = new int[]{0, 0};
		super.onNestedPreScroll(target, dx, dy, superConsumed, type);
		int[] thisConsumed = new int[]{0, 0};
		this.dispatchNestedPreScroll(dx, dy, consumed, null, type);
		consumed[0] = superConsumed[0] + thisConsumed[0];
		consumed[1] = superConsumed[1] + thisConsumed[1];
	}

	public void onNestedPreScroll(@NotNull View target, int dx, int dy, @NotNull int[] consumed) {
		int[] superConsumed = new int[]{0, 0};
		super.onNestedPreScroll(target, dx, dy, superConsumed);
		int[] thisConsumed = new int[]{0, 0};
		this.dispatchNestedPreScroll(dx, dy, consumed, null);
		consumed[0] = superConsumed[0] + thisConsumed[0];
		consumed[1] = superConsumed[1] + thisConsumed[1];
	}

	public void onNestedScroll(@NotNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NotNull int[] consumed) {
		this.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type);
		super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
	}

	public void onNestedScroll(@NotNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
		super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
		this.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type);
	}

	public void onNestedScroll(@NotNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
		super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
		this.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null);
	}

	public void onStopNestedScroll(@NotNull View target, int type) {
		super.onStopNestedScroll(target, type);
		this.stopNestedScroll(type);
	}

	public void onStopNestedScroll(@NotNull View target) {
		super.onStopNestedScroll(target);
		this.stopNestedScroll();
	}

	public boolean onNestedPreFling(@NotNull View target, float velocityX, float velocityY) {
		boolean superResult = super.onNestedPreFling(target, velocityX, velocityY);
		return this.dispatchNestedPreFling(velocityX, velocityY) || superResult;
	}

	public boolean onNestedFling(@NotNull View target, float velocityX, float velocityY, boolean consumed) {
		boolean superResult = super.onNestedFling(target, velocityX, velocityY, consumed);
		return this.dispatchNestedFling(velocityX, velocityY, consumed) || superResult;
	}

	public boolean startNestedScroll(int axes, int type) {
		return this.helper.startNestedScroll(axes, type);
	}

	public boolean startNestedScroll(int axes) {
		return this.helper.startNestedScroll(axes);
	}

	public void stopNestedScroll(int type) {
		this.helper.stopNestedScroll(type);
	}

	public void stopNestedScroll() {
		this.helper.stopNestedScroll();
	}

	public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NotNull int[] consumed) {
		this.helper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
	}

	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
		return this.helper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
	}

	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
		return this.helper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
	}

	public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
		return this.helper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
	}

	public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
		return this.helper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
	}

	public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
		return this.helper.dispatchNestedPreFling(velocityX, velocityY);
	}

	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
		return this.helper.dispatchNestedFling(velocityX, velocityY, consumed);
	}

	@JvmOverloads
	public NestedCoordinatorLayout(@NotNull Context context, @Nullable AttributeSet attrs, @AttrRes @SuppressLint({"PrivateResource"}) int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.helper = new NestedScrollingChildHelper(this);
		this.setNestedScrollingEnabled(true);
	}

	// $FF: synthetic method
	public NestedCoordinatorLayout(Context var1, AttributeSet var2, int var3, int var4) {
		this(var1, (var4 & 2) != 0 ? null : var2, (var4 & 4) != 0 ? attr.coordinatorLayoutStyle : var3);
	}

	@JvmOverloads
	public NestedCoordinatorLayout(@NotNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0, 4);
	}

	@JvmOverloads
	public NestedCoordinatorLayout(@NotNull Context context) {
		this(context, null, 0, 6);
	}
}
