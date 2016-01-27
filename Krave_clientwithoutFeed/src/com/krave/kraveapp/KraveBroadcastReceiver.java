package com.krave.kraveapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.parse.ParsePushBroadcastReceiver;

public class KraveBroadcastReceiver extends ParsePushBroadcastReceiver {
	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";

	@Override
	public void onReceive(Context context, Intent intent) {

		super.onReceive(context, intent);
	}
	
	@Override
	protected void onPushReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onPushReceive(context, intent);
	}
	
	@Override
	protected void onPushOpen(final Context context, final Intent intent) {
		super.onPushOpen(context, intent);
		
//		Handler handler = new Handler();
//		handler.post(new Runnable() {
//		     public void run() {
//		    	 new AlertDialog.Builder(context)
//		    	    .setTitle("Krave")
//		    	    .setMessage("Push notification")
//		    	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//		    	        public void onClick(DialogInterface dialog, int which) { 
//
//		    	        }
//		    	     })
//		    	    .setIcon(android.R.drawable.ic_dialog_alert)
//		    	     .show();
//		     }
//		});		
		
		Intent broadcast = new Intent();
		broadcast.putExtra("come", "announcement");
		broadcast.setAction(BROADCAST_ACTION);
		context.sendBroadcast(broadcast);
	}
	
}
