package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.krave.kraveapp.Activity_Feedback.SendFeedbackAsynchTask;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.UserDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentFeedback extends Fragment implements OnClickListener {

	Activity_Home context;
	SessionManager sessionManager;
	UserDTO userDTO;
	AppManager singleton;
	private ImageView submitButton;
	private EditText feedBackEditText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_feedback, container,
				false);
		System.gc();
		context = (Activity_Home) getActivity();
		sessionManager = new SessionManager(context);

		userDTO = sessionManager.getUserDetail();
		AppManager.initInstance();
       	singleton = AppManager.getInstance();
       	
		setLayout(view);
		setListener();
		setTypeFace();

       	
		return view;
	}

	public void setLayout(View view){
		feedBackEditText = (EditText) view.findViewById(R.id.oldPassword);
		submitButton = (ImageView) view.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (0 == feedBackEditText.getText().toString().length()) {
					Toast.makeText(context, R.string.toast_enter_some_text,
							Toast.LENGTH_SHORT).show();
				} else {
					context.easyTrackerEventLog(AppConstants.EVENT_LOG_CATEG_CONT_US, 
							AppConstants.EVENT_LOG_ACTION_BTN, "SubmitFeedBack");
					
					new SendFeedbackAsynchTask()
							.execute(WebServiceConstants.BASE_URL
									+ WebServiceConstants.SEND_FEEDBACK);

				}

			}
		});
	}
	
	public void setTypeFace(){
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		feedBackEditText.setTypeface(typeface);
	}
	
	public void setListener() {

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	public void setVisibility(LinearLayout linear){
		if(linear.getVisibility() == View.VISIBLE)
			linear.setVisibility(View.GONE);
		else
			linear.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
			

		}
	}
	
	class SendFeedbackAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// http://198.12.150.189/~simssoe/index.php?action=send_feedback&feedback=hi&user_id=10
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			params.add(new BasicNameValuePair("user_id", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("feedback", ""
					+ feedBackEditText.getText().toString()));

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
					System.out.print("feedback response : " + jsonArray);
					sessionManager.setFeedback("");
					Toast.makeText(context,
							R.string.toast_your_feedback_sent_successfully,
							Toast.LENGTH_SHORT).show();
					sessionManager.setFeedback("");

				} else if (vStatus.equals("failure")) {
					Toast.makeText(context,
							R.string.toast_feedback_not_send_try_again,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
}
