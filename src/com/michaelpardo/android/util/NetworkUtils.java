package com.michaelpardo.android.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;

public class NetworkUtils {
	public static String toString(HttpEntity httpEntity, String defaultCharset) throws IOException {
		if (httpEntity == null) {
			return null;
		}

		// Get input stream
		InputStream inputStream = AndroidHttpClient.getUngzippedContent(httpEntity);

		if (inputStream == null) {
			return null;
		}

		// Get character encoding
		String encoding = EntityUtils.getContentCharSet(httpEntity);

		if (encoding == null) {
			encoding = defaultCharset;
		}

		if (encoding == null) {
			encoding = HTTP.DEFAULT_CONTENT_CHARSET;
		}

		return IOUtils.toString(inputStream, encoding);
	}
}