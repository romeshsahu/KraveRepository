package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ps.adapters.ChatMatchesAdapter;
import com.ps.adapters.SearchByAdapter;
import com.ps.adapters.UpdateUserListAdapter;
import com.ps.adapters.UserListAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveDAO;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentChatFriend extends Fragment implements OnClickListener,
		OnPageChangeListener {
	/* resources */
	private SessionManager sessionManager;
	private ImageLoaderCircle imageLoaderCircle;
	private Activity_Home context;
	ChatMatchesAdapter chatMatchesAdapter;
	// FindDudesAdapter findDudesAdapter;
	UpdateUserListAdapter updateUserListAdapter;
	SearchByAdapter subListAdapter;

	/* Layout Views */
	private ImageView backButton, plusButton;
	private ImageView faveTabButton, listTabButton, gridViewButton,
			listViewButton, editButton, unFaveButton, addToListButton,
			btnListBack, btnCreateList, deleteListButton, selectAllButton,
			selectAllButton2, removeFromListButton;
	private TextView totaldude, tvListTitle;
	private EditText etCreateList;
	private GridView friendListView;
	private ViewPager viewPager;
	List<UserDTO> searchDudeList = new ArrayList<UserDTO>();
	private List<UserDTO> selectedUserDTOs = new ArrayList<UserDTO>();
	private List<UserListDTO> selectedUserListDTOs = new ArrayList<UserListDTO>();
	private RelativeLayout menuLayout, rlCreateList;
	private LinearLayout delteAllHistoryLayout, cancleLayout;

	private ImageView onlineDudesFilter;
	// public static ChatDetailsDTO chatDetailsDTO;
	/* dto variables initialization */
	private LinearLayout onlyOnlineDudeLayout;
	private EditText searchFriends;
	private ImageView clearSearchResult;
	private boolean filterIsOnline = false;
	private AppManager singleton;

	private View mDownView;
	private float mDownX;
	private int mDownPosition;
	private VelocityTracker mVelocityTracker;
	private float downX, downY, upX, upY;
	static final int MIN_DISTANCE = 100;

	private LinearLayout linearFooter, linearFooterDel, llSelectAll,
			linearFooterRemoveFromList, llSelectAll2;

	private Boolean isSelectAll = false;
	private Boolean isFromCreateList = false;

	private int faveMode = 00;
	private int faveEdit = -11;
	private int faveView = 3333;

	private int MODE_NULL = -111;
	private int MODE_FAVORITE = 000;
	private int MODE_LIST = 111;
	private int MODE_SUBLIST = 222;

	private int EDIT_NULL = -11;
	private int EDIT_FAVORITES = 22;
	private int EDIT_LISTS = 33;
	private int EDIT_SUBLIST = 44;

	private int VIEW_GRID = 3333;
	private int VIEW_LIST = 4444;

	private ImageView loadingView;
	private LinearLayout llLoading;
	private int loadingCount = 0;

	List<UserListDTO> userListDTOs = new ArrayList<UserListDTO>();
	UserListAdapter userListAdapter, userListAdapterEdit;
	UserListDTO userListDTO;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat_friend, container,
				false);
		context = (Activity_Home) getActivity();
		sessionManager = new SessionManager(context);
		setLayout(view);
		setListeners();
		setTypeFace(view);
		context.mainview.setVisibility(View.VISIBLE);
		if (searchDudeList.size() == 0) {
			if (WebServiceConstants.isOnline(context)) {
				// new GetAllLikeDude().execute(WebServiceConstants.BASE_URL
				// + WebServiceConstants.GET_ALL_LIKE_DUDE);

				/* this uses kosa's API to get all faved dudes */
				new GetAllLikeDude()
						.execute("http://54.219.211.237/KraveAPI/api_calls/get-all-favorite-users.php");
				/* get list */
				new GetUserList().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_USER_LIST);
			} else {
				Toast.makeText(context, "Please check network connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			chatMatchesAdapter.notifyDataSetChanged();
			// count.setText("" + searchDudeList.size() + " Friend");
		}

		return view;
	}

	private void setTypeFace(View view) {
		TextView textView1, textView5, text1, text2;

		textView1 = (TextView) view.findViewById(R.id.dudeName);
		textView5 = (TextView) view.findViewById(R.id.textView1);
		text1 = (TextView) view.findViewById(R.id.text1);
		text2 = (TextView) view.findViewById(R.id.text2);
		etCreateList = (EditText) view.findViewById(R.id.etCreateList);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);
		textView1.setTypeface(typeface);
		textView5.setTypeface(typeface);
		text1.setTypeface(typeface);
		text2.setTypeface(typeface);
		etCreateList.setTypeface(typeface);
		searchFriends.setTypeface(typeface);
		// count.setTypeface(typeface);

		etCreateList.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				btnCreateList
						.setImageResource(etCreateList.getText().toString()
								.length() == 0 ? R.drawable.av_new_fave_createup
								: R.drawable.av_new_fave_createdown);
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
	}

	private void setLayout(View view) {

		LinearLayout mainview = (LinearLayout) view.findViewById(R.id.mainview);
		linearFooter = (LinearLayout) view.findViewById(R.id.linearFooter);
		linearFooterDel = (LinearLayout) view
				.findViewById(R.id.linearFooterDel);
		llSelectAll = (LinearLayout) view.findViewById(R.id.llSelectAll);
		llSelectAll2 = (LinearLayout) view.findViewById(R.id.llSelectAll2);
		linearFooterRemoveFromList = (LinearLayout) view
				.findViewById(R.id.linearFooterRemoveFromList);

		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		changeHeaderTitle(false, "Favorites"); // set title to favorites -pat

		backButton = (ImageView) view.findViewById(R.id.backButton);
		plusButton = (ImageView) view.findViewById(R.id.plusbutton);

		faveTabButton = (ImageView) view.findViewById(R.id.btnFavoritesView);
		listTabButton = (ImageView) view.findViewById(R.id.btnListsView);

		gridViewButton = (ImageView) view.findViewById(R.id.btnGrid);
		listViewButton = (ImageView) view.findViewById(R.id.btnLinear);

		btnCreateList = (ImageView) view.findViewById(R.id.btnCreateList);
		btnListBack = (ImageView) view.findViewById(R.id.btnListBack);
		tvListTitle = (TextView) view.findViewById(R.id.tvListTitle);
		deleteListButton = (ImageView) view.findViewById(R.id.btnDeleteList);

		editButton = (ImageView) view.findViewById(R.id.btnEdit);
		unFaveButton = (ImageView) view.findViewById(R.id.btnUnfave);
		addToListButton = (ImageView) view.findViewById(R.id.btnAddToList);
		selectAllButton = (ImageView) view.findViewById(R.id.edSelectDude);
		selectAllButton2 = (ImageView) view.findViewById(R.id.edSelectDude2);
		removeFromListButton = (ImageView) view
				.findViewById(R.id.btnRemoveFromList);

		// count = (TextView) view.findViewById(R.id.count);
		friendListView = (GridView) view.findViewById(R.id.chatListView);
		menuLayout = (RelativeLayout) view.findViewById(R.id.menu);

		rlCreateList = (RelativeLayout) view.findViewById(R.id.rlCreateList);

		onlineDudesFilter = (ImageView) view.findViewById(R.id.filter);
		onlyOnlineDudeLayout = (LinearLayout) view
				.findViewById(R.id.filterLayout);

		searchFriends = (EditText) view.findViewById(R.id.serchCity);
		clearSearchResult = (ImageView) view
				.findViewById(R.id.clearSearchResult);
		delteAllHistoryLayout = (LinearLayout) view
				.findViewById(R.id.deleteAllHistory);
		cancleLayout = (LinearLayout) view.findViewById(R.id.cancleLayout);

		// chatMatchesAdapter = new ChatMatchesAdapter(context, searchDudeList,
		// 0); //linear

		chatMatchesAdapter = new ChatMatchesAdapter(context, searchDudeList, 2); // grid
		friendListView.setNumColumns(2); // for grid

		friendListView.setAdapter(chatMatchesAdapter);

		// to scroll the list view to bottom on data change

		/* init list adapter */
		userListAdapter = new UserListAdapter(userListDTOs, context, 0);
		userListAdapterEdit = new UserListAdapter(userListDTOs, context, 1);

		/* init Loading Screen */
		loadingView = (ImageView) view.findViewById(R.id.loadingView);
		llLoading = (LinearLayout) view.findViewById(R.id.llLoading);

		/* av singleton */
		AppManager.initInstance();
		singleton = AppManager.getInstance();
	}

	private void setListeners() {
		backButton.setOnClickListener(this);
		plusButton.setOnClickListener(this);

		faveTabButton.setOnClickListener(this);
		listTabButton.setOnClickListener(this);
		gridViewButton.setOnClickListener(this);
		listViewButton.setOnClickListener(this);
		editButton.setOnClickListener(this);

		btnCreateList.setOnClickListener(this);
		delteAllHistoryLayout.setOnClickListener(this);
		cancleLayout.setOnClickListener(this);
		onlineDudesFilter.setOnClickListener(this);
		clearSearchResult.setOnClickListener(this);

		unFaveButton.setOnClickListener(this);
		addToListButton.setOnClickListener(this);
		deleteListButton.setOnClickListener(this);

		llSelectAll.setOnClickListener(this);
		llSelectAll2.setOnClickListener(this);
		removeFromListButton.setOnClickListener(this);

		btnListBack.setOnClickListener(this);

		// friendListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		// System.out.println("arg2 : "+arg2);
		// FragmentChatMatches.backButtonComeFrom = true;
		// FragmentChatMatches.userDTO = (UserDTO) chatMatchesAdapter
		// .getItem(arg2);
		// context.Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
		//
		// }
		// });

		friendListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					System.out.println("ACTION DOWN");

					downX = event.getX();
					downY = event.getY();

					// Find the child view that was touched (perform a hit test)
					Rect rect = new Rect();
					int childCount = friendListView.getChildCount();
					int[] listViewCoords = new int[2];
					friendListView.getLocationOnScreen(listViewCoords);
					int x = (int) event.getRawX() - listViewCoords[0];
					int y = (int) event.getRawY() - listViewCoords[1];
					View child;
					for (int i = 0; i < childCount; i++) {
						child = friendListView.getChildAt(i);
						child.getHitRect(rect);
						if (rect.contains(x, y)) {
							mDownView = child; // This is your down view
							break;
						}
					}

					if (mDownView != null) {
						mDownX = event.getRawX();
						mDownPosition = friendListView
								.getPositionForView(mDownView);

						mVelocityTracker = VelocityTracker.obtain();
						mVelocityTracker.addMovement(event);
					}
					v.onTouchEvent(event);

					return true;
				}
				case MotionEvent.ACTION_MOVE: {
					return false; // enable listview Scrolling
				}
				case MotionEvent.ACTION_UP: {
					System.out.println("ACTION UP");

					upX = event.getX();
					upY = event.getY();

					float deltaX = downX - upX;
					float deltaY = downY - upY;

					if (Math.abs(deltaX) > Math.abs(deltaY)) { // left or right
																// swipe (
																// UNFAVE )
						// if (Math.abs(deltaX) > MIN_DISTANCE) {
						// if (deltaX < 0) { //right
						// System.out.println("SwipeRight");
						//
						// doRemoveFaveWithAnim(300);
						//
						// return true;
						// }
						// if (deltaX > 0) { //left
						// System.out.println("SwipeLeft");
						//
						// doRemoveFaveWithAnim(-300);
						//
						// return true;
						// }
						// }

					} else if (Math.abs(deltaY) > Math.abs(deltaX)) { // top or
																		// bot
						// if (Math.abs(deltaY) > MIN_DISTANCE) {

						// if (deltaY > 0) { //top
						// System.out.println("SwipeTop");
						// return false;
						// }
						// if (deltaY < 0) { //bot
						// System.out.println("SwipeBot");
						// return false;
						// }
						// }
					} else { // CLICK
						if (faveMode == MODE_SUBLIST
								&& faveEdit == EDIT_SUBLIST) {
							/* SUBLISTS EDIT ON */
							UserDTO userListDTO = (UserDTO) subListAdapter
									.getItem(mDownPosition);

							if (userListDTO.isSelected()) {
								selectedUserDTOs.remove(userListDTO);
								userListDTO.setSelected(false);
							} else {
								selectedUserDTOs.add(userListDTO);
								userListDTO.setSelected(true);
							}
							subListAdapter.notifyDataSetChanged();
							isSelectAll = false;
							selectAllButton2
									.setBackgroundResource(R.drawable.av_new_favorites_checkup);

						} else if (faveMode == MODE_LIST
								&& faveEdit != EDIT_LISTS) {
							/* LISTS EDIT OFF */
							faveMode = MODE_SUBLIST;
							faveEdit = EDIT_NULL;

							btnListBack.setVisibility(View.VISIBLE);
							tvListTitle.setVisibility(View.VISIBLE);

							subListAdapter = new SearchByAdapter(context,
									userListAdapter.getItem(mDownPosition)
											.getDudeList(),
									faveView == VIEW_LIST ? 0 : 6);
							friendListView
									.setNumColumns(faveView == VIEW_LIST ? 1
											: 2);
							friendListView.setAdapter(subListAdapter);

							UserListDTO userDTO = (UserListDTO) userListAdapter
									.getItem(mDownPosition);
							tvListTitle.setText(userDTO.getListName());

							userListDTO = userListDTOs.get(mDownPosition);

							rlCreateList.setVisibility(View.GONE);
						} else if (faveMode == MODE_LIST
								&& faveEdit == EDIT_LISTS) {
							/* LISTS EDIT ON */
							UserListDTO userListDTO = (UserListDTO) userListAdapterEdit
									.getItem(mDownPosition);

							System.out.println(userListDTO.getListId()); // get
																			// list
																			// ID
							if (userListDTO.isSelected()) {
								selectedUserListDTOs.remove(userListDTO);
								userListDTO.setSelected(false);
							} else {

								selectedUserListDTOs.add(userListDTO);
								userListDTO.setSelected(true);
							}
							userListAdapterEdit.notifyDataSetChanged();
							isSelectAll = false;
							selectAllButton
									.setBackgroundResource(R.drawable.av_new_favorites_checkup);
						} else if (faveMode == MODE_FAVORITE
								&& faveEdit == EDIT_FAVORITES) {
							/* FAVE EDIT ON */
							UserDTO userDTO = (UserDTO) updateUserListAdapter
									.getItem(mDownPosition);

							System.out.println(userDTO.getUserID()); // get user
																		// ID
							if (userDTO.isSelected()) {
								selectedUserDTOs.remove(userDTO);
								userDTO.setSelected(false);
							} else {

								selectedUserDTOs.add(userDTO);
								userDTO.setSelected(true);
							}
							updateUserListAdapter.notifyDataSetChanged();
							isSelectAll = false;
							selectAllButton
									.setBackgroundResource(R.drawable.av_new_favorites_checkup);
						} else if (faveMode == MODE_FAVORITE
								&& faveEdit != EDIT_FAVORITES) {
							context.easyTrackerEventLog(
									AppConstants.EVENT_LOG_CATEG_FAVORITES,
									AppConstants.EVENT_LOG_ACTION_ITEM,
									"GuySelected");
							/* FAVE EDIT OFF */
							// FragmentHome.clearData = true;
							// FragmentDetailDudesProfile.comeFrom =
							// AppConstants.FRAGMENT_CHAT_FRIEND;
							// context.Attach_Fragment(AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE);
							// Activity_Home.dudeCommonList =
							// (ArrayList<UserDTO>) searchDudeList;
							// Activity_Home.index = mDownPosition;

							System.out.println("arg2 : " + mDownPosition);
							FragmentChatMatches.backButtonComeFrom = true;
							FragmentChatMatches.userDTO = (UserDTO) chatMatchesAdapter
									.getItem(mDownPosition);
							context.Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
							doneButtonEvent(FragmentChatMatches.userDTO
									.getUserID());

							return false; // We don't consume the event
						}
					}

					return true;
				}
				}
				return true;
			}

		});

		// searchFriends.setOnEditorActionListener(new OnEditorActionListener()
		// {
		// @Override
		// public boolean onEditorAction(TextView v, int actionId,
		// KeyEvent event) {
		// // city = searchFriends.getText().toString();
		// // TODO Auto-generated method stub
		// if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		// if (searchFriends.getText().toString().trim().length() == 0) {
		// Toast.makeText(context, "Enter some text..",
		// Toast.LENGTH_SHORT).show();
		// } else {
		//
		// InputMethodManager imm = (InputMethodManager) context
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(
		// searchFriends.getWindowToken(), 0);
		//
		// }
		// }
		//
		// return false;
		// }
		// });

		searchFriends.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (filterIsOnline) {
					chatMatchesAdapter.getFilter().filter(
							s.toString() + "/" + AppConstants.ONLINE);
				} else {
					chatMatchesAdapter.getFilter().filter(
							s.toString() + "/" + AppConstants.OFFLINE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void doneButtonEvent(String UserID) {
		// disableStarFlag();
		if (WebServiceConstants.isOnline(context)) {
			new SendLikeRequestAsynchTask().execute(
					WebServiceConstants.BASE_URL
							+ WebServiceConstants.SEND_LIKE_REQUESTS, UserID);
			new SendLikeRequest2AsynchTask().execute(
					WebServiceConstants.BASE_URL
							+ WebServiceConstants.SEND_LIKE_REQUESTS, UserID);
		}

	}

	@Override
	public void onClick(View v) {
		if (WebServiceConstants.isOnline(context)) {
			switch (v.getId()) {
			case R.id.btnRemoveFromList:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "RemoveFromList");
				if (selectedUserDTOs.size() != 0)
					new RemoveFromListAsynch()
							.execute(WebServiceConstants.AV_DELETE_SELECTED_SUBLIST);
				break;
			case R.id.llSelectAll2:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "ListsSelectAll");

				if (faveMode == MODE_SUBLIST && faveEdit == EDIT_SUBLIST) {
					selectAllButton2
							.setBackgroundResource(!isSelectAll ? R.drawable.av_new_favorites_checkdown
									: R.drawable.av_new_favorites_checkup);
					for (int x = 0; x < subListAdapter.getCount(); x++) {
						UserDTO userDTO = (UserDTO) subListAdapter.getItem(x);
						System.out.println(userDTO.getUserID()); // get user ID
						if (isSelectAll) {
							selectedUserDTOs.remove(userDTO);
							userDTO.setSelected(false);
						} else {

							selectedUserDTOs.add(userDTO);
							userDTO.setSelected(true);
						}
					}
					subListAdapter.notifyDataSetChanged();
				}
				isSelectAll = isSelectAll ? false : true;
				break;
			case R.id.llSelectAll:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "FavoritesSelectAll");
				if (faveMode == MODE_FAVORITE && faveEdit == EDIT_FAVORITES) {
					selectAllButton
							.setBackgroundResource(!isSelectAll ? R.drawable.av_new_favorites_checkdown
									: R.drawable.av_new_favorites_checkup);
					for (int x = 0; x < updateUserListAdapter.getCount(); x++) {
						UserDTO userDTO = (UserDTO) updateUserListAdapter
								.getItem(x);
						System.out.println(userDTO.getUserID()); // get user ID
						if (isSelectAll) {
							selectedUserDTOs.remove(userDTO);
							userDTO.setSelected(false);
						} else {

							selectedUserDTOs.add(userDTO);
							userDTO.setSelected(true);
						}
					}
					updateUserListAdapter.notifyDataSetChanged();
				} else if (faveMode == MODE_LIST && faveEdit == EDIT_LISTS) {
					selectAllButton
							.setBackgroundResource(!isSelectAll ? R.drawable.av_new_favorites_checkdown
									: R.drawable.av_new_favorites_checkup);
					for (int x = 0; x < userListAdapterEdit.getCount(); x++) {
						UserListDTO userListDTO = (UserListDTO) userListAdapterEdit
								.getItem(x);
						System.out.println(userListDTO.getListId()); // get list
																		// ID
						if (isSelectAll) {
							selectedUserListDTOs.remove(userListDTO);
							userListDTO.setSelected(false);
						} else {

							selectedUserListDTOs.add(userListDTO);
							userListDTO.setSelected(true);
						}
					}
					userListAdapterEdit.notifyDataSetChanged();
				}
				isSelectAll = isSelectAll ? false : true;
				break;
			case R.id.btnDeleteList:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "DeleteList");

				if (selectedUserListDTOs.size() != 0) {
					isFromCreateList = true;
					// TODO: MUTIPLE DELETE LIST: REQUEST API FROM KOSA
					new ListDeleteAsyncTask()
							.execute(WebServiceConstants.AV_DELETE_SELECTED_LIST);
				} else {
					Toast.makeText(context, "Please Select a List",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btnCreateList:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "AddList");

				if (0 == etCreateList.getText().toString().trim().length()) {
					Toast.makeText(context, "Please enter a List Name",
							Toast.LENGTH_SHORT).show();
					return;
				}

				boolean listAlreadyExists = false;
				for (UserListDTO listDTO : userListDTOs) {
					if (listDTO.getListName().equalsIgnoreCase(
							etCreateList.getText().toString())) {
						listAlreadyExists = true;
						break;
					}
				}

				if (WebServiceConstants.isOnline(context)) {
					if (listAlreadyExists) {
						Toast.makeText(context, "List Already Exists",
								Toast.LENGTH_SHORT).show();
					} else {
						isFromCreateList = true;
						new CreateNewList()
								.execute(WebServiceConstants.BASE_URL
										+ WebServiceConstants.CREATE_NEW_LIST);
					}

				}
				break;
			case R.id.btnListBack:
				listTabButton.performClick();
				break;
			case R.id.btnUnfave:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "Unfavorite");
				if (selectedUserDTOs.size() != 0) {
					// TODO: MULTIPLE UNFAVE: REQUEST API FROM KOSA
					new UnfavoriteDudeAsynch()
							.execute(WebServiceConstants.AV_UNFAVORITE_SELECTED_DUDE);

				} else {
					Toast.makeText(context, "Select at least one Guy",
							Toast.LENGTH_SHORT).show();
					linearFooter.setVisibility(View.VISIBLE);
					llSelectAll.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.btnAddToList:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "AddToList");
				if (userListDTOs.size() <= 0) {
					Toast.makeText(context, "No list", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (selectedUserDTOs.size() != 0) {

					ListView listView = new ListView(context);
					listView.setAdapter(userListAdapter);

					final Dialog dialog = new Dialog(context);
					dialog.setTitle("Add to List");
					dialog.setContentView(listView);

					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							userListDTO = userListDTOs.get(arg2);
							if (WebServiceConstants.isOnline(context)) {
								new AddDudes()
										.execute(WebServiceConstants.BASE_URL
												+ WebServiceConstants.ADD_DUDES_IN_LIST);
							}
							dialog.dismiss();
						}
					});

					dialog.show();
				} else {
					Toast.makeText(context, "Select at least one Guy",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btnEdit:
				selectedUserDTOs = new ArrayList<UserDTO>(); // reset
				if (faveMode == MODE_FAVORITE && faveEdit != EDIT_FAVORITES) { // edit
																				// fave
																				// on
					context.easyTrackerEventLog(
							AppConstants.EVENT_LOG_CATEG_FAVORITES,
							AppConstants.EVENT_LOG_ACTION_BTN, "FavoritesEdit");

					faveEdit = EDIT_FAVORITES;
					linearFooter.setVisibility(View.VISIBLE);
					llSelectAll.setVisibility(View.VISIBLE);

					editButton.setImageResource(R.drawable.av_new_fave_cancel);

					if (faveView == VIEW_LIST) {
						updateUserListAdapter = new UpdateUserListAdapter(
								context, searchDudeList, 0);
						friendListView.setNumColumns(1);
						friendListView.setAdapter(updateUserListAdapter);
					} else if (faveView == VIEW_GRID) {
						updateUserListAdapter = new UpdateUserListAdapter(
								context, searchDudeList, 2);
						friendListView.setNumColumns(2);
						friendListView.setAdapter(updateUserListAdapter);
					}

					/* Gives Space for Footer Button */
					RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT);
					p.addRule(RelativeLayout.ABOVE, R.id.linearFooter);
					friendListView.setLayoutParams(p);

					updateUserListAdapter.notifyDataSetChanged();
				} else if (faveMode == MODE_FAVORITE
						&& faveEdit == EDIT_FAVORITES) { // edit fave off
					faveEdit = EDIT_NULL;
					linearFooter.setVisibility(View.GONE);
					llSelectAll.setVisibility(View.GONE);
					editButton
							.setImageResource(R.drawable.av_new_favorites_edit_up);

					if (faveView == VIEW_LIST) {
						chatMatchesAdapter = new ChatMatchesAdapter(context,
								searchDudeList, 0);
						friendListView.setNumColumns(1);
						friendListView.setAdapter(chatMatchesAdapter);
					} else if (faveView == VIEW_GRID) {
						chatMatchesAdapter = new ChatMatchesAdapter(context,
								searchDudeList, 2);
						friendListView.setNumColumns(2);
						friendListView.setAdapter(chatMatchesAdapter);
					}

					isSelectAll = false;
					selectAllButton
							.setBackgroundResource(R.drawable.av_new_favorites_checkup);
					for (int x = 0; x < updateUserListAdapter.getCount(); x++) {
						UserDTO userDTO = (UserDTO) updateUserListAdapter
								.getItem(x);
						System.out.println(userDTO.getUserID()); // get user ID
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					updateUserListAdapter.notifyDataSetChanged();
				} else if (faveMode == MODE_LIST && faveEdit != EDIT_LISTS) { // edit
																				// list
																				// on
					context.easyTrackerEventLog(
							AppConstants.EVENT_LOG_CATEG_FAVORITES,
							AppConstants.EVENT_LOG_ACTION_BTN, "ListsEdit");

					if (userListDTOs.size() <= 0) {
						Toast.makeText(context, "No list", Toast.LENGTH_SHORT)
								.show();
						return;
					}

					faveEdit = EDIT_LISTS;
					editButton.setImageResource(R.drawable.av_new_fave_cancel);
					linearFooterDel.setVisibility(View.VISIBLE);
					llSelectAll.setVisibility(View.VISIBLE);

					friendListView.setAdapter(userListAdapterEdit);

					/* Gives Space for Footer Button */
					RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT);
					p.addRule(RelativeLayout.ABOVE, R.id.linearFooterDel);
					friendListView.setLayoutParams(p);
				} else if (faveMode == MODE_LIST && faveEdit == EDIT_LISTS) { // edit
																				// list
																				// off
					faveEdit = EDIT_NULL;
					editButton
							.setImageResource(R.drawable.av_new_favorites_edit_up);
					linearFooterDel.setVisibility(View.GONE);
					llSelectAll.setVisibility(View.GONE);

					friendListView.setAdapter(userListAdapter);

					isSelectAll = false;
					selectAllButton
							.setBackgroundResource(R.drawable.av_new_favorites_checkup);
					for (int x = 0; x < userListAdapterEdit.getCount(); x++) {
						UserListDTO userDTO = (UserListDTO) userListAdapterEdit
								.getItem(x);
						System.out.println(userDTO.getListId()); // get user ID
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					userListAdapterEdit.notifyDataSetChanged();
				} else if (faveMode == MODE_SUBLIST && faveEdit != EDIT_SUBLIST) { // edit
																					// sublist
																					// on
					context.easyTrackerEventLog(
							AppConstants.EVENT_LOG_CATEG_FAVORITES,
							AppConstants.EVENT_LOG_ACTION_BTN, "ListsEdit");

					if (userListDTO.getDudeList().size() <= 0) {
						Toast.makeText(context, "No members",
								Toast.LENGTH_SHORT).show();
						return;
					}

					faveEdit = EDIT_SUBLIST;
					editButton.setImageResource(R.drawable.av_new_fave_cancel);
					llSelectAll2.setVisibility(View.VISIBLE);

					linearFooterRemoveFromList.setVisibility(View.VISIBLE);

					if (faveView == VIEW_LIST) {
						subListAdapter = new SearchByAdapter(context,
								userListAdapter.getItem(mDownPosition)
										.getDudeList(), 5);
						friendListView.setNumColumns(1);
					} else if (faveView == VIEW_GRID) {
						subListAdapter = new SearchByAdapter(context,
								userListAdapter.getItem(mDownPosition)
										.getDudeList(), 7);
						friendListView.setNumColumns(2);
					}
					friendListView.setAdapter(subListAdapter);

					/* Gives Space for Footer Button */
					RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT);
					p.addRule(RelativeLayout.ABOVE,
							R.id.linearFooterRemoveFromList);
					friendListView.setLayoutParams(p);
				} else if (faveMode == MODE_SUBLIST && faveEdit == EDIT_SUBLIST) { // edit
																					// sublist
																					// off
					faveEdit = EDIT_NULL;

					editButton
							.setImageResource(R.drawable.av_new_favorites_edit_up);
					linearFooterRemoveFromList.setVisibility(View.GONE);
					llSelectAll2.setVisibility(View.GONE);

					if (faveView == VIEW_LIST) {
						subListAdapter = new SearchByAdapter(context,
								userListAdapter.getItem(mDownPosition)
										.getDudeList(), 0);
						friendListView.setNumColumns(1);
					} else if (faveView == VIEW_GRID) {
						subListAdapter = new SearchByAdapter(context,
								userListAdapter.getItem(mDownPosition)
										.getDudeList(), 6);
						friendListView.setNumColumns(2);
					}
					friendListView.setAdapter(subListAdapter);

					for (int x = 0; x < subListAdapter.getCount(); x++) {
						UserDTO userDTO = (UserDTO) subListAdapter.getItem(x);
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					subListAdapter.notifyDataSetChanged();
				}
				break;
			case R.id.plusbutton:
				menuLayout.setVisibility(View.VISIBLE);
				break;
			case R.id.cancleLayout:
				menuLayout.setVisibility(View.GONE);

				break;
			case R.id.btnFavoritesView:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "FavoritesTab");
				llSelectAll.setVisibility(View.GONE);
				llSelectAll2.setVisibility(View.GONE);
				linearFooter.setVisibility(View.GONE);
				linearFooterDel.setVisibility(View.GONE);
				linearFooterRemoveFromList.setVisibility(View.GONE);
				btnListBack.setVisibility(View.GONE);
				tvListTitle.setVisibility(View.GONE);
				rlCreateList.setVisibility(View.GONE);
				isSelectAll = false;
				selectAllButton
						.setBackgroundResource(R.drawable.av_new_favorites_checkup);

				editButton
						.setImageResource(R.drawable.av_new_favorites_edit_up);
				faveTabButton
						.setImageResource(R.drawable.av_new_favorites_favedown);
				listTabButton
						.setImageResource(R.drawable.av_new_favorites_listup);

				if (faveMode == MODE_FAVORITE && faveEdit == EDIT_FAVORITES) {
					for (int x = 0; x < updateUserListAdapter.getCount(); x++) {
						UserDTO userDTO = (UserDTO) updateUserListAdapter
								.getItem(x);
						System.out.println(userDTO.getUserID()); // get user ID
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					updateUserListAdapter.notifyDataSetChanged();
				} else if (faveMode == MODE_LIST && faveEdit == EDIT_LISTS) {
					for (int x = 0; x < userListAdapterEdit.getCount(); x++) {
						UserListDTO userDTO = (UserListDTO) userListAdapterEdit
								.getItem(x);
						System.out.println(userDTO.getListId()); // get user ID
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					userListAdapterEdit.notifyDataSetChanged();
				} else if (faveMode == MODE_SUBLIST && faveEdit == EDIT_SUBLIST) {
					for (int x = 0; x < subListAdapter.getCount(); x++) {
						UserDTO userDTO = (UserDTO) subListAdapter.getItem(x);
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					subListAdapter.notifyDataSetChanged();
				}

				faveMode = MODE_FAVORITE;
				faveEdit = EDIT_NULL;

				if (faveView == VIEW_LIST) {
					chatMatchesAdapter = new ChatMatchesAdapter(context,
							searchDudeList, 0);
					friendListView.setNumColumns(1);
					friendListView.setAdapter(chatMatchesAdapter);
				} else if (faveView == VIEW_GRID) {
					chatMatchesAdapter = new ChatMatchesAdapter(context,
							searchDudeList, 2);
					friendListView.setNumColumns(2);
					friendListView.setAdapter(chatMatchesAdapter);
				}

				break;
			case R.id.btnListsView:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "ListsTab");

				llSelectAll.setVisibility(View.GONE);
				llSelectAll2.setVisibility(View.GONE);
				linearFooter.setVisibility(View.GONE);
				linearFooterDel.setVisibility(View.GONE);
				linearFooterRemoveFromList.setVisibility(View.GONE);
				btnListBack.setVisibility(View.GONE);
				tvListTitle.setVisibility(View.GONE);
				rlCreateList.setVisibility(View.VISIBLE);
				isSelectAll = false;
				selectAllButton
						.setBackgroundResource(R.drawable.av_new_favorites_checkup);

				editButton
						.setImageResource(R.drawable.av_new_favorites_edit_up);
				faveTabButton
						.setImageResource(R.drawable.av_new_favorites_faveup);
				listTabButton
						.setImageResource(R.drawable.av_new_favorites_listdown);

				if (faveMode == MODE_FAVORITE && faveEdit == EDIT_FAVORITES) {
					for (int x = 0; x < updateUserListAdapter.getCount(); x++) {
						UserDTO userDTO = (UserDTO) updateUserListAdapter
								.getItem(x);
						System.out.println(userDTO.getUserID()); // get user ID
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					updateUserListAdapter.notifyDataSetChanged();
				} else if (faveMode == MODE_LIST && faveEdit == EDIT_LISTS) {
					for (int x = 0; x < userListAdapterEdit.getCount(); x++) {
						UserListDTO userDTO = (UserListDTO) userListAdapterEdit
								.getItem(x);
						System.out.println(userDTO.getListId()); // get user ID
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					userListAdapterEdit.notifyDataSetChanged();
				} else if (faveMode == MODE_SUBLIST && faveEdit == EDIT_SUBLIST) {
					for (int x = 0; x < subListAdapter.getCount(); x++) {
						UserDTO userDTO = (UserDTO) subListAdapter.getItem(x);
						selectedUserDTOs.remove(userDTO);
						userDTO.setSelected(false);
					}
					subListAdapter.notifyDataSetChanged();
				}

				faveMode = MODE_LIST;
				faveEdit = MODE_NULL;

				friendListView.setNumColumns(1);
				friendListView.setAdapter(userListAdapter);
				break;
			case R.id.btnLinear:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "ListView");

				listViewButton
						.setImageResource(R.drawable.av_new_favorites_h_listdown);
				gridViewButton
						.setImageResource(R.drawable.av_new_favorites_h_gridup);

				faveView = VIEW_LIST;

				if (faveMode == MODE_FAVORITE) { // favorites
					if (faveEdit != EDIT_FAVORITES) { // edit mode off
						chatMatchesAdapter = new ChatMatchesAdapter(context,
								searchDudeList, 0);
						friendListView.setNumColumns(1);
						friendListView.setAdapter(chatMatchesAdapter);
					} else { // edit mode on
						updateUserListAdapter = new UpdateUserListAdapter(
								context, searchDudeList, 0);
						friendListView.setNumColumns(1);
						friendListView.setAdapter(updateUserListAdapter);
					}
				} else if (faveMode == MODE_LIST) { // lists
					// NO CHANGES WILL BE DONE
				} else if (faveMode == MODE_SUBLIST) {
					subListAdapter = new SearchByAdapter(context,
							userListAdapter.getItem(mDownPosition)
									.getDudeList(),
							faveEdit != EDIT_SUBLIST ? 0 : 5);
					friendListView.setNumColumns(1);
					friendListView.setAdapter(subListAdapter);
				}
				break;
			case R.id.btnGrid:
				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FAVORITES,
						AppConstants.EVENT_LOG_ACTION_BTN, "GridView");

				listViewButton
						.setImageResource(R.drawable.av_new_favorites_h_listup);
				gridViewButton
						.setImageResource(R.drawable.av_new_favorites_h_griddown);

				faveView = VIEW_GRID;

				if (faveMode == MODE_FAVORITE) { // favorites
					if (faveEdit != EDIT_FAVORITES) { // edit mode off
						chatMatchesAdapter = new ChatMatchesAdapter(context,
								searchDudeList, 2);
						friendListView.setNumColumns(2);
						friendListView.setAdapter(chatMatchesAdapter);
					} else { // edit mode on
						updateUserListAdapter = new UpdateUserListAdapter(
								context, searchDudeList, 2);
						friendListView.setNumColumns(2);
						friendListView.setAdapter(updateUserListAdapter);
					}
				} else if (faveMode == MODE_LIST) { // lists [lists has no grid
													// view]
					// NO CHANGES WILL BE DONE
				} else if (faveMode == MODE_SUBLIST) {
					subListAdapter = new SearchByAdapter(context,
							userListAdapter.getItem(mDownPosition)
									.getDudeList(),
							faveEdit != EDIT_SUBLIST ? 6 : 7);
					friendListView.setNumColumns(2);
					friendListView.setAdapter(subListAdapter);
				}
				break;
			case R.id.deleteAllHistory:
				menuLayout.setVisibility(View.GONE);
				openDailog();
				break;
			case R.id.filter:
				if (filterIsOnline) {
					onlineDudesFilter
							.setBackgroundResource(R.drawable.btn_offline);
					// findDudesAdapter.getFilter().filter(AppConstants.ALL_DUDE);
					filterIsOnline = false;
					// calculatePages(userDTOs.size());
					// viewPager.setCurrentItem(0);
					chatMatchesAdapter.getFilter().filter(
							searchFriends.getText().toString() + "/"
									+ AppConstants.OFFLINE);

				} else {
					onlineDudesFilter
							.setBackgroundResource(R.drawable.btn_online);
					// findDudesAdapter.getFilter().filter(AppConstants.ONLINE);
					filterIsOnline = true;
					chatMatchesAdapter.getFilter().filter(
							searchFriends.getText().toString() + "/"
									+ AppConstants.ONLINE);
					// viewPager.setCurrentItem(0);
				}
				break;
			case R.id.clearSearchResult:
				searchFriends.setText("");
				break;
			default:
				break;
			}

		} else {
			Toast.makeText(context, "Please check network connection",
					Toast.LENGTH_LONG).show();
		}

	}

	public void doRemoveFaveWithAnim(int translateX) {
		friendListView.getChildAt(mDownPosition).animate()
				.translationX(translateX).withStartAction(new Runnable() {
					public void run() {
						// do something
						/* KOSA's API */
						new removeFavoriteAsynch()
								.execute("http://54.219.211.237/KraveAPI/api_calls/remove-user-to-favorite.php");

					}
				});
		// EndAction
		friendListView.getChildAt(mDownPosition).animate().alpha(0)
				.withEndAction(new Runnable() {
					public void run() {
						// Remove the view from the layout called parent

						searchDudeList.remove(mDownPosition);
						chatMatchesAdapter.notifyDataSetChanged();
						friendListView.getChildAt(mDownPosition).setX(0); // reset
																			// position
						friendListView.getChildAt(mDownPosition).setAlpha(100); // reset
																				// alpha
					}
				});
	}

	/* #pragma mark - ASYNCHRONOUS TASKS */

	public class UnfavoriteDudeAsynch extends
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
			// userid=1&title=test
			// list_id=1&adddude
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));

			for (int i = 0; i < selectedUserDTOs.size(); i++) {
				params.add(new BasicNameValuePair("unfavorite_ids[" + i + "]",
						selectedUserDTOs.get(i).getUserID()));
			}

			System.out.println("remove from list params : " + params);
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			// isFromCreateList = true;
			//
			// new GetUserList().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.GET_USER_LIST);

			/* prototype caching */
			searchDudeList.removeAll(selectedUserDTOs);
			chatMatchesAdapter.notifyDataSetChanged();
			updateUserListAdapter.notifyDataSetChanged();
			editButton.performClick();
			linearFooter.setVisibility(View.GONE);
			llSelectAll.setVisibility(View.GONE);
			llSelectAll2.setVisibility(View.GONE);
			updateUserListAdapter.notifyDataSetChanged();

			Toast.makeText(context,
					"Guy(s) successfully removed from Favorites",
					Toast.LENGTH_SHORT).show();

		}
	}

	public class RemoveFromListAsynch extends
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
			// userid=1&title=test
			// list_id=1&adddude
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("list_id", userListDTO
					.getListId()));

			for (int i = 0; i < selectedUserDTOs.size(); i++) {
				params.add(new BasicNameValuePair("member_ids[" + i + "]",
						selectedUserDTOs.get(i).getUserID()));
			}

			System.out.println("remove from list params : " + params);
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			isFromCreateList = true;

			new GetUserList().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_USER_LIST);
			Toast.makeText(context, "Guy(s) successfully removed from list",
					Toast.LENGTH_SHORT).show();

		}
	}

	public class AddDudes extends AsyncTask<String, Void, JSONArray> {
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
			// userid=1&title=test
			// list_id=1&adddude
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("list_id", userListDTO
					.getListId()));
			for (int i = 0; i < selectedUserDTOs.size(); i++) {
				// reqEntity.addPart("interest[" + i + "][]", new
				// StringBody(String.valueOf(i + 1)));
				params.add(new BasicNameValuePair("adddude[" + i + "][]",
						String.valueOf(selectedUserDTOs.get(i).getUserID())));

			}
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
					Toast.makeText(context, "Guys successfully added",
							Toast.LENGTH_SHORT).show();
					// setResult(RESULT_OK);
					// finish();
				} else {
					Toast.makeText(context, "Guys not added",
							Toast.LENGTH_SHORT).show();
				}
				// for (int i = 0; i < jsonArray.length(); i++) {
				// JSONObject mJsonObject = jsonArray.getJSONObject(i);
				// UserListDTO dto = new UserListDTO();
				// dto.setListId(mJsonObject.getString("list_id"));
				// dto.setListName(mJsonObject.getString("list_title"));
				//
				// }

				new GetUserList().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_USER_LIST);

				// userListAdapter.notifyDataSetChanged();
				// userListAdapterEdit.notifyDataSetChanged();
				// subListAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class ListDeleteAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		// String listId;

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
			// http://198.12.150.189/~simssoe/index.php?action=deletelist&listid=2&userid=1
			// listId = args[1];
			params.add(new BasicNameValuePair("user_id", ""
					+ sessionManager.getUserDetail().getUserID()));
			for (int i = 0; i < selectedUserListDTOs.size(); i++) {
				params.add(new BasicNameValuePair("list_ids[" + i + "]",
						selectedUserListDTOs.get(i).getListId()));
			}

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			Toast.makeText(context, "List Successfully Deleted",
					Toast.LENGTH_SHORT).show();
			isFromCreateList = true;
			/* just reload this to refresh */
			new GetUserList().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_USER_LIST);

		}
	}

	public class CreateNewList extends AsyncTask<String, Void, JSONArray> {
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
			// userid=1&title=test
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("title", etCreateList.getText()
					.toString()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("create new list response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			// [{"list_id":"5","list_title":"test23","user_id":"6"}]
			// 06-03 17:21:52.170: D/create new list response(11423):
			// [{"list_id":"13","user_id":"6","list_title":"dddddddd"}]

			try {

				JSONObject mainJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mainJsonObject.getString("success");
				// if (vStatus.equals("success")) {

				String id = mainJsonObject.getString("list_id");
				String name = mainJsonObject.getString("list_title");
				Intent intent = new Intent();
				intent.putExtra("listId", id);
				intent.putExtra("name", name);
				Log.d("", "new list created=" + id + "...... " + name);
				// setResult(RESULT_OK, intent);
				// } else {
				// setResult(RESULT_CANCELED);
				// }
				// finish();
				etCreateList.setText("");
				new GetUserList().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_USER_LIST);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public class GetUserList extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			singleton.progressLoading(loadingView, llLoading);
			loadingCount++;
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
			// dialog.dismiss();

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
				userListAdapterEdit.notifyDataSetChanged();
				// if (userListDTOs.size() == 0) {
				// Toast.makeText(context, "No list",
				// Toast.LENGTH_SHORT).show();
				// }
				if (isFromCreateList) {
					isFromCreateList = false;
					listTabButton.performClick();
				}
				// friendListView.setAdapter(userListAdapter);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			--loadingCount;
			if (loadingCount == 0)
				singleton.stopLoading(llLoading);
		}
	}

	class removeFavoriteAsynch extends AsyncTask<String, Void, JSONArray> {
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

			FragmentChatMatches.userDTO = (UserDTO) chatMatchesAdapter
					.getItem(mDownPosition);
			System.out.println("USERID: "
					+ FragmentChatMatches.userDTO.getUserID()); // show clicked
																// user ID
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("favorite_id",
					FragmentChatMatches.userDTO.getUserID()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get Like response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();

			Toast.makeText(context, "Dude Removed from Favorites.",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == context.RESULT_OK && requestCode == 1) {
			context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		System.gc();
		context.mainview.setVisibility(View.VISIBLE);

		changeHeaderTitle(true, "UPDATE INFO"); // reset title to krave icon
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();

	}

	public void changeHeaderTitle(Boolean isDestroy, String text) {
		ImageView icon = (ImageView) context.mainview.findViewById(R.id.icon);
		icon.setVisibility(isDestroy ? View.VISIBLE : View.GONE);
		TextView tvHeaderTitle = (TextView) context.mainview
				.findViewById(R.id.titleTextView);
		tvHeaderTitle.setVisibility(isDestroy ? View.GONE : View.VISIBLE);
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Md);
		tvHeaderTitle.setTypeface(typeface);
		tvHeaderTitle.setText(text);
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
		title.setText("Do you really want to delete chat history ?");
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
				new DeleteChatHistory().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.DELETE_CHAT);

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
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			// http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("request_id", ""
					+ sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("delete chat response", "" + json);
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
					KraveDAO dataBaseHelper = new KraveDAO(context);
					dataBaseHelper.clearDataBase();

					Toast.makeText(context,
							"Chat history successfully deleted",
							Toast.LENGTH_SHORT).show();
					for (int i = 0; i < searchDudeList.size(); i++) {
						UserDTO dto = searchDudeList.get(0);
						dto.setUserLastMsg("");

					}
					chatMatchesAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(context,
							"Chat history not  deleted,try again",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(context, "Chat history not  deleted,try again",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	class GetAllLikeDude extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		// http://parkhya.org/Android/krave_app/index.php?action=getfriends&user_id=107
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			singleton.progressLoading(loadingView, llLoading);
			loadingCount++;
		}

		protected JSONArray doInBackground(String... args) {
			// //
			// http://parkhya.org/Android/krave_app/index.php?action=user_login&email=test2@gmail.com&password=123456
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			System.out.println("params : " + params);
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("like user response :", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();
			--loadingCount;
			if (loadingCount == 0)
				singleton.stopLoading(llLoading);
			else if (loadingCount == 1)
				singleton.stopLoading(llLoading);
			searchDudeList.clear();

			System.out.print("like user response :" + jsonArray);
			// System.out.print("search user response :" + jsonArray.length());
			try {

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject Object = jsonArray.getJSONObject(i);

					searchDudeList.add(parseUserData(Object));
				}

				chatMatchesAdapter.notifyDataSetChanged();

				// count.setText("" + searchDudeList.size() + " Friend");
				/* Old API */
				// if (searchDudeList.size() == 0) {
				// Toast.makeText(context, "No Guy", Toast.LENGTH_SHORT)
				// .show();
				// }
				if (loadingCount > 0)
					singleton.progressLoading(loadingView, llLoading);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/* #pragma mark - Parse DTO */

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

		// JCv
		// JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
		JSONObject latlongInfo;
		try {
			latlongInfo = mJsonObject.getJSONObject("latlong");
		} catch (Exception e1) {
			latlongInfo=new JSONObject("{\"ll_id\":\"\",\"User_id\":\"\",\"latitude\":\"\",\"longitude\":\"\",\"ll_street\":\"\",\"ll_city\":\"\",\"ll_state\":\"\",\"ll_zip\":\"\",\"ll_country\":\"\",\"ll_devicetype\":\"\",\"ll_GSMid\":\"\"}");
		}
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
			// - -- - - -

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

		/*
		 * "latlong": { "ll_id": "2398", "User_id": "2707", "latitude":
		 * "14.442501", "longitude": "121.015137", "ll_street": "", "ll_city":
		 * "Las Pi\u00f1as", "ll_state": "", "ll_zip": "", "ll_country":
		 * "Philippines", "ll_devicetype": "android", "ll_GSMid": "" }
		 */
		LatLongDTO llDTO = new LatLongDTO();
		llDTO.setUser_id(latlongInfo.getString("User_id"));
		llDTO.setLatitude(latlongInfo.getString("latitude"));
		llDTO.setLongitude(latlongInfo.getString("longitude"));
		userDTO.setStreet(latlongInfo.getString("ll_street"));
		userDTO.setCity(latlongInfo.getString("ll_city"));
		userDTO.setState(latlongInfo.getString("ll_state"));
		userDTO.setPostalCode(latlongInfo.getString("ll_zip"));
		userDTO.setCountry(latlongInfo.getString("ll_country"));
		latLongDTO = llDTO;

		// for (int i = 0; i < jsonArrayLatLong.length(); i++) {
		// JSONObject latLongJsonObject = jsonArrayLatLong.getJSONObject(i);
		// LatLongDTO mDto = new LatLongDTO();
		// mDto.setUser_id(latLongJsonObject.getString("User_id"));
		// mDto.setLatitude(latLongJsonObject.getString("latitude"));
		// mDto.setLongitude(latLongJsonObject.getString("longitude"));
		// userDTO.setStreet(latLongJsonObject.getString("ll_street"));
		// userDTO.setCity(latLongJsonObject.getString("ll_city"));
		// userDTO.setState(latLongJsonObject.getString("ll_state"));
		// userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
		// userDTO.setCountry(latLongJsonObject.getString("ll_country"));
		// latLongDTO = mDto;
		// }

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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

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
			System.out.println("USERID: " + args[1]); // used to get viewing
														// profile user id
			params.add(new BasicNameValuePair("friend_id", args[1]));
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
			System.out.println("USERID: " + args[1]); // used to get viewing
														// profile user id
			params.add(new BasicNameValuePair("user_id", args[1]));
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
	/*
	 * private void parseUserDataAndSetInSession(JSONObject mJsonObject) throws
	 * JSONException { UserDTO userDTO = new UserDTO(); LatLongDTO latLongDTO =
	 * new LatLongDTO(); WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
	 * List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	 * List<UserProfileImageDTO> userProfileImageDTOs = new
	 * ArrayList<UserProfileImageDTO>();
	 * 
	 * JSONObject MainObject = mJsonObject.getJSONObject("user");
	 * 
	 * JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
	 * JSONArray jsonArrayImages = mJsonObject.getJSONArray("images"); JSONArray
	 * jsonArrayLatLong = mJsonObject.getJSONArray("latlong"); //
	 * System.out.println(MainObject);
	 * 
	 * userDTO.setUserID(MainObject.getString("user_id"));
	 * userDTO.setEmail(MainObject.getString("user_email"));
	 * userDTO.setFirstName(MainObject.getString("user_fname"));
	 * userDTO.setLastName(MainObject.getString("user_lname"));
	 * userDTO.setProfileImage(MainObject.getString("user_image"));
	 * userDTO.setMobile(MainObject.getString("user_mobile"));
	 * userDTO.setAboutMe(MainObject.getString("user_note"));
	 * userDTO.setIsFirstTime(MainObject.getString("isFirstTime")); if
	 * (MainObject.getString("isonline").equals("available")) {
	 * userDTO.setIsOnline(AppConstants.ONLINE);
	 * 
	 * } else if (MainObject.getString("isonline").equals("away")) {
	 * userDTO.setIsOnline(AppConstants.ABSENT); } else {
	 * userDTO.setIsOnline(AppConstants.OFFLINE); }
	 * userDTO.setCommonFriends(MainObject.getString("mutualfriends"));
	 * 
	 * whatAreYouDTO.setFeet(MainObject.getString("user_feet"));
	 * whatAreYouDTO.setInches(MainObject.getString("user_inches"));
	 * whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
	 * whatAreYouDTO.setHight(MainObject.getString("user_height"));
	 * whatAreYouDTO.setAge(MainObject.getString("user_age"));
	 * whatAreYouDTO.setWeight(MainObject.getString("user_weight")); //
	 * whatAreYouDTO.setAge(MainObject.getString("user_note"));
	 * whatAreYouDTO.setRelationshipStatus(MainObject
	 * .getString("user_relationshipStatus"));
	 * whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
	 * whatAreYouDTO.setWhatDoYouKrave(MainObject
	 * .getString("user_whatDoYouKrave"));
	 * 
	 * for (int i = 0; i < jsonArrayInterest.length(); i++) { JSONObject
	 * interestJsonObject = jsonArrayInterest.getJSONObject(i); InterestsDTO
	 * interestsDTO = new InterestsDTO();
	 * interestsDTO.setInterestId(interestJsonObject .getString("intrests_id"));
	 * interestsDTO.setInterestName(interestJsonObject
	 * .getString("intrests_name"));
	 * interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1 +
	 * interestJsonObject.getString("intrests_image"));
	 * interestsDTOs.add(interestsDTO);
	 * 
	 * } for (int i = 0; i < jsonArrayImages.length(); i++) { JSONObject
	 * imagesJsonObject = jsonArrayImages.getJSONObject(i); UserProfileImageDTO
	 * userProfileImageDTO = new UserProfileImageDTO();
	 * userProfileImageDTO.setImageId(imagesJsonObject .getString("image_id"));
	 * userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1 +
	 * imagesJsonObject.getString("image_path"));
	 * 
	 * userProfileImageDTOs.add(userProfileImageDTO);
	 * 
	 * } for (int i = 0; i < jsonArrayLatLong.length(); i++) { JSONObject
	 * latLongJsonObject = jsonArrayLatLong.getJSONObject(i); LatLongDTO mDto =
	 * new LatLongDTO();
	 * mDto.setUser_id(latLongJsonObject.getString("User_id"));
	 * mDto.setLatitude(latLongJsonObject.getString("latitude"));
	 * mDto.setLongitude(latLongJsonObject.getString("longitude"));
	 * userDTO.setStreet(latLongJsonObject.getString("ll_street"));
	 * userDTO.setCity(latLongJsonObject.getString("ll_city"));
	 * userDTO.setState(latLongJsonObject.getString("ll_state"));
	 * userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
	 * userDTO.setCountry(latLongJsonObject.getString("ll_country")); latLongDTO
	 * = mDto;
	 * 
	 * } if (!"url".equals(userDTO.getProfileImage())) { Log.d("",
	 * "facebook image at first position in list"); UserProfileImageDTO
	 * userProfileImageDTO = new UserProfileImageDTO();
	 * userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
	 * userProfileImageDTO.setImagePath(userDTO.getProfileImage());
	 * userProfileImageDTOs.add(0, userProfileImageDTO); }
	 * userDTO.setWhatAreYouDTO(whatAreYouDTO);
	 * userDTO.setLatLongDTO(latLongDTO);
	 * userDTO.setInterestList(interestsDTOs);
	 * userDTO.setUserProfileImageDTOs(userProfileImageDTOs); // int online =
	 * contex.getUserAvailability(userDTO.getUserID()); // Log.d("",
	 * "is online id " + userDTO.getUserID() + " '" + online); //
	 * userDTO.setIsOnline("" + online); userDTOs.add(userDTO);
	 * 
	 * }
	 */
}
