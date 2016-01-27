package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;







import com.krave.kraveapp.R;
import com.ps.models.BodyTypeDTO;
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

public class BodyTypeSpinnerAdapter extends BaseAdapter {
	Context mActivity;
	ArrayList<BodyTypeDTO> marrayList;
	int check;

	public BodyTypeSpinnerAdapter(Context Activity,
			List<BodyTypeDTO> arrayList, int check) {
		this.mActivity = Activity;
		this.marrayList = (ArrayList<BodyTypeDTO>) arrayList;
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
			if (marrayList.get(position).getIsSelected() == true) {
				;
				viewHolder.mImageView.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.av_new_reg_selectdown));
			} else {
				viewHolder.mImageView.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.av_new_reg_selectup));

			}
		}


		viewHolder.mTextView.setText(marrayList.get(position).getBodyTypeName());
		return rowView;
	}
}
