package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.krave.kraveapp.Activity_Home.AcceptPhotoRequest;
import com.krave.kraveapp.Activity_Home.GetChatHistoryByPagination;
import com.ps.adapters.ImageRequstListAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.ImageRequestDto;
import com.ps.models.UserDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FragmentImageRequest extends Fragment implements
		OnItemClickListener {
	// view
	private ListView listView;
	private ImageView loadingView;
	private LinearLayout llLoading;

	// data
	private Activity_Home context;
	private ImageRequstListAdapter imageRequstListAdapter;
	private AppManager singleton;
	private SessionManager sessionManager;
	private List<ImageRequestDto> imageRequestDtos = new ArrayList<ImageRequestDto>();
	private int position;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_request,
				container, false);
		context = (Activity_Home) getActivity();
		Typeface typefaceHeader = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);

		context.headerIcon.setVisibility(View.GONE);
		context.title.setVisibility(View.VISIBLE);
		context.title.setText(R.string.titel_notifications);
		context.title.setTypeface(typefaceHeader);
		AppManager.initInstance();
		singleton = AppManager.getInstance();
		sessionManager = new SessionManager(context);
		imageRequstListAdapter = new ImageRequstListAdapter(context,
				imageRequestDtos);
		setLayout(view);
		new GetAllNotification()
				.execute(WebServiceConstants.GET_ALL_IMAGE_REQUEST);
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

	private void setLayout(View view) {
		LinearLayout mainview = (LinearLayout) view.findViewById(R.id.mainview);
		loadingView = (ImageView) view.findViewById(R.id.loadingView);
		llLoading = (LinearLayout) view.findViewById(R.id.llLoading);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		listView = (ListView) view.findViewById(R.id.list);
		listView.setOnItemClickListener(this);
		listView.setAdapter(imageRequstListAdapter);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.position = position;
		openDailogForAcceptPhotoRequest(imageRequestDtos.get(position));

	}

	// private void sendPhotoRequestBroadcast(String message) {
	//
	// Log.d("", "sendPhotoRequestBroadcast");
	// Intent broadcast = new Intent();
	// broadcast.putExtra("come", "photo_request");
	// broadcast.putExtra("message", message);
	// broadcast.setAction(AppConstants.BROADCAST_ACTION);
	// context.sendBroadcast(broadcast);
	//
	// }

	public void openDailogForAcceptPhotoRequest(final ImageRequestDto dto) {
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_accept_photo_request);
		try {

			Button noButton = (Button) dialog.findViewById(R.id.no);
			Button yesButton = (Button) dialog.findViewById(R.id.yes);
			Button okButton = (Button) dialog.findViewById(R.id.ok);
			TextView title = (TextView) dialog.findViewById(R.id.titel);
			TextView subTitle = (TextView) dialog.findViewById(R.id.sub_titel);
			Typeface typeface = FontStyle.getFont(context,
					AppConstants.HelveticaNeueLTStd_Lt);
			title.setTypeface(typeface);
			noButton.setTypeface(typeface);
			yesButton.setTypeface(typeface);
			okButton.setTypeface(typeface);

			if (!dto.getAlert().contains(
					context.getResources().getString(
							R.string.notification_you_have_an_image_request_))) {

				subTitle.setVisibility(View.GONE);
				noButton.setVisibility(View.GONE);
				yesButton.setVisibility(View.GONE);
				okButton.setVisibility(View.VISIBLE);
			}
			String alert = "";
			if (dto.getAlert().contains(
					context.getResources().getString(
							R.string.notification_you_have_an_image_request_))) {

				alert = getResources()
						.getString(
								R.string.dialog_is_requesting_to_view_your_private_photo);

			} else if (dto.getAlert().contains(
					context.getResources().getString(
							R.string.notification_your_request_is_accepted_))) {

				alert = getResources()
						.getString(
								R.string.dialog_has_approved_your_request_to_view_his_private_photo);
			} else if (dto.getAlert().contains(
					context.getResources().getString(
							R.string.notification_your_request_is_declined_))) {
				alert = getResources()
						.getString(
								R.string.dialog_has_declined_your_request_to_view_his_private_photo_);
			}

			title.setText(dto.getfName() + " " + alert);
			noButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AcceptPhotoRequest()
							.execute(
									WebServiceConstants.ACCEPT_VIEW_PRIVATE_PHOTO_REQUEST,
									dto.getUserid(), dto.getOwnerId(),
									dto.getImageId(),
									AppConstants.PRIVATE_PHOTO_REQUEST_REJECT);
					dialog.dismiss();
				}
			});
			yesButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (WebServiceConstants.isOnline(context)) {

						new AcceptPhotoRequest()
								.execute(
										WebServiceConstants.ACCEPT_VIEW_PRIVATE_PHOTO_REQUEST,
										dto.getUserid(),
										dto.getOwnerId(),
										dto.getImageId(),
										AppConstants.PRIVATE_PHOTO_REQUEST_ACCEPT);

					}
				}
			});

			okButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialog.dismiss();
					if (WebServiceConstants.isOnline(context)) {

						new CancelPhotoResponse().execute(
								WebServiceConstants.CANCEL_IMAGE_RESPONSE,
								dto.getNotificationId());

					}
				}
			});

		} catch (Exception e) {
			Log.d("", "openDailogForAcceptPhotoRequest" + e);
		}

		dialog.show();
	}

	class GetAllNotification extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			singleton.progressLoading(loadingView, llLoading);

		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			if (!(sessionManager.getUserDetail().getUserID() == null)) {
				params.add(new BasicNameValuePair("user_id", sessionManager
						.getUserDetail().getUserID()));
			}
			// -- - - -- - - -
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("GetNotification :", "GetNotification=" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			singleton.stopLoading(llLoading);
			imageRequestDtos.clear();
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String status = jsonObject.getString("status");
				if (status.equals("success")) {
					JSONArray notificationArray = jsonObject
							.getJSONArray("notification");

					for (int i = 0; i < notificationArray.length(); i++) {
						JSONObject Object = notificationArray.getJSONObject(i);
						ImageRequestDto imageRequestDto = new ImageRequestDto();
						imageRequestDto.setJsonObject(Object, context);
						imageRequestDtos.add(imageRequestDto);
					}
				} else if (status.equals("no notification")) {
					Toast.makeText(context,
							R.string.toast_no_notification_found,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("", "imageRequestDtos=" + imageRequestDtos.size());
			imageRequstListAdapter.notifyDataSetChanged();
		}
	}

	class AcceptPhotoRequest extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;
		String userID;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// loadCount++;
			singleton.progressLoading(loadingView, llLoading);
		}

		protected JSONArray doInBackground(String... args) {
			// http://54.219.211.237/index.php?action=respondRequest&user_id=295&owner_id=13605&image_id=952&response=1
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// selected user's ID
			params.add(new BasicNameValuePair("user_id", args[1]));
			params.add(new BasicNameValuePair("owner_id", args[2]));
			params.add(new BasicNameValuePair("image_id", args[3]));
			params.add(new BasicNameValuePair("response", args[4]));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			Log.d("", "AcceptPhotoRequest=" + jsonArray);

			singleton.stopLoading(llLoading);
			try {

				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String status = jsonObject.getString("status");
				if (status.equals("success")
						|| status.equals("Request not responded")
						// || status.equals("Request rejected")
						|| status.equals("user logout")) {
					Toast.makeText(context,
							R.string.toast_request_succefully_accepted,
							Toast.LENGTH_SHORT).show();
					imageRequestDtos.remove(position);
					imageRequstListAdapter.notifyDataSetChanged();
				} else if (status.equals("Request rejected")) {
					Toast.makeText(context, R.string.toast_request_rejected,
							Toast.LENGTH_SHORT).show();
				}

				else if (status.equals("failure")) {
					Toast.makeText(context, R.string.toast_failed_try_again,
							Toast.LENGTH_SHORT).show();
				}
				// -- - - - - - -

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	class CancelPhotoResponse extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;
		String userID;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// loadCount++;
			singleton.progressLoading(loadingView, llLoading);
		}

		protected JSONArray doInBackground(String... args) {
			// http://54.219.211.237/index.php?action=respondRequest&user_id=295&owner_id=13605&image_id=952&response=1
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// selected user's ID
			params.add(new BasicNameValuePair("notification_id", args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			Log.d("", "CancelPhotoResponse=" + jsonArray);

			singleton.stopLoading(llLoading);
			try {

				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String status = jsonObject.getString("status");
				if (status.equals("success")) {
					// Toast.makeText(context, "Request succefully Accepted.",
					// Toast.LENGTH_SHORT).show();
					imageRequestDtos.remove(position);
					imageRequstListAdapter.notifyDataSetChanged();
				}
				// -- - - - - - -

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
