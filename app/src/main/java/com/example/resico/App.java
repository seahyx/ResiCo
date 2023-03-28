package com.example.resico;

import android.app.Application;
import android.content.Context;

/*
* Provides a static reference to the application context anywhere in the app.
* */
public class App extends Application {
	private static Application application;

	public static Application getApplication() {
		return application;
	}

	public static Context getContext() {
		return getApplication().getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
	}
}
