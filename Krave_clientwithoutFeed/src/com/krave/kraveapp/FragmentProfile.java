package com.krave.kraveapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.askerov.dynamicgrid.DynamicGridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person.RelationshipStatus;
import com.image.crop.Gallery_Activity;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.ps.adapters.BodyTypeSpinnerAdapter;
import com.ps.adapters.DudeDetailsInterestAdapter;
import com.ps.adapters.EditProfileSpinnerAdapter;
import com.ps.adapters.RelationshipStatusSpinnerAdapter;
import com.ps.adapters.RoleSpinnerAdapter;
import com.ps.adapters.SettingListInterestAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.BodyTypeDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.RoleDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.FacebookIntegration;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoaderForProfile;
import com.ps.utill.InternalStorageContentProvider;
import com.ps.utill.JSONParser;
import com.ps.utill.LockableScrollView;
import com.ps.utill.OnfacebookDone;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentProfile extends Fragment implements OnClickListener,
		OnItemClickListener {

	LinearLayout mainLayout;

	Button galleryView, cameraView;
	LinearLayout cancleLayout, deleteLayout, imageLayout;
	RelativeLayout pictureLayout;
	private FacebookIntegration facebookIntegrationObject;

	GridView horizontalListView;
	int checkId = 0;

	SessionManager sessionManager;
	ImageLoaderForProfile imageLoader;
	Activity_Home context;
	UserDTO userDTO;
	WhatAreYouDTO whatAreYouDTO;
	public static List<FacebookLikesDTO> facebookLikesDTOs;
	private File file;
	ArrayList<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	private List<WhatAreYouDataDTO> whatAreYouDataDTOs1;
	private List<RoleDTO> roleDTOs;
	private List<BodyTypeDTO> bodyTypeDTOs;
	ArrayList<InterestsDTO> selectedInterest = new ArrayList<InterestsDTO>();

	SettingListInterestAdapter adapter;
	EditProfileSpinnerAdapter spinnerEthnicityAdapter;
	RelationshipStatusSpinnerAdapter relationshipStatusSpinnerAdapter;

	RoleSpinnerAdapter roleSpinnerAdapter;
	public static final String TAG = "MainActivity";

	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

	public static final int REQUEST_CODE_GALLERY = 100;
	public static final int REQUEST_CODE_TAKE_PICTURE = 102;
	public static final int REQUEST_CODE_CROP_IMAGE = 100;
	public static final int REQUEST_CODE_FOR_ADD_IMAGE_ACTVITY = 103;

	public File mFileTemp;
	int imagePosition;
	private String picturePath = "";
	DudeDetailsInterestAdapter dudeDetailsInterestAdapter;

	private ArrayList<UserProfileImageDTO> mPhotoResultSet = new ArrayList<UserProfileImageDTO>();
	private PhotosDynamicGridAdapter mAdapter;
	private ImageView mSelectedImageView;

	private ImageView loadingView;
	private LinearLayout llLoading;
	private AppManager singleton;

	private AlertDialog alertDialog = null;

	private TextView tvBodySlim, tvBodyMed, tvBodyAth, tvBodyLarge,
			tvProfileAge, tvProfileHeight, tvProfileWeight, tvLove, tvHookup;
	private ImageView ivBodySlim, ivBodyMed, ivBodyAth, ivBodyLarge;
	private RelativeLayout rlBodySlim, rlBodyMed, rlBodyAth, rlBodyLarge,
			rlLove, rlHookup, btnProfileSubmit;
	private LinearLayout btnProfileAge, btnProfileHeight, btnProfileWeight;
	private Spinner spnEthnicity, spnRole, spnRelationshipStatus;
	private EditText aboutMe, etUserName, etCountry;
	private int intentValue;

	private LockableScrollView scrollView;

	private int LOVE_TAG = 11, HOOKUP_TAG = 22, NO_TAG = 00;
	public int year = 1990;
	public int month = 1;
	public int day = 1;
	public String storeBirthday, storeWeight, storeWeightUnit, storeHeight,
			storeHeightUnit, storeBodyType;
	private ArrayList<String> storeLoveHookup = new ArrayList<String>();

	private Boolean isAlertShown = false, isRoleSelected = false,
			isEthSelected = false, isRelationshipSelected = false;

	private DynamicGridView photoGrid;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container,
				false);
		System.gc();
		context = (Activity_Home) getActivity();
		context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_UPDATE_IMAGE_PROFILE;

		Typeface typefaceHeader = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);

		context.headerIcon.setVisibility(View.GONE);
		context.title.setVisibility(View.VISIBLE);
		context.title.setText(R.string.titel_edit_profile);
		context.title.setTypeface(typefaceHeader);

		/* av singleton */
		AppManager.initInstance();
		singleton = AppManager.getInstance();

		sessionManager = new SessionManager(context);
		intentValue = context.getIntent().getExtras()
				.getInt(AppConstants.COME_FROM);
		imageLoader = new ImageLoaderForProfile(context);
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			mFileTemp = new File(context.getFilesDir(), TEMP_PHOTO_FILE_NAME);
		}
		setLayout(view);
		setListener();
		setTypeFace(view);
		if (WebServiceConstants.isOnline(context)) {
			new GetAllDataAsynchTask().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_ALL_INTEREST_AND_WHAT_ARE_YOU);
			new GetAllChoices()
					.execute("http://54.219.211.237/KraveAPI/api_calls/returnAllChoices.php");
		}
		setData();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		System.gc();

		context.right_button.setVisibility(View.VISIBLE);
		context.headerIcon.setVisibility(View.VISIBLE);
		context.title.setVisibility(View.GONE);
	}

	public void setData() {
		userDTO = sessionManager.getUserDetail();

		mPhotoResultSet.clear();
		mPhotoResultSet.add(new UserProfileImageDTO());
		mPhotoResultSet.addAll(userDTO.getUserProfileImageDTOs());

		mAdapter = new PhotosDynamicGridAdapter(this.getActivity(),
				mPhotoResultSet, 4);
		photoGrid.setAdapter(mAdapter);

		photoGrid.setOnDragListener(new DynamicGridView.OnDragListener() {
			@Override
			public void onDragStarted(int position) {

			}

			@Override
			public void onDragPositionsChanged(int oldPosition, int newPosition) {
				// check zero for add photo cell
				if (oldPosition != 0 && newPosition != 0)
					Collections.swap(mPhotoResultSet, oldPosition, newPosition);
			}
		});

		photoGrid
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						scrollView.setScrollingEnabled(false);
						photoGrid.startEditMode();
						return true;
					}
				});

		photoGrid.setOnDropListener(new DynamicGridView.OnDropListener() {
			@Override
			public void onActionDrop() {
				photoGrid.stopEditMode();
				scrollView.setScrollingEnabled(true);
			}
		});

		photoGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSelectedImageView = (ImageView) view
						.findViewById(R.id.row_grid_photo);

				photoClick(position);

			}
		});
		if (userDTO.getAboutMe().length() == 0) {
			aboutMe.setText(R.string.profile_about_me_default_string);
		} else {
			aboutMe.setText(userDTO.getAboutMe());
		}
		// aboutMe.setText(userDTO.getAboutMe());
		etUserName.setText(userDTO.getFirstName());
		etCountry.setText(GpsService.country);
		etCountry.setEnabled(false);

		/* Set User age and birthday */
		String birthDate = userDTO.getWhatAreYouDTO().getAge();
		try {
			tvProfileAge.setText(""
					+ singleton.calculateAge(userDTO.getWhatAreYouDTO()
							.getAge()));
		} catch (Exception exc) {

		}

		if (birthDate.length() != 0) {
			year = Integer.valueOf(birthDate.split("/")[2]);
			month = Integer.valueOf(birthDate.split("/")[1]) - 1;
			day = Integer.valueOf(birthDate.split("/")[0]);
		}

		/* Set User height and weight */
		String h = userDTO.getWhatAreYouDTO().getHight();
		String w = userDTO.getWhatAreYouDTO().getWeight();
		if (userDTO.getWhatAreYouDTO().getHeightUnit()
				.equals(AppConstants.HEIGHT_IN_FEET)) {
			try {
				tvProfileHeight.setText(h.split("/")[0] + " '"
						+ h.split("/")[1] + " \"");
			} catch (Exception e) {
				int hight = Integer.valueOf(h);

				h = "" + (hight / 12) + "/" + (hight % 12);
				userDTO.getWhatAreYouDTO().setHight(h);
				tvProfileHeight.setText(h.split("/")[0] + " '"
						+ h.split("/")[1] + " \"");
				// tvProfileHeight.setText("n/a");
			}
		} else {
			try {
				tvProfileHeight.setText(h.split("/")[0] + " m,"
						+ h.split("/")[1] + " cm");
			} catch (Exception e) {
				if (!h.contains("null") && !h.equals("")) {
					int hight = Integer.valueOf(h);
					h = "" + (hight / 100) + "/" + (hight % 100);
					userDTO.getWhatAreYouDTO().setHight(h);

					tvProfileHeight.setText(h.split("/")[0] + " m,"
							+ h.split("/")[1] + " cm");
					// tvProfileHeight.setText("n/a");
				}
			}
		}

		if (userDTO.getWhatAreYouDTO().getWeightUnit()
				.equals(AppConstants.WEIGHT_IN_KILOGRAM)) {
			tvProfileWeight.setText(w.split("/")[0] + " kg");
		} else {
			try {
				tvProfileWeight.setText(w.split("/")[0] + " lb");
			} catch (Exception e) {
				tvProfileHeight.setText("n/a");
			}
		}

		whatAreYouDTO = userDTO.getWhatAreYouDTO();

		String[] loveHookup = userDTO.getLoveHookup().split(",");
		System.out.println("dto " + userDTO.getLoveHookup());
		storeLoveHookup.clear();
		if (loveHookup.length > 1) {
			rlHookup.setBackgroundColor(Color.parseColor("#51ccd8"));
			tvHookup.setTextColor(Color.parseColor("#000000"));
			rlHookup.setTag(HOOKUP_TAG);

			rlLove.setBackgroundColor(Color.parseColor("#f14546"));
			tvLove.setTextColor(Color.parseColor("#000000"));
			rlLove.setTag(LOVE_TAG);

			storeLoveHookup.add(loveHookup[0]);
			storeLoveHookup.add(loveHookup[1]);
		} else {
			if (loveHookup[0].equals("1")) {
				rlLove.setBackgroundColor(Color.parseColor("#f14546"));
				tvLove.setTextColor(Color.parseColor("#000000"));
				rlLove.setTag(LOVE_TAG);

				rlHookup.setBackgroundColor(Color.parseColor("#000000"));
				tvHookup.setTextColor(Color.parseColor("#404041"));
				rlHookup.setTag(NO_TAG);
				storeLoveHookup.add(loveHookup[0]);
			} else if (loveHookup[0].equals("2")) {
				rlHookup.setBackgroundColor(Color.parseColor("#51ccd8"));
				tvHookup.setTextColor(Color.parseColor("#000000"));
				rlHookup.setTag(HOOKUP_TAG);

				rlLove.setBackgroundColor(Color.parseColor("#000000"));
				tvLove.setTextColor(Color.parseColor("#404041"));
				rlLove.setTag(NO_TAG);
				storeLoveHookup.add(loveHookup[0]);
			} else {

			}
		}

		// JCv - relationship status
		int relationshipStatus = 3;
		try {
			relationshipStatus = Integer.parseInt(userDTO.getWhatAreYouDTO()
					.getRelationshipStatus());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		relationshipStatusSpinnerAdapter = new RelationshipStatusSpinnerAdapter(
				context, context.getResources().getStringArray(
						R.array.relationship_status_array));
		spnRelationshipStatus.setAdapter(relationshipStatusSpinnerAdapter);
		spnRelationshipStatus.setSelection(relationshipStatus - 1);
	}

	public void startFacebookIntegration() {
		facebookIntegrationObject = new FacebookIntegration(context,
				AppConstants.COME_FROM_INTEREST, new OnfacebookDone() {

					@Override
					public void onFbcompleate() {

					}
				});
		facebookIntegrationObject.facebookIntegration();
	}

	private void photoClick(int position) {
		Intent intent = new Intent(context, Gallery_Activity.class);

		imagePosition = position;

		// Clicked plus button
		if (position == 0) {
			if (singleton.isPaidUser || mPhotoResultSet.size() < 7) { // for
				// paid
				// user
				// or
				// not
				// condition

				intent.putExtra("delete", false);
				intent.putExtra("profile_image", false);
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_EDIT_PROFILE,
						AppConstants.EVENT_LOG_ACTION_BTN, "AddPhoto");
			} else {
				context.subscribeToPaidAccount();
				return;
			}
		}
		// Clicked other photo
		else {

			intent.putExtra("delete", true);
			if (mPhotoResultSet.get(position).isPublic()) {
				intent.putExtra("profile_image", true);
			} else {
				intent.putExtra("profile_image", false);
			}

			deleteLayout.setVisibility(View.VISIBLE);
			context.easyTrackerEventLog(
					AppConstants.EVENT_LOG_CATEG_EDIT_PROFILE,
					AppConstants.EVENT_LOG_ACTION_BTN, "EditPhoto");
		}

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	private void setListener() {
		mainLayout.setOnClickListener(this);
		cancleLayout.setOnClickListener(this);
		galleryView.setOnClickListener(this);
		cameraView.setOnClickListener(this);
		deleteLayout.setOnClickListener(this);
		horizontalListView.setOnItemClickListener(this);

		rlBodySlim.setOnClickListener(this);
		rlBodyMed.setOnClickListener(this);
		rlBodyAth.setOnClickListener(this);
		rlBodyLarge.setOnClickListener(this);

		rlLove.setOnClickListener(this);
		rlHookup.setOnClickListener(this);

		btnProfileAge.setOnClickListener(this);
		btnProfileHeight.setOnClickListener(this);
		btnProfileWeight.setOnClickListener(this);

		btnProfileSubmit.setOnClickListener(this);

	}

	private void setLayout(View viewLayout) {
		mainLayout = (LinearLayout) viewLayout.findViewById(R.id.mainView);

		// JCv
		photoGrid = (DynamicGridView) viewLayout
				.findViewById(R.id.profileDynamicGrid);
		photoGrid.setExpanded(true);

		galleryView = (Button) viewLayout.findViewById(R.id.gallery);
		cameraView = (Button) viewLayout.findViewById(R.id.camera);
		cancleLayout = (LinearLayout) viewLayout
				.findViewById(R.id.cancleLayout);
		deleteLayout = (LinearLayout) viewLayout
				.findViewById(R.id.deleteLayout);
		pictureLayout = (RelativeLayout) viewLayout
				.findViewById(R.id.imageLayout);

		/* Description */
		aboutMe = (EditText) viewLayout.findViewById(R.id.aboutMe);
		aboutMe.setActivated(false);
		aboutMe.setOnClickListener(this);

		/* Interest Grid View */
		horizontalListView = (GridView) viewLayout
				.findViewById(R.id.horizontalListView1);

		/* Loading Screen */
		loadingView = (ImageView) viewLayout.findViewById(R.id.loadingView);
		llLoading = (LinearLayout) viewLayout.findViewById(R.id.llLoading);

		/* Body Type */
		tvBodySlim = (TextView) viewLayout.findViewById(R.id.tvBodySlim);
		tvBodyMed = (TextView) viewLayout.findViewById(R.id.tvBodyMed);
		tvBodyAth = (TextView) viewLayout.findViewById(R.id.tvBodyAth);
		tvBodyLarge = (TextView) viewLayout.findViewById(R.id.tvBodyLarge);

		ivBodySlim = (ImageView) viewLayout.findViewById(R.id.ivBodySlim);
		ivBodyMed = (ImageView) viewLayout.findViewById(R.id.ivBodyMed);
		ivBodyAth = (ImageView) viewLayout.findViewById(R.id.ivBodyAth);
		ivBodyLarge = (ImageView) viewLayout.findViewById(R.id.ivBodyLarge);

		ivBodySlim.setTag(R.drawable.av_bodytype_slim_up);
		ivBodyMed.setTag(R.drawable.av_bodytype_medium_up);
		ivBodyAth.setTag(R.drawable.av_bodytype_athletic_up);
		ivBodyLarge.setTag(R.drawable.av_bodytype_large_up);

		rlBodySlim = (RelativeLayout) viewLayout.findViewById(R.id.rlBodySlim);
		rlBodyMed = (RelativeLayout) viewLayout.findViewById(R.id.rlBodyMed);
		rlBodyAth = (RelativeLayout) viewLayout.findViewById(R.id.rlBodyAth);
		rlBodyLarge = (RelativeLayout) viewLayout
				.findViewById(R.id.rlBodyLarge);

		/* Looking for */
		rlLove = (RelativeLayout) viewLayout.findViewById(R.id.rlLove);
		rlHookup = (RelativeLayout) viewLayout.findViewById(R.id.rlHookup);
		rlLove.setTag(NO_TAG); // temp
		rlHookup.setTag(NO_TAG);

		tvLove = (TextView) viewLayout.findViewById(R.id.tvLove);
		tvHookup = (TextView) viewLayout.findViewById(R.id.tvHookup);

		rlLove.setBackgroundColor(Color.parseColor("#000000"));
		tvLove.setTextColor(Color.parseColor("#404041"));
		rlHookup.setBackgroundColor(Color.parseColor("#000000"));
		tvHookup.setTextColor(Color.parseColor("#404041"));

		/* Personal Information */
		etUserName = (EditText) viewLayout.findViewById(R.id.etUserName);
		etCountry = (EditText) viewLayout.findViewById(R.id.etCountry);
		btnProfileAge = (LinearLayout) viewLayout
				.findViewById(R.id.btnProfileAge);
		btnProfileHeight = (LinearLayout) viewLayout
				.findViewById(R.id.btnProfileHeight);
		btnProfileWeight = (LinearLayout) viewLayout
				.findViewById(R.id.btnProfileWeight);
		tvProfileAge = (TextView) viewLayout.findViewById(R.id.tvProfileAge);
		tvProfileHeight = (TextView) viewLayout
				.findViewById(R.id.tvProfileHeight);
		tvProfileWeight = (TextView) viewLayout
				.findViewById(R.id.tvProfileWeight);

		spnEthnicity = (Spinner) viewLayout.findViewById(R.id.spnEthnicity);
		spnRole = (Spinner) viewLayout.findViewById(R.id.spnRole);
		spnRelationshipStatus = (Spinner) viewLayout
				.findViewById(R.id.spnRelationshipStatus);

		btnProfileSubmit = (RelativeLayout) viewLayout
				.findViewById(R.id.btnProfileSubmit);

		scrollView = (LockableScrollView) viewLayout
				.findViewById(R.id.scrollView1);
	}

	private void setTypeFace(View view) {
		TextView cancleTextView, addPictureTextView, deleteTextView;
		cancleTextView = (TextView) view.findViewById(R.id.cancleTextView);
		deleteTextView = (TextView) view.findViewById(R.id.deleteTextView);
		addPictureTextView = (TextView) view
				.findViewById(R.id.addPictureTextView);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

		aboutMe.setTypeface(typeface);
		cancleTextView.setTypeface(typeface);
		deleteTextView.setTypeface(typeface);
		addPictureTextView.setTypeface(typeface);

		tvBodySlim.setTypeface(typeface);
		tvBodyMed.setTypeface(typeface);
		tvBodyAth.setTypeface(typeface);
		tvBodyLarge.setTypeface(typeface);

		tvLove.setTypeface(typeface);
		tvHookup.setTypeface(typeface);

		Typeface typeface1 = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

	}

	public void bodyType(int ivRes, ImageView iv, TextView tv, int bodyId) {
		ivBodySlim.setImageResource(R.drawable.av_bodytype_slim_up);
		ivBodyMed.setImageResource(R.drawable.av_bodytype_medium_up);
		ivBodyAth.setImageResource(R.drawable.av_bodytype_athletic_up);
		ivBodyLarge.setImageResource(R.drawable.av_bodytype_large_up);

		ivBodySlim.setTag(R.drawable.av_bodytype_slim_up);
		ivBodyMed.setTag(R.drawable.av_bodytype_medium_up);
		ivBodyAth.setTag(R.drawable.av_bodytype_athletic_up);
		ivBodyLarge.setTag(R.drawable.av_bodytype_large_up);

		tvBodySlim.setTextColor(Color.parseColor("#e2e2e2"));
		tvBodyMed.setTextColor(Color.parseColor("#e2e2e2"));
		tvBodyAth.setTextColor(Color.parseColor("#e2e2e2"));
		tvBodyLarge.setTextColor(Color.parseColor("#e2e2e2"));

		iv.setImageResource(ivRes);
		iv.setTag(ivRes);
		tv.setTextColor(Color.parseColor("#404041"));

		storeBodyType = "" + bodyId;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnProfileSubmit:
			userDTO.setFirstName(etUserName.getText().toString());
			userDTO.setBodyType(storeBodyType);

			if (storeLoveHookup.size() > 1) {
				userDTO.setLoveHookup(storeLoveHookup.get(0) + ","
						+ storeLoveHookup.get(1));
			} else {
				try {
					userDTO.setLoveHookup(storeLoveHookup.get(0));
				} catch (Exception e) {
					userDTO.setLoveHookup("");
				}
			}

			userDTO.setWhatAreYouDTO(whatAreYouDTO);
			new UpdateProfileAsynchTask()
					.execute(WebServiceConstants.BASE_URL_FILE
							+ WebServiceConstants.USER_PROFILE_UPDATE);
			new AvUpdateUserAsynch()
					.execute(WebServiceConstants.AV_UPDATE_PROFILE_DATA);

			break;
		case R.id.rlBodySlim:
			bodyType(R.drawable.av_bodytype_slim_down, ivBodySlim, tvBodySlim,
					1);
			if (!isAlertShown)
				showSaveAlert();
			break;
		case R.id.rlBodyMed:
			bodyType(R.drawable.av_bodytype_medium_down, ivBodyMed, tvBodyMed,
					2);
			if (!isAlertShown)
				showSaveAlert();
			break;
		case R.id.rlBodyAth:
			bodyType(R.drawable.av_bodytype_athletic_down, ivBodyAth,
					tvBodyAth, 3);
			if (!isAlertShown)
				showSaveAlert();
			break;
		case R.id.rlBodyLarge:
			bodyType(R.drawable.av_bodytype_large_down, ivBodyLarge,
					tvBodyLarge, 4);
			if (!isAlertShown)
				showSaveAlert();
			break;
		case R.id.rlLove:
			if (!isAlertShown)
				showSaveAlert();

			if ((Integer) rlLove.getTag() == NO_TAG) {
				storeLoveHookup.add("1");
				rlLove.setBackgroundColor(Color.parseColor("#f14546"));
				tvLove.setTextColor(Color.parseColor("#000000"));
				rlLove.setTag(LOVE_TAG);
			} else {
				if (storeLoveHookup.size() == 1) {
					Toast.makeText(context,
							R.string.toast_atleast_one_option_must_be_selected,
							Toast.LENGTH_LONG).show();
					return;
				}
				storeLoveHookup.clear();
				if ((Integer) rlHookup.getTag() == HOOKUP_TAG) {
					storeLoveHookup.add("2");
				}

				rlLove.setBackgroundColor(Color.parseColor("#000000"));
				tvLove.setTextColor(Color.parseColor("#404041"));
				rlLove.setTag(NO_TAG);
			}
			System.out.println("LOVE : " + storeLoveHookup.toString());
			break;
		case R.id.rlHookup:
			if (!isAlertShown)
				showSaveAlert();

			if ((Integer) rlHookup.getTag() == NO_TAG) {
				storeLoveHookup.add("2");
				rlHookup.setBackgroundColor(Color.parseColor("#51ccd8"));
				tvHookup.setTextColor(Color.parseColor("#000000"));
				rlHookup.setTag(HOOKUP_TAG);
			} else {
				if (storeLoveHookup.size() == 1) {
					Toast.makeText(context,
							R.string.toast_atleast_one_option_must_be_selected,
							Toast.LENGTH_LONG).show();
					return;
				}
				storeLoveHookup.clear();
				if ((Integer) rlLove.getTag() == LOVE_TAG) {
					storeLoveHookup.add("1");
				}

				rlHookup.setBackgroundColor(Color.parseColor("#000000"));
				tvHookup.setTextColor(Color.parseColor("#404041"));
				rlHookup.setTag(NO_TAG);
			}
			System.out.println("HOOK : " + storeLoveHookup.toString());
			break;

		case R.id.btnProfileAge:
			System.out.println("HOY");
			try {
				context.showDialog(1);
			} catch (Exception exp) {

			}
			break;
		case R.id.btnProfileHeight:
			openDialogForHight();
			break;
		case R.id.btnProfileWeight:
			openDialogForWeight();
			break;
		case R.id.mainView:

			break;
		case R.id.imageLayout:

			break;
		case R.id.cancleLayout:
			pictureLayout.setVisibility(View.GONE);
			break;
		case R.id.deleteLayout:
			deleteImage();
			break;
		case R.id.gallery:
			openGallery();
			pictureLayout.setVisibility(View.GONE);
			break;
		case R.id.camera:
			takePicture();

			pictureLayout.setVisibility(View.GONE);
			break;
		case R.id.aboutMe:
			Intent intent = new Intent(context, Activity_AboutMe.class);
			intent.putExtra("about", (aboutMe.getText().toString()
					.equals(getResources().getString(
							R.string.profile_about_me_default_string))) ? ""
					: (aboutMe.getText().toString()));
			startActivityForResult(intent, 1);
			context.easyTrackerEventLog(
					AppConstants.EVENT_LOG_CATEG_EDIT_PROFILE,
					AppConstants.EVENT_LOG_ACTION_TB, "AboutMe");
			break;
		default:
			break;
		}

	}

	private void deletePhoto(int position) {
		mAdapter.deletePhoto((mPhotoResultSet.remove(position).getImageId()));
		mAdapter = new PhotosDynamicGridAdapter(getActivity(), mPhotoResultSet,
				4);
		photoGrid.setAdapter(mAdapter);
		showSaveAlert();
	}

	private void makeProfileImage(int position) {
		if (position != 0)
			Collections.swap(mPhotoResultSet, position, 1);
		mAdapter.notifyDataSetChanged();
		showSaveAlert();
		// mAdapter.deletePhoto((mPhotoResultSet.remove(position).getImageId()));
		// mAdapter = new PhotosDynamicGridAdapter(getActivity(),
		// mPhotoResultSet,
		// 4);
		// photoGrid.setAdapter(mAdapter);
	}

	private void deleteImage() {

		deletePhoto(imagePosition);
	}

	private void takePicture() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(mFileTemp);
			} else {
				/*
				 * The solution is taken from here:
				 * http://stackoverflow.com/questions
				 * /10042695/how-to-get-camera-result-as-a-uri-in-data-folder
				 */
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			context.startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			Log.d("", "cannot take picture", e);
		}
	}

	private void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		context.startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	public void addPhoto(String photoPath, int position, boolean isPublic) {
		UserProfileImageDTO user = new UserProfileImageDTO();

		// if photo edit only
		if (position != 0) {
			try {
				user = mPhotoResultSet.get(position);
			} catch (Exception e) {
				e.printStackTrace();
			}
			user.setImagePath(photoPath);
			mAdapter.deletePhoto(user.getImageId());
			user.setImageId(null);
			user.setPublic(isPublic);
			try {
				mPhotoResultSet.set(position, user);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// New photo
		} else {
			user.setImagePath(photoPath);
			user.setPublic(isPublic);
			mPhotoResultSet.add(user);
		}

		mAdapter.addPhoto(photoPath, isPublic);

		mAdapter = new PhotosDynamicGridAdapter(this.getActivity(),
				mPhotoResultSet, 4);
		photoGrid.setAdapter(mAdapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CODE_CROP_IMAGE
				&& resultCode == context.RESULT_OK) {
			if (data.getExtras().getBoolean("delete")) {
				deleteImage();
			} else if (data.getExtras().getBoolean("profile_image")) {
				makeProfileImage(imagePosition);
			} else {
				picturePath = data.getStringExtra("path");
				Intent intent = new Intent(context, Activity_Add_Image.class);
				intent.putExtra("path", picturePath);
				intent.putExtra("subscribeOrNot", addPrivateImageOrNot());
				startActivityForResult(intent,
						REQUEST_CODE_FOR_ADD_IMAGE_ACTVITY);
				// if (imagePosition == 0) {
				//
				// addPhoto(picturePath, 0);
				// } else {
				// addPhoto(picturePath, imagePosition);
				// }
				//
				// Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
				// mSelectedImageView.setImageBitmap(bitmap);
				//
				// LoadBitmap(bitmap);
			}
		} else if (requestCode == REQUEST_CODE_FOR_ADD_IMAGE_ACTVITY
				&& resultCode == context.RESULT_OK) {
			boolean isPublic = data.getExtras().getBoolean("isPublic");
			if (imagePosition == 0) {

				addPhoto(picturePath, 0, isPublic);
			} else {
				addPhoto(picturePath, imagePosition, isPublic);
			}

			Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
			mSelectedImageView.setImageBitmap(bitmap);

			LoadBitmap(bitmap);

		} else if (requestCode == 1 && resultCode == context.RESULT_OK) {
			String about = data.getExtras().getString("about");
			aboutMe.setText(about.length() == 0 ? getResources().getString(
					R.string.profile_about_me_default_string) : about);

		} else {
			try {

				facebookIntegrationObject.onActivityResultForFacebook(
						requestCode, resultCode, data);
			} catch (Exception e) {

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean addPrivateImageOrNot() {
		if (!singleton.isPaidUser) {
			int privateImageCount = 0;
			for (UserProfileImageDTO dto : mPhotoResultSet) {
				if (!dto.isPublic()) {
					privateImageCount++;
				}

			}
			if (privateImageCount >= 3) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public void showSaveAlert() {
		new AlertDialog.Builder(context)
				.setTitle(R.string.dialog_notice)
				.setMessage(
						R.string.dialog_don_t_forget_to_press_the_submit_button_at_the_bottom_of_the_screen_to_save_your_changes)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								isAlertShown = true;
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	// private void startCropImage() {
	//
	// Intent intent = new Intent(context, CropImage.class);
	// intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
	// intent.putExtra(CropImage.SCALE, true);
	//
	// intent.putExtra(CropImage.ASPECT_X, 3);
	// intent.putExtra(CropImage.ASPECT_Y, 3);
	//
	// startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	// }

	private void LoadBitmap(Bitmap bitmap) {
		Calendar c = Calendar.getInstance();
		String date = fromInt(c.get(Calendar.MONTH))
				+ fromInt(c.get(Calendar.DAY_OF_MONTH))
				+ fromInt(c.get(Calendar.YEAR))
				+ fromInt(c.get(Calendar.HOUR_OF_DAY))
				+ fromInt(c.get(Calendar.MINUTE))
				+ fromInt(c.get(Calendar.SECOND));

		String strMyImagePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/test" + date + ".png";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strMyImagePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);

			fos.flush();
			fos.close();
			picturePath = strMyImagePath;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public String fromInt(int val) {
		return String.valueOf(val);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long arg3) {
		InterestsDTO interestsDTO = interestsDTOs.get(position);

		if (interestsDTO.getIsSelected()) {
			if (selectedInterest.size() > 1) {
				selectedInterest.remove(interestsDTO);
				interestsDTO.setIsSelected(false);
			} else {
				Toast.makeText(context,
						R.string.toast_at_least_one_interest_must_be_selected,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			interestsDTO.setIsSelected(true);
			selectedInterest.add(interestsDTO);

		}
		adapter.notifyDataSetChanged();
		// gridViewAdapter = new GridViewAdapter(context,
		// (ArrayList<InterestsDTO>) interestsDTOs, 1);
		// horizontalListView.setAdapter(gridViewAdapter);
	}

	// public void checkValidation(int id) {
	//
	// String validation = "Please add";
	// checkId = id;
	// // int check = 0;
	// // for (int i = 0; i < picturesPathArray.length; i++) {
	// // if (!picturesPathArray[i].equals("a")) {
	// // check++;
	// // }
	// // }
	// if (aboutMe.getText().toString().trim().length() == 0) {
	// validation = validation + " About me,";
	// }
	// // if (check < 1) {
	// // validation = validation + " Images,";
	// // }
	// if (picturesPathArray[0].equals("a")) {
	// validation = validation + " Profile picture,";
	// }
	// if (selectedInterest.size() == 0) {
	// validation = validation + " Interest,";
	// }
	// if (validation.equals("Please add")) {
	// if (WebServiceConstants.isOnlineWitoutToast(context)) {
	// for(int x = 0;x<selectedInterest.size();x++){
	// context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_EDIT_PROFILE,
	// AppConstants.EVENT_LOG_ACTION_INTEREST,
	// selectedInterest.get(x).getInterestName());
	// }
	//
	// new UpdateProfileAsynchTask()
	// .execute(WebServiceConstants.BASE_URL_FILE
	// + WebServiceConstants.USER_PROFILE_UPDATE);
	// new AvUpdateUserAsynch()
	// .execute(WebServiceConstants.AV_UPDATE_PROFILE_DATA);
	// }
	//
	// } else {
	// // Toast.makeText(context, validation, Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// }
	class AvUpdateUserAsynch extends AsyncTask<String, Void, JSONArray> {
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

		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", ""
					+ userDTO.getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();

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

			int spnRoleSelected = 0, spnBodySelected = 0;
			try {
				JSONArray role = jsonObject.getJSONArray("role");
				// JSONArray bodyType = jsonObject.getJSONArray("body_type");
				/* ROLE */
				userDTO = sessionManager.getUserDetail();
				for (int x = 0; x < role.length(); x++) {
					JSONObject r = role.getJSONObject(x);
					RoleDTO roleDTO = new RoleDTO();
					roleDTO.setRoleId(r.getString("id"));
					roleDTO.setRoleName(r.getString("name"));
					roleDTO.setIsSelected(false);
					if (userDTO.getWhatAreYouDTO().getRole()
							.equals(r.getString("id"))) {
						roleDTO.setIsSelected(true);
						spnRoleSelected = x;
					}
					roleDTOs.add(roleDTO);
				}

				JSONArray body = jsonObject.getJSONArray("body_type");
				for (int x = 0; x < body.length(); x++) {
					JSONObject r = body.getJSONObject(x);
					BodyTypeDTO bodyDTO = new BodyTypeDTO();
					bodyDTO.setBodyTypeId(r.getString("id"));
					bodyDTO.setBodyTypeName(r.getString("name"));
					bodyDTO.setIsSelected(false);
					System.out.println(userDTO.getBodyTypeId() + " "
							+ r.getString("id"));
					if (userDTO.getBodyTypeId().equals(r.getString("id"))) {
						bodyDTO.setIsSelected(true);
						// spnBodySelected = x;
					}
					bodyTypeDTOs.add(bodyDTO);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			if (bodyTypeDTOs.get(0).getIsSelected() == true) {
				bodyType(R.drawable.av_bodytype_slim_down, ivBodySlim,
						tvBodySlim, 1);
			} else if (bodyTypeDTOs.get(1).getIsSelected() == true) {
				bodyType(R.drawable.av_bodytype_medium_down, ivBodyMed,
						tvBodyMed, 2);
			} else if (bodyTypeDTOs.get(2).getIsSelected() == true) {
				bodyType(R.drawable.av_bodytype_athletic_down, ivBodyAth,
						tvBodyAth, 3);
			} else if (bodyTypeDTOs.get(3).getIsSelected() == true) {
				bodyType(R.drawable.av_bodytype_large_down, ivBodyLarge,
						tvBodyLarge, 4);
			}

			roleSpinnerAdapter = new RoleSpinnerAdapter(context, roleDTOs, 0);
			spnRole.setAdapter(roleSpinnerAdapter);
			spnRole.setSelection(spnRoleSelected);
			spnRole.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					whatAreYouDTO.setRole(roleDTOs.get(position).getRoleId());
					System.out.println(whatAreYouDTO.getRole());

					if (!isAlertShown && isRoleSelected)
						showSaveAlert();

					isRoleSelected = true;
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			// bodySpinnerAdapter = new RoleSpinnerAdapter(context,
			// bodyTypeDTOs, 0);
			// spnRole.setAdapter(roleSpinnerAdapter);
			// spnRole.setSelection(spnRoleSelected);
		}

	}

	class GetAllDataAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// singleton.progressLoading(loadingView, llLoading);
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("", ""));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("login response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			// singleton.stopLoading(llLoading);

			int spnSelected = 0;
			try {
				WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
				interestsDTOs = new ArrayList<InterestsDTO>();
				whatAreYouDataDTOs1 = new ArrayList<WhatAreYouDataDTO>();
				roleDTOs = new ArrayList<RoleDTO>();
				bodyTypeDTOs = new ArrayList<BodyTypeDTO>();

				System.out.print("" + jsonArray);

				JSONObject mJsonObject1 = jsonArray.getJSONObject(0);

				JSONObject mJsonObject2 = jsonArray.getJSONObject(1);
				System.out.print("mJsonObject1" + mJsonObject1);
				System.out.print("mJsonObject1" + mJsonObject1);

				// vStatus = mJsonObject.getString("status");
				JSONArray jsonArray1 = mJsonObject1.getJSONArray("Intrast");
				JSONArray jsonArray2 = mJsonObject2.getJSONArray("wru");
				System.out.print("jsonArray1" + jsonArray1);
				System.out.print("jsonArray2" + jsonArray2);
				// "intrests_id":"2","intrests_name":"DANCE","intrests_image":"2.jpeg","intrests_status":"1"
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject mJsonObject = jsonArray1.getJSONObject(i);
					InterestsDTO interestsDTO = new InterestsDTO();
					interestsDTO.setInterestId(mJsonObject
							.getString("intrests_id"));
					interestsDTO.setInterestName(mJsonObject
							.getString("intrests_name"));

					// edited for revision 607
					// MAY 15 2015
					// added if statement
					if (!mJsonObject.getString("intrests_image").isEmpty()) {
						interestsDTO
								.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
										+ mJsonObject
												.getString("intrests_image"));
					}
					// -- - - - -
					interestsDTO.setIsSelected(false);
					String temp = mJsonObject.getString("intrests_id");
					Log.d("", "json interest id=" + temp);

					for (int k = 0; k < userDTO.getInterestList().size(); k++) {
						// InterestsDTO dto =
						// userDTO.getInterestList().get(k);
						Log.d("", "dto interest id="
								+ userDTO.getInterestList().get(k)
										.getInterestId());
						if (userDTO.getInterestList().get(k).getInterestId()
								.equals(temp)) {
							Log.d("", "interest selection =true " + temp);
							interestsDTO.setIsSelected(true);
							selectedInterest.add(interestsDTO);
						}
					}
					interestsDTOs.add(interestsDTO);
				}

				for (int x = 0; x < jsonArray2.length(); x++) {
					JSONObject mj = jsonArray2.getJSONObject(x);
					WhatAreYouDataDTO whatAreYouDataDTO = new WhatAreYouDataDTO();
					whatAreYouDataDTO.setId(mj.getString("id"));
					whatAreYouDataDTO.setName(mj.getString("name"));
					whatAreYouDataDTO.setSelected(false);
					if (userDTO.getWhatAreYouDTO().getWhatAreYou()
							.equals(mj.getString("id"))) {
						whatAreYouDataDTO.setSelected(true);
						spnSelected = x;
					}
					whatAreYouDataDTOs1.add(whatAreYouDataDTO);
				}
			} catch (Exception e) {

			}

			adapter = new SettingListInterestAdapter(context, interestsDTOs,
					AppConstants.ADAPTER_FLAG_EDIT_PROFILE);
			horizontalListView.setAdapter(adapter);

			spinnerEthnicityAdapter = new EditProfileSpinnerAdapter(context,
					whatAreYouDataDTOs1, 0);
			spnEthnicity.setAdapter(spinnerEthnicityAdapter);
			spnEthnicity.setSelection(spnSelected);
			spnEthnicity
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							whatAreYouDTO
									.setWhatAreYou(""
											+ whatAreYouDataDTOs1.get(position)
													.getId());
							if (!isAlertShown && isEthSelected)
								showSaveAlert();

							isEthSelected = true;
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});

			scrollView.scrollTo(0, 0);
			// dudeDetailsInterestAdapter = new
			// DudeDetailsInterestAdapter(context,
			// (ArrayList<InterestsDTO>) interestsDTOs);
			// horizontalListView.setAdapter(dudeDetailsInterestAdapter);
			// relationshipStatusSpinnerAdapter = new
			// RelationshipStatusSpinnerAdapter(
			// context, context.getResources().getStringArray(
			// R.array.relationship_status_array));
			// spnRelationshipStatus.setAdapter(relationshipStatusSpinnerAdapter);
			spnRelationshipStatus
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							whatAreYouDTO.setRelationshipStatus(""
									+ (spnRelationshipStatus
											.getSelectedItemPosition() + 1));

							if (!isAlertShown && isRelationshipSelected)
								showSaveAlert();

							isRelationshipSelected = true;
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});
		}
	}

	class UpdateProfileAsynchTask extends AsyncTask<String, Void, String> {
		private JSONArray jsonArray;
		private JSONObject jsonObject;
		private String vStatus;
		private TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// singleton.progressLoading(loadingView, llLoading);
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
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
				reqEntity.addPart("fname",
						new StringBody(userDTO.getFirstName()));
				// userDTO.getFirstName().toString()));
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

				reqEntity.addPart("role", new StringBody(userDTO
						.getWhatAreYouDTO().getRole().toString()));
				reqEntity.addPart("body_type", new StringBody(userDTO
						.getBodyTypeId().toString()));

				for (int i = 0; i < storeLoveHookup.size(); i++) {
					// params.add(new BasicNameValuePair("friend_ids["+i+"]",
					// selectedUserDTOs.get(i).getUserID()));
					reqEntity.addPart("love_hookup[" + i + "]", new StringBody(
							storeLoveHookup.get(i)));
				}

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

				reqEntity.addPart("user_note", new StringBody(aboutMe.getText()
						.toString()));

				// ELv
				// if (picturesPathArray[0].equals(AppConstants.FACEBOOK_IMAGE))
				// {
				// if (picPathArray.get(0).equals(AppConstants.FACEBOOK_IMAGE))
				// {
				// reqEntity.addPart("user_facebook_image", new StringBody(
				// userDTO.getProfileImage()));
				// } else {
				reqEntity.addPart("user_facebook_image", new StringBody("url"));
				// }

				for (int i = 0; i < selectedInterest.size(); i++) {
					// reqEntity.addPart("interest[" + i + "][]", new
					// StringBody(String.valueOf(i + 1)));
					reqEntity.addPart(
							"interest[" + i + "][]",
							new StringBody(String.valueOf(selectedInterest.get(
									i).getInterestId())));

				}
				if (userDTO.isFacebookLikeEnable()) {
					if (facebookLikesDTOs != null) {
						facebookLikesDTOs = userDTO.getFacebookLikesDTOs();
						Log.d("",
								"isFacebookLikesEnable="
										+ userDTO.isFacebookLikeEnable());
						Log.d("", "size=" + facebookLikesDTOs.size());
						reqEntity.addPart("user_facebook_Interest",
								new StringBody(
										AppConstants.FACEBOOK_LIKE_ENABLE));
						for (int i = 0; i < facebookLikesDTOs.size(); i++) {
							// reqEntity.addPart("interest[" + i + "][]", new
							// StringBody(String.valueOf(i + 1)));
							reqEntity.addPart(
									"fbintrast[" + i + "][]",
									new StringBody(String
											.valueOf(facebookLikesDTOs.get(i)
													.getImagePath())));

						}
					}
				} else {
					reqEntity.addPart("user_facebook_Interest", new StringBody(
							AppConstants.FACEBOOK_LIKE_DISABLE));
				}

				// Delete photos/update photos
				for (int i = 0; i < mAdapter.getDeletePhotoList().size(); i++) {
					reqEntity
							.addPart("delete_image[" + i + "][]",
									new StringBody(mAdapter
											.getDeletePhotoList().get(i)));

				}

				// Add new photos
				for (int i = 0; i < mAdapter.getAddPhotoList().size(); i++) {

					file = new File(mAdapter.getAddPhotoList().get(i)
							.getImagePath());

					FileBody bin = new FileBody(file);
					reqEntity.addPart("thumb_image" + i, bin);

					reqEntity.addPart("is_public" + i,
							new StringBody(mAdapter.getAddPhotoList().get(i)
									.isPublic() ? (AppConstants.IMAGE_PUBLIC)
									: (AppConstants.IMAGE_PRIVATE)));

				}

				// Updating photo order
				// for (int i = mAdapter.getPhotoList().size() - 1; i > 0; i--)
				// {
				// UserProfileImageDTO image = mAdapter.getPhotoList().get(i);
				// if (image.getImageId() == null) {
				// reqEntity.addPart("image_ids[" + i + "]", new
				// StringBody("FILE"));
				// } else {
				// reqEntity.addPart("image_ids[" + i + "]", new
				// StringBody("ID"+image.getImageId()));
				// }
				//
				// }

				// revision 580
				// int i = mAdapter.getPhotoList().size() - 1; i > 0; i--
				//
				for (int i = 1; i < mAdapter.getPhotoList().size(); i++) {
					UserProfileImageDTO image = mAdapter.getPhotoList().get(i);
					if (image.getImageId() == null) {
						reqEntity.addPart("image_ids[" + i + "]",
								new StringBody("FILE"));
					} else {
						reqEntity.addPart("image_ids[" + i + "]",
								new StringBody("ID" + image.getImageId()));
					}

				}

				// Clear arrays for add and delete list
				mAdapter.clearArrays();

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
			} catch (Exception e) {

			}
			return vResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			// singleton.stopLoading(llLoading);

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
					if (sessionManager.isLogin()) {
						sessionManager
								.setUserDetail(parseUserDataAndSetInSession(jsonObject));
					}

					refreshData();

					// ((Activity) context).finish();

				} else if (vStatus.equals("failure")) {
					// Toast.makeText(context, "Profile Not Updated",
					// Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			switch (checkId) {
			case 1:
				// context.slide_me.toggleLeftDrawer();
				break;
			case 2:
				context.slide_me.toggleRightDrawer();

				break;
			case 3:
				context.onClick(context.layoutFindDudes);
				break;

			default:
				break;
			}

		}

	}

	public void refreshData() {
		userDTO = sessionManager.getUserDetail();
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
		userDTO.setBodyType(this.userDTO.getBodyTypeId());
		userDTO.setLoveHookup(this.userDTO.getLoveHookup());
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
		whatAreYouDTO.setRole(this.userDTO.getWhatAreYouDTO().getRole());// MainObject.getString("role"));
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

	public void openDialogForHight() {
		String unit[] = { "METRIC", "U.S." };
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_enter_height);

		final NumberPicker feetWheel = (NumberPicker) dialog
				.findViewById(R.id.feetWheel);
		final NumberPicker inchesWheel = (NumberPicker) dialog
				.findViewById(R.id.inchesWheel);
		final NumberPicker unitWheel = (NumberPicker) dialog
				.findViewById(R.id.unitWheel);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button clear = (Button) dialog.findViewById(R.id.clear);

		final TextView feetText = (TextView) dialog.findViewById(R.id.feet);
		final TextView inchesText = (TextView) dialog.findViewById(R.id.inches);
		final TextView setValue = (TextView) dialog.findViewById(R.id.setValue);
		// title.setText(titleString);
		// text.setText(titleString);

		unitWheel.setMaxValue(1);
		unitWheel.setMinValue(0);
		unitWheel.setDisplayedValues(unit);
		setValue.setText("HIEGHT");
		if (whatAreYouDTO.getHeightUnit().equals(AppConstants.HEIGHT_IN_FEET)) {
			unitWheel.setValue(1);

			feetWheel.setMaxValue(7);
			feetWheel.setMinValue(5);

			inchesWheel.setMaxValue(11);
			inchesWheel.setMinValue(0);
			feetText.setText("Ft");
			inchesText.setText("In");

			if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE
					|| whatAreYouDTO.getHight().length() != 0) {
				String h = whatAreYouDTO.getHight();
				// setValue.setText(h.split("/")[0] + "Feet" + h.split("/")[1]
				// + "Inches");
				System.out.println(h);
				try {
					feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
					inchesWheel.setValue(Integer.valueOf(h.split("/")[1]));
				} catch (Exception e) {
				}

				if (feetWheel.getValue() == 7) {
					inchesWheel.setValue(0);
					inchesWheel.setMaxValue(0);
				}
			}
			setValue.setText("" + feetWheel.getValue() + " feet,"
					+ inchesWheel.getValue() + " inches");

		} else {
			unitWheel.setValue(0);
			feetWheel.setMaxValue(2);
			feetWheel.setMinValue(1);

			inchesWheel.setMaxValue(99);
			inchesWheel.setMinValue(0);
			inchesWheel.setValue(50);
			inchesWheel.setScrollBarSize(150);
			// inchesWheel.setDisplayedValues(grams);
			feetText.setText("Mts");
			inchesText.setText("CM");

			if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE
					|| whatAreYouDTO.getHight().length() != 0) {

				String h = whatAreYouDTO.getHight();
				// setValue.setText(w.split("/")[0] + "Kilo" + w.split("/")[1]
				// + "Grams");
				try {
					feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
					inchesWheel.setValue((Integer.valueOf(h.split("/")[1])));
				} catch (Exception e) {
				}

			}
			setValue.setText("" + feetWheel.getValue() + " meter,"
					+ inchesWheel.getValue() + " centi meter");
		}

		unitWheel.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {

				Log.d("height", "" + newVal);
				if (newVal == 1) {
					whatAreYouDTO.setHeightUnit(AppConstants.HEIGHT_IN_FEET);
					storeHeightUnit = AppConstants.HEIGHT_IN_FEET;
					Double meter = (double) (feetWheel.getValue() + ((inchesWheel
							.getValue()) / 100));
					Log.d("", "meter=" + meter);
					int inches = getMeterToInches(meter);
					Log.d("", "inches=" + inches);
					feetWheel.setMaxValue(7);
					feetWheel.setMinValue(5);

					inchesWheel.setMaxValue(11);
					inchesWheel.setMinValue(0);
					feetText.setText("Ft");
					inchesText.setText("In");

					feetWheel.setValue(inches / 12);
					inchesWheel.setValue(inches % 12);

					storeHeight = feetWheel.getValue() + "/"
							+ inchesWheel.getValue();
					setValue.setText("" + feetWheel.getValue() + " feet,"
							+ inchesWheel.getValue() + " inches");
				} else {
					whatAreYouDTO.setHeightUnit(AppConstants.HEIGHT_IN_METER);
					storeHeightUnit = AppConstants.HEIGHT_IN_METER;
					int in = ((feetWheel.getValue()) * 12)
							+ inchesWheel.getValue();
					int cm = (int) Math.round(in * 2.54);
					Log.d("", "inches=" + in);
					Log.d("", "cm=" + cm);
					feetWheel.setMaxValue(2);
					feetWheel.setMinValue(1);

					inchesWheel.setMaxValue(99);
					inchesWheel.setMinValue(0);
					// inchesWheel.setDisplayedValues(grams);
					feetText.setText("Mts");
					inchesText.setText("CM");

					feetWheel.setValue(cm / 100);
					inchesWheel.setValue((cm % 100));
					storeHeight = feetWheel.getValue() + "/"
							+ inchesWheel.getValue();
					setValue.setText("" + feetWheel.getValue() + " meter,"
							+ inchesWheel.getValue() + " centi meter");
				}

			}

		});

		OnValueChangeListener scrolledListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				Log.d("on valiue change", "oldVal=" + oldVal + " newVal="
						+ newVal);
				if (whatAreYouDTO.getHeightUnit().equals(
						AppConstants.HEIGHT_IN_FEET)) {
					setValue.setText("" + feetWheel.getValue() + " feet,"
							+ inchesWheel.getValue() + " inches");
				} else {
					setValue.setText("" + feetWheel.getValue() + " meter,"
							+ inchesWheel.getValue() + " centi meter");
				}

			}
		};
		// feetWheel.setOnValueChangedListener(scrolledListener);
		feetWheel.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (oldVal != 7 && newVal == 7) {
					inchesWheel.setMaxValue(0);
					inchesWheel.setValue(0);
				} else {
					inchesWheel.setMaxValue(11);
				}

				if (whatAreYouDTO.getHeightUnit().equals(
						AppConstants.HEIGHT_IN_FEET)) {
					setValue.setText("" + feetWheel.getValue() + " feet,"
							+ inchesWheel.getValue() + " inches");
				} else {
					setValue.setText("" + feetWheel.getValue() + " meter,"
							+ inchesWheel.getValue() + " centi meter");
				}
			}
		});

		inchesWheel.setOnValueChangedListener(scrolledListener);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				whatAreYouDTO.setHight("" + feetWheel.getValue() + "/"
						+ inchesWheel.getValue());
				if (whatAreYouDTO.getHeightUnit().equals(
						AppConstants.HEIGHT_IN_FEET)) {
					tvProfileHeight.setText("" + feetWheel.getValue() + "'"
							+ inchesWheel.getValue() + "\"");
				} else {
					tvProfileHeight.setText("" + feetWheel.getValue() + " m,"
							+ inchesWheel.getValue() + " cm");

				}

				Log.d("", "hight=" + whatAreYouDTO.getHight() + " "
						+ whatAreYouDTO.getHeightUnit());

				dialog.dismiss();
				if (!isAlertShown)
					showSaveAlert();
			}
		});
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvProfileHeight.setText("");
				dialog.dismiss();

			}
		});
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();

	}

	public void openDialogForWeight() {
		final String[] grams = new String[10];
		for (int i = 0; i < 10; i++) {

			grams[i] = ("" + (i * 100));

		}
		String unit[] = { "METRIC", "U.S." };
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_enter_weight);

		final NumberPicker feetWheel = (NumberPicker) dialog
				.findViewById(R.id.feetWheel);
		final NumberPicker inchesWheel = (NumberPicker) dialog
				.findViewById(R.id.inchesWheel);
		final NumberPicker unitWheel = (NumberPicker) dialog
				.findViewById(R.id.unitWheel);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button clear = (Button) dialog.findViewById(R.id.clear);

		final TextView feetText = (TextView) dialog.findViewById(R.id.feet);
		final TextView inchesText = (TextView) dialog.findViewById(R.id.inches);
		final TextView setValue = (TextView) dialog.findViewById(R.id.setValue);
		// title.setText(titleString);
		// text.setText(titleString);

		unitWheel.setMaxValue(1);
		unitWheel.setMinValue(0);
		unitWheel.setDisplayedValues(unit);
		setValue.setText("WEIGHT");
		if (whatAreYouDTO.getWeightUnit().equals(
				AppConstants.WEIGHT_IN_KILOGRAM)) {
			unitWheel.setValue(0);
			feetWheel.setMaxValue(199);
			feetWheel.setMinValue(50);
			feetWheel.setValue(50);

			inchesWheel.setMaxValue(9);
			inchesWheel.setMinValue(0);
			inchesWheel.setDisplayedValues(grams);
			feetText.setText("kilo");
			inchesText.setText("gms");

			if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE
					|| whatAreYouDTO.getWeight().length() != 0) {
				String w = whatAreYouDTO.getWeight();
				// setValue.setText(h.split("/")[0] + "Feet" + h.split("/")[1]
				// + "Inches");
				try {
					feetWheel.setValue(Integer.valueOf(w.split("/")[0]));
					inchesWheel
							.setValue((Integer.valueOf(w.split("/")[1])) / 100);
				} catch (Exception e) {

				}
			}
			// setValue.setText("" + feetWheel.getValue() + " kilo,"
			// + grams[inchesWheel.getValue()] + " grams");

			setValue.setText("" + feetWheel.getValue() + " kilo");

		} else {
			unitWheel.setValue(1);
			feetWheel.setMaxValue(700);
			feetWheel.setMinValue(80);
			feetWheel.setValue(100);

			inchesWheel.setMaxValue(9);
			inchesWheel.setMinValue(0);
			inchesWheel.setDisplayedValues(grams);
			feetText.setText("lb");
			inchesText.setText("oz");

			if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE
					|| whatAreYouDTO.getWeight().length() != 0) {

				String h = whatAreYouDTO.getWeight();
				// setValue.setText(w.split("/")[0] + "Kilo" + w.split("/")[1]
				// + "Grams");
				try {
					feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
					inchesWheel
							.setValue((Integer.valueOf(h.split("/")[1])) / 100);
				} catch (Exception e) {
				}

			}
			// setValue.setText("" + feetWheel.getValue() + " pound,"
			// + grams[inchesWheel.getValue()] + " ounce");

			setValue.setText("" + feetWheel.getValue() + " pound");
		}

		unitWheel.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (newVal == 0) {
					whatAreYouDTO
							.setWeightUnit(AppConstants.WEIGHT_IN_KILOGRAM);

					int gms = (int) ((feetWheel.getValue() * 454) + (inchesWheel
							.getValue() * 28.35));
					feetWheel.setMaxValue(199);
					feetWheel.setMinValue(10);

					inchesWheel.setMaxValue(9);
					inchesWheel.setMinValue(0);
					inchesWheel.setDisplayedValues(grams);
					feetText.setText("kilo");
					inchesText.setText("gms");

					feetWheel.setValue(gms / 1000);
					inchesWheel.setValue(Math.round((gms % 1000) / 100));

					// setValue.setText("" + feetWheel.getValue() + " kilo,"
					// + inchesWheel.getValue() + " grams");
					setValue.setText("" + feetWheel.getValue() + " kilo");
				} else {
					whatAreYouDTO.setWeightUnit(AppConstants.WEIGHT_IN_POUND);
					int oz = (int) (((feetWheel.getValue() * 1000) + (inchesWheel
							.getValue())) * 0.0353);
					feetWheel.setMaxValue(439);
					feetWheel.setMinValue(22);

					inchesWheel.setMaxValue(9);
					inchesWheel.setMinValue(0);
					inchesWheel.setDisplayedValues(grams);
					feetText.setText("lb");
					inchesText.setText("oz");

					feetWheel.setValue(oz / 16);
					inchesWheel.setValue(Math.round((oz % 16) / 100));
					// setValue.setText("" + feetWheel.getValue() + " pound,"
					// + inchesWheel.getValue() + " ounce");
					setValue.setText("" + feetWheel.getValue() + " pound");
				}
			}
		});

		OnValueChangeListener scrolledListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (whatAreYouDTO.getWeightUnit().equals(
						AppConstants.WEIGHT_IN_KILOGRAM)) {
					// setValue.setText("" + feetWheel.getValue() + " kilo,"
					// + grams[inchesWheel.getValue()] + " grams");
					setValue.setText("" + feetWheel.getValue() + " kilo");
				} else {
					// setValue.setText("" + feetWheel.getValue() + " pound,"
					// + grams[inchesWheel.getValue()] + " ounce");
					setValue.setText("" + feetWheel.getValue() + " pound");
				}

			}
		};
		feetWheel.setOnValueChangedListener(scrolledListener);
		inchesWheel.setOnValueChangedListener(scrolledListener);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("" + feetWheel.getValue() + "/"
						+ grams[inchesWheel.getValue()]);
				whatAreYouDTO.setWeight("" + feetWheel.getValue() + "/"
						+ grams[inchesWheel.getValue()]);

				if (whatAreYouDTO.getWeightUnit().equals(
						AppConstants.WEIGHT_IN_KILOGRAM)) {
					tvProfileWeight.setText("" + feetWheel.getValue() + " kg");
				} else {
					tvProfileWeight.setText("" + feetWheel.getValue() + " lb");

				}
				Log.d("", "weight=" + whatAreYouDTO.getWeight() + " "
						+ whatAreYouDTO.getWeightUnit());

				if (!isAlertShown)
					showSaveAlert();

				dialog.dismiss();
			}
		});

		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvProfileWeight.setText("");
				dialog.dismiss();

			}
		});

		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();

	}

	private int getMeterToInches(Double meter) {
		Double inches = Double.valueOf(meter) * 39.3701;
		// value = value*12;
		return (int) Math.round(inches);
	}

	public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth + 1;
			day = selectedDay;
			storeBirthday = selectedDay + "/" + (selectedMonth + 1) + "/"
					+ selectedYear;
			Boolean check = calculateAge(selectedDay + "/"
					+ (selectedMonth + 1) + "/" + selectedYear);
			if (check) {
				whatAreYouDTO.setAge(selectedDay + "/" + (selectedMonth + 1)
						+ "/" + selectedYear);

				if (!isAlertShown)
					showSaveAlert();
			}

		}
	};

	private Boolean calculateAge(String berthDateString) {
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
		if (currentDate.before(berthDate)) {
			Toast.makeText(context,
					R.string.toast_please_select_the_currect_birth_date,
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (number_of_years < 18) {
			Toast.makeText(context,
					R.string.toast_you_must_be_18_year_old_or_above,
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (number_of_years > 75) {
			Toast.makeText(context,
					R.string.toast_you_must_be_75_year_old_or_below,
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			// selectedAge.setText("" + number_of_years + "Years,"
			// + number_of_months + "Months");
			tvProfileAge.setText("" + number_of_years);
			return true;
		}
	}

}
