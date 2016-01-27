package com.krave.kraveapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executor;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
import org.jivesoftware.smack.packet.Message;
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
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.ps.adapters.ChatListViewAdapter;
import com.ps.adapters.UserListAdapter;
import com.ps.gallery.Custom_Gallery_Activity;
import com.ps.gallery.Private_Photo_Gallery_Activity;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.ChatDetailsDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.CompressBitmap;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveDAO;
import com.ps.utill.MyXMLHandler;
import com.ps.utill.SessionManager;
import com.ps.utill.Utils;
import com.ps.utill.WebServiceConstants;
import com.ps.video.camera.VideoActivity;
import com.swipre.refresh.SwipyRefreshLayout;
import com.swipre.refresh.SwipyRefreshLayoutDirection;

import android.support.v4.widget.SwipeRefreshLayout;

@SuppressLint("NewApi")
public class FragmentChatMatches extends Fragment implements OnClickListener,
		OnScrollListener {

	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";
	/* resources */

	private SessionManager sessionManager;
	private ImageLoaderCircle imageLoaderCircle;
	private Activity_Home context;

	/* Layout Views */
	private ImageView backButton, addButton, onlineImageView;
	private CircleImageView circleImageView;
	private TextView dudeName;
	private RelativeLayout plusButtonLayout, imageButtonLayout,
			optionButtonLayout, rlxx;
	private LinearLayout viewDudeProfileLayout, blockDudeLayout,
			deleteHistoryLayout, cancle1Layout, takeFromCameraLayout,
			fromGalleryLayout, cancle2Layout, addToListLayout, locationLayout,
			imageLayout, videoLayout, cancle3Layout, privatePhotoLayout;
	private ListView chatListView;
	private EditText messageField;
	private Button sendButton, imageButton;
	private TextView headerDateTime;
	private View headerDateTimeLayout;
	// public static ChatDetailsDTO chatDetailsDTO;
	/* dto variables initialization */
	private ChatListViewAdapter adapter;
	private List<ChatDetailsDTO> chatList = new ArrayList<ChatDetailsDTO>();
	public static String ChatXmppUserId;
	public static boolean isOnline = false;
	public static UserDTO userDTO;
	public static boolean comeFrom = true;
	public static boolean backButtonComeFrom = false;
	KraveDAO databaseHelper;
	public static ChatDetailsDTO chatDetailsDTO;
	BroadcastReceiver messageBroadcastReceiver;
	String previousDate = "";
	private static int RESULT_LOAD_IMAGE = 5;
	private static int RESULT_PRIVATE_IMAGE = 7;
	private static int RESULT_CAMERA_IMAGE = 4;
	private static int RESULT_VIDEO_ACTVITY = 6;
	private String picturePath = "";
	private ChatDetailsDTO chatHistoryChatDetailsDTO;
	private AppManager singleton;
	// private ImageView sendLocation;
	UserListAdapter userListAdapter;
	List<UserListDTO> userListDTOs = new ArrayList<UserListDTO>();
	UserListDTO userListDTO;

	// String mCurrentPhotoPath;
	private String snapChatSecounds = "";

	private SwipeRefreshLayout mSwipyRefreshLayout;

	// pagination variables
	private int pageNumber = 1; // page number of pagination web service
	private int limit = 10; // limit of chat message to be load from server at a
							// once
	private int offset = 0; // number of message comes and gone after first page
							// message is loaded
	private boolean isLoadingOnPull = false;
	private boolean shouldReload = false;
	private boolean isLoadingNextPage = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.gc();
		View view = inflater.inflate(R.layout.fragment_chat_matches, container,
				false);

		context = (Activity_Home) getActivity();
		context.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		sessionManager = new SessionManager(context);
		imageLoaderCircle = new ImageLoaderCircle(context);
		databaseHelper = new KraveDAO(context);
		/* av singleton */
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		setLayout(view);
		setListeners();
		setTypeFace(view);
		context.mainview.setVisibility(View.GONE);

		messageBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				addNewMessage(chatDetailsDTO);

			}
		};
		IntentFilter intentFilter1 = new IntentFilter();
		intentFilter1.addAction("message");

		context.registerReceiver(this.messageBroadcastReceiver, intentFilter1);

		BroadcastReceiver chatHistoryBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				reloadChatList();

			}
		};

		IntentFilter chatHistoryUpdateIntentFilter = new IntentFilter();
		chatHistoryUpdateIntentFilter
				.addAction(AppConstants.INTENT_ACTION_UPDATE_CHAT_HISTORY);
		context.registerReceiver(chatHistoryBroadcastReceiver,
				chatHistoryUpdateIntentFilter);

		if (comeFrom) {
			try {
				loadData();
			} catch (Exception e) {

			}
		} else {
			if (WebServiceConstants.isOnline(context)) {
				new GetDudeAsynchTask().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_DUDE_BY_ID);
			}
			viewDudeProfileLayout.setEnabled(false);
			blockDudeLayout.setEnabled(false);
			comeFrom = true;
		}

		// new GetChatHistoryByPagination()
		// .execute(WebServiceConstants.GET_CHAT_HISTORY_OF_SINGLE_USER_BY_PAGE_ID);
		// getChatFromDatabse();
		// edited for revision 618
		// MAY 21 2 2015
		// ySherlock
		fetchChatMessageFromServer();
		System.gc();
		// - - - -

		return view;
	}

	private void setUnreadMessageAsRead() {
		try {
			int count = singleton.getChatCount.get(ChatXmppUserId);
			GpsService.unreadMsgCount = GpsService.unreadMsgCount - count;
			singleton.getChatCount.put(ChatXmppUserId, 0);
			Intent broadcast = new Intent();
			broadcast.putExtra("come", "notification");
			broadcast.setAction(BROADCAST_ACTION);
			context.sendBroadcast(broadcast);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new UnreadMessageAsyncTaskr()
				.execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.UNREAD_MESSAGE_SERVICE_OF_PARTICULAR_USER);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		outState.putString("tab", "yourAwesomeFragmentsTab");

		super.onSaveInstanceState(outState);

	}

	private void setTypeFace(View view) {
		TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		textView3 = (TextView) view.findViewById(R.id.textView3);
		textView4 = (TextView) view.findViewById(R.id.textView4);
		textView5 = (TextView) view.findViewById(R.id.textView5);
		textView6 = (TextView) view.findViewById(R.id.textView6);
		textView7 = (TextView) view.findViewById(R.id.textView7);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);
		dudeName.setTypeface(typeface);
		textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		textView3.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		textView7.setTypeface(typeface);
		messageField.setTypeface(typeface);
		Typeface headerDateTimetypeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Bd);

		headerDateTime.setTypeface(headerDateTimetypeface);

		// sendButton.setTypeface(typeface);

	}

	private void getChatFromDatabse() {

		super.onStart();
		databaseHelper.previousDate = "";
		chatList.clear();
		chatList.addAll(databaseHelper.getChat(ChatXmppUserId, sessionManager
				.getUserDetail().getUserID()));

		if (chatList.size() != 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat(
					"dd MMMM yyyy  hh:mm");
			Date date2 = new Date(Long.valueOf(chatList
					.get(chatList.size() - 1).getTime()));
			previousDate = dateFormat1.format(date2);
		}

		adapter.notifyDataSetChanged();

	}

	// @Override
	// public void onStart() {
	//
	// super.onStart();
	// databaseHelper.previousDate = "";
	// chatList.clear();
	// chatList.addAll(databaseHelper.getChat(ChatXmppUserId, sessionManager
	// .getUserDetail().getUserID()));
	//
	// if (chatList.size() != 0) {
	// SimpleDateFormat dateFormat1 = new
	// SimpleDateFormat("dd MMMM yyyy  hh:mm");
	// Date date2 = new Date(Long.valueOf(chatList
	// .get(chatList.size() - 1).getTime()));
	// previousDate = dateFormat1.format(date2);
	// }
	//
	// adapter.notifyDataSetChanged();
	//
	// }

	public void reloadChatList() {
		databaseHelper.previousDate = "";

		List<ChatDetailsDTO> cachedChatList = databaseHelper.getChat(
				ChatXmppUserId, sessionManager.getUserDetail().getUserID());
		if (cachedChatList.size() > 0) {
			chatList.clear();
			chatList.addAll(cachedChatList);

			if (chatList.size() != 0) {
				SimpleDateFormat dateFormat1 = new SimpleDateFormat(
						"dd MMMM yyyy  hh:mm");
				Date date2 = new Date(Long.valueOf(chatList.get(
						chatList.size() - 1).getTime()));
				previousDate = dateFormat1.format(date2);
			}

			adapter.notifyDataSetChanged();
		}
	}

	// ////////////////////////////////////////////////////////
	// conrad
	@Override
	public void onPause() {
		super.onPause();
		hideKeyboard();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideKeyboard();
	}

	private void hideKeyboard() {
		try {
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), 0);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void loadData() {
		handler.postDelayed(runnable, 0);
		// userDTO = Activity_Home.dudeCommonList.get(Activity_Home.index);
		if (userDTO.getUserProfileImageDTOs().size() != 0
				&& userDTO.getUserProfileImageDTOs() != null
				&& userDTO.getUserProfileImageDTOs().get(0).getIsImageActive()) {
			imageLoaderCircle.DisplayImage(userDTO.getUserProfileImageDTOs()
					.get(0).getImagePath(), circleImageView);

		} else {
			imageLoaderCircle.DisplayImage(userDTO.getProfileImage(),
					circleImageView);
		}
		ChatXmppUserId = userDTO.getUserID();

		// Edited for revision 604
		// MAY 19 2015
		// added if else statement ( dl )
		if (userDTO.getLastName().toString().equals("(null)")) {
			dudeName.setText(userDTO.getFirstName());

		} else {// commented
			dudeName.setText(userDTO.getFirstName() + " "
					+ userDTO.getLastName());
		}
		// and commented
		// dudeName.setText(userDTO.getFirstName() + " " +
		// userDTO.getLastName());
	}

	private void addNewMessage(ChatDetailsDTO newMessage) {
		formatDate(newMessage);
		// databaseHelper.addChat(chatDetailsDTO);
		setListViewScrollProperties(true);
		if (chatList.size() != 0) {
			ChatDetailsDTO lastMessage = chatList.get(chatList.size() - 1);
			if (!lastMessage.getDate().equals(newMessage.getDate())) {
				addDateLayoutInChatList(newMessage.getDate(), chatList.size());
			}
		} else {
			// addDateLayoutInChatList(newMessage.getDate(), chatList.size());
		}
		offset++;
		chatList.add(newMessage);

		adapter.notifyDataSetChanged();

	}

	private void setLayout(View view) {

		LinearLayout mainview = (LinearLayout) view.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		headerDateTime = (TextView) view.findViewById(R.id.dateTime);
		headerDateTimeLayout = (View) view.findViewById(R.id.dateHeaderLayout);
		headerDateTimeLayout.setVisibility(View.GONE);
		backButton = (ImageView) view.findViewById(R.id.backButton);
		addButton = (ImageView) view.findViewById(R.id.addButton);

		circleImageView = (CircleImageView) view
				.findViewById(R.id.dudeProfilePick);
		dudeName = (TextView) view.findViewById(R.id.dudeName);
		viewDudeProfileLayout = (LinearLayout) view
				.findViewById(R.id.viewDudeProfileLayout);
		blockDudeLayout = (LinearLayout) view
				.findViewById(R.id.blockDudeLayout);

		deleteHistoryLayout = (LinearLayout) view
				.findViewById(R.id.deleteHistory);
		cancle1Layout = (LinearLayout) view.findViewById(R.id.cancleLayout1);
		cancle2Layout = (LinearLayout) view.findViewById(R.id.cancleLayout2);
		cancle3Layout = (LinearLayout) view.findViewById(R.id.cancleLayout3);
		addToListLayout = (LinearLayout) view
				.findViewById(R.id.addToListLayout);

		takeFromCameraLayout = (LinearLayout) view
				.findViewById(R.id.takePictureLayout);
		fromGalleryLayout = (LinearLayout) view
				.findViewById(R.id.fromGalleryLayout);
		locationLayout = (LinearLayout) view.findViewById(R.id.locationLayout);
		imageLayout = (LinearLayout) view.findViewById(R.id.imageLayout);
		videoLayout = (LinearLayout) view.findViewById(R.id.videoLayout);
		privatePhotoLayout = (LinearLayout) view
				.findViewById(R.id.sharePrivatePhoto);
		optionButtonLayout = (RelativeLayout) view
				.findViewById(R.id.optionButtonLayout);
		plusButtonLayout = (RelativeLayout) view
				.findViewById(R.id.plusButtonLayout);
		imageButtonLayout = (RelativeLayout) view
				.findViewById(R.id.imageButtonLayout);
		chatListView = (ListView) view.findViewById(R.id.chatListView);
		imageButton = (Button) view.findViewById(R.id.imageButton);
		messageField = (EditText) view.findViewById(R.id.message);
		sendButton = (Button) view.findViewById(R.id.sendButton);
		onlineImageView = (ImageView) view.findViewById(R.id.onlineImage);
		rlxx = (RelativeLayout) view.findViewById(R.id.xx);
		mSwipyRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipyrefreshlayout);
		adapter = new ChatListViewAdapter(context,
				(ArrayList<ChatDetailsDTO>) chatList);
		setListViewScrollProperties(true);

		// adapter.registerDataSetObserver(new DataSetObserver() {
		// @Override
		// public void onChanged() {
		// super.onChanged();
		// chatListView.setSelection(adapter.getCount() - 1);
		// }
		// });
		chatListView.setAdapter(adapter);

		// sendLocation = (ImageView) view.findViewById(R.id.sendLocation);
		// to scroll the list view to bottom on data change

	}

	private void setListViewScrollProperties(boolean behaviour) {
		if (behaviour) {
			chatListView
					.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			chatListView.setStackFromBottom(true);
		} else {
			chatListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
			chatListView.setStackFromBottom(false);
		}

	}

	private void setListeners() {
		backButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		// viewDudeProfileLayout.setOnClickListener(this);
		// blockDudeLayout.setOnClickListener(this);
		deleteHistoryLayout.setOnClickListener(this);
		cancle1Layout.setOnClickListener(this);
		cancle2Layout.setOnClickListener(this);
		cancle3Layout.setOnClickListener(this);
		imageLayout.setOnClickListener(this);
		videoLayout.setOnClickListener(this);
		privatePhotoLayout.setOnClickListener(this);
		locationLayout.setOnClickListener(this);
		takeFromCameraLayout.setOnClickListener(this);
		fromGalleryLayout.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		imageButton.setOnClickListener(this);
		addToListLayout.setOnClickListener(this);

		circleImageView.setOnClickListener(this);
		chatListView.setOnScrollListener(this);
		// sendLocation.setOnClickListener(this);

		messageField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEND) {

					if (messageField.getText().toString().trim().length() != 0)
						sendMessage();

				}
				return false;
			}
		});
		messageField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().trim().length() == 0) {
					sendButton
							.setBackgroundResource(R.drawable.av_new_chat_senddown);
				} else {
					sendButton
							.setBackgroundResource(R.drawable.av_new_chat_sendup);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		mSwipyRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						pageNumber++;
						isLoadingNextPage = true;

						fetchChatMessageFromServer();
						setListViewScrollProperties(false);
					}

				});
		// mSwipyRefreshLayout
		// .setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
		//
		// // TODO: SWIPE
		// @Override
		// public void onRefresh(SwipyRefreshLayoutDirection direction) {
		// Log.d("MainActivity",
		// "Refresh triggered at "
		// + (direction == SwipyRefreshLayoutDirection.TOP ? "top"
		// : "bottom"));
		//
		// if (direction == SwipyRefreshLayoutDirection.TOP) {
		// pageNumber++;
		// isLoadingNextPage = true;
		//
		// fetchChatMessageFromServer();
		//
		// } else {
		// // pageNumber = 1;
		// // isLoadingOnPull = true;
		// //
		// // fetchChatMessageFromServer();
		// if (mSwipyRefreshLayout.isRefreshing()) {
		// mSwipyRefreshLayout.setRefreshing(false);
		// }
		// }
		//
		// // Toast.makeText(context, "pageNumber=" + pageNumber,
		// // Toast.LENGTH_SHORT).show();
		// }
		//
		// });
		chatListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void fetchChatMessageFromServer() {
		if (!mSwipyRefreshLayout.isRefreshing())
			mSwipyRefreshLayout.setRefreshing(true);

		new GetChatHistoryByPagination().executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR,
				WebServiceConstants.GET_CHAT_HISTORY_OF_SINGLE_USER_BY_PAGE_ID);
		// .execute(WebServiceConstants.GET_CHAT_HISTORY_OF_SINGLE_USER_BY_PAGE_ID);
		// Toast.makeText(context, "pageNumber=" + pageNumber,
		// Toast.LENGTH_SHORT)
		// .show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.locationLayout:
			optionButtonLayout.setVisibility(View.GONE);
			alignChatBoxBottom();
			new AlertDialog.Builder(context)
					.setTitle(R.string.dialog_post_location)
					.setMessage(
							R.string.dialog_do_you_want_to_post_your_location)
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with post
									if (!GpsService.country.equals("")) {
										sendLocation();
									} else {
										Toast.makeText(
												context,
												R.string.toast_gps_services_cannot_track_your_current_location,
												Toast.LENGTH_LONG).show();
									}
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();

			break;
		case R.id.dudeProfilePick:
			try {
				context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_CHAT,
						AppConstants.EVENT_LOG_ACTION_BTN, "ViewProfile");

				FragmentHome.clearData = true;
				FragmentDetailDudesProfile.comeFrom = AppConstants.FRAGMENT_CHATT_MATCHES;
				context.Attach_Fragment(AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE);
				// Activity_Home.dudeCommonList = filteredData;
				// Activity_Home.index = position;
				Activity_Home.gblUserID = userDTO.getUserID();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.backButton:
			if (backButtonComeFrom) {
				// context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
				context.Attach_Fragment(AppConstants.FRAGMENT_CHAT_FRIEND);
				backButtonComeFrom = false;
			} else {
				context.setLeftDrawer = 0;

				context.COME_FROM = AppConstants.FRAGMENT_CHATT_MATCHES;
				// context.finish();

				context.left_button
						.setImageResource(R.drawable.av_new_nav_menuup);
				context.onClick(context.layoutFindDudes);
			}
			break;
		case R.id.addButton:
			optionButtonLayout.setVisibility(View.GONE);
			imageButtonLayout.setVisibility(View.GONE);
			plusButtonLayout.setVisibility(View.VISIBLE);

			alignChatBoxAboveLayout(R.id.plusButtonLayout);

			hideKeyboard();

			break;
		case R.id.addToListLayout:
			context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_CHAT,
					AppConstants.EVENT_LOG_ACTION_BTN, "AddToList");
			if (WebServiceConstants.isOnline(context)) {
				new GetUserList().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_USER_LIST);
			}
			break;
		// case R.id.viewDudeProfileLayout:
		// FragmentDetailDudesProfile.comeFrom =
		// AppConstants.FRAGMENT_CHATT_MATCHES;
		// context.Attach_Fragment(AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE);
		// plusButtonLayout.setVisibility(View.GONE);
		//
		// break;
		// case R.id.blockDudeLayout:
		// Intent intent = new Intent(context, Activity_Block_Dude.class);
		// startActivityForResult(intent, 1);
		// plusButtonLayout.setVisibility(View.GONE);
		// break;
		case R.id.cancleLayout1:
			plusButtonLayout.setVisibility(View.GONE);

			alignChatBoxBottom();
			break;
		case R.id.cancleLayout2:
			imageButtonLayout.setVisibility(View.GONE);

			alignChatBoxBottom();
			break;
		case R.id.cancleLayout3:
			optionButtonLayout.setVisibility(View.GONE);

			alignChatBoxBottom();
			break;
		case R.id.deleteHistory:
			context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_CHAT,
					AppConstants.EVENT_LOG_ACTION_BTN, "DeleteHistory");
			openDailog();
			plusButtonLayout.setVisibility(View.GONE);
			alignChatBoxBottom();
			break;
		case R.id.takePictureLayout:
			dispatchTakePictureIntent();
			imageButtonLayout.setVisibility(View.GONE);
			alignChatBoxBottom();

			context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_CHAT,
					AppConstants.EVENT_LOG_ACTION_SEND_MSG, "Picture");
			break;
		case R.id.sharePrivatePhoto:
			imageButtonLayout.setVisibility(View.GONE);
			optionButtonLayout.setVisibility(View.GONE);
			alignChatBoxBottom();
			openGallery();

			context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_CHAT,
					AppConstants.EVENT_LOG_ACTION_SEND_MSG, "Picture");
			break;
		case R.id.imageButton:
			// Intent intent = new Intent(context, VideoActivity.class);
			// startActivityForResult(intent, RESULT_VIDEO_ACTVITY);
			plusButtonLayout.setVisibility(View.GONE);
			imageButtonLayout.setVisibility(View.GONE);
			optionButtonLayout.setVisibility(View.VISIBLE);

			alignChatBoxAboveLayout(R.id.optionButtonLayout);

			break;
		case R.id.videoLayout:
			Intent intent = new Intent(context, VideoActivity.class);
			startActivityForResult(intent, RESULT_VIDEO_ACTVITY);
			plusButtonLayout.setVisibility(View.GONE);
			imageButtonLayout.setVisibility(View.GONE);
			optionButtonLayout.setVisibility(View.GONE);

			alignChatBoxBottom();

			break;
		case R.id.imageLayout:
			plusButtonLayout.setVisibility(View.GONE);
			imageButtonLayout.setVisibility(View.VISIBLE);
			optionButtonLayout.setVisibility(View.GONE);

			alignChatBoxAboveLayout(R.id.imageButtonLayout);

			break;
		case R.id.sendButton:
			if (messageField.getText().toString().trim().length() != 0) {
				FlurryAgent.logEvent("Chat Send Message"); // Edited revision
															// 582 commented
				sendMessage();
				context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_CHAT,
						AppConstants.EVENT_LOG_ACTION_SEND_MSG, "Text");
			}
			break;
		default:
			break;
		}

	}

	private void alignChatBoxAboveLayout(int layout) {
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.ABOVE, layout);
		rlxx.setLayoutParams(param);
	}

	private void alignChatBoxBottom() {
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlxx.setLayoutParams(param);
	}

	private void startUserCurrentLocationService() {

		Intent intent1 = new Intent(context, ChatService.class);
		context.startService(intent1);

	}

	public void openGallery() {
		// TODO Auto-generated method stub
		// Intent intent = new Intent(Intent.ACTION_PICK);
		// intent.setType("image/*");
		// startActivityForResult(intent, RESULT_LOAD_IMAGE);

		Intent i = new Intent(context, Private_Photo_Gallery_Activity.class);

		startActivityForResult(i, RESULT_PRIVATE_IMAGE);
	}

	public void openCamera() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, RESULT_CAMERA_IMAGE);

	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"krave/Temp");
		;
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		picturePath = image.getAbsolutePath();
		return image;
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				// ...
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, RESULT_CAMERA_IMAGE);
			}
		}
	}

	// private void processPictureWhenReady(final String picturePath) {
	// final File pictureFile = new File(picturePath);
	//
	// if (pictureFile.exists()) {
	// // The picture is ready; process it.
	// } else {
	// // The file does not exist yet. Before starting the file observer,
	// // you
	// // can update your UI to let the user know that the application is
	// // waiting for the picture (for example, by displaying the thumbnail
	// // image and a progress indicator).
	//
	// final File parentDirectory = pictureFile.getParentFile();
	// FileObserver observer = new FileObserver(parentDirectory.getPath()) {
	// // Protect against additional pending events after CLOSE_WRITE
	// // is
	// // handled.
	// private boolean isFileWritten;
	//
	// @Override
	// public void onEvent(int event, String path) {
	// if (!isFileWritten) {
	// // For safety, make sure that the file that was created
	// // in
	// // the directory is actually the one that we're
	// // expecting.
	// File affectedFile = new File(parentDirectory, path);
	// isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile
	// .equals(pictureFile));
	//
	// if (isFileWritten) {
	// stopWatching();
	// new Uploadfi
	// .execute(WebServiceConstants.BASE_URL_FILE
	// + WebServiceConstants.UPLOAD_FILE);
	// // Now that the file is ready, recursively call
	// // processPictureWhenReady again (on the UI thread).
	// // runOnUiThread(new Runnable() {
	// // @Override
	// // public void run() {
	// // processPictureWhenReady(picturePath);
	// // }
	// // });
	// }
	// }
	// }
	// };
	// observer.startWatching();
	// }
	// }

	// private void takePhoto() {
	// final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT,
	// Uri.fromFile(getTempFile(context)));
	// startActivityForResult(intent, RESULT_CAMERA_IMAGE);
	// }

	// private File getTempFile(Context context) {
	// // it will return /sdcard/image.tmp
	// final File path = new File(Environment.getExternalStorageDirectory(),
	// context.getPackageName());
	// if (!path.exists()) {
	// path.mkdir();
	// }
	// return new File(path, "image.jpg");
	// }

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (resultCode == RESULT_OK) {
	// switch(requestCode){
	// case TAKE_PHOTO_CODE:
	// final File file = getTempFile(this);
	// try {
	// Bitmap captureBmp = Media.getBitmap(getContentResolver(),
	// Uri.fromFile(file) );
	// // do whatever you want with the bitmap (Resize, Rename, Add To Gallery,
	// etc)
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// break;
	// }
	// }
	// }

	// public void sendImage() {
	// // TODO Auto-generated method stub
	// // super.onActivityResult(requestCode, resultCode, data);
	// if (WebServiceConstants.isOnline(context)
	// && ChatService.connection.isConnected()) {
	// String to = ChatXmppUserId + AppConstants.CHAT_SERVER_ID;
	// String text = messageField.getText().toString();
	//
	// Log.i("XMPPClient", "Sending text [" + text + "] to [" + to + "]");
	// Message msg = new Message(to, Message.Type.chat);
	// msg.setProperty("name", sessionManager.getUserDetail()
	// .getFirstName());
	//
	// String body = convertImageToBase64(picturePath);
	// msg.setBody(body + AppConstants.MESSAGE_TYPE_IMAGE);
	// ChatService.connection.sendPacket(msg);
	//
	// ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
	// chatDetailsDTO.setTouser(ChatXmppUserId);
	// chatDetailsDTO.setFromuser(sessionManager.getUserDetail()
	// .getUserID());
	// chatDetailsDTO.setTime("" + System.currentTimeMillis());
	// chatDetailsDTO.setMessage(body);
	// chatDetailsDTO.setMeassageType(AppConstants.MESSAGE_TYPE_IMAGE);
	// chatHistoryChatDetailsDTO = chatDetailsDTO;
	// long rowId = databaseHelper.addChat(chatDetailsDTO);
	// chatDetailsDTO.setId("" + rowId);
	// addNewMessage(chatDetailsDTO);
	// messageField.setText("");
	// // new SaveChatHistory().execute(WebServiceConstants.BASE_URL
	// // + WebServiceConstants.SEND_MESSAGE_TO_SERVER);
	//
	// // new UploadImage().execute(WebServiceConstants.BASE_URL
	// // + WebServiceConstants.UPLOAD_FILE);
	// }
	// }

	public void sendImage(String pictureUrl) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		try {
			if (WebServiceConstants.isOnline(context)
					&& ChatService.connection != null
					&& ChatService.connection.isConnected()) {
				String to = ChatXmppUserId + AppConstants.CHAT_SERVER_ID;
				String text = messageField.getText().toString();

				Log.i("XMPPClient", "Sending text [" + text + "] to [" + to
						+ "]");
				Message msg = new Message(to, Message.Type.chat);
				msg.setProperty("name", sessionManager.getUserDetail()
						.getFirstName());

				msg.setBody(pictureUrl + snapChatSecounds
						+ AppConstants.MESSAGE_TYPE_IMAGE);
				ChatService.connection.sendPacket(msg);

				ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
				chatDetailsDTO.setTouser(ChatXmppUserId);
				chatDetailsDTO.setFromuser(sessionManager.getUserDetail()
						.getUserID());
				chatDetailsDTO.setTime("" + System.currentTimeMillis());
				chatDetailsDTO.setMessage(pictureUrl + snapChatSecounds);
				chatDetailsDTO.setMeassageType(AppConstants.MESSAGE_TYPE_IMAGE);
				chatHistoryChatDetailsDTO = chatDetailsDTO;
				long rowId = databaseHelper.addChat(chatDetailsDTO);
				chatDetailsDTO.setId("" + rowId);
				addNewMessage(chatDetailsDTO);
				messageField.setText("");
				new SaveChatHistory().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.SEND_MESSAGE_TO_SERVER);

				// new UploadImage().execute(WebServiceConstants.BASE_URL
				// + WebServiceConstants.UPLOAD_FILE);
			} else {
				startUserCurrentLocationService();
			}
		} catch (Exception e) {
			Toast.makeText(context,
					R.string.toast_connection_loss_please_login_again,
					Toast.LENGTH_SHORT).show();
		}
	}

	public void sendVideo(String thumbUrl, String videoUrl) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		try {
			if (WebServiceConstants.isOnline(context)
					&& ChatService.connection != null
					&& ChatService.connection.isConnected()) {
				String to = ChatXmppUserId + AppConstants.CHAT_SERVER_ID;
				// String text = messageField.getText().toString();
				//
				// Log.i("XMPPClient", "Sending text [" + text + "] to [" + to +
				// "]");
				Message msg = new Message(to, Message.Type.chat);
				msg.setProperty("name", sessionManager.getUserDetail()
						.getFirstName());

				msg.setBody(thumbUrl + "|" + videoUrl + "|" + snapChatSecounds
						+ AppConstants.MESSAGE_TYPE_VIDEO);
				ChatService.connection.sendPacket(msg);

				ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
				chatDetailsDTO.setTouser(ChatXmppUserId);
				chatDetailsDTO.setFromuser(sessionManager.getUserDetail()
						.getUserID());
				chatDetailsDTO.setTime("" + System.currentTimeMillis());
				chatDetailsDTO.setMessage(thumbUrl + "|" + videoUrl + "|"
						+ snapChatSecounds);
				chatDetailsDTO.setMeassageType(AppConstants.MESSAGE_TYPE_VIDEO);
				chatHistoryChatDetailsDTO = chatDetailsDTO;
				long rowId = databaseHelper.addChat(chatDetailsDTO);
				chatDetailsDTO.setId("" + rowId);
				addNewMessage(chatDetailsDTO);
				messageField.setText("");
				new SaveChatHistory().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.SEND_MESSAGE_TO_SERVER);

				// new UploadImage().execute(WebServiceConstants.BASE_URL
				// + WebServiceConstants.UPLOAD_FILE);
			} else {
				startUserCurrentLocationService();
			}
		} catch (Exception e) {
			Toast.makeText(context,
					R.string.toast_connection_loss_please_login_again,
					Toast.LENGTH_SHORT).show();
		}
	}

	// public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth,
	// int bitmapHeight) {
	// return Bitmap
	// .createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
	// }

	// private String convertImageToBase64(String picturePath) {
	// // String imageInSD = "/sdcard/UserImages/" + userImageName;
	// // BitmapFactory.Options options = null;
	// // options = new BitmapFactory.Options();
	// // options.inSampleSize = 2;
	// Bitmap bitmap = convertBitmap(picturePath);
	// // Bitmap resizeBitmap = getResizedBitmap(bitmap, 100, 100);
	// // Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
	// ByteArrayOutputStream stream = new ByteArrayOutputStream();
	//
	// bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	// byte[] byteArray = stream.toByteArray();
	// // byte windowImageString[] = Base64.decode(
	// // jObj.getString(Shared.TAG_windowimage), Base64.DEFAULT);
	// //
	// // byte[] bytes = cursor1.getBlob(cursor1
	// // .getColumnIndex(Shared.TAG_windowimage));
	// String windowImageString = null;
	// if (byteArray != null) {
	//
	// windowImageString = Base64
	// .encodeToString(byteArray, Base64.DEFAULT);
	// Log.e("windowImage", windowImageString);
	// }
	//
	// try {
	// stream.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// stream = null;
	// bitmap.recycle();
	//
	// // new UploadFile().execute(WebServiceConstants.BASE_URL
	// // + WebServiceConstants.UPLOAD_FILE, windowImageString);
	// return windowImageString;
	//
	// }

	// public Bitmap convertBitmap(String path) {
	//
	// Bitmap bitmap = null;
	// BitmapFactory.Options bfOptions = new BitmapFactory.Options();
	// bfOptions.inDither = false; // Disable Dithering mode
	// bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
	// // memory, the Bitmap can be cleared
	// bfOptions.inInputShareable = true; // Which kind of reference will be
	// // used to recover the Bitmap data
	// // after being clear, when it will
	// // be used in the future
	// bfOptions.inTempStorage = new byte[32 * 1024];
	//
	// File file = new File(path);
	// FileInputStream fs = null;
	// try {
	// fs = new FileInputStream(file);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// try {
	// if (fs != null) {
	// bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
	// bfOptions);
	// }
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// } finally {
	// if (fs != null) {
	// try {
	// fs.close();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// return bitmap;
	// }

	// private void LoadBitmap(Bitmap bitmap, String strMyImagePath) {
	// // String strMyImagePath = Environment.getExternalStorageDirectory()
	// // .getAbsolutePath() + "/test.png";
	// FileOutputStream fos = null;
	// try {
	// fos = new FileOutputStream(strMyImagePath);
	// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	//
	// fos.flush();
	// fos.close();
	// picturePath = strMyImagePath;
	// // MediaStore.Images.Media.insertImage(getContentResolver(), b,
	// // "Screen", "screen");
	// } catch (FileNotFoundException e) {
	//
	// e.printStackTrace();
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// }

	// private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException
	// {
	// BitmapFactory.Options o = new BitmapFactory.Options();
	// o.inJustDecodeBounds = true;
	// BitmapFactory.decodeStream(context.getContentResolver()
	// .openInputStream(selectedImage), null, o);
	// final int REQUIRED_SIZE = 200;
	// int width_tmp = o.outWidth, height_tmp = o.outHeight;
	// int scale = 1;
	// while (true) {
	// if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
	// break;
	// }
	// width_tmp /= 2;
	// height_tmp /= 2;
	// scale *= 2;
	// }
	// BitmapFactory.Options o2 = new BitmapFactory.Options();
	// o2.inSampleSize = scale;
	// return BitmapFactory.decodeStream(context.getContentResolver()
	// .openInputStream(selectedImage), null, o2);
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("", "onActivityResult");
		if (resultCode == context.RESULT_OK && requestCode == 1) {
			context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
		} else

		// if (requestCode == RESULT_LOAD_IMAGE
		// && resultCode == context.RESULT_OK && null != data) {
		// Uri selectedImage = data.getData();
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		// Cursor cursor = context.getContentResolver().query(selectedImage,
		// filePathColumn, null, null, null);
		// cursor.moveToFirst();
		// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		// picturePath = cursor.getString(columnIndex);
		// cursor.close();
		if (requestCode == RESULT_PRIVATE_IMAGE
				&& resultCode == Activity.RESULT_OK && data != null) {
			// picturePath = data.getStringExtra("single_path");
			// imageLoader.displayImage("file://" + single_path, imgSinglePick);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize =1;
			// Bitmap bitmap = imageOreintationValidator(
			// BitmapFactory.decodeFile(picturePath, options), picturePath);
			// LoadBitmap(BitmapFactory.decodeFile(picturePath), picturePath);
			// openDailogForSelectTime();
			// new UploadImage().execute(WebServiceConstants.BASE_URL_FILE
			// + WebServiceConstants.UPLOAD_FILE);
			// sendImage();
			// try {
			// // ProfilePic.setImageBitmap(decodeUri(selectedImage));
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// share_post_image = 2;
			String vImagePath = data.getStringExtra("single_path");

			picturePath = vImagePath;

			// System.out.println("Image Path.." + vImagePath);
			// imageFileName = new File(vImagePath);
			// bitmap = decodeUri(selectedImage);
			// share_image.setImageBitmap(bitmap);
			openDailogForSelectTime(AppConstants.MESSAGE_TYPE_IMAGE, false);
		} else if (requestCode == RESULT_CAMERA_IMAGE
				&& resultCode == context.RESULT_OK) {
			// picturePath=(String) data.getExtras().get("data");
			// Bitmap photo = (Bitmap) data.getExtras().get("data");
			// LoadBitmap(photo);
			// processPictureWhenReady(picturePath);
			// new UploadImage().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.UPLOAD_FILE);
			// sendImage();
			// ProfilePic.setImageBitmap(photo);

			// final File file = getTempFile(context);
			// picturePath=file.getAbsolutePath();
			// try {
			// // Bitmap captureBmp =
			// Media.getBitmap(context.getContentResolver(),
			// // Uri.fromFile(file));
			// new UploadImage().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.UPLOAD_FILE);
			// // do whatever you want with the bitmap (Resize, Rename, Add To
			// // Gallery, etc)
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 5;
			// Bitmap bitmap = imageOreintationValidator(
			// BitmapFactory.decodeFile(picturePath, options), picturePath);
			// LoadBitmap(bitmap, picturePath);
			// openDailogForSelectTime();
			// CompressBitmap bitmap2 = new CompressBitmap(picturePath, 90);
			// // System.out.println("file....." + bitmap2.getComressFile());
			// picturePath = bitmap2.getComressFile();
			// // System.out.println("Image Path.." + vImagePath);
			// // imageFileName = new File(vImagePath);
			// // bitmap = decodeUri(selectedImage);
			// // share_image.setImageBitmap(bitmap);
			// openDailogForSelectTime(AppConstants.MESSAGE_TYPE_IMAGE);
			// new UploadImage().execute(WebServiceConstants.BASE_URL_FILE
			// + WebServiceConstants.UPLOAD_FILE);
		} else if (requestCode == RESULT_VIDEO_ACTVITY
				&& resultCode == context.RESULT_OK) {
			picturePath = data.getStringExtra("path");
			if (data.getStringExtra("media_type").equals("image")) {
				CompressBitmap bitmap2 = new CompressBitmap(picturePath,
						data.getIntExtra("cameraType", 90));

				picturePath = bitmap2.getComressFile();
				openDailogForSelectTime(AppConstants.MESSAGE_TYPE_IMAGE, false);
			} else {
				new UploadFile().execute(WebServiceConstants.BASE_URL_FILE
						+ WebServiceConstants.UPLOAD_FILE,
						AppConstants.MESSAGE_TYPE_VIDEO);
			}

			// openDailogForSelectTime(AppConstants.MESSAGE_TYPE_VIDEO);
			// new UploadVideo().execute(WebServiceConstants.BASE_URL_FILE
			// + WebServiceConstants.UPLOAD_CHAT_VIDEO);

		}
	}

	// private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
	//
	// ExifInterface ei;
	// try {
	// ei = new ExifInterface(path);
	// int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
	// ExifInterface.ORIENTATION_NORMAL);
	// switch (orientation) {
	// case ExifInterface.ORIENTATION_ROTATE_90:
	// bitmap = rotateImage(bitmap, 90);
	// break;
	// case ExifInterface.ORIENTATION_ROTATE_180:
	// bitmap = rotateImage(bitmap, 180);
	// break;
	// case ExifInterface.ORIENTATION_ROTATE_270:
	// bitmap = rotateImage(bitmap, 270);
	// break;
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return bitmap;
	// }

	// private Bitmap rotateImage(Bitmap source, float angle) {
	//
	// Bitmap bitmap = null;
	// Matrix matrix = new Matrix();
	// matrix.postRotate(angle);
	// try {
	// bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
	// source.getHeight(), matrix, true);
	// } catch (OutOfMemoryError err) {
	// err.printStackTrace();
	// }
	//
	// return bitmap;
	// }

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		try {
			setUnreadMessageAsRead();
			context.mainview.setVisibility(View.VISIBLE);
			handler.removeCallbacks(runnable);
			context.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

			// for new friend added in freind list right panel
			boolean checkIsAvailableInFriendList = false;
			for (UserDTO dto : context.RecentlySearchedcity.searchDudeList) {
				if (dto.getUserID().equals(userDTO.getUserID())) {
					checkIsAvailableInFriendList = true;
					break;
				}
			}
			if (!(checkIsAvailableInFriendList) && chatList.size() > 0) {
				if (userDTO.getLastName().contains("null")) {
					userDTO.setLastName("");
				}
				if (userDTO.getUserProfileImageDTOs().size() != 0
						&& userDTO.getUserProfileImageDTOs() != null
						&& userDTO.getUserProfileImageDTOs().get(0)
								.getIsImageActive()) {

					userDTO.setProfileImage(userDTO.getUserProfileImageDTOs()
							.get(0).getImagePath());

				}

				context.RecentlySearchedcity.searchDudeList.add(0, userDTO);
			}
			// new UnreadMessageAsyncTaskr()
			// .execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.UNREAD_MESSAGE_SERVICE_OF_PARTICULAR_USER);

			System.gc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();

	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			Log.d("", "online thread start");

			Log.d("", "online thread stop");
			if (WebServiceConstants.isOnline(context)) {
				// edited for revision 608
				// MAY 20 2015
				// commented
				// new GetOnline().execute(userDTO.getUserID());
			}

		}
	};

	private void sendMessage() {
		// Log.d("ChatXmppUserId", ChatXmppUserId);
		try {
			if (WebServiceConstants.isOnline(context)
					&& ChatService.connection != null
					&& ChatService.connection.isConnected()) {
				String to = ChatXmppUserId + AppConstants.CHAT_SERVER_ID;
				String text = messageField.getText().toString();

				Log.i("XMPPClient", "Sending text [" + text + "] to [" + to
						+ "]");
				Message msg = new Message(to, Message.Type.chat);
				msg.setProperty("name", sessionManager.getUserDetail()
						.getFirstName());
				msg.setBody(text + AppConstants.MESSAGE_TYPE_TEXT);
				ChatService.connection.sendPacket(msg);

				ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
				chatDetailsDTO.setTouser(ChatXmppUserId);
				chatDetailsDTO.setFromuser(sessionManager.getUserDetail()
						.getUserID());
				chatDetailsDTO.setTime("" + System.currentTimeMillis());
				chatDetailsDTO.setMessage(messageField.getText().toString());
				chatDetailsDTO.setMeassageType(AppConstants.MESSAGE_TYPE_TEXT);
				chatHistoryChatDetailsDTO = chatDetailsDTO;
				long rowId = databaseHelper.addChat(chatDetailsDTO);
				chatDetailsDTO.setId("" + rowId);
				addNewMessage(chatDetailsDTO);
				messageField.setText("");
				new SaveChatHistory().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.SEND_MESSAGE_TO_SERVER);
			} else {
				startUserCurrentLocationService();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// InputMethodManager imm = (InputMethodManager) context
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
	}

	private void sendLocation() {
		Log.d("ChatXmppUserId", ChatXmppUserId);
		try {
			if (WebServiceConstants.isOnline(context)
					&& ChatService.connection != null
					&& ChatService.connection.isConnected()) {
				String to = ChatXmppUserId + AppConstants.CHAT_SERVER_ID;
				String text = GpsService.latitude + "|" + GpsService.longitude
						+ "|" + GpsService.street + " " + GpsService.city + " "
						+ GpsService.state + ", " + GpsService.country;

				Log.i("XMPPClient", "Sending text [" + text + "] to [" + to
						+ "]");
				Message msg = new Message(to, Message.Type.chat);
				msg.setProperty("name", sessionManager.getUserDetail()
						.getFirstName());
				msg.setBody(text + AppConstants.MESSAGE_TYPE_LOCATION);
				ChatService.connection.sendPacket(msg);

				ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
				chatDetailsDTO.setTouser(ChatXmppUserId);
				chatDetailsDTO.setFromuser(sessionManager.getUserDetail()
						.getUserID());
				chatDetailsDTO.setTime("" + System.currentTimeMillis());
				chatDetailsDTO.setMessage(text);
				chatDetailsDTO
						.setMeassageType(AppConstants.MESSAGE_TYPE_LOCATION);
				chatHistoryChatDetailsDTO = chatDetailsDTO;
				long rowId = databaseHelper.addChat(chatDetailsDTO);
				chatDetailsDTO.setId("" + rowId);
				addNewMessage(chatDetailsDTO);
				messageField.setText("");
				new SaveChatHistory().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.SEND_MESSAGE_TO_SERVER);
			} else {
				startUserCurrentLocationService();
			}
		} catch (Exception e) {

		}
		// InputMethodManager imm = (InputMethodManager) context
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
	}

	public void openDailogForSelectTime(final String postType,
			final boolean isPrivateImage) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_select_time);
		// Do you realy want to delete your account ?
		ListView list = (ListView) dialog.findViewById(R.id.list);

		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText(R.string.dialog_please_select_time_limit);
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		title.setTypeface(typeface);
		String array[] = { getResources().getString(R.string.chat_3_seconds),
				getResources().getString(R.string.chat_7_seconds),
				getResources().getString(R.string.chat_11_seconds),
				getResources().getString(R.string.chat_unlimited) };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				R.layout.row_select_time, R.id.text, array);

		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				snapChatSecounds = "";
				switch (position) {
				case 0:
					snapChatSecounds = AppConstants.SNAP_CHAT_CONSTANTS + "3";
					break;
				case 1:
					snapChatSecounds = AppConstants.SNAP_CHAT_CONSTANTS + "7";
					break;
				case 2:
					snapChatSecounds = AppConstants.SNAP_CHAT_CONSTANTS + "11";
					break;

				}
				dialog.dismiss();
				// if image is private image then there is no need to upload
				// image on server only image url is share.
				if (isPrivateImage) {
					sendImage(picturePath);
				} else {
					new UploadFile().execute(WebServiceConstants.BASE_URL_FILE
							+ WebServiceConstants.UPLOAD_FILE, postType);
				}

			}
		});
		dialog.show();
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
		title.setText(R.string.dialog_do_you_really_want_to_delete_chat_history);
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
				// new DeleteChatHistoryOfParticular()
				// .execute(WebServiceConstants.BASE_URL
				// + WebServiceConstants.DELETE_CHAT);

				new DeleteChatHistoryOfParticular()
						.execute(WebServiceConstants.DELETE_CHAT_HISTORY_OF_PARTICULAR_USER);

				// databaseHelper.clearChatOfUser(ChatXmppUserId, sessionManager
				// .getUserDetail().getUserID());
				// onResume();
				dialog.dismiss();
				// previousDate = "";
				// Toast.makeText(context, "Chat history successfully deleted",
				// Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}

	public String savePhoto(Bitmap bmp) {
		File imageFileFolder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath(), "/krave/");
		imageFileFolder.mkdir();
		FileOutputStream out = null;
		Calendar c = Calendar.getInstance();
		// String date = fromInt(c.get(Calendar.MONTH))
		// + fromInt(c.get(Calendar.DAY_OF_MONTH))
		// + fromInt(c.get(Calendar.YEAR))
		// + fromInt(c.get(Calendar.HOUR_OF_DAY))
		// + fromInt(c.get(Calendar.MINUTE))
		// + fromInt(c.get(Calendar.SECOND));
		// imageFileName = new File(imageFileFolder, date.toString() + ".jpg");
		File imageFileName = new File(imageFileFolder, "video_temp_image"
				+ ".jpg");
		try {
			out = new FileOutputStream(imageFileName);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			scanPhoto(imageFileName.toString());
			out = null;
			// mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageFileName.getAbsolutePath();
	}

	private MediaScannerConnection msConn;

	public void scanPhoto(final String imageFileName) {
		msConn = new MediaScannerConnection(context,
				new MediaScannerConnectionClient() {
					public void onMediaScannerConnected() {
						msConn.scanFile(imageFileName, null);
						Log.i("msClient obj  in Photo Utility",
								"connection established");
					}

					@Override
					public void onScanCompleted(String arg0, Uri arg1) {
						// TODO Auto-generated method stub
						msConn.disconnect();
					}
				});
		msConn.connect();
	}

	public String fromInt(int val) {
		return String.valueOf(val);
	}

	// public void openDailogToSendImage() {
	// final Dialog dialog = new Dialog(context,
	// android.R.style.Theme_Translucent_NoTitleBar);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.dialog_logout);
	// // Do you realy want to delete your account ?
	// Button cancle = (Button) dialog.findViewById(R.id.cancle);
	// Button ok = (Button) dialog.findViewById(R.id.ok);
	// TextView title = (TextView) dialog.findViewById(R.id.title);
	// title.setText("Do you really want to delete your account ?");
	// Typeface typeface = FontStyle.getFont(context,
	// AppConstants.HelveticaNeueLTStd_Lt);
	// title.setTypeface(typeface);
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
	// Intent intent = new Intent(context, GpsService.class);
	// context.stopService(intent);
	// Intent inten1 = new Intent(context, ChatService.class);
	// context.stopService(inten1);
	// // new DeleteAccountAsyncTask()
	// // .execute(WebServiceConstants.BASE_URL
	// // + WebServiceConstants.DELETE_ACCOUNT);
	//
	// }
	// });
	// dialog.show();
	// }

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

			// String posturl =
			// "http://198.12.150.189:9090/plugins/presence/status?jid="+arg0[0]+"@ip-198-12-150-189.secureserver.net&type=text";
			//
			// DefaultHttpClient httpclient = new DefaultHttpClient();
			// HttpGet httpget = new HttpGet(posturl);
			// HttpResponse response = httpclient.execute(httpget);
			// BufferedReader in = new BufferedReader(new InputStreamReader(
			// response.getEntity().getContent()));
			// StringBuffer sb = new StringBuffer("");
			// String line = "";
			// String NL = System.getProperty("line.separator");
			// while ((line = in.readLine()) != null) {
			// sb.append(line + NL);
			// }
			// in.close();
			// String result = sb.toString();
			// Log.v("My Response :: ", result);
			//
			// return result;
			// } catch (UnsupportedEncodingException e) {
			// // writing error to Log
			// Log.v("Message...1", e.getMessage());
			// e.printStackTrace();
			// } catch (ClientProtocolException e) {
			// Log.v("Message...2", e.getMessage());
			// // writing exception to log
			// e.printStackTrace();
			// } catch (IOException e) {
			// Log.v("Message...3", e.getMessage());
			// // writing exception to log
			// e.printStackTrace();
			// }

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
					onlineImageView.setBackgroundResource(R.drawable.online);
					isOnline = true;
				} else if (result.equals("unavailable")) {
					onlineImageView.setBackgroundResource(R.drawable.white_crl);
					// filterList.get(poss).setIsOnline(AppConstants.OFFLINE);
				} else if (result.equals("error")) {
					onlineImageView.setBackgroundResource(R.drawable.white_crl);
					// filterList.get(poss).setIsOnline(AppConstants.OFFLINE);
				}
			} catch (Exception e) {

			}
			// new GetOnline().execute(userDTO.getUserID());
			handler.postDelayed(runnable, 30000);
		}
	}

	class SaveChatHistory extends AsyncTask<String, Void, JSONArray> {
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

			// http://198.12.150.189/~simssoe/index.php?action=conversation_reply&user_one=4&user_two=1&time=14526987355&&reply=hello&msg_type=""&ip=182.72.80.18
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_one", ""
					+ chatHistoryChatDetailsDTO.getFromuser()));
			params.add(new BasicNameValuePair("user_two", ""
					+ chatHistoryChatDetailsDTO.getTouser()));
			params.add(new BasicNameValuePair("time", ""
					+ chatHistoryChatDetailsDTO.getTime()));
			params.add(new BasicNameValuePair("reply", ""
					+ chatHistoryChatDetailsDTO.getMessage()));
			params.add(new BasicNameValuePair("msg_type", ""
					+ chatHistoryChatDetailsDTO.getMeassageType()));

			params.add(new BasicNameValuePair("ip", ""));

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
				// vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				System.out.print("get dude response : " + jsonArray);

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}

	class UploadFile extends AsyncTask<String, Void, String> {
		// private JSONArray jsonArray;
		// private JSONObject jsonObject;
		// private String vStatus;
		private String post_type;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected String doInBackground(String... args) {
			String vResult = "";
			try {
				post_type = args[1];
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(args[0]);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				if (post_type.equals(AppConstants.MESSAGE_TYPE_IMAGE)) {
					reqEntity.addPart("post_type", new StringBody("image"));
					reqEntity.addPart("doc",
							new FileBody(new File(picturePath)));
				} else {
					reqEntity.addPart("post_type", new StringBody("video"));
					Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
							picturePath, MediaStore.Video.Thumbnails.MINI_KIND);
					String videoThumbnailPath = savePhoto(bitmap);

					reqEntity.addPart("doc", new FileBody(new File(
							videoThumbnailPath)));
					reqEntity.addPart("video_path", new FileBody(new File(
							picturePath)));
				}
				// reqEntity.addPart("doc", new FileBody(new
				// File(picturePath)));
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

			// 08-04 12:32:11.412: I/System.out(8974): newWidth....150
			// 08-04 12:32:30.272: I/System.out(8974): Responce....Admin
			// Reg.String[{"status":"success","filename":"krave_image/krave_doc/1407135708_test6302014173048.png","extension":"png"}]

			System.out.println("Responce....Admin Reg.String" + result);
			// [{"status":"Success","admin_unique_id":"admin4"}]
			try {
				JSONArray jsonArray = new JSONArray(result);

				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String vStatus = jsonObject.getString("status");
				if (vStatus.equals("success")) {
					if (post_type.equals(AppConstants.MESSAGE_TYPE_IMAGE)) {
						String url = jsonObject.getString("filename");

						sendImage(url);

					} else {
						String thumbUrl = jsonObject.getString("thumb_url");
						String videoUrl = jsonObject.getString("video_url");

						sendVideo(thumbUrl, videoUrl);

					}
				} else {
					Toast.makeText(context, R.string.toast_unsupported_file,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(context, R.string.toast_unsupported_file,
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	// class UploadVideo extends AsyncTask<String, Void, String> {
	// // private JSONArray jsonArray;
	// // private JSONObject jsonObject;
	// // private String vStatus;
	// TransparentProgressDialog dialog;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog = new TransparentProgressDialog(context);
	// // dialog.setMessage("Please Wait...");
	// // dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	// }
	//
	// protected String doInBackground(String... args) {
	// String vResult = "";
	// try {
	// HttpClient client = new DefaultHttpClient();
	// HttpPost post = new HttpPost(args[0]);
	//
	// MultipartEntity reqEntity = new MultipartEntity(
	// HttpMultipartMode.BROWSER_COMPATIBLE);
	//
	// reqEntity.addPart("video", new FileBody(new File(picturePath)));
	// post.setEntity(reqEntity);
	// HttpResponse response = client.execute(post);
	// StringBuffer stringBuffer = new StringBuffer("");
	// BufferedReader rd = new BufferedReader(new InputStreamReader(
	// response.getEntity().getContent()));
	// String line = "";
	// String LineSeparator = System.getProperty("line.separator");
	// while ((line = rd.readLine()) != null) {
	// stringBuffer.append(line + LineSeparator);
	// }
	// vResult = stringBuffer.toString();
	//
	// } catch (UnsupportedEncodingException e) {
	// // writing error to Log
	// Log.v("Message...1", e.getMessage());
	// e.printStackTrace();
	// } catch (ClientProtocolException e) {
	// Log.v("Message...2", e.getMessage());
	// // writing exception to log
	// e.printStackTrace();
	// } catch (IOException e) {
	// Log.v("Message...3", e.getMessage());
	// // writing exception to log
	// e.printStackTrace();
	// }
	// return vResult;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// dialog.dismiss();
	//
	// // 08-04 12:32:11.412: I/System.out(8974): newWidth....150
	// // 08-04 12:32:30.272: I/System.out(8974): Responce....Admin
	// //
	// Reg.String[{"status":"success","filename":"krave_image/krave_doc/1407135708_test6302014173048.png","extension":"png"}]
	//
	// System.out.println("Responce....Admin Reg.String" + result);
	// // [{"status":"Success","admin_unique_id":"admin4"}]
	// try {
	// JSONArray jsonArray = new JSONArray(result);
	//
	// JSONObject jsonObject = jsonArray.getJSONObject(0);
	// String vStatus = jsonObject.getString("status");
	// if (vStatus.equals("success")) {
	// String url = jsonObject.getString("filename");
	// try {
	// sendImage(url);
	// } catch (Exception exc) {
	//
	// }
	// } else {
	// Toast.makeText(context, "Unsupported file",
	// Toast.LENGTH_SHORT).show();
	// }
	// } catch (JSONException e) {
	// Toast.makeText(context, "Unsupported file", Toast.LENGTH_SHORT)
	// .show();
	// }
	//
	// }
	// }

	// class UploadFile extends AsyncTask<String, Void, JSONArray> {
	// String vStatus;
	// TransparentProgressDialog dialog;
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
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	//
	// params.add(new BasicNameValuePair("doc", "" + args[1]));
	//
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	// Log.d("get user response", "" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	// dialog.dismiss();
	// try {
	// JSONObject mJsonObject = jsonArray.getJSONObject(0);
	// // vStatus = mJsonObject.getString("status");
	// System.out.print("" + jsonArray);
	// // if (vStatus.equals("success")) {
	// System.out.print("get dude response : " + jsonArray);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// Toast.makeText(context, "Dudes data not found",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// }
	// }

	class GetDudeAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", "" + ChatXmppUserId));

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

				for (int i = 0; i < jsonArray.length(); i++) {
					userDTO = parseUserDataAndSetInSession(jsonArray
							.getJSONObject(i));
				}
				loadData();
				adapter.notifyDataSetChanged();
				viewDudeProfileLayout.setEnabled(true);
				blockDudeLayout.setEnabled(true);

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(context, R.string.toast_dudes_data_not_found,
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private UserDTO parseUserDataAndSetInSession(JSONObject mJsonObject)
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
			// MAY 15 2015
			// added if statement
			if (!interestJsonObject.getString("intrests_image").isEmpty()) {
				interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
						+ interestJsonObject.getString("intrests_image"));
			}
			// - -- - - - -

			interestsDTOs.add(interestsDTO);

		}

		// dl edited on revision 591
		// May 14 2015 9:50 AM
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
			// - -- -
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

	class DeleteChatHistoryOfParticular extends
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

			// http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002
			// user_id=12668&friend_ids[0]=12903
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("friend_ids[0]", ""
					+ ChatXmppUserId));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("delete chat response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			// [{"MSG":"Deleted","S":200}]

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("S");
				System.out.print("" + jsonArray);
				if (vStatus.equals("200")) {
					databaseHelper.clearChatOfUser(ChatXmppUserId,
							sessionManager.getUserDetail().getUserID());

					previousDate = "";
					getChatFromDatabse();
					Toast.makeText(context,
							R.string.toast_chat_history_successfully_deleted,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context,
							R.string.toast_chat_history_not_deleted_try_again,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(context,
						R.string.toast_chat_history_not_deleted_try_again,
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	class UnreadMessageAsyncTaskr extends AsyncTask<String, Void, JSONArray> {
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

			// 198.12.150.189/~simssoe/index.php?action=updateReadStatus&user_id=4&friend_id=108
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("friend_id", "" + ChatXmppUserId));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("unread message response", "" + json);
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

					// int count = Integer.valueOf(mJsonObject
					// .getString("countread"));
					// GpsService.unreadMsgCount = GpsService.unreadMsgCount
					// - count;
					// singleton.getChatCount.put(ChatXmppUserId, 0);
					// Intent broadcast = new Intent();
					// broadcast.putExtra("come", "notification");
					// broadcast.setAction(BROADCAST_ACTION);
					// context.sendBroadcast(broadcast);

				} else {
					// Toast.makeText(context,
					// "Chat history not  deleted,try again",
					// Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(context,
						R.string.toast_chat_history_not_deleted_try_again,
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	public class GetUserList extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			dialog.show();
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

				if (userListDTOs.size() == 0) {
					Toast.makeText(context, R.string.toast_no_list,
							Toast.LENGTH_SHORT).show();
					return;
				}

				userListAdapter = new UserListAdapter(userListDTOs, context, 0);
				ListView listView = new ListView(context);
				listView.setAdapter(userListAdapter);

				final Dialog dialog = new Dialog(context);
				dialog.setTitle(R.string.dialog_add_to_list);
				dialog.setContentView(listView);

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						userListDTO = userListDTOs.get(arg2);
						if (WebServiceConstants.isOnline(context)) {
							new AddDudes().execute(WebServiceConstants.BASE_URL
									+ WebServiceConstants.ADD_DUDES_IN_LIST);
						}
						dialog.dismiss();
					}
				});

				dialog.show();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public class AddDudes extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(context);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("list_id", userListDTO
					.getListId()));

			params.add(new BasicNameValuePair("adddude[" + 0 + "][]", String
					.valueOf(userDTO.getUserID())));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("add dude response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			try {

				JSONObject mainJsonObject = jsonArray.getJSONObject(0);
				vStatus = mainJsonObject.getString("status");
				if (vStatus.equals("success")) {
					Toast.makeText(context,
							R.string.toast_dude_successfully_added,
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(context, R.string.toast_dude_not_added,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private UserDTO parseUserData(JSONObject mJsonObject) throws JSONException {
		UserDTO userDTO = new UserDTO();
		// AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		LatLongDTO latLongDTO = new LatLongDTO();
		JSONObject MainObject = mJsonObject.getJSONObject("userinfo");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		// System.out.println(MainObject);
		// userDTO.setLanguage(MainObject.getString("user_language"));
		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));

		/* uncomment to show exclamation mark -av */
		// userDTO.setUnreadMsg(MainObject.getString("unread_msg"));

		try {
			if (!MainObject.getString("user_message").equals("null")) {
				if (MainObject.getString("user_message").contains(
						AppConstants.BASE_IMAGE_PATH_1)) {
					userDTO.setUserLastMsg("PHOTO");

				} else {
					userDTO.setUserLastMsg(MainObject.getString("user_message"));

				}
			}
		} catch (Exception e) {
		}

		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
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
		try {
			whatAreYouDTO.setWhatAreYouName(MainObject
					.getString("user_whatAreYou_name"));
		} catch (Exception e) {
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

			// edited for revision 607
			// MAY 15 2015
			// added if statement
			if (!interestJsonObject.getString("intrests_image").isEmpty()) {
				interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
						+ interestJsonObject.getString("intrests_image"));
			}
			// -- - - - - -
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
			// -- - - --
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
		userDTO.setInterestList(interestsDTOs);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		// sessionManager.setUserDetail(userDTO);
		return userDTO;

	}

	// pagination on scroll view chat history

	class GetChatHistoryByPagination extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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

			// user_one=12668&user_two=12903&page=2&limit=5&offset=0

			params.add(new BasicNameValuePair("user_one", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("user_two", ChatXmppUserId));
			params.add(new BasicNameValuePair("page", "" + pageNumber));
			params.add(new BasicNameValuePair("limit", "" + limit));
			params.add(new BasicNameValuePair("offset", "" + offset));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("CHAT HISTORY :", "CHAT HISTORY : currentPage=" + pageNumber
					+ " DATA=" + json);

			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {

			super.onPostExecute(jsonArray);
			// try {
			// // dialog.dismiss();
			// singleton.stopLoading(llLoading);
			// } catch (Exception e) {
			//
			// }
			// {"conver_reply_id":"24957","message":"koko","from_user":"12903","to_user":"12668",
			// "time":"1436859840901","server_time":"1436880036","msg_type":"
			// <text>","opt_one":"12668","opt_two":"12903","conversation":"10013","read_status":"0"}]
			try {

				Log.d("", "jsonArray.length()=" + jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject mJsonObject = jsonArray.getJSONObject(i);
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

					formatDate(chatDetailsDTO);
					// databaseHelper.addChat(chatDetailsDTO);
					if (chatList.size() != 0) {
						ChatDetailsDTO firstPositionMessage = chatList.get(0);
						if (!firstPositionMessage.getDate().equals(
								chatDetailsDTO.getDate())) {
							addDateLayoutInChatList(
									firstPositionMessage.getDate(), 0);
						}
					}
					int size = chatList.size();
					chatList.add(0, chatDetailsDTO);
					// if (size == 0) {
					// addDateLayoutInChatList(chatDetailsDTO.getDate(), 0);
					// }

				}

				adapter.notifyDataSetChanged();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (!(pageNumber <= 0)) {
					pageNumber--;
				}
				Toast.makeText(context, R.string.toast_no_message_found,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			if (chatList.size() == 0) {
				headerDateTimeLayout.setVisibility(View.GONE);
			} else {
				headerDateTimeLayout.setVisibility(View.VISIBLE);

			}
			if (mSwipyRefreshLayout.isRefreshing()) {
				mSwipyRefreshLayout.setRefreshing(false);
			}

		}
	}

	private void addDateLayoutInChatList(String date, int position) {

		ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
		chatDetailsDTO.setId("-1");
		chatDetailsDTO.setDate(date);
		chatList.add(position, chatDetailsDTO);

	}

	public void formatDate(ChatDetailsDTO chatDetailsDTO) {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
		Date date2 = new Date(Long.valueOf(chatDetailsDTO.getTime()));
		String newDateMessageDate = dateFormat1.format(date2);
		chatDetailsDTO.setDate(newDateMessageDate);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		try {
			if (chatList != null) {
				ChatDetailsDTO dto = chatList.get(firstVisibleItem);
				// if(dto.getId().equals("-1")){
				// headerDateTimeLayout.setVisibility(View.GONE);
				// }else{
				// headerDateTimeLayout.setVisibility(View.VISIBLE);
				// }
				headerDateTime.setText(dto.getDate());
			}
		} catch (Exception e) {
			headerDateTime.setText("");
		}

	}
}
