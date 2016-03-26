package com.xwj.xwjplayer.presenters;

import android.view.View;

/**
 * Created by xiaweijia on 16/3/16.
 */
public interface MusicListPresenter {
    void onCreate();

    void onDestroyed();

    void onClick(View view);
}
