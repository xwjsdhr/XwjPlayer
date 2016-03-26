package com.xwj.xwjplayer.presenters.impl;

import android.content.Context;
import android.util.Log;

import com.xwj.xwjplayer.entitys.VideoItem;
import com.xwj.xwjplayer.interator.QueryListener;
import com.xwj.xwjplayer.interator.QueryVideoInteractor;
import com.xwj.xwjplayer.interator.impl.QueryVideoInteractorImpl;
import com.xwj.xwjplayer.presenters.VideoListPresenter;
import com.xwj.xwjplayer.views.VideoListView;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/15.
 */
public class VideoListPresenterImpl implements VideoListPresenter {
    private static final String TAG = VideoListPresenterImpl.class.getSimpleName();
    private Context mContext;
    private VideoListView mVideoListView;
    private QueryVideoInteractor queryVideoInteractor;

    public VideoListPresenterImpl(Context context, VideoListView videoListView) {

        this.mContext = context;
        this.mVideoListView = videoListView;
        queryVideoInteractor = new QueryVideoInteractorImpl(context);
    }

    @Override
    public void onStart() {
        mVideoListView.showProgress();
        queryVideoInteractor.query(new QueryListener<VideoItem>() {
            @Override
            public void onSuccess(List<VideoItem> videoItemList) {
                Log.e(TAG, "onSuccess");
                mVideoListView.bindViews(videoItemList);
                mVideoListView.hideProgress();
            }

            @Override
            public void onFailed(String msg) {
                mVideoListView.hideProgress();
            }
        });
    }

    @Override
    public void onDestroy() {
        queryVideoInteractor.stop();
    }

}
