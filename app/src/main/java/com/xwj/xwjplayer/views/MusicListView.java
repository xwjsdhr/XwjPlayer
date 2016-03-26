package com.xwj.xwjplayer.views;

import com.xwj.xwjplayer.entitys.MusicItem;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/16.
 */
public interface MusicListView extends BaseView<List<MusicItem>> {

    void showProgress();

    void hideProgress();

    void startSeeking(int currentDuration);

    void bindBottomView(MusicItem mMusicItem);

    void togglePlayAndPause();

    void startMusic();

    void pauseMusic();

    void setPauseIcon();

    void setStartIcon();

    void setDuration(int duration);
}
