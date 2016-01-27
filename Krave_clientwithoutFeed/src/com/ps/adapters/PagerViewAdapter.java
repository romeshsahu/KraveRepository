package com.ps.adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.spi.SelectorProvider;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.facebook.model.GraphUser;
import com.flurry.android.FlurryAgent;
import com.image.crop.Gallery_Activity;
import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.Activity_Login;
import com.krave.kraveapp.Activity_ProfileDetails;
import com.krave.kraveapp.Activity_TermAndCondition;
import com.krave.kraveapp.AppManager;
import com.krave.kraveapp.MyApps;
import com.krave.kraveapp.R;
import com.krave.kraveapp.Activity_Home.GetDudeRequistsList;
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
import com.ps.models.WhatAreYouDataDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.InternalStorageContentProvider;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveRestClient;
import com.ps.utill.SessionManager;
import com.ps.utill.Utils;
import com.ps.utill.WebServiceConstants;

/**
 * Created by sys1 on 1/31/14.
 */
public class PagerViewAdapter extends PagerAdapter {
	private List<InterestsDTO> selectedInterest = new ArrayList<InterestsDTO>();
	private List<WhatAreYouDataDTO> selectedWhatDoYouKrave = new ArrayList<WhatAreYouDataDTO>();
	private List<InterestsDTO> interestList;
	private List<WhatAreYouDataDTO> whatAreYouDataList1;
	private List<WhatAreYouDataDTO> whatAreYouDataList2 = new ArrayList<WhatAreYouDataDTO>();
	private GridViewAdapter mGridViewAdapter;
	private Activity_ProfileDetails context;
	private LayoutInflater inflater;
	private int[] integerList;
	public static UserDTO userDTO;
	private String mValue;
	private WhatAreYouDTO whatAreYouDTO;
	private SessionManager sessionManager;
	private String picturePath = "";
	private File file;
	private int intentValue;

	private static int RESULT_LOAD_IMAGE = 1;
	private static int RESULT_CAMERA_IMAGE = 4;
	private AlertDialog alertDialog = null;
	private ImageLoader imageLoader;
	public int year = 1990;
	public int month = 1;
	public int day = 1;
	public static final int REQUEST_CODE_GALLERY = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

	private EditText firstName, lastName, email, mobile, password,
			confirmPassword;
	private Spinner languageSpinner;
	private ImageView backButton;
	private Button langaugeButton;
	public static GraphUser graphUser;
	public static List<FacebookLikesDTO> facebookLikesDTOs;
	private Button submitButton;
	private ListView group1, group2;
	private ImageButton submiButton1, submiButton3;
	private Button submitButton2, submiButton4, submitButton5;
	private GridView gridViewInterest;
	private RelativeLayout inchButton, meterButton;
	private LinearLayout feetButton, weightButton, ageButton;
	private TextView selectedHeight, selectedWeight, selectedAge;
	private Button heightUnitButton, weightUnitButton;
	private RadioGroup relationshipButton;
	private TextView titel, acceptTermAnsConditionTextView, accept;
	private ImageView profilePick1, profilePick2, profilePick3, profilePick4,
			profilePick5, profilePick6;
	private ImageView profilePick1Lock, profilePick2Lock, profilePick3Lock,
			profilePick4Lock, profilePick5Lock, profilePick6Lock;

	private EditText aboutMe;
	// private RadioButton facebookIntegrationRadioBtn;
	private ImageView facebookLikesIntegration, termAndConditionImageView;
	// private boolean isFacebookLikesEnable = false;
	private boolean validationRelationshipStatus = false;
	private ViewGroup viewGroup;
	private int index;
	private GPSTracker gpsTracker;
	private Typeface typefaceHeader;
	private boolean isTermAnsConditionAccept = false;
	public static final int REQUEST_CODE_CROP_IMAGE = 103;
	public static String actionQuit;
	private AppManager singleton;

	// Double heightValue = 0.0, weightValue = 0.0;

	public PagerViewAdapter(Context context, int[] list,
			List<InterestsDTO> list1, List<WhatAreYouDataDTO> list2,
			List<WhatAreYouDataDTO> list3, AppManager singleton) {
		this.context = (Activity_ProfileDetails) context;
		this.integerList = list;
		this.interestList = list1;
		this.whatAreYouDataList1 = list2;

		this.whatAreYouDataList2 = list3;
		this.singleton = singleton;
		intentValue = this.context.getIntent().getExtras()
				.getInt(AppConstants.COME_FROM);
		imageLoader = new ImageLoader(this.context);
		if (intentValue != AppConstants.COME_FROM_UPDATE_PROFILE) {
			userDTO = new UserDTO();
		}
		gpsTracker = new GPSTracker(context);
		typefaceHeader = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
	}

	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {

		return 2;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		viewGroup = container;
		index = position;

		// final View viewLayout = null;
		sessionManager = new SessionManager(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View viewLayout = inflater.inflate(integerList[position],
				container, false);
		Log.d("", "position=" + position);
		// if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
		Button backButton = (Button) viewLayout.findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position == 0) {
					context.finish();
					FlurryAgent.logEvent("Register - QuitButton");
				} else {
					((ViewPager) container).setCurrentItem(position - 1);
					FlurryAgent.logEvent("Register - BackButton");
				}
			}
		});
		backButton.setVisibility(View.VISIBLE);
		// }
		switch (position) {
		case 0:
			actionQuit = "Registration_Page_1";
			setRegistrationLayout(viewLayout, container, position);
			break;
		// case 1:
		//
		// seProfile1Layout(viewLayout, container, position);
		// break;
		case 1:
			actionQuit = "Registration_Page_2";
			seProfile2Layout(viewLayout, container, position);

			break;
		// case 2:
		// actionQuit = "Registration_Page_3";
		// seProfile3Layout(viewLayout, container, position);
		//
		// break;
		case 2:
			actionQuit = "Registration_Page_4";
			seProfile4Layout(viewLayout, container, position);
			break;
		case 3:
			actionQuit = "Registration_Page_5";
			seProfile5Layout(viewLayout);
			break;

		default:
			break;
		}

		((ViewPager) container)
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						Log.d("", "onPageSelected " + arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						Log.d("", "onPageScrolled " + arg0);
					}

					@Override
					public void onPageScrollStateChanged(int id) {
						Log.d("", "onPageScrollStateChanged " + id);
						switch (position) {
						case 0:
							userDTO.setFirstName(firstName.getText().toString());
							userDTO.setLastName(lastName.getText().toString());
							userDTO.setEmail(email.getText().toString());
							userDTO.setMobile(mobile.getText().toString());
							if (intentValue != AppConstants.COME_FROM_UPDATE_PROFILE) {
								userDTO.setPassword(password.getText()
										.toString());
								userDTO.setConfirmPassword(confirmPassword
										.getText().toString());
							}
							break;

						default:
							break;
						}

					}
				});

		((ViewPager) container).addView(viewLayout);

		return viewLayout;

	}

	// private void seProfile3Layout(View viewLayout, final ViewGroup container,
	// final int position) {
	// whatAreYouDTO = userDTO.getWhatAreYouDTO();
	// titel = (TextView) viewLayout.findViewById(R.id.titleTextView);
	// titel.setText("WHAT DO YOU KRAVE?");
	// titel.setTypeface(typefaceHeader);
	//
	// group2 = (ListView) viewLayout.findViewById(R.id.list2);
	// submiButton3 = (ImageButton) viewLayout
	// .findViewById(R.id.submitButton3);
	// final WhatAreYouListAdapter myAdapter1 = new WhatAreYouListAdapter(
	// context, whatAreYouDataList2, 0);
	// group2.setAdapter(myAdapter1);
	//
	// group2.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
	// long arg3) {
	// for (int i = 0; i < whatAreYouDataList2.size(); i++) {
	// if (i == pos) {
	// whatAreYouDataList2.get(i).setSelected(true);
	//
	// } else {
	// whatAreYouDataList2.get(i).setSelected(false);
	// }
	// }
	// Log.d("", "position=" + pos);
	// whatAreYouDTO.setWhatDoYouKrave(""
	// + whatAreYouDataList2.get(pos).getId());
	// myAdapter1.notifyDataSetChanged();
	//
	// }
	//
	// });
	//
	// submiButton3.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// if ("".equals(whatAreYouDTO.getWhatDoYouKrave())) {
	// Toast.makeText(context,
	// "please select at least one choice",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// ((ViewPager) container).setCurrentItem(position + 1);
	// }
	// }
	// });
	//
	// }

	private void seProfile3Layout(View viewLayout, final ViewGroup container,
			final int position) {
		whatAreYouDTO = userDTO.getWhatAreYouDTO();
		titel = (TextView) viewLayout.findViewById(R.id.titleTextView);
		// titel.setText("WHAT DO YOU KRAVE?");
		titel.setText(R.string.titel_create_your_account);
		titel.setTypeface(typefaceHeader);

		group2 = (ListView) viewLayout.findViewById(R.id.list2);
		submiButton3 = (ImageButton) viewLayout
				.findViewById(R.id.submitButton3);
		final WhatAreYouListAdapter myAdapter1 = new WhatAreYouListAdapter(
				context, whatAreYouDataList2, 5);
		group2.setAdapter(myAdapter1);

		group2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// for (int i = 0; i < whatAreYouDataList2.size(); i++) {
				WhatAreYouDataDTO whatAreYouDataDTO = whatAreYouDataList2
						.get(pos);
				if (whatAreYouDataDTO.isSelected()) {
					whatAreYouDataDTO.setSelected(false);
					selectedWhatDoYouKrave.remove(whatAreYouDataDTO);

				} else {
					whatAreYouDataDTO.setSelected(true);
					selectedWhatDoYouKrave.add(whatAreYouDataDTO);

				}
				// }
				// Log.d("", "position=" + pos);
				// whatAreYouDTO.setWhatDoYouKrave(""
				// + whatAreYouDataList2.get(pos).getId());
				myAdapter1.notifyDataSetChanged();

			}

		});

		submiButton3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if ("".equals(whatAreYouDTO.getWhatDoYouKrave())) {
				if (selectedWhatDoYouKrave.size() <= 0) {
					Toast.makeText(context,
							R.string.toast_please_select_at_least_one_choice,
							Toast.LENGTH_SHORT).show();
				} else {
					FlurryAgent.logEvent("Register - WhatDoYouKrave");
					((ViewPager) container).setCurrentItem(position + 1);
				}
			}
		});

	}

	private void seProfile2Layout(View viewLayout, final ViewGroup container,
			final int position) {

		titel = (TextView) viewLayout.findViewById(R.id.titleTextView);
		titel.setText(R.string.titel_create_your_account);
		titel.setTypeface(typefaceHeader);

		group1 = (ListView) viewLayout.findViewById(R.id.p2list1);

		final WhatAreYouListAdapter myAdapter = new WhatAreYouListAdapter(
				context, whatAreYouDataList1, 0);
		group1.setAdapter(myAdapter);

		group1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				for (int i = 0; i < whatAreYouDataList1.size(); i++) {
					if (i == pos) {
						whatAreYouDataList1.get(i).setSelected(true);

					} else {
						whatAreYouDataList1.get(i).setSelected(false);

					}
				}
				Log.d("", "position=" + pos);
				whatAreYouDTO.setWhatAreYou(""
						+ whatAreYouDataList1.get(pos).getId());
				myAdapter.notifyDataSetChanged();

			}

		});

		LinearLayout llDontShow = (LinearLayout) viewLayout
				.findViewById(R.id.llDontShow);
		final ImageView checkboxDontShow = (ImageView) viewLayout
				.findViewById(R.id.checkboxDontShow);
		TextView tvDontShow = (TextView) viewLayout
				.findViewById(R.id.textView88);
		checkboxDontShow.setTag(R.drawable.unselect_notofication);

		feetButton = (LinearLayout) viewLayout.findViewById(R.id.hightLayout);

		ageButton = (LinearLayout) viewLayout.findViewById(R.id.ageLayout);
		weightButton = (LinearLayout) viewLayout
				.findViewById(R.id.weightLayout);
		selectedHeight = (TextView) viewLayout.findViewById(R.id.selectedHight);

		selectedWeight = (TextView) viewLayout
				.findViewById(R.id.selectedWeight);
		selectedAge = (TextView) viewLayout.findViewById(R.id.selectedAge);

		TextView heightTextView = (TextView) viewLayout.findViewById(R.id.feet);
		TextView weightTextView = (TextView) viewLayout
				.findViewById(R.id.weight);
		TextView ageTextView = (TextView) viewLayout.findViewById(R.id.age);
		TextView relationShipStausText = (TextView) viewLayout
				.findViewById(R.id.relationShipStausText);
		RadioButton radio00 = (RadioButton) viewLayout
				.findViewById(R.id.radio0);
		RadioButton radio11 = (RadioButton) viewLayout
				.findViewById(R.id.radio1);
		RadioButton radio22 = (RadioButton) viewLayout
				.findViewById(R.id.radio2);
		RadioButton radio33 = (RadioButton) viewLayout
				.findViewById(R.id.radio3);

		relationshipButton = (RadioGroup) viewLayout
				.findViewById(R.id.radioGroup1);
		submitButton2 = (Button) viewLayout.findViewById(R.id.submitButton2);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		heightTextView.setTypeface(typeface);
		weightTextView.setTypeface(typeface);
		ageTextView.setTypeface(typeface);
		selectedHeight.setTypeface(typeface);
		selectedWeight.setTypeface(typeface);
		selectedAge.setTypeface(typeface);
		submitButton2.setTypeface(typeface);

		selectedHeight.setText("???");
		selectedWeight.setText("???");
		selectedAge.setText("???");

		tvDontShow.setTypeface(typeface);

		radio00.setTypeface(typeface);
		radio11.setTypeface(typeface);
		radio22.setTypeface(typeface);
		radio33.setTypeface(typeface);
		Typeface typeface1 = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Md);
		relationShipStausText.setTypeface(typeface1);

		// if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
		String h = whatAreYouDTO.getHight();
		String w = whatAreYouDTO.getWeight();
		String a = whatAreYouDTO.getAge();
		if (h.length() != 0) {
			if (whatAreYouDTO.getHeightUnit().equals(
					AppConstants.HEIGHT_IN_FEET)) {
				selectedHeight.setText(h.split("/")[0] + "'" + h.split("/")[1]
						+ "\"");
			} else {
				selectedHeight.setText(h.split("/")[0] + " m,"
						+ h.split("/")[1] + " cm");
			}
		}
		// else {
		// selectedHeight.setText("");
		// }
		if (w.length() != 0) {
			if (whatAreYouDTO.getWeightUnit().equals(
					AppConstants.WEIGHT_IN_KILOGRAM)) {
				selectedWeight.setText(w.split("/")[0] + "kg");
			} else {
				selectedWeight.setText(w.split("/")[0] + "lb");
			}
		}
		// selectedAge.setText(a);
		if (a.length() != 0) {
			year = Integer.valueOf(a.split("/")[2]);
			month = Integer.valueOf(a.split("/")[1]) - 1;
			day = Integer.valueOf(a.split("/")[0]);
			calculateAge(whatAreYouDTO.getAge());
		}

		// }

		llDontShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((Integer) checkboxDontShow.getTag() == R.drawable.unselect_notofication) {
					checkboxDontShow.setImageDrawable(context.getResources()
							.getDrawable(R.drawable.select_notification));
					checkboxDontShow.setTag(R.drawable.select_notification);
				} else {
					checkboxDontShow.setImageDrawable(context.getResources()
							.getDrawable(R.drawable.unselect_notofication));
					checkboxDontShow.setTag(R.drawable.unselect_notofication);
				}
			}
		});
		OnClickListener heightListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				openDialogForHight();

			}
		};
		OnClickListener weightListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				openDialogForWeight();

			}
		};
		OnClickListener ageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					context.showDialog(0);
				} catch (Exception exp) {

				}

			}
		};
		selectedHeight.setOnClickListener(heightListener);
		heightTextView.setOnClickListener(heightListener);
		feetButton.setOnClickListener(heightListener);
		selectedWeight.setOnClickListener(weightListener);
		weightTextView.setOnClickListener(weightListener);
		weightButton.setOnClickListener(weightListener);
		selectedAge.setOnClickListener(ageListener);
		ageTextView.setOnClickListener(ageListener);
		ageButton.setOnClickListener(ageListener);
		RadioButton radio1, radio2, radio3, radio4;
		switch (Integer.parseInt(whatAreYouDTO.getRelationshipStatus())) {

		case 1:
			radio1 = (RadioButton) viewLayout.findViewById(R.id.radio0);
			radio1.setChecked(true);

			break;
		case 2:
			radio2 = (RadioButton) viewLayout.findViewById(R.id.radio1);
			radio2.setChecked(true);
			break;
		case 3:
			radio3 = (RadioButton) viewLayout.findViewById(R.id.radio2);
			radio3.setChecked(true);
			break;
		case 4:
			radio4 = (RadioButton) viewLayout.findViewById(R.id.radio3);
			radio4.setChecked(true);
			break;

		default:
			break;
		}

		relationshipButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radio0:
							whatAreYouDTO.setRelationshipStatus("" + 1);

							break;
						case R.id.radio1:
							whatAreYouDTO.setRelationshipStatus("" + 2);

							break;
						case R.id.radio2:
							whatAreYouDTO.setRelationshipStatus("" + 3);

							break;

						}
						validationRelationshipStatus = true;
					}
				});
		submitButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String validationString = context.getResources().getString(
						R.string.registration_please_fill_in_your)
						+ " ";
				boolean showValidation = false;

				String selectedHeightString = "selectedHeight";
				String selectedWeightString = "SpannableStringBuilder";
				String selectedAgeString = "selectedAge";

				if ("".equals(whatAreYouDTO.getWhatAreYou())) {
					showValidation = true;

					validationString = validationString.concat(context
							.getResources().getString(
									R.string.toast_space_what_are_you));
					validationString = validationString.concat(" ");
				}

				// if (0 == selectedHeight.getText().toString().trim().length()
				// || selectedHeightString.equalsIgnoreCase(selectedHeight
				// .getText().toString())) {
				// showValidation = true;
				//
				// validationString = validationString.concat(context
				// .getResources().getString(
				// R.string.registration_height));
				// validationString = validationString.concat(", ");
				// }
				// else
				Log.d("JCv",
						"selectedWeight.getText().toString().trim().length()"
								+ selectedWeight.getText().toString().trim()
										.length());
				// if (0 == selectedWeight.getText().toString().trim().length()
				// || selectedWeightString.equalsIgnoreCase(selectedWeight
				// .getText().toString())) {
				// showValidation = true;
				//
				// validationString = validationString.concat(context
				// .getResources().getString(
				// R.string.registration_weight));
				// validationString = validationString.concat(", ");
				//
				// }
				// else
				Log.d("JCv",
						"selectedAgeString.equalsIgnoreCase(selectedAge.getText().toString()"
								+ selectedAgeString
										.equalsIgnoreCase(selectedAge.getText()
												.toString()));
				// if (selectedAge.getText().toString().trim().length() == 0
				// || selectedAgeString.equalsIgnoreCase(selectedAge
				// .getText().toString())) {
				if (selectedAge.getText().toString().trim().equals("???")) {
					showValidation = true;
					validationString = validationString.concat(context
							.getResources()
							.getString(R.string.registration_age));
					validationString = validationString.concat(", ");
				}
				if ("".equals(whatAreYouDTO.getHight())) {
					// validationCheck = validationCheck + " Height,";
					whatAreYouDTO.setHight("" + 0 + "/" + 0);
				}
				if ("".equals(whatAreYouDTO.getWeight())) {
					// validationCheck = validationCheck + " Weight,";
					// whatAreYouDTO.setWeightUnit(AppConstants.WEIGHT_IN_POUND);
					whatAreYouDTO.setWeight("" + 0 + "/" + 0);
				}
				if (showValidation) {
					int lastCharacter = validationString.length() - 1;
					if (validationString.subSequence(lastCharacter,
							validationString.length()).equals(",")) {
						validationString = (String) validationString
								.subSequence(0, lastCharacter);
					}
					Toast.makeText(context, validationString, Toast.LENGTH_LONG)
							.show();
				} else {
					// ((ViewPager) container).setCurrentItem(position + 1);

					if (WebServiceConstants.isOnline(context)) {
						context.easyTrackerEventLog("Registered_UserAge_"
								+ context.singleton.calculateAge(whatAreYouDTO
										.getAge()));
						new RegistrationAsynchTask()
								.execute(WebServiceConstants.BASE_URL_FILE
										+ WebServiceConstants.USER_REGISTRATION);
						new AvRegUserAsynch()
								.execute(WebServiceConstants.AV_INSERT_PROFILE_DATA);
						FlurryAgent.logEvent("Register - Done"); // Edited
																	// revision
																	// 582
																	// commented
					}
				}
			}
		});

	}

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
		} else if (number_of_years < 18 || number_of_years > 75) {
			Toast.makeText(context,
					R.string.toast_you_must_be_18_to_75_years_old,
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			selectedAge.setText(""
					+ number_of_years
					+ context.getResources().getString(
							R.string.registration_years));
			return true;
		}
	}

	public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth + 1;
			day = selectedDay;
			Boolean check = calculateAge(selectedDay + "/"
					+ (selectedMonth + 1) + "/" + selectedYear);
			if (check) {
				whatAreYouDTO.setAge(selectedDay + "/" + (selectedMonth + 1)
						+ "/" + selectedYear);
			}

		}
	};

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
				feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
				inchesWheel.setValue(Integer.valueOf(h.split("/")[1]));
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
				feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
				inchesWheel.setValue((Integer.valueOf(h.split("/")[1])));

			}
			setValue.setText("" + feetWheel.getValue() + " meter,"
					+ inchesWheel.getValue() + " centi meter");
		}

		unitWheel.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (newVal == 1) {
					whatAreYouDTO.setHeightUnit(AppConstants.HEIGHT_IN_FEET);
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

					setValue.setText("" + feetWheel.getValue() + " feet,"
							+ inchesWheel.getValue() + " inches");
				} else {
					whatAreYouDTO.setHeightUnit(AppConstants.HEIGHT_IN_METER);
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
					if ((cm / 100) == 2) { // change by romesh 26/05/2015
						inchesWheel.setMaxValue(13);
						inchesWheel.setMinValue(0);
					} // change by romesh 26/05/2015
					inchesWheel.setValue((cm % 100));
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
				if (newVal == 1) { // change by romesh 26/05/2015
					inchesWheel.setMaxValue(99);
					inchesWheel.setValue(0);
				} else if (newVal == 2) {
					inchesWheel.setMaxValue(11);
					inchesWheel.setValue(0);
				} // change by romesh 26/05/2015
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

		// feetWheel.setOnValueChangedListener(scrolledListener);
		inchesWheel.setOnValueChangedListener(scrolledListener);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				whatAreYouDTO.setHight("" + feetWheel.getValue() + "/"
						+ inchesWheel.getValue());
				if (whatAreYouDTO.getHeightUnit().equals(
						AppConstants.HEIGHT_IN_FEET)) {
					selectedHeight.setText("" + feetWheel.getValue() + "'"
							+ inchesWheel.getValue() + "\"");
				} else {
					selectedHeight.setText("" + feetWheel.getValue() + " m,"
							+ inchesWheel.getValue() + " cm");

				}

				Log.d("", "hight=" + whatAreYouDTO.getHight() + " "
						+ whatAreYouDTO.getHeightUnit());

				dialog.dismiss();
			}
		});
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				whatAreYouDTO.setHight("");
				selectedHeight.setText("???");
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
				feetWheel.setValue(Integer.valueOf(w.split("/")[0]));
				inchesWheel.setValue((Integer.valueOf(w.split("/")[1])) / 100);
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
				feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
				inchesWheel.setValue((Integer.valueOf(h.split("/")[1])) / 100);

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
					selectedWeight.setText("" + feetWheel.getValue() + " kg");
				} else {
					selectedWeight.setText("" + feetWheel.getValue() + " lb");

				}
				Log.d("", "weight=" + whatAreYouDTO.getWeight() + " "
						+ whatAreYouDTO.getWeightUnit());

				dialog.dismiss();
			}
		});

		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				whatAreYouDTO.setWeight("");
				selectedWeight.setText("???");
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

	//

	private int getMeterToInches(Double meter) {
		Double inches = Double.valueOf(meter) * 39.3701;
		// value = value*12;

		return (int) Math.round(inches);

	}

	public String getfeet2meter(String feet) {

		Double value = Double.valueOf(feet) / 3.2808;

		value = Math.round(value * 100.0) / 100.0;
		return "" + value;
	}

	public String getmeter2feet(String meter) {
		Double value = Double.valueOf(meter) * 3.2808;
		value = Math.round(value * 100.0) / 100.0;
		return "" + value;
	}

	public void openDialog(final String titleString) {
		final String[] grams = new String[20];
		for (int i = 0; i < 20; i++) {

			grams[i] = ("" + (i * 50));

		}
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_enter_height);
		/*
		 * RadioGroup radioGroup = (RadioGroup) dialog
		 * .findViewById(R.id.heightRadioGroup);
		 */

		final NumberPicker feetWheel = (NumberPicker) dialog
				.findViewById(R.id.feetWheel);
		final NumberPicker inchesWheel = (NumberPicker) dialog
				.findViewById(R.id.inchesWheel);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		Button cancle = (Button) dialog.findViewById(R.id.cancle);

		// TextView title=(TextView) dialog.findViewById(R.id.title);
		// TextView text=(TextView) dialog.findViewById(R.id.heightText);
		TextView feetText = (TextView) dialog.findViewById(R.id.feet);
		TextView inchesText = (TextView) dialog.findViewById(R.id.inches);
		final TextView setValue = (TextView) dialog.findViewById(R.id.setValue);
		// title.setText(titleString);
		// text.setText(titleString);
		setValue.setText(titleString);
		if (titleString.equals("HEIGHT")) {
			feetWheel.setMaxValue(7);
			feetWheel.setMinValue(5);

			inchesWheel.setMaxValue(11);
			inchesWheel.setMinValue(0);
			feetText.setText("Feet");
			inchesText.setText("Inches");

			if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE
					|| whatAreYouDTO.getHight().length() != 0) {
				String h = whatAreYouDTO.getHight();
				// setValue.setText(h.split("/")[0] + "Feet" + h.split("/")[1]
				// + "Inches");
				feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
				inchesWheel.setValue(Integer.valueOf(h.split("/")[1]));
			}
			setValue.setText("" + feetWheel.getValue() + "Feet,"
					+ inchesWheel.getValue() + "Inches");

		} else {
			feetWheel.setMaxValue(200);
			feetWheel.setMinValue(20);

			inchesWheel.setMaxValue(19);
			inchesWheel.setMinValue(0);
			inchesWheel.setDisplayedValues(grams);
			feetText.setText("Kilo");
			inchesText.setText("Grams");

			if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE
					|| whatAreYouDTO.getWeight().length() != 0) {

				String w = whatAreYouDTO.getWeight();
				// setValue.setText(w.split("/")[0] + "Kilo" + w.split("/")[1]
				// + "Grams");
				feetWheel.setValue(Integer.valueOf(w.split("/")[0]));
				inchesWheel.setValue((Integer.valueOf(w.split("/")[1])) / 50);

			}
			setValue.setText("" + feetWheel.getValue() + "Kilo,"
					+ grams[inchesWheel.getValue()] + "Grams");
		}
		// radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// if (checkedId == R.id.radioButton1) {
		//
		// } else {
		//
		// }
		//
		// }
		// });
		OnValueChangeListener scrolledListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (titleString.equals("HEIGHT")) {
					setValue.setText("" + feetWheel.getValue() + "Feet,"
							+ inchesWheel.getValue() + "Inches");
				} else {
					setValue.setText("" + feetWheel.getValue() + "Kilo,"
							+ grams[inchesWheel.getValue()] + "Grams");
				}

			}
		};
		feetWheel.setOnValueChangedListener(scrolledListener);
		inchesWheel.setOnValueChangedListener(scrolledListener);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (titleString.equals("HEIGHT")) {
					whatAreYouDTO.setHight("" + feetWheel.getValue() + "/"
							+ inchesWheel.getValue());
					selectedHeight.setText("" + feetWheel.getValue() + "Feet,"
							+ inchesWheel.getValue() + "Inches,");
					Log.d("",
							"" + feetWheel.getValue() + "Feet,"
									+ inchesWheel.getValue() + "Inches,");
				} else {
					whatAreYouDTO.setWeight("" + feetWheel.getValue() + "/"
							+ grams[inchesWheel.getValue()]);
					Log.d("", "" + feetWheel.getValue() + "Kilo,"
							+ grams[inchesWheel.getValue()] + "Grams,");
					selectedWeight.setText("" + feetWheel.getValue() + "Kilo,"
							+ grams[inchesWheel.getValue()] + "Grams,");
				}
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

	// public void weightDialog() {
	// final String[] kg = new String[191];
	// final String[] grams = new String[19];
	//
	// for (int i = 0; i < 191; i++) {
	// if (i == 0) {
	// kg[i] = "" + 10;
	// } else {
	// kg[i] = "" + (10 + i);
	// }
	// }
	//
	// for (int i = 0; i < 19; i++) {
	// if (i == 0) {
	// grams[i] = ("" + 50);
	// } else {
	// grams[i] = ("" + ((i + 1) * 50));
	// }
	// }
	//
	// final String[] feet = { "1", "2", "3", "4", "5", "6", "7", "8" };
	// final String[] inches = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
	// "10", "11", "12" };
	// final Dialog dialog = new Dialog(context);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.dialog_enter_height);
	//
	// final WheelView kgWheel = (WheelView) dialog
	// .findViewById(R.id.feetWheel);
	// final WheelView gramsWheel = (WheelView) dialog
	// .findViewById(R.id.inchesWheel);
	//
	// kgWheel.setLabelFor(R.string.app_name);
	// Button ok = (Button) dialog.findViewById(R.id.ok);
	// Button cancle = (Button) dialog.findViewById(R.id.cancle);
	// // final TextView text = (TextView)
	// // dialog.findViewById(R.id.heightText);
	// kgWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, kg));
	// kgWheel.setVisibleItems(2);
	// kgWheel.setCurrentItem(0);
	// gramsWheel
	// .setViewAdapter(new ArrayWheelAdapter<String>(context, grams));
	// gramsWheel.setVisibleItems(2);
	// gramsWheel.setCurrentItem(0);
	// OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
	// @Override
	// public void onScrollingStarted(WheelView wheel) {
	//
	// }
	//
	// @Override
	// public void onScrollingFinished(WheelView wheel) {
	// // text.setText("" + kg[kgWheel.getCurrentItem()] + "Kilo,"
	// // + grams[gramsWheel.getCurrentItem()] + "Grams");
	// }
	// };
	// kgWheel.addScrollingListener(scrolledListener);
	// gramsWheel.addScrollingListener(scrolledListener);
	//
	// ok.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// whatAreYouDTO.setWeight("" + kg[kgWheel.getCurrentItem()]
	// + "Kilo," + grams[gramsWheel.getCurrentItem()]
	// + "Grams");
	// Log.d("", "" + kg[kgWheel.getCurrentItem()] + "Kilo,"
	// + grams[gramsWheel.getCurrentItem()] + "Grams");
	// dialog.dismiss();
	// }
	// });
	//
	// cancle.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	//
	// }
	// });
	// dialog.show();
	//
	// }

	// OnWheelChangedListener changedListener = new OnWheelChangedListener() {
	// @Override
	// public void onChanged(WheelView wheel, int oldValue, int newValue) {
	//
	// }
	// };
	// // Wheel scrolled listener
	// OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
	// @Override
	// public void onScrollingStarted(WheelView wheel) {
	// wheelScrolled = true;
	// }
	//
	// @Override
	// public void onScrollingFinished(WheelView wheel) {
	// wheelScrolled = false;
	// // updateStatus();
	// }
	// };
	//
	// // Wheel changed listener
	// private final OnWheelChangedListener changedListener = new
	// OnWheelChangedListener() {
	// @Override
	// public void onChanged(WheelView wheel, int oldValue, int newValue) {
	// Log.d("", "onChanged, wheelScrolled = " + wheelScrolled);
	// if (!wheelScrolled) {
	// // updateStatus();
	// }
	// }
	// };
	//
	// private void initWheel1(int id, String[] item) {
	// WheelView wheel = (WheelView) view.findViewById(id);
	//
	// wheel.setViewAdapter(new ArrayWheelAdapter<String>(this, wheelMenu4));
	// wheel.setVisibleItems(2);
	// wheel.setCurrentItem(0);
	// // wheel.addScrollingListener(scrolledListener);
	// }
	//
	// /**
	// * Returns wheel by Id
	// *
	// * @param id
	// * the wheel Id
	// * @return the wheel with passed Id
	// */
	// private WheelView getWheel(int id) {
	// return (WheelView) view.findViewById(id);
	// }

	// public void ageDialog() {
	// final String[] years = new String[91];
	// final String[] months = new String[12];
	//
	// for (int i = 0; i < 91; i++) {
	// if (i == 0) {
	// years[i] = "" + 10;
	// } else {
	// months[i] = "" + (10 + i);
	// }
	// }
	//
	// for (int i = 0; i < 12; i++) {
	//
	// months[i] = ("" + i);
	//
	// }
	//
	// final Dialog dialog = new Dialog(context);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.dialog_enter_height);
	//
	// final WheelView kgWheel = (WheelView) dialog
	// .findViewById(R.id.feetWheel);
	// final WheelView gramsWheel = (WheelView) dialog
	// .findViewById(R.id.inchesWheel);
	//
	// kgWheel.setLabelFor(R.string.app_name);
	// Button ok = (Button) dialog.findViewById(R.id.ok);
	// Button cancle = (Button) dialog.findViewById(R.id.cancle);
	// // final TextView text = (TextView)
	// // dialog.findViewById(R.id.heightText);
	//
	// kgWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, years));
	// kgWheel.setVisibleItems(2);
	// kgWheel.setCurrentItem(0);
	// gramsWheel
	// .setViewAdapter(new ArrayWheelAdapter<String>(context, months));
	// gramsWheel.setVisibleItems(2);
	// gramsWheel.setCurrentItem(0);
	// OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
	// @Override
	// public void onScrollingStarted(WheelView wheel) {
	//
	// }
	//
	// @Override
	// public void onScrollingFinished(WheelView wheel) {
	// // text.setText("" + years[kgWheel.getCurrentItem()] + "Years,"
	// // + months[gramsWheel.getCurrentItem()] + "Months");
	// }
	// };
	// kgWheel.addScrollingListener(scrolledListener);
	// gramsWheel.addScrollingListener(scrolledListener);
	//
	// ok.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// whatAreYouDTO.setWeight("" + years[kgWheel.getCurrentItem()]
	// + "Years," + months[gramsWheel.getCurrentItem()]
	// + "Months");
	// Log.d("", "" + years[kgWheel.getCurrentItem()] + "Years,"
	// + months[gramsWheel.getCurrentItem()] + "Months");
	// dialog.dismiss();
	// }
	// });
	//
	// cancle.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	//
	// }
	// });
	// dialog.show();
	//
	// }

	private void seProfile1Layout(View viewLayout, final ViewGroup container,
			final int position) {

		titel = (TextView) viewLayout.findViewById(R.id.titleTextView);
		titel.setText(R.string.titel_what_are_you);
		titel.setTypeface(typefaceHeader);

		group1 = (ListView) viewLayout.findViewById(R.id.list1);
		submiButton1 = (ImageButton) viewLayout
				.findViewById(R.id.submitButton1);

		final WhatAreYouListAdapter myAdapter = new WhatAreYouListAdapter(
				context, whatAreYouDataList1, 0);
		group1.setAdapter(myAdapter);

		group1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				for (int i = 0; i < whatAreYouDataList1.size(); i++) {
					if (i == pos) {
						whatAreYouDataList1.get(i).setSelected(true);

					} else {
						whatAreYouDataList1.get(i).setSelected(false);

					}
				}
				Log.d("", "position=" + pos);
				whatAreYouDTO.setWhatAreYou(""
						+ whatAreYouDataList1.get(pos).getId());
				myAdapter.notifyDataSetChanged();

			}

		});

		submiButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("".equals(whatAreYouDTO.getWhatAreYou())) {
					Toast.makeText(context,
							R.string.toast_please_select_at_least_one_choice,
							Toast.LENGTH_SHORT).show();
				} else {
					FlurryAgent.logEvent("Register - WhatAreYou1");
					((ViewPager) container).setCurrentItem(position + 1);
				}

			}
		});

	}

	private void setRegistrationLayout(View viewLayout,
			final ViewGroup container, final int position) {
		titel = (TextView) viewLayout.findViewById(R.id.titleTextView);
		if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
			titel.setText("Update Your Profile");
		} else {

			titel.setText(R.string.titel_create_your_account);
		}
		titel.setTypeface(typefaceHeader);
		// backButton = (ImageView) viewLayout.findViewById(R.id.back_button);
		languageSpinner = (Spinner) viewLayout.findViewById(R.id.spnLang);
		languageSpinner.setEnabled(false);
		languageSpinner.setClickable(false);
		langaugeButton = (Button) viewLayout.findViewById(R.id.selectLanguage);
		submitButton = (Button) viewLayout.findViewById(R.id.done_button);
		firstName = (EditText) viewLayout.findViewById(R.id.first_name);
		lastName = (EditText) viewLayout.findViewById(R.id.last_name);
		email = (EditText) viewLayout.findViewById(R.id.email);
		mobile = (EditText) viewLayout.findViewById(R.id.mobile_no);
		termAndConditionImageView = (ImageView) viewLayout
				.findViewById(R.id.acceptImageView);
		accept = (TextView) viewLayout.findViewById(R.id.accept);
		acceptTermAnsConditionTextView = (TextView) viewLayout
				.findViewById(R.id.termAndCondition);
		acceptTermAnsConditionTextView.setText(Html.fromHtml("<u>"
				+ context.getResources().getString(
						R.string.registration_term_and_conditions) + "</u>"));
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(10);
		mobile.setFilters(filterArray);
		password = (EditText) viewLayout.findViewById(R.id.password);
		confirmPassword = (EditText) viewLayout
				.findViewById(R.id.confirmPassword);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		firstName.setTypeface(typeface);
		lastName.setTypeface(typeface);
		mobile.setTypeface(typeface);
		email.setTypeface(typeface);
		password.setTypeface(typeface);
		confirmPassword.setTypeface(typeface);
		accept.setTypeface(typeface);
		acceptTermAnsConditionTextView.setTypeface(typeface);
		submitButton.setTypeface(typeface);

		setUserDto();
		whatAreYouDTO = userDTO.getWhatAreYouDTO();
		if (AppConstants.ENGLISH == userDTO.getLanguage()) {
			langaugeButton.setBackgroundResource(R.drawable.eng_select);
		} else {
			langaugeButton.setBackgroundResource(R.drawable.spn_select);
		}

		if (isTermAnsConditionAccept) {
			termAndConditionImageView.setBackgroundResource(R.drawable.check);
		} else {
			termAndConditionImageView.setBackgroundResource(R.drawable.uncheck);
		}
		termAndConditionImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isTermAnsConditionAccept) {
					termAndConditionImageView
							.setBackgroundResource(R.drawable.uncheck);
					isTermAnsConditionAccept = false;
				} else {
					termAndConditionImageView
							.setBackgroundResource(R.drawable.check);
					isTermAnsConditionAccept = true;
				}
			}
		});
		if (intentValue == AppConstants.COME_FROM_FACEBOOK) {
			firstName.setText(graphUser.getFirstName());
			lastName.setText(graphUser.getLastName());
			email.setText(graphUser.getProperty("email").toString());
			// mobile.setText(graphUser.getProperty(propertyName));

		}
		if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {

			firstName.setText(userDTO.getFirstName());

			// Edited on revision 601
			// MAY 18 2015
			// commented
			// lastName.setText(userDTO.getLastName());
			// added
			if (userDTO.getLastName() != null
					|| userDTO.getLastName() != "null") {
				lastName.setText(userDTO.getLastName());
			} else {
				lastName.setText("");
			}

			email.setText(userDTO.getEmail());
			email.setEnabled(false);
			mobile.setText(userDTO.getMobile());
			password.setText("aa");
			confirmPassword.setText("aa");
			password.setVisibility(View.INVISIBLE);
			confirmPassword.setVisibility(View.INVISIBLE);
		}
		// backButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// context.finish();
		//
		// }
		// });
		langaugeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.ENGLISH == userDTO.getLanguage()) {
					userDTO.setLanguage(AppConstants.SPANISH);
					langaugeButton.setBackgroundResource(R.drawable.spn_select);
				} else {
					userDTO.setLanguage(AppConstants.ENGLISH);
					langaugeButton.setBackgroundResource(R.drawable.eng_select);
				}
				Log.d("", "language=" + userDTO.getLanguage());
			}

		});

		confirmPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int actionId,
					KeyEvent arg2) {
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				startRegistrationProcess(container, position);
				return false;
			}
		});

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startRegistrationProcess(container, position);

			}
		});
		acceptTermAnsConditionTextView
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								Activity_TermAndCondition.class);
						context.startActivity(intent);

					}
				});
		// ((ViewPager) container)
		// .setOnPageChangeListener(new OnPageChangeListener() {
		//
		// @Override
		// public void onPageSelected(int arg0) {
		// if (arg0 == 0) {
		// startRegistrationProcess(container, position);
		// }
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		//
		// }
		//
		// @Override
		// public void onPageScrollStateChanged(int arg0) {
		//
		// }
		// });

	}

	private void startRegistrationProcess(ViewGroup container, int position) {

		if (0 == firstName.getText().toString().trim().length()) {
			Toast.makeText(context, R.string.toast_enter_user_name,
					Toast.LENGTH_SHORT).show();
		}
		// else if (0 == lastName.getText().toString().trim().length()) {
		// Toast.makeText(context, "Enter Last name", Toast.LENGTH_SHORT)
		// .show();
		// }

		else if ((email.getText().toString().trim().length() == 0)) {
			Toast.makeText(context, R.string.toast_enter_email_id,
					Toast.LENGTH_SHORT).show();
		} else if (!Utils.isValidEmail(email.getText().toString().trim())) {
			Toast.makeText(context, R.string.toast_enter_correct_email_id,
					Toast.LENGTH_SHORT).show();
		} else if (0 == mobile.getText().toString().trim().length()) {
			Toast.makeText(context, R.string.toast_enter_mobile_number,
					Toast.LENGTH_SHORT).show();
		} else if (mobile.getText().toString().trim().length() < 10) {
			Toast.makeText(
					context,
					R.string.toast_please_enter_at_least_10_digit_mobile_number,
					Toast.LENGTH_SHORT).show();
		}

		else if (password.getText().toString().trim().length() == 0) {
			Toast.makeText(context, R.string.toast_enter_password,
					Toast.LENGTH_SHORT).show();
		} else if (password.getText().toString().trim().length() < 6
				&& intentValue != AppConstants.COME_FROM_UPDATE_PROFILE) {
			Toast.makeText(context,
					R.string.toast_password_at_least_6_character_long,
					Toast.LENGTH_SHORT).show();
		} else if (0 == confirmPassword.getText().toString().trim().length()) {
			Toast.makeText(context, R.string.toast_enter_confirm_password,
					Toast.LENGTH_SHORT).show();
		} else if (!password.getText().toString()
				.equals(confirmPassword.getText().toString())) {
			Toast.makeText(
					context,
					R.string.toast_password_and_confirm_password_should_be_same,
					Toast.LENGTH_SHORT).show();
		} else if (isTermAnsConditionAccept) {

			// setUserDto();
			userDTO.setFirstName(firstName.getText().toString());

			// edited on revision 601
			// MAY 18 2015
			// dl commented
			// userDTO.setLastName(lastName.getText().toString());
			// added
			if (lastName.getText().toString() != null
					|| lastName.getText().toString() != "null") {
				userDTO.setLastName(lastName.getText().toString());
			} else {
				userDTO.setLastName("");
			}
			// - - - - -
			userDTO.setEmail(email.getText().toString());
			userDTO.setMobile(mobile.getText().toString());
			if (intentValue != AppConstants.COME_FROM_UPDATE_PROFILE) {
				userDTO.setPassword(password.getText().toString());
				userDTO.setConfirmPassword(confirmPassword.getText().toString());
				new CheckUserAvailabilityAsynchTask()
						.execute(WebServiceConstants.BASE_URL
								+ WebServiceConstants.CHECK_FACEBOOK_LOGIN_STATUS);
				FlurryAgent.logEvent("Register - CreateAccount"); // Edited
																	// revision
																	// 582
																	// commented
			} else {
				((ViewPager) container).setCurrentItem(position + 1);
			}

			// if (intentValue == AppConstants.COME_FROM_FACEBOOK) {
			//
			// new RegistrationAsynchTask()
			// .execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.USER_REGISTRATION);
			//
			// } else {

			// PagerViewAdapter.userDTO = userDTO;
			// Intent intent = new Intent(Activity_Registration.this,
			// Activity_ProfileDetails.class);
			// startActivity(intent);
			// finish();
			// }

		} else {
			Toast.makeText(context, R.string.toast_accept_term_and_conditions,
					Toast.LENGTH_SHORT).show();
		}

	}

	private void setUserDto() {
		if (intentValue != AppConstants.COME_FROM_UPDATE_PROFILE) {

			// userDTO = new UserDTO();
			// userDTO.setProfileImage(picturePath);
			firstName.setText(userDTO.getFirstName());

			// edited on revision 601
			// MAY 18 2015
			// dl commented
			// lastName.setText(userDTO.getLastName());
			if (userDTO.getLastName() != null
					|| userDTO.getLastName() != "null") {
				lastName.setText(userDTO.getLastName());
			} else {
				lastName.setText("");
			}

			// - - - -

			email.setText(userDTO.getEmail());
			mobile.setText(userDTO.getMobile());
			password.setText(userDTO.getPassword());
			confirmPassword.setText(userDTO.getConfirmPassword());
			// .setText(userDTO.getIsFirstTime());
			// WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
			// whatAreYouDTO.setWhatAreYou("");
			// whatAreYouDTO.setFeet("");
			// whatAreYouDTO.setInches("");
			// whatAreYouDTO.setMeters("");
			// whatAreYouDTO.setWeight("");
			// whatAreYouDTO.setAge("");
			// whatAreYouDTO.setRelationshipStatus("");
			// whatAreYouDTO.setWhatDoYouKrave("");
			// whatAreYouDTO.setInterest("");
			// userDTO.setWhatAreYouDTO(whatAreYouDTO);
		}

	}

	private void seProfile4Layout(View viewLayout, final ViewGroup container,
			final int position) {
		titel = (TextView) viewLayout.findViewById(R.id.titleTextView);
		// titel.setText("INTERESTS");
		titel.setText(R.string.titel_create_your_account);
		titel.setTypeface(typefaceHeader);
		gridViewInterest = (GridView) viewLayout.findViewById(R.id.gridView);
		facebookLikesIntegration = (ImageView) viewLayout
				.findViewById(R.id.facebookLikeIntegration);
		facebookLikesIntegration.setVisibility(View.GONE);
		submiButton4 = (Button) viewLayout.findViewById(R.id.submitButton4);

		TextView woudYouLike = (TextView) viewLayout
				.findViewById(R.id.textView1);
		TextView facebookLikeTextView = (TextView) viewLayout
				.findViewById(R.id.facebookLikeTextView);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		woudYouLike.setTypeface(typeface);
		facebookLikeTextView.setTypeface(typeface);
		submiButton4.setTypeface(typeface);

		woudYouLike.setVisibility(View.GONE);
		facebookLikeTextView.setVisibility(View.GONE);

		if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
			for (int i = 0; i < interestList.size(); i++) {
				if (interestList.get(i).getIsSelected()) {
					selectedInterest.add(interestList.get(i));
				}
			}

		}

		mGridViewAdapter = new GridViewAdapter((Activity) context,
				(ArrayList<InterestsDTO>) interestList);
		gridViewInterest.setAdapter(mGridViewAdapter);

		gridViewInterest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				InterestsDTO interestsDTO = interestList.get(position);

				if (interestsDTO.getIsSelected()) {
					selectedInterest.remove(interestsDTO);
					interestsDTO.setIsSelected(false);
				} else {
					interestsDTO.setIsSelected(true);
					selectedInterest.add(interestsDTO);
				}

				mGridViewAdapter.notifyDataSetChanged();
			}
		});

		submiButton4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// String interest = "";
				// for (int j = 0; j < selectedInterest.size(); j++) {
				// interest = interest
				// + selectedInterest.get(j).getInterestId() + ",";
				// }
				// // whatAreYouDTO.setInterest(interest);
				if (selectedInterest.size() == 0) {
					Toast.makeText(context,
							R.string.toast_select_at_least_one_interest,
							Toast.LENGTH_SHORT).show();
					FlurryAgent.logEvent("Register - NoInterest"); // Edited
																	// revision
																	// 582
																	// commented
				} else {
					FlurryAgent.logEvent("Register - Interests"); // Edited
																	// revision
																	// 582
																	// commented
					((ViewPager) container).setCurrentItem(position + 1);
				}
			}
		});
		/* ENABLE FACEBOOK INTEGRATION */
		// if (userDTO.isFacebookLikeEnable()) {
		// facebookLikesIntegration.setBackgroundDrawable(context
		// .getResources().getDrawable(R.drawable.check));
		// } else {
		// facebookLikesIntegration.setBackgroundDrawable(context
		// .getResources().getDrawable(R.drawable.uncheck));
		// }

		// facebookLikesIntegration.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// if (userDTO.isFacebookLikeEnable()) {
		// // facebookLikesIntegration.setBackgroundDrawable(context
		// // .getResources().getDrawable(R.drawable.uncheck));
		// // userDTO.setFacebookLikeEnable(false);
		// compareFacebookInterest(false);
		// } else {
		// // facebookLikesIntegration.setBackgroundDrawable(context
		// // .getResources().getDrawable(R.drawable.check));
		// // userDTO.setFacebookLikeEnable(true);
		// if (intentValue != AppConstants.COME_FROM_FACEBOOK) {
		//
		// context.startFacebookIntegration();
		//
		// } else {
		// compareFacebookInterest(true);
		// }
		//
		// }
		//
		// // notifyDataSetChanged();
		//
		// }
		// }); // {
		/* ====END==== */
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// if (isChecked) {
		// Log.d("", "checked");
		// radioGroup.clearCheck();
		// activateFacebookLikes = true;
		// if (intentValue != AppConstants.COME_FROM_FACEBOOK) {
		//
		// context.startFacebookIntegration();
		// }
		//
		// } else {
		// Log.d("", "UnChecked");
		// facebookLikesIntegration.setChecked(true);
		// activateFacebookLikes = false;
		// }
		//
		// }
		// });
		// facebookLikesIntegration.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if(facebookLikesIntegration.isChecked()){
		// facebookLikesIntegration.setChecked(false);
		// }else{
		// facebookLikesIntegration.setChecked(true);
		// }
		//
		// }
		// });
	}

	public void compareFacebookInterest(boolean check) {
		Log.d("", "compareFacebookInterest");
		try {
			for (int i = 0; i < interestList.size(); i++) {
				InterestsDTO dto = interestList.get(i);
				String name = dto.getInterestName();
				// String category = facebookLikesDTOs.get(i).getCategory();

				for (int j = 0; j < facebookLikesDTOs.size(); j++) {
					String category = facebookLikesDTOs.get(j).getCategory()
							.equals("Musician/band") ? "music"
							: (facebookLikesDTOs.get(j).getCategory());
					if (category.equals(name.toLowerCase())) {
						Log.d("", "compareFacebookInterest=======true");

						if (check) {
							facebookLikesIntegration
									.setBackgroundDrawable(context
											.getResources().getDrawable(
													R.drawable.check));
							userDTO.setFacebookLikeEnable(true);
							dto.setIsSelected(true);
							selectedInterest.add(dto);
						} else {
							facebookLikesIntegration
									.setBackgroundDrawable(context
											.getResources().getDrawable(
													R.drawable.uncheck));
							userDTO.setFacebookLikeEnable(false);
							dto.setIsSelected(false);
							selectedInterest.remove(dto);
						}
						break;
					}
				}

			}

			mGridViewAdapter.notifyDataSetChanged();
		} catch (Exception exe) {

		}
	}

	Button galleryView, cameraView;
	LinearLayout cancleLayout, deleteLayout;
	RelativeLayout pictureLayout;

	private void seProfile5Layout(View viewLayout) {
		titel = (TextView) viewLayout.findViewById(R.id.titleTextView);
		// titel.setText("PROFILE");
		titel.setText(R.string.titel_create_your_account);
		titel.setTypeface(typefaceHeader);

		profilePick1 = (ImageView) viewLayout.findViewById(R.id.profileImage1);
		profilePick2 = (ImageView) viewLayout.findViewById(R.id.profileImage2);
		profilePick3 = (ImageView) viewLayout.findViewById(R.id.profileImage3);
		profilePick4 = (ImageView) viewLayout.findViewById(R.id.profileImage4);
		profilePick5 = (ImageView) viewLayout.findViewById(R.id.profileImage5);
		profilePick6 = (ImageView) viewLayout.findViewById(R.id.profileImage6);
		profilePick1Lock = (ImageView) viewLayout
				.findViewById(R.id.profileImage1Lock);
		profilePick2Lock = (ImageView) viewLayout
				.findViewById(R.id.profileImage2Lock);
		profilePick3Lock = (ImageView) viewLayout
				.findViewById(R.id.profileImage3Lock);
		profilePick4Lock = (ImageView) viewLayout
				.findViewById(R.id.profileImage4Lock);
		profilePick5Lock = (ImageView) viewLayout
				.findViewById(R.id.profileImage5Lock);
		profilePick6Lock = (ImageView) viewLayout
				.findViewById(R.id.profileImage6Lock);
		aboutMe = (EditText) viewLayout.findViewById(R.id.aboutMe);

		submitButton5 = (Button) viewLayout.findViewById(R.id.submitButton5);

		galleryView = (Button) viewLayout.findViewById(R.id.gallery);
		cameraView = (Button) viewLayout.findViewById(R.id.camera);
		cancleLayout = (LinearLayout) viewLayout
				.findViewById(R.id.cancleLayout);
		deleteLayout = (LinearLayout) viewLayout
				.findViewById(R.id.deleteLayout);
		pictureLayout = (RelativeLayout) viewLayout
				.findViewById(R.id.imageLayout);

		TextView cancleTextView, addPictureTextView, deleteTextView;

		cancleTextView = (TextView) viewLayout
				.findViewById(R.id.cancleTextView);
		deleteTextView = (TextView) viewLayout
				.findViewById(R.id.deleteTextView);
		addPictureTextView = (TextView) viewLayout.findViewById(R.id.textView2);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

		aboutMe.setTypeface(typeface);
		cancleTextView.setTypeface(typeface);
		deleteTextView.setTypeface(typeface);
		addPictureTextView.setTypeface(typeface);

		submitButton5.setTypeface(typeface);

		pictureLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		cancleLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pictureLayout.setVisibility(View.GONE);

			}
		});

		deleteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pictureLayout.setVisibility(View.GONE);
				if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
					if (context.imagePosition == 0
							&& context.picturesPathArray[0]
									.equals(AppConstants.FACEBOOK_IMAGE)) {
						context.picturesPathArray[0] = "a";
						userDTO.getUserProfileImageDTOs().remove(
								context.imagePosition);
					} else {
						if (context.picturesPathArray[context.imagePosition]
								.equals("url")) {
							if ((userDTO.getUserProfileImageDTOs().size() - 1) >= context.imagePosition) {
								context.delete[context.imagePosition] = userDTO
										.getUserProfileImageDTOs()
										.get(context.imagePosition)
										.getImageId();
							}
							userDTO.getUserProfileImageDTOs().remove(
									context.imagePosition);
						}

					}

					context.picturesPathArray[userDTO.getUserProfileImageDTOs()
							.size()] = "a";
				} else {

					context.picturesPathArray[context.imagePosition] = "a";
				}
				context.profilePick.setImageResource(R.drawable.camera_200);
			}
		});
		profilePick1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				context.imagePosition = 0;
				context.profilePick = profilePick1;
				context.profilePickLock = profilePick1Lock;
				openDailogForProfileImage();
			}
		});
		profilePick2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.imagePosition = 1;
				context.profilePick = profilePick2;
				context.profilePickLock = profilePick2Lock;
				openDailogForProfileImage();

			}
		});
		profilePick3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.imagePosition = 2;
				context.profilePick = profilePick3;
				context.profilePickLock = profilePick3Lock;
				openDailogForProfileImage();

			}
		});
		profilePick4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.imagePosition = 3;
				context.profilePick = profilePick4;
				context.profilePickLock = profilePick4Lock;
				openDailogForProfileImage();

			}
		});
		profilePick5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.imagePosition = 4;
				context.profilePick = profilePick5;
				context.profilePickLock = profilePick5Lock;
				openDailogForProfileImage();

			}
		});
		profilePick6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.imagePosition = 5;
				context.profilePick = profilePick6;
				context.profilePickLock = profilePick6Lock;
				openDailogForProfileImage();

			}
		});
		// http://graph.facebook.com/100001387759280/picture?type=large
		ImageView profileImageViewArray[] = { profilePick1, profilePick2,
				profilePick3, profilePick4, profilePick5, profilePick6 };
		ImageView profileImageViewLockArray[] = { profilePick1Lock,
				profilePick2Lock, profilePick3Lock, profilePick4Lock,
				profilePick5Lock, profilePick6Lock };

		if (intentValue == AppConstants.COME_FROM_FACEBOOK
				|| userDTO.isFacebookLikeEnable()) {
			if (graphUser != null) {
				String facebookImageUrl = "https://graph.facebook.com/"
						+ graphUser.getId() + "/picture?type=large";
				userDTO.setProfileImage(facebookImageUrl);
				profilePick1.setScaleType(ScaleType.FIT_CENTER);
				imageLoader.DisplayImage(facebookImageUrl, profilePick1);
				context.picturesPathArray[0] = AppConstants.FACEBOOK_IMAGE;
				Log.d("", "facebook image url ="
						+ "https://graph.facebook.com/" + graphUser.getId()
						+ "/picture?type=large");
			} else {
				Log.d("", "graph user is null");
			}
			// http://graph.facebook.com/564388043679623/picture?
		}
		if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {

			loadProfileImagesFromURl(profileImageViewArray);

		} // else {
		for (int i = 0; i < context.picturesPathArray.length; i++) {
			if (!("a".equals(context.picturesPathArray[i]))
					&& !(AppConstants.UN_UPDATED_IMAGE
							.equals(context.picturesPathArray[i])))
				if (!(AppConstants.FACEBOOK_IMAGE
						.equals(context.picturesPathArray[i]))) {

					Log.d("", "in the loop path picture array");
					Bitmap bitmap = BitmapFactory
							.decodeFile(context.picturesPathArray[i]);
					profileImageViewArray[i].setImageBitmap(bitmap);

				}
			if (context.picturesPathArrayIsPublic[i]) {
				profileImageViewLockArray[i].setVisibility(View.GONE);
			} else {
				profileImageViewLockArray[i].setVisibility(View.VISIBLE);
			}
		}
		// }

		aboutMe.setText(userDTO.getAboutMe());
		submitButton5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String enter = context.getResources().getString(
						R.string.toast_enter);
				userDTO.setAboutMe(aboutMe.getText().toString());
				if (!enter.equals(checkValidation())) {
					Toast.makeText(context, checkValidation(),
							Toast.LENGTH_SHORT).show();

					FlurryAgent.logEvent("Register - NoAboutMe"); // Edited
																	// revision
																	// 582
																	// commented
				} else {

					if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
						if (WebServiceConstants.isOnline(context)) {
							new UpdateProfileAsynchTask()
									.execute(WebServiceConstants.BASE_URL
											+ WebServiceConstants.USER_PROFILE_UPDATE);
						}
					} else {
						boolean check = true;
						// for (int i = 0; i < 6; i++) {
						// if (!"a".equals(context.picturesPathArray[0])) {
						// check = false;
						// // break;
						// }
						// // }
						// if (check) {
						// Toast.makeText(context,
						// R.string.toast_please_add_profile_picture,
						// Toast.LENGTH_SHORT).show();
						//
						// FlurryAgent.logEvent("Register - NoProfilePic"); //
						// Edited
						// // revision
						// // 582
						// // commented
						// } else {
						if (WebServiceConstants.isOnline(context)) {
							context.easyTrackerEventLog("Registered_UserAge_"
									+ context.singleton
											.calculateAge(whatAreYouDTO
													.getAge()));
							new RegistrationAsynchTask()
									.execute(WebServiceConstants.BASE_URL_FILE
											+ WebServiceConstants.USER_REGISTRATION);
							new AvRegUserAsynch()
									.execute(WebServiceConstants.AV_INSERT_PROFILE_DATA);
							FlurryAgent.logEvent("Register - Done"); // Edited
																		// revision
																		// 582
																		// commented
						}
						// }
					}
				}
			}

		});

	}

	class AvRegUserAsynch extends AsyncTask<String, Void, JSONObject> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected JSONObject doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_email", ""
					+ userDTO.getEmail()));

			JSONObject json = new JSONParser().makeHttpRequest2(args[0],
					"POST", params);

			// Log.d("login response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonArray) {
			super.onPostExecute(jsonArray);

		}
	}

	public void deleteImage() {
		pictureLayout.setVisibility(View.GONE);
		if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
			if (context.imagePosition == 0
					&& context.picturesPathArray[0]
							.equals(AppConstants.FACEBOOK_IMAGE)) {
				context.picturesPathArray[0] = "a";
				userDTO.getUserProfileImageDTOs().remove(context.imagePosition);
			} else {
				if (context.picturesPathArray[context.imagePosition]
						.equals("url")) {
					if ((userDTO.getUserProfileImageDTOs().size() - 1) >= context.imagePosition) {
						context.delete[context.imagePosition] = userDTO
								.getUserProfileImageDTOs()
								.get(context.imagePosition).getImageId();
					}
					userDTO.getUserProfileImageDTOs().remove(
							context.imagePosition);
				}

			}

			context.picturesPathArray[userDTO.getUserProfileImageDTOs().size()] = "a";
		} else {

			context.picturesPathArray[context.imagePosition] = "a";
		}
		context.profilePick.setImageResource(R.drawable.camera_200);
	}

	private String checkValidation() {

		String validationCheck = context.getResources().getString(
				R.string.toast_enter);

		if (0 == userDTO.getFirstName().toString().trim().length()) {
			validationCheck = validationCheck
					+ context.getResources().getString(
							R.string.toast_space_user_name);
		}
		// if (0 == userDTO.getLastName().toString().trim().length()) {
		// validationCheck = validationCheck + " Last name,";
		// }
		if (0 == mobile.getText().toString().trim().length()) {
			validationCheck = validationCheck
					+ context.getResources().getString(
							R.string.toast_space_mobile_number);
		} else if (mobile.getText().toString().trim().length() < 10) {
			validationCheck = validationCheck
					+ context
							.getResources()
							.getString(
									R.string.toast_space_at_least_10_digit_mobile_number);
		}
		// else if (mobile.getText().toString().trim().length() < 10) {
		// validationCheck = validationCheck + "mobi,";
		// }
		if ((email.getText().toString().trim().length() == 0)) {
			validationCheck = validationCheck
					+ context.getResources().getString(
							R.string.toast_space_email_id);
		} else

		if (!Utils.isValidEmail(userDTO.getEmail().toString().trim())) {
			validationCheck = validationCheck
					+ context.getResources().getString(
							R.string.toast_space_correct_email_id);
		}
		if (intentValue != AppConstants.COME_FROM_UPDATE_PROFILE) {
			if (0 == userDTO.getPassword().toString().trim().length()) {
				validationCheck = validationCheck
						+ context.getResources().getString(
								R.string.toast_space_password);
			} else if (password.getText().toString().trim().length() < 6) {
				Toast.makeText(context,
						R.string.toast_password_must_be_6_character_long,
						Toast.LENGTH_SHORT).show();
			} else if (0 == userDTO.getConfirmPassword().toString().trim()
					.length()) {
				validationCheck = validationCheck
						+ context.getResources().getString(
								R.string.toast_space_confirm_password);
			} else if (!userDTO.getConfirmPassword().toString()
					.equals(userDTO.getConfirmPassword().toString())) {

				validationCheck = validationCheck
						+ context
								.getResources()
								.getString(
										R.string.toast_space_password_and_confirm_password_are_not_same);

			}
		}
		if ("".equals(whatAreYouDTO.getWhatAreYou())) {
			validationCheck = validationCheck
					+ context.getResources().getString(
							R.string.toast_space_what_are_you);
		}
		if ("".equals(whatAreYouDTO.getHight())) {
			// validationCheck = validationCheck + " Height,";
			whatAreYouDTO.setHight("" + 0 + "/" + 0);
		}
		if ("".equals(whatAreYouDTO.getWeight())) {
			// validationCheck = validationCheck + " Weight,";
			// whatAreYouDTO.setWeightUnit(AppConstants.WEIGHT_IN_POUND);
			whatAreYouDTO.setWeight("" + 0 + "/" + 0);
		}
		if ("".equals(whatAreYouDTO.getAge())) {
			validationCheck = validationCheck
					+ context.getResources()
							.getString(R.string.toast_space_age);
		}
		// if (validationRelationshipStatus
		// && (intentValue != AppConstants.COME_FROM_UPDATE_PROFILE)) {
		// validationCheck = validationCheck + " Relationship status,";
		// }
		if (selectedInterest.size() == 0) {
			validationCheck = validationCheck
					+ context.getResources().getString(
							R.string.toast_space_interest);
		}
		// if (aboutMe.getText().toString().trim().length() == 0) {
		// validationCheck = validationCheck
		// + context.getResources().getString(
		// R.string.toast_space_about_me);
		// }
		if (!isTermAnsConditionAccept) {
			validationCheck = validationCheck
					+ context.getResources().getString(
							R.string.toast_space_accept_term_and_conditions);
		}
		return validationCheck;

	}

	private void loadProfileImagesFromURl(ImageView[] profileImageViewArray) {
		int i = 0;
		for (i = 0; i < userDTO.getUserProfileImageDTOs().size(); i++) {
			if (AppConstants.FACEBOOK_IMAGE.equals(userDTO
					.getUserProfileImageDTOs().get(i).getImageId())
					&& (i == 0)) {
				imageLoader
						.DisplayImage(userDTO.getUserProfileImageDTOs().get(i)
								.getImagePath(), profileImageViewArray[i]);
				context.picturesPathArray[i] = AppConstants.FACEBOOK_IMAGE;
			} else {
				imageLoader
						.DisplayImage(userDTO.getUserProfileImageDTOs().get(i)
								.getImagePath(), profileImageViewArray[i]);
				context.picturesPathArray[i] = AppConstants.UN_UPDATED_IMAGE;
			}

		}
		// for (int j = i; j < 6; j++) {
		// context.picturesPathArray[j] = "a";
		// }
	}

	public void openDailog(final int id, String title, String hint) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_enter_value);

		TextView titleTextView = (TextView) dialog
				.findViewById(R.id.titleTextView);
		final EditText editText = (EditText) dialog.findViewById(R.id.editText);
		ImageButton button = (ImageButton) dialog.findViewById(R.id.button);

		titleTextView.setText(title);
		editText.setHint(hint);
		// if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
		switch (id) {
		case 1:
			editText.setText(whatAreYouDTO.getHight());
			break;

		case 4:
			editText.setText(whatAreYouDTO.getWeight());
			break;
		case 5:
			editText.setText(whatAreYouDTO.getAge());
			break;

		default:
			break;
		}

		// }

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mValue = editText.getText().toString();
				if ("".equals(mValue)) {
					dialog.dismiss();
				} else {

					switch (id) {
					case 1:

						if (Long.valueOf(mValue) >= 121
								&& Long.valueOf(mValue) <= 304) {
							whatAreYouDTO.setHight("" + mValue);
							dialog.dismiss();
						} else {
							Toast.makeText(context,
									R.string.toast_enter_valid_height,
									Toast.LENGTH_SHORT).show();
						}
						break;

					case 4:
						if (Long.valueOf(mValue) >= 20
								&& Long.valueOf(mValue) <= 160) {
							whatAreYouDTO.setWeight("" + mValue);
							dialog.dismiss();
						} else {
							Toast.makeText(context,
									R.string.toast_enter_valid_weight,
									Toast.LENGTH_SHORT).show();
						}

						break;
					case 5:
						if (Long.valueOf(mValue) >= 14
								&& Long.valueOf(mValue) <= 100) {
							whatAreYouDTO.setAge("" + mValue);
							dialog.dismiss();
						} else {
							Toast.makeText(context,
									R.string.toast_enter_valid_age,
									Toast.LENGTH_SHORT).show();
						}
						break;

					default:
						break;
					}
				}
			}
		});

		dialog.show();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);

	}

	class RegistrationAsynchTask extends AsyncTask<String, Void, String> {

		// heightUnit
		// weightUnit

		private JSONArray jsonArray;
		private JSONObject jsonObject;
		private String vStatus;
		private TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected String doInBackground(String... args) {
			String vResult = "";
			String Temp;
			try {
				String tempWhatDoYouString = "";
				for (int i = 0; i < selectedWhatDoYouKrave.size(); i++) {
					tempWhatDoYouString = tempWhatDoYouString
							+ selectedWhatDoYouKrave.get(i).getId() + ",";
				}
				whatAreYouDTO.setWhatDoYouKrave(tempWhatDoYouString);

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(args[0]);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				if (intentValue == AppConstants.COME_FROM_FACEBOOK) {
					Temp = "facebook";
				} else {
					Temp = "general";
				}
				reqEntity.addPart("lat",
						new StringBody("" + gpsTracker.getLatitude()));
				reqEntity.addPart("lon",
						new StringBody("" + gpsTracker.getLongitude()));
				reqEntity.addPart("language",
						new StringBody(userDTO.getLanguage()));
				reqEntity.addPart("user_type", new StringBody(Temp));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));

				// edited on revision 601
				// MAY 18 2015
				// dl commented
				// reqEntity.addPart("lname", new
				// StringBody(userDTO.getLastName()
				// .toString()));
				if (userDTO.getLastName().toString() != null
						|| userDTO.getLastName().toString() != "null") {
					reqEntity.addPart("lname", new StringBody(userDTO
							.getLastName().toString()));
				} else {
					reqEntity.addPart("lname", new StringBody(""));
				}
				// - - - - - -

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

				if (context.picturesPathArray[0]
						.equals(AppConstants.FACEBOOK_IMAGE)) {
					reqEntity.addPart("user_facebook_image", new StringBody(
							userDTO.getProfileImage()));
				} else {
					reqEntity.addPart("user_facebook_image", new StringBody(
							"url"));
				}

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
				// reqEntity.addPart("thumb_image",
				// new FileBody(new File(userDTO.getProfileImage())));

				for (int i = 0; i < context.picturesPathArray.length; i++) {
					System.out.println("AddItemActivity.arrayList.get(i)"
							+ context.picturesPathArray[i]);
					if (!"a".equals(context.picturesPathArray[i])) {
						if (!context.picturesPathArray[i]
								.equals(AppConstants.FACEBOOK_IMAGE)) {
							Log.d("IMAGE", "IMAGE POSITION =" + i);
							Log.d("IMAGE", "ispublic ="
									+ context.picturesPathArrayIsPublic[i]);
							file = new File(context.picturesPathArray[i]);
							FileBody bin = new FileBody(file);
							reqEntity.addPart("thumb_image" + i, bin);
							reqEntity
									.addPart(
											"is_public" + i,
											new StringBody(
													context.picturesPathArrayIsPublic[i] ? (AppConstants.IMAGE_PUBLIC)
															: (AppConstants.IMAGE_PRIVATE)));

						}
					}
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
			} catch (Exception e) {
				Log.v("Message...3", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			}
			return vResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			System.out.println("Responce....Admin Reg.String" + result);
			// [{"status":"Success","admin_unique_id":"admin4"}]
			try {
				jsonArray = new JSONArray(result);

				jsonObject = jsonArray.getJSONObject(0);
				vStatus = jsonObject.getString("status");
				if (vStatus.equals("success")) {

					// Toast.makeText(context,
					// "Please check your email Inbox/Spam folder and activate your account.",
					// Toast.LENGTH_SHORT).show();
					Toast.makeText(context,
							R.string.toast_registration_successfull,
							Toast.LENGTH_SHORT).show();
					// UserDTO userDTO =
					// parseUserDataAndSetInSession(jsonObject);
					// if (intentValue == AppConstants.COME_FROM_FACEBOOK) {
					// Toast.makeText(context,
					// R.string.toast_registration_successfull,
					// Toast.LENGTH_SHORT).show();
					// Intent mIntent = new Intent(context,
					// Activity_Home.class);
					// mIntent.putExtra("open", AppConstants.FRAGMENT_HOME);
					// context.startActivity(mIntent);
					// sessionManager.setUserDetail(userDTO);
					// context.finish();
					// } else {
					// Toast.makeText(
					// context,
					// R.string.toast_please_check_your_email_inbox_spam_folder_and_activate_your_account,
					// Toast.LENGTH_SHORT).show();
					// Intent mIntent = new Intent(context,
					// Activity_Login.class);
					// context.startActivity(mIntent);
					// context.finish();
					// }
					new LoginAsynchTask().execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.USER_LOGIN);
					new XmppRegistrationAsynchTask()
							.execute(WebServiceConstants.XMPP_USER_REGISTRATION);
				} else if (vStatus.equals("alreadyexist")) {
					Toast.makeText(context, R.string.toast_user_already_exist,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, R.string.toast_registration_failed,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class LoginAsynchTask extends AsyncTask<String, Void, JSONArray> {
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
			params.add(new BasicNameValuePair("email", userDTO.getEmail()
					.toString()));
			params.add(new BasicNameValuePair("password", userDTO.getPassword()
					.toString()));
			params.add(new BasicNameValuePair("GCMID", singleton.gcmRegId));
			params.add(new BasicNameValuePair("devicetype", "android"));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("login response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("login response :" + jsonArray);
				if (vStatus.equals("success") || vStatus.equals("unverified")) {

					parseUserDataAndSetInSession(mJsonObject);
					new InsertActivityLog()
							.execute(
									WebServiceConstants.AV_BASE_URL
											+ WebServiceConstants.AV_INSERT_ACTIVITY_LOG,
									"Login", ""
											+ sessionManager.getUserDetail()
													.getUserID());
				}
				// else if (vStatus.equals("unverified")) {
				// Toast.makeText(
				// context,
				// R.string.toast_your_email_id_is_not_verified_please_verify_email_id,
				// Toast.LENGTH_SHORT).show();
				// }
				else if (vStatus.equals("failure")) {
					Toast.makeText(
							context,
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

	// private UserDTO parseUserDataAndSetInSession(JSONObject mJsonObject)
	// throws JSONException {
	// UserDTO userDTO = new UserDTO();
	// AddressDTO addressDTO = new AddressDTO();
	// WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
	// List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	// List<UserProfileImageDTO> userProfileImageDTOs = new
	// ArrayList<UserProfileImageDTO>();
	// List<FacebookLikesDTO> facebookLikesDTOs = new
	// ArrayList<FacebookLikesDTO>();
	//
	// JSONObject MainObject = mJsonObject.getJSONObject("user");
	// JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
	// JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
	// JSONArray jsonArrayFacebookLikes = mJsonObject
	// .getJSONArray("fbintrast");
	// System.out.println(MainObject);
	// userDTO.setLanguage(MainObject.getString("user_language"));
	// userDTO.setUserID(MainObject.getString("user_id"));
	// userDTO.setEmail(MainObject.getString("user_email"));
	// userDTO.setFirstName(MainObject.getString("user_fname"));
	//
	// // edited on revision 601
	// // MAY 18 2015
	// // dl commented
	// // userDTO.setLastName(MainObject.getString("user_lname"));
	// // added
	// if (MainObject.getString("user_lname") != null
	// || MainObject.getString("user_lname") != "null") {
	// userDTO.setLastName(MainObject.getString("user_lname"));
	// } else {
	// userDTO.setLastName("");
	// }
	//
	// // - - - - -
	// userDTO.setProfileImage(MainObject.getString("user_image"));
	// userDTO.setMobile(MainObject.getString("user_mobile"));
	// userDTO.setAboutMe(MainObject.getString("user_note"));
	// userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
	// if (MainObject.getString("user_facebook_Interest").equals(
	// AppConstants.FACEBOOK_LIKE_ENABLE)) {
	// userDTO.setFacebookLikeEnable(true);
	// } else {
	// userDTO.setFacebookLikeEnable(false);
	// }
	// this.userDTO.setUserID(MainObject.getString("user_id"));
	// whatAreYouDTO.setFeet(MainObject.getString("user_height"));
	// whatAreYouDTO.setInches(MainObject.getString("user_inches"));
	// whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
	// whatAreYouDTO.setHight(MainObject.getString("user_height"));
	// whatAreYouDTO.setAge(MainObject.getString("user_age"));
	// whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
	// whatAreYouDTO.setHeightUnit(MainObject.getString("user_heightUnit"));
	// whatAreYouDTO.setWeightUnit(MainObject.getString("user_weightUnit"));
	//
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
	// .getString("intrest_id"));
	// interestsDTOs.add(interestsDTO);
	//
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
	// for (int i = 0; i < jsonArrayImages.length(); i++) {
	// JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
	// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
	// userProfileImageDTO.setImageId(imagesJsonObject
	// .getString("image_id"));
	//
	// // edited for revision 607
	// // MAY 15 2015
	// // added if statement
	// if (!imagesJsonObject.getString("image_path").isEmpty()) {
	// userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
	// + imagesJsonObject.getString("image_path"));
	// }
	// // - - - --
	// userProfileImageDTO.setImagePosition(imagesJsonObject
	// .getString("image_position"));
	// if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
	// .getString("user_img_status"))) {
	// userProfileImageDTO.setIsImageActive(true);
	// }
	// userProfileImageDTOs.add(userProfileImageDTO);
	//
	// }
	// if (!"url".equals(userDTO.getProfileImage())) {
	// Log.d("", "facebook image at first position in list");
	// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
	// userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
	// userProfileImageDTO.setImagePath(userDTO.getProfileImage());
	// userProfileImageDTO.setIsImageActive(true);
	// userProfileImageDTOs.add(0, userProfileImageDTO);
	// }
	// userDTO.setWhatAreYouDTO(whatAreYouDTO);
	// userDTO.setInterestList(interestsDTOs);
	// userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
	// userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
	// return userDTO;
	// }

	public void openDailogForProfileImage() {

		Intent intent = new Intent(context, Gallery_Activity.class);
		// pictureLayout.setVisibility(View.VISIBLE);
		// if (context.picturesPathArray[context.imagePosition].equals("a")) {
		// deleteLayout.setVisibility(View.GONE);
		// } else {
		// deleteLayout.setVisibility(View.VISIBLE);
		// }

		if (context.picturesPathArray[context.imagePosition].equals("a")) {
			// deleteLayout.setVisibility(View.GONE);

			//

			intent.putExtra("delete", false);
		} else {

			intent.putExtra("delete", true);
			deleteLayout.setVisibility(View.VISIBLE);
		}

		context.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);

		// ImageView galleryView, cameraView;
		//
		// TextView galleryText, cameraText;
		//
		// AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// LayoutInflater inflater = LayoutInflater.from(context);
		// View view = inflater.inflate(R.layout.dialog_for_gallery_camera,
		// null);
		// builder.setTitle("Choose an action");
		// builder.setView(view);
		// galleryView = (ImageView) view.findViewById(R.id.galleryView);
		// cameraView = (ImageView) view.findViewById(R.id.cameraView);
		// galleryText = (TextView) view.findViewById(R.id.galleryText);
		// cameraText = (TextView) view.findViewById(R.id.cameraText);
		//
		// galleryView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(Intent.ACTION_PICK,
		//
		// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// // intent.setType("image/*");
		// // intent.putExtra("crop", "true");
		// // intent.putExtra("aspectX", 0);
		// // intent.putExtra("aspectY", 0);
		// // intent.putExtra("outputX", 200);
		// // intent.putExtra("outputY", 150);
		//
		// intent.setType("image/*");
		// intent.setAction(Intent.ACTION_PICK);
		// // ******** code for crop image
		// intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 0);
		// intent.putExtra("aspectY", 0);
		// intent.putExtra("outputX", 500);
		// intent.putExtra("outputY", 500);
		//
		// // Read more:
		// //
		// http://www.androidhub4you.com/2012/07/how-to-crop-image-from-camera-and.html#ixzz33Cbh0ce6
		//
		// // Read more:
		// //
		// http://www.androidhub4you.com/2012/07/how-to-crop-image-from-camera-and.html#ixzz33CVOUnU8
		//
		// try {
		//
		// intent.putExtra("return-data", true);
		// ((Activity) context).startActivityForResult(intent,
		// RESULT_LOAD_IMAGE);
		//
		// } catch (Exception e) {
		// // Do nothing for now
		// }
		// openGallery();
		// pictureLayout.setVisibility(View.GONE);
		// // alertDialog.dismiss();
		// }
		// });
		// cameraView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(
		// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 0);
		// intent.putExtra("aspectY", 0);
		// intent.putExtra("outputX", 500);
		// intent.putExtra("outputY", 500);
		//
		// // Read more:
		// //
		// http://www.androidhub4you.com/2012/07/how-to-crop-image-from-camera-and.html#ixzz33CVOUnU8
		//
		// try {
		//
		// intent.putExtra("return-data", true);
		//
		// ((Activity) context).startActivityForResult(intent,
		// RESULT_CAMERA_IMAGE);
		//
		// } catch (Exception e) {
		// // Do nothing for now
		// }
		//
		// // alertDialog.dismiss();
		// takePicture();
		// pictureLayout.setVisibility(View.GONE);
		// }
		// });

		// alertDialog = builder.create();
		// alertDialog.show();

	}

	private void takePicture() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(context.mFileTemp);
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

	class UpdateProfileAsynchTask extends AsyncTask<String, Void, String> {
		private JSONArray jsonArray;
		private JSONObject jsonObject;
		private String vStatus;
		private TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));

				// edited on revision 601
				// MAY 18 2015
				// dl commented
				// reqEntity.addPart("lname", new
				// StringBody(userDTO.getLastName()
				// .toString()));
				// added
				if (userDTO.getLastName().toString() != null
						|| userDTO.getLastName().toString() != "null") {
					reqEntity.addPart("lname", new StringBody(userDTO
							.getLastName().toString()));
				} else {
					reqEntity.addPart("lname", new StringBody(""));
				}
				// - - - -

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

				if (context.picturesPathArray[0]
						.equals(AppConstants.FACEBOOK_IMAGE)) {
					reqEntity.addPart("user_facebook_image", new StringBody(
							userDTO.getProfileImage()));
				} else {
					reqEntity.addPart("user_facebook_image", new StringBody(
							"url"));
				}

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
				/* delete images */
				for (int i = 0; i < context.delete.length; i++) {
					if (!AppConstants.FACEBOOK_IMAGE.equals(context.delete[i])
							&& !"a".equals(context.delete[i])) {

						// if (!(i == 0 && AppConstants.FACEBOOK_IMAGE
						// .equals(context.picturesPathArray[i]))) {

						reqEntity.addPart(
								"delete_image[" + i + "][]",
								new StringBody(String
										.valueOf(context.delete[i])));
						Log.d("update image image Id=" + context.delete[i],
								"update image image Id=" + context.delete[i]);

						// }
					}

				}

				for (int i = 0; i < context.picturesPathArray.length; i++) {
					System.out.println("AddItemActivity.arrayList.get(i)"
							+ context.picturesPathArray[i]);
					if (!AppConstants.UN_UPDATED_IMAGE
							.equals(context.picturesPathArray[i])
							&& !"a".equals(context.picturesPathArray[i])) {
						if (!(i == 0 && AppConstants.FACEBOOK_IMAGE
								.equals(context.picturesPathArray[i]))) {
							file = new File(context.picturesPathArray[i]);
							FileBody bin = new FileBody(file);
							reqEntity.addPart("thumb_image" + i, bin);
							Log.d("add image image Id=" + context.delete[i],
									"add image image path="
											+ context.picturesPathArray[i]
											+ " image id=" + i);
						}
					}
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
			dialog.dismiss();
			System.out.println("Responce....Admin Reg.String" + result);
			// [{"status":"Success","admin_unique_id":"admin4"}]
			try {
				jsonArray = new JSONArray(result);

				jsonObject = jsonArray.getJSONObject(0);
				vStatus = jsonObject.getString("status");
				if (vStatus.equals("success")) {
					// Toast.makeText(context, "Profile Updated Successfully",
					// Toast.LENGTH_SHORT).show();

					// sessionManager
					// .setUserDetail(parseUserDataAndSetInSession(jsonObject));
					((Activity) context).finish();

				} else if (vStatus.equals("failure")) {
					// Toast.makeText(context, "Profile Not Updated",
					// Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class XmppRegistrationAsynchTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			Log.d("", "xmpp registration start");
			try {
				// http://54.241.85.74:9090/plugins/userService/userservice?type=add&secret=7bDFD4TP&username=555&password=555&name=yogesh%20pawar&email=ypawar979@gmail.com
				// changed ip 54.241.85.74
				String posturl = "http://184.169.159.101:9090/plugins/userService/userservice?type=add&secret=7bDFD4TP&username="
						+ userDTO.getUserID()
						+ "&password="
						+ userDTO.getUserID()
						+ "&name="
						+ URLEncoder.encode(userDTO.getFirstName().replace(" ",
								"")
								+ userDTO.getLastName().replace(" ", ""))
						+ "&email=" + userDTO.getEmail();

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
			} catch (Exception e) {

			}
			return mValue;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Log.d("", "xmpp registration successfull");

		}
	}

	class CheckUserAvailabilityAsynchTask extends
			AsyncTask<String, Void, JSONArray> {
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
			// params.add(new BasicNameValuePair("action", "login_user"));
			params.add(new BasicNameValuePair("user_email", email.getText()
					.toString()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("check user response", "" + json);
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
					Toast.makeText(
							context,
							R.string.toast_account_already_exists_with_this_email,
							1).show();
				} else if (vStatus.equals("failure")) {
					Activity_ProfileDetails.viewPager.setPagingEnabled(true);
					((ViewPager) viewGroup).setCurrentItem(1);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

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
		Intent mIntent = new Intent(context, Activity_Home.class);
		mIntent.putExtra("open", AppConstants.FRAGMENT_HOME);
		mIntent.putExtra("loadchatHistory", true);
		context.startActivity(mIntent);

		// if (isRememberMe) {
		// sessionManager.setRememberMe(userName.getText().toString(),
		// password.getText().toString());
		// } else {
		// sessionManager.clearRememberMe();
		// }

		Editor editor = singleton.mFilterPrefs.edit();
		editor.putBoolean(AppConstants.FILTER_PREFS_AGE, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_RADIUS, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_ETHNICITY, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_ROLE, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_INTEREST, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_BODY_TYPE, false);
		editor.putBoolean(AppConstants.FILTER_PREFS_LOOKING_FOR, false);
		editor.commit();

		context.finish();
	}

	// validation to check mail id
	// boolean checkEmailCorrect(String Email) {
	// String pttn = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]+";
	// Pattern p = Pattern.compile(pttn);
	// Matcher m = p.matcher(Email);
	//
	// if (m.matches()) {
	// return true;
	// }
	// return false;
	// }
	//
	// public boolean isValidEmail(CharSequence target) {
	// if (target == null) {
	// return false;
	// } else {
	// return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	// }
	// }
}