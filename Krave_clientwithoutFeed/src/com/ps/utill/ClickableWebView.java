package com.ps.utill;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class ClickableWebView extends WebView {

	private static final int MAX_CLICK_DURATION = 200;
	private long startClickTime;

	public ClickableWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ClickableWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClickableWebView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			startClickTime = Calendar.getInstance().getTimeInMillis();
			break;
		}
		case MotionEvent.ACTION_UP: {
			long clickDuration = Calendar.getInstance().getTimeInMillis()
					- startClickTime;
			if (clickDuration < MAX_CLICK_DURATION) {
				super.performClick();

			}
		}
		}
		return true;
	}

}
