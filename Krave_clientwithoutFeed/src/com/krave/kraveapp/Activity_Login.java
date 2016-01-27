package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.model.GraphUser;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gcm.GCMRegistrar;
import com.krave.kraveapp.Activity_Home.InsertActivityLog;
import com.krave.kraveapp.Activity_Home.UserLogoutAsyncTask;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.SettingDTO;
import com.ps.models.TransitDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.FacebookIntegration;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveRestClient;
import com.ps.utill.OnfacebookDone;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;
import com.tag.trivialdrivesample.util.InAppPurchaseActivity;

public class Activity_Login extends InAppPurchaseActivity implements
		OnClickListener {
	// private String gcmRegId;
	private ImageButton loginButton;
	private TextView facebookButton;
	private Button signUp;
	private TextView forgotPassword, forgotEmail;
	private EditText userName, password;
	private SessionManager sessionManager;
	private JSONParser jsonParser = new JSONParser();
	private GraphUser graphUser;
	FacebookIntegration facebookIntegration;
	private ImageView rememberMe;
	private TextView rememberMeTextView, notAUserTextView;
	private boolean isRememberMe = true;
	private GPSTracker gpsTracker;
	private TextView tvVersionName;

	AppManager singleton;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);

		sessionManager = new SessionManager(Activity_Login.this);
		gpsTracker = new GPSTracker(Activity_Login.this);
		setLayout();
		setListener();
		setTypeFace();
		// isRememberMe = sessionManager.isRemember();
		if (isRememberMe) {
			userName.setText(sessionManager.getEmail());
			// password.setText(sessionManager.getPassword());
			rememberMe.setImageResource(R.drawable.sw_remember_checked_new);
		} else {
			rememberMe.setImageResource(R.drawable.sw_remember_un_checked_new);
		}
		/* av singleton */
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		singleton.mTransitPrefs = getSharedPreferences(AppConstants.GPS_PREFS,
				0);
		singleton.mLanguagePrefs = getSharedPreferences(
				AppConstants.LANGUAGE_PREFERENCE, 0);
		singleton.mFilterPrefs = getSharedPreferences(
				AppConstants.FILTER_PREFS, 0);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// EasyTracker.getInstance(this).activityStart(this);
		EasyTracker.getInstance(this).set(Fields.SCREEN_NAME, "Login Screen");
		EasyTracker.getInstance(this).send(MapBuilder.createAppView().build());
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	private void setListener() {
		loginButton.setOnClickListener(this);
		facebookButton.setOnClickListener(this);
		signUp.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		forgotEmail.setOnClickListener(this);
		rememberMe.setOnClickListener(this);
		notAUserTextView.setOnClickListener(this);

	}

	private void setLayout() {

		userName = (EditText) findViewById(R.id.userNameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		loginButton = (ImageButton) findViewById(R.id.signInButton);
		facebookButton = (TextView) findViewById(R.id.connectWithFbButton);
		signUp = (Button) findViewById(R.id.registration);
		forgotPassword = (TextView) findViewById(R.id.forgotPassword);
		forgotEmail = (TextView) findViewById(R.id.forgotEmail);
		rememberMe = (ImageView) findViewById(R.id.rememberMe);
		rememberMeTextView = (TextView) findViewById(R.id.rememberMeTextView);
		notAUserTextView = (TextView) findViewById(R.id.notAUserButton);
		// userName.setText("sahu.romesh670@gmail.com");
		// password.setText("123456");
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
	}

	private void setTypeFace() {

		Typeface typeface = FontStyle.getFont(Activity_Login.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		userName.setTypeface(typeface);
		password.setTypeface(typeface);
		Typeface typeface1 = FontStyle.getFont(Activity_Login.this,
				AppConstants.HelveticaNeueLTStd_Md);
		// signUp.setTypeface(typeface1);
		forgotPassword.setTypeface(typeface1);
		forgotEmail.setTypeface(typeface1);
		rememberMeTextView.setTypeface(typeface1);
		notAUserTextView.setTypeface(typeface1);
		Typeface typeface2 = FontStyle.getFont(Activity_Login.this,
				AppConstants.HelveticaNeueLTStd_Bd);
		facebookButton.setTypeface(typeface2);
		tvVersionName.setTypeface(typeface);
	}

	// private void registerClient() {
	//
	// try {
	// GCMRegistrar.checkDevice(Activity_Login.this);
	// GCMRegistrar.checkManifest(Activity_Login.this);
	//
	// registerReceiver(mHandleMessageReceiver, new IntentFilter(
	// CommonUtilities.DISPLAY_MESSAGE_ACTION));
	//
	// gcmRegId = GCMRegistrar.getRegistrationId(Activity_Login.this);
	// Log.v("REG ID", gcmRegId);
	//
	// if (gcmRegId.equals("")) {
	// GCMRegistrar.register(Activity_Login.this,
	// CommonUtilities.SENDER_ID);
	// Log.v("REG ID IN IF", gcmRegId);
	// } else {
	// if (GCMRegistrar.isRegisteredOnServer(Activity_Login.this)) {
	// // regId = GCMRegistrar.getRegistrationId(this);
	// System.out.println("GCM Register on server" + gcmRegId);
	// } else {
	// GCMRegistrar
	// .setRegisteredOnServer(Activity_Login.this, true);
	// }
	// }
	//
	// //GpsService.gcmId = gcmRegId;
	// Log.v("", "Already registered:  " + gcmRegId);
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

	// registration,login,map,profile,daily krave,setting,right menu
	//
	// Home:- 1)only online dudes underworking
	// 2)friends in common underworking
	// 3)distance away under working
	// 4)shared interest under working
	// 5) congratulation (you have match) send him a message button under
	// working
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// try {
		// unregisterReceiver(mHandleMessageReceiver);
		// GCMRegistrar.onDestroy(this);
		// } catch (Exception e) {
		//
		// Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		// }
	}

	@Override
	public void onClick(View arg0) {
		if (WebServiceConstants.isOnline(Activity_Login.this)) {

			switch (arg0.getId()) {
			case R.id.notAUserButton:
				Intent intent = new Intent(Activity_Login.this,
						Activity_Splash.class);
				Activity_Splash.isFromLogin = true;
				startActivity(intent);
				finish();
				break;
			case R.id.signInButton:
				if (0 == userName.getText().toString().trim().length()) {
					Toast.makeText(Activity_Login.this,
							R.string.toast_enter_email_id, Toast.LENGTH_SHORT)
							.show();
				} else if (0 == password.getText().toString().trim().length()) {
					Toast.makeText(Activity_Login.this,
							R.string.toast_enter_password, Toast.LENGTH_SHORT)
							.show();
				} else {
					// registerClient();

					new LoginAsynchTask().execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.USER_LOGIN);

				}

				break;
			case R.id.connectWithFbButton:
				facebookIntegration = new FacebookIntegration(
						Activity_Login.this, AppConstants.COME_FROM_LOGIN,
						new OnfacebookDone() {

							@Override
							public void onFbcompleate() {
								// TODO Auto-generated method stub
								System.out.println("fb");
							}
						});
				facebookIntegration.facebookIntegration();
				// graphUser = facebookIntegration.getGraphUser();

				break;
			case R.id.registration:

				Intent intent2 = new Intent(Activity_Login.this,
						Activity_ProfileDetails.class);
				intent2.putExtra(AppConstants.COME_FROM,
						AppConstants.COME_FROM_NORMAL_REGISTRATION);
				startActivity(intent2);
				break;
			case R.id.forgotPassword:
				Intent intent3 = new Intent(Activity_Login.this,
						Activity_ForgotPassword.class);
				startActivity(intent3);
				break;
			case R.id.forgotEmail:
				Intent intent4 = new Intent(Activity_Login.this,
						Activity_ForgotEmail.class);
				startActivity(intent4);
				break;
			case R.id.rememberMe:
				if (isRememberMe) {
					rememberMe
							.setImageResource(R.drawable.sw_remember_un_checked_new);
					isRememberMe = false;
				} else {
					rememberMe
							.setImageResource(R.drawable.sw_remember_checked_new);
					isRememberMe = true;
				}
				break;
			default:
				break;
			}
		}

	}

	private void updateUserLogin() {
		// edition on revision 583
		SessionManager sessionManager = new SessionManager(
				MyApps.getAppContext());

		RequestParams params = new RequestParams();
		params.add("user_id", sessionManager.getUserDetail().getUserID());

		Log.d("EL", "userID: " + sessionManager.getUserDetail().getUserID());

		KraveRestClient.post(WebServiceConstants.AV_UPDATE_USER_PROFILE_DATA_1,
				params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						super.onSuccess(statusCode, headers, response);

						try {
							Log.d("EL", "response: " + response.toString(2));
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}
				});
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

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			facebookIntegration.onActivityResultForFacebook(requestCode,
					resultCode, data);
		} catch (Exception e) {

		}
	}

	class LoginAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// dialog = new TransparentProgressDialog(Activity_Login.this);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// //
			// devicetype
			// GCMID
			// http://parkhya.org/Android/krave_app/index.php?action=user_login&email=test2@gmail.com&password=123456
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));

			params.add(new BasicNameValuePair("lat", ""
					+ gpsTracker.getLatitude()));
			params.add(new BasicNameValuePair("log", ""
					+ gpsTracker.getLongitude()));
			params.add(new BasicNameValuePair("email", userName.getText()
					.toString()));
			params.add(new BasicNameValuePair("password", password.getText()
					.toString()));
			params.add(new BasicNameValuePair("GCMID", singleton.gcmRegId));
			params.add(new BasicNameValuePair("devicetype", "android"));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("login params", "params=" + params.toString());
			Log.d("login response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("login response :" + jsonArray);
				if (vStatus.equals("success")) {

					parseUserDataAndSetInSession(mJsonObject);
					new InsertActivityLog()
							.execute(
									WebServiceConstants.AV_BASE_URL
											+ WebServiceConstants.AV_INSERT_ACTIVITY_LOG,
									"Login", ""
											+ sessionManager.getUserDetail()
													.getUserID());
				} else if (vStatus.equals("unverified")) {
					Toast.makeText(
							Activity_Login.this,
							R.string.toast_your_email_id_is_not_verified_please_verify_email_id,
							Toast.LENGTH_SHORT).show();
				} else if (vStatus.equals("failure")) {
					Toast.makeText(
							Activity_Login.this,
							R.string.toast_email_and_or_password_is_not_correct,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			// DFv - May 12, 2015
			// Call update user login after login API call
			updateUserLogin();

		}
	}

	// [{"status":"success",
	// "user":{"user_id":"48","user_email":"ankit@gmail.com","user_password":"202cb962ac59075b964b07152d234b70",
	// "user_fname":"ankit","user_lname":"ankit","user_image":null,"user_mobile":"908242000","user_address":"",
	// "user_dob":"0000-00-00","user_feet":"1","user_inches":"","user_meters":"","user_age":"1","user_weight":"1",
	// "user_height":"","user_note":"","user_relationshipStatus":"1","user_whatAreYou":"","user_whatDoYouKrave":"",
	// "isFirstTime":"0","user_type":"","user_status":"1","user_reg_date":"2014-05-22 11:58:16"},
	// "intrest":[{"ui_id":"40","intrest_id":"1","user_id":"48","ui_type":"admin"},
	// {"ui_id":"41","intrest_id":"2","user_id":"48","ui_type":"admin"},
	// {"ui_id":"42","intrest_id":"3","user_id":"48","ui_type":"admin"},
	// {"ui_id":"43","intrest_id":"4","user_id":"48","ui_type":"admin"},
	// {"ui_id":"44","intrest_id":"5","user_id":"48","ui_type":"admin"},
	// {"ui_id":"45","intrest_id":"6","user_id":"48","ui_type":"admin"},
	// {"ui_id":"46","intrest_id":"7","user_id":"48","ui_type":"admin"},
	// {"ui_id":"47","intrest_id":"8","user_id":"48","ui_type":"admin"}],
	// "images":[{"image_id":"33","User_id":"48","image_name":null,"image_path":"krave_image/14007400960_test.png"},
	// {"image_id":"34","User_id":"48","image_name":null,"image_path":"krave_image/14007400961_test.png"},
	// {"image_id":"35","User_id":"48","image_name":null,"image_path":"krave_image/14007400962_test.png"},
	// {"image_id":"36","User_id":"48","image_name":null,"image_path":"krave_image/14007400963_test.png"},
	// {"image_id":"37","User_id":"48","image_name":null,"image_path":"krave_image/14007400964_test.png"},
	// {"image_id":"38","User_id":"48","image_name":null,"image_path":"krave_image/14007400965_test.png"}]}]

	private void parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		System.out.println(mJsonObject);

		/*
		 * 
		 * {"status":"success", "user": {"user_id":"3140",
		 * "user_email":"kennethdeloso@yahoo.com",
		 * "user_password":"a152e841783914146e4bcd4f39100686",
		 * "user_fname":"supremo68", "user_lname":"", "user_image":"url",
		 * "user_mobile":"", "user_address":"", "user_dob":"0000-00-00",
		 * "user_feet":"", "user_inches":"", "user_meters":"",
		 * "user_age":"01\/11\/1992", "user_weight":"127",
		 * "user_weightUnit":"US", "user_height":"3\/3", "user_heightUnit":"US",
		 * "user_note":"I am supreme\n\nI am the one\n", "user_language":"0",
		 * "role":"1", "body_type":"1", "love_hookup":"",
		 * "user_relationshipStatus":"1", "user_whatAreYou":"3",
		 * "user_whatDoYouKrave":"3", "isFirstTime":"0", "user_type":"",
		 * "user_facebook_Interest":"No", "user_status":"1",
		 * "user_reg_date":"2015-03-11 13:02:08",
		 * "user_whatAreYou_name":"Asian", "notification_status":"1" },
		 * "setting": [ {"s_no":"3140", "user_id":"3140", "min_value":"18",
		 * "max_value":"75", "radius":"300", "interest":"3,6,12,",
		 * "ethencity":"2,", "transit":"1", "role":"1", "body_type":"1",
		 * "love_hookup":"1,2", "notification":"1" } ],
		 * "images":[{"image_id":"4457"
		 * ,"User_id":"3140","image_name":null,"image_path":
		 * "krave_image\/14144703582_.png","image_position":"2","user_date":"2014-10-28","user_img_status":"1"},{"image_id":"13290","User_id":"3140","image_name":null,"image_path":"krave_image\/142486458601209646590_.png","image_position":"0","user_date":"2015-02-25","user_img_status":"0"}],
		 * "intrest"
		 * :[{"ui_id":"85094","intrest_id":"12","user_id":"3140","ui_type"
		 * :"admin"
		 * },{"ui_id":"85093","intrest_id":"8","user_id":"3140","ui_type"
		 * :"admin"
		 * },{"ui_id":"85092","intrest_id":"5","user_id":"3140","ui_type"
		 * :"admin"
		 * },{"ui_id":"85091","intrest_id":"2","user_id":"3140","ui_type"
		 * :"admin"
		 * },{"ui_id":"85090","intrest_id":"1","user_id":"3140","ui_type"
		 * :"admin"}],"fbintrast":[]}
		 */

		UserDTO userDTO = new UserDTO();
		AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		SettingDTO settingDTO = new SettingDTO();

		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
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
		System.out.println("F : " + MainObject.getString("love_hookup"));
		userDTO.setBodyType(MainObject.getString("body_type"));

		settingDTO.setIsNotificationEnable(MainObject
				.getString("notification_status"));
		TransitDTO transitDTO = new TransitDTO();
		transitDTO.setTransitId(mJsonObject.getJSONArray("setting")
				.getJSONObject(0).getString("transit"));
		settingDTO.setTransitDTO(transitDTO);
		settingDTO.setChatHistoryPeriod(mJsonObject.getJSONArray("setting")
				.getJSONObject(0).getString("chat_history"));
		settingDTO.setFindDudesColumnCoun(mJsonObject.getJSONArray("setting")
				.getJSONObject(0).getString("find_guy"));

		Editor e = singleton.mTransitPrefs.edit();
		e.putInt(AppConstants.TRANSIT_PREFERENCE,
				Integer.parseInt(settingDTO.getTransitDTO().getTransitId()));
		e.commit();
		Editor eL = singleton.mLanguagePrefs.edit();
		eL.putInt(AppConstants.LANGUAGE_PREFERENCE,
				Integer.parseInt(settingDTO.getLanguage()));
		eL.commit();

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

		whatAreYouDTO.setRole(MainObject.getString("role"));
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
					.getString("intrest_id"));
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
		// MAY 14
		// for (int i = 0; i < jsonArrayImages.length(); i++) {
		// JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
		// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		// userProfileImageDTO.setImageId(imagesJsonObject
		// .getString("image_id"));
		// userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
		// + imagesJsonObject.getString("image_path"));
		// userProfileImageDTO.setImagePosition(imagesJsonObject
		// .getString("image_position"));
		// if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
		// .getString("user_img_status"))) {
		// userProfileImageDTO.setIsImageActive(true);
		// }
		//
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
			// MAY 15 2015
			// added if statement
			if (!imagesJsonObject.getString("image_path").isEmpty()) {
				userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
						+ imagesJsonObject.getString("image_path"));
			}
			// - - --
			userProfileImageDTO.setImagePosition(imagesJsonObject
					.getString("image_position"));
			boolean isPublic = (imagesJsonObject.getString("is_public").equals(
					AppConstants.IMAGE_PUBLIC) ? true : false);
			userProfileImageDTO.setPublic(isPublic);

			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}
		/* set facebook image at first position in list */

		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}
		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		sessionManager.setUserDetail(userDTO);
		sessionManager.setSettingDetail(settingDTO);
		Intent mIntent = new Intent(Activity_Login.this, Activity_Home.class);
		mIntent.putExtra("open", AppConstants.FRAGMENT_HOME);
		mIntent.putExtra("loadchatHistory", true);
		startActivity(mIntent);

		if (isRememberMe) {
			sessionManager.setRememberMe(userName.getText().toString(),
					password.getText().toString());
		} else {
			sessionManager.clearRememberMe();
		}

		Editor editor = singleton.mFilterPrefs.edit();
		editor.putBoolean(AppConstants.FILTER_PREFS_AGE, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_RADIUS, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_ETHNICITY, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_ROLE, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_INTEREST, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_BODY_TYPE, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_LOOKING_FOR, false);
		editor.commit();

		finish();
	}

}