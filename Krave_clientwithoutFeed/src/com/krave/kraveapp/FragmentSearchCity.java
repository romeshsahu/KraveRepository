package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ps.adapters.FindDudesGridAdapter;
import com.ps.adapters.SearchCityGridAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveRestClient;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;
import com.swipre.refresh.SwipyRefreshLayout;
import com.swipre.refresh.SwipyRefreshLayoutDirection;

@SuppressLint("NewApi")
public class FragmentSearchCity extends Fragment implements OnClickListener,
		OnPageChangeListener {

	static RecentlySearchedHistory RecentlySearchedcity = RecentlySearchedHistory
			.getInstance();

	private ImageView nextButton, previousButton, onlineDudesFilter;
	private RelativeLayout rlFilterNew, rlSearchNew;
	private GridView viewPager;
	private int viewPagerOriginalHeight;
	private SearchCityGridAdapter findDudesAdapter;

	private Activity_Home contex;
	private GPSTracker mGpsTracker;
	private LinearLayout onlyOnlineDudeLayout;
	private RelativeLayout searchCityLayout;

	public static RelativeLayout swipeLayout, refreshButtonLayout;
	private TextView noGuysText;

	private ImageView clearSearchResult, nearMeImageView;
	private AutoCompleteTextView searchCity;

	private final int DUDE_BY_FILTER = 0;
	private final int DUDE_BY_CITY = 1;
	private int comeFrom;
	private SessionManager sessionManager;
	public static boolean Refresh_Data = false;
	public static List<UserDTO> userDTOs = new ArrayList<UserDTO>();
	public static List<UserDTO> cacheUserDTOs = new ArrayList<UserDTO>();
	public static List<UserDTO> tempUserDTOs = new ArrayList<UserDTO>();

	public static boolean filterIsOnline = false;
	public static int previousCount = 0;
	public static int pageNumber = 1;
	public static int filterId = -1;
	public static int currentSelectedPage = 0;
	public static String city = "";
	public static boolean clearData = false;
	public static AppManager singleton;
	private ImageView loadingView;
	private LinearLayout llLoading;
	private TextView tvSearchNew, tvFilterNew;
	RelativeLayout mainview;

	BroadcastReceiver broadcastReceiver1;
	BroadcastReceiver refreshHomeFragment;

	private Boolean isNoGuysFound = false;

	private int userLimit = 24;
	private int itemCount;
	private int firstItemCount;
	private Boolean willReload = false;
	private Boolean isLastItem = false;

	private ImageView imageViewFilter;
	private boolean isOnlineOnly = false;
	// boolean onPage = true;

	// conrad
	private ImageView imgViewFilter;

	private boolean isLoadingOnPull = false;

	private TranslateAnimation upToDown;

	private boolean gridViewHasStartedTouch = false;

	private Boolean isReloading = false;
	private Boolean shouldReload = false;
	private boolean isLoadingNextPage = false;

	// Edited on revision 595
	// MAY 15 2015 11:28 AM
	// dl added variable

	// z
	// JCv -
	SwipyRefreshLayout mSwipyRefreshLayout = null;

	// Edited on revision 595
	// MAY 15 2015 11:28 AM
	// dl added method

	// - - - - - - - -
	@Override
	public void onDestroyView() {

		super.onDestroyView();
		Activity_Home.findDudesScrollPosition = viewPager
				.getFirstVisiblePosition();
		viewPager = null;

		findDudesAdapter = null;

		System.gc();
		if (clearData) {
			clearData = false;
		} else {
			setBackButtonFunction();
		}
		contex.unregisterReceiver(refreshHomeFragment);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_city, container,
				false);

		contex = (Activity_Home) getActivity();

		mGpsTracker = new GPSTracker(contex);

		sessionManager = new SessionManager(contex);
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		setLayout(view);
		setListener();
		setPager();

		setTypeFace(view);

		if (WebServiceConstants.isOnline(contex)) {
			new GetAllCitiesAsynch()
					.execute("http://54.219.211.237/KraveAPI/api_calls/getAllCity.php");
		} else {
			Toast.makeText(contex,
					R.string.toast_please_check_network_connection,
					Toast.LENGTH_LONG).show();
		}

		System.out.println("size : " + userDTOs.size());
		/* av singleton */

		// pageNumber = 1;

		if (mGpsTracker.getLatitude() != 0 && mGpsTracker.getLongitude() != 0) {
			System.out.println("LAT " + mGpsTracker.getLatitude());
			singleton.mGPSLat = mGpsTracker.getLatitude();
			singleton.mGPSLong = mGpsTracker.getLongitude();
		}

		if (contex.COME_FROM == AppConstants.FRAGMENT_SETTING) {

			contex.COME_FROM = 0;
			userDTOs.clear();
			filterId = 0;
			findDudesAdapter.setOnlineDataBeforeNotify();
			findDudesAdapter.notifyDataSetChanged();
			System.out.println("userDTO : " + userDTOs.size());

			if (WebServiceConstants.isOnline(contex) && !isLoadingNextPage) {

				fetchAllUsers();
			} else {
				Toast.makeText(contex,
						R.string.toast_please_check_network_connection,
						Toast.LENGTH_LONG).show();
			}
			setFilterUi();

		} else if (contex.COME_FROM == AppConstants.FRAGMENT_CHATT_MATCHES) {
			contex.COME_FROM = 0;
			// handler2.postDelayed(runnable2, 500);
			findDudesAdapter.setOnlineDataBeforeNotify();
			findDudesAdapter.notifyDataSetChanged();
		} else if (contex.COME_FROM == AppConstants.FRAGMENT_GET_MATCHES) {
			contex.COME_FROM = 0;
			comeFrom = AppConstants.FRAGMENT_GET_MATCHES;

			if (WebServiceConstants.isOnline(contex)) {
				// new GetDudesAsynchTask()
				// .execute("http://parkhya.org/Android/krave_app/index.php?action=searchByUserIntrestWAU&userid=6");
			}
		} else {
			Log.d("", "filter id=" + filterId);
			// if (userDTOs.size() == 0 || Refresh_Data) {
			if (Refresh_Data) {
				if (WebServiceConstants.isOnline(contex)) {
					if (!singleton.isDontSearch) {
						Refresh_Data = false;
						setWebServiceData(filterId);
						setFilterUi();
					} else {
						singleton.isDontSearch = false;
					}
				}
			} else {

				// setFilterUi();
				// findDudesAdapter.setOnlineDataBeforeNotify();
				// findDudesAdapter.notifyDataSetChanged();

				// handler.postDelayed(runnable, 5000);
			}
		}

		refreshHomeFragment = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d("", "onReceive" + refreshHomeFragment);
				setWebServiceData(filterId);
			}
		};

		IntentFilter closeActivity = new IntentFilter();
		closeActivity.addAction("refresh");
		contex.registerReceiver(refreshHomeFragment, closeActivity);

		// edited on revision 595
		// MAY 15 2015
		// 1:25 PM
		// ADDED

		searchCity.setText(RecentlySearchedcity.RecentlySearchedCity);
		if (RecentlySearchedcity.isOnline == "no") {
			// onlineDudesFilter.setBackgroundResource(R.drawable.btn_offline);
			findDudesAdapter.isOnlineOnly = false;
			// filterIsOnline = false;
			isOnlineOnly = false;
			imageViewFilter.setImageDrawable(FragmentSearchCity.this.contex
					.getResources().getDrawable(R.drawable.btn_offline));
		} else if (RecentlySearchedcity.isOnline == "yes") {
			// onlineDudesFilter.setBackgroundResource(R.drawable.btn_online);
			findDudesAdapter.isOnlineOnly = true;
			// filterIsOnline = true;
			isOnlineOnly = true;
			imageViewFilter.setImageDrawable(FragmentSearchCity.this.contex
					.getResources().getDrawable(R.drawable.select_onlinenow));
		}
		if (RecentlySearchedcity.RecentlySearchedCity != "") {

			searchCity.setText(RecentlySearchedcity.RecentlySearchedCity);

			InputMethodManager imm = (InputMethodManager) contex
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchCity.getWindowToken(), 0);
			nearMeImageView.setBackgroundResource(R.drawable.near_uncheck);

			// setWebServiceData(DUDE_BY_CITY);
		}

		// - - - - -
		if (Activity_Home.findDudesScrollPosition != 0) {
			viewPager.setSelection(Activity_Home.findDudesScrollPosition);
			Activity_Home.findDudesScrollPosition = 0;
		}
		if (isOnlineOnly) {
			imageViewFilter.setImageDrawable(getActivity().getResources()
					.getDrawable(R.drawable.select_onlinenow));
		} else {
			imageViewFilter.setImageDrawable(getActivity().getResources()
					.getDrawable(R.drawable.unselect_notofication));
		}
		return view;
	}

	private void filterSearchResults() {
		tempUserDTOs.clear();
		for (int i = 0; i < cacheUserDTOs.size(); i++) {
			if (filterId == 0)
				try {
					parseUserDataWithFilter(cacheUserDTOs, i);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
		userDTOs.clear();
		userDTOs.addAll(tempUserDTOs);
		if (userDTOs.size() <= 0) {
			Toast.makeText(contex, R.string.toast_no_guys_found_expand_search,
					Toast.LENGTH_LONG).show();
			userDTOs.addAll(cacheUserDTOs);
		}
		findDudesAdapter.setOnlineDataBeforeNotify();
		findDudesAdapter.notifyDataSetChanged();
	}

	private void checkGpsEnabledOrnot() {
		LocationManager MLocationManager = (LocationManager) contex
				.getSystemService(Context.LOCATION_SERVICE);
		// getting GPS status
		boolean isGPSEnabled = MLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!isGPSEnabled) {
			/* AV GPS */
			Intent intent = new Intent(contex, Activity_Gps_Alert.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(intent);
		}
	}

	public static void setFirstLoginValues() {
		userDTOs.clear();
		// edited on revision 595
		// MAY 15 2015
		// commented
		filterIsOnline = false;

		previousCount = 0;
		pageNumber = 1;
		filterId = 0;

		city = "";
	}

	public static void setBackButtonFunction() {
		userDTOs.clear();

		previousCount = 0;
		pageNumber = 1;
		filterId = 0;
		city = "";
		RecentlySearchedcity.RecentlySearchedCity = "";
	}

	private void setFilterUi() {

		switch (filterId) {
		case DUDE_BY_FILTER:
			searchCity.setText(city);
			nearMeImageView
					.setBackgroundResource(R.drawable.av_new_home_location);
			// nearMe = true;
			break;
		case DUDE_BY_CITY:
			searchCity.setText(city);
			// edited on revision 595
			// MAY 15 2015
			searchCity.setText(RecentlySearchedcity.RecentlySearchedCity);
			// - - -
			break;

		default:
			break;
		}

		if (filterIsOnline) {
			onlineDudesFilter.setBackgroundResource(R.drawable.btn_online);
		} else {
			onlineDudesFilter.setBackgroundResource(R.drawable.btn_offline);
		}

	}

	Handler handler2 = new Handler();
	Runnable runnable2 = new Runnable() {

		@Override
		public void run() {
			Activity_Home.right_button.performClick();
		}
	};

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			swipeLayout.setVisibility(View.GONE);
			refreshButtonLayout.setVisibility(View.VISIBLE);

		}
	};

	private void setTypeFace(View view) {
		TextView textView1, textView2, textView3, textView4, textView5, textView6;
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		textView3 = (TextView) view.findViewById(R.id.textView3);
		textView4 = (TextView) view.findViewById(R.id.textView4);
		textView5 = (TextView) view.findViewById(R.id.textView5);
		textView6 = (TextView) view.findViewById(R.id.textView6);

		Typeface typeface = FontStyle.getFont(contex,
				AppConstants.HelveticaNeueLTStd_Roman);
		textView1.setTypeface(typeface);

		textView3.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		noGuysText.setTypeface(typeface);
		searchCity.setTypeface(typeface);
		tvSearchNew.setTypeface(typeface);
		tvFilterNew.setTypeface(typeface);

		textView2.setTypeface(typeface);

	}

	// Edited on revision 595
	// MAY 15 2015
	// 1:31 PM
	// dl added method
	public void SearchRecentCity() {

		final String endpointString = WebServiceConstants.AVAPIEndpointGetAllUsersByCity;
		final RequestParams params = paramsForCityFilter();
		KraveRestClient.post(endpointString, params, userMapper());

	}

	// - - - - - - - - -

	private void setListener() {
		nextButton.setOnClickListener(this);
		previousButton.setOnClickListener(this);
		rlFilterNew.setOnClickListener(this);
		onlyOnlineDudeLayout.setOnClickListener(this);
		clearSearchResult.setOnClickListener(this);
		// nearMeLayout.setOnClickListener(this);
		refreshButtonLayout.setOnClickListener(this);
		imgViewFilter.setOnClickListener(this);

		rlSearchNew.setOnClickListener(this);

		imageViewFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (searchCity.getText().toString().trim().length() == 0) {
					return;
				}

				if (isOnlineOnly) {
					// edited on revision 595
					// MAY 15 2015
					//
					RecentlySearchedcity.isOnline = "no";
					//
					isOnlineOnly = false;
					findDudesAdapter.isOnlineOnly = isOnlineOnly;
					// findDudesAdapter.setOnlineDataBeforeNotify();
					// findDudesAdapter.notifyDataSetChanged();

					filterId = 1;
					shouldReload = true;
					pageNumber = 1;

					if (WebServiceConstants.isOnline(contex)) {
						fetchAllUsers();
					} else {
						Toast.makeText(contex,
								R.string.toast_please_check_network_connection,
								Toast.LENGTH_LONG).show();
					}

					imageViewFilter
							.setImageDrawable(FragmentSearchCity.this.contex
									.getResources().getDrawable(
											R.drawable.unselect_notofication));
				} else {
					RecentlySearchedcity.isOnline = "yes";
					isOnlineOnly = true;
					findDudesAdapter.isOnlineOnly = isOnlineOnly;
					// findDudesAdapter.setOnlineDataBeforeNotify();
					// findDudesAdapter.notifyDataSetChanged();

					filterId = 1;
					shouldReload = true;
					pageNumber = 1;

					if (WebServiceConstants.isOnline(contex)) {
						fetchAllUsers();
					} else {
						Toast.makeText(contex,
								R.string.toast_please_check_network_connection,
								Toast.LENGTH_LONG).show();
					}
					imageViewFilter
							.setImageDrawable(FragmentSearchCity.this.contex
									.getResources().getDrawable(
											R.drawable.select_onlinenow));
				}
			}
		});

		searchCity
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						// findDudesAdapter.setOnlineDataBeforeNotify();
						shouldReload = true;
						pageNumber = 1;
						filterId = 1;
						InputMethodManager imm = (InputMethodManager) contex
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
						if (WebServiceConstants.isOnline(contex)) {
							fetchAllUsers();
						} else {
							Toast.makeText(
									contex,
									R.string.toast_please_check_network_connection,
									Toast.LENGTH_LONG).show();
						}
					}
				});

		searchCity.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				city = searchCity.getText().toString();

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (searchCity.getText().toString().trim().length() == 0) {
						Toast.makeText(contex, R.string.toast_enter_city_name,
								Toast.LENGTH_SHORT).show();
					} else {
						// edited on revision 595
						// MAY 15 2015
						// 1:54 PM
						RecentlySearchedcity.RecentlySearchedCity = searchCity
								.getText().toString();
						// - - - - --
						InputMethodManager imm = (InputMethodManager) contex
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								searchCity.getWindowToken(), 0);
						nearMeImageView
								.setBackgroundResource(R.drawable.near_uncheck);

						setWebServiceData(DUDE_BY_CITY);

					}
				}

				return false;
			}
		});

		searchCity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.toString().trim().length() == 0
						&& filterId == DUDE_BY_CITY) {
					Log.d("", "addTextChangedListener");
					city = "";
					setWebServiceData(DUDE_BY_FILTER);
					setFilterUi();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void setLayout(View view) {

		mainview = (RelativeLayout) view.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		noGuysText = (TextView) view.findViewById(R.id.no_guys_text);
		nextButton = (ImageView) view.findViewById(R.id.next1);
		previousButton = (ImageView) view.findViewById(R.id.back1);
		rlFilterNew = (RelativeLayout) view.findViewById(R.id.rlFilterNew);
		onlineDudesFilter = (ImageView) view.findViewById(R.id.filter);
		onlyOnlineDudeLayout = (LinearLayout) view
				.findViewById(R.id.linearLayout1);

		searchCity = (AutoCompleteTextView) view.findViewById(R.id.serchCity);
		clearSearchResult = (ImageView) view
				.findViewById(R.id.clearSearchResult);
		nearMeImageView = (ImageView) view.findViewById(R.id.nearMe);
		swipeLayout = (RelativeLayout) view.findViewById(R.id.swipeLayout);
		refreshButtonLayout = (RelativeLayout) view
				.findViewById(R.id.refreshButton);

		viewPager = (GridView) view.findViewById(R.id.viewpager);
		setGridViewColumn();
		rlSearchNew = (RelativeLayout) view.findViewById(R.id.rlSearchNew);
		searchCityLayout = (RelativeLayout) view
				.findViewById(R.id.LinearLayout01);

		tvSearchNew = (TextView) view.findViewById(R.id.tvSearchNew);
		tvFilterNew = (TextView) view.findViewById(R.id.tvFilterNew);
		tvFilterNew.setText("FILTER");
		loadingView = (ImageView) view.findViewById(R.id.loadingView);
		llLoading = (LinearLayout) view.findViewById(R.id.llLoading);

		imgViewFilter = (ImageView) view.findViewById(R.id.newFilter);

		imageViewFilter = (ImageView) view.findViewById(R.id.imageViewFilter);

		mSwipyRefreshLayout = (SwipyRefreshLayout) view
				.findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout
				.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh(SwipyRefreshLayoutDirection direction) {
						Log.d("MainActivity",
								"Refresh triggered at "
										+ (direction == SwipyRefreshLayoutDirection.TOP ? "top"
												: "bottom"));

						if (direction == SwipyRefreshLayoutDirection.TOP) {
							pageNumber = 1;
							isLoadingOnPull = true;

							fetchAllUsers();

						} else {

							pageNumber++;
							isLoadingNextPage = true;

							fetchAllUsers();
						}
					}
				});

	}

	private void setGridViewColumn() {

		try {
			int selectedFindDudesColumnId = Integer.valueOf(sessionManager
					.getSettingDetail().getFindDudesColumnCoun());
			if (selectedFindDudesColumnId == 0) {
				viewPager.setNumColumns(2);
			} else {
				viewPager.setNumColumns(3);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			viewPager.setNumColumns(2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			viewPager.setNumColumns(2);
		}
	}

	private void setPager() {

		findDudesAdapter = new SearchCityGridAdapter(getActivity(), userDTOs,
				true);

		viewPager.setAdapter(findDudesAdapter);
		findDudesAdapter.setOnlineDataBeforeNotify();
		findDudesAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		/* Init Flurry Params */
		Map<String, String> findGuysParams = new HashMap<String, String>();

		switch (v.getId()) {

		case R.id.rlSearchNew:
			contex.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_FIND_GUYS,
					AppConstants.EVENT_LOG_ACTION_BTN, "Search");
			searchCityLayout.setVisibility(View.VISIBLE);
			rlSearchNew.setVisibility(View.GONE);
			rlFilterNew.setVisibility(View.GONE);
			if (searchCityLayout.getVisibility() == View.VISIBLE)
				searchCity.requestFocus();
			break;
		case R.id.linearLayout1:
			if (filterIsOnline) {
				logFlurryEvent(findGuysParams, "Find Guys - Filter Pressed",
						"Offline");

				onlineDudesFilter.setBackgroundResource(R.drawable.btn_offline);
				// findDudesAdapter.getFilter().filter(AppConstants.ALL_DUDE);
				filterIsOnline = false;

				// edited on revision 595
				// MAY 15 2015
				RecentlySearchedcity.isOnline = "no";
				// - - - -

				// calculatePages(userDTOs.size());
				// viewPager.setCurrentItem(0);

			} else {
				logFlurryEvent(findGuysParams, "Find Guys - Filter Pressed",
						"Online");

				onlineDudesFilter.setBackgroundResource(R.drawable.btn_online);
				// findDudesAdapter.getFilter().filter(AppConstants.ONLINE);
				filterIsOnline = true;

				// edited on revision 595
				// MAY 15 2015
				RecentlySearchedcity.isOnline = "yes";
				// - - - -

				// viewPager.setCurrentItem(0);
			}
			// onPage = false;
			setWebServiceData(filterId);

			break;
		case R.id.clearSearchResult:

			if (filterId == DUDE_BY_CITY) {
				city = "";
				// onPage = false;
				setWebServiceData(DUDE_BY_FILTER);
				setFilterUi();
			} else {
				searchCity.setText("");
			}
			rlSearchNew.setVisibility(View.VISIBLE);
			rlFilterNew.setVisibility(View.VISIBLE);
			searchCityLayout.setVisibility(View.GONE);
			break;
		case R.id.newFilter:
		case R.id.rlFilterNew:

			userDTOs.clear();
			userDTOs.addAll(cacheUserDTOs);

			if (WebServiceConstants.isOnline(contex)) {

				contex.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FIND_GUYS,
						AppConstants.EVENT_LOG_ACTION_BTN, "Filter");

				FragmentSetting.comeFrom = true;
				FragmentHome.clearData = true;
				filterId = DUDE_BY_FILTER;
				city = "";
				contex.Attach_Fragment(AppConstants.FRAGMENT_SETTING);
			}
			break;
		case R.id.refreshButton:

			setWebServiceData(filterId);
			break;

		default:
			break;
		}

	}

	public void logFlurryEvent(Map<String, String> findGuysParams, String log,
			String event) {
		findGuysParams.put("Title", event);
		FlurryAgent.logEvent(log, findGuysParams); // Edited revision 582
													// commented
	}

	private void setWebServiceData(int id) {
		filterId = id;
		previousCount = 0;

		pageNumber = 1;
		userDTOs.clear();
		findDudesAdapter.setOnlineDataBeforeNotify();
		findDudesAdapter.notifyDataSetChanged();
		if (WebServiceConstants.isOnline(contex)) {
			fetchAllUsers();
		}
	}

	private RequestParams paramsForFacetFilteredFetch() {
		RequestParams params = new RequestParams();
		try {
			params.add("user_id", sessionManager.getUserDetail().getUserID());
			params.add("limit", "" + userLimit);
			params.add("lat", "" + mGpsTracker.getLatitude());
			params.add("long", "" + mGpsTracker.getLongitude());
			params.add("page", "" + pageNumber);

			if (isOnlineOnly) {
				params.add("isonline", "" + 1);
				// edited on revision 594
				// /params.add("user_id",
				// sessionManager.getUserDetail().getUserID());
				// /params.add("lat", "" + mGpsTracker.getLatitude());
				// params.add("long", "" + mGpsTracker.getLongitude());
				// params.add("page", "" + pageNumber);
			}

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

			if (isCheckBoxFilterByAgeChecked) {
				params.add("min", singleton.filterDTO.getMinAge());
				params.add("max", singleton.filterDTO.getMaxAge());
			}

			if (isCheckBoxFilterByRadiuesChecked) {
				params.add("radius", singleton.filterDTO.getSearchRadius());
			}

			if (isCheckBoxFilterByEthnicityChecked) {
				int y = 0;
				// ethnicity
				for (int x = 0; x < singleton.filterDTO.getWhatAreYouDataDTOs()
						.size(); x++) {
					if (singleton.filterDTO.getWhatAreYouDataDTOs().get(x)
							.isSelected()) {
						params.add("ethnicity_ids[" + y + "]",
								singleton.filterDTO.getWhatAreYouDataDTOs()
										.get(x).getId());
						y++;
					}
				}
			}

			if (isCheckBoxFilterByRoleChecked) {
				// role
				for (int i = 0; i < singleton.filterDTO.getSelectedRoleList()
						.size(); i++) {
					params.add("roles[" + i + "]", singleton.filterDTO
							.getSelectedRoleList().get(i).getRoleId());
				}
			}

			if (isCheckBoxFilterByInterestChecked) {
				// interest
				for (int i = 0; i < singleton.filterDTO
						.getSelectedInterestList().size(); i++) {
					params.add("interest_ids[" + i + "]", singleton.filterDTO
							.getSelectedInterestList().get(i).getInterestId());
				}
			}

			if (isCheckBoxFilterByBodyTypeChecked) {
				// body type
				for (int i = 0; i < singleton.filterDTO
						.getSelectedBodyTypeList().size(); i++) {
					params.add("body_types[" + i + "]", singleton.filterDTO
							.getSelectedBodyTypeList().get(i).getBodyTypeId());
				}
			}

			if (isCheckBoxFilterByLookingForChecked) {
				// love hookup
				String[] loveHookup = singleton.filterDTO.getLoveHookup()
						.split(",");
				for (int i = 0; i < loveHookup.length; i++) {
					params.add("love_hookup[" + i + "]", loveHookup[i]);
				}
			}
		} catch (Exception e) {

		}
		return params;
	}

	private RequestParams paramsForCityFilter() {
		RequestParams params = new RequestParams();

		params.add("user_id", sessionManager.getUserDetail().getUserID());
		params.add("limit", "" + userLimit);
		params.add("page", "" + pageNumber);
		params.add("city", searchCity.getText().toString());

		// edited on revision 595
		// MAY 15, 2015
		// 1:21 pm
		// added
		RecentlySearchedcity.RecentlySearchedCity = searchCity.getText()
				.toString();
		// - - - -- - - -
		if (isOnlineOnly) {
			params.add("isonline", "" + 1);
			// editen on revision 594
			// MAY 14 2015
			// dl
			params.add("limit", "" + 500);
		}
		Log.d("", "search city=" + params);
		return params;
	}

	private JsonHttpResponseHandler userMapper() {
		return new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {

				super.onFailure(statusCode, headers, throwable, errorResponse);

				if (mSwipyRefreshLayout.isRefreshing()) {
					mSwipyRefreshLayout.setRefreshing(false);
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {

				super.onSuccess(statusCode, headers, response);

				if (mSwipyRefreshLayout.isRefreshing()) {
					mSwipyRefreshLayout.setRefreshing(false);
				}

				JSONArray jsonArray = response;

				try {
					singleton.stopLoading(llLoading);

					if (isLoadingOnPull || shouldReload) {
						shouldReload = false;
						userDTOs.clear();
					} else if (jsonArray != null && jsonArray.length() == 0) {
						Toast.makeText(contex,
								R.string.toast_no_more_guys_to_show,
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {

				}

				try {

					Log.d("JCv", "length : " + jsonArray.length());

					for (int i = 0; i < jsonArray.length(); i++) {
						if (filterId == 0)
							parseUserDataNew(jsonArray.getJSONObject(i), i);
						else if (filterId == 1)
							parseUserByCity(jsonArray.getJSONObject(i), i);
					}

					Log.d("JCv", "userDTOs " + userDTOs.size());
					// if (userDTOs.size() == 0 && pageNumber == 1) {
					//
					// filterId = 0;
					// isNoGuysFound = true;
					// if (WebServiceConstants.isOnline(contex)
					// && !isLoadingNextPage
					// ) {
					// // new GetDudesAsynchTask().execute();
					// fetchAllUsers();
					// }else{
					// Toast.makeText(contex, "Please check Network Connection",
					// Toast.LENGTH_LONG).show();
					// }
					// } else {
					if (userDTOs.size() == 0) {
						Toast.makeText(contex,
								R.string.toast_no_guys_found_expand_search,
								Toast.LENGTH_LONG).show();
						isNoGuysFound = false;
					}
					noGuysText.setVisibility(View.GONE);
					// }

					cacheUserDTOs.clear();
					cacheUserDTOs.addAll(userDTOs);

					findDudesAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
					// if (userDTOs.size() == 0 && pageNumber == 1) {
					//
					// filterId = 0;
					// isNoGuysFound = true;
					// if (WebServiceConstants.isOnline(contex)
					// && !isLoadingNextPage
					// )
					// {
					// fetchAllUsers();
					// }else{
					// Toast.makeText(contex, "Please check Network Connection",
					// Toast.LENGTH_LONG).show();
					// }
					// } else {
					noGuysText.setVisibility(View.GONE);
					if (isNoGuysFound) {
						Toast.makeText(contex,
								R.string.toast_no_guys_found_expand_search,
								Toast.LENGTH_LONG).show();
						isNoGuysFound = false;
					}
					// }
					if (pageNumber > 1)
						pageNumber = pageNumber - 1;

				} catch (NullPointerException ew) {

				}

				handler.postDelayed(runnable, 5000);

				contex.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				contex.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

				/* gps reminder will show */
				singleton.mPrefs = contex.getSharedPreferences(
						AppConstants.GPS_PREFS, 0);
				Boolean isGPSReminderOn = singleton.mPrefs.getBoolean(
						AppConstants.GPS_REMINDER_ON, true);
				if (isGPSReminderOn) {
					checkGpsEnabledOrnot();
				}

				if (contex.COME_FROM == AppConstants.FRAGMENT_SETTING) {
					contex.COME_FROM = 0;
					filterSearchResults();
				}

				isLoadingOnPull = false; // on Pull Loading Flag - implemented :
											// 3/16/2015
				isLastItem = false;

				isReloading = false;
				isLoadingNextPage = false;
			}
		};
	}

	private void fetchAllUsers() {
		if (!mSwipyRefreshLayout.isRefreshing())
			mSwipyRefreshLayout.setRefreshing(true);

		if (filterId == 0) // by facet
		{
			final String endpointString = WebServiceConstants.AVAPIEndpointGetAllUsers;
			final RequestParams params = paramsForFacetFilteredFetch();
			KraveRestClient.post(endpointString, params, userMapper());
		} else if (filterId == 1) // by city
		{
			final String endpointString = WebServiceConstants.AVAPIEndpointGetAllUsersByCity;
			final RequestParams params = paramsForCityFilter();
			KraveRestClient.post(endpointString, params, userMapper());
		}
	}

	class GetAllCitiesAsynch extends AsyncTask<String, Void, JSONObject> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected JSONObject doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = new JSONParser().makeHttpRequest2(args[0],
					"POST", params);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);

			try {
				ArrayList<String> asd = new ArrayList<String>();

				for (int x = 0; x < jsonObject.getJSONArray("cities").length(); x++) {
					asd.add(jsonObject.getJSONArray("cities").getString(x));
				}
				ArrayAdapter adapter = new ArrayAdapter(contex,
						android.R.layout.simple_list_item_1, asd);
				searchCity.setAdapter(adapter);

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void parseUserByCity(JSONObject mJsonObject, int xx)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		LatLongDTO latLongDTO = new LatLongDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();

		String user_id = mJsonObject.getString("user_id");
		String user_fname = mJsonObject.getString("user_fname");

		String user_image = mJsonObject.getString("user_image");

		String user_age = mJsonObject.getString("user_age");

		String[] user_intrest = mJsonObject.getString("user_intrest")
				.split(",");

		String city = mJsonObject.getString("city");
		String country = mJsonObject.getString("country");
		String latitude = mJsonObject.getString("latitude");
		String longitude = mJsonObject.getString("longitude");
		String user_WhatAreYou = mJsonObject.getString("user_whatAreYou");
		String online_status = mJsonObject.getString("online_status");
		String lastActvityDate = mJsonObject.getString("time_stamp");
		userDTO.setLastActiveDate(lastActvityDate);
		userDTO.setUserID(user_id);
		userDTO.setFirstName(user_fname);
		userDTO.setProfileImage(user_image);

		if (online_status.equals("available")) {
			userDTO.setIsOnline(AppConstants.ONLINE);

		} else if (online_status.equals("away")) {
			userDTO.setIsOnline(AppConstants.ABSENT);
		} else {
			userDTO.setIsOnline(AppConstants.OFFLINE);
		}

		whatAreYouDTO.setAge(user_age);

		whatAreYouDTO.setWhatAreYou(user_WhatAreYou);

		for (int i = 0; i < user_intrest.length; i++) {
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(user_intrest[i]);
			interestsDTOs.add(interestsDTO);
		}

		Boolean check = false;

		UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		userProfileImageDTO.setImagePath(user_image);

		userProfileImageDTOs.add(userProfileImageDTO);

		latLongDTO.setUser_id(user_id);
		latLongDTO.setCountry(country);
		latLongDTO.setLatitude(latitude);
		latLongDTO.setLongitude(longitude);

		userDTO.setCity(city);
		userDTO.setCountry(country);

		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO1 = new UserProfileImageDTO();
			userProfileImageDTO1.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO1.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO1.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);

		userDTOs.add(userDTO);

	}

	private void parseUserDataWithFilter(List<UserDTO> mJsonObject, int xx)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		LatLongDTO latLongDTO = new LatLongDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();

		String user_id = mJsonObject.get(xx).getUserID();
		String user_fname = mJsonObject.get(xx).getFirstName();

		String user_image = mJsonObject.get(xx).getProfileImage();

		String user_age = mJsonObject.get(xx).getWhatAreYouDTO().getAge();

		List<InterestsDTO> user_interest = mJsonObject.get(xx)
				.getInterestList();

		String city = mJsonObject.get(xx).getCity();
		String distance = mJsonObject.get(xx).getLatLongDTO().getDistance();
		String latitude = mJsonObject.get(xx).getLatLongDTO().getLatitude();
		String longitude = mJsonObject.get(xx).getLatLongDTO().getLongitude();
		String user_WhatAreYou = mJsonObject.get(xx).getWhatAreYouDTO()
				.getWhatAreYou();
		String online_status = mJsonObject.get(xx).getIsOnline();
		String role = mJsonObject.get(xx).getWhatAreYouDTO().getRole();
		String body = mJsonObject.get(xx).getBodyTypeId();
		String lastActvityDate = mJsonObject.get(xx).getLastActiveDate();
		userDTO.setLastActiveDate(lastActvityDate);
		/* Start Filtering here */
		float fDistance = (float) 0.0;
		float filterRadius = Float.valueOf(singleton.filterDTO
				.getSearchRadius());
		try {
			mGpsTracker.getLocation();
			fDistance = singleton.distFrom(mGpsTracker.getLatitude(),
					mGpsTracker.getLongitude(), Double.valueOf(latitude),
					Double.valueOf(longitude));
		} catch (Exception e) {
		}
		long calculatedAge = singleton.calculateAge(user_age);
		if (calculatedAge < Integer.valueOf(singleton.filterDTO.getMinAge())
				|| calculatedAge > Integer.valueOf(singleton.filterDTO
						.getMaxAge()) || fDistance > filterRadius) {
			// distance and age filter
			return;
		}

		Boolean hasInterest = false;
		for (int x = 0; x < singleton.filterDTO.getSelectedInterestList()
				.size(); x++) {
			for (int y = 0; y < user_interest.size(); y++) {
				if (singleton.filterDTO.getSelectedInterestList().get(x)
						.getInterestId()
						.equals(user_interest.get(y).getInterestId())) {
					hasInterest = true;
					break;
				}
			}
			if (hasInterest)
				break;
		}
		if (!hasInterest) // interest filter
			return;

		Boolean isWhatUKrave = false;
		ArrayList<String> filterEthnicity = new ArrayList<String>();
		for (int x = 0; x < singleton.filterDTO.getWhatAreYouDataDTOs().size(); x++) {
			if (singleton.filterDTO.getWhatAreYouDataDTOs().get(x).isSelected()) {
				filterEthnicity.add(singleton.filterDTO.getWhatAreYouDataDTOs()
						.get(x).getId());
			}
		}
		for (int x = 0; x < filterEthnicity.size(); x++) {
			if (filterEthnicity.get(x).equals(user_WhatAreYou)) {
				isWhatUKrave = true;
				break;
			}
		}
		if (!isWhatUKrave) // ethnicity filter
			return;
		/* ==================== */
		userDTO.setUserID(user_id);
		userDTO.setFirstName(user_fname);
		userDTO.setProfileImage(user_image);

		userDTO.setBodyType(body);

		if (online_status.equals("available")) {
			userDTO.setIsOnline(AppConstants.ONLINE);

		} else if (online_status.equals("away")) {
			userDTO.setIsOnline(AppConstants.ABSENT);
		} else {
			userDTO.setIsOnline(AppConstants.OFFLINE);
		}

		whatAreYouDTO.setAge(user_age);

		whatAreYouDTO.setRole(role);
		whatAreYouDTO.setWhatAreYou(user_WhatAreYou);
		interestsDTOs.addAll(user_interest);

		Boolean check = false;

		UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		userProfileImageDTO.setImagePath(user_image);
		userProfileImageDTOs.add(userProfileImageDTO);

		System.out.println(latitude + " long: " + longitude + " dist "
				+ distance);
		latLongDTO.setUser_id(user_id);
		latLongDTO.setLatitude(latitude);
		latLongDTO.setLongitude(longitude);
		latLongDTO.setDistance(distance);
		userDTO.setCity(city);

		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO1 = new UserProfileImageDTO();
			userProfileImageDTO1.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO1.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO1.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		tempUserDTOs.add(userDTO);
	}

	private void parseUserDataNew(JSONObject mJsonObject, int xx)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		LatLongDTO latLongDTO = new LatLongDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();

		String user_id = mJsonObject.getString("user_id");
		String user_fname = mJsonObject.getString("user_fname");

		String user_image = mJsonObject.getString("user_image");

		String user_age = mJsonObject.getString("user_age");

		String[] user_intrest = mJsonObject.getString("user_intrest")
				.split(",");

		String city = mJsonObject.getString("city");
		String distance = mJsonObject.getString("distance");
		String latitude = mJsonObject.getString("latitude");
		String longitude = mJsonObject.getString("longitude");
		String user_WhatAreYou = mJsonObject.getString("user_whatAreYou");
		String online_status = mJsonObject.getString("online_status");
		String lastActvityDate = mJsonObject.getString("time_stamp");
		userDTO.setLastActiveDate(lastActvityDate);
		userDTO.setUserID(user_id);
		userDTO.setFirstName(user_fname);
		userDTO.setProfileImage(user_image);

		if (online_status.equals("available")) {
			userDTO.setIsOnline(AppConstants.ONLINE);

		} else if (online_status.equals("away")) {
			userDTO.setIsOnline(AppConstants.ABSENT);
		} else {
			userDTO.setIsOnline(AppConstants.OFFLINE);
		}

		whatAreYouDTO.setAge(user_age);

		// whatAreYouDTO.setRole(role);
		whatAreYouDTO.setWhatAreYou(user_WhatAreYou);

		for (int i = 0; i < user_intrest.length; i++) {
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(user_intrest[i]);
			interestsDTOs.add(interestsDTO);
		}

		Boolean check = false;

		UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		userProfileImageDTO.setImagePath(user_image);
		userProfileImageDTOs.add(userProfileImageDTO);

		System.out.println(latitude + " long: " + longitude + " dist "
				+ distance);
		latLongDTO.setUser_id(user_id);
		latLongDTO.setLatitude(latitude);
		latLongDTO.setLongitude(longitude);
		latLongDTO.setDistance(distance);

		userDTO.setCity(city);

		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO1 = new UserProfileImageDTO();
			userProfileImageDTO1.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO1.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO1.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);

		userDTOs.add(userDTO);
		if (userDTO.getIsOnline().equalsIgnoreCase("available")) {
			findDudesAdapter.addOnlineDataBeforeNotify(userDTO);
		}

		findDudesAdapter.notifyDataSetChanged();

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		Log.d("", "on page selected" + arg0);
		currentSelectedPage = arg0;
		if (arg0 != 0) {
			previousCount = findDudesAdapter.getCount();

			if (findDudesAdapter.getCount() == arg0 + 1) {
				if (WebServiceConstants.isOnline(contex) && !isLoadingOnPull)
					fetchAllUsers();
			}
		}
	}
}