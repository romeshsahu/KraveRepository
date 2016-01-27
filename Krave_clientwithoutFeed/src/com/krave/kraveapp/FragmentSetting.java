package com.krave.kraveapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.ps.adapters.RoleSpinnerAdapter;
import com.ps.adapters.SettingListInterestAdapter;
import com.ps.adapters.WhatAreYouListAdapter;
import com.ps.horizontal_listview.MyGridView;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.BodyTypeDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.RoleDTO;
import com.ps.models.SettingDTO;
import com.ps.models.TransitDTO;
import com.ps.models.UserDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.RangeSeekBar;
import com.ps.utill.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;
import com.flurry.android.FlurryAgent;

@SuppressLint("NewApi")
public class FragmentSetting extends Fragment implements OnClickListener {
	SettingDTO settingDTO = new SettingDTO();
	private List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	private List<WhatAreYouDataDTO> whatAreYouDTOs = new ArrayList<WhatAreYouDataDTO>();
	private TextView minyearText, maxyearText, mailesText;
	private MyGridView interestGridView, ethinicityGridView, roleGridView;
	private ImageView enableNotificationImageView, enableGPSReminderImageView,
			onlineDudesFilter;
	private RelativeLayout mainview, clearAllFilterButton;
	private LinearLayout layoutChangePassword, layoutDeleteAccount,
			layoutUpdateInfo, layoutHideAndShow, seekBarLayoutForAge,
			linearLayout1;
	private SeekBar radiusSeekBar;
	private int radius = 50;
	private int MAX_VALUE = 300;
	private int isNotificationEnable;
	SessionManager sessionManager;
	private String userId;
	Activity_Home context;
	private int minAge;
	private int maxAge;
	private RangeSeekBar<Integer> seekBar;
	private SettingListInterestAdapter settingListInterestAdapter;
	private WhatAreYouListAdapter whatAreYouListAdapter;
	private RoleSpinnerAdapter roleSpinnerAdapter;
	private List<InterestsDTO> selectedInterest = new ArrayList<InterestsDTO>();
	private List<RoleDTO> selectedRole = new ArrayList<RoleDTO>();
	private List<BodyTypeDTO> selectedBodyType = new ArrayList<BodyTypeDTO>();

	private List<String> roleStrings = new ArrayList<String>();
	private List<String> bodyTypeStrings = new ArrayList<String>();
	private List<RoleDTO> roleDTOs;
	private List<BodyTypeDTO> bodyTypeDTOs;
	private boolean deleteAcoount = true;
	private boolean isGPSReminderOn;
	// private List<WhatAreYouDataDTO> selectedWhatAreYouDTOs = new
	// ArrayList<WhatAreYouDataDTO>();
	public static boolean comeFrom = false;

	private ImageView loadingView;
	private LinearLayout llLoading;
	private AppManager singleton;

	UserDTO userDTO;
	private String storeBodyType;
	private RelativeLayout rlBodySlim, rlBodyMed, rlBodyAth, rlBodyLarge,
			rlLove, rlHookup;
	private ImageView ivBodySlim, ivBodyMed, ivBodyAth, ivBodyLarge;
	private TextView tvBodySlim, tvBodyMed, tvBodyAth, tvBodyLarge, tvLove,
			tvHookup;

	private int LOVE_TAG = 11, HOOKUP_TAG = 22, NO_TAG = 00;
	private ArrayList<String> storeLoveHookup = new ArrayList<String>();

	private ImageView imageViewCheckBoxFilterByAge,
			imageViewCheckBoxFilterByRadius,
			imageViewCheckBoxFilterByEthnicity, imageViewCheckBoxFilterByRole,
			imageViewCheckBoxFilterByInterest,
			imageViewCheckBoxFilterByBodyType,
			imageViewCheckBoxFilterByLookingFor;

	private LinearLayout linearBodyAge, linearBodyRadius, linearBodyEthnicity,
			linearBodyRole, linearBodyInterest, linearBodyBodyType,
			linearBodyLookingFor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		System.gc();
		context = (Activity_Home) getActivity();
		sessionManager = new SessionManager(context);
		userId = sessionManager.getUserDetail().getUserID();
		userDTO = sessionManager.getUserDetail();

		/* av singleton */
		AppManager.initInstance();
		singleton = AppManager.getInstance();

		setLayout(view);
		setListener();
		setTypeFace(view);
		if (comeFrom) {
			context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_SETTING;
			context.left_button.setImageResource(R.drawable.av_new_chat_backup);
			context.right_button.setVisibility(View.GONE);

			Typeface typefaceHeader = FontStyle.getFont(context,
					AppConstants.HelveticaNeueLTStd_Roman);

			context.headerIcon.setVisibility(View.GONE);
			context.title.setVisibility(View.VISIBLE);
			context.title.setText(R.string.titel_filter);
			context.title.setTypeface(typefaceHeader);

		}
		if (WebServiceConstants.isOnline(context)) {
			try {
				new GetSetting().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_SETTING);
			} catch (Exception e) {
			}
		} else {
			Toast.makeText(context,
					R.string.toast_please_check_network_connection,
					Toast.LENGTH_LONG).show();
		}

		return view;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

		if (WebServiceConstants.isOnline(context) && deleteAcoount) {
			sessionManager.setSettingDetail(settingDTO);

			if (storeLoveHookup.size() == 2) {
				settingDTO.setLoveHookup(storeLoveHookup.get(0) + ","
						+ storeLoveHookup.get(1));
			} else {
				try {
					settingDTO.setLoveHookup(storeLoveHookup.get(0));
				} catch (Exception e) {
					settingDTO.setLoveHookup("");
				}
			}
			settingDTO.setSelectedRoleList(selectedRole);
			settingDTO.setSelectedBodyTypeList(selectedBodyType);

			new UpdateSetting().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.UPDATE_SETTING);

			context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_FILTER,
					AppConstants.EVENT_LOG_ACTION_FILTER,
					(FragmentHome.filterIsOnline) ? "ShowOnlineOnly"
							: "ShowAll");
			easyTrackerEventLogWithValue(AppConstants.EVENT_LOG_CATEG_FILTER,
					AppConstants.EVENT_LOG_ACTION_FILTER, "FilterByAgeMinimum",
					settingDTO.getMinAge());
			easyTrackerEventLogWithValue(AppConstants.EVENT_LOG_CATEG_FILTER,
					AppConstants.EVENT_LOG_ACTION_FILTER, "FilterByAgeMaximum",
					settingDTO.getMaxAge());
			easyTrackerEventLogWithValue(AppConstants.EVENT_LOG_CATEG_FILTER,
					AppConstants.EVENT_LOG_ACTION_FILTER, "FilterByRadius",
					settingDTO.getSearchRadius());

			try {
				for (int x = 0; x < settingDTO.getSelectedInterestList().size(); x++) {
					context.easyTrackerEventLog(
							AppConstants.EVENT_LOG_CATEG_FILTER,
							AppConstants.EVENT_LOG_ACTION_INTEREST, settingDTO
									.getSelectedInterestList().get(x)
									.getInterestName());
				}
				for (int x = 0; x < settingDTO.getWhatAreYouDataDTOs().size(); x++) {
					context.easyTrackerEventLog(
							AppConstants.EVENT_LOG_CATEG_FILTER,
							"SelectedEthnicity", settingDTO
									.getWhatAreYouDataDTOs().get(x).getName());
				}
			} catch (Exception e) {

			}

		}
		System.gc();
		context.left_button.setImageResource(R.drawable.av_new_nav_menuup);
		context.right_button.setVisibility(View.VISIBLE);
		context.headerIcon.setVisibility(View.VISIBLE);
		context.title.setVisibility(View.GONE);

		singleton.filterDTO = settingDTO;
	}

	private void setTypeFace(View view) {
		TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView87, textView100, textViewRole, textViewBodyType, textViewLookingFor;
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		textView3 = (TextView) view.findViewById(R.id.textView3);
		textView4 = (TextView) view.findViewById(R.id.textView4);
		textView5 = (TextView) view.findViewById(R.id.textView5);
		textView6 = (TextView) view.findViewById(R.id.textView6);
		textView7 = (TextView) view.findViewById(R.id.textView7);
		textView8 = (TextView) view.findViewById(R.id.textView8);
		textView87 = (TextView) view.findViewById(R.id.textView87);
		textView100 = (TextView) view.findViewById(R.id.textView100);
		textViewRole = (TextView) view.findViewById(R.id.textViewRole);
		textViewBodyType = (TextView) view.findViewById(R.id.textViewBodyType);
		textViewLookingFor = (TextView) view
				.findViewById(R.id.textViewLookingFor);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);
		textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		textView3.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		textView7.setTypeface(typeface);
		textView8.setTypeface(typeface);
		textView87.setTypeface(typeface);
		textView100.setTypeface(typeface);
		textViewRole.setTypeface(typeface);
		textViewBodyType.setTypeface(typeface);
		textViewLookingFor.setTypeface(typeface);

		tvBodySlim.setTypeface(typeface);
		tvBodyMed.setTypeface(typeface);
		tvBodyAth.setTypeface(typeface);
		tvBodyLarge.setTypeface(typeface);

		tvLove.setTypeface(typeface);
		tvHookup.setTypeface(typeface);

		Typeface typeface1 = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		minyearText.setTypeface(typeface1);
		maxyearText.setTypeface(typeface1);
		mailesText.setTypeface(typeface1);

	}

	public void easyTrackerEventLogWithValue(String Categ, String Action,
			String Label, String Value) {
		EasyTracker.getInstance(context).set(Fields.EVENT_CATEGORY, Categ);
		EasyTracker.getInstance(context).set(Fields.EVENT_ACTION, Action);
		EasyTracker.getInstance(context).set(Fields.EVENT_LABEL, Label);
		EasyTracker.getInstance(context).set(Fields.EVENT_VALUE, Value);
		EasyTracker.getInstance(context).send(
				MapBuilder.createAppView().build());
	}

	// private void setValuesFromLocalSession() {
	// // settingDTO = sessionManager.getSettingDetail();
	// minAge = Integer.valueOf(singleton.filterDTO.getMinAge());
	// maxAge = Integer.valueOf(singleton.filterDTO.getMaxAge());
	// radius = Integer.valueOf(singleton.filterDTO.getSearchRadius());
	// isNotificationEnable = Integer.valueOf(singleton.filterDTO
	// .getIsNotificationEnable());
	// seekBar.setSelectedMaxValue(maxAge);
	// seekBar.setSelectedMinValue(minAge);
	// radiusSeekBar.setProgress((radius));
	// minyearText.setText(minAge + " Year old");
	// maxyearText.setText(maxAge + " Year old");
	// mailesText.setText(radius + " Miles");
	// if (AppConstants.NOTIFICATION_ENABLE == isNotificationEnable) {
	// enableNotificationImageView.setImageDrawable(context.getResources()
	// .getDrawable(R.drawable.select_notification));
	//
	// } else {
	// enableNotificationImageView.setImageDrawable(context.getResources()
	// .getDrawable(R.drawable.unselect_notofication));
	//
	// }
	//
	// singleton.mPrefs = context.getSharedPreferences(AppConstants.GPS_PREFS,
	// 0);
	// isGPSReminderOn =
	// singleton.mPrefs.getBoolean(AppConstants.GPS_REMINDER_ON, true);
	// System.out.println("\n isgpsreminderON : "+isGPSReminderOn);
	// if (isGPSReminderOn) {
	// enableGPSReminderImageView.setImageDrawable(context.getResources()
	// .getDrawable(R.drawable.select_notification));
	// } else {
	// enableGPSReminderImageView.setImageDrawable(context.getResources()
	// .getDrawable(R.drawable.unselect_notofication));
	// }
	//
	// settingListInterestAdapter.notifyDataSetChanged();
	// whatAreYouListAdapter.notifyDataSetChanged();
	//
	// }
	private void setValuesFromSession() {
		// settingDTO = sessionManager.getSettingDetail();
		try {
			minAge = Integer.valueOf(settingDTO.getMinAge());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			minAge = 19;
			e.printStackTrace();
		}
		try {
			maxAge = Integer.valueOf(settingDTO.getMaxAge());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			maxAge = 73;
			e.printStackTrace();
		}
		try {
			radius = Integer.valueOf(settingDTO.getSearchRadius());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			radius = 250;
			e.printStackTrace();
		}
		isNotificationEnable = Integer.valueOf(settingDTO
				.getIsNotificationEnable());
		seekBar.setSelectedMaxValue(maxAge);
		seekBar.setSelectedMinValue(minAge);
		radiusSeekBar.setProgress((radius));

		minyearText.setText(minAge
				+ getResources().getString(R.string.filter_year_old));
		maxyearText.setText(maxAge
				+ getResources().getString(R.string.filter_year_old));
		mailesText.setText(radius
				+ getResources().getString(R.string.filter_miles));

		String[] loveHookup = settingDTO.getLoveHookup().split(",");
		if (loveHookup.length == 2) {
			rlHookup.setBackgroundColor(Color.parseColor("#51ccd8"));
			tvHookup.setTextColor(Color.parseColor("#000000"));
			rlHookup.setTag(HOOKUP_TAG);

			rlLove.setBackgroundColor(Color.parseColor("#f14546"));
			tvLove.setTextColor(Color.parseColor("#000000"));
			rlLove.setTag(LOVE_TAG);
		} else {
			if (loveHookup[0].equals("1")) {
				rlLove.setBackgroundColor(Color.parseColor("#f14546"));
				tvLove.setTextColor(Color.parseColor("#000000"));
				rlLove.setTag(LOVE_TAG);

				rlHookup.setBackgroundColor(Color.parseColor("#000000"));
				tvHookup.setTextColor(Color.parseColor("#404041"));
				rlHookup.setTag(HOOKUP_TAG);
			} else if (loveHookup[0].equals("2")) {
				rlHookup.setBackgroundColor(Color.parseColor("#51ccd8"));
				tvHookup.setTextColor(Color.parseColor("#000000"));
				rlHookup.setTag(HOOKUP_TAG);

				rlLove.setBackgroundColor(Color.parseColor("#000000"));
				tvLove.setTextColor(Color.parseColor("#404041"));
				rlLove.setTag(NO_TAG);
			} else {
				rlLove.setBackgroundColor(Color.parseColor("#000000"));
				tvLove.setTextColor(Color.parseColor("#404041"));
				rlLove.setTag(NO_TAG);

				rlHookup.setBackgroundColor(Color.parseColor("#000000"));
				tvHookup.setTextColor(Color.parseColor("#404041"));
				rlHookup.setTag(HOOKUP_TAG);
			}
		}

		if (AppConstants.NOTIFICATION_ENABLE == isNotificationEnable) {
			enableNotificationImageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.apptheme_btn_radio_on_holo_light));

		} else {
			enableNotificationImageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.unselect_notofication));

		}

		singleton.mPrefs = context.getSharedPreferences(AppConstants.GPS_PREFS,
				0);
		isGPSReminderOn = singleton.mPrefs.getBoolean(
				AppConstants.GPS_REMINDER_ON, true);
		System.out.println("\n isgpsreminderON : " + isGPSReminderOn);
		if (isGPSReminderOn) {
			enableGPSReminderImageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.apptheme_btn_radio_on_holo_light));
		} else {
			enableGPSReminderImageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.unselect_notofication));
		}

		settingListInterestAdapter.notifyDataSetChanged();
		whatAreYouListAdapter.notifyDataSetChanged();

		new GetAllChoices()
				.execute("http://54.219.211.237/KraveAPI/api_calls/returnAllChoices.php");
	}

	private void setListener() {
		try {
			layoutChangePassword.setOnClickListener(this);
			layoutDeleteAccount.setOnClickListener(this);
			layoutUpdateInfo.setOnClickListener(this);
			linearLayout1.setOnClickListener(this);
			enableNotificationImageView.setOnClickListener(this);
			enableGPSReminderImageView.setOnClickListener(this);

			rlBodySlim.setOnClickListener(this);
			rlBodyMed.setOnClickListener(this);
			rlBodyAth.setOnClickListener(this);
			rlBodyLarge.setOnClickListener(this);

			rlHookup.setOnClickListener(this);
			rlLove.setOnClickListener(this);

			imageViewCheckBoxFilterByAge.setOnClickListener(this);
			imageViewCheckBoxFilterByRadius.setOnClickListener(this);
			imageViewCheckBoxFilterByEthnicity.setOnClickListener(this);
			imageViewCheckBoxFilterByRole.setOnClickListener(this);
			imageViewCheckBoxFilterByInterest.setOnClickListener(this);
			imageViewCheckBoxFilterByBodyType.setOnClickListener(this);
			imageViewCheckBoxFilterByLookingFor.setOnClickListener(this);
			clearAllFilterButton.setOnClickListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		radiusSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// progress = progress + 1;
				try {
					radius = progress;
					mailesText.setText("" + radius
							+ getResources().getString(R.string.filter_miles));
					settingDTO.setSearchRadius("" + radius);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	private void setLayout(View view) {
		/* Init Flurry Params */
		final Map<String, String> settingsParams = new HashMap<String, String>();

		mainview = (RelativeLayout) view.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		seekBarLayoutForAge = (LinearLayout) view.findViewById(R.id.seekbar2);
		minyearText = (TextView) view.findViewById(R.id.year1);
		maxyearText = (TextView) view.findViewById(R.id.year2);
		mailesText = (TextView) view.findViewById(R.id.miles);
		radiusSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
		interestGridView = (MyGridView) view.findViewById(R.id.interestList);
		ethinicityGridView = (MyGridView) view.findViewById(R.id.ethinicity);
		roleGridView = (MyGridView) view.findViewById(R.id.role);
		layoutHideAndShow = (LinearLayout) view.findViewById(R.id.footer);
		enableNotificationImageView = (ImageView) view
				.findViewById(R.id.notification);
		enableGPSReminderImageView = (ImageView) view
				.findViewById(R.id.reminder);
		layoutUpdateInfo = (LinearLayout) view.findViewById(R.id.updateInfo);
		layoutChangePassword = (LinearLayout) view
				.findViewById(R.id.changePassword);
		layoutDeleteAccount = (LinearLayout) view
				.findViewById(R.id.deleteAccount);
		clearAllFilterButton = (RelativeLayout) view
				.findViewById(R.id.clearAllFillterButton);
		linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
		onlineDudesFilter = (ImageView) view.findViewById(R.id.filter);
		if (FragmentHome.filterIsOnline) {
			onlineDudesFilter.setBackgroundResource(R.drawable.btn_online);
			FragmentHome.filterIsOnline = true;
		} else {
			onlineDudesFilter.setBackgroundResource(R.drawable.btn_offline);
			FragmentHome.filterIsOnline = false;
		}
		radiusSeekBar.setMax(MAX_VALUE);
		setSeekBarForAge();
		settingListInterestAdapter = new SettingListInterestAdapter(context,
				(ArrayList<InterestsDTO>) interestsDTOs,
				AppConstants.ADAPTER_FLAG_EDIT_PROFILE);
		interestGridView.setAdapter(settingListInterestAdapter);

		whatAreYouListAdapter = new WhatAreYouListAdapter(context,
				whatAreYouDTOs, 1);
		ethinicityGridView.setAdapter(whatAreYouListAdapter);
		ethinicityGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				whatAreYouDTOs.get(pos).setSelected(
						!whatAreYouDTOs.get(pos).isSelected());

				Log.d("", "position=" + pos);

				whatAreYouListAdapter.notifyDataSetChanged();
				logFlurryEvent(settingsParams, "Filter Ethnicity");
			}

		});
		interestGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				InterestsDTO interestsDTO = interestsDTOs.get(position);

				if (interestsDTO.getIsSelected()) {
					selectedInterest.remove(interestsDTO);
					interestsDTO.setIsSelected(false);
				} else {
					interestsDTO.setIsSelected(true);
					selectedInterest.add(interestsDTO);

				}
				settingListInterestAdapter.notifyDataSetChanged();
				// gridViewAdapter = new GridViewAdapter(context,
				// (ArrayList<InterestsDTO>) interestsDTOs, 1);
				// horizontalListView.setAdapter(gridViewAdapter);

				logFlurryEvent(settingsParams, "Filter Interest");
			}
		});

		loadingView = (ImageView) view.findViewById(R.id.loadingView);
		llLoading = (LinearLayout) view.findViewById(R.id.llLoading);

		rlBodySlim = (RelativeLayout) view.findViewById(R.id.rlBodySlim);
		rlBodyMed = (RelativeLayout) view.findViewById(R.id.rlBodyMed);
		rlBodyAth = (RelativeLayout) view.findViewById(R.id.rlBodyAth);
		rlBodyLarge = (RelativeLayout) view.findViewById(R.id.rlBodyLarge);

		ivBodySlim = (ImageView) view.findViewById(R.id.ivBodySlim);
		ivBodyMed = (ImageView) view.findViewById(R.id.ivBodyMed);
		ivBodyAth = (ImageView) view.findViewById(R.id.ivBodyAth);
		ivBodyLarge = (ImageView) view.findViewById(R.id.ivBodyLarge);

		tvBodySlim = (TextView) view.findViewById(R.id.tvBodySlim);
		tvBodyMed = (TextView) view.findViewById(R.id.tvBodyMed);
		tvBodyAth = (TextView) view.findViewById(R.id.tvBodyAth);
		tvBodyLarge = (TextView) view.findViewById(R.id.tvBodyLarge);

		ivBodySlim.setTag(R.drawable.av_bodytype_slim_up);
		ivBodyMed.setTag(R.drawable.av_bodytype_medium_up);
		ivBodyAth.setTag(R.drawable.av_bodytype_athletic_up);
		ivBodyLarge.setTag(R.drawable.av_bodytype_large_up);

		rlHookup = (RelativeLayout) view.findViewById(R.id.rlHookup);
		rlLove = (RelativeLayout) view.findViewById(R.id.rlLove);

		tvHookup = (TextView) view.findViewById(R.id.tvHookup);
		tvLove = (TextView) view.findViewById(R.id.tvLove);

		rlLove.setTag(LOVE_TAG); // temp
		rlHookup.setTag(HOOKUP_TAG);

		linearBodyAge = (LinearLayout) view.findViewById(R.id.linearBodyAge);
		linearBodyRadius = (LinearLayout) view
				.findViewById(R.id.linearBodyRadius);
		linearBodyEthnicity = (LinearLayout) view
				.findViewById(R.id.linearBodyEthnicity);
		linearBodyRole = (LinearLayout) view.findViewById(R.id.linearBodyRole);
		linearBodyInterest = (LinearLayout) view
				.findViewById(R.id.linearBodyInterest);
		linearBodyBodyType = (LinearLayout) view
				.findViewById(R.id.linearBodyBodyType);
		linearBodyLookingFor = (LinearLayout) view
				.findViewById(R.id.linearBodyLookingFor);

		imageViewCheckBoxFilterByAge = (ImageView) view
				.findViewById(R.id.check_box_filter_by_age);
		imageViewCheckBoxFilterByRadius = (ImageView) view
				.findViewById(R.id.check_box_filter_by_radius);
		imageViewCheckBoxFilterByEthnicity = (ImageView) view
				.findViewById(R.id.check_box_filter_by_ethnicity);
		imageViewCheckBoxFilterByRole = (ImageView) view
				.findViewById(R.id.check_box_filter_by_role);
		imageViewCheckBoxFilterByInterest = (ImageView) view
				.findViewById(R.id.check_box_filter_by_interest);
		imageViewCheckBoxFilterByBodyType = (ImageView) view
				.findViewById(R.id.check_box_filter_by_body_type);
		imageViewCheckBoxFilterByLookingFor = (ImageView) view
				.findViewById(R.id.check_box_filter_by_looking_for);

		boolean isCheckBoxFilterByAgeChecked = singleton.mFilterPrefs
				.getBoolean(AppConstants.FILTER_PREFS_AGE, false);
		boolean isCheckBoxFilterByRadiuesChecked = singleton.mFilterPrefs
				.getBoolean(AppConstants.FILTER_PREFS_RADIUS, false);
		boolean isCheckBoxFilterByEthnicityChecked = singleton.mFilterPrefs
				.getBoolean(AppConstants.FILTER_PREFS_ETHNICITY, false);
		boolean isCheckBoxFilterByRoleChecked = singleton.mFilterPrefs
				.getBoolean(AppConstants.FILTER_PREFS_ROLE, false);
		boolean isCheckBoxFilterByInterestChecked = singleton.mFilterPrefs
				.getBoolean(AppConstants.FILTER_PREFS_INTEREST, false);
		boolean isCheckBoxFilterByBodyTypeChecked = singleton.mFilterPrefs
				.getBoolean(AppConstants.FILTER_PREFS_BODY_TYPE, false);
		boolean isCheckBoxFilterByLookingForChecked = singleton.mFilterPrefs
				.getBoolean(AppConstants.FILTER_PREFS_LOOKING_FOR, false);

		this.willShowFilter(imageViewCheckBoxFilterByAge, linearBodyAge,
				isCheckBoxFilterByAgeChecked);
		this.willShowFilter(imageViewCheckBoxFilterByRadius, linearBodyRadius,
				isCheckBoxFilterByRadiuesChecked);
		this.willShowFilter(imageViewCheckBoxFilterByEthnicity,
				linearBodyEthnicity, isCheckBoxFilterByEthnicityChecked);
		this.willShowFilter(imageViewCheckBoxFilterByRole, linearBodyRole,
				isCheckBoxFilterByRoleChecked);
		this.willShowFilter(imageViewCheckBoxFilterByInterest,
				linearBodyInterest, isCheckBoxFilterByInterestChecked);
		this.willShowFilter(imageViewCheckBoxFilterByBodyType,
				linearBodyBodyType, isCheckBoxFilterByBodyTypeChecked);
		this.willShowFilter(imageViewCheckBoxFilterByLookingFor,
				linearBodyLookingFor, isCheckBoxFilterByLookingForChecked);

		// imageViewCheckBoxFilterByAge.setImageResource(this.checkBox(isCheckBoxFilterByAgeChecked));
		// imageViewCheckBoxFilterByRadius.setImageResource(this.checkBox(isCheckBoxFilterByRadiuesChecked));
		// imageViewCheckBoxFilterByEthnicity.setImageResource(this.checkBox(isCheckBoxFilterByEthnicityChecked));
		// imageViewCheckBoxFilterByRole.setImageResource(this.checkBox(isCheckBoxFilterByRoleChecked));
		// imageViewCheckBoxFilterByInterest.setImageResource(this.checkBox(isCheckBoxFilterByInterestChecked));
		// imageViewCheckBoxFilterByBodyType.setImageResource(this.checkBox(isCheckBoxFilterByBodyTypeChecked));
		// imageViewCheckBoxFilterByLookingFor.setImageResource(this.checkBox(isCheckBoxFilterByLookingForChecked));
	}

	private void willShowFilter(ImageView imageView, LinearLayout view,
			boolean willShow) {
		imageView.setImageResource(this.checkBox(willShow));
		this.willShowView(view, willShow);
	}

	private void willShowView(LinearLayout view, boolean willShow) {
		if (willShow) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	private int checkBox(boolean checked) {
		return checked ? R.drawable.filter_checkbox_down
				: R.drawable.filter_checkbox_up;
	}

	private void setSeekBarForAge() {

		seekBar = new RangeSeekBar<Integer>(18, 75, getActivity());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// handle changed range values
				// Log.i(TAG, "User selected new range values: MIN=" + minValue
				// + ", MAX=" + maxValue);
				minAge = minValue;
				maxAge = maxValue;
				settingDTO.setMaxAge("" + maxAge);
				settingDTO.setMinAge("" + minAge);
				minyearText.setText(minValue
						+ getResources().getString(R.string.filter_year_old));
				maxyearText.setText(maxValue
						+ getResources().getString(R.string.filter_year_old));
			}
		});

		// add RangeSeekBar to pre-defined layout

		seekBarLayoutForAge.addView(seekBar);
		seekBar.setSelectedMinValue(10);
		seekBar.setSelectedMaxValue(60);

	}

	public void bodyType(int ivRes, ImageView iv, TextView tv, int bodyId,
			boolean selected) {
		// ivBodySlim.setImageResource(R.drawable.av_bodytype_slim_up);
		// ivBodyMed.setImageResource(R.drawable.av_bodytype_medium_up);
		// ivBodyAth.setImageResource(R.drawable.av_bodytype_athletic_up);
		// ivBodyLarge.setImageResource(R.drawable.av_bodytype_large_up);
		//
		// ivBodySlim.setTag(R.drawable.av_bodytype_slim_up);
		// ivBodyMed.setTag(R.drawable.av_bodytype_medium_up);
		// ivBodyAth.setTag(R.drawable.av_bodytype_athletic_up);
		// ivBodyLarge.setTag(R.drawable.av_bodytype_large_up);
		//
		// tvBodySlim.setTextColor(Color.parseColor("#e2e2e2"));
		// tvBodyMed.setTextColor(Color.parseColor("#e2e2e2"));
		// tvBodyAth.setTextColor(Color.parseColor("#e2e2e2"));
		// tvBodyLarge.setTextColor(Color.parseColor("#e2e2e2"));
		//
		if (selected) {
			iv.setImageResource(ivRes);
			iv.setTag(ivRes);
			tv.setTextColor(Color.parseColor("#404041"));

			BodyTypeDTO bodyDTO = bodyTypeDTOs.get(bodyId - 1);
			bodyDTO.setBodyTypeId("" + bodyId);
			// bodyDTO.setBodyTypeName("");
			bodyDTO.setIsSelected(true);
		} else {
			int body1 = R.drawable.av_bodytype_slim_down, body2 = R.drawable.av_bodytype_medium_down, body3 = R.drawable.av_bodytype_athletic_down, body4 = R.drawable.av_bodytype_large_down;

			BodyTypeDTO bodyDTO = bodyTypeDTOs.get(bodyId - 1);
			bodyDTO.setBodyTypeId("" + bodyId);
			// bodyDTO.setBodyTypeName("");

			if ((Integer) iv.getTag() == body1) {
				iv.setImageResource(R.drawable.av_bodytype_slim_up);
				iv.setTag(R.drawable.av_bodytype_slim_up);
				tv.setTextColor(Color.parseColor("#e2e2e2"));
				bodyDTO.setIsSelected(false);
				selectedBodyType.remove(bodyDTO);
			} else if ((Integer) iv.getTag() == body2) {
				iv.setImageResource(R.drawable.av_bodytype_medium_up);
				iv.setTag(R.drawable.av_bodytype_medium_up);
				tv.setTextColor(Color.parseColor("#e2e2e2"));
				bodyDTO.setIsSelected(false);
				selectedBodyType.remove(bodyDTO);
			} else if ((Integer) iv.getTag() == body3) {
				iv.setImageResource(R.drawable.av_bodytype_athletic_up);
				iv.setTag(R.drawable.av_bodytype_athletic_up);
				tv.setTextColor(Color.parseColor("#e2e2e2"));
				bodyDTO.setIsSelected(false);
				selectedBodyType.remove(bodyDTO);
			} else if ((Integer) iv.getTag() == body4) {
				iv.setImageResource(R.drawable.av_bodytype_large_up);
				iv.setTag(R.drawable.av_bodytype_large_up);
				tv.setTextColor(Color.parseColor("#e2e2e2"));
				bodyDTO.setIsSelected(false);
				selectedBodyType.remove(bodyDTO);
			} else {
				iv.setImageResource(ivRes);
				iv.setTag(ivRes);
				tv.setTextColor(Color.parseColor("#404041"));

				bodyDTO.setIsSelected(true);
				selectedBodyType.add(bodyDTO);
			}
		}
		// storeBodyType = ""+bodyId;
	}

	private void toggleCheckBox(ImageView imageView, LinearLayout view,
			String filterName, SharedPreferences prefs) {
		Editor e = prefs.edit();
		if (prefs.getBoolean(filterName, false)) {
			e.putBoolean(filterName, false);
			this.willShowFilter(imageView, view, false);
		} else {
			e.putBoolean(filterName, true);
			this.willShowFilter(imageView, view, true);
		}
		e.commit();
	}

	private void toggleCheckBoxForClearButton(ImageView imageView,
			LinearLayout view, String filterName, SharedPreferences prefs) {
		Editor e = prefs.edit();
		if (prefs.getBoolean(filterName, false)) {
			e.putBoolean(filterName, false);
			this.willShowFilter(imageView, view, false);
		}
		e.commit();
	}

	@Override
	public void onClick(View v) {
		Map<String, String> settingsParams = new HashMap<String, String>();

		SharedPreferences filterPrefs = singleton.mFilterPrefs;

		switch (v.getId()) {

		case R.id.check_box_filter_by_age:
			this.toggleCheckBox(imageViewCheckBoxFilterByAge, linearBodyAge,
					AppConstants.FILTER_PREFS_AGE, filterPrefs);
			break;
		case R.id.check_box_filter_by_radius:
			this.toggleCheckBox(imageViewCheckBoxFilterByRadius,
					linearBodyRadius, AppConstants.FILTER_PREFS_RADIUS,
					filterPrefs);
			break;
		case R.id.check_box_filter_by_ethnicity:
			this.toggleCheckBox(imageViewCheckBoxFilterByEthnicity,
					linearBodyEthnicity, AppConstants.FILTER_PREFS_ETHNICITY,
					filterPrefs);
			break;
		case R.id.check_box_filter_by_role:
			this.toggleCheckBox(imageViewCheckBoxFilterByRole, linearBodyRole,
					AppConstants.FILTER_PREFS_ROLE, filterPrefs);
			break;
		case R.id.check_box_filter_by_interest:
			this.toggleCheckBox(imageViewCheckBoxFilterByInterest,
					linearBodyInterest, AppConstants.FILTER_PREFS_INTEREST,
					filterPrefs);
			break;
		case R.id.check_box_filter_by_body_type:
			this.toggleCheckBox(imageViewCheckBoxFilterByBodyType,
					linearBodyBodyType, AppConstants.FILTER_PREFS_BODY_TYPE,
					filterPrefs);
			break;
		case R.id.check_box_filter_by_looking_for:
			this.toggleCheckBox(imageViewCheckBoxFilterByLookingFor,
					linearBodyLookingFor,
					AppConstants.FILTER_PREFS_LOOKING_FOR, filterPrefs);
			break;
		case R.id.rlBodySlim:
			bodyType(R.drawable.av_bodytype_slim_down, ivBodySlim, tvBodySlim,
					1, false);
			break;
		case R.id.rlBodyMed:
			bodyType(R.drawable.av_bodytype_medium_down, ivBodyMed, tvBodyMed,
					2, false);
			break;
		case R.id.rlBodyAth:
			bodyType(R.drawable.av_bodytype_athletic_down, ivBodyAth,
					tvBodyAth, 3, false);
			break;
		case R.id.rlBodyLarge:
			bodyType(R.drawable.av_bodytype_large_down, ivBodyLarge,
					tvBodyLarge, 4, false);
			break;

		case R.id.rlLove:
			if ((Integer) rlLove.getTag() == NO_TAG) {
				storeLoveHookup.add("1");
				rlLove.setBackgroundColor(Color.parseColor("#f14546"));
				tvLove.setTextColor(Color.parseColor("#000000"));
				rlLove.setTag(LOVE_TAG);
			} else {
				storeLoveHookup.clear();
				if ((Integer) rlHookup.getTag() == HOOKUP_TAG) {
					storeLoveHookup.add("2");
					rlLove.setBackgroundColor(Color.parseColor("#000000"));
					tvLove.setTextColor(Color.parseColor("#404041"));
					rlLove.setTag(NO_TAG);
				} else {
					Toast.makeText(context,
							R.string.toast_at_least_one_must_be_selected,
							Toast.LENGTH_SHORT).show();

				}

			}

			break;
		case R.id.rlHookup:
			if ((Integer) rlHookup.getTag() == NO_TAG) {
				storeLoveHookup.add("2");
				rlHookup.setBackgroundColor(Color.parseColor("#51ccd8"));
				tvHookup.setTextColor(Color.parseColor("#000000"));
				rlHookup.setTag(HOOKUP_TAG);
			} else {
				storeLoveHookup.clear();
				if ((Integer) rlLove.getTag() == LOVE_TAG) {
					storeLoveHookup.add("1");
					rlHookup.setBackgroundColor(Color.parseColor("#000000"));
					tvHookup.setTextColor(Color.parseColor("#404041"));
					rlHookup.setTag(NO_TAG);
				} else {
					Toast.makeText(context,
							R.string.toast_at_least_one_must_be_selected,
							Toast.LENGTH_SHORT).show();
				}

			}
			break;
		case R.id.linearLayout1:
			if (FragmentHome.filterIsOnline) {
				onlineDudesFilter.setBackgroundResource(R.drawable.btn_offline);
				// findDudesAdapter.getFilter().filter(AppConstants.ALL_DUDE);
				FragmentHome.filterIsOnline = false;
				// calculatePages(userDTOs.size());
				// viewPager.setCurrentItem(0);

			} else {

				onlineDudesFilter.setBackgroundResource(R.drawable.btn_online);
				// findDudesAdapter.getFilter().filter(AppConstants.ONLINE);
				FragmentHome.filterIsOnline = true;

				// viewPager.setCurrentItem(0);
			}
			break;
		case R.id.changePassword:
			logFlurryEvent(settingsParams, "Change Password");
			Intent intent = new Intent(context, Activity_ChangePassword.class);
			startActivity(intent);
			break;
		case R.id.deleteAccount:
			logFlurryEvent(settingsParams, "Delete Account");
			openDailog();
			break;
		case R.id.updateInfo:
			logFlurryEvent(settingsParams, "Update Info");
			// if (WebServiceConstants.isOnline(context)) {
			// Intent intent1 = new Intent(context,
			// Activity_ProfileDetails.class);
			// intent1.putExtra(AppConstants.COME_FROM,
			// AppConstants.COME_FROM_UPDATE_PROFILE);
			// startActivity(intent1);
			// }
			context.Attach_Fragment(AppConstants.FRAGMENT_UPDATE_PROFILE);
			break;
		case R.id.reminder:
			Editor e = singleton.mPrefs.edit();
			if (isGPSReminderOn) {
				enableGPSReminderImageView.setImageDrawable(context
						.getResources().getDrawable(
								R.drawable.unselect_notofication));
				e.putBoolean(AppConstants.GPS_REMINDER_ON, false);
				isGPSReminderOn = false;

				logFlurryEvent(settingsParams, "Disable GPS Reminder");
			} else {
				enableGPSReminderImageView.setImageDrawable(context
						.getResources().getDrawable(
								R.drawable.apptheme_btn_radio_on_holo_light));
				e.putBoolean(AppConstants.GPS_REMINDER_ON, true);
				isGPSReminderOn = true;

				logFlurryEvent(settingsParams, "Enable GPS Reminder");
			}

			singleton.gpsReminderCounter = 0;// reset
			e.commit();
			break;
		case R.id.notification:
			if (AppConstants.NOTIFICATION_ENABLE == isNotificationEnable) {
				enableNotificationImageView.setImageDrawable(context
						.getResources().getDrawable(
								R.drawable.unselect_notofication));
				isNotificationEnable = AppConstants.NOTIFICATION_DISABLE;

				logFlurryEvent(settingsParams, "Disable Notification");
			} else {
				enableNotificationImageView.setImageDrawable(context
						.getResources().getDrawable(
								R.drawable.apptheme_btn_radio_on_holo_light));
				isNotificationEnable = AppConstants.NOTIFICATION_ENABLE;

				logFlurryEvent(settingsParams, "Enable Notification");
			}
			settingDTO.setIsNotificationEnable("" + isNotificationEnable);
			break;
		// case R.id.backimageethinicity:
		// int index2 = ethinicityHorizontalListView.Getposition();

		// ethinicityHorizontalListView.scrollTo(index2 + 50);
		// break;
		// case R.id.nextimageethinicity:
		// int index3 = ethinicityHorizontalListView.Getposition();

		// ethinicityHorizontalListView.scrollTo(index3 - 50);
		// break;
		case R.id.clearAllFillterButton:

			this.toggleCheckBoxForClearButton(imageViewCheckBoxFilterByAge,
					linearBodyAge, AppConstants.FILTER_PREFS_AGE, filterPrefs);

			this.toggleCheckBoxForClearButton(imageViewCheckBoxFilterByRadius,
					linearBodyRadius, AppConstants.FILTER_PREFS_RADIUS,
					filterPrefs);

			this.toggleCheckBoxForClearButton(
					imageViewCheckBoxFilterByEthnicity, linearBodyEthnicity,
					AppConstants.FILTER_PREFS_ETHNICITY, filterPrefs);

			this.toggleCheckBoxForClearButton(imageViewCheckBoxFilterByRole,
					linearBodyRole, AppConstants.FILTER_PREFS_ROLE, filterPrefs);

			this.toggleCheckBoxForClearButton(
					imageViewCheckBoxFilterByInterest, linearBodyInterest,
					AppConstants.FILTER_PREFS_INTEREST, filterPrefs);

			this.toggleCheckBoxForClearButton(
					imageViewCheckBoxFilterByBodyType, linearBodyBodyType,
					AppConstants.FILTER_PREFS_BODY_TYPE, filterPrefs);

			this.toggleCheckBoxForClearButton(
					imageViewCheckBoxFilterByLookingFor, linearBodyLookingFor,
					AppConstants.FILTER_PREFS_LOOKING_FOR, filterPrefs);

			break;
		default:
			break;
		}

	}

	public void logFlurryEvent(Map<String, String> settingsParams, String event) {
		settingsParams.put("Title", event);
		FlurryAgent.logEvent("Setting Buttons", settingsParams); // Edited
																	// revision
																	// 582
																	// commented
	}

	class GetSetting extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			singleton.progressLoading(loadingView, llLoading);
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", "" + userId));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("Get setting response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			// [{"s_no":"1","user_id":"1","min_value":"25","max_value":"45","radius":"150","interest":"1,2,3","notification":"0"}]
			try {
				// singleton.stopLoading(llLoading);

				List<String> interestsStrings = new ArrayList<String>();
				List<String> wruStrings = new ArrayList<String>();

				JSONObject mJsonObject = jsonArray.getJSONObject(0);

				JSONArray jsonArray1 = mJsonObject.getJSONArray("setting");
				JSONArray jsonArray2 = mJsonObject.getJSONArray("intarest");
				JSONArray jsonArray3 = mJsonObject.getJSONArray("wru");
				// JCv
				JSONArray filterArray = mJsonObject.getJSONArray("filter");
				JSONObject filterInfo = filterArray.getJSONObject(0);

				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject mmJsonObject = jsonArray1.getJSONObject(i);
					settingDTO = new SettingDTO();
					settingDTO.setUserID(mmJsonObject.getString("user_id"));
					settingDTO.setMinAge(mmJsonObject.getString("min_value"));
					settingDTO.setMaxAge(mmJsonObject.getString("max_value"));
					settingDTO
							.setSearchRadius(mmJsonObject.getString("radius"));
					settingDTO.setIsNotificationEnable(mmJsonObject
							.getString("notification"));

					settingDTO.setLoveHookup(mmJsonObject
							.getString("love_hookup"));
					TransitDTO tDTO = new TransitDTO();
					tDTO.setTransitId(mmJsonObject.getString("transit"));
					settingDTO.setChatHistoryPeriod(mmJsonObject
							.getString("chat_history"));
					settingDTO.setFindDudesColumnCoun(mmJsonObject
							.getString("find_guy"));
					String bodyType[] = mmJsonObject.getString("body_type")
							.split(",");
					for (int x = 0; x < bodyType.length; x++) {
						bodyTypeStrings.add(bodyType[x]);
					}

					String loveHookup[] = mmJsonObject.getString("love_hookup")
							.split(",");
					for (int x = 0; x < loveHookup.length; x++) {
						storeLoveHookup.add(loveHookup[x]);
					}

					String role[] = mmJsonObject.getString("role").split(",");
					for (int x = 0; x < role.length; x++) {
						roleStrings.add(role[x]);
					}

					String interest[] = mmJsonObject.getString("interest")
							.split(",");
					for (int k = 0; k < interest.length; k++) {

						interestsStrings.add(interest[k]);
					}

					String wru[] = mmJsonObject.getString("ethencity").split(
							",");

					for (int k = 0; k < wru.length; k++) {

						wruStrings.add(wru[k]);
					}

				}

				for (int i = 0; i < jsonArray2.length(); i++) {
					JSONObject mmJsonObject = jsonArray2.getJSONObject(i);
					InterestsDTO interestsDTO = new InterestsDTO();
					interestsDTO.setInterestId(mmJsonObject
							.getString("intrests_id"));
					interestsDTO.setInterestName(mmJsonObject
							.getString("intrests_name"));

					// edited for revision 607
					// MAY 15 2015
					// added if statement
					if (!mmJsonObject.getString("intrests_image").isEmpty()) {
						interestsDTO
								.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
										+ mmJsonObject
												.getString("intrests_image"));
					}
					// - - - - -- -
					interestsDTO.setIsSelected(false);
					String temp = mmJsonObject.getString("intrests_id");
					Log.d("", "json interest id=" + temp);

					for (int k = 0; k < interestsStrings.size(); k++) {
						// InterestsDTO dto = userDTO.getInterestList().get(k);
						Log.d("", "dto interest id=" + interestsStrings.get(k));
						if (interestsStrings.get(k).equals(temp)) {
							Log.d("", "interest selection =true " + temp);
							interestsDTO.setIsSelected(true);
							selectedInterest.add(interestsDTO);
						}

					}

					interestsDTOs.add(interestsDTO);

				}

				for (int i = 0; i < jsonArray3.length(); i++) {
					JSONObject object = jsonArray3.getJSONObject(i);
					WhatAreYouDataDTO whatAreYouDataDTO = new WhatAreYouDataDTO();
					whatAreYouDataDTO.setId(object.getString("id"));
					whatAreYouDataDTO.setName(object.getString("name"));
					whatAreYouDataDTO.setSelected(false);
					String temp = object.getString("id");

					for (int k = 0; k < wruStrings.size(); k++) {

						if (wruStrings.get(k).equals(temp)) {
							Log.d("", "interest selection =true " + temp);
							whatAreYouDataDTO.setSelected(true);

						}

					}
					whatAreYouDTOs.add(whatAreYouDataDTO);
				}

				SharedPreferences filterPrefs = singleton.mFilterPrefs;
				Editor e = filterPrefs.edit();

				if (filterInfo.getString("by_role").equalsIgnoreCase("1")) {
					willShowFilter(imageViewCheckBoxFilterByRole,
							linearBodyRole, true);
					e.putBoolean(AppConstants.FILTER_PREFS_ROLE, true);
				} else {
					willShowFilter(imageViewCheckBoxFilterByRole,
							linearBodyRole, false);
					e.putBoolean(AppConstants.FILTER_PREFS_ROLE, false);
				}

				if (filterInfo.getString("by_bodytype").equalsIgnoreCase("1")) {
					willShowFilter(imageViewCheckBoxFilterByBodyType,
							linearBodyBodyType, true);
					e.putBoolean(AppConstants.FILTER_PREFS_BODY_TYPE, true);
				} else {
					willShowFilter(imageViewCheckBoxFilterByBodyType,
							linearBodyBodyType, false);
					e.putBoolean(AppConstants.FILTER_PREFS_BODY_TYPE, false);
				}

				if (filterInfo.getString("by_lovehookup").equalsIgnoreCase("1")) {
					willShowFilter(imageViewCheckBoxFilterByLookingFor,
							linearBodyLookingFor, true);
					e.putBoolean(AppConstants.FILTER_PREFS_LOOKING_FOR, true);
				} else {
					willShowFilter(imageViewCheckBoxFilterByLookingFor,
							linearBodyLookingFor, false);
					e.putBoolean(AppConstants.FILTER_PREFS_LOOKING_FOR, false);
				}

				if (filterInfo.getString("by_age").equalsIgnoreCase("1")) {
					willShowFilter(imageViewCheckBoxFilterByAge, linearBodyAge,
							true);
					e.putBoolean(AppConstants.FILTER_PREFS_AGE, true);
				} else {
					willShowFilter(imageViewCheckBoxFilterByAge, linearBodyAge,
							false);
					e.putBoolean(AppConstants.FILTER_PREFS_AGE, false);
				}

				if (filterInfo.getString("by_radius").equalsIgnoreCase("1")) {
					willShowFilter(imageViewCheckBoxFilterByRadius,
							linearBodyRadius, true);
					e.putBoolean(AppConstants.FILTER_PREFS_RADIUS, true);
				} else {
					willShowFilter(imageViewCheckBoxFilterByRadius,
							linearBodyRadius, false);
					e.putBoolean(AppConstants.FILTER_PREFS_RADIUS, false);
				}

				if (filterInfo.getString("by_interest").equalsIgnoreCase("1")) {
					willShowFilter(imageViewCheckBoxFilterByInterest,
							linearBodyInterest, true);
					e.putBoolean(AppConstants.FILTER_PREFS_INTEREST, true);
				} else {
					willShowFilter(imageViewCheckBoxFilterByInterest,
							linearBodyInterest, false);
					e.putBoolean(AppConstants.FILTER_PREFS_INTEREST, false);
				}

				if (filterInfo.getString("by_ethnicity").equalsIgnoreCase("1")) {
					willShowFilter(imageViewCheckBoxFilterByEthnicity,
							linearBodyEthnicity, true);
					e.putBoolean(AppConstants.FILTER_PREFS_ETHNICITY, true);
				} else {
					willShowFilter(imageViewCheckBoxFilterByEthnicity,
							linearBodyEthnicity, false);
					e.putBoolean(AppConstants.FILTER_PREFS_ETHNICITY, false);
				}

				e.commit();

				settingDTO.setSelectedInterestList(selectedInterest);
				settingDTO.setWhatAreYouDataDTOs(whatAreYouDTOs);

				// if(singleton.filterDTO != null)
				// setValuesFromLocalSession();
				// else
				setValuesFromSession();

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
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

			params.add(new BasicNameValuePair("userid", "" + userId));

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
					deleteAcoount = false;
					new XmppDeleteUserAsynchTask().execute(
							WebServiceConstants.XMPP_USER_REGISTRATION, userId);
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

				// HttpClient client = new DefaultHttpClient();
				// HttpPost post = new HttpPost(args[0]);
				//
				// MultipartEntity reqEntity = new MultipartEntity(
				// HttpMultipartMode.BROWSER_COMPATIBLE);
				// reqEntity.addPart("type", new StringBody("add"));
				// reqEntity.addPart("secret", new StringBody("7bDFD4TP"));
				// reqEntity.addPart("username",
				// new StringBody(userDTO.getUserID()));
				// reqEntity.addPart("password",
				// new StringBody(userDTO.getPassword()));
				// reqEntity.addPart("name", new
				// StringBody(userDTO.getFirstName()
				// + " " + userDTO.getLastName()));
				// reqEntity.addPart("email", new
				// StringBody(userDTO.getEmail()));
				//
				// post.setEntity(reqEntity);
				// HttpResponse response = client.execute(post);
				// Log.d("", "" + response.toString());
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

	public class UpdateSetting extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("update setting request", "update setting request");

		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {
				// SettingDTO settingDTO = FragmentSetting.settingDTO; //edited
				// on revision 582 commented : culprit
				String interest = "";
				if (settingDTO != null) { // if statement is added - edited on
											// revision 583
					for (int i = 0; i < settingDTO.getSelectedInterestList()
							.size(); i++) {
						interest = interest
								+ settingDTO.getSelectedInterestList().get(i)
										.getInterestId() + ",";
					}
				}
				String ethnicity = "";

				if (settingDTO != null) { // if statement is added - edited on
											// revision 583
					for (int i = 0; i < settingDTO.getWhatAreYouDataDTOs()
							.size(); i++) {
						if (settingDTO.getWhatAreYouDataDTOs().get(i)
								.isSelected()) {
							ethnicity = ethnicity
									+ settingDTO.getWhatAreYouDataDTOs().get(i)
											.getId() + ",";
						}
					}
				}
				Log.d("", "interest=" + interest);
				Log.d("", "ethnicity=" + ethnicity);

				String bodyType = "";
				for (int i = 0; i < settingDTO.getSelectedBodyTypeList().size(); i++) {
					bodyType = bodyType
							+ settingDTO.getSelectedBodyTypeList().get(i)
									.getBodyTypeId() + ",";
				}
				System.out.println("body : " + bodyType);
				String role = "";
				for (int i = 0; i < settingDTO.getSelectedRoleList().size(); i++) {
					role = role
							+ settingDTO.getSelectedRoleList().get(i)
									.getRoleId() + ",";
				}

				// params.add(new BasicNameValuePair("action", "login_user"));
				// http://parkhya.org/Android/krave_app/index.php?action=showsetting&userid=1

				params.add(new BasicNameValuePair("userid", "" + userId));
				params.add(new BasicNameValuePair("min_val", ""
						+ settingDTO.getMinAge()));
				params.add(new BasicNameValuePair("max_val", ""
						+ settingDTO.getMaxAge()));
				params.add(new BasicNameValuePair("radius", ""
						+ settingDTO.getSearchRadius()));
				params.add(new BasicNameValuePair("interest", "" + interest));
				params.add(new BasicNameValuePair("ethencity", "" + ethnicity));
				Log.d("", "notification" + settingDTO.getIsNotificationEnable());
				params.add(new BasicNameValuePair("notification", ""
						+ settingDTO.getIsNotificationEnable()));
				params.add(new BasicNameValuePair("body_type", bodyType));

				params.add(new BasicNameValuePair("love_hookup", settingDTO
						.getLoveHookup()));
				params.add(new BasicNameValuePair("role", role));

				Log.d("JCv", "saving shit");
				// JCv - new params
				SharedPreferences filterPrefs = singleton.mFilterPrefs;
				if (filterPrefs
						.getBoolean(AppConstants.FILTER_PREFS_AGE, false)) {
					params.add(new BasicNameValuePair("by_age", "" + 1));
				} else {
					params.add(new BasicNameValuePair("by_age", "" + 0));
				}

				if (filterPrefs.getBoolean(AppConstants.FILTER_PREFS_ROLE,
						false)) {
					params.add(new BasicNameValuePair("by_role", "" + 1));
				} else {
					params.add(new BasicNameValuePair("by_role", "" + 0));
				}

				if (filterPrefs.getBoolean(AppConstants.FILTER_PREFS_BODY_TYPE,
						false)) {
					params.add(new BasicNameValuePair("by_bodytype", "" + 1));
				} else {
					params.add(new BasicNameValuePair("by_bodytype", "" + 0));
				}

				if (filterPrefs.getBoolean(
						AppConstants.FILTER_PREFS_LOOKING_FOR, false)) {
					params.add(new BasicNameValuePair("by_lovehookup", "" + 1));
				} else {
					params.add(new BasicNameValuePair("by_lovehookup", "" + 0));
				}

				if (filterPrefs.getBoolean(AppConstants.FILTER_PREFS_RADIUS,
						false)) {
					params.add(new BasicNameValuePair("by_radius", "" + 1));
				} else {
					params.add(new BasicNameValuePair("by_radius", "" + 0));
				}

				if (filterPrefs.getBoolean(AppConstants.FILTER_PREFS_INTEREST,
						false)) {
					params.add(new BasicNameValuePair("by_interest", "" + 1));
				} else {
					params.add(new BasicNameValuePair("by_interest", "" + 0));
				}

				if (filterPrefs.getBoolean(AppConstants.FILTER_PREFS_ETHNICITY,
						false)) {
					params.add(new BasicNameValuePair("by_ethnicity", "" + 1));
				} else {
					params.add(new BasicNameValuePair("by_ethnicity", "" + 0));
				}
			} catch (Exception e) {

			}

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("update setting response", "" + json);
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
				// if (vStatus.equals("success")) {
				System.out.print("update setting response" + jsonArray);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class GetAllChoices extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("", "")); // no value

			JSONObject json = new JSONParser().makeHttpRequest2(args[0],
					"POST", params);

			Log.d("get all choices response ", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			try {
				singleton.stopLoading(llLoading);
			} catch (Exception e) {
			}
			try {

				JSONArray role = jsonObject.getJSONArray("role");
				roleDTOs = new ArrayList<RoleDTO>();
				/* ROLE */
				userDTO = sessionManager.getUserDetail();
				for (int x = 0; x < role.length(); x++) {
					JSONObject r = role.getJSONObject(x);
					RoleDTO roleDTO = new RoleDTO();
					roleDTO.setRoleId(r.getString("id"));
					roleDTO.setRoleName(r.getString("name"));
					roleDTO.setIsSelected(false);
					for (int k = 0; k < roleStrings.size(); k++) {
						if (roleStrings.get(k).equals(r.getString("id"))) {
							roleDTO.setIsSelected(true);
							selectedRole.add(roleDTO);
						}
					}
					roleDTOs.add(roleDTO);
				}

				settingDTO.setSelectedRoleList(selectedRole);

				JSONArray body = jsonObject.getJSONArray("body_type");
				bodyTypeDTOs = new ArrayList<BodyTypeDTO>();
				for (int x = 0; x < body.length(); x++) {
					JSONObject r = body.getJSONObject(x);
					BodyTypeDTO bodyDTO = new BodyTypeDTO();
					bodyDTO.setBodyTypeId(r.getString("id"));
					bodyDTO.setBodyTypeName(r.getString("name"));
					bodyDTO.setIsSelected(false);
					for (int k = 0; k < bodyTypeStrings.size(); k++) {
						if (bodyTypeStrings.get(k).equals(r.getString("id"))) {
							bodyDTO.setIsSelected(true);
							selectedBodyType.add(bodyDTO);
						}
					}
					bodyTypeDTOs.add(bodyDTO);
				}
				settingDTO.setSelectedBodyTypeList(selectedBodyType);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (bodyTypeDTOs.get(0).getIsSelected() == true) {
					bodyType(R.drawable.av_bodytype_slim_down, ivBodySlim,
							tvBodySlim, 1, true);
				}
				if (bodyTypeDTOs.get(1).getIsSelected() == true) {
					bodyType(R.drawable.av_bodytype_medium_down, ivBodyMed,
							tvBodyMed, 2, true);
				}
				if (bodyTypeDTOs.get(2).getIsSelected() == true) {
					bodyType(R.drawable.av_bodytype_athletic_down, ivBodyAth,
							tvBodyAth, 3, true);
				}
				if (bodyTypeDTOs.get(3).getIsSelected() == true) {
					bodyType(R.drawable.av_bodytype_large_down, ivBodyLarge,
							tvBodyLarge, 4, true);
				}

				roleSpinnerAdapter = new RoleSpinnerAdapter(context, roleDTOs,
						AppConstants.ADAPTER_FLAG_SETTINGS);
				roleGridView.setAdapter(roleSpinnerAdapter);
				roleGridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {

						RoleDTO roleDTO = roleDTOs.get(pos);

						Log.d("", "position=" + pos);

						if (roleDTO.getIsSelected()) {
							roleDTO.setIsSelected(false);
							selectedRole.remove(roleDTO);
						} else {
							roleDTO.setIsSelected(true);
							selectedRole.add(roleDTO);
						}

						roleSpinnerAdapter.notifyDataSetChanged();
						// logFlurryEvent(settingsParams, "Filter Ethnicity");
						// //Edited revision 582 commented
					}

				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
