package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.krave.kraveapp.Activity_Friens_Request_Accept;
import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.Activity_Push_Notification;
import com.krave.kraveapp.R;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.ImageRequestDto;
import com.ps.models.UserDTO;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class ImageRequstListAdapter extends BaseAdapter {

	Context mActivity;
	ArrayList<ImageRequestDto> marrayList;
	ImageLoaderCircle imageLoaderCircle;
	private List<ImageRequestDto> filteredData = null;

	// private SessionManager sessionManager;
	// private ItemFilter mFilter = new ItemFilter();

	public ImageRequstListAdapter(Context Activity,
			List<ImageRequestDto> arrayList) {
		this.mActivity = Activity;
		// this.marrayList = (ArrayList<UserDTO>) arrayList;
		this.filteredData = arrayList;
		imageLoaderCircle = new ImageLoaderCircle(mActivity);
		// sessionManager = new SessionManager(mActivity);

	}

	// methed to count
	@Override
	public int getCount() {
		return filteredData.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredData.get(position);// /[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// static class for object
	public static class ViewHolder {
		ImageView marker, rightButton, crossButton;
		TextView name, message, time;
		CircleImageView userimage;

	}

	// getview for single row
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = null;
		ViewHolder viewHolder = new ViewHolder();
		final ImageRequestDto imageRequestDto = (ImageRequestDto) getItem(position);
		if (convertView == null) {
			rowView = ((Activity) mActivity).getLayoutInflater().inflate(
					R.layout.row_image_requsts_list, null);
			viewHolder.marker = (ImageView) rowView.findViewById(R.id.marker);
			viewHolder.name = (TextView) rowView.findViewById(R.id.name);
			viewHolder.message = (TextView) rowView.findViewById(R.id.message);
			viewHolder.time = (TextView) rowView.findViewById(R.id.time);
			viewHolder.userimage = (CircleImageView) rowView
					.findViewById(R.id.userimage);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.name.setTypeface(typeface);
			viewHolder.message.setTypeface(typeface);
			viewHolder.time.setTypeface(typeface);
			rowView.setTag(viewHolder);
		} else {
			rowView = convertView;
			viewHolder = (ViewHolder) rowView.getTag();
		}

		viewHolder.name.setText("" + imageRequestDto.getfName());
		viewHolder.message.setText("" + imageRequestDto.getAlert());
		viewHolder.time.setText("" + imageRequestDto.getNotificationTime());
		if (imageRequestDto.getProfileImage().length() != 0) {
			imageLoaderCircle.DisplayImage(imageRequestDto.getProfileImage(),
					viewHolder.userimage);
		} else {
			imageLoaderCircle.DisplayImage(AppConstants.AWATAR_IAMGE,
					viewHolder.userimage);
		}
		// }
		// viewHolder.rightButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// new AcceptLikeRequestAsynchTask().execute(
		// WebServiceConstants.BASE_URL
		// + WebServiceConstants.ACCEPT_LIKE_REQUEST, ""
		// + userDTO.getUserID());
		//
		// }
		// });
		// viewHolder.crossButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// new RejectLikeRequestAsynchTask().execute(
		// WebServiceConstants.BASE_URL
		// + WebServiceConstants.REJECT_LIKE_REQUEST, ""
		// + userDTO.getUserID());
		//
		// }
		// });
		return rowView;
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();

	}

	// public Filter getFilter() {
	// return mFilter;
	// }
	//
	// private class ItemFilter extends Filter {
	// @Override
	// protected FilterResults performFiltering(CharSequence constraint) {
	// String filterString = constraint.toString().toLowerCase();
	// FilterResults results = new FilterResults();
	// // final List<UserDTO> list = arrayList;
	//
	// int count = marrayList.size();
	// final ArrayList<UserDTO> nlist = new ArrayList<UserDTO>(count);
	//
	// String compayerString;
	// for (int i = 0; i < count; i++) {
	// UserDTO dto = marrayList.get(i);
	// compayerString = dto.getFirstName().toString()
	// + dto.getLastName().toString();
	// if (compayerString.toLowerCase().contains(filterString)) {
	// nlist.add(dto);
	// }
	// }
	// results.values = nlist;
	// results.count = nlist.size();
	//
	// return results;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// protected void publishResults(CharSequence constraint,
	// FilterResults results) {
	// filteredData = (ArrayList<UserDTO>) results.values;
	// notifyDataSetChanged();
	// }
	//
	// }
	//
	// class AcceptLikeRequestAsynchTask extends
	// AsyncTask<String, Void, JSONArray> {
	// String vStatus;
	// TransparentProgressDialog dialog;
	// String poss;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog = new TransparentProgressDialog(mActivity);
	// // dialog.setMessage("Please Wait...");
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	// }
	//
	// protected JSONArray doInBackground(String... args) {
	// poss = args[1];
	// // action=acceptfriendrequest&user_id=107&friend_id=77
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// // params.add(new BasicNameValuePair("action", "login_user"));
	// params.add(new BasicNameValuePair("user_id", sessionManager
	// .getUserDetail().getUserID()));
	// params.add(new BasicNameValuePair("friend_id", poss));
	// // params.add(new BasicNameValuePair("user_email", filteredData.get(
	// // poss).getEmail()));
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	// Log.d("get Like response", "" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	// dialog.dismiss();
	//
	// try {
	// JSONObject mJsonObject = jsonArray.getJSONObject(0);
	// vStatus = mJsonObject.getString("status");
	// System.out.print("" + jsonArray);
	// if (vStatus.equals("success")) {
	// UserDTO userDTO = new UserDTO();
	//
	// for (int i = 0; i < filteredData.size(); i++) {
	// if (poss.equals(filteredData.get(i).getUserID())) {
	// userDTO = filteredData.get(i);
	// filteredData.remove(i);
	// break;
	// }
	// }
	// for (int j = 0; j < marrayList.size(); j++) {
	// if (poss.equals(marrayList.get(j).getUserID())) {
	// userDTO = marrayList.get(j);
	// marrayList.remove(j);
	// break;
	// }
	// }
	// notifyDataSetChanged();
	// // /////////////
	// Activity_Home activity_Home = (Activity_Home) mActivity;
	//
	// activity_Home.ShowFriendAcceptDialog(userDTO);
	// // /////////////
	//
	// } else if (vStatus.equals("failure")) {
	// Toast.makeText(mActivity, "Fail", Toast.LENGTH_SHORT)
	// .show();
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }
	//
	// class RejectLikeRequestAsynchTask extends
	// AsyncTask<String, Void, JSONArray> {
	// String vStatus;
	// TransparentProgressDialog dialog;
	// String poss;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog = new TransparentProgressDialog(mActivity);
	// // dialog.setMessage("Please Wait...");
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	// }
	//
	// protected JSONArray doInBackground(String... args) {
	// poss = args[1];
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// // params.add(new BasicNameValuePair("action", "login_user"));
	// params.add(new BasicNameValuePair("user_id", sessionManager
	// .getUserDetail().getUserID()));
	// params.add(new BasicNameValuePair("friend_id", poss));
	// // params.add(new BasicNameValuePair("user_email", filteredData.get(
	// // poss).getEmail()));
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	// Log.d("get Like response", "" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	// dialog.dismiss();
	//
	// try {
	// JSONObject mJsonObject = jsonArray.getJSONObject(0);
	// vStatus = mJsonObject.getString("status");
	// System.out.print("" + jsonArray);
	// if (vStatus.equals("success")) {
	// for (int i = 0; i < filteredData.size(); i++) {
	// if (poss.equals(filteredData.get(i).getUserID())) {
	// filteredData.remove(i);
	// break;
	// }
	// }
	// for (int j = 0; j < marrayList.size(); j++) {
	// if (poss.equals(marrayList.get(j).getUserID())) {
	// marrayList.remove(j);
	// break;
	// }
	// }
	//
	// notifyDataSetChanged();
	// } else if (vStatus.equals("failure")) {
	// Toast.makeText(mActivity, "Fail", Toast.LENGTH_SHORT)
	// .show();
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }
}
