package com.krave.kraveapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.ChatDetailsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.TouchImageView;
import com.ps.utill.WebServiceConstants;

//SELECT * FROM `krave_users` WHERE `user_id`IN('4','110','46','59','64','108','109')
public class Activity_ShowVideo extends Activity {
	private static Activity_ShowVideo activityObject = null;

	public static Activity_ShowVideo getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_ShowVideo activityObject) {
		Activity_ShowVideo.activityObject = activityObject;
	}

	/* resources */
	private SessionManager sessionManager;
	private ImageLoader imageLoader;

	/* Layout Views */
	private TouchImageView showImage;

	private LinearLayout cancelLayout;
	private TextView dateTime;
	ImageButton rotateClockWise, rotateAntiClockWise;
	Bitmap bitmap;
	int angle = 0;
	int leftSnapChatTime = 0;
	/* dto variables initialization */
	private boolean isLoginUser;
	private VideoView video_player_view;
	private MediaController media_Controller;
	private DisplayMetrics dm;
	public static ChatDetailsDTO chatMessageObj;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_show_video);
		activityObject = this;

		//setLayout();
		getInit();
		setListeners();
//		sessionManager = new SessionManager(Activity_ShowVideo.this);
//		imageLoader = new ImageLoader(Activity_ShowVideo.this);
//		isLoginUser = sessionManager.getUserDetail().getUserID()
//				.equals(chatMessageObj.getFromuser());
//
//		// imageLoader.DisplayImage(chatMessageObj.getMessage(), showImage);
//		// showImage.setVisibility(View.GONE);
//
//		if (!isLoginUser
//				&& chatMessageObj.getMessage().contains(
//						AppConstants.SNAP_CHAT_CONSTANTS)) {
//			leftSnapChatTime = chatMessageObj.getImageSnapChatLeftTime();
//
//			handler.postDelayed(thread, 0);
//			getInit();
//
//		} else {
//			SimpleDateFormat dateFormat = new SimpleDateFormat(
//					"dd MMM yyyy hh:mm aa");
//			Date date = new Date(Long.valueOf(chatMessageObj.getTime()));
//
//			dateTime.setText(dateFormat.format(date));
//
//			getInit();
//
//		}

		// setTypeFace();

	}

	public void getInit() {
		video_player_view = (VideoView) findViewById(R.id.video_player_view);
		media_Controller = new MediaController(this);
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;
		int width = dm.widthPixels;
		video_player_view.setMinimumWidth(width);
		video_player_view.setMinimumHeight(height);
		video_player_view.setMediaController(media_Controller);
		video_player_view
				.setVideoPath(chatMessageObj.getMessage().split("\\|")[1]);
		video_player_view.seekTo(100);
		video_player_view.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				dateTime.setText("" + leftSnapChatTime);
			} else {
				finish();
			}
		};

	};

	Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			if (leftSnapChatTime > 0) {
				leftSnapChatTime--;
				handler.sendEmptyMessage(1);

				handler.postDelayed(thread, 1000);
			} else {
				handler.sendEmptyMessage(0);

			}
		}
	});

	private void lockImage() {
		((ImageView) chatMessageObj.getView())
				.setImageResource(R.drawable.snap_chat_padlock);
		handler.removeCallbacks(thread);
		finish();
	}

	private void showImage() {
		((ImageView) chatMessageObj.getView())
				.setImageResource(R.drawable.av_new_interest_transgender_down);
		handler.postDelayed(thread, 0);
		getInit();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;
//
//		if (!isLoginUser
//				&& chatMessageObj.getMessage().contains(
//						AppConstants.SNAP_CHAT_CONSTANTS)) {
//			handler.removeCallbacks(thread);
//			chatMessageObj.setImageSnapChatLeftTime(leftSnapChatTime);
//			if (leftSnapChatTime <= 0) {
//				((ImageView) chatMessageObj.getView())
//						.setImageResource(R.drawable.snap_chat_padlock);
//			}
//			new AddUpdateSnapChatTime().execute(chatMessageObj);
//
//		}
	}

	private void setTypeFace() {
		TextView textView1, textView2;
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);

		Typeface typeface = FontStyle.getFont(Activity_ShowVideo.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		textView1.setTypeface(typeface);

		textView2.setTypeface(typeface);

	}

	// private void loadData() {
	//
	// // setListeners();
	// SimpleDateFormat dateFormat = new SimpleDateFormat(
	// "dd MMM yyyy hh:mm aa");
	// Date date = new Date(Long.valueOf(chatMessageObj.getTime()));
	//
	// // byte windowImageString[] = Base64.decode(chatMessageObj.getMessage(),
	// // Base64.DEFAULT);
	// // bitmap = BitmapFactory.decodeByteArray(windowImageString, 0,
	// // windowImageString.length);
	// //
	// // showImage.setImageBitmap(bitmap);
	// // imageLoader.DisplayImage(chatMessageObj.getMessage(), showImage);
	// showImage.setVisibility(View.VISIBLE);
	// }

	private void setLayout() {

		cancelLayout = (LinearLayout) findViewById(R.id.cancleLayout);

		showImage = (TouchImageView) findViewById(R.id.showImage);
		dateTime = (TextView) findViewById(R.id.dateTime);
		rotateClockWise = (ImageButton) findViewById(R.id.clock);
		rotateAntiClockWise = (ImageButton) findViewById(R.id.anticlock);
		rotateClockWise.setVisibility(View.GONE);
		rotateAntiClockWise.setVisibility(View.GONE);
		cancelLayout.setVisibility(View.INVISIBLE);
	}

	private void setListeners() {
		cancelLayout = (LinearLayout) findViewById(R.id.cancleLayout);
		cancelLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

	}

	class AddUpdateSnapChatTime extends
			AsyncTask<ChatDetailsDTO, Void, JSONObject> {
		String vStatus;
		TransparentProgressDialog dialog;
		ChatDetailsDTO chatDetailsDTO = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new TransparentProgressDialog(context);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONObject doInBackground(ChatDetailsDTO... args) {
			chatDetailsDTO = args[0];
			// http://54.219.211.237/KraveAPI/api_calls/isImageSeen.php?user_id=770&image=1432104507_.png?seconds=11&time=11
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String imageUrl = chatDetailsDTO.getMessage();
			params.add(new BasicNameValuePair("user_id", ""
					+ chatDetailsDTO.getFromuser()));
			params.add(new BasicNameValuePair("image", imageUrl.substring(
					(imageUrl.lastIndexOf('/') + 1),
					(imageUrl.lastIndexOf('?')))));
			params.add(new BasicNameValuePair("time", "" + leftSnapChatTime));
			Log.d("ADD_UPDATE_SNAP_CHAT_MSG_TIME", ""
					+ WebServiceConstants.ADD_UPDATE_SNAP_CHAT_MSG_TIME
					+ params);
			JSONObject json = new JSONParser().makeHttpRequest2(
					WebServiceConstants.ADD_UPDATE_SNAP_CHAT_MSG_TIME, "POST",
					params);
			Log.d("ADD_UPDATE_SNAP_CHAT_MSG_TIME", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			// dialog.dismiss();
			// {"MSG":"45","S":200}
			try {
				if (jsonObject.getInt("S") == AppConstants.RESPONSE_SUCCESS) {
					// Toast.makeText(Activity_ShowImage.this,
					// "" + jsonObject.getString("MSG"),
					// Toast.LENGTH_SHORT).show();

				} else {

				}

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}

	class CheckChatPostImage extends
			AsyncTask<ChatDetailsDTO, Void, JSONObject> {
		String vStatus;
		TransparentProgressDialog dialog;
		ChatDetailsDTO chatDetailsDTO = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new TransparentProgressDialog(context);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONObject doInBackground(ChatDetailsDTO... args) {
			chatDetailsDTO = args[0];
			// http://54.219.211.237/KraveAPI/api_calls/checkChatPostImage.php?user_id=770&image=1432104507_.png?seconds=11
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String imageUrl = chatDetailsDTO.getMessage();
			params.add(new BasicNameValuePair("user_id", ""
					+ chatDetailsDTO.getFromuser()));
			params.add(new BasicNameValuePair("image", ""
					+ imageUrl.substring((imageUrl.lastIndexOf('/') + 1),
							(imageUrl.lastIndexOf('?')))));
			Log.d("", "CheckChatPostImage=" + params);
			JSONObject json = new JSONParser().makeHttpRequest2(
					WebServiceConstants.CHECK_SNAP_CHAT_MSG_TIME_LEFT, "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			// dialog.dismiss();
			// {"MSG":"45","S":200}
			try {
				if (jsonObject.getInt("S") == AppConstants.RESPONSE_SUCCESS) {
					ImageView image = (ImageView) chatDetailsDTO.getView();
					leftSnapChatTime = Integer.valueOf(jsonObject
							.getString("MSG"));
					// String url = chatDetailsDTO.getMessage().split("=")[0]
					// + leftTime;
					if (leftSnapChatTime < 0) {
						leftSnapChatTime = Integer.valueOf(chatMessageObj
								.getMessage().split("=")[1]);

					}
					Log.d("", "remaining time" + leftSnapChatTime);
					if (leftSnapChatTime == 0) {
						lockImage();
					} else {
						showImage();
					}
					// switch (leftSnapChatTime) {
					// case -1:
					//
					// break;
					// case 0:
					//
					// break;
					//
					// default:
					// showImage();
					//
					// break;
					// }
				} else {

				}

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}
	
	
	
	
	
//	// Declare variables
//	ProgressDialog pDialog;
//	VideoView videoview;
//
//	// Insert your Video URL
//	String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// Get the layout from video_main.xml
//		setContentView(R.layout.activity_show_video);
//		activityObject = this;
//		// Find your VideoView in your video_main.xml layout
//		videoview = (VideoView) findViewById(R.id.video_player_view);
//		// Execute StreamVideo AsyncTask
//
//		// Create a progressbar
//		pDialog = new ProgressDialog(Activity_ShowVideo.this);
//		// Set progressbar title
//		pDialog.setTitle("Android Video Streaming Tutorial");
//		// Set progressbar message
//		pDialog.setMessage("Buffering...");
//		pDialog.setIndeterminate(false);
//		pDialog.setCancelable(false);
//		// Show progressbar
//		pDialog.show();
//
//		try {
//			// Start the MediaController
//			MediaController mediacontroller = new MediaController(
//					Activity_ShowVideo.this);
//			mediacontroller.setAnchorView(videoview);
//			// Get the URL from String VideoURL
//			Uri video = Uri.parse(chatMessageObj.getMessage().split("\\|")[1]);
//			videoview.setMediaController(mediacontroller);
//			videoview.setVideoURI(video);
//
//		} catch (Exception e) {
//			Log.e("Error", e.getMessage());
//			e.printStackTrace();
//		}
//
//		videoview.requestFocus();
//		videoview.setOnPreparedListener(new OnPreparedListener() {
//			// Close the progress bar and play the video
//			public void onPrepared(MediaPlayer mp) {
//				pDialog.dismiss();
//				videoview.start();
//			}
//		});
//
//	}

}
