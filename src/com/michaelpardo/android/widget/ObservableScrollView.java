package com.michaelpardo.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {
	public static interface OnScrollChangedListener {
		public void onScrollChanged();
	}

	private OnScrollChangedListener mOnScrollChangedListener;

	public ObservableScrollView(Context context) {
		this(context, null, 0);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
		mOnScrollChangedListener = onScrollChangedListener;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged();
		}
	}

	@Override
	public int computeVerticalScrollRange() {
		return super.computeVerticalScrollRange();
	}
}