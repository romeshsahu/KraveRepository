package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krave.kraveapp.R;
import com.ps.models.UserDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;

public class UpdateUserListAdapter extends BaseAdapter {

	Context mActivity;
	ArrayList<UserDTO> marrayList;
	ImageLoaderCircle imageLoaderCircle;
	ImageLoader imageLoader;
	private int check;
	private List<UserDTO> filteredData = null;
	private GPSTracker gpsTracker;
	private ItemFilter mFilter = new ItemFilter();

	public UpdateUserListAdapter(Context Activity, List<UserDTO> arrayList, int check) {
		this.mActivity = Activity;
		this.marrayList = (ArrayList<UserDTO>) arrayList;
		this.filteredData = arrayList;
		this.check = check;
		imageLoaderCircle = new ImageLoaderCircle(mActivity);
		imageLoader = new ImageLoader(mActivity);
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
			
			if(check == -1){
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.row_update_user_list, null);
				viewHolder.userimage = (CircleImageView) rowView
						.findViewById(R.id.userimage);
			}else if(check == 0){
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.row_favorites_edit, null);
				viewHolder.userimage = (CircleImageView) rowView
						.findViewById(R.id.userimage);
			}else if(check == 2){
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.favorites_grid_edit, null);
				viewHolder.userimage2 = (ImageView) rowView
						.findViewById(R.id.userimage2);
			}
			viewHolder.marker = (ImageView) rowView.findViewById(R.id.marker);
			viewHolder.name = (TextView) rowView.findViewById(R.id.name);
			viewHolder.email = (TextView) rowView.findViewById(R.id.email);

			viewHolder.selected = (ImageView) rowView
					.findViewById(R.id.selectDude);
			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.name.setTypeface(typeface);
			viewHolder.email.setTypeface(typeface);
			rowView.setTag(viewHolder);
		} else {
			rowView = convertView;
			viewHolder = (ViewHolder) rowView.getTag();
		}

		viewHolder.name.setText("" + userDTO.getFirstName() + " "
				+ userDTO.getLastName());
		
//		viewHolder.email.setText("" + userDTO.getAboutMe());
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
		
		if(check == 0){
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
		}else if(check == 2)
		{
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
		}
		else if (check == -1){
			viewHolder.email.setVisibility(View.GONE);
			if (userDTO.getProfileImage() != null){
				imageLoaderCircle.DisplayImage(userDTO.getProfileImage(),
						viewHolder.userimage);
			} else {
				
				imageLoaderCircle.DisplayImage(AppConstants.AWATAR_IAMGE,
						viewHolder.userimage);
			} 

		}
		
		if (userDTO.isSelected()) {
			viewHolder.selected.setBackgroundResource(R.drawable.av_new_favorites_checkdown);
		} else {
			viewHolder.selected.setBackgroundResource(R.drawable.av_new_favorites_checkup);
		}
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
