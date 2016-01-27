package com.ps.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.AppManager;
import com.krave.kraveapp.FragmentDetailDudesProfile;
import com.krave.kraveapp.FragmentHome;
import com.krave.kraveapp.FragmentSearchCity;
import com.krave.kraveapp.R;
import com.ps.models.UserDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.DistanceConverter;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.SessionManager;

public class FindDudesGridAdapter extends BaseAdapter {

	Context mActivity;
	Activity_Home context;
	ArrayList<UserDTO> marrayList;
	ImageLoader imageLoader;
	ArrayList<UserDTO> filteredData = null;
	ArrayList<UserDTO> onlineData;
	// Filter mFilter = new ItemFilter();
	private GPSTracker gpsTracker;
	DistanceConverter distCon;
	int transPref;
	public boolean isOnlineOnly;
	boolean fromSearchCity;
	int startIndex, endIndex;

	public FindDudesGridAdapter(Context Activity, List<UserDTO> arrayList,
			boolean fromSearchCity) {
		context = (Activity_Home) Activity;
		this.mActivity = Activity;
		this.distCon = new DistanceConverter();
		this.marrayList = (ArrayList<UserDTO>) arrayList;
		this.filteredData = (ArrayList<UserDTO>) arrayList;
		this.onlineData = new ArrayList<UserDTO>();
		imageLoader = new ImageLoader(mActivity);
		gpsTracker = new GPSTracker(mActivity);
		isOnlineOnly = false;
		this.fromSearchCity = fromSearchCity;

		AppManager.initInstance();
		AppManager singleton = AppManager.getInstance();
		transPref = singleton.mTransitPrefs.getInt(
				AppConstants.TRANSIT_PREFERENCE, 1);
	}

	public void setListIndex(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public void setOnlineDataBeforeNotify() {
		Log.d("JCv", "setOnlineDataBeforeNotify");

		this.onlineData.clear();
		for (UserDTO userDTO : filteredData) {
			Log.d("JCv", "online: " + userDTO.getIsOnline());
			if (userDTO.getIsOnline().equalsIgnoreCase("available")) {
				this.onlineData.add(userDTO);
			}
		}
	}

	public void addOnlineDataBeforeNotify(UserDTO userDTO) {
		this.onlineData.add(userDTO);
	}

	// methed to count
	@Override
	public int getCount() {

		return new ArrayList<UserDTO>(
				filteredData.subList(startIndex, endIndex)).size();// isOnlineOnly?onlineData.size():filteredData.size();
	}

	@Override
	public Object getItem(int position) {
		return new ArrayList<UserDTO>(
				filteredData.subList(startIndex, endIndex)).get(position);// isOnlineOnly?onlineData.get(position):filteredData.get(position);//
		// /[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// static class for object
	public static class ViewHolder {
		ImageView marker, userimage2, imageViewStamp, imgViewTrans,
				onlineIndicator, onlineInLastWeekIndicator;
		TextView name, email, trans;
		LinearLayout unreadMsgLayout;
	}

	// getview for single row
	@Override
	public View getView(final int position, View rowView, ViewGroup parent) {

		ViewHolder viewHolder = new ViewHolder();
		final UserDTO userDTO = (UserDTO) getItem(position);

		if (rowView == null) {
			viewHolder = new ViewHolder();
			rowView = ((Activity) mActivity).getLayoutInflater().inflate(
					R.layout.row_home_new, null);
			viewHolder.userimage2 = (ImageView) rowView
					.findViewById(R.id.userimage2);
			viewHolder.imageViewStamp = (ImageView) rowView
					.findViewById(R.id.imageView1Stamp);

			viewHolder.marker = (ImageView) rowView.findViewById(R.id.marker);
			viewHolder.onlineIndicator = (ImageView) rowView
					.findViewById(R.id.onlineIndicator);

			viewHolder.name = (TextView) rowView.findViewById(R.id.name);
			viewHolder.email = (TextView) rowView.findViewById(R.id.email);

			viewHolder.imgViewTrans = (ImageView) rowView
					.findViewById(R.id.transPic);
			viewHolder.trans = (TextView) rowView.findViewById(R.id.trans);

			viewHolder.unreadMsgLayout = (LinearLayout) rowView
					.findViewById(R.id.unreadMessageLayout);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.name.setTypeface(typeface);
			viewHolder.email.setTypeface(typeface);
			viewHolder.trans.setTypeface(typeface);

			rowView.setTag(viewHolder);
		} else {

			viewHolder = (ViewHolder) rowView.getTag();
		}

		// if (userDTO.getUserProfileImageDTOs().get(0).getImageId()
		// .equals(AppConstants.FACEBOOK_IMAGE)) {
		viewHolder.userimage2.setScaleType(ScaleType.FIT_CENTER);
		imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
				.getImagePath(), viewHolder.userimage2);
		// } else {
		// viewHolder.userimage2.setScaleType(ScaleType.FIT_XY);
		// if (!userDTO.getUserProfileImageDTOs().get(0)
		// .getIsImageActive()) {
		// viewHolder.imageViewStamp.setVisibility(View.VISIBLE);
		// viewHolder.userimage2
		// .setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
		// } else {
		// imageLoader.DisplayImage(userDTO
		// .getUserProfileImageDTOs().get(0)
		// .getImagePath(), viewHolder.userimage2);
		// }
		// }

		// JCv
		// if (AppConstants.ONLINE.equals(userDTO.getIsOnline())) {
		// viewHolder.userimage2.setBackgroundResource(R.drawable.online_user);
		// } else {
		// viewHolder.userimage2
		// .setBackgroundResource(R.drawable.offline_user);
		// }

		Log.d("JCv", "getIsOnline " + userDTO.getIsOnline());

		// Set onlineIndicator

		if (userDTO.getIsOnline().equalsIgnoreCase(AppConstants.ONLINE)) {
			// viewHolder.userimage2.setBackgroundResource(R.drawable.online_user);
			viewHolder.onlineIndicator
					.setBackgroundResource(R.drawable.select_onlinenow);
			viewHolder.onlineIndicator.setVisibility(View.VISIBLE);
		} else if (isActiveInLastSevenDays(userDTO.getLastActiveDate())) {
			// viewHolder.userimage2.setBackgroundResource(R.drawable.offline_user);
			viewHolder.onlineIndicator
					.setBackgroundResource(R.drawable.yellow_circle);
			viewHolder.onlineIndicator.setVisibility(View.VISIBLE);
		} else {
			// viewHolder.userimage2.setBackgroundResource(R.drawable.offline_user);
			viewHolder.onlineIndicator.setVisibility(View.GONE);

		}

		// String age = calculateAge(userDTO.getWhatAreYouDTO().getAge());
		String name = userDTO.getFirstName();

		if (name.length() > 13) {
			String subname = name.substring(0, 13);
			// viewHolder.name.setText(subname + "... , " + age);
			viewHolder.name.setText(subname + "...");
		} else {
			// viewHolder.name.setText(name + " , " + age);
			viewHolder.name.setText(name);

			// + userDTO.getLastName() + " , " + age);
		}

		float distance = (float) 0.0;
		try {
			gpsTracker.getLocation();
			// distance = Float.valueOf(userDTO.getLatLongDTO().getDistance());
			distance = distFrom(gpsTracker.getLatitude(),
					gpsTracker.getLongitude(),
					Double.valueOf(userDTO.getLatLongDTO().getLatitude()),
					Double.valueOf(userDTO.getLatLongDTO().getLongitude()));
		} catch (Exception e) {

		}
		viewHolder.email.setText("" + distance + " miles");

		viewHolder.trans.setText(distCon.convertToString((double) distance,
				distCon.getConversion(transPref)));

		viewHolder.imgViewTrans.setVisibility(View.VISIBLE);
		viewHolder.trans.setVisibility(View.VISIBLE);

		switch (transPref) {
		case 2:
			viewHolder.imgViewTrans
					.setImageResource(R.drawable.transit_walking);
			break;
		case 3:
			viewHolder.imgViewTrans.setImageResource(R.drawable.transit_biking);
			break;
		case 4:
			viewHolder.imgViewTrans.setImageResource(R.drawable.transit_car);
			break;
		case 5:
			viewHolder.imgViewTrans.setImageResource(R.drawable.transit_public);
			break;
		default:
			viewHolder.imgViewTrans.setVisibility(View.GONE);
			viewHolder.trans.setVisibility(View.GONE);
			break;
		}

		viewHolder.userimage2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentHome.clearData = true;
				if (fromSearchCity) {
					FragmentSearchCity.clearData = true;
					FragmentDetailDudesProfile.comeFrom = AppConstants.FRAGMENT_SEARCH_CITY;
				} else {
					FragmentDetailDudesProfile.comeFrom = AppConstants.FRAGMENT_DUDES_PROFILE;
				}
				context.Attach_Fragment(AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE);
				Activity_Home.dudeCommonList = filteredData;
				Activity_Home.index = filteredData.indexOf(userDTO);

				Activity_Home.gblUserID = userDTO.getUserID();

				context.easyTrackerEventLog(
						AppConstants.EVENT_LOG_CATEG_FIND_GUYS,
						AppConstants.EVENT_LOG_ACTION_ITEM, "GuySelected");
				Log.d("", "size=" + Activity_Home.dudeCommonList.size());
			}
		});
		return rowView;
	}

	private boolean isActiveInLastSevenDays(String lastActiveDateString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		/* UTC LAST ACT DATE */
		TimeZone utcZone = TimeZone.getTimeZone("UTC");
		simpleDateFormat.setTimeZone(utcZone);
		Date lastActDate = null;
		try {
			lastActDate = simpleDateFormat.parse(lastActiveDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		simpleDateFormat.setTimeZone(TimeZone.getDefault());
		// String strLastActDate = simpleDateFormat
		// .format(lastActDate); // last act date in string

		Date dateToday = getDateToday(simpleDateFormat);

		long SEVEN_DAYS_TIME_IN_MILLS = 7 * 24 * 60 * 60 * 1000;

		long timeDifference = dateToday.getTime() - lastActDate.getTime();

		if (timeDifference <= SEVEN_DAYS_TIME_IN_MILLS) {
			return true;
		}
		return false;
	}

	public Date getDateToday(SimpleDateFormat simpleDateFormat) {
		Calendar c = Calendar.getInstance();
		Date dateToday = null;
		try {
			dateToday = simpleDateFormat.parse(simpleDateFormat.format(c
					.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateToday;
	}

	private String calculateAge(String berthDateString) {
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

		return "" + number_of_years;

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
}
