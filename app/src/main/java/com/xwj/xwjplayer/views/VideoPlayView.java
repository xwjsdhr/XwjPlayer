package com.xwj.xwjplayer.views;

import android.content.Intent;

/**
 * Created by xiaweijia on 16/3/16.
 */
public interface VideoPlayView {

    void setVideoPath(String videoPath);

    Intent getIntentObj();

    void start();

    void pause();

    boolean isPlaying();

    void startSeeking(int duration);

    void stopSeeking();

    int getProgress();

    void hideTopBar();

    void showTopBar();

    void hideBottomBar();

    void showBottomBar();

    boolean isBarShown();

    void setDuration(int duration);

    int getDuration();

    void seekTo(int progress);
}
