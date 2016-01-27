/*  
 * Just a simple singleton class 
 */
package com.krave.kraveapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ps.models.SettingDTO;
import com.ps.models.UserDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveDAO;
import com.ps.utill.SessionManager;

public class AppManager {

	private static AppManager instance;
	public String gcmRegId="";
	public boolean isDontSearch;
	public boolean isReloadChatNav;
	public int isFromFlagged;
	public boolean isFromHome;
	public SharedPreferences mPrefs;
	public SharedPreferences mTransitPrefs;
	//public SharedPreferences mFinDudesColumnPrefs;
	public SharedPreferences mLanguagePrefs;
	public SharedPreferences val_mPrefs;
	public SharedPreferences key_mPrefs;
	public SharedPreferences mFilterPrefs;
	public int gpsReminderCounter;
	public ImageView iv;
	public View view;
	public SettingDTO filterDTO = new SettingDTO();
	public boolean isFilterDTO = false;
	public List<UserDTO> storeDudeList = new ArrayList<UserDTO>();
	public String chatUserID;
	public Double mGPSLat;
	public Double mGPSLong;
	public int dudePageNo = 1;
	public boolean isPaidUser=false;      // indentify user is paid or not
	public boolean isGoogleAd=true;      // show google ad mob or admin ad 
	public String adsizeAndLocation=AppConstants.ADS_BOTTOM_BANNER;

	public HashMap<String, Integer> getChatCount = new HashMap<String, Integer>();
	public HashMap<String, String> getLastMsg = new HashMap<String, String>();
	public HashMap<String, String> getLastMsgTime = new HashMap<String, String>();
	public HashMap<String, String> getLastMsgFromOrToUser = new HashMap<String, String>();

	public static void initInstance() {
		if (instance == null) {
			// Create the instance
			instance = new AppManager();
		}
	}

	public static AppManager getInstance() {
		// Return the instance
		return instance;
	}

	private AppManager() {
		// Constructor hidden because this is a singleton
	}

	public void progressLoading(ImageView iv, LinearLayout llLoading) {
		this.iv = iv;

		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
				.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		this.iv.setAnimation(anim);
		this.iv.startAnimation(anim);

		llLoading.setVisibility(View.VISIBLE);
	}

	public void stopLoading(LinearLayout llLoading) {
		this.iv.clearAnimation();
		llLoading.setVisibility(View.INVISIBLE);
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

		double kms = (dist);
		return (float) (Math.round(kms * 10.0) / 10.0);

	}

	public long calculateAge(String berthDateString) {
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

		return number_of_years;

	}
}
