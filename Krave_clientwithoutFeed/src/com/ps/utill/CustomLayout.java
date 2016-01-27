package com.ps.utill;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class CustomLayout extends RelativeLayout{

	
	
	public CustomLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
	
	
}
