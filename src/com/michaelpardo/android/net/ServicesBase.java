package com.michaelpardo.android.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.michaelpardo.android.util.Log;

public abstract class ServicesBase {
	protected Context mContext;
	protected HttpRequestBase mHttpRequest;

	public ServicesBase(Context context) {
		mContext = context;
	}

	protected <T> T doGet(String url, List<NameValuePair> params, ResponseHandler<T> responseHandler) {
		if (params != null) {
			if (!url.endsWith("?")) {
				url += "?";
			}

			url += URLEncodedUtils.format(params, "UTF-8");
		}

		Log.i("Request URL: " + url);

		return doRequest(new HttpGet(url), responseHandler);
	}

	protected <T> T doPost(String url, List<NameValuePair> params, ResponseHandler<T> responseHandler) {
		HttpPost request = new HttpPost(url);
		HttpEntity entity;

		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			Log.e("Couldn't encode params", e);
			return null;
		}

		Header header = entity.getContentType();
		if (header != null) {
			request.addHeader(header);
		}

		request.setEntity(entity);

		Log.i("Request URL: " + url);

		return doRequest(request, responseHandler);
	}

	protected <T> T doPost(String url, String postBody, ResponseHandler<T> responseHandler) {
		HttpPost request = new HttpPost(url);
		HttpEntity entity;

		try {
			entity = new StringEntity(postBody);
		}
		catch (UnsupportedEncodingException e) {
			Log.e("Couldn't encode params", e);
			return null;
		}

		Header header = entity.getContentType();
		if (header != null) {
			request.addHeader(header);
		}

		request.setEntity(entity);

		Log.i("Request URL: " + url);

		return doRequest(request, responseHandler);
	}

	private <T> T doRequest(HttpRequestBase request, ResponseHandler<T> responseHandler) {
		mHttpRequest = request;

		AndroidHttpClient client = AndroidHttpClient.newInstance(getUserAgent(), mContext);
		AndroidHttpClient.modifyRequestToAcceptGzipResponse(mHttpRequest);

		HttpParams httpParameters = client.getParams();
		HttpConnectionParams.setSoTimeout(httpParameters, 100000);

		try {
			return client.execute(mHttpRequest, responseHandler);
		}
		catch (IOException e) {
			Log.e("Server request failed.", e);
		}
		finally {
			client.close();
		}

		return null;
	}

	private String getUserAgent() {
		String versionName;
		String appName;

		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);

			versionName = pi.versionName;
			appName = mContext.getString(pi.applicationInfo.labelRes);
		}
		catch (Exception e) {
			versionName = "1.0";
			appName = "Application";
		}

		return appName + "/" + versionName + " (Android; " + Build.VERSION.RELEASE + ")";
	}
}