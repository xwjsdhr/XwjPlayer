package com.xwj.xwjplayer.presenters;

import com.xwj.xwjplayer.entitys.VideoItem;

/**
 * Created by xiaweijia on 16/3/15.
 */
public interface VideoListPresenter {
    void onStart();

    void onDestroy();

    void onItemLongClick(VideoItem videoItem);
}
