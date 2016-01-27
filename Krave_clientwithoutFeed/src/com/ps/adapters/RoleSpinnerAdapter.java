package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import com.krave.kraveapp.R;
import com.ps.models.RoleDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoleSpinnerAdapter extends BaseAdapter {
	Context mActivity;
	ArrayList<RoleDTO> marrayList;
	int check;

	public RoleSpinnerAdapter(Context Activity, List<RoleDTO> arrayList,
			int check) {
		this.mActivity = Activity;
		this.marrayList = (ArrayList<RoleDTO>) arrayList;
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
		String roleArray[] = mActivity.getResources().getStringArray(
				R.array.role_sample);
		if (convertView == null) {

			rowView = ((Activity) mActivity).getLayoutInflater().inflate(
					R.layout.row_edit_profile_spinner, null);

			viewHolder.mImageView = (ImageView) rowView
					.findViewById(R.id.imageView1);

			viewHolder.mTextView = (TextView) rowView
					.findViewById(R.id.textView);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Lt);
			viewHolder.mTextView.setTypeface(typeface);
			if (check == 0) {
				viewHolder.mImageView.setVisibility(View.GONE);
				viewHolder.mTextView.setTextColor(Color.parseColor("#000000"));

			} else if (check == AppConstants.ADAPTER_FLAG_SETTINGS) {
				viewHolder.mImageView.setVisibility(View.VISIBLE);
				viewHolder.mTextView.setTextColor(Color.parseColor("#ffffff"));
			}
			rowView.setTag(viewHolder);
		} else {
			rowView = convertView;
			viewHolder = (ViewHolder) rowView.getTag();
		}
		if (check == AppConstants.ADAPTER_FLAG_SETTINGS) {
			if (marrayList.get(position).getIsSelected() == true) {
				;
				viewHolder.mImageView.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.select_notification));
			} else {
				viewHolder.mImageView.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.unselect_notofication));

			}
		}
		try {
			viewHolder.mTextView.setText(roleArray[(Integer.valueOf(marrayList
					.get(position).getRoleId()) - 1)]);
		} catch (Exception e) {
			viewHolder.mTextView
					.setText(marrayList.get(position).getRoleName());
		}

		return rowView;
	}
}
