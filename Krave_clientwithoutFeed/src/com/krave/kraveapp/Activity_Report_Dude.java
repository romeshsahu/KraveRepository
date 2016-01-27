package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.krave.kraveapp.Activity_Block_Dude.BlockDudeAsynchTask;
import com.krave.kraveapp.FragmentChatFriend.removeFavoriteAsynch;
import com.navdrawer.SimpleSideDrawer;
import com.ps.adapters.UserListAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.SettingDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class Activity_Report_Dude extends Activity implements OnClickListener {
	private static Activity_Report_Dude activityObject = null;

	public static Activity_Report_Dude getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_Report_Dude activityObject) {
		Activity_Report_Dude.activityObject = activityObject;
	}

	/* resources */
	private SessionManager sessionManager;

	/* Layout Views */

	private TextView guyName;

	private TextView reportButton, cancleButton;
	private RelativeLayout otherOption;
	private LinearLayout spamLayout, inappropriateLayout, editTextLayout;
	private ImageView arrow, checkmark0, checkmark1, checkmark2;
	private EditText reportField;
	private String reason = "";
	private AppManager singleton;
	/* FLAG ENUM VALUES */
	private int FLAG_TRUE = -1,
				FLAG_NULL = 0,
				FLAG_BLOCK = 1,
				FLAG_REPORT = 2,
				FLAG_CANCEL = 3;
	/* dto variables initialization */

	private UserDTO userDTO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_report_dude);
		sessionManager = new SessionManager(Activity_Report_Dude.this);
		activityObject = this;
		
		/* av singleton */
		AppManager.initInstance();
       	singleton = AppManager.getInstance();
       	
		setLayout();
		setListeners();
		setTypeFace();
		loadData();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;
	}

	private void setTypeFace() {
		TextView textView4, textView5, textView6, textView7;

		textView4 = (TextView) findViewById(R.id.textView4);
		textView5 = (TextView) findViewById(R.id.textView5);
		textView6 = (TextView) findViewById(R.id.textView6);
		textView7 = (TextView) findViewById(R.id.textView7);

		Typeface typeface = FontStyle.getFont(Activity_Report_Dude.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		cancleButton.setTypeface(typeface);
		reportButton.setTypeface(typeface);
		guyName.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		textView7.setTypeface(typeface);

	}

	private void loadData() {
		userDTO = FragmentChatMatches.userDTO;
		guyName.setText(userDTO.getFirstName() + " " + userDTO.getLastName());
	}

	private void setLayout() {
		reportButton = (TextView) findViewById(R.id.report);
		reportButton.setEnabled(false);
		cancleButton = (TextView) findViewById(R.id.cancle);
		guyName = (TextView) findViewById(R.id.guyName);
		spamLayout = (LinearLayout) findViewById(R.id.spamLayout);
		inappropriateLayout = (LinearLayout) findViewById(R.id.inappropriateLayout);
		otherOption = (RelativeLayout) findViewById(R.id.other);
		editTextLayout = (LinearLayout) findViewById(R.id.editTextLayout);
		arrow = (ImageView) findViewById(R.id.arrow);
		reportField = (EditText) findViewById(R.id.otherEditText);
		
		checkmark0 = (ImageView) findViewById(R.id.checkmark0);
		checkmark1 = (ImageView) findViewById(R.id.checkmark1);
		checkmark2 = (ImageView) findViewById(R.id.checkmark2);
	}

	private void setListeners() {

		reportButton.setOnClickListener(this);
		cancleButton.setOnClickListener(this);
		spamLayout.setOnClickListener(this);
		inappropriateLayout.setOnClickListener(this);
		otherOption.setOnClickListener(this);
		editTextLayout.setOnClickListener(this);
	}
	
	public void easyTrackerEventLog(String Categ, String Action, String Label){
		EasyTracker.getInstance(this).set(Fields.EVENT_CATEGORY, Categ);
		EasyTracker.getInstance(this).set(Fields.EVENT_ACTION, Action);
		EasyTracker.getInstance(this).set(Fields.EVENT_LABEL, Label);
		EasyTracker.getInstance(this).send(MapBuilder.createAppView().build());
	}
	
	public void checkMark(ImageView checkmark){
		if(checkmark2.getVisibility() == View.VISIBLE){
			editTextLayout.setVisibility(View.GONE);
			reportButton.setEnabled(false);
		}
		checkmark0.setVisibility(View.INVISIBLE);
		checkmark1.setVisibility(View.INVISIBLE);
		checkmark2.setVisibility(View.INVISIBLE);
		
		checkmark.setVisibility(View.VISIBLE);
		reportButton.setEnabled(true);
		reportButton.setTextColor(getResources()
				.getColor(R.color.white));
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			finish();
			break;

		case R.id.report:
			if (checkmark0.getVisibility() == View.VISIBLE){ //REPORT AS SPAM
				if (WebServiceConstants.isOnline(Activity_Report_Dude.this)) {
					easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_REPORT, "ReportUser", "Spam");
			       	if(singleton.isFromFlagged == FLAG_TRUE)
			       	{
			       		singleton.isFromFlagged = FLAG_REPORT;
			       	}
					reason = "spam";
	//				new BlockDudeAsynchTask().execute(WebServiceConstants.BASE_URL
	//						+ WebServiceConstants.BLOCK_DUDE);
					/* this uses kosa's API implementation for blocking */
					new BlockDudeAsynchTask().execute("http://54.219.211.237/KraveAPI/api_calls/block-user-account.php");
			
				}else{
					Toast.makeText(Activity_Report_Dude.this, R.string.toast_please_check_internet_connection, Toast.LENGTH_SHORT).show();
				}
			}else if (checkmark1.getVisibility() == View.VISIBLE){ //REPORT AS OFFENSIVE
				if (WebServiceConstants.isOnline(Activity_Report_Dude.this)) {
					easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_REPORT, "ReportUser", "Inappropriate/Offensive");
					
			       	if(singleton.isFromFlagged == FLAG_TRUE)
			       	{
			       		singleton.isFromFlagged = FLAG_REPORT;
			       	}
					reason = "inappropriate";
	//				new BlockDudeAsynchTask().execute(WebServiceConstants.BASE_URL
	//						+ WebServiceConstants.BLOCK_DUDE);
					/* this uses kosa's API implementation for blocking */
					new BlockDudeAsynchTask().execute("http://54.219.211.237/KraveAPI/api_calls/block-user-account.php");
			
				}else{
					Toast.makeText(Activity_Report_Dude.this, R.string.toast_please_check_internet_connection, Toast.LENGTH_SHORT).show();
				}
				
			}else if (reportField.getText().toString().trim().length() != 0 && checkmark2.getVisibility() == View.VISIBLE) { //REPORT AS "OTHERS"
				
				if (WebServiceConstants.isOnline(Activity_Report_Dude.this)) {
					easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_REPORT, "ReportUser", "Other");
			       	if(singleton.isFromFlagged == FLAG_TRUE)
			       	{
			       		singleton.isFromFlagged = FLAG_REPORT;
			       	}
					reason = reportField.getText().toString();
//					new BlockDudeAsynchTask()
//							.execute(WebServiceConstants.BASE_URL
//									+ WebServiceConstants.BLOCK_DUDE);
					/* this uses kosa's API implementation for blocking */
					new BlockDudeAsynchTask().execute("http://54.219.211.237/KraveAPI/api_calls/block-user-account.php");
					new removeFavoriteAsynch()
					.execute("http://54.219.211.237/KraveAPI/api_calls/remove-user-to-favorite.php");
				}else{
					Toast.makeText(Activity_Report_Dude.this, R.string.toast_please_check_internet_connection, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(Activity_Report_Dude.this,
						 R.string.toast_enter_some_text_in_report_field, Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.spamLayout:
			checkMark(checkmark0);
			break;
		case R.id.inappropriateLayout:
			checkMark(checkmark1);
			break;
		case R.id.other:
			checkMark(checkmark2);
			if (View.VISIBLE == editTextLayout.getVisibility()) {
				editTextLayout.setVisibility(View.GONE);
				reportButton.setEnabled(false);

//				reportButton
//						.setTextColor(getResources().getColor(R.color.gray));
			} else {
				editTextLayout.setVisibility(View.VISIBLE);
				editTextLayout.requestFocus();
				reportButton.setEnabled(true);
//				reportButton.setTextColor(getResources()
//						.getColor(R.color.white));
			}
			break;
		case R.id.arrow:

			break;

		default:
			break;
		}

	}
	class removeFavoriteAsynch extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			dialog = new TransparentProgressDialog(context);
//			// dialog.setMessage("Please Wait...");
//			dialog.setCanceledOnTouchOutside(false);
//			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			System.out.println("USERID: "+FragmentChatMatches.userDTO.getUserID()); //show clicked user ID
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("favorite_id", userDTO.getUserID()));//FragmentChatMatches.userDTO.getUserID()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get Like response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
//			dialog.dismiss();
		}

	}
	class BlockDudeAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(Activity_Report_Dude.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			// params.add(new BasicNameValuePair("userId", sessionManager
			// .getUserDetail().getUserID()));
			// params.add(new BasicNameValuePair("dudeId",
			// userDTO.getUserID()));
			// params.add(new BasicNameValuePair("user_email",
			// userDTO.getEmail()));
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("block_id", userDTO.getUserID()));
			params.add(new BasicNameValuePair("reason", "" + reason));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get Like response", "" + json);
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
					Toast.makeText(Activity_Report_Dude.this,
							 R.string.toast_report_successfully_send, Toast.LENGTH_SHORT)
							.show();
					setResult(RESULT_OK);
					finish();
				} else if (vStatus.equals("failure")) {
					Toast.makeText(Activity_Report_Dude.this,  R.string.toast_fail,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if(singleton.isFromFlagged == FLAG_REPORT)
			{
				finish();
			}
			
		}
	}

}
