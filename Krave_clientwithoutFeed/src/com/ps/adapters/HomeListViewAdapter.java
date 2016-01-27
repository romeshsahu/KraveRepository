package com.ps.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.askerov.dynamicgrid.DynamicGridView;

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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.AppManager;
import com.krave.kraveapp.FragmentDetailDudesProfile;
import com.krave.kraveapp.FragmentHome;
import com.krave.kraveapp.R;
import com.ps.models.UserDTO;
import com.ps.services.ChatService;
import com.ps.services.GPSTracker;
import com.ps.utill.AdMob;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.DistanceConverter;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.SessionManager;

public class HomeListViewAdapter extends BaseAdapter {

	Context mActivity;
	Activity_Home context;
	ArrayList<UserDTO> marrayList;
	ImageLoader imageLoader;
	ArrayList<UserDTO> filteredData = null;

	// Filter mFilter = new ItemFilter();
	private GPSTracker gpsTracker;
	DistanceConverter distCon;
	int transPref;
	public boolean isOnlineOnly;
	boolean fromSearchCity;
	int numberOfColumnInGridView = 2;
	int numberOfrowInGridView = 9;

	AppManager singleton;

	public HomeListViewAdapter(Context Activity, List<UserDTO> arrayList,
			boolean fromSearchCity) {
		context = (Activity_Home) Activity;
		this.mActivity = Activity;
		this.distCon = new DistanceConverter();
		this.marrayList = (ArrayList<UserDTO>) arrayList;
		this.filteredData = (ArrayList<UserDTO>) arrayList;

		imageLoader = new ImageLoader(mActivity);
		gpsTracker = new GPSTracker(mActivity);
		isOnlineOnly = false;
		this.fromSearchCity = fromSearchCity;

		AppManager.initInstance();
		singleton = AppManager.getInstance();
		transPref = singleton.mTransitPrefs.getInt(
				AppConstants.TRANSIT_PREFERENCE, 1);
		setGridViewColumn();
	}

	private void setGridViewColumn() {

		try {
			int selectedFindDudesColumnId = Integer.valueOf(new SessionManager(
					context).getSettingDetail().getFindDudesColumnCoun());
			if (selectedFindDudesColumnId == 0) {
				numberOfColumnInGridView = 2;
			} else {
				numberOfColumnInGridView = 3;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			numberOfColumnInGridView = 2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			numberOfColumnInGridView = 2;
		}
	}

	// methed to count
	@Override
	public int getCount() {
		int count = filteredData.size()
				/ (numberOfColumnInGridView * numberOfrowInGridView);
		int reminder = filteredData.size()
				% (numberOfColumnInGridView * numberOfrowInGridView);
		if (reminder > 0) {
			count++;
		}

		return count;// isOnlineOnly?onlineData.size():filteredData.size();
	}

	@Override
	public List<UserDTO> getItem(int position) {
		int totalItemInGridView = numberOfColumnInGridView
				* numberOfrowInGridView;
		int startIndex = 0, endIndex;
		if (filteredData.size() > (position * totalItemInGridView)) {
			startIndex = position * totalItemInGridView;
		}
		if (filteredData.size() > (startIndex + totalItemInGridView)) {
			endIndex = startIndex + totalItemInGridView;
		} else {
			endIndex = filteredData.size();
		}

		return new ArrayList<UserDTO>(
				filteredData.subList(startIndex, endIndex));// isOnlineOnly?onlineData.get(position):filteredData.get(position);//
		// /[position];
	}

	private int getStartIndex(int position) {
		int totalItemInGridView = numberOfColumnInGridView
				* numberOfrowInGridView;
		int startIndex = 0;
		if (filteredData.size() > (position * totalItemInGridView)) {
			startIndex = position * totalItemInGridView;
		}
		return startIndex;

	}

	private int getEndIndex(int startIndex) {

		int totalItemInGridView = numberOfColumnInGridView
				* numberOfrowInGridView;
		int endIndex;

		if (filteredData.size() > (startIndex + totalItemInGridView)) {
			endIndex = startIndex + totalItemInGridView;
		} else {
			endIndex = filteredData.size();
		}

		return endIndex;

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// static class for object
	public static class ViewHolder {
		private FrameLayout adViewFrameLayout;;
		private DynamicGridView gridView;
		private AdMob adMob;
		private FindDudesGridAdapter findDudesAdapter;
	}

	// getview for single row
	@Override
	public View getView(final int position, View rowView, ViewGroup parent) {

		ViewHolder viewHolder = new ViewHolder();
		int startIndex, endIndex;
		if (rowView == null) {
			viewHolder = new ViewHolder();
			rowView = ((Activity) mActivity).getLayoutInflater().inflate(
					R.layout.row_home_listview, null);
			viewHolder.gridView = (DynamicGridView) rowView
					.findViewById(R.id.dynamicGridView);
			viewHolder.adViewFrameLayout = (FrameLayout) rowView
					.findViewById(R.id.ad);
			viewHolder.gridView.setNumColumns(numberOfColumnInGridView);
			viewHolder.gridView.setExpanded(true);
			subcriptionActive(viewHolder);
			startIndex = getStartIndex(position);
			endIndex = getEndIndex(startIndex);
			viewHolder.findDudesAdapter = new FindDudesGridAdapter(mActivity,
					filteredData, false);
			viewHolder.findDudesAdapter.setListIndex(startIndex, endIndex);
			viewHolder.gridView.setAdapter(viewHolder.findDudesAdapter);
			rowView.setTag(viewHolder);
		} else {

			viewHolder = (ViewHolder) rowView.getTag();
		}

		startIndex = getStartIndex(position);
		endIndex = getEndIndex(startIndex);

		viewHolder.findDudesAdapter.setListIndex(startIndex, endIndex);
		viewHolder.findDudesAdapter.notifyDataSetChanged();

		// viewHolder.userimage2.setScaleType(ScaleType.FIT_CENTER);
		// imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
		// .getImagePath(), viewHolder.userimage2);

		return rowView;
	}

	protected void subcriptionActive(ViewHolder holder) {

		if (singleton.adsizeAndLocation.equals(AppConstants.ADS_CENTER_BANNER))
			if (!singleton.isPaidUser) {
				holder.adViewFrameLayout.setVisibility(View.VISIBLE);
				holder.adMob = new AdMob(context, singleton);
				holder.adMob.startAdvertising(holder.adViewFrameLayout);
				Log.e("", "home adapter= not paid");
			} else {
				Log.e("", "home adapter= paid");
				holder.adViewFrameLayout.setVisibility(View.GONE);
				if (holder.adMob != null) {
					holder.adMob.adViewDestroy();
				}

			}

	}

}
