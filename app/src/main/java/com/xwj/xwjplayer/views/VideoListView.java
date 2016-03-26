package com.xwj.xwjplayer.views;

import com.xwj.xwjplayer.entitys.VideoItem;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/15.
 */
public interface VideoListView extends BaseView<List<VideoItem>> {

    void showProgress();

    void hideProgress();
}
