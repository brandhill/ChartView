package com.michaelpardo.android.content;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Process;
import android.widget.ImageView;

import com.michaelpardo.android.content.TaskManager.Task;
import com.michaelpardo.android.content.TaskManager.TaskListener;
import com.michaelpardo.android.util.Log;

public class ImageManager {
	//////////////////////////////////////////////////////////////////////////////////////
	// INTERFACES
	//////////////////////////////////////////////////////////////////////////////////////

	public interface ImageListener {
		public void onImageLoaded(String url, Bitmap bitmap);

		public void onImageLoadFailed(String url);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTANTS
	//////////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	private static ConcurrentHashMap<String, Bitmap> mCache = new ConcurrentHashMap<String, Bitmap>();
	private static Set<String> mIgnore = new HashSet<String>();

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	private ImageManager() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public static void addImage(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
	}

	public static Bitmap getImage(String url) {
		return mCache.get(url);
	}

	public static boolean containsImage(String url) {
		return mCache.containsKey(url);
	}

	public static boolean loadImage(String url) {
		return loadImage(url, url, null);
	}

	public static boolean loadImage(String url, ImageListener listener) {
		return loadImage(url, url, listener);
	}

	public static boolean loadImage(String url, final ImageView imageView) {
		final String key = imageView.toString();
		final ImageListener listener = new ImageListener() {
			public void onImageLoaded(String url, Bitmap bitmap) {
				imageView.setImageBitmap(bitmap);
			}

			public void onImageLoadFailed(String url) {

			}
		};

		return loadImage(key, url, listener);
	}

	public static boolean loadImage(String key, String url, ImageListener listener) {
		if (containsImage(url)) {
			if (listener != null) {
				listener.onImageLoaded(url, mCache.get(url));
			}
			return true;
		}
		else if (mIgnore.contains(url)) {
			if (listener != null) {
				listener.onImageLoadFailed(url);
			}
			return false;
		}

		if (TaskManager.isExecuting(key)) {
			TaskManager.clearListener(key);
		}

		TaskManager.startTask(key, new ImageDownloadTask(url), new ImageDownloadTaskListener(url, listener));

		return false;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////////////
	// TASKS / LISTENERS
	//////////////////////////////////////////////////////////////////////////////////////

	private static class ImageDownloadTask implements Task<Bitmap> {
		private String mUrl;
		private HttpURLConnection mConn;

		public ImageDownloadTask(String url) {
			mUrl = url;
		}

		@Override
		public Bitmap execute() {
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

			URL imageUrl = null;
			try {
				imageUrl = new URL(mUrl);
			}
			catch (MalformedURLException e) {
				Log.w("Malformed image url.", e);
				return null;
			}

			try {
				mConn = (HttpURLConnection) imageUrl.openConnection();
				mConn.setDoInput(true);
				mConn.connect();
				InputStream in = mConn.getInputStream();
				return BitmapFactory.decodeStream(in);
			}
			catch (FileNotFoundException e) {
				Log.w("Ignoring url because it could not be found: " + mUrl);
				mIgnore.add(mUrl);
				return null;
			}
			catch (IOException e) {
				Log.w("Could not fetch image, could not load.", e);
				return null;
			}
			catch (OutOfMemoryError e) {
				Log.e("Could not fetch image, ran out of memory.", e);
				return null;
			}
		}
	}

	private static class ImageDownloadTaskListener implements TaskListener<Bitmap> {
		private String mUrl;
		private ImageListener mImageListener;

		public ImageDownloadTaskListener(String url, ImageListener imageListener) {
			mUrl = url;
			mImageListener = imageListener;
		}

		@Override
		public void onTaskCanceled() {
			if (mImageListener != null) {
				mImageListener.onImageLoadFailed(mUrl);
			}
		}

		@Override
		public void onTaskCompleted(Bitmap results) {
			if (results == null) {
				if (mImageListener != null) {
					mImageListener.onImageLoadFailed(mUrl);
				}
				return;
			}

			mCache.put(mUrl, results);
			if (mImageListener != null) {
				mImageListener.onImageLoaded(mUrl, results);
			}
		}
	}
}