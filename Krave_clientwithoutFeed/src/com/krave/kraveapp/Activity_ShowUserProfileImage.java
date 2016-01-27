package com.krave.kraveapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.krave.kraveapp.Activity_ShowImage.AddUpdateSnapChatTime;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.ChatDetailsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.TouchImageView;
import com.ps.utill.WebServiceConstants;

public class Activity_ShowUserProfileImage extends Activity {
	private static Activity_ShowUserProfileImage activityObject = null;

	public static Activity_ShowUserProfileImage getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(
			Activity_ShowUserProfileImage activityObject) {
		Activity_ShowUserProfileImage.activityObject = activityObject;
	}

	/* resources */

	private ImageLoader imageLoader;

	/* Layout Views */
	private TouchImageView showImage;

	private LinearLayout cancelLayout;

	private ImageButton rotateClockWise;

	private ImageButton rotateAntiClockWise;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_show_image);
		activityObject = this;

		imageLoader = new ImageLoader(Activity_ShowUserProfileImage.this);

		setLayout();
		String url = getIntent().getExtras().getString("imageUrl");

		imageLoader.DisplayImage(url, showImage);

	}

	private void setLayout() {

		cancelLayout = (LinearLayout) findViewById(R.id.cancleLayout);

		showImage = (TouchImageView) findViewById(R.id.showImage);
		rotateClockWise = (ImageButton) findViewById(R.id.clock);
		rotateAntiClockWise = (ImageButton) findViewById(R.id.anticlock);
		rotateClockWise.setVisibility(View.GONE);
		rotateAntiClockWise.setVisibility(View.GONE);
		cancelLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;

	}

}
