package com.xwj.xwjplayer.presenters;

import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by xiaweijia on 16/3/22.
 */
public interface VideoPlayPresenter {
    void onCreate();

    void onClick(View view);

    void onDestroy();

    void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
}
