package com.xwj.xwjplayer.interator;

import com.xwj.xwjplayer.entitys.VideoItem;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/15.
 */
public interface QueryListener<T> {
    void onSuccess(List<T> list);

    void onFailed(String msg);
}
