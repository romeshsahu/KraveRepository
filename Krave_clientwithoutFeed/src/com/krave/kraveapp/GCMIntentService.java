package com.krave.kraveapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.JsonObject;

import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.services.PhotoNotificationBrodcastReceiver;
import com.ps.utill.AppConstants;

public class GCMIntentService extends GCMBaseIntentService {
	// public static String BROADCAST_ACTION =
	// "com.unitedcoders.android.broadcasttest.SHOWTOAST";
	private static final String TAG = "GCMIntentService";
	public static String userId = "-1";

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		CommonUtilities.displayMessage(context, "registrationId="
				+ registrationId);
		// Log.d("NAME", MainActivity.name);
		// ServerUtilities.register(context, MainActivity.name,
		// MainActivity.email, registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_unregistered));
		// ServerUtilities.unregister(context, registrationId);

	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		try {
			String message = intent.getExtras().getString("data");
			// Toast.makeText(context, "message" + message,
			// Toast.LENGTH_SHORT).show();
			Log.d(TAG, "Received message " + message);

			CommonUtilities.displayMessage(context, message);
			// notifies user

			/* notification for accept friend request */
			if (message.contains("Your request accepted by")) {
				if (ChatService.APPVISIBLEORNOT) {

					sendBroadcast(context, message.split("#")[1]);
				} else {
					userId = message.split("#")[1];
					generateNotification(context, message);
				}

				/* get friend request */
			} else if (message.contains("You have new Friend request")) {

				// GpsService.notification = true;
				GpsService.notificationCount++;
				if (ChatService.APPVISIBLEORNOT) {
					generateNotification(context, message);
					sendBroadcast();
				} else {
					generateNotification(context, message);

				}

			}

			else if (message.contains("push_hash")) {
				// This message is from Parse, do not handle it.

				// JSONObject json = new JSONObject(message);
				// message = json.getString("alert");
				//
				// if (MyApps.APPVISIBLEORNOT)
				// {
				// generateNotification(context, message);
				// Intent broadcast = new Intent();
				// broadcast.putExtra("come", "announcement");
				// broadcast.putExtra("alert", message);
				// broadcast.setAction(BROADCAST_ACTION);
				// sendBroadcast(broadcast);
				// }
				// else
				// {
				// generateNotification(context, message);
				// }

			} /* photo request notification */
			else if (message.contains("You have an image request")) {

				// GpsService.notification = true;
				// GpsService.notificationCount++;
				if (ChatService.APPVISIBLEORNOT) {
					// generateNotification(context, message);

					sendPhotoRequestBroadcast(message);
				} else {
					generateNotification(context, message);

				}
				/* photo response notification */
			} else if (message.contains("You have an image response")) {

				// GpsService.notification = true;
				// GpsService.notificationCount++;
				if (ChatService.APPVISIBLEORNOT) {
					// generateNotification(context, message);

					sendPhotoRequestBroadcast(message);
				} else {
					generateNotification(context, message);

				}

			} else {
				generateNotification(context, message);
			}
		} catch (Exception e) {

		}
	}

	private void sendBroadcast() {

		Log.d("", "brodacst notification");
		Intent broadcast = new Intent();
		broadcast.putExtra("come", "notification");
		broadcast.setAction(AppConstants.BROADCAST_ACTION);
		sendBroadcast(broadcast);

	}

	private void sendPhotoRequestBroadcast(String message) {

		Log.d("", "sendPhotoRequestBroadcast");
		Intent broadcast = new Intent();
		broadcast.putExtra("come", "photo_request");
		broadcast.putExtra("message", message);
		broadcast.setAction(AppConstants.BROADCAST_ACTION);
		sendBroadcast(broadcast);

	}

	public void sendBroadcast(Context context, String userId) {

		Intent broadcast = new Intent();
		broadcast.putExtra("come", "push_notification");
		broadcast.putExtra("userId", userId);
		broadcast.setAction(AppConstants.BROADCAST_ACTION);
		context.sendBroadcast(broadcast);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		CommonUtilities.displayMessage(context, message);
		// notifies user
		// generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */

	private static void generateNotification(Context context, String message) {

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		JSONObject jsonObject;
		String alert = "";
		try {
			jsonObject = new JSONObject(message);
			alert = jsonObject.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, alert, when);

		String title = context.getString(R.string.app_name);
		Intent notificationIntent = null;
		PendingIntent intent = null;
		if (!message.contains("admin")) {
			notificationIntent = new Intent(context,
					PhotoNotificationBrodcastReceiver.class);
			notificationIntent.putExtra("message", message);
			intent = PendingIntent.getBroadcast(context, 0, notificationIntent,
					PendingIntent.FLAG_ONE_SHOT);
		} else {

			notificationIntent = new Intent(context, Activity_Home.class);
			notificationIntent.putExtra("open", AppConstants.FRAGMENT_HOME);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent = PendingIntent.getActivity(context, 0, notificationIntent,
					PendingIntent.FLAG_ONE_SHOT);
		}

		// notificationIntent.setAction("android.intent.action.MAIN");
		// notificationIntent.addCategory("android.intent.category.LAUNCHER");
		// set intent so it does not start a new activity
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// notificationIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// PendingIntent intent = PendingIntent.getBroadcast(context, 0,
		// notificationIntent, 0);
		//

		// PendingIntent intent = PendingIntent.getBroadcast(context,
		// Integer.valueOf(message.getFrom().split("@")[0]),
		// notificationIntent, PendingIntent.FLAG_ONE_SHOT);
		notification.setLatestEventInfo(context, title, alert, intent);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "your_sound_file_name.mp3");

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		notificationManager.notify(0, notification);

	}
}
