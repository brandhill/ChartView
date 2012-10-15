package com.michaelpardo.android.content;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.michaelpardo.android.util.AndroidUtils;
import com.michaelpardo.android.util.Log;

@TargetApi(11)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TaskManager {
	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTANTS
	//////////////////////////////////////////////////////////////////////////////////////

	private static final int NULL_VALUE = -1;

	//////////////////////////////////////////////////////////////////////////////////////
	// INTERFACES
	//////////////////////////////////////////////////////////////////////////////////////

	public interface Task<T> {
		public T execute();
	}

	public interface TaskListener<T> {
		public void onTaskCanceled();

		public void onTaskCompleted(T results);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	private static ConcurrentHashMap<String, ManagedTask> mTasks = new ConcurrentHashMap<String, ManagedTask>();
	private static ConcurrentHashMap<String, Object> mResults = new ConcurrentHashMap<String, Object>();

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR / SINGLETON
	//////////////////////////////////////////////////////////////////////////////////////

	private TaskManager() {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public static <T> void startTask(String key, Task<T> task, TaskListener<T> listener) {
		if (isExecuting(key)) {
			addListener(key, listener);
			return;
		}

		if (!mTasks.containsKey(key)) {
			Log.i("Starting task: " + key);

			ManagedTask managedTask = new ManagedTask(key, task);
			managedTask.addListener(listener);

			mTasks.put(key, managedTask);

			if (AndroidUtils.getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB) {
				managedTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			else {
				managedTask.execute();
			}
		}
	}

	public static boolean cancelTask(String key) {
		if (mResults.containsKey(key)) {
			Log.i("Removing results: " + key);

			mResults.remove(key);
		}
		else if (mTasks.containsKey(key)) {
			Log.i("Cancelling task: " + key);

			ManagedTask task = mTasks.remove(key);
			for (TaskListener<?> listener : task.getListeners()) {
				listener.onTaskCanceled();
			}

			return task.cancel(true);
		}

		return true;
	}

	public static boolean isExecuting(String key) {
		return mResults.containsKey(key) || mTasks.containsKey(key);
	}

	public static void addListener(String key, TaskListener listener) {
		if (mResults.containsKey(key)) {
			Object results = getResultValue(mResults.remove(key));

			if (listener != null) {
				listener.onTaskCompleted(results);
			}

			return;
		}

		if (mTasks.containsKey(key)) {
			mTasks.get(key).addListener(listener);
		}
	}

	public static void removeListener(String key, TaskListener<?> listener) {
		if (mTasks.containsKey(key)) {
			ManagedTask task = mTasks.get(key);
			task.removeListener(listener);
		}
	}

	public static void clearListener(String key) {
		if (mTasks.containsKey(key)) {
			ManagedTask task = mTasks.get(key);
			task.clearListeners();
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private static Object getResultValue(Object result) {
		if (result.equals(NULL_VALUE)) {
			result = null;
		}

		return result;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES
	//////////////////////////////////////////////////////////////////////////////////////

	private static class ManagedTask extends AsyncTask<Void, Void, Object> {
		private String mKey;
		private Task<?> mTask;
		private List<TaskListener<?>> mListeners;

		public ManagedTask(String key, Task<?> task) {
			mKey = key;
			mTask = task;
			mListeners = new ArrayList<TaskListener<?>>();
		}

		@Override
		protected Object doInBackground(Void... params) {
			return mTask.execute();
		}

		@Override
		protected void onPostExecute(Object result) {
			Log.i("Task completed: " + mKey);

			if (result == null) {
				result = NULL_VALUE;
			}

			mResults.put(mKey, result);
			mTasks.remove(mKey);

			if (mListeners != null && mListeners.size() > 0) {
				Object results = getResultValue(mResults.remove(mKey));

				for (TaskListener listener : mListeners) {
					if (listener != null) {
						listener.onTaskCompleted(results);
					}
				}
			}
		}

		public void addListener(TaskListener<?> listener) {
			mListeners.add(listener);
		}

		public void removeListener(TaskListener<?> listener) {
			mListeners.remove(listener);
		}

		public void clearListeners() {
			mListeners.clear();
		}

		public List<TaskListener<?>> getListeners() {
			return mListeners;
		}
	}
}