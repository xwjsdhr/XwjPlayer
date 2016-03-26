package com.xwj.xwjplayer.presenters;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiaweijia on 16/3/22.
 */
public interface VideoPlayPresenter {
    void onCreate();

    void onClick(View view);

    boolean onTouchEvent(MotionEvent motionEvent);
}
