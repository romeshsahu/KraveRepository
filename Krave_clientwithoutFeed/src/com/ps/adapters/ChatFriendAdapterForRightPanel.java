package com.ps.adapters;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.AppManager;
import com.krave.kraveapp.R;
import com.ps.models.ChatDetailsDTO;
import com.ps.models.UserDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.ImageLoaderCircleAsyncTask;
import com.ps.utill.KraveDAO;
import com.ps.utill.Utils;

public class ChatFriendAdapterForRightPanel extends BaseAdapter {

	Context mActivity;
	ArrayList<UserDTO> marrayList;
	ImageLoaderCircleAsyncTask imageLoaderCircle;
	ImageLoader imageLoader;
	ArrayList<UserDTO> filteredData = null;
	Filter mFilter = new ItemFilter();
	int check;
	private GPSTracker gpsTracker;
	private AppManager singleton;

	KraveDAO databaseHelper;

	public ChatFriendAdapterForRightPanel(Context Activity,
			List<UserDTO> arrayList, int check) {
		this.mActivity = Activity;
		this.marrayList = (ArrayList<UserDTO>) arrayList;
		this.filteredData = (ArrayList<UserDTO>) arrayList;
		this.check = check;
		imageLoaderCircle = new ImageLoaderCircleAsyncTask(mActivity);
		// imageLoader = new ImageLoader(mActivity);
		gpsTracker = new GPSTracker(mActivity);
		databaseHelper = new KraveDAO(mActivity);
		AppManager.initInstance();
		singleton = AppManager.getInstance();
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
	public class ViewHolder {
		ImageView marker, userimage2, timeImage;
		RelativeLayout notification;
		TextView name, email, lastMsgTime, notificationTv;
		CircleImageView userimage;
		LinearLayout unreadMsgLayout;
	}

	// getview for single row
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {

		// Edited on revision 894
		// MAY 14 2015
		// dl
		String checker = "no";
		ViewHolder viewHolder;
		UserDTO userDTO = (UserDTO) getItem(position);

		if (rowView == null) {
			//imageLoaderCircle.clearCache();
			viewHolder = new ViewHolder();

			rowView = ((Activity) mActivity).getLayoutInflater().inflate(
					R.layout.row_home4, null);
			viewHolder.userimage = (CircleImageView) rowView
					.findViewById(R.id.userimage);
			viewHolder.lastMsgTime = (TextView) rowView
					.findViewById(R.id.lastMsgTime);
			viewHolder.timeImage = (ImageView) rowView
					.findViewById(R.id.timeImage);
			viewHolder.marker = (ImageView) rowView.findViewById(R.id.marker);

			viewHolder.notification = (RelativeLayout) rowView
					.findViewById(R.id.notification);
			viewHolder.notificationTv = (TextView) rowView
					.findViewById(R.id.notificationTv);

			viewHolder.name = (TextView) rowView.findViewById(R.id.name);
			viewHolder.email = (TextView) rowView.findViewById(R.id.email);

			viewHolder.unreadMsgLayout = (LinearLayout) rowView
					.findViewById(R.id.unreadMessageLayout);

			try {
				System.out.println("asdasd : " + singleton.chatUserID);
				if (singleton.chatUserID.equals(userDTO.getUserID())) {
					userDTO.setUnreadMsg("1");
					singleton.chatUserID = "-1";
				}
			} catch (Exception e) {
			}

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.name.setTypeface(typeface);
			viewHolder.email.setTypeface(typeface);

			rowView.setTag(viewHolder);
		} else {

			viewHolder = (ViewHolder) rowView.getTag();
		}

		// if (check == 3 || check == 1) {
//		 if (viewHolder.userimage != null) {
//		 new
//		 ImageDownloaderTask(viewHolder.userimage).execute(userDTO.getProfileImage());
//		 }
		imageLoaderCircle.DisplayImage(userDTO.getProfileImage(),
				viewHolder.userimage);

		// } else {
		// imageLoaderCircle.DisplayImage(AppConstants.AWATAR_IAMGE,
		// viewHolder.userimage);
		//
		// }

		if (userDTO.getIsOnline().equals(AppConstants.ONLINE)) {
			viewHolder.marker.setBackgroundResource(R.drawable.online);
		} else {
			viewHolder.marker.setBackgroundResource(R.drawable.white_crl);
		}

		/* Unread Chat Count */

		if (Integer.valueOf(userDTO.getUnreadMsg()) > 0) {// Integer.valueOf(userDTO.getUnreadMsg())
															// >
															// 0)
															// {
			viewHolder.notification.setVisibility(View.VISIBLE);
			viewHolder.notificationTv.setText(""
					+ singleton.getChatCount.get(userDTO.getUserID()));// userDTO.getUnreadMsg());
			viewHolder.unreadMsgLayout.setBackgroundResource(R.color.select);
		} else {
			viewHolder.notification.setVisibility(View.GONE);
			viewHolder.unreadMsgLayout
					.setBackgroundResource(R.color.transprent_color_code);
		}

		viewHolder.name.setText(userDTO.getFirstName() + " "
				+ userDTO.getLastName());

		viewHolder.email.setVisibility(View.VISIBLE);
		// for last message type is location message or other

		viewHolder.email.setText(userDTO.getUserLastMsg());
		viewHolder.timeImage.setVisibility(View.VISIBLE);
		if (userDTO.getUserLastMsgFromOrTo().equals(
				Activity_Home.sessionManager.getUserDetail().getUserID())) {
			viewHolder.timeImage.setImageResource(R.drawable.reverse);
		} else if (userDTO.getUserLastMsgFromOrTo().equals("0")) {
			viewHolder.timeImage.setVisibility(View.INVISIBLE);

		} else {
			viewHolder.timeImage.setImageResource(R.drawable.right);
		}
		// viewHolder.timeImage.setVisibility(View.VISIBLE);
		// viewHolder.lastMsgTime.setVisibility(View.VISIBLE);
		viewHolder.lastMsgTime.setText(userDTO.getUserLastMsgTime());

		return rowView;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		for (UserDTO dto : filteredData) {
			setUserChatInformation(dto);
		}
		Collections.sort(filteredData, new Comparator<UserDTO>() {
			@Override
			public int compare(UserDTO arg0, UserDTO arg1) {
				// TODO Auto-generated method stub
				return arg1.getUnreadMsg().compareTo(arg0.getUnreadMsg());
			}
		});
		super.notifyDataSetChanged();
	}

	private void setUserChatInformation(UserDTO userDTO) {
		String user_id = userDTO.getUserID();
		// set last msg
		try {
			if (singleton.getLastMsg.get(user_id).contains(
					AppConstants.BASE_IMAGE_PATH_1)) {
				if (singleton.getLastMsg.get(user_id).contains("|")) {
					userDTO.setUserLastMsg("VIDEO");
				} else {
					userDTO.setUserLastMsg("PHOTO");
				}
			} else if (singleton.getLastMsg.get(user_id).contains("|")) {
				userDTO.setUserLastMsg(singleton.getLastMsg.get(user_id).split(
						"\\|")[2]);
			} else {
				userDTO.setUserLastMsg(singleton.getLastMsg.get(user_id));
			}
		} catch (Exception e) {
		}

		// set last msg time
		try {

			userDTO.setUserLastMsgTime(Utils.getTime(singleton.getLastMsgTime
					.get(user_id)));
			userDTO.setUserLastMsgFromOrTo(singleton.getLastMsgFromOrToUser
					.get(user_id));

		} catch (Exception e) {
		}
		// set unread msg count
		try {
			System.out.println(singleton.getChatCount.get(user_id));
			if (singleton.getChatCount.get(user_id) != null) {
				userDTO.setUnreadMsg("" + singleton.getChatCount.get(user_id));
			} else {
				userDTO.setUnreadMsg("" + 0);
			}
		} catch (Exception e) {
			userDTO.setUnreadMsg("" + 0);
		}
		// set last msg to or from
	}

	

	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().split("/")[0]
					.toString().toLowerCase();
			String online = constraint.toString().split("/")[1].toString();
			FilterResults results = new FilterResults();
			// final List<UserDTO> list = arrayList;

			int count = marrayList.size();
			final ArrayList<UserDTO> nlist = new ArrayList<UserDTO>(count);

			String compayerString;
			String isOnline;
			for (int i = 0; i < count; i++) {
				UserDTO dto = marrayList.get(i);
				compayerString = dto.getFirstName().toString()
						+ dto.getLastName().toString();
				isOnline = dto.getIsOnline();
				if (compayerString.toLowerCase().contains(filterString)) {
					if (AppConstants.ONLINE.equals(online)) {
						if (isOnline.equals(AppConstants.ONLINE)) {
							nlist.add(dto);
						}
					} else {
						nlist.add(dto);
					}

				}
			}
			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredData = (ArrayList<UserDTO>) results.values;
			notifyDataSetChanged();
		}

	}

	class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;

		public ImageDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			return downloadBitmap(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					if (bitmap != null) {
						imageView.setImageBitmap(bitmap);
					} else {
						Drawable placeholder = imageView.getContext()
								.getResources()
								.getDrawable(R.drawable.loading);
						imageView.setImageDrawable(placeholder);
					}
				}
			}
		}
	}

	private Bitmap downloadBitmap(String url) {
		HttpURLConnection urlConnection = null;
		try {
			URL uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			int statusCode = urlConnection.getResponseCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			InputStream inputStream = urlConnection.getInputStream();
			if (inputStream != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
		} catch (Exception e) {
			// urlConnection.disconnect();
			Log.w("ImageDownloader", "Error downloading image from " + url);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
	}
}
