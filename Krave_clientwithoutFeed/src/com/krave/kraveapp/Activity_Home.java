package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.internal.db;
import com.krave.kraveapp.FragmentChatFriend.DeleteChatHistory;
import com.krave.kraveapp.FragmentChatMatches.GetChatHistoryByPagination;
import com.krave.kraveapp.FragmentDetailDudesProfile.SendPhotoRequest;
import com.krave.kraveapp.FragmentImageRequest.CancelPhotoResponse;
import com.navdrawer.SimpleSideDrawer;
import com.ps.adapters.ChatFriendAdapterForRightPanel;
import com.ps.adapters.ChatMatchesAdapter;
import com.ps.adapters.RequistListAdapter;
import com.ps.adapters.SearchByAdapter;
import com.ps.adapters.UpdateUserListAdapter;
import com.ps.adapters.UserListAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AdDTO;
import com.ps.models.ChatDetailsDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.UserUnreadMessageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AdMob;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveDAO;
import com.ps.utill.SessionManager;
import com.ps.utill.Utils;
import com.ps.utill.WebServiceConstants;
import com.tag.trivialdrivesample.util.InAppPurchaseActivity;

public class Activity_Home extends InAppPurchaseActivity implements
		OnClickListener {
	// dl edited on revision 589
	// May 13 2015 4:21 pm
	RecentlySearchedHistory RecentlySearchedcity = RecentlySearchedHistory
			.getInstance();
	public int PastFragment = 1;
	public String RecentSearchedCity = "";

	private int fragmentIds = 0;
	// private String gcmRegId = "";
	public int COME_FROM = 0;
	public int setLeftDrawer = 0;
	public boolean isOpenCongratulationLayout = false;
	public static int index;
	public static int findDudesScrollIndex = 0;
	public static int findDudesScrollPosition = 0;
	public static String gblUserID;
	public static List<UserDTO> dudeCommonList;// = new ArrayList<UserDTO>();
	public static List<UserUnreadMessageDTO> dudeChatUnreadMessage = new ArrayList<UserUnreadMessageDTO>();
	public static ImageButton left_button, right_button, back_button;
	private RelativeLayout notificationHeader;
	private RelativeLayout notificationRequiest;
	private TextView notificationHeaderTextView, notificationRequiestTextView,
			notificationTv;
	public static SessionManager sessionManager;
	public TextView title;
	public ImageView headerIcon;
	public static SimpleSideDrawer slide_me;
	private static FragmentManager fragmentManager;
	// private MyApps xmppClient = new MyApps();
	// private LinearLayout layoutHome;
	private LinearLayout layoutProfile;
	public LinearLayout layoutFindDudes;
	private LinearLayout layoutSearchCity;
	// private LinearLayout layoutGetMatches;
	private LinearLayout layoutDailyKrave;
	private LinearLayout layoutNightlifeFinder;
	private LinearLayout layoutChatMatches;
	private LinearLayout layoutSetting;
	private LinearLayout layoutImageRequest;
	private LinearLayout layoutContactUs;
	// private LinearLayout layoutMap;
	private LinearLayout layoutLogout;

	private ImageView loadingView;
	private LinearLayout llLoading;
	private FrameLayout adViewFrameLayout;
	// private boolean LoadHomeFlag = false;
	int selectedItem = 1;
	int previousItem = 1;
	static Fragment tempFragment = null;
	public static ChatFriendAdapterForRightPanel chatMatchesAdapter;
	// static FragmentHome fragmentHome;
	// private Presence presence;
	// public XMPPConnection connection;
	// static Fragment fragmentProfile;
	// static Fragment fragmentFindDudes;
	// static Fragment fragmentGetMatches;
	// static Fragment fragmentDailyKrave;
	// static Fragment fragmentChatMatches;
	// static Fragment fragmentSetting;
	// static Fragment fragmentMap;

	// private final int PROFILE = 2;
	// private final int FIND_DUDES = 3;
	// private final int GET_MATCHES = 4;

	// private final int SETTING = 7;

	public static ListView dudeListView;
	private EditText searchDude;
	public LinearLayout mainview;

	public LinearLayout ll_act_home_bar;

	/* right menu variable */
	// public List<UserDTO> searchDudeList = new ArrayList<UserDTO>();
	public List<UserDTO> requestDudesList = new ArrayList<UserDTO>();
	List<UserListDTO> userListDTOs = new ArrayList<UserListDTO>();

	List<UserDTO> selectedUserDTOs = new ArrayList<UserDTO>(); // editList
	UpdateUserListAdapter updateUserListAdapter;

	UserListAdapter userListAdapter;
	SearchByAdapter subListAdapter;
	RequistListAdapter requistListAdapter;
	ImageLoader imageLoader;
	BroadcastReceiver broadcastReceiver, closeActivtiyBroadcastReceiver;

	private Boolean isEditList = false;
	private TextView editAllGuysButton;
	private CharSequence storeSearch;
	private KraveDAO kraveDAO;
	private AppManager singleton;

	// static Activity_Home INSTANCE = null;
	// public boolean showReviewDialog = true;

	/* add mob variabels */
	private AdMob adMob;
	public List<AdDTO> adDTOs = new ArrayList<AdDTO>(); // admin custom adds
														// list
	private boolean isAdsLoaded = false; // check whether web service is
											// succefully get ads data or not

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// INSTANCE = this;
		// configure Flurry
		FlurryAgent.setLogEnabled(true);

		// init Flurry
		FlurryAgent.init(this, "T2CZ7BTTPPJR8W42MQPH"); // edited revision 582
														// commented

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		kraveDAO = new KraveDAO(Activity_Home.this);
		/* av singleton */
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		singleton.mTransitPrefs = getSharedPreferences(AppConstants.GPS_PREFS,
				0);
		singleton.mLanguagePrefs = getSharedPreferences(
				AppConstants.LANGUAGE_PREFERENCE, 0);
		// singleton.mFinDudesColumnPrefs = getSharedPreferences(
		// AppConstants.FIND_DUDES_COLUMN_PREFERENCE, 1);

		singleton.mFilterPrefs = getSharedPreferences(
				AppConstants.FILTER_PREFS, 0);

		imageLoader = new ImageLoader(Activity_Home.this);
		mainview = (LinearLayout) findViewById(R.id.mainview);
		ChatService.APPACTIVITYSTATE = 0;
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		// initialize session manager
		sessionManager = new SessionManager(Activity_Home.this);
		// for review dialog counter
		sessionManager.setReviewCounter();
		singleton.key_mPrefs = getSharedPreferences("KEY_"
				+ sessionManager.getUserDetail().getUserID(), 0);
		String key = singleton.key_mPrefs.getString("KEY_"
				+ sessionManager.getUserDetail().getUserID(), "");
		System.out.println("KEY " + key);

		singleton.val_mPrefs = getSharedPreferences("VAL_"
				+ sessionManager.getUserDetail().getUserID(), 0);
		String value = singleton.val_mPrefs.getString("VAL_"
				+ sessionManager.getUserDetail().getUserID(), "");
		System.out.println("VALUE " + value);

		String[] keys = key.split(",");
		String[] values = value.split(",");

		singleton.getChatCount.clear();
		for (int x = 1; x < keys.length; x++) {
			singleton.getChatCount.put(keys[x], Integer.valueOf(values[x]));
		}

		fragmentManager = getFragmentManager();

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.left_menu);
		slide_me.setRightBehindContentView(R.layout.right_menu);
		// Toast.makeText(Activity_Home.this,
		// "sin.gcmRegId=" + singleton.gcmRegId, Toast.LENGTH_SHORT)
		// .show();
		setLayout();
		setTypeFace();
		setListeners();
		// registerClient();
		// adMob = new AdMob(adViewFrameLayout, this, singleton); // admob class
		// object
		startUserCurrentLocationService();

		if (FragmentHome.userDTOs != null) {
			FragmentHome.setFirstLoginValues();
		}

		int intentValue = getIntent().getExtras().getInt("open",
				AppConstants.FRAGMENT_HOME);
		if (intentValue == AppConstants.FRAGMENT_HOME) {

			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			// if (!GCMIntentService.userId.equals("-1")) {
			// new GetDudeById().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.GET_DUDE_BY_ID,
			// GCMIntentService.userId);
			// }
		} else if (intentValue == AppConstants.FRAGMENT_SETTING_NEW) {
			selectedItem = 7;

			FragmentSetting.comeFrom = false;
			Attach_Fragment(AppConstants.FRAGMENT_SETTING_NEW);

		} else {

			String openDailog = getIntent().getExtras().getString(
					"openAcceptPhotoDailog", "no");
			if ("yes".equals(openDailog)) {
				openDailogForAcceptPhotoRequest((String) getIntent()
						.getExtras().get("message"));
			}
			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			// edited for revision 603
			// MAY 19 2015
			slide_me.close();
			// - - - - - - -
		}
		broadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// Toast.makeText(context, "SMS SENT!!", Toast.LENGTH_SHORT)
				// .show();
				if ("chat".equals(intent.getExtras().get("come"))) {
					// edited on revision 603
					// MAY 19 2015 commented
					Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
					slide_me.close();
					// - - - - - -
				} else if ("push_notification".equals(intent.getExtras().get(
						"come"))) {
					String userId = intent.getExtras().getString("userId");

					if (WebServiceConstants.isOnline(Activity_Home.this)) {
						new GetDudeById().execute(WebServiceConstants.BASE_URL
								+ WebServiceConstants.GET_DUDE_BY_ID, userId);
					} else {
						Toast.makeText(
								Activity_Home.this,
								R.string.toast_please_check_internet_connection,
								Toast.LENGTH_LONG).show();
					}
				} else if ("announcement"
						.equals(intent.getExtras().get("come"))) {
					new AlertDialog.Builder(context)
							.setTitle("Annoucement")
							.setMessage(
									(String) intent.getExtras().get("alert"))
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();

				} else if ("photo_request".equals(intent.getExtras()
						.get("come"))) {

					openDailogForAcceptPhotoRequest((String) intent.getExtras()
							.get("message"));
					Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
					slide_me.close();
					// - - - - - -
				} else if ("in_app_purchase".equals(intent.getExtras().get(
						"come"))) {
					subcriptionActive();
				} else {
					setNotification();
				}

				// Edited on revision 595
				// MAY 15 2015 2:20 PM
				// dl commented
				// added first if statement

				// editated by romesh
				// if (RecentlySearchedcity.isConvoHistoryLoaded == false) {
				// if (WebServiceConstants.isOnline(Activity_Home.this)) {
				// Log.d("GET_HISTORY", "onCreate");
				// new GetChatHistory()
				// .execute(WebServiceConstants.BASE_URL
				// + WebServiceConstants.GET_HISTORY);
				//
				// // Edited for revision 608
				// // MAY 20 2015
				// // added
				// RecentlySearchedcity.isConvoHistoryLoaded = true;
				// // - - - -
				// }
				// // settingAllGuysLayout();
				// }
				// editated by romesh
			}
		};
		IntentFilter filterSend = new IntentFilter();
		filterSend.addAction(AppConstants.BROADCAST_ACTION);
		registerReceiver(broadcastReceiver, filterSend);

		closeActivtiyBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};

		IntentFilter closeActivity = new IntentFilter();
		closeActivity.addAction("close");
		registerReceiver(closeActivtiyBroadcastReceiver, closeActivity);

		setNotification();
		// subcriptionActive();

		// try {
		// PackageInfo info = getPackageManager().getPackageInfo(
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

		// Edited on revision 608
		// MAY 15 2015 2:20 PM
		// added a copy
		// May 12,15 edited on revision 85
		// editated by romesh
		// if (RecentlySearchedcity.isGetFriendsLoaded == false) {
		// try {
		// if (WebServiceConstants.isOnline(Activity_Home.this)) {
		// new GetChatFriends()
		// .execute(WebServiceConstants.AV_CHAT_RIGHT_NAV);
		// RecentlySearchedcity.isGetFriendsLoaded = true;
		// Log.d("dl", "GetChatFriends() api is running");
		// } else {
		// Toast.makeText(Activity_Home.this,
		// "Please Check Network Connection",
		// Toast.LENGTH_LONG).show();
		// }
		// } catch (Exception e) {
		// Log.d("dl", e.toString());
		// }
		// }
		// editated by romesh
		// - -- - -- -- - - - -

		// edited for revision 608
		// MAY 20 2015
		// added a copy
		// if (RecentlySearchedcity.isConvoHistoryLoaded == false) {
		// if (getIntent().getBooleanExtra("loadchatHistory", false)) {
		// if (WebServiceConstants.isOnline(Activity_Home.this)) {
		// Log.d("GET_HISTORY", "onCreate");
		// new GetChatHistory().execute(WebServiceConstants.BASE_URL
		// + WebServiceConstants.GET_HISTORY);
		//
		// // Edited for revision 608
		// // MAY 20 2015
		// // added
		// RecentlySearchedcity.isConvoHistoryLoaded = true;
		// // - - - -
		//
		// }
		// } else {
		// settingAllGuysLayout();
		// }
		// }

		// - - - - - - - - --

	}

	protected void subcriptionActive() {

		if (!singleton.isPaidUser) {
			new GetAds().execute(WebServiceConstants.GET_ADS);
		} else {
			adViewFrameLayout.setVisibility(View.GONE);
			if (adMob != null)
				adMob.adViewDestroy();
			// refresh dudes detaill prfofile screen when payment is succesful
			if (ChatService.ONCHAT == AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE) {
				((FragmentDetailDudesProfile) tempFragment).refreshGridView();
			} else if (ChatService.ONCHAT == AppConstants.FRAGMENT_HOME) {
				((FragmentHome) tempFragment).refreshGridView();
			}
		}

	}

	private void startUserCurrentLocationService() {
		Intent intent = new Intent(Activity_Home.this, GpsService.class);
		startService(intent);
		Intent intent1 = new Intent(Activity_Home.this, ChatService.class);
		startService(intent1);

	}

	// private void registerClient() {
	//
	// try {
	// GCMRegistrar.checkDevice(Activity_Home.this);
	// GCMRegistrar.checkManifest(Activity_Home.this);
	//
	// registerReceiver(mHandleMessageReceiver, new IntentFilter(
	// CommonUtilities.DISPLAY_MESSAGE_ACTION));
	//
	// singleton.gcmRegId = GCMRegistrar
	// .getRegistrationId(Activity_Home.this);
	// Log.v("REG ID", singleton.gcmRegId);
	//
	// if (singleton.gcmRegId.equals("")) {
	// GCMRegistrar.register(Activity_Home.this,
	// CommonUtilities.SENDER_ID);
	// Log.v("REG ID IN IF", singleton.gcmRegId);
	// } else {
	// if (GCMRegistrar.isRegisteredOnServer(Activity_Home.this)) {
	// // regId = GCMRegistrar.getRegistrationId(this);
	// System.out.println("GCM Register on server"
	// + singleton.gcmRegId);
	// } else {
	// GCMRegistrar
	// .setRegisteredOnServer(Activity_Home.this, true);
	// }
	// }
	//
	// // GpsService.gcmId = MyApps.gcmRegId;
	// Log.v("", "Already registered:  " + singleton.gcmRegId);
	//
	// } catch (UnsupportedOperationException e) {
	// Log.v("JCv", "No GCM");
	// }
	//
	// }

	// private final BroadcastReceiver mHandleMessageReceiver = new
	// BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String newMessage = intent.getExtras().getString(
	// CommonUtilities.EXTRA_MESSAGE);
	// System.out.println("FUCK THIS SHIT");
	// }
	//
	// };

	public void easyTrackerEventLog(String Categ, String Action, String Label) {
		EasyTracker.getInstance(this).set(Fields.EVENT_CATEGORY, Categ);
		EasyTracker.getInstance(this).set(Fields.EVENT_ACTION, Action);
		EasyTracker.getInstance(this).set(Fields.EVENT_LABEL, Label);
		EasyTracker.getInstance(this).send(MapBuilder.createAppView().build());
	}

	private void showReleaseNotes() {
		final Dialog dialog = new Dialog(this,
				R.style.TransparentProgressDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog_release_notes);

		WebView webView = (WebView) dialog
				.findViewById(R.id.dialog_webview_release_note);

		webView.getSettings().setJavaScriptEnabled(true);

		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("http://www.kraveapp.net/release-notes/");

		dialog.show();
	}

	@Override
	protected void onResume() {
		super.onResume();

		PackageInfo packageInfo;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);

			int currentVersion = packageInfo.versionCode;

			Context context = this.getApplicationContext();
			SharedPreferences appPreferences = context.getSharedPreferences(
					AppConstants.APP_PREFS, Context.MODE_PRIVATE);

			int installedVersion = appPreferences.getInt("currentVersion", 0);

			if (installedVersion != currentVersion) {
				SharedPreferences.Editor editor = appPreferences.edit();

				editor.putInt("currentVersion", currentVersion);
				editor.commit();

				// TODO: SHOW RELEASE NOTES
				// showReleaseNotes();
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// MyApps.activityResumed();

		ChatService.APPVISIBLEORNOT = true;
		if (ChatService.isApplicationBaground) {
			// edited on revision 603
			// MAY 19 2015 commented

			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			slide_me.close();
			// - - - - - - - - -
			ChatService.APPACTIVITYSTATE = 0;
			ChatService.isApplicationBaground = false;
		}
		if (!GCMIntentService.userId.equals("-1")) {
			if (WebServiceConstants.isOnline(Activity_Home.this)) {
				new GetDudeById().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_DUDE_BY_ID,
						GCMIntentService.userId);
			} else {
				Toast.makeText(Activity_Home.this,
						R.string.toast_please_check_network_connection,
						Toast.LENGTH_LONG).show();
			}
		}
		// if (isRightMenuList) {
		// new GetUserList().execute(WebServiceConstants.BASE_URL
		// + WebServiceConstants.GET_USER_LIST);
		// isRightMenuList = false;
		// }

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ChatService.APPVISIBLEORNOT = false;
		ChatService.APPACTIVITYSTATE = 1;
		// MyApps.activityPaused();
		// unregisterReceiver(broadcastReceiver);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		FlurryAgent.onStartSession(this, "T2CZ7BTTPPJR8W42MQPH"); // Edited
																	// revision
																	// 582
																	// commented

		EasyTracker.getInstance(this).activityStart(this);
		EasyTracker.getInstance(this).set(Fields.SCREEN_NAME, "Home Screen");
		EasyTracker.getInstance(this).send(MapBuilder.createAppView().build());
	}

	@Override
	public void onStop() {
		super.onStop();

		FlurryAgent.onEndSession(this); // Edited revision 582 commented

		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ChatService.APPACTIVITYSTATE = 0;

		super.onDestroy();
		// try {
		// unregisterReceiver(mHandleMessageReceiver);
		// GCMRegistrar.onDestroy(this);
		// } catch (Exception e) {
		//
		// Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		// }
	}

	private void stopUserCurrentLocationService() {
		Intent intent = new Intent(Activity_Home.this, GpsService.class);
		stopService(intent);
		Intent intent1 = new Intent(Activity_Home.this, ChatService.class);
		stopService(intent1);

	}

	private void setListeners() {
		left_button.setOnClickListener(this);
		right_button.setOnClickListener(this);
		// back_button.setOnClickListener(this);
		// layoutHome.setOnClickListener(this);
		layoutProfile.setOnClickListener(this);
		layoutFindDudes.setOnClickListener(this);
		layoutSearchCity.setOnClickListener(this);
		// layoutGetMatches.setOnClickListener(this);
		layoutDailyKrave.setOnClickListener(this);
		layoutNightlifeFinder.setOnClickListener(this);
		layoutChatMatches.setOnClickListener(this);
		layoutSetting.setOnClickListener(this);
		layoutImageRequest.setOnClickListener(this);
		layoutContactUs.setOnClickListener(this);
		// layoutMap.setOnClickListener(this);
		layoutLogout.setOnClickListener(this);
		// dudeListView.setOnItemClickListener(this);
	}

	FrameLayout rightContainer;

	private void setLayout() {
		// View leftView = slide_me.getLeftBehindView();
		notificationHeader = (RelativeLayout) findViewById(R.id.notofication);
		// notificationRequiest = (ImageView) findViewById(R.id.notofication);
		notificationTv = (TextView) findViewById(R.id.notoficationTv);
		notificationHeaderTextView = (TextView) findViewById(R.id.notificationCount);
		// notificationRequiestTextView = (TextView)
		// findViewById(R.id.notificationRCount);
		left_button = (ImageButton) findViewById(R.id.left_buton);
		right_button = (ImageButton) findViewById(R.id.right_buton);
		back_button = (ImageButton) findViewById(R.id.back_button);
		// layoutHome = (LinearLayout) findViewById(R.id.home);
		headerIcon = (ImageView) findViewById(R.id.icon);
		title = (TextView) findViewById(R.id.titleTextView);
		layoutProfile = (LinearLayout) findViewById(R.id.profile);
		layoutFindDudes = (LinearLayout) findViewById(R.id.findDudes);
		layoutSearchCity = (LinearLayout) findViewById(R.id.searchCity);
		// layoutGetMatches = (LinearLayout) findViewById(R.id.getMatches);
		layoutDailyKrave = (LinearLayout) findViewById(R.id.dailyKrave);
		layoutNightlifeFinder = (LinearLayout) findViewById(R.id.nightlife_finder);
		layoutChatMatches = (LinearLayout) findViewById(R.id.chatMatches);
		layoutSetting = (LinearLayout) findViewById(R.id.setting);
		layoutImageRequest = (LinearLayout) findViewById(R.id.imageRequest);
		layoutContactUs = (LinearLayout) findViewById(R.id.contactUs);
		// layoutMap = (LinearLayout) findViewById(R.id.map);
		layoutLogout = (LinearLayout) findViewById(R.id.logout);

		ll_act_home_bar = (LinearLayout) findViewById(R.id.ll_act_home_bar);

		rightContainer = (FrameLayout) findViewById(R.id.rightContainer);
		adViewFrameLayout = (FrameLayout) findViewById(R.id.ad);
		settingAllGuysLayout();
	}

	private void setTypeFace() {
		TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11, tvVersionName;
		// textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		textView5 = (TextView) findViewById(R.id.textView5);
		textView6 = (TextView) findViewById(R.id.textView6);
		textView7 = (TextView) findViewById(R.id.textView7);
		textView8 = (TextView) findViewById(R.id.textView8);
		textView9 = (TextView) findViewById(R.id.textView9);
		textView10 = (TextView) findViewById(R.id.textView10);
		textView11 = (TextView) findViewById(R.id.textView11);

		tvVersionName = (TextView) findViewById(R.id.tvVersionName);
		try {
			tvVersionName
					.setText("v"
							+ getPackageManager().getPackageInfo(
									getPackageName(), 0).versionName
							+ " b"
							+ getPackageManager().getPackageInfo(
									getPackageName(), 0).versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		// textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		textView3.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		textView7.setTypeface(typeface);
		textView8.setTypeface(typeface);
		textView9.setTypeface(typeface);
		textView10.setTypeface(typeface);
		textView11.setTypeface(typeface);
		tvVersionName.setTypeface(typeface);
	}

	private void setNotification() {

		int totalValues = 0;
		try {
			Iterator it = singleton.getChatCount.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				totalValues += Integer.valueOf("" + pairs.getValue());
			}
		} catch (Exception e) {
		}

		try {
			String key = "";
			String values = "";

			Iterator it = singleton.getChatCount.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				key = key + "," + pairs.getKey();
				values = values + "," + pairs.getValue();
			}

			singleton.key_mPrefs = getSharedPreferences("KEY_"
					+ sessionManager.getUserDetail().getUserID(), 0);
			Editor editor = singleton.key_mPrefs.edit();
			editor.putString("KEY_"
					+ sessionManager.getUserDetail().getUserID(), key);
			editor.commit();

			singleton.val_mPrefs = getSharedPreferences("VAL_"
					+ sessionManager.getUserDetail().getUserID(), 0);
			Editor editor1 = singleton.val_mPrefs.edit();
			editor1.putString("VAL_"
					+ sessionManager.getUserDetail().getUserID(), values);
			editor1.commit();
			System.out.println("key " + key + " value " + values);
		} catch (Exception e) {
		}

		// int total = GpsService.unreadMsgCount;
		int total = totalValues;// GpsService.unreadMsgCount;//GpsService.notificationCount
								// + GpsService.unreadMsgCount;
		if (total > 0) {

			// edited for revision 615
			// MAY 20 2015
			// added updater
			RecentlySearchedcity.isConvoHistoryLoaded = false;
			RecentlySearchedcity.isGetFriendsLoaded = false;

			// Edited on revision 608
			// MAY 15 2015 2:20 PM
			// added a copy
			// May 12,15 edited on revision 85
			// editated by romesh
			// if (RecentlySearchedcity.isGetFriendsLoaded == false) {
			// try {
			// if (WebServiceConstants.isOnline(Activity_Home.this)) {
			// new GetChatFriends()
			// .execute(WebServiceConstants.AV_CHAT_RIGHT_NAV);
			// RecentlySearchedcity.isGetFriendsLoaded = true;
			// Log.d("dl", "GetChatFriends() api is running");
			// } else {
			// Toast.makeText(Activity_Home.this,
			// "Please Check Network Connection",
			// Toast.LENGTH_LONG).show();
			// }
			// } catch (Exception e) {
			// Log.d("dl", e.toString());
			// }
			// }
			// editated by romesh
			// - -- - -- -- - - - -

			// edited for revision 608
			// MAY 20 2015
			// added a copy
			// if (RecentlySearchedcity.isConvoHistoryLoaded == false) {
			// if (WebServiceConstants.isOnline(Activity_Home.this)) {
			// Log.d("GET_HISTORY", "onCreate");
			// new GetChatHistory().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.GET_HISTORY);
			//
			// // Edited for revision 608
			// // MAY 20 2015
			// // added
			// RecentlySearchedcity.isConvoHistoryLoaded = true;
			// // - - - -
			//
			// }
			// }
			// editated by romesh
			// - - - - - - - - --

			notificationHeader.setVisibility(View.VISIBLE);
			notificationHeaderTextView.setVisibility(View.VISIBLE);
			notificationHeaderTextView.setText("" + total);
			Log.d("", "total=" + total);
			notificationTv.setText("" + total);
			if (GpsService.notificationCount > 0) {
				notificationRequiest.setVisibility(View.VISIBLE);

				notificationRequiestTextView.setVisibility(View.VISIBLE);

				notificationRequiestTextView.setText(""
						+ GpsService.notificationCount);
			}
		} else {
			notificationHeader.setVisibility(View.INVISIBLE);
			notificationRequiest.setVisibility(View.INVISIBLE);
			notificationHeaderTextView.setVisibility(View.INVISIBLE);
			notificationRequiestTextView.setVisibility(View.INVISIBLE);
		}

		notificationHeaderTextView.setVisibility(View.GONE);
		notificationRequiestTextView.setVisibility(View.GONE);
		chatMatchesAdapter.notifyDataSetChanged();
		 Utils.sendBadgeCountBrodcast(total, getApplicationContext());
		//BadgeCountService.setBadge(getApplicationContext(), "" + total);
	}

	private void settingAllGuysLayout() {
		final TextView deleteButton;
		FrameLayout requistsLayout;

		// - -- - -- -- - - - -
		LayoutInflater inflater = getLayoutInflater();
		rightContainer.removeAllViews();
		View view = inflater.inflate(R.layout.right_menu_all_guys,
				rightContainer);
		notificationRequiest = (RelativeLayout) view
				.findViewById(R.id.requestsNotification);
		notificationRequiestTextView = (TextView) findViewById(R.id.notificationRCount);
		editAllGuysButton = (TextView) view
				.findViewById(R.id.editAllGuysButton);
		deleteButton = (TextView) view.findViewById(R.id.listsButton);
		TextView allGuys = (TextView) view.findViewById(R.id.allGuys);
		TextView requestTextView = (TextView) view
				.findViewById(R.id.requestTextView);

		dudeListView = (ListView) view.findViewById(R.id.dudeList);
		requistsLayout = (FrameLayout) view.findViewById(R.id.reuistsLayout);
		searchDude = (EditText) view.findViewById(R.id.searchDude);

		llLoading = (LinearLayout) view.findViewById(R.id.llLoading);
		loadingView = (ImageView) view.findViewById(R.id.loadingView);

		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		editAllGuysButton.setTypeface(typeface);
		deleteButton.setTypeface(typeface);
		allGuys.setTypeface(typeface);
		requestTextView.setTypeface(typeface);
		searchDude.setTypeface(typeface);

		// searchDudeList = new ArrayList<UserDTO>(); // edit on 15/07/2015

		// Edited for revision 608
		// MAY 20 2015
		// added
		chatMatchesAdapter = new ChatFriendAdapterForRightPanel(
				Activity_Home.this, RecentlySearchedcity.searchDudeList, 3);

		// commented
		// chatMatchesAdapter = new ChatMatchesAdapter(Activity_Home.this,
		// searchDudeList, 3);

		dudeListView.setAdapter(chatMatchesAdapter);

		if (WebServiceConstants.isOnline(Activity_Home.this)) {
			// Log.d("GET_HISTORY", "settingAllGuys");
			// new GetChatHistory().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.GET_HISTORY);
		}
		// May 12,15 edited on revision 85
		Log.d("dl", "running");
		try {

			// Edited on revision 595
			// MAY 15 2015 2:20 PM
			// added if statement only

			if (RecentlySearchedcity.isGetFriendsLoaded == false) {
				if (WebServiceConstants.isOnline(Activity_Home.this)) {
					new GetChatFriends()
							.execute(WebServiceConstants.AV_CHAT_RIGHT_NAV);
					RecentlySearchedcity.isGetFriendsLoaded = true;

					Log.d("dl", "GetChatFriends() api is running");
				} else {
					Toast.makeText(Activity_Home.this,
							R.string.toast_please_check_network_connection,
							Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			Log.d("dl", e.toString());
		}
		searchDude.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// chatMatchesAdapter.getFilter().filter(s.toString());
				storeSearch = s; // -pat
				if (isEditList)
					updateUserListAdapter.getFilter().filter(s.toString());
				else
					chatMatchesAdapter.getFilter().filter(
							s.toString() + "/" + AppConstants.OFFLINE);
			}
		});

		dudeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (isEditList) {
					UserDTO userDTO = (UserDTO) updateUserListAdapter
							.getItem(arg2);

					System.out.println(userDTO.getUserID()); // get user ID
					if (userDTO.isSelected()) {
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					} else {

						selectedUserDTOs.add(userDTO);
						userDTO.setSelected(true);
					}
					updateUserListAdapter.notifyDataSetChanged();
				} else {
					easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_RIGHT_NAV,
							AppConstants.EVENT_LOG_ACTION_BTN, "GuySelected");
					FragmentHome.clearData = true;
					FragmentChatMatches.userDTO = (UserDTO) chatMatchesAdapter
							.getItem(arg2);

					// Edited for revision 614
					// MAY 20 2015
					// added
					RecentlySearchedcity.isConvoHistoryLoaded = false;
					RecentlySearchedcity.isGetFriendsLoaded = false;
					// - -- - - - - -

					Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
					slide_me.close();
				}

			}
		});

		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isEditList) {
					if (WebServiceConstants.isOnline(Activity_Home.this)) {
						easyTrackerEventLog(
								AppConstants.EVENT_LOG_CATEG_RIGHT_NAV,
								AppConstants.EVENT_LOG_ACTION_BTN, "Delete");
						new deleteSelectedDude()
								.execute(WebServiceConstants.AV_DELETE_SELECTED_FRIEND);
					}

				} else {
					openDailogForDeleteChatHistory();
				}

			}

		});
		editAllGuysButton.setOnClickListener(new OnClickListener() { // edit and
					// cancel
					// button

					@Override
					public void onClick(View v) {
						selectedUserDTOs = new ArrayList<UserDTO>(); // reset
																		// dis
						if (isEditList) {
							easyTrackerEventLog(
									AppConstants.EVENT_LOG_CATEG_RIGHT_NAV,
									AppConstants.EVENT_LOG_ACTION_BTN, "Cancel");
							isEditList = false;

							editAllGuysButton.setText(R.string.rightpanel_edit);
							deleteButton.setText(R.string.rightpanel_clear_all);
							// deleteButton.setVisibility(View.INVISIBLE);

							// edited for revision 608
							// MAY 20 2015
							// commented
							// chatMatchesAdapter = new
							// ChatMatchesAdapter(Activity_Home.this,
							// searchDudeList, 3);
							// added
							chatMatchesAdapter = new ChatFriendAdapterForRightPanel(
									Activity_Home.this,
									RecentlySearchedcity.searchDudeList, 3);
							// - - --- --

							dudeListView.setAdapter(chatMatchesAdapter);

							try { /* still filter search results on cancel clicked */
								chatMatchesAdapter.getFilter().filter(
										storeSearch.toString() + "/"
												+ AppConstants.OFFLINE);
							} catch (Exception e) {
							}
						} else {
							easyTrackerEventLog(
									AppConstants.EVENT_LOG_CATEG_RIGHT_NAV,
									AppConstants.EVENT_LOG_ACTION_BTN, "Edit");
							isEditList = true;
							editAllGuysButton
									.setText(R.string.rightpanel_cancel);
							deleteButton.setText(R.string.rightpanel_delete);
							// deleteButton.setVisibility(View.VISIBLE);

							// edited for revision 608
							// MAY 20 2015
							// commented
							// updateUserListAdapter = new
							// UpdateUserListAdapter(
							// Activity_Home.this, searchDudeList, -1);
							// added
							updateUserListAdapter = new UpdateUserListAdapter(
									Activity_Home.this,
									RecentlySearchedcity.searchDudeList, -1);

							dudeListView.setAdapter(updateUserListAdapter);

							try { /* still filter search results on edit clicked */
								updateUserListAdapter.getFilter().filter(
										storeSearch.toString());
							} catch (Exception e) {
							}
						}

						/* old but gold */
						// Activity_R_EditList.userDTOs = searchDudeList;
						// Intent intent2 = new Intent(Activity_Home.this,
						// Activity_R_EditList.class);
						//
						// intent2.putExtra(AppConstants.COME_FROM,
						// AppConstants.COME_FROM_ALL_GUYS);
						// startActivityForResult(intent2,
						// AppConstants.EDIT_LIST);

					}
				});

		// requistsLayout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// setRequistsLayout();
		// }
		// });
		setNotification();

		/* init loading */
		// edited for revision 608
		// MAY 20 2015
		// commented
		// singleton.progressLoading(loadingView, llLoading);
	}

	// private void setRequistsLayout() {
	// ImageView backButton;
	// EditText searchDudesEditText;
	// LinearLayout backLayout;
	// ListView requestList;
	//
	// LayoutInflater inflater = getLayoutInflater();
	// rightContainer.removeAllViews();
	// View view1 = inflater.inflate(R.layout.right_menu_requst,
	// rightContainer);
	//
	// backButton = (ImageView) view1.findViewById(R.id.backButton);
	// searchDudesEditText = (EditText) view1.findViewById(R.id.searchDude);
	// requestList = (ListView) view1.findViewById(R.id.dudelist);
	//
	// backLayout = (LinearLayout) view1.findViewById(R.id.backLayout);
	//
	// TextView requestTextView = (TextView) view1
	// .findViewById(R.id.requestTextView);
	// TextView listTextView = (TextView) view1
	// .findViewById(R.id.listTextView);
	//
	// Typeface typeface = FontStyle.getFont(Activity_Home.this,
	// AppConstants.HelveticaNeueLTStd_Roman);
	// requestTextView.setTypeface(typeface);
	// listTextView.setTypeface(typeface);
	// searchDudesEditText.setTypeface(typeface);
	//
	// requistListAdapter = new RequistListAdapter(Activity_Home.this,
	// requestDudesList);
	// requestList.setAdapter(requistListAdapter);
	// if (WebServiceConstants.isOnline(Activity_Home.this)) {
	// new GetDudeRequistsList().execute(WebServiceConstants.BASE_URL
	// + WebServiceConstants.GET_ALL_REQUEST);
	// }
	//
	// searchDudesEditText.addTextChangedListener(new TextWatcher() {
	//
	// public void afterTextChanged(Editable s) {
	// }
	//
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// }
	//
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// requistListAdapter.getFilter().filter(s.toString());
	// }
	// });
	// requestList.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1,
	// int position, long arg3) {
	// // setListsItemLayout(position);
	//
	// }
	// });
	//
	// backButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// settingAllGuysLayout();
	//
	// }
	// });
	// backLayout.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// settingAllGuysLayout();
	//
	// }
	// });
	//
	// }

	// private Roster roster;

	// private void setListsLayout() {
	// ImageView backFromListImageButton;
	// ListView nameList;
	//
	// LinearLayout createNewListLayout, editListName;
	//
	// EditText searchNameListEditText;
	//
	// LayoutInflater inflater = getLayoutInflater();
	// rightContainer.removeAllViews();
	// View view1 = inflater
	// .inflate(R.layout.right_menu_lists, rightContainer);
	//
	// backFromListImageButton = (ImageView) view1
	// .findViewById(R.id.backFromListName1);
	// editListName = (LinearLayout) view1.findViewById(R.id.editListButton);
	// searchNameListEditText = (EditText) view1
	// .findViewById(R.id.searchNameEditText);
	// nameList = (ListView) view1.findViewById(R.id.nameList);
	//
	// createNewListLayout = (LinearLayout) view1
	// .findViewById(R.id.createNewListsLayout);
	// TextView listTextView = (TextView) view1
	// .findViewById(R.id.listTextView);
	// TextView createNewListTextView = (TextView) view1
	// .findViewById(R.id.createNewListTextView);
	// TextView editListNameTextView = (TextView) view1
	// .findViewById(R.id.editListButtonTextView);
	//
	// Typeface typeface = FontStyle.getFont(Activity_Home.this,
	// AppConstants.HelveticaNeueLTStd_Roman);
	// listTextView.setTypeface(typeface);
	// createNewListTextView.setTypeface(typeface);
	// searchNameListEditText.setTypeface(typeface);
	// editListNameTextView.setTypeface(typeface);
	// userListAdapter = new UserListAdapter(userListDTOs, Activity_Home.this,
	// 0);
	// nameList.setAdapter(userListAdapter);
	// if (WebServiceConstants.isOnline(Activity_Home.this)) {
	// new GetUserList().execute(WebServiceConstants.BASE_URL
	// + WebServiceConstants.GET_USER_LIST);
	// }
	// searchNameListEditText.addTextChangedListener(new TextWatcher() {
	//
	// public void afterTextChanged(Editable s) {
	// }
	//
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// }
	//
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// userListAdapter.getFilter().filter(s.toString());
	// }
	// });
	// nameList.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1,
	// int position, long arg3) {
	// setListsItemLayout(userListAdapter.getItem(position));
	//
	// }
	// });
	//
	// backFromListImageButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// settingAllGuysLayout();
	//
	// }
	// });
	// editListName.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// setEditListsLayout();
	//
	// }
	// });
	// createNewListLayout.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// Intent intent = new Intent(Activity_Home.this,
	// Activity_R_CreateNewList.class);
	// startActivityForResult(intent, 1);
	//
	// }
	// });
	// }

	// private void setEditListsLayout() {
	// LayoutInflater inflater = getLayoutInflater();
	// rightContainer.removeAllViews();
	// LinearLayout cancelEditListButton, saveEditListButton, createNewList;
	// EditText searchNameListEditText;
	// ListView nameList;
	// View view1 = inflater.inflate(R.layout.right_menu_lists_edit,
	//
	// rightContainer);
	// cancelEditListButton = (LinearLayout) view1.findViewById(R.id.cancel);
	// saveEditListButton = (LinearLayout) view1.findViewById(R.id.save);
	// searchNameListEditText = (EditText) view1
	// .findViewById(R.id.searchNameEditText);
	// nameList = (ListView) view1.findViewById(R.id.nameList);
	//
	// createNewList = (LinearLayout) view1
	// .findViewById(R.id.createNewListsLayout);
	// TextView listTextView = (TextView) view1
	// .findViewById(R.id.listTextView);
	// TextView createNewListTextView = (TextView) view1
	// .findViewById(R.id.createNewListTextView);
	// TextView cancleTextView = (TextView) view1
	// .findViewById(R.id.backFromListName1);
	// TextView saveTextView = (TextView) view1
	// .findViewById(R.id.createNewListImageButton);
	//
	// Typeface typeface = FontStyle.getFont(Activity_Home.this,
	// AppConstants.HelveticaNeueLTStd_Roman);
	// listTextView.setTypeface(typeface);
	// createNewListTextView.setTypeface(typeface);
	// searchNameListEditText.setTypeface(typeface);
	// cancleTextView.setTypeface(typeface);
	// saveTextView.setTypeface(typeface);
	//
	// userListAdapter = new UserListAdapter(userListDTOs, Activity_Home.this,
	// 1);
	// nameList.setAdapter(userListAdapter);
	// if (WebServiceConstants.isOnline(Activity_Home.this)) {
	// new GetUserList().execute(WebServiceConstants.BASE_URL
	// + WebServiceConstants.GET_USER_LIST);
	// }
	// searchNameListEditText.addTextChangedListener(new TextWatcher() {
	//
	// public void afterTextChanged(Editable s) {
	// }
	//
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// }
	//
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// userListAdapter.getFilter().filter(s.toString());
	// }
	// });
	//
	// cancelEditListButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// setListsLayout();
	//
	// }
	// });
	// saveEditListButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// setListsLayout();
	//
	// }
	// });
	// createNewList.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// Intent intent = new Intent(Activity_Home.this,
	// Activity_R_CreateNewList.class);
	// startActivityForResult(intent, 1);
	//
	// }
	// });
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (fragmentIds == AppConstants.FRAGMENT_PROFILE) {
			tempFragment.onActivityResult(requestCode, resultCode, data);
		} else if (fragmentIds == AppConstants.FRAGMENT_CHATT_MATCHES) {
			Log.d("", "onActivityResult1 chat fragmnet");
			tempFragment.onActivityResult(requestCode, resultCode, data);
		}

		if (resultCode == RESULT_OK
				&& requestCode == AppConstants.CREATE_NEW_LIST) {
			UserListDTO userListDTO = new UserListDTO();
			userListDTO.setListId(data.getExtras().getString("listId"));
			userListDTO.setListName(data.getExtras().getString("name"));
			userListDTOs.add(userListDTO);
			userListAdapter.notifyDataSetChanged();
			Log.d("", "my new list =" + data.getExtras().getString("listId")
					+ " " + data.getExtras().getString("name"));
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.DELETE_DUDE_FROM_LIST) {

			subListAdapter.notifyDataSetChanged();
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.EDIT_LIST) {

			chatMatchesAdapter.notifyDataSetChanged();
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.PUSH_NOTIFICATION_ACTIVITY) {

			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);

			// Edited for revision 615
			// MAY 20 2015
			// added
			RecentlySearchedcity.isConvoHistoryLoaded = false;
			RecentlySearchedcity.isGetFriendsLoaded = false;
			// - - - - -
			slide_me.close();
		} else if (requestCode == AppConstants.FRIEND_NOTIFICATION_ACTIVITY
				&& resultCode == AppConstants.FRIEND_NOTIFICATION_RESULT2) {

			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			slide_me.close();
		} else if (requestCode == AppConstants.FRIEND_NOTIFICATION_ACTIVITY
				&& resultCode == AppConstants.FRIEND_NOTIFICATION_RESULT1) {
			// LoadHomeFlag=true;
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			slide_me.close();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// private void setListsItemLayout(final UserListDTO userListDTO) {
	// ListView subList;
	//
	// EditText searchSubListEditText;
	// TextView editSubListButton;
	//
	// LayoutInflater inflater = getLayoutInflater();
	//
	// View view = inflater.inflate(R.layout.right_menu_lists_item,
	// rightContainer);
	// TextView listName = (TextView) view.findViewById(R.id.listName);
	// listName.setText("" + userListDTO.getListName());
	// editSubListButton = (TextView) view.findViewById(R.id.editSubList);
	// TextView lists = (TextView) view.findViewById(R.id.lists);
	// searchSubListEditText = (EditText) view
	// .findViewById(R.id.searchSubListEditText);
	//
	// Typeface typeface = FontStyle.getFont(Activity_Home.this,
	// AppConstants.HelveticaNeueLTStd_Roman);
	// listName.setTypeface(typeface);
	// editSubListButton.setTypeface(typeface);
	// lists.setTypeface(typeface);
	// searchSubListEditText.setTypeface(typeface);
	//
	// subList = (ListView) view.findViewById(R.id.subList);
	//
	// subListAdapter = new SearchByAdapter(Activity_Home.this,
	// userListDTO.getDudeList(), 0);
	// subList.setAdapter(subListAdapter);
	// subList.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// FragmentChatMatches.userDTO = (UserDTO) subListAdapter
	// .getItem(arg2);
	//
	// // Edited for revision 614
	// // MAY 20 2015
	// // added
	// RecentlySearchedcity.isConvoHistoryLoaded = false;
	// RecentlySearchedcity.isGetFriendsLoaded = false;
	// // - -- - - - - -
	//
	// Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
	// slide_me.close();
	// }
	// });
	//
	// searchSubListEditText.addTextChangedListener(new TextWatcher() {
	//
	// public void afterTextChanged(Editable s) {
	// }
	//
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// }
	//
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// subListAdapter.getFilter().filter(s.toString());
	// }
	// });
	//
	// editSubListButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// if (userListDTO.getDudeList().size() != 0) {
	// Activity_R_EditList.userListDTO = userListDTO;
	// Intent intent = new Intent(Activity_Home.this,
	// Activity_R_EditList.class);
	// intent.putExtra(AppConstants.COME_FROM,
	// AppConstants.COME_FROM_LISTS);
	// startActivityForResult(intent,
	// AppConstants.DELETE_DUDE_FROM_LIST);
	// } else {
	// Toast.makeText(Activity_Home.this, "Empty list",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// }
	// });
	//
	// lists.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// setListsLayout();
	//
	// }
	// });
	//
	// }

	@Override
	public void onClick(View v) {

		/* Init Flurry Params */
		Map<String, String> leftDrawerParams = new HashMap<String, String>();

		switch (v.getId()) {
		case R.id.left_buton:

			if (fragmentIds == AppConstants.FRAGMENT_PROFILE) {

				// if (WebServiceConstants.isOnline(Activity_Home.this)) {
				// ((FragmentProfile) tempFragment).checkValidation(1);
				// } else {
				slide_me.toggleLeftDrawer();
				// }

			} else {
				setLeftDrawer();
			}

			break;
		case R.id.right_buton: {
			FlurryAgent.logEvent("Toggle Right Drawer"); // Edited revision 582
															// commented

			// if (fragmentIds == AppConstants.FRAGMENT_PROFILE) {
			//
			// if (WebServiceConstants.isOnline(Activity_Home.this)) {
			// // ((FragmentProfile) tempFragment).checkValidation(2);
			// setLeftDrawer = 0;
			// } else {
			// slide_me.toggleRightDrawer();
			// ;
			// }
			// } else {
			// if(LoadHomeFlag)
			// {
			// Attach_Fragment(AppConstants.FRAGMENT_HOME);
			// LoadHomeFlag=false;
			// }
			// if (mSearchByAdapter != null) {
			// mSearchByAdapter.notifyDataSetChanged();
			// }
			//
			// if (requistListAdapter != null) {
			// requistListAdapter.notifyDataSetChanged();
			// }
			// if(singleton.isReloadChatNav){

			// EDITED FOR REVISION 608
			// MAY 20 2015
			// added if statement
			if (RecentlySearchedcity.isConvoHistoryLoaded == false
					|| RecentlySearchedcity.isGetFriendsLoaded == false) {
				// settingAllGuysLayout();
			}
			chatMatchesAdapter.notifyDataSetChanged();
			// }

			// edited for revision 603
			// MAY 19 2015 added
			RecentlySearchedcity.isRightMenuActive = true;
			// - - - -
			slide_me.toggleRightDrawer();

		}
			break;
		// case R.id.home:
		// // Attach_Fragment(AppConstants.FRAGMENT_HOME);
		// break;
		case R.id.profile:

			// dl editen on revision 589
			// May 13 2015 4:18 pm
			PastFragment = 2;

			if (WebServiceConstants.isOnline(Activity_Home.this)) {
				logFlurryEvent(leftDrawerParams, "Profile");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "Profile");
				selectedItem = 0;
				Attach_Fragment(AppConstants.FRAGMENT_PROFILE);
				slide_me.close();

			} else {
				Toast.makeText(Activity_Home.this, R.string.toast_please_wait,
						Toast.LENGTH_SHORT).show();
			}

			// if (WebServiceConstants.isOnline(Activity_Home.this)) {
			// Intent intent = new Intent(Activity_Home.this,
			// Activity_ProfileDetails.class);
			// intent.putExtra(AppConstants.COME_FROM,
			// AppConstants.COME_FROM_UPDATE_PROFILE);
			// startActivity(intent);
			// }
			break;
		case R.id.findDudes:
			// dl editen on revision 589
			// May 13 2015 4:18 pm
			PastFragment = 3;

			logFlurryEvent(leftDrawerParams, "Find Guys");
			easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
					AppConstants.EVENT_LOG_ACTION_BTN, "FindGuys");

			selectedItem = 1;
			// Activity_Home.findDudesScrollPosition = 0;
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			slide_me.close();
			break;
		case R.id.searchCity:

			// dl editen on revision 589
			// May 13 2015 4:18 pm
			if (singleton.isPaidUser) {
				PastFragment = 15;

				logFlurryEvent(leftDrawerParams, "Search City");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "Search City");

				selectedItem = 2;
				// Activity_Home.findDudesScrollPosition = 0;
				FragmentSearchCity.setBackButtonFunction();
				Attach_Fragment(AppConstants.FRAGMENT_SEARCH_CITY);
				slide_me.close();
			} else {
				subscribeToPaidAccount();
			}
			break;
		// case R.id.getMatches:
		// // COME_FROM = AppConstants.FRAGMENT_GET_MATCHES;
		// //
		// // Attach_Fragment(AppConstants.FRAGMENT_HOME);
		// break;
		case R.id.dailyKrave:

			if (WebServiceConstants.isOnline(Activity_Home.this)) {

				// dl editen on revision 589
				// May 13 2015 4:18 pm
				PastFragment = 5;

				logFlurryEvent(leftDrawerParams, "Daily Krave");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "DailyKrave");
				selectedItem = 3;

				Attach_Fragment(AppConstants.FRAGMENT_DAILY_KRAVE);
				slide_me.close();
			}
			break;
		case R.id.chatMatches:

			if (WebServiceConstants.isOnline(Activity_Home.this)) {

				// dl editen on revision 589
				// May 13 2015 4:18 pm
				PastFragment = 6;

				logFlurryEvent(leftDrawerParams, "Favorites");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "Favorites");
				selectedItem = 4; // original:4 edited on revision 859 :6
				Attach_Fragment(AppConstants.FRAGMENT_CHAT_FRIEND);
				slide_me.close();
			}
			break;

		case R.id.nightlife_finder:

			if (WebServiceConstants.isOnline(Activity_Home.this)) {

				// dl editen on revision 589
				// May 13 2015 4:18 pm
				PastFragment = 17;

				logFlurryEvent(leftDrawerParams, "Nightlife Finder");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "Nightlife Finder");
				selectedItem = 5; // original:4 edited on revision 859 :6
				Attach_Fragment(AppConstants.FRAGMENT_NIGHTLIFE_FINDER);
				slide_me.close();
			}
			break;

		case R.id.imageRequest:
			if (WebServiceConstants.isOnline(Activity_Home.this)) {

				PastFragment = 16;

				logFlurryEvent(leftDrawerParams, "Image Request");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "Image Request");

				selectedItem = 6;

				Attach_Fragment(AppConstants.FRAGMENT_IMAGE_REQUEST);
				slide_me.close();
			}
			break;

		case R.id.setting:
			if (WebServiceConstants.isOnline(Activity_Home.this)) {

				// dl editen on revision 589
				// May 13 2015 4:18 pm
				PastFragment = 87;

				logFlurryEvent(leftDrawerParams, "Setting");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "Settings");

				selectedItem = 7;

				FragmentSetting.comeFrom = false;
				Attach_Fragment(AppConstants.FRAGMENT_SETTING_NEW);
				slide_me.close();
			}
			break;
		// case R.id.map:
		// Attach_Fragment(AppConstants.FRAGMENT_MAP);
		// break;

		case R.id.contactUs:
			if (WebServiceConstants.isOnline(Activity_Home.this)) {

				// dl editen on revision 589
				// May 13 2015 4:18 pm
				PastFragment = 100;

				logFlurryEvent(leftDrawerParams, "Contact Us");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "ContactUs");
				// Intent intent = new Intent(Activity_Home.this,
				// Activity_Feedback.class);
				// startActivity(intent);
				selectedItem = 8;
				Attach_Fragment(AppConstants.FRAGMENT_FEEDBACK);
				slide_me.close();
			}
			break;
		case R.id.logout:

			if (WebServiceConstants.isOnline(Activity_Home.this)) {

				// dl editen on revision 589
				// May 13 2015 4:18 pm
				PastFragment = 1;

				logFlurryEvent(leftDrawerParams, "Logout");
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_LEFT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "Logout");
				selectedItem = 9;
				openDailog();
			}

			// Intent intent = new Intent(Activity_Home.this,
			// MainActivity.class);
			// startActivity(intent);
			// finish();
			// // connection.disconnect();// chat lagout
			// sessionManager.Logout();
			// Intent intent = new Intent(Activity_Home.this,
			// MainActivity.class);
			// startActivity(intent);

			break;

		default:
			break;
		}
		// /*----------for ads----------------*/
		// if ((v.getId() != R.id.logout) && (v.getId() != R.id.left_buton)
		// && (v.getId() != R.id.right_buton)
		// && (v.getId() != R.id.searchCity)) {
		// startInterstitialAds();
		// }
		// /*---------------------------------*/

	}

	public void logFlurryEvent(Map<String, String> leftDrawerParams,
			String event) {
		leftDrawerParams.put("Title", event);
		FlurryAgent.logEvent("Left Drawer Button Pressed", leftDrawerParams); // Edited
																				// revision
																				// 582
																				// commented
	}

	public void openDailog() {
		try {
			final Dialog dialog = new Dialog(Activity_Home.this,
					android.R.style.Theme_Translucent_NoTitleBar);

			dialog.setContentView(R.layout.dialog_logout);

			Button cancle = (Button) dialog.findViewById(R.id.cancle);
			Button ok = (Button) dialog.findViewById(R.id.ok);
			TextView title = (TextView) dialog.findViewById(R.id.title);
			Typeface typeface = FontStyle.getFont(Activity_Home.this,
					AppConstants.HelveticaNeueLTStd_Lt);
			title.setTypeface(typeface);
			cancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (WebServiceConstants.isOnline(Activity_Home.this)) {
						stopUserCurrentLocationService();
						// RecentlySearchedcity.setInstance();
						new InsertActivityLog()
								.execute(
										WebServiceConstants.AV_BASE_URL
												+ WebServiceConstants.AV_INSERT_ACTIVITY_LOG,
										"Logout", ""
												+ sessionManager
														.getUserDetail()
														.getUserID());
						clearNotification();
						finish();
						// connection.disconnect();// chat lagout

						sessionManager.Logout();
					} else {
						Toast.makeText(
								Activity_Home.this,
								R.string.toast_please_check_internet_connection,
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			dialog.show();
		} catch (Exception e) {

		}
	}

	protected void clearNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}

	public void openDailogForAcceptPhotoRequest(String message) {
		final Dialog dialog = new Dialog(Activity_Home.this,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_accept_photo_request);
		try {

			Button noButton = (Button) dialog.findViewById(R.id.no);
			Button yesButton = (Button) dialog.findViewById(R.id.yes);
			Button okButton = (Button) dialog.findViewById(R.id.ok);
			TextView title = (TextView) dialog.findViewById(R.id.titel);
			TextView subTitle = (TextView) dialog.findViewById(R.id.sub_titel);
			Typeface typeface = FontStyle.getFont(Activity_Home.this,
					AppConstants.HelveticaNeueLTStd_Lt);
			title.setTypeface(typeface);
			noButton.setTypeface(typeface);
			yesButton.setTypeface(typeface);
			okButton.setTypeface(typeface);
			// {"alert":"You have an image request","owner_id":"13605","user_id":"13631","image_id":"21137"}
			JSONObject jsonObject = new JSONObject(message);
			final String alert = jsonObject.getString("message");
			// final String name = jsonObject.getString("name");
			// final String name = "demo";
			final String userid = jsonObject.getString("user_id");
			final String ownerId = jsonObject.getString("owner_id");
			final String imageId = jsonObject.getString("image_id");
			final String fName = jsonObject.getString("user_fname");
			final String lName = jsonObject.getString("user_lname");
			// final String notificationId =
			// jsonObject.getString("notification_id");
			// {"message":"You have an image request",
			// "user_lname":"","owner_id":"13605",
			// "user_id":"770","user_fname":"jojo",
			// "image_id":"21137"}

			if (alert.contains("You have an image request")) {
				FragmentChatMatches.ChatXmppUserId = userid;
				FragmentChatMatches.comeFrom = false;
				title.setText(fName
						+ getResources()
								.getString(
										R.string.dialog_is_requesting_to_view_your_private_photo));

			} else if (alert.contains("You have an image response")) {
				FragmentChatMatches.ChatXmppUserId = ownerId;
				FragmentChatMatches.comeFrom = false;
				if (jsonObject.getString("response").equals("1")) {// approved
																	// photo
																	// request
					title.setText(fName
							+ getResources()
									.getString(
											R.string.dialog_has_approved_your_request_to_view_his_private_photo));
				} else {// reject photo request
					title.setText(fName
							+ getResources()
									.getString(
											R.string.dialog_has_declined_your_request_to_view_his_private_photo_));
				}

				subTitle.setVisibility(View.GONE);
				noButton.setVisibility(View.GONE);
				yesButton.setVisibility(View.GONE);
				okButton.setVisibility(View.VISIBLE);
			}

			noButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AcceptPhotoRequest()
							.execute(
									WebServiceConstants.ACCEPT_VIEW_PRIVATE_PHOTO_REQUEST,
									userid, ownerId, imageId,
									AppConstants.PRIVATE_PHOTO_REQUEST_REJECT);
					dialog.dismiss();
				}
			});
			yesButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (WebServiceConstants.isOnline(Activity_Home.this)) {

						new AcceptPhotoRequest()
								.execute(
										WebServiceConstants.ACCEPT_VIEW_PRIVATE_PHOTO_REQUEST,
										userid,
										ownerId,
										imageId,
										AppConstants.PRIVATE_PHOTO_REQUEST_ACCEPT);

					}
				}
			});

			okButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// new CancelPhotoResponse().execute(
					// WebServiceConstants.CANCEL_IMAGE_RESPONSE,
					// notificationId);
					dialog.dismiss();

				}
			});

		} catch (Exception e) {
			Log.d("", "openDailogForAcceptPhotoRequest" + e);
		}

		dialog.show();
	}

	// public void openDailogForPushNotification(final UserDTO userDTO) {
	// final Dialog dialog = new Dialog(Activity_Home.this,
	// android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	//
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//
	// dialog.setContentView(R.layout.dialog_push_notification_friend_request);
	//
	// Button cancle = (Button) dialog.findViewById(R.id.cancle);
	// Button ok = (Button) dialog.findViewById(R.id.ok);
	// ImageView userProfileImage = (ImageView) dialog
	// .findViewById(R.id.userProfileImage);
	// ImageView dudeProfileImage = (ImageView) dialog
	// .findViewById(R.id.dudeProfileImage);
	//
	// imageLoader.DisplayImage(sessionManager.getUserDetail()
	// .getUserProfileImageDTOs().get(0).getImagePath(),
	// userProfileImage);
	// Log.d("",
	// "userProfileImage="
	// + sessionManager.getUserDetail()
	// .getUserProfileImageDTOs().get(0)
	// .getImagePath());
	//
	// Log.d("", "dudeProfileImage="
	// + userDTO.getUserProfileImageDTOs().get(0).getImagePath());
	// imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
	// .getImagePath(), dudeProfileImage);
	//
	// cancle.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// FragmentHome.Refresh_Data = true;
	// dialog.dismiss();
	// }
	// });
	// ok.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// FragmentChatMatches.userDTO = userDTO;
	// dialog.dismiss();
	// FragmentHome.Refresh_Data = true;
	// Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
	//
	// }
	// });
	// dialog.show();
	// }

	private void setLeftDrawer() {
		switch (setLeftDrawer) {
		case AppConstants.BACK_BUTTON_FROM_DUDES_PROFILE:
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			setLeftDrawer = 0;
			left_button.setImageResource(R.drawable.av_new_nav_menuup);
			break;
		case AppConstants.BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE:
			if (FragmentDetailDudesProfile.comeFrom == AppConstants.FRAGMENT_CHAT_FRIEND) {
				Attach_Fragment(AppConstants.FRAGMENT_CHAT_FRIEND);
			} else if (FragmentDetailDudesProfile.comeFrom == AppConstants.FRAGMENT_SEARCH_CITY) {
				Attach_Fragment(AppConstants.FRAGMENT_SEARCH_CITY);
			} else {
				Attach_Fragment(AppConstants.FRAGMENT_HOME);
			}
			setLeftDrawer = 0;
			left_button.setImageResource(R.drawable.av_new_nav_menuup);
			break;
		case AppConstants.BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE_WITH_CHAT:
			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			setLeftDrawer = 0;
			left_button.setImageResource(R.drawable.av_new_nav_menuup);
			break;
		case AppConstants.BACK_BUTTON_FROM_SETTING:
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			setLeftDrawer = 0;
			COME_FROM = AppConstants.FRAGMENT_SETTING;
			left_button.setImageResource(R.drawable.av_new_nav_menuup);

			break;
		case AppConstants.BACK_BUTTON_FROM_DAILY_KRAVE_DETAIL:
			Attach_Fragment(AppConstants.FRAGMENT_DAILY_KRAVE);
			setLeftDrawer = 0;
			left_button.setImageResource(R.drawable.av_new_nav_menuup);
			break;
		case AppConstants.BACK_BUTTON_FROM_UPDATE_PROFILE:
			((FragmentUpdateProfile) tempFragment).setBackButtonFunction();

			break;
		// case AppConstants.BACK_BUTTON_FROM_UPDATE_IMAGE_PROFILE:
		// if (((FragmentProfile) tempFragment).checkValidation()) {
		// setLeftDrawer = 0;
		// slide_me.toggleLeftDrawer();
		// }
		//
		// break;
		default:
			FlurryAgent.logEvent("Toggle Left Drawer"); // Edited revision 582
														// commented

			slide_me.toggleLeftDrawer();
			break;
		}

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {

		try {
			if (id == 0) {
				FragmentUpdateProfile fragmentUpdateProfile = (FragmentUpdateProfile) tempFragment;
				return new DatePickerDialog(
						this,
						fragmentUpdateProfile.updateProfileAdapter.datePickerListener,
						fragmentUpdateProfile.updateProfileAdapter.year,
						fragmentUpdateProfile.updateProfileAdapter.month,
						fragmentUpdateProfile.updateProfileAdapter.day);
			} else if (id == 1) {
				FragmentProfile fragmentProfile = (FragmentProfile) tempFragment;
				return new DatePickerDialog(this,
						fragmentProfile.datePickerListener,
						fragmentProfile.year, fragmentProfile.month,
						fragmentProfile.day);
			} else {
				return null;
			}
		} catch (Exception e) {

			e.printStackTrace();
			Toast.makeText(Activity_Home.this,
					"Please restart app and try again", Toast.LENGTH_SHORT)
					.show();
			return null;
		}

	}

	public void Attach_Fragment(int fragmentId) {

		try {
			LinearLayout selectedLayout[] = { layoutProfile, layoutFindDudes,
					layoutSearchCity, layoutDailyKrave, layoutChatMatches,
					layoutNightlifeFinder, layoutImageRequest, layoutSetting,
					layoutContactUs, layoutLogout };
			// if (fragmentId <= 7) {
			// selectedLayout[selectedItem]
			// .setBackgroundColor(R.color.gray);
			// selectedItem = fragmentId;
			// selectedLayout[selectedItem - 1]
			// .setBackgroundColor(R.color.unselect);
			// }
			// Toast.makeText(Activity_Home.this, "" + selectedItem,
			// Toast.LENGTH_SHORT).show();
			// Toast.makeText(Activity_Home.this, "" + previousItem,
			// Toast.LENGTH_SHORT).show();

			// for (int i = 0; i < 6; i++) {
			// if ((selectedItem) == i) {
			selectedLayout[previousItem]
					.setBackgroundResource(R.color.transprent_color_code);
			selectedLayout[selectedItem].setBackgroundResource(R.color.select);
			previousItem = selectedItem;
			// } else {

			// }
			// }

			this.fragmentIds = fragmentId;
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			RemoveAll(fragmentTransaction);

			switch (fragmentId) {
			case AppConstants.FRAGMENT_SETTING_NEW:
				tempFragment = new FragmentSettingNew();
				break;
			case AppConstants.FRAGMENT_FEEDBACK:
				tempFragment = new FragmentFeedback();
				break;
			case AppConstants.FRAGMENT_HOME:
				tempFragment = new FragmentHome();
				break;
			case AppConstants.FRAGMENT_SEARCH_CITY:
				tempFragment = new FragmentSearchCity();

				break;
			case AppConstants.FRAGMENT_PROFILE:
				tempFragment = new FragmentProfile();

				break;
			case AppConstants.FRAGMENT_FIND_DUDES:
				// tempFragment = new FragmentFindDudes();
				tempFragment = new FragmentHome();
				break;
			case AppConstants.FRAGMENT_GET_MATCHES:
				// tempFragment = new FragmentGetMatches();
				tempFragment = new FragmentHome();
				break;
			case AppConstants.FRAGMENT_DAILY_KRAVE:
				tempFragment = new FragmentDailyKraveWithWebView();

				break;
			case AppConstants.FRAGMENT_CHATT_MATCHES:
				tempFragment = new FragmentChatMatches();

				break;
			case AppConstants.FRAGMENT_NIGHTLIFE_FINDER:
				tempFragment = new FragmentNightlifeFinder();

				break;
			case AppConstants.FRAGMENT_SETTING:
				notificationHeader.setVisibility(View.GONE);
				// Activity_Home.findDudesScrollPosition = 0;
				tempFragment = new FragmentSetting();

				break;
			case AppConstants.FRAGMENT_MAP:
				// tempFragment = new FragmentMap();

				break;
			case AppConstants.FRAGMENT_DUDES_PROFILE:
				tempFragment = new FragmentDudesProfile();

				break;
			case AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE:
				tempFragment = new FragmentDetailDudesProfile();

				break;

			case AppConstants.FRAGMENT_CHAT_FRIEND:
				/* av singleton */
				AppManager.initInstance();
				AppManager.getInstance().isFromHome = true;
				tempFragment = new FragmentChatFriend();
				break;
			// case AppConstants.FRAGMENT_DUDES_CONGRATULATION:
			// tempFragment = new FragmentDudesCongratulation();
			//
			// break;
			case AppConstants.FRAGMENT_DAILY_KRAVE_DETAIL:
				tempFragment = new FragmentDetailDailyKrave();

				break;
			case AppConstants.FRAGMENT_UPDATE_PROFILE:
				tempFragment = new FragmentUpdateProfile();

				break;
			case AppConstants.FRAGMENT_IMAGE_REQUEST:
				tempFragment = new FragmentImageRequest();
				break;
			default:
				break;
			}
			ChatService.ONCHAT = fragmentId;
			tempFragment.setRetainInstance(true);
			fragmentTransaction.replace(R.id.containerLayout, tempFragment);

			// fragmentTransaction.attach(tempFragment);

			fragmentTransaction.commitAllowingStateLoss();
			fragmentTransaction.disallowAddToBackStack();
			/*----------set banner ads location ----------------*/
			setBannerAdsLocation();
			/* show interstatial ads on chat and user detail screen */
			if ((fragmentId == AppConstants.FRAGMENT_HOME)
			// || (fragmentId == AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE)
			) {
				startInterstitialAds();
			}
			/*---------------------------------*/
		} catch (Exception e) {

		}
	}

	private void setBannerAdsLocation() {

		if (!singleton.isPaidUser) {
			if (singleton.adsizeAndLocation
					.equals(AppConstants.ADS_CENTER_BANNER)) { // for native
																// ads

				// if (fragmentIds == AppConstants.FRAGMENT_HOME) {
				adViewFrameLayout.setVisibility(View.GONE);
				// } else {
				// adViewFrameLayout.setVisibility(View.VISIBLE);
				// }

			} else if (singleton.adsizeAndLocation
					.equals(AppConstants.ADS_BOTTOM_BANNER)) { // for
				// adViewFrameLayout.setVisibility(View.VISIBLE);

				if (fragmentIds == AppConstants.FRAGMENT_HOME) {
					adViewFrameLayout.setVisibility(View.VISIBLE);
				} else {
					adViewFrameLayout.setVisibility(View.GONE);
				}

			} else if (singleton.adsizeAndLocation
					.equals(AppConstants.ADS_INTERSTITIAL)) { // for
				// interstatial
				// ads
				adViewFrameLayout.setVisibility(View.GONE);
			}

		}
	}

	private void startInterstitialAds() {

		if ((!singleton.isPaidUser)
				&& (singleton.adsizeAndLocation
						.equals(AppConstants.ADS_INTERSTITIAL))) { // for
																	// interstatial
																	// ads
			adViewFrameLayout.setVisibility(View.GONE);
			if (adMob == null) {
				adMob = new AdMob(this, singleton);

			}
			adMob.startAdvertising(adViewFrameLayout);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			switch (fragmentIds) {
			case AppConstants.FRAGMENT_HOME:
				((FragmentHome) tempFragment).setBackButtonFunction();
				finish();
				break;
			case AppConstants.FRAGMENT_DUDES_PROFILE:
				// Edited on revision 59
				Attach_Fragment(AppConstants.FRAGMENT_HOME);
				setLeftDrawer = 0;
				// left_button.setImageResource(R.drawable.nav);
				break;
			case AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE:
				// Edited on revision 595
				// MAY 15 2015
				// 10:59 AM
				// dl from AppConstants.FRAGMENT_HOME to PastFragment
				Attach_Fragment(PastFragment);
				setLeftDrawer = 0;

				// left_button.setImageResource(R.drawable.nav);
				break;
			case AppConstants.FRAGMENT_CHATT_MATCHES:
				// if (FragmentChatMatches.backButtonComeFrom) {

				// dl edited on revision 589
				// May 13 2015 3:58 PM
				// commented
				// Attach_Fragment(AppConstants.FRAGMENT_CHAT_FRIEND);
				// selectedItem = 4;
				// setLeftDrawer = 0;
				// Added
				setLeftDrawer = 0;
				selectedItem = 1;
				Attach_Fragment(PastFragment);
				// slide_me.toggleRightDrawer();

				// } else {
				//
				// setLeftDrawer = 0;
				//
				// left_button.setImageResource(R.drawable.nav);
				// onClick(layoutFindDudes);
				//
				// }
				break;
			case AppConstants.FRAGMENT_DAILY_KRAVE_DETAIL:
				setLeftDrawer = 0;

				left_button.setImageResource(R.drawable.av_new_nav_menuup);
				Attach_Fragment(AppConstants.FRAGMENT_DAILY_KRAVE);
				break;
			case AppConstants.FRAGMENT_SETTING:

				setLeftDrawer = 0;

				// left_button.setImageResource(R.drawable.nav);
				// onClick(layoutFindDudes);
				selectedItem = 1;
				Attach_Fragment(AppConstants.FRAGMENT_HOME);

				break;
			case AppConstants.FRAGMENT_UPDATE_PROFILE:
				((FragmentUpdateProfile) tempFragment).setBackButtonFunction();
				break;
			case AppConstants.FRAGMENT_PROFILE:
				// ((FragmentProfile) tempFragment).checkValidation(3);

				// dl edited on revision 589
				// May 13 2015 3:51 pm putted
				selectedItem = 1;
				Attach_Fragment(AppConstants.FRAGMENT_HOME);
				break;
			default:
				selectedItem = 1;
				Attach_Fragment(AppConstants.FRAGMENT_HOME);
				// onClick(layoutFindDudes);
				break;
			}
		}
		return false;
	}

	private static void RemoveAll(FragmentTransaction fragmentTransaction) {
		try {
			ChatService.ONCHAT = AppConstants.FRAGMENT_HOME;
			if (tempFragment != null)
				fragmentTransaction.detach(tempFragment);
			System.gc();
		} catch (Exception e) {

		}

	}

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long
	// arg3) {
	//
	// FragmentChatMatches.userDTO = searchDudeList.get(arg2);
	// Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
	//
	// }

	class deleteSelectedDude extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		// http://parkhya.org/Android/krave_app/index.php?action=getfriends&user_id=107
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(Activity_Home.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// //
			// http://parkhya.org/Android/krave_app/index.php?action=user_login&email=test2@gmail.com&password=123456
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));

			for (int i = 0; i < selectedUserDTOs.size(); i++) {
				params.add(new BasicNameValuePair("friend_ids[" + i + "]",
						selectedUserDTOs.get(i).getUserID()));
			}
			// ArrayList<String> arrIDSeparator = new ArrayList<String>();
			//
			// for (int i = 0; i < selectedUserDTOs.size(); i++) {
			// ArrayList<String> arrFriendID = new ArrayList<String>();
			// arrFriendID.add(String.valueOf(selectedUserDTOs.get(i).getUserID()));
			//
			// arrIDSeparator.add(String.valueOf(arrFriendID));
			// }
			// params.add(new BasicNameValuePair("friend_ids",
			// String.valueOf(arrIDSeparator)) );
			System.out.println("params : " + params);

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("delete user response :", "" + json);

			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				dialog.dismiss();
			} catch (Exception e) {

			}

			// edited for revision 608
			// MAY 20 2015
			// added
			RecentlySearchedcity.searchDudeList.removeAll(selectedUserDTOs);
			// - - - --
			// searchDudeList.removeAll(selectedUserDTOs); // edit on 15/07/2015
			updateUserListAdapter.notifyDataSetChanged();

		}
	}

	class GetChatFriends extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// rightContainer.setVisibility(View.GONE);
			singleton.progressLoading(loadingView, llLoading);

			// singleton.progressLoading(loadingView, llLoading);
			// if(singleton.isReloadChatNav){
			// dialog = new TransparentProgressDialog(Activity_Home.this);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
			// singleton.isReloadChatNav = false;
			// }
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// edited for revision 610
			// MAY 20 2015
			// added if statement
			if (!(sessionManager.getUserDetail().getUserID() == null)) {
				params.add(new BasicNameValuePair("user_id", sessionManager
						.getUserDetail().getUserID()));
			}
			// -- - - -- - - -
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("GetChatFriends :", "GetChatFriends=" + json);

			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			// Edited for revision 608
			// MAY 20 2015
			// added
			RecentlySearchedcity.searchDudeList.clear();
			// - - - - - -
			// searchDudeList.clear();// edit on 15/07/2015

			// System.out.print("search user response :" + jsonArray.length());

			try {

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject Object = jsonArray.getJSONObject(i);

					// Edited for revision 608
					// MAY 20 2015
					// added
					UserDTO userDTO = parseUserDataForChatList(Object);
					RecentlySearchedcity.searchDudeList.add(userDTO);
					// - - - - - -
					// searchDudeList.add(parseUserDataForChatList(Object)); //
					// edit on 15/07/2015
					new GetChatHistoryByPagination()
							.execute(
									WebServiceConstants.GET_CHAT_HISTORY_OF_SINGLE_USER_BY_PAGE_ID,
									userDTO.getUserID());
				}

				// Collections.sort(searchDudeList, new Comparator<UserDTO>() {
				// // edit on 15/07/2015
				// @Override
				// public int compare(UserDTO arg0, UserDTO arg1) {
				// // TODO Auto-generated method stub
				// return arg1.getUnreadMsg().compareTo(
				// arg0.getUnreadMsg());
				// }
				// });

				// Edited for revision 608
				// MAY 20 2015
				// added
				Collections.sort(RecentlySearchedcity.searchDudeList,
						new Comparator<UserDTO>() {
							@Override
							public int compare(UserDTO arg0, UserDTO arg1) {
								// TODO Auto-generated method stub
								return arg1.getUnreadMsg().compareTo(
										arg0.getUnreadMsg());
							}
						});
				// - - - - - - -

				// System.out.println("fuck: " + searchDudeList.size()); // edit
				// on 15/07/2015

				/*
				 * if (searchDudeList.size() == 0) {
				 * Toast.makeText(Activity_Home.this, "No Guy",
				 * Toast.LENGTH_SHORT).show(); }
				 */
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// try {
			// if (jsonArray == null || jsonArray.length() == 0) {
			singleton.stopLoading(llLoading);
			chatMatchesAdapter.notifyDataSetChanged();
			// }
			//
			// } catch (Exception e) {
			//
			// }
		}
	}

	// class GetChatHistory extends AsyncTask<String, Void, JSONArray> {
	// String vStatus;
	// ProgressDialog dialog;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// rightContainer.setVisibility(View.GONE);
	// singleton.progressLoading(loadingView, llLoading);
	// }
	//
	// protected JSONArray doInBackground(String... args) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	//
	// // edited for revision 609
	// // MAY 20 2015
	// // added if statement only
	// if (!(sessionManager.getUserDetail().getUserID() == null)) {
	// params.add(new BasicNameValuePair("user", ""
	// + sessionManager.getUserDetail().getUserID()));
	// }
	// // - - - - -
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	// Log.d("get user response", "" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	// // --loadCount;
	// // if (loadCount == 0)
	// singleton.stopLoading(llLoading);
	// // send broadcast for model update
	// Intent broadcast = new Intent();
	// broadcast.setAction(AppConstants.INTENT_ACTION_UPDATE_CHAT_HISTORY);
	// sendBroadcast(broadcast);
	//
	// if (WebServiceConstants.isOnline(Activity_Home.this)) {
	// new ApplyChatHistory().execute(jsonArray);
	// } else {
	// Toast.makeText(Activity_Home.this,
	// "Please Check Internet Connection.", Toast.LENGTH_LONG)
	// .show();
	// }
	//
	// }
	// }

	// class ApplyChatHistory extends AsyncTask<JSONArray, Void, String> {
	//
	// @Override
	// protected String doInBackground(JSONArray... args) {
	// // TODO Auto-generated method stub
	// JSONArray jsonArray = args[0];
	// KraveDAO databaseHelper = new KraveDAO(Activity_Home.this);
	// try {
	// // JCv - added clear
	// databaseHelper.clearDataBase();
	// for (int i = 0; i < jsonArray.length(); i++) {
	// JSONObject mJsonObject = jsonArray.getJSONObject(i);
	// ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
	// chatDetailsDTO.setFromuser(mJsonObject
	// .getString("from_user"));
	// chatDetailsDTO.setTouser(mJsonObject.getString("to_user"));
	// chatDetailsDTO.setMessage(mJsonObject.getString("message"));
	// chatDetailsDTO.setMeassageType(mJsonObject
	// .getString("msg_type"));
	// chatDetailsDTO.setTime(mJsonObject.getString("time"));
	// databaseHelper.addChat(chatDetailsDTO);
	// }
	//
	// } catch (JSONException e) {
	//
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(String s) {
	// super.onPostExecute(s);
	// chatMatchesAdapter.notifyDataSetChanged();
	// // Intent broadcast = new Intent();
	// // broadcast.setAction(AppConstants.INTENT_ACTION_UPDATE_CHAT_HISTORY);
	// // sendBroadcast(broadcast);
	// //
	// // rightContainer.setVisibility(View.VISIBLE);
	// // editated by romesh
	// // if (WebServiceConstants.isOnline(Activity_Home.this)) {
	// // new GetChatFriends()
	// // .execute(WebServiceConstants.AV_CHAT_RIGHT_NAV);
	// // } else {
	// // Toast.makeText(Activity_Home.this,
	// // "Please Check Network Connection", Toast.LENGTH_LONG)
	// // .show();
	// // }
	// // editated by romesh
	// }
	// }

	class GetChatHistoryByPagination extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		String userId;

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// user_one=12668&user_two=12903&page=2&limit=5&offset=0
			userId = args[1];
			params.add(new BasicNameValuePair("user_one", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("user_two", args[1]));
			params.add(new BasicNameValuePair("page", "" + 1));
			params.add(new BasicNameValuePair("limit", "1"));
			params.add(new BasicNameValuePair("offset", "0"));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("CHAT HISTORY :", "CHAT HISTORY : userId=" + args[1]
					+ " DATA=" + json);
			try {

				for (int i = 0; i < json.length(); i++) {
					JSONObject mJsonObject = json.getJSONObject(i);
					ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
					chatDetailsDTO.setId("" + 0);
					chatDetailsDTO.setFromuser(mJsonObject
							.getString("from_user"));
					chatDetailsDTO.setTouser(mJsonObject.getString("to_user"));
					chatDetailsDTO.setMessage(mJsonObject.getString("message"));
					chatDetailsDTO.setMeassageType(mJsonObject
							.getString("msg_type"));
					chatDetailsDTO.setTime(Utils
							.convertServerTimeToLocalTime((mJsonObject
									.getString("server_time"))));
					// kraveDAO.addChat(chatDetailsDTO);
					kraveDAO.setLastMsgInfo(chatDetailsDTO);
					// chatList.add(0, chatDetailsDTO);

				}

				// adapter.notifyDataSetChanged();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			// {"conver_reply_id":"24957","message":"koko","from_user":"12903","to_user":"12668",
			// "time":"1436859840901","server_time":"1436880036","msg_type":"
			// <text>","opt_one":"12668","opt_two":"12903","conversation":"10013","read_status":"0"}]

			try {
				// dialog.dismiss();
				// String id = RecentlySearchedcity.searchDudeList.get(
				// RecentlySearchedcity.searchDudeList.size() - 1)
				// .getUserID();
				// if (userId.equals(id)) {
				// singleton.stopLoading(llLoading);
				chatMatchesAdapter.notifyDataSetChanged();
				// }
			} catch (Exception e) {

			}

		}
	}

	// private void parseUserDataAndSetInSession(JSONArray mjsonArray)
	// throws JSONException {
	// JSONObject MainObject;
	// for (int i = 0; i <= mjsonArray.length(); i++) {
	// MainObject = mjsonArray.getJSONObject(i);
	// UserDTO userDTO = new UserDTO();
	// AddressDTO addressDTO = new AddressDTO();
	// WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
	//
	// userDTO.setUserID(MainObject.getString("user_id"));
	// userDTO.setEmail(MainObject.getString("user_email"));
	// userDTO.setFirstName(MainObject.getString("user_fname"));
	// userDTO.setLastName(MainObject.getString("user_lname"));
	// userDTO.setProfileImage(MainObject.getString("user_image"));
	// userDTO.setMobile(MainObject.getString("user_mobile"));
	// userDTO.setAboutMe(MainObject.getString("user_note"));
	// userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
	// userDTO.setAboutMe(MainObject.getString("user_note"));
	//
	// whatAreYouDTO.setFeet(MainObject.getString("user_feet"));
	// whatAreYouDTO.setInches(MainObject.getString("user_inches"));
	// whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
	// whatAreYouDTO.setHight(MainObject.getString("user_height"));
	// whatAreYouDTO.setAge(MainObject.getString("user_age"));
	// whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
	// whatAreYouDTO.setRelationshipStatus(MainObject
	// .getString("user_relationshipStatus"));
	// whatAreYouDTO
	// .setWhatAreYou(MainObject.getString("user_whatAreYou"));
	// whatAreYouDTO.setWhatDoYouKrave(MainObject
	// .getString("user_whatDoYouKrave"));
	//
	// userDTO.setWhatAreYouDTO(whatAreYouDTO);
	// searchDudeList.add(userDTO);
	//
	// }
	// }

	private UserDTO parseUserDataForChatList(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		// AddressDTO addressDTO = new AddressDTO();
		// WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		// List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		// List<UserProfileImageDTO> userProfileImageDTOs = new
		// ArrayList<UserProfileImageDTO>();
		// List<FacebookLikesDTO> facebookLikesDTOs = new
		// ArrayList<FacebookLikesDTO>();
		// LatLongDTO latLongDTO = new LatLongDTO();

		String status = mJsonObject.getString("status");
		String user_id = mJsonObject.getString("user_id");
		String user_fname = mJsonObject.getString("user_fname");
		String image = mJsonObject.getString("image");
		System.out.println("status : " + status + " userid : " + user_id
				+ " fname: " + user_fname + " image: " + image);

		// try {
		// if (singleton.getLastMsg.get(user_id).contains(
		// AppConstants.BASE_IMAGE_PATH_1)) {
		// if (singleton.getLastMsg.get(user_id).contains("|")) {
		// userDTO.setUserLastMsg("VIDEO");
		// } else {
		// userDTO.setUserLastMsg("PHOTO");
		// }
		// } else {
		// userDTO.setUserLastMsg(singleton.getLastMsg.get(user_id));
		// }
		// } catch (Exception e) {
		// }
		// // set last msg time
		// try {
		//
		// userDTO.setUserLastMsgTime(singleton.getLastMsgTime.get(user_id));
		// userDTO.setUserLastMsgFromOrTo(singleton.getLastMsgFromOrToUser
		// .get(user_id));
		//
		// } catch (Exception e) {
		// }
		//
		// try {
		// System.out.println(singleton.getChatCount.get(user_id));
		// if (singleton.getChatCount.get(user_id) != null) {
		// userDTO.setUnreadMsg("" + singleton.getChatCount.get(user_id));
		// } else {
		// userDTO.setUnreadMsg("" + 0);
		// }
		// } catch (Exception e) {
		// userDTO.setUnreadMsg("" + 0);
		// }

		try {
			if (status.equals("available")) {
				userDTO.setIsOnline(AppConstants.ONLINE);

			} else if (status.equals("away")) {
				userDTO.setIsOnline(AppConstants.ABSENT);
			} else {
				userDTO.setIsOnline(AppConstants.OFFLINE);
			}
		} catch (Exception e) {

		}

		userDTO.setUserID(user_id);
		userDTO.setFirstName(user_fname);

		// edited for revision 607
		// MAY 19 2015
		// added if statement
		if (!image.isEmpty()) {
			userDTO.setProfileImage(AppConstants.BASE_IMAGE_PATH_1 + image);
		}
		kraveDAO.getUserChatLastMsg(user_id, sessionManager.getUserDetail()
				.getUserID());
		// - - - - - - - -
		// userDTO.setUserProfileImageDTOs(userProfileImageDTOs)

		return userDTO;

	}

	private UserDTO parseUserData(JSONObject mJsonObject) throws JSONException {
		UserDTO userDTO = new UserDTO();
		// AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		LatLongDTO latLongDTO = new LatLongDTO();
		JSONObject MainObject = mJsonObject.getJSONObject("userinfo");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");

		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		// System.out.println(MainObject);
		userDTO.setLanguage(MainObject.getString("user_language"));
		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
		userDTO.setLoveHookup(MainObject.getString("love_hookup"));
		userDTO.setBodyType(MainObject.getString("body_type"));
		try {
			userDTO.setUnreadMsg(MainObject.getString("unread_msg"));

			if (!MainObject.getString("user_message").equals("null")) {
				if (MainObject.getString("user_message").contains(
						AppConstants.BASE_IMAGE_PATH_1)) {
					userDTO.setUserLastMsg("PHOTO");
				} else {
					userDTO.setUserLastMsg(MainObject.getString("user_message"));

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			if (MainObject.getString("isonline").equals("available")) {
				userDTO.setIsOnline(AppConstants.ONLINE);

			} else if (MainObject.getString("isonline").equals("away")) {
				userDTO.setIsOnline(AppConstants.ABSENT);
			} else {
				userDTO.setIsOnline(AppConstants.OFFLINE);
			}
		} catch (Exception e) {

		}
		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}
		// userDTO.setCommonFriends(MainObject.getString("mutualfriends"));
		whatAreYouDTO.setFeet(MainObject.getString("user_height"));
		whatAreYouDTO.setInches(MainObject.getString("user_inches"));
		whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
		whatAreYouDTO.setHight(MainObject.getString("user_height"));
		whatAreYouDTO.setAge(MainObject.getString("user_age"));
		whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
		whatAreYouDTO.setHeightUnit(MainObject.getString("user_heightUnit"));
		whatAreYouDTO.setWeightUnit(MainObject.getString("user_weightUnit"));
		// whatAreYouDTO.setAge(MainObject.getString("user_note"));
		whatAreYouDTO.setRole(MainObject.getString("role"));
		whatAreYouDTO.setRelationshipStatus(MainObject
				.getString("user_relationshipStatus"));
		whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
		try {
			whatAreYouDTO.setWhatAreYouName(MainObject
					.getString("user_whatAreYou_name"));
		} catch (Exception e) {
			whatAreYouDTO.setWhatAreYouName("ASIAN");
		}
		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrests_id"));
			interestsDTO.setInterestName(interestJsonObject
					.getString("intrests_name"));

			// Edited for revision 607
			// MAY 19 2015
			// added if statement
			if (!interestJsonObject.getString("intrests_image").isEmpty()) {
				interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
						+ interestJsonObject.getString("intrests_image"));
			}
			// - - - - -
			interestsDTOs.add(interestsDTO);

		}

		for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
			JSONObject facebookJsonObject = jsonArrayFacebookLikes
					.getJSONObject(i);
			FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
			facebookLikesDTO.setLikeId(facebookJsonObject
					.getString("intrest_id"));

			facebookLikesDTOs.add(facebookLikesDTO);

		}

		// dl edited on revision 591 -- commented
		// MAY 14 2015
		// for (int i = 0; i < jsonArrayImages.length(); i++) {
		// JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
		// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		// userProfileImageDTO.setImageId(imagesJsonObject
		// .getString("image_id"));
		// userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
		// + imagesJsonObject.getString("image_path"));
		// if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
		// .getString("user_img_status"))) {
		// userProfileImageDTO.setIsImageActive(true);
		// }
		// userProfileImageDTOs.add(userProfileImageDTO);
		//
		// }
		// edited at revision 580
		for (int i = jsonArrayImages.length() - 1; i >= 0; i--) {
			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(imagesJsonObject
					.getString("image_id"));

			// edited for revision 607
			// MAY 19 2015
			// added if statement
			if (!imagesJsonObject.getString("image_path").isEmpty()) {
				userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
						+ imagesJsonObject.getString("image_path"));
			}
			// - - - - -
			userProfileImageDTO.setImagePosition(imagesJsonObject
					.getString("image_position"));

			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}
		for (int k = 0; k < userProfileImageDTOs.size(); k++) {
			UserProfileImageDTO dto = userProfileImageDTOs.get(k);
			if (dto.getIsImageActive()) {
				userProfileImageDTOs.remove(dto);
				userProfileImageDTOs.add(0, dto);
				break;
			}
		}

		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		for (int i = 0; i < jsonArrayLatLong.length(); i++) {
			JSONObject latLongJsonObject = jsonArrayLatLong.getJSONObject(i);
			LatLongDTO mDto = new LatLongDTO();
			mDto.setUser_id(latLongJsonObject.getString("User_id"));
			mDto.setLatitude(latLongJsonObject.getString("latitude"));
			mDto.setLongitude(latLongJsonObject.getString("longitude"));
			userDTO.setStreet(latLongJsonObject.getString("ll_street"));
			userDTO.setCity(latLongJsonObject.getString("ll_city"));
			userDTO.setState(latLongJsonObject.getString("ll_state"));
			userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
			userDTO.setCountry(latLongJsonObject.getString("ll_country"));
			latLongDTO = mDto;

		}

		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		// sessionManager.setUserDetail(userDTO);
		return userDTO;

	}

	private UserDTO parseUserDataForSingleDude(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		LatLongDTO latLongDTO = new LatLongDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
		// System.out.println(MainObject);

		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}
		whatAreYouDTO.setFeet(MainObject.getString("user_feet"));
		whatAreYouDTO.setInches(MainObject.getString("user_inches"));
		whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
		whatAreYouDTO.setHight(MainObject.getString("user_height"));
		whatAreYouDTO.setAge(MainObject.getString("user_age"));
		whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
		whatAreYouDTO.setHeightUnit(MainObject.getString("user_heightUnit"));
		whatAreYouDTO.setWeightUnit(MainObject.getString("user_weightUnit"));
		// whatAreYouDTO.setAge(MainObject.getString("user_note"));
		whatAreYouDTO.setRelationshipStatus(MainObject
				.getString("user_relationshipStatus"));
		whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
		whatAreYouDTO.setWhatAreYouName(MainObject
				.getString("user_whatAreYou_name"));
		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrests_id"));
			interestsDTO.setInterestName(interestJsonObject
					.getString("intrests_name"));

			// edited for revision 607
			// MAY 19 2015
			// added if statement
			if (!interestJsonObject.getString("intrests_image").isEmpty()) {
				interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
						+ interestJsonObject.getString("intrests_image"));
			}
			// - - - - - -

			interestsDTOs.add(interestsDTO);

		}

		// dl edited on revision 591 -- commented
		// MAY 14 2015
		// for (int i = 0; i < jsonArrayImages.length(); i++) {
		// JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
		// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		// userProfileImageDTO.setImageId(imagesJsonObject
		// .getString("image_id"));
		// userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
		// + imagesJsonObject.getString("image_path"));
		// if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
		// .getString("user_img_status"))) {
		// userProfileImageDTO.setIsImageActive(true);
		// }
		// userProfileImageDTOs.add(userProfileImageDTO);
		//
		// }

		// edited at revision 591
		for (int i = jsonArrayImages.length() - 1; i >= 0; i--) {
			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(imagesJsonObject
					.getString("image_id"));

			// edited for revision 607
			// MAY 15 2015
			// added if statement
			if (!imagesJsonObject.getString("image_path").isEmpty()) {
				userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
						+ imagesJsonObject.getString("image_path"));
			}
			// - - - - - --
			userProfileImageDTO.setImagePosition(imagesJsonObject
					.getString("image_position"));

			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}

		for (int k = 0; k < userProfileImageDTOs.size(); k++) {
			UserProfileImageDTO dto = userProfileImageDTOs.get(k);
			if (dto.getIsImageActive()) {
				userProfileImageDTOs.remove(dto);
				userProfileImageDTOs.add(0, dto);
				break;
			}
		}
		for (int i = 0; i < jsonArrayLatLong.length(); i++) {
			JSONObject latLongJsonObject = jsonArrayLatLong.getJSONObject(i);
			LatLongDTO mDto = new LatLongDTO();
			mDto.setUser_id(latLongJsonObject.getString("User_id"));
			mDto.setLatitude(latLongJsonObject.getString("latitude"));
			mDto.setLongitude(latLongJsonObject.getString("longitude"));
			userDTO.setStreet(latLongJsonObject.getString("ll_street"));
			userDTO.setCity(latLongJsonObject.getString("ll_city"));
			userDTO.setState(latLongJsonObject.getString("ll_state"));
			userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
			userDTO.setCountry(latLongJsonObject.getString("ll_country"));
			latLongDTO = mDto;

		}
		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
			JSONObject facebookJsonObject = jsonArrayFacebookLikes
					.getJSONObject(i);
			FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
			facebookLikesDTO.setLikeId(facebookJsonObject
					.getString("intrest_id"));

			facebookLikesDTOs.add(facebookLikesDTO);

		}

		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);

		return userDTO;

	}

	// private UserDTO parseUserData(JSONObject mJsonObject) throws
	// JSONException {
	// UserDTO userDTO = new UserDTO();
	// // AddressDTO addressDTO = new AddressDTO();
	// WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
	// List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	// List<UserProfileImageDTO> userProfileImageDTOs = new
	// ArrayList<UserProfileImageDTO>();
	//
	// JSONObject MainObject = mJsonObject.getJSONObject("userinfo");
	// JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
	// JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
	// // System.out.println(MainObject);
	// userDTO.setLanguage(MainObject.getString("user_language"));
	// userDTO.setUserID(MainObject.getString("user_id"));
	// userDTO.setEmail(MainObject.getString("user_email"));
	// userDTO.setFirstName(MainObject.getString("user_fname"));
	// userDTO.setLastName(MainObject.getString("user_lname"));
	// userDTO.setProfileImage(MainObject.getString("user_image"));
	// userDTO.setMobile(MainObject.getString("user_mobile"));
	// userDTO.setAboutMe(MainObject.getString("user_note"));
	// userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
	//
	// whatAreYouDTO.setFeet(MainObject.getString("user_height"));
	// whatAreYouDTO.setInches(MainObject.getString("user_inches"));
	// whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
	// whatAreYouDTO.setHight(MainObject.getString("user_height"));
	// whatAreYouDTO.setAge(MainObject.getString("user_age"));
	// whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
	// // whatAreYouDTO.setAge(MainObject.getString("user_note"));
	// whatAreYouDTO.setRelationshipStatus(MainObject
	// .getString("user_relationshipStatus"));
	// whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
	// whatAreYouDTO.setWhatDoYouKrave(MainObject
	// .getString("user_whatDoYouKrave"));
	//
	// for (int i = 0; i < jsonArrayInterest.length(); i++) {
	// JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
	// InterestsDTO interestsDTO = new InterestsDTO();
	// interestsDTO.setInterestId(interestJsonObject
	// .getString("intrests_id"));
	// interestsDTOs.add(interestsDTO);
	//
	// }
	// for (int i = 0; i < jsonArrayImages.length(); i++) {
	// JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
	// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
	// userProfileImageDTO.setImageId(imagesJsonObject
	// .getString("image_id"));
	// userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
	// + imagesJsonObject.getString("image_path"));
	//
	// userProfileImageDTOs.add(userProfileImageDTO);
	//
	// }
	// if (!"url".equals(userDTO.getProfileImage())) {
	// Log.d("", "facebook image at first position in list");
	// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
	// userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
	// userProfileImageDTO.setImagePath(userDTO.getProfileImage());
	// userProfileImageDTOs.add(0, userProfileImageDTO);
	// }
	// userDTO.setWhatAreYouDTO(whatAreYouDTO);
	// userDTO.setInterestList(interestsDTOs);
	// userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
	// // sessionManager.setUserDetail(userDTO);
	// return userDTO;
	//
	// }

	private UserDTO parseUserDataforList(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		// AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		LatLongDTO latLongDTO = new LatLongDTO();
		JSONObject MainObject = mJsonObject.getJSONObject("userinfo");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONObject latLongJsonObject = mJsonObject.getJSONObject("latlong");

		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		// System.out.println(MainObject);
		userDTO.setLanguage(MainObject.getString("user_language"));
		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
		try {
			if (MainObject.getString("isonline").equals("available")) {
				userDTO.setIsOnline(AppConstants.ONLINE);

			} else if (MainObject.getString("isonline").equals("away")) {
				userDTO.setIsOnline(AppConstants.ABSENT);
			} else {
				userDTO.setIsOnline(AppConstants.OFFLINE);
			}
		} catch (Exception e) {

		}
		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}
		// userDTO.setCommonFriends(MainObject.getString("mutualfriends"));
		whatAreYouDTO.setFeet(MainObject.getString("user_height"));
		whatAreYouDTO.setInches(MainObject.getString("user_inches"));
		whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
		whatAreYouDTO.setHight(MainObject.getString("user_height"));
		whatAreYouDTO.setAge(MainObject.getString("user_age"));
		whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
		whatAreYouDTO.setHeightUnit(MainObject.getString("user_heightUnit"));
		whatAreYouDTO.setWeightUnit(MainObject.getString("user_weightUnit"));
		// whatAreYouDTO.setAge(MainObject.getString("user_note"));
		whatAreYouDTO.setRelationshipStatus(MainObject
				.getString("user_relationshipStatus"));
		whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));

		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrests_id"));
			interestsDTO.setInterestName(interestJsonObject
					.getString("intrests_name"));

			// edited for revision 607
			// MAY 15 2015
			// added if statement
			if (!interestJsonObject.getString("intrests_image").isEmpty()) {
				interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
						+ interestJsonObject.getString("intrests_image"));
			}
			// - - - - - -
			interestsDTOs.add(interestsDTO);

		}

		for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
			JSONObject facebookJsonObject = jsonArrayFacebookLikes
					.getJSONObject(i);
			FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
			facebookLikesDTO.setLikeId(facebookJsonObject
					.getString("intrest_id"));

			facebookLikesDTOs.add(facebookLikesDTO);

		}

		// dl edited on revision 591 -- commented
		// MAY 14 2015
		// for (int i = 0; i < jsonArrayImages.length(); i++) {
		// JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
		// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		// userProfileImageDTO.setImageId(imagesJsonObject
		// .getString("image_id"));
		// userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
		// + imagesJsonObject.getString("image_path"));
		//
		// if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
		// .getString("user_img_status"))) {
		// userProfileImageDTO.setIsImageActive(true);
		// }
		//
		// userProfileImageDTOs.add(userProfileImageDTO);
		//
		// }

		// edited at revision 591
		for (int i = jsonArrayImages.length() - 1; i >= 0; i--) {
			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(imagesJsonObject
					.getString("image_id"));

			// edited for revision 607
			// MAY 15 2015
			// added if statement
			if (!imagesJsonObject.getString("image_path").isEmpty()) {
				userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
						+ imagesJsonObject.getString("image_path"));
			}
			userProfileImageDTO.setImagePosition(imagesJsonObject
					.getString("image_position"));

			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}

		for (int k = 0; k < userProfileImageDTOs.size(); k++) {
			UserProfileImageDTO dto = userProfileImageDTOs.get(k);
			if (dto.getIsImageActive()) {
				userProfileImageDTOs.remove(dto);
				userProfileImageDTOs.add(0, dto);
				break;
			}
		}
		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		LatLongDTO mDto = new LatLongDTO();
		mDto.setUser_id(latLongJsonObject.getString("User_id"));
		mDto.setLatitude(latLongJsonObject.getString("latitude"));
		mDto.setLongitude(latLongJsonObject.getString("longitude"));
		userDTO.setStreet(latLongJsonObject.getString("ll_street"));
		userDTO.setCity(latLongJsonObject.getString("ll_city"));
		userDTO.setState(latLongJsonObject.getString("ll_state"));
		userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
		userDTO.setCountry(latLongJsonObject.getString("ll_country"));
		latLongDTO = mDto;

		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		// sessionManager.setUserDetail(userDTO);
		return userDTO;

	}

	/* right menu web service */

	public class GetUserList extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// dialog = new TransparentProgressDialog(Activity_Home.this);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user list response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			userListDTOs.clear();
			// [{"list_id":"1","list_title":"abc","user_id":"1"},{"list_id":"3","list_title":"testsdfsdfsdf","user_id":"1"},{"list_id":"4","list_title":"test","user_id":"1"}]
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject listJsonObject = jsonArray.getJSONObject(i);
					UserListDTO dto = new UserListDTO();
					dto.setListId(listJsonObject.getString("list_id"));
					dto.setListName(listJsonObject.getString("list_title"));

					JSONArray userJsonArray = listJsonObject
							.getJSONArray("users");
					List<UserDTO> userDTOs = new ArrayList<UserDTO>();
					for (int j = 0; j < userJsonArray.length(); j++) {
						JSONObject jsonObject = userJsonArray.getJSONObject(j);

						userDTOs.add(parseUserData(jsonObject));
					}

					dto.setDudeList(userDTOs);
					userListDTOs.add(dto);
				}
				userListAdapter.notifyDataSetChanged();
				if (userListDTOs.size() == 0) {
					Toast.makeText(Activity_Home.this, R.string.toast_no_list,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public class GetDudeRequistsList extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(Activity_Home.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			// params.add(new BasicNameValuePair("userid", ""
			// + sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get request dude list response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			requestDudesList.clear();
			// [{"list_id":"1","list_title":"abc","user_id":"1"},{"list_id":"3","list_title":"testsdfsdfsdf","user_id":"1"},{"list_id":"4","list_title":"test","user_id":"1"}]

			try {
				// JSONObject mainObject = jsonArray.getJSONObject(0);
				// JSONArray userJsonArray = mainObject.getJSONArray("users");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject Object = jsonArray.getJSONObject(i);

					requestDudesList.add(parseUserData(Object));
				}

				requistListAdapter.notifyDataSetChanged();
				if (requestDudesList.size() == 0) {
					Toast.makeText(Activity_Home.this, "No request",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	class GetDudeById extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", "" + args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				System.out.print("get dude response : " + jsonArray);
				UserDTO userDTO = null;
				for (int i = 0; i < jsonArray.length(); i++) {
					userDTO = parseUserDataForSingleDude(mJsonObject);
				}
				GCMIntentService.userId = "-1";
				Activity_Push_Notification.userDTO = userDTO;
				Intent intent = new Intent(Activity_Home.this,
						Activity_Push_Notification.class);
				startActivityForResult(intent,
						AppConstants.PUSH_NOTIFICATION_ACTIVITY);
				// openDailogForPushNotification(userDTO);
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(Activity_Home.this,
						R.string.toast_dudes_data_not_found, Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	class GetChatFriendById extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", "" + args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("GetChatFriendByParticularId", "GetChatFriendByParticularId="
					+ json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);

				UserDTO userDTO = null;
				for (int i = 0; i < jsonArray.length(); i++) {
					userDTO = parseUserDataForSingleDude(mJsonObject);
				}
				userDTO.setProfileImage(userDTO.getUserProfileImageDTOs()
						.get(0).getImagePath());
				RecentlySearchedcity.searchDudeList.add(0, userDTO);
				chatMatchesAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(Activity_Home.this,
						R.string.toast_dudes_data_not_found, Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	public void ShowFriendAcceptDialog(UserDTO userDTO) {
		Activity_Friens_Request_Accept.userDTO = userDTO;
		Intent intent = new Intent(this, Activity_Friens_Request_Accept.class);
		startActivityForResult(intent,
				AppConstants.FRIEND_NOTIFICATION_ACTIVITY);
	}

	class InsertActivityLog extends AsyncTask<String, Void, JSONObject> {
		String userID;

		@Override
		protected JSONObject doInBackground(String... args) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			userID = args[2];
			params.add(new BasicNameValuePair("user_id", "" + userID));
			params.add(new BasicNameValuePair("activity", args[1]));

			JSONObject json = new JSONParser().makeHttpRequest2(args[0],
					"POST", params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			if (WebServiceConstants.isOnline(Activity_Home.this)) {
				new UserLogoutAsyncTask().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.LOGOUT, userID);
			} else {
				Toast.makeText(Activity_Home.this,
						R.string.toast_please_check_internet_connection,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	class UserLogoutAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// dialog = new TransparentProgressDialog(Activity_Home.this);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", "" + args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					// stopUserCurrentLocationService();
					singleton.getChatCount.clear();
				} else {
					Toast.makeText(Activity_Home.this,
							R.string.toast_logout_fail_please_try_again,
							Toast.LENGTH_SHORT).show();
				}
				// finish();
				// // connection.disconnect();// chat lagout
				// sessionManager.Logout();
			} catch (JSONException e) {
				e.printStackTrace();

			}

		}

	}

	// user logout
	// success
	// Request not responded
	// Request rejected
	// failure
	// (06:55:02 IST) Govinda(PHP):
	// ------------------------------------------
	// user logout
	// success
	// request already sent
	// failure

	class AcceptPhotoRequest extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;
		String userID;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// loadCount++;
			singleton.progressLoading(loadingView, llLoading);
		}

		protected JSONArray doInBackground(String... args) {
			// http://54.219.211.237/index.php?action=respondRequest&user_id=295&owner_id=13605&image_id=952&response=1
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// selected user's ID
			params.add(new BasicNameValuePair("user_id", args[1]));
			params.add(new BasicNameValuePair("owner_id", args[2]));
			params.add(new BasicNameValuePair("image_id", args[3]));
			params.add(new BasicNameValuePair("response", args[4]));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			Log.d("", "AcceptPhotoRequest=" + jsonArray);
			// dialog.dismiss();
			// --loadCount;
			// if (loadCount == 0)

			singleton.stopLoading(llLoading);
			try {

				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String status = jsonObject.getString("status");
				if (status.equals("success")) {
					Toast.makeText(Activity_Home.this,
							R.string.toast_request_succefully_accepted,
							Toast.LENGTH_SHORT).show();
				} else if (status.equals("Request not responded")) {
					Toast.makeText(Activity_Home.this,
							R.string.toast_request_not_responded,
							Toast.LENGTH_SHORT).show();
				} else if (status.equals("Request rejected")) {
					Toast.makeText(Activity_Home.this,
							R.string.toast_request_rejected, Toast.LENGTH_SHORT)
							.show();
				} else if (status.equals("user logout")) {
					Toast.makeText(Activity_Home.this,
							R.string.toast_user_currently_logout,
							Toast.LENGTH_SHORT).show();
				} else if (status.equals("failure")) {
					Toast.makeText(Activity_Home.this,
							R.string.toast_failed_try_again, Toast.LENGTH_SHORT)
							.show();
				}
				// -- - - - - - -

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class CancelPhotoResponse extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;
		String userID;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// loadCount++;
			singleton.progressLoading(loadingView, llLoading);
		}

		protected JSONArray doInBackground(String... args) {
			// http://54.219.211.237/index.php?action=respondRequest&user_id=295&owner_id=13605&image_id=952&response=1
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// selected user's ID
			params.add(new BasicNameValuePair("notification_id", args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			Log.d("", "CancelPhotoResponse=" + jsonArray);

			singleton.stopLoading(llLoading);
			try {

				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String status = jsonObject.getString("status");
				if (status.equals("success")) {

				}
				// -- - - - - - -

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public void openDailogForDeleteChatHistory() {
		final Dialog dialog = new Dialog(Activity_Home.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_logout);
		// Do you realy want to delete your account ?
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText(R.string.dialog_do_you_really_want_to_delete_chat_history);
		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		title.setTypeface(typeface);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_RIGHT_NAV,
						AppConstants.EVENT_LOG_ACTION_BTN, "Clear All");
				new DeleteChatHistory()
						.execute(WebServiceConstants.DELETE_CHAT_HISTORY_OF_ALL_FRIENDS);

				// KraveDAO dataBaseHelper = new KraveDAO(context);
				// dataBaseHelper.clearDataBase();
				dialog.dismiss();
				// Toast.makeText(context, "Chat history successfully deleted",
				// Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}

	class DeleteChatHistory extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(Activity_Home.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// http://54.219.211.237/index.php?action=clearConversation&user_id=100
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", ""
					+ sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("delete chat HOSTORY response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					KraveDAO dataBaseHelper = new KraveDAO(Activity_Home.this);
					dataBaseHelper.clearDataBase();

					Toast.makeText(Activity_Home.this,
							R.string.toast_chat_history_successfully_deleted,
							Toast.LENGTH_SHORT).show();
					if (RecentlySearchedcity.searchDudeList != null) {
						RecentlySearchedcity.searchDudeList.clear();
					}
					chatMatchesAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(Activity_Home.this,
							R.string.toast_chat_history_not_deleted_try_again,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(Activity_Home.this,
						R.string.toast_chat_history_not_deleted_try_again,
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	class GetAds extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", sessionManager
					.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("Ad mob :", "GetAds=" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			try {

				adDTOs.clear();

				JSONObject jsonObject = jsonArray.getJSONObject(0);

				String status = jsonObject.getString("status");

				// String add_position;
				// String add_type = "googleadd";

				if (status.equals("success")) {
					String add_type = jsonObject.getString("add_type");
					singleton.adsizeAndLocation = jsonObject.getString("view");

					if (add_type.equals("custom_add")) {
						JSONArray adJsonArray = jsonObject
								.getJSONArray("advertisement");
						singleton.isGoogleAd = false;
						for (int i = 0; i < adJsonArray.length(); i++) {
							JSONObject ajsonObject = adJsonArray
									.getJSONObject(i);
							AdDTO adDTO = new AdDTO();
							adDTO.parse(ajsonObject);
							adDTOs.add(adDTO);
						}
						if (adDTOs.size() == 0) {
							adViewFrameLayout.setVisibility(View.GONE);
							return;
						}
					} else {
						singleton.isGoogleAd = true;
					}
					setBannerAdsLocation();
					if (adMob == null) {
						adMob = new AdMob(Activity_Home.this, singleton);

					}
					adMob.startAdvertising(adViewFrameLayout);
					if (ChatService.ONCHAT == AppConstants.FRAGMENT_HOME) {
						((FragmentHome) tempFragment).refreshGridView();
					}
				} else {

				}

			} catch (JSONException e) {

				e.printStackTrace();
				adViewFrameLayout.setVisibility(View.GONE);
			}

		}

	}

	// and here are the other cards that needed your feedback if you already
	// done some research on this.
	// -
	// https://trello.com/c/CclvsLLt/206-research-ad-formats-for-native-and-interstitial-ads-google-admob
	// -
	// https://trello.com/c/6ifddfsU/186-research-start-i-am-a-user-i-get-push-notifications-timed-off-my-sign-up
	// -
	// https://trello.com/c/L2hjtgYJ/145-list-out-parts-which-will-take-the-longest-for-feed
	// - https://trello.com/c/7fwp67OI/195-setup-answers-events
	// - https://trello.com/c/eg0koVYM/153-have-a-full-screen-inboxand
	//
	// here are the other cards that needed your estimates.
	// -
	// https://trello.com/c/rzCYdOxg/15-1-popup-template-screen-for-4-different-uses
	// -
	// https://trello.com/c/zJ7l4TpE/35-i-want-private-albums-i-can-share-with-other-users-through-chat
	// - https://trello.com/c/sE0mkAgd/202-show-if-chat-was-read-if-premium

}