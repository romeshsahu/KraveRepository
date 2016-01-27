package com.ps.utill;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class ParentLinearLayout extends LinearLayout {

	public ParentLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}
}
