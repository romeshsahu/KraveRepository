package com.ps.services;

import com.ps.utill.SessionManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceShuttDownBrodcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SessionManager sessionManager = new SessionManager(context);
		Log.d("DeviceShuttDownBrodcastReceiver", "DeviceShuttDownBrodcastReceiver= " + sessionManager.isLogin());

		if (sessionManager.isLogin()) {
			if (ChatService.connection != null) {
				if (ChatService.connection.isConnected()) {
					ChatService.connection.disconnect();
				}
			}
		}

	}

}
