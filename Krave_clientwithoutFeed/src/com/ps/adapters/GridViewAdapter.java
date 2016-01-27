package com.ps.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.krave.kraveapp.R;
import com.ps.models.InterestsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<InterestsDTO> interestsDTOs;
	// private ArrayList<Integer> mSelectedImages;
	// private ArrayList<Boolean> mSelections;
	// private ArrayList<String> mTexts;
	private ViewHolder mViewHolder;
	// private ImageLoader imageLoader;
	private boolean isFirstTime = true;// , isDoneLoading = false;

	public GridViewAdapter(Activity activity, ArrayList<InterestsDTO> list) {

		this.mActivity = activity;
		this.interestsDTOs = list;

	}

	@Override
	public int getCount() {
		return interestsDTOs.size();
	}

	@Override
	public Object getItem(int position) {
		return interestsDTOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// imageLoader = new ImageLoader(mActivity);
		String interestArrayName[] = mActivity.getResources().getStringArray(
				R.array.interest_array);
		if (convertView == null) {

			convertView = mActivity.getLayoutInflater().inflate(
					R.layout.row_interest_edit_profile_view, null);
			mViewHolder = new ViewHolder();
			mViewHolder.itemImageView = (ImageView) convertView
					.findViewById(R.id.itemImageView);
			mViewHolder.selectedItemImageView = (ImageView) convertView
					.findViewById(R.id.itemImageViewCheck);
			mViewHolder.itemNameTextView = (TextView) convertView
					.findViewById(R.id.itemNameTextView);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Lt);
			mViewHolder.itemNameTextView.setTypeface(typeface);

			convertView.setTag(mViewHolder);
		}

		else {

			mViewHolder = (ViewHolder) convertView.getTag();
		}
		InterestsDTO interestsDTO = interestsDTOs.get(position);

		// mViewHolder.itemImageView
		// .setBackgroundResource(R.drawable.normal_icon);

		/* INTEREST DICTIONARY */
		HashMap<String, Integer> myInterestsUp = new HashMap<String, Integer>();
		myInterestsUp.put("MUSIC", R.drawable.av_interestbox_music_up);
		myInterestsUp.put("DANCE", R.drawable.av_interestbox_dance_up);
		myInterestsUp.put("MUSCLE", R.drawable.av_interestbox_muscle_up);
		myInterestsUp.put("TWINK", R.drawable.av_interestbox_twink_up);
		myInterestsUp.put("LEATHER", R.drawable.av_interestbox_leather_up);
		myInterestsUp.put("MILITARY", R.drawable.av_interestbox_military_up);
		myInterestsUp.put("COLLEGE", R.drawable.av_interestbox_college_up);
		myInterestsUp.put("POZ", R.drawable.av_interestbox_poz_up);
		myInterestsUp.put("TRANSGENDER",
				R.drawable.av_interestbox_transgender_up);
		myInterestsUp.put("COWBOY", R.drawable.av_interestbox_cowboy_up);

		HashMap<String, Integer> myInterestsDown = new HashMap<String, Integer>();
		myInterestsDown.put("MUSIC", R.drawable.av_interestbox_music_down);
		myInterestsDown.put("DANCE", R.drawable.av_interestbox_dance_down);
		myInterestsDown.put("MUSCLE", R.drawable.av_interestbox_muscle_down);
		myInterestsDown.put("TWINK", R.drawable.av_interestbox_twink_down);
		myInterestsDown.put("LEATHER", R.drawable.av_interestbox_leather_down);
		myInterestsDown
				.put("MILITARY", R.drawable.av_interestbox_military_down);
		myInterestsDown.put("COLLEGE", R.drawable.av_interestbox_college_down);
		myInterestsDown.put("POZ", R.drawable.av_interestbox_poz_down);
		myInterestsDown.put("TRANSGENDER",
				R.drawable.av_interestbox_transgender_down);
		myInterestsDown.put("COWBOY", R.drawable.av_interestbox_cowboy_down);

		if (isFirstTime) {
			mViewHolder.itemImageView.setImageDrawable(mActivity.getResources()
					.getDrawable(
							myInterestsUp.get(interestsDTO.getInterestName())));
			try {
				mViewHolder.itemNameTextView.setText(interestArrayName[(Integer
						.valueOf(interestsDTO.getInterestId()) - 1)]);
			} catch (Exception e) {
				mViewHolder.itemNameTextView.setText(interestsDTO
						.getInterestName());
			}

			if (position == interestsDTOs.size() - 1) {
				isFirstTime = false;
			}

		}
		Log.d("", "selected interest" + interestsDTO.getIsSelected());
		if (interestsDTO.getIsSelected()) {

			mViewHolder.itemImageView
					.setImageDrawable(mActivity.getResources()
							.getDrawable(
									myInterestsDown.get(interestsDTO
											.getInterestName())));
			mViewHolder.itemNameTextView.setTextColor(mActivity.getResources()
					.getColor(R.color.title_bar));

		} else {
			mViewHolder.itemImageView.setImageDrawable(mActivity.getResources()
					.getDrawable(
							myInterestsUp.get(interestsDTO.getInterestName())));
			mViewHolder.itemNameTextView.setTextColor(mActivity.getResources()
					.getColor(R.color.black));
		}

		return convertView;
	}

	private static class ViewHolder {

		public ImageView itemImageView, selectedItemImageView;
		public TextView itemNameTextView;
	}
}
