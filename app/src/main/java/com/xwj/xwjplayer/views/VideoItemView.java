package com.xwj.xwjplayer.views;

import android.os.Bundle;

import com.xwj.xwjplayer.entitys.VideoItem;

/**
 * Created by xiaweijia on 16/3/16.
 */
public interface VideoItemView {
    void startActivity(Class<?> clazz, Bundle bundle);

    void bindView(VideoItem item);
}
