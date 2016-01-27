package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import com.krave.kraveapp.R;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EditProfileSpinnerAdapter extends BaseAdapter {
	Context mActivity;
	ArrayList<WhatAreYouDataDTO> marrayList;
	int check;

	public EditProfileSpinnerAdapter(Context Activity,
			List<WhatAreYouDataDTO> arrayList, int check) {
		this.mActivity = Activity;
		this.marrayList = (ArrayList<WhatAreYouDataDTO>) arrayList;
		this.check = check;
	}

	// methed to count
	@Override
	public int getCount() {
		return marrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return marrayList.get(position);// /[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// static class for object
	public static class ViewHolder {
		ImageView mImageView;
		TextView mTextView;

	}

	// getview for single row
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = null;
		ViewHolder viewHolder = new ViewHolder();
		String ethinicityArray[] = mActivity.getResources().getStringArray(
				R.array.ethnicity_sample);
		if (convertView == null) {
			if (check == 0) {
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.row_edit_profile_spinner, null);
			}

			viewHolder.mImageView = (ImageView) rowView
					.findViewById(R.id.imageView1);
			viewHolder.mTextView = (TextView) rowView
					.findViewById(R.id.textView);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Lt);
			viewHolder.mTextView.setTypeface(typeface);

			rowView.setTag(viewHolder);
		} else {
			rowView = convertView;
			viewHolder = (ViewHolder) rowView.getTag();
		}
		if (check == 0) {
			if (marrayList.get(position).isSelected() == true) {
				;
				viewHolder.mImageView.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.av_new_reg_selectdown));
			} else {
				viewHolder.mImageView.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.av_new_reg_selectup));

			}
		}

		try {
			viewHolder.mTextView.setText(ethinicityArray[(Integer
					.valueOf(marrayList.get(position).getId()) - 1)]);
		} catch (Exception e) {
			viewHolder.mTextView.setText(marrayList.get(position).getName());

		}

		return rowView;
	}
}
