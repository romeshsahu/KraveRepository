package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.utill.ImageLoaderForProfile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PhotosDynamicGridAdapter extends BaseDynamicGridAdapter {

	Context mContext;
	ArrayList<UserProfileImageDTO> mData = new ArrayList<UserProfileImageDTO>();
	public static ArrayList<String> mDataDelete = new ArrayList<String>();
	public static ArrayList<UserProfileImageDTO> mDataAdd = new ArrayList<UserProfileImageDTO>();
	int mColumnCount;
	ImageLoaderForProfile imageLoader;

	public PhotosDynamicGridAdapter(Context context,
			ArrayList<UserProfileImageDTO> data, int columnCount) {
		super(context, data, columnCount);

		this.mContext = context;
		this.mData = data;
		this.mColumnCount = columnCount;

		imageLoader = new ImageLoaderForProfile(context);
		Collections.sort(data, new Comparator<UserProfileImageDTO>() {

			@Override
			public int compare(UserProfileImageDTO lhs, UserProfileImageDTO rhs) {
				// TODO Auto-generated method stub
				return lhs.getImagePosition().compareTo(rhs.getImagePosition());
			}
		});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.row_grid_photo, null);

			holder = new PhotoViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (PhotoViewHolder) convertView.getTag();
		}
		holder.build(getItem(position).toString());

		if (position != 0) {
			// ELv
			try {
				UserProfileImageDTO item = mData.get(position);
				imageLoader.DisplayImage(item.getImagePath(),
						holder.pictureImageView);

				if (!item.getIsImageActive() && item.isPublic()) {
					holder.stampImageView.setVisibility(View.VISIBLE);
				} else {
					holder.stampImageView.setVisibility(View.GONE);
				}
				if (item.isPublic()) {
					holder.lockImage.setVisibility(View.GONE);
				} else {
					holder.lockImage.setVisibility(View.VISIBLE);
				}

				if (item.getImagePosition().equals("0")) {

				}
			} catch (Exception e) {
				// why is there a try catch here?
				e.printStackTrace();
			}
		}

		return convertView;
	}

	public void addPhoto(String imagePath, boolean isPublic) {
		UserProfileImageDTO user = new UserProfileImageDTO();
		user.setImagePath(imagePath);
		user.setPublic(isPublic);
		mDataAdd.add(user);
	}

	public ArrayList<String> getDeletePhotoList() {
		return mDataDelete;
	}

	public ArrayList<UserProfileImageDTO> getPhotoList() {
		return mData;
	}

	public ArrayList<UserProfileImageDTO> getAddPhotoList() {
		return mDataAdd;
	}

	public void deletePhoto(String imageId) {
		mDataDelete.add(imageId);
	}

	public void clearArrays() {
		mDataAdd.clear();
		mDataDelete.clear();
	}

	private class PhotoViewHolder {
		private ImageView pictureImageView;
		private ImageView stampImageView;
		private ImageView lockImage;

		private PhotoViewHolder(View view) {
			pictureImageView = (ImageView) view
					.findViewById(R.id.row_grid_photo);
			stampImageView = (ImageView) view.findViewById(R.id.row_grid_stamp);
			lockImage = (ImageView) view.findViewById(R.id.lock_image);
		}

		void build(String title) {
			pictureImageView
					.setImageResource(R.drawable.edit_profile_add_photo);
			stampImageView.setImageResource(R.drawable.stamp);
		}
	}

}
