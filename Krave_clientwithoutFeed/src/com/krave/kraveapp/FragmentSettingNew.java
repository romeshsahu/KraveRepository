package com.krave.kraveapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ps.adapters.ChatHistoryAdapter;
import com.ps.adapters.TransitAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.ChatHistoryDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.SettingDTO;
import com.ps.models.TransitDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.Utils;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentSettingNew extends Fragment implements OnClickListener {
	SettingDTO settingDTO;

	private LinearLayout linearHeaderNotifications, linearBodyNotifications,
			linearHeaderUpdateInfo, linearBodyUpdateInfo,
			linearHeaderChangePass, linearBodyChangePass,
			linearHeaderDeleteAccount, linearBodyDeleteAccount,
			linearHeaderShowAge, linearBodyShowAge, linearHeaderFindDudeColumn,
			linearBodyFindDudeColumn, linearHeaderLanguage, linearBodyLanguage,
			linearHeaderTransit, linearBodyTransit, linearHeaderChatHistory,
			linearBodyChatHistory, linearHeaderSave;
	private Button btnSaveUpdateInfo, btnSaveChangePass, btnDeleteAccount;
	private ImageView ivNotification, ivShowAgeWeightHeight;
	private EditText etUserName, etLastName, etMobile, etEmail, etOldPassword,
			etNewPassword, etConfirmPassword;

	private GridView gridTransit;
	private ArrayList<TransitDTO> arrTransitDTO;
	private TransitAdapter gridViewAdapter;
	private String selectedTransitId;

	private GridView gridChatHistory;
	private ArrayList<ChatHistoryDTO> arrChatHistoryDTO;
	private ChatHistoryAdapter chatHistoryAdapter;
	private String selectedChatHistoryColumnId;

	private GridView gridFindDudesColumn;
	private ArrayList<TransitDTO> arrFindDudesColumn;
	private TransitAdapter findDudesColumnAdapter;
	private String selectedFindDudesColumnId;

	private GridView gridLanguage;
	private ArrayList<TransitDTO> arrLanguage;
	private TransitAdapter languageAdapter;
	private String selectedLanguageId;

	private ImageView loadingView;
	private LinearLayout llLoading;

	private ScrollView scroll;

	SessionManager sessionManager;
	Activity_Home context;
	AppManager singleton;
	UserDTO userDTO;
	boolean isProcessing = false;
	private Boolean isGPSReminderOn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings_new, container,
				false);
		System.gc();
		context = (Activity_Home) getActivity();
		sessionManager = new SessionManager(context);

		userDTO = sessionManager.getUserDetail();
		settingDTO = sessionManager.getSettingDetail();
		AppManager.initInstance();
		singleton = AppManager.getInstance();

		setLayout(view);
		setListener();
		setTypeFace();
		setGPSReminders();
		setShowAgeWeightHeight();

		this.initializeChatHistory(view);
		this.initializeTransit(view);
		this.initializeFindDudesColumnSetting(view);
		this.initializeLanguageSetting(view);

		return view;
	}

	private void initializeChatHistory(View view) {
		arrChatHistoryDTO = new ArrayList<ChatHistoryDTO>();
		String arr1[] = { "0", "1", "2" };
		String arr2[] = { getResources().getString(R.string.setting_24_hours),
				getResources().getString(R.string.setting_7_days),
				getResources().getString(R.string.setting_forever) };

		// selectedChatHistoryColumnId = settingDTO.getChatHistoryPeriod();
		selectedChatHistoryColumnId = "2";
		int n = Integer.parseInt(selectedChatHistoryColumnId);

		Log.d("", "selectedChatHistoryColumnId=" + selectedChatHistoryColumnId);
		for (int i = 0; i < arr1.length; i++) {
			ChatHistoryDTO dto = new ChatHistoryDTO();
			dto.setIsSelected(n == i ? true : false);
			dto.setChatHistoryId(arr1[i]);
			dto.setChatHistoryName(arr2[i]);
			arrChatHistoryDTO.add(dto);
		}
		arrChatHistoryDTO.remove(0);
		arrChatHistoryDTO.remove(0);
		chatHistoryAdapter = new ChatHistoryAdapter(context, arrChatHistoryDTO,
				1);

		gridChatHistory = (GridView) view
				.findViewById(R.id.settings_chathistpry);

		gridChatHistory.setAdapter(chatHistoryAdapter);

		gridChatHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				ChatHistoryDTO chatHistoryDTO = arrChatHistoryDTO.get(position);

				for (ChatHistoryDTO ch : arrChatHistoryDTO) {
					ch.setIsSelected(false);
				}

				chatHistoryDTO.setIsSelected(true);
				selectedChatHistoryColumnId = chatHistoryDTO.getChatHistoryId();
				chatHistoryAdapter.notifyDataSetChanged();
				// gridViewAdapter = new GridViewAdapter(context,
				// (ArrayList<InterestsDTO>) interestsDTOs, 1);
				// horizontalListView.setAdapter(gridViewAdapter);

				// logFlurryEvent(settingsParams, "Filter Interest"); //Edited
				// revision 582 commented
			}
		});
	}

	private void initializeFindDudesColumnSetting(View view) {
		arrFindDudesColumn = new ArrayList<TransitDTO>();
		String arr1[] = { "0", "1" };
		String arr2[] = {
				getResources().getString(R.string.setting_two_column),
				getResources().getString(R.string.setting_three_column) };

		selectedFindDudesColumnId = settingDTO.getFindDudesColumnCoun();
		int n = Integer.parseInt(selectedFindDudesColumnId);
		Log.d("", "selectedFindDudesColumnId=" + selectedFindDudesColumnId);
		for (int i = 0; i < arr1.length; i++) {
			TransitDTO dto = new TransitDTO();
			dto.setIsSelected(n == i ? true : false);
			dto.setTransitId(arr1[i]);
			dto.setTransitName(arr2[i]);
			arrFindDudesColumn.add(dto);
		}

		findDudesColumnAdapter = new TransitAdapter(context,
				arrFindDudesColumn, 1);

		gridFindDudesColumn = (GridView) view
				.findViewById(R.id.settings_FindDudesColumn);

		gridFindDudesColumn.setAdapter(findDudesColumnAdapter);

		gridFindDudesColumn.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				TransitDTO transitDTO = arrFindDudesColumn.get(position);

				for (TransitDTO trans : arrFindDudesColumn) {
					trans.setIsSelected(false);
				}

				transitDTO.setIsSelected(true);
				selectedFindDudesColumnId = transitDTO.getTransitId();

				findDudesColumnAdapter.notifyDataSetChanged();
				// gridViewAdapter = new GridViewAdapter(context,
				// (ArrayList<InterestsDTO>) interestsDTOs, 1);
				// horizontalListView.setAdapter(gridViewAdapter);

				// logFlurryEvent(settingsParams, "Filter Interest");
			}
		});
	}

	private void initializeLanguageSetting(View view) {
		arrLanguage = new ArrayList<TransitDTO>();
		String arr1[] = { "0", "1", "2" };
		String arr2[] = getResources().getStringArray(R.array.language_arrays);

		selectedLanguageId = Integer.toString(singleton.mLanguagePrefs.getInt(
				AppConstants.LANGUAGE_PREFERENCE, 0));
		int n = Integer.parseInt(selectedLanguageId);
		Log.d("", "selectedLanguageId=" + selectedLanguageId);
		for (int i = 0; i < arr1.length; i++) {
			TransitDTO dto = new TransitDTO();
			dto.setIsSelected(n == i ? true : false);
			dto.setTransitId(arr1[i]);
			dto.setTransitName(arr2[i]);
			arrLanguage.add(dto);
		}

		languageAdapter = new TransitAdapter(context, arrLanguage, 1);

		gridLanguage = (GridView) view.findViewById(R.id.settings_language);

		gridLanguage.setAdapter(languageAdapter);

		gridLanguage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				Utils.setLocale(context, position);
				Intent i = new Intent(context, Activity_Home.class);
				i.putExtra("open", AppConstants.FRAGMENT_SETTING_NEW);
				context.finish();
				context.startActivity(i);

				TransitDTO transitDTO = arrLanguage.get(position);

				for (TransitDTO trans : arrLanguage) {
					trans.setIsSelected(false);
				}

				transitDTO.setIsSelected(true);
				selectedLanguageId = transitDTO.getTransitId();

				languageAdapter.notifyDataSetChanged();
				Editor eL = singleton.mLanguagePrefs.edit();
				eL.putInt(AppConstants.LANGUAGE_PREFERENCE,
						Integer.parseInt(selectedLanguageId));
				eL.commit();
				// gridViewAdapter = new GridViewAdapter(context,
				// (ArrayList<InterestsDTO>) interestsDTOs, 1);
				// horizontalListView.setAdapter(gridViewAdapter);

				// logFlurryEvent(settingsParams, "Filter Interest");
			}
		});
	}

	private void initializeTransit(View view) {
		arrTransitDTO = new ArrayList<TransitDTO>();
		String arr1[] = { "1", "2", "3", "4", "5" };
		String arr2[] = { getResources().getString(R.string.setting_default),
				getResources().getString(R.string.setting_walking),
				getResources().getString(R.string.setting_biking),
				getResources().getString(R.string.setting_driving),
				getResources().getString(R.string.setting_public_transit) };

		selectedTransitId = Integer.toString(singleton.mTransitPrefs.getInt(
				AppConstants.TRANSIT_PREFERENCE, 1));
		int n = Integer.parseInt(selectedTransitId) - 1;

		for (int i = 0; i < arr1.length; i++) {
			TransitDTO dto = new TransitDTO();
			dto.setIsSelected(n == i ? true : false);
			dto.setTransitId(arr1[i]);
			dto.setTransitName(arr2[i]);
			arrTransitDTO.add(dto);
		}

		gridViewAdapter = new TransitAdapter(context, arrTransitDTO, 1);

		gridTransit = (GridView) view.findViewById(R.id.settings_transit);

		gridTransit.setAdapter(gridViewAdapter);

		gridTransit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				TransitDTO transitDTO = arrTransitDTO.get(position);

				for (TransitDTO trans : arrTransitDTO) {
					trans.setIsSelected(false);
				}

				transitDTO.setIsSelected(true);
				selectedTransitId = transitDTO.getTransitId();

				gridViewAdapter.notifyDataSetChanged();
				// gridViewAdapter = new GridViewAdapter(context,
				// (ArrayList<InterestsDTO>) interestsDTOs, 1);
				// horizontalListView.setAdapter(gridViewAdapter);

				// logFlurryEvent(settingsParams, "Filter Interest");
			}
		});
	}

	public void setLayout(View view) {
		linearHeaderNotifications = (LinearLayout) view
				.findViewById(R.id.linearHeaderNotifications);
		linearHeaderUpdateInfo = (LinearLayout) view
				.findViewById(R.id.linearHeaderUpdateInfo);
		linearHeaderChangePass = (LinearLayout) view
				.findViewById(R.id.linearHeaderChangePass);
		linearHeaderDeleteAccount = (LinearLayout) view
				.findViewById(R.id.linearHeaderDeleteAccount);
		linearHeaderShowAge = (LinearLayout) view
				.findViewById(R.id.linearHeaderShowAge);
		linearHeaderFindDudeColumn = (LinearLayout) view
				.findViewById(R.id.linearHeaderFindDudesColumn);
		linearHeaderTransit = (LinearLayout) view
				.findViewById(R.id.linearHeaderTransit);
		linearHeaderChatHistory = (LinearLayout) view
				.findViewById(R.id.linearHeaderChatHistory);
		linearHeaderLanguage = (LinearLayout) view
				.findViewById(R.id.linearHeaderLanguage);
		linearHeaderSave = (LinearLayout) view
				.findViewById(R.id.linearHeaderSaveAccount);

		linearBodyNotifications = (LinearLayout) view
				.findViewById(R.id.linearBodyNotifications);
		linearBodyUpdateInfo = (LinearLayout) view
				.findViewById(R.id.linearBodyUpdateInfo);
		linearBodyChangePass = (LinearLayout) view
				.findViewById(R.id.linearBodyChangePass);
		linearBodyDeleteAccount = (LinearLayout) view
				.findViewById(R.id.linearBodyDeleteAccount);
		linearBodyShowAge = (LinearLayout) view
				.findViewById(R.id.linearBodyShowAge);
		linearBodyFindDudeColumn = (LinearLayout) view
				.findViewById(R.id.linearBodyFindDudesColumn);
		linearBodyTransit = (LinearLayout) view
				.findViewById(R.id.linearBodyTransit);
		linearBodyChatHistory = (LinearLayout) view
				.findViewById(R.id.linearBodyChatHistory);
		linearBodyLanguage = (LinearLayout) view
				.findViewById(R.id.linearBodyLanguage);

		loadingView = (ImageView) view.findViewById(R.id.loadingView);
		llLoading = (LinearLayout) view.findViewById(R.id.llLoading);
		scroll = (ScrollView) view.findViewById(R.id.svSettingsNew);

		btnSaveUpdateInfo = (Button) view.findViewById(R.id.btnSaveUpdateInfo);
		btnSaveChangePass = (Button) view
				.findViewById(R.id.btnSaveChangePassword);
		btnDeleteAccount = (Button) view.findViewById(R.id.btnDeleteAccount);

		ivNotification = (ImageView) view.findViewById(R.id.notification);
		ivShowAgeWeightHeight = (ImageView) view
				.findViewById(R.id.notificationAge);

		etUserName = (EditText) view.findViewById(R.id.etUserName);
		etLastName = (EditText) view.findViewById(R.id.etLastName);
		etMobile = (EditText) view.findViewById(R.id.etMobile);
		etEmail = (EditText) view.findViewById(R.id.etEmail);
		etOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
		etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
		etConfirmPassword = (EditText) view
				.findViewById(R.id.etConfirmPassword);

		etUserName.setText(userDTO.getFirstName());
		etLastName.setText(userDTO.getLastName());

		etMobile.setText(userDTO.getMobile());
		etEmail.setText(userDTO.getEmail());
		etEmail.setEnabled(false);
	}

	public void setTypeFace() {
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		etEmail.setTypeface(typeface);
		etOldPassword.setTypeface(typeface);
		etNewPassword.setTypeface(typeface);
		etConfirmPassword.setTypeface(typeface);
		etMobile.setTypeface(typeface);
	}

	public void setListener() {
		linearHeaderNotifications.setOnClickListener(this);
		linearHeaderUpdateInfo.setOnClickListener(this);
		linearHeaderChangePass.setOnClickListener(this);
		linearHeaderDeleteAccount.setOnClickListener(this);
		linearHeaderShowAge.setOnClickListener(this);
		linearHeaderFindDudeColumn.setOnClickListener(this);
		linearHeaderTransit.setOnClickListener(this);
		linearHeaderChatHistory.setOnClickListener(this);
		linearHeaderLanguage.setOnClickListener(this);
		linearHeaderSave.setOnClickListener(this);

		linearBodyNotifications.setOnClickListener(this);
		linearBodyUpdateInfo.setOnClickListener(this);
		linearBodyChangePass.setOnClickListener(this);
		linearBodyDeleteAccount.setOnClickListener(this);
		linearBodyShowAge.setOnClickListener(this);
		linearBodyFindDudeColumn.setOnClickListener(this);
		linearBodyTransit.setOnClickListener(this);
		linearBodyChatHistory.setOnClickListener(this);
		linearBodyLanguage.setOnClickListener(this);

		btnSaveUpdateInfo.setOnClickListener(this);
		btnSaveChangePass.setOnClickListener(this);
		btnDeleteAccount.setOnClickListener(this);
	}

	public void setShowAgeWeightHeight() {
		/*
		 * singleton.mPrefs =
		 * context.getSharedPreferences(AppConstants.GPS_PREFS, 0);
		 * isGPSReminderOn =
		 * singleton.mPrefs.getBoolean(AppConstants.GPS_REMINDER_ON, true);
		 * System.out.println("\n isgpsreminderON : "+isGPSReminderOn); if
		 * (isGPSReminderOn) {
		 * ivNotification.setImageDrawable(context.getResources()
		 * .getDrawable(R.drawable.select_notification)); } else {
		 * ivNotification.setImageDrawable(context.getResources()
		 * .getDrawable(R.drawable.unselect_notofication)); }
		 */
	}

	public void setGPSReminders() {
		singleton.mPrefs = context.getSharedPreferences(AppConstants.GPS_PREFS,
				0);
		isGPSReminderOn = singleton.mPrefs.getBoolean(
				AppConstants.GPS_REMINDER_ON, true);
		System.out.println("\n isgpsreminderON : " + isGPSReminderOn);
		if (isGPSReminderOn) {
			ivNotification.setImageDrawable(context.getResources().getDrawable(
					R.drawable.select_notification));
		} else {
			ivNotification.setImageDrawable(context.getResources().getDrawable(
					R.drawable.unselect_notofication));
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	public void setVisibility(LinearLayout linear) {
		if (linear.getVisibility() == View.VISIBLE)
			linear.setVisibility(View.GONE);
		else
			linear.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View arg0) {

		if (isProcessing)
			return;

		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.linearHeaderNotifications:
			setVisibility(linearBodyNotifications);
			break;
		case R.id.linearHeaderUpdateInfo:
			setVisibility(linearBodyUpdateInfo);
			break;
		case R.id.linearHeaderChangePass:
			setVisibility(linearBodyChangePass);
			break;
		case R.id.linearHeaderChatHistory:
			setVisibility(linearBodyChatHistory);
			break;
		case R.id.linearHeaderShowAge:
			setVisibility(linearBodyShowAge);
			break;
		case R.id.linearHeaderFindDudesColumn:
			setVisibility(linearBodyFindDudeColumn);
			break;
		case R.id.linearHeaderTransit:
			setVisibility(linearBodyTransit);
			break;
		case R.id.linearHeaderLanguage:
			setVisibility(linearBodyLanguage);
			break;
		case R.id.linearBodyShowAge:
			Editor e2 = singleton.mPrefs.edit();
			if (isGPSReminderOn) {
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_SETTINGS,
						AppConstants.EVENT_LOG_ACTION_SETTINGS,
						"DisableGPSReminder");

				ivShowAgeWeightHeight.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.unselect_notofication));
				e2.putBoolean(AppConstants.GPS_REMINDER_ON, false);
				isGPSReminderOn = false;

			} else {
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_SETTINGS,
						AppConstants.EVENT_LOG_ACTION_SETTINGS,
						"EnableGPSReminder");

				ivShowAgeWeightHeight.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.select_notification));
				e2.putBoolean(AppConstants.GPS_REMINDER_ON, true);
				isGPSReminderOn = true;
			}

			singleton.gpsReminderCounter = 0;// reset
			e2.commit();
			break;
		case R.id.linearBodyNotifications:
			Editor e = singleton.mPrefs.edit();
			if (isGPSReminderOn) {
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_SETTINGS,
						AppConstants.EVENT_LOG_ACTION_SETTINGS,
						"DisableGPSReminder");

				ivNotification.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.unselect_notofication));
				e.putBoolean(AppConstants.GPS_REMINDER_ON, false);
				isGPSReminderOn = false;

			} else {
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_SETTINGS,
						AppConstants.EVENT_LOG_ACTION_SETTINGS,
						"EnableGPSReminder");

				ivNotification.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.select_notification));
				e.putBoolean(AppConstants.GPS_REMINDER_ON, true);
				isGPSReminderOn = true;
			}

			singleton.gpsReminderCounter = 0;// reset
			e.commit();
			break;
		case R.id.linearBodyChangePass:
			break;
		case R.id.linearBodyDeleteAccount:
			break;

		case R.id.linearHeaderSaveAccount:
			this.saveWholeSetting();
			break;
		case R.id.btnSaveUpdateInfo:
			if (0 == etUserName.getText().toString().trim().length()) {
				Toast.makeText(context, R.string.toast_please_enter_username,
						Toast.LENGTH_SHORT).show();
			} else if ((etEmail.getText().toString().trim().length() == 0)) {
				Toast.makeText(context,
						R.string.toast_please_enter_email_address,
						Toast.LENGTH_SHORT).show();
			} else if (!checkEmailCorrect(etEmail.getText().toString().trim())) {
				Toast.makeText(context,
						R.string.toast_please_enter_correct_email_address,
						Toast.LENGTH_SHORT).show();
			} else {
				userDTO.setFirstName(etUserName.getText().toString());
				userDTO.setLastName(etLastName.getText().toString());
				userDTO.setEmail(etEmail.getText().toString());
				if (WebServiceConstants.isOnline(context)) {
					context.easyTrackerEventLog(
							AppConstants.EVENT_LOG_CATEG_SETTINGS,
							AppConstants.EVENT_LOG_ACTION_SETTINGS,
							"UpdateInfo");
					new UpdateProfileAsynchTask()
							.execute(WebServiceConstants.BASE_URL_FILE
									+ WebServiceConstants.USER_PROFILE_UPDATE);
					new UpdateUserProfileData()
							.execute(WebServiceConstants.AV_UPDATE_USER_PROFILE_DATA);
				}
			}

			break;
		case R.id.btnSaveChangePassword:
			if (0 == etOldPassword.getText().toString().length()) {
				Toast.makeText(context, R.string.toast_enter_old_password,
						Toast.LENGTH_SHORT).show();
			} else if (0 == etNewPassword.getText().toString().length()) {
				Toast.makeText(context, R.string.toast_enter_new_password,
						Toast.LENGTH_SHORT).show();
			} else if (etNewPassword.getText().toString().length() < 6) {
				Toast.makeText(context,
						R.string.toast_new_password_must_be_6_character_long,
						Toast.LENGTH_SHORT).show();
			} else if (etNewPassword.getText().toString()
					.equals(etConfirmPassword.getText().toString())) {
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_SETTINGS,
						AppConstants.EVENT_LOG_ACTION_SETTINGS,
						"ChangePassword");
				if (WebServiceConstants.isOnline(context)) {
					new ChangePasswordAsyncTask()
							.execute(WebServiceConstants.BASE_URL
									+ WebServiceConstants.CHANGE_PASSWORD);
					new UpdateUserProfileData()
							.execute(WebServiceConstants.AV_UPDATE_USER_PROFILE_DATA);
				} else {
					Toast.makeText(context,
							R.string.toast_please_check_network_connection,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						context,
						R.string.toast_new_password_and_confirm_new_password_are_not_same,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.linearHeaderDeleteAccount:
			// setVisibility(linearBodyDeleteAccount);
			// break;
		case R.id.btnDeleteAccount:
			context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_SETTINGS,
					"DeleteAccount", "");
			openDailog();
			break;
		}
	}

	private void saveWholeSetting() {
		int oldPassLen = etOldPassword.getText().toString().length();
		int newPassLen = etNewPassword.getText().toString().length();
		int conPassLen = etConfirmPassword.getText().toString().length();
		int passLen = oldPassLen + newPassLen + conPassLen;

		if (0 == etOldPassword.getText().toString().length() && passLen > 0) {
			Toast.makeText(context, R.string.toast_enter_old_password,
					Toast.LENGTH_SHORT).show();
		} else if (0 == etNewPassword.getText().toString().length()
				&& passLen > 0) {
			Toast.makeText(context, R.string.toast_enter_new_password,
					Toast.LENGTH_SHORT).show();
		} else if (etNewPassword.getText().toString().length() < 6
				&& passLen > 0) {
			Toast.makeText(context,
					R.string.toast_new_password_must_be_6_character_long,
					Toast.LENGTH_SHORT).show();
		} else if (etNewPassword.getText().toString()
				.equals(etConfirmPassword.getText().toString())) {
			context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_SETTINGS,
					AppConstants.EVENT_LOG_ACTION_SETTINGS, "ChangePassword");
			if (WebServiceConstants.isOnline(context)) {
				// singleton.progressLoading(loadingView, llLoading);
				new UpdateUserSettings()
						.execute(WebServiceConstants.BASE_URL_FILE
								+ WebServiceConstants.CHANGE_WHOLE_SETTING);
				new UpdateUserProfileData()
						.execute(WebServiceConstants.AV_UPDATE_USER_PROFILE_DATA);
			} else {
				Toast.makeText(context,
						R.string.toast_please_check_network_connection,
						Toast.LENGTH_LONG).show();
			}
		} else if (!etNewPassword.getText().toString()
				.equals(etConfirmPassword.getText().toString())
				&& passLen > 0) {
			Toast.makeText(
					context,
					R.string.toast_new_password_and_confirm_new_password_are_not_same,
					Toast.LENGTH_SHORT).show();
		}
	}

	boolean checkEmailCorrect(String Email) {

		String pttn = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]+";
		Pattern p = Pattern.compile(pttn);
		Matcher m = p.matcher(Email);

		if (m.matches()) {
			return true;
		}
		return false;
	}

	public void openDailog() {
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_logout);
		// Do you realy want to delete your account ?
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText(R.string.dialog_do_you_really_want_to_delete_your_account);
		Typeface typeface = FontStyle.getFont(context,
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
				Intent intent = new Intent(context, GpsService.class);
				context.stopService(intent);
				Intent inten1 = new Intent(context, ChatService.class);
				context.stopService(inten1);
				new DeleteAccountAsyncTask()
						.execute(WebServiceConstants.BASE_URL
								+ WebServiceConstants.DELETE_ACCOUNT);

			}
		});
		dialog.show();
	}

	/* ======== Asynch Tasks =========== */

	class UpdateUserSettings extends AsyncTask<String, Void, String> {
		private JSONArray jsonArray;
		private JSONObject jsonObject;
		private String vStatus;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isProcessing = true;
			singleton.progressLoading(loadingView, llLoading);
		}

		protected String doInBackground(String... args) {
			String vResult = "";
			try {

				int oldPassLen = etOldPassword.getText().toString().length();
				int newPassLen = etNewPassword.getText().toString().length();
				int conPassLen = etConfirmPassword.getText().toString()
						.length();
				int passLen = oldPassLen + newPassLen + conPassLen;

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(args[0]);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				Log.d("", "language=" + userDTO.getLanguage());
				reqEntity.addPart("language",
						new StringBody(userDTO.getLanguage()));
				reqEntity.addPart("user_id", new StringBody(userDTO.getUserID()
						.toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("lname", new StringBody(userDTO.getLastName()
						.toString()));
				if (etMobile.getText().toString().length() > 0) {
					// reqEntity.addPart("mobile", new
					// StringBody(userDTO.getMobile()
					// .toString()));
					reqEntity.addPart("mobile", new StringBody(etMobile
							.getText().toString()));
				}

				reqEntity.addPart("email", new StringBody(userDTO.getEmail()
						.toString()));
				// reqEntity.addPart("password", new StringBody(userDTO
				// .getPassword().toString()));
				if (passLen > 0) {
					reqEntity.addPart("old_password", new StringBody(
							etOldPassword.getText().toString()));
					reqEntity.addPart("new_password", new StringBody(
							etNewPassword.getText().toString()));
				}

				reqEntity.addPart("transit", new StringBody(selectedTransitId));
				reqEntity.addPart("chat_history", new StringBody(
						selectedChatHistoryColumnId));
				reqEntity.addPart("find_guy", new StringBody(
						selectedFindDudesColumnId));
				// chat_history
				// find_guy
				reqEntity.addPart("isFirstTime", new StringBody(userDTO
						.getIsFirstTime().toString()));
				reqEntity.addPart("user_whatAreYou", new StringBody(userDTO
						.getWhatAreYouDTO().getWhatAreYou().toString()));
				reqEntity.addPart("feet", new StringBody(userDTO
						.getWhatAreYouDTO().getHight().toString()));
				reqEntity.addPart("inches", new StringBody(userDTO
						.getWhatAreYouDTO().getInches().toString()));
				reqEntity.addPart("meters", new StringBody(userDTO
						.getWhatAreYouDTO().getMeters().toString()));
				reqEntity.addPart("height", new StringBody(userDTO
						.getWhatAreYouDTO().getHight().toString()));
				reqEntity.addPart("heightUnit", new StringBody(userDTO
						.getWhatAreYouDTO().getHeightUnit().toString()));
				reqEntity.addPart("weight", new StringBody(userDTO
						.getWhatAreYouDTO().getWeight().toString()));
				reqEntity.addPart("weightUnit", new StringBody(userDTO
						.getWhatAreYouDTO().getWeightUnit().toString()));
				reqEntity.addPart("age", new StringBody(userDTO
						.getWhatAreYouDTO().getAge().toString()));
				reqEntity.addPart("user_relationshipStatus", new StringBody(
						userDTO.getWhatAreYouDTO().getRelationshipStatus()
								.toString()));
				reqEntity.addPart("user_whatDoYouKrave", new StringBody(userDTO
						.getWhatAreYouDTO().getWhatDoYouKrave().toString()));

				reqEntity.addPart("user_note", new StringBody(userDTO
						.getAboutMe().toString()));

				reqEntity.addPart("user_facebook_image",
						new StringBody(userDTO.getProfileImage()));

				for (int i = 0; i < userDTO.getInterestList().size(); i++) {
					// reqEntity.addPart("interest[" + i + "][]", new
					// StringBody(String.valueOf(i + 1)));
					reqEntity
							.addPart(
									"interest[" + i + "][]",
									new StringBody(String.valueOf(userDTO
											.getInterestList().get(i)
											.getInterestId())));

				}
				if (userDTO.isFacebookLikeEnable()) {
					if (userDTO.getFacebookLikesDTOs() != null) {
						Log.d("",
								"isFacebookLikesEnable="
										+ userDTO.isFacebookLikeEnable());
						Log.d("", "size="
								+ userDTO.getFacebookLikesDTOs().size());
						reqEntity.addPart("user_facebook_Interest",
								new StringBody(
										AppConstants.FACEBOOK_LIKE_ENABLE));
						for (int i = 0; i < userDTO.getFacebookLikesDTOs()
								.size(); i++) {
							// reqEntity.addPart("interest[" + i + "][]", new
							// StringBody(String.valueOf(i + 1)));
							reqEntity.addPart(
									"fbintrast[" + i + "][]",
									new StringBody(String.valueOf(userDTO
											.getFacebookLikesDTOs().get(i)
											.getImagePath())));
						}
					}
				} else {
					reqEntity.addPart("user_facebook_Interest", new StringBody(
							AppConstants.FACEBOOK_LIKE_DISABLE));
				}

				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				StringBuffer stringBuffer = new StringBuffer("");
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				String LineSeparator = System.getProperty("line.separator");
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line + LineSeparator);
				}
				vResult = stringBuffer.toString();

			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				Log.v("Message...1", e.getMessage());
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				Log.v("Message...2", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			} catch (IOException e) {
				Log.v("Message...3", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			}
			return vResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// dialog.dismiss();
			try {
				isProcessing = false;
				singleton.stopLoading(llLoading);
				// tvPullDownToRefresh.setVisibility(View.VISIBLE);
			} catch (Exception e) {

			}
			System.out.println("Responce....Admin Reg.String" + result);
			// [{"status":"Success","admin_unique_id":"admin4"}]
			try {
				jsonArray = new JSONArray(result);
				/*
				 * [{"status":"success","didPassChange":"YES",
				 * "user":{"user_id":
				 * "3140","user_email":"kennethdeloso@yahoo.com",
				 * "user_password"
				 * :"d8578edf8458ce06fbc5bb76a58c5ca4","user_fname"
				 * :"supremo68","user_lname":"",
				 * "user_image":"url","user_mobile"
				 * :"12345611","user_address":"",
				 * "user_dob":"0000-00-00","user_feet"
				 * :"","user_inches":"","user_meters":"",
				 * "user_age":"01/11/1992"
				 * ,"user_weight":"127","user_weightUnit":"US",
				 * "user_height":"3/3","user_heightUnit":"US","user_note":
				 * "I am supreme\\n\\nI am the one\\n",
				 * "user_language":"0","role"
				 * :"1","body_type":"1","love_hookup":""
				 * ,"user_relationshipStatus":"1",
				 * "user_whatAreYou":"3","user_whatDoYouKrave"
				 * :"3","isFirstTime":"0","user_type":"",
				 * "user_facebook_Interest"
				 * :"No","user_status":"1","user_reg_date"
				 * :"2015-03-12 13:30:06", "user_whatAreYou_name":"Asian"},
				 * "setting"
				 * :[{"s_no":"3140","user_id":"3140","min_value":"18","max_value"
				 * :"75","radius":"300",
				 * "interest":"1,2,5,8,12","ethencity":"2,",
				 * "transit":"1","role":"1",
				 * "body_type":"1","love_hookup":"","notification"
				 * :"1"}],"intrest":[{"ui_id":"85556","intrest_id":"12",
				 * "user_id"
				 * :"3140","ui_type":"admin"},{"ui_id":"85555","intrest_id"
				 * :"8","user_id":"3140","ui_type":"admin"},
				 * {"ui_id":"85554","intrest_id"
				 * :"5","user_id":"3140","ui_type":"admin"
				 * },{"ui_id":"85553","intrest_id"
				 * :"2","user_id":"3140","ui_type"
				 * :"admin"},{"ui_id":"85552","intrest_id"
				 * :"1","user_id":"3140","ui_type"
				 * :"admin"}],"fbintrast":[],"images"
				 * :[{"image_id":"4457","User_id"
				 * :"3140","image_name":null,"image_path"
				 * :"krave_image/14144703582_.png"
				 * ,"image_position":"2","user_date"
				 * :"2014-10-28","user_img_status"
				 * :"1"},{"image_id":"13290","User_id"
				 * :"3140","image_name":null,"image_path"
				 * :"krave_image/142486458601209646590_.png"
				 * ,"image_position":"0"
				 * ,"user_date":"2015-02-25","user_img_status":"0"}]}]\n
				 */
				jsonObject = jsonArray.getJSONObject(0);
				vStatus = jsonObject.getString("status");
				if (vStatus.equals("success")) {

					Toast.makeText(context,
							R.string.toast_setting_updated_successfully,
							Toast.LENGTH_SHORT).show();

					if (jsonObject.getString("didPassChange").equalsIgnoreCase(
							"NO")) {
						Toast.makeText(context,
								R.string.toast_password_did_not_change,
								Toast.LENGTH_SHORT).show();
					}

					Editor e = singleton.mTransitPrefs.edit();
					e.putInt(AppConstants.TRANSIT_PREFERENCE,
							Integer.parseInt(selectedTransitId));
					e.commit();
					Editor eL = singleton.mLanguagePrefs.edit();
					eL.putInt(AppConstants.LANGUAGE_PREFERENCE,
							Integer.parseInt(selectedLanguageId));
					eL.commit();
					sessionManager
							.setUserDetail(parseUserDataAndSetInSessionUserSetting(jsonObject));
					sessionManager
							.setSettingDetail(parseSettingDataAndSetInSessionUserSetting(jsonObject));

				} else if (vStatus.equals("failure")) {
					Toast.makeText(context, R.string.toast_setting_not_updated,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	// update every save

	class UpdateUserProfileData extends AsyncTask<String, Void, JSONArray> {
		String vStatus;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
		}
	}

	// update info

	class UpdateProfileAsynchTask extends AsyncTask<String, Void, String> {
		private JSONArray jsonArray;
		private JSONObject jsonObject;
		private String vStatus;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			String vResult = "";
			try {

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(args[0]);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				Log.d("", "language=" + userDTO.getLanguage());
				reqEntity.addPart("language",
						new StringBody(userDTO.getLanguage()));
				reqEntity.addPart("user_id", new StringBody(userDTO.getUserID()
						.toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("lname", new StringBody(userDTO.getLastName()
						.toString()));
				reqEntity.addPart("mobile", new StringBody(userDTO.getMobile()
						.toString()));
				reqEntity.addPart("email", new StringBody(userDTO.getEmail()
						.toString()));
				reqEntity.addPart("password", new StringBody(userDTO
						.getPassword().toString()));
				reqEntity.addPart("isFirstTime", new StringBody(userDTO
						.getIsFirstTime().toString()));
				reqEntity.addPart("user_whatAreYou", new StringBody(userDTO
						.getWhatAreYouDTO().getWhatAreYou().toString()));
				reqEntity.addPart("feet", new StringBody(userDTO
						.getWhatAreYouDTO().getHight().toString()));
				reqEntity.addPart("inches", new StringBody(userDTO
						.getWhatAreYouDTO().getInches().toString()));
				reqEntity.addPart("meters", new StringBody(userDTO
						.getWhatAreYouDTO().getMeters().toString()));
				reqEntity.addPart("height", new StringBody(userDTO
						.getWhatAreYouDTO().getHight().toString()));
				reqEntity.addPart("heightUnit", new StringBody(userDTO
						.getWhatAreYouDTO().getHeightUnit().toString()));
				reqEntity.addPart("weight", new StringBody(userDTO
						.getWhatAreYouDTO().getWeight().toString()));
				reqEntity.addPart("weightUnit", new StringBody(userDTO
						.getWhatAreYouDTO().getWeightUnit().toString()));
				reqEntity.addPart("age", new StringBody(userDTO
						.getWhatAreYouDTO().getAge().toString()));
				reqEntity.addPart("user_relationshipStatus", new StringBody(
						userDTO.getWhatAreYouDTO().getRelationshipStatus()
								.toString()));
				reqEntity.addPart("user_whatDoYouKrave", new StringBody(userDTO
						.getWhatAreYouDTO().getWhatDoYouKrave().toString()));

				reqEntity.addPart("user_note", new StringBody(userDTO
						.getAboutMe().toString()));

				reqEntity.addPart("user_facebook_image",
						new StringBody(userDTO.getProfileImage()));

				for (int i = 0; i < userDTO.getInterestList().size(); i++) {
					// reqEntity.addPart("interest[" + i + "][]", new
					// StringBody(String.valueOf(i + 1)));
					reqEntity
							.addPart(
									"interest[" + i + "][]",
									new StringBody(String.valueOf(userDTO
											.getInterestList().get(i)
											.getInterestId())));

				}
				if (userDTO.isFacebookLikeEnable()) {
					if (userDTO.getFacebookLikesDTOs() != null) {
						Log.d("",
								"isFacebookLikesEnable="
										+ userDTO.isFacebookLikeEnable());
						Log.d("", "size="
								+ userDTO.getFacebookLikesDTOs().size());
						reqEntity.addPart("user_facebook_Interest",
								new StringBody(
										AppConstants.FACEBOOK_LIKE_ENABLE));
						for (int i = 0; i < userDTO.getFacebookLikesDTOs()
								.size(); i++) {
							// reqEntity.addPart("interest[" + i + "][]", new
							// StringBody(String.valueOf(i + 1)));
							reqEntity.addPart(
									"fbintrast[" + i + "][]",
									new StringBody(String.valueOf(userDTO
											.getFacebookLikesDTOs().get(i)
											.getImagePath())));
						}
					}
				} else {
					reqEntity.addPart("user_facebook_Interest", new StringBody(
							AppConstants.FACEBOOK_LIKE_DISABLE));
				}

				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				StringBuffer stringBuffer = new StringBuffer("");
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				String LineSeparator = System.getProperty("line.separator");
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line + LineSeparator);
				}
				vResult = stringBuffer.toString();

			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				Log.v("Message...1", e.getMessage());
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				Log.v("Message...2", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			} catch (IOException e) {
				Log.v("Message...3", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			}
			return vResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// dialog.dismiss();
			System.out.println("Responce....Admin Reg.String" + result);
			// [{"status":"Success","admin_unique_id":"admin4"}]
			try {
				jsonArray = new JSONArray(result);

				jsonObject = jsonArray.getJSONObject(0);
				vStatus = jsonObject.getString("status");
				if (vStatus.equals("success")) {
					Toast.makeText(context,
							R.string.toast_profile_updated_successfully,
							Toast.LENGTH_SHORT).show();

					sessionManager
							.setUserDetail(parseUserDataAndSetInSession(jsonObject));

				} else if (vStatus.equals("failure")) {
					Toast.makeText(context, R.string.toast_profile_not_updated,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private UserDTO parseUserDataAndSetInSessionUserSetting(
			JSONObject mJsonObject) throws JSONException {
		UserDTO userDTO = this.parseUserDataAndSetInSession(mJsonObject);

		JSONObject userObject = mJsonObject.getJSONObject("user");

		userDTO.setPassword(userObject.getString("user_password"));
		if (sessionManager.isRemember()) {
			sessionManager.setRememberMe(sessionManager.getEmail(),
					etNewPassword.getText().toString());
		}

		return userDTO;
	}

	private SettingDTO parseSettingDataAndSetInSessionUserSetting(
			JSONObject mJsonObject) throws JSONException {
		SettingDTO settingDTO = sessionManager.getSettingDetail();

		JSONArray settingArray = mJsonObject.getJSONArray("setting");
		// chat_history
		// find_guy
		TransitDTO tDTO = new TransitDTO();
		tDTO.setTransitId(settingArray.getJSONObject(0).getString("transit"));
		settingDTO.setChatHistoryPeriod(settingArray.getJSONObject(0)
				.getString("chat_history"));
		settingDTO.setFindDudesColumnCoun(settingArray.getJSONObject(0)
				.getString("find_guy"));

		settingDTO.setTransitDTO(tDTO);

		return settingDTO;
	}

	private UserDTO parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();

		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		System.out.println(MainObject);
		userDTO.setLanguage(MainObject.getString("user_language"));
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
		this.userDTO.setUserID(MainObject.getString("user_id"));
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
		// MAY 14 2015
		// for (int i = 0; i < jsonArrayImages.length(); i++) {
		// JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
		// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		// userProfileImageDTO.setImageId(imagesJsonObject
		// .getString("image_id"));
		// userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
		// + imagesJsonObject.getString("image_path"));
		// userProfileImageDTO.setImagePosition(imagesJsonObject
		// .getString("image_position"));
		//
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
			// - - - - -
			userProfileImageDTO.setImagePosition(imagesJsonObject
					.getString("image_position"));

			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}

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
		return userDTO;
	}

	// change password
	class ChangePasswordAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// parkhya.org/Android/krave_app/index.php?action=changepassword&oldpassword=123&newpassword=1234&userid=1
			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("oldpassword", etOldPassword
					.getText().toString()));
			params.add(new BasicNameValuePair("newpassword", etNewPassword
					.getText().toString()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("change password response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				// System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					Toast.makeText(context,
							R.string.toast_password_changed_successfully,
							Toast.LENGTH_SHORT).show();
					if (sessionManager.isRemember()) {
						sessionManager.setRememberMe(sessionManager.getEmail(),
								etNewPassword.getText().toString());
					}
				} else {
					Toast.makeText(context,
							R.string.toast_password_was_not_changed,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	// delete account
	class DeleteAccountAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("DELETE ACCOUNT RESPONSE", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			try {
				List<String> interestsStrings = new ArrayList<String>();
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					// deleteAcoount = false;
					new XmppDeleteUserAsynchTask().execute(
							WebServiceConstants.XMPP_USER_REGISTRATION,
							sessionManager.getUserDetail().getUserID());
					sessionManager.Logout();
					context.finish();
					Intent intent = new Intent(context, Activity_Login.class);
					context.startActivity(intent);
					Toast.makeText(context,
							R.string.toast_account_successfully_deleted,
							Toast.LENGTH_SHORT).show();
				} else if (vStatus.equals("failure")) {
					Intent intent = new Intent(context, GpsService.class);
					context.startService(intent);
					Intent inten1 = new Intent(context, ChatService.class);
					context.startService(inten1);
					Toast.makeText(context,
							R.string.toast_fail_to_delete_account,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Intent intent = new Intent(context, GpsService.class);
				context.startService(intent);
			}

		}
	}

	class XmppDeleteUserAsynchTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			Log.d("", "xmpp registration start");
			try {

				// http://54.241.85.74:9090/plugins/userService/userservice?type=delete_roster&secret=7bDFD4TP&username=555&item_jid=ypawar979@gmail.com
				// changed ip 54.241.85.74
				String posturl = "http://184.169.159.101:9090/plugins/userService/userservice?type=delete_roster&secret=7bDFD4TP&username="
						+ args[1];

				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(posturl);
				HttpResponse response = httpclient.execute(httpget);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String result = sb.toString();
				Log.v("My Response :: ", result);
				return null;
			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				Log.v("Message...1", e.getMessage());
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				Log.v("Message...2", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			} catch (IOException e) {
				Log.v("Message...3", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Log.d("", "xmpp registration successfull");

		}
	}
}
