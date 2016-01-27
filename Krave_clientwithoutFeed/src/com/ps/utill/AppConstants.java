package com.ps.utill;

import android.widget.LinearLayout;

public class AppConstants {

	/* constants for intent */
	public static final String COME_FROM = "come_from";
	public static final int COME_FROM_NORMAL_REGISTRATION = 0;
	public static final int COME_FROM_FACEBOOK = 1;
	public static final int COME_FROM_UPDATE_PROFILE = 2;
	public static final int COME_FROM_ALL_GUYS = 0;
	public static final int COME_FROM_LISTS = 1;
	public static final int PUSH_NOTIFICATION = 500;

	// JCv -
	public static final String INTENT_ACTION_UPDATE_CHAT_HISTORY = "updateChatHistory";
	// public static final String ABOUT_ME_DEFAULT_SETRING =
	// "Hey I'm new to Krave! I haven't finished setting up my profile. Message me to find out more.";

	/* constants for IMAGE BASE PATH */
	// public static final String BASE_IMAGE_PATH =
	// "http://www.parkhya.org/Android/krave_app/krave_image/";
	// public static final String BASE_IMAGE_PATH_1 =
	// "http://www.parkhya.org/Android/krave_app/";
	// public static final String BASE_IMAGE_PATH =
	// "http://198.12.150.189/~simssoe/krave/krave/image/";s
	// public static final String BASE_IMAGE_PATH_1 =
	// "http://198.12.150.189/~simssoe/krave/";

	// http://54.176.180.241/krave_image/14077365370_.png
	public static final String BASE_IMAGE_PATH_FOR_DAILY_KRAVE = "http://54.176.180.241/admin/";
	public static final String BASE_IMAGE_PATH_FOR_INTEREST = "http://54.219.211.237";
	public static final String BASE_IMAGE_PATH_1 = "http://54.219.211.237/";
	public static final String UN_UPDATED_IMAGE = "url";
	public static final String AWATAR_IAMGE = "http://54.176.180.241/krave_image/com_facebook_profile_picture_blank_portrait.png";
	public static final int COME_FROM_LOGIN = 0;
	public static final int COME_FROM_INTEREST = 1;
	public static final String FACEBOOK_IMAGE = "facebook_image";
	public static final int NOTIFICATION_ENABLE = 1;
	public static final int NOTIFICATION_DISABLE = 0;
	public static final String GPS_PREFS = "gps_prefs";
	public static final String FILTER_PREFS = "filter_prefs";
	public static final String FILTER_PREFS_AGE = "filter_prefs_age";
	public static final String FILTER_PREFS_RADIUS = "filter_prefs_radius";
	public static final String FILTER_PREFS_ETHNICITY = "filter_prefs_ethnicity";
	public static final String FILTER_PREFS_ROLE = "filter_prefs_role";
	public static final String FILTER_PREFS_INTEREST = "filter_prefs_interest";
	public static final String FILTER_PREFS_BODY_TYPE = "filter_prefs_body_type";
	public static final String FILTER_PREFS_LOOKING_FOR = "filter_prefs_looking_for";

	public static final String TRANSITS_SHARED_PREFS = "trans_prefs";
	public static final String UNREAD_MSG_PREF = "unread_prefs";
	public static final String UNREAD_MSG_KEY = "unread_msg_key";
	public static final String UNREAD_MSG_VALUE = "unread_msg_value";
	public static final String GPS_REMINDER_ON = "reminder_on";
	public static final String TRANSIT_PREFERENCE = "transit_preference";
	public static final String FIND_DUDES_COLUMN_PREFERENCE = "find_dudes_column_preference";
	public static final String LANGUAGE_PREFERENCE = "language_preference";
	public static final String COMPARE_PHOTO_STRING = "54.219.211.237";

	public static final String APP_PREFS = "app_prefs";

	/* CONSTANT FOR BACK BUTTON */
	public static final int BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE = 1;
	public static final int BACK_BUTTON_FROM_SETTING = 2;
	public static final int BACK_BUTTON_FROM_DAILY_KRAVE_DETAIL = 3;
	public static final int BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE_WITH_CHAT = 4;
	public static final int BACK_BUTTON_FROM_UPDATE_PROFILE = 5;
	public static final int BACK_BUTTON_FROM_UPDATE_IMAGE_PROFILE = 6;
	public static final int BACK_BUTTON_FROM_DUDES_PROFILE = 7;
	/* FRAGMENT CONSTANT */

	public static final int FRAGMENT_HOME = 1;
	public static final int FRAGMENT_PROFILE = 2;

	public static final int FRAGMENT_FIND_DUDES = 3;
	public static final int FRAGMENT_GET_MATCHES = 4;
	public static final int FRAGMENT_DAILY_KRAVE = 5;
	public static final int FRAGMENT_CHAT_FRIEND = 6;
	public static final int FRAGMENT_SETTING = 7;
	public static final int FRAGMENT_MAP = 8;

	public static final int FRAGMENT_DUDES_PROFILE = 9;

	public static final int FRAGMENT_DETAIL_DUDES_PROFILE = 11;
	public static final int FRAGMENT_DUDES_CONGRATULATION = 12;
	public static final int FRAGMENT_DAILY_KRAVE_DETAIL = 13;
	public static final int FRAGMENT_UPDATE_PROFILE = 14;
	public static final int FRAGMENT_SEARCH_CITY = 15;
	public static final int FRAGMENT_IMAGE_REQUEST = 16;
	public static final int FRAGMENT_NIGHTLIFE_FINDER = 17;
	public static final int FRAGMENT_CHATT_MATCHES = 50;
	public static final int FRAGMENT_SETTING_NEW = 87;
	public static final int FRAGMENT_FEEDBACK = 100;

	/* LANGUAGE CONSTANT */
	public static final String ENGLISH = "0";
	public static final String SPANISH = "1";

	/* right menu constant */

	public static final int CREATE_NEW_LIST = 1;
	public static final int DELETE_DUDE_FROM_LIST = 2;
	public static final int ADD_DUDE_TO_LIST = 3;
	public static final int EDIT_LIST = 4;
	public static final int PUSH_NOTIFICATION_ACTIVITY = 5;
	public static final int FRIEND_NOTIFICATION_ACTIVITY = 7;
	public static final int FRIEND_NOTIFICATION_RESULT1 = 8;
	public static final int FRIEND_NOTIFICATION_RESULT2 = 9;

	/* font style id */
	public static final int HelveticaNeueLTStd_Bd = 1;
	public static final int HelveticaNeueLTStd_Lt = 2;
	public static final int HelveticaNeueLTStd_Md = 3;
	public static final int HelveticaNeueLTStd_Roman = 4;

	/* user availability constant */
	public static final String OFFLINE = "0";
	public static final String ONLINE = "1";
	public static final String ABSENT = "2";
	public static final String ALL_DUDE = "all";

	/* hight Weight unit constant */
	public static final String HEIGHT_IN_METER = "MT";
	public static final String HEIGHT_IN_FEET = "US";

	public static final String WEIGHT_IN_KILOGRAM = "MT";
	public static final String WEIGHT_IN_POUND = "US";
	/* chat notification constant */

	// http://54.241.85.74:9090/plugins/presence/status?jid=109@ip-10-199-23-53&type=xml

	public static final String CHAT_SERVER_ID = "@ip-10-199-23-53";
	public static final String CHAT_NOTIFICATION_TYPE = "notification_type";
	public static final String CHAT_NOTIFICATION_STRING = "notification_string";
	public static final int CHAT_APP_DOES_NOT_VISIBLE = 1;
	public static final int CHAT_NOT_IN_CHAT_FRAGMENT = 2;
	public static final int CHAT_OTHER_DUDE_NOTIFICATION = 3;

	public static final String CHAT_FROM = "0";
	public static final String CHAT_TO = "1";

	public static final String MESSAGE_TYPE_LOCATION = "<location>";
	public static final String MESSAGE_TYPE_TEXT = "<text>";
	public static final String MESSAGE_TYPE_IMAGE = "<image>";
	public static final String MESSAGE_TYPE_VIDEO = "<video>";
	public static final String SNAP_CHAT_CONSTANTS = "?seconds=";

	/* facebook like enable or disbale */
	public static final String FACEBOOK_LIKE_ENABLE = "Yes";
	public static final String FACEBOOK_LIKE_DISABLE = "No";

	/* image active or not */
	public static final String IMAGE_ACTIVE = "1";
	public static final String IMAGE_IN_ACTIVE = "0";

	/* image active or not */
	public static final String IMAGE_PUBLIC = "0";
	public static final String IMAGE_PRIVATE = "1";

	/* Google Analytics Event Convention */
	public static final String EVENT_LOG_ACTION_BTN = "ButtonPressed";
	public static final String EVENT_LOG_ACTION_ITEM = "ItemSelected";
	public static final String EVENT_LOG_ACTION_SWIPE = "Swipe";
	public static final String EVENT_LOG_ACTION_TB = "TextBoxChanged";
	public static final String EVENT_LOG_ACTION_INTEREST = "SelectedInterest";
	public static final String EVENT_LOG_ACTION_SETTINGS = "SaveSettings";
	public static final String EVENT_LOG_ACTION_FILTER = "SaveFilterSetting";
	public static final String EVENT_LOG_ACTION_SEND_MSG = "SendMessage";

	public static final String EVENT_LOG_CATEG_LEFT_NAV = "LeftNavigationMenu";
	public static final String EVENT_LOG_CATEG_RIGHT_NAV = "RightNavigationMenu";
	public static final String EVENT_LOG_CATEG_FIND_GUYS = "FindGuys";
	public static final String EVENT_LOG_CATEG_DUDE_PROFILE = "DudeProfile";
	public static final String EVENT_LOG_CATEG_EDIT_PROFILE = "Profile";
	public static final String EVENT_LOG_CATEG_FAVORITES = "Favorites";
	public static final String EVENT_LOG_CATEG_SETTINGS = "Settings";
	public static final String EVENT_LOG_CATEG_CONT_US = "ContactUs";
	public static final String EVENT_LOG_CATEG_FILTER = "Filter";
	public static final String EVENT_LOG_CATEG_CHAT = "ChatScreen";
	public static final String EVENT_LOG_CATEG_BLOCK = "BlockScreen";
	public static final String EVENT_LOG_CATEG_REPORT = "ReportScreen";

	public static final int ADAPTER_FLAG_EDIT_PROFILE = 8787;
	public static final int ADAPTER_FLAG_SETTINGS = 7777;

	/* web service responses */
	public static final int RESPONSE_SUCCESS = 200;
	public static final int RESPONSE_FAILURE = 404;

	/* PHOTO REQUEST ACCEPT AND REJECT CONSTANTS */

	public static final String PRIVATE_PHOTO_REQUEST_ACCEPT = "1";
	public static final String PRIVATE_PHOTO_REQUEST_REJECT = "2";
	/* brodcast action */

	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";

	/* ads location and size */

	public static final String ADS_BOTTOM_BANNER = "bottom_adds";
	public static final String ADS_INTERSTITIAL = "interstitial_adds";
	public static final String ADS_CENTER_BANNER = "native_adds";

	/* admob unit id or publisher id */
	public static final String ADMOB_BANNER_UNIIT_ID = "ca-app-pub-4790661992795242/4393613814";
	public static final String ADMOB_INTERSTITIAL_UNIIT_ID = "ca-app-pub-4790661992795242/5870347015";

}
