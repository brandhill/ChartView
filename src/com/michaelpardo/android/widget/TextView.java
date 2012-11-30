package com.michaelpardo.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;

import com.michaelpardo.android.R;
import com.michaelpardo.android.util.TypefaceUtils;

@SuppressLint("ParserError")
public class TextView extends android.widget.TextView {
	private static final int NORMAL = 0;
	private static final int BOLD = 1;
	private static final int ITALIC = 2;
	private static final int BLACK = 8;
	private static final int CONDENSED = 16;
	private static final int LIGHT = 32;
	private static final int MEDIUM = 64;
	private static final int THIN = 128;

	public TextView(Context context) {
		this(context, null, 0);
	}

	public TextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		Resources.Theme theme = context.getTheme();
		TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.TextView, defStyle, 0);

		setTypefaceByStyle(a.getInt(R.styleable.TextView_textStyle, -1));
	}

	public void setTypefaceByStyle(int style) {
		switch (style) {
		case BLACK | ITALIC: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_BLACK_ITALIC);
			break;
		}
		case BLACK: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_BLACK);
			break;
		}
		case BOLD | CONDENSED | ITALIC: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_BOLD_CONDENSED_ITALIC);
			break;
		}
		case BOLD | CONDENSED: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_BOLD_CONDENSED);
			break;
		}
		case BOLD: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_BOLD);
			break;
		}
		case CONDENSED | ITALIC: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_CONDENSED_ITALIC);
			break;
		}
		case CONDENSED: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_CONDENSED);
			break;
		}
		case LIGHT | ITALIC: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_LIGHT_ITALIC);
			break;
		}
		case LIGHT: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_LIGHT);
			break;
		}
		case THIN | ITALIC: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_THIN_ITALIC);
			break;
		}
		case THIN: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_THIN);
			break;
		}
		case MEDIUM | ITALIC: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_MEDIUM_ITALIC);
			break;
		}
		case MEDIUM: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_MEDIUM);
			break;
		}
		case ITALIC: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_ITALIC);
			break;
		}
		case NORMAL: {
			TypefaceUtils.loadTypeface(this, TypefaceUtils.ROBOTO_REGULAR);
			break;
		}
		}
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if (focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean focused) {
		if (focused) {
			super.onWindowFocusChanged(focused);
		}
	}

	@Override
	public boolean isFocused() {
		return getEllipsize() == TruncateAt.MARQUEE;
	}
}