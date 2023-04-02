package com.example.resico.data.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.resico.utils.App;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

class UrlRequestCallback extends UrlRequest.Callback {
	private static final String TAG = "UrlRequestCallback";

	private final OnFinishRequest delegate;

	private String headers;
	private String responseBody;
	private int httpStatusCode;

	/**
	 * When using this callback, create a UrlRequestCallback.OnFinishRequest() and override onFinishRequest method.
	 * The JSON String response will be sent to this interface, when then you can use to perform actions on th UI or otherwise.
	 *
	 * @param onFinishRequest Delegate which is called the HTTP request is completed.
	 */
	public UrlRequestCallback(OnFinishRequest onFinishRequest) {
		delegate = onFinishRequest;
	}

	@Override
	public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
		// Continue processing the request in event of a redirect
		request.followRedirect();
	}

	@Override
	public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
		// Read the data into a bytebuffer - the request is received in chunks
		// In this case 102.4 kilobytes is allocated
		request.read(ByteBuffer.allocateDirect(102400));
	}

	@Override
	public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
		// Keep reading the request until there is no more data
		request.read(byteBuffer);

		int statusCode = info.getHttpStatusCode();
		httpStatusCode = statusCode;

		byte[] bytes;
		if (byteBuffer.hasArray()) {
			bytes = byteBuffer.array();
		} else {
			bytes = new byte[byteBuffer.remaining()];
			byteBuffer.get(bytes);
		}

		String responseBodyString = new String(bytes); // Convert bytes to string

		// Format the response string
		responseBodyString = responseBodyString.trim().replaceAll("(\r\n|\n\r|\r|\n|\r0|\n0)", "");
		if (responseBodyString.endsWith("0")) {
			responseBodyString = responseBodyString.substring(0, responseBodyString.length() - 1);
		}

		responseBody = responseBodyString;

		// Get headers
		Map<String, List<String>> headers = info.getAllHeaders();
		String reqHeaders = createHeaders(headers);

		JSONObject results = new JSONObject();
		try {
			results.put("headers", reqHeaders);
			results.put("body", responseBodyString);
			results.put("statusCode", statusCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Invoke callback on delegate
		delegate.onFinishRequest(results);
	}

	@Override
	public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
		// All the data has been read
		Log.i(TAG, "Successful request: " + info.getUrl() + " | Status code: " + info.getHttpStatusCode() + " | Bytes received: " + info.getReceivedByteCount());
	}

	@Override
	public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
		String inform = "Failed request: " + info.getUrl() +
				" | Status code: " + info.getHttpStatusCode() +
				" | Bytes received: " + info.getReceivedByteCount() +
				" | Caused by: " + error.getLocalizedMessage() + " (" + info.getHttpStatusText() + ").";
		Log.e(TAG, inform);

		JSONObject results = new JSONObject();
		try {
			results.put("headers", createHeaders(info.getAllHeaders()));
			results.put("body", inform);
			results.put("statusCode", info.getHttpStatusCode());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//Send to OnFinishRequest which we will override in activity to read results gotten.
		delegate.onFinishRequest(results);
	}

	private String createHeaders(Map<String, List<String>> headers) {
		SharedPreferences authPreferences = App.getContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
		String accessToken = "null";
		String client = "null";
		String uid = "null";
		long expiry = 0;

		if (headers.containsKey("Access-Token")) {
			List<String> accTok = headers.get("Access-Token");

			if (accTok.size() > 0) {
				accessToken = accTok.get(accTok.size() - 1);
			}
		}

		if (headers.containsKey("Client")) {
			List<String> cl = headers.get("Client");

			if (cl.size() > 0) {
				client = cl.get(cl.size() - 1);
			}
		}

		if (headers.containsKey("Uid")) {
			List<String> u = headers.get("Uid");

			if (u.size() > 0) {
				uid = u.get(u.size() - 1);
			}
		}

		if (headers.containsKey("Expiry")) {
			List<String> ex = headers.get("Expiry");

			if (ex.size() > 0) {
				expiry = Long.parseLong(ex.get(ex.size() - 1));
			}
		}

		JSONObject currentHeaders = new JSONObject();
		try {
			currentHeaders.put("access-token", accessToken);
			currentHeaders.put("client", client);
			currentHeaders.put("uid", uid);
			currentHeaders.put("expiry", expiry);

			return currentHeaders.toString();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return currentHeaders.toString();
	}

	public interface OnFinishRequest {
		void onFinishRequest(JSONObject result);
	}
}