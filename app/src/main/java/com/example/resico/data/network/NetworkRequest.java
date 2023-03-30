package com.example.resico.data.network;

import com.example.resico.App;

import org.chromium.net.CronetEngine;
import org.chromium.net.UploadDataProvider;
import org.chromium.net.UploadDataProviders;
import org.chromium.net.UrlRequest;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Uses the {@link org.chromium.net.CronetEngine} to send requests and receive data from our API host.
 */
public class NetworkRequest {
	private static final CronetEngine engine = new CronetEngine.Builder(App.getContext()).build();
	private static Executor executor = Executors.newSingleThreadExecutor();

	public static UrlRequest.Builder makeRequestBuilder(String url, String httpMethod, UrlRequest.Callback callback) {
		UrlRequest.Builder requestBuilder = engine.newUrlRequestBuilder(url, callback, executor);

		requestBuilder.setHttpMethod(httpMethod);

		// Add request headers if any
		requestBuilder.addHeader("User-Agent", System.getProperty("http.agent"));
		requestBuilder.addHeader("Accept", "*/*"); // Accept any type of response

		return requestBuilder;
	}

	public static UploadDataProvider generateUploadDataProvider(String payload) {
		byte[] bytes = convertStringToBytes(payload);

		return UploadDataProviders.create(bytes);
	}

	public static byte[] convertStringToBytes(String payload) {
		byte[] bytes;
		ByteBuffer byteBuffer = ByteBuffer.wrap(payload.getBytes());
		if (byteBuffer.hasArray()) {
			bytes = byteBuffer.array();
		} else {
			bytes = new byte[byteBuffer.remaining()];
			byteBuffer.get(bytes);
		}

		return bytes;
	}

	public static void get(String url, UrlRequest.Callback callback) {
		UrlRequest.Builder requestBuilder = makeRequestBuilder(url, "GET", callback);
		UrlRequest request = requestBuilder.build();
		request.start();
	}


	public static void put(String url, String payload, UrlRequest.Callback callback) {
		UrlRequest.Builder requestBuilder = makeRequestBuilder(url, "PUT", callback);

		requestBuilder.setUploadDataProvider(generateUploadDataProvider(payload), executor);

		UrlRequest request = requestBuilder.build();

		request.start();
	}

	public static void post(String url, String payload, UrlRequest.Callback callback) {
		UrlRequest.Builder requestBuilder = makeRequestBuilder(url, "POST", callback);

		requestBuilder.setUploadDataProvider(generateUploadDataProvider(payload), executor);
		requestBuilder.addHeader("Content-Type", "application/json");

		UrlRequest request = requestBuilder.build();

		request.start();
	}

	public static void delete(String url, String payload, UrlRequest.Callback callback) {
		UrlRequest.Builder requestBuilder = makeRequestBuilder(url, "DELETE", callback);

		requestBuilder.setUploadDataProvider(generateUploadDataProvider(payload), executor);

		UrlRequest request = requestBuilder.build();

		request.start();
	}

	public static String addQueryParameter(String url, String argument, String value) {
		return url + (url.contains("?") ? "&" : "?") + argument + "=" + value;
	}
}


