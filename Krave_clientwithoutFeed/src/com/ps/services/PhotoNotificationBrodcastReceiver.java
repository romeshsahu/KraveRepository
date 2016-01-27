package com.ps.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.krave.kraveapp.Activity_Block_Dude;
import com.krave.kraveapp.Activity_ChangePassword;
import com.krave.kraveapp.Activity_DudeGallery;
import com.krave.kraveapp.Activity_Friens_Request_Accept;
import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.Activity_Push_Notification;
import com.krave.kraveapp.Activity_R_AddGuys;
import com.krave.kraveapp.Activity_R_CreateNewList;
import com.krave.kraveapp.Activity_R_EditList;
import com.krave.kraveapp.Activity_Report_Dude;
import com.krave.kraveapp.Activity_ShowImage;
import com.krave.kraveapp.FragmentChatMatches;
import com.krave.kraveapp.FragmentHome;
import com.krave.kraveapp.MyApps;
import com.krave.kraveapp.R;
import com.krave.kraveapp.RecentlySearchedHistory;
import com.navdrawer.SimpleSideDrawer;
import com.ps.utill.AppConstants;
import com.ps.utill.SessionManager;

public class PhotoNotificationBrodcastReceiver extends BroadcastReceiver {
	// public static String BROADCAST_ACTION =
	// "com.unitedcoders.android.broadcasttest.SHOWTOAST";

	// edited for revsion 603
	// MAY 19 2015
	// added
	RecentlySearchedHistory RecentlySearchedcity = RecentlySearchedHistory
			.getInstance();
	SimpleSideDrawer slide_me = Activity_Home.slide_me;

	// - - - -

	@Override
	public void onReceive(Context context, Intent intent) {
		SessionManager sessionManager = new SessionManager(context);
		// int notificationType = intent.getExtras().getInt(
		// AppConstants.CHAT_NOTIFICATION_TYPE);
		// String message = intent.getExtras().getString(
		// AppConstants.CHAT_NOTIFICATION_STRING);
		if (Activity_Push_Notification.getActivityObject() != null) {
			Activity_Push_Notification.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}

		if (Activity_Friens_Request_Accept.getActivityObject() != null) {
			Activity_Friens_Request_Accept.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_ChangePassword.getActivityObject() != null) {
			Activity_ChangePassword.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_DudeGallery.getActivityObject() != null) {
			Activity_DudeGallery.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_Report_Dude.getActivityObject() != null) {
			Activity_Report_Dude.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_Block_Dude.getActivityObject() != null) {
			Activity_Block_Dude.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}

		if (Activity_R_CreateNewList.getActivityObject() != null) {
			Activity_R_CreateNewList.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_R_AddGuys.getActivityObject() != null) {
			Activity_R_AddGuys.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_R_EditList.getActivityObject() != null) {
			Activity_R_EditList.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_ShowImage.getActivityObject() != null) {
			Activity_ShowImage.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}

		String message = intent.getExtras().getString("message");
		// String userId = "";
		// try {
		// if (message.contains("You have an image request")) {
		// userId = new JSONObject("message").getString("user_id");
		// } else if (message.contains("You have an image response")) {
		//
		// userId = new JSONObject("message").getString("owner_id");
		//
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// FragmentChatMatches.ChatXmppUserId = userId;
		// FragmentChatMatches.comeFrom = false;
		
			if (ChatService.APPVISIBLEORNOT)// app open
			{

				// if (ChatService.ONCHAT ==
				// AppConstants.FRAGMENT_CHATT_MATCHES) {// on
				// chat
				// screen

				sendPhotoRequestBroadcast(context, message);
				// }

			} else {

				Intent notificationIntent = new Intent(context,
						Activity_Home.class);
				notificationIntent.putExtra("open",
						AppConstants.FRAGMENT_CHATT_MATCHES);
				notificationIntent.putExtra("openAcceptPhotoDailog", "yes");
				notificationIntent.putExtra("message", message);

				if (RecentlySearchedcity.isRightMenuActive == true) {
					slide_me.close();

				}

				notificationIntent
						.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				context.startActivity(notificationIntent);
				if (ChatService.APPACTIVITYSTATE == 1) {
					sendPhotoRequestBroadcast(context, message);
				}
			}
		
	}

	private void sendPhotoRequestBroadcast(Context context, String message) {

		Log.d("", "sendPhotoRequestBroadcast");
		Intent broadcast = new Intent();
		broadcast.putExtra("come", "photo_request");
		broadcast.putExtra("message", message);
		broadcast.setAction(AppConstants.BROADCAST_ACTION);
		context.sendBroadcast(broadcast);

	}

}
