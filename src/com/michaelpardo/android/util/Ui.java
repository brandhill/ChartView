package com.michaelpardo.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

@SuppressWarnings("unchecked")
public class Ui {
	public static Bitmap createBitmapFromView(View v) {
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(canvas);

		return bitmap;
	}

	public static <T> T findView(Activity activity, int resId) {
		return (T) activity.findViewById(resId);
	}

	public static <T> T findView(View view, int resId) {
		return (T) view.findViewById(resId);
	}

	public static <T> T findFragment(FragmentActivity activity, int resId) {
		return (T) activity.getSupportFragmentManager().findFragmentById(resId);
	}

	public static <T> T findFragment(FragmentActivity activity, String tag) {
		return (T) activity.getSupportFragmentManager().findFragmentByTag(tag);
	}

	// Keyboard

	public static void hideSoftKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showSoftkeyboard(View view) {
		showSoftkeyboard(view, null);
	}

	public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
		Configuration config = view.getContext().getResources().getConfiguration();
		if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);

			if (resultReceiver != null) {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT, resultReceiver);
			}
			else {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
			}
		}
	}
}