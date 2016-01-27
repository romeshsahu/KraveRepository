package com.krave.kraveapp;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	// give your server registration url here
	// static final String SERVER_URL =
	// "http://10.0.2.2/gcm_server_php/register.php";
	// static final String SERVER_URL =
	// "http://parkhya.org/test/GCM/register.php";
	// Google project id
	// public static final String SENDER_ID = "700079896076";
	// public static final String SENDER_ID = "570557203706";// krave client id
	public static final String SENDER_ID = "638472218602";
	// public static final String SENDER_ID = "192505512966";//demo id
	// 617151409471
	/**
	 * Tag used on log messages.
	 */
	// static final String TAG = "LosVegas App";
	// edited on revision 594 dl commented
	// MAY 14 2015
	// public static final String DISPLAY_MESSAGE_ACTION =
	// "com.ps.krave.DISPLAY_MESSAGE";
	public static final String DISPLAY_MESSAGE_ACTION = "com.krave.kraveapp.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */

	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}
