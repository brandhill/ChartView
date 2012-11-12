package com.michaelpardo.android.net;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import com.michaelpardo.android.util.Log;
import com.michaelpardo.android.util.NetworkUtils;

public abstract class JsonResponseHandler<T> implements ResponseHandler<T> {
	public abstract T handleJson(JSONObject response);

	@Override
	public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		if (response == null || response.getStatusLine().getStatusCode() != 200) {
			return null;
		}

		try {
			String responseString = NetworkUtils.toString(response.getEntity(), HTTP.UTF_8);
			return handleJson(new JSONObject(responseString));
		}
		catch (IOException e) {
			Log.e("Server request failed.", e);
		}
		catch (JSONException e) {
			Log.e("Failed to parse server response.", e);
		}

		return null;
	}
}