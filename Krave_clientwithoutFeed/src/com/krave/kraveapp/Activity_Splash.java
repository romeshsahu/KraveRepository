package com.krave.kraveapp;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.ps.services.ChatService;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.SessionManager;
import com.ps.utill.Utils;
import com.ps.utill.WebServiceConstants;

public class Activity_Splash extends Activity implements OnClickListener {
	private SessionManager sessionManager;
	private LinearLayout llSignUp;
	private RelativeLayout rl1;
	private ImageView signUp;
	private TextView tvLoginHere, textView1;
	public static boolean isFromLogin = false;
	private boolean isLoginActivate = false;
	// public String gcmRegId;
	private AppManager singleton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash);
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		singleton.mLanguagePrefs = getSharedPreferences(
				AppConstants.LANGUAGE_PREFERENCE, 0);

		Utils.setLocale(Activity_Splash.this, singleton.mLanguagePrefs.getInt(
				AppConstants.LANGUAGE_PREFERENCE, 0));
		registerClient();
		sessionManager = new SessionManager(Activity_Splash.this);

		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		llSignUp = (LinearLayout) rl1.findViewById(R.id.ll_splash_1);
		signUp = (ImageView) rl1.findViewById(R.id.signup);
		tvLoginHere = (TextView) rl1.findViewById(R.id.tvLoginHere);
		textView1 = (TextView) rl1.findViewById(R.id.textView1);
		llSignUp.setVisibility(View.GONE);
		Typeface typeface = FontStyle.getFont(Activity_Splash.this,
				AppConstants.HelveticaNeueLTStd_Md);
		textView1.setTypeface(typeface);
		tvLoginHere.setTypeface(typeface);

		signUp.setOnClickListener(this);
		tvLoginHere.setOnClickListener(this);
		// try {
		// PackageInfo info = getPackageManager().getPackageInfo(
		//
		// // edited on revision 594 com.ps.krave to com.krave.kraveapp dl
		// // "com.ps.krave", PackageManager.GET_SIGNATURES);
		// "com.krave.kraveapp", PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures) {
		// MessageDigest md;
		// md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// String something = new String(Base64.encode(md.digest(), 0));
		// // String something = new
		// // String(Base64.encodeBytes(md.digest()));
		// Log.e("hash key my ....", something);
		// }
		// } catch (PackageManager.NameNotFoundException e1) {
		// Log.e("name not found", e1.toString());
		// } catch (NoSuchAlgorithmException e) {
		// Log.e("no such an algorithm", e.toString());
		// } catch (Exception e) {
		// Log.e("exception", e.toString());
		// }

		// broadcastReceiver = new BroadcastReceiver() {
		//
		// @Override
		// public void onReceive(Context context, Intent intent) {
		// check();
		//
		// }
		// };
		// IntentFilter intentFilter = new IntentFilter();
		// intentFilter.addAction("splash");
		// registerReceiver(broadcastReceiver, intentFilter);
		// check();
		// Intent intent = new Intent(Activity_Splash.this, ChatService.class);
		// startService(intent);

		/*
		 * if (WebServiceConstants.isOnlineWitoutToast(Activity_Splash.this)) {
		 * new GetXmppClient().execute(""); } else {
		 * handler.postDelayed(runnable, 5000); }
		 */

		check();
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		public void run() {

			Toast.makeText(
					Activity_Splash.this,
					R.string.toast_network_is_not_enabled_please_enable_your_device_network_and_continue,
					Toast.LENGTH_SHORT).show();

			finish();

		}
	};

	Handler splash_handler = new Handler();
	Runnable splash_runnable = new Runnable() {

		@Override
		public void run() {
			llSignUp.setVisibility(View.VISIBLE);
		}
	};

	public void check() {

		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 5 seconds
					if (isFromLogin) {
						llSignUp.setVisibility(View.VISIBLE);
						isFromLogin = false;
					} else {
						splash_handler.postDelayed(splash_runnable, 5000);
					}
					// After 5 seconds redirect to another intent
					if (sessionManager.isLogin()) {
						Intent i = new Intent(getBaseContext(),
								Activity_Home.class);
						// String
						// check=getIntent().getExtras().getString("come");
						// if(check!=null){
						// i.putExtra("open", AppConstants.PUSH_NOTIFICATION);
						// i.putExtra("userId",
						// getIntent().getExtras().getString("userId"));
						// }else{
						i.putExtra("open", AppConstants.FRAGMENT_HOME);
						// }
						startActivity(i);
						finish();

					}

				} catch (Exception e) {

				}
			}
		};

		background.start();

	}

	class GetXmppClient extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected JSONArray doInBackground(String... args) {
			Log.i("", "chat service doInBackground");
			xmppClient();
			return null;
			// //

		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {

			super.onPostExecute(jsonArray);

			// if (ChatService.connection != null) {
			// if (!ChatService.connection.isConnected()) {
			// new GetXmppClient().execute("");
			// }
			// }

			// ckStatus();
			// Addcontact();
		}
	}

	public void xmppClient() {

		String host = "198.12.150.189";
		String port = "5222";
		String service = "ip-198-12-150-189.secureserver.net";

		// Create a connection

		ConnectionConfiguration connConfig = new ConnectionConfiguration(host,
				Integer.parseInt(port), service);
		connConfig.setSecurityMode(SecurityMode.disabled);

		ChatService.connection = new XMPPConnection(connConfig);

		try {
			ChatService.connection.connect();
			Log.i("XMPPClient", "[SettingsDialog] Connected to "
					+ ChatService.connection.getHost());
		} catch (XMPPException ex) {
			Log.e("XMPPClient", "[SettingsDialog] Failed to connect to "
					+ ChatService.connection.getHost());
			Log.e("XMPPClient", ex.toString());
			// setConnection(null);

		}

		check();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.signup:
			if (WebServiceConstants.isOnline(this)) {
				Intent intent2 = new Intent(Activity_Splash.this,
						Activity_ProfileDetails.class);
				intent2.putExtra(AppConstants.COME_FROM,
						AppConstants.COME_FROM_NORMAL_REGISTRATION);
				startActivity(intent2);
			} else {
				Toast.makeText(this,
						R.string.toast_please_check_network_connection,
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.tvLoginHere:
			if (sessionManager.isLogin()) {
				Intent i = new Intent(getBaseContext(), Activity_Home.class);
				// String
				// check=getIntent().getExtras().getString("come");
				// if(check!=null){
				// i.putExtra("open", AppConstants.PUSH_NOTIFICATION);
				// i.putExtra("userId",
				// getIntent().getExtras().getString("userId"));
				// }else{
				i.putExtra("open", AppConstants.FRAGMENT_HOME);
				// }

				startActivity(i);
				finish();
			} else {
				// if (!(singleton.gcmRegId.length() == 0)) {
				Intent i = new Intent(getBaseContext(), Activity_Login.class);

				startActivity(i);
				finish();
				// } else {
				// Toast.makeText(Activity_Splash.this, "please wait...",
				// Toast.LENGTH_SHORT).show();
				// }
			}

			break;
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {

			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
	}

	private void registerClient() {
		Log.d("registerClient", "registerClientregisterClientregisterClient");
		try {
			GCMRegistrar.checkDevice(Activity_Splash.this);
			GCMRegistrar.checkManifest(Activity_Splash.this);

			registerReceiver(mHandleMessageReceiver, new IntentFilter(
					CommonUtilities.DISPLAY_MESSAGE_ACTION));

			singleton.gcmRegId = GCMRegistrar
					.getRegistrationId(Activity_Splash.this);
			Log.v("REG ID", singleton.gcmRegId);

			if (singleton.gcmRegId.equals("")) {
				GCMRegistrar.register(Activity_Splash.this,
						CommonUtilities.SENDER_ID);
				Log.v("REG ID IN IF", singleton.gcmRegId);
			} else {
				if (GCMRegistrar.isRegisteredOnServer(Activity_Splash.this)) {
					// regId = GCMRegistrar.getRegistrationId(this);
					System.out.println("GCM Register on server"
							+ singleton.gcmRegId);
				} else {
					GCMRegistrar.setRegisteredOnServer(Activity_Splash.this,
							true);
				}
			}

			// GpsService.gcmId = gcmRegId;
			Log.v("", "Already registered:  " + singleton.gcmRegId);

		} catch (UnsupportedOperationException e) {
			Log.v("JCv", "No GCM");
		}

		// singleton.gcmRegId = gcmRegId;
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(
					CommonUtilities.EXTRA_MESSAGE);
			if (newMessage != null && newMessage.contains("registrationId")) {
				singleton.gcmRegId = newMessage.split("=")[1];
				isLoginActivate = true;
			}
			System.out.println("gcm brodcast receiver actvity splash="
					+ newMessage);
		}

	};

	// public void Addcontact() {
	// try {
	// ChatService.connection.login("68", "68");
	//
	//
	// // ChatService.connection.addPacketListener(new PacketListener() {
	// //
	// // @Override
	// // public void processPacket(Packet arg0) {
	// // // TODO Auto-generated method stub
	// // Log.d("", "packet log"+arg0);
	// // }
	// // }, null);
	//
	//
	// RosterListener rl = new RosterListener() {
	// public void entriesAdded(Collection<String> addresses) {}
	// public void entriesUpdated(Collection<String> addresses) {}
	// public void entriesDeleted(Collection<String> addresses) {}
	// public void presenceChanged(Presence presence) {
	// System.out.println("presence changed!" + presence.getFrom() +
	// " "+presence.getStatus());
	// onPresence(presence);
	// }
	// };
	// if (ChatService.connection.getRoster() != null) {
	// ChatService.connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all);
	//
	// System.out.println("7");
	// ChatService.connection.getRoster().addRosterListener(rl);
	// }
	//
	//
	// Presence presence=new Presence(Presence.Type.available);
	// presence.setMode(Mode.available);
	// presence.setStatus("online");
	// ChatService.connection.sendPacket(presence);
	// } catch (XMPPException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// //Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
	// Roster roster = ChatService.connection.getRoster();
	// try {
	// roster.createEntry("65@ip-198-12-150-189.secureserver.net", "65", null);
	// } catch (XMPPException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// Collection<RosterEntry> entries = roster.getEntries();
	// for (RosterEntry entry : entries) {
	//
	// Log.d("XMPPChatDemoActivity", "--------------------------------------");
	// Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
	// Log.d("XMPPChatDemoActivity", "User: " + entry.getUser());
	// Log.d("XMPPChatDemoActivity", "Name: " + entry.getName());
	// Log.d("XMPPChatDemoActivity", "Status: " + entry.getStatus());
	// Log.d("XMPPChatDemoActivity", "Type: " + entry.getType());
	// Presence entryPresence = roster.getPresence(entry.getUser());
	//
	// Log.d("XMPPChatDemoActivity", "Presence Status: "+
	// entryPresence.getStatus());
	// Log.d("XMPPChatDemoActivity", "Presence Type: " +
	// entryPresence.getType());
	//
	// Presence.Type type = entryPresence.getType();
	// if (type == Presence.Type.available)
	// Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
	// Log.d("XMPPChatDemoActivity", "Presence : " + entryPresence);
	// }
	// }
	// private void onPresence(Presence presence) {
	// String user = presence.getFrom();
	// // if (presence.getType() == Presence.Type.available)
	// // UnityPlayer.UnitySendMessage(eventClass, "Online", user);
	// // else
	// // UnityPlayer.UnitySendMessage(eventClass, "Offline", user);
	//
	// System.out.println("Java: Presence changed, from:" +presence.getFrom() +
	// " type:"+presence.getType() + " toString:"+presence.toString());
	// }
	// public void checkStatus() {
	//
	// final Roster roster = ChatService.connection.getRoster();
	//
	// final Collection<RosterEntry> entries = roster.getEntries();
	// for (final RosterEntry entry : entries) {
	// System.out.println("User: " + entry.getUser());
	// System.out.println("Name: " + entry.getName());
	// // System.out.println("Status: " + entry.getStatus());
	//
	// final Presence p = roster.getPresence(entry.getUser());
	// System.out.println("====== Mode: " + p.getMode());
	// System.out.println("====== Status: " + p.getStatus());
	// }
	// }

}