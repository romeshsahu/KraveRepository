package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.FragmentDudesProfile;
import com.krave.kraveapp.R;
import com.ps.adapters.WhatAreYouListAdapter.ViewHolder;
import com.ps.models.UserDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.SessionManager;

public class SearchByAdapter extends BaseAdapter {

	Context mActivity;
	ArrayList<UserDTO> marrayList;
	ImageLoaderCircle imageLoaderCircle;
	ImageLoader imageLoader;
	ArrayList<UserDTO> filteredData = null;
	Filter mFilter = new ItemFilter();
	int check;
	private GPSTracker gpsTracker;

	public SearchByAdapter(Context Activity, List<UserDTO> arrayList, int check) {
		this.mActivity = Activity;
		this.marrayList = (ArrayList<UserDTO>) arrayList;
		this.filteredData = (ArrayList<UserDTO>) arrayList;
		imageLoaderCircle = new ImageLoaderCircle(mActivity);
		imageLoader = new ImageLoader(mActivity);
		this.check = check;
		gpsTracker = new GPSTracker(mActivity);
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
		ImageView marker, selected;
		TextView name, email;
		CircleImageView userimage;
		ImageView userimage2;
	}

	// getview for single row
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = null;
		ViewHolder viewHolder = new ViewHolder();
		UserDTO userDTO = (UserDTO) getItem(position);
		if (convertView == null) {
			if (check == 0) { //list
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.row_home2, null);
			} else if (check == 5){ //list edit
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.row_home2_edit, null);
				viewHolder.selected = (ImageView) rowView
						.findViewById(R.id.selectDude);
			} else if (check == 6){ //grid
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.favorites_sublist, null);

			}else if (check == 7){ //grid edit
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.favorites_sublist_edit, null);
				viewHolder.selected = (ImageView) rowView
						.findViewById(R.id.selectDude);

			}else {
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.row_home3, null);
			}
			viewHolder.marker = (ImageView) rowView.findViewById(R.id.marker);
			viewHolder.name = (TextView) rowView.findViewById(R.id.name);
			viewHolder.email = (TextView) rowView.findViewById(R.id.about);
			
			if(check == 6 || check == 7){
				viewHolder.userimage2 = (ImageView) rowView
						.findViewById(R.id.userimage2);
			}else{
				viewHolder.userimage = (CircleImageView) rowView
						.findViewById(R.id.userimage);
			}
			
			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.name.setTypeface(typeface);
			viewHolder.email.setTypeface(typeface);

			rowView.setTag(viewHolder);
		} else {
			rowView = convertView;
			viewHolder = (ViewHolder) rowView.getTag();
		}

		if(check == 6 || check == 7){
			if (userDTO.getUserProfileImageDTOs().size() != 0
					&& userDTO.getUserProfileImageDTOs() != null) {
				if (userDTO.getUserProfileImageDTOs().get(0).getIsImageActive()) {
					imageLoader.DisplayImage(userDTO
							.getUserProfileImageDTOs().get(0).getImagePath(),
							viewHolder.userimage2);
				} else {
					imageLoader.DisplayImage(AppConstants.AWATAR_IAMGE,
							viewHolder.userimage2);
				}
			} else {
				imageLoader.DisplayImage(AppConstants.AWATAR_IAMGE,
						viewHolder.userimage2);
			}
		}else{			
			if (userDTO.getUserProfileImageDTOs().size() != 0
					&& userDTO.getUserProfileImageDTOs() != null) {
				if (userDTO.getUserProfileImageDTOs().get(0).getIsImageActive()) {
					imageLoaderCircle.DisplayImage(userDTO
							.getUserProfileImageDTOs().get(0).getImagePath(),
							viewHolder.userimage);
				} else {
					imageLoaderCircle.DisplayImage(AppConstants.AWATAR_IAMGE,
							viewHolder.userimage);
				}
			} else {
				imageLoaderCircle.DisplayImage(AppConstants.AWATAR_IAMGE,
						viewHolder.userimage);
			}
		}
		if (check == 1) {
			if (userDTO.getIsOnline().equals(AppConstants.ONLINE)) {
				viewHolder.marker.setBackgroundResource(R.drawable.online);
			} else {
				viewHolder.marker.setBackgroundResource(R.drawable.white_crl);
			}
		}
		if(check == 5 || check == 7)
		{
			if (userDTO.isSelected()) {
				viewHolder.selected.setBackgroundResource(R.drawable.av_new_favorites_checkdown);
			} else {
				viewHolder.selected.setBackgroundResource(R.drawable.av_new_favorites_checkup);
			}
		}
		viewHolder.name.setText(filteredData.get(position).getFirstName() + " "
				+ filteredData.get(position).getLastName());
		float distance = (float) 0.0;
		try {
			gpsTracker.getLocation();
//			distance = Float.valueOf(userDTO.getLatLongDTO().getDistance());
			distance = distFrom(gpsTracker.getLatitude(),
					gpsTracker.getLongitude(),
					Double.valueOf(userDTO.getLatLongDTO().getLatitude()),
					Double.valueOf(userDTO.getLatLongDTO().getLongitude()));
		} catch (Exception e) {

		}
		viewHolder.email.setText("" + distance + " miles");
//		viewHolder.email.setText(filteredData.get(position).getAboutMe());
		return rowView;
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
	
	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			// final List<UserDTO> list = arrayList;

			int count = marrayList.size();
			final ArrayList<UserDTO> nlist = new ArrayList<UserDTO>(count);

			String compayerString;
			for (int i = 0; i < count; i++) {
				UserDTO dto = marrayList.get(i);
				compayerString = dto.getFirstName().toString()
						+ dto.getLastName().toString();
				if (compayerString.toLowerCase().contains(filterString)) {
					nlist.add(dto);
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
}
