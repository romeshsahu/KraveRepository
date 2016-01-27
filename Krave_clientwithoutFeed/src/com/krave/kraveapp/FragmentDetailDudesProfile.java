package com.krave.kraveapp;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.askerov.dynamicgrid.DynamicGridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.krave.kraveapp.Activity_Home.InsertActivityLog;
import com.ps.adapters.DudeDetailsInterestAdapter;
import com.ps.adapters.DudeProfileImageGridViewAdapter;
import com.ps.adapters.DudesProfileImagesPagerAdapter;
import com.ps.horizontal_listview.HorizontalListView;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.BodyTypeDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.RoleDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.CustomScrollView;
import com.ps.utill.DistanceConverter;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.JSONParser;
import com.ps.utill.LockableScrollView;
import com.ps.utill.MyXMLHandler;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentDetailDudesProfile extends Fragment implements
		OnClickListener {
	private Activity_Home context;
	private UserDTO userDTO;
	private ImageView cancelButton, doneButton, userStatusImage,
			showUserDetailsVisibility, backImage, nextImage;
	private TextView name, address, commonFriends, relationShipStatus,
			whatAreYou, userHightWeight, aboutDude, distanceAway,
			sharedInterest, photosTextView, userStatusText, tvLastActivity,
			tvRole, tvBody, tvLoveHook;
	private Button btn_flag, btn_star, btn_chat;
	private boolean isHide = false;
	private ImageView viewPager, viewPagerStamp;
	private HorizontalListView horizontalListView;
	private DudesProfileImagesPagerAdapter dudesProfileImagesPagerAdapter;
	// private LinearLayout setPagerIconImagesLayout;

	private ImageView imageView0, imageView1, imageView2, imageView3,
			imageView4, imageView5;
	private ImageView imageArray[] = { imageView0, imageView1, imageView2,
			imageView3, imageView4, imageView5 };
	private ImageView imgGal1, imgGal2, imgGal3, imgGal4, imgGal5;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			25, 25);
	private int previousPosition;
	private LinearLayout progressIcon;
	private LinearLayout m_gallery;
	private DudeDetailsInterestAdapter dudeDetailsInterestAdapter;
	private List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	private SessionManager sessionManager;
	public static int comeFrom;
	private GPSTracker gpsTracker;
	private ImageLoader imageLoader;
	static final String logTag = "ActivitySwipeDetector";
	private Activity activity;
	static final int MIN_DISTANCE = 100;
	private float downX, downY, upX, upY;
	// private RelativeLayout swipeLayout;
	private int galleryCount = 0;
	private AppManager singleton;
	private ImageView loadingView;
	private LinearLayout llLoading;

	private int loadCount = 0;

	private String FAVORITE_USER = "YES";
	private String NOT_FAVORITE_USER = "NO";

	private DistanceConverter distanceConverter;
	private int transPref;
	private TextView textViewTrans;
	private ImageView imageViewTrans;
	private RelativeLayout photosGridViewHeaderLayout;
	private DynamicGridView photoGrid;
	private DudeProfileImageGridViewAdapter dudeProfileImageGridViewAdapter;
	/* FLAG ENUM VALUES */
	private int FLAG_TRUE = -1, FLAG_NULL = 0, FLAG_BLOCK = 1, FLAG_REPORT = 2,
			FLAG_CANCEL = 3;

	public final static long SECOND_MILLIS = 1000;
	public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
	public final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
	public final static long DAY_MILLIS = HOUR_MILLIS * 24;

	float lastActDays, lastActHours, lastActMins;
	private List<RoleDTO> roleDTOs;
	private List<BodyTypeDTO> bodyTypeDTOs;

	private CustomScrollView customScrollView;
	private int privateImageRequestCounter = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home3, container, false);
		System.gc();

		context = (Activity_Home) getActivity();
		sessionManager = new SessionManager(context);
		imageLoader = new ImageLoader(context);
		gpsTracker = new GPSTracker(context);

		/* av singleton */
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		distanceConverter = new DistanceConverter();
		transPref = singleton.mTransitPrefs.getInt(
				AppConstants.TRANSIT_PREFERENCE, 1);

		setLayout(view);
		setTypeFace(view);
		if (comeFrom == AppConstants.FRAGMENT_CHATT_MATCHES) {
			context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE_WITH_CHAT;
			context.left_button.setImageResource(R.drawable.av_new_chat_backup);
			cancelButton.setVisibility(View.GONE);
			doneButton.setVisibility(View.GONE);
		} else {
			context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE;
			context.left_button.setImageResource(R.drawable.av_new_chat_backup);
		}

		interestsDTOs.clear();
		// edited for revision 608
		// MAY 20 2015
		// moved from before return view to first call

		loadAPI(Activity_Home.gblUserID);
		// - - - --

		return view;
	}

	private void setTypeFace(View view) {

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

		name.setTypeface(typeface);
		address.setTypeface(typeface);
		relationShipStatus.setTypeface(typeface);
		distanceAway.setTypeface(typeface);
		userHightWeight.setTypeface(typeface);
		aboutDude.setTypeface(typeface);
		sharedInterest.setTypeface(typeface);
		photosTextView.setTypeface(typeface);
		whatAreYou.setTypeface(typeface);
		userStatusText.setTypeface(typeface);
		tvLastActivity.setTypeface(typeface);
		textViewTrans.setTypeface(typeface);

		tvRole.setTypeface(typeface);
		tvBody.setTypeface(typeface);
		tvLoveHook.setTypeface(typeface);

	}

	// private void setPagerView() {
	// previousPosition = 0;
	// dudesProfileImagesPagerAdapter = new DudesProfileImagesPagerAdapter(
	// context, userDTO.getUserProfileImageDTOs());
	// viewPager.setAdapter(dudesProfileImagesPagerAdapter);
	// progressIcon.removeAllViews();
	// viewPager.setOnPageChangeListener(new OnPageChangeListener() {
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// imageArray[previousPosition].setBackgroundDrawable(context
	// .getResources().getDrawable(R.drawable.uncheck));
	// imageArray[arg0].setBackgroundDrawable(context.getResources()
	// .getDrawable(R.drawable.check));
	// previousPosition = arg0;
	//
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	//
	// for (int i = 0; i < userDTO.getUserProfileImageDTOs().size(); i++) {
	// imageArray[i] = new ImageView(context);
	// layoutParams.setMargins(10, 5, 10, 5);
	// imageArray[i].setLayoutParams(layoutParams);
	// imageArray[i].setBackgroundDrawable(context.getResources()
	// .getDrawable(R.drawable.uncheck));
	// if (i == 0) {
	// imageArray[i].setBackgroundDrawable(context.getResources()
	// .getDrawable(R.drawable.check));
	// }
	//
	// final int j = i;
	// imageArray[i].setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// imageArray[previousPosition].setBackgroundDrawable(context
	// .getResources().getDrawable(R.drawable.uncheck));
	// imageArray[j].setBackgroundDrawable(context.getResources()
	// .getDrawable(R.drawable.check));
	// viewPager.setCurrentItem(j);
	// previousPosition = j;
	//
	// }
	// });
	// progressIcon.addView(imageArray[i]);
	// }
	//
	// }

	private void setLayout(View view) {
		RelativeLayout mainview = (RelativeLayout) view
				.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		viewPager = (ImageView) view.findViewById(R.id.viewpager1);
		viewPagerStamp = (ImageView) view.findViewById(R.id.viewpagerStamp);
		progressIcon = (LinearLayout) view.findViewById(R.id.layoutIcon);
		m_gallery = (LinearLayout) view.findViewById(R.id.layout_gallery);

		horizontalListView = (HorizontalListView) view
				.findViewById(R.id.horizontalListView1);
		// setPagerIconImagesLayout = (LinearLayout) view
		// .findViewById(R.id.layoutIcon);
		// userProfileImage = (ImageView)
		// view.findViewById(R.id.userProfileImage);
		cancelButton = (ImageView) view.findViewById(R.id.cancel);
		// addButton = (ImageView) view.findViewById(R.id.add);
		doneButton = (ImageView) view.findViewById(R.id.done);
		showUserDetailsVisibility = (ImageView) view
				.findViewById(R.id.hideShowImage);

		name = (TextView) view.findViewById(R.id.name);
		address = (TextView) view.findViewById(R.id.address);
		// commonFriends = (TextView) view.findViewById(R.id.textView5);
		relationShipStatus = (TextView) view.findViewById(R.id.relationShip);
		userHightWeight = (TextView) view.findViewById(R.id.hightWeight);
		aboutDude = (TextView) view.findViewById(R.id.aboutMe);
		distanceAway = (TextView) view.findViewById(R.id.milesAway);
		sharedInterest = (TextView) view.findViewById(R.id.sharedInterest);
		photosTextView = (TextView) view.findViewById(R.id.photosTexView);
		userStatusText = (TextView) view.findViewById(R.id.textOnOff);
		whatAreYou = (TextView) view.findViewById(R.id.whatareyou);
		backImage = (ImageView) view.findViewById(R.id.backimage);
		nextImage = (ImageView) view.findViewById(R.id.nimage);
		userStatusImage = (ImageView) view.findViewById(R.id.imageOnOff);
		// swipeLayout = (RelativeLayout) view.findViewById(R.id.swipeLayout);

		imgGal1 = (ImageView) view.findViewById(R.id.gallery1);
		imgGal2 = (ImageView) view.findViewById(R.id.gallery2);
		imgGal3 = (ImageView) view.findViewById(R.id.gallery3);
		imgGal4 = (ImageView) view.findViewById(R.id.gallery4);
		imgGal5 = (ImageView) view.findViewById(R.id.gallery5);

		btn_flag = (Button) view.findViewById(R.id.btn_flag);
		btn_star = (Button) view.findViewById(R.id.btn_star);
		btn_chat = (Button) view.findViewById(R.id.btn_bubble);

		textViewTrans = (TextView) view.findViewById(R.id.transTextView);
		imageViewTrans = (ImageView) view.findViewById(R.id.transImageView);
		customScrollView = (CustomScrollView) view
				.findViewById(R.id.scrollView1);
		photosGridViewHeaderLayout = (RelativeLayout) view
				.findViewById(R.id.photosLayout);
		btn_flag.setOnClickListener(this);
		btn_star.setOnClickListener(this);
		btn_chat.setOnClickListener(this);

		backImage.setOnClickListener(this);
		nextImage.setOnClickListener(this);
		showUserDetailsVisibility.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		// addButton.setOnClickListener(this);
		doneButton.setOnClickListener(this);

		dudeDetailsInterestAdapter = new DudeDetailsInterestAdapter(context,
				(ArrayList<InterestsDTO>) interestsDTOs);
		horizontalListView.setAdapter(dudeDetailsInterestAdapter);

		loadingView = (ImageView) view.findViewById(R.id.loadingView);
		llLoading = (LinearLayout) view.findViewById(R.id.llLoading);

		tvLastActivity = (TextView) view.findViewById(R.id.tvLastActivity);

		tvRole = (TextView) view.findViewById(R.id.tvRole);
		tvBody = (TextView) view.findViewById(R.id.tvBody);
		tvLoveHook = (TextView) view.findViewById(R.id.tvLoveHook);
		photoGrid = (DynamicGridView) view
				.findViewById(R.id.profileDynamicGrid);
		photoGrid.setExpanded(true);
		galleryCount = 0;// reset dis
		// viewPager.setOnClickListener(this);
		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// try {
				customScrollView.setScrollingEnabled(false);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					downX = event.getX();
					downY = event.getY();
					if (comeFrom != AppConstants.FRAGMENT_CHATT_MATCHES) {
						// swipeLayout.setVisibility(View.VISIBLE);
						// //temporary
						// remove layout
					}

					return true;
				}
				case MotionEvent.ACTION_UP: {

					upX = event.getX();
					upY = event.getY();

					float deltaX = downX - upX;
					float deltaY = downY - upY;
					// swipeLayout.setVisibility(View.GONE);
					/* expand profile image */
					try {
						if ((Math.abs(deltaX) < 10 || Math.abs(deltaY) < 10)
								&& (userDTO.getUserPublicProfileImageDTOs()
										.size() != 0)
								&& userDTO.getUserPublicProfileImageDTOs()
										.get(galleryCount).getIsImageActive()) {
							// if (singleton.isPaidUser || galleryCount <= 2
							// || userDTO.isLoginUser()) {
							Intent intent = new Intent(context,
									Activity_ShowUserProfileImage.class);
							intent.putExtra("imageUrl",
									userDTO.getUserPublicProfileImageDTOs()
											.get(galleryCount).getImagePath());
							context.startActivity(intent);
							// } else {
							// context.subscribeToPaidAccount();
							// }
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					/* Init Flurry */
					Map<String, String> swipeDudeParams = new HashMap<String, String>();

					// swipe horizontal?
					if (Math.abs(deltaX) > Math.abs(deltaY)
							&& (comeFrom != AppConstants.FRAGMENT_CHATT_MATCHES)) {
						if (Math.abs(deltaX) > MIN_DISTANCE) {

							// handler1.postDelayed(runnable1, 5000);
							// left or right
							if (deltaX < 0) {
								System.out.println("SwipeRight");
								context.easyTrackerEventLog(
										AppConstants.EVENT_LOG_CATEG_DUDE_PROFILE,
										AppConstants.EVENT_LOG_ACTION_SWIPE,
										"Right");
								logFlurryEvent(swipeDudeParams, "Right");

								galleryCount = 0; // reset dis
								try {
									if (Activity_Home.index == 0) {
										Activity_Home.index = Activity_Home.dudeCommonList
												.size();
										Activity_Home.index = Activity_Home.index - 1;
										UserDTO ud = Activity_Home.dudeCommonList
												.get(Activity_Home.index);

										loadAPI(ud.getUserID());
									} else {
										Activity_Home.index = Activity_Home.index - 1;
										UserDTO ud = Activity_Home.dudeCommonList
												.get(Activity_Home.index);

										loadAPI(ud.getUserID());
									}
									// enableStarFlag();
								} catch (Exception e) {
									System.out.println(e);
								}
								galleryIndicator();

								return true;
							}
							if (deltaX > 0) {
								System.out.println("SwipeLeft");
								context.easyTrackerEventLog(
										AppConstants.EVENT_LOG_CATEG_DUDE_PROFILE,
										AppConstants.EVENT_LOG_ACTION_SWIPE,
										"Left");
								logFlurryEvent(swipeDudeParams, "Left");
								galleryCount = 0; // reset dis
								if (Activity_Home.index < Activity_Home.dudeCommonList
										.size() - 1) {
									Activity_Home.index = Activity_Home.index + 1;
									UserDTO ud = Activity_Home.dudeCommonList
											.get(Activity_Home.index);

									loadAPI(ud.getUserID());
									// enableStarFlag();
								} else {
									Toast.makeText(
											context,
											R.string.toast_reached_last_loaded_guy,
											Toast.LENGTH_SHORT).show();
									Activity_Home.index = 0;

									UserDTO ud = Activity_Home.dudeCommonList
											.get(Activity_Home.index);

									loadAPI(ud.getUserID());
									// context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
									// //go back home
								}
								galleryIndicator();
								return true;
							}

						}
					} else if (Math.abs(deltaY) > Math.abs(deltaX)
					/* && (comeFrom != AppConstants.FRAGMENT_CHATT_MATCHES) */) {
						if (Math.abs(deltaY) > MIN_DISTANCE) {
							if (deltaY > 0) {
								System.out.println("SwipeTop");
								logFlurryEvent(swipeDudeParams, "Top");

								int c = userDTO.getUserPublicProfileImageDTOs()
										.size(); // get total images
								galleryCount++;// increment per swipe up
								if (galleryCount + 1 > c) {
									Toast.makeText(context,
											R.string.toast_reach_end_of_images,
											Toast.LENGTH_SHORT).show();
									galleryCount = c - 1; // retain gallery
															// count value
								} else {
									System.out.println("SIZE : " + c);

									if (userDTO.getUserPublicProfileImageDTOs()
											.get(galleryCount)
											.getIsImageActive() == true) {
										// edited for revision 584
										viewPagerStamp
												.setVisibility(View.INVISIBLE);
										// if (singleton.isPaidUser
										// || galleryCount <= 2
										// || userDTO.isLoginUser()) {
										// if
										// user
										// is
										// not
										// subscribed
										// to
										// premium
										// plan

										imageLoader
												.DisplayImage(
														userDTO.getUserPublicProfileImageDTOs()
																.get(galleryCount)
																.getImagePath(),
														viewPager);
										// } else {
										//
										// viewPager
										// .setImageResource(R.drawable.lock_white_bg_big);
										//
										// }
									} else {
										viewPagerStamp
												.setVisibility(View.VISIBLE);
									}

									galleryImagePage();
								}
								return true;
							}
							if (deltaY < 0) {
								System.out.println("SwipeBot");
								logFlurryEvent(swipeDudeParams, "Bottom");

								galleryCount--; // increment per swipe down
								if (galleryCount < 0) {
									Toast.makeText(
											context,
											R.string.toast_reach_start_of_images,
											Toast.LENGTH_SHORT).show();
									galleryCount = 0; // retain gallery
														// count
														// value
								} else {
									if (userDTO.getUserPublicProfileImageDTOs()
											.get(galleryCount)
											.getIsImageActive() == true) {

										// edited for revision 584
										viewPagerStamp
												.setVisibility(View.INVISIBLE);
										// if (singleton.isPaidUser
										// || galleryCount <= 2
										// || userDTO.isLoginUser()) {
										// if
										// user
										// is
										// not
										// subscribed
										// to
										// premium
										// plan
										imageLoader
												.DisplayImage(
														userDTO.getUserPublicProfileImageDTOs()
																.get(galleryCount)
																.getImagePath(),
														viewPager);
										// } else {
										// viewPager
										// .setImageResource(R.drawable.lock_white_bg_big);
										//
										// }
									} else {
										viewPagerStamp
												.setVisibility(View.VISIBLE);
									}
									galleryImagePage();
								}
								return true;
							}
						}

					} else {

						// openGallery();
						return false; // We don't consume the event
					}
					return true;
				}

				}
				return false;
				// } catch (Exception e) {
				//
				// e.printStackTrace();
				// return false;
				// }
			}

		});

	}

	Handler handler1 = new Handler();
	Runnable runnable1 = new Runnable() {

		@Override
		public void run() {

			// swipeLayout.setVisibility(View.GONE);

		}
	};

	public void logFlurryEvent(Map<String, String> params, String event) {
		params.put("Swipe", event);
		FlurryAgent.logEvent("Detail Dudes Profile", params); // Edited revision
																// 582 commented
	}

	// private void openGallery() {
	// if (userDTO.getUserProfileImageDTOs().size() > 0) {
	// Activity_DudeGallery.userDTO = userDTO;
	// Intent intent = new Intent(context, Activity_DudeGallery.class);
	// context.startActivity(intent);
	// }
	// }

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		handler.removeCallbacks(runnable);
		System.gc();
		context.left_button.setImageResource(R.drawable.av_new_nav_menuup);

	}

	public void loadAPI(String userID) {
		// userDTO = Activity_Home.dudeCommonList.get(index);
		if (WebServiceConstants.isOnline(context)) {
			// new GetSpecificUser()
			// .execute(
			// "http://54.219.211.237/KraveAPI/api_calls/retrieve-specific-user.php",
			// userID);
			new GetSpecificUser()
					.execute(
							"http://54.219.211.237/KraveAPI/api_calls/retrieve-specific-user_ios.php",
							userID);
			new GetIsFavorite().execute(
					"http://54.219.211.237/KraveAPI/api_calls/isFavorite.php",
					userID);
			new RetrieveActivityLog().execute(WebServiceConstants.AV_BASE_URL
					+ WebServiceConstants.AV_RETRIEVE_ACTIVITY_LOG);
		} else {
			Toast.makeText(context,
					R.string.toast_please_check_network_connection,
					Toast.LENGTH_LONG).show();
		}
	}

	class GetIsFavorite extends AsyncTask<String, Void, JSONObject> {
		String vStatus;
		TransparentProgressDialog dialog;
		String userID;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadCount++;
			// singleton.progressLoading(loadingView, llLoading);
		}

		protected JSONObject doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			userID = args[1]; // selected user's ID
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("friend_id", userID));// ""+Activity_Home.gblUserID));
			JSONObject json = new JSONParser().makeHttpRequest2(args[0],
					"POST", params);
			// JSONArray json = new JSONParser().makeHttpRequest(args[0],
			// "POST",
			// params);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObj) {
			super.onPostExecute(jsonObj);
			// dialog.dismiss();
			--loadCount;
			if (loadCount == 0)
				singleton.stopLoading(llLoading);
			try {

				// edited for revision 608
				// May 15 2015
				// added if statement
				if (!(jsonObj.getString("isFavorite") == null)) {
					System.out.println("isFavorites : "
							+ jsonObj.getString("isFavorite"));
					// /* KOSA's isFavorite */
					userDTO.setFavorite(jsonObj.getString("isFavorite"));
					btn_star.setBackgroundResource(userDTO.isFavorite().equals(
							FAVORITE_USER) ? R.drawable.av_new_detail_minus_up
							: R.drawable.av_new_detail_starup);
				}
				// -- - - - - - -

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class GetSpecificUser extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;
		String userID;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			singleton.progressLoading(loadingView, llLoading);
			loadCount++;
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			userID = args[1];
			params.add(new BasicNameValuePair("user_id", userID));// ""+Activity_Home.gblUserID));
			params.add(new BasicNameValuePair("login_id", sessionManager
					.getUserDetail().getUserID()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			--loadCount;
			if (loadCount == 0)
				singleton.stopLoading(llLoading);

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);

				parseUserDataAndSetInSession(mJsonObject);
				Log.d("", "privateImageRequestCounte=r"
						+ privateImageRequestCounter);
				new GetAllChoices()
						.execute("http://54.219.211.237/KraveAPI/api_calls/returnAllChoices.php");
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class SendPhotoRequest extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;
		String userID;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadCount++;
			singleton.progressLoading(loadingView, llLoading);
		}

		protected JSONArray doInBackground(String... args) {
			// http://54.219.211.237/index.php?action=sendRequest&user_id=278&owner_id=280&image_id=952
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// selected user's ID
			params.add(new BasicNameValuePair("user_id", args[1]));
			params.add(new BasicNameValuePair("owner_id", args[2]));
			params.add(new BasicNameValuePair("image_id", args[3]));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("", "params=" + params);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			Log.d("", "SendPhotoRequest=" + jsonArray);
			// dialog.dismiss();
			--loadCount;
			if (loadCount == 0)
				singleton.stopLoading(llLoading);
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String status = jsonObject.getString("status");
				if (status.equals("success")) {

					privateImageRequestCounter = Integer.valueOf(jsonObject
							.getString("Totalpuablishrequset"));

					Toast.makeText(context,
							R.string.toast_request_successfully_send,
							Toast.LENGTH_SHORT).show();
				} else if (status.equals("request already sent")) {
					Toast.makeText(context,
							R.string.toast_request_already_send,
							Toast.LENGTH_SHORT).show();
				} else if (status.equals("user logout")) {
					Toast.makeText(context,
							R.string.toast_user_currently_logout,
							Toast.LENGTH_SHORT).show();
				} else if (status.equals("failure")) {
					Toast.makeText(context,
							R.string.toast_request_send_failed_try_again,
							Toast.LENGTH_SHORT).show();
				}
				// -- - - - - - -
				Log.d("", "privateImageRequestCounte=r"
						+ privateImageRequestCounter);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		LatLongDTO latLongDTO = new LatLongDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<UserProfileImageDTO> userPublicProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		JSONObject MainObject = mJsonObject.getJSONObject("user");

		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
		// List<FacebookLikesDTO> facebookLikesDTOs = new
		// ArrayList<FacebookLikesDTO>();
		// JSONArray jsonArrayFacebookLikes = mJsonObject
		// .getJSONArray("fbintrast");
		// System.out.println(MainObject);

		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
		userDTO.setBodyType(MainObject.getString("body_type"));
		userDTO.setRoleId(MainObject.getString("role"));
		userDTO.setLoveHookup(MainObject.getString("love_hookup"));
		userDTO.setLastActiveDate(MainObject.getString("time_stamp"));
		try {
			privateImageRequestCounter = Integer.valueOf(MainObject
					.getString("Totalpuablishrequset"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sessionManager.getUserDetail().getUserID()
				.equals(userDTO.getUserID())) {
			userDTO.setLoginUser(true);
		}

		if (MainObject.getString("isonline").equals("available")) {
			userDTO.setIsOnline(AppConstants.ONLINE);

		} else if (MainObject.getString("isonline").equals("away")) {
			userDTO.setIsOnline(AppConstants.ABSENT);
		} else {
			userDTO.setIsOnline(AppConstants.OFFLINE);
		}

		// if (MainObject.getString("user_facebook_Interest").equals(
		// AppConstants.FACEBOOK_LIKE_ENABLE)) {
		// userDTO.setFacebookLikeEnable(true);
		// } else {
		// userDTO.setFacebookLikeEnable(false);
		// }

		userDTO.setCommonFriends(MainObject.getString("mutualfriends"));

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

		interestsDTOs.clear();
		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrests_id"));

			boolean already = false;
			for (InterestsDTO dto : interestsDTOs) {
				if (dto.getInterestId().equalsIgnoreCase(
						interestsDTO.getInterestId())) {
					already = true;
					break;
				}
			}
			if (already)
				break;

			interestsDTO.setInterestName(interestJsonObject
					.getString("intrests_name"));

			// edited for revision 607
			// MAY 15 2015
			// added if statement
			if (!interestJsonObject.getString("intrests_image").isEmpty()) {
				interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
						+ interestJsonObject.getString("intrests_image"));
			}
			// - - -- -- -
			interestsDTOs.add(interestsDTO);

		}
		Boolean check = false;

		// edited on revision -- as is -- dl
		// MAY 14 2015
		for (int i = 0; i < jsonArrayImages.length(); i++) {
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
			// -- - -- --
			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}
			/* is login user profile then set all images as public */
			boolean isPublic = true;

			// if (!sessionManager.getUserDetail().getUserID()
			// .equals(userDTO.getUserID())) {
			isPublic = (imagesJsonObject.getString("is_public").equals(
					AppConstants.IMAGE_PUBLIC) ? true : false);
			// }
			userProfileImageDTO.setPublic(isPublic);
			if (isPublic /* || userDTO.isLoginUser() */) {
				userPublicProfileImageDTOs.add(userProfileImageDTO);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}
		showAllPublicImage(userPublicProfileImageDTOs);
		for (int k = 0; k < userProfileImageDTOs.size(); k++) {
			UserProfileImageDTO dto = userProfileImageDTOs.get(k);
			if (dto.getIsImageActive()) {
				// edited on revision 588
				// userProfileImageDTOs.remove(dto);
				// userProfileImageDTOs.add(0, dto);
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
		// if (!"url".equals(userDTO.getProfileImage())) {
		// Log.d("", "facebook image at first position in list");
		// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
		// userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
		// userProfileImageDTO.setImagePath(userDTO.getProfileImage());
		// userProfileImageDTO.setIsImageActive(true);
		// userProfileImageDTOs.add(0, userProfileImageDTO);
		// }
		//
		// for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
		// JSONObject facebookJsonObject = jsonArrayFacebookLikes
		// .getJSONObject(i);
		// FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
		// facebookLikesDTO.setLikeId(facebookJsonObject
		// .getString("intrest_id"));
		//
		// facebookLikesDTOs.add(facebookLikesDTO);
		//
		// }

		// userDTO.setFacebookLikesDTOs(facebookLikesDTOs);

		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setUserPublicProfileImageDTOs(userPublicProfileImageDTOs);
		// int online = contex.getUserAvailability(userDTO.getUserID());
		// Log.d("", "is online id " + userDTO.getUserID() + " '" + online);
		// userDTO.setIsOnline("" + online);

		this.userDTO = userDTO;
		// findDudesAdapter.notifyDataSetChanged();
		loadData(userDTO);
		// setLastOnline(userDTO.getLastActiveDate());
	}

	private void showAllPublicImage(
			List<UserProfileImageDTO> userPublicProfileImageDTOs) {
		if (singleton.isPaidUser) {
			for (int j = 0; j < userPublicProfileImageDTOs.size(); j++) {
				userPublicProfileImageDTOs.get(j).setPaidImage(true);
			}
		} else {

			for (int j = 0; (j < 3 && j < userPublicProfileImageDTOs.size()); j++) {
				userPublicProfileImageDTOs.get(j).setPaidImage(true);
			}
		}
	}

	public void refreshGridView() {
		showAllPublicImage(userDTO.getUserPublicProfileImageDTOs());
		if (dudeProfileImageGridViewAdapter != null)
			dudeProfileImageGridViewAdapter.notifyDataSetChanged();
	}

	// private void setLastOnline() {
	// String lastUserAct = "online";
	// boolean isOnline = false;
	// try {
	// if (userDTO.getIsOnline().equalsIgnoreCase(
	// AppConstants.ONLINE)) {
	// isOnline = true;
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// if (!isOnline) {
	// lastUserAct = "online recently";
	// }
	//
	// tvLastActivity.setText(lastUserAct);
	// }

	public void loadData(final UserDTO userDTO) {

		viewPagerStamp.setVisibility(View.GONE);
		if (userDTO.getUserPublicProfileImageDTOs().size() > 0) {
			if (userDTO.getUserPublicProfileImageDTOs().get(0).getImageId()
					.equals(AppConstants.FACEBOOK_IMAGE)) {
				viewPager.setScaleType(ScaleType.FIT_CENTER);
				imageLoader.DisplayImage(userDTO
						.getUserPublicProfileImageDTOs().get(0).getImagePath(),
						viewPager);
			} else {
				viewPager.setScaleType(ScaleType.FIT_CENTER);
				if (!userDTO.getUserPublicProfileImageDTOs().get(0)
						.getIsImageActive()) {
					// Edited on revision 588
					// dl May 13 2015
					// viewPager
					// .setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
					viewPagerStamp.setVisibility(View.VISIBLE);
				} else {
					imageLoader.DisplayImage(userDTO
							.getUserPublicProfileImageDTOs().get(0)
							.getImagePath(), viewPager);
				}
			}
			photosGridViewHeaderLayout.setVisibility(View.VISIBLE);
		} else {

			// edited on revision 588
			// dl May 13 2015
			viewPager
					.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);

			// viewPagerStamp.setVisibility(View.VISIBLE);
			if (userDTO.getUserProfileImageDTOs().size() <= 0) {
				photosGridViewHeaderLayout.setVisibility(View.GONE);
			}
		}

		if (userDTO.getIsOnline().equals(AppConstants.ONLINE)) {
			userStatusImage.setBackgroundResource(R.drawable.online);
			userStatusText.setText("ONLINE");
		} else {
			userStatusImage.setBackgroundResource(R.drawable.red_crcl);
			userStatusText.setText("OFFLINE");
		}

		String age = "";
		try {
			age = calculateAge(userDTO.getWhatAreYouDTO().getAge());
		} catch (Exception e) {

		}
		name.setText(userDTO.getFirstName() + " | " + age);

		/* set ethinicity */
		String ethinicityArray[] = getResources().getStringArray(
				R.array.ethnicity_sample);
		int ethinicityId = 0;
		try {
			ethinicityId = Integer.valueOf(userDTO.getWhatAreYouDTO()
					.getWhatAreYou()) - 1;
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.d("ethinicityId", "ethinicityId="
					+ userDTO.getWhatAreYouDTO().getWhatAreYou());
		}
		whatAreYou.setText(ethinicityArray[ethinicityId]);

		String addressString = "";
		if (!(userDTO.getCity() == null)
				&& !(userDTO.getCity().equals("(null)"))
				&& !(userDTO.getCity().contains("null"))) {
			addressString = addressString + userDTO.getCity();// + ", ";
		}
		// if (!(userDTO.getCountry() == null)) {
		// addressString = addressString + userDTO.getCountry();
		// }
		// String addressString = userDTO.getCity() + ", " +
		// userDTO.getCountry();
		address.setText(addressString);
		// commonFriends.setText("");
		relationShipStatus.setText(getRelationShipStatus());

		setUserHightWeghtNew(userDTO);
		if (userDTO.getAboutMe().length() == 0) {
			aboutDude.setText(R.string.profile_about_me_default_string);
		} else {
			aboutDude.setText(userDTO.getAboutMe());
		}

		Float distance = (float) 0.0;
		try {
			distance = distFrom(gpsTracker.getLatitude(),
					gpsTracker.getLongitude(),
					Double.valueOf(userDTO.getLatLongDTO().getLatitude()),
					Double.valueOf(userDTO.getLatLongDTO().getLongitude()));
		} catch (Exception e) {

		}
		distanceAway.setText("" + distance
				+ getResources().getString(R.string.home_miles_away));
		interestsDTOs.clear();

		// List<InterestsDTO> sesstionInterestsDTOs = sessionManager
		// .getUserDetail().getInterestList();

		// for (int i = 0; i < sesstionInterestsDTOs.size(); i++) {
		// String temp = sesstionInterestsDTOs.get(i).getInterestId();
		//
		// Log.d("", "session interest id=" + temp);
		//
		// for (int k = 0; k < userDTO.getInterestList().size(); k++) {
		//
		// InterestsDTO dto = userDTO.getInterestList().get(k);
		// Log.d("", "dto interest id=" + dto.getInterestId());
		// if (dto.getInterestId().equals(temp)) {
		// Log.d("", "interest selection =true " + temp);
		// dto.setIsSelected(true);
		// }
		// }
		//
		// }

		// for (int j = 0; j < userDTO.getFacebookLikesDTOs().size(); j++) {
		// String id = userDTO.getFacebookLikesDTOs().get(j).getLikeId();
		// InterestsDTO dto = new InterestsDTO();
		// dto.setIsSelected(true);
		// dto.setInterestId(id);
		// dto.setInterestIcon("https://graph.facebook.com/" + id
		// + "/picture?type=large");
		// userDTO.getInterestList().add(dto);
		//
		// }
		interestsDTOs.clear();
		interestsDTOs.addAll(userDTO.getInterestList());
		dudeDetailsInterestAdapter.notifyDataSetChanged();
		// setPagerView();

		textViewTrans.setText(distanceConverter.convertToString(
				(double) distance, distanceConverter.getConversion(transPref)));

		imageViewTrans.setVisibility(View.VISIBLE);
		textViewTrans.setVisibility(View.VISIBLE);
		switch (transPref) {
		case 2:
			imageViewTrans.setImageResource(R.drawable.transit_walking);
			break;
		case 3:
			imageViewTrans.setImageResource(R.drawable.transit_biking);
			break;
		case 4:
			imageViewTrans.setImageResource(R.drawable.transit_car);
			break;
		case 5:
			imageViewTrans.setImageResource(R.drawable.transit_public);
			break;
		default:
			imageViewTrans.setVisibility(View.GONE);
			textViewTrans.setVisibility(View.GONE);
			break;
		}

		handler.postDelayed(runnable, 0);

		galleryIndicator();

		// set grid view
		dudeProfileImageGridViewAdapter = new DudeProfileImageGridViewAdapter(
				context, userDTO);
		photoGrid.setAdapter(dudeProfileImageGridViewAdapter);
		photoGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserProfileImageDTO profileImageDTO = userDTO
						.getUserProfileImageDTOs().get(position);
				if (profileImageDTO.isPublic() || userDTO.isLoginUser()) {
					// if (profileImageDTO.isPaidImage() ||
					// userDTO.isLoginUser()) { // condition

					if (profileImageDTO.getIsImageActive()) {
						Intent intent = new Intent(context,
								Activity_ShowUserProfileImage.class);
						intent.putExtra("imageUrl",
								profileImageDTO.getImagePath());
						context.startActivity(intent);
					}
					// } else {
					// context.subscribeToPaidAccount();
					// }
				} else {
					if (profileImageDTO.isPaidImage()
							|| (privateImageRequestCounter < 3)) {
						openDailog(userDTO.getUserID(),
								profileImageDTO.getImageId());
					} else {
						context.subscribeToPaidAccount();
					}
				}

			}
		});
	}

	// private void setUserHightWeght(UserDTO userDTO) {
	// String hight = "";
	// String weight = "";
	// relationShipStatus.setText(getRelationShipStatus());
	// Log.d("HEIGHT WEIGHT ", "MUCH HIGH : "
	// + userDTO.getWhatAreYouDTO().getHight() + " MUCH FAT : "
	// + userDTO.getWhatAreYouDTO().getWeight());
	// System.out.println("H UNIT : "
	// + userDTO.getWhatAreYouDTO().getHeightUnit() + " FAT UNIT : "
	// + userDTO.getWhatAreYouDTO().getWeightUnit());
	// try {
	// // if (!(userDTO.getWhatAreYouDTO().getHight().contains("null"))
	// // && !(userDTO.getWhatAreYouDTO().getHight().contains("0/0"))
	// // && !(userDTO.getWhatAreYouDTO().getHight().equals("0"))) { //
	// // check
	// // whether
	// // value
	// // is
	// // null
	// // or
	// // zero
	// if (userDTO.getWhatAreYouDTO().getHeightUnit()
	// .equals(AppConstants.HEIGHT_IN_FEET)) {
	//
	// try { // old format
	// hight = userDTO.getWhatAreYouDTO().getHight().split("/")[0]
	// + "'"
	// + userDTO.getWhatAreYouDTO().getHight().split("/")[1]
	// + "\"";// + " (US)";
	// } catch (Exception e) { // inches format
	//
	// String inchToFeet = String.format("%.1f",
	// (Float.valueOf(userDTO.getWhatAreYouDTO()
	// .getHight()) * 0.0833333f));
	// System.out.println("HEIIIGHT : " + inchToFeet);
	// hight = Character.toString(inchToFeet.charAt(0)) + "'"
	// + Character.toString(inchToFeet.charAt(2)) + "\"";
	// }
	// } else {
	//
	// try {
	// hight = userDTO.getWhatAreYouDTO().getHight().split("/")[0]
	// + " m,"
	// + userDTO.getWhatAreYouDTO().getHight().split("/")[1]
	// + " cm";// +
	// // " (METRICs)";
	// } catch (Exception e) {
	// hight = userDTO.getWhatAreYouDTO().getHight() + "cm";
	// }
	// }
	// // }
	// // if (!(userDTO.getWhatAreYouDTO().getWeight().contains("null"))
	// // && !(userDTO.getWhatAreYouDTO().getWeight().contains("0/0"))
	// // && !(userDTO.getWhatAreYouDTO().getWeight().equals("0"))) { //
	// // check
	// // whether
	// // value
	// // is
	// // null
	// // or
	// // zero
	// if (userDTO.getWhatAreYouDTO().getWeightUnit()
	// .equals(AppConstants.WEIGHT_IN_POUND)) {
	// weight = userDTO.getWhatAreYouDTO().getWeight().split("/")[0]
	// + " lbs";// + " (US)";
	// // + userDTO.getWhatAreYouDTO().getWeight().split("/")[1]
	// // + " oz";
	// } else {
	// weight = userDTO.getWhatAreYouDTO().getWeight().split("/")[0]
	// + " kg";// + " (METRICs)";
	// // + userDTO.getWhatAreYouDTO().getWeight().split("/")[1]
	// // + " Gms";
	// }
	// // }
	// } catch (Exception e) {
	// }
	// if (hight.length() != 0 && weight.length() != 0) {
	// hight = hight + " / ";
	// }
	// relationShipStatus.setText(hight + weight);
	//
	// }

	private void setUserHightWeghtNew(UserDTO userDTO) {
		String hightString = userDTO.getWhatAreYouDTO().getHight();
		String weightString = userDTO.getWhatAreYouDTO().getHight();
		String hightUnit = userDTO.getWhatAreYouDTO().getHeightUnit();
		String weightUnit = userDTO.getWhatAreYouDTO().getWeightUnit();
		String hight = "";
		String weight = "";

		try {
			if (!(hightString.contains("null")) && !(hightString.equals("0/0"))
					&& !(hightString.equals("0"))) { // check
														// whether
														// value
														// is
														// null
														// or
														// zero
				if (hightUnit.equals(AppConstants.HEIGHT_IN_FEET)) {

					try {
						if (sessionManager.getUserDetail().getWhatAreYouDTO()
								.getHeightUnit().equals(hightUnit)) {
							hight = hightString.split("/")[0] + "'"
									+ hightString.split("/")[1] + "\"";// +
																		// " (US)";
						} else {

							int inches = (Integer.valueOf(hightString
									.split("/")[0]) * 12 + Integer
									.valueOf(hightString.split("/")[1]));
							int cm = (int) (inches * 2.54);
							hight = cm / 100 + " m," + cm % 100 + " cm";
						}
					} catch (Exception e) { // inches format
						if (sessionManager.getUserDetail().getWhatAreYouDTO()
								.getHeightUnit().equals(hightUnit)) {
							String inchToFeet = String.format("%.1f",
									(Float.valueOf(hightString) * 0.0833333f));
							System.out.println("HEIIIGHT : " + inchToFeet);
							hight = Character.toString(inchToFeet.charAt(0))
									+ "'"
									+ Character.toString(inchToFeet.charAt(2))
									+ "\"";
						} else {
							float cm = (float) (Integer.valueOf(hightString) * 2.54);
							hight = cm / 100 + " m," + cm % 100 + " cm";
						}
					}
				} else {

					try {
						if (sessionManager.getUserDetail().getWhatAreYouDTO()
								.getHeightUnit().equals(hightUnit)) {
							hight = hightString.split("/")[0] + " m,"
									+ hightString.split("/")[1] + " cm";// +
																		// " (METRICs)";
						} else {
							int cm = Integer.valueOf(hightString.split("/")[0])
									* (100)
									+ Integer
											.valueOf(hightString.split("/")[1]);
							int inches = (int) (cm * 0.393701);
							hight = inches / 12 + "'" + inches % 12 + "\"";
						}
					} catch (Exception e) {
						if (sessionManager.getUserDetail().getWhatAreYouDTO()
								.getHeightUnit().equals(hightUnit)) {
							int cm = Integer.valueOf(hightString);
							hight = cm / 100 + " m," + cm % 100 + " cm";
						} else {
							int cm = Integer.valueOf(hightString);
							int inches = (int) (cm * 0.393701);
							hight = inches / 12 + "'" + inches % 12 + "\"";
						}
					}
				}
			}
		} catch (Exception e) {
		}
		try {
			if (!(userDTO.getWhatAreYouDTO().getWeight().contains("null"))
					&& !(userDTO.getWhatAreYouDTO().getWeight().equals("0/0"))
					&& !(userDTO.getWhatAreYouDTO().getWeight().equals("0"))) { // check
				// whether
				// value
				// is
				// null
				// or
				// zero
				if (userDTO.getWhatAreYouDTO().getWeightUnit()
						.equals(AppConstants.WEIGHT_IN_POUND)) {
					if (sessionManager.getUserDetail().getWhatAreYouDTO()
							.getWeightUnit().equals(weightUnit)) {
						weight = userDTO.getWhatAreYouDTO().getWeight()
								.split("/")[0]
								+ " lbs";// + " (US)";
						// +
						// userDTO.getWhatAreYouDTO().getWeight().split("/")[1]
						// + " oz";
					} else {
						int kg = (int) Math
								.round(((Integer.valueOf(userDTO
										.getWhatAreYouDTO().getWeight()
										.split("/")[0])) * 0.453592));
						weight = kg + " kg";
					}
				} else {
					if (sessionManager.getUserDetail().getWhatAreYouDTO()
							.getWeightUnit().equals(weightUnit)) {
						weight = userDTO.getWhatAreYouDTO().getWeight()
								.split("/")[0]
								+ " kg";// + " (METRICs)";
						// +
						// userDTO.getWhatAreYouDTO().getWeight().split("/")[1]
						// + " Gms";
					} else {
						int lb = (int) Math
								.round(((Integer.valueOf(userDTO
										.getWhatAreYouDTO().getWeight()
										.split("/")[0])) * 2.20462));
						weight = lb + " lbs";
					}
				}
			}
		} catch (Exception e) {
		}
		if (hight.length() != 0 && weight.length() != 0) {
			hight = hight + " / ";
		}
		userHightWeight.setText(hight + weight);

	}

	private String calculateAge(String berthDateString) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date berthDate = null;
		try {
			berthDate = format.parse(berthDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date currentDate = new Date();

		long time = currentDate.getTime() - berthDate.getTime();

		long diffInDays = (long) time / (1000 * 60 * 60 * 24);

		long number_of_years = (long) diffInDays / 365;
		long number_of_months = (long) (diffInDays % (365)) / 30;

		return "" + number_of_years;

	}

	public void galleryImagePage() {

		if (galleryCount + 1 == 1) {
			System.out.println("gall count 1");
			imgGal1.setImageResource(R.drawable.select_notification);
			imgGal2.setImageResource(R.drawable.unselect_notofication);
			imgGal3.setImageResource(R.drawable.unselect_notofication);
			imgGal4.setImageResource(R.drawable.unselect_notofication);
			imgGal5.setImageResource(R.drawable.unselect_notofication);
		} else if (galleryCount + 1 == 2) {
			System.out.println("gall count 2");
			imgGal1.setImageResource(R.drawable.unselect_notofication);
			imgGal2.setImageResource(R.drawable.select_notification);
			imgGal3.setImageResource(R.drawable.unselect_notofication);
			imgGal4.setImageResource(R.drawable.unselect_notofication);
			imgGal5.setImageResource(R.drawable.unselect_notofication);
		} else if (galleryCount + 1 == 3) {
			System.out.println("gall count 3");
			imgGal1.setImageResource(R.drawable.unselect_notofication);
			imgGal2.setImageResource(R.drawable.unselect_notofication);
			imgGal3.setImageResource(R.drawable.select_notification);
			imgGal4.setImageResource(R.drawable.unselect_notofication);
			imgGal5.setImageResource(R.drawable.unselect_notofication);
		} else if (galleryCount + 1 == 4) {
			System.out.println("gall count 4");
			imgGal1.setImageResource(R.drawable.unselect_notofication);
			imgGal2.setImageResource(R.drawable.unselect_notofication);
			imgGal3.setImageResource(R.drawable.unselect_notofication);
			imgGal4.setImageResource(R.drawable.select_notification);
			imgGal5.setImageResource(R.drawable.unselect_notofication);
		} else if (galleryCount + 1 == 5) {
			System.out.println("gall count 5");
			imgGal1.setImageResource(R.drawable.unselect_notofication);
			imgGal2.setImageResource(R.drawable.unselect_notofication);
			imgGal3.setImageResource(R.drawable.unselect_notofication);
			imgGal4.setImageResource(R.drawable.unselect_notofication);
			imgGal5.setImageResource(R.drawable.select_notification);
		}
	}

	public void imgGalSetVis(int gsize) {
		imgGal1.setVisibility(View.INVISIBLE);
		imgGal2.setVisibility(View.INVISIBLE);
		imgGal3.setVisibility(View.INVISIBLE);
		imgGal4.setVisibility(View.INVISIBLE);
		imgGal5.setVisibility(View.INVISIBLE);

	}

	public void galleryIndicator() {
		int gsize = userDTO.getUserPublicProfileImageDTOs().size();
		imgGalSetVis(gsize);

		if (gsize > 1) {
			m_gallery.setVisibility(View.VISIBLE);
			imgGal1.setImageResource(R.drawable.select_notification);
			imgGal2.setImageResource(R.drawable.unselect_notofication);
			imgGal3.setImageResource(R.drawable.unselect_notofication);
			imgGal4.setImageResource(R.drawable.unselect_notofication);
			imgGal5.setImageResource(R.drawable.unselect_notofication);

			imgGal1.setVisibility(View.VISIBLE);
			imgGal2.setVisibility(View.VISIBLE);
			imgGal3.setVisibility((gsize > 2) ? View.VISIBLE : View.INVISIBLE);
			imgGal4.setVisibility((gsize > 3) ? View.VISIBLE : View.INVISIBLE);
			imgGal5.setVisibility((gsize > 4) ? View.VISIBLE : View.INVISIBLE);
		} else {
			m_gallery.setVisibility(View.INVISIBLE);
		}
		// if(gsize > 1)
		// {
		// m_gallery.setVisibility(View.VISIBLE);
		// imgGal1.setImageResource(R.drawable.select_notification);
		// imgGal2.setImageResource(R.drawable.unselect_notofication);
		// imgGal3.setImageResource(R.drawable.unselect_notofication);
		// imgGal2.setVisibility(View.VISIBLE);
		// imgGal3.setVisibility(View.VISIBLE);
		// if(gsize == 2)
		// {
		// imgGal3.setVisibility(View.INVISIBLE);
		// }
		// }else{
		// m_gallery.setVisibility(View.INVISIBLE);
		// }
	}

	public float distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;// 1609.34

		// return (float) (dist * meterConversion);
		return (float) (Math.round(dist * 100.0) / 100.0);

	}

	private CharSequence getRelationShipStatus() {
		int i = 3;
		try {
			i = Integer.valueOf(userDTO.getWhatAreYouDTO()
					.getRelationshipStatus());
		} catch (Exception e) {

		}
		Log.d("", "relation ="
				+ userDTO.getWhatAreYouDTO().getRelationshipStatus());
		String relation = "";
		String relationshipArray[] = getResources().getStringArray(
				R.array.relationship_status_array);
		if (i != 3) {
			try {
				relation = relationshipArray[i - 1];
			} catch (Exception e) {

			}
		}
		// switch (i) {
		// case 1:
		// relation = "Single";
		// break;
		// case 2:
		// relation = "In a relationship";
		// break;
		// case 3:
		// // Empty string if don't show
		// relation = "";
		// break;
		// }
		return relation;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			cancelButtonEvent();
			break;

		case R.id.done:
			// doneButtonEvent();

			break;

		case R.id.hideShowImage:
			if (isHide) {
				setVisibility(View.VISIBLE);
				showUserDetailsVisibility.setImageDrawable(context
						.getResources().getDrawable(R.drawable.arrow_down));

				isHide = false;
			} else {
				setVisibility(View.GONE);
				showUserDetailsVisibility.setImageDrawable(context
						.getResources().getDrawable(R.drawable.arrow_up));
				isHide = true;
			}
			break;
		case R.id.backimage:
			int index = horizontalListView.Getposition();

			horizontalListView.scrollTo(index + 50);
			break;
		case R.id.nimage:
			int index1 = horizontalListView.Getposition();

			horizontalListView.scrollTo(index1 - 50);
			break;
		case R.id.viewpager1:
			Activity_DudeGallery.userDTO = userDTO;
			Intent intent = new Intent(context, Activity_DudeGallery.class);
			context.startActivity(intent);
			break;
		case R.id.btn_flag: // block
			callBlockLayout(); // this inflates the block user layout
			context.easyTrackerEventLog(
					AppConstants.EVENT_LOG_CATEG_DUDE_PROFILE,
					AppConstants.EVENT_LOG_ACTION_BTN, "Report");
			break;
		case R.id.btn_star: // favorite
			if (userDTO.isFavorite().equals(FAVORITE_USER)) {
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_DUDE_PROFILE,
						AppConstants.EVENT_LOG_ACTION_BTN, "Favorite");
				removeToFavButtonEvent();
				btn_star.setBackgroundResource(R.drawable.av_new_detail_starup);
			} else {
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_DUDE_PROFILE,
						AppConstants.EVENT_LOG_ACTION_BTN, "Unfavorite");
				addToFavButtonEvent(); // this adds dude to favorite list on
										// click
				btn_star.setBackgroundResource(R.drawable.av_new_detail_minus_up);
			}
			break;
		case R.id.btn_bubble: // chat
			context.easyTrackerEventLog(
					AppConstants.EVENT_LOG_CATEG_DUDE_PROFILE,
					AppConstants.EVENT_LOG_ACTION_BTN, "Chat");
			bubbleButtonEvent(); // go to chat guy
			doneButtonEvent(); // send and accept friend request
			singleton.isReloadChatNav = true;
			break;

		default:
			break;
		}

	}

	private void callBlockLayout() {
		FragmentChatMatches.userDTO = userDTO;
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		singleton.isFromFlagged = FLAG_TRUE;

		Intent intent = new Intent(context, Activity_Block_Dude.class);
		context.startActivity(intent);
	}

	private void bubbleButtonEvent() {
		FragmentHome.Refresh_Data = true;
		// FragmentChatMatches.userDTO = Activity_Home.dudeCommonList
		// .get(Activity_Home.index);
		FragmentChatMatches.userDTO = this.userDTO;
		context.Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
	}

	private void cancelButtonEvent() {
		if (Activity_Home.index < Activity_Home.dudeCommonList.size() - 1) {
			Activity_Home.index = Activity_Home.index + 1;
			// loadAPI(Activity_Home.index);
		}

	}

	private void addToFavButtonEvent() {
		if (WebServiceConstants.isOnline(context)) {
			new AddToFavAsynchTask()
					.execute("http://54.219.211.237/KraveAPI/api_calls/add-user-to-favorites.php");
		} else {
			Toast.makeText(context,
					R.string.toast_please_connect_to_the_internet,
					Toast.LENGTH_SHORT).show();
		}

	}

	private void removeToFavButtonEvent() {
		if (WebServiceConstants.isOnline(context)) {
			new RemoveFromFavAsynchTask()
					.execute("http://54.219.211.237/KraveAPI/api_calls/remove-user-to-favorite.php");
		} else {
			Toast.makeText(context,
					R.string.toast_please_connect_to_the_internet,
					Toast.LENGTH_SHORT).show();
		}

	}

	private void setVisibility(int id) {
		// ELv - remove setting of relationship status visibility
		// relationShipStatus.setVisibility(id);
		userHightWeight.setVisibility(id);
		aboutDude.setVisibility(id);
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			Log.d("", "online thread");
			if (WebServiceConstants.isOnline(context)) {
				// edited for revision 608
				// MAY 20 2015
				// commented
				// new GetOnline().execute(userDTO.getUserID());
			}

		}
	};

	// //Logic for online user////
	public class GetOnline extends AsyncTask<Object, Void, String> {
		ImageView imageView;
		MyXMLHandler myXMLHandler;

		@Override
		protected String doInBackground(Object... arg0) {
			// TODO Auto-generated method stub

			// try {

			try {
				/** Handling XML */
				Log.d("", "online/offline user ID=" + arg0[0]);
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				/** Send URL to parse XML Tags */

				// http://54.241.85.74:9090/plugins/presence/status?jid=109@ip-10-199-23-53&type=xml
				URL sourceUrl = new URL( // changed ip 54.241.85.74
						"http://184.169.159.101:9090/plugins/presence/status?jid="
								+ arg0[0] + "@ip-10-199-23-53&type=xml");
				/** Create handler to handle XML Tags ( extends DefaultHandler ) */
				myXMLHandler = new MyXMLHandler();
				xr.setContentHandler(myXMLHandler);
				xr.parse(new InputSource(sourceUrl.openStream()));

			} catch (Exception e) {
				System.out.println("XML Pasing Excpetion = " + e.getMessage());
			}

			return myXMLHandler.getType();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("string is " + result);
			System.out.println("string is " + result == ("null"));
			try {
				if (result.equals("")) {
					// filterList.get(poss).setIsOnline(AppConstants.ONLINE);
					userStatusImage.setBackgroundResource(R.drawable.online);
					userStatusText.setText("ONLINE");

				} else if (result.equals("unavailable")) {
					userStatusImage.setBackgroundResource(R.drawable.red_crcl);
					userStatusText.setText("OFFLINE");
				} else if (result.equals("error")) {
					userStatusImage.setBackgroundResource(R.drawable.red_crcl);
					userStatusText.setText("OFFLINE");
				}
			} catch (Exception e) {

			}
			// new GetOnline().execute(userDTO.getUserID());
			handler.postDelayed(runnable, 15000);
		}
	}

	class AddToFavAsynchTask extends AsyncTask<String, Void, JSONObject> {
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

		// user_id=2&friend_id=1
		protected JSONObject doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			System.out.println("USERID: " + userDTO.getUserID()); // used to get
																	// viewing
																	// profile
																	// user id
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("favorite_id", userDTO
					.getUserID()));
			JSONObject json = new JSONParser().makeHttpRequest2(args[0],
					"POST", params);
			Log.d("get Like response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject mJsonObject) {
			super.onPostExecute(mJsonObject);
			dialog.dismiss();

			try {
				// UserDTO userDTO = new UserDTO();
				// JSONObject mJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mJsonObject.getString("isFavorite");
				userDTO.setFavorite(FAVORITE_USER);
				// userDTOs.add(userDTO);
				// dudeDetailsInterestAdapter.notifyDataSetChanged();

				// System.out.print("" + jsonArray);
				// System.out.println("\nvStatus : "+vStatus);

				Toast.makeText(context, R.string.toast_added_to_favorites,
						Toast.LENGTH_LONG).show();

				// new
				// GetIsFavorite().execute("http://54.219.211.237/KraveAPI/api_calls/isFavorite.php",
				// Activity_Home.gblUserID);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	class RemoveFromFavAsynchTask extends AsyncTask<String, Void, JSONArray> {
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

		// user_id=2&friend_id=1
		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			System.out.println("USERID: " + userDTO.getUserID()); // used to get
																	// viewing
																	// profile
																	// user id
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("favorite_id", userDTO
					.getUserID()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get Like response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			Toast.makeText(context, R.string.toast_removed_from_favorites,
					Toast.LENGTH_LONG).show();

			userDTO.setFavorite(NOT_FAVORITE_USER);
			// new
			// GetIsFavorite().execute("http://54.219.211.237/KraveAPI/api_calls/isFavorite.php",
			// Activity_Home.gblUserID);
		}

	}

	public void showTemporaryAlert(String s) {
		new AlertDialog.Builder(context).setTitle(s).setCancelable(true)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	@Override
	public void onResume() {
		super.onResume();

		AppManager.initInstance();
		singleton = AppManager.getInstance();
		if (singleton.isFromFlagged == FLAG_BLOCK) {
			singleton.isFromFlagged = FLAG_NULL;
			Activity_Home.dudeCommonList.remove(Activity_Home.index); // removes
																		// user
																		// from
																		// array
																		// to
																		// stop
																		// scrolling
			/* Load next dude after rejecting him */
			galleryCount = 0;
			if (Activity_Home.index < Activity_Home.dudeCommonList.size() - 1) {
				Activity_Home.index = Activity_Home.index + 1;
				UserDTO ud = Activity_Home.dudeCommonList
						.get(Activity_Home.index);

				loadAPI(ud.getUserID());
				// enableStarFlag();
			} else {
				Toast.makeText(context, R.string.toast_reached_last_loaded_guy,
						Toast.LENGTH_SHORT).show();
				Activity_Home.index = 0;
				UserDTO ud = Activity_Home.dudeCommonList
						.get(Activity_Home.index);

				loadAPI(ud.getUserID());
			}
			System.out.println("Flag_Block");
		} else if (singleton.isFromFlagged == FLAG_CANCEL) {
			singleton.isFromFlagged = FLAG_NULL;
			System.out.println("Flag_Cancel");
		} else if (singleton.isFromFlagged == FLAG_NULL) {
			singleton.isFromFlagged = FLAG_NULL;
			System.out.println("Flag_Null");
		} else if (singleton.isFromFlagged == FLAG_REPORT) {
			singleton.isFromFlagged = FLAG_NULL;
			Activity_Home.dudeCommonList.remove(Activity_Home.index); // removes
																		// user
																		// from
																		// array
																		// to
																		// stop
																		// scrolling
			/* Load next dude after rejecting him */
			galleryCount = 0;
			if (Activity_Home.index < Activity_Home.dudeCommonList.size() - 1) {
				Activity_Home.index = Activity_Home.index + 1;
				UserDTO ud = Activity_Home.dudeCommonList
						.get(Activity_Home.index);

				loadAPI(ud.getUserID());
				// enableStarFlag();
			} else {
				Toast.makeText(context, R.string.toast_reached_last_loaded_guy,
						Toast.LENGTH_SHORT).show();
				Activity_Home.index = 0;
				UserDTO ud = Activity_Home.dudeCommonList
						.get(Activity_Home.index);

				loadAPI(ud.getUserID());
			}
			System.out.println("Flag_Report");
		}

	}

	/*
	 * Old But Gold (Send Like Request) this code sends dude a friend request if
	 * both you and the dude sent likes, you are a match
	 */
	/*
	 * now sends friend request and automatically lets other accept that request
	 * in order to chat
	 */
	private void doneButtonEvent() {
		// disableStarFlag();
		if (WebServiceConstants.isOnline(context)) {
			new SendLikeRequestAsynchTask()
					.execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.SEND_LIKE_REQUESTS);
			new SendLikeRequest2AsynchTask()
					.execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.SEND_LIKE_REQUESTS);
		}

	}

	class SendLikeRequestAsynchTask extends AsyncTask<String, Void, JSONArray> {
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

		// user_id=2&friend_id=1
		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			System.out.println("USERID: " + userDTO.getUserID()); // used to get
																	// viewing
																	// profile
																	// user id
			params.add(new BasicNameValuePair("friend_id", userDTO.getUserID()));
			// params.add(new BasicNameValuePair("user_email",
			// userDTO.getEmail()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get Like response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

		}

	}

	class SendLikeRequest2AsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new TransparentProgressDialog(context);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		// user_id=2&friend_id=1
		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			params.add(new BasicNameValuePair("friend_id", sessionManager
					.getUserDetail().getUserID()));
			System.out.println("USERID: " + userDTO.getUserID()); // used to get
																	// viewing
																	// profile
																	// user id
			params.add(new BasicNameValuePair("user_id", userDTO.getUserID()));
			// params.add(new BasicNameValuePair("user_email",
			// userDTO.getEmail()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get Like response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();

		}
	}

	class RetrieveActivityLog extends AsyncTask<String, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(String... args) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// edited for revision 507
			// MAY 19 2015
			// added if statement and try catch
			try {
				if (!(userDTO.getUserID() == null)) {
					params.add(new BasicNameValuePair("user_id", userDTO
							.getUserID()));
				}
				// - - - -
				JSONArray json = new JSONParser().makeHttpRequest(args[0],
						"POST", params);
				Log.d("get activity response", "" + json);
				return json;
			} catch (Exception e) {
				Log.d("connection problem", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			try {

				// EDITED FOR REVISION 608
				// MAY 20 2015
				// added if statement
				if (!(jsonArray.getJSONObject(0) == null)) {
					JSONObject jsonObject = jsonArray.getJSONObject(0);

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");

					/* UTC LAST ACT DATE */
					TimeZone utcZone = TimeZone.getTimeZone("UTC");
					simpleDateFormat.setTimeZone(utcZone);
					Date lastActDate = null;
					try {
						lastActDate = simpleDateFormat.parse(jsonObject.get(
								"date_created").toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					simpleDateFormat.setTimeZone(TimeZone.getDefault());
					String strLastActDate = simpleDateFormat
							.format(lastActDate); // last act date in string
					try {
						lastActDate = simpleDateFormat.parse(strLastActDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Date dateToday = getDateToday(simpleDateFormat);
					Log.d("", "dateToday=" + dateToday);
					Log.d("", "lastActDate=" + lastActDate);
					setLastActDays(dateToday, lastActDate);
					setLastActHours(dateToday, lastActDate);
					setLastActMins(dateToday, lastActDate);

					long timeDifference = (dateToday.getTime() - lastActDate
							.getTime()) / HOUR_MILLIS;
					String lastUserAct = getResources().getString(
							R.string.home_online_now);
					try {

						if (userDTO.getIsOnline().equalsIgnoreCase(
								AppConstants.ONLINE)) {
							lastUserAct = getResources().getString(
									R.string.home_online_now);
						} else if (timeDifference <= 72) {
							lastUserAct = getResources().getString(
									R.string.home_online_now);
						} else if (timeDifference <= (24 * 7)) {
							lastUserAct = getResources().getString(
									R.string.home_online_recently);
						} else {
							// lastUserAct = getResources().getString(
							// R.string.home_not_online_in_last_7_days);
							lastUserAct = "";
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					tvLastActivity.setText(lastUserAct);

				}

				// end of if statement
				// - - - -- - - - -
				// String lastUserAct = getResources().getString(
				// R.string.home_online);
				// ;
				// boolean isOnline = false;
				// try {
				// if (userDTO.getIsOnline().equalsIgnoreCase(
				// AppConstants.ONLINE)) {
				// isOnline = true;
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				// long last_active_hours = (long) ((getLastActDays() * 24) +
				// getLastActHours());
				// Log.d("", "last_active_hours=" + last_active_hours);
				// if (last_active_hours <= 72) {
				// lastUserAct = getResources().getString(
				// R.string.home_online_recently);
				// } else if (!isOnline) {
				// if (getLastActDays() >= 1)
				// lastUserAct = lastUserAct.concat(String.format("%d",
				// (long) getLastActDays())
				// + getResources().getString(R.string.home_days));
				// else if (getLastActHours() >= 1)
				// lastUserAct = lastUserAct
				// .concat(String.format("%d",
				// (long) getLastActHours())
				// + getResources().getString(
				// R.string.home_hours));
				// else if (getLastActMins() >= 1)
				// lastUserAct = lastUserAct.concat(String.format("%d",
				// (long) getLastActMins())
				// + getResources().getString(
				// R.string.home_minutes));
				// lastUserAct = lastUserAct.concat(getResources().getString(
				// R.string.home_ago));
				// }

				String lastUserAct = getResources().getString(
						R.string.home_online_now);

				// boolean isOnline = false;
				// try {
				// if (userDTO.getIsOnline().equalsIgnoreCase(
				// AppConstants.ONLINE)) {
				// isOnline = true;
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				// long last_active_hours = (long) ((getLastActDays() * 24) +
				// getLastActHours());
				// Log.d("", "last_active_hours=" + last_active_hours);
				// if (last_active_hours <= 72) {
				// lastUserAct = getResources().getString(
				// R.string.home_online_now);
				// } else if (last_active_hours <= (24 * 7)) {
				// lastUserAct = getResources().getString(
				// R.string.home_online_recently);
				// } else {
				// lastUserAct = getResources().getString(
				// R.string.home_not_online_in_last_7_days);
				// }
				//
				// tvLastActivity.setText(lastUserAct);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setLastOnline(String date) {

		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			/* UTC LAST ACT DATE */
			TimeZone utcZone = TimeZone.getTimeZone("UTC");
			simpleDateFormat.setTimeZone(utcZone);
			Date lastActDate = null;
			try {
				lastActDate = simpleDateFormat.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			simpleDateFormat.setTimeZone(TimeZone.getDefault());
			String strLastActDate = simpleDateFormat.format(lastActDate); // last
																			// act
																			// date
																			// in
																			// string

			Date dateToday = getDateToday(simpleDateFormat);

			setLastActDays(dateToday, lastActDate);
			setLastActHours(dateToday, lastActDate);
			setLastActMins(dateToday, lastActDate);

			// end of if statement
			// - - - -- - - - -
			String lastUserAct = getResources().getString(R.string.home_online);
			boolean isOnline = false;
			try {
				if (userDTO.getIsOnline().equalsIgnoreCase(AppConstants.ONLINE)) {
					isOnline = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			long last_active_hours = (long) ((getLastActDays() * 24));
			Log.d("", "last_active_hours=" + last_active_hours);
			if (isOnline) {
				lastUserAct = getResources().getString(R.string.home_online);
			} else if (last_active_hours <= 72) {
				lastUserAct = getResources().getString(
						R.string.home_online_recently);
			} else {
				if (getLastActDays() >= 1)
					lastUserAct = lastUserAct.concat(String.format("%d",
							(long) getLastActDays())
							+ getResources().getString(R.string.home_days));
				else if (getLastActHours() >= 1)
					lastUserAct = lastUserAct.concat(String.format("%d",
							(long) getLastActHours())
							+ getResources().getString(R.string.home_hours));
				else if (getLastActMins() >= 1)
					lastUserAct = lastUserAct.concat(String.format("%d",
							(long) getLastActMins())
							+ getResources().getString(R.string.home_minutes));
				lastUserAct = lastUserAct.concat(getResources().getString(
						R.string.home_ago));
			}

			tvLastActivity.setText(lastUserAct);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Date getDateToday(SimpleDateFormat simpleDateFormat) {
		Calendar c = Calendar.getInstance();
		Date dateToday = null;
		try {
			dateToday = simpleDateFormat.parse(simpleDateFormat.format(c
					.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateToday;
	}

	public void setLastActDays(Date dateToday, Date lastActDate) {
		float day = (int) ((dateToday.getTime() / DAY_MILLIS) - (lastActDate
				.getTime() / DAY_MILLIS));
		this.lastActDays = day;
	}

	public float getLastActDays() {
		return lastActDays;
	}

	public void setLastActHours(Date dateToday, Date lastActDate) {
		float hour = (int) ((dateToday.getTime() / HOUR_MILLIS) - (lastActDate
				.getTime() / HOUR_MILLIS));
		this.lastActHours = hour;
	}

	public float getLastActHours() {
		return lastActHours;
	}

	public void setLastActMins(Date dateToday, Date lastActDate) {
		float min = ((int) ((dateToday.getTime() / MINUTE_MILLIS) - (lastActDate
				.getTime() / MINUTE_MILLIS))) / 60;
		this.lastActMins = min;
	}

	public float getLastActMins() {
		return lastActMins;
	}

	public void openDailog(final String imageOwner, final String imageId) {
		try {
			final Dialog dialog = new Dialog(context,
					android.R.style.Theme_Translucent_NoTitleBar);

			dialog.setContentView(R.layout.dialog_send_photo_request);

			Button cancle = (Button) dialog.findViewById(R.id.no);
			Button ok = (Button) dialog.findViewById(R.id.yes);
			TextView title = (TextView) dialog.findViewById(R.id.titel);
			Typeface typeface = FontStyle.getFont(context,
					AppConstants.HelveticaNeueLTStd_Lt);
			title.setTypeface(typeface);
			cancle.setTypeface(typeface);
			title.setTypeface(typeface);
			ok.setTypeface(typeface);
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
					if (WebServiceConstants.isOnline(context)) {

						new SendPhotoRequest()
								.execute(
										WebServiceConstants.SEND_VIEW_PRIVATE_PHOTO_REQUEST,
										sessionManager.getUserDetail()
												.getUserID(), imageOwner,
										imageId);

					}
				}
			});
			dialog.show();
		} catch (Exception e) {

		}
	}

	// public void openDailogForAcceptPhotoRequest(final String imageOwner,
	// final String imageId) {
	// try {
	// final Dialog dialog = new Dialog(context,
	// android.R.style.Theme_Translucent_NoTitleBar);
	//
	// dialog.setContentView(R.layout.dialog_accept_photo_request);
	//
	// Button cancle = (Button) dialog.findViewById(R.id.no);
	// Button ok = (Button) dialog.findViewById(R.id.yes);
	// TextView title = (TextView) dialog.findViewById(R.id.titel);
	// Typeface typeface = FontStyle.getFont(context,
	// AppConstants.HelveticaNeueLTStd_Lt);
	// title.setTypeface(typeface);
	// cancle.setTypeface(typeface);
	// title.setTypeface(typeface);
	// ok.setTypeface(typeface);
	// cancle.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// }
	// });
	// ok.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// if (WebServiceConstants.isOnline(context)) {
	//
	// new SendPhotoRequest()
	// .execute(
	// WebServiceConstants.SEND_VIEW_PRIVATE_PHOTO_REQUEST,
	// sessionManager.getUserDetail()
	// .getUserID(), imageOwner,
	// imageId);
	//
	// }
	// }
	// });
	// dialog.show();
	// } catch (Exception e) {
	//
	// }
	// }

	// public void openDailog(final String dudeId, final String imageId) {
	// try {
	// final Dialog dialog = new Dialog(context,
	// android.R.style.Theme_Translucent_NoTitleBar);
	//
	// dialog.setContentView(R.layout.dialog_accept_photo_request);
	//
	// Button cancle = (Button) dialog.findViewById(R.id.no);
	// Button ok = (Button) dialog.findViewById(R.id.yes);
	// TextView title = (TextView) dialog.findViewById(R.id.titel);
	// TextView subTitle = (TextView) dialog.findViewById(R.id.sub_titel);
	// Typeface typeface = FontStyle.getFont(context,
	// AppConstants.HelveticaNeueLTStd_Lt);
	// title.setTypeface(typeface);
	// cancle.setTypeface(typeface);
	// title.setTypeface(typeface);
	// subTitle.setTypeface(typeface);
	//
	// String text =
	// "<font color='black'>jojo</font> is requesting to view your private photo.";
	// title.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
	// ok.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// }
	// });
	// ok.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// if (WebServiceConstants.isOnline(context)) {
	//
	// new SendPhotoRequest()
	// .execute(
	// WebServiceConstants.AV_BASE_URL
	// + WebServiceConstants.AV_INSERT_ACTIVITY_LOG,
	// sessionManager.getUserDetail()
	// .getUserID(), dudeId, imageId);
	//
	// }
	// }
	// });
	// dialog.show();
	// } catch (Exception e) {
	//
	// }
	// }

	/*
	 * Old But Gold (Flag Button Original Event) this code blocks the dude
	 * without warning then automatically proceeds to the next dude
	 */
	// private void flagButtonEvent() {
	// disableStarFlag();
	//
	// if (WebServiceConstants.isOnline(context)) {
	// // new BlockDudeAsynchTask().execute(WebServiceConstants.BASE_URL
	// // + WebServiceConstants.BLOCK_DUDE);
	// /* this uses kosa's API implementation for blocking */
	// new
	// BlockDudeAsynchTask().execute("http://54.219.211.237/KraveAPI/api_calls/block-user-account.php");
	// }
	// }
	//
	// class BlockDudeAsynchTask extends AsyncTask<String, Void, JSONArray> {
	// String vStatus;
	// TransparentProgressDialog dialog;
	//
	// //
	// http://parkhya.org/Android/krave_app/index.php?action=blockuser&user_id=8&friend_id=12
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog = new TransparentProgressDialog(context);
	// // dialog.setMessage("Please Wait...");
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	// }
	//
	// protected JSONArray doInBackground(String... args) {
	//
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// // params.add(new BasicNameValuePair("action", "login_user"));
	//
	// params.add(new BasicNameValuePair("user_id", sessionManager
	// .getUserDetail().getUserID()));
	// System.out.println("USERID: "+userDTO.getUserID()); //used to get viewing
	// profile user id
	//
	// /* if you want to use the old API for block, use these params */
	// // params.add(new BasicNameValuePair("friend_id", userDTO.getUserID()));
	// // params.add(new BasicNameValuePair("msg", "Not Interested"));
	//
	// params.add(new BasicNameValuePair("block_id", userDTO.getUserID()));
	// params.add(new BasicNameValuePair("reason", "Not Interested"));
	// System.out.println("params: "+params);
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	//
	// Log.d("get Like response", "" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	// dialog.dismiss();
	//
	// try {
	// JSONObject mJsonObject = jsonArray.getJSONObject(0);
	// Activity_Home.dudeCommonList.remove(Activity_Home.index); // removes user
	// from array to stop scrolling
	// vStatus = mJsonObject.getString("status");
	// System.out.print("" + jsonArray);
	// System.out.println("vstat  :"+vStatus);
	// // if (vStatus.equals("success")) {
	// Toast.makeText(context, "Dude successfully blocked",
	// Toast.LENGTH_LONG).show();
	// Activity_Block_Dude blocker = new Activity_Block_Dude();
	// // blocker.setBlock();
	// blocker.setResult(-1);
	// blocker.finish();
	// // setResult();
	// // finish();
	// // } else if (vStatus.equals("failure")) {
	// // Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
	// // }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// /* Load next dude after rejecting him */
	// galleryCount = 0; //reset gallery
	// if (Activity_Home.index < Activity_Home.dudeCommonList.size() - 1) {
	// // Activity_Home.index = Activity_Home.index + 1;
	// loadData(Activity_Home.index);
	// enableStarFlag();
	// }else{
	// Toast.makeText(context, "Reached last loaded guy",
	// Toast.LENGTH_SHORT).show();
	// Activity_Home.index = 0;
	// loadData(Activity_Home.index);
	// // context.Attach_Fragment(AppConstants.FRAGMENT_HOME); //go back home
	// }
	// }
	// }

	class GetAllChoices extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadCount++;
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
				--loadCount;
				if (loadCount == 0)
					singleton.stopLoading(llLoading);
			} catch (Exception e) {
			}
			try {
				JSONArray role = jsonObject.getJSONArray("role");
				roleDTOs = new ArrayList<RoleDTO>();
				bodyTypeDTOs = new ArrayList<BodyTypeDTO>();
				// JSONArray bodyType = jsonObject.getJSONArray("body_type");
				/* ROLE */

				for (int x = 0; x < role.length(); x++) {
					JSONObject r = role.getJSONObject(x);
					RoleDTO roleDTO = new RoleDTO();
					roleDTO.setRoleId(r.getString("id"));
					roleDTO.setRoleName(r.getString("name"));
					// roleDTO.setIsSelected(false);
					// if(userDTO.getWhatAreYouDTO().getRole().equals(r.getString("id"))){
					// roleDTO.setIsSelected(true);
					// }
					roleDTOs.add(roleDTO);
				}

				JSONArray body = jsonObject.getJSONArray("body_type");
				for (int x = 0; x < body.length(); x++) {
					JSONObject r = body.getJSONObject(x);
					BodyTypeDTO bodyDTO = new BodyTypeDTO();
					bodyDTO.setBodyTypeId(r.getString("id"));
					bodyDTO.setBodyTypeName(r.getString("name"));
					// bodyDTO.setIsSelected(false);
					// System.out.println(userDTO.getBodyTypeId()+" "+r.getString("id"));
					// if(userDTO.getBodyTypeId().equals(r.getString("id"))){
					// bodyDTO.setIsSelected(true);
					// }
					bodyTypeDTOs.add(bodyDTO);
				}

				System.out.println(userDTO.getBodyTypeId() + " ASDA "
						+ userDTO.getRoleId());
				try {
					String[] loveAndHook = userDTO.getLoveHookup().split(",");
					String loveHook = "";

					if (loveAndHook.length > 1) {
						loveHook = loveHook.concat(getResources().getString(
								R.string.home_love)
								+ " & "
								+ getResources().getString(R.string.home_fun));
					} else if (loveAndHook.length > 0) {
						if (loveAndHook[0].equals("1"))
							loveHook = loveHook.concat(getResources()
									.getString(R.string.home_love));
						else if (loveAndHook[0].equals("2"))
							loveHook = loveHook.concat(getResources()
									.getString(R.string.home_fun));
						else
							loveHook = loveHook.concat(getResources()
									.getString(R.string.home_undecided));
					}

					tvLoveHook.setText(loveHook);

					String bodyTypeName = "";
					int bodyTypeId = 0;
					try {
						bodyTypeId = Integer.valueOf(userDTO.getBodyTypeId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/* change for language conversion */

					// if (bodyTypeId == -1) {
					// bodyTypeName = "Undecided";
					// } else {
					// bodyTypeName = bodyTypeDTOs.get(bodyTypeId)
					// .getBodyTypeName();
					// }
					String bodyTypeArray[] = getResources().getStringArray(
							R.array.body_type_array);
					bodyTypeName = bodyTypeArray[bodyTypeId];

					String roleTypeName = "";
					int roleTypeId = 0;
					roleTypeId = Integer.valueOf(userDTO.getRoleId()) - 1;
					String roleArray[] = getResources().getStringArray(
							R.array.role_sample);

					if (roleTypeId == -1) {
						roleTypeName = getResources().getString(
								R.string.home_undecided);
					} else {
						roleTypeName = roleArray[roleTypeId];
					}

					bodyTypeName = bodyTypeArray[bodyTypeId];
					tvBody.setText(bodyTypeName);
					tvRole.setText(roleTypeName);
				} catch (Exception e) {

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
