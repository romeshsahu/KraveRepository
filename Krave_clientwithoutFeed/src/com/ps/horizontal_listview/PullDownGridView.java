package com.ps.horizontal_listview;

import com.krave.kraveapp.FragmentHome;
import com.ps.utill.WebServiceConstants;

import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

/**
 * @author Pushpan
 * @date Nov 27, 2012
 **/
public class PullDownGridView extends GridView implements OnScrollListener {

    private GridViewTouchEventListener mTouchListener;
    private boolean pulledDown;
    private TranslateAnimation upToDown;


    public PullDownGridView(Context context) {
        super(context);
        init();
    }

    public PullDownGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public PullDownGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnScrollListener(this);
    }

    private float lastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
        		lastY = ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float newY = ev.getRawY();
            System.out.println("newY " +newY+" . lastY "+lastY);
            setPulledDown((newY - lastY) > 400);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isPulledDown()) {
                        if (mTouchListener != null) {
                            mTouchListener.onGridViewPulledDown();
                            setPulledDown(false);
                        }
                    }
                }
            }, 400);
//            lastY = newY;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
        	if(isPulledDown()){
	        	upToDown = new TranslateAnimation(0, 0, 0, 150);
	        	upToDown.setDuration(300);
	        	upToDown.setFillAfter(true);
	            this.startAnimation(upToDown);
	            
//	            FragmentHome.singleton.progressLoading(FragmentHome.ivPullToRefresh, FragmentHome.llPullToRefresh);
        	}

        	System.out.println(isPulledDown() ? "PULLED DOWN" : "NOT");
            lastY = 0;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        setPulledDown(false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public interface GridViewTouchEventListener {
        public void onGridViewPulledDown();
    }

    public void setGridViewTouchListener(
    		GridViewTouchEventListener touchListener) {
        this.mTouchListener = touchListener;
    }

    public GridViewTouchEventListener getGridViewTouchListener() {
        return mTouchListener;
    }

    public boolean isPulledDown() {
        return pulledDown;
    }

    public void setPulledDown(boolean pulledDown) {
        this.pulledDown = pulledDown;
    }
}