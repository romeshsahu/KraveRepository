package com.ps.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.krave.kraveapp.R;
import com.ps.models.InterestsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingListInterestAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<InterestsDTO> interestsDTOs;
	// private ArrayList<Integer> mSelectedImages;
	// private ArrayList<Boolean> mSelections;
	// private ArrayList<String> mTexts;
	private ViewHolder mViewHolder;
	private ImageLoader imageLoader;
	private ImageLoaderCircle imageLoaderCircle;
	private boolean isFirstTime = true;
	private int check;

	public SettingListInterestAdapter(Activity activity,
			ArrayList<InterestsDTO> list, int check) {

		this.mActivity = activity;
		this.interestsDTOs = list;
		this.check = check;
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
		imageLoader = new ImageLoader(mActivity);
		imageLoaderCircle = new ImageLoaderCircle(mActivity);
		String interestArrayName[] = mActivity.getResources().getStringArray(
				R.array.interest_array);
		if (convertView == null) {

			if (check == AppConstants.ADAPTER_FLAG_SETTINGS) {
				convertView = mActivity.getLayoutInflater().inflate(
						R.layout.row_interest_setting_view, null);
				mViewHolder = new ViewHolder();
				mViewHolder.itemImageView = (CircleImageView) convertView
						.findViewById(R.id.itemImageView);
			} else if (check == AppConstants.ADAPTER_FLAG_EDIT_PROFILE) {
				convertView = mActivity.getLayoutInflater().inflate(
						R.layout.row_interest_edit_profile_view, null);
				mViewHolder = new ViewHolder();
				mViewHolder.itemImageView_box = (ImageView) convertView
						.findViewById(R.id.itemImageView);
			}
			mViewHolder.selectedItemImageView = (ImageView) convertView
					.findViewById(R.id.itemImageViewCheck);
			mViewHolder.itemNameTextView = (TextView) convertView
					.findViewById(R.id.itemNameTextView);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Md);
			mViewHolder.itemNameTextView.setTypeface(typeface);

			convertView.setTag(mViewHolder);
		}

		else {

			mViewHolder = (ViewHolder) convertView.getTag();
		}

		InterestsDTO interestsDTO = interestsDTOs.get(position);
		/* INTEREST DICTIONARY */
		HashMap<String, Integer> myInterestsUp = new HashMap<String, Integer>();
		HashMap<String, Integer> myInterestsDown = new HashMap<String, Integer>();
		if (check == AppConstants.ADAPTER_FLAG_SETTINGS) {
			myInterestsUp.put("MUSIC", R.drawable.av_new_interest_music_up);
			myInterestsUp.put("DANCE", R.drawable.av_new_interest_dance_up);
			myInterestsUp.put("MUSCLE", R.drawable.av_new_interest_muscle_up);
			myInterestsUp.put("TWINK", R.drawable.av_new_interest_twink_up);
			myInterestsUp.put("LEATHER", R.drawable.av_new_interest_leather_up);
			myInterestsUp.put("MILITARY",
					R.drawable.av_new_interest_military_up);
			myInterestsUp.put("COLLEGE", R.drawable.av_new_interest_college_up);
			myInterestsUp.put("POZ", R.drawable.av_new_interest_poz_up);
			myInterestsUp.put("TRANSGENDER",
					R.drawable.av_new_interest_transgender_up);
			myInterestsUp.put("COWBOY", R.drawable.av_new_interest_cowboy_up);

			myInterestsDown.put("MUSIC", R.drawable.av_new_interest_music_down);
			myInterestsDown.put("DANCE", R.drawable.av_new_interest_dance_down);
			myInterestsDown.put("MUSCLE",
					R.drawable.av_new_interest_muscle_down);
			myInterestsDown.put("TWINK", R.drawable.av_new_interest_twink_down);
			myInterestsDown.put("LEATHER",
					R.drawable.av_new_interest_leather_down);
			myInterestsDown.put("MILITARY",
					R.drawable.av_new_interest_military_down);
			myInterestsDown.put("COLLEGE",
					R.drawable.av_new_interest_college_down);
			myInterestsDown.put("POZ", R.drawable.av_new_interest_poz_down);
			myInterestsDown.put("TRANSGENDER",
					R.drawable.av_new_interest_transgender_down);
			myInterestsDown.put("COWBOY",
					R.drawable.av_new_interest_cowboy_down);
		} else if (check == AppConstants.ADAPTER_FLAG_EDIT_PROFILE) {
			myInterestsUp.put("MUSIC", R.drawable.av_interestbox_music_up);
			myInterestsUp.put("DANCE", R.drawable.av_interestbox_dance_up);
			myInterestsUp.put("MUSCLE", R.drawable.av_interestbox_muscle_up);
			myInterestsUp.put("TWINK", R.drawable.av_interestbox_twink_up);
			myInterestsUp.put("LEATHER", R.drawable.av_interestbox_leather_up);
			myInterestsUp
					.put("MILITARY", R.drawable.av_interestbox_military_up);
			myInterestsUp.put("COLLEGE", R.drawable.av_interestbox_college_up);
			myInterestsUp.put("POZ", R.drawable.av_interestbox_poz_up);
			myInterestsUp.put("TRANSGENDER",
					R.drawable.av_interestbox_transgender_up);
			myInterestsUp.put("COWBOY", R.drawable.av_interestbox_cowboy_up);

			myInterestsDown.put("MUSIC", R.drawable.av_interestbox_music_down);
			myInterestsDown.put("DANCE", R.drawable.av_interestbox_dance_down);
			myInterestsDown
					.put("MUSCLE", R.drawable.av_interestbox_muscle_down);
			myInterestsDown.put("TWINK", R.drawable.av_interestbox_twink_down);
			myInterestsDown.put("LEATHER",
					R.drawable.av_interestbox_leather_down);
			myInterestsDown.put("MILITARY",
					R.drawable.av_interestbox_military_down);
			myInterestsDown.put("COLLEGE",
					R.drawable.av_interestbox_college_down);
			myInterestsDown.put("POZ", R.drawable.av_interestbox_poz_down);
			myInterestsDown.put("TRANSGENDER",
					R.drawable.av_interestbox_transgender_down);
			myInterestsDown
					.put("COWBOY", R.drawable.av_interestbox_cowboy_down);
		}
		try {
			mViewHolder.itemNameTextView.setText(interestArrayName[(Integer
					.valueOf(interestsDTO.getInterestId()) - 1)]);
		} catch (Exception e) {
			mViewHolder.itemNameTextView
					.setText(interestsDTO.getInterestName());
		}

		Log.d("", "selected interest" + interestsDTO.getIsSelected());

		if (interestsDTO.getIsSelected()) {
			if (check == AppConstants.ADAPTER_FLAG_SETTINGS) {
				mViewHolder.itemImageView.setImageDrawable(mActivity
						.getResources().getDrawable(
								myInterestsDown.get(interestsDTO
										.getInterestName())));
			} else if (check == AppConstants.ADAPTER_FLAG_EDIT_PROFILE) {
				mViewHolder.itemImageView_box.setImageDrawable(mActivity
						.getResources().getDrawable(
								myInterestsDown.get(interestsDTO
										.getInterestName())));
			}
			if (check == AppConstants.ADAPTER_FLAG_SETTINGS)
				mViewHolder.itemNameTextView.setTextColor(mActivity
						.getResources().getColor(R.color.title_bar));
			else if (check == AppConstants.ADAPTER_FLAG_EDIT_PROFILE)
				mViewHolder.itemNameTextView.setTextColor(Color
						.parseColor("#404041"));
			Log.d("", "selected interest" + position);
		} else {
			if (check == AppConstants.ADAPTER_FLAG_SETTINGS) {
				mViewHolder.itemImageView.setImageDrawable(mActivity
						.getResources().getDrawable(
								myInterestsUp.get(interestsDTO
										.getInterestName())));
			} else if (check == AppConstants.ADAPTER_FLAG_EDIT_PROFILE) {
				mViewHolder.itemImageView_box.setImageDrawable(mActivity
						.getResources().getDrawable(
								myInterestsUp.get(interestsDTO
										.getInterestName())));
			}

			if (check == AppConstants.ADAPTER_FLAG_SETTINGS)
				mViewHolder.itemNameTextView.setTextColor(Color
						.parseColor("#404041"));
			else if (check == AppConstants.ADAPTER_FLAG_EDIT_PROFILE)
				mViewHolder.itemNameTextView.setTextColor(Color
						.parseColor("#e2e2e2"));
		}

		return convertView;
	}

	private static class ViewHolder {

		public ImageView selectedItemImageView, itemImageView_box;
		public CircleImageView itemImageView;
		public TextView itemNameTextView;
	}

	public Bitmap gray(Bitmap bmp) {
		Bitmap operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
				bmp.getConfig());

		for (int i = 0; i < bmp.getWidth(); i++) {
			for (int j = 0; j < bmp.getHeight(); j++) {
				int p = bmp.getPixel(i, j);
				int r = Color.red(p);
				int g = Color.green(p);
				int b = Color.blue(p);

				r = (int) 255;
				g = (int) 255;
				b = (int) 255;

				operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
			}
		}
		return operation;
	}
}
