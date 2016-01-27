package com.krave.kraveapp;

import io.fabric.sdk.android.Fabric;

import java.util.Timer;
import java.util.TimerTask;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ps.services.ChatService;
import com.ps.utill.KraveRestClient;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@ReportsCrashes(formKey = "", mailTo = "support@kraveapp.net")
public class MyApps extends Application {

	private static Context context;
	private static boolean isAppInForeground;

	public static boolean APPVISIBLEORNOT;
	int ONCHAT;

	// public static String gcmRegId="";
	// Handler mHandler = new Handler();
	// XMPPConnection connection;
	// KraveDAO databaseHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		// Fabric.with(this, new Crashlytics());
		Fabric.with(this, new Crashlytics());

		ACRA.init(this);

		/* BLOCK THE PARSE PUSH BECAUSE GOOGLE PUSH IS ACTIVE 30052015 */
		// Parse.initialize(this, "yiN1nFB62x5Ao4EQZEIk7mCEtFlApbiC4HxUJlIH",
		// "0wjpamzpyYLQIMUPxd0b60DaEaPWMvwIzjGbpYc6");
		// ParsePush.subscribeInBackground("", new SaveCallback() {
		// @Override
		// public void done(ParseException e) {
		// if (e == null) {
		//
		// } else {
		//
		// }
		// }
		// });

		/* tapstream code */

		// APPVISIBLEORNOT=false;
		// Ubertesters.initialize(this);

		MyApps.context = getApplicationContext();
		startHeartbeat();
	}

	public static boolean isActivityVisible() {
		return APPVISIBLEORNOT;
	}

	public static void activityResumed() {
		APPVISIBLEORNOT = true;
	}

	public static void activityPaused() {
		APPVISIBLEORNOT = false;
	}

	public static Context getAppContext() {
		return MyApps.context;
	}

	private void startHeartbeat() {
		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							if (ChatService.APPVISIBLEORNOT) {
								pulse();
							}
						} catch (Exception e) {

						}
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, 60000); // 1 minute
	}

	private void pulse() {
		SessionManager sessionManager = new SessionManager(
				MyApps.getAppContext());

		RequestParams params = new RequestParams();
		params.add("user_id", sessionManager.getUserDetail().getUserID());

		KraveRestClient.post(
				WebServiceConstants.AVAPIEndpointUpdateUserLastActionDateTime,
				params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}
				});

	}
}
