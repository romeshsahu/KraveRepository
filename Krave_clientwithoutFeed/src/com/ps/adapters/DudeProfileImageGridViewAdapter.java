package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import com.krave.kraveapp.AppManager;
import com.krave.kraveapp.R;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.utill.ImageLoader;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DudeProfileImageGridViewAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<UserProfileImageDTO> userProfileImageDTOs;

	private ViewHolder mViewHolder;

	private ImageLoader imageLoader;
	private boolean isLoginUser = false;
	private AppManager singleton;
	

	public DudeProfileImageGridViewAdapter(Activity activity, UserDTO userDTO) {
		// super(activity, list, 3);
		this.mActivity = activity;
		this.userProfileImageDTOs = (ArrayList<UserProfileImageDTO>) userDTO
				.getUserProfileImageDTOs();
		this.imageLoader = new ImageLoader(mActivity);
		this.isLoginUser = userDTO.isLoginUser();
		AppManager.initInstance();
		singleton=AppManager.getInstance();
		Log.d("", "userProfileImageDTOs="+userProfileImageDTOs.size());
		Log.d("", "userDTO.getUserProfileImageDTOs()="+userDTO
				.getUserProfileImageDTOs().size());
		//publicImageCounter=0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// imageLoader = new ImageLoader(mActivity);
		if (convertView == null) {

			convertView = mActivity.getLayoutInflater().inflate(
					R.layout.row_dude_profile_image_grid_view, null);
			mViewHolder = new ViewHolder();
			mViewHolder.itemImageView = (ImageView) convertView
					.findViewById(R.id.userimage2);
			mViewHolder.lockImageWhite = (ImageView) convertView
					.findViewById(R.id.lock_image);
			// mViewHolder.selectedItemImageView = (ImageView) convertView
			// .findViewById(R.id.itemImageViewCheck);
			// mViewHolder.itemNameTextView = (TextView) convertView
			// .findViewById(R.id.itemNameTextView);

			// Typeface typeface = FontStyle.getFont(mActivity,
			// AppConstants.HelveticaNeueLTStd_Lt);
			// mViewHolder.itemNameTextView.setTypeface(typeface);

			convertView.setTag(mViewHolder);
		}

		else {

			mViewHolder = (ViewHolder) convertView.getTag();
		}
		UserProfileImageDTO imageDTO = userProfileImageDTOs.get(position);
		mViewHolder.lockImageWhite.setVisibility(View.GONE);
	//	Log.d("", "publicImageCounter="+publicImageCounter);
		if (imageDTO.isPublic()) { 
			if (imageDTO.getIsImageActive()) {
//				if (singleton.isPaidUser || imageDTO.isPaidImage() || isLoginUser) // condition
//																	// for
//																	// perimium
//																	// user
//				{
					imageLoader.DisplayImage(imageDTO.getImagePath(),
							mViewHolder.itemImageView);
				//} else {
					mViewHolder.itemImageView.setImageResource(R.drawable.lock_white_bg);
//				}if(publicImageCounter<=getCount()){
//					++publicImageCounter;
//				}
				
			} else {
				mViewHolder.itemImageView.setImageResource(R.drawable.stamp1);
			}
			
		} else {
			if (isLoginUser) {
				imageLoader.DisplayImage(imageDTO.getImagePath(),
						mViewHolder.itemImageView);
				mViewHolder.lockImageWhite.setVisibility(View.VISIBLE);
			} else {
				mViewHolder.itemImageView
						.setImageResource(R.drawable.private_photo_alert);
			}
		}

		return convertView;
	}

	

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		
		super.notifyDataSetChanged();
		
		
	}
	private static class ViewHolder {

		public ImageView itemImageView, selectedItemImageView, lockImageWhite;
		public TextView itemNameTextView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userProfileImageDTOs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userProfileImageDTOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
