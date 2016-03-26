package com.hw.common.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 带阻力效果ScrollView
 * @author liweicai
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class BounceScrollView extends ScrollView {
private static final int MAX_Y_OVERSCROLL_DISTANCE = 80; 
    private Context mContext; 
    private int mMaxYOverscrollDistance; 
    private GestureDetector mGestureDetector;
 
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//    	getParent().requestDisallowInterceptTouchEvent(true);
    	return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }
 
    class YScrollDetector extends SimpleOnGestureListener {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            /**
             * if we're scrolling more closer to x direction, return false, let subview to process it
             */
            return (Math.abs(distanceY) > Math.abs(distanceX));
        }
    }
      
    public BounceScrollView(Context context){ 
        super(context); 
        mContext = context; 
        initBounceListView(); 
    } 
      
    public BounceScrollView(Context context, AttributeSet attrs){ 
        super(context, attrs); 
        mContext = context; 
        initBounceListView();
    } 
      
    public BounceScrollView(Context context, AttributeSet attrs, int defStyle){ 
        super(context, attrs, defStyle); 
        mContext = context; 
        initBounceListView(); 
    } 
      
    private void initBounceListView(){ 
    	mGestureDetector = new GestureDetector(mContext, new YScrollDetector());
    	setFadingEdgeLength(0);
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics(); 
        final float density = metrics.density; 
          
        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE); 
    } 
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){  
        //这块是关键性代码
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);   
    }
}