package com.xwj.xwjplayer.presenters;

import android.view.View;
import android.widget.SeekBar;

/**
 * Created by xiaweijia on 16/3/16.
 */
public interface MusicListPresenter {
    void onCreate();

    void onDestroyed();

    void onClick(View view);

    void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
}
