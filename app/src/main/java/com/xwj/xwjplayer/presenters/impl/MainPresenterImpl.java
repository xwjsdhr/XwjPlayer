package com.xwj.xwjplayer.presenters.impl;

import android.content.Context;
import android.view.View;

import com.xwj.xwjplayer.R;
import com.xwj.xwjplayer.presenters.MainPresenter;
import com.xwj.xwjplayer.views.MainView;
import com.xwj.xwjplayer.views.impl.MusicListActivity;
import com.xwj.xwjplayer.views.impl.VideoListActivity;

/**
 * Created by xiaweijia on 16/3/16.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView mMainView;
    private Context mContext;

    public MainPresenterImpl(Context context, MainView mainView) {
        this.mMainView = mainView;
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ib_video_list:
                mMainView.startActivity(VideoListActivity.class);
                break;
            case R.id.ib_music_list:
                mMainView.startActivity(MusicListActivity.class);
                break;
        }
    }

    @Override
    public void onDestroy() {
        
    }
}
